package org.onap.vid.api;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonStringEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.testng.Assert.assertFalse;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.APPEND;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET;
import static vid.automation.test.services.SimulatorApi.registerExpectation;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPresets;
import static vid.automation.test.utils.ReadFile.loadResourceAsString;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceMetadataGet;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceToscaModelGet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import vid.automation.test.infra.FeatureTogglingTest;
import vid.automation.test.infra.Features;

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
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        String aLaCarteInstantiationTypeExpectedResponse = loadResourceAsString(A_LA_CARTE_INSTANTIATION_TYPE_EXPECTED_RESPONSE);
        assertThat("The response is in the format of JSON", response.getBody(), is(jsonStringEquals(turnOffInstantiationUI(aLaCarteInstantiationTypeExpectedResponse))));
    }


    @Test
    public void getServiceModelMacroInstantiation() {
        registerToSimulatorWithPresets(MACRO_INSTANTIATION_TYPE_UUID, MACRO_INSTANTIATION_TYPE_INVARIANT_UUID, MACRO_INSTANTIATION_TYPE_FILE_PATH);
        ResponseEntity<String> response = restTemplate.getForEntity(buildUri(SDC_GET_SERVICE_MODEL + MACRO_INSTANTIATION_TYPE_UUID), String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        String macroInstantiationTypeExpectedResponse = loadResourceAsString(MACRO_INSTANTIATION_TYPE_EXPECTED_RESPONSE);
        assertThat("The response is in the format of JSON", response.getBody(), is(jsonStringEquals(turnOffInstantiationUI(macroInstantiationTypeExpectedResponse))));
    }


    @Test
    public void getServiceModelWithoutInstantiationType(){
        registerToSimulatorWithPresets(MACRO_INSTANTIATION_TYPE_UUID, MACRO_INSTANTIATION_TYPE_INVARIANT_UUID, EMPTY_INSTANTIATION_TYPE_FILE_PATH);
        ResponseEntity<String> response = restTemplate.getForEntity(buildUri(SDC_GET_SERVICE_MODEL + MACRO_INSTANTIATION_TYPE_UUID), String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        String emptyInstantiationTypeExpectedResponse = loadResourceAsString(EMPTY_INSTANTIATION_TYPE_EXPECTED_RESPONSE);
        assertThat("The response is in the format of JSON", response.getBody(), is(jsonStringEquals(turnOffInstantiationUI(emptyInstantiationTypeExpectedResponse))));
    }

    @Test
    public void getServiceModelBothInstantiationType(){
        registerToSimulatorWithPresets(MACRO_INSTANTIATION_TYPE_UUID, MACRO_INSTANTIATION_TYPE_INVARIANT_UUID, BOTH_INSTANTIATION_TYPE_FILE_PATH);
        ResponseEntity<String> response = restTemplate.getForEntity(buildUri(SDC_GET_SERVICE_MODEL + MACRO_INSTANTIATION_TYPE_UUID), String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        String macroInstantiationTypeExpectedResponse = loadResourceAsString(MACRO_INSTANTIATION_TYPE_EXPECTED_RESPONSE);
        assertThat("The response is in the format of JSON", response.getBody(), is(jsonStringEquals(turnOffInstantiationUI(macroInstantiationTypeExpectedResponse))));
    }

    @Test
    public void getServiceModelWithGroupsAndCheckMinMaxInitialParams(){
        registerToSimulatorWithPresets(MIN_MAX_INITIAL_UUID, MIN_MAX_INITIAL_INVARIANT_UUID, MIN_MAX_INITIAL_FILE_PATH);
        ResponseEntity<String> response = restTemplate.getForEntity(buildUri(SDC_GET_SERVICE_MODEL + MIN_MAX_INITIAL_UUID), String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        String minMaxInitialExpectedResponse = loadResourceAsString("sdcApiTest/minMaxInitialExpectedResponse.json");
        assertThat("The response is in the format of JSON", response.getBody(), is(jsonStringEquals(turnOffInstantiationUI(minMaxInitialExpectedResponse))));
    }

    @Test
    public void getServiceModelWithGroupsAndCheckMinMaxInitialParamsOldCsar(){
        registerToSimulatorWithPresets(MIN_MAX_INITIAL_UUID_OLD_CSAR, MIN_MAX_INITIAL_INVARIANT_UUID_OLD_CSAR, MIN_MAX_INITIAL_FILE_PATH_OLD_CSAR);
        ResponseEntity<String> response = restTemplate.getForEntity(buildUri(SDC_GET_SERVICE_MODEL + MIN_MAX_INITIAL_UUID_OLD_CSAR), String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        String minMaxInitialExpectedResponseOldCsar = loadResourceAsString("sdcApiTest/minMaxInitialExpectedResponseOldCsar.json");
        assertThat("The response is in the format of JSON", response.getBody(), is(jsonStringEquals(minMaxInitialExpectedResponseOldCsar)));
    }

    @Test
    @FeatureTogglingTest(Features.FLAG_1902_VNF_GROUPING)
    public void getServiceModelWithServiceRoleGrouping(){
        registerToSimulatorWithPresets(GROUPING_SERVICE_ROLE_UUID, GROUPING_SERVICE_ROLE_INVARIANT_UUID, GROUPING_SERVICE_ROLE_FILE_PATH);
        ResponseEntity<String> response = restTemplate.getForEntity(buildUri(SDC_GET_SERVICE_MODEL + GROUPING_SERVICE_ROLE_UUID), String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        String groupingServiceRoleExpectedResponse = loadResourceAsString(GROUPING_SERVICE_ROLE_EXPECTED_RESPONSE);
        assertThat("The response is in the format of JSON", response.getBody(), is(jsonStringEquals(groupingServiceRoleExpectedResponse)));
    }

    private void registerToSimulatorWithPresets(String uuid, String invariantUuid, String pathPath){
        ImmutableList<BasePreset> presets = ImmutableList.of(
                new PresetSDCGetServiceToscaModelGet(uuid, pathPath),
                new PresetSDCGetServiceMetadataGet(uuid,invariantUuid, pathPath));
        registerExpectationFromPresets(presets, CLEAR_THEN_SET);
    }

    private String turnOffInstantiationUI(String expectedJson) {
        if (!Features.FLAG_5G_IN_NEW_INSTANTIATION_UI.isActive()) {
            // replaces the instantiationUI field-value with "legacy", whatever it was
            return expectedJson.replaceFirst("(\"instantiationUI\": *\")[^\"]*(\",)", "$1legacy$2");
        } else {
            return expectedJson;
        }
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
        final String myVnf = "vMMEvProbe_FE_AIC3-11.2.1_VF 1";
        final String base = "/vnfs/" + myVnf;

        assertFalse(response.at(base).isMissingNode(),
                "test relies on '" + myVnf + "' to be in model; got: " + response);

        assertThat("vfModules under '" + myVnf + "' must not be empty; got: " + response,
                response.at(base + "/vfModules").size(), is(not(0)));

        assertThat("volumeGroups under '" + myVnf + "' must not be empty; got: " + response,
                response.at(base + "/volumeGroups").size(), is(not(0)));

    }
}
