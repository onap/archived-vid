import {VfcInstanceGroupMap} from "./vfcInstanceGroupMap";

export interface NodeModelResponseInterface {
  customizationUuid: string;
  name: string;
  version: string;
  description: string;
  category: string;
  uuid: string;
  invariantUuid: string;
  max: number;
  min:number;
}
export interface Level1ModelResponseInterface extends NodeModelResponseInterface{
  serviceType: string;
  serviceRole: string;
  subCategory: string;
  customizationUuid: string;
  serviceEcompNaming: boolean;
  type: string;
  modelCustomizationName: string;
  vfcInstanceGroups: VfcInstanceGroupMap;
  properties: Level1ModelProperties;
}
export class NodeModel {
  name: string;
  version: string;
  description: string;
  category: string;
  uuid: string;
  invariantUuid: string;
  max: number;
  min: number;
  customizationUuid?: string;

  constructor(serviceJson?: NodeModelResponseInterface) {
    if (serviceJson) {
      this.customizationUuid = serviceJson.customizationUuid;
      this.name = serviceJson.name;
      this.version = serviceJson.version;
      this.description = serviceJson.description;
      this.category = serviceJson.category;
      this.uuid = serviceJson.uuid;
      this.invariantUuid = serviceJson.invariantUuid;
      this.max = serviceJson.max;
      this.min = serviceJson.min;
    }
  }

}
export class Level1ModelProperties {
  max_instances : number;
  min_instances : number;
}



export class Level1Model extends NodeModel{
  serviceType: string;
  serviceRole: string;
  subCategory: string;
  customizationUuid: string;
  serviceEcompNaming: boolean;
  type: string;
  modelCustomizationName: string;
  vfcInstanceGroups: VfcInstanceGroupMap;
  isEcompGeneratedNaming: boolean;
  constructor(nodeJson?: Level1ModelResponseInterface) {
    super(nodeJson);
    if (nodeJson) {
      this.serviceType = nodeJson.serviceType;
      this.serviceRole = nodeJson.serviceRole;
      this.subCategory = nodeJson.subCategory;
      this.customizationUuid = nodeJson.customizationUuid;
      this.isEcompGeneratedNaming = this.getIsEcompGeneratedNaming(nodeJson);
      this.type = nodeJson.type;
      this.modelCustomizationName = nodeJson.modelCustomizationName;
      this.vfcInstanceGroups = nodeJson.vfcInstanceGroups;
      this.max = 1;
      this.min = 0;
      if (nodeJson.properties) {
        this.min = nodeJson.properties.min_instances || 0;
        this.max = nodeJson.properties.max_instances || 1000;
      }


    }
  }
  private getIsEcompGeneratedNaming(vnfJson) {
    let ecompGeneratedNaming;
    if (vnfJson.properties) {
      ecompGeneratedNaming = vnfJson.properties.ecomp_generated_naming;
    }
    return ecompGeneratedNaming === "true";
  };
}
