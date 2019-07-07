import {Level1Model} from "./nodeModel";
import {TreeNodeModel} from "./treeNodeModel";
import {PnfInstance} from "./pnfInstance";

export class PnfTreeNode extends TreeNodeModel{
  pnfStoreKey : string;
  typeName: string;
  menuActions: { [p: string]: { method: Function; visible: Function; enable: Function } };
  isFailed: boolean;
  statusMessage?: string;

  constructor(instance: PnfInstance, pnfModel: Level1Model, pnfStoreKey : string){
    super(instance, pnfModel);
    this.type = pnfModel.type;
    this.pnfStoreKey = pnfStoreKey;

  this.name = instance.instanceName? instance.instanceName: !pnfModel.isEcompGeneratedNaming ? pnfModel.modelCustomizationName : '&lt;Automatically Assigned&gt;';
    this.modelName = pnfModel.modelCustomizationName;
    this.isEcompGeneratedNaming = pnfModel.isEcompGeneratedNaming;
  }
}
