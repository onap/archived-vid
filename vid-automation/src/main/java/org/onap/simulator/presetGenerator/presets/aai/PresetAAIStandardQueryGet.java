package org.onap.simulator.presetGenerator.presets.aai;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.text.StringEscapeUtils.escapeJson;
import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId.ATT_AIC;
import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId.hvf6;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StrSubstitutor;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

public class PresetAAIStandardQueryGet extends BaseAAIPreset {
    private final String instanceId;
    private final String instanceName;
    private final String instanceType;
    private final String instanceRole;
    private final String additionalProperties;
    private final String uri;
    private final String typeInResponse;
    private final Multimap<String, String> relationshipsUris;
    private Placement placement;

    private PresetAAIStandardQueryGet(String typeInResponse, String instanceId, String instanceName, String instanceType, String instanceRole, String uri, Multimap<String, String> relationshipsUris, String additionalProperties, Placement placement) {
        this.instanceId = defaultIfNull(instanceId, randomUUID());
        this.instanceName = defaultIfNull(instanceName, randomAlphanumeric());
        this.instanceType = defaultIfNull(instanceType, randomAlphanumeric());
        this.instanceRole = defaultIfNull(instanceRole, randomAlphanumeric());
        this.uri = uri;
        this.typeInResponse = typeInResponse;
        this.additionalProperties = additionalProperties;
        this.relationshipsUris = relationshipsUris;
        this.placement = placement;
    }

    private PresetAAIStandardQueryGet(String typeInResponse, String instanceId, String uri, Multimap<String, String> relationshipsUris, String additionalProperties, Placement placement) {
        this(typeInResponse, instanceId, randomAlphanumeric(), randomAlphanumeric(), randomAlphanumeric(), uri, relationshipsUris, additionalProperties, placement);
    }

    public static PresetAAIStandardQueryGet ofServiceInstance(String instanceId, final String modelVersionId, final String modelInvariantId, String subscriberId, String serviceType, Multimap<String, String> relationshipsUris) {
        return ofServiceInstance(instanceId, "", "", modelVersionId, modelInvariantId, subscriberId, serviceType, "GARBAGE DATA", relationshipsUris);
    }

    public static PresetAAIStandardQueryGet ofServiceInstance(String instanceId, String instanceType, String instanceRole, final String modelVersionId, final String modelInvariantId, String subscriberId, String serviceType, String orchStatus, Multimap<String, String> relationshipsUris) {
        return new PresetAAIStandardQueryGet(
                "service-instance", instanceId,
                randomAlphanumeric(),
                instanceType,
                instanceRole,
                new StrSubstitutor(ImmutableMap.of(
                        "global-customer-id", subscriberId,
                        "service-type", serviceType,
                        "service-instance-id", instanceId
                )).replace("" +
                        "/business/customers/customer/${global-customer-id}" +
                        "/service-subscriptions/service-subscription/${service-type}" +
                        "/service-instances/service-instance/${service-instance-id}"),
                relationshipsUris, "" +
                "\"model-invariant-id\": \"" + modelInvariantId + "\", " +
                "\"model-version-id\": \"" + modelVersionId + "\", " +
                "\"resource-version\": \"GARBAGE DATA\", " +
                "\"orchestration-status\": \"" + orchStatus + "\", ", null
        );
    }

    public static PresetAAIStandardQueryGet ofInstanceGroup(String groupType, String groupRole, Multimap<String, String> relationshipsUris) {
        return ofInstanceGroup(groupType, groupRole, relationshipsUris, "4bb2e27e-ddab-4790-9c6d-1f731bc14a45", "daeb6568-cef8-417f-9075-ed259ce59f48");
    }

    public static PresetAAIStandardQueryGet ofInstanceGroup(String groupType, String groupRole, Multimap<String, String> relationshipsUris, String modelInvariantId, String modelVersionId) {
        final String instanceId = randomUUID();
        return new PresetAAIStandardQueryGet(
                "instance-group", instanceId, randomAlphanumeric(), groupType, groupRole,
                "/network/instance-groups/instance-group/" + instanceId,
                relationshipsUris, "" +
                "    \"id\": \"" + instanceId + "\"," +
                addModelIds(modelInvariantId, modelVersionId) +
                "    \"description\": \"vTSBC Customer Landing Network Collection Desc\"," +
                "    \"resource-version\": \"1536169790853\"," +
                "    \"instance-group-function\": \"vTSBC Customer Landing Network Collection\",", null
        );
    }

    public static PresetAAIStandardQueryGet ofVnf(String instanceId, Multimap<String, String> relationshipsUris, Placement placement) {
        return ofVnf(instanceId, "vnf-instance-model-version-id", "vnf-instance-model-customization-id", "", relationshipsUris, placement);
    }

    public static Placement defaultPlacement() {
        return new Placement(ATT_AIC, hvf6, "bae71557c5bb4d5aac6743a4e5f1d054");
    }

