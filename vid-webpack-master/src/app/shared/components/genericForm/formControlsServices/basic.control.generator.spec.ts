import {getTestBed, TestBed} from '@angular/core/testing';
import {AaiService} from "../../../services/aaiService/aai.service";
import {FormControlModel} from "../../../models/formControlModels/formControl.model";
import {FeatureFlagsService} from "../../../services/featureFlag/feature-flags.service";
import {BasicControlGenerator} from "./basic.control.generator";
import {NgRedux} from '@angular-redux/store';
import each from "jest-each";
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {FileFormControl} from "../../../models/formControlModels/fileFormControl.model";

class MockAppStore<T> {}

class MockFeatureFlagsService {}

describe('Basic Control Generator', () => {
  let injector;
  let service: BasicControlGenerator;
  let httpMock: HttpTestingController;


  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [BasicControlGenerator,
        AaiService,
        {provide:FeatureFlagsService, useClass: MockFeatureFlagsService},
        {provide: NgRedux, useClass: MockAppStore}]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(BasicControlGenerator);
    httpMock = injector.get(HttpTestingController);

  })().then(done).catch(done.fail));


  test('getlegacyRegion with AAIAIC25 - isVisible true', () => {
    const instance = {lcpCloudRegionId : 'AAIAIC25'};
    const legacyRegionControl: FormControlModel = service.getLegacyRegion(instance);
    expect(legacyRegionControl.isVisible).toBeTruthy();
  });

  test('getlegacyRegion without AAIAIC25 - isVisible false', () => {
    const instance = {lcpCloudRegionId : 'olson3'};
    const legacyRegionControl: FormControlModel = service.getLegacyRegion(instance);
    expect(legacyRegionControl.isVisible).toBeFalsy();
  });

  test('sdn-preload checkbox is visible', () => {
    const instance = {};
    const sdncPreload: FormControlModel = service.getSDNCControl(instance);
    expect (sdncPreload.displayName).toEqual('SDN-C pre-load');
    expect (sdncPreload.value).toBeFalsy();
  });

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
    const controls = [service.getLegacyRegion(instance)];
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

  test('always fail', ()=>{
    fail();
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

});
