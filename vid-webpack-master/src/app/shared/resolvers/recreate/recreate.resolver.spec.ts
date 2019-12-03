import {RecreateResolver} from "./recreate.resolver";
import {getTestBed, TestBed} from '@angular/core/testing';
import {NgRedux} from "@angular-redux/store";
import {InstantiationTemplatesService} from "../../services/templateService/instantiationTemplates.service";
import {AaiService} from "../../services/aaiService/aai.service";
import {mock} from "ts-mockito";
import {ServiceInstance} from "../../models/serviceInstance";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {FeatureFlagsService} from "../../services/featureFlag/feature-flags.service";
import {convertToParamMap} from "@angular/router";
import {of} from 'rxjs/observable/of'

class MockAppStore<T> {
  getState(){}
}

describe('Recreate resolver', () => {

  let injector;
  let recreateResolver: RecreateResolver;
  let aaiService: AaiService;
  let instantiationTemplatesService: InstantiationTemplatesService;

  beforeAll( done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        FeatureFlagsService,
        InstantiationTemplatesService,
        RecreateResolver,
        AaiService,
        {provide: NgRedux, useClass: MockAppStore},
      ]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    recreateResolver = injector.get(RecreateResolver);
    aaiService = injector.get(AaiService);
    instantiationTemplatesService = injector.get(InstantiationTemplatesService);
  })().then(done).catch(done.fail));

  describe('#retrieveInstantiationTemplateTopology tests', () => {

    test("when resolve() invoked -> then getServiceModelById and retrieveAndStoreInstantiationTemplateTopology are called", done => {
      jest.spyOn(aaiService, 'getServiceModelById')
        .mockReturnValue(of({}));
      jest.spyOn(instantiationTemplatesService, 'retrieveAndStoreInstantiationTemplateTopology')
        .mockReturnValue(of(mock(ServiceInstance)));

      recreateResolver.resolve(<any>{
        queryParamMap:
          convertToParamMap({
            serviceModelId: "someServiceModelId",
            jobId: "someJobId",
          })
      })

      .subscribe(() => {
        expect(aaiService.getServiceModelById).toHaveBeenCalled();
        expect(instantiationTemplatesService.retrieveAndStoreInstantiationTemplateTopology).toHaveBeenCalled();
        done();
      });
    })

  });

});
