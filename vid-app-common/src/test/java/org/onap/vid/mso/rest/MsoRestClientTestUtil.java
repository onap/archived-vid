/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
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
import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebialabs.restito.semantics.Action;
import com.xebialabs.restito.server.StubServer;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.json.JSONObject;
import org.junit.Assert;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.changeManagement.RelatedInstanceList;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.mso.MsoResponseWrapper;
import org.onap.vid.mso.model.CloudConfiguration;
import org.onap.vid.mso.model.ModelInfo;
import org.onap.vid.mso.model.RequestInfo;
import org.onap.vid.mso.model.RequestParameters;

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
  void executePostCall(String jsonPayload, BiFunction<RequestDetailsWrapper, String, MsoResponseWrapper> func) throws IOException {
    whenHttp(server)
            .match(post(endpoint))
            .then(status(expectedStatus), jsonContent(responsePayload), contentType(MediaType.APPLICATION_JSON));

    RequestDetailsWrapper  sampleRequestDetails =
            new ObjectMapper().readValue(jsonPayload, RequestDetailsWrapper.class);

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
    assertJsonEquals(expectedResponseStr, response.getEntity());
    verifyServer(server, endpoint, Method.DELETE);
  }

  void executeGet(Function<String, MsoResponseWrapper> func) {
    whenHttp(server)
        .match(get(endpoint))
        .then(status(expectedStatus), jsonContent(responsePayload), contentType(MediaType.APPLICATION_JSON));

    MsoResponseWrapper response = func.apply(endpoint);

    Assert.assertEquals(expectedStatus.getStatusCode(), response.getStatus());
    assertJsonEquals(expectedResponseStr, response.getEntity());
    verifyServer(server, endpoint, Method.GET);
  }

  static org.onap.vid.changeManagement.RequestDetails generateMockMsoRequest() {
    org.onap.vid.changeManagement.RequestDetails requestDetails = new org.onap.vid.changeManagement.RequestDetails();
    requestDetails.setVnfInstanceId("vnf-instance-id");
    requestDetails.setVnfName("vnf-name");
    CloudConfiguration cloudConfiguration = new CloudConfiguration();
    cloudConfiguration.setTenantId("tenant-id");
    cloudConfiguration.setLcpCloudRegionId("lcp-region");
    requestDetails.setCloudConfiguration(cloudConfiguration);
    ModelInfo modelInfo = new ModelInfo();
    modelInfo.setModelInvariantId("model-invarient-id");
    modelInfo.setModelCustomizationName("modelCustomizationName");
    requestDetails.setModelInfo(modelInfo);
    RequestInfo requestInfo = new RequestInfo();
    requestInfo.setRequestorId("ok883e");
    requestInfo.setSource("VID");
    requestDetails.setRequestInfo(requestInfo);
    RequestParameters requestParameters = new RequestParameters();
    requestParameters.setSubscriptionServiceType("subscriber-service-type");
    requestParameters.setAdditionalProperty("a", 1);
    requestParameters.setAdditionalProperty("b", 2);
    requestParameters.setAdditionalProperty("c", 3);
    requestParameters.setAdditionalProperty("d", 4);
    String payload = "{\"existing_software_version\": \"3.1\",\"new_software_version\": \"3.2\", \"operations_timeout\": \"3600\"}";
    requestParameters.setAdditionalProperty("payload", payload);

    requestDetails.setRequestParameters(requestParameters);
    return requestDetails;
  }

  static org.onap.vid.changeManagement.RequestDetails generateChangeManagementMockMsoRequest() {
    List<RelatedInstanceList> relatedInstances = new LinkedList<>();
    relatedInstances.add(new RelatedInstanceList());

    org.onap.vid.changeManagement.RequestDetails requestDetails = new org.onap.vid.changeManagement.RequestDetails();

    requestDetails.setVnfName("test-vnf-name");
    requestDetails.setVnfInstanceId("test-vnf-instance_id");
    requestDetails.setRelatedInstList(relatedInstances);

    CloudConfiguration cloudConfiguration = new CloudConfiguration();
    cloudConfiguration.setTenantId("tenant-id");
    cloudConfiguration.setLcpCloudRegionId("lcp-region");
    requestDetails.setCloudConfiguration(cloudConfiguration);

    ModelInfo modelInfo = new ModelInfo();
    modelInfo.setModelInvariantId("model-invarient-id");
    modelInfo.setModelCustomizationName("modelCustomizationName");
    modelInfo.setModelType("test-model-type");
    requestDetails.setModelInfo(modelInfo);

    RequestInfo requestInfo = new RequestInfo();
    requestInfo.setRequestorId("ok883e");
    requestInfo.setSource("VID");
    requestDetails.setRequestInfo(requestInfo);

    RequestParameters requestParameters = new RequestParameters();
    requestParameters.setSubscriptionServiceType("subscriber-service-type");
    requestParameters.setAdditionalProperty("a", 1);
    requestParameters.setAdditionalProperty("b", 2);
    requestParameters.setAdditionalProperty("c", 3);
    requestParameters.setAdditionalProperty("d", 4);
    String payload = "{\"existing_software_version\": \"3.1\",\"new_software_version\": \"3.2\", \"operations_timeout\": \"3600\"}";
    requestParameters.setAdditionalProperty("payload", payload);

    requestDetails.setRequestParameters(requestParameters);
    return requestDetails;
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

