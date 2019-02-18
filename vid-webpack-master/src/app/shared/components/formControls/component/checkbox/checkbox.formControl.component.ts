import {Component, Input} from "@angular/core";
import {FormControlModel} from "../../../../models/formControlModels/formControl.model";
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'checkbox-form-control',
  templateUrl: './checkbox.formControl.component.html'
})
export class CheckboxFormControlComponent{
  @Input() data: FormControlModel;
  @Input() form: FormGroup;

}
