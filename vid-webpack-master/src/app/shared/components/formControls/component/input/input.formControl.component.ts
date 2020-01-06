import {Component, ElementRef, Input, ViewChild} from "@angular/core";
import {InputFormControl} from "../../../../models/formControlModels/inputFormControl.model";
import {FormGroup} from "@angular/forms";

@Component({
  selector : 'form-control-input',
  templateUrl : './input.formControl.component.html',
  styleUrls : ['./input.formControl.component.scss']
})

export class InputFormControlComponent{
  @ViewChild('customInput', {static: false}) element:ElementRef;
  @Input() data: InputFormControl = null;
  @Input() form: FormGroup;



}
