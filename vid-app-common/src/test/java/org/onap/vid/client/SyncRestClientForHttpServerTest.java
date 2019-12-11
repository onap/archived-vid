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
import static com.xebialabs.restito.semantics.Action.status;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import com.att.eelf.configuration.EELFLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.xebialabs.restito.semantics.Action;
import com.xebialabs.restito.semantics.Condition;
import com.xebialabs.restito.server.StubServer;
import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.JsonNode;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;
import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.onap.vid.utils.Logging;
import org.springframework.http.HttpMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SyncRestClientForHttpServerTest {

    private static final SyncRestClientModel.TestModel testObject = new SyncRestClientModel.TestModel(1, "test");
    private static final String NOT_EXISTING_OBJECT = "NOT EXISTING OBJECT";

    private StubServer stubServer;
    private ObjectMapper objectMapper = new ObjectMapper();
    private SyncRestClient syncRestClient;
    private Logging mockLoggingService;

    @BeforeMethod
    public void setUp() {
        stubServer = new StubServer();
        stubServer.run();
        mockLoggingService = mock(Logging.class);
        syncRestClient = new SyncRestClient(mockLoggingService);
    }

    @AfterMethod
    public void tearDown() {
        stubServer.stop();
        syncRestClient.destroy();
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
        String url = getTestUrl("http");
        // when
        HttpResponse<JsonNode> jsonNodeHttpResponse = syncRestClient
            .get(url, Collections.emptyMap(), Collections.emptyMap());
        // then
        verifyHttp(stubServer).once(Condition.method(Method.GET), Condition.url(url));
        verify(mockLoggingService).logRequest(any(EELFLogger.class), eq(HttpMethod.GET), eq(url), eq(Collections.emptyMap()));
        verify(mockLoggingService).logResponse(any(EELFLogger.class), eq(HttpMethod.GET), eq(url), eq(jsonNodeHttpResponse));
        assertEquals(jsonNodeHttpResponse.getStatus(), 200);
        assertEquals(jsonNodeHttpResponse.getBody().getObject().get("key"), 1);
        assertEquals(jsonNodeHttpResponse.getBody().getObject().get("value"), "test");
    }

    @Test
    public void testObjectResponseFromGet() throws JsonProcessingException {
        // given
        stubGetCall();
        String url = getTestUrl("http");
        // when
        HttpResponse<SyncRestClientModel.TestModel> testModelHttpResponse = syncRestClient
            .get(url, Collections.emptyMap(), Collections.emptyMap(), SyncRestClientModel.TestModel.class);
        // then
        verifyHttp(stubServer).once(Condition.method(Method.GET), Condition.url(url));
        verify(mockLoggingService).logRequest(any(EELFLogger.class), eq(HttpMethod.GET), eq(url), eq(Collections.emptyMap()));
        verify(mockLoggingService).logResponse(any(EELFLogger.class), eq(HttpMethod.GET), eq(url), eq(testModelHttpResponse));
        assertEquals(testModelHttpResponse.getStatus(), 200);
        assertEquals(testModelHttpResponse.getBody().getKey(), 1);
        assertEquals(testModelHttpResponse.getBody().getValue(), "test");
    }

    @Test
    public void testJsonResponseFromPost() throws JsonProcessingException {
        // given
        stubPostCall();
        String url = getTestUrl("http");
        // when
        HttpResponse<JsonNode> jsonNodeHttpResponse = syncRestClient.post(url, Collections.emptyMap(), testObject);
        // then
        verify(mockLoggingService).logRequest(any(EELFLogger.class), eq(HttpMethod.POST), eq(url), eq(testObject));
        verify(mockLoggingService).logResponse(any(EELFLogger.class), eq(HttpMethod.POST), eq(url), eq(jsonNodeHttpResponse));
        verifyHttp(stubServer).once(Condition.method(Method.POST), Condition.url(url));
        assertEquals(jsonNodeHttpResponse.getStatus(), 200);
        assertEquals(jsonNodeHttpResponse.getBody().getObject().get("key"), 1);
        assertEquals(jsonNodeHttpResponse.getBody().getObject().get("value"), "test");
    }

    @Test
    public void test404JsonResponseFromPost() throws JsonProcessingException {
        // given
        stubPostCall();
        String url = getTestUrl("http");
        // when
        HttpResponse<JsonNode> jsonNodeHttpResponse = syncRestClient
            .post(url, Collections.emptyMap(), NOT_EXISTING_OBJECT);
        // then
        verify(mockLoggingService).logRequest(any(EELFLogger.class), eq(HttpMethod.POST), eq(url), eq(NOT_EXISTING_OBJECT));
        verify(mockLoggingService).logResponse(any(EELFLogger.class), eq(HttpMethod.POST), eq(url), eq(jsonNodeHttpResponse));
        assertEquals(jsonNodeHttpResponse.getStatus(), 404);
        assertEquals(jsonNodeHttpResponse.getStatusText(), "Not Found");
    }

    @Test
    public void testHeadersWerePassedToPost() throws JsonProcessingException {
        // given
        stubPostCall();
        Map headers = ImmutableMap.<String, String>builder().put("Authorization", "Basic anyHash").build();
        String url = getTestUrl("http");
        // when
        HttpResponse<JsonNode> jsonNodeHttpResponse = syncRestClient.post(url, headers, testObject);
        // then
        verifyHttp(stubServer).once(Condition.withHeader("Authorization"));
        verify(mockLoggingService).logRequest(any(EELFLogger.class), eq(HttpMethod.POST), eq(url), eq(testObject));
        verify(mockLoggingService).logResponse(any(EELFLogger.class), eq(HttpMethod.POST), eq(url), eq(jsonNodeHttpResponse));
        assertEquals(jsonNodeHttpResponse.getStatus(), 200);
    }

    @Test(expectedExceptions = {Exception.class})
    public void testFailedJsonResponseFromPost() throws JsonProcessingException {
        // given
        stubPostCall();
        String url = getTestUrl("http");
        // when
        stubServer.stop();
        syncRestClient.post(url, Collections.emptyMap(), testObject);
    }

    @Test
    public void testObjectResponseFromPost() throws JsonProcessingException {
        // given
        stubPostCall();
        String url = getTestUrl("http");
        // when
        HttpResponse<SyncRestClientModel.TestModel> objectHttpResponse = syncRestClient
            .post(url, Collections.emptyMap(), testObject, SyncRestClientModel.TestModel.class);
        // then
        verifyHttp(stubServer).once(Condition.method(Method.POST), Condition.url(url));
        verify(mockLoggingService).logRequest(any(EELFLogger.class), eq(HttpMethod.POST), eq(url), eq(testObject));
        verify(mockLoggingService).logResponse(any(EELFLogger.class), eq(HttpMethod.POST), eq(url), eq(objectHttpResponse));
        assertEquals(objectHttpResponse.getStatus(), 200);
        assertEquals(objectHttpResponse.getBody().getKey(), 1);
        assertEquals(objectHttpResponse.getBody().getValue(), "test");
    }

    @Test
    public void testJsonResponseFromPut() throws JsonProcessingException {
        // given
        stubPutCall();
        String url = getTestUrl("http");
        // when
        HttpResponse<JsonNode> jsonNodeHttpResponse = syncRestClient.put(url, Collections.emptyMap(), testObject);
        // then
        verifyHttp(stubServer).once(Condition.method(Method.PUT), Condition.url(url));
        verify(mockLoggingService).logRequest(any(EELFLogger.class), eq(HttpMethod.PUT), eq(url),  eq(testObject));
        verify(mockLoggingService).logResponse(any(EELFLogger.class), eq(HttpMethod.PUT), eq(url), eq(jsonNodeHttpResponse));
        assertEquals(jsonNodeHttpResponse.getStatus(), 201);
        assertEquals(jsonNodeHttpResponse.getBody().getObject().get("key"), 1);
        assertEquals(jsonNodeHttpResponse.getBody().getObject().get("value"), "test");
    }

    @Test
    public void testObjectResponseFromPut() throws JsonProcessingException {
        // given
        stubPutCall();
        String url = getTestUrl("http");
        // when
        HttpResponse<SyncRestClientModel.TestModel> modelHttpResponse = syncRestClient
            .put(url, Collections.emptyMap(), testObject, SyncRestClientModel.TestModel.class);
        // then
        verifyHttp(stubServer).once(Condition.method(Method.PUT), Condition.url(url));
        verify(mockLoggingService).logRequest(any(EELFLogger.class), eq(HttpMethod.PUT), eq(url),  eq(testObject));
        verify(mockLoggingService).logResponse(any(EELFLogger.class), eq(HttpMethod.PUT), eq(url), eq(modelHttpResponse));
        assertEquals(modelHttpResponse.getStatus(), 201);
        assertEquals(modelHttpResponse.getBody().getKey(), 1);
        assertEquals(modelHttpResponse.getBody().getValue(), "test");
    }

    @Test
    public void testJsonResponseFromDelete() throws JsonProcessingException {
        // given
        stubDeleteCall();
        String url = getTestUrl("http");
        // when
        HttpResponse<JsonNode> jsonNodeHttpResponse = syncRestClient.delete(url, Collections.emptyMap());
        // then
        verifyHttp(stubServer).once(Condition.method(Method.DELETE), Condition.url(url));
        verify(mockLoggingService).logRequest(any(EELFLogger.class), eq(HttpMethod.DELETE), eq(url));
        verify(mockLoggingService).logResponse(any(EELFLogger.class), eq(HttpMethod.DELETE), eq(url), eq(jsonNodeHttpResponse));
        assertEquals(jsonNodeHttpResponse.getStatus(), 200);
        assertEquals(jsonNodeHttpResponse.getBody().getObject().get("key"), 1);
        assertEquals(jsonNodeHttpResponse.getBody().getObject().get("value"), "test");
    }

    @Test
    public void testObjectResponseFromDelete() throws JsonProcessingException {
        // given
        stubDeleteCall();
        String url = getTestUrl("http");
        // when
        HttpResponse<SyncRestClientModel.TestModel> modelHttpResponse = syncRestClient
            .delete(url, Collections.emptyMap(),  SyncRestClientModel.TestModel.class);
        // then
        verifyHttp(stubServer).once(Condition.method(Method.DELETE), Condition.url(url));
        verify(mockLoggingService).logRequest(any(EELFLogger.class), eq(HttpMethod.DELETE), eq(url));
        verify(mockLoggingService).logResponse(any(EELFLogger.class), eq(HttpMethod.DELETE), eq(url), eq(modelHttpResponse));
        assertEquals(modelHttpResponse.getStatus(), 200);
        assertEquals(modelHttpResponse.getBody().getKey(), 1);
        assertEquals(modelHttpResponse.getBody().getValue(), "test");
    }

    @Test
    public void testRedirectToHttp() throws JsonProcessingException {
        // given
        stubGetCall();
        String secured_url = getTestUrl("https");;
        String available_url = getTestUrl("http");
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
