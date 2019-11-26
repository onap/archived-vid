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

package org.onap.vid.api;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonPartEquals;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonStringEquals;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.onap.simulator.presetGenerator.presets.BasePresets.BaseSDCPreset.SDC_ROOT_PATH;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.APPEND;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET;
import static vid.automation.test.services.SimulatorApi.registerExpectation;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPresets;
import static vid.automation.test.utils.ReadFile.loadResourceAsString;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceMetadataGet;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceToscaModelGet;
import org.onap.vid.more.LoggerFormatTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import vid.automation.test.infra.FeatureTogglingTest;
import vid.automation.test.infra.Features;
import vid.automation.test.infra.ModelInfo;

public class SdcApiTest extends BaseApiTest {

    private static final String SDC_GET_SERVICE_MODEL = "/rest/models/services/";


    private static final String A_LA_CARTE_INSTANTIATION_TYPE_UUID = "4d71990b-d8ad-4510-ac61-496288d9078e";
    private static final String A_LA_CARTE_INSTANTIATION_TYPE_INVARIANT_UUID = "d27e42cf-087e-4d31-88ac-6c4b7585f800";
    private static final String A_LA_CARTE_INSTANTIATION_TYPE_FILE_PATH = "csar15782222_instantiationTypeAlacarte_invariantUUIDMacro.zip";
    private static final String A_LA_CARTE_INSTANTIATION_TYPE_EXPECTED_RESPONSE = "sdcApiTest/aLaCarteInstantiationTypeResponse.json";


    private static final String MACRO_INSTANTIATION_TYPE_FILE_PATH = "csar15782222_instantiationTypeMacro_invariantUUIDAlacarte.zip";
    private static final String MACRO_INSTANTIATION_TYPE_UUID = "4d71990b-d8ad-4510-ac61-496288d9078e";
    private static final String MACRO_INSTANTIATION_TYPE_INVARIANT_UUID = "a8dcd72d-d44d-44f2-aa85-53aa9ca99cba";
    private static final String MACRO_INSTANTIATION_TYPE_EXPECTED_RESPONSE = "sdcApiTest/macroInstantiationTypeResponse.json";


    private static final String EMPTY_INSTANTIATION_TYPE_FILE_PATH = "csar15782222_instantiationTypeEmpty_invariantUUIDAlacarte.zip";
    private static final String EMPTY_INSTANTIATION_TYPE_EXPECTED_RESPONSE = "sdcApiTest/emptyInstantiationTypeResponse.json";

    private static final String BOTH_INSTANTIATION_TYPE_FILE_PATH = "csar15782222_instantiationTypeBoth_invariantUUIDAlacarte.zip";


    private static final String MIN_MAX_INITIAL_UUID = "43f13072-fe50-496b-b673-7af075d10143";
    private static final String MIN_MAX_INITIAL_INVARIANT_UUID = "35fb95d8-d1f0-4e46-99ac-e01b423e8e3f";
    private static final String MIN_MAX_INITIAL_FILE_PATH = "min_max_initial_vfModule_csar_v4.0.zip";

    private static final String MIN_MAX_INITIAL_UUID_OLD_CSAR = "245562de-3984-49ef-a708-6c9d7cfcabd1";
    private static final String MIN_MAX_INITIAL_INVARIANT_UUID_OLD_CSAR = "24216d6-71d0-41c8-ac81-0c5acfee514a";
    private static final String MIN_MAX_INITIAL_FILE_PATH_OLD_CSAR = "service-VflorenceRvpmsFeAic3011217Svc-csar.csar.zip";

    private static final String GROUPING_SERVICE_ROLE_FILE_PATH = "csar15782222_instantiationTypeAlacarte_VnfGrouping.zip";
    private static final String GROUPING_SERVICE_ROLE_UUID = "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc";
    private static final String GROUPING_SERVICE_ROLE_INVARIANT_UUID = "7ee41ce4-4827-44b0-a48e-2707a59905d2";
    private static final String GROUPING_SERVICE_ROLE_EXPECTED_RESPONSE = "VnfGroup/groupingServiceRoleResponse.json";

    @BeforeClass
    public void login() {
        super.login();
    }

    @BeforeMethod
    public void invalidateTheCache(){
        if(Features.FLAG_SERVICE_MODEL_CACHE.isActive()) {
            restTemplate.postForObject(uri + "/rest/models/reset", "", Object.class);
        }
    }

