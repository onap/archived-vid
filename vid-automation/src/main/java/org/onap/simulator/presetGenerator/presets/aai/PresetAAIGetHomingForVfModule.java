package org.onap.simulator.presetGenerator.presets.aai;

import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

public class PresetAAIGetHomingForVfModule extends BaseAAIPreset {
    private final String vnfInstanceId;
    private final String vfModuleId;
    private final String tenantId;
    private final String lcpRegionId;

    public PresetAAIGetHomingForVfModule(String vnfInstanceId, String vfModuleId, String tenantId, String lcpRegionId) {
        this.vnfInstanceId = vnfInstanceId;
        this.vfModuleId = vfModuleId;
        this.tenantId = tenantId;
        this.lcpRegionId = lcpRegionId;
    }

    public PresetAAIGetHomingForVfModule(String vnfInstanceId, String vfModuleId) {
        this(
                vnfInstanceId,
                vfModuleId,
                "db1818f7f2e34862b378bfb2cc520f91",
                "olson5b"
        );
    }

    public PresetAAIGetHomingForVfModule() {
        this(
            "0846287b-65bf-45a6-88f6-6a1af4149fac",
            "a9b70ac0-5917-4203-a308-0e6920e6d09b",
            "db1818f7f2e34862b378bfb2cc520f91",
            "olson5b"
        );
    }

    @Override
    public Object getResponseBody() {
        return "{" +
                "    \"vf-module-id\": \""+vfModuleId+"\"," +
                "    \"vf-module-name\": \"apndns_az_02_module_1\"," +
                "    \"heat-stack-id\": \"apndns_az_02_module_1/97a319f3-b095-4fff-befa-c657508ecaf8\"," +
                "    \"orchestration-status\": \"active\"," +
                "    \"is-base-vf-module\": false," +
                "    \"resource-version\": \"1530559380383\"," +
                "    \"model-invariant-id\": \"74450b48-0aa0-4743-8314-9163e92b7862\"," +
                "    \"model-version-id\": \"6bc01a2b-bc48-4991-b9fe-e22c2215d801\"," +
                "    \"model-customization-id\": \"74f638c2-0368-4212-8f73-e961005af17c\"," +
                "    \"module-index\": 0," +
                "    \"relationship-list\": {" +
                "        \"relationship\": [" +
                "            {" +
                "                \"related-to\": \"l3-network\"," +
                "                \"relationship-label\": \"org.onap.relationships.inventory.DependsOn\"," +
                "                \"related-link\": \"/aai/v12/network/l3-networks/l3-network/335e62be-73a3-41e8-930b-1a677bcafea5\"," +
                "                \"relationship-data\": [" +
                "                    {" +
                "                        \"relationship-key\": \"l3-network.network-id\"," +
                "                        \"relationship-value\": \"335e62be-73a3-41e8-930b-1a677bcafea5\"" +
                "                    }" +
                "                ]," +
                "                \"related-to-property\": [" +
                "                    {" +
                "                        \"property-key\": \"l3-network.network-name\"," +
                "                        \"property-value\": \"MNS-FN-25180-T-02Shared_oam_protected_net_1\"" +
                "                    }" +
                "                ]" +
                "            }," +
                "            {" +
                "                \"related-to\": \"l3-network\"," +
                "                \"relationship-label\": \"org.onap.relationships.inventory.DependsOn\"," +
                "                \"related-link\": \"/aai/v12/network/l3-networks/l3-network/2db4ee3e-2ac7-4fc3-8739-ecf53416459e\"," +
                "                \"relationship-data\": [" +
                "                    {" +
                "                        \"relationship-key\": \"l3-network.network-id\"," +
                "                        \"relationship-value\": \"2db4ee3e-2ac7-4fc3-8739-ecf53416459e\"" +
                "                    }" +
                "                ]," +
                "                \"related-to-property\": [" +
                "                    {" +
                "                        \"property-key\": \"l3-network.network-name\"," +
                "                        \"property-value\": \"Mobisupport-FN-27099-T-02_int_apn_dns_net_1\"" +
                "                    }" +
                "                ]" +
                "            }," +
                "            {" +
                "                \"related-to\": \"volume-group\"," +
                "                \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "                \"related-link\": \"/aai/v12/cloud-infrastructure/cloud-regions/cloud-region/irma-aic/" +  lcpRegionId + "/volume-groups/volume-group/66013ebe-0c81-44b9-a24f-7c6acba73a39\"," +
                "                \"relationship-data\": [" +
                "                    {" +
                "                        \"relationship-key\": \"cloud-region.cloud-owner\"," +
                "                        \"relationship-value\": \"irma-aic\"" +
                "                    }," +
                "                    {" +
                "                        \"relationship-key\": \"cloud-region.cloud-region-id\"," +
                "                        \"relationship-value\": \"" +  lcpRegionId + "\"" +
                "                    }," +
                "                    {" +
                "                        \"relationship-key\": \"volume-group.volume-group-id\"," +
                "                        \"relationship-value\": \"66013ebe-0c81-44b9-a24f-7c6acba73a39\"" +
                "                    }" +
                "                ]" +
                "            }," +
                "            {" +
                "                \"related-to\": \"vserver\"," +
                "                \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "                \"related-link\": \"/aai/v12/cloud-infrastructure/cloud-regions/cloud-region/irma-aic/" +  lcpRegionId + "/tenants/tenant/" + tenantId+ "/vservers/vserver/5eef9f6d-9933-4bc6-9a1a-862d61309437\"," +
                "                \"relationship-data\": [" +
                "                    {" +
                "                        \"relationship-key\": \"cloud-region.cloud-owner\"," +
                "                        \"relationship-value\": \"irma-aic\"" +
                "                    }," +
                "                    {" +
                "                        \"relationship-key\": \"cloud-region.cloud-region-id\"," +
                "                        \"relationship-value\": \"" +  lcpRegionId + "\"" +
                "                    }," +
                "                    {" +
                "                        \"relationship-key\": \"tenant.tenant-id\"," +
                "                        \"relationship-value\": \"" +  tenantId + "\"" +
                "                    }," +
                "                    {" +
                "                        \"relationship-key\": \"vserver.vserver-id\"," +
                "                        \"relationship-value\": \"5eef9f6d-9933-4bc6-9a1a-862d61309437\"" +
                "                    }" +
                "                ]," +
                "                \"related-to-property\": [" +
                "                    {" +
                "                        \"property-key\": \"vserver.vserver-name\"," +
                "                        \"property-value\": \"zolson5bfapn01dns002\"" +
                "                    }" +
                "                ]" +
                "            }" +
                "        ]" +
                "    }" +
                "}";
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/network/generic-vnfs/generic-vnf/" + this.vnfInstanceId + "/vf-modules/vf-module/" + this.vfModuleId;
    }


}
