import {HttpClientTestingModule} from "@angular/common/http/testing";
import {getTestBed, TestBed} from "@angular/core/testing";
import {MockNgRedux, NgReduxTestingModule} from "@angular-redux/store/testing";
import {NetworkModelInfo} from "./network.model.info";
import {DynamicInputsService} from "../../dynamicInputs.service";
import {SharedTreeService} from "../../shared.tree.service";
import {NgRedux} from "@angular-redux/store";
import {NetworkPopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/network/network.popup.service";
import {DialogService} from "ng2-bootstrap-modal";
import {AvailableNodeIcons} from "../../../available-models-tree/available-models-tree.service";
import {DrawingBoardModes} from "../../../drawing-board.modes";
import {IframeService} from "../../../../../shared/utils/iframe.service";
import {DuplicateService} from "../../../duplicate/duplicate.service";
import {ModelInformationItem} from "../../../../../shared/components/model-information/model-information.component";
import {FeatureFlagsService} from "../../../../../shared/services/featureFlag/feature-flags.service";

class MockAppStore<T> {
  getState() {
    return {
      global: {
        'drawingBoardStatus': DrawingBoardModes.CREATE
      },
      service: {
        serviceHierarchy: {
          'servicedId': {
            'networks': {
              'networkName': {
                'properties': {
                  'max_instances': 1
                }
              }
            }
          }
        },
        serviceInstance: {
          'servicedId': {
            'existingNetworksCounterMap': {
              'networkId': 1
            },
            'networks': {
              'networkName': {
                'action': 'Create',
                'originalName': 'networkName'
              }
            }
          }
        }
      }
    }
  }
}

class MockFeatureFlagsService extends FeatureFlagsService {
  getAllFlags(): { [p: string]: boolean } {
    return {};
  }
}


describe('Network Model Info', () => {
  let injector;
  let _dynamicInputsService: DynamicInputsService;
  let _sharedTreeService: SharedTreeService;
  let networkModel: NetworkModelInfo;
  let _dialogService: DialogService;
  let _networkPopupService: NetworkPopupService;
  let _duplicateService: DuplicateService;
  let _iframeService: IframeService;
  let _featureFlagsService: FeatureFlagsService;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NgReduxTestingModule],
      providers: [
        DynamicInputsService,
        SharedTreeService,
        DialogService,
        NetworkPopupService,
        IframeService,
        DuplicateService,
        {provide: NgRedux, useClass: MockAppStore},
        {provide: FeatureFlagsService, useClass: MockFeatureFlagsService},
        MockNgRedux]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    _sharedTreeService = injector.get(SharedTreeService);
    _featureFlagsService = injector.get(FeatureFlagsService);

    networkModel = new NetworkModelInfo(_dynamicInputsService, _sharedTreeService, _dialogService, _networkPopupService, _duplicateService, null, _iframeService, _featureFlagsService, MockNgRedux.getInstance());
  })().then(done).catch(done.fail));

  test('NetworkModelInfo should be defined', () => {
    expect(NetworkModelInfo).toBeDefined();
  });

  test('NetworkModelInfo should defined extra details', () => {
    expect(networkModel.name).toEqual('networks');
    expect(networkModel.type).toEqual('Network');
  });

  test('isEcompGeneratedNaming should return true if = isEcompGeneratedNaming is "true" ', () => {
    let isEcompGeneratedNaming: boolean = networkModel.isEcompGeneratedNaming({
      properties: {
        ecomp_generated_naming: 'true'
      }
    });
    expect(isEcompGeneratedNaming).toBeTruthy();
  });

  test('isEcompGeneratedNaming should return false if isEcompGeneratedNaming is "false"', () => {
    let isEcompGeneratedNaming: boolean = networkModel.isEcompGeneratedNaming({
      properties: {
        ecomp_generated_naming: 'false'
      }
    });
    expect(isEcompGeneratedNaming).toBeFalsy();
  });

  test('isEcompGeneratedNaming should return false if isEcompGeneratedNaming is not defined', () => {
    let isEcompGeneratedNaming: boolean = networkModel.isEcompGeneratedNaming({
      properties: {}
    });
    expect(isEcompGeneratedNaming).toBeFalsy();
  });

  test('getTooltip should return "VF"', () => {
    let tooltip: string = networkModel.getTooltip();
    expect(tooltip).toEqual('Network');
  });

  test('getType should return "VF"', () => {
    let tooltip: string = networkModel.getType();
    expect(tooltip).toEqual('Network');
  });

  test('getNextLevelObject should return null', () => {
    let nextLevel = networkModel.getNextLevelObject();
    expect(nextLevel).toBeNull();
  });

  test('updateDynamicInputsDataFromModel should return empty array', () => {
    let dynamicInputs = networkModel.updateDynamicInputsDataFromModel({});
    expect(dynamicInputs).toEqual([]);
  });

  test('getModel should return Network model', () => {
    let model = networkModel.getModel('2017-388_PASQUALE-vPE 1_1', <any>{}, getServiceHierarchy());
    expect(model.type).toEqual('VL');
  });

  test('showNodeIcons should return false if reachLimit of max', () => {
    let serviceId: string = 'servicedId';
    let node = {
      data: {
        id: 'networkId',
        name: 'networkName',
        modelCustomizationId: 'modelCustomizationId'
      }
    };
    jest.spyOn(_sharedTreeService, 'getExistingInstancesWithDeleteMode').mockReturnValue(0);
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global: {},
      service: {
        serviceHierarchy: {
          'servicedId': {
            'networks': {
              'networkName': {
                'properties': {
                  'max_instances': 1
                }
              }
            }
          }
        },
        serviceInstance: {
          'servicedId': {
            'existingNetworksCounterMap': {
              'modelCustomizationId': 1
            },
            'networks': {
              'networkName': {}
            }
          }
        }
      }
    });

    let result = networkModel.showNodeIcons(<any>node, serviceId);
    expect(result).toEqual(new AvailableNodeIcons(true, false));
  });

  test('showNodeIcons should return true if not reachLimit of max', () => {
    let serviceId: string = 'servicedId';
    let node = {
      data: {
        id: 'networkId',
        name: 'networkName'
      }
    };
    jest.spyOn(_sharedTreeService, 'getExistingInstancesWithDeleteMode').mockReturnValue(0);
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global: {},
      service: {
        serviceHierarchy: {
          'servicedId': {
            'networks': {
              'networkName': {
                'properties': {
                  'max_instances': 2
                }
              }
            }
          }
        },
        serviceInstance: {
          'servicedId': {
            'existingNetworksCounterMap': {
              'networkId': 1
            },
            'networks': {
              'networkName': {}
            }
          }
        }
      }
    });

    let result = networkModel.showNodeIcons(<any>node, serviceId);
    expect(result).toEqual(new AvailableNodeIcons(true, false));
  });

  test('getNodeCount should return number of nodes', () => {
    let serviceId: string = 'servicedId';
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global: {},
      service: {
        serviceHierarchy: {
          'servicedId': {
            'networks': {
              'networkName': {
                'properties': {
                  'max_instances': 1
                }
              }
            }
          }
        },
        serviceInstance: {
          'servicedId': {
            'existingNetworksCounterMap': {
              'modelCustomizationId': 1
            },
            'networks': {
              'networkName': {
                'action': 'Create',
                'originalName': 'networkName'
              }
            }
          }
        }
      }
    });

    let node = {
      data: {
        id: 'networkId',
        name: 'networkName',
        action: 'Create',
        modelCustomizationId: "modelCustomizationId",
        modelUniqueId: "modelCustomizationId"
      }
    };
    let result = networkModel.getNodeCount(<any>node, serviceId);
    expect(result).toEqual(1);

    node.data.modelCustomizationId = 'networkId_notExist';
    node.data.modelUniqueId = 'networkId_notExist';
    result = networkModel.getNodeCount(<any>node, serviceId);
    expect(result).toEqual(0);
  });

  test('getMenuAction: showAuditInfoNetwork', () => {

    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global: {
        "drawingBoardStatus": DrawingBoardModes.RETRY
      }
    });
    jest.spyOn(_sharedTreeService, 'isRetryMode').mockReturnValue(true);
    let node = {
      data: {
        "modelId": "6b528779-44a3-4472-bdff-9cd15ec93450",
        "action": "Create",
        "isFailed": true,
      }
    };
    let serviceModelId = "6b528779-44a3-4472-bdff-9cd15ec93450";
    let result = networkModel.getMenuAction(<any>node, serviceModelId);
    spyOn(result['showAuditInfo'], 'method');
    expect(result['showAuditInfo']).toBeDefined();
    expect(result['showAuditInfo'].visible(node)).toBeTruthy();
    expect(result['showAuditInfo'].enable(node)).toBeTruthy();
    result['showAuditInfo']['method'](node, serviceModelId);
    expect(result['showAuditInfo']['method']).toHaveBeenCalledWith(node, serviceModelId);
  });

  test('Info for network should be correct', () => {
    const model = getNetworkModel();
    const instance = getNetworkInstance();
    let actualNetworkInfo = networkModel.getInfo(model, instance);
    let expectedNetworkInfo = [
      ModelInformationItem.createInstance("Min instances", "0"),
      ModelInformationItem.createInstance("Max instances", "1"),
      ModelInformationItem.createInstance('Network role', "network role 1, network role 2"),
      ModelInformationItem.createInstance("Route target id", null),
      ModelInformationItem.createInstance("Route target role", null),
    ];
    expect(actualNetworkInfo).toEqual(expectedNetworkInfo);
  });

  test('When there is no max Max instances text is: Unlimited (default)', () => {
    let actualVNFInfo = networkModel.getInfo({just:"not empty"},null);
    const maxInstancesItem = actualVNFInfo.find((item)=> item.label == 'Max instances');
    expect(maxInstancesItem.values[0]).toEqual('Unlimited (default)');
  });

  function getNetworkModel() {
    return {
      "customizationUuid": "94fdd893-4a36-4d70-b16a-ec29c54c184f",
      "name": "ExtVL",
      "version": "37.0",
      "description": "ECOMP generic virtual link (network) base type for all other service-level and global networks",
      "uuid": "ddc3f20c-08b5-40fd-af72-c6d14636b986",
      "invariantUuid": "379f816b-a7aa-422f-be30-17114ff50b7c",
      "max": 1,
      "min": 0,
      "isEcompGeneratedNaming": false,
      "type": "VL",
      "modelCustomizationName": "ExtVL 0",
      "roles": ["network role 1", " network role 2"],
      "properties": {
        "network_role": "network role 1, network role 2",
        "network_assignments":
          "{is_external_network=false, ipv4_subnet_default_assignment={min_subnets_count=1}, ecomp_generated_network_assignment=false, ipv6_subnet_default_assignment={min_subnets_count=1}}",
        "exVL_naming": "{ecomp_generated_naming=true}",
        "network_flows": "{is_network_policy=false, is_bound_to_vpn=false}",
        "network_homing": "{ecomp_selected_instance_node_target=false}"
      }
    };

  }

  function getNetworkInstance() {
    return {
      "modelCustomizationId": "94fdd893-4a36-4d70-b16a-ec29c54c184f",
      "modelId": "ddc3f20c-08b5-40fd-af72-c6d14636b986",
      "modelUniqueId": "94fdd893-4a36-4d70-b16a-ec29c54c184f",
      "missingData": true,
      "id": "NETWORK4_INSTANCE_ID",
      "action": "None",
      "orchStatus": "Created",
      "provStatus": "preprov",
      "inMaint": false,
      "instanceId": "NETWORK4_INSTANCE_ID",
      "instanceType": "CONTRAIL30_HIMELGUARD",
      "instanceName": "NETWORK4_INSTANCE_NAME",
      "name": "NETWORK4_INSTANCE_NAME",
      "modelName": "ExtVL 0",
      "type": "VL",
      "isEcompGeneratedNaming": false,
      "networkStoreKey": "NETWORK4_INSTANCE_ID",
      "typeName": "N",
      "menuActions": {"edit": {}, "showAuditInfo": {}, "duplicate": {}, "remove": {}, "delete": {}, "undoDelete": {}},
      "isFailed": false,
      "statusMessage": "",
      "statusProperties": [{"key": "Prov Status:", "value": "preprov", "testId": "provStatus"}, {
        "key": "Orch Status:",
        "value": "Created",
        "testId": "orchStatus"
      }],
      "trackById": "1wvr73xl999",
      "parentType": "",
      "componentInfoType": "Network",
      "errors": {}
    };
  }


  function getServiceHierarchy() {
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
      "networks": {
        "2017-388_PASQUALE-vPE 1_1": {
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
          "type": "VL",
          "modelCustomizationName": "2017-388_PASQUALE-vPE 1",
          "vfModules": {},
          "volumeGroups": {},
          "vfcInstanceGroups": {}
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
