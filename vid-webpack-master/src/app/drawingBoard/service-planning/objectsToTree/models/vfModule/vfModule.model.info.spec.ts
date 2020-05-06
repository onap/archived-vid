import {HttpClientTestingModule} from "@angular/common/http/testing";
import {getTestBed, TestBed} from "@angular/core/testing";
import {MockNgRedux, NgReduxTestingModule} from "@angular-redux/store/testing";
import {VFModuleModelInfo} from "./vfModule.model.info";
import {DynamicInputsService} from "../../dynamicInputs.service";
import {SharedTreeService} from "../../shared.tree.service";
import {VfModulePopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/vfModule/vfModule.popup.service";
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
import {instance, mock, when} from "ts-mockito";
import each from "jest-each";
import {VfModule} from "../../../../../shared/models/vfModule";
import {VfModuleTreeNode} from "../../../../../shared/models/vfModuleTreeNode";

class MockAppStore<T> {
  getState() {
    return {
      global: {
        'drawingBoardStatus': DrawingBoardModes.CREATE
      }
    }
  }
}

describe('VFModule Model Info', () => {
  let injector;
  let  _dynamicInputsService : DynamicInputsService;
  let  _sharedTreeService : SharedTreeService;
  let  vfModuleModel: VFModuleModelInfo;
  let _dialogService : DialogService;
  let _vfModulePopupService : VfModulePopupService;
  let _vfModuleUpgradePopupService : VfModuleUpgradePopupService;
  let _iframeService : IframeService;
  let _componentInfoService : ComponentInfoService;
  let mockFeatureFlagsService: FeatureFlagsService = mock(FeatureFlagsService);
  let mockFeatureFlagsServiceInstance: FeatureFlagsService = instance(mockFeatureFlagsService);


  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NgReduxTestingModule],
      providers: [
        DynamicInputsService,
        DialogService,
        VfModulePopupService,
        VfModuleUpgradePopupService,
        SharedTreeService,
        IframeService,
        {provide: NgRedux, useClass: MockAppStore},
        MockNgRedux,
        AaiService,
        HttpClient,
        HttpHandler,
        {provide: FeatureFlagsService, useValue: mockFeatureFlagsServiceInstance},
        ComponentInfoService
      ]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    _sharedTreeService = injector.get(SharedTreeService);
    _componentInfoService = injector.get(ComponentInfoService);
    vfModuleModel = new VFModuleModelInfo(_dynamicInputsService, _sharedTreeService, _dialogService, _vfModulePopupService, _vfModuleUpgradePopupService,
      _iframeService, mockFeatureFlagsServiceInstance,  MockNgRedux.getInstance(),_componentInfoService);

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

  each([
    ['afterCompletion', 'afterCompletion'],
    ['undefined', undefined]
  ]).
  test('createNode should return pauseInstantiation status %s', (description, pauseInstantiationStatus) => {
    const modelName: string = "vfModuleModelName";
    const vfModuleInstance =  {
      "vfModuleInstanceName": {
        "pauseInstantiation": pauseInstantiationStatus,
      }
    };
    const currentModel = {};
    const parentModel = {};
    const serviceModelId = "serviceModelId";

    let actual: VfModuleTreeNode = vfModuleModel.createNode(<any>vfModuleInstance, <any>currentModel, <any>parentModel, modelName, 0, serviceModelId);
    let expected: string = pauseInstantiationStatus;
    expect(actual.pauseInstantiation).toEqual(expected);
  });

  test('getModel should return Module model', () => {
    let model = vfModuleModel.getModel({
      "uuid": "25284168-24bb-4698-8cb4-3f509146eca5",
      "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
      "customizationUuid": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401",
      "description": null,
      "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
      "version": "6",
      "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
      "properties": {
        "minCountInstances": 2,
        "maxCountInstances": 3,
        "initialCount": 0,
        "vfModuleLabel": "PASQUALE_vRE_BV",
        "baseModule": false
      },
      "inputs": {
        "vnf_config_template_version": {
          "type": "string"
        }
      },
      "volumeGroupAllowed": true
    });
    expect(model).toBeInstanceOf(VfModule);
    expect(model.uuid).toEqual('25284168-24bb-4698-8cb4-3f509146eca5');
    expect(model.min).toBe(2);
    expect(model.max).toBe(3);
    expect(model.baseModule).toBeFalsy();
    expect(model.inputs).toEqual(
      {"vnf_config_template_version": {
        "type": "string"
      }});
    expect(model.volumeGroupAllowed).toBeTruthy();
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
    let node = {
      data : {
        id : 'vfModuleId',
        modelUniqueId : 'vfModuleCustomizationId'
      },
      parent : {
        data : {
          id : 'vnfId',
          modelUniqueId : 'vnfCustomizationId'
        }
      }
    };
    jest.spyOn(_sharedTreeService, 'getSelectedVNF').mockReturnValue('vnfName');
    jest.spyOn(_sharedTreeService, 'modelUniqueId').mockReturnValue('vnfCustomizationId');
    jest.spyOn(vfModuleModel, 'getOptionalVNFs').mockReturnValue([{vnfStoreKey: 'vnfName'}]);
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global : {},
      service : {
        serviceHierarchy : {
          'servicedId' : {
            'vnfs' : {
              'vnfName' : {
                modelInfo: {
                  modelCustomizationId: 'vnfCustomizationId'
                },
                'properties' : {
                  'max_instances' : 1
                }
              }
            },
            'vfModules' : {
              'vfModuleName' : {
                modelInfo: {
                  modelCustomizationId: 'vfModuleCustomizationId'
                },
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
    let vfModuleModelUniqueId = 'vfModuleCustomizationId';
    jest.spyOn(_sharedTreeService, 'modelUniqueId').mockReturnValue(vfModuleModelUniqueId);
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global : {},
      service : {
        serviceHierarchy : {
          'servicedId' : {
            'vnfs' : {
              'vnfName' : {
                modelInfo: {
                  modelCustomizationId: 'vnfCustomizationId'
                },
                'properties' : {
                  'max_instances' : 1
                }
              }
            },
            'vfModules' : {
              'vfModuleName' : {
                modelInfo: {
                  modelCustomizationId: vfModuleModelUniqueId
                },
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
                          modelCustomizationId : vfModuleModelUniqueId
                        }
                      },
                      'vnfModuleName_111_1': {
                        'action': 'Create',
                        'modelInfo' : {
                          modelCustomizationId : vfModuleModelUniqueId
                        }
                      }
                  }
                }
              },
              'vnfName_1' :{
                'action': 'Create',
                'originalName' : 'vnfName',
                'vfModules' : {
                  'vfModuleName' : {
                    'vnfModuleName_111': {
                      'action': 'Create',
                      'modelInfo' : {
                        modelCustomizationId : vfModuleModelUniqueId
                      }
                    },
                    'vnfModuleName_111_1': {
                      'action': 'Create',
                      'modelInfo' : {
                        modelCustomizationId : vfModuleModelUniqueId
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
        modelUniqueId: vfModuleModelUniqueId,
        'action': 'Create',
      },
      parent : {
        data : {
          id : 'vnfId',
          modelUniqueId: 'vnfCustomizationId',
          'action': 'Create',
        }
      }
    };
    let result = vfModuleModel.getNodeCount(<any>node , serviceId);
    expect(result).toEqual(2);
  });


  test('getNodeCount should return number of nodes : there is selectedVNF', ()=>{
    let serviceId : string = 'servicedId';
    let vfModuleModelUniqueId = 'vfModuleCustomizationId';
    jest.spyOn(_sharedTreeService, 'modelUniqueId').mockReturnValue(vfModuleModelUniqueId);
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
                        modelCustomizationId : vfModuleModelUniqueId
                      }
                    },
                    'vnfModuleName_111_1': {
                      'action': 'Create',
                      'modelInfo' : {
                        modelCustomizationId : vfModuleModelUniqueId
                      }
                    }
                  }
                }
              },
              'vnfName_1' :{
                'action': 'Create',
                'originalName' : 'vnfName',
                'vfModules' : {
                  'vfModuleName' : {
                    'vnfModuleName_111': {
                      'action': 'Create',
                      'modelInfo' : {
                        modelCustomizationId: vfModuleModelUniqueId
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
    jest.spyOn(_sharedTreeService, 'getSelectedVNF').mockReturnValue('vnfName_1');

    let node = {
      data : {
        id : 'vfModuleId',
        modelUniqueId: vfModuleModelUniqueId,
        'action': 'Create',
      },
      parent : {
        data : {
          id : 'vnfId',
          modelUniqueId: 'vnfCustomizationId',
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
      ModelInformationItem.createInstance("Max instances", 'Unlimited (default)'),
      ModelInformationItem.createInstance('Initial instances count',"0")
    ];
    expect(actualVNFInfo).toEqual(expectedVNFInfo);
  });

  each([
    ["maxCountInstances 3, currentNodeCount 1, flag on",{maxCountInstances:3}, 1, {FLAG_2002_UNLIMITED_MAX: true}, false],
    ["maxCountInstances 3, currentNodeCount 3, flag on",{maxCountInstances:3}, 3, {FLAG_2002_UNLIMITED_MAX: true}, true],
    ["no maxCountInstances, currentNodeCount 0, flag off",{}, 0, {FLAG_2002_UNLIMITED_MAX: false}, false],
    ["no maxCountInstances, currentNodeCount 1, flag off",{}, 1, {FLAG_2002_UNLIMITED_MAX: false}, true],
    ["no maxCountInstances, currentNodeCount 1, no flags",{}, 1, null, true],
    ["no maxCountInstances, currentNodeCount 0, flag on",{}, 0, {FLAG_2002_UNLIMITED_MAX: true}, false],
    ["no maxCountInstances, currentNodeCount 1, flag on",{}, 1, {FLAG_2002_UNLIMITED_MAX: true}, false],
    ["no maxCountInstances, currentNodeCount 1000, flag on",{}, 1000, {FLAG_2002_UNLIMITED_MAX: true}, false],
  ]).test('isVFModuleReachedLimit: %s', (desc, properties, currentNodeCount, flags, expected) => {

    const node = { data: {
        name : 'vfModuleName'
    }};

    const serviceHierarchy = {
      servicedId :{
        vfModules : {
          vfModuleName : {
            properties
     }}}};

    when(mockFeatureFlagsService.getAllFlags()).thenReturn(flags);

    expect(vfModuleModel.isVFModuleReachedLimit(node, serviceHierarchy, 'servicedId', currentNodeCount)).toEqual(expected);
  });

  function getVFModule(): Partial<VfModule>{
    return {
      "uuid":"522159d5-d6e0-4c2a-aa44-5a542a12a830",
      "invariantUuid":"98a7c88b-b577-476a-90e4-e25a5871e02b",
      "customizationUuid":"55b1be94-671a-403e-a26c-667e9c47d091",
      "description":null,
      "name":"VfVgeraldine..vflorence_vlc..module-1",
      "version":"2",
      "modelCustomizationName":"VfVgeraldine..vflorence_vlc..module-1",
      "min":0,
      "max":null,
      "initial":0,
      "baseModule":false,
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

});
