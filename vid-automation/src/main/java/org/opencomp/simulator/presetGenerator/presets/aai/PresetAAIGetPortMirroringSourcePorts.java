package org.opencomp.simulator.presetGenerator.presets.aai;

import com.google.common.collect.ImmutableMap;
import org.opencomp.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PresetAAIGetPortMirroringSourcePorts extends BaseAAIPreset {
    public PresetAAIGetPortMirroringSourcePorts(String configurationId, String interfaceId, String interfaceName, boolean isPortMirrored) {
        this.configurationId = configurationId;
        this.interfaceId = interfaceId;
        this.interfaceName = interfaceName;
        this.isPortMirrored = isPortMirrored;
    }

    private final String configurationId;
    private final String interfaceId;
    private final String interfaceName;
    private final boolean isPortMirrored;

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
                "format", Collections.singletonList("simple"));
    }

    @Override
    public Object getRequestBody() {
        return ImmutableMap.of(
                "start", "/network/configurations/configuration/" + getConfigurationId(),
                "query", "query/pserver-fromConfiguration"
        );
    }

    public String getConfigurationId() {
        return configurationId;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public String getInterfaceName() { return interfaceName; }

    public boolean getIsPortMirrored() { return isPortMirrored; }

    @Override
    public Object getResponseBody() {
        return "{\n" +
                "    \"results\": [\n" +
                "        {\n" +
                "            \"id\": \"4876980240\",\n" +
                "            \"node-type\": \"l-interface\",\n" +
                "            \"url\": \"/aai/v12/cloud-infrastructure/cloud-regions/cloud-region/att-aic/rdm5b/tenants/tenant/460f35aeb53542dc9f77105066483e83/vservers/vserver/15e46e2f-4b98-4e06-9644-f0e6e35cc79a/l-interfaces/l-interface/zrdm5bfprbVLBA005-vlbagent_aff_int_pktmirror_1_port-dr5jhyxva5ib\",\n" +
                "            \"properties\": {\n" +
                "                \"interface-name\": " + doubleQuoteIfNotNull(getInterfaceName()) + ",\n" +
                "                \"selflink\": \"https://network-aic.rdm5b.cci.att.com:9696/v2.0/ports/6de7bf87-6faa-4984-9492-18d1188b3d4a\",\n" +
                "                \"interface-id\": " + doubleQuoteIfNotNull(getInterfaceId()) + ",\n" +
                "                \"macaddr\": \"02:6d:e7:bf:87:6f\",\n" +
                "                \"network-name\": \"APP-C-24595-D-T001-vprobe_int_pktmirror_net_1\",\n" +
                "                \"is-port-mirrored\": " + getIsPortMirrored() + ",\n" +
                "                \"resource-version\": \"1519383879190\",\n" +
                "                \"in-maint\": false,\n" +
                "                \"is-ip-unnumbered\": false\n" +
                "            },\n" +
                "            \"related-to\": [\n" +
                "                {\n" +
                "                    \"id\": \"4999893128\",\n" +
                "                    \"relationship-label\": \"org.onap.relationships.inventory.BelongsTo\",\n" +
                "                    \"node-type\": \"l3-interface-ipv4-address-list\",\n" +
                "                    \"url\": \"/aai/v12/cloud-infrastructure/cloud-regions/cloud-region/att-aic/rdm5b/tenants/tenant/460f35aeb53542dc9f77105066483e83/vservers/vserver/15e46e2f-4b98-4e06-9644-f0e6e35cc79a/l-interfaces/l-interface/zrdm5bfprbVLBA005-vlbagent_aff_int_pktmirror_1_port-dr5jhyxva5ib/l3-interface-ipv4-address-list/107.244.46.254\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"6150074384\",\n" +
                "                    \"relationship-label\": \"org.onap.relationships.inventory.Source\",\n" +
                "                    \"node-type\": \"logical-link\",\n" +
                "                    \"url\": \"/aai/v12/network/logical-links/logical-link/PMC_a22607fb-8392-42f4-bbe7-b8d845a97183_Sourcezrdm5bepdg_147_lb_2_Gn_UntrustedVrf_5_RVMI_Destzrdm5bfprbVLBA005-vlbagent_aff_int_pktmirror_1_port-dr5jhyxva5ib\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"6975434912\",\n" +
                "                    \"relationship-label\": \"tosca.relationships.network.LinksTo\",\n" +
                "                    \"node-type\": \"logical-link\",\n" +
                "                    \"url\": \"/aai/v12/network/logical-links/logical-link/PMC_a22607fb-8392-42f4-bbe7-b8d845a97183_Sourcezrdm5bepdg_147_lb_1_Gn_UntrustedVrf_5_RVMI_Destzrdm5bfprbVLBA005-vlbagent_aff_int_pktmirror_1_port-dr5jhyxva5ib\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"7007121568\",\n" +
                "                    \"relationship-label\": \"tosca.relationships.network.LinksTo\",\n" +
                "                    \"node-type\": \"logical-link\",\n" +
                "                    \"url\": \"/aai/v12/network/logical-links/logical-link/PMC_9c099448-a0e1-451d-ac20-c5e3ada8cccd_Sourcezrdm5bepdg_147_lb_2_Gn_UntrustedVrf_5_RVMI_Destzrdm5bfprbVLBA005-vlbagent_aff_int_pktmirror_1_port-dr5jhyxva5ib\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"7048110232\",\n" +
                "                    \"relationship-label\": \"tosca.relationships.network.LinksTo\",\n" +
                "                    \"node-type\": \"logical-link\",\n" +
                "                    \"url\": \"/aai/v12/network/logical-links/logical-link/PMC_9c099448-a0e1-451d-ac20-c5e3ada8cccd_Sourcezrdm5bepdg_147_lb_1_Gn_UntrustedVrf_5_RVMI_Destzrdm5bfprbVLBA005-vlbagent_aff_int_pktmirror_1_port-dr5jhyxva5ib\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"5491453960\",\n" +
                "                    \"relationship-label\": \"tosca.relationships.network.BindsTo\",\n" +
                "                    \"node-type\": \"vserver\",\n" +
                "                    \"url\": \"/aai/v12/cloud-infrastructure/cloud-regions/cloud-region/att-aic/rdm5b/tenants/tenant/460f35aeb53542dc9f77105066483e83/vservers/vserver/15e46e2f-4b98-4e06-9644-f0e6e35cc79a\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

    private String doubleQuoteIfNotNull(String str) {
        if (str == null) {
            return null;
        } else {
            return "\"" + str + "\"";
        }
    }
}
