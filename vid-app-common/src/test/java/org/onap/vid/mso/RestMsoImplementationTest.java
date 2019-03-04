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

package org.onap.vid.mso;

import io.joshworks.restclient.request.HttpRequest;
import org.glassfish.jersey.client.JerseyInvocation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.mso.rest.RequestDetails;
import org.springframework.http.HttpMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RestMsoImplementationTest  {

    @Mock
    HttpRequest httpRequest;

    @Mock
    Client mockClient;

    @Mock
    HttpsAuthClient mockHttpsAuthClient;

    @Mock
    WebTarget webTarget;

    @Mock
    javax.ws.rs.client.Invocation.Builder builder;

    @Mock
    Response response;

    @Mock
    JerseyInvocation jerseyInvocation;

    @InjectMocks
    private RestMsoImplementation restMsoImplementation = new RestMsoImplementation(mockHttpsAuthClient);

    String path = "/test_path/";
    String rawData = "test-row-data";

    @BeforeClass
    public void setUp(){
        initMocks(this);
    }

    @Test
    public void shouldProperlyInitMsoClient() {
        //  when
        MultivaluedHashMap<String, Object> result = restMsoImplementation.initMsoClient();

        //  then
        assertThat(result).containsKeys("Authorization","X-ONAP-PartnerName");
        assertThat(result).doesNotContainKey("notExistingKey");
    }

    @Test
    public void shouldProperlyGetRestObjectWithRequestInfo() {
        //  given
        RestObject<HttpRequest> restObject = new RestObject<>();

        prepareMocks(rawData,202,"");

        //  when
        RestObjectWithRequestInfo<HttpRequest> response = restMsoImplementation.Get(httpRequest, path, restObject,false);

        //  then
        assertThat(response.getRequestedUrl()).contains(path);
        assertThat(response.getRawData()).isEqualTo(rawData);
        assertThat(response.getHttpCode()).isEqualTo(202);
        assertThat(response.getHttpMethod()).isEqualTo(HttpMethod.GET);
    }

    @Test( expectedExceptions = GenericUncheckedException.class)
    public void shouldThrowExceptionWhenGetRestObjectWithRequestInfoGetsWrongStatus() {
        //  given
        String path = "";
        String rawData = "";
        RestObject<HttpRequest> restObject = new RestObject<>();

        prepareMocks(rawData,404,"");

        //  when
        restMsoImplementation.Get(httpRequest, path, restObject,false);
    }

    @Test( expectedExceptions = MsoTestException.class)
    public void shouldThrowExceptionWhenGetRestObjectWithRequestInfoGetsWrongParameters() {
        //  given
        String path = "";
        String rawData = "";
        RestObject<HttpRequest> restObject = new RestObject<>();

        prepareMocks(rawData,202,"");
        when(mockClient.target(SystemProperties.getProperty(MsoProperties.MSO_SERVER_URL))).thenThrow(new MsoTestException("test-target-exception"));

        //  when
        restMsoImplementation.Get(httpRequest, path, restObject,false);
    }

    @Test()
    public void shouldProperlyGetRestObjectForObjectWithRequestInfoAnd202Code() {
        //  given

        prepareMocks(rawData,202,"");

        //  when
        RestObject response = restMsoImplementation.GetForObject(path, HttpRequest.class);

        //  then
        assertThat(response.getStatusCode()).isEqualTo(202);
        assertThat(response.getRaw()).isEqualTo(rawData);
    }

    @Test()
    public void shouldProperlyGetRestObjectForObjectWithRequestInfoAnd404Code() {
        //  given

        prepareMocks(rawData,404,"");

        //  when
        RestObject response = restMsoImplementation.GetForObject(path, HttpRequest.class);

        //  then
        assertThat(response.getStatusCode()).isEqualTo(404);
        assertThat(response.getRaw()).isEqualTo(rawData);
    }

    @Test()
    public void shouldProperlyDeleteRestObjectWithStatus202() {
        //  given
        RestObject<HttpRequest> restObject = new RestObject<>();

        prepareMocks(rawData,202,"DELETE");

        //  when
        restMsoImplementation.Delete(httpRequest, "testObject", path, restObject);

        //  then
        assertThat(restObject.getStatusCode()).isEqualTo(202);
    }

    @Test()
    public void shouldProperlyDeleteRestObjectWithStatus200() {
        //  given
        RestObject<HttpRequest> restObject = new RestObject<>();

        prepareMocks(rawData,200,"DELETE");

        //  when
        restMsoImplementation.Delete(httpRequest, "testObject", path, restObject);

        //  then
        assertThat(restObject.getStatusCode()).isEqualTo(200);
    }

    @Test()
    public void shouldProperlyReturnFromDeleteWithStatus404() {
        //  given
        RestObject<HttpRequest> restObject = new RestObject<>();

        prepareMocks(rawData,404,"DELETE");

        //  when
        restMsoImplementation.Delete(httpRequest, "testObject", path, restObject);

        //  then
        assertThat(restObject.getStatusCode()).isEqualTo(404);
    }

    @Test()
    public void shouldProperlyReturnFromDeleteWithStatusOtherThenAbove() {
        //  given
        RestObject<HttpRequest> restObject = new RestObject<>();

        prepareMocks(rawData,501,"DELETE");

        //  when
        restMsoImplementation.Delete(httpRequest, "testObject", path, restObject);

        //  then
        assertThat(restObject.getStatusCode()).isEqualTo(501);
    }

    @Test( expectedExceptions = MsoTestException.class)
    public void shouldThrowExceptionWhenCallsDeleteWithWrongParameters() {
        //  given
        when(mockClient.target(any(String.class))).thenThrow(new MsoTestException("testDeleteException"));

        //  when
        restMsoImplementation.Delete(httpRequest, "testObject", "", null);
    }

    @Test( expectedExceptions = NullPointerException.class)
    public void shouldThrowExceptionWhenCallsDeleteWithWrongObjectType() {
        //  given
        RestObject<HttpRequest> restObject = new RestObject<>();

        prepareMocks(rawData,202,"DELETE");

        //  when
        restMsoImplementation.Delete(null, "testObject", path, restObject);
    }

    @Test
    public void shouldProperlyPostForObject() {
        //  given
        RequestDetails requestDetails = new RequestDetails();

        RestObject<HttpRequest> expectedResponse = new RestObject<>();
        expectedResponse.setStatusCode(202);
        expectedResponse.setRaw(rawData);

        prepareMocks(rawData,202,"POST");

        //  when
        RestObject<HttpRequest> response = restMsoImplementation.PostForObject(requestDetails, path, HttpRequest.class);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyDeleteForObject() {
        //  given
        RequestDetails requestDetails = new RequestDetails();

        RestObject<HttpRequest> expectedResponse = new RestObject<>();
        expectedResponse.setStatusCode(202);
        expectedResponse.setRaw(rawData);

        prepareMocks(rawData,202,"DELETE");

        //  when
        RestObject<HttpRequest> response = restMsoImplementation.DeleteForObject(requestDetails, path, HttpRequest.class);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyPost() {
        //  given
        RequestDetails requestDetails = new RequestDetails();
        RestObject<String> response = new RestObject<>();

        RestObject<String> expectedResponse = new RestObject<>();
        expectedResponse.setStatusCode(202);
        expectedResponse.setRaw(rawData);

        prepareMocks(rawData,202,"POST");

        //  when
        restMsoImplementation.Post(rawData,requestDetails, path, response);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyPrepareClient() {
        //  given
        String method = "POST";
        prepareMocks(rawData,202,method);

        //  when
        javax.ws.rs.client.Invocation.Builder response = restMsoImplementation.prepareClient(path, method);

        //  then
        assertThat(response).isEqualTo(builder);
    }

    @Test
    public void shouldCreatRestObjectOnlyWithHttpMethod() {
        //  given
        String method = "GET";
        prepareMocks(rawData,202,method);

        RestObject<String> expectedResponse = new RestObject<>();
        expectedResponse.setStatusCode(202);
        expectedResponse.setRaw(rawData);

        //  when
        RestObject<String> response = restMsoImplementation.restCall(HttpMethod.GET, String.class, null, path, Optional.empty());

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test( expectedExceptions = MsoTestException.class)
    public void shouldThrowExceptionWhenCreatRestObjectIsCalledWithoutDefinedClient() {
        //  given
        when(mockClient.target(any(String.class))).thenThrow(new MsoTestException("testNoClientException"));


        //  when
        restMsoImplementation.restCall(HttpMethod.GET, String.class, null, "", Optional.empty());
    }

    @Test
    public void shouldProperlyPutRestObjectWithProperParametersAndStatus202() {
        //  given
        String method = "PUT";
        prepareMocks(rawData,202,method);

        org.onap.vid.changeManagement.RequestDetailsWrapper requestDetailsWrapper = new RequestDetailsWrapper();
        RestObject<String> response = new RestObject<>();

        RestObject<String> expectedResponse = new RestObject<>();
        expectedResponse.setStatusCode(202);
        expectedResponse.set(rawData);

        //  when
        restMsoImplementation.Put("testPutBody", requestDetailsWrapper , path, response);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyPutRestObjectWithProperParametersAndStatus300() {
        //  given
        String method = "PUT";
        prepareMocks(rawData,300,method);

        org.onap.vid.changeManagement.RequestDetailsWrapper requestDetailsWrapper = new RequestDetailsWrapper();
        RestObject<String> response = new RestObject<>();

        RestObject<String> expectedResponse = new RestObject<>();
        expectedResponse.setStatusCode(300);
        expectedResponse.set(rawData);

        //  when
        restMsoImplementation.Put("testPutBody", requestDetailsWrapper , path, response);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test( expectedExceptions = MsoTestException.class)
    public void shouldThrowExceptionWhenCallsPutWithWrongParameters() {
        //  given
        when(mockClient.target(any(String.class))).thenThrow(new MsoTestException("testDeleteException"));
        org.onap.vid.changeManagement.RequestDetailsWrapper requestDetailsWrapper = new RequestDetailsWrapper();

        //  when
        restMsoImplementation.Put(null, requestDetailsWrapper, "", null);
    }

    @Test( expectedExceptions = NullPointerException.class)
    public void shouldThrowExceptionWhenCallsPutWithWrongObjectType() {
        //  given
        RestObject<HttpRequest> restObject = new RestObject<>();
        org.onap.vid.changeManagement.RequestDetailsWrapper requestDetailsWrapper = new RequestDetailsWrapper();

        prepareMocks(rawData,202,"DELETE");

        //  when
        restMsoImplementation.Put(null, requestDetailsWrapper, path, restObject);
    }




    private void prepareMocks(String rawData,int status,String httpMethod) {

        when(mockClient.target(any(String.class))).thenReturn(webTarget);
        when(webTarget.request()).thenReturn(builder);


        when(builder.accept(any(String.class))).thenReturn(builder);
        when(builder.headers(any(MultivaluedMap.class))).thenReturn(builder);
        when(builder.get()).thenReturn(response);

        when(builder.build( eq(httpMethod), any(Entity.class))).thenReturn(jerseyInvocation);
        when(builder.build( eq(httpMethod))).thenReturn(jerseyInvocation);

        when(builder.put( any(Entity.class))).thenReturn(response);
        when(jerseyInvocation.invoke()).thenReturn(response);


        when(response.getStatus()).thenReturn(status);
        when(response.readEntity(String.class)).thenReturn(rawData);
    }

    private class MsoTestException extends RuntimeException{
        MsoTestException(String testException) {
            super(testException);
        }
    }

}