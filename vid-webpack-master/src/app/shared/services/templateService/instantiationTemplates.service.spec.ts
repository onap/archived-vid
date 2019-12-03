import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {InstantiationTemplatesService} from "./instantiationTemplates.service";
import {mock} from "ts-mockito";
import {NgRedux} from "@angular-redux/store";
import {getTestBed, TestBed} from "@angular/core/testing";
import {ServiceInstance} from "../../models/serviceInstance";
import {Constants} from "../../utils/constants";

class MockAppStore<T> {
  dispatch() {}
  getState() {}
}

describe("TemplateService", ()=>{
  let injector;
  let httpMock: HttpTestingController;
  let templateService: InstantiationTemplatesService;

  beforeAll( done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        InstantiationTemplatesService,
        {provide: NgRedux, useClass: MockAppStore},
      ]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    httpMock = injector.get(HttpTestingController);
    templateService = injector.get(InstantiationTemplatesService);
  })().then(done).catch(done.fail));

  describe ('#retrieveInstantiationTemplateTopology tests', () => {
    test('when called -> retrieve template from backend', done => {
      const mockedTemplate = mock(ServiceInstance);
      const jobId: string = "some-random-job-id";

      templateService.retrieveInstantiationTemplateTopology(jobId)
        .subscribe((result: ServiceInstance) => {
          expect(Object.is(result, mockedTemplate)).toBe(true);
          done();
        });

      httpMock
        .expectOne(`${Constants.Path.INSTANTIATION_TEMPLATE_TOPOLOGY}/${jobId}`)
        .flush(mockedTemplate);
    })
  })
});

