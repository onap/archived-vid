/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 Nokia.
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.portalsdk.core.domain.User;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.AaiResponseTranslator;
import org.onap.vid.aai.AaiResponseTranslator.PortMirroringConfigData;
import org.onap.vid.aai.AaiResponseTranslator.PortMirroringConfigDataError;
import org.onap.vid.aai.AaiResponseTranslator.PortMirroringConfigDataOk;
import org.onap.vid.aai.OperationalEnvironment;
import org.onap.vid.aai.model.AaiGetAicZone.AicZones;
import org.onap.vid.aai.model.AaiGetAicZone.Zone;
import org.onap.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.aai.model.PortDetailsTranslator.PortDetails;
import org.onap.vid.aai.model.PortDetailsTranslator.PortDetailsError;
import org.onap.vid.aai.model.PortDetailsTranslator.PortDetailsOk;
import org.onap.vid.aai.util.AAIRestInterface;
import org.onap.vid.model.VersionByInvariantIdsRequest;
import org.onap.vid.properties.Features;
import org.onap.vid.roles.RoleProvider;
import org.onap.vid.roles.RoleValidatorBySubscriberAndServiceType;
import org.onap.vid.services.AaiService;
import org.onap.vid.utils.SystemPropertiesWrapper;
import org.onap.vid.utils.Unchecked;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.togglz.core.manager.FeatureManager;

@RunWith(MockitoJUnitRunner.class)
public class AaiControllerTest {

    private static final String ID_1 = "id1";
    private static final String ID_2 = "id2";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private AaiService aaiService;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private AAIRestInterface aaiRestInterface;
    @Mock
    private RoleProvider roleProvider;
    @Mock
    private SystemPropertiesWrapper systemPropertiesWrapper;
    @Mock
    private FeatureManager featureManager;

    private MockMvc mockMvc;
    private AaiController aaiController;

    @Before
    public void setUp() {
        aaiController = new AaiController(aaiService, aaiRestInterface, roleProvider, systemPropertiesWrapper,
            featureManager);
        mockMvc = MockMvcBuilders.standaloneSetup(aaiController).build();
    }

