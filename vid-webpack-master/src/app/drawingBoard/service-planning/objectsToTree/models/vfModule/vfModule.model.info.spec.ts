import {HttpClientTestingModule} from "@angular/common/http/testing";
import {getTestBed, TestBed} from "@angular/core/testing";
import {MockNgRedux, NgReduxTestingModule} from "@angular-redux/store/testing";
import {VFModuleModelInfo} from "./vfModule.model.info";
import {DynamicInputsService} from "../../dynamicInputs.service";
import {SharedTreeService} from "../../shared.tree.service";
import {VfModulePopuopService} from "../../../../../shared/components/genericFormPopup/genericFormServices/vfModule/vfModule.popuop.service";
import {DialogService} from "ng2-bootstrap-modal";
import {NgRedux} from "@angular-redux/store";
import {AvailableNodeIcons} from "../../../available-models-tree/available-models-tree.service";
import {MessageBoxService} from "../../../../../shared/components/messageBox/messageBox.service";
import {DrawingBoardModes} from "../../../drawing-board.modes";
import {IframeService} from "../../../../../shared/utils/iframe.service";
import {ComponentInfoService} from "../../../component-info/component-info.service";
import {ModelInformationItem} from "../../../../../shared/components/model-information/model-information.component";
import {AaiService} from "../../../../../shared/services/aaiService/aai.service";
import {HttpClient, HttpHandler} from "@angular/common/http";
import {FeatureFlagsService} from "../../../../../shared/services/featureFlag/feature-flags.service";
import {VfModuleUpgradePopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/vfModuleUpgrade/vfModule.upgrade.popuop.service";

class MockAppStore<T> {
  getState() {
    return {
      global: {
        'drawingBoardStatus': DrawingBoardModes.CREATE
      }
    }
  }
}

class MockFeatureFlagsService extends  FeatureFlagsService{
  getAllFlags(): { [p: string]: boolean } {
    return {};
  }
}

describe('VFModule Model Info', () => {
  let injector;
  let  _dynamicInputsService : DynamicInputsService;
  let  _sharedTreeService : SharedTreeService;
  let  vfModuleModel: VFModuleModelInfo;
  let _dialogService : DialogService;
  let _vfModulePopupService : VfModulePopuopService;
  let _vfModuleUpgradePopupService : VfModuleUpgradePopupService;
  let _iframeService : IframeService;
  let _componentInfoService : ComponentInfoService;
  let _featureFlagsService : FeatureFlagsService;


  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NgReduxTestingModule],
      providers: [
        DynamicInputsService,
        DialogService,
        VfModulePopuopService,
        VfModuleUpgradePopupService,
        SharedTreeService,
        IframeService,
        {provide: NgRedux, useClass: MockAppStore},
        MockNgRedux,
        AaiService,
        HttpClient,
        HttpHandler,
        {provide: FeatureFlagsService, useClass: MockFeatureFlagsService},
        ComponentInfoService
      ]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    _sharedTreeService = injector.get(SharedTreeService);
    _componentInfoService = injector.get(ComponentInfoService)
    _featureFlagsService = injector.get(FeatureFlagsService);
    vfModuleModel = new VFModuleModelInfo(_dynamicInputsService, _sharedTreeService, _dialogService, _vfModulePopupService, _vfModuleUpgradePopupService, _iframeService,_featureFlagsService,  MockNgRedux.getInstance(),_componentInfoService);

  })().then(done).catch(done.fail));



  test('VFModuleModelInfo should be defined', () => {
    expect(VFModuleModelInfo).toBeDefined();
  });

  test('VnfModelInfo should defined extra details', () => {
    expect(vfModuleModel.name).toEqual('vfModules');
    expect(vfModuleModel.type).toEqual('Module');
  });

  test('isEcompGeneratedNaming should return true if vnf has isEcompGeneratedNaming is "true" ', () => {
    let isEcompGeneratedNaming : boolean = vfModuleModel.isEcompGeneratedNaming({}, {
      properties: {
        ecomp_generated_naming: 'true'
      }
    });
    expect(isEcompGeneratedNaming).toBeTruthy();
  });

  test('isEcompGeneratedNaming should return false if isEcompGeneratedNaming is "false"', () => {
    let isEcompGeneratedNaming : boolean = vfModuleModel.isEcompGeneratedNaming({}, {
      properties: {
        ecomp_generated_naming: 'false'
      }
    });
    expect(isEcompGeneratedNaming).toBeFalsy();
  });

  test('isEcompGeneratedNaming should return false if isEcompGeneratedNaming is not defined', () => {
    let isEcompGeneratedNaming : boolean = vfModuleModel.isEcompGeneratedNaming({}, {
      properties: {
      }
    });
    expect(isEcompGeneratedNaming).toBeFalsy();
  });


  test('getTooltip should return "VFModule"', () => {
    let tooltip: string = vfModuleModel.getTooltip();
    expect(tooltip).toEqual('VFmodule');
  });

  test('getType should return "VFModule"', () => {
    let tooltip: string = vfModuleModel.getType();
    expect(tooltip).toEqual('VFmodule');
  });

  test('getNextLevelObject should return null', () => {
    let nextLevel = vfModuleModel.getNextLevelObject();
    expect(nextLevel).toBeNull();
  });

  test('getModel should return Module model', () => {
    let model = vfModuleModel.getModel('2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1',{}, getServiceHierarchy());
    expect(model.uuid).toEqual('25284168-24bb-4698-8cb4-3f509146eca5');
  });

  test('showNodeIcons should return false false if reachLimit of max', ()=>{
    let serviceId : string = 'servicedId';
    let node = {
      parent : {
        data : {
          id : 'vnfId',
          name : 'vnfName'
        }
      },
      data : {
        id : 'vnfId',
        name : 'vnfName'
      }
    };
    _sharedTreeService.setSelectedVNF({
      data : {
        id : 'vfModuleId',
        name : 'VfModuleName'
      }
    });
    jest.spyOn(vfModuleModel, 'getOptionalVNFs').mockReturnValue([]);
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global : {},
      service : {
        serviceHierarchy : {
          'servicedId' : {
            'vnfs' : {
              'vnfName' : {
                'properties' : {
                  'max_instances' : 1
                }
              }
            }
          }
        },
        serviceInstance : {
          'servicedId' : {
            'existingVNFCounterMap' : {
              'vnfId' : 1
            },
            'vnfs' : {
              'vnfName' :{

              }
            }
          }
        }
      }
    });

    let result = vfModuleModel.showNodeIcons(<any>node, serviceId);
    expect(result).toEqual(new AvailableNodeIcons(false , false));
  });

  test('showNodeIcons should return true, false if reachLimit of max', ()=>{
    let serviceId : string = 'servicedId';
    let node = {
      data : {
        id : 'vfModuleId',
        name : 'vfModuleName'
      },
      parent : {
        data : {
          id : 'vnfId',
          name : 'vnfName'
        }
      }
    };
    jest.spyOn(_sharedTreeService, 'getSelectedVNF').mockReturnValue('vnfName');
    jest.spyOn(vfModuleModel, 'getOptionalVNFs').mockReturnValue(['vnfName']);
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global : {},
      service : {
        serviceHierarchy : {
          'servicedId' : {
            'vnfs' : {
              'vnfName' : {
                'properties' : {
                  'max_instances' : 1
                }
              }
            },
            'vfModules' : {
              'vfModuleName' : {
                'properties' : {
                  maxCountInstances : 1
                }
              }
            }
          }
        },
        serviceInstance : {
          'servicedId' : {
            'existingVNFCounterMap' : {
              'vnfId' : 1
            },
            'vnfs' : {
              'vnfName' :{
                  'originalName' : 'vnfName',
                  'vfModules' : {
                    'vfModuleName' : {

                    }
                  }
              }
            }
          }
        }
      }
    });

    let result = vfModuleModel.showNodeIcons(<any>node, 'servicedId');
    expect(result).toEqual(new AvailableNodeIcons(true , false));
  });


  test('showNodeIcons should return false, true if reachLimit of max', ()=>{
    let serviceId : string = 'servicedId';
    let node = {
      data : {
        id : 'vfModuleId',
        name : 'vfModuleName'
      },
      parent : {
        data : {
          id : 'vnfId',
          name : 'vnfName'
        }
      }
    };
    jest.spyOn(_sharedTreeService, 'getSelectedVNF').mockReturnValue('vnfName');
    jest.spyOn(vfModuleModel, 'getOptionalVNFs').mockReturnValue(['vnfName']);
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global : {},
      service : {
        serviceHierarchy : {
          'servicedId' : {
            'vnfs' : {
              'vnfName' : {
                'properties' : {
                  'max_instances' : 1
                }
              }
            },
            'vfModules' : {
              'vfModuleName' : {
                'properties' : {
                  maxCountInstances : 2
                }
              }
            }
          }
        },
        serviceInstance : {
          'servicedId' : {
            'existingVNFCounterMap' : {
              'vnfId' : 1
            },
            'vnfs' : {
              'vnfName' :{
                'originalName' : 'vnfName',
                'vfModules' : {
                  'vfModuleName' : {

                  }
                }
              }
            }
          }
        }
      }
    });

    let result = vfModuleModel.showNodeIcons(<any>node, 'servicedId');
    expect(result).toEqual(new AvailableNodeIcons(true , false));
  });


  test('getOptionalVNFs should instance if exist', ()=>{
    let serviceId : string = 'servicedId';
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global : {},
      service : {
        serviceHierarchy : {
          'servicedId' : {
            'vnfs' : {
              'vnfName' : {
                'properties' : {
                  'max_instances' : 1
                }
              }
            },
            'vfModules' : {
              'vfModuleName' : {
                'properties' : {
                  maxCountInstances : 2
                }
              }
            }
          }
        },
        serviceInstance : {
          'servicedId' : {
            'existingVNFCounterMap' : {
              'vnfId' : 1
            },
            'vnfs' : {
              'vnfName' :{
                'originalName' : 'vnfName',
                'vfModules' : {
                  'vfModuleName' : {

                  }
                }
              }
            }
          }
        }
      }
    });

    let node = {
      data : {
        id : 'vfModuleId',
        name : 'vfModuleName'
      },
      parent : {
        data : {
          id : 'vnfId',
          name : 'vnfName'
        }
      }
    };
    let result = vfModuleModel.getOptionalVNFs(serviceId , 'vnfName');
    expect(result.length).toEqual(1);
  });

  test('getNodeCount should return number of nodes', ()=>{
    let serviceId : string = 'servicedId';
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global : {},
      service : {
        serviceHierarchy : {
          'servicedId' : {
            'vnfs' : {
              'vnfName' : {
                'properties' : {
                  'max_instances' : 1
                }
              }
            },
            'vfModules' : {
              'vfModuleName' : {
                'properties' : {
                  maxCountInstances : 2
                }
              }
            }
          }
        },
        serviceInstance : {
          'servicedId' : {
            'existingVNFCounterMap' : {
              'vnfId' : 1
            },
            'vnfs' : {
              'vnfName' :{
                'action': 'Create',
                'originalName' : 'vnfName',
                'vfModules' : {
                  'vfModuleName' : {
                      'vnfModuleName_111': {
                        'action': 'Create',
                        'modelInfo' : {
                          modelVersionId : 'vfModuleId'
                        }
                      }
                  }
                }
              }
            }
          }
        }
      }
    });

    let node = {
      data : {
        id : 'vfModuleId',
        name : 'vfModuleName',
        'action': 'Create',
      },
      parent : {
        data : {
          id : 'vnfId',
          name : 'vnfName',
          'action': 'Create',
        }
      }
    };
    let result = vfModuleModel.getNodeCount(<any>node , serviceId);
    expect(result).toEqual(1);
  });


  test('onClickAdd should open message box if no vnfStoreKey', ()=>{
    jest.spyOn(_sharedTreeService, 'getSelectedVNF').mockReturnValue(null);
    jest.spyOn(vfModuleModel, 'getDefaultVNF').mockReturnValue(null);
    jest.spyOn(MessageBoxService.openModal, 'next');
    vfModuleModel.onClickAdd(<any>{}, 'serviceId');
    expect(MessageBoxService.openModal.next).toHaveBeenCalled();
  });

  test('getMenuAction: showAuditInfoVfModule', ()=>{
    jest.spyOn(_sharedTreeService, 'isRetryMode').mockReturnValue(true);

    let node = {
      data : {
        "modelId": "6b528779-44a3-4472-bdff-9cd15ec93450",
        "action": "Create",
        "isFailed": true,
      }
    };
    let serviceModelId = "6b528779-44a3-4472-bdff-9cd15ec93450";
    let result = vfModuleModel.getMenuAction(<any>node, serviceModelId);
    spyOn(result['showAuditInfo'], 'method');
    expect(result['showAuditInfo']).toBeDefined();
    expect(result['showAuditInfo'].visible(node)).toBeTruthy();
    expect(result['showAuditInfo'].enable(node)).toBeTruthy();
    result['showAuditInfo']['method'](node, serviceModelId);
    expect(result['showAuditInfo']['method']).toHaveBeenCalledWith(node, serviceModelId);
  });

  test('Info for vfModule should be correct', () => {
    const model = getVFModule();
    const instance = getVFModuleInstance();
    let actualVNFInfo = vfModuleModel.getInfo(model,instance);
    let expectedVNFInfo = [
      ModelInformationItem.createInstance('Base module', false),
      ModelInformationItem.createInstance('Min instances', "0"),
      ModelInformationItem.createInstance("Max instances", null),
      ModelInformationItem.createInstance('Initial instances count',"0")
    ];
    expect(actualVNFInfo).toEqual(expectedVNFInfo);
  });

  function getVFModule(){
    return {
      "uuid":"522159d5-d6e0-4c2a-aa44-5a542a12a830",
      "invariantUuid":"98a7c88b-b577-476a-90e4-e25a5871e02b",
      "customizationUuid":"55b1be94-671a-403e-a26c-667e9c47d091",
      "description":null,
      "name":"VfVgeraldine..vflorence_vlc..module-1",
      "version":"2",
      "modelCustomizationName":"VfVgeraldine..vflorence_vlc..module-1",
      "properties":{
        "minCountInstances":0,
        "maxCountInstances":null,
        "initialCount":0,
        "vfModuleLabel":"vflorence_vlc",
        "baseModule":false
      },
      "inputs":{},
      "volumeGroupAllowed":false
    };
  }

  function getVFModuleInstance() {
    return {
      "action":"None",
      "instanceName":"ss820f_0918_db",
      "instanceId":"2c1ca484-cbc2-408b-ab86-25a2c15ce280",
      "orchStatus":"deleted",
      "productFamilyId":null,
      "lcpCloudRegionId":null,
      "tenantId":null,
      "modelInfo":{
        "modelCustomizationName":"VfVgeraldine..vflorence_vlc..module-1",
        "modelCustomizationId":"b200727a-1bf9-4e7c-bd06-b5f4c9d920b9",
        "modelInvariantId":"09edc9ef-85d0-4b26-80de-1f569d49e750",
        "modelVersionId":"522159d5-d6e0-4c2a-aa44-5a542a12a830",
        "modelType":"vfModule"
      },
      "instanceType":null,
      "provStatus":null,
      "inMaint":true,
      "uuid":"522159d5-d6e0-4c2a-aa44-5a542a12a830",
      "originalName":"VfVgeraldine..vflorence_vlc..module-1",
      "legacyRegion":null,
      "lineOfBusiness":null,
      "platformName":null,
      "trackById":"vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1:008",
      "isBase":false,
      "volumeGroupName":null
    };
  }


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
