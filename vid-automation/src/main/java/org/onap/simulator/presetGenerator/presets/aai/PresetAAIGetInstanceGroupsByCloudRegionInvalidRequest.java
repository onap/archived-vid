package org.onap.simulator.presetGenerator.presets.aai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PresetAAIGetInstanceGroupsByCloudRegionInvalidRequest extends BaseAAIPreset {

    private String cloudOwner;
    private String cloudRegionId;
    private String networkFunction;
    private final String type = "L3-NETWORK";
    private final String role = "SUB-INTERFACE";

    public PresetAAIGetInstanceGroupsByCloudRegionInvalidRequest(String cloudOwner, String cloudRegionId, String networkFunction) {
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
                "query", "query/instance-groups-byCloudRegion?type=" + type + "&role=" + role + "&function=" + getNetworkFunction()
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
        return "{" +
                "    \"results\": [" +
                "        {" +
                "            \"instance-group\": {" +
                "                \"instance-group-role\": \"JZmha7QSS4tJ\"," +
                "               \"some-filed\": \"some-data\"," + //extra field
                "                \"model-invariant-id\": \"model-id3\"," +
                "                \"model-version-id\": \"a0efd5fc-f7be-4502-936a-a6c6392b958f\"," +
                "                \"id\": \"AAI-12002-test3-vm230w\"," +
                "                \"description\": \"a9DEa0kpY\"," +
                "                \"instance-group-type\": \"type\"," +
                "                \"resource-version\": \"1520888659539\"," +
                "                \"instance-group-name\": \"wKmBXiO1xm8bK\"," +
                "                \"instance-group-function\": \"testfunction2\"," +
                "                \"relationship-list\": {" +
                "                    \"relationship\": [" +
                "                        {" +
                "                            \"related-to\": \"cloud-region\"," +
                "                            \"some-filed\": \"some-data\"," + //extra field
                "                            \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "                            \"related-link\": \"/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/AAI-12002-vm230w/AAI-region-vm230w\"," +
                "                            \"relationship-data\": [" +
                "                                {" +
                "                                    \"relationship-key\": \"cloud-region.cloud-owner\"," +
                "                                    \"relationship-value\": \"AAI-12002-vm230w\"" +
                "                                }," +
                "                                {" +
                "                                    \"relationship-key\": \"cloud-region.cloud-region-id\"," +
                "                                    \"relationship-value\": \"AAI-region-vm230w\"" +
                "                                }" +
                "                            ]," +
                "                            \"related-to-property\": [" +
                "                                {" +
                "                                    \"property-key\": \"cloud-region.owner-defined-type\"" +
                "                                }" +
                "                            ]" +
                "                        }" +
                "                    ]" +
                "                }" +
                "            }" +
                "        }," +
                "        {" +
                "            \"instance-group\": {" +
                "                \"instance-group-role\": \"JZmha7QSS4tJ\"," +
                "                \"model-invariant-id\": \"model-id1\"," +
                "                \"model-version-id\": \"a0efd5fc-f7be-4502-936a-a6c6392b958f\"," +
                "                \"id\": \"AAI-12002-test1-vm230w\"," +
                "                \"description\": \"a9DEa0kpY\"," +
                "                \"instance-group-type\": \"type\"," +
                "                \"resource-version\": \"1520886467989\"," +
                "                \"instance-group-name\": \"wKmBXiO1xm8bK\"," +
                "                \"instance-group-function\": \"testfunction2\"," +
                "                \"relationship-list\": {" +
                "                    \"relationship\": [" +
                "                        {" +
                "                            \"related-to\": \"cloud-region\"," +
                "                            \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "                            \"related-link\": \"/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/AAI-12002-vm230w/AAI-region-vm230w\"," +
                "                            \"relationship-data\": [" +
                "                                {" +
                "                                    \"relationship-key\": \"cloud-region.cloud-owner\"," +
                "                                    \"some-filed\": \"some-data\"," + //extra field
                "                                    \"relationship-value\": \"AAI-12002-vm230w\"" +
                "                                }," +
                "                                {" +
                "                                    \"relationship-key\": \"cloud-region.cloud-region-id\"," +
                "                                    \"relationship-value\": \"AAI-region-vm230w\"" +
                "                                }" +
                "                            ]," +
                "                            \"related-to-property\": [" +
                "                                {" +
                "                                    \"property-key\": \"cloud-region.owner-defined-type\"" +
                "                                }" +
                "                            ]" +
                "                        }" +
                "                    ]" +
                "                }" +
                "            }" +
                "        }," +
                "        {" +
                "            \"instance-group\": {" +
                "                \"instance-group-role\": \"JZmha7QSS4tJ\"," +
                "                \"model-invariant-id\": \"model-id2\"," +
                "                \"model-version-id\": \"version2\"," +
                "                \"id\": \"AAI-12002-test2-vm230w\"," +
                "                \"description\": \"a9DEa0kpY\"," +
                "                \"instance-group-type\": \"type\"," +
                "                \"resource-version\": \"1520888629970\"," +
                "                \"instance-group-name\": \"wKmBXiO1xm8bK\"," +
                "                \"instance-group-function\": \"testfunction2\"," +
                "                \"relationship-list\": {" +
                "                    \"relationship\": [" +
                "                        {" +
                "                            \"related-to\": \"cloud-region\"," +
                "                            \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "                            \"related-link\": \"/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/AAI-12002-vm230w/AAI-region-vm230w\"," +
                "                            \"relationship-data\": [" +
                "                                {" +
                "                                    \"relationship-key\": \"cloud-region.cloud-owner\"," +
                "                                    \"relationship-value\": \"AAI-12002-vm230w\"" +
                "                                }," +
                "                                {" +
                "                                    \"relationship-key\": \"cloud-region.cloud-region-id\"," +
                "                                    \"relationship-value\": \"AAI-region-vm230w\"" +
                "                                }" +
                "                            ]," +
                "                            \"related-to-property\": [" +
                "                                {" +
                "                                    \"property-key\": \"cloud-region.owner-defined-type\"" +
                "                                }" +
                "                            ]" +
                "                        }" +
                "                    ]" +
                "                }" +
                "            }" +
                "        }" +
                "    ]" +
                "}";
    }

}
