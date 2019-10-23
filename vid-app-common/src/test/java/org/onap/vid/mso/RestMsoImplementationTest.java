/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2019 Nokia Intellectual Property. All rights reserved.
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

package org.onap.vid.mso;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import io.joshworks.restclient.request.HttpRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyInvocation;
import org.hamcrest.MatcherAssert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.onap.vid.aai.util.HttpClientMode;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.mso.rest.RequestDetails;
import org.onap.vid.utils.Logging;
import org.onap.vid.utils.SystemPropertiesWrapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RestMsoImplementationTest  {

    @Mock
    private HttpRequest httpRequest;

    @Mock
    private Client mockClient;

    @Mock
    private HttpsAuthClient mockHttpsAuthClient;

    @Mock
    private WebTarget webTarget;

    @Mock
    private javax.ws.rs.client.Invocation.Builder builder;

    @Mock
    private Response response;

    @Mock
    private JerseyInvocation jerseyInvocation;

    @Mock
    private SystemPropertiesWrapper systemProperties;

    @Mock
    private Logging loggingService;

    @InjectMocks
    private RestMsoImplementation restMsoImplementation;

    private String path = "/test_path/";
    private String rawData = "test-row-data";

    @BeforeClass
    public void setUp() throws GeneralSecurityException, IOException {
        initMocks(this);
        when(mockHttpsAuthClient.getClient(any(HttpClientMode.class))).thenReturn(mockClient);
        when(systemProperties.getProperty(MsoProperties.MSO_PASSWORD)).thenReturn("OBF:1ghz1kfx1j1w1m7w1i271e8q1eas1hzj1m4i1iyy1kch1gdz");
    }

    @Test
    public void shouldProperlyInitMsoClient() {
        //  when
        MultivaluedHashMap<String, Object> result = restMsoImplementation.initMsoClient();

        //  then
        List<Object> authorizationHeaders = result.get("Authorization");
        MatcherAssert.assertThat(authorizationHeaders, hasSize(1));
        assertThat((String) authorizationHeaders.get(0)).startsWith("Basic ");
        assertThat(result).doesNotContainKey("notExistingKey");
    }


    @Test()
    public void shouldProperlyGetRestObjectForObjectWithRequestInfoAndAcceptCode() {
        //  given
        prepareMocks(rawData,HttpStatus.ACCEPTED.value(),"");

        //  when
        RestObject response = restMsoImplementation.GetForObject(path, HttpRequest.class);

        //  then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED.value());
        assertThat(response.getRaw()).isEqualTo(rawData);
    }

    @Test()
    public void shouldProperlyGetRestObjectForObjectWithRequestInfoAndBadRequestCode() {
        //  given
        prepareMocks(rawData,HttpStatus.BAD_REQUEST.value(),"");

        //  when
        RestObject response = restMsoImplementation.GetForObject(path, HttpRequest.class);

        //  then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getRaw()).isEqualTo(rawData);
    }


    @Test
    public void shouldProperlyPostForObject() {
        //  given
        RequestDetails requestDetails = new RequestDetails();

        RestObject<HttpRequest> expectedResponse = new RestObject<>();
        expectedResponse.setStatusCode(HttpStatus.ACCEPTED.value());
        expectedResponse.setRaw(rawData);

        prepareMocks(rawData,HttpStatus.ACCEPTED.value(),"POST");

        //  when
        RestObject<HttpRequest> response = restMsoImplementation.PostForObject(requestDetails, path, HttpRequest.class);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyPrepareClient() {
        //  given
        String method = "POST";
        prepareMocks(rawData,HttpStatus.ACCEPTED.value(),method);

        //  when
        javax.ws.rs.client.Invocation.Builder response = restMsoImplementation.prepareClient(path, method);

        //  then
        assertThat(response).isEqualTo(builder);
    }

    @Test
    public void shouldCreatRestObjectOnlyWithHttpMethod() {
        //  given
        String method = "GET";
        prepareMocks(rawData,HttpStatus.ACCEPTED.value(),method);

        RestObject<String> expectedResponse = new RestObject<>();
        expectedResponse.setStatusCode(HttpStatus.ACCEPTED.value());
        expectedResponse.setRaw(rawData);

        //  when
        RestObject<String> response = restMsoImplementation.restCall(HttpMethod.GET, String.class, null, path, Optional.empty());

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test( expectedExceptions = MsoTestException.class)
    public void shouldThrowExceptionWhenCreateRestObjectIsCalledWithoutDefinedClient() {
        //  given
        when(mockClient.target(any(String.class))).thenThrow(new MsoTestException("testNoClientException"));

        //  when
        restMsoImplementation.restCall(HttpMethod.GET, String.class, null, "", Optional.empty());
    }

    private void prepareMocks(String rawData,int status,String httpMethod) {

        when(mockClient.target(any(String.class))).thenReturn(webTarget);
        when(webTarget.request()).thenReturn(builder);


        when(builder.accept(any(String.class))).thenReturn(builder);
        when(builder.headers(any(MultivaluedMap.class))).thenReturn(builder);
        when(builder.property(eq(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION), eq(true))).thenReturn(builder);
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
