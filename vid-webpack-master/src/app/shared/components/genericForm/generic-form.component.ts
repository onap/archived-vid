import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {GenericFormService} from "./generic-form.service";
import {FormControlModel} from "../../models/formControlModels/formControl.model";
import {FormGroup} from "@angular/forms";
import * as _ from 'lodash';

@Component({
  selector : 'generic-form',
  templateUrl : './generic-form.component.html',
  styleUrls : ['./generic-form.component.scss']
})

export class GenericFormComponent implements OnChanges{
  genericFormService: GenericFormService = null;
  dynamicFormGroup: FormGroup = null;

  @Input() formControls : FormControlModel[] = null;
  @Input() dynamicInputs : FormControlModel[] = null;
  @Input() isValidForm : boolean = false;
  @Output() onFormChanged  = new EventEmitter();

  constructor(private _genericFormService: GenericFormService){
    this.genericFormService = _genericFormService;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes["formControls"] !== undefined && changes["formControls"].currentValue !== changes["formControls"].previousValue) {
      this.dynamicFormGroup = this._genericFormService.generateFormBuilder(this.formControls, this.dynamicInputs);
      this.onFormChanged.next(this.dynamicFormGroup);
      this.dynamicFormGroup.valueChanges.subscribe(() => {
        this.onFormChanged.next(this.dynamicFormGroup);
      })
    }
  }

  hasApiError(controlName: string, data: any[], form: FormGroup) {
    if (!_.isNil(data)) {
      if (!form.controls[controlName].disabled && data.length === 0) {
        return true;
      }
    }
    return false;
  }
}

