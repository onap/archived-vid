import {Injectable} from '@angular/core';
import {isNullOrUndefined} from "util";
import {NumbersLettersUnderscoreValidator} from '../../shared/components/validators/numbersLettersUnderscore/numbersLettersUnderscore.validator';
import {ServiceInstanceDetailsComponent} from './service-instance-details/service-instance-details.component';
import {FormGroup} from '@angular/forms';
import * as _ from "lodash";

@Injectable()
export class ServicePopupService {
  onControlError(serviceInstanceDetails : ServiceInstanceDetailsComponent, serviceInstanceDetailsFormGroup : FormGroup) : boolean{
    if(!isNullOrUndefined(serviceInstanceDetailsFormGroup) && !isNullOrUndefined(serviceInstanceDetailsFormGroup.controls['instanceName']) && NumbersLettersUnderscoreValidator.valid(serviceInstanceDetailsFormGroup.controls['instanceName'].value) && serviceInstanceDetailsFormGroup.controls['instanceName'].value != null && serviceInstanceDetailsFormGroup.controls['instanceName'].value.length > 0){
      return true;
    }

    const controlName : Array<string> =  ['productFamilyId', 'lcpCloudRegionId', 'tenantId', 'owningEntityId', 'projectName', 'aicZoneId', 'subscriptionServiceType', 'globalSubscriberId',  'rollbackOnFailure'];
    const selectDataName : Array<string> = ['productFamilies', 'lcpRegions', 'tenants', 'owningEntities', 'projects', 'aicZones', 'serviceTypes', 'subscribers', 'rollbackOnFailure'];
    for(let i = 0 ; i < controlName.length ; i++){
      if (!isNullOrUndefined(serviceInstanceDetails.servicePopupDataModel) && !isNullOrUndefined(serviceInstanceDetails.servicePopupDataModel[selectDataName[i]])) {
        if (!serviceInstanceDetailsFormGroup.controls[controlName[i]].disabled && serviceInstanceDetails.servicePopupDataModel[selectDataName[i]].length === 0) {
          return true;
        }
      }
    }
    return false;
  }

  resetDynamicInputs(serviceInstance : any, defaultDynamicInputs : any) : void {
    for(let dynamicInput of serviceInstance.dynamicInputs){
      const defaultDymanicInput = _.find(defaultDynamicInputs, {name:dynamicInput.name});
      serviceInstance.serviceInstanceDetailsFormGroup.controls[dynamicInput.name].setValue(defaultDymanicInput.value);
    }
  }
}
