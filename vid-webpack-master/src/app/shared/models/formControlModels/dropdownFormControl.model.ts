import {FormControlModel} from "./formControl.model";
import {FormGroup} from "@angular/forms";
import {FormControlType} from "./formControlTypes.enum";
import {Observable} from "rxjs";

export class DropdownFormControl extends FormControlModel{
  options$ : Observable<any[]> = null;
  args : string[];
  onInit: (data : DropdownFormControl, form: FormGroup) => Observable<any>;
  selectedField : string;
  onInitSelectedField : string[]; // key that should select from API response.
  ngValue : string;
  name : string;
  hasEmptyOptions : boolean; // get empty result from API or REDUX.


  constructor(data) {
    super(data);
    this.type = FormControlType.DROPDOWN;
    this.options$ = data.options ? data.options$ : null;
    this.onInit = data.onInit;
    this.selectedField = data.selectedField;
    this.onInitSelectedField = data.onInitSelectedField ? data.onInitSelectedField : null;
    this.ngValue = data.selectedField ? data.selectedField : 'id';
    this.name = data.name;
    this.hasEmptyOptions = false;
  }

}