    @Test
    public void getServiceModelALaCarteInstantiation() {
        registerToSimulatorWithPresets(A_LA_CARTE_INSTANTIATION_TYPE_UUID, A_LA_CARTE_INSTANTIATION_TYPE_INVARIANT_UUID, A_LA_CARTE_INSTANTIATION_TYPE_FILE_PATH);
        ResponseEntity<String> response = restTemplate.getForEntity(buildUri(SDC_GET_SERVICE_MODEL + A_LA_CARTE_INSTANTIATION_TYPE_UUID), String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        String aLaCarteInstantiationTypeExpectedResponse = loadResourceAsString(A_LA_CARTE_INSTANTIATION_TYPE_EXPECTED_RESPONSE);
        assertThat(response.getBody(), jsonEquals(aLaCarteInstantiationTypeExpectedResponse)
            .when(IGNORING_ARRAY_ORDER)
            .whenIgnoringPaths("service.vidNotions.instantiationUI"));
    }


    @Test
    public void getServiceModelMacroInstantiation() {
        registerToSimulatorWithPresets(MACRO_INSTANTIATION_TYPE_UUID, MACRO_INSTANTIATION_TYPE_INVARIANT_UUID, MACRO_INSTANTIATION_TYPE_FILE_PATH);
        ResponseEntity<String> response = restTemplate.getForEntity(buildUri(SDC_GET_SERVICE_MODEL + MACRO_INSTANTIATION_TYPE_UUID), String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        String macroInstantiationTypeExpectedResponse = loadResourceAsString(MACRO_INSTANTIATION_TYPE_EXPECTED_RESPONSE);
        assertThat(response.getBody(), jsonEquals(macroInstantiationTypeExpectedResponse)
            .when(IGNORING_ARRAY_ORDER)
            .whenIgnoringPaths("service.vidNotions.viewEditUI"));
    }


    @Test
    public void getServiceModelWithoutInstantiationType(){
        registerToSimulatorWithPresets(MACRO_INSTANTIATION_TYPE_UUID, MACRO_INSTANTIATION_TYPE_INVARIANT_UUID, EMPTY_INSTANTIATION_TYPE_FILE_PATH);
        ResponseEntity<String> response = restTemplate.getForEntity(buildUri(SDC_GET_SERVICE_MODEL + MACRO_INSTANTIATION_TYPE_UUID), String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        String emptyInstantiationTypeExpectedResponse = loadResourceAsString(EMPTY_INSTANTIATION_TYPE_EXPECTED_RESPONSE);

        final String body = response.getBody();

        assertThat(body, jsonEquals(emptyInstantiationTypeExpectedResponse)
            .when(IGNORING_ARRAY_ORDER)
            .whenIgnoringPaths("service.vidNotions.instantiationUI", "service.vidNotions.instantiationType"));

        assertThat(body, jsonPartEquals("service.vidNotions.instantiationType",
            Features.FLAG_2002_IDENTIFY_INVARIANT_MACRO_UUID_BY_BACKEND.isActive()
                ? "ALaCarte" : "ClientConfig"));
    }

    @Test
    public void getServiceModelBothInstantiationType(){
        registerToSimulatorWithPresets(MACRO_INSTANTIATION_TYPE_UUID, MACRO_INSTANTIATION_TYPE_INVARIANT_UUID, BOTH_INSTANTIATION_TYPE_FILE_PATH);
        ResponseEntity<String> response = restTemplate.getForEntity(buildUri(SDC_GET_SERVICE_MODEL + MACRO_INSTANTIATION_TYPE_UUID), String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        String macroInstantiationTypeExpectedResponse = loadResourceAsString(MACRO_INSTANTIATION_TYPE_EXPECTED_RESPONSE);
        assertThat(response.getBody(), jsonEquals(macroInstantiationTypeExpectedResponse)
            .when(IGNORING_ARRAY_ORDER)
            .whenIgnoringPaths("service.vidNotions.viewEditUI"));
    }

    @Test
    public void getServiceModelWithGroupsAndCheckMinMaxInitialParams(){
        registerToSimulatorWithPresets(MIN_MAX_INITIAL_UUID, MIN_MAX_INITIAL_INVARIANT_UUID, MIN_MAX_INITIAL_FILE_PATH);
        ResponseEntity<String> response = restTemplate.getForEntity(buildUri(SDC_GET_SERVICE_MODEL + MIN_MAX_INITIAL_UUID), String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        String minMaxInitialExpectedResponse = loadResourceAsString("sdcApiTest/minMaxInitialExpectedResponse.json");
        assertThat(response.getBody(), jsonEquals(minMaxInitialExpectedResponse)
            .when(IGNORING_ARRAY_ORDER)
            .whenIgnoringPaths("service.vidNotions.instantiationUI", "service.vidNotions.instantiationType", "service.vidNotions.viewEditUI"));
    }

    @Test
    public void getServiceModelWithGroupsAndCheckMinMaxInitialParamsOldCsar(){
        registerToSimulatorWithPresets(MIN_MAX_INITIAL_UUID_OLD_CSAR, MIN_MAX_INITIAL_INVARIANT_UUID_OLD_CSAR, MIN_MAX_INITIAL_FILE_PATH_OLD_CSAR);
        ResponseEntity<String> response = restTemplate.getForEntity(buildUri(SDC_GET_SERVICE_MODEL + MIN_MAX_INITIAL_UUID_OLD_CSAR), String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        String minMaxInitialExpectedResponseOldCsar = loadResourceAsString("sdcApiTest/minMaxInitialExpectedResponseOldCsar.json");
        assertThat("The response is in the format of JSON", response.getBody(), is(jsonStringEquals(minMaxInitialExpectedResponseOldCsar)));
    }

    @Test
    @FeatureTogglingTest(Features.FLAG_1902_VNF_GROUPING)
    public void getServiceModelWithServiceRoleGrouping() throws Exception {
        registerToSimulatorWithPresets(GROUPING_SERVICE_ROLE_UUID, GROUPING_SERVICE_ROLE_INVARIANT_UUID, GROUPING_SERVICE_ROLE_FILE_PATH);
        ResponseEntity<String> response = restTemplate.getForEntity(buildUri(SDC_GET_SERVICE_MODEL + GROUPING_SERVICE_ROLE_UUID), String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        String groupingServiceRoleExpectedResponse = loadResourceAsString(GROUPING_SERVICE_ROLE_EXPECTED_RESPONSE);
        assertThat(response.getBody(), jsonEquals(groupingServiceRoleExpectedResponse)
            .when(IGNORING_ARRAY_ORDER)
            .whenIgnoringPaths("service.vidNotions.instantiationUI"));

        //assert that instantiationUI is not legacy
        JsonNode instantiationUI = objectMapper.readValue(response.getBody(), JsonNode.class).get("service").get("vidNotions").get("instantiationUI");
        assertThat(instantiationUI.asText(), not(equalTo("legacy")));
    }

    private void registerToSimulatorWithPresets(String uuid, String invariantUuid, String pathPath){
        ImmutableList<BasePreset> presets = ImmutableList.of(
                new PresetSDCGetServiceToscaModelGet(uuid, pathPath),
                new PresetSDCGetServiceMetadataGet(uuid,invariantUuid, pathPath));
        registerExpectationFromPresets(presets, CLEAR_THEN_SET);
    }

    @Test
    public void withModelFromE2eWithToscaParserButNewFlow_requestModels_expectVnfRelatedVfModulesNotNull() {

        /*
        We had a problem that this exact model vnfs returned with no vfModules and
        volumeGroups, because a 'isNewFlow' value in org.onap.vid.asdc.parser.ToscaParserImpl
        was always false because a coding error.
         */
        registerExpectation("get_sdc_catalog_services_VflorenceRvpmsFeAic3011217Svc.json", CLEAR_THEN_SET);
        registerExpectation("create_new_instance/aai_get_full_subscribers.json", APPEND);

        final JsonNode response = restTemplate.getForObject(uri + "/rest/models/services/" + "245562de-3984-49ef-a708-6c9d7cfcabd1", JsonNode.class);

        // using json-pointers instead of path, because vnf name has
        // dots and spaces
        final String myVnf = "vFLORENCEvProbe_FE_AIC3-11.2.1_VF 1";
        final String base = "/vnfs/" + myVnf;

        assertFalse(response.at(base).isMissingNode(),
                "test relies on '" + myVnf + "' to be in model; got: " + response);

        assertThat("vfModules under '" + myVnf + "' must not be empty; got: " + response,
                response.at(base + "/vfModules").size(), is(not(0)));

        assertThat("volumeGroups under '" + myVnf + "' must not be empty; got: " + response,
                response.at(base + "/volumeGroups").size(), is(not(0)));

    }

    @Test
    public void whenCallSdc_thenRequestRecordedInMetricsLog() {

        ModelInfo modelInfo = ModelInfo.transportWithPnfsService;
        String internalPath = SDC_GET_SERVICE_MODEL + modelInfo.modelVersionId;
        registerExpectationFromPresets(ImmutableList.of(
            new PresetSDCGetServiceToscaModelGet(modelInfo),
            new PresetSDCGetServiceMetadataGet(modelInfo),
            new PresetAAIGetSubscribersGet() //for read logs permissions
        ), CLEAR_THEN_SET);

        ResponseEntity<String> response = restTemplate.getForEntity(
            buildUri(internalPath), String.class);

        final String requestId = response.getHeaders().getFirst("X-ECOMP-RequestID-echo");

        LoggerFormatTest.assertHeadersAndMetricLogs(restTemplate, uri, requestId, SDC_ROOT_PATH, 2);
        LoggerFormatTest.verifyExistenceOfIncomingReqsInAuditLogs(restTemplate, uri, requestId, internalPath);
    }

}
