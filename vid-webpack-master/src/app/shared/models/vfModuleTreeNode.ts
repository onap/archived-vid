import {VfModule} from "./vfModule";
import {VfModuleInstance} from "./vfModuleInstance";
import {ServiceNodeTypes} from "./ServiceNodeTypes";

export class VfModuleTreeNode {
  modelId: string;
  name: string;
  modelName: string;
  type: string;

  constructor(vfModuleInstance: VfModuleInstance, vfModuleModel: VfModule, vfModuleModelName: string){
    this.name = vfModuleInstance.instanceName || vfModuleInstance.volumeGroupName || '<Automatically Assigned>';
    this.modelId = vfModuleModel.uuid;
    this.modelName = vfModuleModelName;
    this.type = ServiceNodeTypes.VFmodule;
  }
}
