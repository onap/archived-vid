import {DefaultDataGeneratorService} from "../../../../services/defaultDataServiceGenerator/default.data.generator.service";
import {NgRedux} from "@angular-redux/store";
import {IframeService} from "../../../../utils/iframe.service";
import {VfModulePopupService} from "../vfModule/vfModule.popup.service";
import {FormBuilder} from "@angular/forms";
import {GenericFormService} from "../../../genericForm/generic-form.service";
import {BasicPopupService} from "../basic.popup.service";
import {AaiService} from "../../../../services/aaiService/aai.service";
import {LogService} from "../../../../utils/log/log.service";
import {HttpClient} from "@angular/common/http";
import {ControlGeneratorUtil} from "../../../genericForm/formControlsServices/control.generator.util.service";
import {PnfControlGenerator} from "../../../genericForm/formControlsServices/pnfGenerator/pnf.control.generator";
import {UUIDData} from "../../generic-form-popup.component";
import {FeatureFlagsService} from "../../../../services/featureFlag/feature-flags.service";
import {getTestBed, TestBed} from "@angular/core/testing";
import {VfModuleUpgradePopupService} from "../vfModuleUpgrade/vfModule.upgrade.popuop.service";
import {SharedControllersService} from "../../../genericForm/formControlsServices/sharedControlles/shared.controllers.service";
import { PnfPopupService } from "../pnf/pnf.popup.service";

class MockAppStore<T> {}

class MockReduxStore<T> {
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
          "serviceId": {
            "service": {
              "uuid": "6e59c5de-f052-46fa-aa7e-2fca9d674c44",
              "invariantUuid": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
              "name": "action-data",
              "version": "1.0",
              "toscaModelURL": null,
              "category": "Emanuel",
              "serviceType": "",
              "serviceRole": "",
              "description": "action-data",
              "serviceEcompNaming": "false",
              "instantiationType": "Macro",
              "vidNotions": {
                "instantiationType": "Macro"
              },
            },
            "globalSubscriberId": "subscriberId",
            "pnfs": {
              "pnfInstanceV1": {
                "name": "pnfName",
                "pnfStoreKey": "pnfInstanceV1",
                "version": "1.0",
                "description": "PNF description",
                "uuid": "0903e1c0-8e03-4936-b5c2-260653b96413",
                "invariantUuid": "00beb8f9-6d39-452f-816d-c709b9cbb87d"
              },
              "pnfInstanceV2": {
                "name": "pnfName2",
                "pnfStoreKey": "pnfInstanceV2"
              }
            },
            "modelInfo": {
              "modelInvariantId": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
              "modelVersionId": "6b528779-44a3-4472-bdff-9cd15ec93450",
              "modelName": "action-data",
              "modelVersion": "1.0",
              "uuid": "6b528779-44a3-4472-bdff-9cd15ec93450"
            },
            "instanceName": "InstanceName",
            "owningEntityId": "d61e6f2d-12fa-4cc2-91df-7c244011d6fc",
            "productFamilyId": "17cc1042-527b-11e6-beb8-9e71128cae77",
            "lcpCloudRegionId": "AAIAIC25",
            "tenantId": "092eb9e8e4b7412e8787dd091bc58e86",
            "aicZoneId": "JAG1",
            "projectName": null,
            "rollbackOnFailure": "true",
            "aicZoneName": "YUDFJULP-JAG1",
            "owningEntityName": "WayneHolland",
            "testApi": "GR_API",
            "tenantName": "USP-SIP-IC-24335-T-01",
            "bulkSize": 1,
            "isALaCarte": false,
            "name": "action-data",
            "version": "1.0",
            "description": "",
            "category": "",
            "uuid": "6b528779-44a3-4472-bdff-9cd15ec93450",
            "invariantUuid": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
            "serviceType": "",
            "serviceRole": "",
            "isMultiStepDesign": false
          }
        },
        "serviceInstance": {
          "serviceId": {
            "globalSubscriberId": "subscriberId",
            "pnfs": {
              "pnfInstanceV1": {
                "name": "pnfName",
                "pnfStoreKey": "pnfInstanceV1"
              },
              "pnfInstanceV2": {
                "name": "pnfName2",
                "pnfStoreKey": "pnfInstanceV2"
              }
            },
            "modelInfo": {
              "modelInvariantId": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
              "modelVersionId": "6b528779-44a3-4472-bdff-9cd15ec93450",
              "modelName": "action-data",
              "modelVersion": "1.0",
              "uuid": "6b528779-44a3-4472-bdff-9cd15ec93450"
            },
            "instanceName": "InstanceName",
            "owningEntityId": "d61e6f2d-12fa-4cc2-91df-7c244011d6fc",
            "productFamilyId": "17cc1042-527b-11e6-beb8-9e71128cae77",
            "lcpCloudRegionId": "AAIAIC25",
            "tenantId": "092eb9e8e4b7412e8787dd091bc58e86",
            "aicZoneId": "JAG1",
            "projectName": null,
            "rollbackOnFailure": "true",
            "aicZoneName": "YUDFJULP-JAG1",
            "owningEntityName": "WayneHolland",
            "testApi": "GR_API",
            "tenantName": "USP-SIP-IC-24335-T-01",
            "bulkSize": 1,
            "isALaCarte": false,
            "name": "action-data",
            "version": "1.0",
            "description": "",
            "category": "",
            "uuid": "6b528779-44a3-4472-bdff-9cd15ec93450",
            "invariantUuid": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
            "serviceType": "",
            "serviceRole": "",
            "isMultiStepDesign": false
          }
        },
        "subscribers": [
          {
            "id": "subscriberId",
            "name": "subscriberName",
            "isPermitted": true
          }
        ]
      }
    }
  }
}

class MockFeatureFlagsService {}

