import {TestBed, getTestBed } from '@angular/core/testing';
import {ErrorMsgService} from "./error-msg.service";

describe('Error msg Service', () => {
  let injector;
  let service: ErrorMsgService;

  beforeAll(done => (async () => {

    TestBed.configureTestingModule(
      {
        providers: [
          ErrorMsgService
        ]
      });
    await TestBed.compileComponents();
    injector = getTestBed();
    service = injector.get(ErrorMsgService);
  })().then(done).catch(done.fail));

  test('should return error msg object when call to getScalingErrorObject', () => {
    let errorMsgObject = service.getScalingErrorObject();
    expect(errorMsgObject).toBeDefined();
    expect(errorMsgObject.title).toBe('Error : Too many members');
    expect(errorMsgObject.subtitle).toBe('One or more VNF groups, marked below, exceeds the maximum allowed number of members to associate');
    expect(errorMsgObject.description).toBe('Please make sure the total amount of VNF instances is less than that amount.');
  });

  test('should return error msg object when call to getRetryErrorObject', () => {
    let errorMsgObject = service.getRetryErrorObject(1);
    expect(errorMsgObject).toBeDefined();
    expect(errorMsgObject.title).toBe('ERROR!');
    expect(errorMsgObject.subtitle).toBe(`Attention: You are currently viewing instances from the MSO. \n 1 of the instances failed, please try again.`);
    expect(errorMsgObject.description).toBe(null);
  });

});

