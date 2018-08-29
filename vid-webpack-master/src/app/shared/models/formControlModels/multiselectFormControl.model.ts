import {FormControlModel} from "./formControl.model";
import {Observable} from "rxjs";
import {FormGroup} from "@angular/forms";
import {FormControlType} from "./formControlTypes.enum";

export class MultiselectFormControl extends FormControlModel{
  options$ : Observable<any[]>;
  args : string[];
  onInit: (data : MultiselectFormControl, form: FormGroup) => Observable<any>;
  selectedItems : string;
  onInitSelectedItems : string[];
  ngValue : string;
  settings: {};


  constructor(data) {
    super(data);
    this.type = FormControlType.MULTI_SELECT;
    this.options$ = data.options;
    this.onInit = data.onInit;
    this.selectedItems = data.selectedItems;
    this.onInitSelectedItems = data.onInitSelectedItems ? data.onInitSelectedItems : null;
    this.ngValue = data.selectedField ? data.selectedField : 'id';
    this.settings = data.settings || {};
  }

}
