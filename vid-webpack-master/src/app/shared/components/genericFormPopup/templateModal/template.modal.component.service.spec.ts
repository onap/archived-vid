import {getTestBed, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {TemplateModalComponentService} from "./template.modal.component.service";
import {AaiService} from "../../../services/aaiService/aai.service";
import {ActivatedRoute} from "@angular/router";
import {IframeService} from "../../../utils/iframe.service";
import {NgRedux} from "@angular-redux/store";
import {FeatureFlagsService} from "../../../services/featureFlag/feature-flags.service";
import {TemplateTableRowModel} from "./template-table-row.model";


class ActivatedRouteMock<T> {
  queryParams() {
    return {
      serviceModelId: '6e59c5de-f052-46fa-aa7e-2fca9d674c44'
    }
  }
}

class MockAppStore {

}

describe('Generic Form popup Service', () => {
  let injector;
  let service: TemplateModalComponentService;
  let httpMock: HttpTestingController;
  let _aaiService: AaiService;
  let _activatedRoute: ActivatedRoute;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TemplateModalComponentService,
        IframeService,
        AaiService,
        FeatureFlagsService,
        {provide: ActivatedRoute, useClass: ActivatedRouteMock},
        {provide: NgRedux, useClass: MockAppStore}
      ]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(TemplateModalComponentService);
    httpMock = injector.get(HttpTestingController);
    _aaiService = injector.get(AaiService);
    _activatedRoute = injector.get(ActivatedRoute);

  })().then(done).catch(done.fail));


  test('service should be defined', () => {
    expect(service).toBeDefined();
  });


  test('convertResponseToUI - should return table data', () => {
    const jobs = [{
      "id": 5,
      "created": 1524995555000,
      "modified": 1524995556000,
      "action": "INSTANTIATE",
      "createdId": null,
      "modifiedId": null,
      "rowNum": null,
      "auditUserId": null,
      "auditTrail": null,
      "jobId": "9f88fdb5-bb47-4bf3-8c5f-98f1ad0ec87c",
      "templateId": "ce4ec177-cfc8-483e-8a2c-b7aea53fd740",
      "userId": "16807000",
      "msoRequestId": "c0011670-0e1a-4b74-945d-8bf5aede1d91",
      "requestId": null,
      "jobStatus": "FAILED",
      "statusModifiedDate": 1524995555000,
      "hidden": false,
      "pause": false,
      "owningEntityId": "aaa1",
      "owningEntityName": "aaa1",
      "project": "WATKINS",
      "aicZoneId": "BAN1",
      "aicZoneName": "VSDKYUTP-BAN1",
      "tenantId": "1178612d2b394be4834ad77f567c0af2",
      "tenantName": "AIN Web Tool-15-D-SSPtestcustome",
      "regionId": "hvf6",
      "regionName": null,
      "serviceType": "TYLER SILVIA",
      "subscriberName": "e433710f-9217-458d-a79d-1c7aff376d89",
      "serviceInstanceId": null,
      "serviceInstanceName": 'serviceInstanceName',
      "serviceModelId": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
      "serviceModelName": "ComplexService",
      "serviceModelVersion": "1.0",
      "createdBulkDate": 1524995555000,
      "isRetryEnabled": false
    }];
    const tableRows: TemplateTableRowModel[] = service.convertResponseToUI(jobs);
    expect(tableRows).toHaveLength(1);
    expect(tableRows[0].userId).toEqual('16807000');
    expect(tableRows[0].createDate).toEqual('2018-04-29 12:52:35');
    expect(tableRows[0].instanceName).toEqual('serviceInstanceName');
    expect(tableRows[0].instantiationStatus).toEqual('FAILED');
    expect(tableRows[0].region).toEqual('hvf6 (AAA1)');
    expect(tableRows[0].tenant).toEqual('AIN Web Tool-15-D-SSPtestcustome');
    expect(tableRows[0].aicZone).toEqual('VSDKYUTP-BAN1');
    expect(tableRows[0].jobId).toEqual('9f88fdb5-bb47-4bf3-8c5f-98f1ad0ec87c');
  });


  test('getCloudOwner should remove "-att" from owningEntityName : "att-owner', () => {
    let result: TemplateTableRowModel = new TemplateTableRowModel({
      owningEntityName: 'att-owner',
      regionId: 'regionId'
    });
    expect(result.region).toEqual('regionId (OWNER)');
  });

  test('getCloudOwner should not return owningEntityName if not exist', () => {
    let result: TemplateTableRowModel = new TemplateTableRowModel({owningEntityName: null, regionId: 'regionId'});
    expect(result.region).toEqual('regionId');
  });

  test('getInstanceName should  return instance name id exist if not exist', () => {
    let result: TemplateTableRowModel = new TemplateTableRowModel({serviceInstanceName: 'instanceName'});
    expect(result.instanceName).toEqual('instanceName');
  });

  test('getInstanceName should return <Automatically generated> if instance name not exist', () => {
    let result: TemplateTableRowModel = new TemplateTableRowModel({});
    expect(result.instanceName).toEqual('<Automatically generated>');
  });

});
