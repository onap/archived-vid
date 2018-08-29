import {Injectable} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {FormControlModel} from "../../models/formControlModels/formControl.model";
import * as _ from 'lodash';

@Injectable()
export class GenericFormService {
    constructor(private _formBuilder: FormBuilder){}

  generateFormBuilder(controls : FormControlModel[], dynamicInputs : FormControlModel[]) : FormGroup {
    let controlsList = {};
    if(!_.isNil(controls)){
      for(let control  of controls){
        controlsList[control.controlName] =  new FormControl(
          {
            value: _.isNil(control.value) ? null :control.value,
            disabled: control.isDisabled
          }, Validators.compose(control.validations.map(item => item.validator)));
      }
    }

    if(!_.isNil(dynamicInputs)){
      let dynamicControlsList = {};
      if(!_.isNil(dynamicInputs)){
        for(let control  of dynamicInputs){
          dynamicControlsList[control.controlName] =  new FormControl(
            {
              value: control.value ? control.value : null,
              disabled: control.isDisabled
            }, Validators.compose(control.validations.map(item => item.validator)));
        }
      }
      controlsList['instanceParams'] = this._formBuilder.group(dynamicControlsList);
    }


    return this._formBuilder.group(controlsList);
  }

  shouldDisplayValidationError(form: FormGroup, controlName : string): boolean{
      if(!_.isNil(form) && !_.isNil(form.controls[controlName])){
        if(!form.controls[controlName].touched){
          return false;
        } else if(form.controls[controlName].disabled) {
          return false
        }else if(_.isNil(form.controls[controlName].errors)){
          return false;
        }else {
          return true;
        }
      }
      return false;
  }
}
