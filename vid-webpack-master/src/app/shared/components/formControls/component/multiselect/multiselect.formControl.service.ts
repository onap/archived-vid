import {Injectable} from "@angular/core";
import {MultiselectFormControl} from "../../../../models/formControlModels/multiselectFormControl.model";
import {MultiSelectItem} from "./multiselect.model";
import * as _ from "lodash";


@Injectable()
export class MultiselectFormControlService {

  convertOriginalItems = (control : MultiselectFormControl) : Promise<MultiSelectItem[]> => {
    return new Promise<MultiSelectItem[]>((resolve) =>{
      let result: MultiSelectItem[] = [];
      if(control.options$) {
        let index: number = 1;
        control.options$.map((originalItems: any) => {
          result.push(new MultiSelectItem(index, originalItems[control.ngValue], originalItems[control.selectedFieldName]));
          index++;
        });
      }
      resolve(result);
    })
  };


  convertOptionsToHashMap = (config : MultiselectFormControl) => {
    let index = 1;
    return _.reduce(config.options$ , (obj, param: any ) => {
      param.index = index;
      index++;
      obj[param[config.ngValue]] = param;
      return obj;
    }, {});
  };

  convertSelectedItems(control : MultiselectFormControl) : Promise<MultiSelectItem[]>{
    return new Promise<MultiSelectItem[]>((resolve) =>{
      let result: MultiSelectItem[] = [];
      const hashMap = this.convertOptionsToHashMap(control);

      if(control.options$ && control.value) {
        const convertArray = control.convertOriginalDataToArray ? control.convertOriginalDataToArray(control.value) : control.value;
        convertArray.map((itemId) => {
          const uniqueIdentifier = itemId.trim();
          result.push(new MultiSelectItem(hashMap[uniqueIdentifier].index, hashMap[uniqueIdentifier][control.ngValue], hashMap[uniqueIdentifier][control.selectedFieldName]));
        });
      }
      resolve(result);
    });
  }
}
