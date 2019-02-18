import {FormControlModel} from "./formControl.model";
import {FormControlType} from "./formControlTypes.enum";

export class CheckboxFormControl extends FormControlModel{

  constructor(data) {
    super(data);
    this.type = FormControlType.CHECKBOX;
    this.validations = [];
  }
}
