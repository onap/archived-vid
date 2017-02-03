var exampleServiceModelInfo = {
    modelInvariantId : "SERVICE_INVARIANT_ID_c99-b43e0d88a9a1",
    modelNameVersionId : "SERVICE_NAME_VERSION_ID-4d54-8548-5d0ed23e962b",
    modelName : "SERVICE_NAME_DE220127",
    modelVersion : "SERVICE_VERSION_0.1"
};

var exampleVnfModelInfo = {
    modelInvariantId : "VNF_INVARIANT_ID_c99-b43e0d88a9a1",
    modelNameVersionId : "VNF_NAME_VERSION_ID-4d54-8548-5d0ed23e962b",
    modelName : "VNF_NAME_DE220127",
    modelVersion : "VNF_VERSION_0.1"
};

var exampleVfModuleModelInfo = {
    modelInvariantId : "VF_MODULE_INVARIANT_ID_c99-b43e0d88a9a1",
    modelNameVersionId : "VF_MODULE_NAME_VERSION_ID-4d54-8548-5d0ed23e962b",
    modelName : "VF_MODULE_NAME_DE220127",
    modelVersion : "VF_MODULE_VERSION_0.1"
};

var exampleVolumeGroupModelInfo = {
    modelInvariantId : "VOLUME_GROUP_INVARIANT_ID_c99-b43e0d88a9a1",
    modelNameVersionId : "VOLUME_GROUP_NAME_VERSION_ID-4d54-8548-5d0ed23e962b",
    modelName : "VOLUME_GROUP_NAME_DE220127",
    modelVersion : "VOLUME_GROUP_VERSION_0.1"
};

var exampleAvailableVolumeGroupList = [ "Volume Group Alpha",
	"Volume Group Baker", "Volume Group Charlie" ];

var exampleNetworkModelInfo = {
    modelInvariantId : "NETWORK_INVARIANT_ID_c99-b43e0d88a9a1",
    modelNameVersionId : "NETWORK_NAME_VERSION_ID-4d54-8548-5d0ed23e962b",
    modelName : "NETWORK_NAME_DE220127",
    modelVersion : "NETWORK_VERSION_0.1"
};

var exampleCloudRegionTenantList = [ {
    cloudRegionId : "DEFAULTREGION",
    tenantId : "ebe382f38e414b4dbf45b408c7e5bf9f",
    tenantName : "vMOG-AKRON-123"
}, {
    cloudRegionId : "DEFAULTREGION",
    tenantId : "abcdefhi38e414b4dbf45b408c7e5bf9f",
    tenantName : "tenant 2"
}, {
    cloudRegionId : "DEFAULTREGION",
    tenantId : "rstuvwzyx38e414b4dbf45b408c7e5bf9f",
    tenantName : "tenant 3"
}, {
    cloudRegionId : "cloudR2",
    tenantId : "jklmnop38e414b4dbf45b408c7e5bf9f",
    tenantName : "tenant4"
}, {
    cloudRegionId : "cloudR2",
    tenantId : "qurstuv38e414b4dbf45b408c7e5bf9f",
    tenantName : "tenant 5"
}, {
    cloudRegionId : "cloudR3",
    tenantId : "zyxw38e414b4dbf45b408c7e5bf9f",
    tenantName : "tenant 6"
} ];

var exampleServiceIdList = [ {
    id : "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
    description : "Mobility"
}, {
    id : "e433710f-9217-458d-a79d-1c7aff376d89",
    description : "VIRTUAL USP"
}, {
    id : "c7611ebe-c324-48f1-8085-94aef0c6ef3d",
    description : "HOSTED COMMUNICATIONS"
} ];

var exampleServiceItem = {
    "generic-service" : {
	"service-id" : "415d4d6c-19e8-44ed-bd28-28d41c5b5185",
	"service-name" : "ZRDM1MMSC01",
	"service-type" : "vMMSC Svc Jul 12/vMMSC VOLUME VF new Jul 12 13",
	"service-id" : "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
	"orchestration-status" : "active",
	"in-maint" : false,
	"is-closed-loop-disabled" : false,
	"resource-version" : "1469558690",
	"persona-model-id" : "1fe5c0e1-7ae8-4a3b-bf06-e64b9fa7e7c9",
	"persona-model-version" : "1.0"
    }
};

var exampleVfModuleItem = {
    "generic-vfModule" : {
	"vfModule-id" : "415d4d6c-19e8-44ed-bd28-28d41c5b5185",
	"vfModule-name" : "ZRDM1MMSC01",
	"vfModule-type" : "vMMSC Svc Jul 12/vMMSC VOLUME VF new Jul 12 13",
	"vfModule-id" : "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
	"orchestration-status" : "active",
	"in-maint" : false,
	"is-closed-loop-disabled" : false,
	"resource-version" : "1469558690",
	"persona-model-id" : "1fe5c0e1-7ae8-4a3b-bf06-e64b9fa7e7c9",
	"persona-model-version" : "1.0"
    }
};

var exampleVolumeGroupItem = {
    "generic-volumeGroup" : {
	"volumeGroup-id" : "415d4d6c-19e8-44ed-bd28-28d41c5b5185",
	"volumeGroup-name" : "ZRDM1MMSC01",
	"volumeGroup-type" : "vMMSC Svc Jul 12/vMMSC VOLUME VF new Jul 12 13",
	"volumeGroup-id" : "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
	"orchestration-status" : "active",
	"in-maint" : false,
	"is-closed-loop-disabled" : false,
	"resource-version" : "1469558690",
	"persona-model-id" : "1fe5c0e1-7ae8-4a3b-bf06-e64b9fa7e7c9",
	"persona-model-version" : "1.0"
    }
};

