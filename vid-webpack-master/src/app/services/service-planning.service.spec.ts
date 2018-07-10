import {ServicePlanningService} from "./service-planning.service";
import {ReflectiveInjector} from "@angular/core";
import {NgRedux} from "@angular-redux/store";
import {ServiceNodeTypes} from "../shared/models/ServiceNodeTypes";

export class MockAppStore<T> {
}

describe('Service planning service', () => {
  let injector;
  let service: ServicePlanningService;

  beforeEach(() => {

    let injector = ReflectiveInjector.resolveAndCreate([
      ServicePlanningService,
      {provide: NgRedux, useClass: MockAppStore}
    ]);

    service = injector.get(ServicePlanningService);
  });

  describe('#updateDynamicInputsVnfDataFromModel', () => {
    it('get vfModule instance params', (done: DoneFn) => {
      //get vfModule instance params
      let dynamicInputs = service.updateDynamicInputsVnfDataFromModel(ServiceNodeTypes.VFmodule, generateVFModule());
      expect(dynamicInputs).toEqual([{
        id: '2017488_adiodvpe0_vnf_config_template_version',
        type: 'string',
        name: '2017488_adiodvpe0_vnf_config_template_version',
        value: '17.2',
        isRequired: true,
        description: 'VPE Software Version'
      }, {
        id: '2017488_adiodvpe0_AIC_CLLI',
        type: 'string',
        name: '2017488_adiodvpe0_AIC_CLLI',
        value: 'ATLMY8GA',
        isRequired: true,
        description: 'AIC Site CLLI'
      }]);

      //get vfModule with no instance params should return empty array
      dynamicInputs = service.updateDynamicInputsVnfDataFromModel(ServiceNodeTypes.VFmodule, generateVFModule2);
      expect(dynamicInputs).toEqual([]);

      //get vf instance params should be undefined
      dynamicInputs = service.updateDynamicInputsVnfDataFromModel(ServiceNodeTypes.VF, generateVNF());
      expect(dynamicInputs).toEqual([]);
      done();
    });
  });

  describe('#isUserProvidedNaming', () => {
    it('get vfModule with generate ecompNaming should return userProvided false', (done: DoneFn) => {
      //get vfModule with generate ecompNaming should return userProvided false
      let isUserProvidedNaming = service.isUserProvidedNaming(ServiceNodeTypes.VFmodule, generateVFModule(), generateVNF());
      expect(isUserProvidedNaming).toBeFalsy();

      //get vfModule without generate ecompNaming should return userProvided true
      isUserProvidedNaming = service.isUserProvidedNaming(ServiceNodeTypes.VFmodule, generateVFModule(), generateVNF_ecompNamingFalse());
      expect(isUserProvidedNaming).toBeTruthy();

      //get vnf with generate ecompNaming should return userProvided false
      isUserProvidedNaming = service.isUserProvidedNaming(ServiceNodeTypes.VF, generateVNF(), null);
      expect(isUserProvidedNaming).toBeFalsy();

      //get vnf without generate ecompNaming should return userProvided true
      isUserProvidedNaming = service.isUserProvidedNaming(ServiceNodeTypes.VF, generateVNF_ecompNamingFalse(), null);
      expect(isUserProvidedNaming).toBeTruthy();
      done();
    });
  });

  function generateVFModule() {
    return {
      'uuid': '25284168-24bb-4698-8cb4-3f509146eca5',
      'invariantUuid': '7253ff5c-97f0-4b8b-937c-77aeb4d79aa1',
      'customizationUuid': 'f7e7c365-60cf-49a9-9ebf-a1aa11b9d401',
      'description': null,
      'name': '2017488AdiodVpe..ADIOD_vRE_BV..module-1',
      'version': '6',
      'modelCustomizationName': '2017488AdiodVpe..ADIOD_vRE_BV..module-1',
      'properties': {'minCountInstances': 0, 'maxCountInstances': null, 'initialCount': 0},
      'commands': {},
      'volumeGroupAllowed': true,
      'inputs': {
        '2017488_adiodvpe0_vnf_config_template_version': {
          'type': 'string',
          'description': 'VPE Software Version',
          'entry_schema': null,
          'constraints': [],
          'required': true,
          'default': '17.2'
        },
        '2017488_adiodvpe0_AIC_CLLI': {
          'type': 'string',
          'description': 'AIC Site CLLI',
          'entry_schema': null,
          'constraints': [],
          'required': true,
          'default': 'ATLMY8GA'
        }
      }
    };
  }

  function generateVFModule2() {
    return {
      'uuid': '0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a',
      'invariantUuid': 'eff8cc59-53a1-4101-aed7-8cf24ecf8339',
      'customizationUuid': '3cd946bb-50e0-40d8-96d3-c9023520b557',
      'description': null,
      'name': '2017488AdiodVpe..ADIOD_vPFE_BV..module-2',
      'version': '6',
      'modelCustomizationName': '2017488AdiodVpe..ADIOD_vPFE_BV..module-2',
      'properties': {'minCountInstances': 0, 'maxCountInstances': null, 'initialCount': 0},
      'commands': {},
      'volumeGroupAllowed': true,
      'inputs': {}
    };
  }

  function generateVNF() {
    return {
      'uuid': '0903e1c0-8e03-4936-b5c2-260653b96413',
      'invariantUuid': '00beb8f9-6d39-452f-816d-c709b9cbb87d',
      'description': 'Name ADIOD vPE Description The provider edge function for the ADIOD service supported by the Junipers VMX product Category Router Vendor Juniper Vendor Release Code 17.2 Owners Mary Fragale. Updated 9-25 to use v8.0 of the Juniper Valid 2 VLM',
      'name': '2017-388_ADIOD-vPE',
      'version': '1.0',
      'customizationUuid': '280dec31-f16d-488b-9668-4aae55d6648a',
      'inputs': {
        'vnf_config_template_version': {
          'type': 'string',
          'description': 'VPE Software Version',
          'entry_schema': null,
          'constraints': [],
          'required': true,
          'default': '17.2'
        },
        'bandwidth_units': {
          'type': 'string',
          'description': 'Units of bandwidth',
          'entry_schema': null,
          'constraints': [],
          'required': true,
          'default': 'Gbps'
        },
        'bandwidth': {
          'type': 'string',
          'description': 'Requested VPE bandwidth',
          'entry_schema': null,
          'constraints': [],
          'required': true,
          'default': '10'
        },
        'AIC_CLLI': {
          'type': 'string',
          'description': 'AIC Site CLLI',
          'entry_schema': null,
          'constraints': [],
          'required': true,
          'default': 'ATLMY8GA'
        },
        'ASN': {
          'type': 'string',
          'description': 'AV/PE',
          'entry_schema': null,
          'constraints': [],
          'required': true,
          'default': 'AV_vPE'
        },
        'vnf_instance_name': {
          'type': 'string',
          'description': 'The hostname assigned to the vpe.',
          'entry_schema': null,
          'constraints': [],
          'required': true,
          'default': 'mtnj309me6'
        }
      },
      'commands': {
        'vnf_config_template_version': {
          'displayName': 'vnf_config_template_version',
          'command': 'get_input',
          'inputName': '2017488_adiodvpe0_vnf_config_template_version'
        },
        'bandwidth_units': {
          'displayName': 'bandwidth_units',
          'command': 'get_input',
          'inputName': 'adiodvpe0_bandwidth_units'
        },
        'bandwidth': {'displayName': 'bandwidth', 'command': 'get_input', 'inputName': 'adiodvpe0_bandwidth'},
        'AIC_CLLI': {'displayName': 'AIC_CLLI', 'command': 'get_input', 'inputName': '2017488_adiodvpe0_AIC_CLLI'},
        'ASN': {'displayName': 'ASN', 'command': 'get_input', 'inputName': '2017488_adiodvpe0_ASN'},
        'vnf_instance_name': {
          'displayName': 'vnf_instance_name',
          'command': 'get_input',
          'inputName': '2017488_adiodvpe0_vnf_instance_name'
        }
      },
      'properties': {
        'vmxvre_retype': 'RE-VMX',
        'vnf_config_template_version': 'get_input:2017488_adiodvpe0_vnf_config_template_version',
        'sriov44_net_id': '48d399b3-11ee-48a8-94d2-f0ea94d6be8d',
        'int_ctl_net_id': '2f323477-6936-4d01-ac53-d849430281d9',
        'vmxvpfe_sriov41_0_port_mac': '00:11:22:EF:AC:DF',
        'int_ctl_net_name': 'VMX-INTXI',
        'vmx_int_ctl_prefix': '128.0.0.0',
        'sriov43_net_id': 'da349ca1-6de9-4548-be88-2d88e99bfef5',
        'sriov42_net_id': '760669ba-013d-4d9b-b0e7-4151fe2e6279',
        'sriov41_net_id': '25ad52d5-c165-40f8-b3b0-ddfc2373280a',
        'nf_type': 'vPE',
        'vmxvpfe_int_ctl_ip_1': '128.0.0.16',
        'is_AVPN_service': 'false',
        'vmx_RSG_name': 'vREXI-affinity',
        'vmx_int_ctl_forwarding': 'l2',
        'vmxvre_oam_ip_0': '10.40.123.5',
        'vmxvpfe_sriov44_0_port_mac': '00:11:22:EF:AC:DF',
        'vmxvpfe_sriov41_0_port_vlanstrip': 'false',
        'vmxvpfe_sriov42_0_port_vlanfilter': '4001',
        'vmxvpfe_sriov44_0_port_unknownunicastallow': 'true',
        'vmxvre_image_name_0': 'VRE-ENGINE_17.2-S2.1.qcow2',
        'vmxvre_instance': '0',
        'vmxvpfe_sriov43_0_port_mac': '00:11:22:EF:AC:DF',
        'vmxvre_flavor_name': 'ns.c1r16d32.v5',
        'vmxvpfe_volume_size_0': '40.0',
        'vmxvpfe_sriov43_0_port_vlanfilter': '4001',
        'nf_naming': '{ecomp_generated_naming=true}',
        'nf_naming_code': 'Navneet',
        'vmxvre_name_0': 'vREXI',
        'vmxvpfe_sriov42_0_port_vlanstrip': 'false',
        'vmxvpfe_volume_name_0': 'vPFEXI_FBVolume',
        'vmx_RSG_id': 'bd89a33c-13c3-4a04-8fde-1a57eb123141',
        'vmxvpfe_image_name_0': 'VPE_ROUTING-ENGINE_17.2R1-S2.1.qcow2',
        'vmxvpfe_sriov43_0_port_unknownunicastallow': 'true',
        'vmxvpfe_sriov44_0_port_unknownmulticastallow': 'true',
        'vmxvre_console': 'vidconsole',
        'vmxvpfe_sriov44_0_port_vlanfilter': '4001',
        'vmxvpfe_sriov42_0_port_mac': '00:11:22:EF:AC:DF',
        'vmxvpfe_volume_id_0': '47cede15-da2f-4397-a101-aa683220aff3',
        'vmxvpfe_sriov42_0_port_unknownmulticastallow': 'true',
        'vmxvpfe_sriov44_0_port_vlanstrip': 'false',
        'vf_module_id': '123',
        'nf_function': 'JAI',
        'vmxvpfe_sriov43_0_port_unknownmulticastallow': 'true',
        'vmxvre_int_ctl_ip_0': '128.0.0.1',
        'AIC_CLLI': 'get_input:2017488_adiodvpe0_AIC_CLLI',
        'vnf_name': 'mtnj309me6vre',
        'vmxvpfe_sriov41_0_port_unknownunicastallow': 'true',
        'vmxvre_volume_type_1': 'HITACHI',
        'vmxvpfe_sriov44_0_port_broadcastallow': 'true',
        'vmxvre_volume_type_0': 'HITACHI',
        'vmxvpfe_volume_type_0': 'HITACHI',
        'vmxvpfe_sriov43_0_port_broadcastallow': 'true',
        'bandwidth_units': 'get_input:adiodvpe0_bandwidth_units',
        'vnf_id': '123',
        'vmxvre_oam_prefix': '24',
        'availability_zone_0': 'mtpocfo-kvm-az01',
        'ASN': 'get_input:2017488_adiodvpe0_ASN',
        'vmxvre_chassis_i2cid': '161',
        'vmxvpfe_name_0': 'vPFEXI',
        'bandwidth': 'get_input:adiodvpe0_bandwidth',
        'availability_zone_max_count': '1',
        'vmxvre_volume_size_0': '45.0',
        'vmxvre_volume_size_1': '50.0',
        'vmxvpfe_sriov42_0_port_broadcastallow': 'true',
        'vmxvre_oam_gateway': '10.40.123.1',
        'vmxvre_volume_name_1': 'vREXI_FAVolume',
        'vmxvre_ore_present': '0',
        'vmxvre_volume_name_0': 'vREXI_FBVolume',
        'vmxvre_type': '0',
        'vnf_instance_name': 'get_input:2017488_adiodvpe0_vnf_instance_name',
        'vmxvpfe_sriov41_0_port_unknownmulticastallow': 'true',
        'oam_net_id': 'b95eeb1d-d55d-4827-abb4-8ebb94941429',
        'vmx_int_ctl_len': '24',
        'vmxvpfe_sriov43_0_port_vlanstrip': 'false',
        'vmxvpfe_sriov41_0_port_broadcastallow': 'true',
        'vmxvre_volume_id_1': '6e86797e-03cd-4fdc-ba72-2957119c746d',
        'vmxvpfe_sriov41_0_port_vlanfilter': '4001',
        'nf_role': 'Testing',
        'vmxvre_volume_id_0': 'f4eacb79-f687-4e9d-b760-21847c8bb15a',
        'vmxvpfe_sriov42_0_port_unknownunicastallow': 'true',
        'vmxvpfe_flavor_name': 'ns.c20r16d25.v5'
      },
      'type': 'VF',
      'modelCustomizationName': '2017-388_ADIOD-vPE 1',
      'vfModules': {},
      'volumeGroups': {}
    };
  }

  function generateVNF_ecompNamingFalse() {
    return {
      'uuid': '0903e1c0-8e03-4936-b5c2-260653b96413',
      'invariantUuid': '00beb8f9-6d39-452f-816d-c709b9cbb87d',
      'description': 'Name ADIOD vPE Description The provider edge function for the ADIOD service supported by the Junipers VMX product Category Router Vendor Juniper Vendor Release Code 17.2 Owners Mary Fragale. Updated 9-25 to use v8.0 of the Juniper Valid 2 VLM',
      'name': '2017-388_ADIOD-vPE',
      'version': '1.0',
      'customizationUuid': '280dec31-f16d-488b-9668-4aae55d6648a',
      'inputs': {
        'vnf_config_template_version': {
          'type': 'string',
          'description': 'VPE Software Version',
          'entry_schema': null,
          'constraints': [],
          'required': true,
          'default': '17.2'
        },
        'bandwidth_units': {
          'type': 'string',
          'description': 'Units of bandwidth',
          'entry_schema': null,
          'constraints': [],
          'required': true,
          'default': 'Gbps'
        },
        'bandwidth': {
          'type': 'string',
          'description': 'Requested VPE bandwidth',
          'entry_schema': null,
          'constraints': [],
          'required': true,
          'default': '10'
        },
        'AIC_CLLI': {
          'type': 'string',
          'description': 'AIC Site CLLI',
          'entry_schema': null,
          'constraints': [],
          'required': true,
          'default': 'ATLMY8GA'
        },
        'ASN': {
          'type': 'string',
          'description': 'AV/PE',
          'entry_schema': null,
          'constraints': [],
          'required': true,
          'default': 'AV_vPE'
        },
        'vnf_instance_name': {
          'type': 'string',
          'description': 'The hostname assigned to the vpe.',
          'entry_schema': null,
          'constraints': [],
          'required': true,
          'default': 'mtnj309me6'
        }
      },
      'commands': {
        'vnf_config_template_version': {
          'displayName': 'vnf_config_template_version',
          'command': 'get_input',
          'inputName': '2017488_adiodvpe0_vnf_config_template_version'
        },
        'bandwidth_units': {
          'displayName': 'bandwidth_units',
          'command': 'get_input',
          'inputName': 'adiodvpe0_bandwidth_units'
        },
        'bandwidth': {'displayName': 'bandwidth', 'command': 'get_input', 'inputName': 'adiodvpe0_bandwidth'},
        'AIC_CLLI': {'displayName': 'AIC_CLLI', 'command': 'get_input', 'inputName': '2017488_adiodvpe0_AIC_CLLI'},
        'ASN': {'displayName': 'ASN', 'command': 'get_input', 'inputName': '2017488_adiodvpe0_ASN'},
        'vnf_instance_name': {
          'displayName': 'vnf_instance_name',
          'command': 'get_input',
          'inputName': '2017488_adiodvpe0_vnf_instance_name'
        }
      },
      'properties': {
        'ecomp_generated_naming': "false",
        'vmxvre_retype': 'RE-VMX',
        'vnf_config_template_version': 'get_input:2017488_adiodvpe0_vnf_config_template_version',
        'sriov44_net_id': '48d399b3-11ee-48a8-94d2-f0ea94d6be8d',
        'int_ctl_net_id': '2f323477-6936-4d01-ac53-d849430281d9',
        'vmxvpfe_sriov41_0_port_mac': '00:11:22:EF:AC:DF',
        'int_ctl_net_name': 'VMX-INTXI',
        'vmx_int_ctl_prefix': '128.0.0.0',
        'sriov43_net_id': 'da349ca1-6de9-4548-be88-2d88e99bfef5',
        'sriov42_net_id': '760669ba-013d-4d9b-b0e7-4151fe2e6279',
        'sriov41_net_id': '25ad52d5-c165-40f8-b3b0-ddfc2373280a',
        'nf_type': 'vPE',
        'vmxvpfe_int_ctl_ip_1': '128.0.0.16',
        'is_AVPN_service': 'false',
        'vmx_RSG_name': 'vREXI-affinity',
        'vmx_int_ctl_forwarding': 'l2',
        'vmxvre_oam_ip_0': '10.40.123.5',
        'vmxvpfe_sriov44_0_port_mac': '00:11:22:EF:AC:DF',
        'vmxvpfe_sriov41_0_port_vlanstrip': 'false',
        'vmxvpfe_sriov42_0_port_vlanfilter': '4001',
        'vmxvpfe_sriov44_0_port_unknownunicastallow': 'true',
        'vmxvre_image_name_0': 'VRE-ENGINE_17.2-S2.1.qcow2',
        'vmxvre_instance': '0',
        'vmxvpfe_sriov43_0_port_mac': '00:11:22:EF:AC:DF',
        'vmxvre_flavor_name': 'ns.c1r16d32.v5',
        'vmxvpfe_volume_size_0': '40.0',
        'vmxvpfe_sriov43_0_port_vlanfilter': '4001',
        'nf_naming': '{ecomp_generated_naming=false}',
        'nf_naming_code': 'Navneet',
        'vmxvre_name_0': 'vREXI',
        'vmxvpfe_sriov42_0_port_vlanstrip': 'false',
        'vmxvpfe_volume_name_0': 'vPFEXI_FBVolume',
        'vmx_RSG_id': 'bd89a33c-13c3-4a04-8fde-1a57eb123141',
        'vmxvpfe_image_name_0': 'VPE_ROUTING-ENGINE_17.2R1-S2.1.qcow2',
        'vmxvpfe_sriov43_0_port_unknownunicastallow': 'true',
        'vmxvpfe_sriov44_0_port_unknownmulticastallow': 'true',
        'vmxvre_console': 'vidconsole',
        'vmxvpfe_sriov44_0_port_vlanfilter': '4001',
        'vmxvpfe_sriov42_0_port_mac': '00:11:22:EF:AC:DF',
        'vmxvpfe_volume_id_0': '47cede15-da2f-4397-a101-aa683220aff3',
        'vmxvpfe_sriov42_0_port_unknownmulticastallow': 'true',
        'vmxvpfe_sriov44_0_port_vlanstrip': 'false',
        'vf_module_id': '123',
        'nf_function': 'JAI',
        'vmxvpfe_sriov43_0_port_unknownmulticastallow': 'true',
        'vmxvre_int_ctl_ip_0': '128.0.0.1',
        'AIC_CLLI': 'get_input:2017488_adiodvpe0_AIC_CLLI',
        'vnf_name': 'mtnj309me6vre',
        'vmxvpfe_sriov41_0_port_unknownunicastallow': 'true',
        'vmxvre_volume_type_1': 'HITACHI',
        'vmxvpfe_sriov44_0_port_broadcastallow': 'true',
        'vmxvre_volume_type_0': 'HITACHI',
        'vmxvpfe_volume_type_0': 'HITACHI',
        'vmxvpfe_sriov43_0_port_broadcastallow': 'true',
        'bandwidth_units': 'get_input:adiodvpe0_bandwidth_units',
        'vnf_id': '123',
        'vmxvre_oam_prefix': '24',
        'availability_zone_0': 'mtpocfo-kvm-az01',
        'ASN': 'get_input:2017488_adiodvpe0_ASN',
        'vmxvre_chassis_i2cid': '161',
        'vmxvpfe_name_0': 'vPFEXI',
        'bandwidth': 'get_input:adiodvpe0_bandwidth',
        'availability_zone_max_count': '1',
        'vmxvre_volume_size_0': '45.0',
        'vmxvre_volume_size_1': '50.0',
        'vmxvpfe_sriov42_0_port_broadcastallow': 'true',
        'vmxvre_oam_gateway': '10.40.123.1',
        'vmxvre_volume_name_1': 'vREXI_FAVolume',
        'vmxvre_ore_present': '0',
        'vmxvre_volume_name_0': 'vREXI_FBVolume',
        'vmxvre_type': '0',
        'vnf_instance_name': 'get_input:2017488_adiodvpe0_vnf_instance_name',
        'vmxvpfe_sriov41_0_port_unknownmulticastallow': 'true',
        'oam_net_id': 'b95eeb1d-d55d-4827-abb4-8ebb94941429',
        'vmx_int_ctl_len': '24',
        'vmxvpfe_sriov43_0_port_vlanstrip': 'false',
        'vmxvpfe_sriov41_0_port_broadcastallow': 'true',
        'vmxvre_volume_id_1': '6e86797e-03cd-4fdc-ba72-2957119c746d',
        'vmxvpfe_sriov41_0_port_vlanfilter': '4001',
        'nf_role': 'Testing',
        'vmxvre_volume_id_0': 'f4eacb79-f687-4e9d-b760-21847c8bb15a',
        'vmxvpfe_sriov42_0_port_unknownunicastallow': 'true',
        'vmxvpfe_flavor_name': 'ns.c20r16d25.v5'
      },
      'type': 'VF',
      'modelCustomizationName': '2017-388_ADIOD-vPE 1',
      'vfModules': {},
      'volumeGroups': {}
    };
  }

});