    @Test
    public void getAicZoneForPnf_shouldReturnOKResponse() throws Exception {
        String globalCustomerId = "testCustomerId";
        String serviceType = "testServiceType";
        String serviceId = "testServiceId";
        String expectedResponseBody = "OK_RESPONSE";
        AaiResponse<String> aaiResponse = new AaiResponse<>(expectedResponseBody, null, HttpStatus.OK.value());
        given(aaiService.getAicZoneForPnf(globalCustomerId, serviceType, serviceId)).willReturn(aaiResponse);

        mockMvc.perform(
            get("/aai_get_aic_zone_for_pnf/{globalCustomerId}/{serviceType}/{serviceId}", globalCustomerId, serviceType,
                serviceId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    public void getInstanceGroupsByVnfInstanceId_shouldReturnOKResponse() throws Exception {
        String vnfInstanceId = "testVndInstanceId";
        String expectedResponseBody = "OK_RESPONSE";
        AaiResponse<String> aaiResponse = new AaiResponse<>(expectedResponseBody, null, HttpStatus.OK.value());
        given(aaiService.getInstanceGroupsByVnfInstanceId(vnfInstanceId)).willReturn(aaiResponse);

        mockMvc.perform(get("/aai_get_instance_groups_by_vnf_instance_id/{vnfInstanceId}", vnfInstanceId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    public void doGetServiceInstance_shouldFetchServiceInstance_byServiceInstanceId() throws Exception {
        String serviceInstanceId = "testServiceInstanceId";
        String serviceInstanceType = "Service Instance Id";
        String expectedResponseBody = "OK_RESPONSE";
        Response response = mock(Response.class);
        given(response.readEntity(String.class)).willReturn(expectedResponseBody);
        given(response.getStatus()).willReturn(HttpStatus.OK.value());

        given(aaiRestInterface.RestGet(eq("VidAaiController"), anyString(), eq(Unchecked.toURI(
            "search/nodes-query?search-node-type=service-instance&filter=service-instance-id:EQUALS:"
                + serviceInstanceId)),
            eq(false)).getResponse()).willReturn(response);

        mockMvc
            .perform(get("/aai_get_service_instance/{service-instance-id}/{service-instance-type}", serviceInstanceId,
                serviceInstanceType)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponseBody));
    }

    @Test
    public void doGetServiceInstance_shouldFetchServiceInstance_byServiceInstanceName() throws Exception {
        String serviceInstanceId = "testServiceInstanceId";
        String serviceInstanceType = "testServiceInstanceType";
        String expectedResponseBody = "OK_RESPONSE";
        Response response = mock(Response.class);
        given(response.readEntity(String.class)).willReturn(expectedResponseBody);
        given(response.getStatus()).willReturn(HttpStatus.OK.value());

        given(aaiRestInterface.RestGet(eq("VidAaiController"), anyString(), eq(Unchecked.toURI(
            "search/nodes-query?search-node-type=service-instance&filter=service-instance-name:EQUALS:"
                + serviceInstanceId)),
            eq(false)).getResponse()).willReturn(response);

        mockMvc
            .perform(get("/aai_get_service_instance/{service-instance-id}/{service-instance-type}", serviceInstanceId,
                serviceInstanceType)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponseBody));
    }

    @Test
    public void doGetServices_shouldReturnOkResponse() throws Exception {
        String globalCustomerId = "testGlobalCustomerId";
        String serviceSubscriptionId = "testServiceSubscriptionId";
        String expectedResponseBody = "OK_RESPONSE";
        Response response = mock(Response.class);
        given(response.readEntity(String.class)).willReturn(expectedResponseBody);
        given(response.getStatus()).willReturn(HttpStatus.OK.value());

        given(aaiRestInterface.RestGet(
            eq("VidAaiController"),
            anyString(),
            eq(Unchecked.toURI(
                "business/customers/customer/" + globalCustomerId + "/service-subscriptions/service-subscription/"
                    + serviceSubscriptionId + "?depth=0")),
            eq(false)).getResponse()).willReturn(response);

        mockMvc
            .perform(
                get("/aai_get_service_subscription/{global-customer-id}/{service-subscription-id}", globalCustomerId,
                    serviceSubscriptionId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponseBody));
    }

    @Test
    public void doGetServices_shouldReturnInternalServerError_forEmptyResponse() throws Exception {
        String globalCustomerId = "testGlobalCustomerId";
        String serviceSubscriptionId = "testServiceSubscriptionId";
        String expectedResponseBody = "Failed to fetch data from A&AI, check server logs for details.";
        given(aaiRestInterface.RestGet(
            eq("VidAaiController"),
            anyString(),
            eq(Unchecked.toURI(
                "business/customers/customer/" + globalCustomerId + "/service-subscriptions/service-subscription/"
                    + serviceSubscriptionId + "?depth=0")),
            eq(false)).getResponse()).willReturn(null);

        mockMvc
            .perform(
                get("/aai_get_service_subscription/{global-customer-id}/{service-subscription-id}", globalCustomerId,
                    serviceSubscriptionId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string(expectedResponseBody));
    }

    @Test
    public void getPortMirroringConfigData_givenIds_shouldReturnConfigDataMappedById() throws Exception {
        PortMirroringConfigDataOk okConfigData = new PortMirroringConfigDataOk("foo");
        PortMirroringConfigDataError errorConfigData = new PortMirroringConfigDataError("bar", "{ baz: qux }");
        Map<String, PortMirroringConfigData> expectedJson = ImmutableMap.of(
            ID_1, okConfigData,
            ID_2, errorConfigData);
        given(aaiService.getPortMirroringConfigData(ID_1)).willReturn(okConfigData);
        given(aaiService.getPortMirroringConfigData(ID_2)).willReturn(errorConfigData);

        mockMvc
            .perform(get("/aai_getPortMirroringConfigsData")
                .param("configurationIds", ID_1, ID_2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(expectedJson)));
    }

    @Test
    public void getPortMirroringSourcePorts_givenIds_shouldReturnPortDetailsMappedById() throws Exception {
        PortDetailsOk portDetailsOk = new PortDetailsOk("foo", "testInterface", true);
        PortDetailsError portDetailsError = new PortDetailsError("bar", "{ baz: qux }");
        Multimap<String, PortDetails> expectedJson = ImmutableMultimap.of(
            ID_1, portDetailsOk,
            ID_2, portDetailsError);
        given(aaiService.getPortMirroringSourcePorts(ID_1)).willReturn(Lists.newArrayList(portDetailsOk));
        given(aaiService.getPortMirroringSourcePorts(ID_2)).willReturn(Lists.newArrayList(portDetailsError));

        mockMvc
            .perform(get("/aai_getPortMirroringSourcePorts")
                .param("configurationIds", ID_1, ID_2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(expectedJson.asMap())));
    }

    @Test
    public void getNodeTemplateInstances_givenParams_shouldReturnCorrectResponse() throws Exception {
        String globalCustomerId = "testCustomerId";
        String serviceType = "testServiceType";
        String modelVersionId = UUID.nameUUIDFromBytes("modelVersionId".getBytes()).toString();
        String modelInvariantId = UUID.nameUUIDFromBytes("modelInvariantId".getBytes()).toString();
        String cloudRegion = "testRegion";
        String urlTemplate = "/aai_get_vnf_instances/{globalCustomerId}/{serviceType}/{modelVersionId}/{modelInvariantId}/{cloudRegion}";
        String expectedResponseBody = "myResponse";
        AaiResponse<String> aaiResponse = new AaiResponse<>(expectedResponseBody, "", HttpStatus.OK.value());
        given(aaiService
            .getNodeTemplateInstances(globalCustomerId, serviceType, modelVersionId, modelInvariantId, cloudRegion))
            .willReturn(aaiResponse);

        mockMvc
            .perform(get(urlTemplate, globalCustomerId, serviceType, modelVersionId, modelInvariantId, cloudRegion)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponseBody));
    }

    @Test
    public void getAicZones_shouldReturnAaiZones_whenAaiHttpStatusIsOK() throws Exception {
        AicZones aicZones = new AicZones(ImmutableList.of(new Zone("TEST_ZONE_ID", "TEST_ZONE_NAME")));
        given(aaiService.getAaiZones()).willReturn(new AaiResponse(aicZones, "", HttpStatus.OK.value()));

        mockMvc.perform(get("/aai_get_aic_zones")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(aicZones)));
    }

    @Test
    public void getAicZones_shouldReturnErrorResponse_whenAaiHttpStatusOtherThanOK() throws Exception {
        String expectedErrorMessage = "Calling AAI Failed";
        given(aaiService.getAaiZones())
            .willReturn(new AaiResponse(null, expectedErrorMessage, HttpStatus.INTERNAL_SERVER_ERROR.value()));

        mockMvc.perform(get("/aai_get_aic_zones")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string(expectedErrorMessage));
    }

    @Test
    public void getSpecificPnf_shouldReturnPnfObjectForPnfId() throws Exception {
        String pnfId = "MyPnfId";
        Pnf pnf = Pnf.builder()
            .withPnfId(pnfId)
            .withPnfName("TestPnf")
            .withPnfName2("pnfName2")
            .withPnfName2Source("pnfNameSource")
            .withEquipModel("model")
            .withEquipType("type")
            .withEquipVendor("vendor")
            .build();
        AaiResponse<Pnf> aaiResponse = new AaiResponse<>(pnf, "", HttpStatus.OK.value());
        given(aaiService.getSpecificPnf(pnfId)).willReturn(aaiResponse);

        mockMvc.perform(get("/aai_get_pnfs/pnf/{pnf_id}", pnfId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(pnf)));
    }

    @Test
    public void getSpecificPnf_shouldHandleAAIServiceException() throws Exception {
        String pnfId = "MyPnfId";
        String expectedErrorMessage = "AAI Service Error";
        given(aaiService.getSpecificPnf(pnfId)).willThrow(new RuntimeException(expectedErrorMessage));

        mockMvc.perform(get("/aai_get_pnfs/pnf/{pnf_id}", pnfId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string(expectedErrorMessage));
    }

    public void getPNFInstances_shouldReturnOKResponseFromAAIService() throws Exception {
        String globalCustomerId = "testCustomerId";
        String serviceType = "testServiceType";
        String modelVersionId = UUID.nameUUIDFromBytes("modelVersionId".getBytes()).toString();
        String modelInvariantId = UUID.nameUUIDFromBytes("modelInvariantId".getBytes()).toString();
        String cloudRegion = "testRegion";
        String equipVendor = "testVendor";
        String equipModel = "model123";
        String urlTemplate = "/aai_get_pnf_instances/{globalCustomerId}/{serviceType}/{modelVersionId}/{modelInvariantId}/{cloudRegion}/{equipVendor}/{equipModel}";
        String expectedResponseBody = "myResponse";
        AaiResponse<String> aaiResponse = new AaiResponse<>(expectedResponseBody, "", HttpStatus.OK.value());

        given(aaiService
            .getPNFData(globalCustomerId, serviceType, modelVersionId, modelInvariantId, cloudRegion, equipVendor,
                equipModel)).willReturn(aaiResponse);

        mockMvc.perform(
            get(urlTemplate, globalCustomerId, serviceType, modelVersionId,
                modelInvariantId, cloudRegion, equipVendor, equipModel)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponseBody));
    }

    @Test
    public void getVersionByInvariantId_shouldReturnOKResponse() throws Exception {
        String expectedResponse = "OKResponse";
        VersionByInvariantIdsRequest request = new VersionByInvariantIdsRequest();
        request.versions = ImmutableList.of("ver1", "ver2");
        Response response = mock(Response.class);
        given(response.readEntity(String.class)).willReturn(expectedResponse);
        given(aaiService
            .getVersionByInvariantId(request.versions)).willReturn(response);

        mockMvc.perform(
            post("/aai_get_version_by_invariant_id")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponse));
    }

    @Test
    public void getSubscriberDetails_shouldOmitServiceInstancesFromSubscriberData_whenFeatureEnabled_andOmitFlagIsTrue()
        throws Exception {
        boolean isFeatureActive = true;
        boolean omitServiceInstances = true;

        String subscriberId = "subscriberId";
        String okResponseBody = "OK_RESPONSE";
        AaiResponse<String> aaiResponse = new AaiResponse<>(okResponseBody, "", HttpStatus.OK.value());
        given(featureManager.isActive(Features.FLAG_1906_AAI_SUB_DETAILS_REDUCE_DEPTH)).willReturn(isFeatureActive);
        given(aaiService.getSubscriberData(eq(subscriberId), isA(RoleValidatorBySubscriberAndServiceType.class),
            eq(isFeatureActive && omitServiceInstances)))
            .willReturn(aaiResponse);

        mockMvc.perform(
            get("/aai_sub_details/{subscriberId}", subscriberId)
                .param("omitServiceInstances", Boolean.toString(omitServiceInstances))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(objectMapper.writeValueAsString(okResponseBody)));
    }

    @Test
    public void getSubscriberDetails_shouldIncludeServiceInstancesFromSubscriberData_whenFeatureEnabled_andOmitFlagIsFalse()
        throws Exception {
        boolean isFeatureActive = true;
        boolean omitServiceInstances = false;

        getSubscriberDetails_assertServiceInstancesInclusion(isFeatureActive, omitServiceInstances);
    }

    @Test
    public void getSubscriberDetails_shouldIncludeServiceInstancesFromSubscriberData_whenFeatureDisabled_andOmitFlagIsTrue()
        throws Exception {
        boolean isFeatureActive = false;
        boolean omitServiceInstances = true;

        getSubscriberDetails_assertServiceInstancesInclusion(isFeatureActive, omitServiceInstances);
    }

    @Test
    public void getPortMirroringConfigData_givenThreeIds_ReturnsThreeResults() {

        final AaiResponseTranslator.PortMirroringConfigDataOk toBeReturnedForA = new AaiResponseTranslator.PortMirroringConfigDataOk(
            "foobar");
        final AaiResponseTranslator.PortMirroringConfigDataError toBeReturnedForB = new AaiResponseTranslator.PortMirroringConfigDataError(
            "foo", "{ baz: qux }");
        final AaiResponseTranslator.PortMirroringConfigDataOk toBeReturnedForC = new AaiResponseTranslator.PortMirroringConfigDataOk(
            "corge");

        Mockito
            .doReturn(toBeReturnedForA)
            .doReturn(toBeReturnedForB)
            .doReturn(toBeReturnedForC)
            .when(aaiService).getPortMirroringConfigData(Mockito.anyString());

        final Map<String, AaiResponseTranslator.PortMirroringConfigData> result = aaiController
            .getPortMirroringConfigsData(ImmutableList.of("a", "b", "c"));

        assertThat(result, is(ImmutableMap.of(
            "a", toBeReturnedForA,
            "b", toBeReturnedForB,
            "c", toBeReturnedForC
        )));
    }

    @Test
    public void getSubscriberDetails_shouldIncludeServiceInstancesFromSubscriberData_whenFeatureDisabled_andOmitFlagIsFalse()
        throws Exception {
        boolean isFeatureActive = false;
        boolean omitServiceInstances = false;
        getSubscriberDetails_assertServiceInstancesInclusion(isFeatureActive, omitServiceInstances);
    }

    private void getSubscriberDetails_assertServiceInstancesInclusion(boolean isFeatureActive,
        boolean omitServiceInstances) throws Exception {
        String subscriberId = "subscriberId";
        String okResponseBody = "OK_RESPONSE";
        AaiResponse<String> aaiResponse = new AaiResponse<>(okResponseBody, "", HttpStatus.OK.value());
        given(featureManager.isActive(Features.FLAG_1906_AAI_SUB_DETAILS_REDUCE_DEPTH)).willReturn(isFeatureActive);
        given(aaiService.getSubscriberData(eq(subscriberId), isA(RoleValidatorBySubscriberAndServiceType.class),
            eq(isFeatureActive && omitServiceInstances)))
            .willReturn(aaiResponse);

        mockMvc.perform(
            get("/aai_sub_details/{subscriberId}", subscriberId)
                .param("omitServiceInstances", Boolean.toString(omitServiceInstances))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(objectMapper.writeValueAsString(okResponseBody)));
    }

    @Test
    public void getSpecificConfiguration_shouldReturnOkResponse() throws Exception {
        String configurationId = "testGlobalCustomerId";
        String expectedResponseBody = "OK_RESPONSE";
        Response response = mock(Response.class);
        given(response.readEntity(String.class)).willReturn(expectedResponseBody);
        given(response.getStatus()).willReturn(HttpStatus.OK.value());

        given(aaiRestInterface.RestGet(
            eq("VidAaiController"),
            anyString(),
            eq(Unchecked.toURI("network/configurations/configuration/" + configurationId)),
            eq(false)).getResponse()).willReturn(response);

        mockMvc
            .perform(
                get("/aai_get_configuration/{configuration_id}", configurationId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponseBody));
    }

    @Test
    public void getServiceInstanceAssociatedPnfs_shouldReturnPnfs() throws Exception {
        String globalCustomerId = "testCustomerId";
        String serviceType = "testServiceType";
        String serviceInstanceId = "testServiceInstanceId";
        List<String> expectedPnfs = ImmutableList.of("pnf1", "pnf2", "pnf3");

        given(aaiService.getServiceInstanceAssociatedPnfs(globalCustomerId, serviceType, serviceInstanceId))
            .willReturn(expectedPnfs);

        mockMvc
            .perform(
                get("/aai_get_service_instance_pnfs/{globalCustomerId}/{serviceType}/{serviceInstanceId}",
                    globalCustomerId, serviceType, serviceInstanceId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(expectedPnfs)));
    }

    @Test
    public void getUserID_shouldReturnOKResponse_withExtractedUserId() throws Exception {
        String userPropertyKey = "user";
        String expectedUserLoginId = "testUserLoginId";
        User user = new User();
        user.setLoginId(expectedUserLoginId);
        given(systemPropertiesWrapper.getProperty(SystemProperties.USER_ATTRIBUTE_NAME)).willReturn(userPropertyKey);

        mockMvc.perform(get("/getuserID")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .sessionAttr(userPropertyKey, user))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedUserLoginId));
    }

