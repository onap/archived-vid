import {Component, Input, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {DynamicInput, DynamicMultiSelect, DynamicNumber, DynamicSelect} from "../../shared/models/dynamicInput";

@Component({
  selector: 'dynamic-inputs',
  templateUrl: './dynamic-inputs.html',
  styleUrls: ['./dynamic-inputs.scss']
})

export class DynamicInputsComponent implements OnInit{
  @Input() public list:any[] = [];
  @Input() public group: FormGroup;

  private dynamicList:DynamicInput<any>[] = [];

  isDynamicNumber(item: any): item is DynamicNumber {
    return item;
  }

  buildValidators(item: DynamicInput<any>) {
    let validatorArr = [];
    item.maxLength && validatorArr.push(Validators.maxLength(item.maxLength));
    item.minLength && validatorArr.push(Validators.minLength(item.minLength));
    if(this.isDynamicNumber(item)) {
      item.max && validatorArr.push(Validators.max(item.max));
      item.min && validatorArr.push(Validators.min(item.min));
    }
    return Validators.compose(validatorArr);
  }

  ngOnInit(): void {
    this.list.forEach((item)=> {
      let dynamicItem: DynamicInput<any>;
      switch (item.type) {
        case 'multi_select':
          item.optionList.forEach(function(option) { option.id = option.id||option.name;
          option.itemName = option.name;});
          item.settings = {
            disabled: !item.isEnabled,
            text: item.prompt,
          };
          dynamicItem = new DynamicMultiSelect(item);
          break;
        case 'select':
          let defaultValue:any = item.optionList.find((option) => option.isDataLoading && option.name);
          item.value = defaultValue && defaultValue.id;
          dynamicItem = new DynamicSelect(item);
          break;
        case 'boolean':
          item.value = item.value || false;
          item.optionList = [{name: true, isPermitted: true, isDataLoading: item.value}, {name: false, isPermitted: true, isDataLoading: !item.value}];

          dynamicItem = new DynamicSelect(item);
          break;
        case 'number':
          dynamicItem = new DynamicNumber(item);
          break;
        case 'file':
          dynamicItem = new DynamicInput<any>(item);
          break;
        case 'checkbox':
          dynamicItem = new DynamicInput<boolean>(item);
          break;
        case 'map':
          item.prompt = "{<key1>: <value1>,\.\.\.,<keyN>: <valueN>}";
          dynamicItem = new DynamicInput<string>(item);
          break;
        case 'list':
          item.prompt = "[<value1>,...,<valueN>]";
          dynamicItem = new DynamicInput<string>(item);
          break;
        default: dynamicItem = new DynamicInput<string>(item);
      }
      this.dynamicList.push(dynamicItem);
      this.group.addControl(dynamicItem.name, new FormControl({value: dynamicItem.value, disabled: !dynamicItem.isEnabled}, this.buildValidators(dynamicItem)));
    })
  }

}
