package org.onap.simulator.presetGenerator.presets.aai;

public class PresetAAIGetL3NetworksByCloudRegionSpecificState extends AAIBaseGetL3NetworksByCloudRegionPreset {
    private int stateNum = 1;

    public PresetAAIGetL3NetworksByCloudRegionSpecificState(int stateNum) {
        super();
        this.stateNum = stateNum;
    }

    public PresetAAIGetL3NetworksByCloudRegionSpecificState(String cloudOwner, String cloudRegionId, String tenantId) {
        super(tenantId, null, cloudRegionId, cloudOwner, null);
    }

    @Override
    public Object getResponseBody() {
        if (stateNum == 1) {
            return STATE_1;
        } else
            return STATE_2;
    }

    private static final String STATE_1 = "{ "
            + "  \"results\": [{ "
            + "      \"l3-network\": { "
            + "        \"network-id\": \"3b3308d4-0cd3-43e4-9a7b-d1925c861131\", "
            + "        \"network-name\": \"AUK51a_oam_calea_net_0\", "
            + "        \"network-type\": \"SR-IOV-PROVIDER2-0\", "
            + "        \"network-role\": \"oam_calea_net_0\", "
            + "        \"network-technology\": \"STANDARD-SR-IOV\", "
            + "        \"is-bound-to-vpn\": true, "
            + "        \"resource-version\": \"1540925016770\", "
            + "        \"orchestration-status\": \"Active\", "
            + "        \"model-invariant-id\": \"b9a9b549-0ee4-49fc-b4f2-5edc6701da68\", "
            + "        \"model-version-id\": \"77010093-df36-4dcb-8428-c3d02bf3f88d\", "
            + "        \"model-customization-id\": \"e5f33853-f84c-4cdd-99f2-93846957aa18\", "
            + "        \"physical-network-name\": \"sriovnet0\", "
            + "        \"is-provider-network\": true, "
            + "        \"is-shared-network\": true, "
            + "        \"is-external-network\": false, "
            + "        \"selflink\": \"restconf/config/GENERIC-RESOURCE-API:services/service/ddd91e3d-7cd1-4010-958d-94c729937d2d/service-data/networks/network/dbd80094-df1a-4831-8699-a96949dbca5c/network-data/network-topology/\", "
            + "        \"relationship-list\": { "
            + "          \"relationship\": [{ "
            + "              \"related-to\": \"service-instance\", "
            + "              \"relationship-label\": \"org.onap.relationships.inventory.ComposedOf\", "
            + "              \"related-link\": \"/aai/v14/business/customers/customer/a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb/service-subscriptions/service-subscription/Emanuel/service-instances/service-instance/ddd91e3d-7cd1-4010-958d-94c729937d2d\", "
            + "              \"relationship-data\": [{ "
            + "                  \"relationship-key\": \"customer.global-customer-id\", "
            + "                  \"relationship-value\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\" "
            + "                }, { "
            + "                  \"relationship-key\": \"service-subscription.service-type\", "
            + "                  \"relationship-value\": \"Emanuel\" "
            + "                }, { "
            + "                  \"relationship-key\": \"service-instance.service-instance-id\", "
            + "                  \"relationship-value\": \"1c98917b-8255-43c6-98f1-7a2942e75ce1\" "
            + "                } "
            + "              ], "
            + "              \"related-to-property\": [{ "
            + "                  \"property-key\": \"service-instance.service-instance-name\", "
            + "                  \"property-value\": \"AUK51a_oam_calea_net_service_0\" "
            + "                } "
            + "              ] "
            + "            }, { "
            + "              \"related-to\": \"tenant\", "
            + "              \"relationship-label\": \"org.onap.relationships.inventory.Uses\", "
            + "              \"related-link\": \"/aai/v14/cloud-infrastructure/cloud-regions/cloud-region/att-nc/auk51a/tenants/tenant/b530fc990b6d4334bd45518bebca6a51\", "
            + "              \"relationship-data\": [{ "
            + "                  \"relationship-key\": \"cloud-region.cloud-owner\", "
            + "                  \"relationship-value\": \"att-nc\" "
            + "                }, { "
            + "                  \"relationship-key\": \"cloud-region.cloud-region-id\", "
            + "                  \"relationship-value\": \"auk51a\" "
            + "                }, { "
            + "                  \"relationship-key\": \"tenant.tenant-id\", "
            + "                  \"relationship-value\": \"b530fc990b6d4334bd45518bebca6a51\" "
            + "                } "
            + "              ], "
            + "              \"related-to-property\": [{ "
            + "                  \"property-key\": \"tenant.tenant-name\", "
            + "                  \"property-value\": \"ecomp_ispt\" "
            + "                } "
            + "              ] "
            + "            }, { "
            + "              \"related-to\": \"cloud-region\", "
            + "              \"relationship-label\": \"org.onap.relationships.inventory.Uses\", "
            + "              \"related-link\": \"/aai/v14/cloud-infrastructure/cloud-regions/cloud-region/att-nc/auk51a\", "
            + "              \"relationship-data\": [{ "
            + "                  \"relationship-key\": \"cloud-region.cloud-owner\", "
            + "                  \"relationship-value\": \"att-nc\" "
            + "                }, { "
            + "                  \"relationship-key\": \"cloud-region.cloud-region-id\", "
            + "                  \"relationship-value\": \"auk51a\" "
            + "                } "
            + "              ], "
            + "              \"related-to-property\": [{ "
            + "                  \"property-key\": \"cloud-region.owner-defined-type\", "
            + "                  \"property-value\": \"lcp\" "
            + "                } "
            + "              ] "
            + "            }, { "
            + "              \"related-to\": \"vpn-binding\", "
            + "              \"relationship-label\": \"org.onap.relationships.inventory.Uses\", "
            + "              \"related-link\": \"/aai/v14/network/vpn-bindings/vpn-binding/3e7834fb-a8e0-4243-a837-5352ccab4602\", "
            + "              \"relationship-data\": [{ "
            + "                  \"relationship-key\": \"vpn-binding.vpn-id\", "
            + "                  \"relationship-value\": \"3e7834fb-a8e0-4243-a837-5352ccab4602\" "
            + "                } "
            + "              ], "
            + "              \"related-to-property\": [{ "
            + "                  \"property-key\": \"vpn-binding.vpn-name\", "
            + "                  \"property-value\": \"LPPVPN\" "
            + "                }, { "
            + "                  \"property-key\": \"vpn-binding.vpn-type\", "
            + "                  \"property-value\": \"CUSTOMER\" "
            + "                } "
            + "              ] "
            + "            } "
            + "          ] "
            + "        } "
            + "      } "
            + "    }, { "
            + "      \"l3-network\": { "
            + "        \"network-id\": \"b45cdb14-7a80-4ebd-b979-b1d7c7620851\", "
            + "        \"network-name\": \"AUK51a_oam_calea_net_1\", "
            + "        \"network-type\": \"SR-IOV-PROVIDER2-1\", "
            + "        \"network-role\": \"oam_calea_net_1\", "
            + "        \"network-technology\": \"STANDARD-SR-IOV\", "
            + "        \"is-bound-to-vpn\": true, "
            + "        \"resource-version\": \"1540925016770\", "
            + "        \"orchestration-status\": \"Active\", "
            + "        \"model-invariant-id\": \"3b3308d4-0cd3-43e4-9a7b-d1925c861135\", "
            + "        \"model-version-id\": \"77010093-df36-4dcb-8428-c3d02bf3f88d\", "
            + "        \"model-customization-id\": \"3b45cdb14-7a80-4ebd-b979-b1d7c7620851\", "
            + "        \"physical-network-name\": \"sriovnet1\", "
            + "        \"is-provider-network\": true, "
            + "        \"is-shared-network\": true, "
            + "        \"is-external-network\": false, "
            + "        \"selflink\": \"restconf/config/GENERIC-RESOURCE-API:services/service/ee1b756e-3c9c-4ee0-974b-6218f377b20d/service-data/networks/network/fa1d9589-478d-41ea-96e6-39714ddc6aa5/network-data/network-topology/\", "
            + "        \"relationship-list\": { "
            + "          \"relationship\": [{ "
            + "              \"related-to\": \"service-instance\", "
            + "              \"relationship-label\": \"org.onap.relationships.inventory.ComposedOf\", "
            + "              \"related-link\": \"/aai/v14/business/customers/customer/a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb/service-subscriptions/service-subscription/Emanuel/service-instances/service-instance/ee1b756e-3c9c-4ee0-974b-6218f377b20d\", "
            + "              \"relationship-data\": [{ "
            + "                  \"relationship-key\": \"customer.global-customer-id\", "
            + "                  \"relationship-value\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\" "
            + "                }, { "
            + "                  \"relationship-key\": \"service-subscription.service-type\", "
            + "                  \"relationship-value\": \"Emanuel\" "
            + "                }, { "
            + "                  \"relationship-key\": \"service-instance.service-instance-id\", "
            + "                  \"relationship-value\": \"fc21e453-7ff4-438e-bc69-df1f9474b00a\" "
            + "                } "
            + "              ], "
            + "              \"related-to-property\": [{ "
            + "                  \"property-key\": \"service-instance.service-instance-name\", "
            + "                  \"property-value\": \"AUK51a_oam_calea_net_service_1\" "
            + "                } "
            + "              ] "
            + "            }, { "
            + "              \"related-to\": \"tenant\", "
            + "              \"relationship-label\": \"org.onap.relationships.inventory.Uses\", "
            + "              \"related-link\": \"/aai/v14/cloud-infrastructure/cloud-regions/cloud-region/att-nc/auk51a/tenants/tenant/b530fc990b6d4334bd45518bebca6a51\", "
            + "              \"relationship-data\": [{ "
            + "                  \"relationship-key\": \"cloud-region.cloud-owner\", "
            + "                  \"relationship-value\": \"att-nc\" "
            + "                }, { "
            + "                  \"relationship-key\": \"cloud-region.cloud-region-id\", "
            + "                  \"relationship-value\": \"auk51a\" "
            + "                }, { "
            + "                  \"relationship-key\": \"tenant.tenant-id\", "
            + "                  \"relationship-value\": \"b530fc990b6d4334bd45518bebca6a51\" "
            + "                } "
            + "              ], "
            + "              \"related-to-property\": [{ "
            + "                  \"property-key\": \"tenant.tenant-name\", "
            + "                  \"property-value\": \"ecomp_ispt\" "
            + "                } "
            + "              ] "
            + "            }, { "
            + "              \"related-to\": \"cloud-region\", "
            + "              \"relationship-label\": \"org.onap.relationships.inventory.Uses\", "
            + "              \"related-link\": \"/aai/v14/cloud-infrastructure/cloud-regions/cloud-region/att-nc/auk51a\", "
            + "              \"relationship-data\": [{ "
            + "                  \"relationship-key\": \"cloud-region.cloud-owner\", "
            + "                  \"relationship-value\": \"att-nc\" "
            + "                }, { "
            + "                  \"relationship-key\": \"cloud-region.cloud-region-id\", "
            + "                  \"relationship-value\": \"auk51a\" "
            + "                } "
            + "              ], "
            + "              \"related-to-property\": [{ "
            + "                  \"property-key\": \"cloud-region.owner-defined-type\", "
            + "                  \"property-value\": \"lcp\" "
            + "                } "
            + "              ] "
            + "            }, { "
            + "              \"related-to\": \"vpn-binding\", "
            + "              \"relationship-label\": \"org.onap.relationships.inventory.Uses\", "
            + "              \"related-link\": \"/aai/v14/network/vpn-bindings/vpn-binding/3e7834fb-a8e0-4243-a837-5352ccab4602\", "
            + "              \"relationship-data\": [{ "
            + "                  \"relationship-key\": \"vpn-binding.vpn-id\", "
            + "                  \"relationship-value\": \"3e7834fb-a8e0-4243-a837-5352ccab4602\" "
            + "                } "
            + "              ], "
            + "              \"related-to-property\": [{ "
            + "                  \"property-key\": \"vpn-binding.vpn-name\", "
            + "                  \"property-value\": \"LPPVPN\" "
            + "                }, { "
            + "                  \"property-key\": \"vpn-binding.vpn-type\", "
            + "                  \"property-value\": \"CUSTOMER\" "
            + "                } "
            + "              ] "
            + "            } "
            + "          ] "
            + "        } "
            + "      } "
            + "    }, { "
            + "      \"l3-network\": { "
            + "        \"network-id\": \"10a74149-c9d7-4918-bbcf-d5fb9b1799ce\", "
            + "        \"network-name\": \"AUK51a_oam_calea_net_2\", "
            + "        \"network-type\": \"SR-IOV-PROVIDER2-2\", "
            + "        \"network-role\": \"oam_calea_net_2\", "
            + "        \"network-technology\": \"STANDARD-SR-IOV\", "
            + "        \"is-bound-to-vpn\": true, "
            + "        \"resource-version\": \"1540925016770\", "
            + "        \"orchestration-status\": \"Active\", "
            + "        \"model-invariant-id\": \"3b3308d4-0cd3-43e4-9a7b-d1925c861135\", "
            + "        \"model-version-id\": \"77010093-df36-4dcb-8428-c3d02bf3f88d\", "
            + "        \"model-customization-id\": \"10a74149-c9d7-4918-bbcf-d5fb9b1799ce\", "
            + "        \"physical-network-name\": \"sriovnet2\", "
            + "        \"is-provider-network\": true, "
            + "        \"is-shared-network\": true, "
            + "        \"is-external-network\": false, "
            + "        \"selflink\": \"restconf/config/GENERIC-RESOURCE-API:services/service/315ccdb4-5a11-499f-95dc-25ae4be37dad/service-data/networks/network/2d4f97f5-702a-4707-b9e5-aa9d3e35deeb/network-data/network-topology/\", "
            + "        \"relationship-list\": { "
            + "          \"relationship\": [{ "
            + "              \"related-to\": \"service-instance\", "
            + "              \"relationship-label\": \"org.onap.relationships.inventory.ComposedOf\", "
            + "              \"related-link\": \"/aai/v14/business/customers/customer/a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb/service-subscriptions/service-subscription/Emanuel/service-instances/service-instance/315ccdb4-5a11-499f-95dc-25ae4be37dad\", "
            + "              \"relationship-data\": [{ "
            + "                  \"relationship-key\": \"customer.global-customer-id\", "
            + "                  \"relationship-value\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\" "
            + "                }, { "
            + "                  \"relationship-key\": \"service-subscription.service-type\", "
            + "                  \"relationship-value\": \"Emanuel\" "
            + "                }, { "
            + "                  \"relationship-key\": \"service-instance.service-instance-id\", "
            + "                  \"relationship-value\": \"b28d8a84-7d93-4b56-b525-c239c1e780a4\" "
            + "                } "
            + "              ], "
            + "              \"related-to-property\": [{ "
            + "                  \"property-key\": \"service-instance.service-instance-name\", "
            + "                  \"property-value\": \"AUK51a_oam_calea_net_service_2\" "
            + "                } "
            + "              ] "
            + "            }, { "
            + "              \"related-to\": \"tenant\", "
            + "              \"relationship-label\": \"org.onap.relationships.inventory.Uses\", "
            + "              \"related-link\": \"/aai/v14/cloud-infrastructure/cloud-regions/cloud-region/att-nc/auk51a/tenants/tenant/b530fc990b6d4334bd45518bebca6a51\", "
            + "              \"relationship-data\": [{ "
            + "                  \"relationship-key\": \"cloud-region.cloud-owner\", "
            + "                  \"relationship-value\": \"att-nc\" "
            + "                }, { "
            + "                  \"relationship-key\": \"cloud-region.cloud-region-id\", "
            + "                  \"relationship-value\": \"auk51a\" "
            + "                }, { "
            + "                  \"relationship-key\": \"tenant.tenant-id\", "
            + "                  \"relationship-value\": \"b530fc990b6d4334bd45518bebca6a51\" "
            + "                } "
            + "              ], "
            + "              \"related-to-property\": [{ "
            + "                  \"property-key\": \"tenant.tenant-name\", "
            + "                  \"property-value\": \"ecomp_ispt\" "
            + "                } "
            + "              ] "
            + "            }, { "
            + "              \"related-to\": \"cloud-region\", "
            + "              \"relationship-label\": \"org.onap.relationships.inventory.Uses\", "
            + "              \"related-link\": \"/aai/v14/cloud-infrastructure/cloud-regions/cloud-region/att-nc/auk51a\", "
            + "              \"relationship-data\": [{ "
            + "                  \"relationship-key\": \"cloud-region.cloud-owner\", "
            + "                  \"relationship-value\": \"att-nc\" "
            + "                }, { "
            + "                  \"relationship-key\": \"cloud-region.cloud-region-id\", "
            + "                  \"relationship-value\": \"auk51a\" "
            + "                } "
            + "              ], "
            + "              \"related-to-property\": [{ "
            + "                  \"property-key\": \"cloud-region.owner-defined-type\", "
            + "                  \"property-value\": \"lcp\" "
            + "                } "
            + "              ] "
            + "            }, { "
            + "              \"related-to\": \"vpn-binding\", "
            + "              \"relationship-label\": \"org.onap.relationships.inventory.Uses\", "
            + "              \"related-link\": \"/aai/v14/network/vpn-bindings/vpn-binding/3e7834fb-a8e0-4243-a837-5352ccab4602\", "
            + "              \"relationship-data\": [{ "
            + "                  \"relationship-key\": \"vpn-binding.vpn-id\", "
            + "                  \"relationship-value\": \"3e7834fb-a8e0-4243-a837-5352ccab4602\" "
            + "                } "
            + "              ], "
            + "              \"related-to-property\": [{ "
            + "                  \"property-key\": \"vpn-binding.vpn-name\", "
            + "                  \"property-value\": \"LPPVPN\" "
            + "                }, { "
            + "                  \"property-key\": \"vpn-binding.vpn-type\", "
            + "                  \"property-value\": \"CUSTOMER\" "
            + "                } "
            + "              ] "
            + "            } "
            + "          ] "
            + "        } "
            + "      } "
            + "    }, { "
            + "      \"l3-network\": { "
            + "        \"network-id\": \"95380967-0f1c-41e9-a47f-9baa3f90860c\", "
            + "        \"network-name\": \"AUK51a_oam_calea_net_3\", "
            + "        \"network-type\": \"SR-IOV-PROVIDER2-3\", "
            + "        \"network-role\": \"oam_calea_net_3\", "
            + "        \"network-technology\": \"STANDARD-SR-IOV\", "
            + "        \"is-bound-to-vpn\": true, "
            + "        \"resource-version\": \"1540925016770\", "
            + "        \"orchestration-status\": \"Active\", "
            + "        \"model-invariant-id\": \"3b3308d4-0cd3-43e4-9a7b-d1925c861135\", "
            + "        \"model-version-id\": \"77010093-df36-4dcb-8428-c3d02bf3f88d\", "
            + "        \"model-customization-id\": \"95380967-0f1c-41e9-a47f-9baa3f90860c\", "
            + "        \"physical-network-name\": \"sriovnet3\", "
            + "        \"is-provider-network\": true, "
            + "        \"is-shared-network\": true, "
            + "        \"is-external-network\": false, "
            + "        \"selflink\": \"restconf/config/GENERIC-RESOURCE-API:services/service/c44ad6cc-639a-4c6b-a327-583afd656a0d/service-data/networks/network/cf18fbb3-ddcb-4774-bd30-e0e895c0e35e/network-data/network-topology/\", "
            + "        \"relationship-list\": { "
            + "          \"relationship\": [{ "
            + "              \"related-to\": \"service-instance\", "
            + "              \"relationship-label\": \"org.onap.relationships.inventory.ComposedOf\", "
            + "              \"related-link\": \"/aai/v14/business/customers/customer/a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb/service-subscriptions/service-subscription/Emanuel/service-instances/service-instance/c44ad6cc-639a-4c6b-a327-583afd656a0d\", "
            + "              \"relationship-data\": [{ "
            + "                  \"relationship-key\": \"customer.global-customer-id\", "
            + "                  \"relationship-value\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\" "
            + "                }, { "
            + "                  \"relationship-key\": \"service-subscription.service-type\", "
            + "                  \"relationship-value\": \"Emanuel\" "
            + "                }, { "
            + "                  \"relationship-key\": \"service-instance.service-instance-id\", "
            + "                  \"relationship-value\": \"f09bbb55-8942-4621-892f-4690a8e5570a\" "
            + "                } "
            + "              ], "
            + "              \"related-to-property\": [{ "
            + "                  \"property-key\": \"service-instance.service-instance-name\", "
            + "                  \"property-value\": \"AUK51a_oam_calea_net_service_3\" "
            + "                } "
            + "              ] "
            + "            }, { "
            + "              \"related-to\": \"tenant\", "
            + "              \"relationship-label\": \"org.onap.relationships.inventory.Uses\", "
            + "              \"related-link\": \"/aai/v14/cloud-infrastructure/cloud-regions/cloud-region/att-nc/auk51a/tenants/tenant/b530fc990b6d4334bd45518bebca6a51\", "
            + "              \"relationship-data\": [{ "
            + "                  \"relationship-key\": \"cloud-region.cloud-owner\", "
            + "                  \"relationship-value\": \"att-nc\" "
            + "                }, { "
            + "                  \"relationship-key\": \"cloud-region.cloud-region-id\", "
            + "                  \"relationship-value\": \"auk51a\" "
            + "                }, { "
            + "                  \"relationship-key\": \"tenant.tenant-id\", "
            + "                  \"relationship-value\": \"b530fc990b6d4334bd45518bebca6a51\" "
            + "                } "
            + "              ], "
            + "              \"related-to-property\": [{ "
            + "                  \"property-key\": \"tenant.tenant-name\", "
            + "                  \"property-value\": \"ecomp_ispt\" "
            + "                } "
            + "              ] "
            + "            }, { "
            + "              \"related-to\": \"cloud-region\", "
            + "              \"relationship-label\": \"org.onap.relationships.inventory.Uses\", "
            + "              \"related-link\": \"/aai/v14/cloud-infrastructure/cloud-regions/cloud-region/att-nc/auk51a\", "
            + "              \"relationship-data\": [{ "
            + "                  \"relationship-key\": \"cloud-region.cloud-owner\", "
            + "                  \"relationship-value\": \"att-nc\" "
            + "                }, { "
            + "                  \"relationship-key\": \"cloud-region.cloud-region-id\", "
            + "                  \"relationship-value\": \"auk51a\" "
            + "                } "
            + "              ], "
            + "              \"related-to-property\": [{ "
            + "                  \"property-key\": \"cloud-region.owner-defined-type\", "
            + "                  \"property-value\": \"lcp\" "
            + "                } "
            + "              ] "
            + "            }, { "
            + "              \"related-to\": \"vpn-binding\", "
            + "              \"relationship-label\": \"org.onap.relationships.inventory.Uses\", "
            + "              \"related-link\": \"/aai/v14/network/vpn-bindings/vpn-binding/3e7834fb-a8e0-4243-a837-5352ccab4602\", "
            + "              \"relationship-data\": [{ "
            + "                  \"relationship-key\": \"vpn-binding.vpn-id\", "
            + "                  \"relationship-value\": \"3e7834fb-a8e0-4243-a837-5352ccab4602\" "
            + "                } "
            + "              ], "
            + "              \"related-to-property\": [{ "
            + "                  \"property-key\": \"vpn-binding.vpn-name\", "
            + "                  \"property-value\": \"LPPVPN\" "
            + "                }, { "
            + "                  \"property-key\": \"vpn-binding.vpn-type\", "
            + "                  \"property-value\": \"CUSTOMER\" "
            + "                } "
            + "              ] "
            + "            } "
            + "          ] "
            + "        } "
            + "      } "
            + "    }, { "
            + "      \"l3-network\": { "
            + "        \"network-id\": \"3b3308d4-0cd3-43e4-9a7b-d1925c861135\", "
            + "        \"network-name\": \"AUK51a_oam_calea_net_0\", "
            + "        \"network-type\": \"SR-IOV-PROVIDER2-0\", "
            + "        \"network-technology\": \"STANDARD-SR-IOV\", "
            + "        \"is-bound-to-vpn\": true, "
            + "        \"resource-version\": \"1540925016770\", "
            + "        \"orchestration-status\": \"Active\", "
            + "        \"model-invariant-id\": \"3b3308d4-0cd3-43e4-9a7b-d1925c861135\", "
            + "        \"model-version-id\": \"77010093-df36-4dcb-8428-c3d02bf3f88d\", "
            + "        \"model-customization-id\": \"95380967-0f1c-41e9-a47f-9baa3f90860c\", "
            + "        \"physical-network-name\": \"sriovnet0\", "
            + "        \"is-provider-network\": true, "
            + "        \"is-shared-network\": true, "
            + "        \"is-external-network\": false, "
            + "        \"selflink\": \"restconf/config/GENERIC-RESOURCE-API:services/service/74141179-b5b9-4383-aab4-45f5b9f70127/service-data/networks/network/62cecd1b-f4bf-4e16-add9-2b6f5181e595/network-data/network-topology/\", "
            + "        \"relationship-list\": { "
            + "          \"relationship\": [{ "
            + "              \"related-to\": \"service-instance\", "
            + "              \"relationship-label\": \"org.onap.relationships.inventory.ComposedOf\", "
            + "              \"related-link\": \"/aai/v14/business/customers/customer/a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb/service-subscriptions/service-subscription/Emanuel/service-instances/service-instance/74141179-b5b9-4383-aab4-45f5b9f70127\", "
            + "              \"relationship-data\": [{ "
            + "                  \"relationship-key\": \"customer.global-customer-id\", "
            + "                  \"relationship-value\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\" "
            + "                }, { "
            + "                  \"relationship-key\": \"service-subscription.service-type\", "
            + "                  \"relationship-value\": \"Emanuel\" "
            + "                }, { "
            + "                  \"relationship-key\": \"service-instance.service-instance-id\", "
            + "                  \"relationship-value\": \"1c98917b-8255-43c6-98f1-7a2942e75ce1\" "
            + "                } "
            + "              ], "
            + "              \"related-to-property\": [{ "
            + "                  \"property-key\": \"service-instance.service-instance-name\", "
            + "                  \"property-value\": \"AUK51a_oam_calea_net_service_0\" "
            + "                } "
            + "              ] "
            + "            }, { "
            + "              \"related-to\": \"tenant\", "
            + "              \"relationship-label\": \"org.onap.relationships.inventory.Uses\", "
            + "              \"related-link\": \"/aai/v14/cloud-infrastructure/cloud-regions/cloud-region/att-nc/auk51a/tenants/tenant/b530fc990b6d4334bd45518bebca6a51\", "
            + "              \"relationship-data\": [{ "
            + "                  \"relationship-key\": \"cloud-region.cloud-owner\", "
            + "                  \"relationship-value\": \"att-nc\" "
            + "                }, { "
            + "                  \"relationship-key\": \"cloud-region.cloud-region-id\", "
            + "                  \"relationship-value\": \"auk51a\" "
            + "                }, { "
            + "                  \"relationship-key\": \"tenant.tenant-id\", "
            + "                  \"relationship-value\": \"b530fc990b6d4334bd45518bebca6a51\" "
            + "                } "
            + "              ], "
            + "              \"related-to-property\": [{ "
            + "                  \"property-key\": \"tenant.tenant-name\", "
            + "                  \"property-value\": \"ecomp_ispt\" "
            + "                } "
            + "              ] "
            + "            }, { "
            + "              \"related-to\": \"cloud-region\", "
            + "              \"relationship-label\": \"org.onap.relationships.inventory.Uses\", "
            + "              \"related-link\": \"/aai/v14/cloud-infrastructure/cloud-regions/cloud-region/att-nc/auk51a\", "
            + "              \"relationship-data\": [{ "
            + "                  \"relationship-key\": \"cloud-region.cloud-owner\", "
            + "                  \"relationship-value\": \"att-nc\" "
            + "                }, { "
            + "                  \"relationship-key\": \"cloud-region.cloud-region-id\", "
            + "                  \"relationship-value\": \"auk51a\" "
            + "                } "
            + "              ], "
            + "              \"related-to-property\": [{ "
            + "                  \"property-key\": \"cloud-region.owner-defined-type\", "
            + "                  \"property-value\": \"lcp\" "
            + "                } "
            + "              ] "
            + "            }, { "
            + "              \"related-to\": \"vpn-binding\", "
            + "              \"relationship-label\": \"org.onap.relationships.inventory.Uses\", "
            + "              \"related-link\": \"/aai/v14/network/vpn-bindings/vpn-binding/3e7834fb-a8e0-4243-a837-5352ccab4602\", "
            + "              \"relationship-data\": [{ "
            + "                  \"relationship-key\": \"vpn-binding.vpn-id\", "
            + "                  \"relationship-value\": \"3e7834fb-a8e0-4243-a837-5352ccab4602\" "
            + "                } "
            + "              ], "
            + "              \"related-to-property\": [{ "
            + "                  \"property-key\": \"vpn-binding.vpn-name\", "
            + "                  \"property-value\": \"LPPVPN\" "
            + "                }, { "
            + "                  \"property-key\": \"vpn-binding.vpn-type\", "
            + "                  \"property-value\": \"CUSTOMER\" "
            + "                } "
            + "              ] "
            + "            } "
            + "          ] "
            + "        } "
            + "      } "
            + "    } "
            + "  ] "
            + "}";

