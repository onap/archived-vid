import {getTestBed, TestBed} from '@angular/core/testing';
import {COMPLETED_WITH_ERRORS, INPROGRESS, InstantiationStatusComponentService, PAUSE, PENDING, ServiceStatus, STOPPED, SUCCESS_CIRCLE, UNKNOWN, X_O} from './instantiationStatus.component.service';
import {ServiceInfoModel} from '../shared/server/serviceInfo/serviceInfo.model';
import {AaiService} from "../shared/services/aaiService/aai.service";
import {MsoService} from "../shared/services/msoService/mso.service";
import {NgRedux} from "@angular-redux/store";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {FeatureFlagsService} from "../shared/services/featureFlag/feature-flags.service";
import {DrawingBoardModes} from "../drawingBoard/service-planning/drawing-board.modes";
import {RouterTestingModule} from "@angular/router/testing";
import {of} from "rxjs";
import {UrlTree} from "@angular/router";

class MockAppStore<T> {

  getState() {
    return {
      global: {
        flags: {
          'FLAG_1902_NEW_VIEW_EDIT': true,

        }
      }
    }
  }

  dispatch() {

  }
}
describe('Instantiation Status Service', () => {
  let injector;
  let aaiService: AaiService;
  let msoService: MsoService;
  let service: InstantiationStatusComponentService;


  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
      ],
      providers: [
        InstantiationStatusComponentService,
        AaiService,
        MsoService,
        FeatureFlagsService,
        {provide: NgRedux, useClass: MockAppStore}]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    aaiService = injector.get(AaiService);
    msoService = injector.get(MsoService);
    service = injector.get(InstantiationStatusComponentService);

  })().then(done).catch(done.fail));

  test('generateServiceInfoDataMapping should return mapping of arrays', () => {
    let data : ServiceInfoModel[] = generateServiceInfoData();
    let result = service.generateServiceInfoDataMapping(data);

    expect(result['1']).toBeDefined();
    expect(result['2']).toBeDefined();
    expect(result['3']).toBeDefined();

    expect(result['1'].length).toEqual(2);
    expect(result['2'].length).toEqual(2);
    expect(result['3'].length).toEqual(1);
  });

  test('generateServiceInfoDataMapping if array is empty  should return empty object', () => {
    let result = service.generateServiceInfoDataMapping([]);

    expect(result['1']).not.toBeDefined();
    expect(result['2']).not.toBeDefined();
    expect(result['3']).not.toBeDefined();
  });

  test('convertObjectToArray', () => {

    jest.spyOn(service, 'convertObjectToArray').mockReturnValue(
      of([])
    );

    let data : ServiceInfoModel[] = generateServiceInfoData();
    service.convertObjectToArray(data).subscribe((result) => {
      expect(result).toBeDefined();
    });
  });

  describe('navigations tests:', () => {

    const item = {
      serviceModelId: '28aeb8f6-5620-4148-8bfb-a5fb406f0309',
      serviceInstanceId: 'myInstanceId',
      serviceType: 'myService',
      subscriberId: 'mySubscriber',
      jobId: 'aJobId'
    };

    test('click on "Open" button should open new view edit', () => {
      let params: UrlTree = service.getNewViewEditUrlTree(<any>item, DrawingBoardModes.VIEW);
      expect(params.toString().startsWith('/servicePlanning/VIEW')).toBeTruthy();
      expect(params.queryParams).toEqual(
        {
          serviceModelId: item.serviceModelId,
          serviceInstanceId: item.serviceInstanceId,
          serviceType: item.serviceType,
          subscriberId: item.subscriberId,
          jobId: item.jobId
        });
    });

    test('build the View Edit url', () => {

      let serviceModelUrl: string = '/servicePlanning/EDIT?serviceModelId=28aeb8f6-5620-4148-8bfb-a5fb406f0309' +
        '&serviceInstanceId=myInstanceId&serviceType=myService&subscriberId=mySubscriber&jobId=aJobId';
      let prefix: string = '../../serviceModels.htm#';
      let tree: UrlTree = service.getNewViewEditUrlTree(<any>item, DrawingBoardModes.EDIT);
      let result = service.getViewEditUrl(tree);
      expect(result).toEqual(prefix + serviceModelUrl);
    });

    test('recreate url shall contains mode RECREATE and only jobId and serviceModelId', () =>{
      let params: UrlTree = service.getRecreateUrlTree(<any>item);
      expect(params.toString().startsWith('/servicePlanning/RECREATE')).toBeTruthy();
      expect(params.queryParams).toEqual(
        {
          serviceModelId: item.serviceModelId,
          jobId: item.jobId
        });
    });
  });

  for (let [status, tooltip] of Object.entries({
    'pending': 'Pending: The action required will be sent as soon as possible.',
    'IN_PROGRESS': 'In-progress: the service is in process of the action required.',
    'PAUSED': 'Paused: Service has paused and waiting for your action.\n Select actions from the menu to the right.',
    'FAILED': 'Failed: All planned actions have failed.',
    'COMPLETED': 'Completed successfully: Service is successfully instantiated, updated or deleted.',
    'STOPPED': 'Stopped: Due to previous failure, will not be instantiated.',
    'StOpPeD': 'Stopped: Due to previous failure, will not be instantiated.',
    'COMPLETED_WITH_ERRORS': 'Completed with errors: some of the planned actions where successfully committed while other have not.\n Open the service to check it out.',
    'UNEXPECTED_RANDOM_STATUS': 'Unexpected status: "UNEXPECTED_RANDOM_STATUS"',
  })) {

    test(`getStatusTooltip should return status popover: status=${status}`, () => {
      expect(service.getStatus(status).tooltip).toEqual(tooltip);
    });

  }

  test(`service.getStatus should handle undefined status`, () => {
    const statusResult = service.getStatus(undefined);
    expect(statusResult.tooltip).toEqual('Unexpected status: "undefined"');
    expect(statusResult.iconClassName).toEqual(UNKNOWN);
  });

  test('getStatusTooltip should return correct icon per job status', () => {
    let result : ServiceStatus  = service.getStatus('pending');
    expect(result.iconClassName).toEqual(PENDING);

    result = service.getStatus('IN_PROGRESS');
    expect(result.iconClassName).toEqual(INPROGRESS);

    result = service.getStatus('PAUSED');
    expect(result.iconClassName).toEqual(PAUSE);

    result = service.getStatus('FAILED');
    expect(result.iconClassName).toEqual(X_O);

    result = service.getStatus('COMPLETED');
    expect(result.iconClassName).toEqual(SUCCESS_CIRCLE);

    result = service.getStatus('STOPPED');
    expect(result.iconClassName).toEqual(STOPPED);

    result = service.getStatus('COMPLETED_WITH_ERRORS');
    expect(result.iconClassName).toEqual(COMPLETED_WITH_ERRORS);

    result = service.getStatus('UNEXPECTED_RANDOM_STATUS');
    expect(result.iconClassName).toEqual(UNKNOWN);

    result = service.getStatus(undefined);
    expect(result.iconClassName).toEqual(UNKNOWN);
  });

  function generateServiceInfoData(){
    return JSON.parse(JSON.stringify([
      {
        "created": 1519956533000,
        "modified": 1521727738000,
        "createdId": null,
        "modifiedId": null,
        "rowNum": null,
        "auditUserId": null,
        "auditTrail": null,
        "jobId": "6748648484",
        "userId": "2222",
        "jobStatus": "FAILED",
        "pause": false,
        "owningEntityId": "1234",
        "owningEntityName": null,
        "project": null,
        "aicZoneId": null,
        "aicZoneName": null,
        "tenantId": null,
        "tenantName": null,
        "regionId": null,
        "regionName": null,
        "serviceType": null,
        "subscriberName": null,
        "serviceInstanceId": "1",
        "serviceInstanceName": null,
        "serviceModelId": null,
        "serviceModelName": null,
        "serviceModelVersion": null,
        "createdBulkDate": 1519956533000,
        "statusModifiedDate": 1520042933000,
        "templateId": "1",
        "hidden": false
      },
      {
        "created": 1519956533000,
        "modified": 1521727738000,
        "createdId": null,
        "modifiedId": null,
        "rowNum": null,
        "auditUserId": null,
        "auditTrail": null,
        "jobId": "6748648484",
        "userId": "2222",
        "jobStatus": "FAILED",
        "pause": false,
        "owningEntityId": "1234",
        "owningEntityName": null,
        "project": null,
        "aicZoneId": null,
        "aicZoneName": null,
        "tenantId": null,
        "tenantName": null,
        "regionId": null,
        "regionName": null,
        "serviceType": null,
        "subscriberName": null,
        "serviceInstanceId": "1",
        "serviceInstanceName": null,
        "serviceModelId": null,
        "serviceModelName": null,
        "serviceModelVersion": null,
        "createdBulkDate": 1519956533000,
        "statusModifiedDate": 1520042933000,
        "templateId": "1",
        "hidden": false
      },
      {
        "created": 1519956533000,
        "modified": 1521727738000,
        "createdId": null,
        "modifiedId": null,
        "rowNum": null,
        "auditUserId": null,
        "auditTrail": null,
        "jobId": "6748648484",
        "userId": "2222",
        "jobStatus": "FAILED",
        "pause": false,
        "owningEntityId": "1234",
        "owningEntityName": null,
        "project": null,
        "aicZoneId": null,
        "aicZoneName": null,
        "tenantId": null,
        "tenantName": null,
        "regionId": null,
        "regionName": null,
        "serviceType": null,
        "subscriberName": null,
        "serviceInstanceId": "2",
        "serviceInstanceName": null,
        "serviceModelId": null,
        "serviceModelName": null,
        "serviceModelVersion": null,
        "createdBulkDate": 1519956533000,
        "statusModifiedDate": 1520042933000,
        "templateId": "2",
        "hidden": false
      },
      {
        "created": 1519956533000,
        "modified": 1521727738000,
        "createdId": null,
        "modifiedId": null,
        "rowNum": null,
        "auditUserId": null,
        "auditTrail": null,
        "jobId": "6748648484",
        "userId": "2222",
        "jobStatus": "FAILED",
        "pause": false,
        "owningEntityId": "1234",
        "owningEntityName": null,
        "project": null,
        "aicZoneId": null,
        "aicZoneName": null,
        "tenantId": null,
        "tenantName": null,
        "regionId": null,
        "regionName": null,
        "serviceType": null,
        "subscriberName": null,
        "serviceInstanceId": "2",
        "serviceInstanceName": null,
        "serviceModelId": null,
        "serviceModelName": null,
        "serviceModelVersion": null,
        "createdBulkDate": 1519956533000,
        "statusModifiedDate": 1520042933000,
        "templateId": "2",
        "hidden": false
      },
      {
        "created": 1519956533000,
        "modified": 1521727738000,
        "createdId": null,
        "modifiedId": null,
        "rowNum": null,
        "auditUserId": null,
        "auditTrail": null,
        "jobId": "6748648484",
        "userId": "2222",
        "jobStatus": "FAILED",
        "pause": false,
        "owningEntityId": "1234",
        "owningEntityName": null,
        "project": null,
        "aicZoneId": null,
        "aicZoneName": null,
        "tenantId": null,
        "tenantName": null,
        "regionId": null,
        "regionName": null,
        "serviceType": null,
        "subscriberName": null,
        "serviceInstanceId": "3",
        "serviceInstanceName": null,
        "serviceModelId": null,
        "serviceModelName": null,
        "serviceModelVersion": null,
        "createdBulkDate": 1519956533000,
        "statusModifiedDate": 1520042933000,
        "templateId": "3",
        "hidden": false
      }
    ]));
  }

});
