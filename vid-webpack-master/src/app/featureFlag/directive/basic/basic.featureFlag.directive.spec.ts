import {TestBed, ComponentFixture, tick, inject} from '@angular/core/testing';
import {Component, DebugElement, Renderer2, Type} from "@angular/core";
import {By} from "@angular/platform-browser";
import {BasicFeatureFlagDirective} from "./basic.featureFlag.directive";
import {FeatureFlagService} from "../../service/featureFlag.service";
import {ConfigurationService} from "../../../shared/services/configuration.service";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {NgRedux} from "@angular-redux/store";
import {of} from "rxjs";

@Component({
  template: `
    <div
      id="featureFlagOff"
      featureFlag
      [flagName]='"featureFlagOff"'>
    </div>
    <div
      id="featureFlagOn"
      featureFlag
      [flagName]='"featureFlagOn"'>
    </div>`
})
class TestFeatureFlagComponent {
}

class MockRenderer<T> {
  setStyle() {

  }
}

class MockAppStore<T> {
  getState() {
    return {
      global: {
        flags : {

        }
      }
    }
  }
}


describe('Basic Feature Flag Directive', () => {
  let component: TestFeatureFlagComponent;
  let fixture: ComponentFixture<TestFeatureFlagComponent>;
  let directiveInstance: BasicFeatureFlagDirective;
  let elementOff: DebugElement;
  let elementOn: DebugElement;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      declarations: [
        TestFeatureFlagComponent,
        BasicFeatureFlagDirective],
      providers: [
        FeatureFlagService,
        ConfigurationService,
        {provide: NgRedux, useClass: MockAppStore},
        {provide: Renderer2, useClass: MockRenderer}]
    }).compileComponents();

    fixture = TestBed.createComponent(TestFeatureFlagComponent);
    component = fixture.componentInstance;
    elementOff = fixture.debugElement.query(By.css('#featureFlagOff'));
    elementOn = fixture.debugElement.query(By.css('#featureFlagOn'));
    directiveInstance = elementOff.injector.get(BasicFeatureFlagDirective);
  });


  test('directive should be defined', () => {
    expect(directiveInstance).toBeDefined();
  });

  test('should hide element if feature flag is off', () => {
    directiveInstance.flagName  = 'featureFlagOff';

    directiveInstance.ngAfterContentChecked();
    expect(elementOff.nativeElement.style.display).toEqual('none');
  });

  test('should show element if feature flag is on', () => {
    directiveInstance.flagName  = 'featureFlagOn';

    directiveInstance.ngAfterContentChecked();
    expect(elementOn.nativeElement.style.display).toEqual('');
  });
});