var exampleNetworkItem = {
    "generic-network" : {
	"network-id" : "415d4d6c-19e8-44ed-bd28-28d41c5b5185",
	"network-name" : "ZRDM1MMSC01",
	"network-type" : "vMMSC Svc Jul 12/vMMSC VOLUME VF new Jul 12 13",
	"network-id" : "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
	"orchestration-status" : "active",
	"in-maint" : false,
	"is-closed-loop-disabled" : false,
	"resource-version" : "1469558690",
	"persona-model-id" : "1fe5c0e1-7ae8-4a3b-bf06-e64b9fa7e7c9",
	"persona-model-version" : "1.0"
    }
};

/*
 * Sample query provided by Jimmy on 8/11/16.
 */

var exampleAaiResult = {
    "service-instance" : {
	"service-instance-id" : "mmsc-test-service-instance",
	"resource-version" : "1470921501"
    },
    "extra-properties" : {},
    "inventory-response-items" : {
	"inventory-response-item" : [ {
	    "generic-vnf" : {
		"vnf-id" : "415d4d6c-19e8-44ed-bd28-28d41c5b5185",
		"vnf-name" : "ZRDM1MMSC01",
		"vnf-type" : "vMMSC Svc Jul 12/vMMSC VOLUME VF new Jul 12 13",
		"service-id" : "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
		"orchestration-status" : "active",
		"in-maint" : false,
		"is-closed-loop-disabled" : false,
		"resource-version" : "1469558690",
		"persona-model-id" : "1fe5c0e1-7ae8-4a3b-bf06-e64b9fa7e7c9",
		"persona-model-version" : "1.0"
	    },
	    "extra-properties" : {},
	    "inventory-response-items" : {
		"inventory-response-item" : [ {
		    "vf-module" : {
			"vf-module-id" : "4dd25057-0b2b-4255-b9cd-669443c9b470",
			"vf-module-name" : "ZRDM1MMSC01_base",
			"heat-stack-id" : "ZRDM1MMSC01_base/6261554c-6c34-4604-b91c-68afd148a096",
			"orchestration-status" : "active",
			"is-base-vf-module" : true,
			"resource-version" : "1469559159",
			"persona-model-id" : "6e3b9ac7-cb21-4f39-99ba-f248ba2a597a",
			"persona-model-version" : "1"
		    },
		    "extra-properties" : {},
		    "inventory-response-items" : {
			"inventory-response-item" : [
				{
				    "l3-network" : {
					"network-id" : "b13635a1-5ab8-4dee-9598-768a83eea099",
					"network-name" : "MMS-24413-LAB-vMMSC-01_int_eca_mgmt_net_1",
					"network-type" : "CONTRAIL_BASIC",
					"network-role" : "int_eca_mgmt",
					"network-technology" : "contrail",
					"neutron-network-id" : "07919187-754f-49c2-abb3-9888cfd8ec7d",
					"is-bound-to-vpn" : false,
					"service-id" : "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
					"network-role-instance" : 0,
					"resource-version" : "1469558694",
					"orchestration-status" : "active",
					"heat-stack-id" : "MMS-24413-LAB-vMMSC-01_int_eca_mgmt_net_1/8d34d139-c95f-4d80-86df-d7d13af01067"
				    },
				    "extra-properties" : {},
				    "inventory-response-items" : {}
				},
				{
				    "l3-network" : {
					"network-id" : "44e0d9e0-9334-4ec6-9344-07a96dac629f",
					"network-name" : "dmz_protected_net_0",
					"network-type" : "CONTRAIL_EXTERNAL",
					"network-role" : "dmz_protected",
					"network-technology" : "contrail",
					"neutron-network-id" : "1ad6b2c5-97d0-4298-b016-7e28939e0baf",
					"is-bound-to-vpn" : false,
					"service-id" : "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
					"resource-version" : "1469563787",
					"orchestration-status" : "active",
					"heat-stack-id" : "dmz_protected_net_0/3f93df5c-f4f1-484f-a269-12d3a6b1ec82"
				    },
				    "extra-properties" : {},
				    "inventory-response-items" : {}
				},
				{
				    "l3-network" : {
					"network-id" : "ac49d99b-5daf-4624-9f8e-188b126ea166",
					"network-name" : "cor_direct_net_0",
					"network-type" : "CONTRAIL_BASIC",
					"network-role" : "cor_direct",
					"network-technology" : "contrail",
					"neutron-network-id" : "ac49d99b-5daf-4624-9f8e-188b126ea166",
					"is-bound-to-vpn" : false,
					"service-id" : "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
					"network-role-instance" : 0,
					"resource-version" : "1470846979",
					"orchestration-status" : "active",
					"heat-stack-id" : "",
					"mso-catalog-key" : ""
				    },
				    "extra-properties" : {},
				    "inventory-response-items" : {}
				},
				{
				    "l3-network" : {
					"network-id" : "c597ab81-fece-49f4-a4f5-710cebb13c29",
					"network-name" : "oam_protected_net_0",
					"network-type" : "CONTRAIL_BASIC",
					"network-role" : "oam_protected",
					"network-technology" : "contrail",
					"neutron-network-id" : "c597ab81-fece-49f4-a4f5-710cebb13c29",
					"is-bound-to-vpn" : false,
					"service-id" : "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
					"network-role-instance" : 0,
					"resource-version" : "1470846979",
					"orchestration-status" : "active",
					"heat-stack-id" : "",
					"mso-catalog-key" : ""
				    },
				    "extra-properties" : {},
				    "inventory-response-items" : {}
				} ]
		    }
		} ]
	    }
	} ]
    }
};
