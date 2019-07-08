import {NetworkPopupService} from "./network/network.popup.service";
import {LogService} from "../../../utils/log/log.service";
import {NgRedux} from "@angular-redux/store";
import {BasicControlGenerator} from "../../genericForm/formControlsServices/basic.control.generator";
import {AaiService} from "../../../services/aaiService/aai.service";
import {HttpClient} from "@angular/common/http";
import {NetworkControlGenerator} from "../../genericForm/formControlsServices/networkGenerator/network.control.generator";
import {GenericFormService} from "../../genericForm/generic-form.service";
import {FormBuilder} from "@angular/forms";
import {IframeService} from "../../../utils/iframe.service";
import {DefaultDataGeneratorService} from "../../../services/defaultDataServiceGenerator/default.data.generator.service";
import {FormControlModel} from "../../../models/formControlModels/formControl.model";
import {BasicPopupService} from "./basic.popup.service";
import {SdcUiServices} from "onap-ui-angular";
import {FeatureFlagsService} from "../../../services/featureFlag/feature-flags.service";
import {getTestBed, TestBed} from "@angular/core/testing";

class MockAppStore<T> {}

class MockModalService<T> {}

class MockFeatureFlagsService {}

class MockReduxStore<T> {
  getState() {
    return {
      "global": {
        "name": null,
        "flags": {
          "FLAG_NETWORK_TO_ASYNC_INSTANTIATION": false,
          "FLAG_SHOW_ASSIGNMENTS": true,
          "FLAG_FABRIC_CONFIGURATION_ASSIGNMENTS": true,
          "FLAG_SHOW_VERIFY_SERVICE": false,
          "FLAG_SERVICE_MODEL_CACHE": true,
          "FLAG_ADVANCED_PORTS_FILTER": true,
          "CREATE_INSTANCE_TEST": false,
          "FLAG_SETTING_DEFAULTS_IN_DRAWING_BOARD": false,
          "FLAG_REGION_ID_FROM_REMOTE": true,
          "EMPTY_DRAWING_BOARD_TEST": false,
          "FLAG_ADD_MSO_TESTAPI_FIELD": true
        },
        "type": "[FLAGS] Update"
      }, "service": {
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
              "VF_vGeraldine 0": {
                "uuid": "d6557200-ecf2-4641-8094-5393ae3aae60",
                "invariantUuid": "4160458e-f648-4b30-a176-43881ffffe9e",
                "description": "VSP_vGeraldine",
                "name": "VF_vGeraldine",
                "version": "2.0",
                "customizationUuid": "91415b44-753d-494c-926a-456a9172bbb9",
                "inputs": {},
                "commands": {},
                "properties": {
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
                  "ecomp_generated_naming": "false",
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
                "modelCustomizationName": "VF_vGeraldine 0",
                "vfModules": {
                  "vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1": {
                    "uuid": "522159d5-d6e0-4c2a-aa44-5a542a12a830",
                    "invariantUuid": "98a7c88b-b577-476a-90e4-e25a5871e02b",
                    "customizationUuid": "55b1be94-671a-403e-a26c-667e9c47d091",
                    "description": null,
                    "name": "VfVgeraldine..vflorence_vlc..module-1",
                    "version": "2",
                    "modelCustomizationName": "VfVgeraldine..vflorence_vlc..module-1",
                    "properties": {
                      "minCountInstances": 0,
                      "maxCountInstances": null,
                      "initialCount": 0,
                      "vfModuleLabel": "vflorence_vlc"
                    },
                    "inputs": {},
                    "volumeGroupAllowed": true
                  },
                  "vf_vgeraldine0..VfVgeraldine..vflorence_gpb..module-2": {
                    "uuid": "41708296-e443-4c71-953f-d9a010f059e1",
                    "invariantUuid": "1cca90b8-3490-495e-87da-3f3e4c57d5b9",
                    "customizationUuid": "6add59e0-7fe1-4bc4-af48-f8812422ae7c",
                    "description": null,
                    "name": "VfVgeraldine..vflorence_gpb..module-2",
                    "version": "2",
                    "modelCustomizationName": "VfVgeraldine..vflorence_gpb..module-2",
                    "properties": {
                      "minCountInstances": 0,
                      "maxCountInstances": null,
                      "initialCount": 0,
                      "vfModuleLabel": "vflorence_gpb"
                    },
                    "inputs": {},
                    "volumeGroupAllowed": false
                  },
                  "vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0": {
                    "uuid": "a27f5cfc-7f12-4f99-af08-0af9c3885c87",
                    "invariantUuid": "a6f9e51a-2b35-416a-ae15-15e58d61f36d",
                    "customizationUuid": "f8c040f1-7e51-4a11-aca8-acf256cfd861",
                    "description": null,
                    "name": "VfVgeraldine..base_vflorence..module-0",
                    "version": "2",
                    "modelCustomizationName": "VfVgeraldine..base_vflorence..module-0",
                    "properties": {
                      "minCountInstances": 1,
                      "maxCountInstances": 1,
                      "initialCount": 1,
                      "vfModuleLabel": "base_vflorence"
                    },
                    "inputs": {},
                    "volumeGroupAllowed": true
                  }
                },
                "volumeGroups": {
                  "vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0": {
                    "uuid": "a27f5cfc-7f12-4f99-af08-0af9c3885c87",
                    "invariantUuid": "a6f9e51a-2b35-416a-ae15-15e58d61f36d",
                    "customizationUuid": "f8c040f1-7e51-4a11-aca8-acf256cfd861",
                    "description": null,
                    "name": "VfVgeraldine..base_vflorence..module-0",
                    "version": "2",
                    "modelCustomizationName": "VfVgeraldine..base_vflorence..module-0",
                    "properties": {
                      "minCountInstances": 1,
                      "maxCountInstances": 1,
                      "initialCount": 1,
                      "vfModuleLabel": "base_vflorence"
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
                  "network_assignments": "{is_external_network=false, ipv4_subnet_default_assignment={min_subnets_count=1}, ecomp_generated_network_assignment=false, ipv6_subnet_default_assignment={min_subnets_count=1}}",
                  "exVL_naming": "{ecomp_generated_naming=true}",
                  "network_flows": "{is_network_policy=false, is_bound_to_vpn=false}",
                  "network_homing": "{ecomp_selected_instance_node_target=false}"
                },
                "type": "VL",
                "modelCustomizationName": "ExtVL 0"
              }
            },
            "collectionResources": {},
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
              "vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1": {
                "uuid": "522159d5-d6e0-4c2a-aa44-5a542a12a830",
                "invariantUuid": "98a7c88b-b577-476a-90e4-e25a5871e02b",
                "customizationUuid": "55b1be94-671a-403e-a26c-667e9c47d091",
                "description": null,
                "name": "VfVgeraldine..vflorence_vlc..module-1",
                "version": "2",
                "modelCustomizationName": "VfVgeraldine..vflorence_vlc..module-1",
                "properties": {
                  "minCountInstances": 0,
                  "maxCountInstances": null,
                  "initialCount": 0,
                  "vfModuleLabel": "vflorence_vlc"
                },
                "inputs": {},
                "volumeGroupAllowed": true
              },
              "vf_vgeraldine0..VfVgeraldine..vflorence_gpb..module-2": {
                "uuid": "41708296-e443-4c71-953f-d9a010f059e1",
                "invariantUuid": "1cca90b8-3490-495e-87da-3f3e4c57d5b9",
                "customizationUuid": "6add59e0-7fe1-4bc4-af48-f8812422ae7c",
                "description": null,
                "name": "VfVgeraldine..vflorence_gpb..module-2",
                "version": "2",
                "modelCustomizationName": "VfVgeraldine..vflorence_gpb..module-2",
                "properties": {
                  "minCountInstances": 0,
                  "maxCountInstances": null,
                  "initialCount": 0,
                  "vfModuleLabel": "vflorence_gpb"
                },
                "inputs": {},
                "volumeGroupAllowed": false
              },
              "vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0": {
                "uuid": "a27f5cfc-7f12-4f99-af08-0af9c3885c87",
                "invariantUuid": "a6f9e51a-2b35-416a-ae15-15e58d61f36d",
                "customizationUuid": "f8c040f1-7e51-4a11-aca8-acf256cfd861",
                "description": null,
                "name": "VfVgeraldine..base_vflorence..module-0",
                "version": "2",
                "modelCustomizationName": "VfVgeraldine..base_vflorence..module-0",
                "properties": {
                  "minCountInstances": 1,
                  "maxCountInstances": 1,
                  "initialCount": 1,
                  "vfModuleLabel": "base_vflorence"
                },
                "inputs": {},
                "volumeGroupAllowed": true
              }
            },
            "volumeGroups": {
              "vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0": {
                "uuid": "a27f5cfc-7f12-4f99-af08-0af9c3885c87",
                "invariantUuid": "a6f9e51a-2b35-416a-ae15-15e58d61f36d",
                "customizationUuid": "f8c040f1-7e51-4a11-aca8-acf256cfd861",
                "description": null,
                "name": "VfVgeraldine..base_vflorence..module-0",
                "version": "2",
                "modelCustomizationName": "VfVgeraldine..base_vflorence..module-0",
                "properties": {
                  "minCountInstances": 1,
                  "maxCountInstances": 1,
                  "initialCount": 1,
                  "vfModuleLabel": "base_vflorence"
                },
                "inputs": {}
              }
            },
            "pnfs": {}
          }, "6b528779-44a3-4472-bdff-9cd15ec93450": {
            "service": {
              "uuid": "6b528779-44a3-4472-bdff-9cd15ec93450",
              "invariantUuid": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
              "name": "action-data",
              "version": "1.0",
              "toscaModelURL": null,
              "category": "",
              "serviceType": "",
              "serviceRole": "",
              "description": "",
              "serviceEcompNaming": "false",
              "instantiationType": "A-La-Carte",
              "inputs": {
                "2017488_pasqualevpe0_ASN": {
                  "type": "string",
                  "description": "AV/PE",
                  "entry_schema": null,
                  "inputProperties": null,
                  "constraints": [],
                  "required": true,
                  "default": "AV_vPE"
                }
              }
            },
            "vnfs": {
              "2017-388_PASQUALE-vPE 1": {
                "uuid": "0903e1c0-8e03-4936-b5c2-260653b96413",
                "invariantUuid": "00beb8f9-6d39-452f-816d-c709b9cbb87d",
                "description": "Name PASQUALE vPE Description The provider edge function for the PASQUALE service supported by the Junipers VMX product Category Router Vendor Juniper Vendor Release Code 17.2 Owners Mary Fragale. Updated 9-25 to use v8.0 of the Juniper Valid 2 VLM",
                "name": "2017-388_PASQUALE-vPE",
                "version": "1.0",
                "customizationUuid": "280dec31-f16d-488b-9668-4aae55d6648a",
                "inputs": {
                  "vnf_config_template_version": {
                    "type": "string",
                    "description": "VPE Software Version",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "17.2"
                  },
                  "bandwidth_units": {
                    "type": "string",
                    "description": "Units of bandwidth",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "Gbps"
                  },
                  "bandwidth": {
                    "type": "string",
                    "description": "Requested VPE bandwidth",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "10"
                  },
                  "AIC_CLLI": {
                    "type": "string",
                    "description": "AIC Site CLLI",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "ATLMY8GA"
                  },
                  "ASN": {
                    "type": "string",
                    "description": "AV/PE",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "AV_vPE"
                  },
                  "vnf_instance_name": {
                    "type": "string",
                    "description": "The hostname assigned to the vpe.",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "mtnj309me6"
                  }
                },
                "commands": {
                  "vnf_config_template_version": {
                    "displayName": "vnf_config_template_version",
                    "command": "get_input",
                    "inputName": "2017488_pasqualevpe0_vnf_config_template_version"
                  },
                  "bandwidth_units": {
                    "displayName": "bandwidth_units",
                    "command": "get_input",
                    "inputName": "pasqualevpe0_bandwidth_units"
                  },
                  "bandwidth": {"displayName": "bandwidth", "command": "get_input", "inputName": "pasqualevpe0_bandwidth"},
                  "AIC_CLLI": {
                    "displayName": "AIC_CLLI",
                    "command": "get_input",
                    "inputName": "2017488_pasqualevpe0_AIC_CLLI"
                  },
                  "ASN": {"displayName": "ASN", "command": "get_input", "inputName": "2017488_pasqualevpe0_ASN"},
                  "vnf_instance_name": {
                    "displayName": "vnf_instance_name",
                    "command": "get_input",
                    "inputName": "2017488_pasqualevpe0_vnf_instance_name"
                  }
                },
                "properties": {
                  "vmxvre_retype": "RE-VMX",
                  "vnf_config_template_version": "get_input:2017488_pasqualevpe0_vnf_config_template_version",
                  "sriov44_net_id": "48d399b3-11ee-48a8-94d2-f0ea94d6be8d",
                  "int_ctl_net_id": "2f323477-6936-4d01-ac53-d849430281d9",
                  "vmxvpfe_sriov41_0_port_mac": "00:11:22:EF:AC:DF",
                  "int_ctl_net_name": "VMX-INTXI",
                  "vmx_int_ctl_prefix": "10.0.0.10",
                  "sriov43_net_id": "da349ca1-6de9-4548-be88-2d88e99bfef5",
                  "sriov42_net_id": "760669ba-013d-4d9b-b0e7-4151fe2e6279",
                  "sriov41_net_id": "25ad52d5-c165-40f8-b3b0-ddfc2373280a",
                  "nf_type": "vPE",
                  "vmxvpfe_int_ctl_ip_1": "10.0.0.10",
                  "is_AVPN_service": "false",
                  "vmx_RSG_name": "vREXI-affinity",
                  "vmx_int_ctl_forwarding": "l2",
                  "vmxvre_oam_ip_0": "10.0.0.10",
                  "vmxvpfe_sriov44_0_port_mac": "00:11:22:EF:AC:DF",
                  "vmxvpfe_sriov41_0_port_vlanstrip": "false",
                  "vmxvpfe_sriov42_0_port_vlanfilter": "4001",
                  "vmxvpfe_sriov44_0_port_unknownunicastallow": "true",
                  "vmxvre_image_name_0": "VRE-ENGINE_17.2-S2.1.qcow2",
                  "vmxvre_instance": "0",
                  "vmxvpfe_sriov43_0_port_mac": "00:11:22:EF:AC:DF",
                  "vmxvre_flavor_name": "ns.c1r16d32.v5",
                  "vmxvpfe_volume_size_0": "40.0",
                  "vmxvpfe_sriov43_0_port_vlanfilter": "4001",
                  "nf_naming": "{ecomp_generated_naming=false}",
                  "nf_naming_code": "Navneet",
                  "vmxvre_name_0": "vREXI",
                  "vmxvpfe_sriov42_0_port_vlanstrip": "false",
                  "vmxvpfe_volume_name_0": "vPFEXI_FBVolume",
                  "vmx_RSG_id": "bd89a33c-13c3-4a04-8fde-1a57eb123141",
                  "vmxvpfe_image_name_0": "VPE_ROUTING-ENGINE_17.2R1-S2.1.qcow2",
                  "vmxvpfe_sriov43_0_port_unknownunicastallow": "true",
                  "vmxvpfe_sriov44_0_port_unknownmulticastallow": "true",
                  "vmxvre_console": "vidconsole",
                  "vmxvpfe_sriov44_0_port_vlanfilter": "4001",
                  "vmxvpfe_sriov42_0_port_mac": "00:11:22:EF:AC:DF",
                  "vmxvpfe_volume_id_0": "47cede15-da2f-4397-a101-aa683220aff3",
                  "vmxvpfe_sriov42_0_port_unknownmulticastallow": "true",
                  "vmxvpfe_sriov44_0_port_vlanstrip": "false",
                  "vf_module_id": "123",
                  "nf_function": "JAI",
                  "vmxvpfe_sriov43_0_port_unknownmulticastallow": "true",
                  "vmxvre_int_ctl_ip_0": "10.0.0.10",
                  "ecomp_generated_naming": "false",
                  "AIC_CLLI": "get_input:2017488_pasqualevpe0_AIC_CLLI",
                  "vnf_name": "mtnj309me6vre",
                  "vmxvpfe_sriov41_0_port_unknownunicastallow": "true",
                  "vmxvre_volume_type_1": "HITACHI",
                  "vmxvpfe_sriov44_0_port_broadcastallow": "true",
                  "vmxvre_volume_type_0": "HITACHI",
                  "vmxvpfe_volume_type_0": "HITACHI",
                  "vmxvpfe_sriov43_0_port_broadcastallow": "true",
                  "bandwidth_units": "get_input:pasqualevpe0_bandwidth_units",
                  "vnf_id": "123",
                  "vmxvre_oam_prefix": "24",
                  "availability_zone_0": "mtpocfo-kvm-az01",
                  "ASN": "get_input:2017488_pasqualevpe0_ASN",
                  "vmxvre_chassis_i2cid": "161",
                  "vmxvpfe_name_0": "vPFEXI",
                  "bandwidth": "get_input:pasqualevpe0_bandwidth",
                  "availability_zone_max_count": "1",
                  "vmxvre_volume_size_0": "45.0",
                  "vmxvre_volume_size_1": "50.0",
                  "vmxvpfe_sriov42_0_port_broadcastallow": "true",
                  "vmxvre_oam_gateway": "10.0.0.10",
                  "vmxvre_volume_name_1": "vREXI_FAVolume",
                  "vmxvre_ore_present": "0",
                  "vmxvre_volume_name_0": "vREXI_FBVolume",
                  "vmxvre_type": "0",
                  "vnf_instance_name": "get_input:2017488_pasqualevpe0_vnf_instance_name",
                  "vmxvpfe_sriov41_0_port_unknownmulticastallow": "true",
                  "oam_net_id": "b95eeb1d-d55d-4827-abb4-8ebb94941429",
                  "vmx_int_ctl_len": "24",
                  "vmxvpfe_sriov43_0_port_vlanstrip": "false",
                  "vmxvpfe_sriov41_0_port_broadcastallow": "true",
                  "vmxvre_volume_id_1": "6e86797e-03cd-4fdc-ba72-2957119c746d",
                  "vmxvpfe_sriov41_0_port_vlanfilter": "4001",
                  "nf_role": "Testing",
                  "vmxvre_volume_id_0": "f4eacb79-f687-4e9d-b760-21847c8bb15a",
                  "vmxvpfe_sriov42_0_port_unknownunicastallow": "true",
                  "vmxvpfe_flavor_name": "ns.c20r16d25.v5"
                },
                "type": "VF",
                "modelCustomizationName": "2017-388_PASQUALE-vPE 1",
                "vfModules": {},
                "volumeGroups": {},
                "vfcInstanceGroups": {}
              }, "2017-388_PASQUALE-vPE 0": {
                "uuid": "afacccf6-397d-45d6-b5ae-94c39734b168",
                "invariantUuid": "72e465fe-71b1-4e7b-b5ed-9496118ff7a8",
                "description": "Name PASQUALE vPE Description The provider edge function for the PASQUALE service supported by the Junipers VMX product Category Router Vendor Juniper Vendor Release Code 17.2 Owners Mary Fragale. Updated 9-25 to use v8.0 of the Juniper Valid 2 VLM",
                "name": "2017-388_PASQUALE-vPE",
                "version": "4.0",
                "customizationUuid": "b3c76f73-eeb5-4fb6-9d31-72a889f1811c",
                "inputs": {
                  "vnf_config_template_version": {
                    "type": "string",
                    "description": "VPE Software Version",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "17.2"
                  },
                  "bandwidth_units": {
                    "type": "string",
                    "description": "Units of bandwidth",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "Gbps"
                  },
                  "bandwidth": {
                    "type": "string",
                    "description": "Requested VPE bandwidth",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "10"
                  },
                  "AIC_CLLI": {
                    "type": "string",
                    "description": "AIC Site CLLI",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "ATLMY8GA"
                  },
                  "ASN": {
                    "type": "string",
                    "description": "AV/PE",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "AV_vPE"
                  },
                  "vnf_instance_name": {
                    "type": "string",
                    "description": "The hostname assigned to the vpe.",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "mtnj309me6"
                  }
                },
                "commands": {
                  "vnf_config_template_version": {
                    "displayName": "vnf_config_template_version",
                    "command": "get_input",
                    "inputName": "2017488_pasqualevpe0_vnf_config_template_version"
                  },
                  "bandwidth_units": {
                    "displayName": "bandwidth_units",
                    "command": "get_input",
                    "inputName": "pasqualevpe0_bandwidth_units"
                  },
                  "bandwidth": {"displayName": "bandwidth", "command": "get_input", "inputName": "pasqualevpe0_bandwidth"},
                  "AIC_CLLI": {
                    "displayName": "AIC_CLLI",
                    "command": "get_input",
                    "inputName": "2017488_pasqualevpe0_AIC_CLLI"
                  },
                  "ASN": {"displayName": "ASN", "command": "get_input", "inputName": "2017488_pasqualevpe0_ASN"},
                  "vnf_instance_name": {
                    "displayName": "vnf_instance_name",
                    "command": "get_input",
                    "inputName": "2017488_pasqualevpe0_vnf_instance_name"
                  }
                },
                "properties": {
                  "vmxvre_retype": "RE-VMX",
                  "vnf_config_template_version": "get_input:2017488_pasqualevpe0_vnf_config_template_version",
                  "sriov44_net_id": "48d399b3-11ee-48a8-94d2-f0ea94d6be8d",
                  "int_ctl_net_id": "2f323477-6936-4d01-ac53-d849430281d9",
                  "vmxvpfe_sriov41_0_port_mac": "00:11:22:EF:AC:DF",
                  "int_ctl_net_name": "VMX-INTXI",
                  "vmx_int_ctl_prefix": "10.0.0.10",
                  "sriov43_net_id": "da349ca1-6de9-4548-be88-2d88e99bfef5",
                  "sriov42_net_id": "760669ba-013d-4d9b-b0e7-4151fe2e6279",
                  "sriov41_net_id": "25ad52d5-c165-40f8-b3b0-ddfc2373280a",
                  "nf_type": "vPE",
                  "vmxvpfe_int_ctl_ip_1": "10.0.0.10",
                  "is_AVPN_service": "false",
                  "vmx_RSG_name": "vREXI-affinity",
                  "vmx_int_ctl_forwarding": "l2",
                  "vmxvre_oam_ip_0": "10.0.0.10",
                  "vmxvpfe_sriov44_0_port_mac": "00:11:22:EF:AC:DF",
                  "vmxvpfe_sriov41_0_port_vlanstrip": "false",
                  "vmxvpfe_sriov42_0_port_vlanfilter": "4001",
                  "vmxvpfe_sriov44_0_port_unknownunicastallow": "true",
                  "vmxvre_image_name_0": "VRE-ENGINE_17.2-S2.1.qcow2",
                  "vmxvre_instance": "0",
                  "vmxvpfe_sriov43_0_port_mac": "00:11:22:EF:AC:DF",
                  "vmxvre_flavor_name": "ns.c1r16d32.v5",
                  "vmxvpfe_volume_size_0": "40.0",
                  "vmxvpfe_sriov43_0_port_vlanfilter": "4001",
                  "nf_naming": "{ecomp_generated_naming=false}",
                  "nf_naming_code": "Navneet",
                  "vmxvre_name_0": "vREXI",
                  "vmxvpfe_sriov42_0_port_vlanstrip": "false",
                  "vmxvpfe_volume_name_0": "vPFEXI_FBVolume",
                  "vmx_RSG_id": "bd89a33c-13c3-4a04-8fde-1a57eb123141",
                  "vmxvpfe_image_name_0": "VPE_ROUTING-ENGINE_17.2R1-S2.1.qcow2",
                  "vmxvpfe_sriov43_0_port_unknownunicastallow": "true",
                  "vmxvpfe_sriov44_0_port_unknownmulticastallow": "true",
                  "vmxvre_console": "vidconsole",
                  "vmxvpfe_sriov44_0_port_vlanfilter": "4001",
                  "vmxvpfe_sriov42_0_port_mac": "00:11:22:EF:AC:DF",
                  "vmxvpfe_volume_id_0": "47cede15-da2f-4397-a101-aa683220aff3",
                  "vmxvpfe_sriov42_0_port_unknownmulticastallow": "true",
                  "min_instances": "1",
                  "vmxvpfe_sriov44_0_port_vlanstrip": "false",
                  "vf_module_id": "123",
                  "nf_function": "JAI",
                  "vmxvpfe_sriov43_0_port_unknownmulticastallow": "true",
                  "vmxvre_int_ctl_ip_0": "10.0.0.10",
                  "ecomp_generated_naming": "false",
                  "AIC_CLLI": "get_input:2017488_pasqualevpe0_AIC_CLLI",
                  "vnf_name": "mtnj309me6vre",
                  "vmxvpfe_sriov41_0_port_unknownunicastallow": "true",
                  "vmxvre_volume_type_1": "HITACHI",
                  "vmxvpfe_sriov44_0_port_broadcastallow": "true",
                  "vmxvre_volume_type_0": "HITACHI",
                  "vmxvpfe_volume_type_0": "HITACHI",
                  "vmxvpfe_sriov43_0_port_broadcastallow": "true",
                  "bandwidth_units": "get_input:pasqualevpe0_bandwidth_units",
                  "vnf_id": "123",
                  "vmxvre_oam_prefix": "24",
                  "availability_zone_0": "mtpocfo-kvm-az01",
                  "ASN": "get_input:2017488_pasqualevpe0_ASN",
                  "vmxvre_chassis_i2cid": "161",
                  "vmxvpfe_name_0": "vPFEXI",
                  "bandwidth": "get_input:pasqualevpe0_bandwidth",
                  "availability_zone_max_count": "1",
                  "vmxvre_volume_size_0": "45.0",
                  "vmxvre_volume_size_1": "50.0",
                  "vmxvpfe_sriov42_0_port_broadcastallow": "true",
                  "vmxvre_oam_gateway": "10.0.0.10",
                  "vmxvre_volume_name_1": "vREXI_FAVolume",
                  "vmxvre_ore_present": "0",
                  "vmxvre_volume_name_0": "vREXI_FBVolume",
                  "vmxvre_type": "0",
                  "vnf_instance_name": "get_input:2017488_pasqualevpe0_vnf_instance_name",
                  "vmxvpfe_sriov41_0_port_unknownmulticastallow": "true",
                  "oam_net_id": "b95eeb1d-d55d-4827-abb4-8ebb94941429",
                  "vmx_int_ctl_len": "24",
                  "vmxvpfe_sriov43_0_port_vlanstrip": "false",
                  "vmxvpfe_sriov41_0_port_broadcastallow": "true",
                  "vmxvre_volume_id_1": "6e86797e-03cd-4fdc-ba72-2957119c746d",
                  "vmxvpfe_sriov41_0_port_vlanfilter": "4001",
                  "nf_role": "Testing",
                  "vmxvre_volume_id_0": "f4eacb79-f687-4e9d-b760-21847c8bb15a",
                  "vmxvpfe_sriov42_0_port_unknownunicastallow": "true",
                  "vmxvpfe_flavor_name": "ns.c20r16d25.v5"
                },
                "type": "VF",
                "modelCustomizationName": "2017-388_PASQUALE-vPE 0",
                "vfModules": {},
                "volumeGroups": {},
                "vfcInstanceGroups": {}
              }, "2017-488_PASQUALE-vPE 0": {
                "uuid": "69e09f68-8b63-4cc9-b9ff-860960b5db09",
                "invariantUuid": "72e465fe-71b1-4e7b-b5ed-9496118ff7a8",
                "description": "Name PASQUALE vPE Description The provider edge function for the PASQUALE service supported by the Junipers VMX product Category Router Vendor Juniper Vendor Release Code 17.2 Owners Mary Fragale. Updated 9-25 to use v8.0 of the Juniper Valid 2 VLM",
                "name": "2017-488_PASQUALE-vPE",
                "version": "5.0",
                "customizationUuid": "1da7b585-5e61-4993-b95e-8e6606c81e45",
                "inputs": {
                  "vnf_config_template_version": {
                    "type": "string",
                    "description": "VPE Software Version",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "17.2"
                  },
                  "bandwidth_units": {
                    "type": "string",
                    "description": "Units of bandwidth",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "Gbps"
                  },
                  "bandwidth": {
                    "type": "string",
                    "description": "Requested VPE bandwidth",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "10"
                  },
                  "AIC_CLLI": {
                    "type": "string",
                    "description": "AIC Site CLLI",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "ATLMY8GA"
                  },
                  "ASN": {
                    "type": "string",
                    "description": "AV/PE",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "AV_vPE"
                  },
                  "vnf_instance_name": {
                    "type": "string",
                    "description": "The hostname assigned to the vpe.",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "mtnj309me6"
                  }
                },
                "commands": {
                  "vnf_config_template_version": {
                    "displayName": "vnf_config_template_version",
                    "command": "get_input",
                    "inputName": "2017488_pasqualevpe0_vnf_config_template_version"
                  },
                  "bandwidth_units": {
                    "displayName": "bandwidth_units",
                    "command": "get_input",
                    "inputName": "pasqualevpe0_bandwidth_units"
                  },
                  "bandwidth": {"displayName": "bandwidth", "command": "get_input", "inputName": "pasqualevpe0_bandwidth"},
                  "AIC_CLLI": {
                    "displayName": "AIC_CLLI",
                    "command": "get_input",
                    "inputName": "2017488_pasqualevpe0_AIC_CLLI"
                  },
                  "ASN": {"displayName": "ASN", "command": "get_input", "inputName": "2017488_pasqualevpe0_ASN"},
                  "vnf_instance_name": {
                    "displayName": "vnf_instance_name",
                    "command": "get_input",
                    "inputName": "2017488_pasqualevpe0_vnf_instance_name"
                  }
                },
                "properties": {
                  "vmxvre_retype": "RE-VMX",
                  "vnf_config_template_version": "get_input:2017488_pasqualevpe0_vnf_config_template_version",
                  "sriov44_net_id": "48d399b3-11ee-48a8-94d2-f0ea94d6be8d",
                  "int_ctl_net_id": "2f323477-6936-4d01-ac53-d849430281d9",
                  "vmxvpfe_sriov41_0_port_mac": "00:11:22:EF:AC:DF",
                  "int_ctl_net_name": "VMX-INTXI",
                  "vmx_int_ctl_prefix": "10.0.0.10",
                  "sriov43_net_id": "da349ca1-6de9-4548-be88-2d88e99bfef5",
                  "sriov42_net_id": "760669ba-013d-4d9b-b0e7-4151fe2e6279",
                  "sriov41_net_id": "25ad52d5-c165-40f8-b3b0-ddfc2373280a",
                  "nf_type": "vPE",
                  "vmxvpfe_int_ctl_ip_1": "10.0.0.10",
                  "is_AVPN_service": "false",
                  "vmx_RSG_name": "vREXI-affinity",
                  "vmx_int_ctl_forwarding": "l2",
                  "vmxvre_oam_ip_0": "10.0.0.10",
                  "vmxvpfe_sriov44_0_port_mac": "00:11:22:EF:AC:DF",
                  "vmxvpfe_sriov41_0_port_vlanstrip": "false",
                  "vmxvpfe_sriov42_0_port_vlanfilter": "4001",
                  "vmxvpfe_sriov44_0_port_unknownunicastallow": "true",
                  "vmxvre_image_name_0": "VRE-ENGINE_17.2-S2.1.qcow2",
                  "vmxvre_instance": "0",
                  "vmxvpfe_sriov43_0_port_mac": "00:11:22:EF:AC:DF",
                  "vmxvre_flavor_name": "ns.c1r16d32.v5",
                  "vmxvpfe_volume_size_0": "40.0",
                  "vmxvpfe_sriov43_0_port_vlanfilter": "4001",
                  "nf_naming": "{ecomp_generated_naming=false}",
                  "nf_naming_code": "Navneet",
                  "vmxvre_name_0": "vREXI",
                  "vmxvpfe_sriov42_0_port_vlanstrip": "false",
                  "vmxvpfe_volume_name_0": "vPFEXI_FBVolume",
                  "max_instances": "3",
                  "vmx_RSG_id": "bd89a33c-13c3-4a04-8fde-1a57eb123141",
                  "vmxvpfe_image_name_0": "VPE_ROUTING-ENGINE_17.2R1-S2.1.qcow2",
                  "vmxvpfe_sriov43_0_port_unknownunicastallow": "true",
                  "vmxvpfe_sriov44_0_port_unknownmulticastallow": "true",
                  "vmxvre_console": "vidconsole",
                  "vmxvpfe_sriov44_0_port_vlanfilter": "4001",
                  "vmxvpfe_sriov42_0_port_mac": "00:11:22:EF:AC:DF",
                  "vmxvpfe_volume_id_0": "47cede15-da2f-4397-a101-aa683220aff3",
                  "vmxvpfe_sriov42_0_port_unknownmulticastallow": "true",
                  "min_instances": "1",
                  "vmxvpfe_sriov44_0_port_vlanstrip": "false",
                  "vf_module_id": "123",
                  "nf_function": "JAI",
                  "vmxvpfe_sriov43_0_port_unknownmulticastallow": "true",
                  "vmxvre_int_ctl_ip_0": "10.0.0.10",
                  "ecomp_generated_naming": "false",
                  "AIC_CLLI": "get_input:2017488_pasqualevpe0_AIC_CLLI",
                  "vnf_name": "mtnj309me6vre",
                  "vmxvpfe_sriov41_0_port_unknownunicastallow": "true",
                  "vmxvre_volume_type_1": "HITACHI",
                  "vmxvpfe_sriov44_0_port_broadcastallow": "true",
                  "vmxvre_volume_type_0": "HITACHI",
                  "vmxvpfe_volume_type_0": "HITACHI",
                  "vmxvpfe_sriov43_0_port_broadcastallow": "true",
                  "bandwidth_units": "get_input:pasqualevpe0_bandwidth_units",
                  "vnf_id": "123",
                  "vmxvre_oam_prefix": "24",
                  "availability_zone_0": "mtpocfo-kvm-az01",
                  "ASN": "get_input:2017488_pasqualevpe0_ASN",
                  "vmxvre_chassis_i2cid": "161",
                  "vmxvpfe_name_0": "vPFEXI",
                  "bandwidth": "get_input:pasqualevpe0_bandwidth",
                  "availability_zone_max_count": "1",
                  "vmxvre_volume_size_0": "45.0",
                  "vmxvre_volume_size_1": "50.0",
                  "vmxvpfe_sriov42_0_port_broadcastallow": "true",
                  "vmxvre_oam_gateway": "10.0.0.10",
                  "vmxvre_volume_name_1": "vREXI_FAVolume",
                  "vmxvre_ore_present": "0",
                  "vmxvre_volume_name_0": "vREXI_FBVolume",
                  "vmxvre_type": "0",
                  "vnf_instance_name": "get_input:2017488_pasqualevpe0_vnf_instance_name",
                  "vmxvpfe_sriov41_0_port_unknownmulticastallow": "true",
                  "oam_net_id": "b95eeb1d-d55d-4827-abb4-8ebb94941429",
                  "vmx_int_ctl_len": "24",
                  "vmxvpfe_sriov43_0_port_vlanstrip": "false",
                  "vmxvpfe_sriov41_0_port_broadcastallow": "true",
                  "vmxvre_volume_id_1": "6e86797e-03cd-4fdc-ba72-2957119c746d",
                  "vmxvpfe_sriov41_0_port_vlanfilter": "4001",
                  "nf_role": "Testing",
                  "vmxvre_volume_id_0": "f4eacb79-f687-4e9d-b760-21847c8bb15a",
                  "vmxvpfe_sriov42_0_port_unknownunicastallow": "true",
                  "vmxvpfe_flavor_name": "ns.c20r16d25.v5"
                },
                "type": "VF",
                "modelCustomizationName": "2017-488_PASQUALE-vPE 0",
                "vfModules": {
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
                    "uuid": "25284168-24bb-4698-8cb4-3f509146eca5",
                    "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                    "customizationUuid": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401",
                    "description": null,
                    "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                    "version": "6",
                    "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                    "properties": {
                      "minCountInstances": 0,
                      "maxCountInstances": null,
                      "initialCount": 0,
                      "vfModuleLabel": "PASQUALE_vRE_BV"
                    },
                    "inputs": {
                      "pasqualevpe0_bandwidth": {
                        "type": "string",
                        "description": "Requested VPE bandwidth",
                        "entry_schema": null,
                        "inputProperties": {
                          "sourceType": "HEAT",
                          "vfModuleLabel": "PASQUALE_vRE_BV",
                          "paramName": "bandwidth"
                        },
                        "constraints": null,
                        "required": true,
                        "default": "10"
                      },
                      "2017488_pasqualevpe0_vnf_instance_name": {
                        "type": "string",
                        "description": "The hostname assigned to the vpe.",
                        "entry_schema": null,
                        "inputProperties": {
                          "sourceType": "HEAT",
                          "vfModuleLabel": "PASQUALE_vRE_BV",
                          "paramName": "vnf_instance_name"
                        },
                        "constraints": null,
                        "required": true,
                        "default": "mtnj309me6"
                      },
                      "2017488_pasqualevpe0_vnf_config_template_version": {
                        "type": "string",
                        "description": "VPE Software Version",
                        "entry_schema": null,
                        "inputProperties": {
                          "sourceType": "HEAT",
                          "vfModuleLabel": "PASQUALE_vRE_BV",
                          "paramName": "vnf_config_template_version"
                        },
                        "constraints": null,
                        "required": true,
                        "default": "17.2"
                      },
                      "2017488_pasqualevpe0_AIC_CLLI": {
                        "type": "string",
                        "description": "AIC Site CLLI",
                        "entry_schema": null,
                        "inputProperties": {
                          "sourceType": "HEAT",
                          "vfModuleLabel": "PASQUALE_vRE_BV",
                          "paramName": "AIC_CLLI"
                        },
                        "constraints": null,
                        "required": true,
                        "default": "ATLMY8GA"
                      },
                      "pasqualevpe0_bandwidth_units": {
                        "type": "string",
                        "description": "Units of bandwidth",
                        "entry_schema": null,
                        "inputProperties": {
                          "sourceType": "HEAT",
                          "vfModuleLabel": "PASQUALE_vRE_BV",
                          "paramName": "bandwidth_units"
                        },
                        "constraints": null,
                        "required": true,
                        "default": "Gbps"
                      }
                    },
                    "volumeGroupAllowed": true
                  },
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
                    "uuid": "f8360508-3f17-4414-a2ed-6bc71161e8db",
                    "invariantUuid": "b34833bb-6aa9-4ad6-a831-70b06367a091",
                    "customizationUuid": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3",
                    "description": null,
                    "name": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                    "version": "5",
                    "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                    "properties": {
                      "minCountInstances": 1,
                      "maxCountInstances": 1,
                      "initialCount": 1,
                      "vfModuleLabel": "PASQUALE_base_vPE_BV"
                    },
                    "inputs": {},
                    "volumeGroupAllowed": false
                  },
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2": {
                    "uuid": "0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a",
                    "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
                    "customizationUuid": "3cd946bb-50e0-40d8-96d3-c9023520b557",
                    "description": null,
                    "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                    "version": "6",
                    "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                    "properties": {
                      "minCountInstances": 0,
                      "maxCountInstances": null,
                      "initialCount": 0,
                      "vfModuleLabel": "PASQUALE_vPFE_BV"
                    },
                    "inputs": {},
                    "volumeGroupAllowed": true
                  }
                },
                "volumeGroups": {
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
                    "uuid": "25284168-24bb-4698-8cb4-3f509146eca5",
                    "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                    "customizationUuid": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401",
                    "description": null,
                    "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                    "version": "6",
                    "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                    "properties": {
                      "minCountInstances": 0,
                      "maxCountInstances": null,
                      "initialCount": 0,
                      "vfModuleLabel": "PASQUALE_vRE_BV"
                    },
                    "inputs": {
                      "pasqualevpe0_bandwidth": {
                        "type": "string",
                        "description": "Requested VPE bandwidth",
                        "entry_schema": null,
                        "inputProperties": {
                          "sourceType": "HEAT",
                          "vfModuleLabel": "PASQUALE_vRE_BV",
                          "paramName": "bandwidth"
                        },
                        "constraints": null,
                        "required": true,
                        "default": "10"
                      },
                      "2017488_pasqualevpe0_vnf_instance_name": {
                        "type": "string",
                        "description": "The hostname assigned to the vpe.",
                        "entry_schema": null,
                        "inputProperties": {
                          "sourceType": "HEAT",
                          "vfModuleLabel": "PASQUALE_vRE_BV",
                          "paramName": "vnf_instance_name"
                        },
                        "constraints": null,
                        "required": true,
                        "default": "mtnj309me6"
                      },
                      "2017488_pasqualevpe0_vnf_config_template_version": {
                        "type": "string",
                        "description": "VPE Software Version",
                        "entry_schema": null,
                        "inputProperties": {
                          "sourceType": "HEAT",
                          "vfModuleLabel": "PASQUALE_vRE_BV",
                          "paramName": "vnf_config_template_version"
                        },
                        "constraints": null,
                        "required": true,
                        "default": "17.2"
                      },
                      "2017488_pasqualevpe0_AIC_CLLI": {
                        "type": "string",
                        "description": "AIC Site CLLI",
                        "entry_schema": null,
                        "inputProperties": {
                          "sourceType": "HEAT",
                          "vfModuleLabel": "PASQUALE_vRE_BV",
                          "paramName": "AIC_CLLI"
                        },
                        "constraints": null,
                        "required": true,
                        "default": "ATLMY8GA"
                      },
                      "pasqualevpe0_bandwidth_units": {
                        "type": "string",
                        "description": "Units of bandwidth",
                        "entry_schema": null,
                        "inputProperties": {
                          "sourceType": "HEAT",
                          "vfModuleLabel": "PASQUALE_vRE_BV",
                          "paramName": "bandwidth_units"
                        },
                        "constraints": null,
                        "required": true,
                        "default": "Gbps"
                      }
                    }
                  },
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2": {
                    "uuid": "0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a",
                    "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
                    "customizationUuid": "3cd946bb-50e0-40d8-96d3-c9023520b557",
                    "description": null,
                    "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                    "version": "6",
                    "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                    "properties": {
                      "minCountInstances": 0,
                      "maxCountInstances": null,
                      "initialCount": 0,
                      "vfModuleLabel": "PASQUALE_vPFE_BV"
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
                "inputs": {
                  "vnf_config_template_version": {
                    "type": "string",
                    "description": "VPE Software Version",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "17.2"
                  },
                  "bandwidth_units": {
                    "type": "string",
                    "description": "Units of bandwidth",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "Gbps"
                  },
                  "bandwidth": {
                    "type": "string",
                    "description": "Requested VPE bandwidth",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "10"
                  },
                  "AIC_CLLI": {
                    "type": "string",
                    "description": "AIC Site CLLI",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "ATLMY8GA"
                  },
                  "ASN": {
                    "type": "string",
                    "description": "AV/PE",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "AV_vPE"
                  },
                  "vnf_instance_name": {
                    "type": "string",
                    "description": "The hostname assigned to the vpe.",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "mtnj309me6"
                  }
                },
                "commands": {},
                "properties": {
                  "min_instances": 1,
                  "max_instances": 10,
                  "network_assignments": "{is_external_network=false, ipv4_subnet_default_assignment={min_subnets_count=1}, ecomp_generated_network_assignment=false, ipv6_subnet_default_assignment={min_subnets_count=1}}",
                  "exVL_naming": "{ecomp_generated_naming=true}",
                  "network_flows": "{is_network_policy=false, is_bound_to_vpn=false}",
                  "network_homing": "{ecomp_selected_instance_node_target=false}"
                },
                "type": "VL",
                "modelCustomizationName": "ExtVL 0"
              }
            },
            "collectionResources": {},
            "configurations": {},
            "fabricConfigurations": {},
            "serviceProxies": {},
            "vfModules": {
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
                "uuid": "25284168-24bb-4698-8cb4-3f509146eca5",
                "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                "customizationUuid": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401",
                "description": null,
                "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                "version": "6",
                "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                "properties": {
                  "minCountInstances": 0,
                  "maxCountInstances": null,
                  "initialCount": 0,
                  "vfModuleLabel": "PASQUALE_vRE_BV"
                },
                "inputs": {
                  "pasqualevpe0_bandwidth": {
                    "type": "string",
                    "description": "Requested VPE bandwidth",
                    "entry_schema": null,
                    "inputProperties": {
                      "sourceType": "HEAT",
                      "vfModuleLabel": "PASQUALE_vRE_BV",
                      "paramName": "bandwidth"
                    },
                    "constraints": null,
                    "required": true,
                    "default": "10"
                  },
                  "2017488_pasqualevpe0_vnf_instance_name": {
                    "type": "string",
                    "description": "The hostname assigned to the vpe.",
                    "entry_schema": null,
                    "inputProperties": {
                      "sourceType": "HEAT",
                      "vfModuleLabel": "PASQUALE_vRE_BV",
                      "paramName": "vnf_instance_name"
                    },
                    "constraints": null,
                    "required": true,
                    "default": "mtnj309me6"
                  },
                  "2017488_pasqualevpe0_vnf_config_template_version": {
                    "type": "string",
                    "description": "VPE Software Version",
                    "entry_schema": null,
                    "inputProperties": {
                      "sourceType": "HEAT",
                      "vfModuleLabel": "PASQUALE_vRE_BV",
                      "paramName": "vnf_config_template_version"
                    },
                    "constraints": null,
                    "required": true,
                    "default": "17.2"
                  },
                  "2017488_pasqualevpe0_AIC_CLLI": {
                    "type": "string",
                    "description": "AIC Site CLLI",
                    "entry_schema": null,
                    "inputProperties": {"sourceType": "HEAT", "vfModuleLabel": "PASQUALE_vRE_BV", "paramName": "AIC_CLLI"},
                    "constraints": null,
                    "required": true,
                    "default": "ATLMY8GA"
                  },
                  "pasqualevpe0_bandwidth_units": {
                    "type": "string",
                    "description": "Units of bandwidth",
                    "entry_schema": null,
                    "inputProperties": {
                      "sourceType": "HEAT",
                      "vfModuleLabel": "PASQUALE_vRE_BV",
                      "paramName": "bandwidth_units"
                    },
                    "constraints": null,
                    "required": true,
                    "default": "Gbps"
                  }
                },
                "volumeGroupAllowed": true
              },
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
                "uuid": "f8360508-3f17-4414-a2ed-6bc71161e8db",
                "invariantUuid": "b34833bb-6aa9-4ad6-a831-70b06367a091",
                "customizationUuid": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3",
                "description": null,
                "name": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                "version": "5",
                "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                "properties": {
                  "minCountInstances": 1,
                  "maxCountInstances": 1,
                  "initialCount": 1,
                  "vfModuleLabel": "PASQUALE_base_vPE_BV"
                },
                "inputs": {},
                "volumeGroupAllowed": false
              },
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2": {
                "uuid": "0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a",
                "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
                "customizationUuid": "3cd946bb-50e0-40d8-96d3-c9023520b557",
                "description": null,
                "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                "version": "6",
                "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                "properties": {
                  "minCountInstances": 0,
                  "maxCountInstances": null,
                  "initialCount": 0,
                  "vfModuleLabel": "PASQUALE_vPFE_BV"
                },
                "inputs": {},
                "volumeGroupAllowed": true
              }
            },
            "volumeGroups": {
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
                "uuid": "25284168-24bb-4698-8cb4-3f509146eca5",
                "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                "customizationUuid": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401",
                "description": null,
                "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                "version": "6",
                "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                "properties": {
                  "minCountInstances": 0,
                  "maxCountInstances": null,
                  "initialCount": 0,
                  "vfModuleLabel": "PASQUALE_vRE_BV"
                },
                "inputs": {
                  "pasqualevpe0_bandwidth": {
                    "type": "string",
                    "description": "Requested VPE bandwidth",
                    "entry_schema": null,
                    "inputProperties": {
                      "sourceType": "HEAT",
                      "vfModuleLabel": "PASQUALE_vRE_BV",
                      "paramName": "bandwidth"
                    },
                    "constraints": null,
                    "required": true,
                    "default": "10"
                  },
                  "2017488_pasqualevpe0_vnf_instance_name": {
                    "type": "string",
                    "description": "The hostname assigned to the vpe.",
                    "entry_schema": null,
                    "inputProperties": {
                      "sourceType": "HEAT",
                      "vfModuleLabel": "PASQUALE_vRE_BV",
                      "paramName": "vnf_instance_name"
                    },
                    "constraints": null,
                    "required": true,
                    "default": "mtnj309me6"
                  },
                  "2017488_pasqualevpe0_vnf_config_template_version": {
                    "type": "string",
                    "description": "VPE Software Version",
                    "entry_schema": null,
                    "inputProperties": {
                      "sourceType": "HEAT",
                      "vfModuleLabel": "PASQUALE_vRE_BV",
                      "paramName": "vnf_config_template_version"
                    },
                    "constraints": null,
                    "required": true,
                    "default": "17.2"
                  },
                  "2017488_pasqualevpe0_AIC_CLLI": {
                    "type": "string",
                    "description": "AIC Site CLLI",
                    "entry_schema": null,
                    "inputProperties": {"sourceType": "HEAT", "vfModuleLabel": "PASQUALE_vRE_BV", "paramName": "AIC_CLLI"},
                    "constraints": null,
                    "required": true,
                    "default": "ATLMY8GA"
                  },
                  "pasqualevpe0_bandwidth_units": {
                    "type": "string",
                    "description": "Units of bandwidth",
                    "entry_schema": null,
                    "inputProperties": {
                      "sourceType": "HEAT",
                      "vfModuleLabel": "PASQUALE_vRE_BV",
                      "paramName": "bandwidth_units"
                    },
                    "constraints": null,
                    "required": true,
                    "default": "Gbps"
                  }
                }
              },
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2": {
                "uuid": "0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a",
                "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
                "customizationUuid": "3cd946bb-50e0-40d8-96d3-c9023520b557",
                "description": null,
                "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                "version": "6",
                "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                "properties": {
                  "minCountInstances": 0,
                  "maxCountInstances": null,
                  "initialCount": 0,
                  "vfModuleLabel": "PASQUALE_vPFE_BV"
                },
                "inputs": {}
              }
            },
            "pnfs": {}
          }
        },
        "serviceInstance": {
          "6e59c5de-f052-46fa-aa7e-2fca9d674c44": {
            "vnfs": {
              "VF_vGeraldine 0": {
                "rollbackOnFailure": "true",
                "vfModules": {
                  "vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0": {
                    "vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0vmvzo": {
                      "isMissingData": false,
                      "sdncPreReload": null,
                      "modelInfo": {
                        "modelType": "VFmodule",
                        "modelInvariantId": "a6f9e51a-2b35-416a-ae15-15e58d61f36d",
                        "modelVersionId": "a27f5cfc-7f12-4f99-af08-0af9c3885c87",
                        "modelName": "VfVgeraldine..base_vflorence..module-0",
                        "modelVersion": "2",
                        "modelCustomizationId": "f8c040f1-7e51-4a11-aca8-acf256cfd861",
                        "modelCustomizationName": "VfVgeraldine..base_vflorence..module-0"
                      },
                      "instanceParams": [{}],
                      "trackById": "wmtm6sy2uj"
                    }
                  }
                },
                "isMissingData": true,
                "originalName": "VF_vGeraldine 0",
                "vnfStoreKey": "VF_vGeraldine 0",
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
                  "modelName": "VF_vGeraldine",
                  "modelVersion": "2.0",
                  "modelCustomizationName": "VF_vGeraldine 0"
                }
              }
            },
            "networks": {},
            "instanceParams": [{}],
            "validationCounter": 1,
            "existingNames": {},
            "existingVNFCounterMap": {"d6557200-ecf2-4641-8094-5393ae3aae60": 1},
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
          }, "6b528779-44a3-4472-bdff-9cd15ec93450": {
            "networks": {
              "ExtVL 0": {
                "rollbackOnFailure": "true",
                "isMissingData": false,
                "originalName": "ExtVL 0",
                "networkStoreKey": "ExtVL 0",
                "trackById": "sf3zth68xjf",
                "productFamilyId": "ebc3bc3d-62fd-4a3f-a037-f619df4ff034",
                "lcpCloudRegionId": "hvf6",
                "tenantId": "229bcdc6eaeb4ca59d55221141d01f8e",
                "platformName": "xxx1",
                "lineOfBusiness": "ONAP",
                "instanceParams": [{}],
                "modelInfo": {
                  "modelInvariantId": "379f816b-a7aa-422f-be30-17114ff50b7c",
                  "modelVersionId": "ddc3f20c-08b5-40fd-af72-c6d14636b986",
                  "modelName": "ExtVL",
                  "modelVersion": "37.0",
                  "modelCustomizationId": "94fdd893-4a36-4d70-b16a-ec29c54c184f",
                  "modelCustomizationName": "ExtVL 0",
                  "uuid": "ddc3f20c-08b5-40fd-af72-c6d14636b986"
                },
                "uuid": "ddc3f20c-08b5-40fd-af72-c6d14636b986"
              },
              "ExtVL 0:0001": {
                "rollbackOnFailure": "true",
                "isMissingData": false,
                "originalName": "ExtVL 0",
                "networkStoreKey": "ExtVL 0",
                "trackById": "2mdxioxca9h",
                "productFamilyId": "ebc3bc3d-62fd-4a3f-a037-f619df4ff034",
                "lcpCloudRegionId": "hvf6",
                "tenantId": "229bcdc6eaeb4ca59d55221141d01f8e",
                "platformName": "xxx1",
                "lineOfBusiness": "ONAP",
                "instanceParams": [{}],
                "modelInfo": {
                  "modelInvariantId": "379f816b-a7aa-422f-be30-17114ff50b7c",
                  "modelVersionId": "ddc3f20c-08b5-40fd-af72-c6d14636b986",
                  "modelName": "ExtVL",
                  "modelVersion": "37.0",
                  "modelCustomizationId": "94fdd893-4a36-4d70-b16a-ec29c54c184f",
                  "modelCustomizationName": "ExtVL 0",
                  "uuid": "ddc3f20c-08b5-40fd-af72-c6d14636b986"
                },
                "uuid": "ddc3f20c-08b5-40fd-af72-c6d14636b986"
              },
              "ExtVL 0_1": {
                "rollbackOnFailure": "true",
                "isMissingData": false,
                "originalName": "ExtVL 0",
                "networkStoreKey": "ExtVL 0_1",
                "trackById": "z7vd1gmpbs",
                "instanceName": "ExtVL",
                "productFamilyId": "17cc1042-527b-11e6-beb8-9e71128cae77",
                "lcpCloudRegionId": "hvf6",
                "tenantId": "229bcdc6eaeb4ca59d55221141d01f8e",
                "platformName": "xxx1",
                "lineOfBusiness": "zzz1",
                "instanceParams": [{
                  "vnf_config_template_version": "17.2",
                  "bandwidth_units": "Gbps",
                  "bandwidth": "10",
                  "AIC_CLLI": "ATLMY8GA",
                  "ASN": "AV_vPE",
                  "vnf_instance_name": "yoav"
                }],
                "modelInfo": {
                  "modelInvariantId": "379f816b-a7aa-422f-be30-17114ff50b7c",
                  "modelVersionId": "ddc3f20c-08b5-40fd-af72-c6d14636b986",
                  "modelName": "ExtVL",
                  "modelVersion": "37.0",
                  "modelCustomizationId": "94fdd893-4a36-4d70-b16a-ec29c54c184f",
                  "modelCustomizationName": "ExtVL 0",
                  "uuid": "ddc3f20c-08b5-40fd-af72-c6d14636b986"
                },
                "uuid": "ddc3f20c-08b5-40fd-af72-c6d14636b986"
              }
            },
            "vnfs": {
              "2017-488_PASQUALE-vPE 0": {
                "rollbackOnFailure": "true",
                "vfModules": {
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
                    "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1sgoqi": {
                      "instanceName": "yoav",
                      "volumeGroupName": "123",
                      "modelInfo": {
                        "modelInvariantId": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                        "modelVersionId": "25284168-24bb-4698-8cb4-3f509146eca5",
                        "modelName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                        "modelVersion": "6",
                        "modelCustomizationId": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401",
                        "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                        "uuid": "25284168-24bb-4698-8cb4-3f509146eca5"
                      },
                      "uuid": "25284168-24bb-4698-8cb4-3f509146eca5",
                      "isMissingData": false,
                      "instanceParams": [{
                        "pasqualevpe0_bandwidth": "10",
                        "2017488_pasqualevpe0_vnf_instance_name": "mtnj309me6",
                        "2017488_pasqualevpe0_vnf_config_template_version": "17.2",
                        "2017488_pasqualevpe0_AIC_CLLI": "ATLMY8GA",
                        "pasqualevpe0_bandwidth_units": "Gbps"
                      }]
                    }
                  }
                },
                "isMissingData": false,
                "originalName": "2017-488_PASQUALE-vPE 0",
                "vnfStoreKey": "2017-488_PASQUALE-vPE 0",
                "trackById": "o65b26t2thj",
                "instanceName": "2017488_PASQUALEvPE",
                "productFamilyId": "ebc3bc3d-62fd-4a3f-a037-f619df4ff034",
                "lcpCloudRegionId": "hvf6",
                "tenantId": "bae71557c5bb4d5aac6743a4e5f1d054",
                "platformName": "platform",
                "lineOfBusiness": "ONAP",
                "instanceParams": [{}],
                "modelInfo": {
                  "modelInvariantId": "72e465fe-71b1-4e7b-b5ed-9496118ff7a8",
                  "modelVersionId": "69e09f68-8b63-4cc9-b9ff-860960b5db09",
                  "modelName": "2017-488_PASQUALE-vPE",
                  "modelVersion": "5.0",
                  "modelCustomizationId": "1da7b585-5e61-4993-b95e-8e6606c81e45",
                  "modelCustomizationName": "2017-488_PASQUALE-vPE 0",
                  "uuid": "69e09f68-8b63-4cc9-b9ff-860960b5db09"
                },
                "uuid": "69e09f68-8b63-4cc9-b9ff-860960b5db09"
              }
            },
            "instanceParams": [{"2017488_pasqualevpe0_ASN": "AV_vPE"}],
            "validationCounter": 0,
            "existingNames": {"123": "", "instancename": "", "yoav": "", "extvl": ""},
            "existingVNFCounterMap": {"69e09f68-8b63-4cc9-b9ff-860960b5db09": 1},
            "existingNetworksCounterMap": {"ddc3f20c-08b5-40fd-af72-c6d14636b986": 3},
            "instanceName": "InstanceName",
            "globalSubscriberId": "e433710f-9217-458d-a79d-1c7aff376d89",
            "subscriptionServiceType": "TYLER SILVIA",
            "owningEntityId": "d61e6f2d-12fa-4cc2-91df-7c244011d6fc",
            "productFamilyId": "17cc1042-527b-11e6-beb8-9e71128cae77",
            "lcpCloudRegionId": "AAIAIC25",
            "tenantId": "092eb9e8e4b7412e8787dd091bc58e86",
            "aicZoneId": "JAG1",
            "projectName": null,
            "rollbackOnFailure": "true",
            "aicZoneName": "YUDFJULP-JAG1",
            "owningEntityName": "WayneHolland",
            "testApi": "GR_API",
            "isEcompGeneratedNaming": false,
            "tenantName": "USP-SIP-IC-24335-T-01",
            "bulkSize": 1,
            "modelInfo": {
              "modelInvariantId": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
              "modelVersionId": "6b528779-44a3-4472-bdff-9cd15ec93450",
              "modelName": "action-data",
              "modelVersion": "1.0",
              "uuid": "6b528779-44a3-4472-bdff-9cd15ec93450"
            },
            "isALaCarte": false,
            "name": "action-data",
            "version": "1.0",
            "description": "",
            "category": "",
            "uuid": "6b528779-44a3-4472-bdff-9cd15ec93450",
            "invariantUuid": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
            "serviceType": "",
            "serviceRole": "",
            "isMultiStepDesign": false
          }
        },
        "lcpRegionsAndTenants": {
          "lcpRegionList": [{"id": "AAIAIC25", "name": "AAIAIC25", "isPermitted": true}, {
            "id": "hvf6",
            "name": "hvf6",
            "isPermitted": true
          }], "lcpRegionsTenantsMap": {
            "AAIAIC25": [{
              "id": "092eb9e8e4b7412e8787dd091bc58e86",
              "name": "USP-SIP-IC-24335-T-01",
              "isPermitted": true
            }],
            "hvf6": [{
              "id": "bae71557c5bb4d5aac6743a4e5f1d054",
              "name": "AIN Web Tool-15-D-testalexandria",
              "isPermitted": true
            }, {
              "id": "229bcdc6eaeb4ca59d55221141d01f8e",
              "name": "AIN Web Tool-15-D-STTest2",
              "isPermitted": true
            }, {
              "id": "1178612d2b394be4834ad77f567c0af2",
              "name": "AIN Web Tool-15-D-SSPtestcustome",
              "isPermitted": true
            }, {
              "id": "19c5ade915eb461e8af52fb2fd8cd1f2",
              "name": "AIN Web Tool-15-D-UncheckedEcopm",
              "isPermitted": true
            }, {
              "id": "de007636e25249238447264a988a927b",
              "name": "AIN Web Tool-15-D-dfsdf",
              "isPermitted": true
            }, {
              "id": "62f29b3613634ca6a3065cbe0e020c44",
              "name": "AIN/SMS-16-D-Multiservices1",
              "isPermitted": true
            }, {
              "id": "649289e30d3244e0b48098114d63c2aa",
              "name": "AIN Web Tool-15-D-SSPST66",
              "isPermitted": true
            }, {
              "id": "3f21eeea6c2c486bba31dab816c05a32",
              "name": "AIN Web Tool-15-D-ASSPST47",
              "isPermitted": true
            }, {
              "id": "f60ce21d3ee6427586cff0d22b03b773",
              "name": "CESAR-100-D-sspjg67246",
              "isPermitted": true
            }, {
              "id": "8774659e425f479895ae091bb5d46560",
              "name": "CESAR-100-D-sspjg68359",
              "isPermitted": true
            }, {
              "id": "624eb554b0d147c19ff8885341760481",
              "name": "AINWebTool-15-D-iftach",
              "isPermitted": true
            }, {
              "id": "214f55f5fc414c678059c383b03e4962",
              "name": "CESAR-100-D-sspjg612401",
              "isPermitted": true
            }, {
              "id": "c90666c291664841bb98e4d981ff1db5",
              "name": "CESAR-100-D-sspjg621340",
              "isPermitted": true
            }, {
              "id": "ce5b6bc5c7b348e1bf4b91ac9a174278",
              "name": "sspjg621351cloned",
              "isPermitted": true
            }, {
              "id": "b386b768a3f24c8e953abbe0b3488c02",
              "name": "AINWebTool-15-D-eteancomp",
              "isPermitted": true
            }, {
              "id": "dc6c4dbfd225474e9deaadd34968646c",
              "name": "AINWebTool-15-T-SPFET",
              "isPermitted": true
            }, {
              "id": "02cb5030e9914aa4be120bd9ed1e19eb",
              "name": "AINWebTool-15-X-eeweww",
              "isPermitted": true
            }, {
              "id": "f2f3830e4c984d45bcd00e1a04158a79",
              "name": "CESAR-100-D-spjg61909",
              "isPermitted": true
            }, {
              "id": "05b91bd5137f4929878edd965755c06d",
              "name": "CESAR-100-D-sspjg621512cloned",
              "isPermitted": true
            }, {
              "id": "7002fbe8482d4a989ddf445b1ce336e0",
              "name": "AINWebTool-15-X-vdr",
              "isPermitted": true
            }, {
              "id": "4008522be43741dcb1f5422022a2aa0b",
              "name": "AINWebTool-15-D-ssasa",
              "isPermitted": true
            }, {
              "id": "f44e2e96a1b6476abfda2fa407b00169",
              "name": "AINWebTool-15-D-PFNPT",
              "isPermitted": true
            }, {
              "id": "b69a52bec8a84669a37a1e8b72708be7",
              "name": "AINWebTool-15-X-vdre",
              "isPermitted": true
            }, {
              "id": "fac7d9fd56154caeb9332202dcf2969f",
              "name": "AINWebTool-15-X-NONPODECOMP",
              "isPermitted": true
            }, {
              "id": "2d34d8396e194eb49969fd61ffbff961",
              "name": "DN5242-Nov16-T5",
              "isPermitted": true
            }, {
              "id": "cb42a77ff45b48a8b8deb83bb64acc74",
              "name": "ro-T11",
              "isPermitted": true
            }, {
              "id": "fa45ca53c80b492fa8be5477cd84fc2b",
              "name": "ro-T112",
              "isPermitted": true
            }, {
              "id": "4914ab0ab3a743e58f0eefdacc1dde77",
              "name": "DN5242-Nov21-T1",
              "isPermitted": true
            }, {
              "id": "d0a3e3f2964542259d155a81c41aadc3",
              "name": "test-hvf6-09",
              "isPermitted": true
            }, {"id": "cbb99fe4ada84631b7baf046b6fd2044", "name": "DN5242-Nov16-T3", "isPermitted": true}]
          }
        },
        "productFamilies": [{
          "id": "ebc3bc3d-62fd-4a3f-a037-f619df4ff034",
          "name": "ERICA",
          "isPermitted": true
        }, {
          "id": "17cc1042-527b-11e6-beb8-9e71128cae77",
          "name": "IGNACIO",
          "isPermitted": true
        }, {
          "id": "36b4733a-53f4-4cc8-8ff0-9172e5fc4b8e",
          "name": "Christie",
          "isPermitted": true
        }, {
          "id": "a4f6f2ae-9bf5-4ed7-b904-06b2099c4bd7",
          "name": "Enhanced Services",
          "isPermitted": true
        }, {"id": "vTerrance", "name": "vTerrance", "isPermitted": true}, {
          "id": "323d69d9-2efe-4r45-ay0a-89ea7ard4e6f",
          "name": "vEsmeralda",
          "isPermitted": true
        }, {
          "id": "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
          "name": "Emanuel",
          "isPermitted": true
        }, {
          "id": "d8a6ed93-251c-47ca-adc9-86671fd19f4c",
          "name": "BVOIP",
          "isPermitted": true
        }, {"id": "db171b8f-115c-4992-a2e3-ee04cae357e0", "name": "LINDSEY", "isPermitted": true}, {
          "id": "LRSI-OSPF",
          "name": "LRSI-OSPF",
          "isPermitted": true
        }, {"id": "vRosemarie", "name": "HNGATEWAY", "isPermitted": true}, {
          "id": "vHNPaas",
          "name": "WILKINS",
          "isPermitted": true
        }, {
          "id": "e433710f-9217-458d-a79d-1c7aff376d89",
          "name": "TYLER SILVIA",
          "isPermitted": true
        }, {"id": "b6a3f28c-eebf-494c-a900-055cc7c874ce", "name": "VROUTER", "isPermitted": true}, {
          "id": "vMuriel",
          "name": "vMuriel",
          "isPermitted": true
        }, {
          "id": "0ee8c1bc-7cbd-4b0a-a1ac-e9999255abc1",
          "name": "CARA Griffin",
          "isPermitted": true
        }, {
          "id": "c7611ebe-c324-48f1-8085-94aef0c6ef3d",
          "name": "DARREN MCGEE",
          "isPermitted": true
        }, {"id": "e30755dc-5673-4b6b-9dcf-9abdd96b93d1", "name": "Transport", "isPermitted": true}, {
          "id": "vSalvatore",
          "name": "vSalvatore",
          "isPermitted": true
        }, {"id": "d7bb0a21-66f2-4e6d-87d9-9ef3ced63ae4", "name": "JOSEFINA", "isPermitted": true}, {
          "id": "vHubbard",
          "name": "vHubbard",
          "isPermitted": true
        }, {"id": "12a96a9d-4b4c-4349-a950-fe1159602621", "name": "DARREN MCGEE", "isPermitted": true}],
        "serviceTypes": {
          "e433710f-9217-458d-a79d-1c7aff376d89": [{
            "id": "0",
            "name": "vRichardson",
            "isPermitted": false
          }, {"id": "1", "name": "TYLER SILVIA", "isPermitted": true}, {
            "id": "2",
            "name": "Emanuel",
            "isPermitted": false
          }, {"id": "3", "name": "vJamie", "isPermitted": false}, {
            "id": "4",
            "name": "vVoiceMail",
            "isPermitted": false
          }, {"id": "5", "name": "Kennedy", "isPermitted": false}, {
            "id": "6",
            "name": "vPorfirio",
            "isPermitted": false
          }, {"id": "7", "name": "vVM", "isPermitted": false}, {
            "id": "8",
            "name": "vOTA",
            "isPermitted": false
          }, {"id": "9", "name": "vFLORENCE", "isPermitted": false}, {
            "id": "10",
            "name": "vMNS",
            "isPermitted": false
          }, {"id": "11", "name": "vEsmeralda", "isPermitted": false}, {
            "id": "12",
            "name": "VPMS",
            "isPermitted": false
          }, {"id": "13", "name": "vWINIFRED", "isPermitted": false}, {
            "id": "14",
            "name": "SSD",
            "isPermitted": false
          }, {"id": "15", "name": "vMOG", "isPermitted": false}, {
            "id": "16",
            "name": "LINDSEY",
            "isPermitted": false
          }, {"id": "17", "name": "JOHANNA_SANTOS", "isPermitted": false}, {
            "id": "18",
            "name": "vCarroll",
            "isPermitted": false
          }]
        },
        "aicZones": [{"id": "NFT1", "name": "NFTJSSSS-NFT1"}, {"id": "JAG1", "name": "YUDFJULP-JAG1"}, {
          "id": "YYY1",
          "name": "UUUAIAAI-YYY1"
        }, {"id": "BAN1", "name": "VSDKYUTP-BAN1"}, {"id": "DKJ1", "name": "DKJSJDKA-DKJ1"}, {
          "id": "MCS1",
          "name": "ASACMAMS-MCS1"
        }, {"id": "UIO1", "name": "uioclli1-UIO1"}, {"id": "RAJ1", "name": "YGBIJNLQ-RAJ1"}, {
          "id": "OPA1",
          "name": "opaclli1-OPA1"
        }, {"id": "SDE1", "name": "ZXCVBNMA-SDE1"}, {"id": "VEN2", "name": "FGHJUHIL-VEN2"}, {
          "id": "ORL1",
          "name": "ORLDFLMA-ORL1"
        }, {"id": "JAD1", "name": "JADECLLI-JAD1"}, {"id": "ZXL1", "name": "LWLWCANN-ZXL1"}, {
          "id": "CKL1",
          "name": "CLKSKCKK-CKL1"
        }, {"id": "SDF1", "name": "sdfclli1-SDF1"}, {"id": "RAD1", "name": "RADICAL1-RAD1"}, {
          "id": "KIT1",
          "name": "BHYJFGLN-KIT1"
        }, {"id": "REL1", "name": "INGERFGT-REL1"}, {"id": "JNL1", "name": "CJALSDAC-JNL1"}, {
          "id": "OLK1",
          "name": "OLKOLKLS-OLK1"
        }, {"id": "CHI1", "name": "CHILLIWE-CHI1"}, {"id": "UUU4", "name": "UUUAAAUU-UUU4"}, {
          "id": "TUF1",
          "name": "TUFCLLI1-TUF1"
        }, {"id": "KJN1", "name": "CKALDKSA-KJN1"}, {"id": "SAM1", "name": "SNDGCA64-SAN1"}, {
          "id": "SCK1",
          "name": "SCKSCKSK-SCK1"
        }, {"id": "HJH1", "name": "AOEEQQQD-HJH1"}, {"id": "HGD1", "name": "SDFQWHGD-HGD1"}, {
          "id": "KOR1",
          "name": "HYFLNBVT-KOR1"
        }, {"id": "ATL43", "name": "AICLOCID-ATL43"}, {"id": "ATL54", "name": "AICFTAAI-ATL54"}, {
          "id": "ATL66",
          "name": "CLLIAAII-ATL66"
        }, {"id": "VEL1", "name": "BNMLKUIK-VEL1"}, {"id": "ICC1", "name": "SANJITAT-ICC1"}, {
          "id": "MNT11",
          "name": "WSXEFBTH-MNT11"
        }, {"id": "DEF2", "name": "WSBHGTYL-DEF2"}, {"id": "MAD11", "name": "SDFQWGKL-MAD11"}, {
          "id": "OLG1",
          "name": "OLHOLHOL-OLG1"
        }, {"id": "GAR1", "name": "NGFVSJKO-GAR1"}, {"id": "SAN22", "name": "GNVLSCTL-SAN22"}, {
          "id": "HRG1",
          "name": "HRGHRGGS-HRG1"
        }, {"id": "JCS1", "name": "JCSJSCJS-JCS1"}, {"id": "DHA12", "name": "WSXEDECF-DHA12"}, {
          "id": "HJE1",
          "name": "AOEEWWWD-HJE1"
        }, {"id": "NCA1", "name": "NCANCANN-NCA1"}, {"id": "IOP1", "name": "iopclli1-IOP1"}, {
          "id": "RTY1",
          "name": "rtyclli1-RTY1"
        }, {"id": "KAP1", "name": "HIOUYTRQ-KAP1"}, {"id": "ZEN1", "name": "ZENCLLI1-ZEN1"}, {
          "id": "HKA1",
          "name": "JAKHLASS-HKA1"
        }, {"id": "CQK1", "name": "CQKSCAKK-CQK1"}, {"id": "SAI1", "name": "UBEKQLPD-SAI1"}, {
          "id": "ERT1",
          "name": "ertclli1-ERT1"
        }, {"id": "IBB1", "name": "PLMKOIJU-IBB1"}, {"id": "TIR2", "name": "PLKINHYI-TIR2"}, {
          "id": "HSD1",
          "name": "CHASKCDS-HSD1"
        }, {"id": "SLF78", "name": "SDCTLFN1-SLF78"}, {"id": "SEE78", "name": "SDCTEEE4-SEE78"}, {
          "id": "SAN13",
          "name": "TOKYJPFA-SAN13"
        }, {"id": "SAA78", "name": "SDCTAAA1-SAA78"}, {"id": "LUC1", "name": "ATLDFGYC-LUC1"}, {
          "id": "AMD13",
          "name": "MEMATLAN-AMD13"
        }, {"id": "TOR1", "name": "TOROONXN-TOR1"}, {"id": "QWE1", "name": "QWECLLI1-QWE1"}, {
          "id": "ZOG1",
          "name": "ZOGASTRO-ZOG1"
        }, {"id": "CAL33", "name": "CALIFORN-CAL33"}, {"id": "SHH78", "name": "SDIT1HHH-SHH78"}, {
          "id": "DSA1",
          "name": "LKJHGFDS-DSA1"
        }, {"id": "CLG1", "name": "CLGRABAD-CLG1"}, {"id": "BNA1", "name": "BNARAGBK-BNA1"}, {
          "id": "ATL84",
          "name": "CANTTCOC-ATL84"
        }, {"id": "APP1", "name": "WBHGTYUI-APP1"}, {"id": "RJN1", "name": "RJNRBZAW-RJN1"}, {
          "id": "EHH78",
          "name": "SDCSHHH5-EHH78"
        }, {"id": "mac10", "name": "PKGTESTF-mac10"}, {"id": "SXB78", "name": "SDCTGXB1-SXB78"}, {
          "id": "SAX78",
          "name": "SDCTAXG1-SAX78"
        }, {"id": "SYD1", "name": "SYDNAUBV-SYD1"}, {"id": "TOK1", "name": "TOKYJPFA-TOK1"}, {
          "id": "KGM2",
          "name": "KGMTNC20-KGM2"
        }, {"id": "DCC1b", "name": "POIUYTGH-DCC1b"}, {"id": "SKK78", "name": "SDCTKKK1-SKK78"}, {
          "id": "SGG78",
          "name": "SDCTGGG1-SGG78"
        }, {"id": "SJJ78", "name": "SDCTJJJ1-SJJ78"}, {"id": "SBX78", "name": "SDCTBXG1-SBX78"}, {
          "id": "LAG1",
          "name": "LARGIZON-LAG1"
        }, {"id": "IAA1", "name": "QAZXSWED-IAA1"}, {"id": "POI1", "name": "PLMNJKIU-POI1"}, {
          "id": "LAG1a",
          "name": "LARGIZON-LAG1a"
        }, {"id": "PBL1", "name": "PBLAPBAI-PBL1"}, {"id": "LAG45", "name": "LARGIZON-LAG1a"}, {
          "id": "MAR1",
          "name": "MNBVCXZM-MAR1"
        }, {"id": "HST70", "name": "HSTNTX70-HST70"}, {"id": "DCC1a", "name": "POIUYTGH-DCC1a"}, {
          "id": "TOL1",
          "name": "TOLDOH21-TOL1"
        }, {"id": "LON1", "name": "LONEENCO-LON1"}, {"id": "SJU78", "name": "SDIT1JUB-SJU78"}, {
          "id": "STN27",
          "name": "HSTNTX01-STN27"
        }, {"id": "SSW56", "name": "ss8126GT-SSW56"}, {"id": "SBB78", "name": "SDIT1BBB-SBB78"}, {
          "id": "DCC3",
          "name": "POIUYTGH-DCC3"
        }, {"id": "GNV1", "name": "GNVLSCTL-GNV1"}, {"id": "WAS1", "name": "WASHDCSW-WAS1"}, {
          "id": "TOY1",
          "name": "TORYONNZ-TOY1"
        }, {"id": "STT1", "name": "STTLWA02-STT1"}, {"id": "STG1", "name": "STTGGE62-STG1"}, {
          "id": "SLL78",
          "name": "SDCTLLL1-SLL78"
        }, {"id": "SBU78", "name": "SDIT1BUB-SBU78"}, {"id": "ATL2", "name": "ATLNGANW-ATL2"}, {
          "id": "BOT1",
          "name": "BOTHWAKY-BOT1"
        }, {"id": "SNG1", "name": "SNGPSIAU-SNG1"}, {"id": "NYC1", "name": "NYCMNY54-NYC1"}, {
          "id": "LAG1b",
          "name": "LARGIZON-LAG1b"
        }, {"id": "AMD15", "name": "AMDFAA01-AMD15"}, {"id": "SNA1", "name": "SNANTXCA-SNA1"}, {
          "id": "PLT1",
          "name": "PLTNCA60-PLT1"
        }, {"id": "TLP1", "name": "TLPNXM18-TLP1"}, {"id": "SDD81", "name": "SAIT1DD6-SDD81"}, {
          "id": "DCC1",
          "name": "POIUYTGH-DCC1"
        }, {"id": "DCC2", "name": "POIUYTGH-DCC2"}, {"id": "OKC1", "name": "OKCBOK55-OKC1"}, {
          "id": "PAR1",
          "name": "PARSFRCG-PAR1"
        }, {"id": "TES36", "name": "ABCEETES-TES36"}, {"id": "COM1", "name": "PLMKOPIU-COM1"}, {
          "id": "ANI1",
          "name": "ATLNGTRE-ANI1"
        }, {"id": "SDG78", "name": "SDIT1BDG-SDG78"}, {"id": "mac20", "name": "PKGTESTF-mac20"}, {
          "id": "DSF45",
          "name": "DSFBG123-DSF45"
        }, {"id": "HST25", "name": "HSTNTX01-HST25"}, {"id": "AMD18", "name": "AUDIMA01-AMD18"}, {
          "id": "SAA80",
          "name": "SAIT9AA3-SAA80"
        }, {"id": "SSA56", "name": "SSIT2AA7-SSA56"}, {"id": "SDD82", "name": "SAIT1DD9-SDD82"}, {
          "id": "JCV1",
          "name": "JCVLFLBW-JCV1"
        }, {"id": "SUL2", "name": "WERTYUJK-SUL2"}, {"id": "PUR1", "name": "purelyde-PUR1"}, {
          "id": "FDE55",
          "name": "FDERT555-FDE55"
        }, {"id": "SITE", "name": "LONEENCO-SITE"}, {"id": "ATL1", "name": "ATLNGAMA-ATL1"}, {
          "id": "JUL1",
          "name": "ZXCVBNMM-JUL1"
        }, {"id": "TAT34", "name": "TESAAISB-TAT34"}, {"id": "XCP12", "name": "CHKGH123-XCP12"}, {
          "id": "RAI1",
          "name": "poiuytre-RAI1"
        }, {"id": "HPO1", "name": "ATLNGAUP-HPO1"}, {"id": "KJF12", "name": "KJFDH123-KJF12"}, {
          "id": "SCC80",
          "name": "SAIT9CC3-SCC80"
        }, {"id": "SAA12", "name": "SAIT9AF8-SAA12"}, {"id": "SAA14", "name": "SAIT1AA9-SAA14"}, {
          "id": "ATL35",
          "name": "TTESSAAI-ATL35"
        }, {"id": "CWY1", "name": "CWYMOWBS-CWY1"}, {"id": "ATL76", "name": "TELEPAAI-ATL76"}, {
          "id": "DSL12",
          "name": "DSLFK242-DSL12"
        }, {"id": "ATL53", "name": "AAIATLTE-ATL53"}, {"id": "SAA11", "name": "SAIT9AA2-SAA11"}, {
          "id": "ATL62",
          "name": "TESSASCH-ATL62"
        }, {"id": "AUG1", "name": "ASDFGHJK-AUG1"}, {"id": "POI22", "name": "POIUY123-POI22"}, {
          "id": "SAA13",
          "name": "SAIT1AA9-SAA13"
        }, {"id": "BHY17", "name": "BHYTFRF3-BHY17"}, {"id": "LIS1", "name": "HOSTPROF-LIS1"}, {
          "id": "SIP1",
          "name": "ZXCVBNMK-SIP1"
        }, {"id": "ATL99", "name": "TEESTAAI-ATL43"}, {"id": "ATL64", "name": "FORLOAAJ-ATL64"}, {
          "id": "TAT33",
          "name": "TESAAISA-TAT33"
        }, {"id": "RAD10", "name": "INDIPUNE-RAD10"}, {"id": "RTW5", "name": "BHYTFRY4-RTW5"}, {
          "id": "JGS1",
          "name": "KSJKKKKK-JGS1"
        }, {"id": "ATL98", "name": "TEESTAAI-ATL43"}, {"id": "WAN1", "name": "LEIWANGW-WAN1"}, {
          "id": "ATL44",
          "name": "ATLSANAB-ATL44"
        }, {"id": "RTD2", "name": "BHYTFRk4-RTD2"}, {"id": "NIR1", "name": "ORFLMANA-NIR1"}, {
          "id": "ATL75",
          "name": "SANAAIRE-ATL75"
        }, {"id": "NUM1", "name": "QWERTYUI-NUM1"}, {"id": "hvf32", "name": "MDTWNJ21-hvf32"}, {
          "id": "RTZ4",
          "name": "BHYTFRZ6-RTZ4"
        }, {"id": "ATL56", "name": "ATLSANAC-ATL56"}, {"id": "AMS1", "name": "AMSTNLBW-AMS1"}, {
          "id": "RCT1",
          "name": "AMSTERNL-RCT1"
        }, {"id": "JAN1", "name": "ORFLMATT-JAN1"}, {"id": "ABC14", "name": "TESAAISA-ABC14"}, {
          "id": "TAT37",
          "name": "TESAAISD-TAT37"
        }, {"id": "MIC54", "name": "MICHIGAN-MIC54"}, {"id": "ABC11", "name": "ATLSANAI-ABC11"}, {
          "id": "AMF11",
          "name": "AMDOCS01-AMF11"
        }, {"id": "ATL63", "name": "ATLSANEW-ATL63"}, {"id": "ABC12", "name": "ATLSECIA-ABC12"}, {
          "id": "hvf20",
          "name": "MDTWNJ21-hvf20"
        }, {"id": "ABC15", "name": "AAITESAN-ABC15"}, {"id": "AVT1", "name": "AVTRFLHD-AVT1"}, {
          "id": "ATL34",
          "name": "ATLSANAI-ATL34"
        }],
        "categoryParameters": {
          "owningEntityList": [{
            "id": "aaa1",
            "name": "aaa1"
          }, {"id": "d61e6f2d-12fa-4cc2-91df-7c244011d6fc", "name": "WayneHolland"}, {
            "id": "Melissa",
            "name": "Melissa"
          }],
          "projectList": [{"id": "WATKINS", "name": "WATKINS"}, {"id": "x1", "name": "x1"}, {"id": "yyy1", "name": "yyy1"}],
          "lineOfBusinessList": [{"id": "ONAP", "name": "ONAP"}, {"id": "zzz1", "name": "zzz1"}],
          "platformList": [{"id": "platform", "name": "platform"}, {"id": "xxx1", "name": "xxx1"}]
        },
        "type": "[LCP_REGIONS_AND_TENANTS] Update",
        "subscribers": [{
          "id": "CAR_2020_ER",
          "name": "CAR_2020_ER",
          "isPermitted": true
        }, {
          "id": "21014aa2-526b-11e6-beb8-9e71128cae77",
          "name": "JULIO ERICKSON",
          "isPermitted": false
        }, {
          "id": "DHV1707-TestSubscriber-2",
          "name": "DALE BRIDGES",
          "isPermitted": false
        }, {"id": "DHV1707-TestSubscriber-1", "name": "LLOYD BRIDGES", "isPermitted": false}, {
          "id": "jimmy-example",
          "name": "JimmyExampleCust-20161102",
          "isPermitted": false
        }, {
          "id": "jimmy-example2",
          "name": "JimmyExampleCust-20161103",
          "isPermitted": false
        }, {
          "id": "ERICA5779-TestSub-PWT-102",
          "name": "ERICA5779-TestSub-PWT-102",
          "isPermitted": false
        }, {
          "id": "ERICA5779-TestSub-PWT-101",
          "name": "ERICA5779-TestSub-PWT-101",
          "isPermitted": false
        }, {
          "id": "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
          "name": "Emanuel",
          "isPermitted": false
        }, {
          "id": "ERICA5779-Subscriber-4",
          "name": "ERICA5779-Subscriber-5",
          "isPermitted": false
        }, {
          "id": "ERICA5779-TestSub-PWT-103",
          "name": "ERICA5779-TestSub-PWT-103",
          "isPermitted": false
        }, {
          "id": "ERICA5779-Subscriber-2",
          "name": "ERICA5779-Subscriber-2",
          "isPermitted": false
        }, {
          "id": "e433710f-9217-458d-a79d-1c7aff376d89",
          "name": "SILVIA ROBBINS",
          "isPermitted": true
        }, {
          "id": "ERICA5779-Subscriber-3",
          "name": "ERICA5779-Subscriber-3",
          "isPermitted": false
        }, {"id": "31739f3e-526b-11e6-beb8-9e71128cae77", "name": "CRAIG/ROBERTS", "isPermitted": false}]
      }
    };
  }
}

