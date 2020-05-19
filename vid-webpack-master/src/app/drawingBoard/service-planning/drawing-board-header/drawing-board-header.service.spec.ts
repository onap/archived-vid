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

class MockDragAndDropService<T> {}
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

  each([
    ['', DrawingBoardModes.RECREATE, true],
    ['', DrawingBoardModes.CREATE, true],
    ['Create', DrawingBoardModes.EDIT, true],
    ['Create', DrawingBoardModes.RETRY_EDIT, true],
    ['Create', DrawingBoardModes.VIEW, false],
    ['Create', DrawingBoardModes.OLD_VIEW_EDIT, false],


  ]).
  test('should show edit button in correct Drawing Board Mode state', (action: string, mode: DrawingBoardModes, expected: boolean) => {
    jest.spyOn(store, 'getState').mockReturnValue(<any>{
      service: {
        serviceInstance : {
          'serviceInstanceId' : {
            action: action
          }
        }
      }
    });

  let result = service.showEditService(mode,'serviceInstanceId' );
    expect (result).toBe(expected);
  });

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
    jest.spyOn(store, 'getState').mockReturnValue(<any>{
      service: {
        serviceInstance : {
          'serviceInstanceId' : {
            validationCounter : 1
          }
        }
      }
    });
    let result = service.deployShouldBeDisabled("serviceInstanceId", DrawingBoardModes.RETRY_EDIT);
    expect(result).toBeTruthy();
  });

  test('deployShouldBeDisabled with validationCounter is 0 and not dirty',()=>{
    jest.spyOn(store, 'getState').mockReturnValue(<any>{
      service: {
        serviceInstance : {
          'serviceInstanceId' : {
            validationCounter : 0,
            isDirty : false
          }
        }
      }
    });
    let result = service.deployShouldBeDisabled("serviceInstanceId", DrawingBoardModes.RETRY_EDIT);
    expect(result).toBeFalsy();
  });

  test('deployShouldBeDisabled with validationCounter is 0 and dirty',()=>{
    jest.spyOn(store, 'getState').mockReturnValue(<any>{
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
    let result = service.deployShouldBeDisabled("serviceInstanceId", DrawingBoardModes.RETRY_EDIT);
    expect(result).not.toBeTruthy();
  });

  test('deployShouldBeDisabled with validationCounter is 0 and not and action is None and dirty',()=>{
    jest.spyOn(store, 'getState').mockReturnValue(<any>{
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
    let result = service.deployShouldBeDisabled("serviceInstanceId", DrawingBoardModes.RETRY_EDIT);
    expect(result).not.toBeTruthy();
  });

  const resumeButtonShouldBeDisplayed = [
    ['with', 'be', 'afterCompletion', true],
    ['with', 'not be', 'NOTafterCompletion', false]
  ];

  each(resumeButtonShouldBeDisplayed).
  test('given vfmodule instance %s pauseInstance field then resume button should %s displayed', (description, description1, pauseInstantiationValue: string, expected: boolean) => {
    jest.spyOn(store, 'getState').mockReturnValue(<any>getServiceInstanceWithVfModules(pauseInstantiationValue));
    let result = service.shouldDisplayResumeBtn('serviceModelId');
    expect(result).toEqual(expected);
  });

  const showModeButtonDataProvider = [
    ['be displayed',true, 'RESUME'],
    ['not be displayed', false, 'REDEPLOY']
  ];

  each(showModeButtonDataProvider).
  test('check Resume button for paused instance should %s', (description, isAppear, modeButton: string)=> {
    jest.spyOn(service, 'shouldDisplayResumeBtn').mockReturnValue(isAppear);
    let result: string = service.getModeButton('RETRY_EDIT', 'serviceModelId');
    expect(result).toEqual(modeButton);

  });
  test('getModeButton',()=>{
    jest.spyOn(service, 'shouldDisplayResumeBtn').mockReturnValue(false);
    let result : string = service.getModeButton("EDIT", "serviceModelId");
    expect(result).toEqual('UPDATE');

    result  = service.getModeButton("", "serviceModelId");
    expect(result).toEqual('DEPLOY');

    result  = service.getModeButton("RETRY_EDIT", "serviceModelId");
    expect(result).toEqual('REDEPLOY');
  });
  test('getButtonText',()=>{
    expect(service.getButtonText(DrawingBoardModes.VIEW)).toEqual('EDIT');
    expect(service.getButtonText(DrawingBoardModes.RETRY)).toEqual('REDEPLOY');

  });
  const showEditServiceDataProvider = [
    ['Create action CREATE mode', DrawingBoardModes.CREATE ,ServiceInstanceActions.Create, true],
    ['Create action RETRY_EDIT mode',DrawingBoardModes.RETRY_EDIT,  ServiceInstanceActions.Create,  true],
    ['Create action EDIT mode',DrawingBoardModes.EDIT, ServiceInstanceActions.Create,  true],
    ['Create action RETRY mode',DrawingBoardModes.RETRY, ServiceInstanceActions.Create,  false],
    ['None action EDIT mode',DrawingBoardModes.EDIT,  ServiceInstanceActions.None, false],
    ['None action RETRY_EDIT mode', DrawingBoardModes.RETRY_EDIT, ServiceInstanceActions.None, false]];
  each(showEditServiceDataProvider).test('showEditService service with %s', (description, mode, action, enabled) => {
    jest.spyOn(store, 'getState').mockReturnValue(<any>{
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

  const showResumeServiceDataProvider = [
    ['all conditions of resume- should show resume',true, 'MACRO', 'VPE', 'AssiGNed', true],
    ['flag is disabled- should not show resume ',false, 'MACRO', 'VPE', 'AssiGNed', false],
    ['transport service (PNF)- should not show resume', true, 'Macro', 'transport', 'Assigned', false],
    ['instantiationType is a-la-carte- should not show resume', true, 'ALaCarte', 'VPE', 'Assigned', false],
    ['orchestration Status is not assigned- should not show resume', true, 'Macro', 'VPE', 'Created', false],
    ['orchestration Status is Inventoried - should show resume', true, 'Macro', 'VPE', 'iNventOriEd', true]
  ];

  each(showResumeServiceDataProvider).test('showResumeService when %s', (description, flagResumeMacroService,instantiationType, subscriptionServiceType, orchStatus, shouldShowResumeService) => {
    jest.spyOn(store, 'getState').mockReturnValue(<any>{
      global: {
        flags:{
          'FLAG_1908_RESUME_MACRO_SERVICE': flagResumeMacroService
        }
      },
      service: {
        serviceInstance : {
          'serviceModelId' : {
            'vidNotions': {
              'instantiationType': instantiationType
            },
            'subscriptionServiceType':subscriptionServiceType,
            'orchStatus': orchStatus
          }
        }
      }
    });
    expect(service.showResumeService('serviceModelId')).toBe(shouldShowResumeService);

  });


  const toggleResumeServiceDataProvider = [
    [ServiceInstanceActions.None, true],
    [ServiceInstanceActions.Resume, false]
  ];

  each(toggleResumeServiceDataProvider).test('toggleResumeService - should call %s for resume/ undo Resume',(serviceAction, isResume)=>{
    jest.spyOn(store, 'dispatch');
    service.toggleResumeService("serviceInstanceId", isResume);
    expect(store.dispatch).toHaveBeenCalledWith(addServiceAction("serviceInstanceId", serviceAction));
  });

  function getServiceInstanceWithVfModules(pauseInstantiationValue) {
    return {
      service: {
        serviceInstance: {
          'serviceModelId': {
            vnfs: {
              'vfName': {
                vfModules: {
                  'modelName': {
                    'dynamicModelName': {
                      pauseInstantiation: pauseInstantiationValue
                    }
                  },
                  'modelName1': {
                    'dynamicModelName1': {
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

});
