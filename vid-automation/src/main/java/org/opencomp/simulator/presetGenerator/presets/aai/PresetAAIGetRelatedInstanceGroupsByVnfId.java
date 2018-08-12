package org.opencomp.simulator.presetGenerator.presets.aai;

import com.google.common.collect.ImmutableMap;
import org.opencomp.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PresetAAIGetRelatedInstanceGroupsByVnfId extends BaseAAIPreset {

    private String vnfId;


    public String getVnfId() {
        return vnfId;
    }

    public void setVnfId(String vnfId) {
        this.vnfId = vnfId;
    }


    public PresetAAIGetRelatedInstanceGroupsByVnfId(String vnfId){
        this.vnfId = vnfId;
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/network/generic-vnfs/generic-vnf/" + getVnfId();
    }

    @Override
    public Map<String, List> getQueryParams() {
        return ImmutableMap.of(
                "depth", Collections.singletonList("0")
        );
    }

    @Override
    public Object getResponseBody() {
        return "{\n" +
                "  \"vnf-id\": \"vnf-id-3\",\n" +
                "  \"vnf-name\": \"zmtn6nf-code-110\",\n" +
                "  \"vnf-type\": \"vnf-type\",\n" +
                "  \"prov-status\": \"NVTPROV\",\n" +
                "  \"operational-status\": \"out-of-service-path\",\n" +
                "  \"equipment-role\": \"nf-role-1\",\n" +
                "  \"in-maint\": false,\n" +
                "  \"is-closed-loop-disabled\": false,\n" +
                "  \"resource-version\": \"1524162037142\",\n" +
                "  \"model-invariant-id\": \"inv-id-5000\",\n" +
                "  \"model-version-id\": \"ver-id-5000\",\n" +
                "  \"model-customization-id\": \"vnf-customization-uuid-1\",\n" +
                "  \"selflink\": \"restconf/config/GENERIC-RESOURCE-API:services/service/bgb-instance-2/service-data/vnfs/vnf/vnf-id-3/vnf-data/vnf-topology\",\n" +
                "  \"relationship-list\": {\n" +
                "    \"relationship\": [\n" +
                "      {\n" +
                "        \"related-to\": \"instance-group\",\n" +
                "        \"relationship-label\": \"org.onap.relationships.inventory.MemberOf\",\n" +
                "        \"related-link\": \"/aai/v13/network/instance-groups/instance-group/bgb-net-inst-group-1\",\n" +
                "        \"relationship-data\": [\n" +
                "          {\n" +
                "            \"relationship-key\": \"instance-group.id\",\n" +
                "            \"relationship-value\": \"bgb-net-inst-group-1\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"related-to-property\": [\n" +
                "          {\n" +
                "            \"property-key\": \"instance-group.description\",\n" +
                "            \"property-value\": \"test\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"property-key\": \"instance-group.instance-group-name\",\n" +
                "            \"property-value\": \"instance group name\"\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"related-to\": \"instance-group\",\n" +
                "        \"relationship-label\": \"org.onap.relationships.inventory.MemberOf\",\n" +
                "        \"related-link\": \"/aai/v13/network/instance-groups/instance-group/bgb-net-inst-group-2\",\n" +
                "        \"relationship-data\": [\n" +
                "          {\n" +
                "            \"relationship-key\": \"instance-group.id\",\n" +
                "            \"relationship-value\": \"bgb-net-inst-group-2\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"related-to-property\": [\n" +
                "          {\n" +
                "            \"property-key\": \"instance-group.description\",\n" +
                "            \"property-value\": \"test\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"property-key\": \"instance-group.instance-group-name\",\n" +
                "            \"property-value\": \"instance group name\"\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"related-to\": \"availability-zone\",\n" +
                "        \"relationship-label\": \"org.onap.relationships.inventory.Uses\",\n" +
                "        \"related-link\": \"/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/att-aic/mtn6/availability-zones/availability-zone/AZ-MN02\",\n" +
                "        \"relationship-data\": [\n" +
                "          {\n" +
                "            \"relationship-key\": \"cloud-region.cloud-owner\",\n" +
                "            \"relationship-value\": \"att-aic\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"relationship-key\": \"cloud-region.cloud-region-id\",\n" +
                "            \"relationship-value\": \"mtn6\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"relationship-key\": \"availability-zone.availability-zone-name\",\n" +
                "            \"relationship-value\": \"AZ-MN02\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
    }
}
