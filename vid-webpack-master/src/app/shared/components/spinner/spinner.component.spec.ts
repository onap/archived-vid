import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import { SpinnerComponent } from './spinner.component';

describe('Spinner component', () => {
  let component: SpinnerComponent;
  let fixture: ComponentFixture<SpinnerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [],
      declarations: [SpinnerComponent ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SpinnerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('component should be defined', () => {
    expect(component).toBeDefined();
  });


  it('component constructor should subscribe of showSpinner event with true', ()=> {
    SpinnerComponent.showSpinner.next(true);
    expect(component.show).toBeTruthy();
  });

  it('component constructor should subscribe of showSpinner event with false', ()=> {
    SpinnerComponent.showSpinner.next(false);
    expect(component.show).toBeFalsy();
  });




});
