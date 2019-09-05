/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 - 2019 Nokia. All rights reserved.
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static org.onap.vid.utils.KotlinUtilsKt.JACKSON_OBJECT_MAPPER;
import static org.onap.vid.utils.Logging.ONAP_REQUEST_ID_HEADER_KEY;
import static org.testng.Assert.assertNotEquals;
import static org.testng.AssertJUnit.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.JsonMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.HttpResponseWithRequestInfo;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.changeManagement.WorkflowRequestDetail;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.controller.LocalWebConfig;
import org.onap.vid.model.RequestReferencesContainer;
import org.onap.vid.mso.MsoProperties;
import org.onap.vid.mso.MsoResponseWrapper;
import org.onap.vid.mso.MsoResponseWrapperInterface;
import org.onap.vid.mso.MsoUtil;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.model.RequestReferences;
import org.onap.vid.utils.SystemPropertiesWrapper;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


@ContextConfiguration(classes = {LocalWebConfig.class, SystemProperties.class})
@WebAppConfiguration
public class MsoRestClientTest {


    private final String baseUrl = "http://testURL/";

    @Mock
    private SyncRestClient client;

    @Mock
    private SystemPropertiesWrapper systemProperties;

    private MsoRestClientNew restClient;


    @BeforeClass
    private void setUp(){
        initMocks(this);
        when(systemProperties.getProperty(MsoProperties.MSO_PASSWORD)).thenReturn("OBF:1ghz1kfx1j1w1m7w1i271e8q1eas1hzj1m4i1iyy1kch1gdz");
        when(systemProperties.getProperty("app_display_name")).thenReturn("vid");
        restClient = new MsoRestClientNew(client,baseUrl,null,systemProperties);
    }

