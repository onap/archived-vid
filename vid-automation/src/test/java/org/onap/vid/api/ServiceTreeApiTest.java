package org.onap.vid.api;

import static java.lang.Integer.min;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_EXTRA_FIELDS;
import static org.apache.commons.text.StringEscapeUtils.escapeJson;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId.ATT_AIC;
import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId.ATT_NC;
import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId.AUK51A;
import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId.hvf6;
import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId.olson3;
import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIStandardQueryGet.defaultPlacement;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.onap.simulator.presetGenerator.presets.aai.Placement;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudRegionFromVnf;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetServiceInstancesByInvariantId;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetVfModulesByVnf;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIModelsByInvariantIdGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIStandardQueryGet;
import org.onap.simulator.presetGenerator.presets.ecompportal_att.PresetGetSessionSlotCheckIntervalGet;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceMetadataGet;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceToscaModelGet;
import org.onap.vid.more.LoggerFormatTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.ITestResult;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.util.RetryAnalyzerCount;
import vid.automation.test.services.SimulatorApi;
import vid.automation.test.services.SimulatorApi.RegistrationStrategy;

public class ServiceTreeApiTest extends BaseApiTest {

    private static final String API_URL = "/aai_get_service_instance_topology/{subscriberId}/{serviceType}/{serviceInstanceId}";

    @BeforeClass
    public void login() {
        super.login();
    }

