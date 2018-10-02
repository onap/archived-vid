/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 Nokia. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.client;

import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.JsonNode;
import io.joshworks.restclient.http.RestClient;
import io.joshworks.restclient.http.exceptions.RestClientException;
import io.joshworks.restclient.http.mapper.ObjectMapper;
import org.apache.http.impl.client.CloseableHttpClient;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.eclipse.jetty.util.security.Password;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.conn.ssl.SSLContexts;
import io.vavr.CheckedFunction1;
import lombok.SneakyThrows;
import lombok.val;

import java.security.UnrecoverableKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLException;
import java.security.KeyStoreException;
import java.text.SimpleDateFormat;
import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.text.DateFormat;
import java.io.InputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.io.File;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.properties.VidProperties;

public class SyncRestClient implements SyncRestClientInterface {

    private static final String CANNOT_INITIALIZE_CUSTOM_HTTP_CLIENT = "Cannot initialize custom http client from current configuration. Using default one.";
    private static final String TRY_TO_CALL_OVER_HTTP = "SSL Handshake problem occured. Will try to retry over Http.";
    private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SyncRestClient.class);
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss:SSSS");
    private static final String[] SUPPORTED_SSL_VERSIONS = {"TLSv1", "TLSv1.2"};
    private static final String HTTPS_SCHEMA = "https://";
    private static final String HTTP_SCHEMA = "http://";

    private RestClient restClient;

    public SyncRestClient() {
        restClient = RestClient.newClient().objectMapper(defaultObjectMapper()).httpClient(defaultHttpClient()).build();
    }

    public SyncRestClient(ObjectMapper objectMapper) {
        restClient = RestClient.newClient().objectMapper(objectMapper).httpClient(defaultHttpClient()).build();
    }

    public SyncRestClient(CloseableHttpClient httpClient) {
        restClient = RestClient.newClient().objectMapper(defaultObjectMapper()).httpClient(httpClient).build();
    }

    public SyncRestClient(CloseableHttpClient httpClient, ObjectMapper objectMapper) {
        restClient = RestClient.newClient().objectMapper(objectMapper).httpClient(httpClient).build();
    }

    @Override
    public HttpResponse<JsonNode> post(String url, Map<String, String> headers, Object body) {
        return callWithRetryOverHttp(url, url2 -> restClient.post(url2).headers(headers).body(body).asJson());
    }

    @Override
    public <T> HttpResponse<T> post(String url, Map<String, String> headers, Object body, Class<T> responseClass) {
        return callWithRetryOverHttp(url,
            url2 -> restClient.post(url2).headers(headers).body(body).asObject(responseClass));
    }

    @Override
    public HttpResponse<JsonNode> get(String url, Map<String, String> headers, Map<String, String> routeParams) {
        return callWithRetryOverHttp(url, url2 -> {
            val getRequest = restClient.get(url2).headers(headers);
            routeParams.forEach(getRequest::routeParam);
            return getRequest.asJson();
        });
    }

    @Override
    public <T> HttpResponse<T> get(String url, Map<String, String> headers, Map<String, String> routeParams,
        Class<T> responseClass) {
        return callWithRetryOverHttp(url, url2 -> {
            val getRequest = restClient.get(url2).headers(headers);
            routeParams.forEach(getRequest::routeParam);
            return getRequest.asObject(responseClass);
        });
    }

    @Override
    public HttpResponse<InputStream> getStream(String url, Map<String, String> headers,
        Map<String, String> routeParams) {
        return callWithRetryOverHttp(url, url2 -> {
            val getRequest = restClient.get(url2).headers(headers);
            routeParams.forEach(getRequest::routeParam);
            return getRequest.asBinary();
        });
    }

    @Override
    public HttpResponse<JsonNode> put(String url, Map<String, String> headers, Object body) {
        return callWithRetryOverHttp(url, url2 -> restClient.put(url2).headers(headers).body(body).asJson());
    }

    @Override
    public <T> HttpResponse<T> put(String url, Map<String, String> headers, Object body, Class<T> responseClass) {
        return callWithRetryOverHttp(url,
            url2 -> restClient.put(url2).headers(headers).body(body).asObject(responseClass));
    }

    @Override
    public <T> HttpResponse<T> delete(String url, Map<String, String> headers, Class<T> responseClass) {
        return callWithRetryOverHttp(url, url2 -> restClient.delete(url2).headers(headers).asObject(responseClass));
    }

    @Override
    public HttpResponse<JsonNode> delete(String url, Map<String, String> headers) {
        return callWithRetryOverHttp(url, url2 -> restClient.delete(url2).headers(headers).asJson());
    }

    @Override
    public void destroy() {
        restClient.shutdown();
    }

    @SneakyThrows
    private <T> HttpResponse<T> callWithRetryOverHttp(String url,
                                                      CheckedFunction1<String, HttpResponse<T>> httpRequest) {
        try {
            return httpRequest.apply(url);
        } catch (RestClientException e) {
            if (e.getCause() instanceof SSLException) {
                logger.warn(EELFLoggerDelegate.debugLogger,
                        DATE_FORMAT.format(new Date()) + TRY_TO_CALL_OVER_HTTP, e);
                return httpRequest.apply(url.replaceFirst(HTTPS_SCHEMA, HTTP_SCHEMA));
            }
            throw e;
        }
    }

    private ObjectMapper defaultObjectMapper() {
        val objectMapper = new org.codehaus.jackson.map.ObjectMapper();

        return new ObjectMapper() {
            @Override
            @SneakyThrows
            public <T> T readValue(String value, Class<T> aClass) {
                return objectMapper.readValue(value, aClass);
            }

            @Override
            @SneakyThrows
            public String writeValue(Object value) {
                return objectMapper.writeValueAsString(value);
            }
        };
    }

    private CloseableHttpClient defaultHttpClient() {
        try {
            val trustStorePath = SystemProperties.getProperty(VidProperties.VID_TRUSTSTORE_FILENAME);
            val trustStorePass = SystemProperties.getProperty(VidProperties.VID_TRUSTSTORE_PASSWD_X);
            val decryptedTrustStorePass = Password.deobfuscate(trustStorePass);

            val trustStore = loadTruststore(trustStorePath, decryptedTrustStorePass);
            val sslContext = trustOwnCACerts(decryptedTrustStorePass, trustStore);
            val sslSf = allowTLSProtocols(sslContext);

            return HttpClients.custom().setSSLSocketFactory(sslSf).build();
        } catch (IOException | CertificateException | UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            logger.warn(EELFLoggerDelegate.debugLogger,
                DATE_FORMAT.format(new Date()) + CANNOT_INITIALIZE_CUSTOM_HTTP_CLIENT, e);
            return HttpClients.createDefault();
        }
    }

    private SSLConnectionSocketFactory allowTLSProtocols(SSLContext sslcontext) {
        return new SSLConnectionSocketFactory(
            sslcontext,
            SUPPORTED_SSL_VERSIONS,
            null,
            SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    }

    private SSLContext trustOwnCACerts(String trustStorePass, KeyStore trustStore)
        throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        return SSLContexts.custom()
            .useTLS()
            .loadKeyMaterial(trustStore, trustStorePass.toCharArray())
            .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
            .build();
    }

    private KeyStore loadTruststore(String trustStorePath, String trustStorePass)
        throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        val trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream instream = new FileInputStream(new File(trustStorePath))) {
            trustStore.load(instream, trustStorePass.toCharArray());
        }
        return trustStore;
    }

}
