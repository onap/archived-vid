import {FormControlModel} from "./formControl.model";
import {Observable} from "rxjs";
import {FormGroup} from "@angular/forms";
import {FormControlType} from "./formControlTypes.enum";

export class MultiselectFormControl extends FormControlModel{
  options$ : Observable<any[]>;
  args : string[];
  onInit: (data : MultiselectFormControl, form: FormGroup) => Observable<any>;
  selectedItems :  any[];
  onInitSelectedItems : string[];
  selectedFieldName : string;
  ngValue : string;
  settings: {};
  onInitSelectedField?: string[];
  convertOriginalDataToArray? : (values)=> void;
  limitSelection?: number;


  constructor(data) {
    super(data);
    this.type = FormControlType.MULTI_SELECT;
    this.options$ = data.options;
    this.onInit = data.onInit;
    this.selectedItems = data.selectedItems || [];
    this.onInitSelectedItems = data.onInitSelectedItems ? data.onInitSelectedItems : null;
    this.ngValue = data.selectedField ? data.selectedField : 'id';
    this.selectedFieldName = data.selectedFieldName;
    this.settings = data.settings || {};
    this.onInitSelectedField = data.onInitSelectedField ? data.onInitSelectedField : null;
    this.convertOriginalDataToArray = data.convertOriginalDataToArray ? data.convertOriginalDataToArray : null
    this.limitSelection = data.limitSelection ? data.limitSelection : 1000;
  }

}
