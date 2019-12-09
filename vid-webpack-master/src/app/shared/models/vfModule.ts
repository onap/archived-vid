import {NodeModel, NodeModelResponseInterface} from "./nodeModel";


export interface Properties{
  initialCount: number;
  maxCountInstances: number;
  minCountInstances: number;
}

export interface VFModuleResponseInterface extends NodeModelResponseInterface {
  customizationUuid: string;
  modelCustomizationName: string;
  volumeGroupAllowed : boolean;
  properties: Properties
}

export class VfModule extends NodeModel {

  vgName:string;
  rollbackOnFailure:boolean;
  initial:number;
  customizationUuid: string;
  modelCustomizationName: string;
  volumeGroupAllowed : boolean;

  constructor(vf?: VFModuleResponseInterface) {
    super(vf);
    if(vf){
      this.customizationUuid = vf.customizationUuid;
      this.modelCustomizationName = vf.modelCustomizationName;
      this.volumeGroupAllowed = vf.volumeGroupAllowed || false;
    }
    if (vf && vf.properties) {
      this.min = vf.properties.minCountInstances || 0;
      this.max = vf.properties.maxCountInstances || 1000;
      this.initial = vf.properties.initialCount;
      this.rollbackOnFailure = true
    }
  }
}
