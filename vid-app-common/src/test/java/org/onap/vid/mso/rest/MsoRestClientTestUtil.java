package org.onap.vid.mso.rest;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.builder.verify.VerifyHttp.verifyHttp;
import static com.xebialabs.restito.semantics.Action.contentType;
import static com.xebialabs.restito.semantics.Action.status;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static com.xebialabs.restito.semantics.Condition.delete;
import static com.xebialabs.restito.semantics.Condition.get;
import static com.xebialabs.restito.semantics.Condition.method;
import static com.xebialabs.restito.semantics.Condition.post;
import static com.xebialabs.restito.semantics.Condition.uri;
import static com.xebialabs.restito.semantics.Condition.withHeader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebialabs.restito.semantics.Action;
import com.xebialabs.restito.server.StubServer;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.json.JSONObject;
import org.junit.Assert;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.mso.MsoResponseWrapper;

class MsoRestClientTestUtil implements AutoCloseable {
  private final StubServer server;
  private final String endpoint;
  private final String responsePayload;
  private final HttpStatus expectedStatus;
  private final String expectedResponseStr;

  MsoRestClientTestUtil(StubServer server, String endpoint, HttpStatus expectedStatus,
      String responsePayload,
      String expectedResponseStr) {
    this.server = server;
    this.endpoint = endpoint;
    this.responsePayload = responsePayload;
    this.expectedStatus = expectedStatus;
    this.expectedResponseStr = expectedResponseStr;
  }

  void executePost(String jsonPayload, BiFunction<RequestDetails, String, MsoResponseWrapper> func) throws IOException {
    whenHttp(server)
        .match(post(endpoint))
        .then(status(expectedStatus), jsonContent(responsePayload), contentType(MediaType.APPLICATION_JSON));

    RequestDetails sampleRequestDetails =
        new ObjectMapper().readValue(jsonPayload, RequestDetails.class);

    MsoResponseWrapper response = func.apply(sampleRequestDetails, endpoint);
    JSONObject actualJson = new JSONObject(response.getEntity());

    Assert.assertEquals(expectedStatus.getStatusCode(), response.getStatus());
    Assert.assertEquals(expectedResponseStr, actualJson.toString());
    verifyServer(server, endpoint, Method.POST);

  }

  void executeDelete(String jsonPayload, BiFunction<RequestDetails, String, MsoResponseWrapper> func)
      throws IOException {
    whenHttp(server)
        .match(delete(endpoint))
        .then(status(expectedStatus), jsonContent(responsePayload), contentType(MediaType.APPLICATION_JSON));

    RequestDetails sampleRequestDetails =
        new ObjectMapper().readValue(jsonPayload, RequestDetails.class);
    MsoResponseWrapper response = func.apply(sampleRequestDetails, endpoint);

    Assert.assertEquals(expectedStatus.getStatusCode(), response.getStatus());
    verifyServer(server, endpoint, Method.DELETE);
  }

  void executeGet(Function<String, MsoResponseWrapper> func) {
    whenHttp(server)
        .match(get(endpoint))
        .then(status(expectedStatus), jsonContent(responsePayload), contentType(MediaType.APPLICATION_JSON));

    MsoResponseWrapper response = func.apply(endpoint);

    Assert.assertEquals(expectedStatus.getStatusCode(), response.getStatus());
    verifyServer(server, endpoint, Method.GET);
  }

  private void verifyServer(StubServer server, String endpoint, Method httpMethod) {
    verifyHttp(server).once(
        method(httpMethod),
        uri(endpoint),
        withHeader(HttpHeaders.AUTHORIZATION),
        withHeader(HttpHeaders.ACCEPT),
        withHeader(HttpHeaders.CONTENT_TYPE),
        withHeader(MsoRestClientNew.X_FROM_APP_ID),
        withHeader(SystemProperties.ECOMP_REQUEST_ID));
  }

  private Action jsonContent(String str) {
    return stringContent(str);
  }

  @Override
  public void close() {
  }
}

