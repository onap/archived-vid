import {NodeModel, NodeModelResponseInterface} from "./nodeModel";
import {VfcInstanceGroupMap} from "./vfcInstanceGroupMap";


export interface VnfProperties {
  ecomp_generated_naming: string
}

export interface VNFModelResponseInterface extends NodeModelResponseInterface{

  serviceType: string;
  serviceRole: string;
  subCategory: string;
  customizationUuid: string;
  serviceEcompNaming: boolean;
  type: string;
  modelCustomizationName: string;
  properties: VnfProperties;
  vfcInstanceGroups: VfcInstanceGroupMap;
}

export class VNFModel extends NodeModel{

  serviceType: string;
  serviceRole: string;
  subCategory: string;
  customizationUuid: string;
  isUserProvidedNaming: boolean;
  type: string;
  modelCustomizationName: string;
  vfcInstanceGroups: VfcInstanceGroupMap;

  constructor(vnfJson?: VNFModelResponseInterface){
    super(vnfJson);
    if (vnfJson) {
      this.serviceType = vnfJson.serviceType;
      this.serviceRole = vnfJson.serviceRole;
      this.subCategory = vnfJson.subCategory;
      this.customizationUuid = vnfJson.customizationUuid;
      this.isUserProvidedNaming = this.getIsUserProvidedName(vnfJson);
      this.type = vnfJson.type;
      this.modelCustomizationName = vnfJson.modelCustomizationName;
      this.vfcInstanceGroups = vnfJson.vfcInstanceGroups;

    }
  }

  private getIsUserProvidedName(vnfJson) {
    const ecompGeneratedNaming = vnfJson.properties.ecomp_generated_naming;
    return ecompGeneratedNaming !== undefined && ecompGeneratedNaming === "false";
  };
}
