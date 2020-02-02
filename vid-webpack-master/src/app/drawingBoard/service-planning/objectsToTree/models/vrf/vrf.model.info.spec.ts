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

describe('Vrf Model Info', () => {

  let injector;
  let _componentInfoService : ComponentInfoService;

  let _store : NgRedux<AppState>;
  let _sharedTreeService: SharedTreeService;
  let _dialogService : DialogService;
  let _iframeService : IframeService;
  let _networkStepService : NetworkStepService;
  let _vpnStepService : VpnStepService;
  let _featureFlagsService : FeatureFlagsService;
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
    _featureFlagsService = injector.get(FeatureFlagsService);
    _vpnStepService = injector.get(VpnStepService);
    _store = injector.get(NgRedux);
    _componentInfoService = injector.get(ComponentInfoService);

    vrfModel = new VrfModelInfo(_store,_sharedTreeService, _dialogService, _iframeService, _featureFlagsService, _networkStepService, _vpnStepService);

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
    let model: VrfModel = vrfModel.getModel({
        "uuid": "9cac02be-2489-4374-888d-2863b4511a59",
        "invariantUuid": "b67a289b-1688-496d-86e8-1583c828be0a",
        "properties": {
          "ecomp_generated_naming": "false",
          "type": "VRF-ENTRY",
        },
        "type": "Configuration"
      }
    );
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


})
