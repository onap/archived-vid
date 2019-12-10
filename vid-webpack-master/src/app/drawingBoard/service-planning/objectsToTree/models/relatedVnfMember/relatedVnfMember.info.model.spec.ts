import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {getTestBed, TestBed} from "@angular/core/testing";
import {MockNgRedux, NgReduxTestingModule} from "@angular-redux/store/testing";
import {DynamicInputsService} from "../../dynamicInputs.service";
import {VNFModel} from "../../../../../shared/models/vnfModel";
import {SharedTreeService} from "../../shared.tree.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../../shared/store/reducers";
import {DefaultDataGeneratorService} from "../../../../../shared/services/defaultDataServiceGenerator/default.data.generator.service";
import {DialogService} from "ng2-bootstrap-modal";
import {VfModulePopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/vfModule/vfModule.popup.service";
import {VnfPopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/vnf/vnf.popup.service";
import {DuplicateService} from "../../../duplicate/duplicate.service";
import {IframeService} from "../../../../../shared/utils/iframe.service";
import {RelatedVnfMemberInfoModel} from "./relatedVnfMember.info.model";
import {VfModuleUpgradePopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/vfModuleUpgrade/vfModule.upgrade.popuop.service";


class MockAppStore<T> {
  getState() {
    return {}
  }
}


describe('Related Vnf member Model Info', () => {
  let injector;
  let httpMock: HttpTestingController;
  let  _dynamicInputsService : DynamicInputsService;
  let  _sharedTreeService : SharedTreeService;

  let _store : NgRedux<AppState>;
  let relatedVnfMemeber: RelatedVnfMemberInfoModel;
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NgReduxTestingModule],
      providers: [
        MockNgRedux,
        DynamicInputsService,
        DialogService,
        VfModulePopupService,
        VfModuleUpgradePopupService,
        VnfPopupService,
        DefaultDataGeneratorService,
        SharedTreeService,
        DuplicateService,
        IframeService]
    }).compileComponents();

    injector = getTestBed();
    _sharedTreeService = injector.get(SharedTreeService);
    _store = injector.get(NgRedux);

    relatedVnfMemeber = new RelatedVnfMemberInfoModel(
      _sharedTreeService,
      _dynamicInputsService,
      _store);
  });

  test('relatedVnfMemeber should be defined', () => {
    expect(relatedVnfMemeber).toBeDefined();
  });

  test('RelatedVnfMemeber should defined extra details', () => {
    expect(relatedVnfMemeber.name).toEqual('vnfs');
    expect(relatedVnfMemeber.type).toEqual('relatedVnfMember');
  });

  test('isEcompGeneratedNaming should return true if isEcompGeneratedNaming is "true" ', () => {
    let isEcompGeneratedNaming: boolean = relatedVnfMemeber.isEcompGeneratedNaming(<any>{
      properties: {
        ecomp_generated_naming: 'true'
      }
    });
    expect(isEcompGeneratedNaming).toBeTruthy();
  });

  test('isEcompGeneratedNaming should return false if isEcompGeneratedNaming is "false"', () => {
    let isEcompGeneratedNaming: boolean = relatedVnfMemeber.isEcompGeneratedNaming({
      properties: {
        ecomp_generated_naming: 'false'
      }
    });
    expect(isEcompGeneratedNaming).toBeFalsy();
  });

  test('isEcompGeneratedNaming should return false if isEcompGeneratedNaming is not defined', () => {
    let isEcompGeneratedNaming: boolean = relatedVnfMemeber.isEcompGeneratedNaming({
      properties: {
      }
    });
    expect(isEcompGeneratedNaming).toBeFalsy();
  });


  test('getTooltip should return "VF"', () => {
    let tooltip: string = relatedVnfMemeber.getTooltip();
    expect(tooltip).toEqual('VF');
  });

  test('getType should return "VF"', () => {
    let tooltip: string = relatedVnfMemeber.getType();
    expect(tooltip).toEqual('VF');
  });

  test('getNextLevelObject should be null', () => {
    let nextLevel = relatedVnfMemeber.getNextLevelObject();
    expect(nextLevel).toBeNull();
  });

  test('getModel should return VNF model', () => {
    let model: VNFModel = relatedVnfMemeber.getModel('2017-388_PASQUALE-vPE 1', <any>{
      originalName : '2017-388_PASQUALE-vPE 1'
    }, getServiceHierarchy());
    expect(model.type).toEqual('VF');
  });


  test('getMenuAction: delete', ()=>{
    let node = {"modelId":"d6557200-ecf2-4641-8094-5393ae3aae60","missingData":true,"action":"None","inMaint":true,"name":"jlfBwIks283yKlCD8","modelName":"VF_vGeraldine 0","type":"VF","isEcompGeneratedNaming":true,"networkStoreKey":"VF_vGeraldine 0:004","vnfStoreKey":"VF_vGeraldine 0:004","typeName":"VNF"};
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";
    let result = relatedVnfMemeber.getMenuAction(<any>node, serviceModelId);
    spyOn(result['delete'], 'method');
    expect(result['delete']).toBeDefined();
    expect(result['delete'].visible).toBeTruthy();
    expect(result['delete'].enable).toBeTruthy();
    result['delete']['method'](node, serviceModelId);
    expect(result['delete']['method']).toHaveBeenCalledWith(node, serviceModelId);
  });


  test('getMenuAction: undoDelete', ()=>{
    let node = {"modelId":"d6557200-ecf2-4641-8094-5393ae3aae60","missingData":true,"action":"None","inMaint":true,"name":"jlfBwIks283yKlCD8","modelName":"VF_vGeraldine 0","type":"VF","isEcompGeneratedNaming":true,"networkStoreKey":"VF_vGeraldine 0:004","vnfStoreKey":"VF_vGeraldine 0:004","typeName":"VNF"};
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";
    let result = relatedVnfMemeber.getMenuAction(<any>node, serviceModelId);
    spyOn(result['undoDelete'], 'method');
    expect(result['undoDelete']).toBeDefined();
    expect(result['undoDelete'].visible).toBeDefined();
    expect(result['undoDelete'].enable).toBeDefined();
    result['undoDelete']['method'](node, serviceModelId);
    expect(result['undoDelete']['method']).toHaveBeenCalledWith(node, serviceModelId);
  });



  function getServiceHierarchy(){
    return {
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
        "instantiationType": "Macro",
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
        },
        "vidNotions": {
          "instantiationUI": "legacy",
          "modelCategory": "other"
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
            "bandwidth": {
              "displayName": "bandwidth",
              "command": "get_input",
              "inputName": "pasqualevpe0_bandwidth"
            },
            "AIC_CLLI": {
              "displayName": "AIC_CLLI",
              "command": "get_input",
              "inputName": "2017488_pasqualevpe0_AIC_CLLI"
            },
            "ASN": {
              "displayName": "ASN",
              "command": "get_input",
              "inputName": "2017488_pasqualevpe0_ASN"
            },
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
        },
        "2017-388_PASQUALE-vPE 0": {
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
            "bandwidth": {
              "displayName": "bandwidth",
              "command": "get_input",
              "inputName": "pasqualevpe0_bandwidth"
            },
            "AIC_CLLI": {
              "displayName": "AIC_CLLI",
              "command": "get_input",
              "inputName": "2017488_pasqualevpe0_AIC_CLLI"
            },
            "ASN": {
              "displayName": "ASN",
              "command": "get_input",
              "inputName": "2017488_pasqualevpe0_ASN"
            },
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
        },
        "2017-488_PASQUALE-vPE 0": {
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
            "bandwidth": {
              "displayName": "bandwidth",
              "command": "get_input",
              "inputName": "pasqualevpe0_bandwidth"
            },
            "AIC_CLLI": {
              "displayName": "AIC_CLLI",
              "command": "get_input",
              "inputName": "2017488_pasqualevpe0_AIC_CLLI"
            },
            "ASN": {
              "displayName": "ASN",
              "command": "get_input",
              "inputName": "2017488_pasqualevpe0_ASN"
            },
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
                "vfModuleLabel": "PASQUALE_vRE_BV",
                "baseModule": false
              },
              "inputs": {
                "vnf_config_template_version": {
                  "type": "string",
                  "description": "VPE Software Version",
                  "entry_schema": null,
                  "inputProperties": {
                    "sourceType": "HEAT",
                    "vfModuleLabel": "PASQUALE_vRE_BV",
                    "paramName": "vnf_config_template_version"
                  },
                  "fromInputName": "2017488_pasqualevpe0_vnf_config_template_version",
                  "constraints": null,
                  "required": true,
                  "default": "17.2"
                },
                "bandwidth_units": {
                  "type": "string",
                  "description": "Units of bandwidth",
                  "entry_schema": null,
                  "inputProperties": {
                    "sourceType": "HEAT",
                    "vfModuleLabel": "PASQUALE_vRE_BV",
                    "paramName": "bandwidth_units"
                  },
                  "fromInputName": "pasqualevpe0_bandwidth_units",
                  "constraints": null,
                  "required": true,
                  "default": "Gbps"
                },
                "bandwidth": {
                  "type": "string",
                  "description": "Requested VPE bandwidth",
                  "entry_schema": null,
                  "inputProperties": {
                    "sourceType": "HEAT",
                    "vfModuleLabel": "PASQUALE_vRE_BV",
                    "paramName": "bandwidth"
                  },
                  "fromInputName": "pasqualevpe0_bandwidth",
                  "constraints": null,
                  "required": true,
                  "default": "10"
                },
                "AIC_CLLI": {
                  "type": "string",
                  "description": "AIC Site CLLI",
                  "entry_schema": null,
                  "inputProperties": {
                    "sourceType": "HEAT",
                    "vfModuleLabel": "PASQUALE_vRE_BV",
                    "paramName": "AIC_CLLI"
                  },
                  "fromInputName": "2017488_pasqualevpe0_AIC_CLLI",
                  "constraints": null,
                  "required": true,
                  "default": "ATLMY8GA"
                },
                "vnf_instance_name": {
                  "type": "string",
                  "description": "The hostname assigned to the vpe.",
                  "entry_schema": null,
                  "inputProperties": {
                    "sourceType": "HEAT",
                    "vfModuleLabel": "PASQUALE_vRE_BV",
                    "paramName": "vnf_instance_name"
                  },
                  "fromInputName": "2017488_pasqualevpe0_vnf_instance_name",
                  "constraints": null,
                  "required": true,
                  "default": "mtnj309me6"
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
                "vfModuleLabel": "PASQUALE_base_vPE_BV",
                "baseModule": true
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
                "vfModuleLabel": "PASQUALE_vPFE_BV",
                "baseModule": false
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
                "vfModuleLabel": "PASQUALE_vRE_BV",
                "baseModule": false
              },
              "inputs": {
                "vnf_config_template_version": {
                  "type": "string",
                  "description": "VPE Software Version",
                  "entry_schema": null,
                  "inputProperties": {
                    "sourceType": "HEAT",
                    "vfModuleLabel": "PASQUALE_vRE_BV",
                    "paramName": "vnf_config_template_version"
                  },
                  "fromInputName": "2017488_pasqualevpe0_vnf_config_template_version",
                  "constraints": null,
                  "required": true,
                  "default": "17.2"
                },
                "bandwidth_units": {
                  "type": "string",
                  "description": "Units of bandwidth",
                  "entry_schema": null,
                  "inputProperties": {
                    "sourceType": "HEAT",
                    "vfModuleLabel": "PASQUALE_vRE_BV",
                    "paramName": "bandwidth_units"
                  },
                  "fromInputName": "pasqualevpe0_bandwidth_units",
                  "constraints": null,
                  "required": true,
                  "default": "Gbps"
                },
                "bandwidth": {
                  "type": "string",
                  "description": "Requested VPE bandwidth",
                  "entry_schema": null,
                  "inputProperties": {
                    "sourceType": "HEAT",
                    "vfModuleLabel": "PASQUALE_vRE_BV",
                    "paramName": "bandwidth"
                  },
                  "fromInputName": "pasqualevpe0_bandwidth",
                  "constraints": null,
                  "required": true,
                  "default": "10"
                },
                "AIC_CLLI": {
                  "type": "string",
                  "description": "AIC Site CLLI",
                  "entry_schema": null,
                  "inputProperties": {
                    "sourceType": "HEAT",
                    "vfModuleLabel": "PASQUALE_vRE_BV",
                    "paramName": "AIC_CLLI"
                  },
                  "fromInputName": "2017488_pasqualevpe0_AIC_CLLI",
                  "constraints": null,
                  "required": true,
                  "default": "ATLMY8GA"
                },
                "vnf_instance_name": {
                  "type": "string",
                  "description": "The hostname assigned to the vpe.",
                  "entry_schema": null,
                  "inputProperties": {
                    "sourceType": "HEAT",
                    "vfModuleLabel": "PASQUALE_vRE_BV",
                    "paramName": "vnf_instance_name"
                  },
                  "fromInputName": "2017488_pasqualevpe0_vnf_instance_name",
                  "constraints": null,
                  "required": true,
                  "default": "mtnj309me6"
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
                "vfModuleLabel": "PASQUALE_vPFE_BV",
                "baseModule": false
              },
              "inputs": {}
            }
          },
          "vfcInstanceGroups": {}
        }
      },
      "networks": {},
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
            "vfModuleLabel": "PASQUALE_vRE_BV",
            "baseModule": false
          },
          "inputs": {
            "vnf_config_template_version": {
              "type": "string",
              "description": "VPE Software Version",
              "entry_schema": null,
              "inputProperties": {
                "sourceType": "HEAT",
                "vfModuleLabel": "PASQUALE_vRE_BV",
                "paramName": "vnf_config_template_version"
              },
              "fromInputName": "2017488_pasqualevpe0_vnf_config_template_version",
              "constraints": null,
              "required": true,
              "default": "17.2"
            },
            "bandwidth_units": {
              "type": "string",
              "description": "Units of bandwidth",
              "entry_schema": null,
              "inputProperties": {
                "sourceType": "HEAT",
                "vfModuleLabel": "PASQUALE_vRE_BV",
                "paramName": "bandwidth_units"
              },
              "fromInputName": "pasqualevpe0_bandwidth_units",
              "constraints": null,
              "required": true,
              "default": "Gbps"
            },
            "bandwidth": {
              "type": "string",
              "description": "Requested VPE bandwidth",
              "entry_schema": null,
              "inputProperties": {
                "sourceType": "HEAT",
                "vfModuleLabel": "PASQUALE_vRE_BV",
                "paramName": "bandwidth"
              },
              "fromInputName": "pasqualevpe0_bandwidth",
              "constraints": null,
              "required": true,
              "default": "10"
            },
            "AIC_CLLI": {
              "type": "string",
              "description": "AIC Site CLLI",
              "entry_schema": null,
              "inputProperties": {
                "sourceType": "HEAT",
                "vfModuleLabel": "PASQUALE_vRE_BV",
                "paramName": "AIC_CLLI"
              },
              "fromInputName": "2017488_pasqualevpe0_AIC_CLLI",
              "constraints": null,
              "required": true,
              "default": "ATLMY8GA"
            },
            "vnf_instance_name": {
              "type": "string",
              "description": "The hostname assigned to the vpe.",
              "entry_schema": null,
              "inputProperties": {
                "sourceType": "HEAT",
                "vfModuleLabel": "PASQUALE_vRE_BV",
                "paramName": "vnf_instance_name"
              },
              "fromInputName": "2017488_pasqualevpe0_vnf_instance_name",
              "constraints": null,
              "required": true,
              "default": "mtnj309me6"
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
            "vfModuleLabel": "PASQUALE_base_vPE_BV",
            "baseModule": true
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
            "vfModuleLabel": "PASQUALE_vPFE_BV",
            "baseModule": false
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
            "vfModuleLabel": "PASQUALE_vRE_BV",
            "baseModule": false
          },
          "inputs": {
            "vnf_config_template_version": {
              "type": "string",
              "description": "VPE Software Version",
              "entry_schema": null,
              "inputProperties": {
                "sourceType": "HEAT",
                "vfModuleLabel": "PASQUALE_vRE_BV",
                "paramName": "vnf_config_template_version"
              },
              "fromInputName": "2017488_pasqualevpe0_vnf_config_template_version",
              "constraints": null,
              "required": true,
              "default": "17.2"
            },
            "bandwidth_units": {
              "type": "string",
              "description": "Units of bandwidth",
              "entry_schema": null,
              "inputProperties": {
                "sourceType": "HEAT",
                "vfModuleLabel": "PASQUALE_vRE_BV",
                "paramName": "bandwidth_units"
              },
              "fromInputName": "pasqualevpe0_bandwidth_units",
              "constraints": null,
              "required": true,
              "default": "Gbps"
            },
            "bandwidth": {
              "type": "string",
              "description": "Requested VPE bandwidth",
              "entry_schema": null,
              "inputProperties": {
                "sourceType": "HEAT",
                "vfModuleLabel": "PASQUALE_vRE_BV",
                "paramName": "bandwidth"
              },
              "fromInputName": "pasqualevpe0_bandwidth",
              "constraints": null,
              "required": true,
              "default": "10"
            },
            "AIC_CLLI": {
              "type": "string",
              "description": "AIC Site CLLI",
              "entry_schema": null,
              "inputProperties": {
                "sourceType": "HEAT",
                "vfModuleLabel": "PASQUALE_vRE_BV",
                "paramName": "AIC_CLLI"
              },
              "fromInputName": "2017488_pasqualevpe0_AIC_CLLI",
              "constraints": null,
              "required": true,
              "default": "ATLMY8GA"
            },
            "vnf_instance_name": {
              "type": "string",
              "description": "The hostname assigned to the vpe.",
              "entry_schema": null,
              "inputProperties": {
                "sourceType": "HEAT",
                "vfModuleLabel": "PASQUALE_vRE_BV",
                "paramName": "vnf_instance_name"
              },
              "fromInputName": "2017488_pasqualevpe0_vnf_instance_name",
              "constraints": null,
              "required": true,
              "default": "mtnj309me6"
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
            "vfModuleLabel": "PASQUALE_vPFE_BV",
            "baseModule": false
          },
          "inputs": {}
        }
      },
      "pnfs": {}
    }
  }
});
