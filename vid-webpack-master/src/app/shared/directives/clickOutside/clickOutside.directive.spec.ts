import {ComponentFixture, TestBed} from '@angular/core/testing';
import {Component, DebugElement} from "@angular/core";
import {By} from "@angular/platform-browser";
import {ClickOutsideDirective} from "./clickOutside.directive";

@Component({
  template:
      `<div id="innerDiv" [clickOutside] [classElements]="['outsideDivShouldNotTrigger']"></div>
       <div class="outsideDivShouldTrigger"></div>
       <div class="outsideDivShouldNotTrigger"></div>`
})
class TestHoverFocusComponent {
}


describe('Click outside Directive', () => {
  let component: TestHoverFocusComponent;
  let fixture: ComponentFixture<TestHoverFocusComponent>;
  let directiveInstance : ClickOutsideDirective;
  let outsideDivShouldNotTrigger: DebugElement;
  let outsideDivShouldTrigger: DebugElement;
  let innerDiv: DebugElement;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TestHoverFocusComponent, ClickOutsideDirective]
    });
    fixture = TestBed.createComponent(TestHoverFocusComponent);
    component = fixture.componentInstance;
    outsideDivShouldNotTrigger = fixture.debugElement.query(By.css('.outsideDivShouldNotTrigger'));
    outsideDivShouldTrigger = fixture.debugElement.query(By.css('.outsideDivShouldTrigger'));
    innerDiv = fixture.debugElement.query(By.css('#innerDiv'));
    directiveInstance = innerDiv.injector.get(ClickOutsideDirective);
  });

  test('directive should be defined', () => {
    expect(directiveInstance).toBeDefined();
  });

  test(`should have list of class's`, () => {
    fixture.detectChanges();
    expect(directiveInstance.classElements).toEqual(['outsideDivShouldNotTrigger']);
  });

  test('should not trigger output on click outside', () => {
    fixture.detectChanges();
    spyOn(directiveInstance.clickOutsideTrigger, 'next');
    outsideDivShouldNotTrigger.nativeElement.click();
    expect(directiveInstance.clickOutsideTrigger.next).toHaveBeenCalledTimes(0);
  });

});
