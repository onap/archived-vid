/*
 * ============LICENSE_START==========================================
 * ===================================================================
 * Modifications Copyright (C) 2018 Nokia. All rights reserved.
 * ===================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END============================================
 *
 *
 */
package org.onap.vid.mso;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.onap.vid.mso.MsoBusinessLogicImpl.validateEndpointPath;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.mso.rest.Request;
import org.onap.vid.mso.rest.RequestDetails;
import org.onap.vid.mso.rest.RequestDetailsWrapper;
import org.onap.vid.properties.Features;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.togglz.core.manager.FeatureManager;

@ContextConfiguration(classes = {SystemProperties.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class MsoBusinessLogicImplTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private FeatureManager featureManager;

    @Mock
    private MsoInterface msoInterface;

    private MsoBusinessLogicImpl msoBusinessLogic;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        msoBusinessLogic = new MsoBusinessLogicImpl(msoInterface, featureManager);
    }

    @Test
    public void createConfigurationInstance_shouldCallMsoInterface_withCorrectServiceInstanceId() throws Exception {
        // given
        String serviceInstanceId = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
        String endpointTemplate = String.format("/serviceInstances/v6/%s/configurations", serviceInstanceId);
        RequestDetailsWrapper requestDetailsWrapper = createRequestDetails("mso_request_create_configuration.json");
        MsoResponseWrapper expectedResponse = createOkResponse();
        given(msoInterface.createConfigurationInstance(requestDetailsWrapper, endpointTemplate))
            .willReturn(expectedResponse);

        // when
        MsoResponseWrapper msoResponseWrapper = msoBusinessLogic
            .createConfigurationInstance(requestDetailsWrapper, serviceInstanceId);

        // then
        assertThat(msoResponseWrapper).isEqualToComparingFieldByField(expectedResponse);
    }

    private RequestDetailsWrapper createRequestDetails(String bodyFileName) throws Exception {
        final URL resource = this.getClass().getResource("/payload_jsons/" + bodyFileName);
        RequestDetails requestDetails = objectMapper.readValue(resource, RequestDetails.class);
        return new RequestDetailsWrapper(requestDetails);
    }

    @Test
    public void validateEndpointPath_endPointIsNotEmptyAndVaild_returnProperty() {
        System.setProperty("TestEnv", "123");
        String foundEndPoint = validateEndpointPath("TestEnv");
        assertEquals("123", foundEndPoint);
    }

    @Test
    public void validateEndpointPath_endPointIsNull_throwRuntimeException() {
        assertThatExceptionOfType(RuntimeException.class)
            .isThrownBy(() -> validateEndpointPath("NotExists"));
    }

    @Test
    public void validateEndpointPath_endPointIsNotEmptyButDoesntExists_throwRuntimeException() {
        String endPoint = "EmptyEndPoint";
        System.setProperty(endPoint, "");
        assertThatExceptionOfType(GenericUncheckedException.class)
            .isThrownBy(() -> validateEndpointPath(endPoint))
            .withMessage(endPoint + " env variable is not defined");
    }

    @Test
    public void deleteSvcInstance_verifyEndPointPathConstructing_unAssignFeatureOffOrUnAssignFlagIsFalse() {
        // given
        String endpointTemplate = "/serviceInstances/v5/%s";
        String serviceInstanceId = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
        String svcEndpoint = String.format(endpointTemplate, serviceInstanceId);
        RequestDetails requestDetails = new RequestDetails();
        MsoResponseWrapper expectedResponse = createOkResponse();
        given(msoInterface.deleteSvcInstance(requestDetails, svcEndpoint)).willReturn(expectedResponse);
        given(featureManager.isActive(Features.FLAG_UNASSIGN_SERVICE)).willReturn(false);

        // when
        MsoResponseWrapper msoResponseWrapper = msoBusinessLogic
            .deleteSvcInstance(requestDetails, serviceInstanceId, "unAssignOrDeleteParams");

        // then
        assertThat(msoResponseWrapper).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void deleteSvcInstance_verifyEndPointPathConstructing_unAssignFeatureOnAndUnAssignFlagIsTrue() {
        // given
        String endpointTemplate = "/serviceInstantiation/v5/serviceInstances/%s/unassign";
        String serviceInstanceId = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
        String svcEndpoint = String.format(endpointTemplate, serviceInstanceId);
        RequestDetails requestDetails = new RequestDetails();
        MsoResponseWrapper expectedResponse = createOkResponse();
        given(msoInterface.unassignSvcInstance(requestDetails, svcEndpoint)).willReturn(expectedResponse);
        given(featureManager.isActive(Features.FLAG_UNASSIGN_SERVICE)).willReturn(true);

        // when
        MsoResponseWrapper msoResponseWrapper = msoBusinessLogic
            .deleteSvcInstance(requestDetails, serviceInstanceId, "assigned");

        // then
        assertThat(msoResponseWrapper).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void deleteVnf_verifyEndPointPathConstructing() {
        // when
        String endpointTemplate = "/serviceInstances/v5/%s/vnfs/%s";
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
    public void deleteVfModule_verifyEndPointPathConstructing() {
        // when
        String endpointTemplate = "/serviceInstances/v7/%s/vnfs/%s/vfModules/%s";
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
    public void shouldSendProperScaleOutRequest() throws IOException {
        // given
        String serviceInstanceId = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
        String vnfInstanceId = "testVnfInstanceTempId";
        String endpointTemplate = "/serviceInstantiation/v7/serviceInstances/%s/vnfs/%s/vfModules/scaleOut";
        String vnfEndpoint = String.format(endpointTemplate, serviceInstanceId, vnfInstanceId);
        org.onap.vid.changeManagement.RequestDetails requestDetails = readRequest(
            "scaleOutVfModulePayload.json");
        org.onap.vid.changeManagement.RequestDetailsWrapper expectedRequest = readExpectedRequest(
            "scaleOutVfModulePayloadToMso.json");
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

    private org.onap.vid.changeManagement.RequestDetails readRequest(String requestJsonFilename) throws IOException {
        Path path = Paths.get("payload_jsons", requestJsonFilename);
        URL url = this.getClass().getClassLoader().getResource(path.toString());
        return objectMapper.readValue(url, org.onap.vid.changeManagement.RequestDetails.class);
    }

    private org.onap.vid.changeManagement.RequestDetailsWrapper readExpectedRequest(String requestJsonFilename)
        throws IOException {
        Path path = Paths.get("payload_jsons", requestJsonFilename);
        URL url = this.getClass().getClassLoader().getResource(path.toString());
        return objectMapper.readValue(url,
            new TypeReference<org.onap.vid.changeManagement.RequestDetailsWrapper<org.onap.vid.changeManagement.RequestDetails>>() {
            });
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

    @Test
    public void shouldFilterOutOrchestrationRequestsNotAllowedInDashboard() throws IOException {
        //given
        String vnfModelTypeOrchestrationRequests = getFileContentAsString("mso_model_info_sample_response.json");
        String scaleOutActionOrchestrationRequests = getFileContentAsString("mso_action_scaleout_sample_response.json");

        MsoResponseWrapper msoResponseWrapperMock = mock(MsoResponseWrapper.class);
        given(msoInterface
            .getOrchestrationRequestsForDashboard(any(String.class), any(String.class), any(String.class),
                any(RestObject.class)))
            .willReturn(msoResponseWrapperMock);
        given(msoResponseWrapperMock.getEntity())
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
            .extracting(org.onap.vid.domain.mso.Request::getRequestScope)
            .containsOnly("vnf", "vfModule");
    }

    private String getFileContentAsString(String resourceName) throws IOException {
        URL url = this.getClass().getClassLoader().getResource(".");
        Path path = Paths.get(url.getPath(), "payload_jsons", resourceName);
        return new String(Files.readAllBytes(path));
    }

    private static class MsoRequestWrapperMatcher extends
        ArgumentMatcher<org.onap.vid.changeManagement.RequestDetailsWrapper> {

        private final org.onap.vid.changeManagement.RequestDetailsWrapper expectedRequest;

        public MsoRequestWrapperMatcher(org.onap.vid.changeManagement.RequestDetailsWrapper expectedRequest) {
            this.expectedRequest = expectedRequest;
        }

        @Override
        public boolean matches(Object argument) {
            org.onap.vid.changeManagement.RequestDetailsWrapper requestDetailsWrapper = (org.onap.vid.changeManagement.RequestDetailsWrapper) argument;
            return expectedRequest.requestDetails.equals(requestDetailsWrapper.requestDetails);
        }
    }
}

