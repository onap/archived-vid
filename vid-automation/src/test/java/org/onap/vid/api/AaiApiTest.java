package org.onap.vid.api;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIStandardQueryGet.defaultPlacement;
import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIStandardQueryGet.ofL3Network;
import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIStandardQueryGet.ofServiceInstance;
import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIStandardQueryGet.ofVlanTag;
import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIStandardQueryGet.ofVnf;
import static org.onap.simulator.presetGenerator.presets.aai.PresetBaseAAICustomQuery.FORMAT.SIMPLE;
import static org.onap.simulator.presetGenerator.presets.ecompportal_att.EcompPortalPresetsUtils.getEcompPortalPresets;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static vid.automation.test.Constants.RegisterToSimulator.SearchForServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER_CRAIG_ROBERTS;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.APPEND;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPresets;
import static vid.automation.test.utils.TestHelper.GET_SERVICE_MODELS_BY_DISTRIBUTION_STATUS;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import net.javacrumbs.jsonunit.JsonAssert;
import net.javacrumbs.jsonunit.core.Configuration;
import net.javacrumbs.jsonunit.core.Option;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.client.utils.URIBuilder;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.onap.simulator.presetGenerator.presets.aai.AAIBaseGetL3NetworksByCloudRegionPreset;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIBadBodyForGetServicesGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAICloudRegionAndSourceFromConfigurationPut;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIFilterServiceInstanceById;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetHomingForVfModule;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetInstanceGroupsByCloudRegion;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetInstanceGroupsByCloudRegionInvalidRequest;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetInstanceGroupsByCloudRegionRequiredMissing;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetL3NetworksByCloudRegion;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetL3NetworksByCloudRegionSpecificState;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetModelsByOwningEntity;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetNetworkCollectionDetails;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetNetworkCollectionDetailsInvalidRequest;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetNetworkCollectionDetailsRequiredMissing;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetPortMirroringSourcePorts;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetPortMirroringSourcePortsError;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetRelatedInstanceGroupsByVnfId;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetServiceInstanceBySubscriberIdAndServiceTypeAndSIID;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetVpnsByType;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIModelVersionsByInvariantId;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIServiceInstanceDSLPut;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIStandardQueryGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetBaseAAICustomQuery;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceMetadataGet;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceToscaModelGet;
import org.onap.vid.model.aai.AaiResponse;
import org.onap.vid.model.mso.OperationalEnvironmentList;
import org.onap.vid.more.LoggerFormatTest;
import org.onap.vid.more.LoggerFormatTest.LogName;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.infra.FeatureTogglingTest;
import vid.automation.test.infra.Features;
import vid.automation.test.services.SimulatorApi;
import vid.automation.test.utils.TestHelper;

public class AaiApiTest extends BaseApiAaiTest {

