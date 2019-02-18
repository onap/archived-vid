import {ComponentFixture, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {SpinnerComponent, SpinnerInfo} from './spinner.component';
import {CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";

describe('Spinner component', () => {
  let component: SpinnerComponent;
  let fixture: ComponentFixture<SpinnerComponent>;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [],
      declarations: [SpinnerComponent],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
    });
    await TestBed.compileComponents();

    fixture = TestBed.createComponent(SpinnerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

  })().then(done).catch(done.fail));



  test('component should be defined', () => {
    expect(component).toBeDefined();
  });

  test('component constructor should subscribe of showSpinner event with true', ()=> {
    let spinnerInfo : SpinnerInfo = new SpinnerInfo(true, 'someUrl', 'json');
    SpinnerComponent.showSpinner.next(spinnerInfo);
    expect(component.show).toBeTruthy();
  });

  test('component constructor should subscribe of showSpinner event with true and then to be false', ()=> {
    let spinnerInfo : SpinnerInfo = new SpinnerInfo(true, 'someUrl', 'json');
    SpinnerComponent.showSpinner.next(spinnerInfo);
    spinnerInfo  = new SpinnerInfo(false, 'someUrl', 'json');
    SpinnerComponent.showSpinner.next(spinnerInfo);
    expect(component.show).toBeFalsy();
  });

  test('component constructor should subscribe of showSpinner event with false', ()=> {
    let spinnerInfo : SpinnerInfo = new SpinnerInfo(false, 'someUrl', 'json');
    SpinnerComponent.showSpinner.next(spinnerInfo);
    expect(component.show).toBeFalsy();
  });
});
