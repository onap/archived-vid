import {getTestBed, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {DefaultDataGeneratorService} from "../../../shared/services/defaultDataServiceGenerator/default.data.generator.service";
import {MockNgRedux, NgReduxTestingModule} from "@angular-redux/store/testing";
import {DrawingBoardHeaderService} from "./drawing-board-header.service";
import {ActivatedRoute} from '@angular/router';
import {ServiceInstanceActions} from "../../../shared/models/serviceInstanceActions";
import {AppState} from "../../../shared/store/reducers";
import {NgRedux} from "@angular-redux/store";
import {addServiceAction} from "../../../shared/storeUtil/utils/service/service.actions";
import {ErrorMsgService} from "../../../shared/components/error-msg/error-msg.service";
import {DrawingBoardModes} from "../drawing-board.modes";
import each from "jest-each";

class MockAppStore<T>{
  getState()  {
    return {
      service : {
        serviceInstance : {
          "serviceInstanceId" : {
            action: 'None'
          }
        }
      }
    }
  }
}

describe('Generate path to old View/Edit ', () => {
  let injector;
  let service: DrawingBoardHeaderService;
  let httpMock: HttpTestingController;
  let store :  NgRedux<AppState>;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NgReduxTestingModule],
      providers: [
        DrawingBoardHeaderService,
        DefaultDataGeneratorService,
        MockNgRedux,
        ErrorMsgService,
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot : {
              queryParams:{
                'subscriberId' : 'subscriberId',
                'subscriberName' : 'subscriberName',
                'serviceType' : 'serviceType',
                'serviceInstanceId' : 'serviceInstanceId'
              }
            }
          },
        }]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(DrawingBoardHeaderService);
    httpMock = injector.get(HttpTestingController);
    store = injector.get(NgRedux);

  })().then(done).catch(done.fail));


  test('should generate url to old view/edit ', () => {
    const query: string = 'subscriberId=subscriberId&subscriberName=subscriberName&serviceType=serviceType&serviceInstanceId=serviceInstanceId';
    const path =  '../../serviceModels.htm#/instantiate?' + query;
    let result = service.generateOldViewEditPath();
   expect(result).toEqual(path);
  });

  test('should call update action for Delete',()=>{

    jest.spyOn(store, 'dispatch');
    service.deleteService("serviceInstanceId", true);
    expect(store.dispatch).toHaveBeenCalledWith(addServiceAction("serviceInstanceId", ServiceInstanceActions.Delete));
  });

  test('should call update action for undo delete',()=>{
    jest.spyOn(store, 'dispatch');
    service.deleteService("serviceInstanceId", false);
    expect(store.dispatch).toHaveBeenCalledWith(addServiceAction("serviceInstanceId", ServiceInstanceActions.None));
  });

  test('deployShouldBeDisabled with validationCounter greater then 0',()=>{
    jest.spyOn(store, 'getState').mockReturnValue({
      service: {
        serviceInstance : {
          'serviceInstanceId' : {
            validationCounter : 1
          }
        }
      }
    });
    let result = service.deployShouldBeDisabled("serviceInstanceId");
    expect(result).toBeTruthy();
  });

  test('deployShouldBeDisabled with validationCounter is 0 and not dirty',()=>{
    jest.spyOn(store, 'getState').mockReturnValue({
      service: {
        serviceInstance : {
          'serviceInstanceId' : {
            validationCounter : 0,
            isDirty : false
          }
        }
      }
    });
    let result = service.deployShouldBeDisabled("serviceInstanceId");
    expect(result).toBeFalsy();
  });

  test('deployShouldBeDisabled with validationCounter is 0 and dirty',()=>{
    jest.spyOn(store, 'getState').mockReturnValue({
      service: {
        serviceInstance : {
          'serviceInstanceId' : {
            validationCounter : 0,
            action : 'None',
            isDirty : true
          }
        }
      }
    });
    let result = service.deployShouldBeDisabled("serviceInstanceId");
    expect(result).not.toBeTruthy();
  });

  test('deployShouldBeDisabled with validationCounter is 0 and not and action is None and dirty',()=>{
    jest.spyOn(store, 'getState').mockReturnValue({
      service: {
        serviceInstance : {
          'serviceInstanceId' : {
            validationCounter : 0,
            action : ServiceInstanceActions.None,
            isDirty : true
          }
        }
      }
    });
    let result = service.deployShouldBeDisabled("serviceInstanceId");
    expect(result).not.toBeTruthy();
  });


  test('getModeButton',()=>{
    let result : string = service.getModeButton("EDIT");
    expect(result).toEqual('UPDATE');

    result  = service.getModeButton("");
    expect(result).toEqual('DEPLOY');

    result  = service.getModeButton("RETRY_EDIT");
    expect(result).toEqual('REDEPLOY');
  });
  test('getButtonText',()=>{
    expect(service.getButtonText("VIEW")).toEqual('EDIT');
    expect(service.getButtonText("RETRY")).toEqual('REDEPLOY');

  });
  const showEditServiceDataProvider = [
    ['Create action CREATE mode', DrawingBoardModes.CREATE ,ServiceInstanceActions.Create, true],
    ['Create action RETRY_EDIT mode',DrawingBoardModes.RETRY_EDIT,  ServiceInstanceActions.Create,  true],
    ['Create action EDIT mode',DrawingBoardModes.EDIT, ServiceInstanceActions.Create,  true],
    ['Create action RETRY mode',DrawingBoardModes.RETRY, ServiceInstanceActions.Create,  false],
    ['None action EDIT mode',DrawingBoardModes.EDIT,  ServiceInstanceActions.None, false],
    ['None action RETRY_EDIT mode', DrawingBoardModes.RETRY_EDIT, ServiceInstanceActions.None, false]];
  each(showEditServiceDataProvider).test('showEditService service with %s', (description, mode, action, enabled) => {
    jest.spyOn(store, 'getState').mockReturnValue({
      service: {
        serviceInstance : {
          'serviceInstanceId' : {
            action : action
          }
        }
      }
    });
    expect(service.showEditService(mode, 'serviceInstanceId')).toBe(enabled);

  });
});
