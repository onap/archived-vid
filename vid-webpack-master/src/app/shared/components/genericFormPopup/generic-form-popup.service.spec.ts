import {getTestBed, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {FormBuilder, FormGroup} from '@angular/forms';
import {GenericFormPopupService} from "./generic-form-popup.service";
import {IframeService} from "../../utils/iframe.service";
import {NgRedux} from "@angular-redux/store";
import {ServicePopupService} from "./genericFormServices/service/service.popup.service";
import {BasicControlGenerator} from "../genericForm/formControlsServices/basic.control.generator";
import {AaiService} from "../../services/aaiService/aai.service";
import {ServiceControlGenerator} from "../genericForm/formControlsServices/service.control.generator";
import {GenericFormService} from "../genericForm/generic-form.service";
import {LogService} from "../../utils/log/log.service";
import {DefaultDataGeneratorService} from "../../services/defaultDataServiceGenerator/default.data.generator.service";
import {BasicPopupService} from "./genericFormServices/basic.popup.service";
import {ActivatedRoute} from "@angular/router";
import {UUIDData} from "./generic-form-popup.component";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {NetworkPopupService} from "./genericFormServices/network/network.popup.service";
import {NetworkControlGenerator} from "../genericForm/formControlsServices/networkGenerator/network.control.generator";
import {VfModulePopuopService} from "./genericFormServices/vfModule/vfModule.popuop.service";
import {VfModuleControlGenerator} from "../genericForm/formControlsServices/vfModuleGenerator/vfModule.control.generator";
import {FeatureFlagsService} from "../../services/featureFlag/feature-flags.service";

class MockAppStore<T>{
  getState() {
    return {
      "global": {
        "name": null,
        "flags": {
          "FLAG_NETWORK_TO_ASYNC_INSTANTIATION": false,
          "FLAG_SHOW_ASSIGNMENTS": true,
          "FLAG_FABRIC_CONFIGURATION_ASSIGNMENTS": true,
          "FLAG_UNASSIGN_SERVICE": true,
          "FLAG_SHOW_VERIFY_SERVICE": false,
          "FLAG_COLLECTION_RESOURCE_SUPPORT": true,
          "FLAG_DUPLICATE_VNF": true,
          "FLAG_SERVICE_MODEL_CACHE": true,
          "CREATE_INSTANCE_TEST": false,
          "FLAG_SETTING_DEFAULTS_IN_DRAWING_BOARD": false,
          "FLAG_ASYNC_INSTANTIATION": true,
          "FLAG_ASYNC_JOBS": true,
          "EMPTY_DRAWING_BOARD_TEST": false,
          "FLAG_ADD_MSO_TESTAPI_FIELD": true
        },
        "type": "[FLAGS] Update"
      },
      "service": {
        "serviceHierarchy": {
          "6e59c5de-f052-46fa-aa7e-2fca9d674c44": {
            "service": {
              "uuid": "6e59c5de-f052-46fa-aa7e-2fca9d674c44",
              "invariantUuid": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
              "name": "ComplexService",
              "version": "1.0",
              "toscaModelURL": null,
              "category": "Emanuel",
              "serviceType": "",
              "serviceRole": "",
              "description": "ComplexService",
              "serviceEcompNaming": "false",
              "instantiationType": "Macro",
              "inputs": {}
            },
            "vnfs": {
              "VF_vMee 0": {
                "uuid": "d6557200-ecf2-4641-8094-5393ae3aae60",
                "invariantUuid": "4160458e-f648-4b30-a176-43881ffffe9e",
                "description": "VSP_vMee",
                "name": "VF_vMee",
                "version": "2.0",
                "customizationUuid": "91415b44-753d-494c-926a-456a9172bbb9",
                "inputs": {},
                "commands": {},
                "serviceEcompNaming" : "true",
                "properties": {
                  "ecomp_generated_naming": "false",
                  "max_instances": 10,
                  "min_instances": 1,
                  "gpb2_Internal2_mac": "00:11:22:EF:AC:DF",
                  "sctp-b-ipv6-egress_src_start_port": "0",
                  "sctp-a-ipv6-egress_rule_application": "any",
                  "Internal2_allow_transit": "true",
                  "sctp-b-IPv6_ethertype": "IPv6",
                  "sctp-a-egress_rule_application": "any",
                  "sctp-b-ingress_action": "pass",
                  "sctp-b-ingress_rule_protocol": "icmp",
                  "ncb2_Internal1_mac": "00:11:22:EF:AC:DF",
                  "sctp-b-ipv6-ingress-src_start_port": "0.0",
                  "ncb1_Internal2_mac": "00:11:22:EF:AC:DF",
                  "fsb_volume_size_0": "320.0",
                  "sctp-b-egress_src_addresses": "local",
                  "sctp-a-ipv6-ingress_ethertype": "IPv4",
                  "sctp-a-ipv6-ingress-dst_start_port": "0",
                  "sctp-b-ipv6-ingress_rule_application": "any",
                  "domain_name": "default-domain",
                  "sctp-a-ingress_rule_protocol": "icmp",
                  "sctp-b-egress-src_start_port": "0.0",
                  "sctp-a-egress_src_addresses": "local",
                  "sctp-b-display_name": "epc-sctp-b-ipv4v6-sec-group",
                  "sctp-a-egress-src_start_port": "0.0",
                  "sctp-a-ingress_ethertype": "IPv4",
                  "sctp-b-ipv6-ingress-dst_end_port": "65535",
                  "sctp-b-dst_subnet_prefix_v6": "::",
                  "nf_naming": "{ecomp_generated_naming=true}",
                  "sctp-a-ipv6-ingress_src_subnet_prefix": "0.0.0.0",
                  "sctp-b-egress-dst_start_port": "0.0",
                  "ncb_flavor_name": "nv.c20r64d1",
                  "gpb1_Internal1_mac": "00:11:22:EF:AC:DF",
                  "sctp-b-egress_dst_subnet_prefix_len": "0.0",
                  "Internal2_net_cidr": "10.0.0.10",
                  "sctp-a-ingress-dst_start_port": "0.0",
                  "sctp-a-egress-dst_start_port": "0.0",
                  "fsb1_Internal2_mac": "00:11:22:EF:AC:DF",
                  "sctp-a-egress_ethertype": "IPv4",
                  "vlc_st_service_mode": "in-network-nat",
                  "sctp-a-ipv6-egress_ethertype": "IPv4",
                  "sctp-a-egress-src_end_port": "65535.0",
                  "sctp-b-ipv6-egress_rule_application": "any",
                  "sctp-b-egress_action": "pass",
                  "sctp-a-ingress-src_subnet_prefix_len": "0.0",
                  "sctp-b-ipv6-ingress-src_end_port": "65535.0",
                  "sctp-b-name": "epc-sctp-b-ipv4v6-sec-group",
                  "fsb2_Internal1_mac": "00:11:22:EF:AC:DF",
                  "sctp-a-ipv6-ingress-src_start_port": "0.0",
                  "sctp-b-ipv6-egress_ethertype": "IPv4",
                  "Internal1_net_cidr": "10.0.0.10",
                  "sctp-a-egress_dst_subnet_prefix": "0.0.0.0",
                  "fsb_flavor_name": "nv.c20r64d1",
                  "sctp_rule_protocol": "132",
                  "sctp-b-ipv6-ingress_src_subnet_prefix_len": "0",
                  "sctp-a-ipv6-ingress_rule_application": "any",
                  "sctp-a-IPv6_ethertype": "IPv6",
                  "vlc2_Internal1_mac": "00:11:22:EF:AC:DF",
                  "vlc_st_virtualization_type": "virtual-machine",
                  "sctp-b-ingress-dst_start_port": "0.0",
                  "sctp-b-ingress-dst_end_port": "65535.0",
                  "sctp-a-ipv6-ingress-src_end_port": "65535.0",
                  "sctp-a-display_name": "epc-sctp-a-ipv4v6-sec-group",
                  "sctp-b-ingress_rule_application": "any",
                  "int2_sec_group_name": "int2-sec-group",
                  "vlc_flavor_name": "nd.c16r64d1",
                  "sctp-b-ipv6-egress_src_addresses": "local",
                  "vlc_st_interface_type_int1": "other1",
                  "sctp-b-egress-src_end_port": "65535.0",
                  "sctp-a-ipv6-egress-dst_start_port": "0",
                  "vlc_st_interface_type_int2": "other2",
                  "sctp-a-ipv6-egress_rule_protocol": "any",
                  "Internal2_shared": "false",
                  "sctp-a-ipv6-egress_dst_subnet_prefix_len": "0",
                  "Internal2_rpf": "disable",
                  "vlc1_Internal1_mac": "00:11:22:EF:AC:DF",
                  "sctp-b-ipv6-egress_src_end_port": "65535",
                  "sctp-a-ipv6-egress_src_addresses": "local",
                  "sctp-a-ingress-dst_end_port": "65535.0",
                  "sctp-a-ipv6-egress_src_end_port": "65535",
                  "Internal1_forwarding_mode": "l2",
                  "Internal2_dhcp": "false",
                  "sctp-a-dst_subnet_prefix_v6": "::",
                  "pxe_image_name": "MME_PXE-Boot_16ACP04_GA.qcow2",
                  "vlc_st_interface_type_gtp": "other0",
                  "ncb1_Internal1_mac": "00:11:22:EF:AC:DF",
                  "sctp-b-src_subnet_prefix_v6": "::",
                  "sctp-a-egress_dst_subnet_prefix_len": "0.0",
                  "int1_sec_group_name": "int1-sec-group",
                  "Internal1_dhcp": "false",
                  "sctp-a-ipv6-egress_dst_end_port": "65535",
                  "Internal2_forwarding_mode": "l2",
                  "fsb2_Internal2_mac": "00:11:22:EF:AC:DF",
                  "sctp-b-egress_dst_subnet_prefix": "0.0.0.0",
                  "Internal1_net_cidr_len": "17",
                  "gpb2_Internal1_mac": "00:11:22:EF:AC:DF",
                  "sctp-b-ingress-src_subnet_prefix_len": "0.0",
                  "sctp-a-ingress_dst_addresses": "local",
                  "sctp-a-egress_action": "pass",
                  "fsb_volume_type_0": "SF-Default-SSD",
                  "ncb2_Internal2_mac": "00:11:22:EF:AC:DF",
                  "vlc_st_interface_type_sctp_a": "left",
                  "vlc_st_interface_type_sctp_b": "right",
                  "sctp-a-src_subnet_prefix_v6": "::",
                  "vlc_st_version": "2",
                  "sctp-b-egress_ethertype": "IPv4",
                  "sctp-a-ingress_rule_application": "any",
                  "gpb1_Internal2_mac": "00:11:22:EF:AC:DF",
                  "instance_ip_family_v6": "v6",
                  "sctp-a-ipv6-egress_src_start_port": "0",
                  "sctp-b-ingress-src_start_port": "0.0",
                  "sctp-b-ingress_dst_addresses": "local",
                  "fsb1_Internal1_mac": "00:11:22:EF:AC:DF",
                  "vlc_st_interface_type_oam": "management",
                  "multi_stage_design": "true",
                  "oam_sec_group_name": "oam-sec-group",
                  "Internal2_net_gateway": "10.0.0.10",
                  "sctp-a-ipv6-ingress-dst_end_port": "65535",
                  "sctp-b-ipv6-egress-dst_start_port": "0",
                  "Internal1_net_gateway": "10.0.0.10",
                  "sctp-b-ipv6-egress_rule_protocol": "any",
                  "gtp_sec_group_name": "gtp-sec-group",
                  "sctp-a-ipv6-egress_dst_subnet_prefix": "0.0.0.0",
                  "sctp-b-ipv6-egress_dst_subnet_prefix_len": "0",
                  "sctp-a-ipv6-ingress_dst_addresses": "local",
                  "sctp-a-egress_rule_protocol": "icmp",
                  "sctp-b-ipv6-egress_action": "pass",
                  "sctp-a-ipv6-egress_action": "pass",
                  "Internal1_shared": "false",
                  "sctp-b-ipv6-ingress_rule_protocol": "any",
                  "Internal2_net_cidr_len": "17",
                  "sctp-a-name": "epc-sctp-a-ipv4v6-sec-group",
                  "sctp-a-ingress-src_end_port": "65535.0",
                  "sctp-b-ipv6-ingress_src_subnet_prefix": "0.0.0.0",
                  "sctp-a-egress-dst_end_port": "65535.0",
                  "sctp-a-ingress_action": "pass",
                  "sctp-b-egress_rule_protocol": "icmp",
                  "sctp-b-ipv6-ingress_action": "pass",
                  "vlc_st_service_type": "firewall",
                  "sctp-b-ipv6-egress_dst_end_port": "65535",
                  "sctp-b-ipv6-ingress-dst_start_port": "0",
                  "vlc2_Internal2_mac": "00:11:22:EF:AC:DF",
                  "vlc_st_availability_zone": "true",
                  "fsb_volume_image_name_1": "MME_FSB2_16ACP04_GA.qcow2",
                  "sctp-b-ingress-src_subnet_prefix": "0.0.0.0",
                  "sctp-a-ipv6-ingress_src_subnet_prefix_len": "0",
                  "Internal1_allow_transit": "true",
                  "gpb_flavor_name": "nv.c20r64d1",
                  "availability_zone_max_count": "1",
                  "fsb_volume_image_name_0": "MME_FSB1_16ACP04_GA.qcow2",
                  "sctp-b-ipv6-ingress_dst_addresses": "local",
                  "sctp-b-ipv6-egress_dst_subnet_prefix": "0.0.0.0",
                  "sctp-b-ipv6-ingress_ethertype": "IPv4",
                  "vlc1_Internal2_mac": "00:11:22:EF:AC:DF",
                  "sctp-a-ingress-src_subnet_prefix": "0.0.0.0",
                  "sctp-a-ipv6-ingress_action": "pass",
                  "Internal1_rpf": "disable",
                  "sctp-b-ingress_ethertype": "IPv4",
                  "sctp-b-egress_rule_application": "any",
                  "sctp-b-ingress-src_end_port": "65535.0",
                  "sctp-a-ipv6-ingress_rule_protocol": "any",
                  "sctp-a-ingress-src_start_port": "0.0",
                  "sctp-b-egress-dst_end_port": "65535.0"
                },
                "type": "VF",
                "modelCustomizationName": "VF_vMee 0",
                "vfModules": {
                  "vf_vmee0..VfVmee..vmme_vlc..module-1": {
                    "uuid": "522159d5-d6e0-4c2a-aa44-5a542a12a830",
                    "invariantUuid": "98a7c88b-b577-476a-90e4-e25a5871e02b",
                    "customizationUuid": "55b1be94-671a-403e-a26c-667e9c47d091",
                    "description": null,
                    "name": "VfVmee..vmme_vlc..module-1",
                    "version": "2",
                    "modelCustomizationName": "VfVmee..vmme_vlc..module-1",
                    "properties": {
                      "minCountInstances": 0,
                      "maxCountInstances": null,
                      "initialCount": 0,
                      "vfModuleLabel": "vmme_vlc"
                    },
                    "inputs": {},
                    "volumeGroupAllowed": true
                  },
                  "vf_vmee0..VfVmee..vmme_gpb..module-2": {
                    "uuid": "41708296-e443-4c71-953f-d9a010f059e1",
                    "invariantUuid": "1cca90b8-3490-495e-87da-3f3e4c57d5b9",
                    "customizationUuid": "6add59e0-7fe1-4bc4-af48-f8812422ae7c",
                    "description": null,
                    "name": "VfVmee..vmme_gpb..module-2",
                    "version": "2",
                    "modelCustomizationName": "VfVmee..vmme_gpb..module-2",
                    "properties": {
                      "minCountInstances": 0,
                      "maxCountInstances": null,
                      "initialCount": 0,
                      "vfModuleLabel": "vmme_gpb"
                    },
                    "inputs": {},
                    "volumeGroupAllowed": false
                  },
                  "vf_vmee0..VfVmee..base_vmme..module-0": {
                    "uuid": "a27f5cfc-7f12-4f99-af08-0af9c3885c87",
                    "invariantUuid": "a6f9e51a-2b35-416a-ae15-15e58d61f36d",
                    "customizationUuid": "f8c040f1-7e51-4a11-aca8-acf256cfd861",
                    "description": null,
                    "name": "VfVmee..base_vmme..module-0",
                    "version": "2",
                    "modelCustomizationName": "VfVmee..base_vmme..module-0",
                    "properties": {
                      "minCountInstances": 1,
                      "maxCountInstances": 1,
                      "initialCount": 1,
                      "vfModuleLabel": "base_vmme"
                    },
                    "inputs": {},
                    "volumeGroupAllowed": true
                  }
                },
                "volumeGroups": {
                  "vf_vmee0..VfVmee..base_vmme..module-0": {
                    "uuid": "a27f5cfc-7f12-4f99-af08-0af9c3885c87",
                    "invariantUuid": "a6f9e51a-2b35-416a-ae15-15e58d61f36d",
                    "customizationUuid": "f8c040f1-7e51-4a11-aca8-acf256cfd861",
                    "description": null,
                    "name": "VfVmee..base_vmme..module-0",
                    "version": "2",
                    "modelCustomizationName": "VfVmee..base_vmme..module-0",
                    "properties": {
                      "minCountInstances": 1,
                      "maxCountInstances": 1,
                      "initialCount": 1,
                      "vfModuleLabel": "base_vmme"
                    },
                    "inputs": {}
                  }
                },
                "vfcInstanceGroups": {}
              }
            },
            "networks": {
              "ExtVL 0": {
                "uuid": "ddc3f20c-08b5-40fd-af72-c6d14636b986",
                "invariantUuid": "379f816b-a7aa-422f-be30-17114ff50b7c",
                "description": "ECOMP generic virtual link (network) base type for all other service-level and global networks",
                "name": "ExtVL",
                "version": "37.0",
                "customizationUuid": "94fdd893-4a36-4d70-b16a-ec29c54c184f",
                "inputs": {},
                "commands": {},
                "properties": {
                  "ecomp_generated_naming": "false",
                  "network_assignments": "{is_external_network=false, ipv4_subnet_default_assignment={min_subnets_count=1}, ecomp_generated_network_assignment=false, ipv6_subnet_default_assignment={min_subnets_count=1}}",
                  "exVL_naming": "{ecomp_generated_naming=true}",
                  "network_flows": "{is_network_policy=false, is_bound_to_vpn=false}",
                  "network_homing": "{ecomp_selected_instance_node_target=false}"
                },
                "type": "VL",
                "modelCustomizationName": "ExtVL 0"
              }
            },
            "collectionResource": {},
            "configurations": {
              "Port Mirroring Configuration By Policy 0": {
                "uuid": "b4398538-e89d-4f13-b33d-ca323434ba50",
                "invariantUuid": "6ef0ca40-f366-4897-951f-abd65d25f6f7",
                "description": "A port mirroring configuration by policy object",
                "name": "Port Mirroring Configuration By Policy",
                "version": "27.0",
                "customizationUuid": "3c3b7b8d-8669-4b3b-8664-61970041fad2",
                "inputs": {},
                "commands": {},
                "properties": {},
                "type": "Configuration",
                "modelCustomizationName": "Port Mirroring Configuration By Policy 0",
                "sourceNodes": [],
                "collectorNodes": null,
                "configurationByPolicy": false
              }
            },
            "serviceProxies": {},
            "vfModules": {
              "vf_vmee0..VfVmee..vmme_vlc..module-1": {
                "uuid": "522159d5-d6e0-4c2a-aa44-5a542a12a830",
                "invariantUuid": "98a7c88b-b577-476a-90e4-e25a5871e02b",
                "customizationUuid": "55b1be94-671a-403e-a26c-667e9c47d091",
                "description": null,
                "name": "VfVmee..vmme_vlc..module-1",
                "version": "2",
                "modelCustomizationName": "VfVmee..vmme_vlc..module-1",
                "properties": {
                  "minCountInstances": 0,
                  "maxCountInstances": null,
                  "initialCount": 0,
                  "vfModuleLabel": "vmme_vlc"
                },
                "inputs": {},
                "volumeGroupAllowed": true
              },
              "vf_vmee0..VfVmee..vmme_gpb..module-2": {
                "uuid": "41708296-e443-4c71-953f-d9a010f059e1",
                "invariantUuid": "1cca90b8-3490-495e-87da-3f3e4c57d5b9",
                "customizationUuid": "6add59e0-7fe1-4bc4-af48-f8812422ae7c",
                "description": null,
                "name": "VfVmee..vmme_gpb..module-2",
                "version": "2",
                "modelCustomizationName": "VfVmee..vmme_gpb..module-2",
                "properties": {
                  "minCountInstances": 0,
                  "maxCountInstances": null,
                  "initialCount": 0,
                  "vfModuleLabel": "vmme_gpb"
                },
                "inputs": {},
                "volumeGroupAllowed": false
              },
              "vf_vmee0..VfVmee..base_vmme..module-0": {
                "uuid": "a27f5cfc-7f12-4f99-af08-0af9c3885c87",
                "invariantUuid": "a6f9e51a-2b35-416a-ae15-15e58d61f36d",
                "customizationUuid": "f8c040f1-7e51-4a11-aca8-acf256cfd861",
                "description": null,
                "name": "VfVmee..base_vmme..module-0",
                "version": "2",
                "modelCustomizationName": "VfVmee..base_vmme..module-0",
                "properties": {
                  "minCountInstances": 1,
                  "maxCountInstances": 1,
                  "initialCount": 1,
                  "vfModuleLabel": "base_vmme"
                },
                "inputs": {},
                "volumeGroupAllowed": true
              }
            },
            "volumeGroups": {
              "vf_vmee0..VfVmee..base_vmme..module-0": {
                "uuid": "a27f5cfc-7f12-4f99-af08-0af9c3885c87",
                "invariantUuid": "a6f9e51a-2b35-416a-ae15-15e58d61f36d",
                "customizationUuid": "f8c040f1-7e51-4a11-aca8-acf256cfd861",
                "description": null,
                "name": "VfVmee..base_vmme..module-0",
                "version": "2",
                "modelCustomizationName": "VfVmee..base_vmme..module-0",
                "properties": {
                  "minCountInstances": 1,
                  "maxCountInstances": 1,
                  "initialCount": 1,
                  "vfModuleLabel": "base_vmme"
                },
                "inputs": {}
              }
            },
            "pnfs": {}
          }
        },
        "serviceInstance": {
          "6e59c5de-f052-46fa-aa7e-2fca9d674c44": {
            "networks": {},
            "vnfs": {
              "VF_vMee 0": {
                "rollbackOnFailure": "true",
                "vfModules": {
                  "vf_vmee0..VfVmee..base_vmme..module-0": {
                    "vf_vmee0..VfVmee..base_vmme..module-0vmvzo": {
                      "isMissingData": false,
                      "sdncPreReload": null,
                      "modelInfo": {
                        "modelType": "VFmodule",
                        "modelInvariantId": "a6f9e51a-2b35-416a-ae15-15e58d61f36d",
                        "modelVersionId": "a27f5cfc-7f12-4f99-af08-0af9c3885c87",
                        "modelName": "VfVmee..base_vmme..module-0",
                        "modelVersion": "2",
                        "modelCustomizationId": "f8c040f1-7e51-4a11-aca8-acf256cfd861",
                        "modelCustomizationName": "VfVmee..base_vmme..module-0"
                      },
                      "instanceParams": [
                        {}
                      ],
                      "trackById": "wmtm6sy2uj"
                    }
                  }
                },
                "isMissingData": true,
                "originalName": "VF_vMee 0",
                "vnfStoreKey": "VF_vMee 0",
                "trackById": "p3wk448m5do",
                "uuid": "d6557200-ecf2-4641-8094-5393ae3aae60",
                "productFamilyId": "36b4733a-53f4-4cc8-8ff0-9172e5fc4b8e",
                "lcpCloudRegionId": null,
                "tenantId": null,
                "lineOfBusiness": null,
                "platformName": null,
                "modelInfo": {
                  "modelType": "VF",
                  "modelInvariantId": "4160458e-f648-4b30-a176-43881ffffe9e",
                  "modelVersionId": "6e59c5de-f052-46fa-aa7e-2fca9d674c44",
                  "modelName": "VF_vMee",
                  "modelVersion": "2.0",
                  "modelCustomizationName": "VF_vMee 0"
                }
              }
            },
            "instanceParams": [
              {}
            ],
            "validationCounter": 1,
            "existingNames": {},
            "existingVNFCounterMap": {
              "d6557200-ecf2-4641-8094-5393ae3aae60": 1
            },
            "globalSubscriberId": "e433710f-9217-458d-a79d-1c7aff376d89",
            "subscriptionServiceType": "TYLER SILVIA",
            "owningEntityId": "d61e6f2d-12fa-4cc2-91df-7c244011d6fc",
            "productFamilyId": "36b4733a-53f4-4cc8-8ff0-9172e5fc4b8e",
            "lcpCloudRegionId": "hvf6",
            "tenantId": "229bcdc6eaeb4ca59d55221141d01f8e",
            "aicZoneId": "JAG1",
            "projectName": "x1",
            "rollbackOnFailure": "true",
            "bulkSize": 1,
            "modelInfo": {
              "modelInvariantId": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
              "modelVersionId": "6e59c5de-f052-46fa-aa7e-2fca9d674c44",
              "modelName": "ComplexService",
              "modelVersion": "1.0",
              "uuid": "6e59c5de-f052-46fa-aa7e-2fca9d674c44"
            },
            "isALaCarte": false,
            "name": "ComplexService",
            "version": "1.0",
            "description": "ComplexService",
            "category": "Emanuel",
            "uuid": "6e59c5de-f052-46fa-aa7e-2fca9d674c44",
            "invariantUuid": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
            "serviceType": "",
            "serviceRole": "",
            "isEcompGeneratedNaming": true,
            "isMultiStepDesign": false
          }
        },
        "lcpRegionsAndTenants": {
          "lcpRegionList": [
            {
              "id": "JANET25",
              "name": "JANET25",
              "isPermitted": true
            },
            {
              "id": "hvf6",
              "name": "hvf6",
              "isPermitted": true
            }
          ],
          "lcpRegionsTenantsMap": {
            "JANET25": [
              {
                "id": "092eb9e8e4b7412e8787dd091bc58e86",
                "name": "USP-SIP-IC-24335-T-01",
                "isPermitted": true
              }
            ],
            "hvf6": [
              {
                "id": "bae71557c5bb4d5aac6743a4e5f1d054",
                "name": "AIN Web Tool-15-D-testalexandria",
                "isPermitted": true
              },
              {
                "id": "d0a3e3f2964542259d155a81c41aadc3",
                "name": "test-hvf6-09",
                "isPermitted": true
              },
              {
                "id": "fa45ca53c80b492fa8be5477cd84fc2b",
                "name": "ro-T112",
                "isPermitted": true
              },
              {
                "id": "cbb99fe4ada84631b7baf046b6fd2044",
                "name": "DN5242-Nov16-T3",
                "isPermitted": true
              }
            ]
          }
        },
        "productFamilies": [
          {
            "id": "ebc3bc3d-62fd-4a3f-a037-f619df4ff034",
            "name": "SCOTTIE",
            "isPermitted": true
          },
          {
            "id": "17cc1042-527b-11e6-beb8-9e71128cae77",
            "name": "IGNACIO",
            "isPermitted": true
          },
          {
            "id": "36b4733a-53f4-4cc8-8ff0-9172e5fc4b8e",
            "name": "Christie",
            "isPermitted": true
          },
          {
            "id": "a4f6f2ae-9bf5-4ed7-b904-06b2099c4bd7",
            "name": "Enhanced Services",
            "isPermitted": true
          },
          {
            "id": "vTerrance",
            "name": "vTerrance",
            "isPermitted": true
          },
          {
            "id": "323d69d9-2efe-4r45-ay0a-89ea7ard4e6f",
            "name": "vSCP",
            "isPermitted": true
          },
          {
            "id": "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
            "name": "Emanuel",
            "isPermitted": true
          },
          {
            "id": "d8a6ed93-251c-47ca-adc9-86671fd19f4c",
            "name": "BVOIP",
            "isPermitted": true
          },
          {
            "id": "db171b8f-115c-4992-a2e3-ee04cae357e0",
            "name": "LINDSEY",
            "isPermitted": true
          },
          {
            "id": "LRSI-OSPF",
            "name": "LRSI-OSPF",
            "isPermitted": true
          },
          {
            "id": "vRosemarie",
            "name": "HNGATEWAY",
            "isPermitted": true
          },
          {
            "id": "vHNPaas",
            "name": "WILKINS",
            "isPermitted": true
          },
          {
            "id": "e433710f-9217-458d-a79d-1c7aff376d89",
            "name": "TYLER SILVIA",
            "isPermitted": true
          },
          {
            "id": "b6a3f28c-eebf-494c-a900-055cc7c874ce",
            "name": "VROUTER",
            "isPermitted": true
          },
          {
            "id": "Cisneros",
            "name": "vMuriel",
            "isPermitted": true
          },
          {
            "id": "0ee8c1bc-7cbd-4b0a-a1ac-e9999255abc1",
            "name": "CARA Griffin",
            "isPermitted": true
          },
          {
            "id": "c7611ebe-c324-48f1-8085-94aef0c6ef3d",
            "name": "DARREN MCGEE",
            "isPermitted": true
          },
          {
            "id": "e30755dc-5673-4b6b-9dcf-9abdd96b93d1",
            "name": "Transport",
            "isPermitted": true
          },
          {
            "id": "vSalvatore",
            "name": "vSalvatore",
            "isPermitted": true
          },
          {
            "id": "d7bb0a21-66f2-4e6d-87d9-9ef3ced63ae4",
            "name": "Josefina",
            "isPermitted": true
          },
          {
            "id": "vHubbard",
            "name": "vHubbard",
            "isPermitted": true
          },
          {
            "id": "12a96a9d-4b4c-4349-a950-fe1159602621",
            "name": "DARREN MCGEE",
            "isPermitted": true
          }
        ],
        "serviceTypes": {
          "e433710f-9217-458d-a79d-1c7aff376d89": [
            {
              "id": "0",
              "name": "vRichardson",
              "isPermitted": false
            },
            {
              "id": "1",
              "name": "TYLER SILVIA",
              "isPermitted": true
            },
            {
              "id": "2",
              "name": "Emanuel",
              "isPermitted": false
            },
            {
              "id": "3",
              "name": "vJamie",
              "isPermitted": false
            },
            {
              "id": "4",
              "name": "vVoiceMail",
              "isPermitted": false
            },
            {
              "id": "5",
              "name": "Kennedy",
              "isPermitted": false
            },
            {
              "id": "6",
              "name": "vSEGW",
              "isPermitted": false
            },
            {
              "id": "7",
              "name": "vVM",
              "isPermitted": false
            },
            {
              "id": "8",
              "name": "vOTA",
              "isPermitted": false
            },
            {
              "id": "9",
              "name": "vMME",
              "isPermitted": false
            },
            {
              "id": "10",
              "name": "vMNS",
              "isPermitted": false
            },
            {
              "id": "11",
              "name": "vSCP",
              "isPermitted": false
            },
            {
              "id": "12",
              "name": "VPMS",
              "isPermitted": false
            },
            {
              "id": "13",
              "name": "vMMSC",
              "isPermitted": false
            },
            {
              "id": "14",
              "name": "SSD",
              "isPermitted": false
            },
            {
              "id": "15",
              "name": "vMOG",
              "isPermitted": false
            },
            {
              "id": "16",
              "name": "LINDSEY",
              "isPermitted": false
            },
            {
              "id": "17",
              "name": "JOHANNA_SANTOS",
              "isPermitted": false
            },
            {
              "id": "18",
              "name": "vCarroll",
              "isPermitted": false
            }
          ]
        },
        "aicZones": [
          {
            "id": "NFT1",
            "name": "NFTJSSSS-NFT1"
          },
          {
            "id": "JAG1",
            "name": "YUDFJULP-JAG1"
          },
          {
            "id": "YYY1",
            "name": "UUUAIAAI-YYY1"
          },
          {
            "id": "AVT1",
            "name": "AVTRFLHD-AVT1"
          },
          {
            "id": "ATL34",
            "name": "ATLSANAI-ATL34"
          }
        ],
        "categoryParameters": {
          "owningEntityList": [
            {
              "id": "aaa1",
              "name": "aaa1"
            },
            {
              "id": "d61e6f2d-12fa-4cc2-91df-7c244011d6fc",
              "name": "WayneHolland"
            },
            {
              "id": "Melissa",
              "name": "Melissa"
            }
          ],
          "projectList": [
            {
              "id": "WATKINS",
              "name": "WATKINS"
            },
            {
              "id": "x1",
              "name": "x1"
            },
            {
              "id": "yyy1",
              "name": "yyy1"
            }
          ],
          "lineOfBusinessList": [
            {
              "id": "ONAP",
              "name": "ONAP"
            },
            {
              "id": "zzz1",
              "name": "zzz1"
            }
          ],
          "platformList": [
            {
              "id": "platform",
              "name": "platform"
            },
            {
              "id": "xxx1",
              "name": "xxx1"
            }
          ]
        },
        "type": "[LCP_REGIONS_AND_TENANTS] Update",
        "subscribers": [
          {
            "id": "CAR_2020_ER",
            "name": "CAR_2020_ER",
            "isPermitted": true
          },
          {
            "id": "21014aa2-526b-11e6-beb8-9e71128cae77",
            "name": "JULIO ERICKSON",
            "isPermitted": false
          },
          {
            "id": "DHV1707-TestSubscriber-2",
            "name": "DALE BRIDGES",
            "isPermitted": false
          },
          {
            "id": "DHV1707-TestSubscriber-1",
            "name": "LLOYD BRIDGES",
            "isPermitted": false
          },
          {
            "id": "jimmy-example",
            "name": "JimmyExampleCust-20161102",
            "isPermitted": false
          },
          {
            "id": "jimmy-example2",
            "name": "JimmyExampleCust-20161103",
            "isPermitted": false
          },
          {
            "id": "ERICA5779-TestSub-PWT-102",
            "name": "ERICA5779-TestSub-PWT-102",
            "isPermitted": false
          },
          {
            "id": "ERICA5779-TestSub-PWT-101",
            "name": "ERICA5779-TestSub-PWT-101",
            "isPermitted": false
          },
          {
            "id": "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
            "name": "Emanuel",
            "isPermitted": false
          },
          {
            "id": "ERICA5779-Subscriber-4",
            "name": "ERICA5779-Subscriber-5",
            "isPermitted": false
          },
          {
            "id": "ERICA5779-TestSub-PWT-103",
            "name": "ERICA5779-TestSub-PWT-103",
            "isPermitted": false
          },
          {
            "id": "ERICA5779-Subscriber-2",
            "name": "ERICA5779-Subscriber-2",
            "isPermitted": false
          },
          {
            "id": "e433710f-9217-458d-a79d-1c7aff376d89",
            "name": "SILVIA ROBBINS",
            "isPermitted": true
          },
          {
            "id": "ERICA5779-Subscriber-3",
            "name": "ERICA5779-Subscriber-3",
            "isPermitted": false
          },
          {
            "id": "31739f3e-526b-11e6-beb8-9e71128cae77",
            "name": "CRAIG/ROBERTS",
            "isPermitted": false
          }
        ]
      }
    }
  }
}
class ActivatedRouteMock<T>{
  queryParams() {
    return {
      serviceModelId : '6e59c5de-f052-46fa-aa7e-2fca9d674c44'
    }
  }
}

class MockFeatureFlagsService {}

describe('Generic Form popup Service', () => {
  let injector;
  let service: GenericFormPopupService;
  let httpMock: HttpTestingController;
  let form : FormGroup;
  let fb : FormBuilder;
  let netwokPopupService : NetworkPopupService;
  let vfModulePopupService : VfModulePopuopService;
  let servicePopupService : ServicePopupService;
  let _aaiService : AaiService;
  let _activatedRoute : ActivatedRoute;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [GenericFormPopupService,
        FormBuilder,
        IframeService,
        ServicePopupService,
        BasicControlGenerator,
        ServiceControlGenerator,
        GenericFormService,
        LogService,
        DefaultDataGeneratorService,
        BasicPopupService,
        AaiService,
        NetworkPopupService,
        NetworkControlGenerator,
        VfModulePopuopService,
        VfModuleControlGenerator,
        {provide:FeatureFlagsService, useClass: MockFeatureFlagsService},
        {provide: ActivatedRoute, useClass: ActivatedRouteMock},
        {provide: NgRedux, useClass: MockAppStore}]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(GenericFormPopupService);
    httpMock = injector.get(HttpTestingController);
    fb = injector.get(FormBuilder);
    netwokPopupService = injector.get(NetworkPopupService);
    vfModulePopupService = injector.get(VfModulePopuopService);
    servicePopupService = injector.get(ServicePopupService);
    _aaiService = injector.get(AaiService);
    _activatedRoute = injector.get(ActivatedRoute);

  })().then(done).catch(done.fail));


  test('VL: getGenericFormDetails should return FormPopupDetails', () => {
    let uuidData : UUIDData = <any>{
      type : 'VL',
      serviceId : '6e59c5de-f052-46fa-aa7e-2fca9d674c44',
      networkId : 'ExtVL 0',
      networkStoreKey : 'ExtVL 0',
      popupService : netwokPopupService
    };
    let node : ITreeNode = <any>{data : {} };

    jest.spyOn(uuidData.popupService ,'getGenericFormPopupDetails');
    service.getGenericFormDetails(uuidData, node,true);

    expect(uuidData.popupService.getGenericFormPopupDetails).toHaveBeenCalledTimes(1);
    expect(uuidData.popupService.getGenericFormPopupDetails).toHaveBeenCalledWith(uuidData['serviceId'],
      uuidData['networkId'],
      uuidData['networkStoreKey'],
      node,
      uuidData,
      true
    );
  });

  test('VFmodule: getGenericFormDetails should return FormPopupDetails', () => {
    let uuidData : UUIDData = <any>{
      type : 'VFmodule',
      serviceId : '6e59c5de-f052-46fa-aa7e-2fca9d674c44',
      vnfStoreKey : 'VF_vMee 0',
      vFModuleStoreKey : 'vf_vmee0..VfVmee..vmme_vlc..module-1',
      popupService : vfModulePopupService
    };
    let node : ITreeNode = <any>{data : {}};

    spyOn(uuidData.popupService ,'getGenericFormPopupDetails');
    service.getGenericFormDetails(uuidData, node,true);

    expect(uuidData.popupService.getGenericFormPopupDetails).toHaveBeenCalledTimes(1)
    expect(uuidData.popupService.getGenericFormPopupDetails).toHaveBeenCalledWith(
      uuidData['serviceId'],
      uuidData['vnfStoreKey'],
      uuidData['vFModuleStoreKey'],
      node,
      uuidData,
      true
    );
  });


  test('service: getGenericFormDetails should return FormPopupDetails', () => {
    let uuidData : UUIDData = <any>{
      type : 'service',
      serviceId : '6e59c5de-f052-46fa-aa7e-2fca9d674c44',
      popupService : servicePopupService
    };
    let node : ITreeNode = <any>{data : {}};

    jest.spyOn(uuidData.popupService ,'getGenericFormPopupDetails');
    service.getGenericFormDetails(uuidData, node,true);

    expect(uuidData.popupService.getGenericFormPopupDetails).toHaveBeenCalledTimes(1);
    expect(uuidData.popupService.getGenericFormPopupDetails).toHaveBeenCalledWith(
      uuidData['serviceId'],
      null,
      null,
      node,
      uuidData,
      true
    );
  });


  test('initReduxOnCreateNewService',() => {
    jest.spyOn(_aaiService, 'getServiceModelById');
    service.initReduxOnCreateNewService();
  })
});
