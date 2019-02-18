import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {DynamicInputsComponent} from "./dynamic-inputs.component";
import {async, ComponentFixture, getTestBed, TestBed} from "@angular/core/testing";
import {CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, ValidatorFn} from "@angular/forms";
import {BrowserModule} from "@angular/platform-browser";
import {CommonModule} from "@angular/common";
import {RouterTestingModule} from "@angular/router/testing";
import {DynamicInputLabelPipe} from "../../pipes/dynamicInputLabel/dynamic-input-label.pipe";
import {DynamicNumber} from "../../models/dynamicInput";


describe('DynamicInputs Component', () => {
  let component: DynamicInputsComponent;
  let fixture: ComponentFixture<DynamicInputsComponent>;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [BrowserModule, CommonModule, FormsModule, HttpClientTestingModule, RouterTestingModule, ReactiveFormsModule],
      declarations: [DynamicInputsComponent, DynamicInputLabelPipe],
      providers : [FormBuilder],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
    });
    await TestBed.compileComponents();

    fixture = TestBed.createComponent(DynamicInputsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  })().then(done).catch(done.fail));

  test('should be defined', () => {
    expect(component).toBeDefined();
  });

  test('isDynamicNumber should return item', () => {
    let options = {
      minLength : 10,
      maxLength : 10
    };

    let dynamicNumber : DynamicNumber = new DynamicNumber(<any>options);
    expect(component.isDynamicNumber(dynamicNumber)).toBeDefined();
  });

  test('buildValidators should return validator', () => {
    let options = {
      minLength : 10,
      maxLength : 10,
      max : 10,
      min : 1
    };

    let dynamicNumber : DynamicNumber = new DynamicNumber(<any>options);
    let validator : ValidatorFn = component.buildValidators(dynamicNumber);
    expect(validator).toBeDefined();
  });


  test('ngOnInit', ()=>{
    component.group = new FormGroup({

    });
      component.list = [
        {
          type : 'select',
          value : 'optionName',
          name : 'multiSelectControl',
          optionList : [{
            isDataLoading : true,
            name : 'optionName',
            id : 'id'
          }]
        },
        {
          type : 'select',
          value : 'optionName',
          name : 'multiSelectControl',
          optionList : [{
            isDataLoading : true,
            name : 'optionName',
            id : 'id'
          }]
        },
        {
          type : 'multi_select',
          value : 'optionName',
          name : 'selectControl',
          optionList : [{
            isDataLoading : true,
            name : 'optionName',
            id : 'id'
          }]
        },
        {
          type : 'multi_select',
          value : 'optionName',
          name : 'selectControl',
          optionList : [{
            isDataLoading : true,
            name : 'optionName'
          }]
        },
        {
          type : 'boolean',
          value : true,
          name : 'booleanControl'
        },
        {
          type : 'boolean',
          name : 'booleanControl2'
        },
        {
          type : 'number',
          value : 100,
          name : 'numberControl'
        },
        {
          type : 'file',
          value : 'someValue',
          name : 'fileControl'
        },
        {
          type : 'checkbox',
          value : true,
          name : 'checkboxControl'
        },
        {
          type : 'map',
          value : true,
          name : 'mapControl'
        },
        {
          type : 'list',
          value : true,
          name : 'listControl'
        },
        {
          type : 'default',
          value : true,
          name : 'defaultControl'
        }
      ];

      component.ngOnInit();
      expect(component.group.controls['multiSelectControl'].value).toEqual('id');
      expect(component.group.controls['selectControl'].value).toEqual('optionName');
      expect(component.group.controls['booleanControl'].value).toEqual(true);
      expect(component.group.controls['booleanControl2'].value).toEqual(false);
      expect(component.group.controls['numberControl'].value).toEqual(100);
      expect(component.group.controls['fileControl'].value).toEqual('someValue');
      expect(component.group.controls['checkboxControl'].value).toEqual(true);
      expect(component.group.controls['mapControl'].value).toEqual(true);
      expect(component.group.controls['listControl'].value).toEqual(true);
      expect(component.group.controls['defaultControl'].value).toEqual(true);
  })

});
