import { Injectable } from '@angular/core';
import { isNullOrUndefined } from "util";
import { FormGroup } from '@angular/forms';
@Injectable()
export class VnfInstanceDetailsService {
  isUnique(serviceInstance : any, serviceId : string, name: string, isEqualToOriginalInstanceName : boolean) : boolean {
    const service = serviceInstance[serviceId];
    let countInstanceName = 0;
    let countVolumeGroupName = 0;
    if(service){
      if(service.instanceName === name) return false;
      if(service.vnfs){
        for(let key in service.vnfs){
          if(service.vnfs[key].instanceName === name) {
            countInstanceName++;
            if((isEqualToOriginalInstanceName && countInstanceName > 1) || (!isEqualToOriginalInstanceName)) return false;
          }
          if(service.vnfs[key].vfModules){
            for(let vfModule in service.vnfs[key].vfModules){
              if(service.vnfs[key].vfModules[vfModule]) {
                for(let module in service.vnfs[key].vfModules[vfModule]){
                  if(service.vnfs[key].vfModules[vfModule][module].instanceName === name ) {
                    countInstanceName++;
                    if((isEqualToOriginalInstanceName && countInstanceName > 1) || (!isEqualToOriginalInstanceName)) return false;
                  }

                  if(service.vnfs[key].vfModules[vfModule][module].volumeGroupName === name ) {
                    countVolumeGroupName++;
                    if((isEqualToOriginalInstanceName && countVolumeGroupName > 1) || (!isEqualToOriginalInstanceName)) return false;
                  }
                }
              }
            }
          }

        }
      }
    }
    return true;
  }

  hasApiError(controlName: string, data: Array<any>, form: FormGroup) {
    if (!isNullOrUndefined(data)) {
      if (!form.controls[controlName].disabled && data.length === 0) {
        return true;
      }
    }
    return false;
  }

  hasInstanceNameError(form : FormGroup) : boolean {
    if(!isNullOrUndefined(form) && !isNullOrUndefined(form.controls['instanceName'])){
      if (form.controls['instanceName'].touched && form.controls['instanceName'].errors && form.controls['instanceName'].errors.pattern) {
        return true;
      }
    }
    return false;
  }

  hasUniqueError(isNotUniqueInstanceName) : boolean {
    return isNotUniqueInstanceName;
  }

}
