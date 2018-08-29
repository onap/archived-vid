import {getTestBed, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {NgRedux} from "@angular-redux/store";
import {ViewEditResolver} from "./viewEdit.resolver";
import {AaiService} from "../../services/aaiService/aai.service";
import {FeatureFlagsService} from "../../services/featureFlag/feature-flags.service";
import {ActivatedRouteSnapshot, convertToParamMap} from "@angular/router";
import {AppState} from "../../store/reducers";
import {UpdateDrawingBoardStatusAction} from "../../storeUtil/utils/global/global.actions";

class MockAppStore<T> {
  getState() {
    return {
      global:{
        drawingBoardStatus: "VIEW"
      },
      service: {
        serviceInstance: {}
      }
    }
  }
  dispatch(){

  }
}


describe('View Edit resolver', () => {
  let injector;
  let aaiService: AaiService;
  let resolver: ViewEditResolver;
  let httpMock: HttpTestingController;
  let store : NgRedux<AppState>;

  let activatedRouteSnapshot: ActivatedRouteSnapshot;
  let updateDrawingBoardStatusAction: UpdateDrawingBoardStatusAction;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        ViewEditResolver,
        AaiService,
        FeatureFlagsService,
        {provide: NgRedux, useClass: MockAppStore},
        {
          provide: ActivatedRouteSnapshot, useValue: {
            queryParamMap:
              convertToParamMap({
                serviceModelId: 'serviceModelId',
                subscriberId: 'subscriberId',
                serviceType: 'serviceType',
                serviceInstanceId : 'serviceInstanceId'
              })
          },

        }
      ]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    aaiService = injector.get(AaiService);
    resolver = injector.get(ViewEditResolver);
    httpMock = injector.get(HttpTestingController);
    activatedRouteSnapshot = injector.get(ActivatedRouteSnapshot);
    store = injector.get(NgRedux)

  })().then(done).catch(done.fail));


  test('should call both api', () => {
    // spyOn(aaiService, 'getServiceModelById');
    // spyOn(aaiService, 'retrieveAndStoreServiceInstanceTopology');
    // spyOn(store, 'dispatch');
    // resolver.resolve(activatedRouteSnapshot);
    //
    // expect(aaiService.getServiceModelById).toHaveBeenCalledWith('serviceModelId');
    // expect(aaiService.retrieveAndStoreServiceInstanceTopology).toHaveBeenCalledWith('serviceInstanceId', 'subscriberId', 'serviceType', 'serviceModelId');
  });

});
