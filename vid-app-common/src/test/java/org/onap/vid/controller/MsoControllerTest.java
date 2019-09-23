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

package org.onap.vid.controller;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.misc.BooleanRandomizer;
import org.jeasy.random.randomizers.text.StringRandomizer;
import org.junit.Before;
import org.junit.Test;
import org.onap.vid.model.RequestReferencesContainer;
import org.onap.vid.mso.MsoBusinessLogic;
import org.onap.vid.mso.MsoResponseWrapper;
import org.onap.vid.mso.MsoResponseWrapper2;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.rest.MsoRestClientNew;
import org.onap.vid.mso.rest.Request;
import org.onap.vid.mso.rest.RequestDetails;
import org.onap.vid.mso.rest.RequestDetailsWrapper;
import org.onap.vid.mso.rest.Task;
import org.onap.vid.services.CloudOwnerService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class MsoControllerTest {

    private static final long STATIC_SEED = 5336L;
    private EasyRandomParameters parameters = new EasyRandomParameters()
        .randomize(Boolean.class, new BooleanRandomizer(STATIC_SEED))
        .randomize(String.class, new StringRandomizer(4, 4, STATIC_SEED))
        .collectionSizeRange(2, 3);
    private EasyRandom modelGenerator = new EasyRandom(parameters);
    private ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;
    private MsoBusinessLogic msoBusinessLogic;
    private CloudOwnerService cloudService;
    private MsoRestClientNew msoRestClient;

    @Before
    public void setUp() {
        msoBusinessLogic = mock(MsoBusinessLogic.class);
        cloudService = mock(CloudOwnerService.class);
        msoRestClient = mock(MsoRestClientNew.class);
        MsoController msoController = new MsoController(msoBusinessLogic, msoRestClient, cloudService);

        mockMvc = MockMvcBuilders.standaloneSetup(msoController).build();
    }

    @Test
    public void shouldGetOrchestrationRequest() throws Exception {
        // given
        RequestDetails requestDetails = modelGenerator.nextObject(RequestDetails.class);
        String requestId = "bc305d54-75b4-431b-adb2-eb6b9e546014";

        MsoResponseWrapper expectedResponse = new MsoResponseWrapper(200, "test");
        given(msoBusinessLogic
            .getOrchestrationRequest(requestId))
            .willReturn(expectedResponse);

        // when & then
        mockMvc.perform(get(format("/mso/mso_get_orch_req/%s", requestId))
            .content(asJson(requestDetails))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(asJson(expectedResponse)));

        then(cloudService).shouldHaveZeroInteractions();
    }

    @Test
    public void shouldDelegateNewServiceInstantiation() throws Exception {
        // given
        RequestDetails requestDetails = modelGenerator.nextObject(RequestDetails.class);

        MsoResponseWrapper expectedResponse = new MsoResponseWrapper(200, "test");
        given(msoBusinessLogic
            .createSvcInstance(objectEqualTo(requestDetails)))
            .willReturn(expectedResponse);

        // when & then
        mockMvc.perform(post("/mso/mso_create_svc_instance")
            .content(asJson(requestDetails))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(asJson(expectedResponse)));

        then(cloudService).should(only()).enrichRequestWithCloudOwner(objectEqualTo(requestDetails));
    }

    @Test
    public void shouldCreateVfModuleInstance() throws Exception {
        // given
        RequestDetails requestDetails = modelGenerator.nextObject(RequestDetails.class);
        String serviceInstanceId = "bc305d54-75b4-431b-adb2-eb6b9e546014";
        String vnfInstanceId = "fe9000-0009-9999";

        MsoResponseWrapper expectedResponse = new MsoResponseWrapper(200, "test");
        given(msoBusinessLogic
            .createVfModuleInstance(objectEqualTo(requestDetails), eq(serviceInstanceId), eq(vnfInstanceId)))
            .willReturn(expectedResponse);

        // when & then
        mockMvc.perform(post(format("/mso/mso_create_vfmodule_instance/%s/vnfs/%s", serviceInstanceId, vnfInstanceId))
            .content(asJson(requestDetails))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(asJson(expectedResponse)));

        then(cloudService).should(only()).enrichRequestWithCloudOwner(objectEqualTo(requestDetails));
    }
    
    @Test
    public void shouldCreateConfigurationInstance() throws Exception {
        // given
        RequestDetailsWrapper requestDetails = modelGenerator.nextObject(RequestDetailsWrapper.class);
        String serviceInstanceId = "bc305d54-75b4-431b-adb2-eb6b9e546014";

        MsoResponseWrapper expectedResponse = new MsoResponseWrapper(200, "test");
        given(msoBusinessLogic
            .createConfigurationInstance(objectEqualTo(requestDetails), eq(serviceInstanceId)))
            .willReturn(expectedResponse);

        // when & then
        mockMvc.perform(post(format("/mso/mso_create_configuration_instance/%s/configurations/", serviceInstanceId))
            .content(asJson(requestDetails))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(asJson(expectedResponse)));

        then(cloudService).should(only()).enrichRequestWithCloudOwner(objectEqualTo(requestDetails.getRequestDetails()));
    }

    @Test
    public void shouldDeleteE2eSvcInstance() throws Exception {
        // given
        RequestDetails requestDetails = modelGenerator.nextObject(RequestDetails.class);
        String serviceInstanceId = "bc305d54-75b4-431b-adb2-eb6b9e546014";
        String configurationId = "28630972-d548-4d5f-acc2-ad1d748d023d";

        MsoResponseWrapper expectedResponse = new MsoResponseWrapper(200, "test");
        given(msoBusinessLogic
            .setConfigurationActiveStatus(objectEqualTo(requestDetails), eq(serviceInstanceId), eq(configurationId), eq(true)))
            .willReturn(expectedResponse);

        // when & then
        mockMvc.perform(post(format("/mso/mso_activate_configuration/%s/configurations/%s", serviceInstanceId, configurationId))
            .content(asJson(requestDetails))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(asJson(expectedResponse)));

        then(cloudService).should(only()).enrichRequestWithCloudOwner(objectEqualTo(requestDetails));
    }

    @Test
    public void shouldDeactivateConfiguration() throws Exception {
        // given
        RequestDetails requestDetails = modelGenerator.nextObject(RequestDetails.class);
        String serviceInstanceId = "bc305d54-75b4-431b-adb2-eb6b9e546014";
        String configurationId = "28630972-d548-4d5f-acc2-ad1d748d023d";

        MsoResponseWrapper expectedResponse = new MsoResponseWrapper(200, "test");
        given(msoBusinessLogic
            .setConfigurationActiveStatus(objectEqualTo(requestDetails), eq(serviceInstanceId), eq(configurationId), eq(false)))
            .willReturn(expectedResponse);

        // when & then
        mockMvc.perform(post(format("/mso/mso_deactivate_configuration/%s/configurations/%s", serviceInstanceId, configurationId))
            .content(asJson(requestDetails))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(asJson(expectedResponse)));

        then(cloudService).should(only()).enrichRequestWithCloudOwner(objectEqualTo(requestDetails));
    }

    @Test
    public void shouldDisablePortOnConfiguration() throws Exception {
        // given
        RequestDetails requestDetails = modelGenerator.nextObject(RequestDetails.class);
        String serviceInstanceId = "bc305d54-75b4-431b-adb2-eb6b9e546014";
        String configurationId = "28630972-d548-4d5f-acc2-ad1d748d023d";

        MsoResponseWrapper expectedResponse = new MsoResponseWrapper(200, "test");
        given(msoBusinessLogic
            .setPortOnConfigurationStatus(objectEqualTo(requestDetails), eq(serviceInstanceId), eq(configurationId), eq(false)))
            .willReturn(expectedResponse);

        // when & then
        mockMvc.perform(post(format("/mso/mso_disable_port_configuration/%s/configurations/%s", serviceInstanceId, configurationId))
            .content(asJson(requestDetails))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(asJson(expectedResponse)));

        then(cloudService).should(only()).enrichRequestWithCloudOwner(objectEqualTo(requestDetails));
    }

    @Test
    public void shouldEnablePortOnConfiguration() throws Exception {
        // given
        RequestDetails requestDetails = modelGenerator.nextObject(RequestDetails.class);
        String serviceInstanceId = "bc305d54-75b4-431b-adb2-eb6b9e546014";
        String configurationId = "28630972-d548-4d5f-acc2-ad1d748d023d";

        MsoResponseWrapper expectedResponse = new MsoResponseWrapper(200, "test");
        given(msoBusinessLogic
            .setPortOnConfigurationStatus(objectEqualTo(requestDetails), eq(serviceInstanceId), eq(configurationId), eq(true)))
            .willReturn(expectedResponse);

        // when & then
        mockMvc.perform(post(format("/mso/mso_enable_port_configuration/%s/configurations/%s", serviceInstanceId, configurationId))
            .content(asJson(requestDetails))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(asJson(expectedResponse)));

        then(cloudService).should(only()).enrichRequestWithCloudOwner(objectEqualTo(requestDetails));
    }

    @Test
    public void shouldCreateVolumeInstance() throws Exception {
        // given
        RequestDetails requestDetails = modelGenerator.nextObject(RequestDetails.class);
        String serviceInstanceId = "bc305d54-75b4-431b-adb2-eb6b9e546014";
        String vnfInstanceId = "fe9000-0009-9999";

        MsoResponseWrapper expectedResponse = new MsoResponseWrapper(200, "test");
        given(msoBusinessLogic
            .createVolumeGroupInstance(objectEqualTo(requestDetails), eq(serviceInstanceId), eq(vnfInstanceId)))
            .willReturn(expectedResponse);

        // when & then
        mockMvc.perform(post(format("/mso/mso_create_volumegroup_instance/%s/vnfs/%s", serviceInstanceId, vnfInstanceId))
            .content(asJson(requestDetails))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(asJson(expectedResponse)));

        then(cloudService).should(only()).enrichRequestWithCloudOwner(objectEqualTo(requestDetails));
    }

    @Test
    public void shouldReturnOrchestrationRequestsForDashboard() throws Exception {
        // given
        List<Request> orchestrationRequests = modelGenerator
            .objects(Request.class, 2)
            .collect(Collectors.toList());

        given(msoBusinessLogic.getOrchestrationRequestsForDashboard()).willReturn(orchestrationRequests);

        // when & then
        mockMvc.perform(get("/mso/mso_get_orch_reqs/dashboard"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(orchestrationRequests)));

        then(cloudService).shouldHaveZeroInteractions();
    }

    @Test
    public void shouldReturnManualTasksById() throws Exception {
        // given
        List<Task> manualTasks = modelGenerator
            .objects(Task.class, 2)
            .collect(Collectors.toList());

        String originalRequestId = "za1234d1-5a33-55df-13ab-12abad84e335";
        given(msoBusinessLogic.getManualTasksByRequestId(originalRequestId)).willReturn(manualTasks);

        // when & then
        mockMvc.perform(get("/mso/mso_get_man_task/{id}", originalRequestId))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(manualTasks)));

        then(cloudService).shouldHaveZeroInteractions();
    }

    @Test
    public void shouldDelegateE2EServiceInstantiation() throws Exception {
        // given
        String requestDetails = "some request details";
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("requestDetails", requestDetails);

        MsoResponseWrapper wrapper = mock(MsoResponseWrapper.class);
        given(wrapper.getResponse()).willReturn("some response");
        given(msoBusinessLogic.createE2eSvcInstance(requestDetails)).willReturn(wrapper);

        // when & then
        mockMvc.perform(post("/mso/mso_create_e2e_svc_instance")
            .content(asJson(payload))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("some response"));

        then(cloudService).shouldHaveZeroInteractions();
    }

    @Test
    public void shouldDelegateServiceInstantiation() throws Exception {
        // given
        RequestDetails requestDetails = modelGenerator.nextObject(RequestDetails.class);

        MsoResponseWrapper wrapper = mock(MsoResponseWrapper.class);
        given(wrapper.getResponse()).willReturn("some response");
        given(msoBusinessLogic.createSvcInstance(objectEqualTo(requestDetails))).willReturn(wrapper);

        // when & then
        mockMvc.perform(post("/mso/mso_create_svc_instance")
            .content(asJson(requestDetails))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("some response"));

        then(cloudService).should(only()).enrichRequestWithCloudOwner(objectEqualTo(requestDetails));
    }

    @Test
    public void shouldDelegateVnfInstantiation() throws Exception {
        // given
        RequestDetails requestDetails = modelGenerator.nextObject(RequestDetails.class);
        String serviceInstanceId = "bc305d54-75b4-431b-adb2-eb6b9e546014";

        MsoResponseWrapper wrapper = mock(MsoResponseWrapper.class);
        given(wrapper.getResponse()).willReturn("some response");
        given(msoBusinessLogic.createVnf(objectEqualTo(requestDetails), eq(serviceInstanceId))).willReturn(wrapper);

        // when & then
        mockMvc.perform(post("/mso/mso_create_vnf_instance/" + serviceInstanceId)
            .content(asJson(requestDetails))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("some response"));

        then(cloudService).should(only()).enrichRequestWithCloudOwner(objectEqualTo(requestDetails));
    }

    @Test
    public void shouldDeleteVfModuleInstance() throws Exception {
        // given
        RequestDetails requestDetails = modelGenerator.nextObject(RequestDetails.class);
        String serviceInstanceId = "bc305d54-75b4-431b-adb2-eb6b9e546014";
        String vnfInstanceId = "fe9000-0009-9999";
        String vfModuleId = "abeeee-abeeee-abeeee";

        MsoResponseWrapper wrapper = mock(MsoResponseWrapper.class);
        given(wrapper.getResponse()).willReturn("some response");
        given(msoBusinessLogic.deleteVfModule(objectEqualTo(requestDetails), eq(serviceInstanceId), eq(vnfInstanceId), eq(vfModuleId))).willReturn(wrapper);

        // when & then
        mockMvc.perform(post(format("/mso/mso_delete_vfmodule_instance/%s/vnfs/%s/vfModules/%s", serviceInstanceId, vnfInstanceId, vfModuleId))
            .content(asJson(requestDetails))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("some response"));

        then(cloudService).should(only()).enrichRequestWithCloudOwner(objectEqualTo(requestDetails));
    }

    @Test
    public void shouldDeleteVolumeGroup() throws Exception {
        // given
        RequestDetails requestDetails = modelGenerator.nextObject(RequestDetails.class);
        String serviceInstanceId = "bc305d54-75b4-431b-adb2-eb6b9e546014";
        String vnfInstanceId = "fe9000-0009-9999";
        String volumeGroupId = "abeeee-abeeee-abeeee";

        MsoResponseWrapper wrapper = mock(MsoResponseWrapper.class);
        given(wrapper.getResponse()).willReturn("some response");
        given(msoBusinessLogic.deleteVolumeGroupInstance(objectEqualTo(requestDetails), eq(serviceInstanceId), eq(vnfInstanceId), eq(volumeGroupId))).willReturn(wrapper);

        // when & then
        mockMvc.perform(post(format("/mso/mso_delete_volumegroup_instance/%s/vnfs/%s/volumeGroups/%s", serviceInstanceId, vnfInstanceId, volumeGroupId))
            .content(asJson(requestDetails))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("some response"));

        then(cloudService).should(only()).enrichRequestWithCloudOwner(objectEqualTo(requestDetails));
    }

    @Test
    public void shouldDeleteInstance() throws Exception {
        // given
        RequestDetails requestDetails = modelGenerator.nextObject(RequestDetails.class);
        String serviceInstanceId = "bc305d54-75b4-431b-adb2-eb6b9e546014";
        String networkInstanceId = "fe9000-0009-9999";

        MsoResponseWrapper wrapper = mock(MsoResponseWrapper.class);
        given(wrapper.getResponse()).willReturn("some response");
        given(msoBusinessLogic.deleteNwInstance(objectEqualTo(requestDetails), eq(serviceInstanceId), eq(networkInstanceId))).willReturn(wrapper);

        // when & then
        mockMvc.perform(post(format("/mso/mso_delete_nw_instance/%s/networks/%s", serviceInstanceId, networkInstanceId))
            .content(asJson(requestDetails))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("some response"));

        then(cloudService).should(only()).enrichRequestWithCloudOwner(objectEqualTo(requestDetails));
    }

    @Test
    public void shouldDeleteServiceInstance() throws Exception {
        // given
        RequestDetails requestDetails = modelGenerator.nextObject(RequestDetails.class);
        String serviceInstanceId = "bc305d54-75b4-431b-adb2-eb6b9e546014";
        String serviceStatus = "ACTIVE";

        MsoResponseWrapper wrapper = mock(MsoResponseWrapper.class);
        given(wrapper.getResponse()).willReturn("some response");
        given(msoBusinessLogic.deleteSvcInstance(objectEqualTo(requestDetails), eq(serviceInstanceId), eq(serviceStatus))).willReturn(wrapper);

        // when & then
        mockMvc.perform(post(format("/mso/mso_delete_svc_instance/%s", serviceInstanceId))
            .param("serviceStatus", serviceStatus)
            .content(asJson(requestDetails))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("some response"));

        then(cloudService).shouldHaveZeroInteractions();
    }

    @Test
    public void shouldDeleteVnf() throws Exception {
        // given
        RequestDetails requestDetails = modelGenerator.nextObject(RequestDetails.class);
        String serviceInstanceId = "bc305d54-75b4-431b-adb2-eb6b9e546014";
        String vnfInstanceId = "fe9000-0009-9999";

        MsoResponseWrapper wrapper = mock(MsoResponseWrapper.class);
        given(wrapper.getResponse()).willReturn("some response");
        given(msoBusinessLogic.deleteVnf(objectEqualTo(requestDetails), eq(serviceInstanceId), eq(vnfInstanceId))).willReturn(wrapper);

        // when & then
        mockMvc.perform(post(format("/mso/mso_delete_vnf_instance/%s/vnfs/%s", serviceInstanceId, vnfInstanceId))
            .content(asJson(requestDetails))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("some response"));

        then(cloudService).should(only()).enrichRequestWithCloudOwner(objectEqualTo(requestDetails));
    }

    @Test
    public void shouldDeleteConfiguration() throws Exception {
        // given
        RequestDetailsWrapper requestDetails = modelGenerator.nextObject(RequestDetailsWrapper.class);
        String serviceInstanceId = "bc305d54-75b4-431b-adb2-eb6b9e546014";
        String configurationId = "fe9000-0009-9999";

        MsoResponseWrapper wrapper = mock(MsoResponseWrapper.class);
        given(wrapper.getResponse()).willReturn("some response");
        given(msoBusinessLogic.deleteConfiguration(objectEqualTo(requestDetails), eq(serviceInstanceId), eq(configurationId))).willReturn(wrapper);

        // when & then
        mockMvc.perform(post(format("/mso/mso_delete_configuration/%s/configurations/%s", serviceInstanceId, configurationId))
            .content(asJson(requestDetails))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("some response"));

        then(cloudService).should(only()).enrichRequestWithCloudOwner(objectEqualTo(requestDetails.getRequestDetails()));
    }

    @Test
    public void shouldDelegateNewInstanceCreation() throws Exception {
        // given
        RequestDetails requestDetails = modelGenerator.nextObject(RequestDetails.class);
        String serviceInstanceId = "bc305d54-75b4-431b-adb2-eb6b9e546014";

        MsoResponseWrapper wrapper = mock(MsoResponseWrapper.class);
        given(wrapper.getResponse()).willReturn("some response");
        given(msoBusinessLogic.createNwInstance(objectEqualTo(requestDetails), eq(serviceInstanceId))).willReturn(wrapper);

        // when & then
        mockMvc.perform(post("/mso/mso_create_nw_instance/" + serviceInstanceId)
            .content(asJson(requestDetails))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("some response"));

        then(cloudService).should(only()).enrichRequestWithCloudOwner(objectEqualTo(requestDetails));
    }

    @Test
    public void shouldCompleteManualTask() throws Exception {
        // given
        RequestDetails requestDetails = modelGenerator.nextObject(RequestDetails.class);
        String taskId = "bc305d54-75b4-431b-adb2-eb6b9e546014";

        MsoResponseWrapper wrapper = mock(MsoResponseWrapper.class);
        given(wrapper.getResponse()).willReturn("some response");
        given(msoBusinessLogic.completeManualTask(objectEqualTo(requestDetails), eq(taskId))).willReturn(wrapper);

        // when & then
        mockMvc.perform(post("/mso/mso_post_man_task/" + taskId)
            .content(asJson(requestDetails))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("some response"));

        then(cloudService).shouldHaveZeroInteractions();
    }

    @Test
    public void shouldActivateFabricConfiguration() throws Exception {
        // given
        RequestDetails requestDetails = modelGenerator.nextObject(RequestDetails.class);
        String serviceInstanceId = "bc305d54-75b4-431b-adb2-eb6b9e546014";

        String path = "/mso/path";
        given(msoBusinessLogic.getActivateFabricConfigurationPath(eq(serviceInstanceId))).willReturn(path);

        RestObject<RequestReferencesContainer> response = new RestObject<>();
        response.set(mock(RequestReferencesContainer.class));
        response.setRaw("some response");
        response.setStatusCode(200);

        given(msoRestClient.PostForObject(objectEqualTo(requestDetails), eq(path), eq(RequestReferencesContainer.class))).willReturn(response);

        // when & then
        mockMvc.perform(post(format("/mso/mso_activate_fabric_configuration/%s", serviceInstanceId))
            .content(asJson(requestDetails))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("{\"status\":200,\"entity\":{}}"));

        then(cloudService).shouldHaveZeroInteractions();
    }

    private <T> String asJson(T value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T objectEqualTo(T expected) {
        return argThat(given -> asJson(given).equals(asJson(expected)));
    }

    @Test
    public void testActivateFabricConfiguration() throws Exception {

        String serviceInstanceId = "tempId";

        //define mock response object
        String responseString = "{" +
            "      \"requestReferences\": {" +
            "        \"instanceId\": \"tempId\"," +
            "        \"requestId\": \"dbe54591-c8ed-46d3-abc7-d3a24873dfbd\"" +
            "      }" +
            "    }";
        final RestObject<RequestReferencesContainer> restObject = new RestObject<>();
        restObject.set(objectMapper.readValue(responseString, RequestReferencesContainer.class));
        restObject.setStatusCode(200);

        //register mock
        String msoPath = "justAFakePath";

        when(msoBusinessLogic.getActivateFabricConfigurationPath(serviceInstanceId)).thenReturn(msoPath);
        when(msoRestClient.PostForObject(new RequestDetails(), msoPath, RequestReferencesContainer.class)).thenReturn(restObject);

        //expected response
        MsoResponseWrapper2<RequestReferencesContainer> expectedResponse = new MsoResponseWrapper2<>(restObject);

        //get response from controller
        // when & then
        mockMvc.perform(post(format("/mso/mso_activate_fabric_configuration/%s", serviceInstanceId))
            .content(asJson(new RequestDetails()))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(asJson(expectedResponse)));
    }
}
