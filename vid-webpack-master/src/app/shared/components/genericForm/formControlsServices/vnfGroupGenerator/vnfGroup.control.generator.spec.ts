import {getTestBed, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {NgRedux} from '@angular-redux/store';
import {FormControlNames} from "../service.control.generator";
import {BasicControlGenerator} from "../basic.control.generator";
import {AaiService} from "../../../../services/aaiService/aai.service";
import {GenericFormService} from "../../generic-form.service";
import {FormBuilder} from "@angular/forms";
import {
  FormControlModel,
  ValidatorModel,
  ValidatorOptions
} from "../../../../models/formControlModels/formControl.model";
import {LogService} from "../../../../utils/log/log.service";
import {VnfGroupControlGenerator} from "./vnfGroup.control.generator";
import {Observable} from "rxjs";
import {SelectOption} from "../../../../models/selectOption";
import {FeatureFlagsService} from "../../../../services/featureFlag/feature-flags.service";

class MockAppStore<T> {
  getState(){
    return {
      "global": {
        "name": null,
        "type": "UPDATE_DRAWING_BOARD_STATUS",
        "drawingBoardStatus": "CREATE",
        "flags": {
          "FLAG_NETWORK_TO_ASYNC_INSTANTIATION": false,
          "FLAG_ADD_MSO_TESTAPI_FIELD": true,
          "FLAG_SERVICE_MODEL_CACHE": false,
          "FLAG_SHOW_ASSIGNMENTS": true,
          "FLAG_FABRIC_CONFIGURATION_ASSIGNMENTS": true,
          "FLAG_DEFAULT_VNF": true,
          "FLAG_SETTING_DEFAULTS_IN_DRAWING_BOARD": true,
          "FLAG_A_LA_CARTE_AUDIT_INFO": true,
          "FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST": true,
          "FLAG_PRESENT_PROVIDER_NETWORKS_ASSOCIATIONS": true,
          "FLAG_1810_CR_SOFT_DELETE_ALACARTE_VF_MODULE": true,
          "FLAG_1902_NEW_VIEW_EDIT": true
        }
      },
      "service": {
        "serviceHierarchy": {
          "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc": {
            "service": {
              "vidNotions": {
                "instantiationUI": "serviceWithVnfGrouping",
                "modelCategory": "other",
                "viewEditUI": "serviceWithVnfGrouping"
              },
              "uuid": "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc",
              "invariantUuid": "7ee41ce4-4827-44b0-a48e-2707a59905d2",
              "name": "Grouping Service for Test",
              "version": "1.0",
              "toscaModelURL": null,
              "category": "Network L4+",
              "serviceType": "INFRASTRUCTURE",
              "serviceRole": "GROUPING",
              "description": "xxx",
              "serviceEcompNaming": "false",
              "instantiationType": "A-La-Carte",
              "inputs": {}
            },
            "vnfs": {},
            "networks": {},
            "collectionResources": {},
            "configurations": {},
            "fabricConfigurations": {},
            "serviceProxies": {
              "vdorothea_svc_vprs_proxy 0": {
                "uuid": "65fadfa8-a0d9-443f-95ad-836cd044e26c",
                "invariantUuid": "f4baae0c-b3a5-4ca1-a777-afbffe7010bc",
                "description": "A Proxy for Service vDOROTHEA_Svc_vPRS",
                "name": "vDOROTHEA_Svc_vPRS Service Proxy",
                "version": "1.0",
                "customizationUuid": "bdb63d23-e132-4ce7-af2c-a493b4cafac9",
                "inputs": {},
                "commands": {},
                "properties": {},
                "type": "Service Proxy",
                "sourceModelUuid": "da7827a2-366d-4be6-8c68-a69153c61274",
                "sourceModelInvariant": "24632e6b-584b-4f45-80d4-fefd75fd9f14",
                "sourceModelName": "vDOROTHEA_Svc_vPRS"
              },
              "tsbc0001vm001_svc_proxy 0": {
                "uuid": "65fadfa8-a0d9-443f-95ad-836cd044e26c",
                "invariantUuid": "f4baae0c-b3a5-4ca1-a777-afbffe7010bc",
                "description": "A Proxy for Service tsbc0001vm001_Svc",
                "name": "tsbc0001vm001_Svc Service Proxy",
                "version": "1.0",
                "customizationUuid": "3d814462-30fb-4c62-b997-9aa360d27ead",
                "inputs": {},
                "commands": {},
                "properties": {},
                "type": "Service Proxy",
                "sourceModelUuid": "28aeb8f6-5620-4148-8bfb-a5fb406f0309",
                "sourceModelInvariant": "c989ab9a-33c7-46ec-b521-1b2daef5f047",
                "sourceModelName": "tsbc0001vm001_Svc"
              }
            },
            "vfModules": {},
            "volumeGroups": {},
            "pnfs": {},
            "vnfGroups": {
              "groupingservicefortest..ResourceInstanceGroup..0": {
                "type": "VnfGroup",
                "invariantUuid": "4bb2e27e-ddab-4790-9c6d-1f731bc14a45",
                "uuid": "daeb6568-cef8-417f-9075-ed259ce59f48",
                "version": "1",
                "name": "groupingservicefortest..ResourceInstanceGroup..0",
                "modelCustomizationName": "groupingservicefortest..ResourceInstanceGroup..0",
                "properties": {
                  "ecomp_generated_naming": "false",
                  "contained_resource_type": "VF",
                  "role": "SERVICE-ACCESS",
                  "function": "DATA",
                  "description": "DDD0",
                  "type": "LOAD-GROUP"
                },
                "members": {
                  "vdorothea_svc_vprs_proxy 0": {
                    "uuid": "65fadfa8-a0d9-443f-95ad-836cd044e26c",
                    "invariantUuid": "f4baae0c-b3a5-4ca1-a777-afbffe7010bc",
                    "description": "A Proxy for Service vDOROTHEA_Svc_vPRS",
                    "name": "vDOROTHEA_Svc_vPRS Service Proxy",
                    "version": "1.0",
                    "customizationUuid": "bdb63d23-e132-4ce7-af2c-a493b4cafac9",
                    "inputs": {},
                    "commands": {},
                    "properties": {},
                    "type": "Service Proxy",
                    "sourceModelUuid": "da7827a2-366d-4be6-8c68-a69153c61274",
                    "sourceModelInvariant": "24632e6b-584b-4f45-80d4-fefd75fd9f14",
                    "sourceModelName": "vDOROTHEA_Svc_vPRS"
                  }
                }
              },
              "groupingservicefortest..ResourceInstanceGroup..1": {
                "type": "VnfGroup",
                "invariantUuid": "a704112d-dbc6-4e56-8d4e-aec57e95ef9a",
                "uuid": "c2b300e6-45de-4e5e-abda-3032bee2de56",
                "version": "1",
                "name": "groupingservicefortest..ResourceInstanceGroup..1",
                "modelCustomizationName": "groupingservicefortest..ResourceInstanceGroup..1",
                "properties": {
                  "ecomp_generated_naming": "true",
                  "contained_resource_type": "VF",
                  "role": "SERVICE-ACCESS",
                  "function": "SIGNALING",
                  "description": "DDD1",
                  "type": "LOAD-GROUP"
                },
                "members": {
                  "tsbc0001vm001_svc_proxy 0": {
                    "uuid": "65fadfa8-a0d9-443f-95ad-836cd044e26c",
                    "invariantUuid": "f4baae0c-b3a5-4ca1-a777-afbffe7010bc",
                    "description": "A Proxy for Service tsbc0001vm001_Svc",
                    "name": "tsbc0001vm001_Svc Service Proxy",
                    "version": "1.0",
                    "customizationUuid": "3d814462-30fb-4c62-b997-9aa360d27ead",
                    "inputs": {},
                    "commands": {},
                    "properties": {},
                    "type": "Service Proxy",
                    "sourceModelUuid": "28aeb8f6-5620-4148-8bfb-a5fb406f0309",
                    "sourceModelInvariant": "c989ab9a-33c7-46ec-b521-1b2daef5f047",
                    "sourceModelName": "tsbc0001vm001_Svc"
                  }
                }
              }
            }
          }
        },
        "serviceInstance": {
          "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc": {
            "existingVNFCounterMap": {},
            "existingVnfGroupCounterMap": {
              "daeb6568-cef8-417f-9075-ed259ce59f48": 1,
              "c2b300e6-45de-4e5e-abda-3032bee2de56": 0
              },
              "existingNetworksCounterMap": {},
              "vnfs": {},
              "vnfGroups": {
                "groupingservicefortest..ResourceInstanceGroup..0": {
                  "inMaint": false,
                  "rollbackOnFailure": "true",
                  "originalName": "groupingservicefortest..ResourceInstanceGroup..0",
                  "isMissingData": false,
                  "trackById": "johjmxpmrlk",
                  "vnfGroupStoreKey": "groupingservicefortest..ResourceInstanceGroup..0",
                  "instanceName": "groupingservicefortestResourceInstanceGroup0",
                  "instanceParams": [
                    {}
                  ],
                  "modelInfo": {
                    "modelInvariantId": "4bb2e27e-ddab-4790-9c6d-1f731bc14a45",
                    "modelVersionId": "daeb6568-cef8-417f-9075-ed259ce59f48",
                    "modelName": "groupingservicefortest..ResourceInstanceGroup..0",
                    "modelVersion": "1",
                    "modelCustomizationName": "groupingservicefortest..ResourceInstanceGroup..0",
                    "uuid": "daeb6568-cef8-417f-9075-ed259ce59f48"
                  },
                  "uuid": "daeb6568-cef8-417f-9075-ed259ce59f48"
                }
              },
              "isEcompGeneratedNaming": false,
            "existingNames": {}
          }
        }
      }
    }
  }
}

class MockFeatureFlagsService {}

describe('VNF Group Control Generator', () => {
  let injector;
  let service: VnfGroupControlGenerator;
  let httpMock: HttpTestingController;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [VnfGroupControlGenerator,
        GenericFormService,
        BasicControlGenerator,
        AaiService,
        FormBuilder,
        LogService,
        {provide:FeatureFlagsService, useClass: MockFeatureFlagsService},
        {provide: NgRedux, useClass: MockAppStore}]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(VnfGroupControlGenerator);
    httpMock = injector.get(HttpTestingController);

  })().then(done).catch(done.fail));



  test('getMacroFormControls check for mandatory controls', () => {
    const serviceId : string = "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc";
    const vnfGroupName : string = "groupingservicefortest..ResourceInstanceGroup..0";
    const vnfGroupStoreKey : string = "groupingservicefortest..ResourceInstanceGroup..0";
    const controls :FormControlModel[] = service.getMacroFormControls(serviceId, vnfGroupStoreKey, vnfGroupName, []);

    const mandatoryControls : string[] = [
      FormControlNames.INSTANCE_NAME
    ];

    for(let i = 0 ; i < mandatoryControls.length ; i++){
      let requiredExist = controls.find(ctrl => ctrl.controlName === mandatoryControls[i]).validations.find(item => item.validatorName === 'required');
      expect(requiredExist).toBeDefined();
    }
  });

  test('getMacroFormControls should return the correct order of controls', () => {
    const serviceId : string = "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc";
    const vnfGroupName : string = "groupingservicefortest..ResourceInstanceGroup..0";
    const vnfGroupStoreKey : string = "groupingservicefortest..ResourceInstanceGroup..0";
    const controls :FormControlModel[] = service.getMacroFormControls(serviceId, vnfGroupStoreKey, vnfGroupName, []);

    const controlsOrderNames = [
      FormControlNames.INSTANCE_NAME,
      'rollbackOnFailure'];

    expect(controls.length).toEqual(1);
    for(let i = 0 ; i < controls.length ; i++){
      expect(controls[i].controlName).toEqual(controlsOrderNames[i]);
    }
  });

  test('getMacroFormControls check for mandatory controls when ecomp naming = true', () => {
    const serviceId : string = "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc";
    const vnfGroupName : string = "groupingservicefortest..ResourceInstanceGroup..1";
    const vnfGroupStoreKey : string = "groupingservicefortest..ResourceInstanceGroup..1";
    const controls :FormControlModel[] = service.getMacroFormControls(serviceId, vnfGroupStoreKey, vnfGroupName, []);

    let isOptional = controls.find(ctrl => ctrl.controlName === 'instanceName').validations.find(item => item.validatorName !== 'required');
    expect(isOptional).toBeTruthy();
  });

  test('getAlacartFormControls should return the correct order of controls', () => {
    const controls:FormControlModel[] = getALaCarteFormControls();

    const controlsOrderNames = [
      FormControlNames.INSTANCE_NAME,
      'rollbackOnFailure'];
    expect(controls.length).toEqual(2);
    for(let i = 0 ; i < controls.length ; i++) {
      expect(controls[i].controlName).toEqual(controlsOrderNames[i]);
    }
  });


  test('getAlacartFormControls check for mandatory controls', () => {
    const controls:FormControlModel[] = getALaCarteFormControls();

    const mandatoryControls : string[] = [
      FormControlNames.INSTANCE_NAME,
      'rollbackOnFailure'
    ];
    for(let i = 0 ; i < mandatoryControls.length ; i++){
      let requiredExist = controls.find(ctrl => ctrl.controlName === mandatoryControls[i]).validations.find(item => item.validatorName === 'required');
      expect(requiredExist).toBeDefined();
    }
  });

  test('getAlacartFormControls with ecomp_naming true check for mandatory controls', () => {
    const controls:FormControlModel[] = getALaCarteFormControls();

    const mandatoryControls : string[] = [
      'rollbackOnFailure'
    ];
    for(let i = 0 ; i < mandatoryControls.length ; i++){
      let requiredExist = controls.find(ctrl => ctrl.controlName === mandatoryControls[i]).validations.find(item => item.validatorName === 'required');
      expect(requiredExist).toBeDefined();
    }
  });

  test('default instanceName', () => {
    const serviceId : string = "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc";
    const vnfGroupName : string = "groupingservicefortest..ResourceInstanceGroup..0";
    let result:FormControlModel  = service.getInstanceName(null, serviceId, vnfGroupName, false);
    expect(result.value).toEqual("groupingservicefortest..ResourceInstanceGroup..0");
  });

  test('rollbackOnFailure', () => {
    let result : Observable<SelectOption[]> = service.getRollBackOnFailureOptions();
    result.subscribe((val)=>{
      expect(val).toEqual([
        new SelectOption({id: 'true', name: 'Rollback'}),
        new SelectOption({id: 'false', name: 'Don\'t Rollback'})
      ]);
    });
  });

  test('getAlacartFormControls instance name control validator shall have the expected regex', () => {
    const controls:FormControlModel[] = getALaCarteFormControls();

    const instanceNameControl: FormControlModel = <FormControlModel>controls.find(item => item.controlName === FormControlNames.INSTANCE_NAME);
    const instanceNameValidator: ValidatorModel = instanceNameControl.validations.find(val => val.validatorName === ValidatorOptions.pattern);
    expect(instanceNameValidator.validatorArg).toEqual(/^[a-zA-Z0-9._-]*$/);
  });

  function getALaCarteFormControls():FormControlModel[] {
    const serviceId: string = "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc";
    const vnfGroupName: string = "groupingservicefortest..ResourceInstanceGroup..0";
    const vnfGroupStoreKey: string = "groupingservicefortest..ResourceInstanceGroup..0";
    const controls: FormControlModel[] = service.getAlaCarteFormControls(serviceId, vnfGroupStoreKey, vnfGroupName, []);
    return controls;
  }
});

