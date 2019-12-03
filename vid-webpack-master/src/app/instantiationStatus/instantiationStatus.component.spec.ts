import {ComponentFixture, TestBed} from '@angular/core/testing';
import {InstantiationStatusComponent} from './instantiationStatus.component';
import {ServiceInfoService} from '../shared/server/serviceInfo/serviceInfo.service';
import {InstantiationStatusComponentService} from './instantiationStatus.component.service';
import {ContextMenuModule, ContextMenuService} from 'ngx-contextmenu';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {ScrollToModule} from '@nicky-lenaers/ngx-scroll-to';
import {ConfigurationService} from '../shared/services/configuration.service';
import {LogService} from '../shared/utils/log/log.service';
import {NgRedux} from '@angular-redux/store';
import {RouterTestingModule} from '@angular/router/testing';
import {CapitalizeAndFormatPipe} from "../shared/pipes/capitalize/capitalize-and-format.pipe";
import {AaiService} from "../shared/services/aaiService/aai.service";
import {MsoService} from "../shared/services/msoService/mso.service";
import {FeatureFlagsService} from "../shared/services/featureFlag/feature-flags.service";
import {JobStatus, ServiceAction} from "../shared/models/serviceInstanceActions";
import each from 'jest-each';
import {ServiceInfoModel} from "../shared/server/serviceInfo/serviceInfo.model";
import {TooltipModule} from 'ngx-tooltip';
import {SearchFilterPipe} from "../shared/pipes/searchFilter/search-filter.pipe";
import {ActivatedRoute} from "@angular/router";
import {FormsModule} from "@angular/forms";

class MockAppStore<T> {

  getState() {
    return {
      global: {
        flags: {
          'FLAG_1902_NEW_VIEW_EDIT': true
        }
      }
    }
  }

  dispatch() {

  }
}

class ActivatedRouteMock<T>{
  queryParams() {
    return {}
  };

  snapshot = {
    queryParams : {}
  }
}

