package org.onap.simulator.presetGenerator.presets.aai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PresetAAIModelVersionsByInvariantId extends BaseAAIPreset {


    @Override
    public String getReqPath() {
        return getRootPath() + "/query";
    }

    @Override
    public Map<String, List> getQueryParams() {
        return ImmutableMap.of(
                "format", Collections.singletonList("resource"),
                "depth", Collections.singletonList("0")
        );
    }

    @Override
    public Object getRequestBody() {
        return ImmutableMap.of(
                "start", ImmutableList.of("service-design-and-creation/models/model/f6342be5-d66b-4d03-a1aa-c82c3094c4ea"),
                "query", "query/serviceModels-byDistributionStatus?distributionStatus=DISTRIBUTION_COMPLETE_OK"
        );
        //  "{" +
        //  "  \"start\": [\"service-design-and-creation/models/model/f6342be5-d66b-4d03-a1aa-c82c3094c4ea"]," +
        //  "  \"query\": \"query/serviceModels-byDistributionStatus?distributionStatus=DISTRIBUTION_COMPLETE_OK\"" +
        //  "}";
    }

    @Override
    public Object getResponseBody() {
        return "{" +
                "    \"results\": [" +
                "        {" +
                "            \"model\": {" +
                "              \"model-invariant-id\": \"f6342be5-d66b-4d03-a1aa-c82c3094c4ea\"," +
                "              \"model-type\": \"service\"," +
                "              \"resource-version\": \"1534274421300\"" +
                "           }" +
                "      }," +
                "      {" +
                "          \"model-ver\": {" +
                "              \"model-version-id\": \"a92f899d-a3ec-465b-baed-1663b0a5aee1\"," +
                "              \"model-name\": \"NCM_VLAN_SVC_ym161f\"," +
                "              \"model-version\": \"3.0\"," +
                "              \"distribution-status\": \"DISTRIBUTION_COMPLETE_OK\"," +
                "              \"model-description\": \"Network Collection service for vLAN tagging\"," +
                "              \"resource-version\": \"1534788756086\"" +
                "          }" +
                "       }," +
                "       {" +
                "           \"model-ver\": {" +
                "               \"model-version-id\": \"d2fda667-e92e-4cfa-9620-5da5de01a319\"," +
                "               \"model-name\": \"NCM_VLAN_SVC_ym161f\"," +
                "               \"model-version\": \"1.0\"," +
                "               \"distribution-status\": \"DISTRIBUTION_COMPLETE_OK\"," +
                "                \"model-description\": \"Network Collection service for vLAN tagging\"," +
                "                \"resource-version\": \"1534444087221\"" +
                "            }" +
                "        }," +
                "        {" +
                "            \"model-ver\": {" +
                "                \"model-version-id\": \"0e97a118-b1b6-40d5-bbad-98cdd51b1c48\"," +
                "                \"model-name\": \"NCM_VLAN_SVC_ym161f\"," +
                "                \"model-version\": \"11.0\"," +
                "                \"distribution-status\": \"DISTRIBUTION_COMPLETE_OK\"," +
                "                \"model-description\": \"Network Collection service for vLAN tagging the-newest-version\"," +
                "                \"resource-version\": \"1550783120267\"" +
                "            }" +
                "        }" +
                "    ]" +

                "}";
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.PUT;
    }

}
