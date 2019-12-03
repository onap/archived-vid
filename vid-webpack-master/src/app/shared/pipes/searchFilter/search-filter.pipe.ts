import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'searchFilter'
})
export class SearchFilterPipe implements PipeTransform {
  transform(items: Object[], searchText: string): any[] {
    if(!items) return [];
    if(!searchText) return items;
    return items.filter( item => {
      return JSON.stringify(item).toLowerCase().includes(searchText.toLowerCase());
    });
  }
}
