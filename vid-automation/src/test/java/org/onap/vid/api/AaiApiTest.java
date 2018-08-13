package org.onap.vid.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.javacrumbs.jsonunit.JsonAssert;
import org.apache.commons.text.StringEscapeUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIBadBodyForGetServicesGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAICloudRegionAndSourceFromConfigurationPut;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetInstanceGroupsByCloudRegion;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetInstanceGroupsByCloudRegionInvalidRequest;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetInstanceGroupsByCloudRegionRequiredMissing;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetNetworkCollectionDetails;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetNetworkCollectionDetailsInvalidRequest;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetNetworkCollectionDetailsRequiredMissing;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetPortMirroringSourcePorts;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetPortMirroringSourcePortsError;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetRelatedInstanceGroupsByVnfId;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.vid.model.aai.AaiResponse;
import org.onap.vid.model.mso.OperationalEnvironmentList;
import org.onap.vid.more.LoggerFormatTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.services.SimulatorApi;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static org.onap.simulator.presetGenerator.presets.ecompportal_att.EcompPortalPresetsUtils.getEcompPortalPresets;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.APPEND;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET;

public class AaiApiTest extends BaseApiAaiTest {


    public static final String GET_OPERATIONAL_ENVIRONMENTS_JSON = "get_operational_environments_aai.json";
    public static final String GET_OPERATIONAL_ENVIRONMENTS_JSON_ERROR = "get_operational_environments_aai_error.json";
    public static final String[] AAI_GET_SERVICES_ERROR_SIMULATOR_RESPONSES = {"getServicesAaiErrorResp.json", "aai_get_full_subscribers.json"};
    public static final String[] AAI_GET_SERVICES_FINE_SIMULATOR_RESPONSES = {"getServicesAaiFineResp.json", "aai_get_full_subscribers.json"};