describe('pnf new popup service', () => {
  let injector;
  let service: PnfPopupService;
  let genericFormService: GenericFormService
  let defaultDataGeneratorService: DefaultDataGeneratorService;
  let fb: FormBuilder;
  let iframeService: IframeService;


  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      providers : [
        PnfPopupService,
        PnfPopupService,
        DefaultDataGeneratorService,
        GenericFormService,
        FormBuilder,
        IframeService,
        {provide:FeatureFlagsService, useClass: MockFeatureFlagsService},
        AaiService,
        LogService,
        BasicPopupService,
        VfModulePopupService,
        VfModuleUpgradePopupService,
        ControlGeneratorUtil,
        SharedControllersService,
        PnfControlGenerator,
        {provide: NgRedux, useClass: MockReduxStore},
        {provide: HttpClient, useClass: MockAppStore},
      ]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(PnfPopupService);
    genericFormService = injector.get(GenericFormService);
    defaultDataGeneratorService = injector.get(DefaultDataGeneratorService);
    fb = injector.get(FormBuilder);
    iframeService = injector.get(IframeService);

  })().then(done).catch(done.fail));

  test('getGenericFormPopupDetails returns the FormPopupDetails object', () => {
      const serviceId: string = 'serviceId';
      const pnfModelName: string = 'pnfInstanceV2';
      const pnfStoreKey: string = 'pnfInstanceV2';
      let uuidData: UUIDData = <any>{
        serviceId: "serviceId",
        modelName: "pnfInstanceV2",
        pnfStoreKey: "pnfInstanceV2"
      };

      const formPopupDetailsObject = service.getGenericFormPopupDetails(serviceId, pnfModelName, pnfStoreKey, null, uuidData, true);

      expect(formPopupDetailsObject).toBeDefined();
    }
  );

  test('getInstance with empty storekey should be created', () => {
    const serviceId: string = 'serviceId';
    const pnfModelName: string = 'pnfInstanceV1';

    const newInstance = service.getInstance(serviceId, pnfModelName, null);

    expect(newInstance).toBeDefined();
    expect(newInstance.pnfStoreKey).toBeNull();
  });

  test('getInstance with not empty storekey should return existing instance with the same model name as store key', () => {
    const serviceId: string = 'serviceId';
    const pnfModelName: string = 'pnfInstanceV1';
    const pnfStoreKey: string = 'pnfInstanceV2';

    const newInstance = service.getInstance(serviceId, pnfModelName, pnfStoreKey);

    expect(newInstance).toBeDefined();
    expect(newInstance.pnfStoreKey).toEqual('pnfInstanceV2');
  });

  test('getModelInformation  pnf should update modelInformations', () => {
    const serviceId: string = 'serviceId';
    const pnfModelName: string = 'pnfInstanceV1';

    service.getModelInformation(serviceId, pnfModelName);

    expect(service.modelInformations.length).toEqual(14);

    expect(service.modelInformations[0].label).toEqual("Subscriber Name");
    expect(service.modelInformations[0].values).toEqual(['subscriberName']);

    expect(service.modelInformations[1].label).toEqual("Service Name");
    expect(service.modelInformations[1].values).toEqual(['action-data']);

    expect(service.modelInformations[2].label).toEqual("Service Instance Name");
    expect(service.modelInformations[2].values).toEqual(['InstanceName']);

    expect(service.modelInformations[3].label).toEqual("Model Name");
    expect(service.modelInformations[3].values).toEqual(['pnfName']);

    expect(service.modelInformations[4].label).toEqual("Model version");
    expect(service.modelInformations[4].values).toEqual(['1.0']);

    expect(service.modelInformations[5].label).toEqual("Description");
    expect(service.modelInformations[5].values).toEqual(['PNF description']);

    expect(service.modelInformations[6].label).toEqual("Category");
    expect(service.modelInformations[6].values).toEqual([undefined]);

    expect(service.modelInformations[7].label).toEqual("Sub Category");
    expect(service.modelInformations[7].values).toEqual([undefined]);

    expect(service.modelInformations[8].label).toEqual("UUID");
    expect(service.modelInformations[8].values).toEqual(['0903e1c0-8e03-4936-b5c2-260653b96413']);

    expect(service.modelInformations[9].label).toEqual("Invariant UUID");
    expect(service.modelInformations[9].values).toEqual(['00beb8f9-6d39-452f-816d-c709b9cbb87d']);

    expect(service.modelInformations[10].label).toEqual("Service type");
    expect(service.modelInformations[10].values).toEqual(['']);

    expect(service.modelInformations[11].label).toEqual("Service role");
    expect(service.modelInformations[11].values).toEqual(['']);

    expect(service.modelInformations[12].label).toEqual("Minimum to instantiate");
    expect(service.modelInformations[12].values).toEqual(['0']);

    expect(service.modelInformations[13].label).toEqual("Maximum to instantiate");
    expect(service.modelInformations[13].values).toEqual(['1']);
  });




  //
  // test('getTitle pnf should return the correct title for edit and create mode', () => {
  //   expect(service.getTitle(false)).toBe('Set a new PNF');
  //   expect(service.getTitle(true)).toBe('Edit PNF instance');
  // });
  //
  // test('getSubRightTitle pnf should return popup type', () => {
  //   expect(service.getSubRightTitle()).toBe('PNF Instance Details');
  // });
  //

  //
  // test('getSubLeftTitle new pnf popup should return service model name', () => {
  //   service.uuidData = {
  //     serviceId: '6e59c5de-f052-46fa-aa7e-2fca9d674c44',
  //     modelName: 'VF_vGeraldine 0'
  //   };
  //   expect(service.getSubLeftTitle()).toBe('PNF MODEL: VF_vGeraldine');
  // });
  //



});
