package org.onap.simulator.presetGenerator.presets.aai;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;

public class PresetAAIGetL3NetworksByCloudRegion extends AAIBaseGetL3NetworksByCloudRegionPreset {

    private static final int NUMBER_OF_VALID_NETWORKS = 3;
    private static final String NETWORK_NAME_TEMPLATE = "AUK51a_oam_calea_net_";
    private static final String NETWORK_TYPE_TEMPLATE = "SR-IOV-PROVIDER2-";
    private static final String NETWORK_PHYSICAL_NAME_TEMPLATE = "sriovnet";
    private static final String NETWORK_MODEL_ID = "77010093-df36-4dcb-8428-c3d02bf3f88d";
    private static final String ACTIVE = "Active";
    private static final String SERVICE_INSTANCE_NAME_TEMPLATE = "AUK51a_oam_calea_net_service_";
    private static final String NETWORK_ROLE_TEMPLATE = "oam_calea_net_";
    private List<Network> networks = new ArrayList<>();


    private void initNetworks() {
        // Valid networks
        for (int i=0; i <= NUMBER_OF_VALID_NETWORKS; i++) {
            networks.add(new Network(
                    NETWORK_NAME_TEMPLATE + i,
                    UUID.randomUUID().toString(),
                    NETWORK_TYPE_TEMPLATE + i,
                    networkRole == null? NETWORK_ROLE_TEMPLATE + i : networkRole,
                    ACTIVE,
                    NETWORK_PHYSICAL_NAME_TEMPLATE + i,
                    NETWORK_MODEL_ID,
                    SERVICE_INSTANCE_NAME_TEMPLATE + i,
                    UUID.randomUUID().toString(),
                    tenantName,
                    cloudRegionId,
                    true
                    ));
        }
        // Not Active
        int i = NUMBER_OF_VALID_NETWORKS + 1;
        networks.add(new Network(
                NETWORK_NAME_TEMPLATE + i,
                UUID.randomUUID().toString(),
                NETWORK_TYPE_TEMPLATE + i,
                networkRole == null? NETWORK_ROLE_TEMPLATE + i : networkRole,
                "Assigned",
                NETWORK_PHYSICAL_NAME_TEMPLATE + i,
                NETWORK_MODEL_ID,
                SERVICE_INSTANCE_NAME_TEMPLATE + i,
                UUID.randomUUID().toString(),
                tenantName,
                cloudRegionId,
                true
        ));
        // Empty name
        i++;
        networks.add(new Network(
                "",
                UUID.randomUUID().toString(),
                NETWORK_TYPE_TEMPLATE + i,
                networkRole == null? NETWORK_ROLE_TEMPLATE + i : networkRole,
                ACTIVE,
                NETWORK_PHYSICAL_NAME_TEMPLATE + i,
                NETWORK_MODEL_ID,
                SERVICE_INSTANCE_NAME_TEMPLATE + i,
                UUID.randomUUID().toString(),
                tenantName,
                cloudRegionId,
                true
        ));
        // No name
        i++;
        networks.add(new Network(
                null,
                UUID.randomUUID().toString(),
                NETWORK_TYPE_TEMPLATE + i,
                networkRole == null? NETWORK_ROLE_TEMPLATE + i : networkRole,
                ACTIVE,
                NETWORK_PHYSICAL_NAME_TEMPLATE + i,
                NETWORK_MODEL_ID,
                SERVICE_INSTANCE_NAME_TEMPLATE + i,
                UUID.randomUUID().toString(),
                tenantName,
                cloudRegionId,
                true
        ));
        // Not related to VPN binding
        i++;
        networks.add(new Network(
                NETWORK_NAME_TEMPLATE + i,
                UUID.randomUUID().toString(),
                NETWORK_TYPE_TEMPLATE + i,
                networkRole == null? NETWORK_ROLE_TEMPLATE + i : networkRole,
                ACTIVE,
                NETWORK_PHYSICAL_NAME_TEMPLATE + i,
                NETWORK_MODEL_ID,
                SERVICE_INSTANCE_NAME_TEMPLATE + i,
                UUID.randomUUID().toString(),
                tenantName,
                cloudRegionId,
                false
        ));
    }