    private static final String AAI_HOMING_DATA_RESPONSE = "viewEdit/aaiHomingDataResponse.json";
    public static final String GET_OPERATIONAL_ENVIRONMENTS_JSON = "get_operational_environments_aai.json";
    public static final String GET_OPERATIONAL_ENVIRONMENTS_JSON_ERROR = "get_operational_environments_aai_error.json";
    public static final String[] AAI_GET_SERVICES_ERROR_SIMULATOR_RESPONSES = {"getServicesAaiErrorResp.json", "create_new_instance/aai_get_full_subscribers.json"};
    public static final String[] AAI_GET_SERVICES_FINE_SIMULATOR_RESPONSES = {"getServicesAaiFineResp.json", "create_new_instance/aai_get_full_subscribers.json"};
    public static final String AAI_VNFS_FOR_CHANGE_MANAGEMENT_JSON = "changeManagement/get_vnf_data_by_globalid_and_service_type.json";
    public static final String AAI_VNFS_FOR_CHANGE_MANAGEMENT_JSON_BY_PARAMS = "registration_to_simulator/changeManagement/get_vnf_data_by_globalid_and_service_type_reduced_response.json";
    public static final String OPERATIONAL_ENVIRONMENT_TYPE = "VNF";
    public static final String OPERATIONAL_ENVIRONMENT_STATUS = "Activate";
    public static final String GET_INSTANCE_GROUPS_BY_CLOUDREGION_EXPECTED_RESPONSE = "{\"results\":[{\"instance-group\":{\"id\":\"AAI-12002-test3-vm230w\",\"description\":\"a9DEa0kpY\",\"instance-group-role\":\"JZmha7QSS4tJ\",\"model-invariant-id\":\"model-id3\",\"model-version-id\":\"a0efd5fc-f7be-4502-936a-a6c6392b958f\",\"instance-group-type\":\"type\",\"resource-version\":\"1520888659539\",\"instance-group-name\":\"wKmBXiO1xm8bK\",\"instance-group-function\":\"testfunction2\",\"relationship-list\":{\"relationship\":[{\"relationDataList\":[{\"relationship-key\":\"cloud-region.cloud-owner\",\"relationship-value\":\"AAI-12002-vm230w\"},{\"relationship-key\":\"cloud-region.cloud-region-id\",\"relationship-value\":\"AAI-region-vm230w\"}],\"relatedToPropertyList\":[{\"property-key\":\"cloud-region.owner-defined-type\",\"property-value\":null}],\"related-to\":\"cloud-region\",\"related-link\":\"/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/AAI-12002-vm230w/AAI-region-vm230w\",\"relationship-label\":\"org.onap.relationships.inventory.Uses\",\"relationship-data\":[{\"relationship-key\":\"cloud-region.cloud-owner\",\"relationship-value\":\"AAI-12002-vm230w\"},{\"relationship-key\":\"cloud-region.cloud-region-id\",\"relationship-value\":\"AAI-region-vm230w\"}],\"related-to-property\":[{\"property-key\":\"cloud-region.owner-defined-type\",\"property-value\":null}]}]}}},{\"instance-group\":{\"id\":\"AAI-12002-test1-vm230w\",\"description\":\"a9DEa0kpY\",\"instance-group-role\":\"JZmha7QSS4tJ\",\"model-invariant-id\":\"model-id1\",\"model-version-id\":\"a0efd5fc-f7be-4502-936a-a6c6392b958f\",\"instance-group-type\":\"type\",\"resource-version\":\"1520886467989\",\"instance-group-name\":\"wKmBXiO1xm8bK\",\"instance-group-function\":\"testfunction2\",\"relationship-list\":{\"relationship\":[{\"relationDataList\":[{\"relationship-key\":\"cloud-region.cloud-owner\",\"relationship-value\":\"AAI-12002-vm230w\"},{\"relationship-key\":\"cloud-region.cloud-region-id\",\"relationship-value\":\"AAI-region-vm230w\"}],\"relatedToPropertyList\":[{\"property-key\":\"cloud-region.owner-defined-type\",\"property-value\":null}],\"related-to\":\"cloud-region\",\"related-link\":\"/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/AAI-12002-vm230w/AAI-region-vm230w\",\"relationship-label\":\"org.onap.relationships.inventory.Uses\",\"relationship-data\":[{\"relationship-key\":\"cloud-region.cloud-owner\",\"relationship-value\":\"AAI-12002-vm230w\"},{\"relationship-key\":\"cloud-region.cloud-region-id\",\"relationship-value\":\"AAI-region-vm230w\"}],\"related-to-property\":[{\"property-key\":\"cloud-region.owner-defined-type\",\"property-value\":null}]}]}}},{\"instance-group\":{\"id\":\"AAI-12002-test2-vm230w\",\"description\":\"a9DEa0kpY\",\"instance-group-role\":\"JZmha7QSS4tJ\",\"model-invariant-id\":\"model-id2\",\"model-version-id\":\"version2\",\"instance-group-type\":\"type\",\"resource-version\":\"1520888629970\",\"instance-group-name\":\"wKmBXiO1xm8bK\",\"instance-group-function\":\"testfunction2\",\"relationship-list\":{\"relationship\":[{\"relationDataList\":[{\"relationship-key\":\"cloud-region.cloud-owner\",\"relationship-value\":\"AAI-12002-vm230w\"},{\"relationship-key\":\"cloud-region.cloud-region-id\",\"relationship-value\":\"AAI-region-vm230w\"}],\"relatedToPropertyList\":[{\"property-key\":\"cloud-region.owner-defined-type\",\"property-value\":null}],\"related-to\":\"cloud-region\",\"related-link\":\"/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/AAI-12002-vm230w/AAI-region-vm230w\",\"relationship-label\":\"org.onap.relationships.inventory.Uses\",\"relationship-data\":[{\"relationship-key\":\"cloud-region.cloud-owner\",\"relationship-value\":\"AAI-12002-vm230w\"},{\"relationship-key\":\"cloud-region.cloud-region-id\",\"relationship-value\":\"AAI-region-vm230w\"}],\"related-to-property\":[{\"property-key\":\"cloud-region.owner-defined-type\",\"property-value\":null}]}]}}}]}\n";
    public static final String GET_NETWORK_COLLECTION_EXPECTED_RESPONSE = "{\"results\":{\"collection\":{\"collection-id\":\"collection-1-2018-rs804s\",\"model-invariant-id\":\"5761e0a7-defj777\",\"model-version-id\":\"5761e0a7-defj232\",\"collection-name\":\"collection-name\",\"collection-type\":\"L3-NETWORK\",\"collection-role\":\"SUB-INTERFACE\",\"collection-function\":\"collection-function\",\"collection-customization-id\":\"custom-unique-data-id\",\"relationship-list\":{\"relationship\":[{\"relationDataList\":[{\"relationship-key\":\"customer.global-customer-id\",\"relationship-value\":\"customer-1-2017-rs804s\"},{\"relationship-key\":\"service-subscription.service-type\",\"relationship-value\":\"service-value7-rs804s\"},{\"relationship-key\":\"service-instance.service-instance-id\",\"relationship-value\":\"2UJZZ01777-rs804s\"}],\"relatedToPropertyList\":[{\"property-key\":\"service-instance.service-instance-name\",\"property-value\":null}],\"related-to\":\"service-instance\",\"related-link\":\"/aai/v13/business/customers/customer/customer-1-2017-rs804s/service-subscriptions/service-subscription/service-value7-rs804s/service-instances/service-instance/2UJZZ01777-rs804s\",\"relationship-label\":\"org.onap.relationships.inventory.MemberOf\",\"relationship-data\":[{\"relationship-key\":\"customer.global-customer-id\",\"relationship-value\":\"customer-1-2017-rs804s\"},{\"relationship-key\":\"service-subscription.service-type\",\"relationship-value\":\"service-value7-rs804s\"},{\"relationship-key\":\"service-instance.service-instance-id\",\"relationship-value\":\"2UJZZ01777-rs804s\"}],\"related-to-property\":[{\"property-key\":\"service-instance.service-instance-name\",\"property-value\":null}]},{\"relationDataList\":[{\"relationship-key\":\"instance-group.id\",\"relationship-value\":\"instanceGroup-2018-rs804s\"}],\"relatedToPropertyList\":[{\"property-key\":\"instance-group.description\",\"property-value\":\"zr6h\"},{\"property-key\":\"instance-group.instance-group-name\",\"property-value\":\"wKmBXiO1xm8bK\"}],\"related-to\":\"instance-group\",\"related-link\":\"/aai/v13/network/instance-groups/instance-group/instanceGroup-2018-rs804s\",\"relationship-label\":\"org.onap.relationships.inventory.MemberOf\",\"relationship-data\":[{\"relationship-key\":\"instance-group.id\",\"relationship-value\":\"instanceGroup-2018-rs804s\"}],\"related-to-property\":[{\"property-key\":\"instance-group.description\",\"property-value\":\"zr6h\"},{\"property-key\":\"instance-group.instance-group-name\",\"property-value\":\"wKmBXiO1xm8bK\"}]}]},\"resource-version\":\"1521662811309\"},\"networks\":[{\"network-id\":\"l3network-id-rs804s\",\"network-name\":\"oam-net\",\"network-type\":\"Tenant_Layer_3\",\"network-role\":\"RosemaProtectedOam.OAM\",\"network-technology\":\"Contrail\",\"is-bound-to-vpn\":false,\"resource-version\":\"1521662814627\",\"orchestration-status\":\"Created\",\"is-provider-network\":false,\"is-shared-network\":false,\"is-external-network\":false,\"relationship-list\":{\"relationship\":[{\"relationDataList\":[{\"relationship-key\":\"instance-group.id\",\"relationship-value\":\"instanceGroup-2018-rs804s\"}],\"relatedToPropertyList\":[{\"property-key\":\"instance-group.description\",\"property-value\":\"zr6h\"},{\"property-key\":\"instance-group.instance-group-name\",\"property-value\":\"wKmBXiO1xm8bK\"}],\"related-to\":\"instance-group\",\"related-link\":\"/aai/v13/network/instance-groups/instance-group/instanceGroup-2018-rs804s\",\"relationship-label\":\"org.onap.relationships.inventory.MemberOf\",\"relationship-data\":[{\"relationship-key\":\"instance-group.id\",\"relationship-value\":\"instanceGroup-2018-rs804s\"}],\"related-to-property\":[{\"property-key\":\"instance-group.description\",\"property-value\":\"zr6h\"},{\"property-key\":\"instance-group.instance-group-name\",\"property-value\":\"wKmBXiO1xm8bK\"}]}]}},{\"network-id\":\"l3network-id-3-rs804s\",\"network-name\":\"oam-net\",\"network-type\":\"Tenant_Layer_3\",\"network-role\":\"RosemaProtectedOam.OAM\",\"network-technology\":\"Contrail\",\"is-bound-to-vpn\":false,\"resource-version\":\"1521662816043\",\"orchestration-status\":\"Created\",\"is-provider-network\":false,\"is-shared-network\":false,\"is-external-network\":false,\"relationship-list\":{\"relationship\":[{\"relationDataList\":[{\"relationship-key\":\"instance-group.id\",\"relationship-value\":\"instanceGroup-2018-rs804s\"}],\"relatedToPropertyList\":[{\"property-key\":\"instance-group.description\",\"property-value\":\"zr6h\"},{\"property-key\":\"instance-group.instance-group-name\",\"property-value\":\"wKmBXiO1xm8bK\"}],\"related-to\":\"instance-group\",\"related-link\":\"/aai/v13/network/instance-groups/instance-group/instanceGroup-2018-rs804s\",\"relationship-label\":\"org.onap.relationships.inventory.MemberOf\",\"relationship-data\":[{\"relationship-key\":\"instance-group.id\",\"relationship-value\":\"instanceGroup-2018-rs804s\"}],\"related-to-property\":[{\"property-key\":\"instance-group.description\",\"property-value\":\"zr6h\"},{\"property-key\":\"instance-group.instance-group-name\",\"property-value\":\"wKmBXiO1xm8bK\"}]}]}},{\"network-id\":\"l3network-id-2-rs804s\",\"network-name\":\"oam-net\",\"network-type\":\"Tenant_Layer_3\",\"network-role\":\"RosemaProtectedOam.OAM\",\"network-technology\":\"Contrail\",\"is-bound-to-vpn\":false,\"resource-version\":\"1521662815304\",\"orchestration-status\":\"Created\",\"is-provider-network\":false,\"is-shared-network\":false,\"is-external-network\":false,\"relationship-list\":{\"relationship\":[{\"relationDataList\":[{\"relationship-key\":\"instance-group.id\",\"relationship-value\":\"instanceGroup-2018-rs804s\"}],\"relatedToPropertyList\":[{\"property-key\":\"instance-group.description\",\"property-value\":\"zr6h\"},{\"property-key\":\"instance-group.instance-group-name\",\"property-value\":\"wKmBXiO1xm8bK\"}],\"related-to\":\"instance-group\",\"related-link\":\"/aai/v13/network/instance-groups/instance-group/instanceGroup-2018-rs804s\",\"relationship-label\":\"org.onap.relationships.inventory.MemberOf\",\"relationship-data\":[{\"relationship-key\":\"instance-group.id\",\"relationship-value\":\"instanceGroup-2018-rs804s\"}],\"related-to-property\":[{\"property-key\":\"instance-group.description\",\"property-value\":\"zr6h\"},{\"property-key\":\"instance-group.instance-group-name\",\"property-value\":\"wKmBXiO1xm8bK\"}]}]}}],\"service-instance\":{\"service-instance-id\":\"2UJZZ01777-rs804s\",\"resource-version\":\"1521662813382\",\"relationship-list\":{\"relationship\":[{\"relationDataList\":[{\"relationship-key\":\"collection.collection-id\",\"relationship-value\":\"collection-1-2018-rs804s\"}],\"relatedToPropertyList\":null,\"related-to\":\"collection\",\"related-link\":\"/aai/v13/network/collections/collection/collection-1-2018-rs804s\",\"relationship-label\":\"org.onap.relationships.inventory.MemberOf\",\"relationship-data\":[{\"relationship-key\":\"collection.collection-id\",\"relationship-value\":\"collection-1-2018-rs804s\"}],\"related-to-property\":null}]}},\"instance-group\":{\"id\":\"instanceGroup-2018-rs804s\",\"description\":\"zr6h\",\"instance-group-role\":\"JZmha7QSS4tJ\",\"model-invariant-id\":\"5761e0a7-defj777\",\"model-version-id\":\"5761e0a7-defj22\",\"instance-group-type\":\"7DDjOdNL\",\"resource-version\":\"1521662814023\",\"instance-group-name\":\"wKmBXiO1xm8bK\",\"instance-group-function\":\"testfunction2\",\"relationship-list\":{\"relationship\":[{\"relationDataList\":[{\"relationship-key\":\"l3-network.network-id\",\"relationship-value\":\"l3network-id-rs804s\"}],\"relatedToPropertyList\":[{\"property-key\":\"l3-network.network-name\",\"property-value\":\"oam-net\"}],\"related-to\":\"l3-network\",\"related-link\":\"/aai/v13/network/l3-networks/l3-network/l3network-id-rs804s\",\"relationship-label\":\"org.onap.relationships.inventory.MemberOf\",\"relationship-data\":[{\"relationship-key\":\"l3-network.network-id\",\"relationship-value\":\"l3network-id-rs804s\"}],\"related-to-property\":[{\"property-key\":\"l3-network.network-name\",\"property-value\":\"oam-net\"}]},{\"relationDataList\":[{\"relationship-key\":\"collection.collection-id\",\"relationship-value\":\"collection-1-2018-rs804s\"}],\"relatedToPropertyList\":null,\"related-to\":\"collection\",\"related-link\":\"/aai/v13/network/collections/collection/collection-1-2018-rs804s\",\"relationship-label\":\"org.onap.relationships.inventory.MemberOf\",\"relationship-data\":[{\"relationship-key\":\"collection.collection-id\",\"relationship-value\":\"collection-1-2018-rs804s\"}],\"related-to-property\":null},{\"relationDataList\":[{\"relationship-key\":\"l3-network.network-id\",\"relationship-value\":\"l3network-id-3-rs804s\"}],\"relatedToPropertyList\":[{\"property-key\":\"l3-network.network-name\",\"property-value\":\"oam-net\"}],\"related-to\":\"l3-network\",\"related-link\":\"/aai/v13/network/l3-networks/l3-network/l3network-id-3-rs804s\",\"relationship-label\":\"org.onap.relationships.inventory.MemberOf\",\"relationship-data\":[{\"relationship-key\":\"l3-network.network-id\",\"relationship-value\":\"l3network-id-3-rs804s\"}],\"related-to-property\":[{\"property-key\":\"l3-network.network-name\",\"property-value\":\"oam-net\"}]},{\"relationDataList\":[{\"relationship-key\":\"l3-network.network-id\",\"relationship-value\":\"l3network-id-2-rs804s\"}],\"relatedToPropertyList\":[{\"property-key\":\"l3-network.network-name\",\"property-value\":\"oam-net\"}],\"related-to\":\"l3-network\",\"related-link\":\"/aai/v13/network/l3-networks/l3-network/l3network-id-2-rs804s\",\"relationship-label\":\"org.onap.relationships.inventory.MemberOf\",\"relationship-data\":[{\"relationship-key\":\"l3-network.network-id\",\"relationship-value\":\"l3network-id-2-rs804s\"}],\"related-to-property\":[{\"property-key\":\"l3-network.network-name\",\"property-value\":\"oam-net\"}]}]}}}}\n";

