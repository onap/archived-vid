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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.xebialabs.restito.semantics.Condition;
import com.xebialabs.restito.server.StubServer;
import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.JsonNode;
import org.glassfish.grizzly.http.util.HttpStatus;
import com.xebialabs.restito.semantics.Action;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.glassfish.grizzly.http.Method;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Map;

import static com.xebialabs.restito.builder.verify.VerifyHttp.verifyHttp;
import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static com.xebialabs.restito.semantics.Action.contentType;
import static com.xebialabs.restito.semantics.Action.status;
import static com.xebialabs.restito.semantics.Action.ok;
import static org.testng.Assert.assertEquals;

public class SyncRestClientForHttpServerTest {

    private static final SyncRestClientModel.TestModel testObject = new SyncRestClientModel.TestModel(1, "test");
    private static final String NOT_EXISTING_OBJECT = "NOT EXISTING OBJECT";

    private StubServer stubServer;
    private ObjectMapper objectMapper = new ObjectMapper();
    private SyncRestClient syncRestClient;

    @BeforeMethod
    public void setUp() {
        stubServer = new StubServer();
        stubServer.run();
        syncRestClient = new SyncRestClient();
    }

    @AfterMethod
    public void tearDown() {
        stubServer.stop();
        syncRestClient.destroy();
    }

    @Test
    public void testJsonResponseFromGet() throws JsonProcessingException {
        // given
        stubGetCall();
        String url = "http://0.0.0.0:" + stubServer.getPort() + "/test";
        // when
        HttpResponse<JsonNode> jsonNodeHttpResponse = syncRestClient
            .get(url, Collections.emptyMap(), Collections.emptyMap());
        // then
        verifyHttp(stubServer).once(Condition.method(Method.GET), Condition.url(url));
        assertEquals(jsonNodeHttpResponse.getStatus(), 200);
        assertEquals(jsonNodeHttpResponse.getBody().getObject().get("key"), 1);
        assertEquals(jsonNodeHttpResponse.getBody().getObject().get("value"), "test");
    }

    @Test
    public void testObjectResponseFromGet() throws JsonProcessingException {
        // given
        stubGetCall();
        String url = "http://0.0.0.0:" + stubServer.getPort() + "/test";
        // when
        HttpResponse<SyncRestClientModel.TestModel> testModelHttpResponse = syncRestClient
            .get(url, Collections.emptyMap(), Collections.emptyMap(), SyncRestClientModel.TestModel.class);
        // then
        verifyHttp(stubServer).once(Condition.method(Method.GET), Condition.url(url));
        assertEquals(testModelHttpResponse.getStatus(), 200);
        assertEquals(testModelHttpResponse.getBody().getKey(), 1);
        assertEquals(testModelHttpResponse.getBody().getValue(), "test");
    }

    @Test
    public void testJsonResponseFromPost() throws JsonProcessingException {
        // given
        stubPostCall();
        String url = "http://0.0.0.0:" + stubServer.getPort() + "/test";
        // when
        HttpResponse<JsonNode> jsonNodeHttpResponse = syncRestClient.post(url, Collections.emptyMap(), testObject);
        // then
        verifyHttp(stubServer).once(Condition.method(Method.POST), Condition.url(url));
        assertEquals(jsonNodeHttpResponse.getStatus(), 200);
        assertEquals(jsonNodeHttpResponse.getBody().getObject().get("key"), 1);
        assertEquals(jsonNodeHttpResponse.getBody().getObject().get("value"), "test");
    }

    @Test
    public void test404JsonResponseFromPost() throws JsonProcessingException {
        // given
        stubPostCall();
        String url = "http://0.0.0.0:" + stubServer.getPort() + "/test";
        // when
        HttpResponse<JsonNode> jsonNodeHttpResponse = syncRestClient
            .post(url, Collections.emptyMap(), NOT_EXISTING_OBJECT);
        // then
        assertEquals(jsonNodeHttpResponse.getStatus(), 404);
        assertEquals(jsonNodeHttpResponse.getStatusText(), "Not Found");
    }

    @Test
    public void testHeadersWerePassedToPost() throws JsonProcessingException {
        // given
        stubPostCall();
        Map headers = ImmutableMap.<String, String>builder().put("Authorization", "Basic anyHash").build();
        String url = "http://0.0.0.0:" + stubServer.getPort() + "/test";
        // when
        HttpResponse<JsonNode> jsonNodeHttpResponse = syncRestClient.post(url, headers, testObject);
        // then
        verifyHttp(stubServer).once(Condition.withHeader("Authorization"));
        assertEquals(jsonNodeHttpResponse.getStatus(), 200);
    }

    @Test(expectedExceptions = {Exception.class})
    public void testFailedJsonResponseFromPost() throws JsonProcessingException {
        // given
        stubPostCall();
        String url = "http://0.0.0.0:" + stubServer.getPort() + "/test";
        // when
        stubServer.stop();
        syncRestClient.post(url, Collections.emptyMap(), testObject);
    }