    public static final String OPERATIONAL_ENVIRONMENT_TYPE = "VNF";
    public static final String OPERATIONAL_ENVIRONMENT_STATUS = "Activate";
    public static final String BASE_GET_SERVICES_AAI_REQUEST_BODY = "{\"start\" : \"service-design-and-creation/models\", \"query\" : \"query/serviceModels-byDistributionStatus?distributionStatus=DISTRIBUTION_COMPLETE_OK\";}";
    public static final String GET_INSTANCE_GROUPS_BY_CLOUDREGION_EXPECTED_RESPONSE = "{\"results\":[{\"instance-group\":{\"id\":\"AAI-12002-test3-vm230w\",\"description\":\"a9DEa0kpY\",\"instance-group-role\":\"JZmha7QSS4tJ\",\"model-invariant-id\":\"model-id3\",\"model-version-id\":\"a0efd5fc-f7be-4502-936a-a6c6392b958f\",\"instance-group-type\":\"type\",\"resource-version\":\"1520888659539\",\"instance-group-name\":\"wKmBXiO1xm8bK\",\"instance-group-function\":\"testfunction2\",\"relationship-list\":{\"relationship\":[{\"relationDataList\":[{\"relationship-key\":\"cloud-region.cloud-owner\",\"relationship-value\":\"AAI-12002-vm230w\"},{\"relationship-key\":\"cloud-region.cloud-region-id\",\"relationship-value\":\"AAI-region-vm230w\"}],\"relatedToPropertyList\":[{\"property-key\":\"cloud-region.owner-defined-type\",\"property-value\":null}],\"related-to\":\"cloud-region\",\"related-link\":\"/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/AAI-12002-vm230w/AAI-region-vm230w\",\"relationship-label\":\"org.onap.relationships.inventory.Uses\",\"relationship-data\":[{\"relationship-key\":\"cloud-region.cloud-owner\",\"relationship-value\":\"AAI-12002-vm230w\"},{\"relationship-key\":\"cloud-region.cloud-region-id\",\"relationship-value\":\"AAI-region-vm230w\"}],\"related-to-property\":[{\"property-key\":\"cloud-region.owner-defined-type\",\"property-value\":null}]}]}}},{\"instance-group\":{\"id\":\"AAI-12002-test1-vm230w\",\"description\":\"a9DEa0kpY\",\"instance-group-role\":\"JZmha7QSS4tJ\",\"model-invariant-id\":\"model-id1\",\"model-version-id\":\"a0efd5fc-f7be-4502-936a-a6c6392b958f\",\"instance-group-type\":\"type\",\"resource-version\":\"1520886467989\",\"instance-group-name\":\"wKmBXiO1xm8bK\",\"instance-group-function\":\"testfunction2\",\"relationship-list\":{\"relationship\":[{\"relationDataList\":[{\"relationship-key\":\"cloud-region.cloud-owner\",\"relationship-value\":\"AAI-12002-vm230w\"},{\"relationship-key\":\"cloud-region.cloud-region-id\",\"relationship-value\":\"AAI-region-vm230w\"}],\"relatedToPropertyList\":[{\"property-key\":\"cloud-region.owner-defined-type\",\"property-value\":null}],\"related-to\":\"cloud-region\",\"related-link\":\"/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/AAI-12002-vm230w/AAI-region-vm230w\",\"relationship-label\":\"org.onap.relationships.inventory.Uses\",\"relationship-data\":[{\"relationship-key\":\"cloud-region.cloud-owner\",\"relationship-value\":\"AAI-12002-vm230w\"},{\"relationship-key\":\"cloud-region.cloud-region-id\",\"relationship-value\":\"AAI-region-vm230w\"}],\"related-to-property\":[{\"property-key\":\"cloud-region.owner-defined-type\",\"property-value\":null}]}]}}},{\"instance-group\":{\"id\":\"AAI-12002-test2-vm230w\",\"description\":\"a9DEa0kpY\",\"instance-group-role\":\"JZmha7QSS4tJ\",\"model-invariant-id\":\"model-id2\",\"model-version-id\":\"version2\",\"instance-group-type\":\"type\",\"resource-version\":\"1520888629970\",\"instance-group-name\":\"wKmBXiO1xm8bK\",\"instance-group-function\":\"testfunction2\",\"relationship-list\":{\"relationship\":[{\"relationDataList\":[{\"relationship-key\":\"cloud-region.cloud-owner\",\"relationship-value\":\"AAI-12002-vm230w\"},{\"relationship-key\":\"cloud-region.cloud-region-id\",\"relationship-value\":\"AAI-region-vm230w\"}],\"relatedToPropertyList\":[{\"property-key\":\"cloud-region.owner-defined-type\",\"property-value\":null}],\"related-to\":\"cloud-region\",\"related-link\":\"/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/AAI-12002-vm230w/AAI-region-vm230w\",\"relationship-label\":\"org.onap.relationships.inventory.Uses\",\"relationship-data\":[{\"relationship-key\":\"cloud-region.cloud-owner\",\"relationship-value\":\"AAI-12002-vm230w\"},{\"relationship-key\":\"cloud-region.cloud-region-id\",\"relationship-value\":\"AAI-region-vm230w\"}],\"related-to-property\":[{\"property-key\":\"cloud-region.owner-defined-type\",\"property-value\":null}]}]}}}]}\n";
    public static final String GET_NETWORK_COLLECTION_EXPECTED_RESPONSE = "{\"results\":{\"collection\":{\"collection-id\":\"collection-1-2018-rs804s\",\"model-invariant-id\":\"5761e0a7-defj777\",\"model-version-id\":\"5761e0a7-defj232\",\"collection-name\":\"collection-name\",\"collection-type\":\"L3-NETWORK\",\"collection-role\":\"SUB-INTERFACE\",\"collection-function\":\"collection-function\",\"collection-customization-id\":\"custom-unique-data-id\",\"relationship-list\":{\"relationship\":[{\"relationDataList\":[{\"relationship-key\":\"customer.global-customer-id\",\"relationship-value\":\"customer-1-2017-rs804s\"},{\"relationship-key\":\"service-subscription.service-type\",\"relationship-value\":\"service-value7-rs804s\"},{\"relationship-key\":\"service-instance.service-instance-id\",\"relationship-value\":\"2UJZZ01777-rs804s\"}],\"relatedToPropertyList\":[{\"property-key\":\"service-instance.service-instance-name\",\"property-value\":null}],\"related-to\":\"service-instance\",\"related-link\":\"/aai/v13/business/customers/customer/customer-1-2017-rs804s/service-subscriptions/service-subscription/service-value7-rs804s/service-instances/service-instance/2UJZZ01777-rs804s\",\"relationship-label\":\"org.onap.relationships.inventory.MemberOf\",\"relationship-data\":[{\"relationship-key\":\"customer.global-customer-id\",\"relationship-value\":\"customer-1-2017-rs804s\"},{\"relationship-key\":\"service-subscription.service-type\",\"relationship-value\":\"service-value7-rs804s\"},{\"relationship-key\":\"service-instance.service-instance-id\",\"relationship-value\":\"2UJZZ01777-rs804s\"}],\"related-to-property\":[{\"property-key\":\"service-instance.service-instance-name\",\"property-value\":null}]},{\"relationDataList\":[{\"relationship-key\":\"instance-group.id\",\"relationship-value\":\"instanceGroup-2018-rs804s\"}],\"relatedToPropertyList\":[{\"property-key\":\"instance-group.description\",\"property-value\":\"zr6h\"},{\"property-key\":\"instance-group.instance-group-name\",\"property-value\":\"wKmBXiO1xm8bK\"}],\"related-to\":\"instance-group\",\"related-link\":\"/aai/v13/network/instance-groups/instance-group/instanceGroup-2018-rs804s\",\"relationship-label\":\"org.onap.relationships.inventory.MemberOf\",\"relationship-data\":[{\"relationship-key\":\"instance-group.id\",\"relationship-value\":\"instanceGroup-2018-rs804s\"}],\"related-to-property\":[{\"property-key\":\"instance-group.description\",\"property-value\":\"zr6h\"},{\"property-key\":\"instance-group.instance-group-name\",\"property-value\":\"wKmBXiO1xm8bK\"}]}]},\"resource-version\":\"1521662811309\"},\"networks\":[{\"network-id\":\"l3network-id-rs804s\",\"network-name\":\"oam-net\",\"network-type\":\"Tenant_Layer_3\",\"network-role\":\"HngwProtectedOam.OAM\",\"network-technology\":\"Contrail\",\"is-bound-to-vpn\":false,\"resource-version\":\"1521662814627\",\"is-provider-network\":false,\"is-shared-network\":false,\"is-external-network\":false,\"relationship-list\":{\"relationship\":[{\"relationDataList\":[{\"relationship-key\":\"instance-group.id\",\"relationship-value\":\"instanceGroup-2018-rs804s\"}],\"relatedToPropertyList\":[{\"property-key\":\"instance-group.description\",\"property-value\":\"zr6h\"},{\"property-key\":\"instance-group.instance-group-name\",\"property-value\":\"wKmBXiO1xm8bK\"}],\"related-to\":\"instance-group\",\"related-link\":\"/aai/v13/network/instance-groups/instance-group/instanceGroup-2018-rs804s\",\"relationship-label\":\"org.onap.relationships.inventory.MemberOf\",\"relationship-data\":[{\"relationship-key\":\"instance-group.id\",\"relationship-value\":\"instanceGroup-2018-rs804s\"}],\"related-to-property\":[{\"property-key\":\"instance-group.description\",\"property-value\":\"zr6h\"},{\"property-key\":\"instance-group.instance-group-name\",\"property-value\":\"wKmBXiO1xm8bK\"}]}]}},{\"network-id\":\"l3network-id-3-rs804s\",\"network-name\":\"oam-net\",\"network-type\":\"Tenant_Layer_3\",\"network-role\":\"HngwProtectedOam.OAM\",\"network-technology\":\"Contrail\",\"is-bound-to-vpn\":false,\"resource-version\":\"1521662816043\",\"is-provider-network\":false,\"is-shared-network\":false,\"is-external-network\":false,\"relationship-list\":{\"relationship\":[{\"relationDataList\":[{\"relationship-key\":\"instance-group.id\",\"relationship-value\":\"instanceGroup-2018-rs804s\"}],\"relatedToPropertyList\":[{\"property-key\":\"instance-group.description\",\"property-value\":\"zr6h\"},{\"property-key\":\"instance-group.instance-group-name\",\"property-value\":\"wKmBXiO1xm8bK\"}],\"related-to\":\"instance-group\",\"related-link\":\"/aai/v13/network/instance-groups/instance-group/instanceGroup-2018-rs804s\",\"relationship-label\":\"org.onap.relationships.inventory.MemberOf\",\"relationship-data\":[{\"relationship-key\":\"instance-group.id\",\"relationship-value\":\"instanceGroup-2018-rs804s\"}],\"related-to-property\":[{\"property-key\":\"instance-group.description\",\"property-value\":\"zr6h\"},{\"property-key\":\"instance-group.instance-group-name\",\"property-value\":\"wKmBXiO1xm8bK\"}]}]}},{\"network-id\":\"l3network-id-2-rs804s\",\"network-name\":\"oam-net\",\"network-type\":\"Tenant_Layer_3\",\"network-role\":\"HngwProtectedOam.OAM\",\"network-technology\":\"Contrail\",\"is-bound-to-vpn\":false,\"resource-version\":\"1521662815304\",\"is-provider-network\":false,\"is-shared-network\":false,\"is-external-network\":false,\"relationship-list\":{\"relationship\":[{\"relationDataList\":[{\"relationship-key\":\"instance-group.id\",\"relationship-value\":\"instanceGroup-2018-rs804s\"}],\"relatedToPropertyList\":[{\"property-key\":\"instance-group.description\",\"property-value\":\"zr6h\"},{\"property-key\":\"instance-group.instance-group-name\",\"property-value\":\"wKmBXiO1xm8bK\"}],\"related-to\":\"instance-group\",\"related-link\":\"/aai/v13/network/instance-groups/instance-group/instanceGroup-2018-rs804s\",\"relationship-label\":\"org.onap.relationships.inventory.MemberOf\",\"relationship-data\":[{\"relationship-key\":\"instance-group.id\",\"relationship-value\":\"instanceGroup-2018-rs804s\"}],\"related-to-property\":[{\"property-key\":\"instance-group.description\",\"property-value\":\"zr6h\"},{\"property-key\":\"instance-group.instance-group-name\",\"property-value\":\"wKmBXiO1xm8bK\"}]}]}}],\"service-instance\":{\"service-instance-id\":\"2UJZZ01777-rs804s\",\"resource-version\":\"1521662813382\",\"relationship-list\":{\"relationship\":[{\"relationDataList\":[{\"relationship-key\":\"collection.collection-id\",\"relationship-value\":\"collection-1-2018-rs804s\"}],\"relatedToPropertyList\":null,\"related-to\":\"collection\",\"related-link\":\"/aai/v13/network/collections/collection/collection-1-2018-rs804s\",\"relationship-label\":\"org.onap.relationships.inventory.MemberOf\",\"relationship-data\":[{\"relationship-key\":\"collection.collection-id\",\"relationship-value\":\"collection-1-2018-rs804s\"}],\"related-to-property\":null}]}},\"instance-group\":{\"id\":\"instanceGroup-2018-rs804s\",\"description\":\"zr6h\",\"instance-group-role\":\"JZmha7QSS4tJ\",\"model-invariant-id\":\"5761e0a7-defj777\",\"model-version-id\":\"5761e0a7-defj22\",\"instance-group-type\":\"7DDjOdNL\",\"resource-version\":\"1521662814023\",\"instance-group-name\":\"wKmBXiO1xm8bK\",\"instance-group-function\":\"testfunction2\",\"relationship-list\":{\"relationship\":[{\"relationDataList\":[{\"relationship-key\":\"l3-network.network-id\",\"relationship-value\":\"l3network-id-rs804s\"}],\"relatedToPropertyList\":[{\"property-key\":\"l3-network.network-name\",\"property-value\":\"oam-net\"}],\"related-to\":\"l3-network\",\"related-link\":\"/aai/v13/network/l3-networks/l3-network/l3network-id-rs804s\",\"relationship-label\":\"org.onap.relationships.inventory.MemberOf\",\"relationship-data\":[{\"relationship-key\":\"l3-network.network-id\",\"relationship-value\":\"l3network-id-rs804s\"}],\"related-to-property\":[{\"property-key\":\"l3-network.network-name\",\"property-value\":\"oam-net\"}]},{\"relationDataList\":[{\"relationship-key\":\"collection.collection-id\",\"relationship-value\":\"collection-1-2018-rs804s\"}],\"relatedToPropertyList\":null,\"related-to\":\"collection\",\"related-link\":\"/aai/v13/network/collections/collection/collection-1-2018-rs804s\",\"relationship-label\":\"org.onap.relationships.inventory.MemberOf\",\"relationship-data\":[{\"relationship-key\":\"collection.collection-id\",\"relationship-value\":\"collection-1-2018-rs804s\"}],\"related-to-property\":null},{\"relationDataList\":[{\"relationship-key\":\"l3-network.network-id\",\"relationship-value\":\"l3network-id-3-rs804s\"}],\"relatedToPropertyList\":[{\"property-key\":\"l3-network.network-name\",\"property-value\":\"oam-net\"}],\"related-to\":\"l3-network\",\"related-link\":\"/aai/v13/network/l3-networks/l3-network/l3network-id-3-rs804s\",\"relationship-label\":\"org.onap.relationships.inventory.MemberOf\",\"relationship-data\":[{\"relationship-key\":\"l3-network.network-id\",\"relationship-value\":\"l3network-id-3-rs804s\"}],\"related-to-property\":[{\"property-key\":\"l3-network.network-name\",\"property-value\":\"oam-net\"}]},{\"relationDataList\":[{\"relationship-key\":\"l3-network.network-id\",\"relationship-value\":\"l3network-id-2-rs804s\"}],\"relatedToPropertyList\":[{\"property-key\":\"l3-network.network-name\",\"property-value\":\"oam-net\"}],\"related-to\":\"l3-network\",\"related-link\":\"/aai/v13/network/l3-networks/l3-network/l3network-id-2-rs804s\",\"relationship-label\":\"org.onap.relationships.inventory.MemberOf\",\"relationship-data\":[{\"relationship-key\":\"l3-network.network-id\",\"relationship-value\":\"l3network-id-2-rs804s\"}],\"related-to-property\":[{\"property-key\":\"l3-network.network-name\",\"property-value\":\"oam-net\"}]}]}}}}\n";
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
            "    \"distributionStatus\": \"DISTRIBUTION_COMPLETE_OK\",\n" +
            "    \"artifacts\": null,\n" +
            "    \"resources\": null\n" +
            "  }],\n" +
            "  \"readOnly\": false\n" +
            "}";

