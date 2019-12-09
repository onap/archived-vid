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
import {VfModulePopuopService} from "../../../../../shared/components/genericFormPopup/genericFormServices/vfModule/vfModule.popuop.service";
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
 let _vfModulePopupService : VfModulePopuopService;
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
        VfModulePopuopService,
        VfModuleUpgradePopupService,
        VnfPopupService,
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
    let model: VNFModel = vnfModel.getModel('2017-388_PASQUALE-vPE 1', <any>{
      originalName : '2017-388_PASQUALE-vPE 1'
    }, getServiceHierarchy());
    expect(model.type).toEqual('VF');
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
