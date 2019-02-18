
import { TestBed, getTestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {GenericFormService} from './generic-form.service';
import {FormBuilder, FormGroup} from '@angular/forms';
import {FormControlModel, ValidatorModel, ValidatorOptions} from "../../models/formControlModels/formControl.model";
import {FormControlType} from "../../models/formControlModels/formControlTypes.enum";

describe('Generic Form  Service', () => {

  let injector;
  let service: GenericFormService;
  let httpMock: HttpTestingController;
  let form : FormGroup;
  let fb : FormBuilder;


  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [GenericFormService, FormBuilder]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(GenericFormService);
    httpMock = injector.get(HttpTestingController);
    fb = injector.get(FormBuilder);

  })().then(done).catch(done.fail));

  let controls : FormControlModel[] = [
    generateFormControlModel(FormControlType.INPUT, 'InputControlName','Test Value', false, generateFormValidators([ValidatorOptions.required])),
    generateFormControlModel(FormControlType.INPUT, 'InputControlName_1', 'Test InputControlName_1', true, generateFormValidators([ValidatorOptions.required])),
    generateFormControlModel(FormControlType.INPUT, 'InputControlName_2', 'Test InputControlName_2', false, [generateFormValidatorWithArg(ValidatorOptions.minLength, 4)]),
    generateFormControlModel(FormControlType.INPUT, 'InputControlName_3', 'Exact 14 chars', false, [generateFormValidatorWithArg(ValidatorOptions.maxLength, 14)]),
    generateFormControlModel(FormControlType.INPUT, 'InputControlName_4', 'Test pattern', false, [generateFormValidatorWithArg(ValidatorOptions.pattern, '^[a-zA-Z]+$')])
  ];

  describe('generateFormBuilder', ()=> {
    test('validators should work correct', () => {
      const controlName: string = 'InputControlName';
      let form : FormGroup = service.generateFormBuilder(controls, []);

      expect(form instanceof FormGroup).toBeTruthy();
      expect(form.controls[controlName]).toBeDefined();
      expect(form.controls[controlName].value).toEqual('Test Value');
      expect(form.controls[controlName].disabled).toBeFalsy();
      expect(form.controls[controlName].valid).toBeTruthy();
      expect(form.controls[controlName].errors).toBeNull();

      form.controls[controlName].setValue('');
      expect(form.controls[controlName].valid).toBeFalsy();
      expect(form.controls[controlName].errors.required).toBeTruthy();
    });

    test('validators should prevent the value to appear', () => {
      const controlName: string = 'InputControlName_1';
      let form : FormGroup = service.generateFormBuilder(controls, []);

      expect(form instanceof FormGroup).toBeTruthy();
      expect(form.controls[controlName]).toBeDefined();
      expect(form.controls[controlName].disabled).toBeTruthy();
      expect(form.controls[controlName].value).toEqual('Test InputControlName_1');
      expect(form.controls[controlName].errors).toBeNull();
    });

    test('validators with minimum length args', () => {
      const controlName: string = 'InputControlName_2';
      let form : FormGroup = service.generateFormBuilder(controls, []);

      expect(form instanceof FormGroup).toBeTruthy();
      expect(form.controls[controlName]).toBeDefined();
      expect(form.controls[controlName].disabled).toBeFalsy();
      expect(form.controls[controlName].value).toEqual('Test InputControlName_2');
      expect(form.controls[controlName].errors).toBeNull();

      form.controls[controlName].setValue('123'); // less then 4 characters. -> error
      expect(form.controls[controlName].errors.minlength).toBeDefined();
      form.controls[controlName].setValue('1234');
      expect(form.controls[controlName].errors).toBeNull();
    });

    test('validators with maximum length args', () => {
      const controlName: string = 'InputControlName_3';
      let form : FormGroup = service.generateFormBuilder(controls, []);

      expect(form instanceof FormGroup).toBeTruthy();
      expect(form.controls[controlName]).toBeDefined();
      expect(form.controls[controlName].disabled).toBeFalsy();
      expect(form.controls[controlName].value).toEqual('Exact 14 chars');
      expect(form.controls[controlName].errors).toBeNull();

      form.controls[controlName].setValue('More than max length'); // more than max characters. -> error
      expect(form.controls[controlName].errors.maxlength).toBeDefined();
      form.controls[controlName].setValue('Exact 14 chars');
      expect(form.controls[controlName].errors).toBeNull();
    });

    test('pattern validator letters only', () => {
      const controlName: string = 'InputControlName_4';
      let form : FormGroup = service.generateFormBuilder(controls, []);


      expect(form.controls[controlName].errors.pattern).toBeDefined();
      form.controls[controlName].setValue('AAAAAAAA');
      expect(form.controls[controlName].errors).toBeNull();
    });
  });



  function generateFormValidators(validatorsNames :ValidatorOptions[]){
    let validators : ValidatorModel[] = [];
    for(let validatorName of validatorsNames){
      validators.push(new ValidatorModel(validatorName, 'error ' + validatorName));
    }
    return validators;
  }

  function generateFormValidatorWithArg(validatorName :ValidatorOptions, arg : any){
    return new ValidatorModel(validatorName, 'error ' + validatorName, arg);
  }

  function generateFormControlModel(type : FormControlType,
                                    controlName: string,
                                    value: any,
                                    isDisabled: boolean,
                                    validations: ValidatorModel[]){
    let data : any = {
      type : type,
      isDisabled : isDisabled,
      validations : validations,
      value : value,
      controlName :controlName
    };

    return new FormControlModel(data);
  }
});