    private static final String STATE_2 = "{\"results\": [" +
            "   {\"l3-network\":    {" +
            "      \"network-id\": \"CC-l3network_1\"," +
            "      \"is-bound-to-vpn\": false," +
            "      \"resource-version\": \"1556206041065\"," +
            "      \"is-provider-network\": false," +
            "      \"is-shared-network\": false," +
            "      \"is-external-network\": false," +
            "      \"relationship-list\": {\"relationship\": [      {" +
            "         \"related-to\": \"tenant\"," +
            "         \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
            "         \"related-link\": \"/aai/v17/cloud-infrastructure/cloud-regions/cloud-region/cloud-owner-CC-ANAI-528/cloud-region-id-CC-ANAI-528/tenants/tenant/tenant-id-CC-ANAI-528T1\"," +
            "         \"relationship-data\":          [" +
            "                        {" +
            "               \"relationship-key\": \"cloud-region.cloud-owner\"," +
            "               \"relationship-value\": \"cloud-owner-CC-ANAI-528\"" +
            "            }," +
            "                        {" +
            "               \"relationship-key\": \"cloud-region.cloud-region-id\"," +
            "               \"relationship-value\": \"cloud-region-id-CC-ANAI-528\"" +
            "            }," +
            "                        {" +
            "               \"relationship-key\": \"tenant.tenant-id\"," +
            "               \"relationship-value\": \"tenant-id-CC-ANAI-528T1\"" +
            "            }" +
            "         ]," +
            "         \"related-to-property\": [         {" +
            "            \"property-key\": \"tenant.tenant-name\"," +
            "            \"property-value\": \"tenant-name-CC-ANAI-528T1\"" +
            "         }]" +
            "      }]}" +
            "   }}," +
            "   {\"l3-network\":    {" +
            "      \"network-id\": \"CC-l3network_3\"," +
            "      \"network-name\": \"DLLSTXRNDS3\"," +
            "      \"network-role\": \"Backup\"," +
            "      \"is-bound-to-vpn\": true," +
            "      \"resource-version\": \"1556139217403\"," +
            "      \"is-provider-network\": false," +
            "      \"is-shared-network\": false," +
            "      \"is-external-network\": false," +
            "      \"relationship-list\": {\"relationship\":       [" +
            "                  {" +
            "            \"related-to\": \"tenant\"," +
            "            \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
            "            \"related-link\": \"/aai/v17/cloud-infrastructure/cloud-regions/cloud-region/cloud-owner-CC-ANAI-528/cloud-region-id-CC-ANAI-528/tenants/tenant/tenant-id-CC-ANAI-528T1\"," +
            "            \"relationship-data\":             [" +
            "                              {" +
            "                  \"relationship-key\": \"cloud-region.cloud-owner\"," +
            "                  \"relationship-value\": \"cloud-owner-CC-ANAI-528\"" +
            "               }," +
            "                              {" +
            "                  \"relationship-key\": \"cloud-region.cloud-region-id\"," +
            "                  \"relationship-value\": \"cloud-region-id-CC-ANAI-528\"" +
            "               }," +
            "                              {" +
            "                  \"relationship-key\": \"tenant.tenant-id\"," +
            "                  \"relationship-value\": \"tenant-id-CC-ANAI-528T1\"" +
            "               }" +
            "            ]," +
            "            \"related-to-property\": [            {" +
            "               \"property-key\": \"tenant.tenant-name\"," +
            "               \"property-value\": \"tenant-name-CC-ANAI-528T1\"" +
            "            }]" +
            "         }," +
            "                  {" +
            "            \"related-to\": \"tenant\"," +
            "            \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
            "            \"related-link\": \"/aai/v17/cloud-infrastructure/cloud-regions/cloud-region/cloud-owner-CC-ANAI-528/cloud-region-id-CC-ANAI-528/tenants/tenant/tenant-id-CC-ANAI-528-T2\"," +
            "            \"relationship-data\":             [" +
            "                              {" +
            "                  \"relationship-key\": \"cloud-region.cloud-owner\"," +
            "                  \"relationship-value\": \"cloud-owner-CC-ANAI-528\"" +
            "               }," +
            "                              {" +
            "                  \"relationship-key\": \"cloud-region.cloud-region-id\"," +
            "                  \"relationship-value\": \"cloud-region-id-CC-ANAI-528\"" +
            "               }," +
            "                              {" +
            "                  \"relationship-key\": \"tenant.tenant-id\"," +
            "                  \"relationship-value\": \"tenant-id-CC-ANAI-528-T2\"" +
            "               }" +
            "            ]," +
            "            \"related-to-property\": [            {" +
            "               \"property-key\": \"tenant.tenant-name\"," +
            "               \"property-value\": \"tenant-name-CC-ANAI-528T2\"" +
            "            }]" +
            "         }," +
            "                  {" +
            "            \"related-to\": \"tenant\"," +
            "            \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
            "            \"related-link\": \"/aai/v17/cloud-infrastructure/cloud-regions/cloud-region/cloud-owner-CC-ANAI-528/cloud-region-id-CC-ANAI-528/tenants/tenant/tenant-id-CC-ANAI-528T3\"," +
            "            \"relationship-data\":             [" +
            "                              {" +
            "                  \"relationship-key\": \"cloud-region.cloud-owner\"," +
            "                  \"relationship-value\": \"cloud-owner-CC-ANAI-528\"" +
            "               }," +
            "                              {" +
            "                  \"relationship-key\": \"cloud-region.cloud-region-id\"," +
            "                  \"relationship-value\": \"cloud-region-id-CC-ANAI-528\"" +
            "               }," +
            "                              {" +
            "                  \"relationship-key\": \"tenant.tenant-id\"," +
            "                  \"relationship-value\": \"tenant-id-CC-ANAI-528T3\"" +
            "               }" +
            "            ]," +
            "            \"related-to-property\": [            {" +
            "               \"property-key\": \"tenant.tenant-name\"," +
            "               \"property-value\": \"tenant-name-CC-ANAI-528T3\"" +
            "            }]" +
            "         }" +
            "      ]}" +
            "   }}," +
            "   {\"l3-network\":    {" +
            "      \"network-id\": \"l3network2-550-as988q\"," +
            "      \"network-name\": \"CHICGIL01VDBE01_SubIntNtwk043\"," +
            "      \"network-type\": \"K6VD\"," +
            "      \"network-role\": \"X92XE0j\"," +
            "      \"network-technology\": \"ZYbPEnCjX6Oqd\"," +
            "      \"neutron-network-id\": \"fpFvDje\"," +
            "      \"is-bound-to-vpn\": false," +
            "      \"service-id\": \"NJnzYaRlz0Test\"," +
            "      \"network-role-instance\": 162," +
            "      \"resource-version\": \"1556823012443\"," +
            "      \"orchestration-status\": \"active\"," +
            "      \"heat-stack-id\": \"0GmDeg\"," +
            "      \"mso-catalog-key\": \"la5ylhZ5g0D\"," +
            "      \"contrail-network-fqdn\": \"EaeexDk47\"," +
            "      \"model-customization-id\": \"wgmn6PrJ5\"," +
            "      \"widget-model-id\": \"e0yNr\"," +
            "      \"widget-model-version\": \"yKpry3J0VVLn\"," +
            "      \"physical-network-name\": \"wq6OKbZMTY\"," +
            "      \"is-provider-network\": false," +
            "      \"is-shared-network\": false," +
            "      \"is-external-network\": false," +
            "      \"selflink\": \"9xtMu4EPuTi\"," +
            "      \"operational-status\": \"W8aj\"," +
            "      \"is-trunked\": true," +
            "      \"relationship-list\": {\"relationship\":       [" +
            "                  {" +
            "            \"related-to\": \"tenant\"," +
            "            \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
            "            \"related-link\": \"/aai/v17/cloud-infrastructure/cloud-regions/cloud-region/cloud-owner-CC-ANAI-528/cloud-region-id-CC-ANAI-528/tenants/tenant/tenant-id-CC-ANAI-528T1\"," +
            "            \"relationship-data\":             [" +
            "                              {" +
            "                  \"relationship-key\": \"cloud-region.cloud-owner\"," +
            "                  \"relationship-value\": \"cloud-owner-CC-ANAI-528\"" +
            "               }," +
            "                              {" +
            "                  \"relationship-key\": \"cloud-region.cloud-region-id\"," +
            "                  \"relationship-value\": \"cloud-region-id-CC-ANAI-528\"" +
            "               }," +
            "                              {" +
            "                  \"relationship-key\": \"tenant.tenant-id\"," +
            "                  \"relationship-value\": \"tenant-id-CC-ANAI-528T1\"" +
            "               }" +
            "            ]," +
            "            \"related-to-property\": [            {" +
            "               \"property-key\": \"tenant.tenant-name\"," +
            "               \"property-value\": \"tenant-name-CC-ANAI-528T1\"" +
            "            }]" +
            "         }," +
            "                  {" +
            "            \"related-to\": \"configuration\"," +
            "            \"relationship-label\": \"org.onap.relationships.inventory.PartOf\"," +
            "            \"related-link\": \"/aai/v17/network/configurations/configuration/configuration2-550-as988q\"," +
            "            \"relationship-data\": [            {" +
            "               \"relationship-key\": \"configuration.configuration-id\"," +
            "               \"relationship-value\": \"configuration2-550-as988q\"" +
            "            }]" +
            "         }," +
            "                  {" +
            "            \"related-to\": \"configuration\"," +
            "            \"relationship-label\": \"org.onap.relationships.inventory.PartOf\"," +
            "            \"related-link\": \"/aai/v17/network/configurations/configuration/configuration1-550-as988q\"," +
            "            \"relationship-data\": [            {" +
            "               \"relationship-key\": \"configuration.configuration-id\"," +
            "               \"relationship-value\": \"configuration1-550-as988q\"" +
            "            }]" +
            "         }," +
            "                  {" +
            "            \"related-to\": \"vpn-binding\"," +
            "            \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
            "            \"related-link\": \"/aai/v17/network/vpn-bindings/vpn-binding/VPNbinding3-550-as988q\"," +
            "            \"relationship-data\": [            {" +
            "               \"relationship-key\": \"vpn-binding.vpn-id\"," +
            "               \"relationship-value\": \"VPNbinding3-550-as988q\"" +
            "            }]," +
            "            \"related-to-property\":             [" +
            "                              {" +
            "                  \"property-key\": \"vpn-binding.vpn-name\"," +
            "                  \"property-value\": \"SZ2A5L_VPNTest\"" +
            "               }," +
            "                              {" +
            "                  \"property-key\": \"vpn-binding.vpn-type\"," +
            "                  \"property-value\": \"1903e94v\"" +
            "               }" +
            "            ]" +
            "         }" +
            "      ]}" +
            "   }}," +
            "   {\"l3-network\":    {" +
            "      \"network-id\": \"CC-l3network_2\"," +
            "      \"network-name\": \"DLLSTXRNDS2\"," +
            "      \"network-role\": \"Primary\"," +
            "      \"is-bound-to-vpn\": true," +
            "      \"resource-version\": \"1556136783141\"," +
            "      \"is-provider-network\": true," +
            "      \"is-shared-network\": true," +
            "      \"is-external-network\": true," +
            "      \"relationship-list\": {\"relationship\":       [" +
            "                  {" +
            "            \"related-to\": \"tenant\"," +
            "            \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
            "            \"related-link\": \"/aai/v17/cloud-infrastructure/cloud-regions/cloud-region/cloud-owner-CC-ANAI-528/cloud-region-id-CC-ANAI-528/tenants/tenant/tenant-id-CC-ANAI-528T1\"," +
            "            \"relationship-data\":             [" +
            "                              {" +
            "                  \"relationship-key\": \"cloud-region.cloud-owner\"," +
            "                  \"relationship-value\": \"cloud-owner-CC-ANAI-528\"" +
            "               }," +
            "                              {" +
            "                  \"relationship-key\": \"cloud-region.cloud-region-id\"," +
            "                  \"relationship-value\": \"cloud-region-id-CC-ANAI-528\"" +
            "               }," +
            "                              {" +
            "                  \"relationship-key\": \"tenant.tenant-id\"," +
            "                  \"relationship-value\": \"tenant-id-CC-ANAI-528T1\"" +
            "               }" +
            "            ]," +
            "            \"related-to-property\": [            {" +
            "               \"property-key\": \"tenant.tenant-name\"," +
            "               \"property-value\": \"tenant-name-CC-ANAI-528T1\"" +
            "            }]" +
            "         }," +
            "                  {" +
            "            \"related-to\": \"tenant\"," +
            "            \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
            "            \"related-link\": \"/aai/v17/cloud-infrastructure/cloud-regions/cloud-region/cloud-owner-CC-ANAI-528/cloud-region-id-CC-ANAI-528/tenants/tenant/tenant-id-CC-ANAI-528-T2\"," +
            "            \"relationship-data\":             [" +
            "                              {" +
            "                  \"relationship-key\": \"cloud-region.cloud-owner\"," +
            "                  \"relationship-value\": \"cloud-owner-CC-ANAI-528\"" +
            "               }," +
            "                              {" +
            "                  \"relationship-key\": \"cloud-region.cloud-region-id\"," +
            "                  \"relationship-value\": \"cloud-region-id-CC-ANAI-528\"" +
            "               }," +
            "                              {" +
            "                  \"relationship-key\": \"tenant.tenant-id\"," +
            "                  \"relationship-value\": \"tenant-id-CC-ANAI-528-T2\"" +
            "               }" +
            "            ]," +
            "            \"related-to-property\": [            {" +
            "               \"property-key\": \"tenant.tenant-name\"," +
            "               \"property-value\": \"tenant-name-CC-ANAI-528T2\"" +
            "            }]" +
            "         }" +
            "      ]}" +
            "   }}" +
            "]}";




}
