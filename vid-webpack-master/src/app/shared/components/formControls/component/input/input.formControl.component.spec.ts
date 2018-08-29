import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core'
import {InputFormControlComponent} from "./input.formControl.component";
import {CommonModule} from "@angular/common";
import {FormBuilder, FormControl, ReactiveFormsModule, Validators} from "@angular/forms";
import {InputFormControl} from "../../../../models/formControlModels/inputFormControl.model";
import {ValidatorModel, ValidatorOptions} from "../../../../models/formControlModels/formControl.model";
import {FormControlMessageErrorComponent} from "../../errorMessage/formControlMessageError.component";
import {BrowserModule} from "@angular/platform-browser";
import {TooltipModule} from "ngx-tooltip";

describe('Input Form Control Component', () => {
  let component: InputFormControlComponent;
  let fixture: ComponentFixture<InputFormControlComponent>;
  let fb: FormBuilder;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [CommonModule, BrowserModule, ReactiveFormsModule, TooltipModule],
      providers: [FormBuilder],
      declarations: [InputFormControlComponent, FormControlMessageErrorComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    });
    await TestBed.compileComponents();

    fixture = TestBed.createComponent(InputFormControlComponent);
    component = fixture.componentInstance;
    fb = TestBed.get(FormBuilder);

  })().then(done).catch(done.fail));

  test('component should initialize basic parameters', () => {
      component.data = new InputFormControl({
        value: "value",
        displayName: "display Name",
        validations: [new ValidatorModel(ValidatorOptions.required, 'is required')],
        dataTestId: "data-test-id",
        placeHolder: "place holder",
        controlName: 'testInstanceName'
      });

      component.data.hasErrors = function () {
        return this.formGroup.controls[this.controlName].touched && this.formGroup.controls[this.controlName].errors ? ['error-style'] : [];
      };

      component.data.onBlur = function () {
        component.form.controls['testInstanceName'].setValue('onBlur');
      };

      component.form = fb.group({
        'testInstanceName': new FormControl({
          value: component.data.value,
          disabled: false
        }, Validators.compose(component.data.validations.map(item => item.validator)))
      });


      component.form.controls['testInstanceName'].setValue('newValue');
      expect(component.form.controls['testInstanceName'].errors).toBeFalsy();
      component.form.controls['testInstanceName'].setValue('');
      expect(component.form.controls['testInstanceName'].errors.required).toBeTruthy();
      component.data.onBlur();
      expect(component.form.controls['testInstanceName'].value).toEqual('onBlur');
      expect(component.form.controls['testInstanceName'].errors).toBeFalsy();
    }
  )
});