    public PresetAAIGetL3NetworksByCloudRegion(String tenentId, String tenantId, String cloudRegionId, String cloudOwner, String networkRole) {
        super(tenantId, tenantId, cloudRegionId, cloudOwner, networkRole);
        initNetworks();
    }

    public PresetAAIGetL3NetworksByCloudRegion() {
        super();
        initNetworks();
    }

    public PresetAAIGetL3NetworksByCloudRegion(String networkRole) {
        super();
        this.networkRole = networkRole;
        initNetworks();
    }

    public String getActiveNetworksWithNameAndRelatedToVpnBindingAsJsonString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(networks.stream().filter(network -> network.networkName != null && !network.networkName.isEmpty() && network.orchStatus.equals(ACTIVE) && network.isBoundToVpn).collect(Collectors.toList()));
    }

    @Override
    public Object getResponseBody() {
        StringJoiner stringJoiner = new StringJoiner(",", "[", "]");
        for (Network network : networks) {
            stringJoiner.add(network.toString());
        }
        return "{\"results\": " + stringJoiner.toString() + "}";
    }

    private class Network {
        @JsonProperty("instanceName")
        public String networkName;
        @JsonProperty("instanceId")
        public String networkId;
        @JsonProperty("instanceType")
        public String networkType;
        public String role;
        public String orchStatus;
        public String physicalName;
        @JsonProperty("uuid")
        public String modelUUID;
        public String serviceName;
        public String serviceUUID;
        public String tenantName;
        @JsonProperty("lcpCloudRegionId")
        public String region;
        private boolean isBoundToVpn;


        public Network(String networkName, String networkId, String networkType, String role, String orchStatus, String physicalName,
                       String modelUUID, String serviceName, String serviceUUID, String tenantName, String region, boolean isBoundToVpn) {
            this.networkName = networkName;
            this.networkId = networkId;
            this.networkType = networkType;
            this.role = role;
            this.orchStatus = orchStatus;
            this.physicalName = physicalName;
            this.modelUUID = modelUUID;
            this.serviceName = serviceName;
            this.serviceUUID = serviceUUID;
            this.tenantName = tenantName;
            this.region = region;
            this.isBoundToVpn = isBoundToVpn;
        }

        @Override
        public String toString (){
            return  "{\"l3-network\": " +
                    "{\n" +
                    "    \"network-id\": \"" + networkId + "\",\n" +
                    (networkName == null ? "" : "    \"network-name\": \"" + networkName + "\",\n") +
                    "    \"network-type\": \"" + networkType + "\",\n" +
                    "    \"network-role\": \"" + role + "\",\n" +
                    "    \"network-technology\": \"STANDARD-SR-IOV\",\n" +
                    "    \"is-bound-to-vpn\": " + isBoundToVpn + ",\n" +
                    "    \"resource-version\": \"1540925016770\",\n" +
                    "    \"orchestration-status\": \"" + orchStatus + "\",\n" +
                    "    \"model-invariant-id\": \"b9a9b549-0ee4-49fc-b4f2-5edc6701da68\",\n" +
                    "    \"model-version-id\": \"" + modelUUID + "\",\n" +
                    "    \"model-customization-id\": \"e5f33853-f84c-4cdd-99f2-93846957aa18\",\n" +
                    "    \"physical-network-name\": \"" + physicalName + "\",\n" +
                    "    \"is-provider-network\": true,\n" +
                    "    \"is-shared-network\": true,\n" +
                    "    \"is-external-network\": false,\n" +
                    "    \"selflink\": \"restconf/config/GENERIC-RESOURCE-API:services/service/" + serviceUUID + "/service-data/networks/network/" + networkId + "/network-data/network-topology/\",\n" +
                    "    \"relationship-list\": {\n" +
                    "      \"relationship\": [{\n" +
                    "        \"related-to\": \"service-instance\",\n" +
                    "        \"relationship-label\": \"org.onap.relationships.inventory.ComposedOf\",\n" +
                    "        \"related-link\": \"/aai/v14/business/customers/customer/a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb/service-subscriptions/service-subscription/Emanuel/service-instances/service-instance/" + serviceUUID + "\",\n" +
                    "        \"relationship-data\": [{\n" +
                    "          \"relationship-key\": \"customer.global-customer-id\",\n" +
                    "          \"relationship-value\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\"\n" +
                    "        }, {\n" +
                    "          \"relationship-key\": \"service-subscription.service-type\",\n" +
                    "          \"relationship-value\": \"Emanuel\"\n" +
                    "        }, {\n" +
                    "          \"relationship-key\": \"service-instance.service-instance-id\",\n" +
                    "          \"relationship-value\": \"" + serviceUUID + "\"\n" +
                    "        }],\n" +
                    "        \"related-to-property\": [{\n" +
                    "          \"property-key\": \"service-instance.service-instance-name\",\n" +
                    "          \"property-value\": \"" + serviceName + "\"\n" +
                    "        }]\n" +
                    "      }, {\n" +
                    "        \"related-to\": \"tenant\",\n" +
                    "        \"relationship-label\": \"org.onap.relationships.inventory.Uses\",\n" +
                    "        \"related-link\": \"/aai/v14/cloud-infrastructure/cloud-regions/cloud-region/" + cloudOwner + "/" + cloudRegionId + "/tenants/tenant/" + tenantId + "\",\n" +
                    "        \"relationship-data\": [{\n" +
                    "          \"relationship-key\": \"cloud-region.cloud-owner\",\n" +
                    "          \"relationship-value\": \"" + cloudOwner + "\"\n" +
                    "        }, {\n" +
                    "          \"relationship-key\": \"cloud-region.cloud-region-id\",\n" +
                    "          \"relationship-value\": \"" + region + "\"\n" +
                    "        }, {\n" +
                    "          \"relationship-key\": \"tenant.tenant-id\",\n" +
                    "          \"relationship-value\": \"" + tenantId + "\"\n" +
                    "        }],\n" +
                    "        \"related-to-property\": [{\n" +
                    "          \"property-key\": \"tenant.tenant-name\",\n" +
                    "          \"property-value\": \"" + tenantName +"\"\n" +
                    "        }]\n" +
                    "      }, {\n" +
                    "        \"related-to\": \"cloud-region\",\n" +
                    "        \"relationship-label\": \"org.onap.relationships.inventory.Uses\",\n" +
                    "        \"related-link\": \"/aai/v14/cloud-infrastructure/cloud-regions/cloud-region/" + cloudOwner + "/" + cloudRegionId + "\",\n" +
                    "        \"relationship-data\": [{\n" +
                    "          \"relationship-key\": \"cloud-region.cloud-owner\",\n" +
                    "          \"relationship-value\": \"" + cloudOwner + "\"\n" +
                    "        }, {\n" +
                    "          \"relationship-key\": \"cloud-region.cloud-region-id\",\n" +
                    "          \"relationship-value\": \"" + region + "\"\n" +
                    "        }],\n" +
                    "        \"related-to-property\": [{\n" +
                    "          \"property-key\": \"cloud-region.owner-defined-type\",\n" +
                    "          \"property-value\": \"lcp\"\n" +
                    "        }]\n" +
                    "      }, {\n" +
                    "        \"related-to\":\"" + (isBoundToVpn ? "vpn-binding" : "something-else") + "\",\n" +
                    "        \"relationship-label\":\"org.onap.relationships.inventory.Uses\",\n" +
                    "        \"related-link\":\"/aai/v14/network/vpn-bindings/vpn-binding/3e7834fb-a8e0-4243-a837-5352ccab4602\",\n" +
                    "        \"relationship-data\":[{\n" +
                    "          \"relationship-key\":\"vpn-binding.vpn-id\",\n" +
                    "          \"relationship-value\":\"3e7834fb-a8e0-4243-a837-5352ccab4602\"\n" +
                    "        }],\n" +
                    "        \"related-to-property\": [{\n" +
                    "          \"property-key\":\"vpn-binding.vpn-name\",\n" +
                    "          \"property-value\":\"LPPVPN\"\n" +
                    "       }, {\n" +
                    "          \"property-key\":\"vpn-binding.vpn-type\",\n" +
                    "          \"property-value\":\"CUSTOMER\"\n" +
                    "       }]\n" +
                    "      }]\n" +
                    "    }\n" +
                    "    }\n" +
                    "  }";
        }
    }

}
