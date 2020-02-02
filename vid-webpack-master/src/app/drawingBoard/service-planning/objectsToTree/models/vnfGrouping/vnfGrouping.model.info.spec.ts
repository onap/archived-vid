import {HttpClientTestingModule} from "@angular/common/http/testing";
import {getTestBed, TestBed} from "@angular/core/testing";
import {MockNgRedux, NgReduxTestingModule} from "@angular-redux/store/testing";
import {VnfGroupingModelInfo} from "./vnfGrouping.model.info";
import {DynamicInputsService} from "../../dynamicInputs.service";
import {SharedTreeService} from "../../shared.tree.service";
import {VnfGroupPopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/vnfGroup/vnfGroup.popup.service";
import {DialogService} from "ng2-bootstrap-modal";
import {AvailableNodeIcons} from "../../../available-models-tree/available-models-tree.service";
import {VnfGroupModel} from "../../../../../shared/models/vnfGroupModel";
import {IframeService} from "../../../../../shared/utils/iframe.service";
import {RouterTestingModule} from "@angular/router/testing";
import {DrawingBoardModes} from "../../../drawing-board.modes";
import {AaiService} from "../../../../../shared/services/aaiService/aai.service";
import {FeatureFlagsService} from "../../../../../shared/services/featureFlag/feature-flags.service";
import {ITableContent} from "../../../../../shared/components/searchMembersModal/members-table/element-table-row.model";
import {VNFModel} from "../../../../../shared/models/vnfModel";

describe('VnfGroupingModelInfo Model Info', () => {
  let injector;
  let _dialogService : DialogService;
  let _vnfGroupPopupService : VnfGroupPopupService;
  let  _dynamicInputsService : DynamicInputsService;
  let _sharedTreeService : SharedTreeService;
  let _iframeService : IframeService;
  let vnfGroupModel: VnfGroupingModelInfo;
  let _aaiService : AaiService;
  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NgReduxTestingModule, RouterTestingModule],
      providers: [
        DialogService,
        VnfGroupPopupService,
        DynamicInputsService,
        SharedTreeService,
        IframeService,
        AaiService,
        FeatureFlagsService,
        MockNgRedux]
    });
    await TestBed.compileComponents();
    injector = getTestBed();
    _sharedTreeService = injector.get(SharedTreeService);
    _aaiService = injector.get(AaiService);

    vnfGroupModel =  new VnfGroupingModelInfo(_dynamicInputsService, _sharedTreeService, _dialogService, _vnfGroupPopupService, _iframeService,  _aaiService, MockNgRedux.getInstance());
  })().then(done).catch(done.fail));


  test('VnfGroupingModelInfo should be defined', () => {
    expect(vnfGroupModel).toBeDefined();
  });

  test('VnfGroupingModelInfo should defined extra details', () => {
    expect(vnfGroupModel.name).toEqual('vnfGroups');
    expect(vnfGroupModel.type).toEqual('VnfGroup');
  });

  test('isEcompGeneratedNaming should return true if vnf group has isEcompGeneratedNaming is "true" ', () => {
    let isEcompGeneratedNaming : boolean = vnfGroupModel.isEcompGeneratedNaming({
      properties: {
        ecomp_generated_naming: 'true'
      }
    });
    expect(isEcompGeneratedNaming).toBeTruthy();
  });

  test('getTooltip should return "vnfGrouping"', () => {
    let tooltip: string = vnfGroupModel.getTooltip();
    expect(tooltip).toEqual('VnfGroup');
  });

  test('getType should return "vnfGrouping"', () => {
    let tooltip: string = vnfGroupModel.getType();
    expect(tooltip).toEqual('VnfGroup');
  });

  test('getNextLevelObject should return null', () => {
    let nextLevel = vnfGroupModel.getNextLevelObject();
    expect(nextLevel).not.toBeNull();
  });

  test('showNodeIcons should return true if not reachLimit of max', ()=>{
    let serviceId : string = 'servicedId';
    let node = {
      data : {
        id : 'vnfGroupId',
        name : 'vnfGroupName'
      }
    };
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
        global:{
          drawingBoardStatus: "EDIT"
        },
      service : {
        serviceHierarchy : {
          'servicedId' : {
            'vnfGroups' : {
              'vnfGroupName' : {
                'properties' : {
                }
              }
            }
          }
        },
        serviceInstance : {
          'servicedId' : {
            'existingVnfGroupCounterMap' : {
              'vnfGroupId' : 1
            },
            'vnfGroups' : {
              'vnfGroupName' :{

              }
            }
          }
        }
      }
    });

    let result = vnfGroupModel.showNodeIcons(<any>node, serviceId);
    expect(result).toEqual(new AvailableNodeIcons(true , false));
  });

  test('getNodeCount should return number of nodes', ()=>{
    let serviceId : string = 'servicedId';
    jest.spyOn(_sharedTreeService, 'getExistingInstancesWithDeleteMode').mockReturnValue(0);
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global : {},
      service : {
        serviceHierarchy : {
          'servicedId' : {
            'vnfGroups' : {
              'vnfGroupName' : {
                'properties' : {
                }
              }
            }
          }
        },
        serviceInstance : {
          'servicedId' : {
            'existingVnfGroupCounterMap' : {
              'vnfGroupId' : 1
            },
            'vnfGroups' : {
              'vnfGroupName' :{
                'originalName' : 'vnfGroupName'
              }
            }
          }
        }
      }
    });

    let node = {
      data : {
        id : 'vnfGroupId',
        name : 'vnfGroupName',
        modelUniqueId: 'vnfGroupId',
      }
    };
    let result = vnfGroupModel.getNodeCount(<any>node , serviceId);
    expect(result).toEqual(1);

    node.data.modelUniqueId = 'vnfGroupId_notExist';
    result = vnfGroupModel.getNodeCount(<any>node , serviceId);
    expect(result).toEqual(0);

    result = vnfGroupModel.getNodeCount(<any>node , serviceId + '_notExist');
    expect(result).toEqual(0);
  });

  test('getModel should return VnfGroup model', () => {
    expect(vnfGroupModel.getModel({})).toBeInstanceOf(VnfGroupModel);
  });

  test('getMenuAction: showAuditInfoVnfGroup', ()=>{

    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global: {
        "drawingBoardStatus": DrawingBoardModes.RETRY
      }
    });
    jest.spyOn(_sharedTreeService, 'isRetryMode').mockReturnValue(true);
    let node = {
      data : {
        "modelId": "6b528779-44a3-4472-bdff-9cd15ec93450",
        "action": "Create",
        "isFailed": true,
      }
    };
    let serviceModelId = "6b528779-44a3-4472-bdff-9cd15ec93450";
    let result = vnfGroupModel.getMenuAction(<any>node, serviceModelId);
    spyOn(result['showAuditInfo'], 'method');
    expect(result['showAuditInfo']).toBeDefined();
    expect(result['showAuditInfo'].visible(node)).toBeTruthy();
    expect(result['showAuditInfo'].enable(node)).toBeTruthy();
    result['showAuditInfo']['method'](node, serviceModelId);
    expect(result['showAuditInfo']['method']).toHaveBeenCalledWith(node, serviceModelId);
  });


  test('generateRelatedMemberTableContent', ()=> {
    const tableContents: ITableContent[] = vnfGroupModel.generateRelatedMemberTableContent();
    expect(tableContents).toEqual([
      {
        id: 'vnfName',
        contents: [{
          id: ['instanceName'],
          value: ['instanceName']
        }, {
          id: ['instanceId'],
          value: ["instanceId"],
          prefix: 'UUID: '
        }]
      },
      {
        id: 'version',
        contents: [{
          id: ['modelInfo', 'modelVersion'],
          value: ['modelInfo', 'modelVersion']
        }]
      },
      {
        id: 'modelName',
        contents: [{
          id: ['modelInfo', 'modelName'],
          value: ['modelInfo', 'modelName']
        }]
      },
      {
        id: 'provStatus',
        contents: [{
          id: ['provStatus'],
          value: ['provStatus']
        }]
      },
      {
        id: 'serviceInstance',
        contents: [{
          id: ['serviceInstanceName'],
          value: ['serviceInstanceName']
        }, {
          id: ['serviceInstanceId'],
          value: ["serviceInstanceId"],
          prefix: 'UUID: '
        }]
      },
      {
        id: 'cloudRegion',
        contents: [{
          id: ['lcpCloudRegionId'],
          value: ['lcpCloudRegionId']
        }]
      },
      {
        id: 'tenantName',
        contents: [{
          id: ['tenantName'],
          value: ['tenantName']
        }]
      }
    ]);
  });


});
