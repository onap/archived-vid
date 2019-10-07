package org.onap.simulator.presetGenerator.presets.aai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

public class PresetAAIGetNetworkCollectionDetails extends BaseAAIPreset {


    private String serviceInstanceId;

    public PresetAAIGetNetworkCollectionDetails(String serviceInstanceId) {
        this.serviceInstanceId = serviceInstanceId;
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.PUT;
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        Map<String, String> map = super.getRequestHeaders();
        map.put("X-ONAP-PartnerName", "UNKNOWN");
        map.put("X-TransactionId", ".*");
        return map;
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
                "start", ImmutableList.of("nodes/service-instances/service-instance/" + getServiceInstanceId()),
                "query", "query/network-collection-ByServiceInstance"
        );
    }

    public String getServiceInstanceId() {
        return serviceInstanceId;
    }


    @Override
    public Object getResponseBody() {
        return "{\n" +
                "  \"results\": [\n" +
                "    {\n" +
                "      \"service-instance\": {\n" +
                "        \"service-instance-id\": \"2UJZZ01777-rs804s\",\n" +
                "        \"resource-version\": \"1521662813382\",\n" +
                "        \"relationship-list\": {\n" +
                "          \"relationship\": [\n" +
                "            {\n" +
                "              \"related-to\": \"collection\",\n" +
                "              \"relationship-label\": \"org.onap.relationships.inventory.MemberOf\",\n" +
                "              \"related-link\": \"/aai/v13/network/collections/collection/collection-1-2018-rs804s\",\n" +
                "              \"relationship-data\": [\n" +
                "                {\n" +
                "                  \"relationship-key\": \"collection.collection-id\",\n" +
                "                  \"relationship-value\": \"collection-1-2018-rs804s\"\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"collection\": {\n" +
                "        \"collection-id\": \"collection-1-2018-rs804s\",\n" +
                "        \"model-invariant-id\": \"5761e0a7-defj777\",\n" +
                "        \"model-version-id\": \"5761e0a7-defj232\",\n" +
                "        \"collection-name\": \"collection-name\",\n" +
                "        \"collection-type\": \"L3-NETWORK\",\n" +
                "        \"collection-role\": \"SUB-INTERFACE\",\n" +
                "        \"collection-function\": \"collection-function\",\n" +
                "        \"collection-customization-id\": \"custom-unique-data-id\",\n" +
                "        \"relationship-list\": {\n" +
                "          \"relationship\": [\n" +
                "            {\n" +
                "              \"related-to\": \"service-instance\",\n" +
                "              \"relationship-label\": \"org.onap.relationships.inventory.MemberOf\",\n" +
                "              \"related-link\": \"/aai/v13/business/customers/customer/customer-1-2017-rs804s/service-subscriptions/service-subscription/service-value7-rs804s/service-instances/service-instance/2UJZZ01777-rs804s\",\n" +
                "              \"relationship-data\": [\n" +
                "                {\n" +
                "                  \"relationship-key\": \"customer.global-customer-id\",\n" +
                "                  \"relationship-value\": \"customer-1-2017-rs804s\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"relationship-key\": \"service-subscription.service-type\",\n" +
                "                  \"relationship-value\": \"service-value7-rs804s\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"relationship-key\": \"service-instance.service-instance-id\",\n" +
                "                  \"relationship-value\": \"2UJZZ01777-rs804s\"\n" +
                "                }\n" +
                "              ],\n" +
                "              \"related-to-property\": [\n" +
                "                {\n" +
                "                  \"property-key\": \"service-instance.service-instance-name\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"related-to\": \"instance-group\",\n" +
                "              \"relationship-label\": \"org.onap.relationships.inventory.MemberOf\",\n" +
                "              \"related-link\": \"/aai/v13/network/instance-groups/instance-group/instanceGroup-2018-rs804s\",\n" +
                "              \"relationship-data\": [\n" +
                "                {\n" +
                "                  \"relationship-key\": \"instance-group.id\",\n" +
                "                  \"relationship-value\": \"instanceGroup-2018-rs804s\"\n" +
                "                }\n" +
                "              ],\n" +
                "              \"related-to-property\": [\n" +
                "                {\n" +
                "                  \"property-key\": \"instance-group.description\",\n" +
                "                  \"property-value\": \"zr6h\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"property-key\": \"instance-group.instance-group-name\",\n" +
                "                  \"property-value\": \"wKmBXiO1xm8bK\"\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        \"resource-version\": \"1521662811309\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"instance-group\": {\n" +
                "        \"instance-group-role\": \"JZmha7QSS4tJ\",\n" +
                "        \"model-invariant-id\": \"5761e0a7-defj777\",\n" +
                "        \"model-version-id\": \"5761e0a7-defj22\",\n" +
                "        \"id\": \"instanceGroup-2018-rs804s\",\n" +
                "        \"description\": \"zr6h\",\n" +
                "        \"instance-group-type\": \"7DDjOdNL\",\n" +
                "        \"resource-version\": \"1521662814023\",\n" +
                "        \"instance-group-name\": \"wKmBXiO1xm8bK\",\n" +
                "        \"instance-group-function\": \"testfunction2\",\n" +
                "        \"relationship-list\": {\n" +
                "          \"relationship\": [\n" +
                "            {\n" +
                "              \"related-to\": \"l3-network\",\n" +
                "              \"relationship-label\": \"org.onap.relationships.inventory.MemberOf\",\n" +
                "              \"related-link\": \"/aai/v13/network/l3-networks/l3-network/l3network-id-rs804s\",\n" +
                "              \"relationship-data\": [\n" +
                "                {\n" +
                "                  \"relationship-key\": \"l3-network.network-id\",\n" +
                "                  \"relationship-value\": \"l3network-id-rs804s\"\n" +
                "                }\n" +
                "              ],\n" +
                "              \"related-to-property\": [\n" +
                "                {\n" +
                "                  \"property-key\": \"l3-network.network-name\",\n" +
                "                  \"property-value\": \"oam-net\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"related-to\": \"collection\",\n" +
                "              \"relationship-label\": \"org.onap.relationships.inventory.MemberOf\",\n" +
                "              \"related-link\": \"/aai/v13/network/collections/collection/collection-1-2018-rs804s\",\n" +
                "              \"relationship-data\": [\n" +
                "                {\n" +
                "                  \"relationship-key\": \"collection.collection-id\",\n" +
                "                  \"relationship-value\": \"collection-1-2018-rs804s\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"related-to\": \"l3-network\",\n" +
                "              \"relationship-label\": \"org.onap.relationships.inventory.MemberOf\",\n" +
                "              \"related-link\": \"/aai/v13/network/l3-networks/l3-network/l3network-id-3-rs804s\",\n" +
                "              \"relationship-data\": [\n" +
                "                {\n" +
                "                  \"relationship-key\": \"l3-network.network-id\",\n" +
                "                  \"relationship-value\": \"l3network-id-3-rs804s\"\n" +
                "                }\n" +
                "              ],\n" +
                "              \"related-to-property\": [\n" +
                "                {\n" +
                "                  \"property-key\": \"l3-network.network-name\",\n" +
                "                  \"property-value\": \"oam-net\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"related-to\": \"l3-network\",\n" +
                "              \"relationship-label\": \"org.onap.relationships.inventory.MemberOf\",\n" +
                "              \"related-link\": \"/aai/v13/network/l3-networks/l3-network/l3network-id-2-rs804s\",\n" +
                "              \"relationship-data\": [\n" +
                "                {\n" +
                "                  \"relationship-key\": \"l3-network.network-id\",\n" +
                "                  \"relationship-value\": \"l3network-id-2-rs804s\"\n" +
                "                }\n" +
                "              ],\n" +
                "              \"related-to-property\": [\n" +
                "                {\n" +
                "                  \"property-key\": \"l3-network.network-name\",\n" +
                "                  \"property-value\": \"oam-net\"\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"l3-network\": {\n" +
                "        \"network-id\": \"l3network-id-rs804s\",\n" +
                "        \"network-name\": \"oam-net\",\n" +
                "        \"network-type\": \"Tenant_Layer_3\",\n" +
                "        \"network-role\": \"RosemaProtectedOam.OAM\",\n" +
                "        \"network-technology\": \"Contrail\",\n" +
                "        \"is-bound-to-vpn\": false,\n" +
                "        \"resource-version\": \"1521662814627\",\n" +
                "        \"orchestration-status\": \"Created\",\n" +
                "        \"is-provider-network\": false,\n" +
                "        \"is-shared-network\": false,\n" +
                "        \"is-external-network\": false,\n" +
                "        \"relationship-list\": {\n" +
                "          \"relationship\": [\n" +
                "            {\n" +
                "              \"related-to\": \"instance-group\",\n" +
                "              \"relationship-label\": \"org.onap.relationships.inventory.MemberOf\",\n" +
                "              \"related-link\": \"/aai/v13/network/instance-groups/instance-group/instanceGroup-2018-rs804s\",\n" +
                "              \"relationship-data\": [\n" +
                "                {\n" +
                "                  \"relationship-key\": \"instance-group.id\",\n" +
                "                  \"relationship-value\": \"instanceGroup-2018-rs804s\"\n" +
                "                }\n" +
                "              ],\n" +
                "              \"related-to-property\": [\n" +
                "                {\n" +
                "                  \"property-key\": \"instance-group.description\",\n" +
                "                  \"property-value\": \"zr6h\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"property-key\": \"instance-group.instance-group-name\",\n" +
                "                  \"property-value\": \"wKmBXiO1xm8bK\"\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"l3-network\": {\n" +
                "        \"network-id\": \"l3network-id-3-rs804s\",\n" +
                "        \"network-name\": \"oam-net\",\n" +
                "        \"network-type\": \"Tenant_Layer_3\",\n" +
                "        \"network-role\": \"RosemaProtectedOam.OAM\",\n" +
                "        \"network-technology\": \"Contrail\",\n" +
                "        \"is-bound-to-vpn\": false,\n" +
                "        \"resource-version\": \"1521662816043\",\n" +
                "        \"orchestration-status\": \"Created\",\n" +
                "        \"is-provider-network\": false,\n" +
                "        \"is-shared-network\": false,\n" +
                "        \"is-external-network\": false,\n" +
                "        \"relationship-list\": {\n" +
                "          \"relationship\": [\n" +
                "            {\n" +
                "              \"related-to\": \"instance-group\",\n" +
                "              \"relationship-label\": \"org.onap.relationships.inventory.MemberOf\",\n" +
                "              \"related-link\": \"/aai/v13/network/instance-groups/instance-group/instanceGroup-2018-rs804s\",\n" +
                "              \"relationship-data\": [\n" +
                "                {\n" +
                "                  \"relationship-key\": \"instance-group.id\",\n" +
                "                  \"relationship-value\": \"instanceGroup-2018-rs804s\"\n" +
                "                }\n" +
                "              ],\n" +
                "              \"related-to-property\": [\n" +
                "                {\n" +
                "                  \"property-key\": \"instance-group.description\",\n" +
                "                  \"property-value\": \"zr6h\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"property-key\": \"instance-group.instance-group-name\",\n" +
                "                  \"property-value\": \"wKmBXiO1xm8bK\"\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"l3-network\": {\n" +
                "        \"network-id\": \"l3network-id-2-rs804s\",\n" +
                "        \"network-name\": \"oam-net\",\n" +
                "        \"network-type\": \"Tenant_Layer_3\",\n" +
                "        \"network-role\": \"RosemaProtectedOam.OAM\",\n" +
                "        \"network-technology\": \"Contrail\",\n" +
                "        \"is-bound-to-vpn\": false,\n" +
                "        \"resource-version\": \"1521662815304\",\n" +
                "        \"orchestration-status\": \"Created\",\n" +
                "        \"is-provider-network\": false,\n" +
                "        \"is-shared-network\": false,\n" +
                "        \"is-external-network\": false,\n" +
                "        \"relationship-list\": {\n" +
                "          \"relationship\": [\n" +
                "            {\n" +
                "              \"related-to\": \"instance-group\",\n" +
                "              \"relationship-label\": \"org.onap.relationships.inventory.MemberOf\",\n" +
                "              \"related-link\": \"/aai/v13/network/instance-groups/instance-group/instanceGroup-2018-rs804s\",\n" +
                "              \"relationship-data\": [\n" +
                "                {\n" +
                "                  \"relationship-key\": \"instance-group.id\",\n" +
                "                  \"relationship-value\": \"instanceGroup-2018-rs804s\"\n" +
                "                }\n" +
                "              ],\n" +
                "              \"related-to-property\": [\n" +
                "                {\n" +
                "                  \"property-key\": \"instance-group.description\",\n" +
                "                  \"property-value\": \"zr6h\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"property-key\": \"instance-group.instance-group-name\",\n" +
                "                  \"property-value\": \"wKmBXiO1xm8bK\"\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

}
