import {FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {Subject} from "rxjs/Subject";
import {FormControlType} from "./formControlTypes.enum";
import {CustomValidators} from "../../validators/uniqueName/uniqueName.validator";
import * as _ from 'lodash';
import {UploadFilesLinkModel} from "../../components/genericForm/genericFormSharedComponent/uploadFiles/upload-files-link.model";

export class  FormControlModel {
  formGroup : FormGroup;
  controlName : string;
  type : FormControlType;
  displayName : string;
  dataTestId : string;
  placeHolder? : string;
  tooltip? :string;
  isDisabled? : boolean;
  validations? : ValidatorModel[];
  isVisible? : boolean;
  value? : any;
  originalValue?: any;
  minLength?: number;
  maxLength?: number;
  onChange? : Function;
  onBlur? : Function;
  preventionsAttribute : AttributeMap[] = [];
  waitFor? : Subject<string>[] = [];
  hasErrors : () =>  string[];


  constructor(data: any){
    this.type = data.type;
    this.displayName = data.displayName;
    this.dataTestId = data.dataTestId;
    this.placeHolder = data.placeHolder;
    this.tooltip = data.tooltip;
    this.isDisabled = data.isDisabled || false;
    this.validations = data.validations || [];
    this.isVisible = !_.isNil(data.isVisible)  ? data.isVisible : true;
    this.value = data.value;
    this.originalValue = data.value;
    this.controlName = data.controlName;
    this.minLength = data.minLength;
    this.maxLength = data.maxLength;
    this.preventionsAttribute = data.preventionsAttribute || [];
    this.onBlur = function(){};
    this.onChange = data.onChange ? data.onChange: function () {}

  }

  isRequired() : boolean {
    for(let validtorsModel of this.validations){
      let required = 'required';
      if(validtorsModel.validatorName.toString() === required){
        return true;
      }
    }
    return false;
  }

  getPreventionAttribute() : AttributeMap[] {
    let result : AttributeMap[] = [new AttributeMap('data-tests-id', this.dataTestId)];
    return this.preventionsAttribute.concat(result);
  }
}



export class ValidatorModel {
  validator : ValidatorFn;
  validatorArg? : any;
  validatorName : ValidatorOptions | CustomValidatorOptions;
  errorMsg : string;

  constructor(validatorName : ValidatorOptions | CustomValidatorOptions, errorMsg : string, validatorArg: any = null){
    this.validatorName = validatorName;
    this.validator = this.setValidator(validatorName, validatorArg);
    this.errorMsg = errorMsg;
    this.validatorArg = validatorArg;
  }


  setValidator(validatorName : ValidatorOptions | CustomValidatorOptions, validatorArg: any = null) : ValidatorFn {
    if(validatorName in ValidatorOptions){
      return validatorArg ? Validators[validatorName](validatorArg) : Validators[validatorName];
    }else {
      return validatorArg ? CustomValidators[CustomValidatorOptions[validatorName]].apply(this, validatorArg) : CustomValidators[CustomValidatorOptions[validatorName]];
    }
  }
}



export enum ValidatorOptions {
  required = 'required',
  minLength = 'minLength',
  maxLength = 'maxLength',
  pattern = 'pattern',
  nullValidator = 'nullValidator'
}



export enum CustomValidatorOptions {
  uniqueInstanceNameValidator = 'uniqueInstanceNameValidator',
  emptyDropdownOptions = 'emptyDropdownOptions',
  isValidJson = 'isValidJson',
  isFileTooBig = 'isFileTooBig',
  isStringContainTags = 'isStringContainTags'
}

export class AttributeMap {
  key : string;
  value : string;

  constructor(key : string, value? : string){
    this.key = key;
    this.value = value ? value : '';
  }
}




