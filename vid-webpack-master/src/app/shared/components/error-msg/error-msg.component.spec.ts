import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import { ErrorMsgComponent } from './error-msg.component';
import {ErrorMsgService} from "./error-msg.service";
import {ErrorMsgObject} from "./error-msg.model";

describe('ErrorMsgComponent', () => {

  let component: ErrorMsgComponent;
  let fixture: ComponentFixture<ErrorMsgComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ErrorMsgComponent],
      providers: [ErrorMsgService]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ErrorMsgComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should triggerShowError fill error msg object', () => {
    let errorMsgObj:ErrorMsgObject = new ErrorMsgObject("Title", "SubTitle", "Description")
    component.errorMsgService.triggerShowError.next(errorMsgObj);
    let errorMsg = component.errorMsgService.errorMsgObject;
    expect(errorMsg).toBeDefined();
    expect(errorMsg.title).toBe('Title');
    expect(errorMsg.subtitle).toBe('SubTitle');
    expect(errorMsg.description).toBe('Description');
  });

  test('should triggerClearError delete error msg object', () => {
    let errorMsgObj:ErrorMsgObject = new ErrorMsgObject("Title", "SubTitle", "Description")
    component.errorMsgService.triggerShowError.next(errorMsgObj);
    let errorMsg = component.errorMsgService.errorMsgObject;
    expect(errorMsg).toBeDefined();
    component.errorMsgService.triggerClearError.next();
    expect(component.errorMsgService.errorMsgObject).toBeNull();

  });
});
