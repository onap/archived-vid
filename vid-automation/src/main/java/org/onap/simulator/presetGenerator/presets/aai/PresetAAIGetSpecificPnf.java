package org.onap.simulator.presetGenerator.presets.aai;

import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

public class PresetAAIGetSpecificPnf extends BaseAAIPreset {


    public PresetAAIGetSpecificPnf() {
    }


    @Override
    public Object getResponseBody() {
        return "{" +
                "      \"pnf-name\": \"MX_960-F722\"," +
                "      \"pnf-name2\": \"MX_960-F722-name-2\"," +
                "      \"pnf-name2-source\": \"MX_960-F722-name-2-source\"," +
                "      \"pnf-id\": \"MX_960-F722-id\"," +
                "      \"equip-type\": \"Switch\"," +
                "      \"equip-vendor\": \"Cisco\"," +
                "      \"equip-model\": \"ASR1002-X\"," +
                "      \"resource-version\": \"1494001797554\"," +
                "      \"relationship-list\": {" +
                "        \"relationship\": [" +
                "          {" +
                "            \"related-to\": \"complex\"," +
                "            \"related-link\": \"/aai/v11/cloud-infrastructure/complexes/complex/NAMEAAI2\"," +
                "            \"relationship-data\": [" +
                "              {" +
                "                \"relationship-key\": \"complex.physical-location-id\"," +
                "                \"relationship-value\": \"NAMEAAI2\"" +
                "              }" +
                "            ]" +
                "          }" +
                "        ]" +
                "      }" +
                "    }";
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/network/pnfs/pnf/MX_960-F722";
    }

    @Override
    public int getResponseCode() {
        return 200;
    }
}
