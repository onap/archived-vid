import {NodeModel, NodeModelResponseInterface} from "./nodeModel";


export interface properties{
  initialCount: number;
  maxCountInstances: number;
  minCountInstances: number;
}

export interface VFModuleResponseInterface extends NodeModelResponseInterface {
  customizationUuid: string;
  modelCustomizationName: string;
  properties: properties
}

export class VfModule extends NodeModel {

  min:number;
  max:number;
  vgName:string;
  rollbackOnFailure:boolean;
  initial:number;
  customizationUuid: string;
  modelCustomizationName: string;

  constructor(vf?: VFModuleResponseInterface) {
    super(vf);
    if(vf){
      this.customizationUuid = vf.customizationUuid;
      this.modelCustomizationName = vf.modelCustomizationName;
    }
    if (vf && vf.properties) {
      this.min = vf.properties.minCountInstances;
      this.max = vf.properties.maxCountInstances;
      this.initial = vf.properties.initialCount;
      this.rollbackOnFailure = true
    }
  }
}
