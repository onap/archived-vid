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
import io.joshworks.restclient.request.GetRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.jetty.util.security.Password;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.properties.VidProperties;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;

public class SyncRestClient implements SyncRestClientInterface {
    private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SyncRestClient.class);
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
            GetRequest getRequest = restClient.get(url2).headers(headers);
            routeParams.forEach(getRequest::routeParam);
            return getRequest.asJson();
        });
    }

    @Override
    public <T> HttpResponse<T> get(String url, Map<String, String> headers, Map<String, String> routeParams,
                                   Class<T> responseClass) {
        return callWithRetryOverHttp(url, url2 -> {
            GetRequest getRequest = restClient.get(url2).headers(headers);
            routeParams.forEach(getRequest::routeParam);
            return getRequest.asObject(responseClass);
        });
    }

    @Override
    public HttpResponse<InputStream> getStream(String url, Map<String, String> headers,
                                               Map<String, String> routeParams) {
        return callWithRetryOverHttp(url, url2 -> {
            GetRequest getRequest = restClient.get(url2).headers(headers);
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
    public <T> HttpResponse<T> delete(String url, Map<String, String> headers, Object body, Class<T> responseClass) {
        return callWithRetryOverHttp(url, url2 -> restClient.delete(url2).headers(headers).body(body).asObject(responseClass));
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

    private <T> HttpResponse<T> callWithRetryOverHttp(String url, HttpRequest<T> httpRequest) {
        try {
            return callWithRetryOverHttpThrows(url, httpRequest);
        } catch (IOException e) {
            throw new SyncRestClientException("IOException while calling rest service", e);
        }
    }

    private <T> HttpResponse<T> callWithRetryOverHttpThrows(String url, HttpRequest<T> httpRequest) throws IOException {
        try {
            return httpRequest.apply(url);
        } catch (RestClientException e) {
            if (!(e.getCause() instanceof SSLException)) {
                throw e;
            }
            logger.warn(EELFLoggerDelegate.debugLogger, "SSL Handshake problem occured. Will try to retry over Http.", e);
            return httpRequest.apply(url.replaceFirst(HTTPS_SCHEMA, HTTP_SCHEMA));
        }
    }

    private ObjectMapper defaultObjectMapper() {
        org.codehaus.jackson.map.ObjectMapper objectMapper = new org.codehaus.jackson.map.ObjectMapper();

        return new ObjectMapper() {
            @Override
            public <T> T readValue(String value, Class<T> aClass) {
                try {
                    return objectMapper.readValue(value, aClass);
                } catch (IOException e) {
                    throw new SyncRestClientException("IOException while reading value", e);
                }
            }

            @Override
            public String writeValue(Object value) {
                try {
                    return objectMapper.writeValueAsString(value);
                } catch (IOException e) {
                    throw new SyncRestClientException("IOException while writing value", e);
                }
            }
        };
    }

    private CloseableHttpClient defaultHttpClient() {
        try {
            String trustStorePath = SystemProperties.getProperty(VidProperties.VID_TRUSTSTORE_FILENAME);
            String trustStorePass = SystemProperties.getProperty(VidProperties.VID_TRUSTSTORE_PASSWD_X);
            String decryptedTrustStorePass = Password.deobfuscate(trustStorePass);

            KeyStore trustStore = loadTruststore(trustStorePath, decryptedTrustStorePass);
            SSLContext sslContext = trustOwnCACerts(decryptedTrustStorePass, trustStore);
            SSLConnectionSocketFactory sslSf = allowTLSProtocols(sslContext);

            return HttpClients.custom().setSSLSocketFactory(sslSf).build();
        } catch (IOException | CertificateException | UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            logger.warn(EELFLoggerDelegate.debugLogger, "Cannot initialize custom http client from current configuration. Using default one.", e);
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
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream instream = new FileInputStream(new File(trustStorePath))) {
            trustStore.load(instream, trustStorePass.toCharArray());
        }
        return trustStore;
    }

    @FunctionalInterface
    private interface HttpRequest<T> {
        HttpResponse<T> apply(String url) throws IOException;
    }

}
