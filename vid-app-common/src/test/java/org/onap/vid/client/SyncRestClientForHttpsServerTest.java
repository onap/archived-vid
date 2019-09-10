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

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.builder.verify.VerifyHttp.verifyHttp;
import static com.xebialabs.restito.semantics.Action.contentType;
import static com.xebialabs.restito.semantics.Action.ok;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static org.apache.http.client.config.RequestConfig.custom;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

import com.att.eelf.configuration.EELFLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebialabs.restito.semantics.Action;
import com.xebialabs.restito.semantics.Condition;
import com.xebialabs.restito.server.StubServer;
import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.JsonNode;
import java.security.GeneralSecurityException;
import java.util.Collections;
import javax.net.ssl.SSLContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.glassfish.grizzly.http.Method;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SyncRestClientForHttpsServerTest {

    private static final SyncRestClientModel.TestModel testObject = new SyncRestClientModel.TestModel(1, "test");
    private static final String[] SUPPORTED_PROTOCOLS = {"TLSv1", "TLSv1.2"};
    private StubServer stubServer;
    private ObjectMapper objectMapper = new ObjectMapper();

    private SyncRestClient syncRestClient;
    private EELFLogger mockLogger;

    @BeforeMethod
    public void setUp() throws GeneralSecurityException {
        stubServer = new StubServer();
        stubServer.secured().run();
        mockLogger = mock(EELFLogger.class);
        syncRestClient = new SyncRestClient(createNaiveHttpClient(), mockLogger);
    }

    @AfterMethod
    public void tearDown() {
        stubServer.stop();
    }

    @Test
    public void testJsonResponseFromGet() throws JsonProcessingException {
        // given
        stubGetCall();
        String securedUrl = "https://0.0.0.0:" + stubServer.getPort() + "/test";
        String notSecuredUrl = "http://0.0.0.0:" + stubServer.getPort() + "/test";
        // when
        HttpResponse<JsonNode> jsonNodeHttpResponse = syncRestClient
            .get(securedUrl, Collections.emptyMap(), Collections.emptyMap());
        // then
        verifyHttp(stubServer)
            .once(Condition.method(Method.GET), Condition.url(securedUrl), Condition.not(Condition.url(notSecuredUrl)));
        assertEquals(jsonNodeHttpResponse.getStatus(), 200);
        assertEquals(jsonNodeHttpResponse.getBody().getObject().get("key"), 1);
        assertEquals(jsonNodeHttpResponse.getBody().getObject().get("value"), "test");
    }

    @Test
    public void testObjectResponseFromGet() throws JsonProcessingException {
        // given
        stubServer.run();
        stubGetCall();
        String securedUrl = "https://0.0.0.0:" + stubServer.getPort() + "/test";
        String notSecuredUrl = "http://0.0.0.0:" + stubServer.getPort() + "/test";
        // when
        HttpResponse<SyncRestClientModel.TestModel> testModelHttpResponse = syncRestClient
            .get(securedUrl, Collections.emptyMap(), Collections.emptyMap(), SyncRestClientModel.TestModel.class);
        // then
        verifyHttp(stubServer)
            .once(Condition.method(Method.GET), Condition.url(securedUrl), Condition.not(Condition.url(notSecuredUrl)));
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

    private CloseableHttpClient createNaiveHttpClient() throws GeneralSecurityException {
        final SSLContext context = new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy())
            .build();

        final SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(context, SUPPORTED_PROTOCOLS,
            null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("https", socketFactory)
            .build();

        return HttpClientBuilder.create()
            .setDefaultRequestConfig(custom().setConnectionRequestTimeout(10000).build())
            .setConnectionManager(new PoolingHttpClientConnectionManager(registry))
            .setSSLSocketFactory(socketFactory).build();
    }

}
