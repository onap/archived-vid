import {Injectable} from "@angular/core";
import {ServiceNodeTypeToModelKeyMapper} from "../../shared/models/serviceNodeTypeToModelKeyMapper";
import {ServiceNodeTypes} from "../../shared/models/ServiceNodeTypes";
import {VfModule} from "../../shared/models/vfModule";
import {ServicePlanningService} from "../../services/service-planning.service";
import {VNFModel} from "../../shared/models/vnfModel";
import * as _ from 'lodash';
import {isNullOrUndefined} from "util";
import {NumbersLettersUnderscoreValidator} from '../../shared/components/validators/numbersLettersUnderscore/numbersLettersUnderscore.validator';
import {FormGroup} from '@angular/forms';
import {VnfInstanceDetailsComponent} from './vnf-instance-details/vnf-instance-details.component';
import {VnfInstanceDetailsService} from './vnf-instance-details/vnf-instance-details.service';

@Injectable()
export class VnfPopupService {

  constructor(private _servicePlanningService : ServicePlanningService, private _vnfInstanceDetailsService : VnfInstanceDetailsService) {
  }

  public getModelFromResponse(result : any, modelType : string, modelName:string) {
    let model = null;
    let rawModel = _.get(result, [ServiceNodeTypeToModelKeyMapper[modelType], modelName]);
    if (!rawModel) return;

    if (modelType === ServiceNodeTypes.VFmodule) {
      model = new VfModule(rawModel);
    }
    else {
      model = new VNFModel(rawModel);
    }
    return model;
  }

  onControlError(servicePopupDataModel : VnfInstanceDetailsComponent, serviceInstanceDetailsFormGroup : FormGroup, isNotUniqueInstanceName : boolean, isNotUniqueVolumeGroupName : boolean) : boolean{
    if(this._vnfInstanceDetailsService.hasUniqueError(isNotUniqueInstanceName) || isNotUniqueVolumeGroupName){
      return true;
    }
    if(!isNullOrUndefined(serviceInstanceDetailsFormGroup.controls['instanceName']) && NumbersLettersUnderscoreValidator.valid(serviceInstanceDetailsFormGroup.controls['instanceName'].value) && serviceInstanceDetailsFormGroup.controls['instanceName'].value != null && serviceInstanceDetailsFormGroup.controls['instanceName'].value.length > 0){
      return true;
    }

    const controlName : Array<string> =  ['lcpCloudRegionId', 'tenantId', 'lineOfBusiness', 'platformName'];
    const selectDataName : Array<string> = ['lcpRegions', 'tenants', 'lineOfBusinesses', 'platforms', 'projects'];

    for(let i = 0 ; i < controlName.length ; i++){
      if (!isNullOrUndefined(servicePopupDataModel.vnfPopupDataModel) && !isNullOrUndefined(servicePopupDataModel.vnfPopupDataModel[selectDataName[i]])) {
        if (!isNullOrUndefined(serviceInstanceDetailsFormGroup.controls[controlName[i]]) && !serviceInstanceDetailsFormGroup.controls[controlName[i]].disabled && servicePopupDataModel.vnfPopupDataModel[selectDataName[i]].length === 0) {
          return true;
        }
      }
    }
    return false;
  }

}
