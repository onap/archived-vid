import {getTestBed, TestBed} from '@angular/core/testing';
import {
  COMPLETED_WITH_ERRORS,
  INPROGRESS,
  InstantiationStatusComponentService,
  PAUSE,
  PENDING,
  ServiceStatus,
  STOPED,
  SUCCESS_CIRCLE,
  X_O
} from './instantiationStatus.component.service';
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

  test('click on "Open" button should open new view edit' , ()=>{
    const item = {
      serviceModelId : 'serviceModelId',
      serviceInstanceId : 'serviceInstanceId',
      serviceType : 'serviceType',
      subscriberId : 'subscriberId'
    };
    let params:UrlTree = service.getNewViewEditUrlTree(item, DrawingBoardModes.VIEW);
    expect(params.toString().startsWith('/servicePlanning/VIEW')).toBeTruthy();
    expect(params.queryParams).toEqual(
      {
        serviceModelId: item.serviceModelId,
        serviceInstanceId: item.serviceInstanceId,
        serviceType : item.serviceType,
        subscriberId : item.subscriberId
      });
  });

  test('build the View Edit url' , ()=>{
    const item = {
      serviceModelId : '28aeb8f6-5620-4148-8bfb-a5fb406f0309',
    };
    let serviceModelUrl: string = '/servicePlanning/EDIT?serviceModelId=28aeb8f6-5620-4148-8bfb-a5fb406f0309';
    let suffix:string = '../../serviceModels.htm#';
    let tree:UrlTree = service.getNewViewEditUrlTree(item, DrawingBoardModes.EDIT);
    let result = service.getViewEditUrl(tree);
    expect (suffix + serviceModelUrl).toEqual(result);
  });

  test('getStatusTooltip should return status popover', () => {
    let result : ServiceStatus  = service.getStatus('pending');
    expect(result.tooltip).toEqual('Pending: The action required will be sent as soon as possible.');

    result = service.getStatus('IN_PROGRESS');
    expect(result.tooltip).toEqual('In-progress: the service is in process of the action required.');

    result = service.getStatus('PAUSED');
    expect(result.tooltip).toEqual('Paused: Service has paused and waiting for your action.\n Select actions from the menu to the right.');

    result = service.getStatus('FAILED');
    expect(result.tooltip).toEqual('Failed: All planned actions have failed.');

    result = service.getStatus('COMPLETED');
    expect(result.tooltip).toEqual('Completed successfully: Service is successfully instantiated, updated or deleted.');

    result = service.getStatus('STOPPED');
    expect(result.tooltip).toEqual('Stopped: Due to previous failure, will not be instantiated.');

    result = service.getStatus('COMPLETED_WITH_ERRORS');
    expect(result.tooltip).toEqual('Completed with errors: some of the planned actions where successfully committed while other have not.\n Open the service to check it out.');
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
    expect(result.iconClassName).toEqual(STOPED);

    result = service.getStatus('COMPLETED_WITH_ERRORS');
    expect(result.iconClassName).toEqual(COMPLETED_WITH_ERRORS);
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
