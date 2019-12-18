import {NodeModel, NodeModelResponseInterface} from "./nodeModel";
import {Utils} from "../utils/utils";


export interface Properties{
  initialCount: number;
  maxCountInstances: number;
  minCountInstances: number;
  baseModule: boolean;
}

export interface VFModuleResponseInterface extends NodeModelResponseInterface {
  customizationUuid: string;
  modelCustomizationName: string;
  volumeGroupAllowed : boolean;
  properties: Properties;
  inputs: any;
}

export class VfModule extends NodeModel {

  rollbackOnFailure:boolean;
  initial:number;
  customizationUuid: string;
  modelCustomizationName: string;
  volumeGroupAllowed : boolean;
  baseModule : boolean;
  inputs: any;

  constructor(vf?: VFModuleResponseInterface, flags?: { [key: string]: boolean }) {
    super(vf);
    if(vf){
      this.customizationUuid = vf.customizationUuid;
      this.modelCustomizationName = vf.modelCustomizationName;
      this.volumeGroupAllowed = vf.volumeGroupAllowed || false;
      this.inputs = vf.inputs;
    }
    if (vf && vf.properties) {
      this.min = vf.properties.minCountInstances;
      this.max = Utils.getMaxVfModule(vf.properties, flags);
      this.initial = vf.properties.initialCount;
      this.rollbackOnFailure = true;
      this.baseModule = vf.properties.baseModule;
    }
  }
}
