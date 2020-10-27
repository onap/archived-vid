import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {getTestBed, TestBed} from "@angular/core/testing";
import {MockNgRedux, NgReduxTestingModule} from "@angular-redux/store/testing";
import {PnfModelInfoExtended} from "./pnf.model.info.extended";
import {DynamicInputsService} from "../../dynamicInputs.service";
import {SharedTreeService} from "../../shared.tree.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../../shared/store/reducers";
import {DefaultDataGeneratorService} from "../../../../../shared/services/defaultDataServiceGenerator/default.data.generator.service";
import {DialogService} from "ng2-bootstrap-modal";
import {DuplicateService} from "../../../duplicate/duplicate.service";
import {IframeService} from "../../../../../shared/utils/iframe.service";
import {ServiceInfoService} from "../../../../../shared/server/serviceInfo/serviceInfo.service";
import {ComponentInfoService} from "../../../component-info/component-info.service";
import {AaiService} from "../../../../../shared/services/aaiService/aai.service";
import {HttpClient, HttpHandler} from "@angular/common/http";
import {FeatureFlagsService} from "../../../../../shared/services/featureFlag/feature-flags.service";
import {PnfPopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/pnf/pnf.popup.service";
import {DrawingBoardModes} from "../../../drawing-board.modes";
import {ServiceInstanceActions} from "../../../../../shared/models/serviceInstanceActions";
import {VFModuleModelInfo} from "../vfModule/vfModule.model.info";
import {VNFModel} from "../../../../../shared/models/vnfModel";
import {PNFModel} from "../../../../../shared/models/pnfModel";
import {AvailableNodeIcons} from "../../../available-models-tree/available-models-tree.service";

class MockFeatureFlagsService extends FeatureFlagsService{
  getAllFlags(): { [p: string]: boolean } {
    return {};
  }
}

class NodeBuilder {
  static getPnfNode() {
    return this.getPnfNodeWithAction(ServiceInstanceActions.None);
  }

  static getPnfNodeWithAction(action: ServiceInstanceActions) {
    return {
      data: {
        "action": action,
        "pnfStoreKey": "PNF_KEY",
        "children": null,
        "name": "pnfName",
        "modelUniqueId": "modelCustomizationId",
        "menuActions": {
          "delete": "",
          "undoDelete": ""
        }
      },
      children: null,
      type: 'PNF'
    };
  }
}

describe('Pnf Model Info Extended', () => {
  let injector;
  let httpMock: HttpTestingController;
  let  _dynamicInputsService : DynamicInputsService;
  let  _sharedTreeService : SharedTreeService;
  let _serviceInfoService: ServiceInfoService;
  let _defaultDataGeneratorService : DefaultDataGeneratorService;
  let _dialogService : DialogService;
  let _pnfPopupService : PnfPopupService;
  let _duplicateService : DuplicateService;
  let _iframeService : IframeService;
  let _componentInfoService : ComponentInfoService;
  let _featureFlagsService : FeatureFlagsService;
  let _store : NgRedux<AppState>;
  let pnfModelExtended: PnfModelInfoExtended;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NgReduxTestingModule],
      providers: [
        MockNgRedux,
        DynamicInputsService,
        DialogService,
        PnfPopupService,
        DefaultDataGeneratorService,
        SharedTreeService,
        DuplicateService,
        AaiService,
        HttpClient,
        HttpHandler,
        {provide: FeatureFlagsService, useClass: MockFeatureFlagsService},
        ComponentInfoService,
        IframeService]
    }).compileComponents();

    injector = getTestBed();
    _sharedTreeService = injector.get(SharedTreeService);
    _store = injector.get(NgRedux);
    _featureFlagsService = injector.get(FeatureFlagsService);

    pnfModelExtended = new PnfModelInfoExtended(
      _store,
      _sharedTreeService,
      _dialogService,
      _pnfPopupService,
      _iframeService,
      _duplicateService,
      null,
      _dynamicInputsService);
  });

  test('pnfModelExtended should be defined', () => {
    expect(pnfModelExtended).toBeDefined();
  });

  test('getMenuAction: edit should not be visible when mode is null and actions is "None"', () => {
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global : {}
    });

    let node = NodeBuilder.getPnfNodeWithAction(ServiceInstanceActions.None);
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";
    let menuActions = pnfModelExtended.getMenuAction(<any>node, serviceModelId);

    expect(menuActions['edit']).toBeDefined();
    expect(menuActions['edit'].visible(node)).toBeFalsy();
    expect(menuActions['edit'].enable(node)).toBeFalsy();
  });

  test('getMenuAction: edit should be visible when mode is null and action is "Create"', () => {
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global : {}
    });

    let node = NodeBuilder.getPnfNodeWithAction(ServiceInstanceActions.Create);
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";
    let menuActions = pnfModelExtended.getMenuAction(<any>node, serviceModelId);

    expect(menuActions['edit']).toBeDefined();
    expect(menuActions['edit'].visible(node)).toBeTruthy();
    expect(menuActions['edit'].enable(node)).toBeTruthy();
  });

  test('getMenuAction: edit should be visible when mode is "RETRY" and action is "Create"', () => {
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global : {
        drawingBoardStatus : DrawingBoardModes.RETRY
      }
    });

    let node = NodeBuilder.getPnfNodeWithAction(ServiceInstanceActions.Create);
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";
    let menuActions = pnfModelExtended.getMenuAction(<any>node, serviceModelId);

    expect(menuActions['edit']).toBeDefined();
    expect(menuActions['edit'].visible(node)).toBeFalsy();
    expect(menuActions['edit'].enable(node)).toBeFalsy();
  });

  test('getMenuAction: showAuditInfo should be visible when mode is "RETRY"', () => {
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global : {
        drawingBoardStatus : DrawingBoardModes.RETRY
      }
    });

    let node = NodeBuilder.getPnfNodeWithAction(ServiceInstanceActions.Create);
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";
    let menuActions = pnfModelExtended.getMenuAction(<any>node, serviceModelId);

    expect(menuActions['showAuditInfo']).toBeDefined();
    expect(menuActions['showAuditInfo'].visible(node)).toBeTruthy();
    expect(menuActions['showAuditInfo'].enable(node)).toBeTruthy();
  });

  test('getMenuAction: showAuditInfo should not be visible when mode is not "RETRY" and action is "CREATE"', () => {
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global : {
        drawingBoardStatus : DrawingBoardModes.EDIT
      }
    });

    let node = NodeBuilder.getPnfNodeWithAction(ServiceInstanceActions.Create);
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";
    let menuActions = pnfModelExtended.getMenuAction(<any>node, serviceModelId);

    expect(menuActions['showAuditInfo']).toBeDefined();
    expect(menuActions['showAuditInfo'].visible(node)).toBeFalsy();
    expect(menuActions['showAuditInfo'].enable(node)).toBeFalsy();
  });

  test('getMenuAction: remove should dispatch 2 actions with proper data',() => {
    let node = NodeBuilder.getPnfNodeWithAction(ServiceInstanceActions.None);
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";
    let menuActions = pnfModelExtended.getMenuAction(<any>node, serviceModelId);

    spyOn(_store, 'dispatch');
    menuActions['remove'].method(node, serviceModelId);

    expect(_store.dispatch).toHaveBeenCalledWith(jasmine.objectContaining({
      type: "REMOVE_INSTANCE",
      storeKey: "PNF_KEY"
    }));
    expect(_store.dispatch).toHaveBeenCalledWith(jasmine.objectContaining({
      type: "CHANGE_INSTANCE_COUNTER",
      changeBy: -1
    }));
  });

  test('getMenuAction: remove should not dispatch actions when node has children',() => {
    let node = NodeBuilder.getPnfNodeWithAction(ServiceInstanceActions.None);
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";
    let menuActions = pnfModelExtended.getMenuAction(<any>node, serviceModelId);
    node.data.children = [
      {"not": "empty"}
    ];

    spyOn(_store, 'dispatch');
    menuActions['remove'].method(node, serviceModelId);

    expect(_store.dispatch).not.toHaveBeenCalledWith(jasmine.objectContaining({
      type: "REMOVE_INSTANCE",
      storeKey: "PNF_KEY"
    }));
    expect(_store.dispatch).not.toHaveBeenCalledWith(jasmine.objectContaining({
      type: "CHANGE_INSTANCE_COUNTER",
      changeBy: -1
    }));
  });

  test('getMenuAction: delete should dispatch delete action',() => {
    let node = NodeBuilder.getPnfNodeWithAction(ServiceInstanceActions.None);
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";
    let menuActions = pnfModelExtended.getMenuAction(<any>node, serviceModelId);

    spyOn(_store, 'dispatch');
    menuActions['delete'].method(node, serviceModelId);

    expect(_store.dispatch).toHaveBeenCalledWith(jasmine.objectContaining({
      type: "DELETE_PNF_INSTANCE",
      storeKey: "PNF_KEY"
    }));
  });

  test('getMenuAction: delete should show modal when node has children',() => {
    let node = NodeBuilder.getPnfNodeWithAction(ServiceInstanceActions.None);
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";
    let menuActions = pnfModelExtended.getMenuAction(<any>node, serviceModelId);
    node.data.children = [
      {"not": "empty"}
    ];

    spyOn(_sharedTreeService, 'shouldShowDeleteInstanceWithChildrenModal');
    menuActions['delete'].method(node, serviceModelId);

    expect(_sharedTreeService.shouldShowDeleteInstanceWithChildrenModal).toHaveBeenCalledWith(jasmine.objectContaining({
      type: "PNF"
    }), jasmine.anything(), jasmine.anything());
  });

  test('getMenuAction: delete should not be visible when service isMacro', () => {
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global : {
        drawingBoardStatus : DrawingBoardModes.EDIT
      },
      service : {
        serviceInstance: {
          "d6557200-ecf2-4641-8094-5393ae3aae60": {
            isALaCarte: false
          }
        }
      }
    });

    let node = NodeBuilder.getPnfNodeWithAction(ServiceInstanceActions.None);
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";
    let menuActions = pnfModelExtended.getMenuAction(<any>node, serviceModelId);

    expect(menuActions['delete']).toBeDefined();
    expect(menuActions['delete'].visible(node)).toBeFalsy();
    expect(menuActions['delete'].enable(node)).toBeFalsy();
  });

  test('getMenuAction: delete should not be visible when service is aLaCarte and Action is Create', () => {
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global : {
        drawingBoardStatus : DrawingBoardModes.EDIT
      },
      service : {
        serviceInstance: {
          "d6557200-ecf2-4641-8094-5393ae3aae60": {
            isALaCarte: true
          }
        }
      }
    });

    let node = NodeBuilder.getPnfNodeWithAction(ServiceInstanceActions.Create);
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";
    let menuActions = pnfModelExtended.getMenuAction(<any>node, serviceModelId);

    expect(menuActions['delete']).toBeDefined();
    expect(menuActions['delete'].visible(node)).toBeFalsy();
    expect(menuActions['delete'].enable(node)).toBeFalsy();
  });

  test('getMenuAction: delete should be visible when service is aLaCarte and Action is None', () => {
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global : {
        drawingBoardStatus : DrawingBoardModes.EDIT
      },
      service : {
        serviceInstance: {
          "d6557200-ecf2-4641-8094-5393ae3aae60": {
            isALaCarte: true
          }
        }
      }
    });

    let node = NodeBuilder.getPnfNodeWithAction(ServiceInstanceActions.None);
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";
    let menuActions = pnfModelExtended.getMenuAction(<any>node, serviceModelId);

    expect(menuActions['delete']).toBeDefined();
    expect(menuActions['delete'].visible(node)).toBeTruthy();
    expect(menuActions['delete'].enable(node)).toBeTruthy();
  });

  test('getMenuAction: undo delete should dispatch undo delete action when no children',() => {
    let node = NodeBuilder.getPnfNodeWithAction(ServiceInstanceActions.None);
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";
    let menuActions = pnfModelExtended.getMenuAction(<any>node, serviceModelId);

    spyOn(_store, 'dispatch');
    menuActions['undoDelete'].method(node, serviceModelId);

    expect(_store.dispatch).toHaveBeenCalledWith(jasmine.objectContaining({
      type: "UNDO_DELETE_PNF_INSTANCE",
      storeKey: "PNF_KEY"
    }));
  });

  test('getMenuAction: undo delete should iterate over children when they exist',() => {
    let node = NodeBuilder.getPnfNodeWithAction(ServiceInstanceActions.None);
    node.data.children = [
      {"not": "empty"}
    ];
    node.children = node.data.children;
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";
    let menuActions = pnfModelExtended.getMenuAction(<any>node, serviceModelId);

    spyOn(_store, 'dispatch');
    menuActions['undoDelete'].method(node, serviceModelId);

    expect(_store.dispatch).toHaveBeenCalledWith(jasmine.objectContaining({
      type: "UNDO_DELETE_PNF_INSTANCE",
      storeKey: "PNF_KEY"
    }));
  });

  test('getMenuAction: undo delete should not be visible when action is Create or Delete', () => {
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global : {
        drawingBoardStatus : DrawingBoardModes.EDIT
      },
      service : {
        serviceInstance: {
          "d6557200-ecf2-4641-8094-5393ae3aae60": { }
        }
      }
    });

    let node = NodeBuilder.getPnfNodeWithAction(ServiceInstanceActions.Create);
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";
    let menuActions = pnfModelExtended.getMenuAction(<any>node, serviceModelId);

    expect(menuActions['undoDelete']).toBeDefined();
    expect(menuActions['undoDelete'].visible(node)).toBeFalsy();
    expect(menuActions['undoDelete'].enable(node)).toBeFalsy();
  });

  test('getMenuAction: undo delete should be visible when action is contains "*_Delete"', () => {
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global : {
        drawingBoardStatus : DrawingBoardModes.EDIT
      },
      service : {
        serviceInstance: {
          "d6557200-ecf2-4641-8094-5393ae3aae60": {
            action: ServiceInstanceActions.None
          }
        }
      }
    });

    let node = NodeBuilder.getPnfNodeWithAction(ServiceInstanceActions.None_Delete);
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";
    let menuActions = pnfModelExtended.getMenuAction(<any>node, serviceModelId);

    expect(menuActions['undoDelete']).toBeDefined();
    expect(menuActions['undoDelete'].visible(node)).toBeTruthy();
    expect(menuActions['undoDelete'].enable(node, serviceModelId)).toBeTruthy();
  });

  test('getModel should return PNF model', () => {
    expect(pnfModelExtended.getModel({})).toBeInstanceOf(PNFModel);
  });

  test('getNextLevelObject should return null as there are no childs expected in PNF for now', () => {
    expect(pnfModelExtended.getNextLevelObject()).toBeNull();
  });

  test('getNodeCount should return counter of 0 when existingPNFCounterMap doesnt exist', ()=>{
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global : {},
      service : {
        serviceInstance : {
          'd6557200-ecf2-4641-8094-5393ae3aae60' : {
          }
        }
      }
    });

    let serviceId : string = 'd6557200-ecf2-4641-8094-5393ae3aae60';
    let node = NodeBuilder.getPnfNode();
    let result = pnfModelExtended.getNodeCount(<any>node , serviceId);
    jest.spyOn(_sharedTreeService, 'getExistingInstancesWithDeleteMode').mockReturnValue(0);

    expect(result).toEqual(0);
  });

  test('getNodeCount should return counter of 1 when one node exists and no nodes in delete mode', ()=>{
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global : {},
      service : {
        serviceInstance : {
          'd6557200-ecf2-4641-8094-5393ae3aae60' : {
            'existingPNFCounterMap' : {
              'modelCustomizationId' : 1
            }
          }
        }
      }
    });

    let serviceId : string = 'd6557200-ecf2-4641-8094-5393ae3aae60';
    let node = NodeBuilder.getPnfNode();
    let result = pnfModelExtended.getNodeCount(<any>node , serviceId);
    jest.spyOn(_sharedTreeService, 'getExistingInstancesWithDeleteMode').mockReturnValue(0);

    expect(result).toEqual(1);
  });

  test('getNodeCount should return counter of 2 when three nodes exist and one node is in delete mode', ()=>{
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global : {},
      service : {
        serviceInstance : {
          'd6557200-ecf2-4641-8094-5393ae3aae60' : {
            'existingPNFCounterMap' : {
              'modelCustomizationId' : 3
            }
          }
        }
      }
    });

    jest.spyOn(_sharedTreeService, 'getExistingInstancesWithDeleteMode').mockReturnValue(1);
    let serviceId : string = 'd6557200-ecf2-4641-8094-5393ae3aae60';
    let node = NodeBuilder.getPnfNode();
    let result = pnfModelExtended.getNodeCount(<any>node , serviceId);

    expect(result).toEqual(2);
  });

  test('getTooltip should return "PNF"', () => {
    expect(pnfModelExtended.getTooltip()).toEqual('PNF');
  });

  test('getType should return "PNF"', () => {
    expect(pnfModelExtended.getType()).toEqual('PNF');
  });

  test('isEcompGeneratedNaming should return true if isEcompGeneratedNaming is "true" ', () => {
    let isEcompGeneratedNaming: boolean = pnfModelExtended.isEcompGeneratedNaming(<any>{
      properties: {
        ecomp_generated_naming: 'true'
      }
    });
    expect(isEcompGeneratedNaming).toBeTruthy();
  });

  test('isEcompGeneratedNaming should return false if isEcompGeneratedNaming is not defined', () => {
    let isEcompGeneratedNaming: boolean = pnfModelExtended.isEcompGeneratedNaming({
      properties: {
      }
    });
    expect(isEcompGeneratedNaming).toBeFalsy();
  });

  test('showNodeIcons should return false if reached limit of instances', () => {
    let serviceId: string = 'servicedId';
    let node = NodeBuilder.getPnfNode();
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global: {},
      service: {
        serviceHierarchy: {
          'servicedId': {
            'pnfs': {
              modelCustomizationId: "modelCustomizationId",
              'modelInfo': {
                modelCustomizationId: "modelCustomizationId"
              },
              'pnfName': {
                'properties': {
                  'max_instances': 1
                }
              }
            }
          }
        },
        serviceInstance: {
          'servicedId': {
            'existingPNFCounterMap': {
              'modelCustomizationId': 1
            },
            'pnfs': {
              'pnfName': {}
            }
          }
        }
      }
    });

    let result = pnfModelExtended.showNodeIcons(<any>node, serviceId);
    expect(result).toEqual(new AvailableNodeIcons(false, true));
  });

  test('showNodeIcons should return true if limit of instances is not reached', () => {
    let serviceId: string = 'servicedId';
    let node = NodeBuilder.getPnfNode();
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global: {
        drawingBoardStatus : DrawingBoardModes.EDIT
      },
      service: {
        serviceHierarchy: {
          'servicedId': {
            'pnfs': {
              modelCustomizationId: "modelCustomizationId",
              'modelInfo': {
                modelCustomizationId: "modelCustomizationId"
              },
              'pnfName': {
                'properties': {
                  'max_instances': 2
                }
              }
            }
          }
        },
        serviceInstance: {
          'servicedId': {
            'existingPNFCounterMap': {
              'modelCustomizationId': 1
            },
            'pnfs': {
              'pnfName': {}
            }
          }
        }
      }
    });

    let result = pnfModelExtended.showNodeIcons(<any>node, serviceId);
    expect(result).toEqual(new AvailableNodeIcons(true, false));
  });

});
