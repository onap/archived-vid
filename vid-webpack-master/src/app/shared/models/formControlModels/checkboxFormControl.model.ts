import {FormControlModel} from "./formControl.model";
import {FormControlType} from "./formControlTypes.enum";
import * as _ from "lodash";

export class CheckboxFormControl extends FormControlModel{
  extraContents : object[];
  constructor(data) {
    super(data);
    this.type = FormControlType.CHECKBOX;
    this.validations = [];
    this.extraContents = !_.isNil(data.extraContents)  ? data.extraContents : null;
  }
}
