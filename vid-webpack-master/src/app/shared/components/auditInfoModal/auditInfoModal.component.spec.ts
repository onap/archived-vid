import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ContextMenuModule, ContextMenuService} from 'ngx-contextmenu';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {AuditInfoModalComponent} from "./auditInfoModal.component";
import {NgRedux} from "@angular-redux/store";
import {FeatureFlagsService} from "../../services/featureFlag/feature-flags.service";
import {AaiService} from "../../services/aaiService/aai.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {ScrollToModule} from "@nicky-lenaers/ngx-scroll-to";
import {RouterTestingModule} from "@angular/router/testing";
import {ModalModule} from "ngx-bootstrap";
import {CapitalizeAndFormatPipe} from "../../pipes/capitalize/capitalize-and-format.pipe";
import {ServiceInfoService} from "../../server/serviceInfo/serviceInfo.service";
import {IframeService} from "../../utils/iframe.service";
import {AuditInfoModalComponentService} from "./auditInfoModal.component.service";
import {ServiceInfoModel} from "../../server/serviceInfo/serviceInfo.model";
import {getTestBed} from "@angular/core/testing";
import {of} from 'rxjs';
import {NodeInstance} from "../../models/nodeInstance";
import {AuditInformationItem} from "../../models/auditInfoControlModels/auditInformationItems.model";

class MockAppStore<T> {
  getState() {
    return {
      global: {
        flags: {
          'FLAG_1902_NEW_VIEW_EDIT': true
        }
      },
      service: {
        serviceInstance: {}
      }
    }
  }

  dispatch() {

  }
}


