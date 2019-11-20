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
import {VfModuleUpgradePopupService} from "./vfModule.upgrade.popuop.service";

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

  test('getGenericFormPopupDetails should return object with correct title', () => {
    expect(service.getTitle()).toBe("Upgrade Module")
  });

});
