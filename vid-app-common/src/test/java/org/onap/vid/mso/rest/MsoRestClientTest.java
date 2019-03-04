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

import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.JsonMapper;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.mockito.Mock;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.changeManagement.RelatedInstanceList;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.controller.LocalWebConfig;
import org.onap.vid.model.RequestReferencesContainer;
import org.onap.vid.mso.MsoResponseWrapper;
import org.onap.vid.mso.MsoResponseWrapperInterface;
import org.onap.vid.mso.MsoUtil;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.model.CloudConfiguration;
import org.onap.vid.mso.model.ModelInfo;
import org.onap.vid.mso.model.RequestInfo;
import org.onap.vid.mso.model.RequestParameters;
import org.onap.vid.mso.model.RequestReferences;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


@ContextConfiguration(classes = {LocalWebConfig.class, SystemProperties.class})
@WebAppConfiguration
public class MsoRestClientTest {


    private final String baseUrl = "http://testURL/";

    @Mock
    private SyncRestClient client;

    private MsoRestClientNew restClient;


    @BeforeClass
    private void setUp(){
        initMocks(this);
        restClient = new MsoRestClientNew(client,baseUrl,null);

    }

    @Test
    public void shouldProperlyCreateSvcInstance() {
        //  given
        RequestDetails requestDetails = generateMockMsoRequest();

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
        RequestDetails requestDetails = generateMockMsoRequest();

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
    public void shouldProperlyCreateVnf() {
        //  given
        RequestDetails requestDetails = generateMockMsoRequest();

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
        RequestDetails requestDetails = generateMockMsoRequest();

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
        RequestDetails requestDetails = generateMockMsoRequest();

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
        RequestDetails requestDetails = generateMockMsoRequest();

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
        RequestDetails requestDetails = generateMockMsoRequest();

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
        RequestDetails requestDetails = generateMockMsoRequest();

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
        RequestDetails requestDetails = generateMockMsoRequest();

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
        RequestDetails requestDetails = generateMockMsoRequest();

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
        RequestDetails requestDetails = generateMockMsoRequest();

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
        RequestDetails requestDetails = generateMockMsoRequest();

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
        RequestDetails requestDetails = generateMockMsoRequest();

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
        RequestDetails requestDetails = generateMockMsoRequest();

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
        RequestDetails requestDetails = generateMockMsoRequest();

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
        //  given
        RestObject restObject = generateMockMsoRestObject();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.get( eq(baseUrl+endpoint),anyMap(),anyMap(),eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.getOrchestrationRequest(null,null,endpoint,restObject,true);

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
    public void shouldProperlyGetManualTasks() {
        //  given
        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when( client.get( eq(baseUrl+endpoint),anyMap(),anyMap(),eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.getManualTasks(endpoint);

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

        /// WUT 'baseUrl+baseUrl+endpoint'
        when( client.get( eq(baseUrl+baseUrl+endpoint),anyMap(),anyMap(),eq(String.class) )  ).thenReturn(httpResponse);

        //  when
        MsoResponseWrapper response = restClient.getManualTasksByRequestId(null,null,endpoint,restObject);

        //  then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test(expectedExceptions = MsoTestException.class)
    public void shouldThrowExceptionWhenGetManualTasksByRequestIdGetsWrongParameter() {
        //  given
        when( client.get( eq(baseUrl+baseUrl),anyMap(),anyMap(),eq(String.class) )  ).thenThrow(new MsoTestException("testsException"));

        //  when
        restClient.getManualTasksByRequestId(null,null,"",null);
    }

    @Test
    public void shouldProperlyCompleteManualTask() {
        //  given
        RequestDetails requestDetails = generateMockMsoRequest();
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
        org.onap.vid.changeManagement.RequestDetails requestDetails = generateChangeManagementMockMsoRequest();

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
        org.onap.vid.changeManagement.RequestDetails requestDetails = generateChangeManagementMockMsoRequest();

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
        org.onap.vid.changeManagement.RequestDetails requestDetails = generateChangeManagementMockMsoRequest();

        when( client.post( eq(baseUrl), anyMap(), any(RequestDetailsWrapper.class), eq(String.class) )  ).thenThrow(new MsoTestException("test-post-exception"));

        //  when
        restClient.replaceVnf(requestDetails, "");
    }

    @Test
    public void shouldProperlyDeleteConfiguration() {
        //  given
        org.onap.vid.changeManagement.RequestDetails requestDetails = generateChangeManagementMockMsoRequest();
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
        RequestDetails requestDetails = generateMockMsoRequest();

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
        RequestDetails requestDetails = generateMockMsoRequest();

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
    public void shouldProperlyChangeManagementUpdate() {
        //  given
        RequestDetails requestDetails = generateMockMsoRequest();
        RequestDetailsWrapper<RequestDetails> requestDetailsWrapper = new RequestDetailsWrapper<>(requestDetails);

        String endpoint = "testEndpoint";
        HttpResponse<RequestReferencesContainer> httpResponse = HttpResponse.fallback(
                new RequestReferencesContainer(
                        new RequestReferences()));

        MsoResponseWrapperInterface expectedResponse = MsoUtil.wrapResponse(httpResponse);

        when(client.post(eq(baseUrl + endpoint), anyMap(), eq(requestDetailsWrapper), eq(RequestReferencesContainer.class))).thenReturn(httpResponse);

        //  when
        MsoResponseWrapperInterface response = restClient.changeManagementUpdate(requestDetailsWrapper, endpoint);

        //  then
        assertThat(expectedResponse).isEqualToComparingFieldByField(response);
    }

    @Test
    public void shouldProperlyUpdateVnfAndUpdateInstance() {
        //  given
        org.onap.vid.changeManagement.RequestDetails requestDetails = generateChangeManagementMockMsoRequest();

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
        org.onap.vid.changeManagement.RequestDetails requestDetails = generateChangeManagementMockMsoRequest();
        String endpoint = "";

        when(client.put(eq(baseUrl), anyMap(), any(RequestDetailsWrapper.class), eq(String.class))).thenThrow(new MsoTestException("test-post-exception"));

        //  when
        restClient.updateVnf(requestDetails, endpoint);

        //  then
    }

    @Test
    public void shouldProperlySetServiceInstanceStatus() {
        //  given
        RequestDetails requestDetails = generateMockMsoRequest();
        RestObject<String> restObject = generateMockMsoRestObject();

        String endpoint = "testEndpoint";
        HttpResponse<String> httpResponse = HttpResponse.fallback("testOkResponse");

        when(client.post(eq(baseUrl + endpoint), anyMap(), eq(requestDetails), eq(String.class))).thenReturn(httpResponse);

        //  when
        restClient.setServiceInstanceStatus(requestDetails,"", "", endpoint, restObject);
    }

    @Test( expectedExceptions = MsoTestException.class)
    public void shouldThrowExceptionWhenSetServiceInstanceStatusGetsWrongParameter() {
        //  given
        String endpoint = "";

        when(client.post(eq(baseUrl), anyMap(), eq(null), eq(String.class))).thenThrow(new MsoTestException("test-post-exception"));

        //  when
        restClient.setServiceInstanceStatus(null,"", "", endpoint, null);
    }

    @Test
    public void shouldProperlyRemoveRelationshipFromServiceInstance() {
        //  given
        RequestDetails requestDetails = generateMockMsoRequest();

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
        RequestDetails requestDetails = generateMockMsoRequest();

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
    public void shouldProperlyGet_HttpResponse() {
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
    public void shouldProperlyPos_HttpResponse() {
        //  given

        RequestDetailsWrapper<RequestDetails> requestDetailsWrapper = new RequestDetailsWrapper<>(generateMockMsoRequest());

        String endpoint = "testEndpoint";
        HttpResponse<String> expectedResponse = HttpResponse.fallback("testOkResponse");

        when(client.post(eq(baseUrl + endpoint), anyMap(), eq(requestDetailsWrapper), eq(String.class))).thenReturn(expectedResponse);

        //  when
        HttpResponse<String>  response = restClient.post(endpoint,requestDetailsWrapper, String.class);

        //  then
        assertThat(expectedResponse).isEqualToComparingFieldByField(response);
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

    private RequestDetails generateMockMsoRequest() {
        RequestDetails requestDetails = new RequestDetails();

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

    private org.onap.vid.changeManagement.RequestDetails generateChangeManagementMockMsoRequest() {
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

    private RestObject<String> generateMockMsoRestObject() {
        RestObject<String> restObject = new RestObject<>();

        restObject.set("test-rest-object-body");
        restObject.setRaw("test-rest-object-raw-string");
        restObject.setStatusCode(202);
        return restObject;
    }
}
