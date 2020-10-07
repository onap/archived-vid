import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {getTestBed, TestBed} from "@angular/core/testing";
import {MockNgRedux, NgReduxTestingModule} from "@angular-redux/store/testing";
import {DynamicInputsService} from "../../dynamicInputs.service";
import {VNFModel} from "../../../../../shared/models/vnfModel";
import {SharedTreeService} from "../../shared.tree.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../../shared/store/reducers";
import {DefaultDataGeneratorService} from "../../../../../shared/services/defaultDataServiceGenerator/default.data.generator.service";
import {DialogService} from "ng2-bootstrap-modal";
import {VfModulePopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/vfModule/vfModule.popup.service";
import {VnfPopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/vnf/vnf.popup.service";
import {DuplicateService} from "../../../duplicate/duplicate.service";
import {IframeService} from "../../../../../shared/utils/iframe.service";
import {RelatedVnfMemberInfoModel} from "./relatedVnfMember.info.model";
import {VfModuleUpgradePopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/vfModuleUpgrade/vfModule.upgrade.popuop.service";
import {PnfPopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/pnf/pnf.popup.service";


class MockAppStore<T> {
  getState() {
    return {}
  }
}


describe('Related Vnf member Model Info', () => {
  let injector;
  let httpMock: HttpTestingController;
  let  _dynamicInputsService : DynamicInputsService;
  let  _sharedTreeService : SharedTreeService;

  let _store : NgRedux<AppState>;
  let relatedVnfMemeber: RelatedVnfMemberInfoModel;
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
        IframeService]
    }).compileComponents();

    injector = getTestBed();
    _sharedTreeService = injector.get(SharedTreeService);
    _store = injector.get(NgRedux);

    relatedVnfMemeber = new RelatedVnfMemberInfoModel(
      _sharedTreeService,
      _dynamicInputsService,
      _store);
  });

  test('relatedVnfMemeber should be defined', () => {
    expect(relatedVnfMemeber).toBeDefined();
  });

  test('RelatedVnfMemeber should defined extra details', () => {
    expect(relatedVnfMemeber.name).toEqual('vnfs');
    expect(relatedVnfMemeber.type).toEqual('relatedVnfMember');
  });

  test('isEcompGeneratedNaming should return true if isEcompGeneratedNaming is "true" ', () => {
    let isEcompGeneratedNaming: boolean = relatedVnfMemeber.isEcompGeneratedNaming(<any>{
      properties: {
        ecomp_generated_naming: 'true'
      }
    });
    expect(isEcompGeneratedNaming).toBeTruthy();
  });

  test('isEcompGeneratedNaming should return false if isEcompGeneratedNaming is "false"', () => {
    let isEcompGeneratedNaming: boolean = relatedVnfMemeber.isEcompGeneratedNaming({
      properties: {
        ecomp_generated_naming: 'false'
      }
    });
    expect(isEcompGeneratedNaming).toBeFalsy();
  });

  test('isEcompGeneratedNaming should return false if isEcompGeneratedNaming is not defined', () => {
    let isEcompGeneratedNaming: boolean = relatedVnfMemeber.isEcompGeneratedNaming({
      properties: {
      }
    });
    expect(isEcompGeneratedNaming).toBeFalsy();
  });


  test('getTooltip should return "VF"', () => {
    let tooltip: string = relatedVnfMemeber.getTooltip();
    expect(tooltip).toEqual('VF');
  });

  test('getType should return "VF"', () => {
    let tooltip: string = relatedVnfMemeber.getType();
    expect(tooltip).toEqual('VF');
  });

  test('getNextLevelObject should be null', () => {
    let nextLevel = relatedVnfMemeber.getNextLevelObject();
    expect(nextLevel).toBeNull();
  });

  test('getModel should return VNF model', () => {
    expect(relatedVnfMemeber.getModel({})).toBeInstanceOf(VNFModel);
  });


  test('getMenuAction: delete', ()=>{
    let node = {"modelId":"d6557200-ecf2-4641-8094-5393ae3aae60","missingData":true,"action":"None","inMaint":true,"name":"jlfBwIks283yKlCD8","modelName":"VF_vGeraldine 0","type":"VF","isEcompGeneratedNaming":true,"networkStoreKey":"VF_vGeraldine 0:004","vnfStoreKey":"VF_vGeraldine 0:004","typeName":"VNF"};
    let serviceModelId = "d6557200-ecf2-4641-8094-5393ae3aae60";
    let result = relatedVnfMemeber.getMenuAction(<any>node, serviceModelId);
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
    let result = relatedVnfMemeber.getMenuAction(<any>node, serviceModelId);
    spyOn(result['undoDelete'], 'method');
    expect(result['undoDelete']).toBeDefined();
    expect(result['undoDelete'].visible).toBeDefined();
    expect(result['undoDelete'].enable).toBeDefined();
    result['undoDelete']['method'](node, serviceModelId);
    expect(result['undoDelete']['method']).toHaveBeenCalledWith(node, serviceModelId);
  });

});