    @Test
    public void serviceWithNoChildren_requestDoesSomething() {
        final PresetAAIStandardQueryGet serviceInstance =
                PresetAAIStandardQueryGet.ofServiceInstance("service-instance-id", "7a6ee536-f052-46fa-aa7e-2fca9d674c44", "service-instance-model-invariant-id", "global-customer-id", "service-instance-type", ImmutableMultimap.of());

        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                serviceInstance,
                new PresetAAIModelsByInvariantIdGet(ImmutableList.of("service-instance-model-invariant-id")),
                new PresetSDCGetServiceMetadataGet("7a6ee536-f052-46fa-aa7e-2fca9d674c44", "service-instance-model-invariant-id", "csar15782222_instantiationTypeMacroWithNetwork.zip"),
                new PresetSDCGetServiceToscaModelGet("7a6ee536-f052-46fa-aa7e-2fca9d674c44", "csar15782222_instantiationTypeMacroWithNetwork.zip")
        ), CLEAR_THEN_SET);
        final String response = restTemplate.getForObject(buildUri(API_URL), String.class, "global-customer-id", "service-instance-type", "service-instance-id");

        assertJsonEquals(response, "" +
                "{" +
                "  \"instanceName\": \"" + escapeJson(serviceInstance.getInstanceName()) + "\"," +
                "  \"action\": \"None\"," +
                "  \"instanceId\": \"service-instance-id\"," +
                "  \"orchStatus\": \"GARBAGE DATA\"," +
                "  \"globalSubscriberId\": \"global-customer-id\"," +
                "  \"subscriptionServiceType\": \"service-instance-type\"," +
                "  \"owningEntityId\": null," +
                "  \"owningEntityName\": null," +
                "  \"productFamilyId\": null," +
                "  \"lcpCloudRegionId\": null," +
                "  \"tenantId\": null," +
                "  \"tenantName\": null," +
                "  \"aicZoneId\": null," +
                "  \"aicZoneName\": null," +
                "  \"projectName\": null," +
                "  \"rollbackOnFailure\": null," +
                "  \"modelInfo\": {" +
                "    \"modelInvariantId\": \"service-instance-model-invariant-id\"," +
                "    \"modelVersionId\": \"7a6ee536-f052-46fa-aa7e-2fca9d674c44\"," +
                "    \"modelName\": \"vf_vEPDG\"," +
                "    \"modelType\": \"service\"," +
                "    \"modelVersion\": \"2.0\"" +
                "  }," +
                "  \"vnfs\": {}," +
                "  \"networks\": {}," +
                "  \"vnfGroups\": {}," +
                "  \"validationCounter\": 0," +
                "  \"existingVNFCounterMap\": {}," +
                "  \"existingNetworksCounterMap\": {}," +
                "  \"existingVnfGroupCounterMap\": {}," +
                "  \"isALaCarte\": false" +
                "}");
    }

    @Test
    public void searchGroupMembers_expected4vnfs() {
        PresetAAIStandardQueryGet instanceGroup1 = PresetAAIStandardQueryGet.ofInstanceGroup("L3-NETWORK", "SUB_INTERFACE", ImmutableMultimap.of());
        PresetAAIStandardQueryGet instanceGroup2 = PresetAAIStandardQueryGet.ofInstanceGroup("LOAD-GROUP", "SERVICE-ACCESS", ImmutableMultimap.of());

        String vnfInstanceIdPrefix = StringUtils.left(randUuid(), 7);

        PresetAAIStandardQueryGet vnfPreset1 =
                PresetAAIStandardQueryGet.ofRelatedVnf(randUuid(vnfInstanceIdPrefix), "7a6ee536-f052-46fa-aa7e-2fca9d674c44",  "",
                        ImmutableMultimap.of("instance-group", instanceGroup1.getReqPath()));

        PresetAAIStandardQueryGet vnfPreset2 =
                PresetAAIStandardQueryGet.ofRelatedVnf(randUuid(), "eb5f56bf-5855-4e61-bd00-3e19a953bf02",
                        "\"in-maint\": true,", ImmutableMultimap.of());

        PresetAAIStandardQueryGet vnfPreset3 =
                PresetAAIStandardQueryGet.ofRelatedVnf(randUuid(), "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc", "",
                        ImmutableMultimap.of("instance-group", instanceGroup1.getReqPath()));

        PresetAAIStandardQueryGet vnfPreset4 =
                PresetAAIStandardQueryGet.ofRelatedVnf(randUuid(), "b7f2e8fb-ac71-4ea0-a801-06ef1479ea84",
                        "\"in-maint\": true,", ImmutableMultimap.of());

        PresetAAIStandardQueryGet vnfPreset5 =
                PresetAAIStandardQueryGet.ofRelatedVnf(randUuid(), "b7f2e8fb-ac71-4ea0-a801-06ef1479ea84",
                        "\"in-maint\": true,", ImmutableMultimap.of("instance-group", instanceGroup2.getReqPath()));

        Multimap<String, String> serviceInstance1 = ImmutableMultimap.<String, String>builder()
                .putAll("generic-vnf", vnfPreset1.getReqPath())
                .putAll("generic-vnf", vnfPreset3.getReqPath())
                .build();

        Multimap<String, String> serviceInstance2 = ImmutableMultimap.<String, String>builder()
                .putAll("generic-vnf", vnfPreset2.getReqPath())
                .putAll("generic-vnf", vnfPreset4.getReqPath())
                .putAll("generic-vnf", vnfPreset5.getReqPath())
                .build();

        PresetAAIGetServiceInstancesByInvariantId serviceInstancesList = new PresetAAIGetServiceInstancesByInvariantId(
                "global-customer-id", "service-instance-type", "24632e6b-584b-4f45-80d4-fefd75fd9f14",
                ImmutableMap.of("service-instance-id1", serviceInstance1,
                        "service-instance-id2", serviceInstance2));


        SimulatorApi.registerExpectationFromPresets(
                ImmutableList.of(serviceInstancesList,
                        vnfPreset1, instanceGroup1,
                        vnfPreset2,
                        vnfPreset3,
                        vnfPreset4,
                        vnfPreset5, instanceGroup2, //this vnf should be filtered out
                        new PresetAAIModelsByInvariantIdGet(ImmutableList.of("vnf-instance-model-invariant-id")),
                        new PresetAAIGetCloudRegionFromVnf(vnfPreset1.getInstanceId()),
                        new PresetAAIGetCloudRegionFromVnf(vnfPreset2.getInstanceId()),
                        new PresetAAIGetCloudRegionFromVnf(vnfPreset3.getInstanceId()),
                        new PresetAAIGetCloudRegionFromVnf(vnfPreset4.getInstanceId()),
                        new PresetAAIGetSubscribersGet()
                ), CLEAR_THEN_SET);

        String api_url = "aai_search_group_members?subscriberId={subscriberId}&serviceType={serviceType}&serviceInvariantId={serviceInvariantId}" +
                "&groupType={groupType}&groupRole={groupRole}";

        final ResponseEntity<String> responseEntity = restTemplate.getForEntity(buildUri(api_url), String.class, "global-customer-id", "service-instance-type", "24632e6b-584b-4f45-80d4-fefd75fd9f14", "LOAD-GROUP", "SERVICE-ACCESS");
        String response = responseEntity.getBody();

        LOGGER.info(response);

        String expected = TestUtils.convertRequest(objectMapper, "VnfGroup/searchMembersResponse.json");
        expected = expected
                .replace("VNF1_INSTANCE_NAME", vnfPreset1.getInstanceName())
                .replace("VNF1_INSTANCE_ID", vnfPreset1.getInstanceId())
                .replace("VNF1_INSTANCE_TYPE", vnfPreset1.getInstanceType())
                .replace("VNF2_INSTANCE_NAME", vnfPreset2.getInstanceName())
                .replace("VNF2_INSTANCE_ID", vnfPreset2.getInstanceId())
                .replace("VNF2_INSTANCE_TYPE", vnfPreset2.getInstanceType())
                .replace("VNF3_INSTANCE_NAME", vnfPreset3.getInstanceName())
                .replace("VNF3_INSTANCE_ID", vnfPreset3.getInstanceId())
                .replace("VNF3_INSTANCE_TYPE", vnfPreset3.getInstanceType())
                .replace("VNF4_INSTANCE_NAME", vnfPreset4.getInstanceName())
                .replace("VNF4_INSTANCE_ID", vnfPreset4.getInstanceId())
                .replace("VNF4_INSTANCE_TYPE", vnfPreset4.getInstanceType());

        assertJsonEquals(response, expected);

        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetSubscribersGet(), RegistrationStrategy.APPEND);
        LoggerFormatTest.assertHeadersAndMetricLogs(restTemplate, uri, echoedRequestId(responseEntity),  "/network/generic-vnfs/generic-vnf/", 5);
        // org.onap.vid.aai.AaiClient.getCloudRegionAndTenantByVnfId for presets PresetAAIGetCloudRegionFromVnf is
        // PUTing to AAI, so path is just /aai/v../query
        LoggerFormatTest.assertHeadersAndMetricLogs(restTemplate, uri, echoedRequestId(responseEntity),  "/query", 4);
    }

    @Test
    public void searchGroupMembers_expectedNoResult() {
        PresetAAIGetServiceInstancesByInvariantId serviceInstancesList = new PresetAAIGetServiceInstancesByInvariantId(
                "global-customer-id", "service-instance-type", "24632e6b-584b-4f45-80d4-fefd75fd9f14",
                ImmutableMap.of("service-instance-id1", ImmutableMultimap.of(),
                        "service-instance-id2", ImmutableMultimap.of()));


        SimulatorApi.registerExpectationFromPreset(serviceInstancesList, CLEAR_THEN_SET);

        String api_url = "aai_search_group_members?subscriberId={subscriberId}&serviceType={serviceType}&serviceInvariantId={serviceInvariantId}" +
                "&groupType={groupType}&groupRole={groupRole}";

        final String response = restTemplate.getForObject(buildUri(api_url), String.class, "global-customer-id", "service-instance-type", "24632e6b-584b-4f45-80d4-fefd75fd9f14", "LOAD-GROUP", "SERVICE-ACCESS");

        assertJsonEquals(response, "[]");
    }

    @Test
    public void serviceWithMultiplePlys_responseIsReasonable() {

        PresetAAIStandardQueryGet l3NetworkPreset1 =
                PresetAAIStandardQueryGet.ofL3Network("CONTRAIL30_BASIC", "Assigned",
                        ImmutableMultimap.of());

        PresetAAIStandardQueryGet vlanTag1 = PresetAAIStandardQueryGet.ofVlanTag(44);

        PresetAAIStandardQueryGet l3NetworkPreset2 =

                PresetAAIStandardQueryGet.ofL3Network("CONTRAIL30_BASIC", "Created",
                        ImmutableMultimap.of("vlan-tag", vlanTag1.getReqPath()));

        PresetAAIStandardQueryGet l3NetworkPreset3 =
                PresetAAIStandardQueryGet.ofL3Network("CONTRAIL30_BASIC", "Assigned", "nvtprov", "ddc3f20c-08b5-40fd-af72-c6d14636b986","94fdd893-4a36-4d70-b16a-ec29c54c184f",
                        ImmutableMultimap.of());

        PresetAAIStandardQueryGet l3NetworkPreset4 =
                PresetAAIStandardQueryGet.ofL3Network("CONTRAIL30_HIMELGUARD", "Created", "preprov", "ddc3f20c-08b5-40fd-af72-c6d14636b986","94fdd893-4a36-4d70-b16a-ec29c54c184f",
                        ImmutableMultimap.of());

        PresetAAIStandardQueryGet volumeGroup1 =
                PresetAAIStandardQueryGet.ofVolumeGroup("vSON_test", ImmutableMultimap.of());

        PresetAAIStandardQueryGet instanceGroup1 = PresetAAIStandardQueryGet.ofInstanceGroup("L3-NETWORK", "Ruby Figueroa", ImmutableMultimap.of());

        PresetAAIStandardQueryGet collection1 =
                PresetAAIStandardQueryGet.ofCollectionResource(
                        "Assigned",
                        ImmutableMultimap.of("instance-group", instanceGroup1.getReqPath()),
                        "081ceb56-eb71-4566-a72d-3e7cbee5cdf1",
                        "ce8c98bc-4691-44fb-8ff0-7a47487c11c4"
                );

        PresetAAIStandardQueryGet vnfPreset1 =
                PresetAAIStandardQueryGet.ofVnf(randUuid(),
                        ImmutableMultimap.of("l3-network", l3NetworkPreset1.getReqPath(), "l3-network", l3NetworkPreset2.getReqPath()),
                        new Placement(ATT_NC, olson3, "229bcdc6eaeb4ca59d55221141d01f8e"));

        PresetAAIStandardQueryGet vnfPreset2 =
                PresetAAIStandardQueryGet.ofVnf(randUuid(), "d6557200-ecf2-4641-8094-5393ae3aae60","91415b44-753d-494c-926a-456a9172bbb9",
                        "\"in-maint\": true,", ImmutableMultimap.of("volume-group", volumeGroup1.getReqPath()),
                        new Placement(ATT_AIC, hvf6, "88a6ca3ee0394ade9403f075db23167e"));


        Placement vfModule2Placement
            = new Placement(ATT_NC, AUK51A, "73bb4c548dc048d78eccecd445ac06fc");

        PresetAAIGetVfModulesByVnf twoVfModulesPreset =
            new PresetAAIGetVfModulesByVnf(vnfPreset2.getInstanceId(), vfModule2Placement);

        final PresetAAIStandardQueryGet serviceInstance =
                PresetAAIStandardQueryGet.ofServiceInstance("service-instance-id", "6e59c5de-f052-46fa-aa7e-2fca9d674c44", "d27e42cf-087e-4d31-88ac-6c4b7585f800", "global-customer-id", "service-instance-type",
                        ImmutableMultimap.<String, String>builder()
                                .putAll("l3-network", l3NetworkPreset3.getReqPath(), l3NetworkPreset4.getReqPath())
                                .putAll("collection", collection1.getReqPath())
                                .putAll("generic-vnf", vnfPreset1.getReqPath(), vnfPreset2.getReqPath())
                                .build()
                );

        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                serviceInstance,
                l3NetworkPreset3, l3NetworkPreset4, vnfPreset1, vnfPreset2, collection1,
                volumeGroup1, l3NetworkPreset1, l3NetworkPreset2, instanceGroup1,
                vlanTag1, twoVfModulesPreset,
                new PresetAAIModelsByInvariantIdGet(ImmutableList.of("d27e42cf-087e-4d31-88ac-6c4b7585f800")),
                new PresetGetSessionSlotCheckIntervalGet(),
                new PresetAAIGetSubscribersGet(),
                new PresetSDCGetServiceMetadataGet("6e59c5de-f052-46fa-aa7e-2fca9d674c44", "d27e42cf-087e-4d31-88ac-6c4b7585f800", "csar15782222_instantiationTypeMacroWithNetwork.zip"),
                new PresetSDCGetServiceToscaModelGet("6e59c5de-f052-46fa-aa7e-2fca9d674c44", "csar15782222_instantiationTypeMacroWithNetwork.zip")
        ), CLEAR_THEN_SET);

        String expected = TestUtils.convertRequest(objectMapper, "aaiGetInstanceTopology/ServiceTreeWithMultipleChildren_serviceInstance.json");
        expected = expected
                .replace("SERVICE_INSTANCE_NAME", serviceInstance.getInstanceName())
                .replace("VNF1_INSTANCE_NAME", vnfPreset1.getInstanceName())
                .replace("VNF1_INSTANCE_ID", vnfPreset1.getInstanceId())
                .replace("VNF1_INSTANCE_TYPE", vnfPreset1.getInstanceType())
                .replace("VNF2_INSTANCE_NAME", vnfPreset2.getInstanceName())
                .replace("VNF2_INSTANCE_ID", vnfPreset2.getInstanceId())
                .replace("VNF2_INSTANCE_TYPE", vnfPreset2.getInstanceType())
                .replace("NETWORK1_INSTANCE_NAME", l3NetworkPreset1.getInstanceName())
                .replace("NETWORK1_INSTANCE_ID", l3NetworkPreset1.getInstanceId())
                .replace("NETWORK2_INSTANCE_NAME", l3NetworkPreset2.getInstanceName())
                .replace("NETWORK2_INSTANCE_ID", l3NetworkPreset2.getInstanceId())
                .replace("NETWORK3_INSTANCE_NAME", l3NetworkPreset3.getInstanceName())
                .replace("NETWORK3_INSTANCE_ID", l3NetworkPreset3.getInstanceId())
                .replace("NETWORK4_INSTANCE_NAME", l3NetworkPreset4.getInstanceName())
                .replace("NETWORK4_INSTANCE_ID", l3NetworkPreset4.getInstanceId());

        ResponseEntity<String> response = restTemplate.getForEntity(buildUri(API_URL), String.class, "global-customer-id", "service-instance-type", "service-instance-id");

        assertJsonEquals(response.getBody(), expected);
        LoggerFormatTest.assertHeadersAndMetricLogs(restTemplate, uri, echoedRequestId(response),  "/vf-modules", 2);
    }

    private String echoedRequestId(ResponseEntity<?> response) {
        return response.getHeaders().getFirst("X-ECOMP-RequestID-echo");
    }

    @Override
    protected void assertJsonEquals(String actual, String expected) {
        assertThat(actual, jsonEquals(expected)
                .when(IGNORING_ARRAY_ORDER)
                .when(IGNORING_EXTRA_FIELDS)
        );
    }

    @Test
    public void serviceWithVnfGotError_exceptionIsThrown() {

        PresetAAIStandardQueryGet vnfPreset =
                PresetAAIStandardQueryGet.ofVnf(randUuid(),
                        ImmutableMultimap.of("l3-network", "/aai/v../I'm a wrong path"),
                        defaultPlacement());

        final PresetAAIStandardQueryGet serviceInstance =
                PresetAAIStandardQueryGet.ofServiceInstance("service-instance-id", "7a6ee536-f052-46fa-aa7e-2fca9d674c44", "service-instance-model-invariant-id", "global-customer-id", "service-instance-type",
                        ImmutableMultimap.<String, String>builder()
                                .putAll("generic-vnf", vnfPreset.getReqPath())
                                .build()
                );

        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                serviceInstance,
                vnfPreset,
                new PresetAAIModelsByInvariantIdGet(ImmutableList.of("service-instance-model-invariant-id")),
                new PresetSDCGetServiceMetadataGet("7a6ee536-f052-46fa-aa7e-2fca9d674c44", "service-instance-model-invariant-id", "csar15782222_instantiationTypeMacroWithNetwork.zip"),
                new PresetSDCGetServiceToscaModelGet("7a6ee536-f052-46fa-aa7e-2fca9d674c44", "csar15782222_instantiationTypeMacroWithNetwork.zip"),
                new PresetGetSessionSlotCheckIntervalGet(),
                new PresetAAIGetSubscribersGet()
        ), CLEAR_THEN_SET);

        final ResponseEntity<String> response = restTemplateErrorAgnostic.getForEntity(buildUri(API_URL), String.class, "global-customer-id", "service-instance-type", "service-instance-id");
        assertThat(response.getBody(),containsString("AAI node fetching failed"));
        assertThat(response.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Test
    public void serviceWithTwoVnfGroupsAndRelatedVnfs() {
        PresetAAIStandardQueryGet relatedVnf1 =
                PresetAAIStandardQueryGet.ofRelatedVnf(randUuid(), "7a6ee536-f052-46fa-aa7e-2fca9d674c44",
                        "", ImmutableMultimap.of());

        PresetAAIStandardQueryGet relatedVnf2 =
                PresetAAIStandardQueryGet.ofRelatedVnf(randUuid(), "d6557200-ecf2-4641-8094-5393ae3aae60",
                        "", ImmutableMultimap.of());

        PresetAAIStandardQueryGet relatedVnf3 =
                PresetAAIStandardQueryGet.ofRelatedVnf(randUuid(), "d6557200-ecf2-4641-8094-5393ae3aae60",
                        "", ImmutableMultimap.of());

        final PresetAAIStandardQueryGet vnfGroup1 =
                PresetAAIStandardQueryGet.ofInstanceGroup("vnfGroup-type", "Teresa Bradley",
                        ImmutableMultimap.<String, String>builder()
                                .putAll("generic-vnf", relatedVnf1.getReqPath(), relatedVnf2.getReqPath(), relatedVnf3.getReqPath())
                                .build()
                );

        final PresetAAIStandardQueryGet vnfGroup2 =
                PresetAAIStandardQueryGet.ofInstanceGroup("vnfGroup-type", "Stanley Mccarthy", ImmutableMultimap.of());

        final PresetAAIStandardQueryGet serviceInstance =
                PresetAAIStandardQueryGet.ofServiceInstance("service-instance-id", "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc", "7ee41ce4-4827-44b0-a48e-2707a59905d2", "global-customer-id", "service-instance-type",
                        ImmutableMultimap.<String, String>builder()
                                .putAll("instance-group", vnfGroup1.getReqPath(),vnfGroup2.getReqPath())
                                .build()
                );

        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                serviceInstance,
                vnfGroup1,vnfGroup2, relatedVnf1, relatedVnf2, relatedVnf3,
                new PresetAAIModelsByInvariantIdGet(ImmutableList.of("7ee41ce4-4827-44b0-a48e-2707a59905d2", "vnf-instance-model-invariant-id")),
                new PresetGetSessionSlotCheckIntervalGet(),
                new PresetAAIGetSubscribersGet(),
                new PresetSDCGetServiceMetadataGet("4117a0b6-e234-467d-b5b9-fe2f68c8b0fc", "7ee41ce4-4827-44b0-a48e-2707a59905d2", "service-vnf-grouping-csar.zip"),
                new PresetSDCGetServiceToscaModelGet("4117a0b6-e234-467d-b5b9-fe2f68c8b0fc", "service-vnf-grouping-csar.zip")
        ), CLEAR_THEN_SET);

        String expected = TestUtils.convertRequest(objectMapper, "VnfGroup/serviceWithVnfGroupsChildren_serviceInstance.json");
        expected = expected
                .replace("SERVICE_INSTANCE_NAME", serviceInstance.getInstanceName())
                .replace("VNF_GROUP1_INSTANCE_ID", vnfGroup1.getInstanceId())
                .replace("VNF_GROUP1_INSTANCE_NAME", vnfGroup1.getInstanceName())
                .replace("VNF_GROUP1_INSTANCE_TYPE", vnfGroup1.getInstanceType())
                .replace("VNF_GROUP1_INSTANCE_ROLE", vnfGroup1.getInstanceRole())

                .replace("RELATED_VNF1_INSTANCE_ID", relatedVnf1.getInstanceId())
                .replace("RELATED_VNF1_INSTANCE_NAME", relatedVnf1.getInstanceName())
                .replace("RELATED_VNF1_INSTANCE_TYPE", relatedVnf1.getInstanceType())

                .replace("RELATED_VNF2_INSTANCE_ID", relatedVnf2.getInstanceId())
                .replace("RELATED_VNF2_INSTANCE_NAME", relatedVnf2.getInstanceName())
                .replace("RELATED_VNF2_INSTANCE_TYPE", relatedVnf2.getInstanceType())

                .replace("RELATED_VNF3_INSTANCE_ID", relatedVnf3.getInstanceId())
                .replace("RELATED_VNF3_INSTANCE_NAME", relatedVnf3.getInstanceName())
                .replace("RELATED_VNF3_INSTANCE_TYPE", relatedVnf3.getInstanceType())

                .replace("VNF_GROUP2_INSTANCE_ID", vnfGroup2.getInstanceId())
                .replace("VNF_GROUP2_INSTANCE_NAME", vnfGroup2.getInstanceName())
                .replace("VNF_GROUP2_INSTANCE_TYPE", vnfGroup2.getInstanceType())
                .replace("VNF_GROUP2_INSTANCE_ROLE", vnfGroup2.getInstanceRole());

        final String response = restTemplate.getForObject(buildUri(API_URL), String.class, "global-customer-id", "service-instance-type", "service-instance-id");

        assertJsonEquals(response, expected);
    }

    public static class RetryServiceWithVrf extends RetryAnalyzerCount {
        // For an unknown reason serviceWithVrf_resultAsExpected test is failing sometimes.
        // This single retry might shed more light on the situation: will it work the 2nd
        // time, or still fail 2 times in strike?

        public RetryServiceWithVrf() {
            setCount(2);
        }

        @Override
        public boolean retryMethod(ITestResult result) {
            if (result.isSuccess()) {
                return false;
            } else {
                LOGGER.error(result.getName() + " failed; retrying (" + this.getClass().getCanonicalName() + ")");
                return true;
            }
        }
    }

    @Test(retryAnalyzer = RetryServiceWithVrf.class)
    public void serviceWithVrf_resultAsExpected() {
        PresetAAIStandardQueryGet vpnBindingPreset =
                PresetAAIStandardQueryGet.ofVpn("Active", ImmutableMultimap.of(), "mock-global-1", "mock-role-x", "VPN1260","USA,EMEA");

        // in order to verify thst only one route target is parsed from the Vpn - binding of the network
        PresetAAIStandardQueryGet vpnBindingPresetOnlyForTheNetwork =
                PresetAAIStandardQueryGet.ofVpn("Active", ImmutableMultimap.of(), "shouldNotBeOnResult", "shouldNotBeOnResult","shouldNotBeOnResult","shouldNotBeOnResult");

        PresetAAIStandardQueryGet l3NetworkPreset =
                PresetAAIStandardQueryGet.ofL3Network("SR-IOV-PROVIDER2-2", "Assigned",
                        ImmutableMultimap.of("vpn-binding", vpnBindingPreset.getReqPath(),
                                "vpn-binding", vpnBindingPresetOnlyForTheNetwork.getReqPath()));

        PresetAAIStandardQueryGet vrfPreset =
                PresetAAIStandardQueryGet.ofVrf("Create",
                        ImmutableMultimap.of(
                                "l3-network", l3NetworkPreset.getReqPath(),
                                "vpn-binding", vpnBindingPreset.getReqPath()));

        final PresetAAIStandardQueryGet serviceInstance =
                PresetAAIStandardQueryGet.ofServiceInstance("service-instance-id", "BONDING", "INFRASTRUCTURE-VPN", "f028b2e2-7080-4b13-91b2-94944d4c42d8",
                        "dfc2c44c-2429-44ca-ae26-1e6dc1f207fb", "global-customer-id", "service-instance-type", "GARBAGE DATA",
                        ImmutableMultimap.of("configuration", vrfPreset.getReqPath()));

        String expected = TestUtils.convertRequest(objectMapper, "aaiGetInstanceTopology/serviceWithVrfTopology.json");
        expected = expected
                .replace("SERVICE_INSTANCE_NAME", serviceInstance.getInstanceName())
                .replace("VRF_INSTANCE_ID", vrfPreset.getInstanceId())
                .replace("VRF_INSTANCE_NAME", vrfPreset.getInstanceName())
                .replace("VPN_INSTANCE_ID", vpnBindingPreset.getInstanceId())
                .replace("VPN_INSTANCE_NAME", vpnBindingPreset.getInstanceName())
                .replace("NETWORK_INSTANCE_NAME", l3NetworkPreset.getInstanceName())
                .replace("NETWORK_INSTANCE_ROLE", l3NetworkPreset.getInstanceRole())
                .replace("NETWORK_INSTANCE_ID", l3NetworkPreset.getInstanceId());

        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                serviceInstance,
                vrfPreset,
                vpnBindingPreset,
                l3NetworkPreset,
                new PresetAAIModelsByInvariantIdGet(ImmutableList.of("network-instance-model-invariant-id", "dfc2c44c-2429-44ca-ae26-1e6dc1f207fb", "b67a289b-1688-496d-86e8-1583c828be0a" )),
                new PresetGetSessionSlotCheckIntervalGet(),
                new PresetAAIGetSubscribersGet(),
                new PresetSDCGetServiceMetadataGet("f028b2e2-7080-4b13-91b2-94944d4c42d8", "dfc2c44c-2429-44ca-ae26-1e6dc1f207fb", "service-Infravpn-csar.zip"),
                new PresetSDCGetServiceToscaModelGet("f028b2e2-7080-4b13-91b2-94944d4c42d8", "service-Infravpn-csar.zip")
        ), CLEAR_THEN_SET);

        final String response = restTemplate.getForObject(buildUri(API_URL), String.class, "global-customer-id", "service-instance-type", "service-instance-id");

        try {
            assertJsonEquals(response, expected);
        } catch (AssertionError error) {
            // Logs what happens on simulator's end when error occures
            final Map<String, Long> recordedSimulatorPaths = SimulatorApi.retrieveRecordedRequestsPathCounter();
            System.err.println(recordedSimulatorPaths);
            LOGGER.error(recordedSimulatorPaths);
            throw error;
        }
    }

    private String randUuid() {
        return UUID.randomUUID().toString();
    }

    private String randUuid(String prefix) {
        int prefixLen = min(prefix.length(), 7);
        return StringUtils.left(prefix, prefixLen) + StringUtils.substring(randUuid(), prefixLen);
    }

}
