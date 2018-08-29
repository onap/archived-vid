import {ComponentFixture, TestBed} from '@angular/core/testing';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core'
import {CommonModule} from "@angular/common";
import {FormBuilder, FormControl, ReactiveFormsModule, Validators} from "@angular/forms";
import {
  ValidatorModel,
  ValidatorOptions
} from "../../../../models/formControlModels/formControl.model";
import {FormControlMessageErrorComponent} from "../../errorMessage/formControlMessageError.component";
import {BrowserModule} from "@angular/platform-browser";
import {MultiselectFormControlComponent} from "./multiselect.formControl.component";
import {MultiselectFormControl} from "../../../../models/formControlModels/multiselectFormControl.model";
import { of } from "rxjs";
describe('Dropdown Form Control Component', () => {
  let component: MultiselectFormControlComponent;
  let fixture: ComponentFixture<MultiselectFormControlComponent>;
  let fb: FormBuilder;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [CommonModule, BrowserModule, ReactiveFormsModule],
      providers: [FormBuilder],
      declarations: [MultiselectFormControlComponent, FormControlMessageErrorComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    });
    await TestBed.compileComponents();

    fixture = TestBed.createComponent(MultiselectFormControlComponent);
    component = fixture.componentInstance;
    fb = TestBed.get(FormBuilder);

  })().then(done).catch(done.fail));

  test('component should initialize basic parameters', () => {
      component.data = new MultiselectFormControl({
        displayName: "display Name",
        validations: [new ValidatorModel(ValidatorOptions.required, 'is required')],
        dataTestId: "data-test-id",
        placeHolder: "place holder",
        controlName: 'testDropdown',
        options: of([
          'option1',
          'option2',
          'option3',
          'onBlur'
        ])
      });

      component.data.hasErrors = function () {
        return this.formGroup.controls[this.controlName].touched && this.formGroup.controls[this.controlName].errors ? ['error-style'] : [];
      };

      component.data.onBlur = function () {
        component.form.controls['testDropdown'].setValue('onBlur');
      };

      component.form = fb.group({
        'testDropdown': new FormControl({
          value: component.data.value,
          disabled: false
        }, Validators.compose(component.data.validations.map(item => item.validator)))
      });

      component.form.controls['testDropdown'].setValue('');
      expect(component.form.controls['testDropdown'].errors.required).toBeTruthy();
      component.form.controls['testDropdown'].setValue('option2');
      expect(component.form.controls['testDropdown'].errors).toBeFalsy();
      component.data.onBlur();
      expect(component.form.controls['testDropdown'].value).toEqual('onBlur');
      expect(component.form.controls['testDropdown'].errors).toBeFalsy();
    }
  )
});