    public static final String GET_SERVICE_INSTANCE_BY_SUBSCRIBER_DSL_EXPECTED_RESPONSE = "{\"results\": [{\"customer\": {\"global-customer-id\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\",\"subscriber-name\": \"Mobility\",\"subscriber-type\": \"INFRA\",\"resource-version\": \"1602518417955\",\"related-nodes\": [{\"service-subscription\": {\"service-type\": \"VPMS\",\"resource-version\": \"1629183620246\",\"related-nodes\": [{\"service-instance\": {\"service-instance-id\": \"5d942bc7-3acf-4e35-836a-393619ebde66\",\"service-instance-name\": \"dpa2actsf5001v_Port_Mirroring_dpa2a_SVC\",\"service-type\": \"PORT-MIRROR\",\"service-role\": \"VPROBE\",\"environment-context\": \"General_Revenue-Bearing\",\"workload-context\": \"Production\",\"model-invariant-id\": \"0757d856-a9c6-450d-b494-e1c0a4aab76f\",\"model-version-id\": \"a9088517-efe8-4bed-9c54-534462cb08c2\",\"resource-version\": \"1615330529236\",\"selflink\": \"SOME_SELF_LINK\",\"orchestration-status\": \"Active\"}}]}}]}}]}";
    public static final String GET_SEARCH_SERVICE_INSTANCE_BY_SUBSCRIBER_DSL_EXPECTED_RESPONSE = "{\"service-instances\":[{\"serviceInstanceId\":\"5d942bc7-3acf-4e35-836a-393619ebde66\",\"serviceType\":\"VPMS\",\"serviceInstanceName\":\"dpa2actsf5001v_Port_Mirroring_dpa2a_SVC\",\"subscriberName\":\"Mobility\",\"aaiModelInvariantId\":\"0757d856-a9c6-450d-b494-e1c0a4aab76f\",\"aaiModelVersionId\":\"a9088517-efe8-4bed-9c54-534462cb08c2\",\"owningEntityId\":null,\"isPermitted\":false,\"globalCustomerId\":\"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\"}]}";

    public static final String GET_AAI_SERVIES_EXPECTED_RESULT = "{\n" +
            "  \"services\": [{\n" +
            "    \"uuid\": \"20c4431c-246d-11e7-93ae-92361f002671\",\n" +
            "    \"invariantUUID\": \"78ca26d0-246d-11e7-93ae-92361f002671\",\n" +
            "    \"name\": \"vSAMP10aDEV::base::module-0\",\n" +
            "    \"version\": \"2\",\n" +
            "    \"toscaModelURL\": null,\n" +
            "    \"category\": \"resource\",\n" +
            "    \"lifecycleState\": null,\n" +
            "    \"lastUpdaterUserId\": null,\n" +
            "    \"lastUpdaterFullName\": null,\n" +
            "    \"distributionStatus\": \"DISTRIBUTION_COMPLETE_OK\",\n" +
            "    \"orchestrationType\": null,\n" +
            "    \"isInstantiationTemplateExists\": false,\n" +
            "    \"artifacts\": null,\n" +
            "    \"resources\": null\n" +
            "  }, {\n" +
            "    \"uuid\": \"797a6c41-0f80-4d35-a288-3920c4e06baa\",\n" +
            "    \"invariantUUID\": \"5b607929-6088-4614-97ef-cac817508e0e\",\n" +
            "    \"name\": \"CONTRAIL30_L2NODHCP\",\n" +
            "    \"version\": \"1.0\",\n" +
            "    \"toscaModelURL\": null,\n" +
            "    \"category\": \"resource\",\n" +
            "    \"lifecycleState\": null,\n" +
            "    \"lastUpdaterUserId\": null,\n" +
            "    \"lastUpdaterFullName\": null,\n" +
            "    \"distributionStatus\": \"DISTRIBUTION_COMPLETE_ERROR\",\n" +
            "    \"orchestrationType\": null,\n" +
            "    \"isInstantiationTemplateExists\": false,\n" +
            "    \"artifacts\": null,\n" +
            "    \"resources\": null\n" +
            "  }, {\n" +
            "    \"uuid\": \"f1bde010-cc5f-4765-941f-75f15b24f9fc\",\n" +
            "    \"invariantUUID\": \"0143d57b-a517-4de9-a0a1-eb76db51f402\",\n" +
            "    \"name\": \"BkVmxAv061917..base_vPE_AV..module-0\",\n" +
            "    \"version\": \"2\",\n" +
            "    \"toscaModelURL\": null,\n" +
            "    \"category\": \"resource\",\n" +
            "    \"lifecycleState\": null,\n" +
            "    \"lastUpdaterUserId\": null,\n" +
            "    \"lastUpdaterFullName\": null,\n" +
            "    \"orchestrationType\": null,\n" +
            "    \"isInstantiationTemplateExists\": false,\n" +
            "    \"distributionStatus\": \"DISTRIBUTION_COMPLETE_OK\",\n" +
            "    \"artifacts\": null,\n" +
            "    \"resources\": null\n" +
            "  }, {\n" +
            "    \"uuid\": \"ipe-resource-id-ps-02\",\n" +
            "    \"invariantUUID\": \"ipe-resource-id-ps-02\",\n" +
            "    \"name\": \"abc\",\n" +
            "    \"version\": \"v1.0\",\n" +
            "    \"toscaModelURL\": null,\n" +
            "    \"category\": \"resource\",\n" +
            "    \"lifecycleState\": null,\n" +
            "    \"lastUpdaterUserId\": null,\n" +
            "    \"lastUpdaterFullName\": null,\n" +
            "    \"distributionStatus\": \"DISTRIBUTION_COMPLETE_OK\",\n" +
            "    \"orchestrationType\": null,\n" +
            "    \"isInstantiationTemplateExists\": false,\n" +
            "    \"artifacts\": null,\n" +
            "    \"resources\": null\n" +
            "  }, {\n" +
            "    \"uuid\": \"lmoser410-connector-model-version-id\",\n" +
            "    \"invariantUUID\": \"lmoser410-connector-model-id\",\n" +
            "    \"name\": \"connector\",\n" +
            "    \"version\": \"v1.0\",\n" +
            "    \"toscaModelURL\": null,\n" +
            "    \"category\": \"widget\",\n" +
            "    \"lifecycleState\": null,\n" +
            "    \"lastUpdaterUserId\": null,\n" +
            "    \"lastUpdaterFullName\": null,\n" +
            "    \"distributionStatus\": \"DISTRIBUTION_COMPLETE_OK\",\n" +
            "    \"orchestrationType\": null,\n" +
            "    \"isInstantiationTemplateExists\": false,\n" +
            "    \"artifacts\": null,\n" +
            "    \"resources\": null\n" +
            "  }, {\n" +
            "    \"uuid\": \"ff2ae348-214a-11e7-93ae-92361f002673\",\n" +
            "    \"invariantUUID\": \"3a97db99-c4bb-498a-a13a-38f65f1ced3d\",\n" +
            "    \"name\": \"vSAMP10aDEV::base::module-0\",\n" +
            "    \"version\": \"1.0\",\n" +
            "    \"toscaModelURL\": null,\n" +
            "    \"category\": \"resource\",\n" +
            "    \"lifecycleState\": null,\n" +
            "    \"lastUpdaterUserId\": null,\n" +
            "    \"lastUpdaterFullName\": null,\n" +
            "    \"orchestrationType\": null,\n" +
            "    \"isInstantiationTemplateExists\": false,\n" +
            "    \"distributionStatus\": \"DISTRIBUTION_COMPLETE_OK\",\n" +
            "    \"artifacts\": null,\n" +
            "    \"resources\": null\n" +
            "  }, {\n" +
            "    \"uuid\": \"204c641a-3494-48c8-979a-86856f5fd32a\",\n" +
            "    \"invariantUUID\": \"3c504d40-b847-424c-9d25-4fb7e0a3e994\",\n" +
            "    \"name\": \"named-query-element\",\n" +
            "    \"version\": \"1.0\",\n" +
            "    \"toscaModelURL\": null,\n" +
            "    \"category\": \"widget\",\n" +
            "    \"lifecycleState\": null,\n" +
            "    \"lastUpdaterUserId\": null,\n" +
            "    \"lastUpdaterFullName\": null,\n" +
            "    \"orchestrationType\": null,\n" +
            "    \"isInstantiationTemplateExists\": false,\n" +
            "    \"distributionStatus\": \"DISTRIBUTION_COMPLETE_OK\",\n" +
            "    \"artifacts\": null,\n" +
            "    \"resources\": null\n" +
            "  }, {\n" +
            "    \"uuid\": \"acba1f72-c6e0-477f-9426-ad190151e100\",\n" +
            "    \"invariantUUID\": \"93e56950-cb19-44e6-ace4-8b50f2d02e45\",\n" +
            "    \"name\": \"RG_6-19_Test\",\n" +
            "    \"version\": \"1.0\",\n" +
            "    \"toscaModelURL\": null,\n" +
            "    \"category\": \"resource\",\n" +
            "    \"lifecycleState\": null,\n" +
            "    \"lastUpdaterUserId\": null,\n" +
            "    \"lastUpdaterFullName\": null,\n" +
            "    \"orchestrationType\": null,\n" +
            "    \"isInstantiationTemplateExists\": false,\n" +
            "    \"distributionStatus\": \"DISTRIBUTION_COMPLETE_OK\",\n" +
            "    \"artifacts\": null,\n" +
            "    \"resources\": null\n" +
            "  }, {\n" +
            "    \"uuid\": \"fc65e5e7-45c7-488a-b36d-f453ab3057fe\",\n" +
            "    \"invariantUUID\": \"ee448504-ceee-47db-8e1b-742115f219db\",\n" +
            "    \"name\": \"ciServicea268facd387e\",\n" +
            "    \"version\": \"1.0\",\n" +
            "    \"toscaModelURL\": null,\n" +
            "    \"category\": \"service\",\n" +
            "    \"lifecycleState\": null,\n" +
            "    \"lastUpdaterUserId\": null,\n" +
            "    \"lastUpdaterFullName\": null,\n" +
            "    \"orchestrationType\": null,\n" +
            "    \"isInstantiationTemplateExists\": false,\n" +
            "    \"distributionStatus\": \"DISTRIBUTION_COMPLETE_OK\",\n" +
            "    \"artifacts\": null,\n" +
            "    \"resources\": null\n" +
            "  }, {\n" +
            "    \"uuid\": \"027948b6-25e2-4e39-b87d-d9f5797941de\",\n" +
            "    \"invariantUUID\": \"56f2d0d3-7943-4159-bf01-b82692ec035e\",\n" +
            "    \"name\": \"service_sanity_amir\",\n" +
            "    \"version\": \"2.0\",\n" +
            "    \"toscaModelURL\": null,\n" +
            "    \"category\": \"service\",\n" +
            "    \"lifecycleState\": null,\n" +
            "    \"lastUpdaterUserId\": null,\n" +
            "    \"lastUpdaterFullName\": null,\n" +
            "    \"orchestrationType\": null,\n" +
            "    \"isInstantiationTemplateExists\": false,\n" +
            "    \"distributionStatus\": \"DISTRIBUTION_COMPLETE_OK\",\n" +
            "    \"artifacts\": null,\n" +
            "    \"resources\": null\n" +
            "  }, {\n" +
            "    \"uuid\": \"fbf96e3b-1804-4c89-bf5b-53acb7f2edc0\",\n" +
            "    \"invariantUUID\": \"56f2d0d3-7943-4159-bf01-b82692ec035e\",\n" +
            "    \"name\": \"service_sanity_amir\",\n" +
            "    \"version\": \"3.0\",\n" +
            "    \"toscaModelURL\": null,\n" +
            "    \"category\": \"service\",\n" +
            "    \"lifecycleState\": null,\n" +
            "    \"lastUpdaterUserId\": null,\n" +
            "    \"lastUpdaterFullName\": null,\n" +
            "    \"orchestrationType\": null,\n" +
            "    \"isInstantiationTemplateExists\": false,\n" +
            "    \"distributionStatus\": \"DISTRIBUTION_COMPLETE_OK\",\n" +
            "    \"artifacts\": null,\n" +
            "    \"resources\": null\n" +
            "  }],\n" +
            "  \"readOnly\": false\n" +
            "}";
    private static final String AAI_GET_ACTIVE_NETWORKS = "/aai_get_active_networks";

