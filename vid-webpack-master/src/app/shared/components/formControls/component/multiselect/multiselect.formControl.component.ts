import {Component, Input, OnChanges, SimpleChanges} from "@angular/core";
import {FormGroup} from "@angular/forms";
import {MultiselectFormControl} from "../../../../models/formControlModels/multiselectFormControl.model";

@Component({
  selector: 'multiselect-form-control',
  templateUrl: './multiselect.formControl.component.html'
})
export class MultiselectFormControlComponent implements OnChanges{
  @Input() data: MultiselectFormControl = null;
  @Input() form: FormGroup = null;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes["data"] !== undefined && changes["data"].currentValue !== changes["data"].previousValue && changes["data"].firstChange) {
      if(this.data.onInit){
        this.data.onInit(this.data, this.form);
      }
    }
  }
}
