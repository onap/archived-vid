import {Component, Input, OnChanges, SimpleChanges} from "@angular/core";
import {FormGroup} from "@angular/forms";
import {MultiselectFormControl} from "../../../../models/formControlModels/multiselectFormControl.model";
import {MultiselectFormControlService} from "./multiselect.formControl.service";
import {MultiSelectItem} from "./multiselect.model";

@Component({
  selector: 'multiselect-form-control',
  templateUrl: './multiselect.formControl.component.html'
})
export class MultiselectFormControlComponent implements OnChanges{
  @Input() data: MultiselectFormControl = null;
  @Input() multiselectOptions: [] = null;
  @Input() selectedItems  = [];
  @Input() form: FormGroup = null;


  multiselectFormControlService : MultiselectFormControlService;
  constructor(private _multiselectFormControlService : MultiselectFormControlService){
    this.multiselectFormControlService = _multiselectFormControlService;
  }

  dropdownSettings = {
    singleSelection : false,
    limitSelection : 1000
  };

  options : MultiSelectItem[];



  async ngOnChanges(changes: SimpleChanges) {
    if(this.data.options$){
      this._multiselectFormControlService.convertOriginalItems(this.data).then((options)=>{
          this.options = options;
          this._multiselectFormControlService.convertSelectedItems(this.data).then((res)=> {
            this.selectedItems = res;
            this.data.onChange(this.selectedItems ,this.form);
          })
      });
    }
    if (changes["data"] !== undefined && changes["data"].currentValue !== changes["data"].previousValue && changes["data"].firstChange) {
      if (this.data.onInit) {
        this.dropdownSettings.limitSelection = this.data.limitSelection;
        this.data.onInit(this.data, this.form);
      }
    }
  }

  onItemSelect(item:any){
    this.data.onChange(this.selectedItems ,this.form);
  }
  OnItemDeSelect(item:any){
    this.data.onChange(this.selectedItems ,this.form);
  }
  onSelectAll(items: any){
    this.data.onChange(this.selectedItems ,this.form);
  }
  onDeSelectAll(items: any){
    this.data.onChange(this.selectedItems ,this.form);
  }
}