    private String getGetOperationEnvironmentsUri() {
        return uri.toASCIIString() + "/get_operational_environments";
    }

    private String getAaiServicesUri() {
        return uri.toASCIIString() + "/rest/models/services";
    }

    private String getGetOperationEnvironmentUriWithParameters() {
        String url = getGetOperationEnvironmentsUri();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                // Add query parameter
                .queryParam("operationalEnvironmentStatus", OPERATIONAL_ENVIRONMENT_STATUS)
                .queryParam("operationalEnvironmentType", OPERATIONAL_ENVIRONMENT_TYPE);

        String urlWithParameters = builder.toUriString();
        return urlWithParameters;

    }

    private AaiResponse<OperationalEnvironmentList> loginAndDoGetWithUrl(String url) {
        ResponseEntity<AaiResponse<OperationalEnvironmentList>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<AaiResponse<OperationalEnvironmentList>>() {});
        AaiResponse<OperationalEnvironmentList> response = responseEntity.getBody();
        return response;
    }

    @Test
    public void testErrorGetOperationalEnvironments() {
        //Register required response
        SimulatorApi.registerExpectation(GET_OPERATIONAL_ENVIRONMENTS_JSON_ERROR, APPEND);
        String url = getGetOperationEnvironmentsUri();
        AaiResponse<OperationalEnvironmentList> response = loginAndDoGetWithUrl(url);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getHttpCode());
        assertEquals("simulated error text", response.getErrorMessage());


    }

    //This test requires a simulator which runs on VID
    @Test
    public void testSuccessGetOperationalEnvironments() {
        //Register required response
        String uuidOfOperationalEnvironment = "f07ca256-96dd-40ad-b4d2-7a77e2a974ed";
        SimulatorApi.registerExpectation(GET_OPERATIONAL_ENVIRONMENTS_JSON, ImmutableMap.of("UUID_of_Operational_Environment", uuidOfOperationalEnvironment), APPEND);
        String url = getGetOperationEnvironmentUriWithParameters();
        AaiResponse<OperationalEnvironmentList> response = loginAndDoGetWithUrl(url);
        assertEquals(HttpStatus.OK.value(), response.getHttpCode());
        OperationalEnvironmentList list = response.getT();
        assertNotNull(list.getOperationalEnvironment());
        assertEquals(2, list.getOperationalEnvironment().size());
        assertEquals(uuidOfOperationalEnvironment, list.getOperationalEnvironment().get(0).getOperationalEnvironmentId());
        assertEquals(1, list.getOperationalEnvironment().get(0).getRelationshipList().getRelationship().size());
    }

    @Test(dataProvider = "errorCodes")
    public void getServicesWitErrorResponse(int errorCode) throws IOException, URISyntaxException {
        TestHelper.resetAaiCache(GET_SERVICE_MODELS_BY_DISTRIBUTION_STATUS, restTemplate, uri);
        final String expectedResult = "{\"services\":[],\"readOnly\":false}";

        callAaiWithSimulatedErrorResponse(AAI_GET_SERVICES_ERROR_SIMULATOR_RESPONSES,
                ImmutableMap.of("500", Integer.toString(errorCode), "ERROR_PAYLOAD", StringEscapeUtils.escapeJson(expectedResult)),
                getAaiServicesUri(), "", 200, expectedResult, HttpMethod.GET);

    }

    @Test
    public void getServicesFineRequest() throws IOException, URISyntaxException {
        TestHelper.resetAaiCache(GET_SERVICE_MODELS_BY_DISTRIBUTION_STATUS, restTemplate, uri);
        callAaiWithSimulatedErrorResponse(AAI_GET_SERVICES_FINE_SIMULATOR_RESPONSES,
                ImmutableMap.of(),
                getAaiServicesUri(), "", 200, GET_AAI_SERVIES_EXPECTED_RESULT, HttpMethod.GET);
    }

    @Test
    public void whenGetServicesErrorResponse_badResponseIsNotCached() throws IOException, URISyntaxException {
        TestHelper.resetAaiCache(GET_SERVICE_MODELS_BY_DISTRIBUTION_STATUS, restTemplate, uri);


        final String expectedErrorResult = "{\"services\":[],\"readOnly\":false}";
        //call AAI with bad response by clear exceptions from simulator, bad response shall not be cached
        callAaiWithSimulatedErrorResponse(new String[]{}, ImmutableMap.of(), getAaiServicesUri(), "", 200, expectedErrorResult, HttpMethod.GET);

        //call AAI with fine response
        callAaiWithSimulatedErrorResponse(AAI_GET_SERVICES_FINE_SIMULATOR_RESPONSES,
                ImmutableMap.of(),
                getAaiServicesUri(), "", 200, GET_AAI_SERVIES_EXPECTED_RESULT, HttpMethod.GET);
    }

    @DataProvider
    public static Object[][] errorCodes(Method test) {
        return new Object[][]{
                {500},{505}, {400}, {401}, {405}
        };
    }

    @Test
    public void whenThrowExceptionInsAaiResponseErrorAreLogged() {
        String notAJson = "not a json";
        SimulatorApi.registerExpectationFromPreset(new PresetAAIBadBodyForGetServicesGet(notAJson), CLEAR_THEN_SET);
        SimulatorApi.registerExpectationFromPresets(getEcompPortalPresets(), APPEND);
        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetSubscribersGet(), APPEND);

        restTemplateErrorAgnostic.getForEntity(uri + "/aai_get_services", String.class);
        String logLines = LoggerFormatTest.getLogLines(LogName.error, 15, 0, restTemplate, uri);

        assertThat("not found in error log", logLines, containsString("Failed to parse aai response"));
        assertThat("not found in error log", logLines, containsString(notAJson));
        assertThat("not found in error log", logLines, containsString("GetServicesAAIRespone"));

    }

    @Test
    public void portMirroringConfigData_givenValidAaiResponse_yieldCloudRegionId() {
        SimulatorApi.registerExpectationFromPreset(new PresetAAICloudRegionAndSourceFromConfigurationPut("SOME-RANDOM-UUID", "THE-EXPECTED-REGION-ID"), CLEAR_THEN_SET);

        final String response = restTemplate.getForObject(uri + "/aai_getPortMirroringConfigsData?configurationIds=" + "SOME-RANDOM-UUID", String.class);

        final ImmutableMap<String, ImmutableMap<String, String>> expected = ImmutableMap.of(
                "SOME-RANDOM-UUID", ImmutableMap.of(
                        "cloudRegionId", "THE-EXPECTED-REGION-ID"
                ));

        assertResponse(expected, response);

        /*
        More tests:
        [ ]  Error responses from AAI (404 etc): descriptive error response, including what tried and what happened
        [ ]  malformed response from AAI: descriptive error response, including the payload
        [ ]  empty/missing value for configurationId: client-error http code
         */
    }


    @Test
    public void portMirroringSourcePorts_validAAIResponseWithSinglePort_yieldCorrectPortData() {
        SimulatorApi.registerExpectationFromPreset(
                new PresetAAIGetPortMirroringSourcePorts("CONFIGURATION-ID", "INTERFACE-ID", "INTERFACE-NAME", true),
                CLEAR_THEN_SET
        );

        final String response = restTemplate.getForObject(uri + "/aai_getPortMirroringSourcePorts?configurationIds=" + "CONFIGURATION-ID", String.class);

        final ImmutableMap<String, ImmutableList> expected = ImmutableMap.of(
                "CONFIGURATION-ID", ImmutableList.of(ImmutableMap.of(
                        "interfaceId", "INTERFACE-ID", "interfaceName", "INTERFACE-NAME", "isPortMirrored", true
                )));

        assertResponse(expected, response);
    }

    @Test
    public void portMirroringSourcePorts_nullValueForInterfaceId_yield200OkWithFineDescription() {
        final PresetAAIGetPortMirroringSourcePorts preset = new PresetAAIGetPortMirroringSourcePorts("CONFIGURATION-ID", null, "INTERFACE-NAME", true);
        SimulatorApi.registerExpectationFromPreset(
                preset,
                CLEAR_THEN_SET
        );

        final String response = restTemplate.getForObject(uri + "/aai_getPortMirroringSourcePorts?configurationIds=" + "CONFIGURATION-ID", String.class);

        final ImmutableMap<String, ImmutableList<ImmutableMap>> expected = ImmutableMap.of(
                "CONFIGURATION-ID", ImmutableList.of(ImmutableMap.of(
                        "errorDescription", "Value of 'interface-id' is missing.",
                        "rawAaiResponse", preset.getResponseBody().toString()
                )));

        assertResponse(expected, response);
    }

    @Test
    public void portMirroringSourcePorts_given503ErrorAaiResponse_yield200OkWithErrorMsg() {
        final PresetAAIGetPortMirroringSourcePortsError preset = new PresetAAIGetPortMirroringSourcePortsError("CONFIGURATION-ID", "INTERFACE-ID", "INTERFACE-NAME", true);
        SimulatorApi.registerExpectationFromPreset(
                preset,
                CLEAR_THEN_SET
        );

        final String response = restTemplate.getForObject(uri + "/aai_getPortMirroringSourcePorts?configurationIds=" + "CONFIGURATION-ID", String.class);

        final ImmutableMap<String, ImmutableList<ImmutableMap>> expected = ImmutableMap.of(
                "CONFIGURATION-ID", ImmutableList.of(ImmutableMap.of(
                        "errorDescription", "Got 503 from aai",
                        "rawAaiResponse", preset.getResponseBody()
                )));

        assertResponse(expected, response);
    }

    @Test
    public void portMirroringConfigData_given404ErrorAaiResponse_yield200OkWithErrorMsg() {
        SimulatorApi.clearAll();

        final String response = restTemplate.getForObject(uri + "/aai_getPortMirroringConfigsData?configurationIds=" + "SOME-RANDOM-UUID", String.class);

        final ImmutableMap<String, ImmutableMap<String, String>> expected = ImmutableMap.of(
                "SOME-RANDOM-UUID", ImmutableMap.of(
                        "errorDescription", "Got 404 from aai",
                        "rawAaiResponse", ""
                ));

        assertResponse(expected, response);
    }

    @Test
    public void getNetworkCollectionDetailsByServiceInstanceId_yieldValidResponse() {
        SimulatorApi.clearAll();
        final PresetAAIGetNetworkCollectionDetails presetAAIGetNetworkCollectionDetails = new PresetAAIGetNetworkCollectionDetails("SOME-RANDOM-UUID");
        SimulatorApi.registerExpectationFromPreset(presetAAIGetNetworkCollectionDetails, CLEAR_THEN_SET);
        final String response = restTemplate.getForObject(uri + "/aai_get_network_collection_details/" + "SOME-RANDOM-UUID", String.class);

        assertResponse(GET_NETWORK_COLLECTION_EXPECTED_RESPONSE, response);
    }

    @Test
    public void getNetworkCollectionDetailsByServiceInstanceId_responseWithExtraFields_yieldValidResponse() {
        SimulatorApi.clearAll();
        final PresetAAIGetNetworkCollectionDetailsInvalidRequest presetAAIGetNetworkCollectionDetails = new PresetAAIGetNetworkCollectionDetailsInvalidRequest("SOME-RANDOM-UUID");
        SimulatorApi.registerExpectationFromPreset(presetAAIGetNetworkCollectionDetails, CLEAR_THEN_SET);
        final String response = restTemplate.getForObject(uri + "/aai_get_network_collection_details/" + "SOME-RANDOM-UUID", String.class);

        assertResponse(GET_NETWORK_COLLECTION_EXPECTED_RESPONSE, response);
    }

    @Test
    public void getNetworkCollectionDetailsByServiceInstanceId_given404ErrorAaiResponse_yield200OkWithErrorMsg() {
        SimulatorApi.clearAll();
        try {
            restTemplate.getForObject(uri + "/aai_get_network_collection_details/" + "SOME-RANDOM-UUID", String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void getNetworkCollectionDetailsByServiceInstanceId_responseWithRequiredMissing() {
        SimulatorApi.clearAll();
        final PresetAAIGetNetworkCollectionDetailsRequiredMissing presetAAIGetNetworkCollectionDetails = new PresetAAIGetNetworkCollectionDetailsRequiredMissing("SOME-RANDOM-UUID");
        SimulatorApi.registerExpectationFromPreset(presetAAIGetNetworkCollectionDetails, CLEAR_THEN_SET);
        try {
            restTemplate.getForObject(uri + "/aai_get_network_collection_details/" + "SOME-RANDOM-UUID", String.class);
        } catch (HttpServerErrorException e) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
        }
    }

    @Test
    public void getGetInstanceGroupsByCloudRegion_yieldValidResponse() {
        SimulatorApi.clearAll();
        final PresetAAIGetInstanceGroupsByCloudRegion presetAAIGetInstanceGroupsByCloudRegion = new PresetAAIGetInstanceGroupsByCloudRegion("CLOUD%20OWNER", "CLOUD-REGION-ID", "NETWORK%20FUNCTION");
        SimulatorApi.registerExpectationFromPreset(presetAAIGetInstanceGroupsByCloudRegion, CLEAR_THEN_SET);
        final String response = restTemplate.getForObject(uri + "/aai_get_instance_groups_by_cloudregion/" + "CLOUD OWNER" + "/" + "CLOUD-REGION-ID" + "/" + "NETWORK FUNCTION", String.class);

        assertResponse(GET_INSTANCE_GROUPS_BY_CLOUDREGION_EXPECTED_RESPONSE, response);
    }

    @Test
    public void getGetInstanceGroupsByCloudRegion_responseWithExtraFields_yieldValidResponse() {
        SimulatorApi.clearAll();
        final PresetAAIGetInstanceGroupsByCloudRegionInvalidRequest presetAAIGetInstanceGroupsByCloudRegion = new PresetAAIGetInstanceGroupsByCloudRegionInvalidRequest("CLOUD-OWNER", "CLOUD-REGION-ID", "NETWORK-FUNCTION");
        SimulatorApi.registerExpectationFromPreset(presetAAIGetInstanceGroupsByCloudRegion, CLEAR_THEN_SET);
        final String response = restTemplate.getForObject(uri + "/aai_get_instance_groups_by_cloudregion/" + "CLOUD-OWNER" + "/" + "CLOUD-REGION-ID" + "/" + "NETWORK-FUNCTION", String.class);

        assertResponse(GET_INSTANCE_GROUPS_BY_CLOUDREGION_EXPECTED_RESPONSE, response);
    }

    @Test
    public void getGetInstanceGroupsByCloudRegion_given404ErrorAaiResponse_yield200OkWithErrorMsg() {
        SimulatorApi.clearAll();
        try {
            restTemplate.getForObject(uri + "/aai_get_instance_groups_by_cloudregion/" + "CLOUD-OWNER" + "/" + "CLOUD-REGION-ID" + "/" + "NETWORK-FUNCTION", String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void getGetInstanceGroupsByCloudRegion_responseWithRequiredMissing() {
        SimulatorApi.clearAll();
        final PresetAAIGetInstanceGroupsByCloudRegionRequiredMissing presetAAIGetInstanceGroupsByCloudRegion = new PresetAAIGetInstanceGroupsByCloudRegionRequiredMissing("CLOUD-OWNER", "CLOUD-REGION-ID", "NETWORK-FUNCTION");
        SimulatorApi.registerExpectationFromPreset(presetAAIGetInstanceGroupsByCloudRegion, CLEAR_THEN_SET);
        try {
            restTemplate.getForObject(uri + "/aai_get_instance_groups_by_cloudregion/" + "CLOUD-OWNER" + "/" + "CLOUD-REGION-ID" + "/" + "NETWORK-FUNCTION", String.class);
        } catch (HttpServerErrorException e) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
        }
    }

    @Test
    public void portMirroringConfigData_emptyIdOnAaiResponse_yieldError() {
        final PresetAAICloudRegionAndSourceFromConfigurationPut presetAAIResponseWitnProblem = new PresetAAICloudRegionAndSourceFromConfigurationPut("SOME-RANDOM-UUID", "");
        SimulatorApi.registerExpectationFromPreset(presetAAIResponseWitnProblem, CLEAR_THEN_SET);

        final String response = restTemplate.getForObject(uri + "/aai_getPortMirroringConfigsData?configurationIds=" + "SOME-RANDOM-UUID", String.class);

        final ImmutableMap<String, ImmutableMap<String, String>> expected = ImmutableMap.of(
                "SOME-RANDOM-UUID", ImmutableMap.of(
                        "errorDescription", "Node 'properties.cloud-region-id' of node-type 'cloud-region' is blank",
                        "rawAaiResponse", presetAAIResponseWitnProblem.getResponseBody().toString().replace(" ", "")
                ));

        assertResponse(expected, response);
    }

    @Test
    public void getGetRelatedInstanceGroupsByVnfId__yieldValidResponse() {
        String vnfId = "some_vnf_id";
        final PresetAAIGetRelatedInstanceGroupsByVnfId getRelatedInstanceGroupsByVnfId = new PresetAAIGetRelatedInstanceGroupsByVnfId(vnfId);
        SimulatorApi.registerExpectationFromPreset(getRelatedInstanceGroupsByVnfId, CLEAR_THEN_SET);

        final String response = restTemplate.getForObject(uri + "/aai_get_instance_groups_by_vnf_instance_id/" + vnfId,
                String.class);

        assertResponse("[{\"type\":\"instance-group\",\"name\":\"instance group name\"},{\"type\":\"instance-group\",\"name\":\"instance group name\"}]", response);
    }

    @Test
    public void getHomingDataForVfModule(){
        String vnfId= "0846287b-65bf-45a6-88f6-6a1af4149fac", vfModuleId= "a9b70ac0-5917-4203-a308-0e6920e6d09b";
        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetHomingForVfModule(vnfId,vfModuleId), CLEAR_THEN_SET);
        final String response = restTemplate.getForObject(uri + "/aai_get_homing_by_vfmodule/"+ vnfId +"/"+vfModuleId, String.class);
        String exectedResponse = TestUtils.convertRequest(objectMapper, AAI_HOMING_DATA_RESPONSE);
        assertResponse(exectedResponse,response);
    }

    @Test
    public void getGetRelatedInstanceGroupsByVnfId__yield404NotFound() {
        final PresetAAIGetRelatedInstanceGroupsByVnfId getRelatedInstanceGroupsByVnfId = new PresetAAIGetRelatedInstanceGroupsByVnfId("abcd");
        SimulatorApi.registerExpectationFromPreset(getRelatedInstanceGroupsByVnfId, CLEAR_THEN_SET);
        try {
            restTemplate.getForObject(uri + "/aai_get_instance_groups_by_vnf_instance_id/" + "dcba", String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }

    }

    @Test
    public void portMirroringConfigData_twoResponsesOneValidAndOneInvalid_yieldBothErrorAndOk() {
        final PresetAAICloudRegionAndSourceFromConfigurationPut presetAAIResponseWitnProblem = new PresetAAICloudRegionAndSourceFromConfigurationPut("ANOTHER-RANDOM-UUID", "");
        SimulatorApi.registerExpectationFromPreset(presetAAIResponseWitnProblem, CLEAR_THEN_SET);
        SimulatorApi.registerExpectationFromPreset(new PresetAAICloudRegionAndSourceFromConfigurationPut("SOME-RANDOM-UUID", "THE-EXPECTED-REGION-ID"), APPEND);

        final String response = restTemplate.getForObject(uri + "/aai_getPortMirroringConfigsData?configurationIds=" + "SOME-RANDOM-UUID,ANOTHER-RANDOM-UUID", String.class);

        final ImmutableMap<String, ImmutableMap<String, Object>> expected = ImmutableMap.of(
                "SOME-RANDOM-UUID", ImmutableMap.of(
                        "cloudRegionId", "THE-EXPECTED-REGION-ID"
                ),
                "ANOTHER-RANDOM-UUID", ImmutableMap.of(
                        "errorDescription", "Node 'properties.cloud-region-id' of node-type 'cloud-region' is blank",
                        "rawAaiResponse", presetAAIResponseWitnProblem.getResponseBody().toString().replace(" ", "")
                ));

        assertResponse(expected, response);
    }

    @Test
    @FeatureTogglingTest(Features.FLAG_PRESENT_PROVIDER_NETWORKS_ASSOCIATIONS)
    public void networksToVlans_simpleRequest_responseIsCorrect() {
        // Prepare randomized values
        // Some of these random values are persisted to match with the
        // cypress preset "aaiGetNetworksToVlansByServiceInstance.json".
        String globalCustomerId = UUID.randomUUID().toString();
        String sdcModelUuid = "5a3ad576-c01d-4bed-8194-0e72b4a3d020";
        String serviceType = "vMOG";
        int vlanIdOuter = 34612;
        int vlanIdOuter2 = 8568012;
        int vlanIdOuter3 = 4;

        // build hierarchical presets:
        //
        //     service instance
        //               ║
        //               ╠═  network 1
        //               ║      ╠═  vlan 1
        //               ║      ╚═  vlan 2
        //               ║
        //               ╠═  network 2
        //               ║      ╚═  vlan 3
        //               ║
        PresetAAIStandardQueryGet vlanTagPreset1 = ofVlanTag(vlanIdOuter);
        PresetAAIStandardQueryGet vlanTagPreset2 = ofVlanTag(vlanIdOuter2);
        PresetAAIStandardQueryGet vlanTagPreset3 = ofVlanTag(vlanIdOuter3);

        PresetAAIStandardQueryGet l3NetworkPreset1 = ofL3Network("7989a6d2-ba10-4a5d-8f15-4520bc833090", "DDDEEEFFF", "Provider Network",
                ImmutableMultimap.of("vlan-tag", vlanTagPreset1.getReqPath(), "vlan-tag", vlanTagPreset2.getReqPath()), "Failed");
        PresetAAIStandardQueryGet l3NetworkPreset2 = ofL3Network("e8e2332e-1f84-4237-bc97-3b5b842f52e4","GGGHHHIII", "Network",
                ImmutableMultimap.of("vlan-tag", vlanTagPreset3.getReqPath()), "Assigned");

        PresetAAIStandardQueryGet serviceInstance = ofServiceInstance("9cdd1b2a-43a7-47bc-a88e-759ba2399f0b",
                "7a6ee536-f052-46fa-aa7e-2fca9d674c44", "6e59c5de-f052-46fa-aa7e-2fca9d674c44", globalCustomerId, serviceType,
                ImmutableMultimap.of("l3-network", l3NetworkPreset1.getReqPath(), "l3-network", l3NetworkPreset2.getReqPath()));

        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                serviceInstance, l3NetworkPreset1, l3NetworkPreset2, vlanTagPreset1, vlanTagPreset2, vlanTagPreset3,
                new PresetSDCGetServiceMetadataGet(sdcModelUuid, UUID.randomUUID().toString(), "service-vl-with-5g-network-provider-alacarte.zip"),
                new PresetSDCGetServiceToscaModelGet(sdcModelUuid, "service-vl-with-5g-network-provider-alacarte.zip")
        ), CLEAR_THEN_SET);

        // THE TEST
        final String response = restTemplate.getForObject(uri + "/aai/standardQuery/vlansByNetworks"
                        + "?serviceInstanceId=" + serviceInstance.getInstanceId()
                        + "&serviceType=" + serviceType
                        + "&globalCustomerId=" + globalCustomerId
                        + "&sdcModelUuid=" + sdcModelUuid
                , String.class);

        assertResponse(JsonAssert.when(Option.IGNORING_ARRAY_ORDER),
                getResourceAsString("serviceWithNetwork/aaiGetNetworksToVlansByServiceInstance.json"),
                response);
    }

    @Test
    @FeatureTogglingTest(Features.FLAG_PRESENT_PROVIDER_NETWORKS_ASSOCIATIONS)
    public void networksWithVlansToVnf_simpleRequest_responseIsCorrect() {
        // Prepare randomized values
        // Some of these random values are persisted to match with the
        // cypress preset "aaiGetNetworksWithVlansToVnfByServiceInstance.json".
        String globalCustomerId = UUID.randomUUID().toString();
        String sdcModelUuid = "5a3ad576-c01d-4bed-8194-0e72b4a3d020";
        String serviceType = "vMOG";
        int vlanIdOuter = 34123;
        int vlanIdOuter2 = 65540;
        int vlanIdOuter3 = 12345;
        int vlanIdOuter4 = 67890;
        int vlanIdOuter5 = 417695;
        int vlanIdOuter6 = 783243;

        // build hierarchical presets:
        //
        //     service instance
        //               ║
        //               ╠═  vnf 1
        //               ║    ╚═  network 1
        //               ║          ╠═  vlan 1
        //               ║          ╚═  vlan 2
        //               ║    ╚═  network 2
        //               ║          ╠═  vlan 3
        //               ║          ╚═  vlan 4
        //               ║
        //               ╠═  vnf 2
        //               ║    ╚═  network 3
        //               ║          ╠═  vlan 5
        //               ║          ╚═  vlan 6
        //               ║
        PresetAAIStandardQueryGet vlanTagPreset1 = ofVlanTag(vlanIdOuter);
        PresetAAIStandardQueryGet vlanTagPreset2 = ofVlanTag(vlanIdOuter2);
        PresetAAIStandardQueryGet vlanTagPreset3 = ofVlanTag(vlanIdOuter3);
        PresetAAIStandardQueryGet vlanTagPreset4 = ofVlanTag(vlanIdOuter4);
        PresetAAIStandardQueryGet vlanTagPreset5 = ofVlanTag(vlanIdOuter5);
        PresetAAIStandardQueryGet vlanTagPreset6 = ofVlanTag(vlanIdOuter6);

        PresetAAIStandardQueryGet l3NetworkPreset1 = ofL3Network("36517f3d-2bc2-48f5-aaf8-418520c54330","AAAAABBBBCCCC", "Provider Network",
                ImmutableMultimap.of("vlan-tag", vlanTagPreset1.getReqPath(), "vlan-tag", vlanTagPreset2.getReqPath()), "Assigned");

        PresetAAIStandardQueryGet l3NetworkPreset2 = ofL3Network("12347f3d-2bc2-48f5-aaf8-418520c54330","DDDEEEE", "Provider Network",
                ImmutableMultimap.of("vlan-tag", vlanTagPreset3.getReqPath(), "vlan-tag", vlanTagPreset4.getReqPath()), "Created");

        PresetAAIStandardQueryGet vnfPreset1 = ofVnf("c015cc0f-0f37-4488-aabf-53795fd93cd3",
                ImmutableMultimap.of("l3-network", l3NetworkPreset1.getReqPath() , "l3-network", l3NetworkPreset2.getReqPath()),
                defaultPlacement());

        PresetAAIStandardQueryGet l3NetworkPreset3 = ofL3Network("12aa7f3d-2bc2-48f5-aaf8-418520c54330","XXXYYYZZZ", "Network",
                ImmutableMultimap.of("vlan-tag", vlanTagPreset5.getReqPath(), "vlan-tag", vlanTagPreset6.getReqPath()), "Created");

        PresetAAIStandardQueryGet vnfPreset2 = ofVnf("c55da606-cf38-42c7-bc3c-be8e23b19299", ImmutableMultimap.of("l3-network", l3NetworkPreset3.getReqPath()),
                defaultPlacement());

        PresetAAIStandardQueryGet serviceInstance = ofServiceInstance("9cdd1b2a-43a7-47bc-a88e-759ba2399f0b",
                "7a6ee536-f052-46fa-aa7e-2fca9d674c44", "6e59c5de-f052-46fa-aa7e-2fca9d674c44", globalCustomerId, serviceType,
                ImmutableMultimap.of("generic-vnf", vnfPreset1.getReqPath(), "generic-vnf", vnfPreset2.getReqPath()));

        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                serviceInstance, vnfPreset1, vnfPreset2, l3NetworkPreset1, l3NetworkPreset2, l3NetworkPreset3, vlanTagPreset1, vlanTagPreset2, vlanTagPreset3, vlanTagPreset4, vlanTagPreset5, vlanTagPreset6,
                new PresetSDCGetServiceMetadataGet(sdcModelUuid, UUID.randomUUID().toString(), "service-vl-with-5g-network-provider-alacarte.zip"),
                new PresetSDCGetServiceToscaModelGet(sdcModelUuid, "service-vl-with-5g-network-provider-alacarte.zip")
        ), CLEAR_THEN_SET);

        // THE TEST
        final String response = restTemplate.getForObject(uri + "/aai/standardQuery/vlansByNetworks"
                        + "?serviceInstanceId=" + serviceInstance.getInstanceId()
                        + "&serviceType=" + serviceType
                        + "&globalCustomerId=" + globalCustomerId
                        + "&sdcModelUuid=" + sdcModelUuid
                , String.class);

        assertResponse(JsonAssert.when(Option.IGNORING_ARRAY_ORDER),
                getResourceAsString("serviceWithNetwork/aaiGetNetworksWithVlansToVnfByServiceInstance.json"),
                response);
    }

    @FeatureTogglingTest(value = Features.FLAG_FLASH_REDUCED_RESPONSE_CHANGEMG, flagActive = false)
    @Test
    public void getVnfDataByGlobalIdAndServiceType() {

        SimulatorApi.registerExpectationFromPreset(new PresetBaseAAICustomQuery(
            SIMPLE,
            "business/customers/customer/a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb/service-subscriptions/service-subscription/vRichardson/service-instances",
            "query/vnf-topology-fromServiceInstance"
        ) {
            @Override
            public Object getResponseBody() {
                return getResourceAsString(
                    "registration_to_simulator/changeManagement/get_vnf_data_by_globalid_and_service_type_response.json");
            }
        }, CLEAR_THEN_SET);

        String url = uri + "/get_vnf_data_by_globalid_and_service_type/a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb/vRichardson";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertResponse(JsonAssert.when(Option.IGNORING_ARRAY_ORDER),
                getResourceAsString("registration_to_simulator/changeManagement/get_vnf_data_by_globalid_and_service_type_response.json"),
                response.getBody());
    }

    @Test
    public void whenCallAaiThroughAAIRestInterface_thenRequestRecordedInMetricsLog() {
        registerExpectationFromPresets(ImmutableList.of(
            new PresetAAIGetVpnsByType(),
            new PresetAAIGetSubscribersGet()
        ),CLEAR_THEN_SET);
        String internalPath = "/aai_get_vpn_list";
        String url = uri + internalPath;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        final String requestId = response.getHeaders().getFirst("X-ECOMP-RequestID-echo");
        LoggerFormatTest.assertHeadersAndMetricLogs(restTemplate, uri, requestId,"/network/vpn-bindings" , 1);
        LoggerFormatTest.verifyExistenceOfIncomingReqsInAuditLogs(restTemplate, uri, requestId, internalPath);
    }

    @Test
    public void getVpnList() {
        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetVpnsByType(), CLEAR_THEN_SET);
        String url = uri + "/aai_get_vpn_list";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String expected = getResourceAsString("viewEdit/aaiGetVpnList.json");
        assertThat(response.getBody(), jsonEquals(expected).when(Option.IGNORING_ARRAY_ORDER).when(Option.IGNORING_EXTRA_FIELDS));
    }

    @DataProvider
    public static Object[][] getActiveNetworkAaiStates() {
        return new Object[][] {
                {1, "viewEdit/aaiGetActiveNetworks.json"},
                {2, "viewEdit/aaiGetActiveNetworks2.json"}
        };
    }

    @Test(dataProvider = "getActiveNetworkAaiStates")
    public void getActiveNetworks_givenSpecificAAIState_cypressPresetMatch(int state, String expectedResultFileName) {
        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
            new PresetAAIGetL3NetworksByCloudRegionSpecificState(state),
            PresetAAIGetCloudOwnersByCloudRegionId.PRESET_AUK51A_TO_ATT_NC
        ), CLEAR_THEN_SET);
        String url = uri + AAI_GET_ACTIVE_NETWORKS
            + "?cloudRegion=" + AAIBaseGetL3NetworksByCloudRegionPreset.DEFAULT_CLOUD_REGION_ID
            + "&tenantId=" + AAIBaseGetL3NetworksByCloudRegionPreset.DEFAULT_TENANT_ID;
        String response = restTemplate.getForObject(url, String.class);
        System.out.println("response = " + response);
        String expected = getResourceAsString(expectedResultFileName);
        assertThat(response, jsonEquals(expected));
    }

    @Test
    public void getActiveNetworks() throws JsonProcessingException {
        String networkRole = "Some role";
        PresetAAIGetL3NetworksByCloudRegion presetAAIGetL3NetworksByCloudRegion = new PresetAAIGetL3NetworksByCloudRegion(networkRole);
        List<BasePreset> presets = ImmutableList.of(
                presetAAIGetL3NetworksByCloudRegion,
                PresetAAIGetCloudOwnersByCloudRegionId.PRESET_AUK51A_TO_ATT_NC
        );
        SimulatorApi.registerExpectationFromPresets(presets, CLEAR_THEN_SET);
        String url = uri + AAI_GET_ACTIVE_NETWORKS +
                "?cloudRegion=" + presetAAIGetL3NetworksByCloudRegion.getCloudRegionId() +
                "&tenantId=" + presetAAIGetL3NetworksByCloudRegion.getTenantId() +
                 "&networkRole=" + networkRole;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertResponse(JsonAssert.when(Option.IGNORING_EXTRA_FIELDS, Option.IGNORING_ARRAY_ORDER),
                presetAAIGetL3NetworksByCloudRegion.getActiveNetworksWithNameAndRelatedToVpnBindingAsJsonString(),
                response.getBody());
    }

    @Test
    public void getNewestModelVersionByInvariant() throws JsonProcessingException {
        String invariantId = "f6342be5-d66b-4d03-a1aa-c82c3094c4ea";

        SimulatorApi.registerExpectationFromPreset(new PresetAAIModelVersionsByInvariantId(), CLEAR_THEN_SET );

        String url = uri +
                "/aai_get_newest_model_version_by_invariant/" + invariantId;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertTrue(response.toString().contains("the-newest-version"));
    }

    @Test
    public void getNewestModelVersionByInvariant_modelNotExist_thenEmptyResponse() {
        String invariantId = "f6342be5-d66b-4d03-a1aa-c82c3094c4ea";

        SimulatorApi.registerExpectationFromPreset(new PresetAAIModelVersionsByInvariantId(), CLEAR_THEN_SET );

        String url = uri +
                "/aai_get_newest_model_version_by_invariant/" + "model-not-exist";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        AssertJUnit.assertNull(response.getBody());
    }

    @Test
    @FeatureTogglingTest(Features.FLAG_FLASH_REDUCED_RESPONSE_CHANGEMG)
    public void getVnfsWithCustomQueryNewReducedResponse() throws URISyntaxException {

        String globalCustomerId = "globalCustomerId1-360-as988q";
        String serviceType = "TEST1-360";
        String nfRole = "test360";
        SimulatorApi.registerExpectationFromPreset(new PresetBaseAAICustomQuery(
            SIMPLE,
            "/business/customers/customer/" + globalCustomerId + "/service-subscriptions/service-subscription/"
                + serviceType + "/service-instances",
            "query/vnfs-fromServiceInstance-filter?nfRole=" + nfRole
            ) {
            @Override
            public Object getResponseBody() {
                return getResourceAsString(
                    AAI_VNFS_FOR_CHANGE_MANAGEMENT_JSON_BY_PARAMS);
            }
        }, CLEAR_THEN_SET);
        URIBuilder urlBuilder  = new URIBuilder(uri + "/get_vnf_data_by_globalid_and_service_type/" + globalCustomerId + "/" + serviceType);
        urlBuilder.addParameter("nfRole", nfRole);
        ResponseEntity<String> response = restTemplate.getForEntity(urlBuilder.build().toString(), String.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertThat(response.getBody(), jsonEquals(getResourceAsString(AAI_VNFS_FOR_CHANGE_MANAGEMENT_JSON_BY_PARAMS)));
    }

    @Test
    public void searchServiceInstancesBySubscriber_serviceInstanceOfAnotherSubscriber_authIsFollowingFeatureToggle() {
        String craigRobertsSubscriberId = "31739f3e-526b-11e6-beb8-9e71128cae77";
        String aServiceOwningEntityId = "f160c875-ddd1-4ef5-84d8-d098784daa3a";
        String currentUserAuthorizedOwningEntityId = "d61e6f2d-12fa-4cc2-91df-7c244011d6fc";

        SimulatorApi.registerExpectation(GET_SUBSCRIBERS_FOR_CUSTOMER_CRAIG_ROBERTS,
            ImmutableMap.of(aServiceOwningEntityId, currentUserAuthorizedOwningEntityId), CLEAR_THEN_SET);

        searchServicesAndAssertIsPermitted("subscriberId=" + craigRobertsSubscriberId, "4ea864f2-b946-473a-b51c-51a7c10b8391");
    }

    @Test
    public void searchServiceInstancesByOwningEntity_serviceInstanceOfAnotherSubscriber_authIsFollowingFeatureToggle() {
        String owningEntityName = "someOwning";
        String owningEntityId = "SILVIA ROBBINS"; // this will need to change with translateOwningEntityNameToOwningEntityId

        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetModelsByOwningEntity(owningEntityName, owningEntityId, "fakeSubscriberId"), CLEAR_THEN_SET);

        searchServicesAndAssertIsPermitted("owningEntity=" + owningEntityName, "af9d52f9-13b2-4657-a198-463677f82dc0");
    }

    private void searchServicesAndAssertIsPermitted(String queryParams, String aServiceInstanceId) {
        boolean expectedPermission = Features.FLAG_2006_USER_PERMISSIONS_BY_OWNING_ENTITY.isActive();

        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetSubscribersGet(), APPEND);

        JsonNode serviceInstancesResult = restTemplate
            .getForObject(uri + "/search_service_instances?" + queryParams, JsonNode.class);

        assertThat(serviceInstancesResult.path("service-instances").isArray(), is(true));

        ArrayNode servicesArray = ((ArrayNode) serviceInstancesResult.path("service-instances"));

        JsonNode aServiceResult = Streams.fromIterator(servicesArray.iterator())
            .filter(it -> it.path("serviceInstanceId").asText().equals(aServiceInstanceId))
            .findAny()
            .orElseThrow(() -> new AssertionError("could not find serviceInstanceId=" + aServiceInstanceId));

        assertThat(aServiceResult.toString(),
            aServiceResult.path("isPermitted").booleanValue(), is(expectedPermission));
    }

    private void assertResponse(Object expected, String response) {
        assertResponse(Configuration.empty(), expected, response);
    }

    private void assertResponse(Configuration configuration, Object expected, String response) {
        try {
            JsonAssert.assertJsonEquals(expected, response, configuration);
        } catch (Exception | AssertionError e) {
            System.err.println("response was: " + response);
            throw e;
        }
    }

    @Test
    public void getServiceInstanceBySubscriberAndServiceIdentifierAndSubscriberName_dsl_givenValidAaiResponse() {
        SimulatorApi.clearAll();
        String globalCustomerId = "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb";
        String serviceIdentifier = "5d942bc7-3acf-4e35-836a-393619ebde66";
        String serviceIdentifierType = "Service Instance Id";
        String serviceType = "TYLER SILVIA";
        SimulatorApi.registerExpectationFromPreset(new PresetAAIServiceInstanceDSLPut(
            globalCustomerId,serviceIdentifier,serviceIdentifierType), CLEAR_THEN_SET);
        SimulatorApi.registerExpectationFromPreset(new PresetAAIFilterServiceInstanceById(
            globalCustomerId,serviceType,serviceIdentifier), APPEND);
        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetServiceInstanceBySubscriberIdAndServiceTypeAndSIID(
            globalCustomerId,serviceType,serviceIdentifier), APPEND);
        final String response = restTemplate.getForObject(
            uri + "/aai_get_service_instance_by_id_and_type"+ "/" + globalCustomerId + "/" + serviceIdentifier + "/" + serviceIdentifierType + "/Mobility" ,
            String.class);
        assertResponse(GET_SERVICE_INSTANCE_BY_SUBSCRIBER_DSL_EXPECTED_RESPONSE, response);
    }

    @Test
    public void getServiceInstanceBySubscriberAndServiceIdentifierWithoutSubscriberName_dsl_givenValidAaiResponse() {
        SimulatorApi.clearAll();
        String globalCustomerId = "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb";
        String serviceIdentifier = "5d942bc7-3acf-4e35-836a-393619ebde66";
        String serviceIdentifierType = "Service Instance Id";
        String serviceType = "TYLER SILVIA";
        SimulatorApi.registerExpectationFromPreset(new PresetAAIServiceInstanceDSLPut(
            globalCustomerId,serviceIdentifier,serviceIdentifierType), CLEAR_THEN_SET);
        SimulatorApi.registerExpectationFromPreset(new PresetAAIFilterServiceInstanceById(
            globalCustomerId,serviceType,serviceIdentifier), APPEND);
        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetServiceInstanceBySubscriberIdAndServiceTypeAndSIID(
            globalCustomerId,serviceType,serviceIdentifier), APPEND);
        final String response = restTemplate.getForObject(
            uri + "/aai_get_service_instance_by_id_and_type"+ "/" + globalCustomerId + "/" + serviceIdentifier + "/" + serviceIdentifierType ,
            String.class);
        assertResponse(GET_SERVICE_INSTANCE_BY_SUBSCRIBER_DSL_EXPECTED_RESPONSE, response);
    }

    @Test
    public void getServiceInstanceBySubscriberAndServiceIdentifierWithSubscriberName_dsl_givenValidAaiResponse() {
        SimulatorApi.clearAll();
        String globalCustomerId = "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb";
        String serviceIdentifier = "5d942bc7-3acf-4e35-836a-393619ebde66";
        String serviceIdentifierType = "Service Instance Id";
        String serviceType = "TYLER SILVIA";
        String subscriberName = "Mobility";
        SimulatorApi.registerExpectationFromPreset(new PresetAAIServiceInstanceDSLPut(
            globalCustomerId,serviceIdentifier,serviceIdentifierType), CLEAR_THEN_SET);
        SimulatorApi.registerExpectationFromPreset(new PresetAAIFilterServiceInstanceById(
            globalCustomerId,serviceType,serviceIdentifier), APPEND);
        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetServiceInstanceBySubscriberIdAndServiceTypeAndSIID(
            globalCustomerId,serviceType,serviceIdentifier), APPEND);
        final String response = restTemplate.getForObject(
            uri + "/aai_get_service_instance_by_id_and_type"+ "/" + globalCustomerId + "/" + serviceIdentifier + "/" + serviceIdentifierType + "/" + subscriberName ,
            String.class);
        assertResponse(GET_SERVICE_INSTANCE_BY_SUBSCRIBER_DSL_EXPECTED_RESPONSE, response);
    }

    @Test
    public void searchServiceInstanceByIdentifierType_id_dsl_givenValidAaiResponse() {
        SimulatorApi.clearAll();
        String globalCustomerId = "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb";
        String serviceInstanceIdentifier = "5d942bc7-3acf-4e35-836a-393619ebde66";
        String serviceInstanceIdentifierType = "Service Instance Id";

        SimulatorApi.registerExpectationFromPreset(new PresetAAIServiceInstanceDSLPut(
            globalCustomerId,serviceInstanceIdentifier,serviceInstanceIdentifierType), CLEAR_THEN_SET);
        String path = "/search_service_instances?subscriberId="+globalCustomerId+
            "&serviceInstanceIdentifier="+serviceInstanceIdentifier+
            "&serviceInstanceIdentifierType="+serviceInstanceIdentifierType+"";
        final String response = restTemplate.getForObject( uri + path, String.class);
        assertResponse(GET_SEARCH_SERVICE_INSTANCE_BY_SUBSCRIBER_DSL_EXPECTED_RESPONSE, response);

    }

    @Test
    public void searchServiceInstanceByIdentifierType_name_dsl_givenValidAaiResponse() {
        SimulatorApi.clearAll();
        String globalCustomerId = "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb";
        String serviceInstanceIdentifierType = "Service Instance Name";
        String serviceInstanceIdentifier = "dpa2actsf5001v_Port_Mirroring_dpa2a_SVC";

        SimulatorApi.registerExpectationFromPreset(new PresetAAIServiceInstanceDSLPut(
            globalCustomerId,serviceInstanceIdentifier,serviceInstanceIdentifierType), CLEAR_THEN_SET);
        String path = "/search_service_instances?subscriberId="+globalCustomerId+
            "&serviceInstanceIdentifier="+serviceInstanceIdentifier+
            "&serviceInstanceIdentifierType="+serviceInstanceIdentifierType+"";
        final String response = restTemplate.getForObject( uri + path, String.class);
        assertResponse(GET_SEARCH_SERVICE_INSTANCE_BY_SUBSCRIBER_DSL_EXPECTED_RESPONSE, response);
    }

}
