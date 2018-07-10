import {TestBed, inject, getTestBed} from '@angular/core/testing';

import { HealthStatusService } from './health-status.service';
import {Constants} from "../../utils/constants";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {ExternalComponentStatus} from "../../models/externalComponentStatus";

describe('HealthStatusService', () => {

  let injector;
  let service: HealthStatusService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [HealthStatusService]
    });

    injector = getTestBed();
    service = injector.get(HealthStatusService);
    httpMock = injector.get(HttpTestingController);
  });

  describe('#getProbe', () =>{
    it('when call probe, there is http GET with right url', () => {

      service.getProbe().subscribe((result: Array<ExternalComponentStatus>)=>{
        expect(result[0].component).toEqual("AAI");
        expect(result[0].available).toBeTruthy();
        expect(result[0].metadata).toEqual({ myKey: 'myValue' });

        expect(result[1].component).toEqual("MSO");
        expect(result[1].available).toBeFalsy();
        expect(result[1].metadata).toEqual({otherKey: 123});
      });

      const req = httpMock.expectOne(Constants.Path.SERVICES_PROBE_PATH);
      expect(req.request.method).toEqual('GET');
      req.flush([
        {
          "component": "AAI",
          "available": true,
          "metadata": {
            "myKey": "myValue"
          }
        },
        {
          "component": "MSO",
          "available": false,
          "metadata": {
            "otherKey": 123
          }
        },
      ]);
    });

  });

});
