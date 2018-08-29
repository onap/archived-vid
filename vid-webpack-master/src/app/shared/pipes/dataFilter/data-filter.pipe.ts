/**
 * Created by cp2122 on 1/4/2018.
 */
import { Pipe, PipeTransform } from '@angular/core';
import * as _ from 'lodash';

@Pipe({
  name: 'dataFilter'
})
export class DataFilterPipe implements PipeTransform {

  transform(items: any, searchStr: string, keys?: string[][]): any {
    if (items != null && items.length > 0 && !_.isNil(searchStr)) {
      let ans = [];

      if (_.isNil(keys) || keys.length === 0) {
        keys = Object.keys(items[0]).map((key)=> new Array(key) );
      }
      for (const item of items) {
        for(const key of keys) {

          let val: string = DataFilterPipe.getDeepObjectValueByKeys(item, key);
          if (!_.isNil(val) && val.toLowerCase().includes(searchStr.toLowerCase())) {
            ans.push(item);
            break;
          }
        }
      }
      return ans;
    }
  }
  /**********************************************************************
   get value from obj data by array of keys.
   @keys: all table column and keys
   @rowData : row data
   ************************************************************************/
  static getDeepObjectValueByKeys(rowData: any , keys: string[]) : string {
    let obj =  rowData[keys[0]];
    if(_.isNil(obj)) {
      return obj;
    }
    for(let i = 1; i < keys.length ; i++){
      obj = obj[keys[i]];
    }
    return obj.toString();
  }
}
