import { Pipe, PipeTransform } from '@angular/core';
import * as _ from 'lodash';
@Pipe({  name: 'orderBy' })
export class OrderByPipe implements PipeTransform {

  transform(records: any[], args: any = {}): any {
    args.direction =  !_.isNil(args.direction) ? args.direction : 1;

    if(!_.isNil(records)){
      return records.sort(function(a, b){
        if(args.property){
          if(a[args.property] < b[args.property]){
            return -1 * args.direction;
          }
          else if( a[args.property] > b[args.property]){
            return 1 * args.direction;
          }
          else{
            return 0;
          }
        }else {
          if(a < b){
            return -1 * args.direction;
          }
          else if( a > b){
            return 1 * args.direction;
          }
          else{
            return 0;
          }
        }
      });
    }
  };
}
