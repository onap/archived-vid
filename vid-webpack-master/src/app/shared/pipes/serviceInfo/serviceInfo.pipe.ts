import {PipeTransform, Pipe} from '@angular/core';
import * as _ from 'lodash';

@Pipe({ name: 'serviceInfo'})
export class ServiceInfoPipe implements PipeTransform {
  transform(service: string, store : any , modelId : string, fieldName : string): string {
    const serviceHierarchy = store.getState().service.serviceHierarchy;
    if(!_.isNil(serviceHierarchy) && !_.isNil(serviceHierarchy[modelId])){
      return serviceHierarchy[modelId].service[fieldName] || null;
    }
    return null;
  }
}
