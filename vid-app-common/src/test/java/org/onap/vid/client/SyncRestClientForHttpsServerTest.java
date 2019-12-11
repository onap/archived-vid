/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 - 2019 Nokia. All rights reserved.
 * Modifications Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
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

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.builder.verify.VerifyHttp.verifyHttp;
import static com.xebialabs.restito.semantics.Action.contentType;
import static com.xebialabs.restito.semantics.Action.ok;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static org.apache.http.client.config.RequestConfig.custom;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import com.att.eelf.configuration.EELFLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebialabs.restito.semantics.Action;
import com.xebialabs.restito.semantics.Condition;
import com.xebialabs.restito.server.StubServer;
import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.JsonNode;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Collections;
import java.util.Enumeration;
import javax.net.ssl.SSLContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.glassfish.grizzly.http.Method;
import org.jetbrains.annotations.NotNull;
import org.onap.vid.utils.Logging;
import org.springframework.http.HttpMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SyncRestClientForHttpsServerTest {

    private static final SyncRestClientModel.TestModel testObject = new SyncRestClientModel.TestModel(1, "test");
    private static final String[] SUPPORTED_PROTOCOLS = {"TLSv1", "TLSv1.2"};
    private StubServer stubServer;
    private ObjectMapper objectMapper = new ObjectMapper();

    private SyncRestClient syncRestClient;
    private Logging mockLoggingService;

    @BeforeMethod
    public void setUp() throws GeneralSecurityException{
        stubServer = new StubServer();
        stubServer.secured().run();
        mockLoggingService = mock(Logging.class);
        syncRestClient = new SyncRestClient(createNaiveHttpClient(), mockLoggingService);
    }

    @AfterMethod
    public void tearDown() {
        stubServer.stop();
    }

    @NotNull
    private String getTestUrl(String protocol) {
        try {
            return new URI(protocol, null,  "127.0.0.1" , stubServer.getPort(), "/test", null, null).toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testJsonResponseFromGet() throws JsonProcessingException {
        // given
        stubGetCall();
        String securedUrl = getTestUrl("https");
        String notSecuredUrl = getTestUrl("http");
        // when
        HttpResponse<JsonNode> jsonNodeHttpResponse = syncRestClient
            .get(securedUrl, Collections.emptyMap(), Collections.emptyMap());
        // then
        verifyHttp(stubServer)
            .once(Condition.method(Method.GET), Condition.url(securedUrl), Condition.not(Condition.url(notSecuredUrl)));
        verify(mockLoggingService).logRequest(any(EELFLogger.class), eq(HttpMethod.GET), eq(securedUrl), eq(Collections.emptyMap()));
        verify(mockLoggingService).logResponse(any(EELFLogger.class), eq(HttpMethod.GET), eq(securedUrl), eq(jsonNodeHttpResponse));
        assertEquals(jsonNodeHttpResponse.getStatus(), 200);
        assertEquals(jsonNodeHttpResponse.getBody().getObject().get("key"), 1);
        assertEquals(jsonNodeHttpResponse.getBody().getObject().get("value"), "test");
    }

    @Test
    public void testObjectResponseFromGet() throws JsonProcessingException {
        // given
        stubServer.run();
        stubGetCall();
        String securedUrl = getTestUrl("https");
        String notSecuredUrl = getTestUrl("http");
        // when
        HttpResponse<SyncRestClientModel.TestModel> testModelHttpResponse = syncRestClient
            .get(securedUrl, Collections.emptyMap(), Collections.emptyMap(), SyncRestClientModel.TestModel.class);
        // then
        verifyHttp(stubServer)
            .once(Condition.method(Method.GET), Condition.url(securedUrl), Condition.not(Condition.url(notSecuredUrl)));
        verify(mockLoggingService).logRequest(any(EELFLogger.class), eq(HttpMethod.GET), eq(securedUrl), eq(Collections.emptyMap()));
        verify(mockLoggingService).logResponse(any(EELFLogger.class), eq(HttpMethod.GET), eq(securedUrl), eq(testModelHttpResponse));
        assertEquals(testModelHttpResponse.getStatus(), 200);
        assertEquals(testModelHttpResponse.getBody().getKey(), 1);
        assertEquals(testModelHttpResponse.getBody().getValue(), "test");
    }

    private Action jsonContent() throws JsonProcessingException {
        return stringContent(objectMapper.writeValueAsString(testObject));
    }

    private void stubGetCall() throws JsonProcessingException {
        whenHttp(stubServer)
            .match(Condition.get("/test"))
            .then(ok(), jsonContent(), contentType("application/json"));
    }

    private CloseableHttpClient createNaiveHttpClient() throws GeneralSecurityException{
        KeyStore mock = getMockedKeyStore();

        final SSLContext context = SSLContextBuilder
                .create()
                .loadTrustMaterial(mock, new TrustSelfSignedStrategy())
                .build();

        final SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(context, SUPPORTED_PROTOCOLS,
            null, NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("https", socketFactory)
            .build();

        return HttpClientBuilder.create()
            .setDefaultRequestConfig(custom().setConnectionRequestTimeout(10000).build())
            .setConnectionManager(new PoolingHttpClientConnectionManager(registry))
            .setSSLSocketFactory(socketFactory).build();
    }

    private KeyStore getMockedKeyStore() throws KeyStoreException {
        KeyStore mock = mock(KeyStore.class);
        doReturn(new Enumeration<String>() {

            @Override
            public boolean hasMoreElements() {
                return false;
            }

            @Override
            public String nextElement() {
                return null;
            }
        }).when(mock).aliases();
        return mock;
    }
}
