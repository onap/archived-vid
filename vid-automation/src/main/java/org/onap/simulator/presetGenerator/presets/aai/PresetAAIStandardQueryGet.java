package org.onap.simulator.presetGenerator.presets.aai;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.text.StringEscapeUtils.escapeJson;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomStringUtils;
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

    private PresetAAIStandardQueryGet(String typeInResponse, String instanceId, String instanceName, String instanceType, String instanceRole, String uri, Multimap<String, String> relationshipsUris, String additionalProperties) {
        this.instanceId = defaultIfNull(instanceId, randomUUID());
        this.instanceName = defaultIfNull(instanceName, randomAlphanumeric());
        this.instanceType = defaultIfNull(instanceType, randomAlphanumeric());
        this.instanceRole = defaultIfNull(instanceRole, randomAlphanumeric());
        this.uri = uri;
        this.typeInResponse = typeInResponse;
        this.additionalProperties = additionalProperties;
        this.relationshipsUris = relationshipsUris;
    }

    private PresetAAIStandardQueryGet(String typeInResponse, String instanceId, String uri, Multimap<String, String> relationshipsUris, String additionalProperties) {
        this(typeInResponse, instanceId, randomAlphanumeric(), randomAlphanumeric(), randomAlphanumeric(), uri, relationshipsUris, additionalProperties);
    }

    public static PresetAAIStandardQueryGet ofServiceInstance(String instanceId, final String modelVersionId, final String modelInvariantId, String subscriberId, String serviceType, Multimap<String, String> relationshipsUris) {
        return new PresetAAIStandardQueryGet(
                "service-instance", instanceId,
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
                "\"orchestration-status\": \"GARBAGE DATA\", "
        );
    }

    public static PresetAAIStandardQueryGet ofInstanceGroup(String groupType, String groupRole, Multimap<String, String> relationshipsUris) {
        final String instanceId = randomUUID();
        return new PresetAAIStandardQueryGet(
                "instance-group", instanceId, randomAlphanumeric(), groupType, groupRole,
                "/network/instance-groups/instance-group/" + instanceId,
                relationshipsUris, "" +
                "    \"id\": \"" + instanceId + "\"," +
                "    \"model-invariant-id\": \"4bb2e27e-ddab-4790-9c6d-1f731bc14a45\"," +
                "    \"model-version-id\": \"daeb6568-cef8-417f-9075-ed259ce59f48\"," +
                "    \"description\": \"vTSBC Customer Landing Network Collection Desc\"," +
                "    \"resource-version\": \"1536169790853\"," +
                "    \"instance-group-function\": \"vTSBC Customer Landing Network Collection\","
        );
    }

    public static PresetAAIStandardQueryGet ofVnf(String instanceId, Multimap<String, String> relationshipsUris) {
        return ofVnf(instanceId, "vnf-instance-model-version-id", "vnf-instance-model-customization-id", "", relationshipsUris);
    }

    public static PresetAAIStandardQueryGet ofVnf(String instanceId, String modelVersionId,String modelCustomizationId, String additionalProperties, Multimap<String, String> relationshipsUris) {
        return new PresetAAIStandardQueryGet(
                "vnf", instanceId,
                "/network/generic-vnfs/generic-vnf/" + instanceId,
                relationshipsUris, additionalProperties +
                "\"model-invariant-id\": \"vnf-instance-model-invariant-id\", " +
                "\"model-customization-id\": \"" + modelCustomizationId + "\", "+
                "\"model-version-id\": \"" + modelVersionId + "\", "
        );
    }

    public static PresetAAIStandardQueryGet ofRelatedVnf(String instanceId, String modelVersionId, String additionalProperties, Multimap<String, String> relationshipsUris) {
        return new PresetAAIStandardQueryGet(
                "vnf", instanceId,
                "/network/generic-vnfs/generic-vnf/" + instanceId,
                relationshipsUris, additionalProperties +
                "\"model-invariant-id\": \"vnf-instance-model-invariant-id\", " +
                "\"model-version-id\": \"" + modelVersionId + "\", "
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

    public static PresetAAIStandardQueryGet ofL3Network(String instanceType, String orchStatus, String provStatus, String modelVersionId, String modelCustomizationId,Multimap<String, String> relationshipsUris) {
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
                "\"model-version-id\": \"" + modelVersionId + "\", "
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
                "    \"resource-version\": \"1533679899735\","
        );
    }

    public static PresetAAIStandardQueryGet ofCollectionResource(String orchStatus, Multimap<String, String> relationshipsUris) {
        final String instanceId = randomUUID();
        return new PresetAAIStandardQueryGet(
                "collection", instanceId, randomAlphanumeric(), "L3-NETWORK", randomAlphanumeric(),
                "/network/collections/collection/" + instanceId,
                relationshipsUris, "" +
                "\"orchestration-status\": \"" + orchStatus + "\", " +
                "\"model-invariant-id\": \"081ceb56-eb71-4566-a72d-3e7cbee5cdf1\", " +
                "\"model-version-id\": \"ce8c98bc-4691-44fb-8ff0-7a47487c11c4\", "
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
                "\"is-ip-unnumbered\": false, "
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
                relationshipsUris.entries().stream().map(
                        entry -> buildRelationship(entry.getKey(), entry.getValue())
                ).collect(Collectors.joining(",")) +
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

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + uri;
    }


}
