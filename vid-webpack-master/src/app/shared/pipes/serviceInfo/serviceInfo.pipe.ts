import {PipeTransform, Pipe} from '@angular/core';
import {isNullOrUndefined} from "util";

@Pipe({ name: 'serviceInfo'})
export class ServiceInfoPipe implements PipeTransform {
  transform(service: string, store : any , modelId : string, fieldName : string): string {
    const serviceHierarchy = store.getState().service.serviceHierarchy;
    if(!isNullOrUndefined(serviceHierarchy) && !isNullOrUndefined(serviceHierarchy[modelId])){
      return serviceHierarchy[modelId].service[fieldName] || null;
    }
    return null;
  }
}