    @Test
    public void testObjectResponseFromPost() throws JsonProcessingException {
        // given
        stubPostCall();
        String url = "http://0.0.0.0:" + stubServer.getPort() + "/test";
        // when
        HttpResponse<SyncRestClientModel.TestModel> objectHttpResponse = syncRestClient
            .post(url, Collections.emptyMap(), testObject, SyncRestClientModel.TestModel.class);
        // then
        verifyHttp(stubServer).once(Condition.method(Method.POST), Condition.url(url));
        assertEquals(objectHttpResponse.getStatus(), 200);
        assertEquals(objectHttpResponse.getBody().getKey(), 1);
        assertEquals(objectHttpResponse.getBody().getValue(), "test");
    }

    @Test
    public void testJsonResponseFromPut() throws JsonProcessingException {
        // given
        stubPutCall();
        String url = "http://0.0.0.0:" + stubServer.getPort() + "/test";
        // when
        HttpResponse<JsonNode> jsonNodeHttpResponse = syncRestClient.put(url, Collections.emptyMap(), testObject);
        // then
        verifyHttp(stubServer).once(Condition.method(Method.PUT), Condition.url(url));
        assertEquals(jsonNodeHttpResponse.getStatus(), 201);
        assertEquals(jsonNodeHttpResponse.getBody().getObject().get("key"), 1);
        assertEquals(jsonNodeHttpResponse.getBody().getObject().get("value"), "test");
    }

    @Test
    public void testObjectResponseFromPut() throws JsonProcessingException {
        // given
        stubPutCall();
        String url = "http://0.0.0.0:" + stubServer.getPort() + "/test";
        // when
        HttpResponse<SyncRestClientModel.TestModel> modelHttpResponse = syncRestClient
            .put(url, Collections.emptyMap(), testObject, SyncRestClientModel.TestModel.class);
        // then
        verifyHttp(stubServer).once(Condition.method(Method.PUT), Condition.url(url));
        assertEquals(modelHttpResponse.getStatus(), 201);
        assertEquals(modelHttpResponse.getBody().getKey(), 1);
        assertEquals(modelHttpResponse.getBody().getValue(), "test");
    }

    @Test
    public void testJsonResponseFromDelete() throws JsonProcessingException {
        // given
        stubDeleteCall();
        String url = "http://0.0.0.0:" + stubServer.getPort() + "/test";
        // when
        HttpResponse<JsonNode> jsonNodeHttpResponse = syncRestClient.delete(url, Collections.emptyMap());
        // then
        verifyHttp(stubServer).once(Condition.method(Method.DELETE), Condition.url(url));
        assertEquals(jsonNodeHttpResponse.getStatus(), 200);
        assertEquals(jsonNodeHttpResponse.getBody().getObject().get("key"), 1);
        assertEquals(jsonNodeHttpResponse.getBody().getObject().get("value"), "test");
    }

    @Test
    public void testObjectResponseFromDelete() throws JsonProcessingException {
        // given
        stubDeleteCall();
        String url = "http://0.0.0.0:" + stubServer.getPort() + "/test";
        // when
        HttpResponse<SyncRestClientModel.TestModel> modelHttpResponse = syncRestClient
            .delete(url, Collections.emptyMap(),  SyncRestClientModel.TestModel.class);
        // then
        verifyHttp(stubServer).once(Condition.method(Method.DELETE), Condition.url(url));
        assertEquals(modelHttpResponse.getStatus(), 200);
        assertEquals(modelHttpResponse.getBody().getKey(), 1);
        assertEquals(modelHttpResponse.getBody().getValue(), "test");
    }

    @Test
    public void testRedirectToHttp() throws JsonProcessingException {
        // given
        stubGetCall();
        String secured_url = "https://0.0.0.0:" + stubServer.getPort() + "/test";
        String available_url = "http://0.0.0.0:" + stubServer.getPort() + "/test";
        // when
        HttpResponse<JsonNode> jsonNodeHttpResponse = syncRestClient
            .get(secured_url, Collections.emptyMap(), Collections.emptyMap());
        // then
        verifyHttp(stubServer).once(Condition.method(Method.GET), Condition.url(available_url),
            Condition.not(Condition.url(secured_url)));
        assertEquals(jsonNodeHttpResponse.getStatus(), 200);
    }

    private void stubGetCall() throws JsonProcessingException {
        whenHttp(stubServer)
            .match(Condition.get("/test"))
            .then(ok(), jsonContent(), contentType("application/json"));
    }

    private void stubDeleteCall() throws JsonProcessingException {
        whenHttp(stubServer)
            .match(Condition.delete("/test"))
            .then(ok(), jsonContent(), contentType("application/json"));
    }

    private void stubPostCall() throws JsonProcessingException {
        whenHttp(stubServer)
            .match(Condition.post("/test"),
                Condition.withPostBodyContaining(objectMapper.writeValueAsString(testObject)))
            .then(ok(), jsonContent(), contentType("application/json"));
    }

    private void stubPutCall() throws JsonProcessingException {
        whenHttp(stubServer)
            .match(Condition.put("/test"),
                Condition.withPostBodyContaining(objectMapper.writeValueAsString(testObject)))
            .then(status(HttpStatus.CREATED_201), jsonContent(), contentType("application/json"));
    }

    private Action jsonContent() throws JsonProcessingException {
        return stringContent(objectMapper.writeValueAsString(testObject));
    }

}