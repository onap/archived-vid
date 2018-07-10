import {TestBed, getTestBed} from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import {AvailableModelsTreeService} from './available-models-tree.service';
import {ServicePlanningService} from "../../services/service-planning.service";
import {ServiceNodeTypes} from "../../shared/models/ServiceNodeTypes";
import {NgRedux} from "@angular-redux/store";
import {MockAppStore} from "../../services/service-planning.service.spec";

describe('Available Models Tree Service', () => {
  let injector;
  let service: AvailableModelsTreeService;
  let httpMock: HttpTestingController;

  beforeEach(() => {

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AvailableModelsTreeService,
        ServicePlanningService,
        {provide: NgRedux, useClass: MockAppStore}]
    });

    injector = getTestBed();
    service = injector.get(AvailableModelsTreeService);
    httpMock = injector.get(HttpTestingController);
  });

  describe('#shouldShowAddIcon', () => {
    it('should return true if number of current vnf modules is under the max', (done: DoneFn) => {
      let treeNode = {
        data: {
          children: [],
          name: 'vf_vmee0..VfVmee..base_vmme..module-0'
        }
      };

      let serviceHierarchy = getSericeServiceHierarchy();
      let result = service.shouldShowAddIcon(treeNode, serviceHierarchy, '6e59c5de-f052-46fa-aa7e-2fca9d674c44', 0);
      expect(result).toBeTruthy();
      done();
    });

    it('should return false if number of current vnf modules are more than max', (done: DoneFn) => {
      let treeNode = {
        data: {
          children: [],
          name: 'vf_vmee0..VfVmee..base_vmme..module-0'
        }
      };

      let serviceHierarchy = getSericeServiceHierarchy();
      let result = service.shouldShowAddIcon(treeNode, serviceHierarchy, '6e59c5de-f052-46fa-aa7e-2fca9d674c44', 2);
      expect(result).toBeFalsy();
      done();
    });

    it('should return true if number of current vnf modules are more than max and max is null', (done: DoneFn) => {
      let treeNode = {
        data: {
          children: [],
          name: 'vf_vmee0..VfVmee..base_vmme..module-0'
        }
      };

      let serviceHierarchy = getSericeServiceHierarchy();
      let result = service.shouldShowAddIcon(treeNode, serviceHierarchy, '6e59c5de-f052-46fa-aa7e-2fca9d674c44', 0);
      expect(result).toBeTruthy();
      done();
    });
  });

  describe('#shouldOpenModalDialogOnAddInstance', () => {
    let serviceHierarchy = getSericeServiceHierarchy();

    it('should open popup on add instance', (done: DoneFn) => {
      // add vnf should return true
      let result = service.shouldOpenDialog(ServiceNodeTypes.VF, [], false);
      expect(result).toBeTruthy();

      //  add vfModule with user provided naming should return true
      result = service.shouldOpenDialog(ServiceNodeTypes.VFmodule, [], true);
      expect(result).toBeTruthy();

      //  add vfModule with dynamicInputs without defaultValues should return true
      result = service.shouldOpenDialog(ServiceNodeTypes.VFmodule, [{
        id: '2017488_adiodvpe0_vnf_config_template_version',
        type: 'string',
        name: '2017488_adiodvpe0_vnf_config_template_version',
        isRequired: true,
        description: 'VPE Software Version'
      }], false);
      expect(result).toBeTruthy();

      // add vfModule with dynamicInputs with defaultValues should return false
      result = service.shouldOpenDialog(ServiceNodeTypes.VFmodule, [{
        id: '2017488_adiodvpe0_vnf_config_template_version',
        type: 'string',
        name: '2017488_adiodvpe0_vnf_config_template_version',
        value: '17.2',
        isRequired: true,
        description: 'VPE Software Version'
      }], false);
      expect(result).toBeFalsy();
      done();
    });
  });

  function getSericeServiceHierarchy() {
    return JSON.parse(JSON.stringify(
      {
        '6e59c5de-f052-46fa-aa7e-2fca9d674c44': {
          'service': {
            'uuid': '6e59c5de-f052-46fa-aa7e-2fca9d674c44',
            'invariantUuid': 'e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0',
            'name': 'ComplexService',
            'version': '1.0',
            'toscaModelURL': null,
            'category': 'Mobility',
            'serviceType': '',
            'serviceRole': '',
            'description': 'ComplexService',
            'serviceEcompNaming': 'true',
            'instantiationType': 'Macro',
            'inputs': {}
          },
          'vnfs': {
            'VF_vMee 0': {
              'uuid': 'd6557200-ecf2-4641-8094-5393ae3aae60',
              'invariantUuid': '4160458e-f648-4b30-a176-43881ffffe9e',
              'description': 'VSP_vMee',
              'name': 'VF_vMee',
              'version': '2.0',
              'customizationUuid': '91415b44-753d-494c-926a-456a9172bbb9',
              'inputs': {},
              'commands': {},
              'properties': {
                'gpb2_Internal2_mac': '00:80:37:0E:02:22',
                'sctp-b-ipv6-egress_src_start_port': '0',
                'sctp-a-ipv6-egress_rule_application': 'any',
                'Internal2_allow_transit': 'true',
                'sctp-b-IPv6_ethertype': 'IPv6',
                'sctp-a-egress_rule_application': 'any',
                'sctp-b-ingress_action': 'pass',
                'sctp-b-ingress_rule_protocol': 'icmp',
                'ncb2_Internal1_mac': '00:80:37:0E:0F:12',
                'sctp-b-ipv6-ingress-src_start_port': '0.0',
                'ncb1_Internal2_mac': '00:80:37:0E:09:12',
                'fsb_volume_size_0': '320.0',
                'sctp-b-egress_src_addresses': 'local',
                'sctp-a-ipv6-ingress_ethertype': 'IPv4',
                'sctp-a-ipv6-ingress-dst_start_port': '0',
                'sctp-b-ipv6-ingress_rule_application': 'any',
                'domain_name': 'default-domain',
                'sctp-a-ingress_rule_protocol': 'icmp',
                'sctp-b-egress-src_start_port': '0.0',
                'sctp-a-egress_src_addresses': 'local',
                'sctp-b-display_name': 'epc-sctp-b-ipv4v6-sec-group',
                'sctp-a-egress-src_start_port': '0.0',
                'sctp-a-ingress_ethertype': 'IPv4',
                'sctp-b-ipv6-ingress-dst_end_port': '65535',
                'sctp-b-dst_subnet_prefix_v6': '::',
                'nf_naming': '{ecomp_generated_naming=true}',
                'sctp-a-ipv6-ingress_src_subnet_prefix': '0.0.0.0',
                'sctp-b-egress-dst_start_port': '0.0',
                'ncb_flavor_name': 'nv.c20r64d1',
                'gpb1_Internal1_mac': '00:80:37:0E:01:22',
                'sctp-b-egress_dst_subnet_prefix_len': '0.0',
                'Internal2_net_cidr': '169.255.0.0',
                'sctp-a-ingress-dst_start_port': '0.0',
                'sctp-a-egress-dst_start_port': '0.0',
                'fsb1_Internal2_mac': '00:80:37:0E:0B:12',
                'sctp-a-egress_ethertype': 'IPv4',
                'vlc_st_service_mode': 'in-network-nat',
                'sctp-a-ipv6-egress_ethertype': 'IPv4',
                'sctp-a-egress-src_end_port': '65535.0',
                'sctp-b-ipv6-egress_rule_application': 'any',
                'sctp-b-egress_action': 'pass',
                'sctp-a-ingress-src_subnet_prefix_len': '0.0',
                'sctp-b-ipv6-ingress-src_end_port': '65535.0',
                'sctp-b-name': 'epc-sctp-b-ipv4v6-sec-group',
                'fsb2_Internal1_mac': '00:80:37:0E:0D:12',
                'sctp-a-ipv6-ingress-src_start_port': '0.0',
                'sctp-b-ipv6-egress_ethertype': 'IPv4',
                'Internal1_net_cidr': '169.253.0.0',
                'sctp-a-egress_dst_subnet_prefix': '0.0.0.0',
                'fsb_flavor_name': 'nv.c20r64d1',
                'sctp_rule_protocol': '132',
                'sctp-b-ipv6-ingress_src_subnet_prefix_len': '0',
                'sctp-a-ipv6-ingress_rule_application': 'any',
                'sctp-a-IPv6_ethertype': 'IPv6',
                'vlc2_Internal1_mac': '00:80:37:0E:02:12',
                'vlc_st_virtualization_type': 'virtual-machine',
                'sctp-b-ingress-dst_start_port': '0.0',
                'sctp-b-ingress-dst_end_port': '65535.0',
                'sctp-a-ipv6-ingress-src_end_port': '65535.0',
                'sctp-a-display_name': 'epc-sctp-a-ipv4v6-sec-group',
                'sctp-b-ingress_rule_application': 'any',
                'int2_sec_group_name': 'int2-sec-group',
                'vlc_flavor_name': 'nd.c16r64d1',
                'sctp-b-ipv6-egress_src_addresses': 'local',
                'vlc_st_interface_type_int1': 'other1',
                'sctp-b-egress-src_end_port': '65535.0',
                'sctp-a-ipv6-egress-dst_start_port': '0',
                'vlc_st_interface_type_int2': 'other2',
                'sctp-a-ipv6-egress_rule_protocol': 'any',
                'Internal2_shared': 'false',
                'sctp-a-ipv6-egress_dst_subnet_prefix_len': '0',
                'Internal2_rpf': 'disable',
                'vlc1_Internal1_mac': '00:80:37:0E:01:12',
                'sctp-b-ipv6-egress_src_end_port': '65535',
                'sctp-a-ipv6-egress_src_addresses': 'local',
                'sctp-a-ingress-dst_end_port': '65535.0',
                'sctp-a-ipv6-egress_src_end_port': '65535',
                'Internal1_forwarding_mode': 'l2',
                'Internal2_dhcp': 'false',
                'sctp-a-dst_subnet_prefix_v6': '::',
                'pxe_image_name': 'MME_PXE-Boot_16ACP04_GA.qcow2',
                'vlc_st_interface_type_gtp': 'other0',
                'ncb1_Internal1_mac': '00:80:37:0E:09:12',
                'sctp-b-src_subnet_prefix_v6': '::',
                'sctp-a-egress_dst_subnet_prefix_len': '0.0',
                'int1_sec_group_name': 'int1-sec-group',
                'Internal1_dhcp': 'false',
                'sctp-a-ipv6-egress_dst_end_port': '65535',
                'Internal2_forwarding_mode': 'l2',
                'fsb2_Internal2_mac': '00:80:37:0E:0D:12',
                'sctp-b-egress_dst_subnet_prefix': '0.0.0.0',
                'Internal1_net_cidr_len': '17',
                'gpb2_Internal1_mac': '00:80:37:0E:02:22',
                'sctp-b-ingress-src_subnet_prefix_len': '0.0',
                'sctp-a-ingress_dst_addresses': 'local',
                'sctp-a-egress_action': 'pass',
                'fsb_volume_type_0': 'SF-Default-SSD',
                'ncb2_Internal2_mac': '00:80:37:0E:0F:12',
                'vlc_st_interface_type_sctp_a': 'left',
                'vlc_st_interface_type_sctp_b': 'right',
                'sctp-a-src_subnet_prefix_v6': '::',
                'vlc_st_version': '2',
                'sctp-b-egress_ethertype': 'IPv4',
                'sctp-a-ingress_rule_application': 'any',
                'gpb1_Internal2_mac': '00:80:37:0E:01:22',
                'instance_ip_family_v6': 'v6',
                'sctp-a-ipv6-egress_src_start_port': '0',
                'sctp-b-ingress-src_start_port': '0.0',
                'sctp-b-ingress_dst_addresses': 'local',
                'fsb1_Internal1_mac': '00:80:37:0E:0B:12',
                'vlc_st_interface_type_oam': 'management',
                'multi_stage_design': 'false',
                'oam_sec_group_name': 'oam-sec-group',
                'Internal2_net_gateway': '169.255.0.3',
                'sctp-a-ipv6-ingress-dst_end_port': '65535',
                'sctp-b-ipv6-egress-dst_start_port': '0',
                'Internal1_net_gateway': '169.253.0.3',
                'sctp-b-ipv6-egress_rule_protocol': 'any',
                'gtp_sec_group_name': 'gtp-sec-group',
                'sctp-a-ipv6-egress_dst_subnet_prefix': '0.0.0.0',
                'sctp-b-ipv6-egress_dst_subnet_prefix_len': '0',
                'sctp-a-ipv6-ingress_dst_addresses': 'local',
                'sctp-a-egress_rule_protocol': 'icmp',
                'sctp-b-ipv6-egress_action': 'pass',
                'sctp-a-ipv6-egress_action': 'pass',
                'Internal1_shared': 'false',
                'sctp-b-ipv6-ingress_rule_protocol': 'any',
                'Internal2_net_cidr_len': '17',
                'sctp-a-name': 'epc-sctp-a-ipv4v6-sec-group',
                'sctp-a-ingress-src_end_port': '65535.0',
                'sctp-b-ipv6-ingress_src_subnet_prefix': '0.0.0.0',
                'sctp-a-egress-dst_end_port': '65535.0',
                'sctp-a-ingress_action': 'pass',
                'sctp-b-egress_rule_protocol': 'icmp',
                'sctp-b-ipv6-ingress_action': 'pass',
                'vlc_st_service_type': 'firewall',
                'sctp-b-ipv6-egress_dst_end_port': '65535',
                'sctp-b-ipv6-ingress-dst_start_port': '0',
                'vlc2_Internal2_mac': '00:80:37:0E:02:12',
                'vlc_st_availability_zone': 'true',
                'fsb_volume_image_name_1': 'MME_FSB2_16ACP04_GA.qcow2',
                'sctp-b-ingress-src_subnet_prefix': '0.0.0.0',
                'sctp-a-ipv6-ingress_src_subnet_prefix_len': '0',
                'Internal1_allow_transit': 'true',
                'gpb_flavor_name': 'nv.c20r64d1',
                'availability_zone_max_count': '1',
                'fsb_volume_image_name_0': 'MME_FSB1_16ACP04_GA.qcow2',
                'sctp-b-ipv6-ingress_dst_addresses': 'local',
                'sctp-b-ipv6-egress_dst_subnet_prefix': '0.0.0.0',
                'sctp-b-ipv6-ingress_ethertype': 'IPv4',
                'vlc1_Internal2_mac': '00:80:37:0E:01:12',
                'sctp-a-ingress-src_subnet_prefix': '0.0.0.0',
                'sctp-a-ipv6-ingress_action': 'pass',
                'Internal1_rpf': 'disable',
                'sctp-b-ingress_ethertype': 'IPv4',
                'sctp-b-egress_rule_application': 'any',
                'sctp-b-ingress-src_end_port': '65535.0',
                'sctp-a-ipv6-ingress_rule_protocol': 'any',
                'sctp-a-ingress-src_start_port': '0.0',
                'sctp-b-egress-dst_end_port': '65535.0'
              },
              'type': 'VF',
              'modelCustomizationName': 'VF_vMee 0',
              'vfModules': {
                'vf_vmee0..VfVmee..vmme_vlc..module-1': {
                  'uuid': '522159d5-d6e0-4c2a-aa44-5a542a12a830',
                  'invariantUuid': '98a7c88b-b577-476a-90e4-e25a5871e02b',
                  'customizationUuid': '55b1be94-671a-403e-a26c-667e9c47d091',
                  'description': null,
                  'name': 'VfVmee..vmme_vlc..module-1',
                  'version': '2',
                  'modelCustomizationName': 'VfVmee..vmme_vlc..module-1',
                  'properties': {'minCountInstances': 0, 'maxCountInstances': null, 'initialCount': 0},
                  'commands': {},
                  'volumeGroupAllowed': false
                },
                'vf_vmee0..VfVmee..vmme_gpb..module-2': {
                  'uuid': '41708296-e443-4c71-953f-d9a010f059e1',
                  'invariantUuid': '1cca90b8-3490-495e-87da-3f3e4c57d5b9',
                  'customizationUuid': '6add59e0-7fe1-4bc4-af48-f8812422ae7c',
                  'description': null,
                  'name': 'VfVmee..vmme_gpb..module-2',
                  'version': '2',
                  'modelCustomizationName': 'VfVmee..vmme_gpb..module-2',
                  'properties': {'minCountInstances': 0, 'maxCountInstances': null, 'initialCount': 0},
                  'commands': {},
                  'volumeGroupAllowed': false
                },
                'vf_vmee0..VfVmee..base_vmme..module-0': {
                  'uuid': 'a27f5cfc-7f12-4f99-af08-0af9c3885c87',
                  'invariantUuid': 'a6f9e51a-2b35-416a-ae15-15e58d61f36d',
                  'customizationUuid': 'f8c040f1-7e51-4a11-aca8-acf256cfd861',
                  'description': null,
                  'name': 'VfVmee..base_vmme..module-0',
                  'version': '2',
                  'modelCustomizationName': 'VfVmee..base_vmme..module-0',
                  'properties': {'minCountInstances': 1, 'maxCountInstances': 1, 'initialCount': 1},
                  'commands': {},
                  'volumeGroupAllowed': true
                }
              },
              'volumeGroups': {
                'vf_vmee0..VfVmee..base_vmme..module-0': {
                  'uuid': 'a27f5cfc-7f12-4f99-af08-0af9c3885c87',
                  'invariantUuid': 'a6f9e51a-2b35-416a-ae15-15e58d61f36d',
                  'customizationUuid': 'f8c040f1-7e51-4a11-aca8-acf256cfd861',
                  'description': null,
                  'name': 'VfVmee..base_vmme..module-0',
                  'version': '2',
                  'modelCustomizationName': 'VfVmee..base_vmme..module-0',
                  'properties': {'minCountInstances': 1, 'maxCountInstances': 1, 'initialCount': 1}
                }
              }
            }
          },
          'networks': {
            'ExtVL 0': {
              'uuid': 'ddc3f20c-08b5-40fd-af72-c6d14636b986',
              'invariantUuid': '379f816b-a7aa-422f-be30-17114ff50b7c',
              'description': 'ECOMP generic virtual link (network) base type for all other service-level and global networks',
              'name': 'ExtVL',
              'version': '37.0',
              'customizationUuid': '94fdd893-4a36-4d70-b16a-ec29c54c184f',
              'inputs': {},
              'commands': {},
              'properties': {
                'network_assignments': '{is_external_network=false, ipv4_subnet_default_assignment={min_subnets_count=1}, ecomp_generated_network_assignment=false, ipv6_subnet_default_assignment={min_subnets_count=1}}',
                'exVL_naming': '{ecomp_generated_naming=true}',
                'network_flows': '{is_network_policy=false, is_bound_to_vpn=false}',
                'network_homing': '{ecomp_selected_instance_node_target=false}'
              },
              'type': 'VL',
              'modelCustomizationName': 'ExtVL 0'
            }
          },
          'configurations': {
            'Port Mirroring Configuration By Policy 0': {
              'uuid': 'b4398538-e89d-4f13-b33d-ca323434ba50',
              'invariantUuid': '6ef0ca40-f366-4897-951f-abd65d25f6f7',
              'description': 'A port mirroring configuration by policy object',
              'name': 'Port Mirroring Configuration By Policy',
              'version': '27.0',
              'customizationUuid': '3c3b7b8d-8669-4b3b-8664-61970041fad2',
              'inputs': {},
              'commands': {},
              'properties': {},
              'type': 'Configuration',
              'modelCustomizationName': 'Port Mirroring Configuration By Policy 0',
              'sourceNodes': [],
              'collectorNodes': null,
              'configurationByPolicy': false
            }
          },
          'serviceProxies': {},
          'vfModules': {
            'vf_vmee0..VfVmee..vmme_vlc..module-1': {
              'uuid': '522159d5-d6e0-4c2a-aa44-5a542a12a830',
              'invariantUuid': '98a7c88b-b577-476a-90e4-e25a5871e02b',
              'customizationUuid': '55b1be94-671a-403e-a26c-667e9c47d091',
              'description': null,
              'name': 'VfVmee..vmme_vlc..module-1',
              'version': '2',
              'modelCustomizationName': 'VfVmee..vmme_vlc..module-1',
              'properties': {'minCountInstances': 0, 'maxCountInstances': null, 'initialCount': 0},
              'commands': {},
              'volumeGroupAllowed': false
            },
            'vf_vmee0..VfVmee..vmme_gpb..module-2': {
              'uuid': '41708296-e443-4c71-953f-d9a010f059e1',
              'invariantUuid': '1cca90b8-3490-495e-87da-3f3e4c57d5b9',
              'customizationUuid': '6add59e0-7fe1-4bc4-af48-f8812422ae7c',
              'description': null,
              'name': 'VfVmee..vmme_gpb..module-2',
              'version': '2',
              'modelCustomizationName': 'VfVmee..vmme_gpb..module-2',
              'properties': {'minCountInstances': 0, 'maxCountInstances': null, 'initialCount': 0},
              'commands': {},
              'volumeGroupAllowed': false
            },
            'vf_vmee0..VfVmee..base_vmme..module-0': {
              'uuid': 'a27f5cfc-7f12-4f99-af08-0af9c3885c87',
              'invariantUuid': 'a6f9e51a-2b35-416a-ae15-15e58d61f36d',
              'customizationUuid': 'f8c040f1-7e51-4a11-aca8-acf256cfd861',
              'description': null,
              'name': 'VfVmee..base_vmme..module-0',
              'version': '2',
              'modelCustomizationName': 'VfVmee..base_vmme..module-0',
              'properties': {'minCountInstances': 1, 'maxCountInstances': 1, 'initialCount': 1},
              'commands': {},
              'volumeGroupAllowed': true
            }
          },
          'volumeGroups': {
            'vf_vmee0..VfVmee..base_vmme..module-0': {
              'uuid': 'a27f5cfc-7f12-4f99-af08-0af9c3885c87',
              'invariantUuid': 'a6f9e51a-2b35-416a-ae15-15e58d61f36d',
              'customizationUuid': 'f8c040f1-7e51-4a11-aca8-acf256cfd861',
              'description': null,
              'name': 'VfVmee..base_vmme..module-0',
              'version': '2',
              'modelCustomizationName': 'VfVmee..base_vmme..module-0',
              'properties': {'minCountInstances': 1, 'maxCountInstances': 1, 'initialCount': 1}
            }
          },
          'pnfs': {}
        }
      }
    ));
  }

});
