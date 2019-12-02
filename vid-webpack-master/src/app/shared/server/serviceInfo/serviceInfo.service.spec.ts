import {getTestBed, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {ServiceInfoService} from './serviceInfo.service';
import {ServiceInfoModel} from './serviceInfo.model';
import {Constants} from "../../utils/constants";

describe('Service Info Service', () => {
  let injector;
  let service: ServiceInfoService;
  let httpMock: HttpTestingController;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ServiceInfoService]
    });
    await TestBed.compileComponents();


    injector = getTestBed();
    service = injector.get(ServiceInfoService);
    httpMock = injector.get(HttpTestingController);

  })().then(done).catch(done.fail));

  describe('#getMacroJobAuditStatus', ()=> {
    test('should return Observable<Object[]>', ()=>{
      let job: ServiceInfoModel = new ServiceInfoModel();
      job.jobId = '111';

      service.getJobAuditStatus(job).subscribe();
      const req = httpMock.expectOne(Constants.Path.SERVICES_JOB_INFO_PATH + Constants.Path.SERVICES_JOB_AUDIT_PATH + '/' + job.jobId + '?source=VID');
      const req2 = httpMock.expectOne(Constants.Path.SERVICES_JOB_INFO_PATH + Constants.Path.SERVICES_JOB_AUDIT_PATH + '/' + job.jobId + '?source=MSO');

      expect(req.request.method).toBe('GET');
      expect(req2.request.method).toBe('GET');
    });
  });

  describe('#getServicesJobInfo', ()=> {
    test('should call without serviceModelId', ()=>{
      let job: ServiceInfoModel = new ServiceInfoModel();

      service.getServicesJobInfo().subscribe();
      const req = httpMock.expectOne(Constants.Path.SERVICES_JOB_INFO_PATH);

      expect(req.request.method).toBe('GET');
    });

    test('should call with serviceModelId', ()=>{
      let job: ServiceInfoModel = new ServiceInfoModel();

      service.getServicesJobInfo(true, "123").subscribe();
      const req = httpMock.expectOne(`${Constants.Path.SERVICES_JOB_INFO_PATH}?${Constants.Path.SERVICE_MODEL_ID}=123`);
      expect(req.request.method).toBe('GET');
    });
  });

  describe('#getALaCarteJobAuditStatus Without params', ()=> {
    test('should return Observable<Object[]>', ()=>{
      let job: ServiceInfoModel = new ServiceInfoModel();
      job.aLaCarte = true;
      job.jobId = '111';

      service.getJobAuditStatus(job).subscribe();
      const req = httpMock.expectOne(Constants.Path.SERVICES_JOB_INFO_PATH + Constants.Path.SERVICES_JOB_AUDIT_PATH + '/' + job.jobId + '?source=VID');
      const req2 = httpMock.expectOne(Constants.Path.SERVICES_JOB_INFO_PATH + Constants.Path.SERVICES_JOB_AUDIT_PATH + '/' + job.jobId + '/mso');

      expect(req.request.method).toEqual('GET');
      expect(req2.request.method).toEqual('GET');
    });
  });

  describe('#getALaCarteJobAuditStatus With ServiceInstanceId', ()=> {
    test('should return Observable<Object[]>', ()=>{
      let job: ServiceInfoModel = new ServiceInfoModel();
      job.aLaCarte = true;
      job.jobId = '111';
      job.serviceInstanceId = '222';

      service.getJobAuditStatus(job).subscribe();
      const req = httpMock.expectOne(Constants.Path.SERVICES_JOB_INFO_PATH + Constants.Path.SERVICES_JOB_AUDIT_PATH + '/' + job.jobId + '?source=VID');
      const req2 = httpMock.expectOne(Constants.Path.SERVICES_JOB_INFO_PATH + Constants.Path.SERVICES_JOB_AUDIT_PATH + '/' + job.jobId + '/mso?serviceInstanceId=' + job.serviceInstanceId);

      expect(req.request.method).toEqual('GET');
      expect(req2.request.method).toEqual('GET');
    });
  });

  describe('#getALaCarteJobAuditStatus With RequestId', ()=> {
    test('should return Observable<Object[]>', ()=>{
      let job: ServiceInfoModel = new ServiceInfoModel();
      job.aLaCarte = true;
      job.jobId = '111';
      job.requestId = '333';

      service.getJobAuditStatus(job).subscribe();
      const req = httpMock.expectOne(Constants.Path.SERVICES_JOB_INFO_PATH + Constants.Path.SERVICES_JOB_AUDIT_PATH + '/' + job.jobId + '?source=VID');
      const req2 = httpMock.expectOne(Constants.Path.SERVICES_JOB_INFO_PATH + Constants.Path.SERVICES_JOB_AUDIT_PATH + '/' + job.jobId + '/mso?requestId=' + job.requestId);

      expect(req.request.method).toEqual('GET');
      expect(req2.request.method).toEqual('GET');
    });
  });

  function generateServiceInfoData(){
    return <ServiceInfoModel[]>JSON.parse(JSON.stringify(
      [{
        "created": 1519956533000,
        "modified": 1521727738000,
        "action": "INSTANTIATE",
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
        "hidden": false
      },
      {
        "created": 1519956533000,
        "modified": 1521727738000,
        "action": "INSTANTIATE",
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
        "hidden": false
      },
      {
        "created": 1519956533000,
        "modified": 1521727738000,
        "action": "INSTANTIATE",
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
        "hidden": false
      },
      {
        "created": 1519956533000,
        "modified": 1521727738000,
        "action": "INSTANTIATE",
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
        "hidden": false
      },
      {
        "created": 1519956533000,
        "modified": 1521727738000,
        "action": "INSTANTIATE",
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
        "hidden": false
      }]
    ));
  }
});
