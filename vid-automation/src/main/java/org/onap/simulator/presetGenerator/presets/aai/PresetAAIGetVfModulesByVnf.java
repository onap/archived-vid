package org.onap.simulator.presetGenerator.presets.aai;

import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

public class PresetAAIGetVfModulesByVnf extends BaseAAIPreset {
    private final String vnfInstanceId;
    private final Placement vfModule1Placement;

    public PresetAAIGetVfModulesByVnf(String vnfInstanceId) {
        this.vnfInstanceId = vnfInstanceId;
        vfModule1Placement = null;
    }

    public PresetAAIGetVfModulesByVnf(String vnfInstanceId, Placement vfModule2Placement) {
        this.vnfInstanceId = vnfInstanceId;
        this.vfModule1Placement = vfModule2Placement;
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/network/generic-vnfs/generic-vnf/" + this.vnfInstanceId + "/vf-modules";
    }

    private String placementRelationship(Placement placement) {
        if (placement != null) {
            return "," + Placement.Util.placementRelationship("vserver", placement);
        } else {
            return "";
        }
    }

    @Override
    public Object getResponseBody() {
        return "" +
                "{" +
                "  \"vf-module\": [{" +
                "      \"vf-module-id\": \"2c1ca484-cbc2-408b-ab86-25a2c15ce280\"," +
                "      \"vf-module-name\": \"ss820f_0918_db\"," +
                "      \"orchestration-status\": \"deleted\"," +
                "      \"is-base-vf-module\": false," +
                "      \"in-maint\": true," +
                "      \"automated-assignment\": false," +
                "      \"resource-version\": \"1537396469591\"," +
                "      \"model-invariant-id\": \"09edc9ef-85d0-4b26-80de-1f569d49e750\"," +
                "      \"model-version-id\": \"522159d5-d6e0-4c2a-aa44-5a542a12a830\"," +
                "      \"model-customization-id\": \"55b1be94-671a-403e-a26c-667e9c47d091\"," +
                "      \"module-index\": 0," +
                "      \"relationship-list\": {" +
                "        \"relationship\": [{" +
                "            \"related-to\": \"vnfc\"," +
                "            \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "            \"related-link\": \"/aai/v13/network/vnfcs/vnfc/ss820f_0918_refvnf_vnfinstancem002mmb001\"," +
                "            \"relationship-data\": [{" +
                "                \"relationship-key\": \"vnfc.vnfc-name\"," +
                "                \"relationship-value\": \"ss820f_0918_refvnf_vnfinstancem002mmb001\"" +
                "              }" +
                "            ]" +
                "          }" +
                "        ]" +
                "      }" +
                "    }, {" +
                "      \"vf-module-id\": \"3ef042c4-259f-45e0-9aba-0989bd8d1cc5\"," +
                "      \"vf-module-name\": \"ss820f_0918_base\"," +
                "      \"heat-stack-id\": \"\"," +
                "      \"orchestration-status\": \"Assigned\"," +
                "      \"is-base-vf-module\": true," +
                "      \"automated-assignment\": false," +
                "      \"resource-version\": \"1537310272044\"," +
                "      \"model-invariant-id\": \"1e463c9c-404d-4056-ba56-28fd102608de\"," +
                "      \"model-version-id\": \"dc229cd8-c132-4455-8517-5c1787c18b14\"," +
                "      \"model-customization-id\": \"8ad8670b-0541-4499-8101-275bbd0e8b6a\"," +
                "      \"module-index\": 0," +
                "      \"relationship-list\": {" +
                "        \"relationship\": [{" +
                "            \"related-to\": \"vnfc\"," +
                "            \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "            \"related-link\": \"/aai/v13/network/vnfcs/vnfc/ss820f_0918_refvnf_vnfinstancem001mmb001\"," +
                "            \"relationship-data\": [{" +
                "                \"relationship-key\": \"vnfc.vnfc-name\"," +
                "                \"relationship-value\": \"ss820f_0918_refvnf_vnfinstancem001mmb001\"" +
                "              }" +
                "            ]" +
                "          }" +
            placementRelationship(vfModule1Placement) +
                "        ]" +
                "      }" +
                "    }" +
                "  ]" +
                "}";
    }


}
