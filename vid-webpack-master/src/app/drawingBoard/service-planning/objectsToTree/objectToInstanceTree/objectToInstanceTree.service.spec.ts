import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {getTestBed, TestBed} from "@angular/core/testing";
import {MockNgRedux, NgReduxTestingModule} from "@angular-redux/store/testing";
import {NgRedux} from "@angular-redux/store";
import {ObjectToInstanceTreeService} from "./objectToInstanceTree.service";
import {SharedTreeService} from "../shared.tree.service";
import {DynamicInputsService} from "../dynamicInputs.service";
import {DefaultDataGeneratorService} from "../../../../shared/services/defaultDataServiceGenerator/default.data.generator.service";
import {AvailableModelsTreeService} from "../../available-models-tree/available-models-tree.service";
import {ObjectToTreeService} from "../objectToTree.service";
import {DrawingBoardModes} from "../../drawing-board.modes";
import {DialogService} from "ng2-bootstrap-modal";
import {VnfPopupService} from "../../../../shared/components/genericFormPopup/genericFormServices/vnf/vnf.popup.service";
import {ControlGeneratorUtil} from "../../../../shared/components/genericForm/formControlsServices/control.generator.util.service";
import {AaiService} from "../../../../shared/services/aaiService/aai.service";
import {FeatureFlagsService} from "../../../../shared/services/featureFlag/feature-flags.service";
import {VnfControlGenerator} from "../../../../shared/components/genericForm/formControlsServices/vnfGenerator/vnf.control.generator";
import {GenericFormService} from "../../../../shared/components/genericForm/generic-form.service";
import {FormBuilder} from "@angular/forms";
import {LogService} from "../../../../shared/utils/log/log.service";
import {IframeService} from "../../../../shared/utils/iframe.service";
import {BasicPopupService} from "../../../../shared/components/genericFormPopup/genericFormServices/basic.popup.service";
import {NetworkPopupService} from "../../../../shared/components/genericFormPopup/genericFormServices/network/network.popup.service";
import {NetworkControlGenerator} from "../../../../shared/components/genericForm/formControlsServices/networkGenerator/network.control.generator";
import {VfModulePopupService} from "../../../../shared/components/genericFormPopup/genericFormServices/vfModule/vfModule.popup.service";
import {VfModuleControlGenerator} from "../../../../shared/components/genericForm/formControlsServices/vfModuleGenerator/vfModule.control.generator";
import {VnfGroupPopupService} from "../../../../shared/components/genericFormPopup/genericFormServices/vnfGroup/vnfGroup.popup.service";
import {VnfGroupControlGenerator} from "../../../../shared/components/genericForm/formControlsServices/vnfGroupGenerator/vnfGroup.control.generator";
import {DuplicateService} from "../../duplicate/duplicate.service";
import {SdcUiComponentsModule} from "onap-ui-angular";
import {ErrorMsgService} from "../../../../shared/components/error-msg/error-msg.service";
import {ComponentInfoService} from "../../component-info/component-info.service";
import {NetworkStepService} from "../models/vrf/vrfModal/networkStep/network.step.service";
import {VpnStepService} from "../models/vrf/vrfModal/vpnStep/vpn.step.service";
import {VfModuleUpgradePopupService} from "../../../../shared/components/genericFormPopup/genericFormServices/vfModuleUpgrade/vfModule.upgrade.popuop.service";
import {SharedControllersService} from "../../../../shared/components/genericForm/formControlsServices/sharedControlles/shared.controllers.service";
import {ModalService} from "../../../../shared/components/customModal/services/modal.service";
import {CreateDynamicComponentService} from "../../../../shared/components/customModal/services/create-dynamic-component.service";

class MockAppStore<T> {
  getState() {
    return {
      global: {
        'drawingBoardStatus': DrawingBoardModes.CREATE,
        flags : {
          FLAG_1911_INSTANTIATION_ORDER_IN_ASYNC_ALACARTE : false
        }
      }
    }
  }
}

