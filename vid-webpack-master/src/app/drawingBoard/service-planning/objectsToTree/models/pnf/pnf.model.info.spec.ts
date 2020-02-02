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
import {VNFModel} from "../../../../../shared/models/vnfModel";
import {PNFModel} from "../../../../../shared/models/pnfModel";


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
    expect(pnfModel.getModel({})).toBeInstanceOf(PNFModel);
  });

})