    @Test
    public void shouldProperlyCreateServiceInstance() {
        //  given
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.post( eq(baseUrl+endpoint),anyMap(),eq(requestDetails),eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.createSvcInstance(requestDetails,endpoint);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test( expectedExceptions = MsoTestException.class)
    public void shouldThrowExceptionWhenCreateSvcInstanceGetsWrongParameters() {
        //  given
        String endpoint = "";

        when( client.post( eq(baseUrl+endpoint),anyMap(),eq(null),eq(String.class) )  ).
                thenThrow(new MsoTestException("test-post-exception"));

        //  when
        restClient.createSvcInstance(null,endpoint);
    }

    @Test
    public void shouldProperlyCreateE2eSvcInstance() {
        //  given
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.post( eq(baseUrl+endpoint),anyMap(),eq(requestDetails),eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.createE2eSvcInstance(requestDetails,endpoint);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void whenCreateInstanceTwice_thenRequestIdHeaderIsDifferentEachTime() {

        //given
        Mockito.reset(client);
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        when( client.post( anyString() ,anyMap(), any(RequestDetails.class), eq(String.class) )  ).thenReturn(httpResponse);

        //when
        restClient.createInstance(requestDetails,"someEndPoint");
        restClient.createInstance(requestDetails,"someEndPoint");

        //then
        ArgumentCaptor<Map<String,String>> requestCaptor = ArgumentCaptor.forClass(Map.class);
        verify(client, times(2)).post(anyString(), requestCaptor.capture(), any(RequestDetails.class), eq(String.class));
        assertEquals(2, requestCaptor.getAllValues().size());
        assertNotEquals(requestCaptor.getAllValues().get(0).get(SystemProperties.ECOMP_REQUEST_ID),
                        requestCaptor.getAllValues().get(1).get(SystemProperties.ECOMP_REQUEST_ID),
                        SystemProperties.ECOMP_REQUEST_ID+ " headers are the same");
        assertNotEquals(requestCaptor.getAllValues().get(0).get(ONAP_REQUEST_ID_HEADER_KEY),
                        requestCaptor.getAllValues().get(1).get(ONAP_REQUEST_ID_HEADER_KEY),
                        ONAP_REQUEST_ID_HEADER_KEY+ " headers are the same");
    }

    @Test
    public void shouldProperlyCreateVnf() {
        //  given
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.post( eq(baseUrl+endpoint),anyMap(),eq(requestDetails),eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.createVnf(requestDetails,endpoint);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyCreateNwInstance() {
        //  given
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.post( eq(baseUrl+endpoint),anyMap(),eq(requestDetails),eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.createNwInstance(requestDetails,endpoint);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyCreateVolumeGroupInstance() {
        //  given
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.post( eq(baseUrl+endpoint),anyMap(),eq(requestDetails),eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.createVolumeGroupInstance(requestDetails,endpoint);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyCreateVfModuleInstance() {
        //  given
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.post( eq(baseUrl+endpoint),anyMap(),eq(requestDetails),eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.createVfModuleInstance(requestDetails,endpoint);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyScaleOutVFModuleInstance() {
        //  given
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();

        RequestDetailsWrapper<RequestDetails> wrappedRequestDetails = new RequestDetailsWrapper<>(requestDetails);
        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.post( eq(baseUrl+endpoint),anyMap(),eq(wrappedRequestDetails),eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.scaleOutVFModuleInstance(wrappedRequestDetails,endpoint);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyCreateConfigurationInstance() {
        //  given
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();

        org.onap.vid.mso.rest.RequestDetailsWrapper wrappedRequestDetails = new  org.onap.vid.mso.rest.RequestDetailsWrapper(requestDetails);
        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.post( eq(baseUrl+endpoint),anyMap(),eq(wrappedRequestDetails),eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.createConfigurationInstance(wrappedRequestDetails,endpoint);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyDeleteE2eSvcInstance() {
        //  given
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();

        org.onap.vid.mso.rest.RequestDetailsWrapper wrappedRequestDetails = new  org.onap.vid.mso.rest.RequestDetailsWrapper(requestDetails);
        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.delete( eq(baseUrl+endpoint),anyMap(),eq(wrappedRequestDetails),eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.deleteE2eSvcInstance(wrappedRequestDetails,endpoint);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyDeleteSvcInstance() {
        //  given
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.delete( eq(baseUrl+endpoint),anyMap(),eq(requestDetails),eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.deleteSvcInstance(requestDetails,endpoint);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyUnassignSvcInstance() {
        //  given
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.post( eq(baseUrl+endpoint),anyMap(),eq(requestDetails),eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.unassignSvcInstance(requestDetails,endpoint);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyDeleteVnf() {
        //  given
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.delete( eq(baseUrl+endpoint),anyMap(),eq(requestDetails),eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.deleteVnf(requestDetails,endpoint);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyDeleteVfModule() {
        //  given
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.delete( eq(baseUrl+endpoint),anyMap(),eq(requestDetails),eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.deleteVfModule(requestDetails,endpoint);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyDeleteVolumeGroupInstance() {
        //  given
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.delete( eq(baseUrl+endpoint),anyMap(),eq(requestDetails),eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.deleteVolumeGroupInstance(requestDetails,endpoint);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyDeleteNwInstance() {
        //  given
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.delete( eq(baseUrl+endpoint),anyMap(),eq(requestDetails),eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.deleteNwInstance(requestDetails,endpoint);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyGetOrchestrationRequest() {
        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        String expectedPath = baseUrl+endpoint;
        HttpResponseWithRequestInfo<String> expectedResponse = new HttpResponseWithRequestInfo<>(httpResponse, expectedPath, HttpMethod.GET);

        when( client.get( eq(expectedPath), anyMap(), anyMap(), eq(String.class) )).thenReturn(httpResponse);

        //  when
        HttpResponseWithRequestInfo<String> response = restClient.getOrchestrationRequest(endpoint, true);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyGetOrchestrationRequestWithOnlyEndpoint() {
        //  given
        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.get( eq(baseUrl+endpoint),anyMap(),anyMap(),eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.getOrchestrationRequest(endpoint);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyGetManualTasksByRequestId() {
        //  given
        RestObject restObject = generateMockMsoRestObject();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.get( eq(baseUrl+endpoint),anyMap(),anyMap(),eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.getManualTasksByRequestId(null,null,endpoint,restObject);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test(expectedExceptions = MsoTestException.class)
    public void shouldThrowExceptionWhenGetManualTasksByRequestIdGetsWrongParameter() {
        //  given
        when( client.get( eq(baseUrl),anyMap(),anyMap(),eq(String.class) )  ).thenThrow(new MsoTestException("testsException"));

        //  when
        restClient.getManualTasksByRequestId(null,null,"",null);
    }

    @Test
    public void shouldProperlyCompleteManualTask() {
        //  given
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();
        RestObject restObject = generateMockMsoRestObject();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.post( eq(baseUrl+endpoint), anyMap(), eq(requestDetails), eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.completeManualTask(requestDetails,null,null,endpoint,restObject);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test(expectedExceptions = MsoTestException.class)
    public void shouldThrowExceptionWhenCompleteManualTaskWrongParameter() {
        //  given
        when( client.post( eq(baseUrl),anyMap(),eq(null), eq(String.class) )  ).thenThrow(new MsoTestException("testsException"));

        //  when
        restClient.completeManualTask(null, null,null,"",null);
    }

    @Test
    public void shouldProperlyReplaceVnf() {
        //  given
        org.onap.vid.changeManagement.RequestDetails requestDetails = MsoRestClientTestUtil.generateChangeManagementMockMsoRequest();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.post( eq(baseUrl+endpoint), anyMap(), any(RequestDetailsWrapper.class), eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.replaceVnf(requestDetails,endpoint);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyReplaceVnfWithStatus202() {
        //  given
        org.onap.vid.changeManagement.RequestDetails requestDetails = MsoRestClientTestUtil.generateChangeManagementMockMsoRequest();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = createOkResponse();
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.post( eq(baseUrl+endpoint), anyMap(), any(RequestDetailsWrapper.class), eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.replaceVnf(requestDetails,endpoint);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test( expectedExceptions = MsoTestException.class)
    public void shouldThrowExceptionWhenReplaceVnfGetsWrongParameters() {
        //  given
        org.onap.vid.changeManagement.RequestDetails requestDetails = MsoRestClientTestUtil.generateChangeManagementMockMsoRequest();

        when( client.post( eq(baseUrl), anyMap(), any(RequestDetailsWrapper.class), eq(String.class) )  ).thenThrow(new MsoTestException("test-post-exception"));

        //  when
        restClient.replaceVnf(requestDetails, "");
    }

    @Test
    public void shouldProperlyDeleteConfiguration() {
        //  given
        org.onap.vid.changeManagement.RequestDetails requestDetails = MsoRestClientTestUtil.generateChangeManagementMockMsoRequest();
        org.onap.vid.mso.rest.RequestDetailsWrapper requestDetailsWrapper = new org.onap.vid.mso.rest.RequestDetailsWrapper(requestDetails);

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.delete( eq(baseUrl+endpoint), anyMap(), eq(requestDetailsWrapper), eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.deleteConfiguration(requestDetailsWrapper,endpoint);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test( expectedExceptions = MsoTestException.class )
    public void shouldThrowExceptionWhenProperlyDeleteConfigurationGetsWrongParameters() {
        //  given
        when( client.delete( eq(baseUrl), anyMap(), eq(null), eq(String.class) )  ).thenThrow(new MsoTestException("test-delete-exception"));

        //  when
        restClient.deleteConfiguration(null,"");
    }

    @Test
    public void shouldProperlySetConfigurationActiveStatus() {
        //  given
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when(client.post(eq(baseUrl + endpoint), anyMap(), eq(requestDetails), eq(String.class))).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.setConfigurationActiveStatus(requestDetails, endpoint);

        //  then
        assertThat(expectedResponse).isEqualToComparingFieldByField(response);
    }

    @Test(expectedExceptions = MsoTestException.class)
    public void shouldThrowExceptionWhenSetConfigurationActiveStatusGetsWrongParameters() {
        //  given

        when(client.post(eq(baseUrl), anyMap(), eq(null), eq(String.class))).thenThrow(new MsoTestException("test-post-exception"));

        //  when
        restClient.setConfigurationActiveStatus(null, "");
    }

    @Test
    public void shouldProperlySetPortOnConfigurationStatus() {
        //  given
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when(client.post(eq(baseUrl + endpoint), anyMap(), eq(requestDetails), eq(String.class))).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.setPortOnConfigurationStatus(requestDetails, endpoint);

        //  then
        assertThat(expectedResponse).isEqualToComparingFieldByField(response);
    }

    @Test(expectedExceptions = MsoTestException.class)
    public void shouldThrowExceptionWhenSetPortOnConfigurationStatusGetsWrongParameters() {
        //  given
        String endpoint = "";

        when(client.post(eq(baseUrl), anyMap(), eq(null), eq(String.class))).thenThrow(new MsoTestException("test-post-exception"));

        //  when
        restClient.setPortOnConfigurationStatus(null, endpoint);
    }

    @Test
    public void shouldProperlyChangeManagementUpdate() throws JsonProcessingException {
        //  given
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();
        RequestDetailsWrapper<RequestDetails> requestDetailsWrapper = new RequestDetailsWrapper<>(requestDetails);

        String endpoint = "testEndpoint";
        RequestReferencesContainer entity = new RequestReferencesContainer(new RequestReferences());
        HttpResponse<String> httpResponse = HttpResponse.fallback(JACKSON_OBJECT_MAPPER.writeValueAsString(entity));

        when(client.post(eq(baseUrl + endpoint), anyMap(), eq(requestDetailsWrapper), eq(String.class))).thenReturn(httpResponse);

        //  when
        MsoResponseWrapperInterface response = restClient.changeManagementUpdate(requestDetailsWrapper, endpoint);

        //  then
        assertThat(response.getEntity()).isEqualToComparingFieldByField(entity);
        assertThat(response.getStatus()).isEqualTo(0);
    }

    @Test
    public void shouldProperlyUpdateVnfAndUpdateInstance() {
        //  given
        org.onap.vid.changeManagement.RequestDetails requestDetails = MsoRestClientTestUtil.generateChangeManagementMockMsoRequest();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapperInterface expectedResponse = MsoUtil.wrapResponse(httpResponse);


        when(client.put(eq(baseUrl + endpoint), anyMap(), any(RequestDetailsWrapper.class), eq(String.class))).thenReturn(httpResponse);

        //  when
        MsoResponseWrapperInterface response = restClient.updateVnf(requestDetails, endpoint);

        //  then
        assertThat(expectedResponse).isEqualToComparingFieldByField(response);
    }

    @Test( expectedExceptions = MsoTestException.class )
    public void shouldThrowExceptionWhenUpdateVnfAndUpdateInstanceGetsWrongParameter() {
        //  given
        org.onap.vid.changeManagement.RequestDetails requestDetails = MsoRestClientTestUtil.generateChangeManagementMockMsoRequest();
        String endpoint = "";

        when(client.put(eq(baseUrl), anyMap(), any(RequestDetailsWrapper.class), eq(String.class))).thenThrow(new MsoTestException("test-post-exception"));

        //  when
        restClient.updateVnf(requestDetails, endpoint);

        //  then
    }

    @Test
    public void shouldProperlySetServiceInstanceStatus() {
        //  given
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();
        RestObject<String> restObject = generateMockMsoRestObject();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");

        when(client.post(eq(baseUrl + endpoint), anyMap(), eq(requestDetails), eq(String.class))).thenReturn(httpResponse);

        //  when
        restClient.setServiceInstanceStatus(requestDetails, endpoint);
    }

    @Test( expectedExceptions = MsoTestException.class)
    public void shouldThrowExceptionWhenSetServiceInstanceStatusGetsWrongParameter() {
        //  given
        String endpoint = "";

        when(client.post(eq(baseUrl), anyMap(), eq(null), eq(String.class))).thenThrow(new MsoTestException("test-post-exception"));

        //  when
        restClient.setServiceInstanceStatus(null, endpoint);
    }

    @Test
    public void shouldProperlyRemoveRelationshipFromServiceInstance() {
        //  given
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when(client.post(eq(baseUrl + endpoint), anyMap(), eq(requestDetails), eq(String.class))).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.removeRelationshipFromServiceInstance(requestDetails, endpoint);

        //  then
        assertThat(expectedResponse).isEqualToComparingFieldByField(response);
    }

    @Test( expectedExceptions = MsoTestException.class)
    public void shouldThrowExceptionWhenRemoveRelationshipFromServiceInstanceGetsWrongParameter() {
        //  given
        String endpoint = "";

        when(client.post(eq(baseUrl), anyMap(), eq(null), eq(String.class))).thenThrow(new MsoTestException("test-post-exception"));

        //  when
        restClient.removeRelationshipFromServiceInstance(null,endpoint);
    }

    @Test
    public void shouldProperlyAddRelationshipToServiceInstance() {
        //  given
        RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when(client.post(eq(baseUrl + endpoint), anyMap(), eq(requestDetails), eq(String.class))).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.addRelationshipToServiceInstance(requestDetails, endpoint);

        //  then
        assertThat(expectedResponse).isEqualToComparingFieldByField(response);
    }

    @Test( expectedExceptions = MsoTestException.class)
    public void shouldThrowExceptionWhenAddRelationshipToServiceInstanceGetsWrongParameter() {
        //  given
        String endpoint = "";

        when(client.post(eq(baseUrl), anyMap(), eq(null), eq(String.class))).thenThrow(new MsoTestException("test-post-exception"));

        //  when
        restClient.addRelationshipToServiceInstance(null,endpoint);
    }

    @Test
    public void shouldProperlyPerformGetRequest() {
        //  given
        String endpoint = "testEndpoint";
        HttpResponse<String> expectedResponse = HttpResponse.fallback("testOkResponse");

        when(client.get(eq(baseUrl + endpoint), anyMap(), anyMap(), eq(String.class))).thenReturn(expectedResponse);

        //  when
        HttpResponse<String>  response = restClient.get(endpoint, String.class);

        //  then
        assertThat(expectedResponse).isEqualToComparingFieldByField(response);
    }

    @Test
    public void shouldProperlyPerformPostRequest() {
        //  given

        RequestDetailsWrapper<RequestDetails> requestDetailsWrapper = new RequestDetailsWrapper<>(MsoRestClientTestUtil.generateMockMsoRequest());

        String endpoint = "testEndpoint";
        HttpResponse<String> expectedResponse = HttpResponse.fallback("testOkResponse");

        when(client.post(eq(baseUrl + endpoint), anyMap(), eq(requestDetailsWrapper), eq(String.class))).thenReturn(expectedResponse);

        //  when
        HttpResponse<String>  response = restClient.post(endpoint,requestDetailsWrapper, String.class);

        //  then
        assertThat(expectedResponse).isEqualToComparingFieldByField(response);
    }

    @Test
    public void shouldProperlyInvokeWorkflows() {
        //  given
        String endpoint = "testPath";
        HttpResponse expectedResponse = createOkResponse();

        WorkflowRequestDetail workflowRequestDetail = MsoRestClientTestUtil.createWorkflowRequestDetail();

        RequestDetailsWrapper<WorkflowRequestDetail> requestDetailsWrapper = new RequestDetailsWrapper<>(workflowRequestDetail);

        UUID requestId = UUID.randomUUID();

        when(client.post(eq(baseUrl + endpoint), argThat(allOf(hasEntry("X-ONAP-RequestID", requestId.toString()),hasEntry("Content-Type", "application/json"))), refEq(requestDetailsWrapper))).
                thenReturn(expectedResponse);

        Map<String,String> extraHeaders = new HashMap<>();
        extraHeaders.put("X-ONAP-RequestID",requestId.toString());
        extraHeaders.put("X-ONAP-PartnerName","VID");
        extraHeaders.put("X-RequestorID","testRequester");

        //  when
        MsoResponseWrapper response = restClient.invokeWorkflow(workflowRequestDetail, endpoint, extraHeaders);

        //  then
        assertThat(response).isEqualToComparingFieldByField(MsoUtil.wrapResponse(expectedResponse));

    }

    private class MsoTestException extends RuntimeException{
        MsoTestException(String testException) {
            super(testException);
        }
    }

    private HttpResponse<String> createOkResponse() {
        StatusLine statusline = new BasicStatusLine(
                new ProtocolVersion("http",1,1), 202, "acceptResponse");

        org.apache.http.HttpResponse responseBase = new BasicHttpResponse(statusline);

        return new HttpResponse<>(responseBase ,String.class, new JsonMapper());
    }

    private RestObject<String> generateMockMsoRestObject() {
        RestObject<String> restObject = new RestObject<>();

        restObject.set("test-rest-object-body");
        restObject.setRaw("test-rest-object-raw-string");
        restObject.setStatusCode(202);
        return restObject;
    }
}
