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
    return {
      "global": {
        "flags": {
          "FLAG_NETWORK_TO_ASYNC_INSTANTIATION": false,
          "FLAG_SHOW_ASSIGNMENTS": true,
          "FLAG_FABRIC_CONFIGURATION_ASSIGNMENTS": true,
          "FLAG_SHOW_VERIFY_SERVICE": false,
          "FLAG_SERVICE_MODEL_CACHE": true,
          "FLAG_ADD_MSO_TESTAPI_FIELD": true
        }
      },
      "service": {
        "serviceHierarchy": {
          "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc": {
            "service": {
              "uuid": "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc",
              "invariantUuid": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
              "name": "ComplexService",
              "version": "1.0",
              "toscaModelURL": null,
              "category": "Emanuel",
              "serviceType": "",
              "serviceRole": "",
              "description": "ComplexService",
              "serviceEcompNaming": "false",
              "instantiationType": "Macro",
              "inputs": {}
            },
            "vnfs": {
              "VF_vGeraldine 0": {
                "uuid": "d6557200-ecf2-4641-8094-5393ae3aae60",
                "invariantUuid": "4160458e-f648-4b30-a176-43881ffffe9e",
                "description": "VSP_vGeraldine",
                "name": "VF_vGeraldine",
                "version": "2.0",
                "customizationUuid": "91415b44-753d-494c-926a-456a9172bbb9",
                "inputs": {},
                "commands": {},
                "properties": {
                  "max_instances": 10,
                  "min_instances": 1,

                },
                "type": "VF",
                "modelCustomizationName": "VF_vGeraldine 0",
                "vfModules": {
                  "vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1": {
                    "uuid": "522159d5-d6e0-4c2a-aa44-5a542a12a830",
                    "invariantUuid": "98a7c88b-b577-476a-90e4-e25a5871e02b",
                    "customizationUuid": "55b1be94-671a-403e-a26c-667e9c47d091",
                    "description": null,
                    "name": "VfVgeraldine..vflorence_vlc..module-1",
                    "version": "2",
                    "modelCustomizationName": "VfVgeraldine..vflorence_vlc..module-1",
                    "properties": {
                      "minCountInstances": 0,
                      "maxCountInstances": null,
                      "initialCount": 0,
                      "vfModuleLabel": "vflorence_vlc"
                    },
                    "inputs": {},
                    "volumeGroupAllowed": true
                  }
                }
              }
            }
          }
        },
        "serviceInstance": {
          "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc": {
            "vnfs": {
              "2017-388_PASQUALE-vPE 0": {
                "action": "Create",
                "inMaint": false,
                "rollbackOnFailure": "true",
                "originalName": "2017-388_PASQUALE-vPE 0",
                "isMissingData": false,
                "trackById": "eymgwlevh54",
                "vfModules": {},
                "vnfStoreKey": "2017-388_PASQUALE-vPE 0",
                "uuid": "afacccf6-397d-45d6-b5ae-94c39734b168",
                "productFamilyId": "e433710f-9217-458d-a79d-1c7aff376d89",
                "lcpCloudRegionId": "AAIAIC25",
                "tenantId": "092eb9e8e4b7412e8787dd091bc58e86",
                "lineOfBusiness": "ONAP",
                "platformName": "platform",
                "modelInfo": {
                  "modelInvariantId": "72e465fe-71b1-4e7b-b5ed-9496118ff7a8",
                  "modelVersionId": "afacccf6-397d-45d6-b5ae-94c39734b168",
                  "modelName": "2017-388_PASQUALE-vPE",
                  "modelVersion": "4.0",
                  "modelCustomizationId": "b3c76f73-eeb5-4fb6-9d31-72a889f1811c",
                  "modelCustomizationName": "2017-388_PASQUALE-vPE 0",
                  "uuid": "afacccf6-397d-45d6-b5ae-94c39734b168",
                  "modelUniqueId": "b3c76f73-eeb5-4fb6-9d31-72a889f1811c"
                },
                "instanceName": "2017-388_PASQUALE-vPEAjXzainstanceName",
                "legacyRegion": "some legacy region",
                "instanceParams": [
                  {
                    "vnf_config_template_version": "17.2",
                    "bandwidth_units": "Gbps",
                    "bandwidth": "10",
                    "AIC_CLLI": "ATLMY8GA",
                    "ASN": "AV_vPE",
                    "vnf_instance_name": "mtnj309me6"
                  }
                ]
              }
            },
            "service": {
              "vidNotions": {
                "instantiationUI": "serviceWithVRF",
                "modelCategory": "other",
                "viewEditUI": "serviceWithVRF",
                "instantiationType": "ALaCarte"
              },
              "uuid": "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc",
              "invariantUuid": "7ee41ce4-4827-44b0-a48e-2707a59905d2",
              "name": "VRF Service for Test",
              "version": "1.0",
              "toscaModelURL": null,
              "category": "Network L4+",
              "serviceType": "INFRASTRUCTURE",
              "serviceRole": "Configuration",
              "description": "xxx",
              "serviceEcompNaming": "true",
              "instantiationType": "A-La-Carte",
              "inputs": {}
            },
            "isALaCarte": true
          }
        }
      }
    }
  }
}

class MockFeatureFlagsService {}
