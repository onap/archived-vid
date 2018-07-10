import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import {InstantiationStatusComponent} from './instantiationStatus.component';
import {ServiceInfoService} from '../shared/server/serviceInfo/serviceInfo.service';
import {InstantiationStatusComponentService} from './instantiationStatus.component.service';
import { ContextMenuModule, ContextMenuService } from 'ngx-contextmenu';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ScrollToModule } from '@nicky-lenaers/ngx-scroll-to';
import { ConfigurationService } from '../services/configuration.service';
import { LogService } from '../shared/utils/log/log.service';

describe('Instantiation Status Component', () => {
  let component: InstantiationStatusComponent;
  let fixture: ComponentFixture<InstantiationStatusComponent>;
  let enableDeleteItems = [
    { jobStatus:"PENDING" },
    { jobStatus:"STOPPED" }];
  let disableDeleteItems = [
    { jobStatus:"COMPLETED" },
    { jobStatus:"FAILED" },
    {jobStatus:"IN_PROGRESS"},
    {jobStatus:"UnknownOne"}];


  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ContextMenuModule, ScrollToModule.forRoot()],
      providers: [ServiceInfoService, InstantiationStatusComponentService, ContextMenuService, ConfigurationService, LogService],
      declarations: [InstantiationStatusComponent],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InstantiationStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('component should initialize basic parameters', (done: DoneFn) => {
    component.TIMER_TIME_IN_SECONDS = 2;
    expect(component.TIMER_TIME_IN_SECONDS).toEqual(2);
    expect(component.dataIsReady).toBeFalsy();
    expect(component.lastUpdatedDate).toBeNull();
    done();
  });

  it('component constructor should call activateInterval and ngOnInit', (done: DoneFn) => {
    component.refreshData();
    expect(component.dataIsReady).toBeFalsy();
    done();
  });

  it('stopped and pending status isDeleteEnabled button should be enabled, not allowed delete statuses isDeleteEnabled button should be disabled', (done: DoneFn) => {
    enableDeleteItems.forEach((item) => {
      let isDeleteEnabled: boolean = component.isDeleteEnabled(item);
      expect(isDeleteEnabled).toBeTruthy();
    });

    disableDeleteItems.forEach((item) => {
      let isDeleteEnabled: boolean = component.isDeleteEnabled(item);
      expect(isDeleteEnabled).toBeFalsy();
    });
    done();
  });

  it('[COMPLETED, FAILED, STOPPED]  status isHideEnable button should be enabled, [IN_PROGRESS, PAUSE, PENDING]  status isHideEnable button should be disabled', (done: DoneFn) => {
    const enableHideItems = [
      { jobStatus:"COMPLETED" },
      { jobStatus:"FAILED" },
      { jobStatus:"STOPPED" }];
    enableHideItems.forEach((item) => {
      let isDeleteEnabled: boolean = component.isHideEnabled(item);
      expect(isDeleteEnabled).toBeTruthy();
    });

    const disableHideItems = [
      { jobStatus:"IN_PROGRESS" },
      { jobStatus:"PAUSE" },
      { jobStatus:"PENDING" },
      { jobStatus:"NOT_MATTER"}];
    disableHideItems.forEach((item) => {
      let isDeleteEnabled: boolean = component.isHideEnabled(item);
      expect(isDeleteEnabled).toBeFalsy();
    });
    done();
  });
});
