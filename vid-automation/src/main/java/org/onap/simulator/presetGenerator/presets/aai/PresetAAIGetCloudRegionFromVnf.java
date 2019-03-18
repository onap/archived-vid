package org.onap.simulator.presetGenerator.presets.aai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

public class PresetAAIGetCloudRegionFromVnf extends BaseAAIPreset {

    private String vnfInstanceId;

    public PresetAAIGetCloudRegionFromVnf(String vnfInstanceId) {
        this.vnfInstanceId = vnfInstanceId;
    }

    public String getVnfInstanceId() {
        return vnfInstanceId;
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
                "format", Collections.singletonList("simple")
        );
    }

    @Override
    public Object getRequestBody() {
        return ImmutableMap.of(
                "start", ImmutableList.of("/network/generic-vnfs/generic-vnf/" + getVnfInstanceId()),
                "query", "/query/cloud-region-fromVnf"
        );
    }

    @Override
    public Object getResponseBody() {
        return "{" +
                "    \"results\": [" +
                "        {" +
                "            \"id\": \"363687968\"," +
                "            \"node-type\": \"generic-vnf\"," +
                "            \"url\": \"/aai/v14/network/generic-vnfs/generic-vnf/" + getVnfInstanceId() + "\"," +
                "            \"properties\": {" +
                "                \"vnf-id\": \"" + getVnfInstanceId() + "\"," +
                "                \"vnf-name\": \"zhvf23bmogx05_oam_01_rk\"," +
                "                \"vnf-type\": \"vMOG_SVC_UPDATED_FLAV_OAM/vMOG_VSP_UPDATED_FLAV 0\"," +
                "                \"service-id\": \"db171b8f-115c-4992-a2e3-ee04cae357e0\"," +
                "                \"prov-status\": \"NVTPROV\"," +
                "                \"orchestration-status\": \"Created\"," +
                "                \"in-maint\": false," +
                "                \"is-closed-loop-disabled\": false," +
                "                \"resource-version\": \"1522780608709\"," +
                "                \"model-invariant-id\": \"5108d7e4-f32a-44cd-8835-433a70915b8c\"," +
                "                \"model-version-id\": \"bae37933-b694-4b4e-a734-db63416f9cfe\"," +
                "                \"model-customization-id\": \"732305c6-d36a-47ee-a701-6f28f87b90cd\"," +
                "                \"nf-type\": \"POLICY\"," +
                "                \"nf-function\": \"Emanuel Orchestration Gateway (MOG)\"," +
                "                \"nf-role\": \"vMOG\"," +
                "                \"nf-naming-code\": \"\"" +
                "            }," +
                "            \"related-to\": [" +
                "                {" +
                "                    \"id\": \"193753168\"," +
                "                    \"relationship-label\": \"org.onap.relationships.inventory.ComposedOf\"," +
                "                    \"node-type\": \"service-instance\"," +
                "                    \"url\": \"/aai/v14/business/customers/customer/a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb/service-subscriptions/service-subscription/vMOG/service-instances/service-instance/7f10ffe2-548f-4168-8722-b782d05675b7\"" +
                "                }" +
                "            ]" +
                "        }," +
                "        {" +
                "            \"id\": \"8757432\"," +
                "            \"node-type\": \"tenant\"," +
                "            \"url\": \"/aai/v14/cloud-infrastructure/cloud-regions/cloud-region/irma-aic/hvf23b/tenants/tenant/3e9a20a3e89e45f884e09df0cc2d2d2a\"," +
                "            \"properties\": {" +
                "                \"tenant-id\": \"3e9a20a3e89e45f884e09df0cc2d2d2a\"," +
                "                \"tenant-name\": \"APPC-24595-T-IST-02C\"," +
                "                \"resource-version\": \"1539855222745\"" +
                "            }," +
                "            \"related-to\": [" +
                "                {" +
                "                    \"id\": \"264798392\"," +
                "                    \"relationship-label\": \"org.onap.relationships.inventory.BelongsTo\"," +
                "                    \"node-type\": \"vserver\"," +
                "                    \"url\": \"/aai/v14/cloud-infrastructure/cloud-regions/cloud-region/irma-aic/hvf23b/tenants/tenant/3e9a20a3e89e45f884e09df0cc2d2d2a/vservers/vserver/088c2bf0-80e2-4bdf-93ae-b2469fbeba84\"" +
                "                }" +
                "            ]" +
                "        }," +
                "        {" +
                "            \"id\": \"302227536\"," +
                "            \"node-type\": \"cloud-region\"," +
                "            \"url\": \"/aai/v14/cloud-infrastructure/cloud-regions/cloud-region/irma-aic/hvf23b\"," +
                "            \"properties\": {" +
                "                \"cloud-owner\": \"irma-aic\"," +
                "                \"cloud-region-id\": \"hvf23b\"," +
                "                \"cloud-type\": \"openstack\"," +
                "                \"owner-defined-type\": \"lcp\"," +
                "                \"cloud-region-version\": \"3.0\"," +
                "                \"identity-url\": \"https://iden.onap.org:5000/v2.0\"," +
                "                \"cloud-zone\": \"z1\"," +
                "                \"complex-name\": \"c1\"," +
                "                \"sriov-automation\": \"false\"," +
                "                \"resource-version\": \"1539855222969\"" +
                "            }," +
                "            \"related-to\": [" +
                "                {" +
                "                    \"id\": \"2744328\"," +
                "                    \"relationship-label\": \"org.onap.relationships.inventory.BelongsTo\"," +
                "                    \"node-type\": \"tenant\"," +
                "                    \"url\": \"/aai/v14/cloud-infrastructure/cloud-regions/cloud-region/irma-aic/hvf23b/tenants/tenant/5452d6bd0cb34e99a3553d349456c642\"" +
                "                }" +
                "            ]" +
                "        }" +
                "    ]" +
                "}";
    }

}
