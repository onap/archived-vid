package org.onap.simulator.presetGenerator.presets.aai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PresetAAIGetServiceInstancesByInvariantId extends BaseAAIPreset {

    private String globalCustomerId;
    private String serviceType;
    private String invariantId;
    private Map<String, Multimap<String, String>> servicesWithRelationships;

    public PresetAAIGetServiceInstancesByInvariantId(String globalCustomerId, String serviceType, String invariantId,
                                                     Map<String, Multimap<String, String>> servicesWithRelationships) {
        this.globalCustomerId = globalCustomerId;
        this.serviceType = serviceType;
        this.invariantId = invariantId;
        this.servicesWithRelationships = servicesWithRelationships;
    }

    @Override
    public String getResponseBody() {
        return "{" +
                "  \"service-instance\":[" +
                servicesWithRelationships.entrySet().stream().map(
                    entry -> buildServiceInstance(entry.getKey(), entry.getValue())
                ).collect(Collectors.joining(",")) +
                "  ]" +
                "}";
    }

    private String buildServiceInstance(String serviceInstanceId, Multimap<String, String> relationships) {
        return "    {" +
                "      \"service-instance-id\":\"" + serviceInstanceId + "\"," +
                "      \"service-instance-name\":\"service-instance-name\"," +
                "      \"service-instance-type\":\"service-instance-type\"," +
                "      \"service-instance-role\":\"service-instance-role\"," +
                "      \"model-invariant-id\":\"" + invariantId + "\"," +
                "      \"model-version-id\":\"7a6ee536-f052-46fa-aa7e-2fca9d674c44\"," +
                "      \"resource-version\":\"GARBAGE DATA\"," +
                "      \"orchestration-status\":\"GARBAGE DATA\"," +
                "      \"relationship-list\":{" +
                "        \"relationship\":[" +
                relationships.entries().stream().map(
                        entry -> buildRelationship(entry.getKey(), entry.getValue())
                ).collect(Collectors.joining(",")) +
                "        ]" +
                "      }" +
                "    }";
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
        return getRootPath() + "/business/customers/customer/" +
                globalCustomerId + "/service-subscriptions/service-subscription/" +
                serviceType + "/service-instances";
    }

    @Override
    public Map<String, List> getQueryParams() {
        return ImmutableMap.of("model-invariant-id", ImmutableList.of(invariantId));
    }
}
