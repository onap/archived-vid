import {Injectable} from "@angular/core";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../shared/store/reducers";
import {AaiService} from "../../../shared/services/aaiService/aai.service";
import {Subject} from "rxjs";
import {ComponentInfoModel, ComponentInfoType} from "./component-info-model";
import {ModelInformationItem} from "../../../shared/components/model-information/model-information.component";
import * as _ from 'lodash';
import {SharedTreeService} from "../objectsToTree/shared.tree.service";
@Injectable()
export class ComponentInfoService {
  static triggerComponentInfoChange: Subject<ComponentInfoModel> = new Subject<ComponentInfoModel>();
  constructor( private _store: NgRedux<AppState>, private _aaiService : AaiService, private _sharedTreeService : SharedTreeService){ }

  getInfoForService(serviceModelId):ComponentInfoModel {
    if(_.isNil(this._store.getState().service.serviceHierarchy[serviceModelId])) return null;

    const serviceHierarchy = this._store.getState().service.serviceHierarchy[serviceModelId].service;
    const serviceInstance = this._store.getState().service.serviceInstance[serviceModelId];
    const modelInfoItems: ModelInformationItem[] = [
       ModelInformationItem.createInstance("Type", serviceHierarchy.serviceType),
       ModelInformationItem.createInstance("Model version", serviceHierarchy.version ),
       ModelInformationItem.createInstance("Model customization ID", serviceHierarchy.customizationUuid ),
       ModelInformationItem.createInstance("Instance ID", serviceInstance.instanceId),
       ModelInformationItem.createInstance("Subscriber name",this._aaiService.extractSubscriberNameBySubscriberId(serviceInstance.globalSubscriberId)),
       ModelInformationItem.createInstance("Service type",serviceInstance.subscriptionServiceType),
       ModelInformationItem.createInstance("Service role",serviceHierarchy.serviceRole),
    ];

    return this._sharedTreeService.getComponentInfoModelByModelInformationItems(modelInfoItems, ComponentInfoType.SERVICE, serviceInstance );
  }
}
