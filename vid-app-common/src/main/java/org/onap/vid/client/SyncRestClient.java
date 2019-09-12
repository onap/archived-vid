/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 - 2019 Nokia. All rights reserved.
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

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.onap.vid.client.UnirestPatchKt.patched;

import com.att.eelf.configuration.EELFLogger;
import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.JsonNode;
import io.joshworks.restclient.http.RestClient;
import io.joshworks.restclient.http.exceptions.RestClientException;
import io.joshworks.restclient.http.mapper.ObjectMapper;
import io.joshworks.restclient.request.GetRequest;
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
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.jetty.util.security.Password;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.properties.VidProperties;
import org.onap.vid.utils.Logging;

public class SyncRestClient implements SyncRestClientInterface {
    private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SyncRestClient.class);
    private static final String[] SUPPORTED_SSL_VERSIONS = {"TLSv1", "TLSv1.2"};
    private static final String HTTPS_SCHEMA = "https://";
    private static final String HTTP_SCHEMA = "http://";
    private final Logging loggingService;
    private final EELFLogger outgoingRequestsLogger;

    private RestClient restClient;

    public SyncRestClient(Logging loggingService) {
        this(null, null, loggingService);
    }

    public SyncRestClient(ObjectMapper objectMapper, Logging loggingService) {
        this(null, objectMapper,  loggingService);
    }

    public SyncRestClient(CloseableHttpClient httpClient, Logging loggingService) {
        this(httpClient, null,  loggingService);
    }

    public SyncRestClient(CloseableHttpClient httpClient, ObjectMapper objectMapper, Logging loggingService) {
        restClient = RestClient
            .newClient()
            .objectMapper(ObjectUtils.defaultIfNull(objectMapper, defaultObjectMapper()))
            .httpClient(ObjectUtils.defaultIfNull(httpClient , defaultHttpClient()))
            .build();
        this.loggingService = loggingService;
        this.outgoingRequestsLogger = Logging.getRequestsLogger("syncRestClient");
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
            return patched(httpRequest.apply(url));
        } catch (RestClientException e) {
            if (causedBySslHandshakeError(e)) {
                logger.warn(EELFLoggerDelegate.debugLogger, "SSL Handshake problem occured. Will try to retry over Http.", e);
                return patched(httpRequest.apply(url.replaceFirst(HTTPS_SCHEMA, HTTP_SCHEMA)));
            }
            throw e;
        }
    }

    private boolean causedBySslHandshakeError(RestClientException exception) {
        return exception.getCause() instanceof SSLException;
    }

    private ObjectMapper defaultObjectMapper() {
        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

        return new ObjectMapper() {
            @Override
            public <T> T readValue(String value, Class<T> aClass) {
                try {
                    return isEmpty(value) ? null : objectMapper.readValue(value, aClass);
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
