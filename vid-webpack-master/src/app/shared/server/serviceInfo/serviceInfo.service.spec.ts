import {getTestBed, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {ServiceInfoService} from './serviceInfo.service';
import {ServiceInfoModel} from './serviceInfo.model';
import {Constants} from "../../utils/constants";

describe('Service Info Service', () => {
  let injector;
  let service: ServiceInfoService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ServiceInfoService]
    });

    injector = getTestBed();
    service = injector.get(ServiceInfoService);
    httpMock = injector.get(HttpTestingController);
  });

  describe('#getServicesJobInfo', () => {
    it('should return an Observable<ServiceInfoModel[]>', () => {
      const dummyServiceInfo: ServiceInfoModel[] = generateServiceInfoData();

      service.getServicesJobInfo(true).subscribe((serviceInfo:Array<ServiceInfoModel>) => {
        expect(serviceInfo).toEqual(dummyServiceInfo);
      });
    });


  });

  describe('#deleteJob', () =>{
    it('delete job', () => {
      const jobId : string = "1111";

      service.deleteJob(jobId).subscribe();

      const req = httpMock.expectOne(Constants.Path.SERVICES_JOB_INFO_PATH + '/job/' + jobId);
      expect(req.request.method).toEqual('DELETE');

    });
  });

  describe('#hideJob', () =>{
    it('when call hide job, there is http POST with right url', () => {
      const jobId : string = "3";

      service.hideJob(jobId).subscribe();

      const req = httpMock.expectOne(Constants.Path.SERVICES_JOB_INFO_PATH + '/hide/' + jobId);
      expect(req.request.method).toEqual('POST');
    });
  });

  describe('#getJobAuditStatus', ()=> {
    it('should return Observable<Object[]>', ()=>{
      const jobId : string = '111';

      service.getJobAuditStatus(jobId).subscribe();
      const req = httpMock.expectOne(Constants.Path.SERVICES_JOB_INFO_PATH + Constants.Path.SERVICES_JOB_AUDIT_PATH + '/' + jobId + '?source=VID');
      const req2 = httpMock.expectOne(Constants.Path.SERVICES_JOB_INFO_PATH + Constants.Path.SERVICES_JOB_AUDIT_PATH + '/' + jobId + '?source=MSO');

      expect(req.request.method).toEqual('GET');
      expect(req2.request.method).toEqual('GET');
    });
  });

  function generateServiceInfoData(){
    return <ServiceInfoModel[]>JSON.parse(JSON.stringify(
      [{
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
        "hidden": false
      }]
    ));
  }
});
