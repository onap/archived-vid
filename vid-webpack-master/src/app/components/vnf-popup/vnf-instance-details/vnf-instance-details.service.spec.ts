import { TestBed, getTestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import { VnfInstanceDetailsService } from './vnf-instance-details.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { NumbersLettersUnderscoreValidator } from '../../../shared/components/validators/numbersLettersUnderscore/numbersLettersUnderscore.validator';

describe('Vnf Instance Details Service', () => {
  let injector;
  let service: VnfInstanceDetailsService;
  let httpMock: HttpTestingController;

  let SERVICE_ID: string = '1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd';
  let serviceHierarchy;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [VnfInstanceDetailsService]
    });

    injector = getTestBed();
    service = injector.get(VnfInstanceDetailsService);
    httpMock = injector.get(HttpTestingController);
    serviceHierarchy = getServiceServiceHierarchy();
  });


  describe('#hasInstanceNameError', ()=> {
    it('hasInstanceNameError should return true if instanceName is illegal and enabled', (done: DoneFn) => {
      let form = generateFormGroup();
      form.controls['instanceName'].setValue('----');
      form.controls['instanceName'].setErrors({
        pattern : true
      });
      form.controls['instanceName'].markAsTouched();
      let result = service.hasInstanceNameError(form);
      expect(result).toBeTruthy();
      done();
    });

    it('hasInstanceNameError should return false if instanceName is illegal and enabled and pattern is ok', (done: DoneFn) => {
      let form = generateFormGroup();
      form.controls['instanceName'].setValue('----');
      form.controls['instanceName'].setErrors({
        otherError : true
      });
      form.controls['instanceName'].markAsTouched();
      let result = service.hasInstanceNameError(form);
      expect(result).toBeFalsy();
      done();
    });
  });

  describe('#isUnique', () => {
    it('Create Mode: should return false if instanceName exist', (done: DoneFn) => {
      serviceHierarchy = getServiceServiceHierarchy();
      let result = service.isUnique(serviceHierarchy, SERVICE_ID, 'uniqueInstanceName', false);
      expect(result).toBeFalsy();
      done();
    });

    it('Update Mode: should return true if instanceName exist once', (done: DoneFn) => {
      let result = service.isUnique(serviceHierarchy, SERVICE_ID, 'uniqueInstanceName', true);
      expect(result).toBeTruthy()
      done();
    });

    it('Create Mode: should return true if instanceName not exist', (done: DoneFn) => {
      let result = service.isUnique(serviceHierarchy, SERVICE_ID, 'uniqueInstanceNameNotExist', false);
      expect(result).toBeTruthy();
      done();
    });

    it('Create Mode: should return false if instanceName exist inside vf modules', (done: DoneFn) => {
      let result = service.isUnique(serviceHierarchy, SERVICE_ID, 'uniqueInstanceNameVfModule', false);
      expect(result).toBeFalsy();
      done();
    });

    it('Update Mode: should return true if instanceName  exist once inside vf modules', (done: DoneFn) => {
      let result = service.isUnique(serviceHierarchy, SERVICE_ID, 'uniqueInstanceNameVfModule', true);
      expect(result).toBeTruthy();
      done();
    });

    it('Create Mode: should return true if instanceName is not exist at vf modules and vnfs', (done: DoneFn) => {
      let result = service.isUnique(serviceHierarchy, SERVICE_ID, 'uniqueInstanceNameVfModuleNotExist', false);
      expect(result).toBeTruthy();
      done();
    });

    it('Create Mode: should return false if instanceName exist service name', (done: DoneFn) => {
      let result = service.isUnique(serviceHierarchy, SERVICE_ID, 'Instance-Name', false);
      expect(result).toBeFalsy();
      done();
    });

    it('Create Mode: should return false if volumeGroupName exist service name', (done: DoneFn) => {
      let result = service.isUnique(serviceHierarchy, SERVICE_ID, 'volumeGroupNameExist', false);
      expect(result).toBeFalsy();
      done();
    });

    it('Create Mode: should return true if volumeGroupName not exist service name', (done: DoneFn) => {
      let result = service.isUnique(serviceHierarchy, SERVICE_ID, 'volumeGroupNameNotExist', false);
      expect(result).toBeTruthy();
      done();
    });
  });

  function getServiceServiceHierarchy() {
    return JSON.parse(JSON.stringify(
      {
        "1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd": {
          "vnfs": {
            "2017-388_ADIOD-vPE 1": {
              "rollbackOnFailure": "true",
              "vfModules": {},
              "instanceParams": [
                {}
              ],
              "productFamilyId": "a4f6f2ae-9bf5-4ed7-b904-06b2099c4bd7",
              "lcpCloudRegionId": "AAIAIC25",
              "tenantId": "092eb9e8e4b7412e8787dd091bc58e86",
              "lineOfBusiness": "zzz1",
              "platformName": "platform",
              "instanceName": "uniqueInstanceName",
              "modelInfo": {
                "modelInvariantId": "00beb8f9-6d39-452f-816d-c709b9cbb87d",
                "modelVersionId": "0903e1c0-8e03-4936-b5c2-260653b96413",
                "modelName": "2017-388_ADIOD-vPE",
                "modelVersion": "1.0",
                "modelCustomizationId": "280dec31-f16d-488b-9668-4aae55d6648a",
                "modelCustomizationName": "2017-388_ADIOD-vPE 1"
              },
              "isUserProvidedNaming": true
            },
            "2017-388_ADIOD-vPE 0": {
              "rollbackOnFailure": "true",
              "vfModules": {},
              "instanceParams": [
                {}
              ],
              "productFamilyId": null,
              "lcpCloudRegionId": "mtn6",
              "tenantId": "1178612d2b394be4834ad77f567c0af2",
              "lineOfBusiness": "ECOMP",
              "platformName": "xxx1",
              "instanceName": "blaaa",
              "modelInfo": {
                "modelInvariantId": "72e465fe-71b1-4e7b-b5ed-9496118ff7a8",
                "modelVersionId": "afacccf6-397d-45d6-b5ae-94c39734b168",
                "modelName": "2017-388_ADIOD-vPE",
                "modelVersion": "4.0",
                "modelCustomizationId": "b3c76f73-eeb5-4fb6-9d31-72a889f1811c",
                "modelCustomizationName": "2017-388_ADIOD-vPE 0"
              },
              "isUserProvidedNaming": true
            },
            "2017488_ADIODvPE 0": {
              "rollbackOnFailure": "true",
              "vfModules": {
                "2017488_adiodvpe0..2017488AdiodVpe..ADIOD_vRE_BV..module-1": {
                  "2017488_adiodvpe0..2017488AdiodVpe..ADIOD_vRE_BV..module-1": {
                    "rollbackOnFailure": "true",
                    "instanceName": "uniqueInstanceNameVfModule",
                    "volumeGroupName": "volumeGroupNameExist",
                    "modelInfo": {
                      "modelInvariantId": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                      "modelVersionId": "25284168-24bb-4698-8cb4-3f509146eca5",
                      "modelName": "2017488AdiodVpe..ADIOD_vRE_BV..module-1",
                      "modelVersion": "6"
                    }
                  }
                }
              },
              "instanceParams": [
                {}
              ],
              "productFamilyId": "ebc3bc3d-62fd-4a3f-a037-f619df4ff034",
              "lcpCloudRegionId": "mtn6",
              "tenantId": "19c5ade915eb461e8af52fb2fd8cd1f2",
              "lineOfBusiness": "zzz1",
              "platformName": "platform",
              "instanceName": "2017488_ADIODvPE",
              "modelInfo": {
                "modelInvariantId": "72e465fe-71b1-4e7b-b5ed-9496118ff7a8",
                "modelVersionId": "69e09f68-8b63-4cc9-b9ff-860960b5db09",
                "modelName": "2017488_ADIODvPE",
                "modelVersion": "5.0",
                "modelCustomizationId": "1da7b585-5e61-4993-b95e-8e6606c81e45",
                "modelCustomizationName": "2017488_ADIODvPE 0"
              },
              "isUserProvidedNaming": true
            }
          },
          "instanceParams": [
            {}
          ],
          "globalSubscriberId": "e433710f-9217-458d-a79d-1c7aff376d89",
          "productFamilyId": "17cc1042-527b-11e6-beb8-9e71128cae77",
          "subscriptionServiceType": "VIRTUAL USP",
          "lcpCloudRegionId": "AAIAIC25",
          "tenantId": "092eb9e8e4b7412e8787dd091bc58e86",
          "aicZoneId": "DKJ1",
          "projectName": "DFW",
          "owningEntityId": "aaa1",
          "instanceName": "Instance-Name",
          "bulkSize": 1,
          "modelInfo": {
            "modelInvariantId": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
            "modelVersionId": "1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd",
            "modelName": "action-data",
            "modelVersion": "1.0"
          },
          "tenantName": "USP-SIP-IC-24335-T-01",
          "aicZoneName": "DKJSJDKA-DKJ1",
          "isUserProvidedNaming": true
        }
      }
    ));
  }

  function generateFormGroup() {
    return new FormGroup({
      productFamilyId: new FormControl(),
      lcpCloudRegionId: new FormControl(Validators.required),
      tenantId: new FormControl({value: null, disabled: false}, Validators.required),
      legacyRegion: new FormControl(),
      lineOfBusiness: new FormControl(),
      platformName: new FormControl(Validators.required),
      rollbackOnFailure: new FormControl(Validators.required),
      instanceName: new FormControl({value: null}, Validators.compose([Validators.required, NumbersLettersUnderscoreValidator.valid]))

    });
  }

});
