import {NodeModel, NodeModelResponseInterface} from "./nodeModel";
import {VidNotions} from "./vidNotions";


export interface ServiceModelResponseInterface extends NodeModelResponseInterface{

  serviceType: string;
  serviceRole: string;
  serviceEcompNaming: boolean;
  vidNotions: any;
}

export class ServiceModel extends NodeModel{

  serviceType: string;
  serviceRole: string;
  servicesQty: number;
  isEcompGeneratedNaming: boolean;
  isMultiStepDesign: boolean;
  vidNotions?: VidNotions;

  constructor(serviceModelJson?: any){
    super(serviceModelJson.service);
    if (serviceModelJson) {
      const service: ServiceModelResponseInterface = serviceModelJson.service;
      this.serviceType = service.serviceType;
      this.serviceRole = service.serviceRole;
      this.vidNotions= service.vidNotions;
      this.isEcompGeneratedNaming = this.getServiceEcompNaming(service);
      this.isMultiStepDesign = this.getIsMultiStepDesign(serviceModelJson);
    }
  }

  private getServiceEcompNaming(serviceJson): boolean {
    return serviceJson.serviceEcompNaming === "true";
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
