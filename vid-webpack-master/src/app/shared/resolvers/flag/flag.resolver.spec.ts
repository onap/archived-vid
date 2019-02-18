import {TestBed, getTestBed} from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import {NgRedux} from "@angular-redux/store";
import {FeatureFlagsService} from "../../services/featureFlag/feature-flags.service";
import {ActivatedRouteSnapshot, convertToParamMap} from "@angular/router";
import {FlagsResolve} from "./flag.resolver";
import {ConfigurationService} from "../../services/configuration.service";

class MockAppStore<T> {
  getState() {
    return {
      service: {
        serviceInstance: {}
      }
    }
  }
}


describe('Flag resolver', () => {
  let injector;
  let resolver: FlagsResolve;
  let configurationService: ConfigurationService;
  let httpMock: HttpTestingController;

  let activatedRouteSnapshot: ActivatedRouteSnapshot;
  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        FlagsResolve,
        ConfigurationService,
        FeatureFlagsService,
        {provide: NgRedux, useClass: MockAppStore},
        {
          provide: ActivatedRouteSnapshot, useValue: {
            queryParamMap:
              convertToParamMap({ })
          }
        },
      ]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    resolver = injector.get(FlagsResolve);
    httpMock = injector.get(HttpTestingController);
    activatedRouteSnapshot = injector.get(ActivatedRouteSnapshot);
    configurationService = injector.get(ConfigurationService);

  })().then(done).catch(done.fail));

  test('should update flags', () => {
    spyOn(configurationService, 'getFlags');
    resolver.resolve(activatedRouteSnapshot);
    expect(configurationService.getFlags).toHaveBeenCalled();
  });
});
