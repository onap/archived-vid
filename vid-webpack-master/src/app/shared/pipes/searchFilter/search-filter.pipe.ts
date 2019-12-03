import {Pipe, PipeTransform} from '@angular/core';
import * as _ from 'lodash';

@Pipe({
  name: 'searchFilter'
})
export class SearchFilterPipe implements PipeTransform {
  transform(items: Object[], searchText: string): any[] {
    if (!items) return [];
    if (!searchText) return items;
    return items.filter((item: object) => {

      const deepFlatObject = this.flatten(item);

      const values = _.values(deepFlatObject).map((item: string) => {
        return item.toString().toLowerCase()
      });

      return _.some(values, _.method('includes', searchText.toLowerCase()));
    });
  }

  flatten = object => {
    return Object.assign(
      {},
      ...(function _flatten(objectBit, path = '') {
        //spread the result into our return object
        if(objectBit === null) return [];
        return [].concat(
          //concat everything into one level

          ...Object.keys(objectBit).map(
            //iterate over object
            key =>
              typeof objectBit[key] === 'object' //check if there is a nested object
                ? _flatten(objectBit[key], `${path}/${key}`) //call itself if there is
                : { [`${path}/${key}`]: objectBit[key] } //append object with it’s path as key
          )
        );
      })(object)
    );
  };
}
