import {FormControlModel} from "./formControl.model";
import {FormControlType} from "./formControlTypes.enum";

export class InputFormControl extends FormControlModel {
  onBlur ?: Function;
  onKeypress?: Function;
  inputType: string = 'text';
  pattern: string;
  preventions: string[];

  constructor(data) {
    super(data);
    this.type = FormControlType.INPUT;
    this.pattern = data.pattern;
    this.onKeypress = data.onKeypress ? data.onKeypress : ()=>{}
    this.onBlur = data.onBlur ? data.onBlur : ()=>{}
  }
}

