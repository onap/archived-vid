package org.onap.simulator.presetGenerator.presets.aai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PresetAAICloudRegionAndSourceFromConfigurationPut extends BaseAAIPreset {

    public PresetAAICloudRegionAndSourceFromConfigurationPut(String configurationId, String cloudRegionId) {
        this.configurationId = configurationId;
        this.cloudRegionId = cloudRegionId;
    }

    private final String configurationId;
    private final String cloudRegionId;

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
                "format", Collections.singletonList("simple"),
                "nodesOnly", Collections.singletonList("true")
        );
    }

    @Override
    public Object getRequestBody() {
        return ImmutableMap.of(
                "start", ImmutableList.of("network/configurations/configuration/" + getConfigurationId()),
                "query", "query/cloud-region-and-source-FromConfiguration"
        );
        //  "{" +
        //  "  \"start\": [\"network/configurations/configuration/{configuration-id}\"]," +
        //  "  \"query\": \"query/cloud-region-and-source-FromConfiguration\"" +
        //  "}";
    }

    public String getConfigurationId() {
        return configurationId;
    }

    public String getCloudRegionId() {
        return cloudRegionId;
    }

    @Override
    public Object getResponseBody() {
        return "" +
                "{" +
                "  \"results\": [{" +
                "      \"id\": \"2979590232\"," +
                "      \"node-type\": \"cloud-region\"," +
                "      \"url\": \"/aai/v12/cloud-infrastructure/cloud-regions/cloud-region/att-aic/SDNO-S-BcloudReg-E1802\"," +
                "      \"properties\": {" +
                "        \"cloud-owner\": \"att-aic\"," +
                "        \"cloud-region-id\": \"" + getCloudRegionId() + "\"," +
                "        \"sriov-automation\": false," +
                "        \"resource-version\": \"1513631040564\"" +
                "      }" +
                "    }," +
                "    {" +
                "      \"id\": \"2979598424\"," +
                "      \"node-type\": \"generic-vnf\"," +
                "      \"url\": \"/aai/v12/network/generic-vnfs/generic-vnf/SOURCE-gVnf-E1802\"," +
                "      \"properties\": {" +
                "        \"vnf-id\": \"SOURCE-gVnf-E1802\"," +
                "        \"vnf-name\": \"SOURCE-vnf-SDNO\"," +
                "        \"vnf-type\": \"S-1-SDNO\"," +
                "        \"service-id\": \"a9a77d5a-123e-4-SDNO\"," +
                "        \"orchestration-status\": \"active\"," +
                "        \"in-maint\": true," +
                "        \"is-closed-loop-disabled\": false," +
                "        \"resource-version\": \"1513631043149\"" +
                "      }" +
                "    }" +
                "  ]" +
                "}";
    }
}
