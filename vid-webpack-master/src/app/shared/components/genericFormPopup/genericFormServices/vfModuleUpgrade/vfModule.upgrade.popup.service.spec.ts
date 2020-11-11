import {LogService} from "../../../../utils/log/log.service";
import {NgRedux} from "@angular-redux/store";
import {
  ControlGeneratorUtil,
  SDN_C_PRE_LOAD,
  SUPPLEMENTARY_FILE
} from "../../../genericForm/formControlsServices/control.generator.util.service";
import {AaiService} from "../../../../services/aaiService/aai.service";
import {HttpClient} from "@angular/common/http";
import {GenericFormService} from "../../../genericForm/generic-form.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {IframeService} from "../../../../utils/iframe.service";
import {DefaultDataGeneratorService} from "../../../../services/defaultDataServiceGenerator/default.data.generator.service";
import {BasicPopupService} from "../basic.popup.service";
import {VfModuleControlGenerator} from "../../../genericForm/formControlsServices/vfModuleGenerator/vfModule.control.generator";
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
import {SharedControllersService} from "../../../genericForm/formControlsServices/sharedControlles/shared.controllers.service";
import {ModalService} from "../../../customModal/services/modal.service";

class MockModalService<T> {}

class MockAppStore<T> {}

class MockReduxStore<T> {
  getState() {
    return {
      service: {
        serviceHierarchy: {
          serviceId: {
            vfModules: {
              vfModuleName: {
                volumeGroupAllowed: true
              }
            }
          }
        },
        serviceInstance : {
          serviceId : {
            vnfs : {
              vnfStoreKey : {
                vfModules: {
                  vfModuleName: {
                    vfModuleId : {
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
  let _sharedTreeService : SharedTreeService;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      providers: [
        VfModuleUpgradePopupService,
        ControlGeneratorUtil,
        SharedControllersService,
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
        {provide: ModalService, useClass: MockModalService}
      ]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(VfModuleUpgradePopupService);
    genericFormService = injector.get(GenericFormService);
    defaultDataGeneratorService = injector.get(DefaultDataGeneratorService);
    fb = injector.get(FormBuilder);
    iframeService = injector.get(IframeService);
    _sharedTreeService = injector.get(SharedTreeService);
    store = injector.get(NgRedux);
    service.uuidData = {
      vfModule: {
        data: {}
      },
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

  test('get controls should return retainVolumeGroup control with false', ()=> {
    getControlByNameAndCheckValue(UpgradeFormControlNames.RETAIN_VOLUME_GROUPS, false, true);
  });

  [true, false].forEach(notExistsOnModel => {
    test(`retainVolumeGroup control is shown when vfModule not from model (notExistsOnModel=${notExistsOnModel})`, () => {
      let stateCopy = _.cloneDeep(store.getState());
      stateCopy.service.serviceHierarchy['serviceId'].vfModules['vfModuleName'].volumeGroupAllowed = false;
      jest.spyOn(store, 'getState').mockReturnValue(stateCopy);

      jest.spyOn(_sharedTreeService, 'isVfModuleCustomizationIdNotExistsOnModel').mockReturnValue(notExistsOnModel);

      getControlByNameAndCheckValue(UpgradeFormControlNames.RETAIN_VOLUME_GROUPS, false, notExistsOnModel);
    });
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
        value: '[{"name": "c", "value": "c"}, {"name": "d", "value": "1"}]'
        },
      supplementaryFile_hidden : {
        value: {
          name: "name"
        }
      }
    };
form.value['retainAssignments'] = false;
    let expectedMergePayload = {
      a: "value",
      b: "another",
      supplementaryFileContent: [{"name": "c", "value": "c"}, {"name": "d", "value": "1"}],
      supplementaryFileName: "name",
	  retainAssignments: false
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


  test( 'get controls should return usePreload with true value', () => {
    getControlByNameAndCheckValue(SDN_C_PRE_LOAD, true, true);
  });
});
