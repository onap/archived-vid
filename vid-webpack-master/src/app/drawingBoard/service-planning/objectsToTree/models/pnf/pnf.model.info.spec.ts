import {ComponentInfoService} from "../../../component-info/component-info.service";
import {AppState} from "../../../../../shared/store/reducers";
import {NgRedux} from "@angular-redux/store";
import {PnfModelInfo} from "./pnf.model.info";
import {getTestBed, TestBed} from "@angular/core/testing";
import {DynamicInputsService} from "../../dynamicInputs.service";
import {SharedTreeService} from "../../shared.tree.service";
import {DuplicateService} from "../../../duplicate/duplicate.service";
import {AaiService} from "../../../../../shared/services/aaiService/aai.service";
import {FeatureFlagsService} from "../../../../../shared/services/featureFlag/feature-flags.service";
import {IframeService} from "../../../../../shared/utils/iframe.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {MockNgRedux, NgReduxTestingModule} from "@angular-redux/store/testing";
import {HttpClient, HttpHandler} from "@angular/common/http";
import {CollectionResourceModel} from "../../../../../shared/models/collectionResourceModel";
import {ComponentInfoType} from "../../../component-info/component-info-model";


describe('PNF model info', () => {
  let injector;
  let _componentInfoService: ComponentInfoService;
  let _store: NgRedux<AppState>;
  let _sharedTreeService;
  let pnfModel: PnfModelInfo;
  beforeEach(done => (async () => {
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
        IframeService]
    });
    await TestBed.compileComponents();
    injector = getTestBed();
    _store = injector.get(NgRedux);
    _sharedTreeService = injector.get(SharedTreeService);
    pnfModel = new PnfModelInfo(_sharedTreeService);
    _componentInfoService = injector.get(ComponentInfoService);
  })().then(done).catch(done.fail));

  test('pnf model should be defined', () => {
    expect(pnfModel).toBeDefined();
  });

  test('pnf model should defined extra details', () => {
    expect(pnfModel.name).toEqual('pnfs');
    expect(pnfModel.type).toEqual('PNF');
    expect(pnfModel.typeName).toEqual('PNF');
    expect(pnfModel.componentInfoType).toEqual(ComponentInfoType.PNF);
  });

  test('getTooltip should return "PNF"', () => {
    let tooltip: string = pnfModel.getTooltip();
    expect(tooltip).toEqual('PNF');
  });

  test('getType should return "pnf"', () => {
    let tooltip: string = pnfModel.getType();
    expect(tooltip).toEqual('pnf');
  });

  test('getNextLevelObject should be null', () => {
    let nextLevel: any = pnfModel.getNextLevelObject();
    expect(nextLevel).toBeNull();
  });

  test('isEcompGeneratedNaming should return false', () => {
    let isEcompGeneratedNaming: boolean = pnfModel.isEcompGeneratedNaming(<any>{});
    expect(isEcompGeneratedNaming).toBeFalsy();
  });

  test('getModel should return pnf model', () => {
    let model: CollectionResourceModel = pnfModel.getModel('PNF1', <any>{
      originalName : 'PNF1'
    }, getServiceHierarchy());
    expect(model.type).toEqual('PNF');
  });

  function getServiceHierarchy(){
    return {
      "service": {
        "uuid": "12550cd7-7708-4f53-a09e-41d3d6327ebc",
        "invariantUuid": "561faa57-7bbb-40ec-a81c-c0d4133e98d4",
        "name": "AIM Transport SVC_ym161f",
        "version": "1.0",
        "toscaModelURL": null,
        "category": "Network L1-3",
        "serviceType": "TRANSPORT",
        "serviceRole": "AIM",
        "description": "AIM Transport service",
        "serviceEcompNaming": "true",
        "instantiationType": "Macro",
        "inputs": {},
        "vidNotions": {
          "instantiationUI": "legacy",
          "modelCategory": "other",
          "viewEditUI": "legacy"
        }
      },
      "vnfs": {},
      "networks": {},
      "collectionResources": {},
      "configurations": {},
      "fabricConfigurations": {},
      "serviceProxies": {},
      "vfModules": {},
      "volumeGroups": {},
      "pnfs": {
        "PNF1": {
          "uuid": "1c831fa9-28a6-4778-8c1d-80b9e769f2ed",
          "invariantUuid": "74e13a12-dac9-4fba-b102-cd242d9e7f02",
          "description": "AIM Transport service",
          "name": "AIM PNF Model",
          "version": "1.0",
          "customizationUuid": "dce78da7-c842-47a1-aba2-2de1cd03ab7a",
          "inputs": {},
          "commands": {},
          "properties": {
            "nf_function": "SDNGW",
            "nf_role": "pD2IPE",
            "ecomp_generated_naming": "false",
            "nf_type": "ROUTER"
          },
          "type": "PNF"
        }
      },
      "vnfGroups": {}
    }
  }
})
