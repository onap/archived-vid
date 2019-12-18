import {getTestBed, TestBed} from '@angular/core/testing';
import {AaiService} from "../../../services/aaiService/aai.service";
import {FeatureFlagsService} from "../../../services/featureFlag/feature-flags.service";
import {ControlGeneratorUtil} from "./control.generator.util.service";
import {NgRedux} from '@angular-redux/store';
import each from "jest-each";
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {FileFormControl} from "../../../models/formControlModels/fileFormControl.model";
import {AppState} from "../../../store/reducers";
import {SelectOption} from "../../../models/selectOption";
import {SharedControllersService} from "./sharedControlles/shared.controllers.service";

describe('Control Generator Util', () => {
  let injector;
  let service: ControlGeneratorUtil;
  let sharedControllersService : SharedControllersService;
  let httpMock: HttpTestingController;
  let store: NgRedux<AppState>;


  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ControlGeneratorUtil,
        SharedControllersService,
        AaiService,
        {provide:FeatureFlagsService, useClass: MockFeatureFlagsService},
        {provide: NgRedux, useClass: MockAppStore}]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(ControlGeneratorUtil);
    httpMock = injector.get(HttpTestingController);
    sharedControllersService = injector.get(SharedControllersService);
    store = injector.get(NgRedux);

  })().then(done).catch(done.fail));


  test('given instance, get supp file from getSupplementaryFile ', () => {
    const instance = {};
    const suppFileForInstance: FileFormControl = service.getSupplementaryFile(instance);
    expect(suppFileForInstance.isVisible).toBeTruthy();
    expect(suppFileForInstance.hiddenFile.length).toBeGreaterThanOrEqual(1);
    expect(suppFileForInstance.hiddenFile[0].validations[0].validatorName).toEqual("isFileTooBig");
  });

  test('concatSupplementaryFile add SupplementaryFile control and hidden file', () => {

    //given
    const instance = {};
    const controls = [sharedControllersService.getLegacyRegion(instance)];
    expect(controls).toHaveLength(1);

    //when
    const result = service.concatSupplementaryFile(controls, instance);

    //then
    expect(controls).toHaveLength(1); //original controls remain the same

    expect(result.map((control) => {return control.controlName})).toEqual([
      "legacyRegion",
      "supplementaryFile",
      "supplementaryFile_hidden",
      "supplementaryFile_hidden_content"
    ]);
  });
  each([
    [null, false],
    [{}, true]
  ]).
  test('retrieveInstanceIfUpdateMode returns %s if update mode is %s', (expected, isUpdateModalMode) => {
    //given
    const store= <any>{
      getState() {
        return {
          global: {
            isUpdateModalMode
          }
        }}};
    const instance = {};

    //when
    let retrievedInstance = service.retrieveInstanceIfUpdateMode (store, instance);

    //then
    expect(retrievedInstance).toEqual(expected);

  });

  test('getRollBackOnFailureOptions', async (done)=> {
    service.getRollBackOnFailureOptions().subscribe((rollBackOnFailureOptions : SelectOption[])=>{
      expect(rollBackOnFailureOptions[0].id).toEqual('true');
      expect(rollBackOnFailureOptions[0].name).toEqual('Rollback');
      expect(rollBackOnFailureOptions[1].id).toEqual('false');
      expect(rollBackOnFailureOptions[1].name).toEqual('Don\'t Rollback');
      done();
    });
  });


});


class MockAppStore<T> {
  getState() {
    return {}
  }
}

class MockFeatureFlagsService {}