describe('Instantiation Status Component', () => {
  let component: InstantiationStatusComponent;
  let fixture: ComponentFixture<InstantiationStatusComponent>;
  let item = new ServiceInfoModel();

  beforeAll(done => (async () => {

    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        ContextMenuModule,
        ScrollToModule.forRoot(),
        RouterTestingModule,
        TooltipModule,
        FormsModule,
      ],
      providers: [
        ServiceInfoService,
        InstantiationStatusComponentService,
        AaiService,
        MsoService,
        ContextMenuService,
        FeatureFlagsService,
        ConfigurationService,
        LogService,
        {provide: ActivatedRoute, useClass: ActivatedRouteMock},
        {provide: NgRedux, useClass: MockAppStore}
      ],
      declarations: [InstantiationStatusComponent, CapitalizeAndFormatPipe, SearchFilterPipe],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
    });
    await TestBed.compileComponents();

    fixture = TestBed.createComponent(InstantiationStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  })().then(done).catch(done.fail));


  test('component should initialize basic parameters', () => {
    component.TIMER_TIME_IN_SECONDS = 2;
    expect(component.TIMER_TIME_IN_SECONDS).toEqual(2);
    expect(component.dataIsReady).toBeFalsy();
    expect(component.lastUpdatedDate).toBeNull();
  });

  test('component constructor should call activateInterval and ngOnInit', () => {
    component.refreshData();
    expect(component.dataIsReady).toBeFalsy();
  });

  const enableDeleteItemsDataProvider = [
    ['INSTANTIATE action PENDING job status',JobStatus.PENDING , ServiceAction.INSTANTIATE],
    ['DELETE action PENDING job status',JobStatus.PENDING , ServiceAction.DELETE],
    ['UPDATE action PENDING job status',JobStatus.PENDING , ServiceAction.UPDATE],
    ['INSTANTIATE action STOPPED job status',JobStatus.STOPPED , ServiceAction.INSTANTIATE]];
  each(enableDeleteItemsDataProvider).test('delete item should enable for %s', (desc, jobStatus, action ) => {
    item.action=action;
    item.jobStatus=jobStatus;
    let isDeleteEnabled: boolean = component.isDeleteEnabled(item);
    expect(isDeleteEnabled).toBeTruthy();
  });

  const disableDeleteItemsDataProvider = [
    [ 'INSTANTIATE action COMPLETED job status', JobStatus.COMPLETED , ServiceAction.INSTANTIATE],
    [ 'INSTANTIATE action FAILED job status', JobStatus.FAILED , ServiceAction.INSTANTIATE],
    [ 'INSTANTIATE action IN_PROGRESS job status', JobStatus.IN_PROGRESS, ServiceAction.INSTANTIATE],
    [ 'INSTANTIATE action COMPLETED_WITH_ERRORS job status', JobStatus.COMPLETED_WITH_ERRORS, ServiceAction.INSTANTIATE],
    [ 'DELETE action IN_PROGRESS job status', JobStatus.IN_PROGRESS, ServiceAction.DELETE],
    [ 'DELETE action COMPLETED_WITH_ERRORS job status',JobStatus.COMPLETED_WITH_ERRORS, ServiceAction.DELETE],
    [ 'UPDATE action IN_PROGRESS job status', JobStatus.IN_PROGRESS, ServiceAction.UPDATE],
    [ 'UPDATE action COMPLETED_WITH_ERRORS job status', JobStatus.COMPLETED_WITH_ERRORS, ServiceAction.UPDATE],
    [ 'INSTANTIATE action UNKNOWN job status', "UNKNOWN", ServiceAction.INSTANTIATE]];
  each(disableDeleteItemsDataProvider).test('delete item should disable for %s', (desc,jobStatus, action ) => {
    item.action=action;
    item.jobStatus=jobStatus;
    let isDeleteEnabled: boolean = component.isDeleteEnabled(item);
    expect(isDeleteEnabled).toBeFalsy();
  });

  const enableHideItemsDataProvider = [
    ['instantiate action job status COMPLETED',JobStatus.COMPLETED, ServiceAction.INSTANTIATE ],
    ['instantiate action job status FAILED',JobStatus.FAILED, ServiceAction.INSTANTIATE  ],
    ['instantiate action job status STOPPED', JobStatus.STOPPED, ServiceAction.INSTANTIATE  ],
    ['instantiate action job status COMPLETED_WITH_ERRORS', JobStatus.COMPLETED_WITH_ERRORS, ServiceAction.INSTANTIATE  ],
    ['delete action job status COMPLETED', JobStatus.COMPLETED, ServiceAction.DELETE ],
    ['delete action job status FAILED', JobStatus.FAILED, ServiceAction.DELETE  ],
    ['delete action job status STOPPED',JobStatus.STOPPED, ServiceAction.DELETE  ],
    ['delete action job status COMPLETED_WITH_ERRORS', JobStatus.COMPLETED_WITH_ERRORS, ServiceAction.DELETE  ],
    ['update action job status COMPLETED', JobStatus.COMPLETED, ServiceAction.UPDATE ],
    ['update action job status FAILED',JobStatus.FAILED, ServiceAction.UPDATE  ],
    ['update action job status STOPPED', JobStatus.STOPPED, ServiceAction.UPDATE  ],
    ['update action job status COMPLETED_WITH_ERRORS', JobStatus.COMPLETED_WITH_ERRORS, ServiceAction.UPDATE ]];
  each(enableHideItemsDataProvider).test('hide item should be enabled for %s', (desc, jobStatus, action ) => {
    item.action=action;
    item.jobStatus=jobStatus;
    let isHideEnabled: boolean = component.isHideEnabled(item);
    expect(isHideEnabled).toBeTruthy();
  });

  const disableHideItemsDataProvider = [
    ['action instantiate job status IN_PROGRESS',JobStatus.IN_PROGRESS , ServiceAction.INSTANTIATE],
    ['action instantiate job status PAUSE',JobStatus.PAUSE, ServiceAction.INSTANTIATE],
    ['action instantiate job status PENDING', JobStatus.PENDING, ServiceAction.INSTANTIATE ],
    ['action instantiate job status UNKNOWN', "UNKNOWN", ServiceAction.INSTANTIATE],
    ['update instantiate job status IN_PROGRESS', JobStatus.IN_PROGRESS , ServiceAction.UPDATE],
    ['update instantiate job status PAUSE', JobStatus.PAUSE, ServiceAction.UPDATE],
    ['update instantiate job status PENDING', JobStatus.PENDING, ServiceAction.UPDATE ],
    ['update instantiate job status UNKNOWN',"UNKNOWN", ServiceAction.UPDATE],
    ['delete instantiate job status IN_PROGRESS',JobStatus.IN_PROGRESS , ServiceAction.DELETE],
    ['delete instantiate job status PAUSE', JobStatus.PAUSE, ServiceAction.DELETE],
    ['delete instantiate job status PENDING', JobStatus.PENDING, ServiceAction.DELETE ],
    ['delete instantiate job status UNKNOWN', "UNKNOWN", ServiceAction.DELETE]];
  each(disableHideItemsDataProvider).test('hide item should disable for %s', (desc, jobStatus, action ) => {
    item.action=action;
    item.jobStatus=jobStatus;
    let isHideEnabled: boolean = component.isHideEnabled(item);
    expect(isHideEnabled).toBeFalsy();
  });

  const enableAuditItemsDataProvider = [
    ['instantiate action UNKNOWN job status', "UNKNOWN", ServiceAction.INSTANTIATE ],
    ['delete action JobStatus IN_PROGRESS', JobStatus.IN_PROGRESS, ServiceAction.DELETE ],
    ['delete action JobStatus PAUSE', JobStatus.PAUSE, ServiceAction.DELETE ],
    ['delete action JobStatus FAILED', JobStatus.FAILED, ServiceAction.DELETE ],
    ['delete action JobStatus COMPLETED', JobStatus.COMPLETED, ServiceAction.DELETE ],
    ['delete action JobStatus COMPLETED_WITH_ERRORS', JobStatus.COMPLETED_WITH_ERRORS, ServiceAction.DELETE ],
    ['update action JobStatus IN_PROGRESS', JobStatus.IN_PROGRESS, ServiceAction.UPDATE ],
    ['update action JobStatus PAUSE', JobStatus.PAUSE, ServiceAction.UPDATE  ],
    ['update action JobStatus COMPLETED', JobStatus.COMPLETED, ServiceAction.UPDATE  ],
    ['update action JobStatus FAILED', JobStatus.FAILED, ServiceAction.UPDATE  ],
    ['update action JobStatus COMPLETED_WITH_ERRORS', JobStatus.COMPLETED_WITH_ERRORS, ServiceAction.UPDATE ]];

  each(enableAuditItemsDataProvider).test('audit item should be enabled for %s', (desc, jobStatus, action ) => {
    item.action=action;
    item.jobStatus=jobStatus;
    const isAuditEnabled: boolean = component.isAuditInfoEnabled(item);
    expect(isAuditEnabled).toBeTruthy();
  });

  const disableAuditItemsDataProvider = [
    ['Job status STOPPED action update', JobStatus.STOPPED, ServiceAction.UPDATE ],
    ['Job status PENDING action update', JobStatus.PENDING, ServiceAction.UPDATE ],
    ['Job status UNKNOWN action update', "UNKNOWN", ServiceAction.UPDATE],
    ['Job status STOPPED action delete',JobStatus.STOPPED, ServiceAction.DELETE],
    ['Job status PENDING action delete', JobStatus.PENDING, ServiceAction.DELETE ],
    ['Job status UNKNOWN action delete', "UNKNOWN", ServiceAction.DELETE]];
  each(disableAuditItemsDataProvider).test('audit item should be disabled for %s', (desc, jobStatus, action ) => {
    item.action=action;
    item.jobStatus=jobStatus;
    let isAuditEnabled: boolean = component.isAuditInfoEnabled(item);
    expect(isAuditEnabled).toBeFalsy();
  });

  const enableOpenItemsDataProvider = [
    ['action instantiate job status PAUSE', JobStatus.PAUSE, ServiceAction.INSTANTIATE ],
    ['action instantiate job status COMPLETED', JobStatus.COMPLETED, ServiceAction.INSTANTIATE  ],
    ['action instantiate job status COMPLETED_WITH_ERRORS', JobStatus.COMPLETED_WITH_ERRORS, ServiceAction.INSTANTIATE ],
    ['action delete job status PENDING', JobStatus.PENDING, ServiceAction.DELETE ],
    ['action delete job status FAILED', JobStatus.FAILED, ServiceAction.DELETE ],
    ['action delete job status COMPLETED_WITH_ERRORS', JobStatus.COMPLETED_WITH_ERRORS, ServiceAction.DELETE ],
    ['action update job status PENDING', JobStatus.PENDING, ServiceAction.UPDATE ],
    ['action update job status PAUSE', JobStatus.PAUSE, ServiceAction.UPDATE ],
    ['action update job status COMPLETED', JobStatus.COMPLETED, ServiceAction.UPDATE  ],
    ['action update job status FAILED', JobStatus.FAILED, ServiceAction.UPDATE  ],
    ['action update job status COMPLETED_WITH_ERRORS', JobStatus.COMPLETED_WITH_ERRORS, ServiceAction.UPDATE ]];
  each(enableOpenItemsDataProvider).test('open item should be enabled for %s', (desc, jobStatus, action ) => {
    item.action=action;
    item.jobStatus=jobStatus;
    let isOpenEnabled: boolean = component.isOpenEnabled(item);
    expect(isOpenEnabled).toBeTruthy();
  });

  const disableOpenItemsDataProvider = [
    ['action instantiate job status STOPPED', JobStatus.STOPPED, ServiceAction.INSTANTIATE],
    ['action instantiate job status FAILED', JobStatus.FAILED, ServiceAction.INSTANTIATE],
    ['action instantiate job status UNKNOWN', "UNKNOWN", ServiceAction.INSTANTIATE],
    ['action update job status STOPPED', JobStatus.STOPPED, ServiceAction.UPDATE ],
    ['action update job status IN_PROGRESS', JobStatus.IN_PROGRESS, ServiceAction.UPDATE ],
    ['action update job status UNKNOWN', "UNKNOWN", ServiceAction.UPDATE],
    ['action delete job status COMPLETED', JobStatus.COMPLETED, ServiceAction.DELETE],
    ['action delete job status PAUSE', JobStatus.PAUSE, ServiceAction.DELETE],
    ['action delete job status IN_PROGRESS', JobStatus.IN_PROGRESS, ServiceAction.DELETE ],
    ['action delete job status UNKNOWN',"UNKNOWN", ServiceAction.DELETE]];
  each(disableOpenItemsDataProvider).test('open item should be disabled for %s', (desc, jobStatus, action ) => {
    item.action=action;
    item.jobStatus=jobStatus;
    let isOpenEnabled: boolean = component.isOpenEnabled(item);
    expect(isOpenEnabled).toBeFalsy();
  });

});
