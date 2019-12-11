import {ComponentInfoService} from "../../../component-info/component-info.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../../shared/store/reducers";
import {getTestBed, TestBed} from "@angular/core/testing";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {MockNgRedux, NgReduxTestingModule} from "@angular-redux/store/testing";
import {DynamicInputsService} from "../../dynamicInputs.service";
import {SharedTreeService} from "../../shared.tree.service";
import {DuplicateService} from "../../../duplicate/duplicate.service";
import {AaiService} from "../../../../../shared/services/aaiService/aai.service";
import {HttpClient, HttpHandler} from "@angular/common/http";
import {FeatureFlagsService} from "../../../../../shared/services/featureFlag/feature-flags.service";
import {IframeService} from "../../../../../shared/utils/iframe.service";
import {VrfModelInfo} from "./vrf.model.info";
import {ComponentInfoType} from "../../../component-info/component-info-model";
import {ModelInformationItem} from "../../../../../shared/components/model-information/model-information.component";
import {VrfModel} from "../../../../../shared/models/vrfModel";
import {AvailableNodeIcons} from "../../../available-models-tree/available-models-tree.service";
import each from 'jest-each';
import {DialogService} from "ng2-bootstrap-modal";
import {NetworkStepService} from "./vrfModal/networkStep/network.step.service";
import {VpnStepService} from "./vrfModal/vpnStep/vpn.step.service";
import {Utils} from "../../../../../shared/utils/utils";

