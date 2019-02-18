import {Injectable} from "@angular/core";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../shared/store/reducers";
import {AaiService} from "../../../shared/services/aaiService/aai.service";
import {Subject} from "rxjs";
import {ComponentInfoModel, ComponentInfoType} from "./component-info-model";
import {ModelInformationItem} from "../../../shared/components/model-information/model-information.component";
import * as _ from 'lodash';
@Injectable()
export class ComponentInfoService {
  static triggerComponentInfoChange: Subject<ComponentInfoModel> = new Subject<ComponentInfoModel>();
  constructor( private _store: NgRedux<AppState>, private _aaiService : AaiService){ }

  getInfoForService(serviceModelId):ComponentInfoModel {
    if(_.isNil(this._store.getState().service.serviceHierarchy[serviceModelId])) return null;

    let serviceHierarchy = this._store.getState().service.serviceHierarchy[serviceModelId].service;
    const serviceInstance = this._store.getState().service.serviceInstance[serviceModelId];
    const modelInfoItems: ModelInformationItem[] = [
     ModelInformationItem.createInstance("Subscriber Name",this._aaiService.extractSubscriberNameBySubscriberId(serviceInstance.globalSubscriberId)),
     ModelInformationItem.createInstance("Service Type",serviceInstance.subscriptionServiceType),
     ModelInformationItem.createInstance("Service Role",serviceHierarchy.serviceRole),
    ];
    serviceHierarchy.type = serviceHierarchy.serviceType;
    return this.addGeneralInfoItems(modelInfoItems, ComponentInfoType.SERVICE, serviceHierarchy, serviceInstance );
  }


  addGeneralInfoItems(modelInfoSpecificItems: ModelInformationItem[], type: ComponentInfoType, model, instance) {
    let modelInfoItems: ModelInformationItem[] = [
      ModelInformationItem.createInstance("Type", (model && model.type) ? model.type : ((instance && instance.modelInfo) ? instance.modelInfo.modelType : null)),
      ModelInformationItem.createInstance("Model Version", model ? model.version : null),
      ModelInformationItem.createInstance("Model Customization ID", model ? model.customizationUuid : null),
      ModelInformationItem.createInstance("Instance ID", instance ? instance.instanceId : null),
      ModelInformationItem.createInstance("In Maintenance", instance? instance.inMaint : null),
    ];
    modelInfoItems = modelInfoItems.concat(modelInfoSpecificItems);
    const modelInfoItemsWithoutEmpty = _.filter(modelInfoItems, function(item){ return !item.values.every(_.isNil)});
    return new ComponentInfoModel(type, modelInfoItemsWithoutEmpty, []);
  }
}
