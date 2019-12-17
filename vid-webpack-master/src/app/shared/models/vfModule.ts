import {NodeModel, NodeModelResponseInterface} from "./nodeModel";
import {Utils} from "../utils/utils";


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

  rollbackOnFailure:boolean;
  initial:number;
  customizationUuid: string;
  modelCustomizationName: string;
  volumeGroupAllowed : boolean;

  constructor(vf?: VFModuleResponseInterface, flags?: { [key: string]: boolean }) {
    super(vf);
    if(vf){
      this.customizationUuid = vf.customizationUuid;
      this.modelCustomizationName = vf.modelCustomizationName;
      this.volumeGroupAllowed = vf.volumeGroupAllowed || false;
    }
    if (vf && vf.properties) {
      this.min = vf.properties.minCountInstances;
      this.max = Utils.getMaxVfModule(vf.properties, flags);
      this.initial = vf.properties.initialCount;
      this.rollbackOnFailure = true
    }
  }
}
