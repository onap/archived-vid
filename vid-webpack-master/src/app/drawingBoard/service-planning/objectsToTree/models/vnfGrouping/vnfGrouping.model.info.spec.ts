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
import {Utils} from "../../../../../shared/utils/utils";

describe('VnfGroupingModelInfo Model Info', () => {
  let injector;
  let _dialogService : DialogService;
  let _vnfGroupPopupService : VnfGroupPopupService;
  let  _dynamicInputsService : DynamicInputsService;
  let _sharedTreeService : SharedTreeService;
  let _iframeService : IframeService;
  let vnfGroupModel: VnfGroupingModelInfo;
  let _aaiService : AaiService;
  let _utils: Utils;
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
        Utils,
        FeatureFlagsService,
        MockNgRedux]
    });
    await TestBed.compileComponents();
    injector = getTestBed();
    _sharedTreeService = injector.get(SharedTreeService);
    _aaiService = injector.get(AaiService);
    _utils = injector.get(Utils);

    vnfGroupModel =  new VnfGroupingModelInfo(_dynamicInputsService, _sharedTreeService, _dialogService, _vnfGroupPopupService, _iframeService, _aaiService, _utils, MockNgRedux.getInstance());
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
    let model: VnfGroupModel = vnfGroupModel.getModel('ResourceGroup0', <any>{
      originalName : 'ResourceGroup0'
    }, getServiceHierarchy());
    expect(model.type).toEqual('VnfGroup');
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
      "vnfGroups": {
        "ResourceGroup0": {
          "type": "VnfGroup",
          "uuid": "0903e1c0-8e03-4936-b5c2-260653b96413",
          "invariantUuid": "00beb8f9-6d39-452f-816d-c709b9cbb87d",
          "version": "1",
          "name": "ResourceGroup0",
          "modelCustomizationName": "ResourceGroup0",
          "properties": {
            "contained_resource_type": "VF",
            "role": "SERVICE-ACCESS",
            "function": "DATA",
            "description": "DDD0",
            "type": "LOAD-GROUP"
          },
          "members": {
            "vdorothea_svc_vprs_proxy 0": {
              "uuid": "65fadfa8-a0d9-443f-95ad-836cd044e26c",
              "invariantUuid": "f4baae0c-b3a5-4ca1-a777-afbffe7010bc",
              "description": "A Proxy for Service vDOROTHEA_Svc_vPRS",
              "name": "vDOROTHEA_Svc_vPRS Service Proxy",
              "version": "1.0",
              "customizationUuid": "bdb63d23-e132-4ce7-af2c-a493b4cafac9",
              "inputs": {},
              "commands": {},
              "properties": {},
              "type": "Service Proxy",
              "sourceModelUuid": "da7827a2-366d-4be6-8c68-a69153c61274",
              "sourceModelInvariant": "24632e6b-584b-4f45-80d4-fefd75fd9f14",
              "sourceModelName": "vDOROTHEA_Svc_vPRS"
            },
            "vdorothea_svc_vprs_proxy 1": {
              "uuid": "111dfa8-a0d9-443f-95ad-836cd044e26c",
              "invariantUuid": "111ae0c-b3a5-4ca1-a777-afbffe7010bc",
              "description": "A Proxy for Service vDOROTHEA_Svc_vPRS",
              "name": "111_Svc_vPRS Service Proxy",
              "version": "1.0",
              "customizationUuid": "1113d23-e132-4ce7-af2c-a493b4cafac9",
              "inputs": {},
              "commands": {},
              "properties": {},
              "type": "Service Proxy",
              "sourceModelUuid": "11127a2-366d-4be6-8c68-a69153c61274",
              "sourceModelInvariant": "1112e6b-584b-4f45-80d4-fefd75fd9f14",
              "sourceModelName": "111_Svc_vPRS"
            }
          }
        }
      },
      "networks": {},
      "collectionResources": {},
      "configurations": {},
      "fabricConfigurations": {},
      "serviceProxies": {},
      "vfModules": {},
      "volumeGroups": {},
      "pnfs": {}
    }
  }

  function loadMockMembers(): any[] {
    return [
      {
        "action": "None",
        "instanceName": "VNF1_INSTANCE_NAME",
        "instanceId": "VNF1_INSTANCE_ID",
        "orchStatus": null,
        "productFamilyId": null,
        "lcpCloudRegionId": "hvf23b",
        "tenantId": "3e9a20a3e89e45f884e09df0cc2d2d2a",
        "tenantName": "APPC-24595-T-IST-02C",
        "modelInfo": {
          "modelInvariantId": "vnf-instance-model-invariant-id",
          "modelVersionId": "7a6ee536-f052-46fa-aa7e-2fca9d674c44",
          "modelVersion": "2.0",
          "modelName": "vf_vEPDG",
          "modelType": "vnf"
        },
        "instanceType": "VNF1_INSTANCE_TYPE",
        "provStatus": null,
        "inMaint": false,
        "uuid": "7a6ee536-f052-46fa-aa7e-2fca9d674c44",
        "originalName": null,
        "legacyRegion": null,
        "lineOfBusiness": null,
        "platformName": null,
        "trackById": "7a6ee536-f052-46fa-aa7e-2fca9d674c44:002",
        "serviceInstanceId": "service-instance-id1",
        "serviceInstanceName": "service-instance-name"
      },
      {
        "action": "None",
        "instanceName": "VNF2_INSTANCE_NAME",
        "instanceId": "VNF2_INSTANCE_ID",
        "orchStatus": null,
        "productFamilyId": null,
        "lcpCloudRegionId": "hvf23b",
        "tenantId": "3e9a20a3e89e45f884e09df0cc2d2d2a",
        "tenantName": "APPC-24595-T-IST-02C",
        "modelInfo": {
          "modelInvariantId": "vnf-instance-model-invariant-id",
          "modelVersionId": "eb5f56bf-5855-4e61-bd00-3e19a953bf02",
          "modelVersion": "1.0",
          "modelName": "vf_vEPDG",
          "modelType": "vnf"
        },
        "instanceType": "VNF2_INSTANCE_TYPE",
        "provStatus": null,
        "inMaint": true,
        "uuid": "eb5f56bf-5855-4e61-bd00-3e19a953bf02",
        "originalName": null,
        "legacyRegion": null,
        "lineOfBusiness": null,
        "platformName": null,
        "trackById": "eb5f56bf-5855-4e61-bd00-3e19a953bf02:003",
        "serviceInstanceId": "service-instance-id2",
        "serviceInstanceName": "service-instance-name"
      }
    ];
  }


});