    public static PresetAAIStandardQueryGet ofVnf(String instanceId, String modelVersionId, String modelCustomizationId, String additionalProperties, Multimap<String, String> relationshipsUris, Placement placement) {
        return new PresetAAIStandardQueryGet(
                "vnf", instanceId,
                "/network/generic-vnfs/generic-vnf/" + instanceId,
                relationshipsUris, additionalProperties +
                "\"model-invariant-id\": \"vnf-instance-model-invariant-id\", " +
                "\"model-customization-id\": \"" + modelCustomizationId + "\", " +
                "\"model-version-id\": \"" + modelVersionId + "\", ", placement
        );
    }

    public static PresetAAIStandardQueryGet ofRelatedVnf(String instanceId, String modelVersionId, String additionalProperties, Multimap<String, String> relationshipsUris) {
        return new PresetAAIStandardQueryGet(
                "vnf", instanceId,
                "/network/generic-vnfs/generic-vnf/" + instanceId,
                relationshipsUris, additionalProperties +
                "\"model-invariant-id\": \"vnf-instance-model-invariant-id\", " +
                "\"model-version-id\": \"" + modelVersionId + "\", ", null
        );
    }

    private static String randomAlphanumeric() {
        return RandomStringUtils.randomAlphanumeric(17);
    }

    private static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    public static PresetAAIStandardQueryGet ofL3Network(String instanceType, String orchStatus, Multimap<String, String> relationshipsUris) {
        return ofL3Network(randomUUID(), randomAlphanumeric(), instanceType, relationshipsUris, orchStatus, "prov", "network-instance-model-version-id", "network-instance-model-customization-id");
    }

    public static PresetAAIStandardQueryGet ofL3Network(String instanceType, String orchStatus, String provStatus, String modelVersionId, String modelCustomizationId, Multimap<String, String> relationshipsUris) {
        return ofL3Network(randomUUID(), randomAlphanumeric(), instanceType, relationshipsUris, orchStatus, provStatus, modelVersionId, modelCustomizationId);
    }

    public static PresetAAIStandardQueryGet ofL3Network(String instanceId, String instanceName, String instanceType, Multimap<String, String> relationshipsUris, String orchStatus) {
        return ofL3Network(instanceId, instanceName, instanceType, relationshipsUris, orchStatus, "prov", "network-instance-model-version-id", "94fdd893-4a36-4d70-b16a-ec29c54c184f");
    }

    public static PresetAAIStandardQueryGet ofL3Network(String instanceId, String instanceName, String instanceType, Multimap<String, String> relationshipsUris, String orchStatus, String provStatus, String modelVersionId, String modelCustomizationId) {
        return new PresetAAIStandardQueryGet(
                "network", instanceId, instanceName, instanceType, randomAlphanumeric(),
                "/network/l3-networks/l3-network/" + instanceId,
                relationshipsUris, "" +
                "\"orchestration-status\": \"" + orchStatus + "\"," +
                "\"prov-status\": \"" + provStatus + "\"," +
                "\"network-technology\": \"contrail\", " +
                "\"neutron-network-id\": \"66ee6123-1c45-4e71-b6c0-a748ae0fee88\", " +
                "\"is-bound-to-vpn\": true, " +
                "\"is-external-network\": true, " +
                "\"model-invariant-id\": \"network-instance-model-invariant-id\", " +
                "\"model-customization-id\": \"" + modelCustomizationId + "\", " +
                "\"model-version-id\": \"" + modelVersionId + "\", ", null
        );
    }

    public static PresetAAIStandardQueryGet ofVolumeGroup(String instanceType, Multimap<String, String> relationshipsUris) {
        final String instanceId = randomUUID();
        return new PresetAAIStandardQueryGet(
                "volume-group", instanceId, randomAlphanumeric(), instanceType, randomAlphanumeric(),
                "/cloud-infrastructure/cloud-regions/cloud-region/irma-aic/olson5b/volume-groups/volume-group/" + instanceId,
                relationshipsUris, "" +
                "    \"vnf-type\": \"vSON_test_ap7145/vSON_test_ap7145 0\"," +
                "    \"orchestration-status\": \"Active\"," +
                "    \"model-customization-id\": \"bc3bbdcc-42f3-4682-b151-99c308d15255\"," +
                "    \"vf-module-model-customization-id\": \"bc3bbdcc-42f3-4682-b151-99c308d15255\"," +
                "    \"resource-version\": \"1533679899735\",", null
        );
    }


    public static PresetAAIStandardQueryGet ofCollectionResource(String orchStatus, Multimap<String, String> relationshipsUris, String modelInvariantId, String modelVersionId) {
        final String instanceId = randomUUID();
        return new PresetAAIStandardQueryGet(
                "collection", instanceId, randomAlphanumeric(), "L3-NETWORK", randomAlphanumeric(),
                "/network/collections/collection/" + instanceId,
                relationshipsUris, "" +
                "\"orchestration-status\": \"" + orchStatus + "\", " +
                addModelIds(modelInvariantId, modelVersionId), null
        );
    }

