import {getTestBed, TestBed} from '@angular/core/testing';
import {
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
import { Observable } from 'rxjs/Rx';

describe('Instantiation Status Service', () => {
  let injector;
  let service: InstantiationStatusComponentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [],
      providers: [InstantiationStatusComponentService]
    });

    injector = getTestBed();
    service = injector.get(InstantiationStatusComponentService);
  });

  it('generateServiceInfoDataMapping should return mapping of arrays', (done: DoneFn) => {
    let data : Array<ServiceInfoModel> = generateServiceInfoData();
    let result = service.generateServiceInfoDataMapping(data);

    expect(result['1']).toBeDefined();
    expect(result['2']).toBeDefined();
    expect(result['3']).toBeDefined();

    expect(result['1'].length).toEqual(2);
    expect(result['2'].length).toEqual(2);
    expect(result['3'].length).toEqual(1);
    done();
  });

  it('generateServiceInfoDataMapping if array is empty  should return empty object', (done: DoneFn) => {
    let result = service.generateServiceInfoDataMapping([]);

    expect(result['1']).not.toBeDefined();
    expect(result['2']).not.toBeDefined();
    expect(result['3']).not.toBeDefined();
    done();
  });

  it('convertObjectToArray', (done: DoneFn) => {

    spyOn(service, 'convertObjectToArray').and.returnValue(
      Observable.of([])
    );

    let data : Array<ServiceInfoModel> = generateServiceInfoData();
    service.convertObjectToArray(data).subscribe((result) => {
      expect(result).toBeDefined();
      done();
    });
  });

  it('getStatusTooltip should return status popover', (done: DoneFn) => {
    let result : ServiceStatus  = service.getStatus('pending');
    expect(result.tooltip).toEqual('Pending: The service will automatically be sent for instantiation as soon as possible.');

    result = service.getStatus('IN_PROGRESS');
    expect(result.tooltip).toEqual('In-progress: the service is in process of instantiation.');

    result = service.getStatus('PAUSED');
    expect(result.tooltip).toEqual('Paused: Service has paused and waiting for your action.\n Select actions from the menu to the right.');

    result = service.getStatus('FAILED');
    expect(result.tooltip).toEqual('Failed: Service instantiation has failed, load the service to see the error returned.');

    result = service.getStatus('COMPLETED');
    expect(result.tooltip).toEqual('Completed successfully: Service is successfully instantiated.');

    result = service.getStatus('STOPPED');
    expect(result.tooltip).toEqual('Stopped: Due to previous failure, will not be instantiated.');
    done();
  });

  it('getStatusTooltip should return correct icon per job status', (done: DoneFn) => {
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
    done();
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
