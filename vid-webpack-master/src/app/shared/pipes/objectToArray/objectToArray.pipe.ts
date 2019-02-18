import { PipeTransform, Pipe } from '@angular/core';
import * as _ from 'lodash';
@Pipe({name: 'objecttoarray'})
export class ObjectToArrayPipe implements PipeTransform {
  transform(obj) : any {
    return _.values(obj);
  }
}
