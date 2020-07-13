import {VfModule} from "./vfModule";
import {VfModuleInstance} from "./vfModuleInstance";
import {ServiceNodeTypes} from "./ServiceNodeTypes";
import {TreeLevel, TreeNodeModel} from "./treeNodeModel";

export class VfModuleTreeNode extends TreeNodeModel{
  dynamicInputs: any;
  dynamicModelName : string;
  typeName: string;
  menuActions: { [p: string]: { method: Function; visible: Function; enable: Function } };
  isFailed: boolean;
  statusMessage?: string;
  position: number;

  constructor(vfModuleInstance: VfModuleInstance, vfModuleModel: VfModule, vfModuleModelName: string, dynamicInputs: any, isEcompGeneratedNaming: boolean, dynamicModelName : string){
    super(vfModuleInstance,vfModuleModel);
    this.name = vfModuleInstance.instanceName || vfModuleInstance.volumeGroupName || '&lt;Automatically Assigned&gt;';
    this.modelName = vfModuleModelName;
    this.type = ServiceNodeTypes.VFmodule;
    this.isEcompGeneratedNaming = isEcompGeneratedNaming;
    this.dynamicInputs = dynamicInputs;
    this.dynamicModelName  = dynamicModelName;
  }
}
