import {HttpClientTestingModule} from "@angular/common/http/testing";
import {getTestBed, TestBed} from "@angular/core/testing";
import {MockNgRedux, NgReduxTestingModule} from "@angular-redux/store/testing";
import {DynamicInputsService} from "../../dynamicInputs.service";
import {SharedTreeService} from "../../shared.tree.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../../shared/store/reducers";
import {DuplicateService} from "../../../duplicate/duplicate.service";
import {IframeService} from "../../../../../shared/utils/iframe.service";
import {ComponentInfoService} from "../../../component-info/component-info.service";
import {AaiService} from "../../../../../shared/services/aaiService/aai.service";
import {HttpClient, HttpHandler} from "@angular/common/http";
import {FeatureFlagsService} from "../../../../../shared/services/featureFlag/feature-flags.service";
import {CollectionResourceModelInfo} from "./collectionResource.model.info";
import {ComponentInfoType} from "../../../component-info/component-info-model";
import {CollectionResourceModel} from "../../../../../shared/models/collectionResourceModel";
import {NcfModelInfo} from "../ncf/ncf.model.info";

describe('Collection Resource Model Info', () => {
  let injector;
  let _componentInfoService : ComponentInfoService;

  let _store : NgRedux<AppState>;
  let collectionResourceModel: CollectionResourceModelInfo;
  let  _sharedTreeService : SharedTreeService;

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
    _componentInfoService = injector.get(ComponentInfoService);
    _sharedTreeService = injector.get(SharedTreeService);

    collectionResourceModel = new CollectionResourceModelInfo(_store, _sharedTreeService);


  })().then(done).catch(done.fail));

  test('collection resource should be defined', () => {
    expect(collectionResourceModel).toBeDefined();
  });

  test('collectionResourceModel should defined extra details', () => {
    expect(collectionResourceModel.name).toEqual('collectionResources');
    expect(collectionResourceModel.type).toEqual('collection Resource');
    expect(collectionResourceModel.childNames).toEqual(['ncfs']);
    expect(collectionResourceModel.componentInfoType).toEqual(ComponentInfoType.COLLECTION_RESOURCE);
  });

  test('isEcompGeneratedNaming should return false', () => {
    let isEcompGeneratedNaming: boolean = collectionResourceModel.isEcompGeneratedNaming(<any>{});
    expect(isEcompGeneratedNaming).toBeFalsy();
  });


  test('getTooltip should return "Collection Resource"', () => {
    let tooltip: string = collectionResourceModel.getTooltip();
    expect(tooltip).toEqual('Collection Resource');
  });

  test('getType should return "collectionResources"', () => {
    let tooltip: string = collectionResourceModel.getType();
    expect(tooltip).toEqual('collectionResource');
  });

  test('getNextLevelObject should return ncfs', () => {
    let nextLevel: NcfModelInfo = collectionResourceModel.getNextLevelObject();
    expect(nextLevel.type).toEqual('NCF');
  });

  test('getModel should return collectionResource model', () => {
    expect(collectionResourceModel.getModel({})).toBeInstanceOf(CollectionResourceModel);
  });

  test('cr getMenuAction: delete', ()=>{
    let node = {};
    let serviceModelId = 'serviceModelId';
    let result = collectionResourceModel.getMenuAction(<any>node, serviceModelId);
    spyOn(result['delete'], 'method');
    expect(result['delete']).toBeDefined();
    expect(result['delete'].visible()).toBeFalsy();
    expect(result['delete'].enable()).toBeFalsy();
    result['delete']['method'](node, serviceModelId);
    expect(result['delete']['method']).toHaveBeenCalledWith(node, serviceModelId);
  });

});