    private static String addModelIds(String modelInvariantId, String modelVersionId) {
        return "\"model-invariant-id\": \"" + modelInvariantId + "\", " +
                "\"model-version-id\": \"" + modelVersionId + "\", ";
    }

    public static PresetAAIStandardQueryGet ofVrf(String orchStatus, Multimap<String, String> relationshipsUris) {
        final String instanceId = randomUUID();
        return new PresetAAIStandardQueryGet(
                "configuration", instanceId, randomAlphanumeric(), "COLLECTION", randomAlphanumeric(),
                "/network/configurations/configuration/" + instanceId,
                relationshipsUris, "" +
                "\"orchestration-status\": \"" + orchStatus + "\", " +
                "\"model-invariant-id\": \"b67a289b-1688-496d-86e8-1583c828be0a\", " +
                "\"model-customization-id\": \"dd024d73-9bd1-425d-9db5-476338d53433\", " +
                "\"model-version-id\": \"9cac02be-2489-4374-888d-2863b4511a59\", ", null
        );
    }

    public static PresetAAIStandardQueryGet ofVpn(String orchStatus, Multimap<String, String> relationshipsUris, String globalRoutTarget, String routeTargetRole, String customerId, String region) {
        final String instanceId = randomUUID();
        return new PresetAAIStandardQueryGet(
                "vpn", instanceId, randomAlphanumeric(), "SERVICE-INFRASTRUCTURE", randomAlphanumeric(),
                "/network/collections/collection/" + instanceId,
                relationshipsUris, "" +
                "\"orchestration-status\": \"" + orchStatus + "\", " +
                "\"prov-status\": \"" + "prov" + "\"," +
                "\"model-invariant-id\": \"vpn-model-invariant-id\", " +
                "\"model-customization-id\": \"vpn-model-customization-id\", " +
                "\"customer-vpn-id\": \"" + customerId + "\", " +
                "\"vpn-region\": \"" + region + "\", " +
                "\"route-targets\" : [" +
                "            {" +
                "              \"global-route-target\":\"" + globalRoutTarget + "\"," +
                "              \"route-target-role\" : \"" + routeTargetRole + "\"" +
                "            }" +
                "            ],"+
                "\"model-version-id\": \"vpn-model-version-id\", ", null
        );
    }

    public static PresetAAIStandardQueryGet ofVlanTag(int vlanIdOuter) {
        final String instanceId = randomUUID();
        return new PresetAAIStandardQueryGet(
                "vlan-tag", instanceId,
                "/this is an invented link/tag/vlan-tags/vlan-tag/" + instanceId,
                ImmutableMultimap.of(), "" +
                "\"vlan-interface\": \"US-10688-genvnf-vlan-interface1\", " +
                "\"vlan-id-inner\": " + 123456789 + ", " +
                "\"vlan-id-outer\": " + vlanIdOuter + ", " +
                "\"resource-version\": \"1518934744675\", " +
                "\"in-maint\": false, " +
                "\"is-ip-unnumbered\": false, ", null
        );
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public String getInstanceRole() {
        return instanceRole;
    }

    @Override
    public Object getResponseBody() {
        return "" +
                "{ " +
                "  \"" + typeInResponse + "-id\": \"" + getInstanceId() + "\", " +
                "  \"" + typeInResponse + "-name\": \"" + escapeJson(getInstanceName()) + "\", " +
                "  \"" + typeInResponse + "-type\": \"" + escapeJson(getInstanceType()) + "\", " +
                "  \"" + typeInResponse + "-role\": \"" + escapeJson(getInstanceRole()) + "\", " +
                additionalProperties +
                "  \"relationship-list\": { " +
                "    \"relationship\": [ " +
                Stream.concat(
                        placement !=null ? Stream.of(buildPlacementRelationship()) : Stream.empty(),
                        relationshipsUris.entries().stream().map(entry -> buildRelationship(entry.getKey(), entry.getValue())
                        )).collect(Collectors.joining(",")) +
                "    ] " +
                "  } " +
                "} ";
    }

    private String buildRelationship(String relatedTo, final String relatedLink) {
        return "" +
                "{ " +
                "  \"related-to\": \"" + relatedTo + "\", " +
                "  \"relationship-label\": \"org.onap.relationships.inventory.ComposedOf\", " +
                "  \"related-link\": \"" + relatedLink + "\", " +
                "  \"relationship-data\": [" +
                "      { " +
                "        \"relationship-key\": \"GARBAGE DATA\", " +
                "        \"relationship-value\": \"GARBAGE DATA\" " +
                "      } " +
                "  ] " +
                "}";
    }

    private String buildPlacementRelationship() {
        String relatedTo = StringUtils.equals(instanceType,"vf-module")? "vserver": "tenant";
        return Placement.Util.placementRelationship(relatedTo, placement);
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + uri;
    }
}