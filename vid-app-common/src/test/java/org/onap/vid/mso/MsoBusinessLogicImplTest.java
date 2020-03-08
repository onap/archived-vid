/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 Nokia. All rights reserved.
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

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.tuple;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.onap.vid.controller.MsoController.CONFIGURATION_ID;
import static org.onap.vid.controller.MsoController.REQUEST_TYPE;
import static org.onap.vid.controller.MsoController.SVC_INSTANCE_ID;
import static org.onap.vid.controller.MsoController.VNF_INSTANCE_ID;
import static org.onap.vid.model.probes.ExternalComponentStatus.Component.MSO;
import static org.onap.vid.mso.MsoBusinessLogicImpl.validateEndpointPath;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.joshworks.restclient.http.HttpResponse;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.ws.rs.BadRequestException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpException;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.jetbrains.annotations.NotNull;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.hamcrest.MockitoHamcrest;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.aai.HttpResponseWithRequestInfo;
import org.onap.vid.changeManagement.ChangeManagementRequest;
import org.onap.vid.changeManagement.WorkflowRequestDetail;
import org.onap.vid.controller.OperationalEnvironmentController;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.model.SOWorkflowList;
import org.onap.vid.model.SoftDeleteRequest;
import org.onap.vid.model.probes.ErrorMetadata;
import org.onap.vid.model.probes.ExternalComponentStatus;
import org.onap.vid.model.probes.HttpRequestMetadata;
import org.onap.vid.mso.model.CloudConfiguration;
import org.onap.vid.mso.model.ModelInfo;
import org.onap.vid.mso.model.OperationalEnvironmentActivateInfo;
import org.onap.vid.mso.model.OperationalEnvironmentDeactivateInfo;
import org.onap.vid.mso.model.RequestParameters;
import org.onap.vid.mso.rest.OperationalEnvironment.OperationEnvironmentRequestDetails;
import org.onap.vid.mso.rest.Request;
import org.onap.vid.mso.rest.RequestDetails;
import org.onap.vid.mso.rest.RequestDetailsWrapper;
import org.onap.vid.mso.rest.Task;
import org.onap.vid.testUtils.TestUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

@ContextConfiguration(classes = {SystemProperties.class})
public class MsoBusinessLogicImplTest extends AbstractTestNGSpringContextTests {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String MY_PRETTY_URL = "my pretty url";

    @Mock
    private MsoInterface msoInterface;

    @Mock
    private SOWorkflowList workflowList;

    @Mock
    private HttpResponse<SOWorkflowList> workflowListResponse;

    @Mock
    private RequestDetails msoRequest;

    @Mock
    private FeatureManager featureManager;



