import {TestBed, ComponentFixture} from '@angular/core/testing';
import {Component, DebugElement} from "@angular/core";
import {By} from "@angular/platform-browser";
import { InputPreventionPatternDirective } from './inputPreventionPattern.directive';

@Component({
  template: `<input 
    patternInput
    pattern="^[a-zA-Z0-9_]*$">`
})
class TestHoverFocusComponent {
}


describe('InputPrevention Pattern Directive', () => {

  let component: TestHoverFocusComponent;
  let fixture: ComponentFixture<TestHoverFocusComponent>;
  let directiveInstance : InputPreventionPatternDirective;
  let inputEl: DebugElement;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TestHoverFocusComponent, InputPreventionPatternDirective]
    });
    fixture = TestBed.createComponent(TestHoverFocusComponent);
    component = fixture.componentInstance;
    inputEl = fixture.debugElement.query(By.css('input'));
    directiveInstance = inputEl.injector.get(InputPreventionPatternDirective);
  });

  it('directive should be defined', () => {
    expect(directiveInstance).toBeDefined();
  });

  it('pattern exists', () => {
    expect(inputEl.nativeElement.pattern).toEqual('^[a-zA-Z0-9_]*$');
  });

  it('kepress legal input', () => {
    fixture.detectChanges();
    inputEl.nativeElement.value = "legalInput";
    expect(new RegExp(inputEl.nativeElement.pattern).test(inputEl.nativeElement.value)).toBeTruthy();
  });

  it('kepress illegal input', () => {
    inputEl.triggerEventHandler('kepress', " ");
    fixture.detectChanges();
    expect(inputEl.nativeElement.value).toBe('');
  });

  it('kepress event legal input should return event', () => {
    const event = <any>{ key: 'A' };
    inputEl.nativeElement.value = "legalInput";
    let result = directiveInstance.onKeypress(event);
    expect(result).toBe(event);
  });

  it('kepress event illegal input should prevent default', () => {
    const event = <any>{key: '-', preventDefault : function () {} };
    spyOn(event, 'preventDefault');
    inputEl.nativeElement.value = "-";
    let result = directiveInstance.onKeypress(event);
    expect(event.preventDefault).toHaveBeenCalled();
  });
});
