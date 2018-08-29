import {Component, Input} from "@angular/core";
import {FormGroup} from "@angular/forms";
import {NumberFormControl} from "../../../../models/formControlModels/numberFormControl.model";

@Component({
  selector: 'number-form-control',
  templateUrl: './number.formControl.component.html'
})
export class NumberFormControlComponent {
  @Input() data: NumberFormControl = null;
  @Input() form: FormGroup = null;
}
