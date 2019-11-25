import {getTestBed, TestBed} from '@angular/core/testing';
import {AaiService} from "../../../services/aaiService/aai.service";
import {FormControlModel, ValidatorModel, ValidatorOptions} from "../../../models/formControlModels/formControl.model";
import {FeatureFlagsService} from "../../../services/featureFlag/feature-flags.service";
import {BasicControlGenerator} from "./basic.control.generator";
import {NgRedux} from '@angular-redux/store';
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

  test('given instance, get supp file from getSupplementaryFile ', () => {
    const instance = {};
    const suppFileForInstance: FileFormControl = service.getSupplementaryFile(instance);
    expect(suppFileForInstance.isVisible).toBeTruthy();
    expect(suppFileForInstance.hiddenFile.length).toBeGreaterThanOrEqual(1);
    expect(suppFileForInstance.hiddenFile[0].validations[0].validatorName).toEqual("isFileTooBig");
  });
});

