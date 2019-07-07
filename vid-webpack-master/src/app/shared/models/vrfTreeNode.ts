import {TreeNodeModel} from "./treeNodeModel";
import {NetworkInstance} from "./networkInstance";
import {Level1Model} from "./nodeModel";
import {VrfInstance} from "./vrfInstance";

export class VrfTreeNode extends TreeNodeModel {
  vrfStoreKey : string;
  typeName: string;
  menuActions: { [p: string]: { method: Function; visible: Function; enable: Function } };
  isFailed: boolean;
  statusMessage?: string;

  constructor(instance: VrfInstance, vrfModel: Level1Model, vrfStoreKey : string){
    super(instance, vrfModel);
    this.name = instance.instanceName? instance.instanceName: '&lt;Automatically Assigned&gt;';
    this.modelName = vrfModel.modelCustomizationName;
    this.type = vrfModel.type;
    this.isEcompGeneratedNaming = vrfModel.isEcompGeneratedNaming;
    this.vrfStoreKey = vrfStoreKey;
  }
}

