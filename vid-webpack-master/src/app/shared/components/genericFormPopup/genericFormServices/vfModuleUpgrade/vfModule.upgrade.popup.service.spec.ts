import {LogService} from "../../../../utils/log/log.service";
import {NgRedux} from "@angular-redux/store";
import {
  BasicControlGenerator,
  SDN_C_PRE_LOAD,
  SUPPLEMENTARY_FILE
} from "../../../genericForm/formControlsServices/basic.control.generator";
import {AaiService} from "../../../../services/aaiService/aai.service";
import {HttpClient} from "@angular/common/http";
import {GenericFormService} from "../../../genericForm/generic-form.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {IframeService} from "../../../../utils/iframe.service";
import {DefaultDataGeneratorService} from "../../../../services/defaultDataServiceGenerator/default.data.generator.service";
import {BasicPopupService} from "../basic.popup.service";
import {VfModuleControlGenerator} from "../../../genericForm/formControlsServices/vfModuleGenerator/vfModule.control.generator";
import {SdcUiServices} from "onap-ui-angular";
import {FeatureFlagsService} from "../../../../services/featureFlag/feature-flags.service";
import {getTestBed, TestBed} from "@angular/core/testing";
import {UpgradeFormControlNames, VfModuleUpgradePopupService} from "./vfModule.upgrade.popuop.service";
import {SharedTreeService} from "../../../../../drawingBoard/service-planning/objectsToTree/shared.tree.service";
import {AppState} from "../../../../store/reducers";
import {instance, mock} from "ts-mockito";
import {GeneralActions} from "../../../../storeUtil/utils/general/general.actions";
import {VfModuleActions} from "../../../../storeUtil/utils/vfModule/vfModule.actions";
import {ServiceActions} from "../../../../storeUtil/utils/service/service.actions";
import {FormControlModel} from "../../../../models/formControlModels/formControl.model";
import * as _ from "lodash";

class MockModalService<T> {}

class MockAppStore<T> {}

class MockReduxStore<T> {
  getState() {
    return {
      service: {
        serviceInstance : {
          serviceId : {
            vnfs : {
              vnfStoreKey : {
                vfModules: {
                  vfModuleName: {
                    vfModuleId : {
                      volumeGroupName: "VGName",
                      supplementaryFileName: "myFileName"
                    }}}}}}}}
    };
  }
  dispatch() {}
}

class MockFeatureFlagsService {}

