import {Injectable} from "@angular/core";
import {ModelInformationItem} from "../model-information/model-information.component";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../store/reducers";
import {Subscriber} from "../../models/subscriber";
import {ServiceInfoModel} from "../../server/serviceInfo/serviceInfo.model";
import * as _ from 'lodash';
import {AuditStatus} from "../../server/serviceInfo/AuditStatus.model";
import {AuditInformationItem} from "../../models/auditInfoControlModels/auditInformationItems.model";

@Injectable()
export class AuditInfoModalComponentService {

  constructor(private _store: NgRedux<AppState>){}

  getModelInfo(model, instance, serviceModelId: string): ModelInformationItem[] {
    const serviceInstance = this._store.getState().service.serviceInstance[serviceModelId];
    const modelInformation = !_.isNil(model) ? [
      new ModelInformationItem('Model name', 'model_name', [model.name]),
      new ModelInformationItem('Model version', 'model_version', [model.version]),
      new ModelInformationItem('Model customization ID', 'model_customization_ID', [model.customizationUuid]),
      new ModelInformationItem('Base module', 'base_module', [!_.isNil(model.properties) ? model.properties.baseModule : null])
    ] : [];

    const instanceInfo = !_.isNil(instance) ? [
      new ModelInformationItem('Instance name', 'instance_name', [instance.instanceName]),
      new ModelInformationItem('Instance ID', 'instance_ID', [instance.instanceId])
    ] : [];

    const serviceInfo = !_.isNil(serviceInstance) ? [
      new ModelInformationItem("Subscriber name", "subscriberName", [this.extractSubscriberNameBySubscriberId(serviceInstance.globalSubscriberId, this._store)]),
      new ModelInformationItem('Service type', 'service_type', [serviceInstance.subscriptionServiceType])
    ] : [];

    const result = [modelInformation, instanceInfo, serviceInfo];
    return _.uniq(_.flatten(result));
  }

  static createModelInformationItemsJob(serviceModel: ServiceInfoModel): ModelInformationItem[] {
    return [
      new ModelInformationItem('Subscriber name', 'subscriberName', [serviceModel.subscriberName]),
      new ModelInformationItem('Service type', 'serviceType', [serviceModel.serviceType]),
      new ModelInformationItem('Service model version', 'serviceModelVersion', [serviceModel.serviceModelVersion]),
      new ModelInformationItem('Service instance name', 'serviceInstanceName', [serviceModel.serviceInstanceName || 'Automatically generated'], '', true),
      new ModelInformationItem('Service instance ID', 'serviceInstanceId', [serviceModel.serviceInstanceId]),
      new ModelInformationItem('Requestor User ID', 'userId', [serviceModel.userId]),
    ];
  }

  static getInstanceModelName(model) : string {
    return !_.isNil(model) && model.name ? model.name : '';
  }

  static setModalTitlesType(type : string) : string {
    return !_.isNil(type) ? AuditInfoModalComponentService.getTypeMap()[type] : 'Service' ;
  }

  static getTypeMap(){
    return {
      SERVICE : 'Service',
      VNFGROUP : 'Vnf Group',
      NETWORK : 'Network',
      VFMODULE : 'VfModule',
      VNF : 'VNF'
    };
  }

  static setModalTitle(type : string) : string {
    return !_.isNil(type) ? (AuditInfoModalComponentService.getTypeMap()[type] + " Instantiation Information") : 'Service Instantiation Information';
  }

  private extractSubscriberNameBySubscriberId(subscriberId: string, store: NgRedux<AppState>) {
    let result: string = null;
    let filteredArray: any = _.filter(store.getState().service.subscribers, function (o: Subscriber) {
      return o.id === subscriberId
    });
    if (filteredArray.length > 0) {
      result = filteredArray[0].name;
    }
    return result;
  }

  static initAuditInfoItems() {
    return new AuditInformationItem(
      true,
      false,
      [],
      [],
      true,
      false,
      false,
      null ,
      null
    )
  }
}