    static final ObjectMapper om = new ObjectMapper();

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
        assertNotEquals(null, list.getOperationalEnvironment());
        assertEquals(2, list.getOperationalEnvironment().size());
        assertEquals(uuidOfOperationalEnvironment, list.getOperationalEnvironment().get(0).getOperationalEnvironmentId());
        assertEquals(1, list.getOperationalEnvironment().get(0).getRelationshipList().getRelationship().size());
    }


    @Test
    public void testVoid(){
        SimulatorApi.registerExpectation(SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET,
                "add_subinterface/aai_get_tenants.json"
                , "add_subinterface/aai_get_services.json"
        );
    }


    @Test(dataProvider = "errorCodes")
    public void getServicesWitErrorResponse(int errorCode) throws IOException, URISyntaxException {
        final String expectedResult = "{\"services\":[],\"readOnly\":false}";

        callAaiWithSimulatedErrorResponse(AAI_GET_SERVICES_ERROR_SIMULATOR_RESPONSES,
                ImmutableMap.of("500", Integer.toString(errorCode), "ERROR_PAYLOAD", StringEscapeUtils.escapeJson(expectedResult)),
                getAaiServicesUri(), "", 200, expectedResult, HttpMethod.GET);

    }

    @Test
    public void getServicesFineRequest() throws IOException, URISyntaxException {

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

        restTemplateErrorAgnostic.getForEntity(uri + "/aai_get_services",String.class);
        String logLines = LoggerFormatTest.getLogLines("error", 15, 0, restTemplate, uri);

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
        }catch (HttpClientErrorException e){
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
        }catch (HttpServerErrorException e){
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
        }
    }

    @Test
    public void getGetInstanceGroupsByCloudRegion_yieldValidResponse() {
        SimulatorApi.clearAll();
        final PresetAAIGetInstanceGroupsByCloudRegion presetAAIGetInstanceGroupsByCloudRegion = new PresetAAIGetInstanceGroupsByCloudRegion("CLOUD-OWNER", "CLOUD-REGION-ID", "NETWORK-FUNCTION");
        SimulatorApi.registerExpectationFromPreset(presetAAIGetInstanceGroupsByCloudRegion, CLEAR_THEN_SET);
        final String response = restTemplate.getForObject(uri + "/aai_get_instance_groups_by_cloudregion/" + "CLOUD-OWNER" + "/" + "CLOUD-REGION-ID" + "/" +"NETWORK-FUNCTION", String.class);

        assertResponse(GET_INSTANCE_GROUPS_BY_CLOUDREGION_EXPECTED_RESPONSE, response);
    }

    @Test
    public void getGetInstanceGroupsByCloudRegion_responseWithExtraFields_yieldValidResponse() {
        SimulatorApi.clearAll();
        final PresetAAIGetInstanceGroupsByCloudRegionInvalidRequest presetAAIGetInstanceGroupsByCloudRegion = new PresetAAIGetInstanceGroupsByCloudRegionInvalidRequest("CLOUD-OWNER", "CLOUD-REGION-ID", "NETWORK-FUNCTION");
        SimulatorApi.registerExpectationFromPreset(presetAAIGetInstanceGroupsByCloudRegion, CLEAR_THEN_SET);
        final String response = restTemplate.getForObject(uri + "/aai_get_instance_groups_by_cloudregion/" + "CLOUD-OWNER" + "/" + "CLOUD-REGION-ID" + "/" +"NETWORK-FUNCTION", String.class);

        assertResponse(GET_INSTANCE_GROUPS_BY_CLOUDREGION_EXPECTED_RESPONSE, response);
    }
    @Test
    public void getGetInstanceGroupsByCloudRegion_given404ErrorAaiResponse_yield200OkWithErrorMsg() {
        SimulatorApi.clearAll();
        try {
            restTemplate.getForObject(uri + "/aai_get_instance_groups_by_cloudregion/" + "CLOUD-OWNER" + "/" + "CLOUD-REGION-ID" + "/" +"NETWORK-FUNCTION", String.class);
        }catch (HttpClientErrorException e){
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void getGetInstanceGroupsByCloudRegion_responseWithRequiredMissing() {
        SimulatorApi.clearAll();
        final PresetAAIGetInstanceGroupsByCloudRegionRequiredMissing presetAAIGetInstanceGroupsByCloudRegion = new PresetAAIGetInstanceGroupsByCloudRegionRequiredMissing("CLOUD-OWNER", "CLOUD-REGION-ID", "NETWORK-FUNCTION");
        SimulatorApi.registerExpectationFromPreset(presetAAIGetInstanceGroupsByCloudRegion, CLEAR_THEN_SET);
        try {
            restTemplate.getForObject(uri + "/aai_get_instance_groups_by_cloudregion/" + "CLOUD-OWNER" + "/" + "CLOUD-REGION-ID" + "/" +"NETWORK-FUNCTION", String.class);
        }catch (HttpServerErrorException e){
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

        final String response = restTemplate.getForObject(uri + "/aai_get_instance_groups_by_vnf_instance_id/" + vnfId, String.class);

        assertResponse("[{\"type\":\"instance-group\",\"name\":\"instance group name\"},{\"type\":\"instance-group\",\"name\":\"instance group name\"}]", response);
    }

    @Test
    public void getGetRelatedInstanceGroupsByVnfId__yield404NotFound() {
        final PresetAAIGetRelatedInstanceGroupsByVnfId getRelatedInstanceGroupsByVnfId = new PresetAAIGetRelatedInstanceGroupsByVnfId("abcd");
        SimulatorApi.registerExpectationFromPreset(getRelatedInstanceGroupsByVnfId, CLEAR_THEN_SET);
        try {
            restTemplate.getForObject(uri + "/aai_get_instance_groups_by_vnf_instance_id/" + "dcba", String.class);
        } catch (HttpClientErrorException e){
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

    private void assertResponse(Object expected, String response) {
        try {
            JsonAssert.assertJsonEquals(expected, response);
        } catch (Exception | AssertionError e) {
            System.err.println("response was: " + response);
            throw e;
        }
    }
}
