import {Injectable} from "@angular/core";
import {MultiselectFormControl} from "../../../../models/formControlModels/multiselectFormControl.model";
import {MultiSelectItem} from "./multiselect.model";
import * as _ from "lodash";


@Injectable()
export class MultiselectFormControlService {

  convertOriginalItems = (data : MultiselectFormControl) : Promise<MultiSelectItem[]> => {
    return new Promise<MultiSelectItem[]>((resolve) =>{
      let result: MultiSelectItem[] = [];
      if(data.options$) {
        let index: number = 1;
        data.options$.map((originalItems: any) => {
          result.push(new MultiSelectItem(index, originalItems[data.ngValue], originalItems[data.selectedFieldName]));
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

  convertSelectedItems(data : MultiselectFormControl) : Promise<MultiSelectItem[]>{
    return new Promise<MultiSelectItem[]>((resolve) =>{
      let result: MultiSelectItem[] = [];
      const hashMap = this.convertOptionsToHashMap(data);

      if(data.options$ && data.value) {
        data.value.map((itemId) => {
          const uniqueIdentifier = itemId.trim();
          result.push(new MultiSelectItem(hashMap[uniqueIdentifier].index, hashMap[uniqueIdentifier][data.ngValue], hashMap[uniqueIdentifier][data.selectedFieldName]));
        });
      }
      resolve(result);
    });
  }
}
