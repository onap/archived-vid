import {ComponentInfoService} from './component-info.service';
import {getTestBed, TestBed} from '@angular/core/testing';
import {MockNgRedux, NgReduxTestingModule} from '@angular-redux/store/testing';
import {NgRedux} from '@angular-redux/store';
import {AaiService} from '../../../shared/services/aaiService/aai.service';
import {HttpClient, HttpHandler} from '@angular/common/http';
import {FeatureFlagsService} from '../../../shared/services/featureFlag/feature-flags.service';
import {ModelInformationItem} from "../../../shared/components/model-information/model-information.component";
import {ComponentInfoModel, ComponentInfoType} from "./component-info-model";

class MockAppStore<T> {
  getState() {
    return {
      global: {
      },
      service : {
        serviceHierarchy : {
          '1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd' : {
            service:{
              serviceRole: 'Testing',
              serviceType: 'pnf',
              uuid: '1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd',
              version: '1.0',
              customizationUuid: ''
            }
          }
        },
        serviceInstance : {
          '1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd' : {
            globalSubscriberId: 'e433710f-9217-458d-a79d-1c7aff376d89',
            subscriptionServiceType:'TYLER SILVIA',
            instanceId: '2f7130e8-27d6-4c01-8988-60ca67e8dae4'
          }
        },
        subscribers: [
          {
            'id': 'CAR_2020_ER',
            'name': 'CAR_2020_ER',
            'isPermitted': true
          },  
          {
            'id': 'e433710f-9217-458d-a79d-1c7aff376d89',
            'name': 'SILVIA ROBBINS',
            'isPermitted': true
          }
        ]
      }
    }
  }
}

beforeAll(done => (async () => {
  TestBed.configureTestingModule({
    imports: [NgReduxTestingModule],
    providers: [
      AaiService,
      HttpClient,
      HttpHandler,
      FeatureFlagsService,
      ComponentInfoService,
      {provide: NgRedux, useClass: MockAppStore},
      MockNgRedux]
  });
  await TestBed.compileComponents();
  let injector = getTestBed();
  service = injector.get(ComponentInfoService);
  })().then(done).catch(done.fail));

let service: ComponentInfoService;
describe('Service Info Data', () => {

  test('ComponentInfoService should be defined', () => {
    expect(service).toBeDefined();
  });

  test('Info for service should be correct', () => {
    let actualServiceInfo = service.getInfoForService('1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
    let expectedServiceInfo = [
      ModelInformationItem.createInstance('Type', 'pnf'),
      ModelInformationItem.createInstance('Model Version', '1.0'),
      ModelInformationItem.createInstance('Model Customization ID', ''),
      ModelInformationItem.createInstance('Instance ID', '2f7130e8-27d6-4c01-8988-60ca67e8dae4'),
      ModelInformationItem.createInstance('Subscriber Name', 'SILVIA ROBBINS'),
      ModelInformationItem.createInstance('Service Type', 'TYLER SILVIA'),
      ModelInformationItem.createInstance('Service Role', 'Testing'),
    ];
    expect(actualServiceInfo).toEqual(new ComponentInfoModel(ComponentInfoType.SERVICE, expectedServiceInfo, []));


  });

});