    @Test
    public void getTargetProvStatus_shouldReturnProperty() throws Exception {
        String expectedResponse = "targetProvStatus";
        given(systemPropertiesWrapper.getProperty("aai.vnf.provstatus")).willReturn(expectedResponse);

        mockMvc.perform(get("/get_system_prop_vnf_prov_status")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponse));
    }

    @Test
    public void getOperationalEnvironments_shouldReturnOkResponse() throws Exception {
        String operationalEnvironmentType = "testEnvType";
        String operationalEnvironmentStatus = "testEnvStatus";
        OperationalEnvironmentList operationalEnvironmentList = new OperationalEnvironmentList(
            ImmutableList.of(OperationalEnvironment.builder()
                .withOperationalEnvironmentType(operationalEnvironmentType)
                .withOperationalEnvironmentStatus(operationalEnvironmentStatus)
                .build()));
        AaiResponse<OperationalEnvironmentList> aaiResponse = new AaiResponse<>(operationalEnvironmentList, null,
            HttpStatus.OK.value());
        given(aaiService.getOperationalEnvironments(operationalEnvironmentType, operationalEnvironmentStatus))
            .willReturn(aaiResponse);

        mockMvc.perform(get("/get_operational_environments")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .param("operationalEnvironmentType", operationalEnvironmentType)
            .param("operationalEnvironmentStatus", operationalEnvironmentStatus))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(aaiResponse)));
    }
}

