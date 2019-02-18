import {FormControlModel} from "./formControl.model";
import {FormControlType} from "./formControlTypes.enum";

export class NumberFormControl extends FormControlModel{
  min: number;
  max: number;


  constructor(data) {
    super(data);
    this.type = FormControlType.NUMBER;
    this.min = data.min;
    this.max = data.max;
  }

}
