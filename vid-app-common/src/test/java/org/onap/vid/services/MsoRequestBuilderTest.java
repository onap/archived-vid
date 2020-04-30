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

package org.onap.vid.services;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.EMPTY_MAP;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonNodeAbsent;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonPartEquals;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.javacrumbs.jsonunit.ConfigurableJsonMatcher;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.SessionFactory;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.config.DataSourceConfig;
import org.onap.vid.config.MockedAaiClientAndFeatureManagerConfig;
import org.onap.vid.dal.AsyncInstantiationRepository;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.job.command.MsoRequestBuilder;
import org.onap.vid.model.Action;
import org.onap.vid.model.serviceInstantiation.InstanceGroup;
import org.onap.vid.model.serviceInstantiation.Network;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.model.serviceInstantiation.VfModule;
import org.onap.vid.model.serviceInstantiation.Vnf;
import org.onap.vid.mso.MsoOperationalEnvironmentTest;
import org.onap.vid.mso.model.AddOrRemoveInstanceGroupMemberRequestDetails;
import org.onap.vid.mso.model.InstanceGroupInstantiationRequestDetails;
import org.onap.vid.mso.model.ModelInfo;
import org.onap.vid.mso.model.NetworkInstantiationRequestDetails;
import org.onap.vid.mso.model.ServiceDeletionRequestDetails;
import org.onap.vid.mso.model.ServiceInstantiationRequestDetails;
import org.onap.vid.mso.model.ServiceInstantiationRequestDetails.UserParamNameAndValue;
import org.onap.vid.mso.model.VfModuleMacro;
import org.onap.vid.mso.model.VfModuleOrVolumeGroupRequestDetails;
import org.onap.vid.mso.model.VnfInstantiationRequestDetails;
import org.onap.vid.mso.model.VolumeGroupRequestDetails;
import org.onap.vid.properties.Features;
import org.onap.vid.testUtils.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@ContextConfiguration(classes = {DataSourceConfig.class, SystemProperties.class, MockedAaiClientAndFeatureManagerConfig.class})
public class MsoRequestBuilderTest extends AsyncInstantiationBaseTest {

    @Inject
    private DataAccessService dataAccessService;

    @Mock
    private JobAdapter jobAdapterMock;

    @Mock
    private JobsBrokerService jobsBrokerServiceMock;

    private AsyncInstantiationRepository asyncInstantiationRepository;

    private AuditService auditService;

    @Autowired
    private SessionFactory sessionFactory;

    private AsyncInstantiationBusinessLogicImpl asyncInstantiationBL;

    private MsoRequestBuilder msoRequestBuilder;


    @BeforeClass
    void initServicesInfoService() {
        MockitoAnnotations.initMocks(this);
        AsyncInstantiationRepository realAsyncInstantiationRepository = new AsyncInstantiationRepository(dataAccessService);
        asyncInstantiationRepository = spy(realAsyncInstantiationRepository);

        auditService = new AuditServiceImpl(null, asyncInstantiationRepository);

        AsyncInstantiationBusinessLogicImpl realAsyncInstantiationBL = new AsyncInstantiationBusinessLogicImpl(jobAdapterMock, jobsBrokerServiceMock, sessionFactory, aaiClient, featureManager, cloudOwnerService, asyncInstantiationRepository, auditService);
        asyncInstantiationBL = Mockito.spy(realAsyncInstantiationBL);

        msoRequestBuilder = new MsoRequestBuilder(asyncInstantiationBL, cloudOwnerService, aaiClient, featureManager);

        createInstanceParamsMaps();
    }

