import {Component, Input, OnChanges, SimpleChanges} from "@angular/core";
import {FormGroup} from "@angular/forms";
import {FormControlModel} from "../../../models/formControlModels/formControl.model";

@Component({
  selector : 'form-control-message-error',
  templateUrl : './formControlMessageError.component.html'
})
export class FormControlMessageErrorComponent implements OnChanges{
  @Input() formControl: FormControlModel = null;
  @Input() form: FormGroup = null;

  ngOnChanges(changes: SimpleChanges): void {
  }
}