describe('Audit Info Modal Component_serviceInfoService', () => {
  let component: AuditInfoModalComponent;
  let fixture: ComponentFixture<AuditInfoModalComponent>;
  let _serviceInfoService: ServiceInfoService;
  let injector;
  beforeAll(done => (async () => {

    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        ContextMenuModule,
        ScrollToModule.forRoot(),
        RouterTestingModule,
        ModalModule.forRoot()
      ],
      providers: [
        ServiceInfoService,
        AaiService,
        IframeService,
        AuditInfoModalComponentService,
        ContextMenuService,
        FeatureFlagsService,
        {provide: NgRedux, useClass: MockAppStore}
      ],
      declarations: [AuditInfoModalComponent, CapitalizeAndFormatPipe],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    _serviceInfoService = injector.get(ServiceInfoService);
    fixture = TestBed.createComponent(AuditInfoModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  })().then(done).catch(done.fail));


  test('component should be defined', () => {
    expect(component).toBeDefined();
  });
  test('setModalTitles should set modal title according to type', () => {
    component.setModalTitles('VNF');
    expect(component.type).toEqual('VNF');
    expect(component.title).toEqual('VNF Instantiation Information');
  });

  test('initializeProperties should init some component properties', () => {
    const auditInfoItems = getAuditInitItems();
    component.initializeProperties();
    expect(component.modelInfoItems).toEqual([]);
    expect(component.auditInfoItems).toEqual(auditInfoItems);

  });

  test('openAuditInfoModal', () => {
    const jobData: ServiceInfoModel = new ServiceInfoModel();
    spyOn(AuditInfoModalComponentService, 'createModelInformationItemsJob');
    spyOn(component, 'initAuditInfoData');
    spyOn(component.auditInfoModal, 'show');
    component.openAuditInfoModal(jobData);

    expect(AuditInfoModalComponentService.createModelInformationItemsJob).toHaveBeenCalledWith(jobData);
    expect(component.initAuditInfoData).toHaveBeenCalledWith(jobData);
    expect(component.auditInfoModal.show).toHaveBeenCalled();
  });

  test('onCancelClick', () => {
    spyOn(component, 'initializeProperties');
    spyOn(component.auditInfoModal, 'hide');
    component.onCancelClick();

    expect(component.initializeProperties).toHaveBeenCalledWith();
    expect(component.auditInfoModal.hide).toHaveBeenCalled();
  });

  test('openInstanceAuditInfoModal calls to getAuditStatusForRetry function', () => {
    spyOn(component.auditInfoModalComponentService, 'getModelInfo').and.returnValue([]);
    spyOn(component, 'initializeProperties');
    spyOn(component, 'setModalTitles');
    spyOn(component.auditInfoModal, 'show');
    spyOn(_serviceInfoService, 'getAuditStatusForRetry');
    jest.spyOn(_serviceInfoService, 'getAuditStatusForRetry').mockReturnValue(of([]))

    spyOn(AuditInfoModalComponentService, 'getInstanceModelName');
    const instanceId: string = "instanceID";
    const type: string = 'VNF';
    const model = {};
    const instance: NodeInstance = new NodeInstance();
    instance.trackById = 'trackById';
    instance.isFailed= true;

    AuditInfoModalComponent.openInstanceAuditInfoModal.next({
      instanceId: instanceId,
      type: type,
      model: model,
      instance: instance
    });

    expect(component.auditInfoItems.showVidStatus).toEqual(false);
    expect(component.initializeProperties).toHaveBeenCalled();
    expect(component.setModalTitles).toHaveBeenCalled();
    expect(_serviceInfoService.getAuditStatusForRetry).toHaveBeenCalledWith(instance.trackById);
    expect(component.auditInfoModal.show).toHaveBeenCalled();
    expect(AuditInfoModalComponentService.getInstanceModelName).toHaveBeenCalledWith(model);
  });

  test('openInstanceAuditInfoModal calls to getInstanceAuditStatus function', () => {
    spyOn(component.auditInfoModalComponentService, 'getModelInfo').and.returnValue([]);
    spyOn(component, 'initializeProperties');
    spyOn(component, 'setModalTitles');
    spyOn(component.auditInfoModal, 'show');
    spyOn(_serviceInfoService, 'getAuditStatusForRetry');
    jest.spyOn(_serviceInfoService, 'getInstanceAuditStatus').mockReturnValue(of([]))

    spyOn(AuditInfoModalComponentService, 'getInstanceModelName');
    const instanceId: string = "instanceID";
    const type: string = 'VNF';
    const model = {};
    const instance = {};

    AuditInfoModalComponent.openInstanceAuditInfoModal.next({
      instanceId: instanceId,
      type: type,
      model: model,
      instance: instance
    });

    expect(component.auditInfoItems.showVidStatus).toEqual(false);
    expect(component.initializeProperties).toHaveBeenCalled();
    expect(component.setModalTitles).toHaveBeenCalled();
    expect(_serviceInfoService.getInstanceAuditStatus).toHaveBeenCalledWith(instanceId, type);
    expect(component.auditInfoModal.show).toHaveBeenCalled();
    expect(AuditInfoModalComponentService.getInstanceModelName).toHaveBeenCalledWith(model);
  });

  test('openInstanceAuditInfoModal : openModal : with job data', () => {
    spyOn(component.auditInfoModalComponentService, 'getModelInfo').and.returnValue([]);
    spyOn(component, 'initializeProperties');
    spyOn(component.auditInfoModal, 'show');
    spyOn(AuditInfoModalComponentService, 'getInstanceModelName');
    const jobData: ServiceInfoModel = new ServiceInfoModel();
    jobData.aLaCarte = true;
    AuditInfoModalComponent.openModal.next(jobData);

    expect(component.auditInfoItems.showVidStatus).toEqual(false);
    expect(component.initializeProperties).toHaveBeenCalled();
    expect(component.auditInfoModal.show).toHaveBeenCalled();
  });

  test('openInstanceAuditInfoModal : openModal : without job data', () => {
    spyOn(component.auditInfoModal, 'hide');
    const jobData: ServiceInfoModel = null;
    AuditInfoModalComponent.openModal.next(jobData);
    expect(component.auditInfoModal.hide).toHaveBeenCalled();
  });

  function getAuditInitItems() {
    return new AuditInformationItem(true, false, [], [], true, false, false, null , null)
  }
});
