package org.onap.simulator.presetGenerator.presets.aai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PresetAAIGetInstanceGroupsByCloudRegion extends BaseAAIPreset {

    private String cloudOwner;
    private String cloudRegionId;
    private String networkFunction;
    private final String type = "L3-NETWORK";
    private final String role = "SUB-INTERFACE";

    public PresetAAIGetInstanceGroupsByCloudRegion(String cloudOwner, String cloudRegionId, String networkFunction) {
        this.cloudOwner = cloudOwner;
        this.cloudRegionId = cloudRegionId;
        this.networkFunction = networkFunction;
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.PUT;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/query";
    }

    @Override
    public Map<String, List> getQueryParams() {
        return ImmutableMap.of(
                "format", Collections.singletonList("resource")
        );
    }

    @Override
    public Object getRequestBody() {
        return ImmutableMap.of(
                "start", ImmutableList.of("cloud-infrastructure/cloud-regions/cloud-region/" + getCloudOwner() + "/" + getCloudRegionId()),
                "query", "query/instance-group-byCloudRegion?type=" + type + "&role=" + role + "&function=" + getNetworkFunction()
        );
    }

    public String getCloudOwner() {
        return cloudOwner;
    }

    public String getCloudRegionId() {
        return cloudRegionId;
    }

    public String getNetworkFunction() {
        return networkFunction;
    }

    @Override
    public Object getResponseBody() {
        return "{\n" +
                "    \"results\": [\n" +
                "        {\n" +
                "            \"instance-group\": {\n" +
                "                \"instance-group-role\": \"JZmha7QSS4tJ\",\n" +
                "                \"model-invariant-id\": \"model-id3\",\n" +
                "                \"model-version-id\": \"a0efd5fc-f7be-4502-936a-a6c6392b958f\",\n" +
                "                \"id\": \"AAI-12002-test3-vm230w\",\n" +
                "                \"description\": \"a9DEa0kpY\",\n" +
                "                \"instance-group-type\": \"type\",\n" +
                "                \"resource-version\": \"1520888659539\",\n" +
                "                \"instance-group-name\": \"wKmBXiO1xm8bK\",\n" +
                "                \"instance-group-function\": \"testfunction2\",\n" +
                "                \"relationship-list\": {\n" +
                "                    \"relationship\": [\n" +
                "                        {\n" +
                "                            \"related-to\": \"cloud-region\",\n" +
                "                            \"relationship-label\": \"org.onap.relationships.inventory.Uses\",\n" +
                "                            \"related-link\": \"/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/AAI-12002-vm230w/AAI-region-vm230w\",\n" +
                "                            \"relationship-data\": [\n" +
                "                                {\n" +
                "                                    \"relationship-key\": \"cloud-region.cloud-owner\",\n" +
                "                                    \"relationship-value\": \"AAI-12002-vm230w\"\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"relationship-key\": \"cloud-region.cloud-region-id\",\n" +
                "                                    \"relationship-value\": \"AAI-region-vm230w\"\n" +
                "                                }\n" +
                "                            ],\n" +
                "                            \"related-to-property\": [\n" +
                "                                {\n" +
                "                                    \"property-key\": \"cloud-region.owner-defined-type\"\n" +
                "                                }\n" +
                "                            ]\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"instance-group\": {\n" +
                "                \"instance-group-role\": \"JZmha7QSS4tJ\",\n" +
                "                \"model-invariant-id\": \"model-id1\",\n" +
                "                \"model-version-id\": \"a0efd5fc-f7be-4502-936a-a6c6392b958f\",\n" +
                "                \"id\": \"AAI-12002-test1-vm230w\",\n" +
                "                \"description\": \"a9DEa0kpY\",\n" +
                "                \"instance-group-type\": \"type\",\n" +
                "                \"resource-version\": \"1520886467989\",\n" +
                "                \"instance-group-name\": \"wKmBXiO1xm8bK\",\n" +
                "                \"instance-group-function\": \"testfunction2\",\n" +
                "                \"relationship-list\": {\n" +
                "                    \"relationship\": [\n" +
                "                        {\n" +
                "                            \"related-to\": \"cloud-region\",\n" +
                "                            \"relationship-label\": \"org.onap.relationships.inventory.Uses\",\n" +
                "                            \"related-link\": \"/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/AAI-12002-vm230w/AAI-region-vm230w\",\n" +
                "                            \"relationship-data\": [\n" +
                "                                {\n" +
                "                                    \"relationship-key\": \"cloud-region.cloud-owner\",\n" +
                "                                    \"relationship-value\": \"AAI-12002-vm230w\"\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"relationship-key\": \"cloud-region.cloud-region-id\",\n" +
                "                                    \"relationship-value\": \"AAI-region-vm230w\"\n" +
                "                                }\n" +
                "                            ],\n" +
                "                            \"related-to-property\": [\n" +
                "                                {\n" +
                "                                    \"property-key\": \"cloud-region.owner-defined-type\"\n" +
                "                                }\n" +
                "                            ]\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"instance-group\": {\n" +
                "                \"instance-group-role\": \"JZmha7QSS4tJ\",\n" +
                "                \"model-invariant-id\": \"model-id2\",\n" +
                "                \"model-version-id\": \"version2\",\n" +
                "                \"id\": \"AAI-12002-test2-vm230w\",\n" +
                "                \"description\": \"a9DEa0kpY\",\n" +
                "                \"instance-group-type\": \"type\",\n" +
                "                \"resource-version\": \"1520888629970\",\n" +
                "                \"instance-group-name\": \"wKmBXiO1xm8bK\",\n" +
                "                \"instance-group-function\": \"testfunction2\",\n" +
                "                \"relationship-list\": {\n" +
                "                    \"relationship\": [\n" +
                "                        {\n" +
                "                            \"related-to\": \"cloud-region\",\n" +
                "                            \"relationship-label\": \"org.onap.relationships.inventory.Uses\",\n" +
                "                            \"related-link\": \"/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/AAI-12002-vm230w/AAI-region-vm230w\",\n" +
                "                            \"relationship-data\": [\n" +
                "                                {\n" +
                "                                    \"relationship-key\": \"cloud-region.cloud-owner\",\n" +
                "                                    \"relationship-value\": \"AAI-12002-vm230w\"\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"relationship-key\": \"cloud-region.cloud-region-id\",\n" +
                "                                    \"relationship-value\": \"AAI-region-vm230w\"\n" +
                "                                }\n" +
                "                            ],\n" +
                "                            \"related-to-property\": [\n" +
                "                                {\n" +
                "                                    \"property-key\": \"cloud-region.owner-defined-type\"\n" +
                "                                }\n" +
                "                            ]\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}\n";
    }

}
