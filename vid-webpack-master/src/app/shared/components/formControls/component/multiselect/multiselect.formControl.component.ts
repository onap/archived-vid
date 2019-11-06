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
  @Input() form: FormGroup = null;


  multiselectFormControlService : MultiselectFormControlService;
  constructor(private _multiselectFormControlService : MultiselectFormControlService){
    this.multiselectFormControlService = _multiselectFormControlService;
  }
  dropdownSettings = {
    singleSelection : false
  };
  selectedItems : MultiSelectItem[];
  options : MultiSelectItem[];



  async ngOnChanges(changes: SimpleChanges) {
    if(this.data.options$){
      this._multiselectFormControlService.convertOriginalItems(this.data).then((options)=>{
          this.options = options;
          this._multiselectFormControlService.convertSelectedItems(this.data).then((res)=> {
            this.selectedItems = res;
            this.form.controls[this.data.controlName].setValue(this.selectedItems);
          })
      });

    }
    if (changes["data"] !== undefined && changes["data"].currentValue !== changes["data"].previousValue && changes["data"].firstChange) {
      if (this.data.onInit) {
        this.data.onInit(this.data, this.form);
      }
    }
  }

  async onOpenMultiSelect() {
    console.log("onOpenMultiSelect");
    console.log(this.data.options$)
  }

  onItemSelect(item:any){
    console.log(item);
    console.log(this.form.controls[this.data.controlName].value);
    // this.selectedValuesArray.push((String(this.form.controls[this.data.controlName].value)).valueOf())
  }
  OnItemDeSelect(item:any){
    // console.log(item);
    // console.log(this.form.controls[this.data.controlName].value);
    // var indexToRemove = this.selectedValuesArray.findIndex(item)
    // this.selectedValuesArray.
  }
  onSelectAll(items: any){
    console.log(this.form.controls[this.data.controlName].value);
  }
  onDeSelectAll(items: any){
    console.log(this.form.controls[this.data.controlName].value);
    // this.selectedValuesArray = [];
  }
}