    private MsoBusinessLogicImpl msoBusinessLogic;
    private String userId = "testUserId";
    private String operationalEnvironmentId = "testOperationalEnvironmentId";

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        msoBusinessLogic = new MsoBusinessLogicImpl(msoInterface, featureManager);
    }

    @Test
    public void shouldProperlyCreateConfigurationInstanceWithCorrectServiceInstanceId() throws Exception {
        // given
        String serviceInstanceId = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
        String endpointTemplate = String.format("/serviceInstantiation/v7/serviceInstances/%s/configurations", serviceInstanceId);
        RequestDetailsWrapper requestDetailsWrapper = createRequestDetails();
        MsoResponseWrapper expectedResponse = createOkResponse();
        given(msoInterface.createConfigurationInstance(requestDetailsWrapper, endpointTemplate))
                .willReturn(expectedResponse);

        // when
        MsoResponseWrapper msoResponseWrapper = msoBusinessLogic
                .createConfigurationInstance(requestDetailsWrapper, serviceInstanceId);

        // then
        assertThat(msoResponseWrapper).isEqualToComparingFieldByField(expectedResponse);
    }

    private RequestDetailsWrapper createRequestDetails() throws Exception {
        final URL resource = this.getClass().getResource("/payload_jsons/mso_request_create_configuration.json");
        RequestDetails requestDetails = objectMapper.readValue(resource, RequestDetails.class);
        return new RequestDetailsWrapper(requestDetails);
    }

    @Test
    public void shouldProperlyValidateEndpointPathWheEendPointIsNotEmptyAndValid() {
        System.setProperty("TestEnv", "123");
        String foundEndPoint = validateEndpointPath("TestEnv");
        assertEquals("123", foundEndPoint);
    }

    @Test
    public void shouldThrowRuntimeExceptionWhenValidateEndpointPathEndPointIsNotEmptyAndValid() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> validateEndpointPath("NotExists"));
    }

    @Test
    public void shouldThrowRuntimeExceptionWhenValidateEndpointPathWhenEndPointIsNotEmptyButDoesntExists() {
        String endPoint = "EmptyEndPoint";
        System.setProperty(endPoint, "");
        assertThatExceptionOfType(GenericUncheckedException.class)
                .isThrownBy(() -> validateEndpointPath(endPoint))
                .withMessage(endPoint + " env variable is not defined");
    }

    @Test
    public void shouldProperlyCreateSvcInstanceWithProperParameters() {

        MsoResponseWrapper expectedResponse = createOkResponse();
        String svcEndpoint = SystemProperties.getProperty(MsoProperties.MSO_RESTAPI_SERVICE_INSTANCE);
        given(msoInterface.createSvcInstance(msoRequest, svcEndpoint)).willReturn(expectedResponse);

        MsoResponseWrapper response = msoBusinessLogic.createSvcInstance(msoRequest);

        // then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyCreateE2eSvcInstanceWithProperParameters() {
        //  given
        MsoResponseWrapper expectedResponse = createOkResponse();
        String svcEndpoint = SystemProperties.getProperty(MsoProperties.MSO_REST_API_E2E_SVC_INSTANCE);
        given(msoInterface.createE2eSvcInstance(msoRequest, svcEndpoint)).willReturn(expectedResponse);

        //  when
        MsoResponseWrapper response = msoBusinessLogic.createE2eSvcInstance(msoRequest);

        // then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyCreateVnfWithProperParameters() {

        MsoResponseWrapper expectedResponse = createOkResponse();
        String endpoint = SystemProperties.getProperty(MsoProperties.MSO_REST_API_VNF_INSTANCE);
        String vnfInstanceId = "testVnfInstanceTempId";
        String vnfEndpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, vnfInstanceId);

        given(msoInterface.createVnf(msoRequest, vnfEndpoint)).willReturn(expectedResponse);

        MsoResponseWrapper response = msoBusinessLogic.createVnf(msoRequest, vnfInstanceId);

        // then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyCreateNwInstanceWithProperParameters() {

        MsoResponseWrapper expectedResponse = createOkResponse();
        String vnfInstanceId = "testNwInstanceTempId";
        String nwEndpoint = SystemProperties.getProperty(MsoProperties.MSO_REST_API_NETWORK_INSTANCE);
        String nw_endpoint = nwEndpoint.replaceFirst(SVC_INSTANCE_ID, vnfInstanceId);

        given(msoInterface.createNwInstance(msoRequest, nw_endpoint)).willReturn(expectedResponse);

        MsoResponseWrapper response = msoBusinessLogic.createNwInstance(msoRequest, vnfInstanceId);

        // then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyCreateVolumeGroupInstanceWithProperParameters() {
        MsoResponseWrapper expectedResponse = createOkResponse();
        String serviceInstanceId = "testServiceInstanceTempId";
        String vnfInstanceId = "testVnfInstanceTempId";
        String nwEndpoint = SystemProperties.getProperty(MsoProperties.MSO_REST_API_VOLUME_GROUP_INSTANCE);
        String vnfEndpoint = nwEndpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        vnfEndpoint = vnfEndpoint.replaceFirst(VNF_INSTANCE_ID, vnfInstanceId);

        given(msoInterface.createVolumeGroupInstance(msoRequest, vnfEndpoint)).willReturn(expectedResponse);

        MsoResponseWrapper response = msoBusinessLogic.createVolumeGroupInstance(msoRequest, serviceInstanceId, vnfInstanceId);

        // then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyCreateVfModuleInstanceWithProperParameters() {
        MsoResponseWrapper expectedResponse = createOkResponse();
        String serviceInstanceId = "testServiceInstanceTempId";
        String vnfInstanceId = "testVnfInstanceTempId";
        String partial_endpoint = SystemProperties.getProperty(MsoProperties.MSO_REST_API_VF_MODULE_INSTANCE);
        String vf_module_endpoint = partial_endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        vf_module_endpoint = vf_module_endpoint.replaceFirst(VNF_INSTANCE_ID, vnfInstanceId);

        given(msoInterface.createVfModuleInstance(msoRequest, vf_module_endpoint)).willReturn(expectedResponse);

        MsoResponseWrapper response = msoBusinessLogic.createVfModuleInstance(msoRequest, serviceInstanceId, vnfInstanceId);

        // then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyDeleteE2eSvcInstanceWithProperParameters() {
        MsoResponseWrapper expectedResponse = createOkResponse();
        String serviceInstanceId = "testDeleteE2eSvcInstanceTempId";
        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_E2E_SVC_INSTANCE) + "/" + serviceInstanceId;

        given(msoInterface.deleteE2eSvcInstance(msoRequest, endpoint)).willReturn(expectedResponse);

        MsoResponseWrapper response = msoBusinessLogic.deleteE2eSvcInstance(msoRequest, serviceInstanceId);

        // then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @DataProvider
    public Object[][] deleteSvcInstanceShouldDelete() {
        return new Object[][]{{"Active"}, {"unexpected-status"}};
    }

    @DataProvider
    public Object[][] deleteSvcInstanceShouldUnassign() {
        return new Object[][]{{"Created"}, {"Pendingdelete"}, {"pending-Delete"}, {"Assigned"}};
    }


    @Test(dataProvider = "deleteSvcInstanceShouldDelete")
    public void shouldProperlyDeleteSvcInstanceWithProperParametersShouldDelete(String status) {
        // given
        String endpointTemplate = "/serviceInstantiation/v7/serviceInstances/%s";
        String serviceInstanceId = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
        String svcEndpoint = String.format(endpointTemplate, serviceInstanceId);
        RequestDetails requestDetails = new RequestDetails();
        MsoResponseWrapper expectedResponse = createOkResponse();
        given(msoInterface.deleteSvcInstance(requestDetails, svcEndpoint)).willReturn(expectedResponse);

        // when
        MsoResponseWrapper msoResponseWrapper = msoBusinessLogic
                .deleteSvcInstance(requestDetails, serviceInstanceId, status);

        // then
        assertThat(msoResponseWrapper).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test(dataProvider = "deleteSvcInstanceShouldUnassign")
    public void shouldProperlyDeleteSvcInstanceWithProperParametersShouldUnassign(String status) {
        // given
        String endpointTemplate = "/serviceInstantiation/v7/serviceInstances/%s/unassign";
        String serviceInstanceId = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
        String svcEndpoint = String.format(endpointTemplate, serviceInstanceId);
        RequestDetails requestDetails = new RequestDetails();
        MsoResponseWrapper expectedResponse = createOkResponse();
        given(msoInterface.unassignSvcInstance(requestDetails, svcEndpoint)).willReturn(expectedResponse);

        // when
        MsoResponseWrapper msoResponseWrapper = msoBusinessLogic
                .deleteSvcInstance(requestDetails, serviceInstanceId, status);

        // then
        assertThat(msoResponseWrapper).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyDeleteVnfWithProperParameters() {
        // when
        String endpointTemplate = "/serviceInstantiation/v7/serviceInstances/%s/vnfs/%s";
        String serviceInstanceId = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
        String vnfInstanceId = "testVnfInstanceTempId";
        String vnfEndpoint = String.format(endpointTemplate, serviceInstanceId, vnfInstanceId);
        RequestDetails requestDetails = new RequestDetails();
        MsoResponseWrapper expectedResponse = createOkResponse();
        given(msoInterface.deleteVnf(requestDetails, vnfEndpoint)).willReturn(expectedResponse);

        // when
        MsoResponseWrapper msoResponseWrapper = msoBusinessLogic
                .deleteVnf(requestDetails, serviceInstanceId, vnfInstanceId);

        // then
        assertThat(msoResponseWrapper).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyDeleteVfModuleWithProperParameters() {
        // when
        String endpointTemplate = "/serviceInstantiation/v7/serviceInstances/%s/vnfs/%s/vfModules/%s";
        String serviceInstanceId = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
        String vnfInstanceId = "testVnfInstanceTempId";
        String vfModuleId = "testVfModuleId";
        String vnfEndpoint = String.format(endpointTemplate, serviceInstanceId, vnfInstanceId, vfModuleId);
        RequestDetails requestDetails = new RequestDetails();
        MsoResponseWrapper expectedResponse = createOkResponse();
        given(msoInterface.deleteVfModule(requestDetails, vnfEndpoint)).willReturn(expectedResponse);

        // when
        MsoResponseWrapper msoResponseWrapper = msoBusinessLogic
                .deleteVfModule(requestDetails, serviceInstanceId, vnfInstanceId, vfModuleId);
        // then
        assertThat(msoResponseWrapper).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyDeleteVolumeGroupInstanceWithProperParameters() {
        MsoResponseWrapper expectedResponse = createOkResponse();
        String serviceInstanceId = "testServiceInstanceTempId";
        String vnfInstanceId = "testVnfInstanceTempId";
        String volumeGroupId = "testvolumeGroupIdTempId";

        String deleteVolumeGroupEndpoint = getDeleteVolumeGroupEndpoint(serviceInstanceId, vnfInstanceId, volumeGroupId);

        given(msoInterface.deleteVolumeGroupInstance(msoRequest, deleteVolumeGroupEndpoint)).willReturn(expectedResponse);

        MsoResponseWrapper response = msoBusinessLogic.deleteVolumeGroupInstance(msoRequest, serviceInstanceId, vnfInstanceId, volumeGroupId);

        // then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @NotNull
    private String getDeleteVolumeGroupEndpoint(String serviceInstanceId, String vnfInstanceId, String volumeGroupId) {
        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VOLUME_GROUP_INSTANCE);
        String svc_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        String vnfEndpoint = svc_endpoint.replaceFirst(VNF_INSTANCE_ID, vnfInstanceId);
        return vnfEndpoint + "/" + volumeGroupId;
    }

    @Test
    public void shouldProperlyDeleteNwInstanceWithProperParameters() {
        MsoResponseWrapper expectedResponse = createOkResponse();
        String serviceInstanceId = "testServiceInstanceTempId";
        String networkInstanceId = "testNetworkInstanceTempId";

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_NETWORK_INSTANCE);
        String svc_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        String delete_nw_endpoint = svc_endpoint + "/" + networkInstanceId;

        given(msoInterface.deleteNwInstance(msoRequest, delete_nw_endpoint)).willReturn(expectedResponse);

        MsoResponseWrapper response = msoBusinessLogic.deleteNwInstance(msoRequest, serviceInstanceId, networkInstanceId);

        // then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyGetOrchestrationRequestWithProperParameters() {
        MsoResponseWrapper expectedResponse = createOkResponse();
        String requestId = "testRequestTempId";
        String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_GET_ORC_REQ);
        String path = p + "/" + requestId;

        given(msoInterface.getOrchestrationRequest(path)).willReturn(expectedResponse);

        MsoResponseWrapper response = msoBusinessLogic.getOrchestrationRequest(requestId);
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test(expectedExceptions = MsoTestException.class)
    public void shouldProperlyGetOrchestrationRequestWithWrongParameters() {
        String requestId = "testWrongRequestTempId";
        String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_GET_ORC_REQ);
        String path = p + "/" + requestId;

        given(msoInterface.getOrchestrationRequest(path)).willThrow(new MsoTestException("testException"));

        msoBusinessLogic.getOrchestrationRequest(requestId);
    }

    @Test
    public void shouldProperlyGetOrchestrationRequestsWithProperParameters() {
        MsoResponseWrapper expectedResponse = createOkResponse();
        String filterString = "testRequestsTempId";
        String url = SystemProperties.getProperty(MsoProperties.MSO_REST_API_GET_ORC_REQS);
        String path = url + filterString;

        given(msoInterface.getOrchestrationRequest(path)).willReturn(expectedResponse);

        MsoResponseWrapper response = msoBusinessLogic.getOrchestrationRequests(filterString);
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test(expectedExceptions = MsoTestException.class)
    public void shouldThrowExceptionWhenGetOrchestrationRequestsWithWrongParameters() {
        String filterString = "testRequestsTempId";
        String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_GET_ORC_REQS);
        String path = p + filterString;

        given(msoInterface.getOrchestrationRequest(path)).willThrow(new MsoTestException("testException"));

        msoBusinessLogic.getOrchestrationRequests(filterString);
    }

    @Test
    public void shouldSendProperScaleOutRequest() throws IOException {
        // given
        String serviceInstanceId = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
        String vnfInstanceId = "testVnfInstanceTempId";
        String endpointTemplate = "/serviceInstantiation/v7/serviceInstances/%s/vnfs/%s/vfModules/scaleOut";
        String vnfEndpoint = String.format(endpointTemplate, serviceInstanceId, vnfInstanceId);
        org.onap.vid.changeManagement.RequestDetails requestDetails = readRequest();
        org.onap.vid.changeManagement.RequestDetailsWrapper<org.onap.vid.changeManagement.RequestDetails> expectedRequest = readExpectedRequest();
        MsoResponseWrapper expectedMsoResponseWrapper = createOkResponse();
        given(
                msoInterface
                        .scaleOutVFModuleInstance(argThat(new MsoRequestWrapperMatcher(expectedRequest)),
                                eq(vnfEndpoint)))
                .willReturn(expectedMsoResponseWrapper);

        // when
        MsoResponseWrapper msoResponseWrapper = msoBusinessLogic
                .scaleOutVfModuleInstance(requestDetails, serviceInstanceId, vnfInstanceId);

        // then
        assertThat(msoResponseWrapper).isEqualToComparingFieldByField(expectedMsoResponseWrapper);
    }

    private org.onap.vid.changeManagement.RequestDetails readRequest() throws IOException {
        Path path = Paths.get("payload_jsons", "scaleOutVfModulePayload.json");
        URL url = this.getClass().getClassLoader().getResource(path.toString());
        return objectMapper.readValue(url, org.onap.vid.changeManagement.RequestDetails.class);
    }

    private org.onap.vid.changeManagement.RequestDetailsWrapper<org.onap.vid.changeManagement.RequestDetails> readExpectedRequest()
            throws IOException {
        Path path = Paths.get("payload_jsons", "scaleOutVfModulePayloadToMso.json");
        URL url = this.getClass().getClassLoader().getResource(path.toString());
        return objectMapper.readValue(url,
                new TypeReference<org.onap.vid.changeManagement.RequestDetailsWrapper<org.onap.vid.changeManagement.RequestDetails>>() {
                });
    }


    @Test
    public void shouldFilterOutOrchestrationRequestsNotAllowedInDashboard() throws Exception {
        //given
        String vnfModelTypeOrchestrationRequests = getFileContentAsString("mso_model_info_sample_response.json");
        String scaleOutActionOrchestrationRequests = getFileContentAsString("mso_action_scaleout_sample_response.json");

        HttpResponse<String> httpResponse = mockForGetOrchestrationRequest();
        given(httpResponse.getBody())
                .willReturn(vnfModelTypeOrchestrationRequests, scaleOutActionOrchestrationRequests);

        //when
        List<Request> filteredOrchestrationReqs = msoBusinessLogic.getOrchestrationRequestsForDashboard();
        //then
        assertThat(filteredOrchestrationReqs).hasSize(3);
        assertThat(MsoBusinessLogicImpl.DASHBOARD_ALLOWED_TYPES)
                .containsAll(filteredOrchestrationReqs
                        .stream()
                        .map(el -> el.getRequestType().toUpperCase())
                        .collect(Collectors.toList()));
        assertThat(filteredOrchestrationReqs)
                .extracting(Request::getRequestScope)
                .containsOnly("vnf", "vfModule");
    }

    @Test(expectedExceptions = GenericUncheckedException.class)
    public void shouldThrowGenericUncheckedExceptionWhenGetOrchestrationRequestsForDashboardWithWrongJsonFile_() throws Exception {
        //given
        String vnfModelTypeOrchestrationRequests = getFileContentAsString("mso_model_info_sample_wrong_response.json");

        mockForGetOrchestrationRequest(200, vnfModelTypeOrchestrationRequests);

        //when
        msoBusinessLogic.getOrchestrationRequestsForDashboard();
    }

    @Test
    public void shouldProperlyGetManualTasksByRequestIdWithProperParameters() throws Exception {
        //given
        String manualTasksList = getFileContentAsString("manual_tasks_by_requestId_test.json");

        MsoResponseWrapper msoResponseWrapperMock = mock(MsoResponseWrapper.class);
        given(msoInterface
                .getManualTasksByRequestId(any(String.class), any(String.class), any(String.class),
                        any(RestObject.class)))
                .willReturn(msoResponseWrapperMock);
        given(msoResponseWrapperMock.getEntity())
                .willReturn(manualTasksList);

        //when
        List<Task> filteredOrchestrationReqs = msoBusinessLogic.getManualTasksByRequestId("TestId");

        //then
        assertThat(filteredOrchestrationReqs).hasSize(2);
        assertThat(filteredOrchestrationReqs).extracting("taskId", "type").
                contains(
                        tuple("123123abc", "testTask"),
                        tuple("321321abc", "testTask")
                );
    }

    @Test(expectedExceptions = GenericUncheckedException.class)
    public void shouldThrowGenericUncheckedExceptionWhenGetManualTasksByRequestIdWithWrongJsonFile() throws Exception {
        //given
        String manualTasksList = getFileContentAsString("manual_tasks_by_requestId_wrongJson_test.json");

        MsoResponseWrapper msoResponseWrapperMock = mock(MsoResponseWrapper.class);
        given(msoInterface
                .getManualTasksByRequestId(any(String.class), any(String.class), any(String.class),
                        any(RestObject.class)))
                .willReturn(msoResponseWrapperMock);
        given(msoResponseWrapperMock.getEntity())
                .willReturn(manualTasksList);

        //when
        msoBusinessLogic.getManualTasksByRequestId("TestId");
    }

    @Test(expectedExceptions = MsoTestException.class)
    public void getManualTasksByRequestIdWithArgument_shouldThrowException() {
        //given
        given(msoInterface
                .getManualTasksByRequestId(any(String.class), any(String.class), any(String.class),
                        any(RestObject.class)))
                .willThrow(MsoTestException.class);

        //when
        msoBusinessLogic.getManualTasksByRequestId("TestId");
    }

    @Test
    public void shouldProperlyCompleteManualTaskWithProperParameters() {
        //given
        MsoResponseWrapper expectedResponse = createOkResponse();
        RequestDetails requestDetails = new RequestDetails();
        String taskId = "testTaskId";

        String url = SystemProperties.getProperty(MsoProperties.MSO_REST_API_GET_MAN_TASKS);
        String path = url + "/" + taskId + "/complete";

        given(msoInterface.completeManualTask(eq(requestDetails), any(String.class), any(String.class), eq(path), any(RestObject.class))).willReturn(expectedResponse);

        //when
        MsoResponseWrapper response = msoBusinessLogic.completeManualTask(requestDetails, taskId);

        //then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);

    }

    @Test
    public void shouldProperlyActivateServiceInstanceWithProperParameters() {
        //given
        RequestDetails detail = new RequestDetails();
        String taskId = "testTaskId";

        RestObject<String> restObjStr = new RestObject<>();
        restObjStr.set("");
        MsoResponseWrapper expectedResponse = MsoUtil.wrapResponse(restObjStr);

        //when
        MsoResponseWrapper response = msoBusinessLogic.activateServiceInstance(detail, taskId);

        //then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);

    }

    @Test(expectedExceptions = MsoTestException.class)
    public void shouldThrowExceptionWhenManualTaskWithWrongParameters() {
        //given
        RequestDetails requestDetails = new RequestDetails();
        String taskId = "";

        String url = SystemProperties.getProperty(MsoProperties.MSO_REST_API_GET_MAN_TASKS);
        String path = url + "/" + taskId + "/complete";

        given(msoInterface.completeManualTask(eq(requestDetails), any(String.class), any(String.class), eq(path), any(RestObject.class))).willThrow(new MsoTestException("empty path"));

        //when
        msoBusinessLogic.completeManualTask(requestDetails, taskId);
    }

    @Test
    public void shouldProperlyUpdateVnfWithProperParameters() {
        //given
        MsoResponseWrapper expectedResponse = createOkResponse();
        org.onap.vid.changeManagement.RequestDetails requestDetails = new org.onap.vid.changeManagement.RequestDetails();

        String serviceInstanceId = "testServiceId";
        String vnfInstanceId = "testVnfInstanceId";

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VNF_INSTANCE);
        String vnfEndpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        vnfEndpoint = vnfEndpoint + '/' + vnfInstanceId;

        given(msoInterface.updateVnf(requestDetails, vnfEndpoint)).willReturn(expectedResponse);

        //when
        MsoResponseWrapper response = (MsoResponseWrapper) msoBusinessLogic.updateVnf(requestDetails, serviceInstanceId, vnfInstanceId);

        //then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyReplaceVnfWithProperParameters() {
        //given
        MsoResponseWrapper expectedResponse = createOkResponse();
        org.onap.vid.changeManagement.RequestDetails requestDetails = new org.onap.vid.changeManagement.RequestDetails();

        String serviceInstanceId = "testServiceId";
        String vnfInstanceId = "testVnfInstanceId";

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VNF_CHANGE_MANAGEMENT_INSTANCE);
        String vnfEndpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        vnfEndpoint = vnfEndpoint.replace(VNF_INSTANCE_ID, vnfInstanceId);
        vnfEndpoint = vnfEndpoint.replace(REQUEST_TYPE, ChangeManagementRequest.MsoChangeManagementRequest.REPLACE);

        given(msoInterface.replaceVnf(requestDetails, vnfEndpoint)).willReturn(expectedResponse);

        //when
        MsoResponseWrapper response = (MsoResponseWrapper) msoBusinessLogic.replaceVnf(requestDetails, serviceInstanceId, vnfInstanceId);

        //then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyGenerateInPlaceMsoRequestWithProperParameters() {
        //given
        org.onap.vid.changeManagement.RequestDetails requestDetails = new org.onap.vid.changeManagement.RequestDetails();

        requestDetails.setVnfInstanceId("testVnfInstanceId");
        requestDetails.setVnfName("testVnfName");
        requestDetails.setRequestParameters(new RequestParameters());

        requestDetails.getRequestParameters().setAdditionalProperty("payload", "{" +
                "\"existing_software_version\": \"testExistingSoftwareParam\"," +
                "\"new_software_version\": \"testNewSoftwareParam\"," +
                "\"operations_timeout\": \"100\"" +
                "}");

        RequestDetails inPlaceSoftwareUpdateRequest = new RequestDetails();
        inPlaceSoftwareUpdateRequest.setCloudConfiguration(requestDetails.getCloudConfiguration());
        inPlaceSoftwareUpdateRequest.setRequestParameters(requestDetails.getRequestParameters());
        inPlaceSoftwareUpdateRequest.setRequestInfo(requestDetails.getRequestInfo());
        org.onap.vid.changeManagement.RequestDetailsWrapper requestDetailsWrapper = new org.onap.vid.changeManagement.RequestDetailsWrapper();
        requestDetailsWrapper.requestDetails = inPlaceSoftwareUpdateRequest;

        //when
        org.onap.vid.changeManagement.RequestDetailsWrapper response = msoBusinessLogic.generateInPlaceMsoRequest(requestDetails);

        //then
        assertThat(response).isEqualToComparingFieldByField(requestDetailsWrapper);
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void shouldThrowExceptionWhenGenerateInPlaceMsoRequestWithParametersWithWrongCharacters() {
        //given
        org.onap.vid.changeManagement.RequestDetails requestDetails = new org.onap.vid.changeManagement.RequestDetails();

        requestDetails.setVnfInstanceId("testVnfInstanceId");
        requestDetails.setVnfName("testVnfName");
        requestDetails.setRequestParameters(new RequestParameters());

        requestDetails.getRequestParameters().setAdditionalProperty("payload", "{" +
                "\"existing_software_version\": \"#####\"," +
                "\"new_software_version\": \"testNewSoftwareParam\"" +
                "}");

        //when
        msoBusinessLogic.generateInPlaceMsoRequest(requestDetails);
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void shouldThrowExceptionWhenGenerateInPlaceMsoRequestWithWrongParameters() {
        //given
        org.onap.vid.changeManagement.RequestDetails requestDetails = new org.onap.vid.changeManagement.RequestDetails();

        requestDetails.setVnfInstanceId("testVnfInstanceId");
        requestDetails.setVnfName("testVnfName");
        requestDetails.setRequestParameters(new RequestParameters());

        requestDetails.getRequestParameters().setAdditionalProperty("payload", "{" +
                "\"test-wrong-parameter\": \"testParam\"," +
                "\"new_software_version\": \"testNewSoftwareParam\"" +
                "}");

        //when
        msoBusinessLogic.generateInPlaceMsoRequest(requestDetails);
    }

    @Test
    public void shouldProprleyGenerateConfigMsoRequestWithProperParameters() {
        //given
        org.onap.vid.changeManagement.RequestDetails requestDetails = new org.onap.vid.changeManagement.RequestDetails();

        requestDetails.setVnfInstanceId("testVnfInstanceId");
        requestDetails.setVnfName("testVnfName");
        requestDetails.setRequestParameters(new RequestParameters());

        requestDetails.getRequestParameters().setAdditionalProperty("payload", "{" +
                "\"request-parameters\": \"testRequestParam\"," +
                "\"configuration-parameters\": \"testConfigParams\"" +
                "}");

        RequestDetails configUpdateRequest = new RequestDetails();
        configUpdateRequest.setRequestParameters(requestDetails.getRequestParameters());
        configUpdateRequest.setRequestInfo(requestDetails.getRequestInfo());

        org.onap.vid.changeManagement.RequestDetailsWrapper requestDetailsWrapper = new org.onap.vid.changeManagement.RequestDetailsWrapper();
        requestDetailsWrapper.requestDetails = configUpdateRequest;

        //when
        org.onap.vid.changeManagement.RequestDetailsWrapper response = msoBusinessLogic.generateConfigMsoRequest(requestDetails);

        //then
        assertThat(response).isEqualToComparingFieldByField(requestDetailsWrapper);
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void shouldThrowExceptionGenerateConfigMsoRequestWithoutAdditionalParameters() {
        //given
        org.onap.vid.changeManagement.RequestDetails requestDetails = new org.onap.vid.changeManagement.RequestDetails();

        requestDetails.setVnfInstanceId("testVnfInstanceId");
        requestDetails.setVnfName("testVnfName");
        requestDetails.setRequestParameters(null);

        //when
        msoBusinessLogic.generateConfigMsoRequest(requestDetails);
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void shouldThrowExceptionWhenGenerateConfigMsoRequestWithWrongPayload() {
        //given
        org.onap.vid.changeManagement.RequestDetails requestDetails = new org.onap.vid.changeManagement.RequestDetails();

        requestDetails.setVnfInstanceId("testVnfInstanceId");
        requestDetails.setVnfName("testVnfName");
        requestDetails.setRequestParameters(new RequestParameters());

        requestDetails.getRequestParameters().setAdditionalProperty("payload", null);

        //when
        msoBusinessLogic.generateConfigMsoRequest(requestDetails);
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void shouldThrowExceptionGenerateConfigMsoRequestWithoutAnyParameter() {
        //given
        org.onap.vid.changeManagement.RequestDetails requestDetails = new org.onap.vid.changeManagement.RequestDetails();

        requestDetails.setVnfInstanceId("testVnfInstanceId");
        requestDetails.setVnfName("testVnfName");
        requestDetails.setRequestParameters(new RequestParameters());

        requestDetails.getRequestParameters().setAdditionalProperty("payload", "{" +
                "\"test-wrong-parameter\": \"testParam\"," +
                "\"configuration-parameters\": \"testConfigParam\"" +
                "}");

        //when
        msoBusinessLogic.generateConfigMsoRequest(requestDetails);
    }

    @Test
    public void shouldProperlyGetActivateFabricConfigurationPathWithProperParameters() {
        // given
        String serviceInstanceId = "testServiceId";
        String path = validateEndpointPath(MsoProperties.MSO_RESTAPI_SERVICE_INSTANCE);
        path += "/" + serviceInstanceId + "/activateFabricConfiguration";

        // when
        String response = msoBusinessLogic.getActivateFabricConfigurationPath(serviceInstanceId);

        // then
        assertThat(response).isEqualTo(path);
    }

    @Test
    public void shouldProperlyGetDeactivateAndCloudDeletePathWithProperParameters() {

        // when
        String response = msoBusinessLogic.getDeactivateAndCloudDeletePath("testServiceId", "testVnfInstanceId", "testVfModuleInstanceId");

        // then
        String expectedPath = "/serviceInstantiation/v7/serviceInstances/testServiceId/vnfs/testVnfInstanceId/"+
            "vfModules/testVfModuleInstanceId/deactivateAndCloudDelete";
        assertThat(response).isEqualTo(expectedPath);
    }

    @Test
    public void shouldProperlyBuildRequestDetailsForSoftDeleteWithProperParameters() throws IOException {
        SoftDeleteRequest request = new SoftDeleteRequest("testTenantId", "testCloudId", "testUserID");
        String expected = IOUtils.toString(this.getClass().getResource("/payload_jsons/vfModuleDeactivateAndCloudDelete.json"), "UTF-8");
        final RequestDetails result = msoBusinessLogic.buildRequestDetailsForSoftDelete(request);
        MatcherAssert.assertThat(result, jsonEquals(expected));
    }

    private void setRequestParametersForRequestDetails(RequestDetails requestDetails) {
        RequestParameters requestParameters = new RequestParameters();
        requestParameters.setTestApi("GR_API");
        requestDetails.setRequestParameters(requestParameters);
    }

    private void setModelInfoForRequestDetails(RequestDetails requestDetails) {
        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setModelType("vfModule");
        requestDetails.setModelInfo(modelInfo);
    }

    @Test
    public void shouldProperlyUpdateVnfSoftwareWithProperParameters() {
        //  given
        String serviceInstanceId = "testServiceId";
        String vnfInstanceId = "testVnfInstanceId";

        org.onap.vid.changeManagement.RequestDetails requestDetails = new org.onap.vid.changeManagement.RequestDetails();

        requestDetails.setVnfInstanceId("testVnfInstanceId");
        requestDetails.setVnfName("testVnfName");
        requestDetails.setRequestParameters(new RequestParameters());

        requestDetails.getRequestParameters().setAdditionalProperty("payload", "{" +
                "\"existing_software_version\": \"testExistingSoftwareParam\"," +
                "\"new_software_version\": \"testNewSoftwareParam\"," +
                "\"operations_timeout\": \"100\"" +
                "}");

        MsoResponseWrapper okResponse = createOkResponse();

        given(msoInterface.changeManagementUpdate(isA(org.onap.vid.changeManagement.RequestDetailsWrapper.class), any(String.class))).willReturn(okResponse);

        //  when
        MsoResponseWrapper response = (MsoResponseWrapper) msoBusinessLogic.updateVnfSoftware(requestDetails, serviceInstanceId, vnfInstanceId);

        //  then
        assertThat(response).isEqualToComparingFieldByField(okResponse);
    }

    @Test
    public void shouldProperlyUpdateVnfConfigWithProperParameters() {
        //  given
        String serviceInstanceId = "testServiceId";
        String vnfInstanceId = "testVnfInstanceId";

        org.onap.vid.changeManagement.RequestDetails requestDetails = new org.onap.vid.changeManagement.RequestDetails();

        requestDetails.setVnfInstanceId("testVnfInstanceId");
        requestDetails.setVnfName("testVnfName");
        requestDetails.setRequestParameters(new RequestParameters());

        requestDetails.getRequestParameters().setAdditionalProperty("payload", "{" +
                "\"request-parameters\": \"testRequestParam\"," +
                "\"configuration-parameters\": \"testConfigParams\"" +
                "}");

        MsoResponseWrapper okResponse = createOkResponse();

        given(msoInterface.changeManagementUpdate(isA(org.onap.vid.changeManagement.RequestDetailsWrapper.class), any(String.class))).willReturn(okResponse);

        //  when
        MsoResponseWrapper response = (MsoResponseWrapper) msoBusinessLogic.updateVnfConfig(requestDetails, serviceInstanceId, vnfInstanceId);

        //  then
        assertThat(response).isEqualToComparingFieldByField(okResponse);
    }

    @Test
    public void shouldProperlyDeleteConfigurationWithProperParameters() {
        //  given
        String serviceInstanceId = "testServiceId";
        String configurationId = "testConfigurationId";

        org.onap.vid.changeManagement.RequestDetails requestDetails = new org.onap.vid.changeManagement.RequestDetails();

        requestDetails.setVnfInstanceId("testVnfInstanceId");
        requestDetails.setVnfName("testVnfName");

        MsoResponseWrapper okResponse = createOkResponse();
        RequestDetailsWrapper wrappedRequestDetail = new RequestDetailsWrapper(requestDetails);

        given(msoInterface.deleteConfiguration(eq(wrappedRequestDetail), any(String.class))).willReturn(okResponse);

        //  when
        MsoResponseWrapper response = msoBusinessLogic.deleteConfiguration(wrappedRequestDetail, serviceInstanceId, configurationId);

        //  then
        assertThat(response).isEqualToComparingFieldByField(okResponse);
    }

    @Test
    public void shouldProperlySetConfigurationActiveStatusActiveWithProperParameters() {
        //  given
        String serviceInstanceId = "testServiceId";
        String configurationId = "testConfigurationId";

        org.onap.vid.changeManagement.RequestDetails requestDetails = new org.onap.vid.changeManagement.RequestDetails();

        requestDetails.setVnfInstanceId("testVnfInstanceId");
        requestDetails.setVnfName("testVnfName");

        MsoResponseWrapper okResponse = createOkResponse();

        String endpoint =
                validateEndpointPath(MsoProperties.MSO_REST_API_CONFIGURATION_INSTANCE)
                        .replace(SVC_INSTANCE_ID, serviceInstanceId)
                        .replace(CONFIGURATION_ID, configurationId)
                        + "/activate";

        given(msoInterface.setConfigurationActiveStatus(eq(requestDetails), eq(endpoint))).willReturn(okResponse);

        //  when
        MsoResponseWrapper response = msoBusinessLogic.setConfigurationActiveStatus(requestDetails, serviceInstanceId, configurationId, true);

        //  then
        assertThat(response).isEqualToComparingFieldByField(okResponse);
    }

    @Test
    public void shouldProperlySetConfigurationActiveStatusDeactivateWithProperParameters() {
        //  given
        String serviceInstanceId = "testServiceId";
        String configurationId = "testConfigurationId";

        org.onap.vid.changeManagement.RequestDetails requestDetails = new org.onap.vid.changeManagement.RequestDetails();

        requestDetails.setVnfInstanceId("testVnfInstanceId");
        requestDetails.setVnfName("testVnfName");

        MsoResponseWrapper okResponse = createOkResponse();

        String endpoint =
                validateEndpointPath(MsoProperties.MSO_REST_API_CONFIGURATION_INSTANCE)
                        .replace(SVC_INSTANCE_ID, serviceInstanceId)
                        .replace(CONFIGURATION_ID, configurationId)
                        + "/deactivate";

        given(msoInterface.setConfigurationActiveStatus(eq(requestDetails), eq(endpoint))).willReturn(okResponse);

        //  when
        MsoResponseWrapper response = msoBusinessLogic.setConfigurationActiveStatus(requestDetails, serviceInstanceId, configurationId, false);

        //  then
        assertThat(response).isEqualToComparingFieldByField(okResponse);
    }

    @Test
    public void shouldProperlySetServiceInstanceStatusActiveWithProperParameters() {
        //  given
        String serviceInstanceId = "testServiceId";
        MsoResponseWrapper okResponse = createOkResponse();

        org.onap.vid.changeManagement.RequestDetails requestDetails = new org.onap.vid.changeManagement.RequestDetails();

        given(msoInterface.setServiceInstanceStatus(eq(requestDetails), endsWith(serviceInstanceId + "/activate"))).willReturn(okResponse);

        //  when
        MsoResponseWrapper response = msoBusinessLogic.setServiceInstanceStatus(requestDetails, serviceInstanceId, true);

        //  then
        assertThat(response).isEqualToComparingFieldByField(okResponse);
    }

    @Test
    public void shouldProperlySetServiceInstanceStatusDeactivateWithProperParameters() {
        //  given
        String serviceInstanceId = "testServiceId";
        MsoResponseWrapper okResponse = createOkResponse();

        org.onap.vid.changeManagement.RequestDetails requestDetails = new org.onap.vid.changeManagement.RequestDetails();

        given(msoInterface.setServiceInstanceStatus(eq(requestDetails), endsWith(serviceInstanceId + "/deactivate"))).willReturn(okResponse);

        //  when
        MsoResponseWrapper response = msoBusinessLogic.setServiceInstanceStatus(requestDetails, serviceInstanceId, false);

        //  then
        assertThat(response).isEqualToComparingFieldByField(okResponse);
    }

    @Test(expectedExceptions = MsoTestException.class)
    public void shouldThrowExceptionWhenSetServiceInstanceStatusWithWrongParameters() {
        //  given
        String serviceInstanceId = "testServiceId";

        doThrow(new MsoTestException("testException")).
                when(msoInterface).setServiceInstanceStatus(eq(null), any(String.class));

        //  when
        msoBusinessLogic.setServiceInstanceStatus(null, serviceInstanceId, true);
    }

    @Test
    public void shouldProperlySetPortOnConfigurationStatusEnableWithProperParameters() {
        //  given
        String serviceInstanceId = "testServiceId";
        String configurationId = "testConfigurationId";
        org.onap.vid.changeManagement.RequestDetails requestDetails = new org.onap.vid.changeManagement.RequestDetails();
        requestDetails.setVnfInstanceId("testVnfInstanceId");
        requestDetails.setVnfName("testVnfName");
        MsoResponseWrapper okResponse = createOkResponse();

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_CONFIGURATION_INSTANCE)
                .replace(SVC_INSTANCE_ID, serviceInstanceId)
                .replace(CONFIGURATION_ID, configurationId)
                + "/enablePort";

        given(msoInterface.setPortOnConfigurationStatus(eq(requestDetails), eq(endpoint))).willReturn(okResponse);

        //  when
        MsoResponseWrapper response = msoBusinessLogic.setPortOnConfigurationStatus(requestDetails, serviceInstanceId, configurationId, true);

        //  then
        assertThat(response).isEqualToComparingFieldByField(okResponse);
    }

    @Test
    public void shouldProperlySetPortOnConfigurationStatusDisableWithProperParameters() {
        //  given
        String serviceInstanceId = "testServiceId";
        String configurationId = "testConfigurationId";
        org.onap.vid.changeManagement.RequestDetails requestDetails = new org.onap.vid.changeManagement.RequestDetails();
        requestDetails.setVnfInstanceId("testVnfInstanceId");
        requestDetails.setVnfName("testVnfName");
        MsoResponseWrapper okResponse = createOkResponse();

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_CONFIGURATION_INSTANCE)
                .replace(SVC_INSTANCE_ID, serviceInstanceId)
                .replace(CONFIGURATION_ID, configurationId)
                + "/disablePort";

        given(msoInterface.setPortOnConfigurationStatus(eq(requestDetails), eq(endpoint))).willReturn(okResponse);

        //  when
        MsoResponseWrapper response = msoBusinessLogic.setPortOnConfigurationStatus(requestDetails, serviceInstanceId, configurationId, false);

        //  then
        assertThat(response).isEqualToComparingFieldByField(okResponse);
    }

    @Test
    public void shouldProperlyCreateOperationalEnvironmentActivationRequestDetailsWithProperParameters() {
        //  given
        OperationalEnvironmentActivateInfo details = createTestOperationalEnvironmentActivateInfo();
        //  when
        org.onap.vid.changeManagement.RequestDetailsWrapper<RequestDetails> requestDetails = msoBusinessLogic.createOperationalEnvironmentActivationRequestDetails(details);

        //  then
        assertThat(requestDetails.requestDetails.getRequestParameters().getAdditionalProperties().values()).contains(details.getWorkloadContext(), details.getManifest());
        assertThat(requestDetails.requestDetails.getRequestInfo().getRequestorId()).isEqualTo(userId);
    }

    @Test
    public void shouldProperlyGetOperationalEnvironmentActivationPathWithProperParameters() {
        // given
        OperationalEnvironmentActivateInfo details = createTestOperationalEnvironmentActivateInfo();

        // when
        String response = msoBusinessLogic.getOperationalEnvironmentActivationPath(details);

        // then
        assertThat(response).contains(operationalEnvironmentId);
    }

    @Test
    public void shouldProperlyCreateOperationalEnvironmentDeactivationRequestDetailsWithProperParameters() {
        // given
        OperationalEnvironmentDeactivateInfo details = createTestOperationalEnvironmentDeactivateInfo();

        // when
        org.onap.vid.changeManagement.RequestDetailsWrapper<RequestDetails> response;
        response = msoBusinessLogic.createOperationalEnvironmentDeactivationRequestDetails(details);

        // then
        assertThat(response.requestDetails.getRequestInfo().getRequestorId()).isEqualTo(userId);
    }

    @Test
    public void shouldProperlyGetCloudResourcesRequestsStatusPathWithProperParameters() {
        // given
        String requestId = "testRequestId";

        // when
        String response = msoBusinessLogic.getCloudResourcesRequestsStatusPath(requestId);

        // then
        assertThat(response).contains(requestId);
    }

    @Test
    public void shouldProperlyGetOperationalEnvironmentDeactivationPathWithProperParameters() {
        // given
        OperationalEnvironmentDeactivateInfo details = createTestOperationalEnvironmentDeactivateInfo();

        // when
        String response = msoBusinessLogic.getOperationalEnvironmentDeactivationPath(details);

        // then
        assertThat(response).contains(operationalEnvironmentId);
    }

    @Test
    public void shouldProperlyGetOperationalEnvironmentCreationPathWithProperParameters() {
        // when
        String response = msoBusinessLogic.getOperationalEnvironmentCreationPath();

        // then
        assertThat(response).isNotBlank();
    }

    @Test
    public void shouldProperlyConvertParametersToRequestDetailsWithProperParameters() {
        // given
        OperationalEnvironmentController.OperationalEnvironmentCreateBody input = createTestOperationalEnvironmentCreateBody();

        // when
        org.onap.vid.changeManagement.RequestDetailsWrapper<OperationEnvironmentRequestDetails> response
                = msoBusinessLogic.convertParametersToRequestDetails(input, userId);

        // then
        assertThat(response.requestDetails.getRequestInfo().getInstanceName()).isEqualTo(input.getInstanceName());
        assertThat(response.requestDetails.getRequestInfo().getRequestorId()).isEqualTo(userId);
        assertThat(response.requestDetails.getRequestParameters().getOperationalEnvironmentType()).isEqualTo(input.getOperationalEnvironmentType());
        assertThat(response.requestDetails.getRequestParameters().getTenantContext()).isEqualTo(input.getTenantContext());
        assertThat(response.requestDetails.getRequestParameters().getWorkloadContext()).isEqualTo(input.getWorkloadContext());
    }

    @Test
    public void shouldProperlyRemoveRelationshipFromServiceInstanceWithProperParameters() {
        // given
        MsoResponseWrapper expectedResponse = createOkResponse();
        String serviceInstanceId = "testServiceId";
        org.onap.vid.changeManagement.RequestDetails requestDetails = new org.onap.vid.changeManagement.RequestDetails();

        given(msoInterface.removeRelationshipFromServiceInstance(eq(requestDetails), endsWith("/" + serviceInstanceId + "/removeRelationships")))
                .willReturn(expectedResponse);

        // when
        MsoResponseWrapper response = msoBusinessLogic.removeRelationshipFromServiceInstance(requestDetails, serviceInstanceId);

        // then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyAddRelationshipToServiceInstanceWithProperParameters() {
        // given
        MsoResponseWrapper expectedResponse = createOkResponse();
        String serviceInstanceId = "testServiceId";
        org.onap.vid.changeManagement.RequestDetails requestDetails = new org.onap.vid.changeManagement.RequestDetails();

        given(msoInterface.addRelationshipToServiceInstance(eq(requestDetails), endsWith("/" + serviceInstanceId + "/addRelationships")))
                .willReturn(expectedResponse);

        // when
        MsoResponseWrapper response = msoBusinessLogic.addRelationshipToServiceInstance(requestDetails, serviceInstanceId);

        // then
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldProperlyRequestTypeFromValueWithValidParameters() {
        // given
        String testValue = "createInstance";
        // when
        MsoBusinessLogicImpl.RequestType response = MsoBusinessLogicImpl.RequestType.fromValue(testValue);

        // then
        assertThat(response.toString()).isEqualTo(testValue);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenRequestTypeFromValueWithWrongParameter() {
        // given
        String testValue = "notExistingParameter";
        // when
        MsoBusinessLogicImpl.RequestType.fromValue(testValue);
    }

    @Test
    public void shouldProperlyInvokeVnfWorkflowWithValidParameters() {
        // given
        MsoResponseWrapper okResponse = createOkResponse();
        WorkflowRequestDetail request = createWorkflowRequestDetail();
        UUID serviceInstanceId = new UUID(1,10);
        UUID vnfInstanceId = new UUID(2,20);
        UUID workflow_UUID = new UUID(3,30);
        String path = "/instanceManagement/v1/serviceInstances/"+serviceInstanceId+"/vnfs/"+vnfInstanceId+"/workflows/"+workflow_UUID;

        given(msoInterface.invokeWorkflow(eq(request), eq(path), MockitoHamcrest.argThat(allOf(hasEntry("X-RequestorID", "testRequester"),hasEntry("X-ONAP-PartnerName", "VID.VID"))))).willReturn(okResponse);

        // when
        MsoResponseWrapper response = msoBusinessLogic.invokeVnfWorkflow(request, "testRequester", serviceInstanceId, vnfInstanceId, workflow_UUID);

        // then
        assertThat(response).isEqualToComparingFieldByField(okResponse);
    }


    @Test
    public void shouldReturnWorkflowListForGivenModelId() {
        given(msoInterface.getWorkflowListByModelId(anyString())).willReturn(workflowListResponse);
        given(workflowListResponse.getBody()).willReturn(workflowList);
        given(workflowListResponse.getStatus()).willReturn(HttpStatus.ACCEPTED.value());

        SOWorkflowList workflows = msoBusinessLogic.getWorkflowListByModelId("sampleModelId");

        assertThat(workflows).isEqualTo(workflowList);
    }

    @Test(expectedExceptions = {MsoBusinessLogicImpl.WorkflowListException.class})
    public void shouldRaiseExceptionWhenRetrievingWorkflowsFailed() {
        given(msoInterface.getWorkflowListByModelId(anyString())).willReturn(workflowListResponse);
        given(workflowListResponse.getStatus()).willReturn(HttpStatus.INTERNAL_SERVER_ERROR.value());

        msoBusinessLogic.getWorkflowListByModelId("sampleModelId");
    }


    @Test
    public void probeShouldReturnOrchestrationRequestsAndConnectionStatus(){
        String body =
                "{"
                + "  \"requestList\":"
                + "  [{"
                + "      \"request\": {}"
                + "    }"
                + "  ]"
                + "}";
        mockForGetOrchestrationRequest(200, body);

        ExternalComponentStatus externalComponentStatus = msoBusinessLogic.probeComponent();

        assertThat(externalComponentStatus.isAvailable()).isTrue();
        assertThat(externalComponentStatus.getComponent()).isEqualTo(ExternalComponentStatus.Component.MSO);
    }

    private WorkflowRequestDetail createWorkflowRequestDetail() {
        WorkflowRequestDetail workflowRequestDetail = new WorkflowRequestDetail();
        org.onap.vid.changeManagement.RequestParameters requestParameters = new org.onap.vid.changeManagement.RequestParameters();
        HashMap<String,String> paramsMap = new HashMap<>();
        paramsMap.put("testKey1","testValue1");
        paramsMap.put("testKey2","testValue2");

        List<Map<String,String>> mapArray= new ArrayList<>();
        mapArray.add(paramsMap);
        requestParameters.setUserParams(mapArray);

        CloudConfiguration cloudConfiguration = new CloudConfiguration();
        cloudConfiguration.setCloudOwner("testOwne");
        cloudConfiguration.setTenantId("testId");
        cloudConfiguration.setLcpCloudRegionId("testLcpCloudId");

        workflowRequestDetail.setRequestParameters(requestParameters);
        workflowRequestDetail.setCloudConfiguration(cloudConfiguration);
        return workflowRequestDetail;
    }

    private OperationalEnvironmentActivateInfo createTestOperationalEnvironmentActivateInfo() {
        OperationalEnvironmentController.OperationalEnvironmentActivateBody operationalEnvironmentActivateBody = new OperationalEnvironmentController.OperationalEnvironmentActivateBody(
                "testRelatedInstanceId",
                "testRelatedInstanceName",
                "testWorkloadContext",
                new OperationalEnvironmentController.OperationalEnvironmentManifest()
        );
        return new OperationalEnvironmentActivateInfo(operationalEnvironmentActivateBody, userId, operationalEnvironmentId);
    }

    private OperationalEnvironmentDeactivateInfo createTestOperationalEnvironmentDeactivateInfo() {
        return new OperationalEnvironmentDeactivateInfo(userId, operationalEnvironmentId);
    }

    private OperationalEnvironmentController.OperationalEnvironmentCreateBody createTestOperationalEnvironmentCreateBody() {
        return new OperationalEnvironmentController.OperationalEnvironmentCreateBody(
                "testInstanceName",
                "testEcompInstanceId",
                "testEcompInstanceName",
                "testOperationalEnvironmentType",
                "testTenantContext",
                "testWorkloadContext"
        );
    }

    private MsoResponseWrapper createOkResponse() {
        HttpStatus expectedStatus = HttpStatus.ACCEPTED;
        String expectedBody = " \"body\": {\n" +
                "      \"requestReferences\": {\n" +
                "        \"instanceId\": \" 123456 \",\n" +
                "        \"requestId\": \"b6dc9806-b094-42f7-9386-a48de8218ce8\"\n" +
                "      }";
        MsoResponseWrapper responseWrapper = new MsoResponseWrapper();
        responseWrapper.setEntity(expectedBody);
        responseWrapper.setStatus(expectedStatus.value());
        return responseWrapper;
    }

    private String getFileContentAsString(String resourceName) throws Exception {
        Path path = Paths.get("payload_jsons", resourceName);
        URL url = this.getClass().getClassLoader().getResource(path.toString());
        String result = "";
        if (url != null) {
            result = IOUtils.toString(url.toURI(), "UTF-8");
        }
        return result;
    }

    private static class MsoRequestWrapperMatcher implements
            ArgumentMatcher<org.onap.vid.changeManagement.RequestDetailsWrapper> {

        private final org.onap.vid.changeManagement.RequestDetailsWrapper expectedRequest;

        MsoRequestWrapperMatcher(org.onap.vid.changeManagement.RequestDetailsWrapper expectedRequest) {
            this.expectedRequest = expectedRequest;
        }

        @Override
        public boolean matches(org.onap.vid.changeManagement.RequestDetailsWrapper argument) {
            return expectedRequest.requestDetails.equals(argument.requestDetails);
        }
    }

    private class MsoTestException extends RuntimeException {
        MsoTestException(String testException) {
            super(testException);
        }
    }

    //you need to add mocks to httpResponse
    private HttpResponse<String> mockForGetOrchestrationRequest() {

        HttpResponse<String> httpResponse = mock(HttpResponse.class);
        HttpResponseWithRequestInfo<String> httpResponseWithRequestInfo = new HttpResponseWithRequestInfo<>(httpResponse, MY_PRETTY_URL, HttpMethod.GET);
        when(msoInterface.getOrchestrationRequest(any(String.class),anyBoolean()))
            .thenReturn(httpResponseWithRequestInfo);
        return httpResponse;
    }

    private HttpResponse<String> mockForGetOrchestrationRequest(int statusCode, String body) {

        HttpResponse<String> httpResponse = mockForGetOrchestrationRequest();
        when(httpResponse.getStatus()).thenReturn(statusCode);
        when(httpResponse.getBody()).thenReturn(body);
        TestUtils.mockGetRawBodyWithStringBody(httpResponse, body);
        return httpResponse;
    }

    @Test
    public void probeComponent_verifyGoodRequest(){
        String responseString = "" +
            "{ " +
            " \"requestList\": [{ " +
            "   \"request\": { " +
            "    \"requestDetails\": { " +
            "     \"cloudConfiguration\": { " +
            "      \"lcpCloudRegionId\": \"hvf6\", " +
            "      \"cloudOwner\": \"irma-aic\", " +
            "      \"tenantId\": \"ffdf52b5e5104b0e8f329b0b1637ee2e\" " +
            "     }, " +
            "     \"modelInfo\": { " +
            "      \"modelCustomizationName\": \"VSP1710PID298109_vWINIFRED 0\", " +
            "      \"modelCustomizationId\": \"24d43fdb-9aa6-4287-a68e-1e702ea89d13\", " +
            "      \"modelInvariantId\": \"e7961100-cde6-4b5a-bcda-b8945086950a\", " +
            "      \"modelVersionId\": \"959a7ba0-89ee-4984-9af6-65d5bdda4b0e\", " +
            "      \"modelName\": \"VSP1710PID298109_vWINIFRED\", " +
            "      \"modelType\": \"vnf\", " +
            "      \"modelVersion\": \"1.0\" " +
            "     }, " +
            "     \"relatedModelList\": [{ " +
            "       \"relatedInstance\": { " +
            "        \"instanceId\": \"6dd0f8de-93c7-48a2-914b-1a8d58e0eb48\", " +
            "        \"modelInfo\": { " +
            "         \"modelInvariantId\": \"57e00952-0af7-4f0f-b19a-408a6f73c8df\", " +
            "         \"modelType\": \"service\", " +
            "         \"modelName\": \"ServicevWINIFREDPID298109\", " +
            "         \"modelVersion\": \"1.0\", " +
            "         \"modelVersionId\": \"fe6985cd-ea33-3346-ac12-ab121484a3fe\" " +
            "        } " +
            "       } " +
            "      } " +
            "     ], " +
            "     \"requestInfo\": { " +
            "      \"source\": \"VID\", " +
            "      \"suppressRollback\": false, " +
            "      \"requestorId\": \"ds828e\" " +
            "     }, " +
            "     \"requestParameters\": { " +
            "      \"userParams\": [ " +
            "      ], " +
            "      \"aLaCarte\": false, " +
            "      \"usePreload\": true, " +
            "      \"rebuildVolumeGroups\": false, " +
            "      \"autoBuildVfModules\": false, " +
            "      \"cascadeDelete\": false " +
            "     }, " +
            "     \"relatedInstanceList\": [{ " +
            "       \"relatedInstance\": { " +
            "        \"instanceId\": \"6dd0f8de-93c7-48a2-914b-1a8d58e0eb48\", " +
            "        \"modelInfo\": { " +
            "         \"modelInvariantId\": \"57e00952-0af7-4f0f-b19a-408a6f73c8df\", " +
            "         \"modelType\": \"service\", " +
            "         \"modelName\": \"ServicevWINIFREDPID298109\", " +
            "         \"modelVersion\": \"1.0\", " +
            "         \"modelVersionId\": \"fe6985cd-ea33-3346-ac12-ab121484a3fe\" " +
            "        } " +
            "       } " +
            "      } " +
            "     ] " +
            "    }, " +
            "    \"requestId\": \"d352c70d-5ef8-4977-9ea8-4c8cbe860422\", " +
            "    \"requestScope\": \"vnf\", " +
            "    \"requestStatus\": { " +
            "     \"percentProgress\": 100.0, " +
            "     \"requestState\": \"Some Unknown Value\", " +
            "     \"statusMessage\": \"Update Is In Progress\", " +
            "     \"finishTime\": \"Fri, 08 Sep 2017 19:34:33 GMT\" " +
            "    }, " +
            "    \"requestType\": \"updateInstance\", " +
            "    \"startTime\": \"<IN_PROGRESS_DATE>\", " +
            "    \"instanceReferences\": { " +
            "     \"serviceInstanceId\": \"6dd0f8de-93c7-48a2-914b-1a8d58e0eb48\", " +
            "     \"vnfInstanceId\": \"7c00cc1e-6425-4fc3-afc3-0289db288d4c\", " +
            "     \"requestorId\": \"ds828e\" " +
            "    } " +
            "   } " +
            "  } " +
            " ] " +
            "} ";

        mockForGetOrchestrationRequest(200, responseString);

        final ExternalComponentStatus msoStatus = msoBusinessLogic.probeComponent();

        assertMsoStatus(msoStatus, true);
        assertMetadata(msoStatus, 200, startsWith(responseString.substring(0, 400)), MY_PRETTY_URL, equalTo("OK"));
    }

    @Test
    public void probeComponent_response200OkButEmptyPayload_shouldDescribeCorrectly() {
        String responseString = "" +
            "{ " +
            " \"requestList\": []" +
            "}";

        mockForGetOrchestrationRequest(200, responseString);

        final ExternalComponentStatus msoStatus = msoBusinessLogic.probeComponent();

        assertMsoStatus(msoStatus, true);

        assertMetadata(msoStatus, 200, equalTo(responseString), MY_PRETTY_URL, containsString("OK"));
    }

    @Test
    public void probeComponent_response200OkButInvalidPayload_shouldDescribeCorrectly() {
        String responseString = "this payload is an invalid json";

        mockForGetOrchestrationRequest(200, responseString);

        final ExternalComponentStatus msoStatus = msoBusinessLogic.probeComponent();

        assertMsoStatus(msoStatus, false);

        assertMetadata(msoStatus, 200, equalTo(responseString), MY_PRETTY_URL, containsString("JsonParseException: Unrecognized token"));
    }

    @Test
    public void probeComponent_verifyResponse406() {
        String responseString = "my raw data";

        when(msoInterface.getOrchestrationRequest(any(), eq(true))).thenThrow(
            new ExceptionWithRequestInfo(HttpMethod.GET, MY_PRETTY_URL, responseString, 406,
                new GenericUncheckedException(
                    new HttpException("Simulating as 406 was returned (200 or 202 expected)"))));

        final ExternalComponentStatus msoStatus = msoBusinessLogic.probeComponent();

        assertMsoStatus(msoStatus, false);

        assertMetadata(msoStatus, 406, equalTo(responseString), MY_PRETTY_URL, containsString("HttpException: Simulating as 406 was returned"));
    }


    @Test
    public void probeComponent_throwNullPointerException_resultIsWithErrorMetadata() {
        when(msoInterface.getOrchestrationRequest(any(), eq(true))).thenThrow(new NullPointerException());

        final ExternalComponentStatus msoStatus = msoBusinessLogic.probeComponent();

        MatcherAssert.assertThat(msoStatus.isAvailable(), is(false));
        MatcherAssert.assertThat(msoStatus.getComponent(), is(MSO));
        MatcherAssert.assertThat(msoStatus.getMetadata(), instanceOf(ErrorMetadata.class));

        final ErrorMetadata metadata = ((ErrorMetadata) msoStatus.getMetadata());
        org.junit.Assert.assertThat(metadata.getDescription(), containsString("NullPointerException"));
    }

    private void assertMsoStatus(ExternalComponentStatus msoStatus, boolean isAvailable) {
        MatcherAssert.assertThat(msoStatus.isAvailable(), is(isAvailable));
        MatcherAssert.assertThat(msoStatus.getComponent(), is(MSO));
        MatcherAssert.assertThat(msoStatus.getMetadata(), instanceOf(HttpRequestMetadata.class));
    }

    private void assertMetadata(ExternalComponentStatus msoStatus, int httpCode, Matcher<String> rawData, String url, Matcher<String> descriptionMatcher) {
        final HttpRequestMetadata metadata = ((HttpRequestMetadata) msoStatus.getMetadata());
        org.junit.Assert.assertThat(metadata.getHttpMethod(), equalTo(HttpMethod.GET));
        org.junit.Assert.assertThat(metadata.getHttpCode(), equalTo(httpCode));
        org.junit.Assert.assertThat(metadata.getUrl(), equalTo(url));
        org.junit.Assert.assertThat(metadata.getRawData(), rawData);
        org.junit.Assert.assertThat(metadata.getDescription(), descriptionMatcher);
    }
}