describe('Model Tree Generator service', () => {
  let injector;
  let service: ObjectToInstanceTreeService;
  let httpMock: HttpTestingController;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NgReduxTestingModule, SdcUiComponentsModule],
      providers: [
        DuplicateService,
        AvailableModelsTreeService,
        ObjectToTreeService,
        ObjectToInstanceTreeService,
        DefaultDataGeneratorService,
        DynamicInputsService,
        SharedTreeService,
        VnfPopupService,
        VnfGroupPopupService,
        ControlGeneratorUtil,
        SharedControllersService,
        GenericFormService,
        FormBuilder,
        LogService,
        IframeService,
        BasicPopupService,
        NetworkPopupService,
        NetworkControlGenerator,
        VfModulePopupService,
        VfModuleUpgradePopupService,
        VfModuleControlGenerator,
        VnfGroupControlGenerator,
        DialogService,
        FeatureFlagsService,
        VnfControlGenerator,
        AaiService,
        DialogService,
        ErrorMsgService,
        ComponentInfoService,
        ModalService,
        NetworkStepService,
        VpnStepService,
        ModalService,
        CreateDynamicComponentService,
        { provide: NgRedux, useClass: MockAppStore },
        MockNgRedux]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(ObjectToInstanceTreeService);
    httpMock = injector.get(HttpTestingController);

  })().then(done).catch(done.fail));

  test('ObjectToInstanceTreeService should be defined', () => {
    expect(service).toBeDefined();
  });


  test('should return instance nodes correctly: VNF', () => {

    let convertToNodes = service.convertServiceInstanceToTreeData(getInstanceServiceVNF(), getServiceInstanceHierarchy());
    delete convertToNodes[0].menuActions;
    delete convertToNodes[0].errors;
    delete convertToNodes[0].isFailed;
    delete convertToNodes[0].trackById;
    delete convertToNodes[0].updatePoistionFunction;
    delete convertToNodes[0].position;
    delete convertToNodes[0].getInfo;
    delete convertToNodes[0].componentInfoType;
    delete convertToNodes[0].getModel;
    delete convertToNodes[1].children[0].isFailed;
    delete convertToNodes[1].children[0].menuActions;
    delete convertToNodes[1].children[0].trackById;
    delete convertToNodes[1].children[0].errors;
    delete convertToNodes[1].children[0].updatePoistionFunction;
    delete convertToNodes[1].children[0].position;
    delete convertToNodes[1].children[0].getInfo;
    delete convertToNodes[1].children[0].getModel;
    delete convertToNodes[1].children[0].componentInfoType;
    expect(Object.assign({}, convertToNodes[0])).toEqual(Object.assign({}, expectInstanceNodesResultVNF()[0]));
    expect(Object.assign({}, convertToNodes[1].children[0])).toEqual(Object.assign({}, expectInstanceNodesResultVNF()[1].children[0]));
    expect(convertToNodes[1].inMaint).toBeFalsy();
    expect(convertToNodes[0].inMaint).toBeTruthy();
    expect(service.numberOfFailed).toBe(0);
    expect(service.numberOfElements).toBe(3);
  });

  test('should return instance nodes correctly: VNF and Network', () => {
    let convertToNodes = service.convertServiceInstanceToTreeData(getInstanceServiceVNF_Network(), getServiceHeirarchyVNF_Network());

    delete convertToNodes[1].menuActions;
    delete convertToNodes[1].trackById;
    delete convertToNodes[1].errors;
    delete convertToNodes[1].isFailed;
    delete convertToNodes[1].updatePoistionFunction;
    delete convertToNodes[1].position;
    delete convertToNodes[1].getModel;
    delete convertToNodes[1].getInfo;
    delete convertToNodes[1].componentInfoType;
    expect(Object.assign({}, convertToNodes[0].children[0].dynamicInputs)).toEqual(Object.assign({}, expectInstanceNodesResultVNF_Network()[0]['children'][0].dynamicInputs));
    expect(Object.assign({}, convertToNodes[0].children[0].missingData)).toEqual(Object.assign({}, expectInstanceNodesResultVNF_Network()[0]['children'][0].missingData));
    expect(Object.assign({}, convertToNodes[1])).toEqual(Object.assign({}, expectInstanceNodesResultVNF_Network()[1]));
    expect(service.numberOfFailed).toBe(0);
    expect(service.numberOfElements).toBe(5);
  });

  function getServiceInstanceHierarchy() {
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

  function getServiceHeirarchyVNF_Network() {
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
            "vmxvpfe_flavor_name": "ns.c20r16d25.v5",
            "max_instances": 5
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
            "network_role": "network role 1, network role 2",
            "min_instances": 1,
            "max_instances": 10,
            "ecomp_generated_naming": "true",
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
      }
    }
  }

  function getInstanceServiceVNF() {
    return {
      "vnfs": {
        "2017-388_PASQUALE-vPE 0": {
          "action": 'None',
          "inMaint": true,
          "orchStatus": "Active",
          "provStatus": "prov",
          "rollbackOnFailure": "true",
          "originalName": "2017-388_PASQUALE-vPE 0",
          "isMissingData": true,
          "trackById": "u5mtsvzmq6p",
          "vfModules": {},
          "vnfStoreKey": "2017-388_PASQUALE-vPE 0",
          "uuid": "afacccf6-397d-45d6-b5ae-94c39734b168",
          "productFamilyId": "d8a6ed93-251c-47ca-adc9-86671fd19f4c",
          "lcpCloudRegionId": null,
          "tenantId": null,
          "lineOfBusiness": null,
          "statusMessage": "Failed Vnf Message",
          "platformName": null,
          "modelInfo": {
            "modelType": "VF",
            "modelInvariantId": "72e465fe-71b1-4e7b-b5ed-9496118ff7a8",
            "modelVersionId": "afacccf6-397d-45d6-b5ae-94c39734b168",
            "modelName": "2017-388_PASQUALE-vPE",
            "modelVersion": "4.0",
            "modelCustomizationId": "b3c76f73-eeb5-4fb6-9d31-72a889f1811c",
            "modelCustomizationName": "2017-388_PASQUALE-vPE 0"
          }
        },
        "2017-488_PASQUALE-vPE 0": {
          "action": 'None',
          "inMaint": false,
          "rollbackOnFailure": "true",
          "originalName": "2017-488_PASQUALE-vPE 0",
          "isMissingData": false,
          "trackById": "1d6dg4fsgbm",
          "vfModules": {
            "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0zssmp": {
                "action": 'None',
                "isMissingData": true,
                "sdncPreReload": null,
                "modelInfo": {
                  "modelType": "VFmodule",
                  "modelInvariantId": "b34833bb-6aa9-4ad6-a831-70b06367a091",
                  "modelVersionId": "f8360508-3f17-4414-a2ed-6bc71161e8db",
                  "modelName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                  "modelVersion": "5",
                  "modelCustomizationId": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3",
                  "modelUniqueId": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3",
                  "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0"
                },
                "instanceParams": [
                  {}
                ],
                "trackById": "v2egx1b8i1l",
                "statusMessage": "Failed vfModel message"
              }
            }
          },
          "vnfStoreKey": "2017-488_PASQUALE-vPE 0",
          "uuid": "69e09f68-8b63-4cc9-b9ff-860960b5db09",
          "productFamilyId": "d8a6ed93-251c-47ca-adc9-86671fd19f4c",
          "lcpCloudRegionId": "AAIAIC25",
          "tenantId": "092eb9e8e4b7412e8787dd091bc58e86",
          "lineOfBusiness": "ONAP",
          "platformName": "platform",
          "modelInfo": {
            "modelInvariantId": "72e465fe-71b1-4e7b-b5ed-9496118ff7a8",
            "modelVersionId": "69e09f68-8b63-4cc9-b9ff-860960b5db09",
            "modelName": "2017-488_PASQUALE-vPE",
            "modelVersion": "5.0",
            "modelCustomizationId": "1da7b585-5e61-4993-b95e-8e6606c81e45",
            "modelUniqueId": "1da7b585-5e61-4993-b95e-8e6606c81e45",
            "modelCustomizationName": "2017-488_PASQUALE-vPE 0",
            "uuid": "69e09f68-8b63-4cc9-b9ff-860960b5db09"
          },
          "instanceName": "2017488_PASQUALEvPE",
          "legacyRegion": "123",
          "instanceParams": [
            {}
          ]
        }
      },
      "instanceParams": [
        {
          "2017488_pasqualevpe0_ASN": "AV_vPE"
        }
      ],
      "validationCounter": 3,
      "existingNames": {
        "yoav": ""
      },
      "existingVNFCounterMap": {
        "afacccf6-397d-45d6-b5ae-94c39734b168": 1,
        "69e09f68-8b63-4cc9-b9ff-860960b5db09": 1
      },
      "existingNetworksCounterMap": {},
      "networks": {},
      "instanceName": "yoav",
      "globalSubscriberId": "e433710f-9217-458d-a79d-1c7aff376d89",
      "subscriptionServiceType": "TYLER SILVIA",
      "owningEntityId": "d61e6f2d-12fa-4cc2-91df-7c244011d6fc",
      "productFamilyId": "d8a6ed93-251c-47ca-adc9-86671fd19f4c",
      "lcpCloudRegionId": "AAIAIC25",
      "tenantId": "092eb9e8e4b7412e8787dd091bc58e86",
      "aicZoneId": "ABC15",
      "projectName": "WATKINS",
      "rollbackOnFailure": "true",
      "bulkSize": 1,
      "aicZoneName": "AAITESAN-ABC15",
      "owningEntityName": "WayneHolland",
      "testApi": "VNF_API",
      "isEcompGeneratedNaming": false,
      "tenantName": "USP-SIP-IC-24335-T-01",
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
  }

  function getInstanceServiceVNF_Network() {
    return {
      "vnfs": {
        "2017-488_PASQUALE-vPE 0": {
          "action": 'None',
          "rollbackOnFailure": "true",
          "originalName": "2017-488_PASQUALE-vPE 0",
          "isMissingData": false,
          "trackById": "o65b26t2thj",
          "vfModules": {
            "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1sgoqi": {
                "action": 'None',
                "instanceName": "yoav",
                "volumeGroupName": "123",
                "modelInfo": {
                  "modelInvariantId": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                  "modelVersionId": "25284168-24bb-4698-8cb4-3f509146eca5",
                  "modelName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                  "modelVersion": "6",
                  "modelCustomizationId": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401",
                  "modelUniqueId": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401",
                  "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                  "uuid": "25284168-24bb-4698-8cb4-3f509146eca5"
                },
                "uuid": "25284168-24bb-4698-8cb4-3f509146eca5",
                "isMissingData": false,
                "instanceParams": [
                  {
                    "pasqualevpe0_bandwidth": "10",
                    "2017488_pasqualevpe0_vnf_instance_name": "mtnj309me6",
                    "2017488_pasqualevpe0_vnf_config_template_version": "17.2",
                    "2017488_pasqualevpe0_AIC_CLLI": "ATLMY8GA",
                    "pasqualevpe0_bandwidth_units": "Gbps"
                  }
                ]
              }
            }
          },
          "vnfStoreKey": "2017-488_PASQUALE-vPE 0",
          "uuid": "69e09f68-8b63-4cc9-b9ff-860960b5db09",
          "productFamilyId": "ebc3bc3d-62fd-4a3f-a037-f619df4ff034",
          "lcpCloudRegionId": "hvf6",
          "tenantId": "bae71557c5bb4d5aac6743a4e5f1d054",
          "lineOfBusiness": "ONAP",
          "platformName": "platform",
          "modelInfo": {
            "modelInvariantId": "72e465fe-71b1-4e7b-b5ed-9496118ff7a8",
            "modelVersionId": "69e09f68-8b63-4cc9-b9ff-860960b5db09",
            "modelName": "2017-488_PASQUALE-vPE",
            "modelVersion": "5.0",
            "modelCustomizationId": "1da7b585-5e61-4993-b95e-8e6606c81e45",
            "modelCustomizationName": "2017-488_PASQUALE-vPE 0",
            "uuid": "69e09f68-8b63-4cc9-b9ff-860960b5db09"
          },
          "instanceName": "2017488_PASQUALEvPE",
          "instanceParams": [
            {}
          ]
        }
      },
      "instanceParams": [
        {
          "2017488_pasqualevpe0_ASN": "AV_vPE"
        }
      ],
      "validationCounter": 0,
      "existingNames": {
        "123": "",
        "yoav": "",
        "instancename": "",
        "extvl": ""
      },
      "existingVNFCounterMap": {
        "69e09f68-8b63-4cc9-b9ff-860960b5db09": 1
      },
      "existingNetworksCounterMap": {
        "ddc3f20c-08b5-40fd-af72-c6d14636b986": 3
      },
      "networks": {
        "ExtVL 0": {
          "action": "None",
          "rollbackOnFailure": "true",
          "isMissingData": false,
          "originalName": "ExtVL 0",
          "networkStoreKey": "ExtVL 0",
          "trackById": "sf3zth68xjf",
          "statusMessage": "Network failed message",
          "productFamilyId": "ebc3bc3d-62fd-4a3f-a037-f619df4ff034",
          "lcpCloudRegionId": "hvf6",
          "tenantId": "229bcdc6eaeb4ca59d55221141d01f8e",
          "platformName": "xxx1",
          "lineOfBusiness": "ONAP",
          "routeTarget": {
            "globalRouteTarget": "mock-global-1",
            "routeTargetRole": "mock-role-x"
          },
          "instanceParams": [
            {}
          ],
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
          "action": "None",
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
          "instanceParams": [
            {}
          ],
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
          "action": "None",
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
          "instanceParams": [
            {
              "vnf_config_template_version": "17.2",
              "bandwidth_units": "Gbps",
              "bandwidth": "10",
              "AIC_CLLI": "ATLMY8GA",
              "ASN": "AV_vPE",
              "vnf_instance_name": "yoav"
            }
          ],
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
      "bulkSize": 1,
      "aicZoneName": "YUDFJULP-JAG1",
      "owningEntityName": "WayneHolland",
      "testApi": "GR_API",
      "isEcompGeneratedNaming": false,
      "tenantName": "USP-SIP-IC-24335-T-01",
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
  }

  function expectInstanceNodesResultVNF() {
    return [
      {
        "action": 'None',
        "modelId": "afacccf6-397d-45d6-b5ae-94c39734b168",
        "modelInvariantId": "72e465fe-71b1-4e7b-b5ed-9496118ff7a8",
        "modelCustomizationId": "b3c76f73-eeb5-4fb6-9d31-72a889f1811c",
        "modelUniqueId": "b3c76f73-eeb5-4fb6-9d31-72a889f1811c",
        "missingData": true,
        "id": "u5mtsvzmq6p",
        "instanceModelInfo": {
          "modelCustomizationId": "b3c76f73-eeb5-4fb6-9d31-72a889f1811c",
          "modelCustomizationName": "2017-388_PASQUALE-vPE 0",
          "modelInvariantId": "72e465fe-71b1-4e7b-b5ed-9496118ff7a8",
          "modelName": "2017-388_PASQUALE-vPE",
          "modelType": "VF",
          "modelVersion": "4.0",
          "modelVersionId": "afacccf6-397d-45d6-b5ae-94c39734b168"
        },
        "orchStatus": 'Active',
        "provStatus": 'prov',
        "inMaint": true,
        "name": "2017-388_PASQUALE-vPE 0",
        "modelName": "2017-388_PASQUALE-vPE 0",
        "modelTypeName": "vnfs",
        "type": "VF",
        "parentType": '',
        "isEcompGeneratedNaming": false,
        "networkStoreKey": "2017-388_PASQUALE-vPE 0",
        "vnfStoreKey": "2017-388_PASQUALE-vPE 0",
        "typeName": "VNF",
        "children": [],
        "statusMessage": "Failed Vnf Message",
        "statusProperties": [Object({
          key: 'Prov Status: ',
          value: 'prov',
          testId: 'provStatus'
        }), Object({ key: 'Orch Status: ', value: 'Active', testId: 'orchStatus' }), Object({
          key: 'In-maintenance',
          value: '',
          testId: 'inMaint'
        })]
      },
      {
        "action": 'None',
        "modelId": "69e09f68-8b63-4cc9-b9ff-860960b5db09",
        "missingData": false,
        "id": "1d6dg4fsgbm",
        "inMaint": false,
        "parentType": '',
        "name": "2017-488_PASQUALE-vPE 0",
        "modelName": "2017-488_PASQUALE-vPE 0",
        "modelTypeName": "vnfs",
        "type": "VF",
        "isEcompGeneratedNaming": false,
        "networkStoreKey": "2017-488_PASQUALE-vPE 0",
        "vnfStoreKey": "2017-488_PASQUALE-vPE 0",
        "typeName": "VNF",
        "statusProperties": [Object({
          key: 'Prov Status: ',
          value: undefined,
          testId: 'provStatus'
        }), Object({ key: 'Orch Status: ', value: undefined, testId: 'orchStatus' })],
        "children": [{
          "parentType": 'VNF',
          "action": 'None',
          "modelId": "f8360508-3f17-4414-a2ed-6bc71161e8db",
          "modelInvariantId": "b34833bb-6aa9-4ad6-a831-70b06367a091",
          "modelCustomizationId": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3",
          "modelUniqueId": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3",
          "missingData": true,
          "id": "v2egx1b8i1l",
          "instanceModelInfo": {
            "modelCustomizationId": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3",
            "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
            "modelInvariantId": "b34833bb-6aa9-4ad6-a831-70b06367a091",
            "modelName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
            "modelType": "VFmodule",
            "modelUniqueId": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3",
            "modelVersion": "5",
            "modelVersionId": "f8360508-3f17-4414-a2ed-6bc71161e8db"
          },
          "statusMessage": "Failed vfModel message",
          "name": "&lt;Automatically Assigned&gt;",
          "modelName": "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
          "modelTypeName": "vfModules",
          "type": "VFmodule",
          "isEcompGeneratedNaming": false,
          "dynamicInputs": [],
          "dynamicModelName": "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0zssmp",
          "typeName": "M",
          "statusProperties": [Object({
            key: 'Prov Status: ',
            value: undefined,
            testId: 'provStatus'
          }), Object({ key: 'Orch Status: ', value: undefined, testId: 'orchStatus' }),
            Object({
                key: 'Model Version: ',
                value: '5',
                testId: 'modelVersion'
            })],
        }]
      }]
  }

  function expectInstanceNodesResultVNF_Network() {
    return [
      {
        "parentType": '',
        "action": 'None',
        "modelId": "69e09f68-8b63-4cc9-b9ff-860960b5db09",
        "missingData": false,
        "id": "o65b26t2thj",
        "name": "2017488_PASQUALEvPE",
        "modelName": "2017-488_PASQUALE-vPE 0",
        "modelTypeName": "vnfs",
        "type": "VF",
        "isEcompGeneratedNaming": false,
        "vnfStoreKey": "2017-488_PASQUALE-vPE 0",
        "typeName": "VNF",
        "statusProperties": [Object({
          key: 'Prov Status: ',
          value: undefined,
          testId: 'provStatus'
        }), Object({ key: 'Orch Status: ', value: undefined, testId: 'orchStatus' })],
        "children": [
          {
            "action": 'None',
            "modelId": "25284168-24bb-4698-8cb4-3f509146eca5",
            "missingData": false,
            "name": "yoav",
            "modelName": "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
            "modelTypeName": "vfModules",
            "type": "VFmodule",
            "isEcompGeneratedNaming": false,
            "typeName": "M",
            "dynamicInputs": [
              {
                "id": "pasqualevpe0_bandwidth",
                "type": "string",
                "name": "pasqualevpe0_bandwidth",
                "value": "10",
                "isRequired": true,
                "description": "Requested VPE bandwidth"
              },
              {
                "id": "2017488_pasqualevpe0_vnf_instance_name",
                "type": "string",
                "name": "2017488_pasqualevpe0_vnf_instance_name",
                "value": "mtnj309me6",
                "isRequired": true,
                "description": "The hostname assigned to the vpe."
              },
              {
                "id": "2017488_pasqualevpe0_vnf_config_template_version",
                "type": "string",
                "name": "2017488_pasqualevpe0_vnf_config_template_version",
                "value": "17.2",
                "isRequired": true,
                "description": "VPE Software Version"
              },
              {
                "id": "2017488_pasqualevpe0_AIC_CLLI",
                "type": "string",
                "name": "2017488_pasqualevpe0_AIC_CLLI",
                "value": "ATLMY8GA",
                "isRequired": true,
                "description": "AIC Site CLLI"
              },
              {
                "id": "pasqualevpe0_bandwidth_units",
                "type": "string",
                "name": "pasqualevpe0_bandwidth_units",
                "value": "Gbps",
                "isRequired": true,
                "description": "Units of bandwidth"
              }
            ],
            "dynamicModelName": "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1sgoqi"
          }
        ]
      },
      {
        "parentType": '',
        "action": 'None',
        "modelId": "ddc3f20c-08b5-40fd-af72-c6d14636b986",
        "modelInvariantId": "379f816b-a7aa-422f-be30-17114ff50b7c",
        "modelCustomizationId": "94fdd893-4a36-4d70-b16a-ec29c54c184f",
        "modelUniqueId": "94fdd893-4a36-4d70-b16a-ec29c54c184f",
        "missingData": false,
        "id": "sf3zth68xjf",
        "instanceModelInfo": {
          "modelCustomizationId": "94fdd893-4a36-4d70-b16a-ec29c54c184f",
          "modelCustomizationName": "ExtVL 0",
          "modelInvariantId": "379f816b-a7aa-422f-be30-17114ff50b7c",
          "modelName": "ExtVL",
          "modelVersion": "37.0",
          "modelVersionId": "ddc3f20c-08b5-40fd-af72-c6d14636b986",
          "uuid": "ddc3f20c-08b5-40fd-af72-c6d14636b986"
        },
        "name": "&lt;Automatically Assigned&gt;",
        "routeTargetId": "mock-global-1",
        "routeTargetRole": "mock-role-x",
        "statusMessage": "Network failed message",
        "modelName": "ExtVL 0",
        "modelTypeName": "networks",
        "type": "VL",
        "isEcompGeneratedNaming": true,
        "networkStoreKey": "ExtVL 0",
        "typeName": "N",
        "statusProperties": [Object({
          key: 'Prov Status: ',
          value: undefined,
          testId: 'provStatus'
        }), Object({ key: 'Orch Status: ', value: undefined, testId: 'orchStatus' })],
      },
      {
        "parentType": '',
        "action": 'None',
        "originalAction": 'None',
        "modelId": "ddc3f20c-08b5-40fd-af72-c6d14636b986",
        "missingData": false,
        "id": "2mdxioxca9h",
        "name": "&lt;Automatically Assigned&gt;",
        "modelName": "ExtVL 0",
        "modelTypeName": "networks",
        "type": "VL",
        "isEcompGeneratedNaming": true,
        "networkStoreKey": "ExtVL 0:0001",
        "typeName": "M"
      },
      {
        "parentType": '',
        "action": 'None',
        "originalAction": 'None',
        "modelId": "ddc3f20c-08b5-40fd-af72-c6d14636b986",
        "missingData": false,
        "id": "z7vd1gmpbs",
        "name": "ExtVL",
        "modelName": "ExtVL 0",
        "modelTypeName": "networks",
        "type": "VL",
        "isEcompGeneratedNaming": true,
        "networkStoreKey": "ExtVL 0_1",
        "typeName": "M"
      }
    ];
  }
});
