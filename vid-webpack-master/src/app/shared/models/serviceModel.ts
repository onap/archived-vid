import {NodeModel, NodeModelResponseInterface} from "./nodeModel";
import * as _ from "lodash";


export interface ServiceModelResponseInterface extends NodeModelResponseInterface{

  serviceType: string;
  serviceRole: string;
  serviceEcompNaming: boolean;
}

export class ServiceModel extends NodeModel{

  serviceType: string;
  serviceRole: string;
  servicesQty: number;
  isUserProvidedNaming: boolean;
  isMultiStepDesign: boolean;

  constructor(serviceModelJson?: any){
    super(serviceModelJson.service);
    if (serviceModelJson) {
      const service: ServiceModelResponseInterface = serviceModelJson.service;
      this.serviceType = service.serviceType;
      this.serviceRole = service.serviceRole;
      this.isUserProvidedNaming = this.getIsUserProvidedName(service);
      this.isMultiStepDesign = this.getIsMultiStepDesign(serviceModelJson);
    }
  }

  private getIsUserProvidedName(serviceJson): boolean {
    return serviceJson.serviceEcompNaming !== undefined && serviceJson.serviceEcompNaming === "false";
  };

  private getIsMultiStepDesign(serviceModel): boolean {
    for (let key in serviceModel.vnfs) {
      const vnf = serviceModel.vnfs[key];
      if (vnf.properties.multi_stage_design === "true") {
        return true
      }
    }
    return false;
  }
}