describe('Vrf Model Info', () => {

  let injector;
  let _componentInfoService : ComponentInfoService;

  let _store : NgRedux<AppState>;
  let _sharedTreeService: SharedTreeService;
  let _dialogService : DialogService;
  let _iframeService : IframeService;
  let _networkStepService : NetworkStepService;
  let _vpnStepService : VpnStepService;
  let _utils : Utils;
  let vrfModel: VrfModelInfo;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NgReduxTestingModule],
      providers: [
        MockNgRedux,
        DynamicInputsService,
        SharedTreeService,
        DuplicateService,
        AaiService,
        HttpClient,
        HttpHandler,
        FeatureFlagsService,
        ComponentInfoService,
        DialogService,
        Utils,
        IframeService,
        IframeService,
        NetworkStepService,
       VpnStepService]
    }).compileComponents();

    injector = getTestBed();
    _sharedTreeService = injector.get(SharedTreeService);
    _dialogService = injector.get(DialogService);
    _iframeService = injector.get(IframeService);
    _networkStepService = injector.get(NetworkStepService);
    _vpnStepService = injector.get(VpnStepService);
    _utils = injector.get(Utils);
    _store = injector.get(NgRedux);
    _componentInfoService = injector.get(ComponentInfoService);

    vrfModel = new VrfModelInfo(_store, _sharedTreeService, _dialogService, _iframeService, _networkStepService, _utils, _vpnStepService);

  });


  test('vrfModel should be defined', () => {
    expect(vrfModel).toBeDefined();
  });

  test('vrfModel should defined extra details', () => {
    expect(vrfModel.name).toEqual('vrfs');
    expect(vrfModel.type).toEqual('VRF');
    expect(vrfModel.childNames).toEqual(['networks','vpns']);
    expect(vrfModel.componentInfoType).toEqual(ComponentInfoType.VRF);
  });

  test('Info for vrf should be correct', () => {
    const model = new VrfModel();
    const instance = null;
    let actualNetworkInfo = vrfModel.getInfo(model,instance);
    let expectedNetworkInfo = [
      ModelInformationItem.createInstance('Min instances', "1"),
      ModelInformationItem.createInstance('Max instances', "1"),
      ModelInformationItem.createInstance("Association", "L3-Network - VPN")
    ];
    expect(actualNetworkInfo).toEqual(expectedNetworkInfo);
  });



  test('getModel should return VRF model with min and max are equal to 1 (hard coded)', () => {
    let model: VrfModel = vrfModel.getModel('VRF Entry Configuration 0', <any>{
      originalName : 'VRF Entry Configuration 0'
    }, getServiceHierarchy());
    expect(model.properties['type']).toEqual('VRF-ENTRY');
    expect(model.min).toEqual(1);
    expect(model.max).toEqual(1);
  });

  const showNodeIconsDataProvider = [
    [false, new AvailableNodeIcons(true , false)],
    [true, new AvailableNodeIcons(false , true)]
  ];

  each(showNodeIconsDataProvider).test('showNodeIcons should return value according reach limit of max',(existVRFs,expectedResult)=>{
    const serviceId : string = 'servicedId';
    const node = getNode();
    let state = getMockState();
    if(existVRFs) {
      state.service.serviceInstance.servicedId.existingVRFCounterMap = {
        'modelCustomizationId': 1
      };
    }
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue(state);
    const result = vrfModel.showNodeIcons(<any>node, serviceId);
    expect(result).toEqual(expectedResult);
  });

  function getNode() {
    return {
      data: {
        id: 'vrfId',
        name: 'VRF Entry Configuration 0',
        modelCustomizationId: 'modelCustomizationId',
        modelUniqueId: 'modelCustomizationId',
        getModel: function () {
          return {max: 1}
        }
      }
    };
  }

  function getMockState(){
    return {
      global:{
        "drawingBoardStatus": "CREATE"
      },
      service : {
        serviceHierarchy : {
          'servicedId' : {
          }
        },
        serviceInstance : {
          'servicedId' : {
            'existingVRFCounterMap' : {},
            'vrfs' : {
              'vrfName' :{

              }
            }
          }
        }
      }
    };
  }

  function getServiceHierarchy() {
    return {
      "service": {
        "uuid": "f028b2e2-7080-4b13-91b2-94944d4c42d8",
        "invariantUuid": "dfc2c44c-2429-44ca-ae26-1e6dc1f207fb",
        "name": "infraVPN",
        "version": "1.0",
        "toscaModelURL": null,
        "category": "Network Service",
        "serviceType": "BONDING",
        "serviceRole": "INFRASTRUCTURE-VPN",
        "description": "ddd",
        "serviceEcompNaming": "true",
        "instantiationType": "A-La-Carte",
        "inputs": {},
        "vidNotions": {
          "instantiationUI": "macroService",
          "modelCategory": "other",
          "viewEditUI": "legacy"
        }
      },
      "vnfs": {},
      "networks": {},
      "collectionResources": {},
      "configurations": {},
      "fabricConfigurations": {},
      "serviceProxies": {
        "misvpn_service_proxy 0": {
          "uuid": "35186eb0-e6b6-4fa5-86bb-1501b342a7b1",
          "invariantUuid": "73f89e21-b96c-473f-8884-8b93bcbd2f76",
          "description": "A Proxy for Service MISVPN_SERVICE",
          "name": "MISVPN_SERVICE Service Proxy",
          "version": "3.0",
          "customizationUuid": "4c2fb7e0-a0a5-4b32-b6ed-6a974e55d923",
          "inputs": {},
          "commands": {},
          "properties": {
            "ecomp_generated_naming": "false"
          },
          "type": "Service Proxy",
          "sourceModelUuid": "d5cc7d15-c842-450e-95ae-2a69e66dd23b",
          "sourceModelInvariant": "c126ec86-59fe-48c0-9532-e39a9b3e5272",
          "sourceModelName": "MISVPN_SERVICE"
        }
      },
      "vfModules": {},
      "volumeGroups": {},
      "pnfs": {},
      "vnfGroups": {},
      "vrfs": {
        "VRF Entry Configuration 0": {
          "uuid": "9cac02be-2489-4374-888d-2863b4511a59",
          "invariantUuid": "b67a289b-1688-496d-86e8-1583c828be0a",
          "description": "VRF Entry configuration object",
          "name": "VRF Entry Configuration",
          "version": "30.0",
          "customizationUuid": "dd024d73-9bd1-425d-9db5-476338d53433",
          "inputs": {},
          "commands": {},
          "properties": {
            "ecomp_generated_naming": "false",
            "type": "VRF-ENTRY",
            "role": "INFRASTRUCTURE-CLOUD-VPN"
          },
          "type": "Configuration",
          "modelCustomizationName": "VRF Entry Configuration 0",
          "sourceNodes": [],
          "collectorNodes": null,
          "configurationByPolicy": false
        }
      }
    }
  }



})
