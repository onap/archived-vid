import { getTestBed, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ServiceInstanceDetailsService } from './service-instance-details.service';
import { NgRedux } from '@angular-redux/store';

export class MockAppStore<T> {}

describe('Service instance details service', () => {
  let injector;
  let service: ServiceInstanceDetailsService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ServiceInstanceDetailsService,
        {provide: NgRedux, useClass: MockAppStore}]
    });

    injector = getTestBed();
    service = injector.get(ServiceInstanceDetailsService);
    httpMock = injector.get(HttpTestingController);
  });
});