    @Test(dataProvider = "pauseAndInstanceParams", enabled = false) //Test is irrelevant with unique names feature
    public void createMacroServiceInstantiationMsoRequest(Boolean isPause, HashMap<String, String> vfModuleInstanceParamsMap, List vnfInstanceParams) throws Exception {
        ServiceInstantiation serviceInstantiationPayload = generateMacroMockServiceInstantiationPayload(isPause, createVnfList(vfModuleInstanceParamsMap, vnfInstanceParams, true));
        final URL resource = this.getClass().getResource("/payload_jsons/bulk_macro_service_request.json");
        RequestDetailsWrapper<ServiceInstantiationRequestDetails> result =
                msoRequestBuilder.generateMacroServiceInstantiationRequest(null, serviceInstantiationPayload, serviceInstantiationPayload.getInstanceName(), "az2016");
        String expected = IOUtils.toString(resource, "UTF-8");
        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    @Test
    public void createServiceInfo_WithUserProvidedNamingFalse_ServiceInfoIsAsExpected() throws IOException {
        createMacroServiceInfo_WithUserProvidedNamingFalse_ServiceInfoIsAsExpected(true,
                false);
    }

    @Test
    public void createServiceInfo_WithUserProvidedNamingFalseAndNoVfmodules_ServiceInfoIsAsExpected() throws IOException {
        createMacroServiceInfo_WithUserProvidedNamingFalse_ServiceInfoIsAsExpected(false, false);
    }

    @Test
    public void shouldCreateServiceInfoWithHomingSolutionDisabled() throws IOException {
        doReturn(true).when(featureManager).isActive(Features.FLAG_DISABLE_HOMING);

        createMacroServiceInfo_WithUserProvidedNamingFalse_ServiceInfoIsAsExpected(true, true);
    }

    private void createMacroServiceInfo_WithUserProvidedNamingFalse_ServiceInfoIsAsExpected(boolean withVfmodules, boolean disabledHoming) throws IOException {

        ServiceInstantiation serviceInstantiationPayload = generateMockMacroServiceInstantiationPayload(true,
                createVnfList(vfModuleInstanceParamsMapWithParamsToRemove, EMPTY_LIST, false),
                1,
                false, PROJECT_NAME, true);
        URL resource;
        if (disabledHoming) {
            resource = this.getClass().getResource("/payload_jsons/bulk_service_no_homing.json");
        } else if (withVfmodules) {
            resource = this.getClass().getResource("/payload_jsons/bulk_service_request_ecomp_naming.json");
        } else {
            // remove the vf modules
            serviceInstantiationPayload.getVnfs().values().forEach(vnf -> vnf.getVfModules().clear());
            resource = this.getClass().getResource("/payload_jsons/bulk_service_request_no_vfmodule_ecomp_naming.json");
        }

        RequestDetailsWrapper<ServiceInstantiationRequestDetails> result =
                msoRequestBuilder.generateMacroServiceInstantiationRequest(null, serviceInstantiationPayload, serviceInstantiationPayload.getInstanceName(), "az2016");

        String expected = IOUtils.toString(resource, "UTF-8");
        assertThat(result, jsonEquals(expected));
    }

    @Test
    public void createALaCarteService_WithUserProvidedNamingFalse_RequestDetailsIsAsExpected() throws IOException {
        ServiceInstantiation serviceInstantiationPayload = generateMockALaCarteServiceInstantiationPayload(false,
                newHashMap(),
                newHashMap(),
                newHashMap(),
                1,
                false, PROJECT_NAME, true, null);

        RequestDetailsWrapper<ServiceInstantiationRequestDetails> result =
                msoRequestBuilder.generateALaCarteServiceInstantiationRequest(serviceInstantiationPayload, serviceInstantiationPayload.getInstanceName(), "az2016");

        URL resource = this.getClass().getResource("/payload_jsons/bulk_alacarte_service_request_naming_false.json");
        String expected = IOUtils.toString(resource, "UTF-8");
        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    @Test
    public void generateALaCarteServiceInstantiationRequest_withVnfList_HappyFllow() throws IOException {
        ServiceInstantiation serviceInstantiationPayload = generateALaCarteWithVnfsServiceInstantiationPayload();
        RequestDetailsWrapper<ServiceInstantiationRequestDetails> result =
                msoRequestBuilder.generateALaCarteServiceInstantiationRequest(serviceInstantiationPayload, serviceInstantiationPayload.getInstanceName(), "az2016");

        String serviceExpected = IOUtils.toString(this.getClass().getResource("/payload_jsons/bulk_alacarte_service_request.json"), "UTF-8");
        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(serviceExpected, result);
    }

    @Test
    public void generateALaCarteServiceInstantiationRequest_verifyRequestIsAsExpected() throws IOException {
        ServiceInstantiation serviceInstantiationPayload = generateALaCarteServiceInstantiationPayload();
        final URL resource = this.getClass().getResource("/payload_jsons/bulk_alacarte_service_request.json");
        RequestDetailsWrapper<ServiceInstantiationRequestDetails> result =
                msoRequestBuilder.generateALaCarteServiceInstantiationRequest(serviceInstantiationPayload, serviceInstantiationPayload.getInstanceName(), "az2016");
        String expected = IOUtils.toString(resource, "UTF-8");
        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    @Test
    public void generateALaCarteServiceDeletionRequest_verifyRequestIsAsExpected() throws IOException {
        String expected = generateServiceDeletionRequest(true, "VNF_API");

        ServiceInstantiation serviceDeletionPayload = generateALaCarteServiceDeletionPayload();
        RequestDetailsWrapper<ServiceDeletionRequestDetails> result =
                msoRequestBuilder.generateServiceDeletionRequest(serviceDeletionPayload, "az2016");

        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    private ServiceInstantiation generateALaCarteServiceDeletionPayload() {
        return generateMockAlaCarteServiceDeletionPayload(false, EMPTY_MAP, EMPTY_MAP, EMPTY_MAP, 1, true, PROJECT_NAME, false, "VNF_API", "1234567890");
    }

    @Test
    public void generateServiceDeletionRequest_verifyRequestIsAsExpected() throws IOException {
        String expected = generateServiceDeletionRequest(false, null);

        ServiceInstantiation serviceDeletionPayload = generateServiceDeletionPayload();
        RequestDetailsWrapper<ServiceDeletionRequestDetails> result =
                msoRequestBuilder.generateServiceDeletionRequest(serviceDeletionPayload, "az2016");

        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    private String generateServiceDeletionRequest(boolean isAlaCarte, String testApi) {
        String expected = TestUtils.readFileAsString("/payload_jsons/bulk_alacarte_service_deletion_request.json");
        expected = expected.replace("[TEST_API]", String.valueOf(testApi));
        expected = expected.replace("[IS_ALACARTE]", Boolean.toString(isAlaCarte));
        return expected;
    }

    private ServiceInstantiation generateServiceDeletionPayload() {
        return generateMockServiceDeletionPayload(false, EMPTY_MAP, EMPTY_MAP, EMPTY_MAP, 1, true, PROJECT_NAME, false, "VNF_API", "1234567890");
    }

    @DataProvider
    public static Object[][] createVnfParameters() {
        return new Object[][]{
                {true, true, "/payload_jsons/vnf/bulk_vnf_request.json"},
                {false, true, "/payload_jsons/vnf/bulk_vnf_request_without_cloud_owner.json"},
                {true, false, "/payload_jsons/vnf/bulk_vnf_request_without_instance_name.json"},
        };
    }

    @Test(dataProvider = "createVnfParameters")
    public void createVnfRequestDetails_detailsAreAsExpected(boolean isFlagAddCloudOwnerActive, boolean isUserProvidedNaming, String expectedFile) throws IOException {
        final List<Vnf> vnfList = new ArrayList<>(createVnfList(new HashMap<>(), null, isUserProvidedNaming, true).values());
        createVnfPayloadAndAssert(vnfList.get(0), isFlagAddCloudOwnerActive, expectedFile);
    }

    @DataProvider
    public static Object[][] inputVnfEndExpectedResult() {
        return new Object[][]{
                {"/payload_jsons/vnf/vnf_without_lob_user_input.json", "/payload_jsons/vnf/vnf_without_lob_expected.json"},
        };
    }

    @Test(dataProvider = "inputVnfEndExpectedResult")
    public void createVnfRequestFromJson_andCompre(String userInputFile, String expectedFile) throws IOException {
        Vnf vnf = TestUtils.readJsonResourceFileAsObject(userInputFile, Vnf.class);
        createVnfPayloadAndAssert(vnf, true, expectedFile);
    }

    private void createVnfPayloadAndAssert(Vnf vnf, boolean isFlagAddCloudOwnerActive, String expectedFile) throws IOException {
        ModelInfo siModelInfo = createServiceModelInfo();
        String serviceInstanceId = "aa3514e3-5a33-55df-13ab-12abad84e7aa";
        Mockito.reset(aaiClient);
        mockAaiClientAaiStatusOK();
        enableAddCloudOwnerOnMsoRequest(isFlagAddCloudOwnerActive);
        final RequestDetailsWrapper<VnfInstantiationRequestDetails> result = msoRequestBuilder.generateVnfInstantiationRequest(vnf, siModelInfo, serviceInstanceId, "pa0916", "VNF_API");
        String expected = IOUtils.toString(this.getClass().getResource(expectedFile), "UTF-8");
        assertThat(result, jsonEquals(expected).when(IGNORING_ARRAY_ORDER));
    }

    @DataProvider
    public static Object[][] testBuildVnfInstanceParamsDataProvider(Method test) {
        return new Object[][]{
                {
                        EMPTY_LIST,
                        ImmutableList.of(
                                ImmutableList.of(ImmutableMap.of("k1", "v1", "k2", "v2")),
                                ImmutableList.of(ImmutableMap.of("k3", "v3", "k2", "v2"))
                        ),
                        ImmutableList.of(ImmutableMap.of("k1", "v1", "k2", "v2", "k3", "v3"))
                },
                {
                        ImmutableList.of(ImmutableMap.of("j1", "w1", "k1", "v1", "vnf_name", "w2", "vf_module_name", "w3")), //vnf_name, vf_module_name are excluded
                        ImmutableList.of(
                                ImmutableList.of(ImmutableMap.of("k1", "v1", "k2", "v2")),
                                ImmutableList.of(ImmutableMap.of("k3", "v3", "k2", "v2")),
                                ImmutableList.of(EMPTY_MAP),
                                singletonList(null)
                        ),
                        ImmutableList.of(ImmutableMap.of("k1", "v1", "k2", "v2", "k3", "v3", "j1", "w1"))
                },
                {
                        EMPTY_LIST,
                        Arrays.asList(null, null),
                        EMPTY_LIST //mso is expect to empty list and not list with empty map
                },
                {
                        ImmutableList.of(EMPTY_MAP),
                        ImmutableList.of(
                                ImmutableList.of(EMPTY_MAP),
                                ImmutableList.of(EMPTY_MAP)
                        ),
                        EMPTY_LIST //mso is expect to empty list and not list with empty map
                }
        };
    }

    @Test(dataProvider = "testBuildVnfInstanceParamsDataProvider")
    public void testBuildVnfInstanceParams(List<Map<String, String>> currentVnfInstanceParams,
                                           List<List<Map<String, String>>> vfModulesInstanceParams,
                                           List<Map<String, String>> expectedResult) {
        List<VfModuleMacro> vfModules =
                vfModulesInstanceParams.stream().map(params -> new VfModuleMacro(new ModelInfo(), null, null, params)).collect(Collectors.toList());
        List<Map<String, String>> actual = msoRequestBuilder.buildVnfInstanceParams(currentVnfInstanceParams, vfModules);
        assertThat(actual, equalTo(expectedResult));
    }

    @DataProvider
    public static Object[][] vfModuleRequestDetails(Method test) {
        return new Object[][]{
                {"cc3514e3-5a33-55df-13ab-12abad84e7cc", true, false, "/payload_jsons/vfmodule_instantiation_request.json"},
                {null, true, null, "/payload_jsons/vfmodule_instantiation_request_without_volume_group.json"},
                {null, false, true, "/payload_jsons/vfmodule_instantiation_request_without_instance_name.json"}
        };
    }

    @Test(dataProvider = "vfModuleRequestDetails")
    public void createVfModuleRequestDetails_detailsAreAsExpected(String volumeGroupInstanceId, boolean isUserProvidedNaming, Boolean usePreload, String fileName) throws IOException {

        ModelInfo siModelInfo = createServiceModelInfo();
        ModelInfo vnfModelInfo = createVnfModelInfo(true);
        List<Map<String, String>> instanceParams = ImmutableList.of(ImmutableMap.of("vmx_int_net_len", "24",
                "vre_a_volume_size_0", "120"));
        List<UserParamNameAndValue> supplementaryParams = ImmutableList.of(
            new UserParamNameAndValue("vre_a_volume_size_0", "100"),
            new UserParamNameAndValue("availability_zone_0", "mtpocdv-kvm-az01")
        );

        VfModule vfModule = createVfModule("201673MowAvpnVpeBvL..AVPN_vRE_BV..module-1", "56e2b103-637c-4d1a-adc8-3a7f4a6c3240",
                "72d9d1cd-f46d-447a-abdb-451d6fb05fa8", instanceParams, supplementaryParams,
                (isUserProvidedNaming ? "vmxnjr001_AVPN_base_vRE_BV_expansion" : null), "myVgName", true, usePreload);

        String serviceInstanceId = "aa3514e3-5a33-55df-13ab-12abad84e7aa";
        String vnfInstanceId = "bb3514e3-5a33-55df-13ab-12abad84e7bb";

        Mockito.reset(aaiClient);
        mockAaiClientAaiStatusOK();
        enableAddCloudOwnerOnMsoRequest();
        when(aaiClient.isNodeTypeExistsByName(eq("vmxnjr001_AVPN_base_vRE_BV_expansion"), eq(ResourceType.VF_MODULE))).thenReturn(false);

        String expected = IOUtils.toString(this.getClass().getResource(fileName), "UTF-8");
        final RequestDetailsWrapper<VfModuleOrVolumeGroupRequestDetails> result = msoRequestBuilder.generateVfModuleInstantiationRequest(
                vfModule, siModelInfo, serviceInstanceId,
                vnfModelInfo, vnfInstanceId, volumeGroupInstanceId, "pa0916", "VNF_API");
        assertThat(result, jsonEquals(expected).when(IGNORING_ARRAY_ORDER));
    }

    @Test
    public void createVolumeGroup_verifyResultAsExpected() throws IOException {
        final URL resource = this.getClass().getResource("/payload_jsons/volumegroup_instantiation_request.json");
        VfModule vfModule = createVfModule("201673MowAvpnVpeBvL..AVPN_vRE_BV..module-1",
                "56e2b103-637c-4d1a-adc8-3a7f4a6c3240",
                "72d9d1cd-f46d-447a-abdb-451d6fb05fa8",
                emptyList(),
                emptyList(),
                "vmxnjr001_AVPN_base_vRE_BV_expansion",
                "myVgName",
                true,
                true);
        vfModule.getModelInfo().setModelInvariantId("ff5256d2-5a33-55df-13ab-12abad84e7ff");
        vfModule.getModelInfo().setModelVersion("1");
        ModelInfo vnfModelInfo = createVnfModelInfo(true);
        RequestDetailsWrapper<VolumeGroupRequestDetails> result =
                msoRequestBuilder.generateVolumeGroupInstantiationRequest(vfModule,
                        createServiceModelInfo(),
                        "ff3514e3-5a33-55df-13ab-12abad84e7ff",
                        vnfModelInfo,
                        "vnfInstanceId",
                        "az2016",
                        "VNF_API");
        String expected = IOUtils.toString(resource, "UTF-8");
        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    @DataProvider
    public static Object[][] expectedNetworkRequestDetailsParameters() {
        return new Object[][]{
                {VNF_NAME, "/payload_jsons/network_instantiation_request.json"},
                {null, "/payload_jsons/network_instantiation_request_without_instance_name.json"}
        };
    }

    @Test(dataProvider = "expectedNetworkRequestDetailsParameters")
    public void createNetworkRequestDetails_detailsAreAsExpected(String networkName, String filePath) throws IOException {

        List<NetworkDetails> networkDetails = singletonList(new NetworkDetails(networkName, "ab153b6e-c364-44c0-bef6-1f2982117f04"));
        final List<Network> networksList = new ArrayList<>(createNetworkList(null, networkDetails, true).values());
        ModelInfo siModelInfo = createServiceModelInfo();
        String serviceInstanceId = "aa3514e3-5a33-55df-13ab-12abad84e7aa";

        Mockito.reset(aaiClient);
        mockAaiClientAaiStatusOK();
        enableAddCloudOwnerOnMsoRequest();

        String expected = IOUtils.toString(this.getClass().getResource(filePath), "UTF-8");
        final RequestDetailsWrapper<NetworkInstantiationRequestDetails> result = msoRequestBuilder.generateNetworkInstantiationRequest(networksList.get(0), siModelInfo, serviceInstanceId, "pa0916", "VNF_API");
        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    @Test
    public void createInstanceGroupRequestDetails_detailsAreAsExpected() throws IOException {

        final InstanceGroup instanceGroup = createInstanceGroup(true, Action.Create);
        ModelInfo siModelInfo = createServiceModelInfo();
        String serviceInstanceId = "aa3514e3-5a33-55df-13ab-12abad84e7aa";

        Mockito.reset(aaiClient);
        mockAaiClientAaiStatusOK();
        enableAddCloudOwnerOnMsoRequest();

        String expected = IOUtils.toString(this.getClass().getResource("/payload_jsons/instance_group_instantiation_request.json"), "UTF-8");
        final RequestDetailsWrapper<InstanceGroupInstantiationRequestDetails> result = msoRequestBuilder.generateInstanceGroupInstantiationRequest(instanceGroup, siModelInfo, serviceInstanceId, "az2018", "VNF_API");
        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    @Test
    public void generateAddOrRemoveInstanceGroupMemberRequest_verifyResultAsExpected() throws IOException {
        String expected = " {" +
                "   \"requestDetails\": {" +
                "     \"requestInfo\": {" +
                "       \"source\": \"VID\"," +
                "       \"requestorId\": \"az2018\"" +
                "     }," +
                "     \"relatedInstanceList\": [" +
                "        {" +
                "           \"relatedInstance\": {" +
                "              \"instanceId\": \"aa3514e3-5a33-55df-13ab-12abad84e7aa\"," +
                "              \"modelInfo\": {" +
                "                 \"modelType\": \"vnf\"" +
                "              }" +
                "           }" +
                "        }" +
                "     ]" +
                "   }" +
                " }";
        RequestDetailsWrapper<AddOrRemoveInstanceGroupMemberRequestDetails> result =
                msoRequestBuilder.generateInstanceGroupMemberRequest("aa3514e3-5a33-55df-13ab-12abad84e7aa", "az2018");
        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    @Test
    public void checkIfNullProjectNameSentToMso() {
        ServiceInstantiation serviceInstantiationPayload = generateMockMacroServiceInstantiationPayload(true,
                createVnfList(vfModuleInstanceParamsMapWithParamsToRemove, EMPTY_LIST, false),
                1,
                false, null, false);
        RequestDetailsWrapper<ServiceInstantiationRequestDetails> result =
                msoRequestBuilder.generateMacroServiceInstantiationRequest(null, serviceInstantiationPayload, serviceInstantiationPayload.getInstanceName(), "az2016");
        JsonNode jsonNode = new ObjectMapper().valueToTree(result.requestDetails);
        Assert.assertTrue(jsonNode.get("project").isNull());
        serviceInstantiationPayload = generateMockMacroServiceInstantiationPayload(true,
                createVnfList(vfModuleInstanceParamsMapWithParamsToRemove, EMPTY_LIST, false),
                1,
                false, "not null", false);
        result = msoRequestBuilder.generateMacroServiceInstantiationRequest(null, serviceInstantiationPayload, serviceInstantiationPayload.getInstanceName(), "az2016");
        jsonNode = new ObjectMapper().valueToTree(result.requestDetails);
        Assert.assertTrue(jsonNode.get("project").get("projectName").asText().equalsIgnoreCase("not null"));
    }

    @Test
    public void generateDeleteVnfRequest_verifyResultAsExpected() throws IOException {
        String expected =
                "{ " +
                        "\"requestDetails\" : {\n" +
                        "    \"modelInfo\" : {\n" +
                        "      \"modelCustomizationName\" : \"2016-73_MOW-AVPN-vPE-BV-L 0\",\n" +
                        "      \"modelCustomizationId\" : \"ab153b6e-c364-44c0-bef6-1f2982117f04\",\n" +
                        "      \"modelInvariantId\" : \"11111111-f63c-463e-ba94-286933b895f9\",\n" +
                        "      \"modelVersionId\" : \"7f40c192-f63c-463e-ba94-286933b895f8\",\n" +
                        "      \"modelName\" : \"2016-73_MOW-AVPN-vPE-BV-L\",\n" +
                        "      \"modelType\" : \"vnf\",\n" +
                        "      \"modelVersion\" : \"10.0\"\n" +
                        "    },\n" +
                        "    \"cloudConfiguration\" : {\n" +
                        "      \"lcpCloudRegionId\" : \"AAIAIC25\",\n" +
                        "      \"tenantId\" : \"092eb9e8e4b7412e8787dd091bc58e86\",\n" +
                        "      \"cloudOwner\" : \"irma-aic\"\n" +
                        "    },\n" +
                        "    \"requestInfo\" : {\n" +
                        "      \"source\" : \"VID\",\n" +
                        "      \"requestorId\" : \"az2018\"\n" +
                        "    }\n" +
                        "  }" +
                        "}";
        Vnf vnfDetails = new Vnf(createVnfModelInfo(true), "productFamily", "instanceName", Action.Delete.name(), "platform", "AAIAIC25", null,
                "092eb9e8e4b7412e8787dd091bc58e86", null, null, false, "VNF_INSTANCE_ID", null, UUID.randomUUID().toString(), null, null,
            null, "originalName");
        RequestDetailsWrapper<VnfInstantiationRequestDetails> result =
                msoRequestBuilder.generateDeleteVnfRequest(vnfDetails, "az2018");
        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    @Test
    public void generateDeleteVfModuleRequest_verifyResultAsExpected() throws IOException {
        String expected =
                "{ " +
                        "\"requestDetails\" : {" +
                        "    \"modelInfo\" : {" +
                        "      \"modelCustomizationName\" : \"201673MowAvpnVpeBvL..AVPN_base_vPE_BV..module-0\"," +
                        "      \"modelCustomizationId\" : \"a25e8e8c-58b8-4eec-810c-97dcc1f5cb7f\"," +
                        "      \"modelVersionId\" : \"4c75f813-fa91-45a4-89d0-790ff5f1ae79\"," +
                        "      \"modelInvariantId\" : \"22222222-f63c-463e-ba94-286933b895f9\"," +
                        "      \"modelName\" : \"201673MowAvpnVpeBvL..AVPN_base_vPE_BV..module-0\"," +
                        "      \"modelType\" : \"vfModule\"," +
                        "      \"modelVersion\" : \"10.0\"" +
                        "    }," +
                        "    \"cloudConfiguration\" : {" +
                        "      \"lcpCloudRegionId\" : \"mdt1\"," +
                        "      \"tenantId\" : \"88a6ca3ee0394ade9403f075db23167e\"," +
                        "      \"cloudOwner\" : \"irma-aic\"" +
                        "    }," +
                        "    \"requestInfo\" : {" +
                        "      \"source\" : \"VID\"," +
                        "      \"requestorId\" : \"az2018\"" +
                        "    }" +
                        "  }" +
                        "}";
        VfModule vfModuleDetails = createVfModule("201673MowAvpnVpeBvL..AVPN_base_vPE_BV..module-0", VF_MODULE_0_MODEL_VERSION_ID, VF_MODULE_0_MODEL_CUSTOMIZATION_NAME,
            null, emptyList(), "vmxnjr001_AVPN_base_vPE_BV_base", null, true, true);
        RequestDetailsWrapper<VfModuleOrVolumeGroupRequestDetails> result =
                msoRequestBuilder.generateDeleteVfModuleRequest(vfModuleDetails, "az2018");
        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    @Test(dataProvider = "cloudConfigurationPermutations")
    public void createPre1806TransportServiceInstantiationMsoRequest(String tenantId, String lcpCloudRegionId, String jsonFile) throws IOException {
        ServiceInstantiation serviceInstantiationPayload = generatePre1806MacroTransportServiceInstantiationPayload(tenantId, lcpCloudRegionId);

        final URL resource = this.getClass().getResource(jsonFile);
        RequestDetailsWrapper<ServiceInstantiationRequestDetails> result =
                msoRequestBuilder.generateMacroServicePre1806InstantiationRequest(serviceInstantiationPayload, "az2016");
        String expected = IOUtils.toString(resource, "UTF-8");
        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    @DataProvider
    public static Object[][] cloudConfigurationPermutations() {
        return new Object[][]{
                {"88a6ca3ee0394ade9403f075db23167e", "mdt1", "/payload_jsons/pre_1806_macro_service_instantiation_request.json"},
                {"", "mdt1", "/payload_jsons/pre_1806_macro_without_cloudConfiguration.json"},
                {"88a6ca3ee0394ade9403f075db23167e", "", "/payload_jsons/pre_1806_macro_without_cloudConfiguration.json"},
                {null, null, "/payload_jsons/pre_1806_macro_without_cloudConfiguration.json"}
        };
    }

    @Test
    public void createPre1806MacroWithVrfEntry() throws IOException {
        ServiceInstantiation serviceInstantiationPayload = TestUtils.readJsonResourceFileAsObject(
                "/payload_jsons/vrfEntry/service_with_vrf_entry_fe_input.json",
                ServiceInstantiation.class);

        when(featureManager.isActive(Features.FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST)).thenReturn(true);
        when(aaiClient.getCloudOwnerByCloudRegionId("lcpCloudRegionId")).thenReturn("irma-aic");

        RequestDetailsWrapper<ServiceInstantiationRequestDetails> result =
                msoRequestBuilder.generateMacroServicePre1806InstantiationRequest(serviceInstantiationPayload, "az2016");

        final URL url = this.getClass().getResource("/payload_jsons/vrfEntry/service_with_vrf_instantiation_request.json");
        String expected = IOUtils.toString(url, "UTF-8");

        assertThat(result, jsonEquals(expected).when(IGNORING_ARRAY_ORDER));
    }

    @Test(dataProvider = "trueAndFalseAndNull", dataProviderClass = TestUtils.class)
    public void generateReplaceVfModuleRequest_whenRetainAssignmentsProvidedFromFrontend_retainAssignmentsToMsoIsTheSame(Boolean retainAssignments) {
        assertThat(generatedVfModuleReplaceRequest(retainAssignments, null, null),
            jsonPartEqualsOrUndefined(
                "requestDetails.requestParameters.retainAssignments", retainAssignments));
    }

    @Test(dataProvider = "trueAndFalseAndNull", dataProviderClass = TestUtils.class)
    public void generateReplaceVfModuleRequest_whenRetainVolumeGroupIsGiven_rebuildVolumeGroupIsNegated(Boolean retainVolumeGroups) {
        assertThat(generatedVfModuleReplaceRequest(null, retainVolumeGroups, null),
            jsonPartEqualsOrUndefined("requestDetails.requestParameters.rebuildVolumeGroups", BooleanUtils.negate(retainVolumeGroups)));
    }

    private <T> ConfigurableJsonMatcher<T> jsonPartEqualsOrUndefined(String path, Boolean expected) {
        return (expected != null)
            ? jsonPartEquals(path, expected)
            : jsonNodeAbsent(path);
    }

    @Test
    public void generateReplaceVfModuleRequest_whenThereAreSupplementaryParams_thenTheyAreAddToUserParams() {

        String expectedParams = "["
            + "        {"
            + "          \"name\": \"vre_a_volume_size_0\","
            + "          \"value\": \"100\""
            + "        },"
            + "        {"
            + "          \"name\": \"vmx_int_net_len\","
            + "          \"value\": \"24\""
            + "        },"
            + "        {"
            + "          \"name\": \"availability_zone_0\","
            + "          \"value\": \"abc\""
            + "        }"
            + "     ]";

        List<UserParamNameAndValue> supplementaryParams = ImmutableList.of(
            new UserParamNameAndValue( "vre_a_volume_size_0", "100"),
            new UserParamNameAndValue("vmx_int_net_len", "24"),
            new UserParamNameAndValue("availability_zone_0", "abc")
        );

        assertThat(generatedVfModuleReplaceRequest(null, null, supplementaryParams),
            jsonPartEquals("requestDetails.requestParameters.userParams", expectedParams));
    }

    @Test
    public void generateReplaceVfModuleRequest_verifyResultAsExpected() {
        Boolean retainVolumeGroups = null;
        Boolean retainAssignments = null;

        String expected = TestUtils.readFileAsString("/payload_jsons/vfmodule/replace_vfmodule__payload_to_mso.json");
        assertThat(generatedVfModuleReplaceRequest(retainAssignments, retainVolumeGroups, null), jsonEquals(expected).when(IGNORING_ARRAY_ORDER));
    }

    @Test
    public void generateReplaceVfModuleRequestWithoutCloudConfiguration_verifyResultAsExpected() {
        String expected = TestUtils.readFileAsString("/payload_jsons/vfmodule/replace_vfmodule__payload_to_mso.json");
        assertThat(generatedVfModuleReplaceRequestWithOrWithoutCloudConfiguration(null, null, null, true), jsonNodeAbsent("requestDetails.cloudConfiguration"));
        assertThat(generatedVfModuleReplaceRequestWithOrWithoutCloudConfiguration(null, null, null, true), jsonEquals(expected).whenIgnoringPaths("requestDetails.cloudConfiguration").when(IGNORING_ARRAY_ORDER));
    }

    private RequestDetailsWrapper<VfModuleOrVolumeGroupRequestDetails> generatedVfModuleReplaceRequest(
        Boolean retainAssignments, Boolean retainVolumeGroups, List<UserParamNameAndValue> supplementaryParams) {
        return generatedVfModuleReplaceRequestWithOrWithoutCloudConfiguration(retainAssignments, retainVolumeGroups, supplementaryParams, false);
    }

    private RequestDetailsWrapper<VfModuleOrVolumeGroupRequestDetails> generatedVfModuleReplaceRequestWithOrWithoutCloudConfiguration(
            Boolean retainAssignments, Boolean retainVolumeGroups, List<UserParamNameAndValue> supplementaryParams, Boolean noHomingData) {
        when(featureManager.isActive(Features.FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST)).thenReturn(true);
        when(aaiClient.getCloudOwnerByCloudRegionId("regionOne")).thenReturn("irma-aic");

        ModelInfo vfModuleModelInfo = createVfModuleModelInfo("newest-model-name-vfm", "newest-model-version-vfm", "newest-model-uuid-vfm",
                "f7a867f2-596b-4f4a-a128-421e825a6190", "newest-model-customization-uuid-vfm","newest-model-customization-name-vfm" );

        VfModule vfModuleDetails = createVfModuleForReplace(vfModuleModelInfo, "replace_module", noHomingData? null : "regionOne", noHomingData? null :"0422ffb57ba042c0800a29dc85ca70f8",
                retainAssignments, retainVolumeGroups, supplementaryParams);

        ModelInfo serviceModelInfo = createServiceModelInfo("newest-model-name-service", "newest-model-version-service", "newest-model-uuid-service", "b16a9398-ffa3-4041-b78c-2956b8ad9c7b", null, null );

        ModelInfo vnfModelInfo = createVnfModelInfo("newest-model-name-vnf", "newest-model-version-vnf", "newest-model-uuid-vnf", "23122c9b-dd7f-483f-bf0a-e069303db2f7", "newest-model-customization-uuid-vnf", "newest-model-customization-name-vnf" );

        return msoRequestBuilder.generateVfModuleReplaceRequest(vfModuleDetails, serviceModelInfo,
                "e9993045-cc96-4f3f-bf9a-71b2a400a956", vnfModelInfo,
                "5c9c2896-1fe6-4055-b7ec-d0a01e5f9bf5", null, "az2016", "GR_API"
        );
    }
}