describe('Basic popup service', () => {
  let injector;
  let service: BasicPopupService;
  let genericFormService : GenericFormService;
  let defaultDataGeneratorService : DefaultDataGeneratorService;
  let fb : FormBuilder;
  let iframeService : IframeService;


  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      providers : [
        NetworkPopupService,
        BasicControlGenerator,
        NetworkControlGenerator,
        DefaultDataGeneratorService,
        GenericFormService,
        FormBuilder,
        IframeService,
        AaiService,
        LogService,
        BasicPopupService,
        {provide:FeatureFlagsService, useClass: MockFeatureFlagsService},
        {provide: NgRedux, useClass: MockReduxStore},
        {provide: HttpClient, useClass: MockAppStore},
        {provide: SdcUiServices.ModalService, useClass: MockModalService}
      ]
    });
    await TestBed.compileComponents();
    injector = getTestBed();
    service = injector.get(BasicPopupService);
    genericFormService = injector.get(GenericFormService);
    defaultDataGeneratorService = injector.get(DefaultDataGeneratorService);
    fb = injector.get(FormBuilder);
    iframeService = injector.get(IframeService);

  })().then(done).catch(done.fail));




  test('getDynamicInputs should list of formControl model', () => {
    const serviceId: string = '6b528779-44a3-4472-bdff-9cd15ec93450';
    const networkModel : string = 'ExtVL 0';
    const networkStoreKey : string = null;
    const controls : FormControlModel[] = service.getDynamicInputs(serviceId, networkModel, networkStoreKey, 'networks');
    expect(controls.length).toEqual(6);
    for(let i = 0 ; i < controls.length ; i++){
      expect(controls[i].type).toEqual('INPUT');
      expect(controls[i].isVisible).toBeTruthy();
    }
  });

  test('getModelFromResponse should return undefined if raw model not exist', () => {
    const result= service.getModelFromResponse({
      "modelType" : {
        "modelName" : null
      }
    }, 'modelType', 'modelName');
    expect(result).toBeUndefined();
  });
});
