import {getTestBed, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {NgRedux} from '@angular-redux/store';
import {FormControlNames} from "../service.control.generator";
import {ControlGeneratorUtil} from "../control.generator.util.service";
import {AaiService} from "../../../../services/aaiService/aai.service";
import {GenericFormService} from "../../generic-form.service";
import {FormBuilder} from "@angular/forms";
import {FormControlModel} from "../../../../models/formControlModels/formControl.model";
import {LogService} from "../../../../utils/log/log.service";
import {FeatureFlagsService} from "../../../../services/featureFlag/feature-flags.service";
import {SharedControllersService} from "../sharedControlles/shared.controllers.service";
import {PnfControlGenerator} from "./pnf.control.generator";
import {MockNgRedux} from "@angular-redux/store/testing";

class MockFeatureFlagsService {
}

class MockAppStore<T> {
  getState() {
    return {
      global: {
        flags: {
          "FLAG_NETWORK_TO_ASYNC_INSTANTIATION": false,
          "FLAG_SHOW_ASSIGNMENTS": true,
          "FLAG_FABRIC_CONFIGURATION_ASSIGNMENTS": true,
          "FLAG_SHOW_VERIFY_SERVICE": false,
          "FLAG_SERVICE_MODEL_CACHE": true,
          "FLAG_ADD_MSO_TESTAPI_FIELD": true
        }
      },
      service: {
        serviceHierarchy: {
          'serviceId': {
            'pnfs': {
              'pnfName': {}
            }
          }
        },
        serviceInstance: {
          'serviceId': {
            'pnfs': {
              'pnfName': {}
            }
          }
        }
      }
    }
  }
}

describe('PNF Control Generator', () => {
  let injector;
  let service: PnfControlGenerator;
  let httpMock: HttpTestingController;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        MockNgRedux,
        PnfControlGenerator,
        GenericFormService,
        ControlGeneratorUtil,
        SharedControllersService,
        AaiService,
        FormBuilder,
        LogService,
        {provide: FeatureFlagsService, useClass: MockFeatureFlagsService},
        {provide: NgRedux, useClass: MockAppStore}]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(PnfControlGenerator);
    httpMock = injector.get(HttpTestingController);

  })().then(done).catch(done.fail));

  test('getMacroFormControls check for mandatory controls', () => {
    const serviceId = "serviceId";
    const pnfName = "pnfName";
    const pnfStoreKey = "pnfStoreKey";
    const mandatoryControls: string[] = [
      FormControlNames.INSTANCE_NAME,
      FormControlNames.LCPCLOUD_REGION_ID,
      FormControlNames.TENANT_ID,
      FormControlNames.PRODUCT_FAMILY_ID
    ];

    const controls: FormControlModel[] = service.getMacroFormControls(serviceId, pnfStoreKey, pnfName, []);

    for (let i = 0; i < mandatoryControls.length; i++) {
      let requiredExist = controls.find(ctrl => ctrl.controlName === mandatoryControls[i]).validations.find(item => item.validatorName === 'required');
      expect(requiredExist).toBeDefined();
    }
  });

  test('should provide empty array on getMacroFormControls when serviceId, pnfName and pnfStoreKey equals to null', () => {
    let pnfStoreKey = null;
    const serviceId = null;
    const pnfName: string = null;

    const controls: FormControlModel[] = service.getMacroFormControls(serviceId, pnfStoreKey, pnfName, []);

    expect(controls).toEqual([]);
  });

  test('getAlacartFormControls check for mandatory controls', () => {
    const serviceId = "serviceId";
    const pnfName = "pnfName";
    const pnfStoreKey = "pnfStoreKey";
    const mandatoryControls: string[] = [
      FormControlNames.INSTANCE_NAME,
      FormControlNames.LCPCLOUD_REGION_ID,
      'tenantId',
      'platformName',
      'lineOfBusiness',
      'rollbackOnFailure'
    ];

    const controls: FormControlModel[] = service.getAlaCarteFormControls(serviceId, pnfStoreKey, pnfName, []);

    for (let i = 0; i < mandatoryControls.length; i++) {
      let requiredExist = controls.find(ctrl => ctrl.controlName === mandatoryControls[i]).validations.find(item => item.validatorName === 'required');
      expect(requiredExist).toBeDefined();
    }
  });

  test('should provide empty array on getAlaCarteFormControls when serviceId, pnfName and pnfStoreKey equals to null', () => {
    let pnfStoreKey = null;
    const serviceId = null;
    const pnfName : string = null;

    const result:FormControlModel[] = service.getAlaCarteFormControls(serviceId, pnfStoreKey, pnfName, []);

    expect(result).toEqual([]);
  });

  //
  // test('getMacroFormControls should return the correct order of controls', () => {
  //   const serviceId : string = "6e59c5de-f052-46fa-aa7e-2fca9d674c44";
  //   const pnfName : string = "VF_vGeraldine 0";
  //   const pnfStoreKey : string = null;
  //   const controls :FormControlModel[] = service.getMacroFormControls(serviceId, pnfStoreKey, pnfName, []);
  //
  //   const controlsOrderNames = [
  //     FormControlNames.INSTANCE_NAME,
  //     FormControlNames.PRODUCT_FAMILY_ID,
  //     FormControlNames.LCPCLOUD_REGION_ID ,
  //     'legacyRegion',
  //     'tenantId',
  //     'platformName',
  //     'lineOfBusiness'];
  //
  //   expect(controls.length).toEqual(7);
  //   for(let i = 0 ; i < controls.length ; i++){
  //     expect(controls[i].controlName).toEqual(controlsOrderNames[i]);
  //   }
  // });
  //
  // test('getAlacartFormControls should return the correct order of controls', () => {
  //   const controls = getALaCarteFormControls(null);
  //
  //   const controlsOrderNames = [
  //     FormControlNames.INSTANCE_NAME,
  //     FormControlNames.PRODUCT_FAMILY_ID,
  //     FormControlNames.LCPCLOUD_REGION_ID,
  //     'legacyRegion',
  //     'tenantId',
  //     'platformName',
  //     'lineOfBusiness',
  //     'rollbackOnFailure'];
  //   expect(controls.length).toEqual(8);
  //   for(let i = 0 ; i < controls.length ; i++) {
  //     expect(controls[i].controlName).toEqual(controlsOrderNames[i]);
  //   }
  // });
  //
  //
});

