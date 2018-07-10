/**
 * Created by cp2122 on 1/4/2018.
 */
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dataFilter'
})
export class DataFilterPipe implements PipeTransform {
  keys = [];
  transform(items: any, args: string): any {
    if (items != null && items.length > 0) {
      let ans = [];

      if (this.keys.length === 0) {
        this.keys = Object.keys(items[0]);
      }
      for (let i of items) {
        for (let k of this.keys) {
          if (i[k] !== null && i[k].toString().match('^.*' + args + '.*$')) {
            ans.push(i);
            break;
          }
        }
      }
      return ans;
    }
  }
}
