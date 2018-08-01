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


import java.io.InputStream;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.onap.portalsdk.core.util.SystemProperties;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.conn.ssl.SSLContexts;
import org.onap.vid.properties.VidProperties;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.apache.http.client.HttpClient;
import io.vavr.CheckedFunction1;
import lombok.SneakyThrows;
import lombok.NonNull;
import lombok.val;

import java.security.UnrecoverableKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLHandshakeException;
import java.security.KeyStoreException;
import java.text.SimpleDateFormat;
import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.text.DateFormat;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.io.File;

public class SyncRestClient {

    private static final String CANNOT_INITIALIZE_CUSTOM_HTTP_CLIENT = "Cannot initialize custom http client from current configuration. Using default one.";
    private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(HttpsBasicClient.class);
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss:SSSS");
    private static final String[] SUPPORTED_PROTOCOLS = {"TLSv1", "TLSv1.2"};
    private static final String HTTPS_SCHEMA = "https://";
    private static final String HTTP_SCHEMA = "http://";

    public SyncRestClient() {
        initMappers();
        Unirest.setHttpClient(initHttpClient());
    }

    public SyncRestClient(HttpClient httpClient) {
        initMappers();
        Unirest.setHttpClient(httpClient);
    }

    public HttpResponse<JsonNode> post(@NonNull String url, @NonNull Map<String, String> headers,
        @NonNull Object body) {
        return callWithRetry(url, url2 -> Unirest.post(url2).headers(headers).body(body).asJson());
    }

    public <T> HttpResponse<T> post(@NonNull String url, @NonNull Map<String, String> headers, @NonNull Object body,
        @NonNull Class<T> aClass) {
        return callWithRetry(url, url2 -> Unirest.post(url2).headers(headers).body(body).asObject(aClass));
    }

    public HttpResponse<JsonNode> get(@NonNull String url, @NonNull Map<String, String> headers,
        @NonNull Map<String, String> routeParams) {
        return callWithRetry(url, url2 -> {
            val getRequest = Unirest.get(url2).headers(headers);
            routeParams.forEach(getRequest::routeParam);
            return getRequest.asJson();
        });
    }

    public <T> HttpResponse<T> get(@NonNull String url, @NonNull Map<String, String> headers,
        @NonNull Map<String, String> routeParams, @NonNull Class<T> aClass) {
        return callWithRetry(url, url2 -> {
            val getRequest = Unirest.get(url2).headers(headers);
            routeParams.forEach(getRequest::routeParam);
            return getRequest.asObject(aClass);
        });
    }

    public HttpResponse<InputStream> getStream(@NonNull String url, @NonNull Map<String, String> headers, @NonNull Map<String, String> routeParams) {
        return callWithRetry(url, url2 -> {
            val getRequest = Unirest.get(url2).headers(headers);
            routeParams.forEach(getRequest::routeParam);
            return getRequest.asBinary();
        });
    }

    public HttpResponse<JsonNode> put(@NonNull String url, @NonNull Map<String, String> headers, @NonNull Object body) {
        return callWithRetry(url, url2 -> Unirest.put(url2).headers(headers).body(body).asJson());
    }

    public <T> HttpResponse<T> put(@NonNull String url, @NonNull Map<String, String> headers, @NonNull Object body,
        @NonNull Class<T> aClass) {
        return callWithRetry(url, url2 -> Unirest.put(url2).headers(headers).body(body).asObject(aClass));
    }

    public <T> HttpResponse<T> delete(@NonNull String url, @NonNull Map<String, String> headers,  @NonNull Class<T> aClass) {
        return callWithRetry(url, url2 -> Unirest.delete(url2).headers(headers).asObject(aClass));
    }

    public HttpResponse<JsonNode> delete(@NonNull String url, @NonNull Map<String, String> headers) {
        return callWithRetry(url, url2 -> Unirest.delete(url2).headers(headers).asJson());
    }

    @SneakyThrows
    private <T> HttpResponse<T> callWithRetry(String url, CheckedFunction1<String, HttpResponse<T>> httpRequest) {
        try {
            return httpRequest.apply(url);
        } catch (UnirestException e) {
            if (e.getCause() instanceof SSLHandshakeException) {
                return httpRequest.apply(url.replaceFirst(HTTPS_SCHEMA, HTTP_SCHEMA));
            }
            throw e;
        }
    }

    private void initMappers() {
        val objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

        Unirest.setObjectMapper(new ObjectMapper() {
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
        });
    }

    private HttpClient initHttpClient() {
        try {
            val trustStorePath = SystemProperties.getProperty(VidProperties.VID_TRUSTSTORE_FILENAME);
            val trustStorePass = SystemProperties.getProperty(VidProperties.VID_TRUSTSTORE_PASSWD_X);

            val trustStore = loadTruststore(trustStorePath, trustStorePass);
            val sslcontext = trustOwnCACerts(trustStorePass, trustStore);
            val sslsf = allowTLSProtocols(sslcontext);

            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (IOException | CertificateException | UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            logger.warn(EELFLoggerDelegate.debugLogger,
                DATE_FORMAT.format(new Date()) + CANNOT_INITIALIZE_CUSTOM_HTTP_CLIENT, e);
            return HttpClients.createDefault();
        }
    }

    private SSLConnectionSocketFactory allowTLSProtocols(SSLContext sslcontext) {
        return new SSLConnectionSocketFactory(
            sslcontext,
            SUPPORTED_PROTOCOLS,
            null,
            SSLConnectionSocketFactory.STRICT_HOSTNAME_VERIFIER);
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
