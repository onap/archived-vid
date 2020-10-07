import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {getTestBed, TestBed} from "@angular/core/testing";
import {MockNgRedux, NgReduxTestingModule} from "@angular-redux/store/testing";
import {VnfModelInfo} from "./vnf.model.info";
import {DynamicInputsService} from "../../dynamicInputs.service";
import {VFModuleModelInfo} from "../vfModule/vfModule.model.info";
import {VNFModel} from "../../../../../shared/models/vnfModel";
import {SharedTreeService} from "../../shared.tree.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../../shared/store/reducers";
import {DefaultDataGeneratorService} from "../../../../../shared/services/defaultDataServiceGenerator/default.data.generator.service";
import {DialogService} from "ng2-bootstrap-modal";
import {VfModulePopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/vfModule/vfModule.popup.service";
import {VnfPopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/vnf/vnf.popup.service";
import {AvailableNodeIcons} from "../../../available-models-tree/available-models-tree.service";
import {DuplicateService} from "../../../duplicate/duplicate.service";
import {IframeService} from "../../../../../shared/utils/iframe.service";
import {ServiceInfoService} from "../../../../../shared/server/serviceInfo/serviceInfo.service";
import {AuditInfoModalComponent} from "../../../../../shared/components/auditInfoModal/auditInfoModal.component";
import {ModelInformationItem} from "../../../../../shared/components/model-information/model-information.component";
import {ComponentInfoService} from "../../../component-info/component-info.service";
import {AaiService} from "../../../../../shared/services/aaiService/aai.service";
import {HttpClient, HttpHandler} from "@angular/common/http";
import {FeatureFlagsService} from "../../../../../shared/services/featureFlag/feature-flags.service";
import {VfModuleUpgradePopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/vfModuleUpgrade/vfModule.upgrade.popuop.service";
import { PnfPopupService } from "../../../../../shared/components/genericFormPopup/genericFormServices/pnf/pnf.popup.service";

class MockFeatureFlagsService extends  FeatureFlagsService{
  getAllFlags(): { [p: string]: boolean } {
    return {};
  }
}

describe('Vnf Model Info', () => {
 let injector;
 let httpMock: HttpTestingController;
 let  _dynamicInputsService : DynamicInputsService;
 let  _sharedTreeService : SharedTreeService;
 let _serviceInfoService: ServiceInfoService;
 let _defaultDataGeneratorService : DefaultDataGeneratorService;
 let _dialogService : DialogService;
 let _vfModulePopupService : VfModulePopupService;
 let _vfModuleUpgradePopupService : VfModuleUpgradePopupService;
 let _vnfPopupService : VnfPopupService;
 let _duplicateService : DuplicateService;
 let _iframeService : IframeService;
 let _componentInfoService : ComponentInfoService;
 let _featureFlagsService : FeatureFlagsService;

  let _store : NgRedux<AppState>;
  let vnfModel: VnfModelInfo;
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

    vnfModel = new VnfModelInfo(
      _dynamicInputsService,
      _sharedTreeService,
      _defaultDataGeneratorService,
      _dialogService,
      _vnfPopupService,
      _vfModulePopupService,
      _vfModuleUpgradePopupService,
      _duplicateService,
      null,
      _iframeService,
      _componentInfoService,
      _featureFlagsService,
      _store);


  });

  test('VnfModelInfo should be defined', () => {
    expect(VnfModelInfo).toBeDefined();
  });

  test('VnfModelInfo should defined extra details', () => {
    expect(vnfModel.name).toEqual('vnfs');
    expect(vnfModel.type).toEqual('VNF');
    expect(vnfModel.childNames).toEqual(['vfModules']);
  });

  test('isEcompGeneratedNaming should return true if isEcompGeneratedNaming is "true" ', () => {
    let isEcompGeneratedNaming: boolean = vnfModel.isEcompGeneratedNaming(<any>{
      properties: {
        ecomp_generated_naming: 'true'
      }
    });
    expect(isEcompGeneratedNaming).toBeTruthy();
  });

  test('isEcompGeneratedNaming should return false if isEcompGeneratedNaming is "false"', () => {
    let isEcompGeneratedNaming: boolean = vnfModel.isEcompGeneratedNaming({
      properties: {
        ecomp_generated_naming: 'false'
      }
    });
    expect(isEcompGeneratedNaming).toBeFalsy();
  });

  test('isEcompGeneratedNaming should return false if isEcompGeneratedNaming is not defined', () => {
    let isEcompGeneratedNaming: boolean = vnfModel.isEcompGeneratedNaming({
      properties: {
      }
    });
    expect(isEcompGeneratedNaming).toBeFalsy();
  });


  test('getTooltip should return "VF"', () => {
    let tooltip: string = vnfModel.getTooltip();
    expect(tooltip).toEqual('VF');
  });

  test('getType should return "VF"', () => {
    let tooltip: string = vnfModel.getType();
    expect(tooltip).toEqual('VF');
  });

  test('getNextLevelObject should vfModule level object', () => {
    let nextLevel: VFModuleModelInfo = vnfModel.getNextLevelObject();
    expect(nextLevel.type).toEqual('Module');
  });

  test('getModel should return VNF model', () => {
    expect(vnfModel.getModel({})).toBeInstanceOf(VNFModel);
  });

  test('showNodeIcons should return false if reachLimit of max', ()=>{
    let serviceId : string = 'servicedId';
    let node = {
      data : {
        id : 'vnfId',
        name : 'vnfName',
        modelCustomizationId : "modelCustomizationId",
        modelUniqueId: "modelCustomizationId"
      }
    };
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global:{},
      service : {
        serviceHierarchy : {
          'servicedId' : {
            'vnfs' : {
              modelCustomizationId : "modelCustomizationId",
              'modelInfo': {
                modelCustomizationId : "modelCustomizationId"
              },
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
              'modelCustomizationId' : 1
            },
            'vnfs' : {
              'vnfName' :{

              }
            }
          }
        }
      }
    });

    let result = vnfModel.showNodeIcons(<any>node, serviceId);
    expect(result).toEqual(new AvailableNodeIcons(false , true));
  });

  test('showNodeIcons should return true if not reachLimit of max', ()=>{
    let serviceId : string = 'servicedId';
    let node = {
      data : {
        id : 'vnfId',
        name : 'vnfName'
      }
    };
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global:{
        drawingBoardStatus: "EDIT"
      },
      service : {
        serviceHierarchy : {
          'servicedId' : {
            'vnfs' : {
              'vnfName' : {
                'properties' : {
                  'max_instances' : 2
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

    let result = vnfModel.showNodeIcons(<any>node, serviceId);
    expect(result).toEqual(new AvailableNodeIcons(true , false));
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
            }
          }
        },
        serviceInstance : {
          'servicedId' : {
            'existingVNFCounterMap' : {
              'modelCustomizationId' : 1
            },
            'vnfs' : {
              'vnfName' :{
                'originalName' : 'vnfName'
              }
            }
          }
        }
      }
    });

    let node = {
      data : {
        id : 'vnfId',
        name : 'vnfName',
        modelCustomizationId : "modelCustomizationId",
        modelUniqueId:  "modelCustomizationId"
      }
    };
    let result = vnfModel.getNodeCount(<any>node , serviceId);
    expect(result).toEqual(1);

    node.data.modelCustomizationId = 'vnfId_notExist';
    node.data.modelUniqueId = 'vnfId_notExist';
    result = vnfModel.getNodeCount(<any>node , serviceId);
    expect(result).toEqual(0);

    result = vnfModel.getNodeCount(<any>node , serviceId + '_notExist');
    expect(result).toEqual(0);
  });

  test('getMenuAction: edit', ()=>{
    let node = {"modelId":"d6557200-ecf2-4641-8094-5393ae3aae60","missingData":true,"action":"None","inMaint":true,"name":"jlfBwIks283yKlCD8","modelName":"VF_vGeraldine 0","type":"VF","isEcompGeneratedNaming":true,"networkStoreKey":"VF_vGeraldine 0:004","vnfStoreKey":"VF_vGeraldine 0:004","typeName":"VNF"};
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";
    let result = vnfModel.getMenuAction(<any>node, serviceModelId);
    spyOn(result['edit'], 'method');
    expect(result['edit']).toBeDefined();
    expect(result['edit'].visible).toBeTruthy();
    expect(result['edit'].enable).toBeTruthy();
    result['edit']['method'](node, serviceModelId);
    expect(result['edit']['method']).toHaveBeenCalledWith(node, serviceModelId);
  });

  test('member should defined edit method', () => {
    let node = {
      data : {
        "modelId": "d6557200-ecf2-4641-8094-5393ae3aae60",
        "missingData": true,
        "action": "None",
        "inMaint": true,
        "name": "jlfBwIks283yKlCD8",
        "modelName": "VF_vGeraldine 0",
        "type": "VF",
        "userProvidedNaming": false,
        "networkStoreKey": "VF_vGeraldine 0:004",
        "vnfStoreKey": "VF_vGeraldine 0:004",
        "typeName": "VNF"
      },
      type : 'VNF'

    };
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";

    let menuActions = vnfModel.getMenuAction(<any>node, serviceModelId);
    spyOn(menuActions['edit'], 'method');
    expect(menuActions['edit']).toBeDefined();
    expect(menuActions['edit'].visible(node)).toBeFalsy();
    menuActions['edit']['method'](node, serviceModelId);
    expect(menuActions['edit']['method']).toHaveBeenCalledWith(node, serviceModelId);
  });

  test('getMenuAction: duplicate', ()=>{
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      "global": {
      "name": null,
        "type": "UPDATE_DRAWING_BOARD_STATUS",
        "drawingBoardStatus": "CREATE"
      }
    });
    let node = {
      data : {
        "modelId": "d6557200-ecf2-4641-8094-5393ae3aae60",
        "missingData": true,
        "action": "None",
        "inMaint": true,
        "name": "jlfBwIks283yKlCD8",
        "modelName": "VF_vGeraldine 0",
        "type": "VF",
        "isEcompGeneratedNaming":true,
        "networkStoreKey": "VF_vGeraldine 0:004",
        "vnfStoreKey": "VF_vGeraldine 0:004",
        "typeName": "VNF"
      },
      type : 'VNF'

    };
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";
    let menuActions = vnfModel.getMenuAction(<any>node, serviceModelId);
    spyOn(menuActions['duplicate'], 'method');
    expect(menuActions['duplicate']).toBeDefined();
    expect(menuActions['duplicate'].visible(node)).toBeFalsy();
    menuActions['duplicate']['method'](node, serviceModelId);
    expect(menuActions['duplicate']['method']).toHaveBeenCalledWith(node, serviceModelId);
  });

  test('getMenuAction: remove', ()=>{
    let node = {
      data : {
        "modelId": "d6557200-ecf2-4641-8094-5393ae3aae60",
        "missingData": true,
        "action": "None",
        "inMaint": true,
        "name": "jlfBwIks283yKlCD8",
        "modelName": "VF_vGeraldine 0",
        "type": "VF",
        "isEcompGeneratedNaming":true,
        "networkStoreKey": "VF_vGeraldine 0:004",
        "vnfStoreKey": "VF_vGeraldine 0:004",
        "typeName": "VNF"
      },
      type : 'VNF'

    };
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";

    let menuActions = vnfModel.getMenuAction(<any>node, serviceModelId);
    spyOn(menuActions['remove'], 'method');
    expect(menuActions['remove']).toBeDefined();
    expect(menuActions['remove'].visible(node)).toBeFalsy();
    menuActions['remove']['method'](node, serviceModelId);
    expect(menuActions['remove']['method']).toHaveBeenCalledWith(node, serviceModelId);
  });

  test('getMenuAction: delete', ()=>{
    let node = {"modelId":"d6557200-ecf2-4641-8094-5393ae3aae60","missingData":true,"action":"None","inMaint":true,"name":"jlfBwIks283yKlCD8","modelName":"VF_vGeraldine 0","type":"VF","isEcompGeneratedNaming":true,"networkStoreKey":"VF_vGeraldine 0:004","vnfStoreKey":"VF_vGeraldine 0:004","typeName":"VNF"};
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";
    let result = vnfModel.getMenuAction(<any>node, serviceModelId);
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
    let result = vnfModel.getMenuAction(<any>node, serviceModelId);
    spyOn(result['undoDelete'], 'method');
    expect(result['undoDelete']).toBeDefined();
    expect(result['undoDelete'].visible).toBeDefined();
    expect(result['undoDelete'].enable).toBeDefined();
    result['undoDelete']['method'](node, serviceModelId);
    expect(result['undoDelete']['method']).toHaveBeenCalledWith(node, serviceModelId);
  });

  test('getMenuAction: showAuditInfo', ()=>{
    jest.spyOn(_sharedTreeService, 'isRetryMode').mockReturnValue(true);

    jest.spyOn(AuditInfoModalComponent.openInstanceAuditInfoModal, 'next');
    let node = {
      data : {
        "modelId": "6b528779-44a3-4472-bdff-9cd15ec93450",
        "action": "Create",
        "type": "VF",
        "vnfStoreKey": "MSOTEST103a",
        "typeName": "VNF",
        "isFailed": true,
      },
      type : 'VNF'

    };
    let serviceModelId = "6b528779-44a3-4472-bdff-9cd15ec93450";
    let result = vnfModel.getMenuAction(<any>node, serviceModelId);
    spyOn(result['showAuditInfo'], 'method');
    expect(result['showAuditInfo']).toBeDefined();
    expect(result['showAuditInfo'].visible(node)).toBeTruthy();
    expect(result['showAuditInfo'].enable(node)).toBeTruthy();
    result['showAuditInfo']['method'](node, serviceModelId);

  });

  test('Info for vnf should be correct', () => {
    const model = getVNFModel();
    const instance = getVNFInstance();
    let actualVNFInfo = vnfModel.getInfo(model,instance);
    let expectedVNFInfo = [
      ModelInformationItem.createInstance('Min instances', "0"),
      ModelInformationItem.createInstance('Max instances',"1"),
      ModelInformationItem.createInstance('NF type',undefined),
      ModelInformationItem.createInstance('NF role',undefined)
    ];
    expect(actualVNFInfo).toEqual(expectedVNFInfo);
  });

  test('Info for vnf in left side - model only - should be correct', () => {
    const model = getVNFModel();
    let actualVNFInfo = vnfModel.getInfo(model,null);
    let expectedVNFInfo = [
      ModelInformationItem.createInstance('Min instances', "0"),
      ModelInformationItem.createInstance('Max instances',"1")
    ];
    expect(actualVNFInfo).toEqual(expectedVNFInfo);
  });

  test('When there is no max Max instances text is: Unlimited (default)', () => {
    let actualVNFInfo = vnfModel.getInfo({just:"not empty"},null);
    const maxInstancesItem = actualVNFInfo.find((item)=> item.label == 'Max instances');
    expect(maxInstancesItem.values[0]).toEqual('Unlimited (default)');
  });

  function getVNFModel(){
    return {
      "name":"VF_vGeraldine",
      "version":"2.0",
      "description":"VSP_vGeraldine",
      "uuid":"d6557200-ecf2-4641-8094-5393ae3aae60",
      "invariantUuid":"4160458e-f648-4b30-a176-43881ffffe9e",
      "max":1,
      "min":0,
      "customizationUuid":"91415b44-753d-494c-926a-456a9172bbb9",
      "isEcompGeneratedNaming":true,
      "type":"VF",
      "modelCustomizationName":"VF_vGeraldine 0",
      "vfcInstanceGroups":{},
      "properties":{
        "gpb2_Internal2_mac":"00:11:22:EF:AC:DF",
        "sctp-a-ipv6-egress_rule_application":"any",
        "sctp-b-ipv6-egress_src_start_port":"0",
        "Internal2_allow_transit":"true",
        "sctp-b-IPv6_ethertype":"IPv6",
        "ncb2_Internal1_mac":"00:11:22:EF:AC:DF",
        "sctp-b-ingress_rule_protocol":"icmp","sctp-b-ingress_action":"pass",
        "sctp-a-egress_rule_application":"any",
        "sctp-b-ipv6-ingress-src_start_port":"0.0",
        "ncb1_Internal2_mac":"00:11:22:EF:AC:DF","sctp-b-egress_src_addresses":"local",
        "fsb_volume_size_0":"320.0",
        "sctp-a-ipv6-ingress-dst_start_port":"0",
        "sctp-a-ipv6-ingress_ethertype":"IPv4","sctp-b-ipv6-ingress_rule_application":"any",
        "domain_name":"default-domain",
        "sctp-a-egress_src_addresses":"local",
        "sctp-b-egress-src_start_port":"0.0",
        "sctp-a-ingress_rule_protocol":"icmp",
        "sctp-b-display_name":"epc-sctp-b-ipv4v6-sec-group",
        "sctp-b-ipv6-ingress-dst_end_port":"65535",
        "sctp-a-ingress_ethertype":"IPv4",
        "sctp-a-egress-src_start_port":"0.0",
        "sctp-b-dst_subnet_prefix_v6":"::",
        "nf_naming":"{ecomp_generated_naming=true}",
        "sctp-a-ipv6-ingress_src_subnet_prefix":"0.0.0.0"
        }
    };

  }

  function getVNFInstance(){
    return {
      "action":"None",
      "instanceName":"4O61SmpFAdCm1oVEs",
      "instanceId":"66cbb3b5-c823-470c-9520-4e0b85112250",
      "orchStatus":null,
      "productFamilyId":null,
      "lcpCloudRegionId":null,
      "tenantId":null,
      "modelInfo":{
        "modelCustomizationName":"VF_vGeraldine 0",
        "modelInvariantId":"vnf-instance-model-invariant-id",
        "modelVersionId":"d6557200-ecf2-4641-8094-5393ae3aae60",
        "modelType":"vnf"
      },
      "instanceType":"7538ifdSoTccmbEkr",
      "provStatus":null,
      "inMaint":true,
      "uuid":"d6557200-ecf2-4641-8094-5393ae3aae60",
      "originalName":"VF_vGeraldine 0",
      "legacyRegion":null,
      "lineOfBusiness":null,
      "platformName":null,
      "trackById":"VF_vGeraldine 0:004",
      "vfModules":{
        "vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1":{
          "vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1:008":{
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
          }
        },
        "dc229cd8-c132-4455-8517-5c1787c18b14":{
          "dc229cd8-c132-4455-8517-5c1787c18b14:009":{
            "action":"None",
            "instanceName":"ss820f_0918_base",
            "instanceId":"3ef042c4-259f-45e0-9aba-0989bd8d1cc5",
            "orchStatus":"Assigned",
            "productFamilyId":null,
            "lcpCloudRegionId":null,
            "tenantId":null,
            "modelInfo":{
              "modelCustomizationId":"8ad8670b-0541-4499-8101-275bbd0e8b6a",
              "modelInvariantId":"1e463c9c-404d-4056-ba56-28fd102608de",
              "modelVersionId":"dc229cd8-c132-4455-8517-5c1787c18b14",
              "modelType":"vfModule"
            },
            "instanceType":null,
            "provStatus":null,
            "inMaint":false,
            "uuid":"dc229cd8-c132-4455-8517-5c1787c18b14",
            "originalName":null,
            "legacyRegion":null,
            "lineOfBusiness":null,
            "platformName":null,
            "trackById":"dc229cd8-c132-4455-8517-5c1787c18b14:009",
            "isBase":true,
            "volumeGroupName":null
          }
        }
      },
      "networks":{

      }
    };
  }

});