describe('VFModule popup service', () => {
  let injector;
  let service: VfModuleUpgradePopupService;
  let genericFormService: GenericFormService;
  let defaultDataGeneratorService: DefaultDataGeneratorService;
  let fb: FormBuilder;
  let iframeService: IframeService;
  let store : NgRedux<AppState>;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      providers: [
        VfModuleUpgradePopupService,
        BasicControlGenerator,
        VfModuleControlGenerator,
        DefaultDataGeneratorService,
        GenericFormService,
        FormBuilder,
        IframeService,
        AaiService,
        LogService,
        BasicPopupService,
        SharedTreeService,
        {provide: FeatureFlagsService, useClass: MockFeatureFlagsService},
        {provide: NgRedux, useClass: MockReduxStore},
        {provide: HttpClient, useClass: MockAppStore},
        {provide: SdcUiServices.ModalService, useClass: MockModalService}
      ]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(VfModuleUpgradePopupService);
    genericFormService = injector.get(GenericFormService);
    defaultDataGeneratorService = injector.get(DefaultDataGeneratorService);
    fb = injector.get(FormBuilder);
    iframeService = injector.get(IframeService);
    store = injector.get(NgRedux);
    service.uuidData = {
      modelName: 'vfModuleName',
      vFModuleStoreKey: 'vfModuleId'
    };

  })().then(done).catch(done.fail));

  test('getTitle should return the correct title', () => {
    expect(service.getTitle()).toBe("Upgrade Module")
  });

  function findControlByName(controls: FormControlModel[], controlName: string) : FormControlModel {
    return controls.find((control) => {
      return control.controlName === controlName;
    });
  }

  function getControlByNameAndCheckValue(controlName: string, expectedValue: any, shouldControllerExist: boolean) {
    const controls = service.getControls('serviceId', 'vnfStoreKey', 'vfModuleId', true);
    const control = findControlByName(controls, controlName);
    if(shouldControllerExist){
      expect(control).toBeDefined();
      expect(control.value).toEqual(expectedValue);
    }
    else{
      expect(control).toBeUndefined();
    }
  }

  test('get controls should return retainAssignments control with true value', ()=> {
    getControlByNameAndCheckValue(UpgradeFormControlNames.RETAIN_ASSIGNMENTS, true, true);
  });

  test('get controls should return retainVolumeGroup control with true value', ()=> {
    getControlByNameAndCheckValue(UpgradeFormControlNames.RETAIN_VOLUME_GROUPS, true, true);
  });

  test('get controls should NOT return retainVolumeGroup control with true value', ()=> {
    let stateCopy =_.cloneDeep(store.getState());
    stateCopy.service.serviceInstance['serviceId'].vnfs['vnfStoreKey'].vfModules['vfModuleName']['vfModuleId']['volumeGroupName'] = undefined;
    jest.spyOn(store, 'getState').mockReturnValue(stateCopy);
    getControlByNameAndCheckValue(UpgradeFormControlNames.RETAIN_VOLUME_GROUPS, null, false);
  });

  test('get controls should contain SUPPLEMENTARY_FILE controller', ()=> {

    //when
    const controls = service.getControls('serviceId', 'vnfStoreKey', 'vfModuleId', true);

    //then
    const control = findControlByName(controls, SUPPLEMENTARY_FILE);
    expect(control).toBeDefined();
    expect(control.selectedFile).toBe("myFileName");
  });

  test('on submit should call merge action of form value to vfModule', () => {

    //given

    const serviceId = "serviceId5";
    const vnfStoreKey = 'vnfStoreKey3';
    const modelName = 'modelA';
    const dynamicModelName = 'dynamicModel';
    const that = {
      uuidData : {
        vfModule : {
          data : {
            modelName,
            dynamicModelName
          },
          parent : {
            data: {
              vnfStoreKey
            }}},
        serviceId
      },
      serviceModel: {
        uuid : "someUuid"
      },
      _iframeService: {
        removeClassCloseModal : jest.fn()
      }
    };

    let mockFrom: FormGroup = mock(FormGroup);
    let form = instance(mockFrom);
    form.value = {
      a: "value",
      b: "another"
    };
    form.controls = {
      supplementaryFile_hidden_content : {
        value: '{"c": "c", "d": 1}'
        },
      supplementaryFile_hidden : {
        value: {
          name: "name"
        }
      }
    };

    let expectedMergePayload = {
      a: "value",
      b: "another",
      supplementaryFileContent: {c: "c", d: 1},
      supplementaryFileName: "name"
    };

    spyOn(store, 'dispatch');

    //when
    service.onSubmit(that, form);

    //then
    expect(store.dispatch).toBeCalledWith(
      {type: GeneralActions.MERGE_OBJECT_BY_PATH, path: ['serviceInstance', serviceId, 'vnfs', vnfStoreKey, 'vfModules',modelName, dynamicModelName], payload:expectedMergePayload});
    expect(store.dispatch).toBeCalledWith(
      {type: VfModuleActions.UPGRADE_VFMODULE, dynamicModelName: "dynamicModel", modelName: "modelA", serviceId: "serviceId5", vnfStoreKey: "vnfStoreKey3"});
    expect(store.dispatch).toBeCalledWith({type: ServiceActions.UPGRADE_SERVICE_ACTION, serviceUuid: "serviceId5"});

  });


  test( 'get controls should return usePreload with false value', () => {
    getControlByNameAndCheckValue(SDN_C_PRE_LOAD, false, true);
  });
});
