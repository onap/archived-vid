import {ComponentFixture, TestBed} from '@angular/core/testing';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core'
import {FileFormControlComponent} from "./file.formControl.component";
import {CommonModule} from "@angular/common";
import {FormBuilder, FormControl, ReactiveFormsModule, Validators} from "@angular/forms";
import {ValidatorModel, ValidatorOptions} from "../../../../models/formControlModels/formControl.model";
import {FormControlMessageErrorComponent} from "../../errorMessage/formControlMessageError.component";
import {BrowserModule} from "@angular/platform-browser";
import {TooltipModule} from "ngx-tooltip";
import {FileFormControl} from "../../../../models/formControlModels/fileFormControl.model";

describe('File Form Control Component', () => {
  let component: FileFormControlComponent;
  let fixture: ComponentFixture<FileFormControlComponent>;
  let fb: FormBuilder;


  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [CommonModule, BrowserModule, ReactiveFormsModule, TooltipModule],
      providers: [FormBuilder],
      declarations: [FileFormControlComponent, FormControlMessageErrorComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    });
    await TestBed.compileComponents();

    fixture = TestBed.createComponent(FileFormControlComponent);
    component = fixture.componentInstance;
    fb = TestBed.get(FormBuilder);

  })().then(done).catch(done.fail));

  test('file component should initialize basic parameters', () => {
    component.data = new FileFormControl({
      displayName: "display Name",
      validations: [new ValidatorModel(ValidatorOptions.required, 'is required')],
      dataTestId: "data-test-id",
      placeHolder: "place holder",
      controlName: 'testFile',
      acceptedExtentions: "json"
    });

    component.form = fb.group({
      'testFile': new FormControl({
          value: component.data.selectedFile,
          disabled: false
        },
        Validators.compose(component.data.validations.map(item => item.validator))
      ),
    });

    component.data.onDelete = function () {
      component.form.controls['testFile'].setValue('onDelete');
    };

    expect(component.form.controls['testFile'].value).toEqual("place holder");
    let event = new Event("onDelete", null);
    component.data.onDelete(event, component.data, component.form);
    expect(component.form.controls['testFile'].value).toEqual('onDelete');
  })
});

