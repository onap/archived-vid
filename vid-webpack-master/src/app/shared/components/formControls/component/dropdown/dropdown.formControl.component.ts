import {Component, Input, OnChanges, SimpleChanges} from "@angular/core";
import {DropdownFormControl} from "../../../../models/formControlModels/dropdownFormControl.model";
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'dropdown-form-control',
  templateUrl: './dropdown.formControl.component.html',
  styleUrls : ['./dropdown.formControl.component.scss']
})
export class DropdownFormControlComponent implements OnChanges{
  @Input() data: DropdownFormControl = null;
  @Input() form: FormGroup = null;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes["data"] !== undefined && changes["data"].currentValue !== changes["data"].previousValue && changes["data"].firstChange) {
      if(this.data.onInit){
        this.data.onInit(this.data, this.form);
      }
    }

    if (changes["data"] !== undefined) {
      this.form.controls[this.data.controlName].valueChanges.subscribe((value)=>{
        this.data.onChange(value, this.form);
      })
    }
  }
}
