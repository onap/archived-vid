import {LogService} from "../../../../utils/log/log.service";
import {NgRedux} from "@angular-redux/store";
import {BasicControlGenerator} from "../../../genericForm/formControlsServices/basic.control.generator";
import {AaiService} from "../../../../services/aaiService/aai.service";
import {HttpClient} from "@angular/common/http";
import {GenericFormService} from "../../../genericForm/generic-form.service";
import {FormBuilder} from "@angular/forms";
import {IframeService} from "../../../../utils/iframe.service";
import {DefaultDataGeneratorService} from "../../../../services/defaultDataServiceGenerator/default.data.generator.service";
import {BasicPopupService} from "../basic.popup.service";
import {VfModuleControlGenerator} from "../../../genericForm/formControlsServices/vfModuleGenerator/vfModule.control.generator";
import {SdcUiServices} from "onap-ui-angular";
import {FeatureFlagsService} from "../../../../services/featureFlag/feature-flags.service";
import {getTestBed, TestBed} from "@angular/core/testing";
import {UpgradeFormControlNames, VfModuleUpgradePopupService} from "./vfModule.upgrade.popuop.service";
import {SharedTreeService} from "../../../../../drawingBoard/service-planning/objectsToTree/shared.tree.service";

class MockModalService<T> {
}

class MockAppStore<T> {
}

let uuidData = {};

class MockReduxStore<T> {
  getState() {
    return {};
  }
}

class MockFeatureFlagsService {
}

describe('VFModule popup service', () => {
  let injector;
  let service: VfModuleUpgradePopupService;
  let genericFormService: GenericFormService;
  let defaultDataGeneratorService: DefaultDataGeneratorService;
  let fb: FormBuilder;
  let iframeService: IframeService;

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

  })().then(done).catch(done.fail));

  test('getTitle should return the correct title', () => {
    expect(service.getTitle()).toBe("Upgrade Module")
  });

  test('get controls should return retainAssignments control with false i', ()=> {

    const controls = service.getControls();
    expect(controls.length).toEqual(2);

    const retainAssignmentsControl = controls.find((control)=>{
      return control.controlName === UpgradeFormControlNames.RETAIN_ASSIGNMENTS;
    });

    expect(retainAssignmentsControl).toBeDefined();
    expect(retainAssignmentsControl.value).toBeTruthy();


    const retainVolumeGroup = controls.find((control)=>{
      return control.controlName === UpgradeFormControlNames.RETAIN_VOLUME_GROUPS;
    });

    expect(retainVolumeGroup).toBeDefined();
    expect(retainVolumeGroup.value).toBeTruthy();
  });
});
