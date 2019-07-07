import {TreeNodeModel} from "./treeNodeModel";
import {NetworkInstance} from "./networkInstance";
import {Level1Model} from "./nodeModel";

export class FirstLevelTreeNode extends TreeNodeModel {
  networkStoreKey: string;
  typeName: string;
  menuActions: { [p: string]: { method: Function; visible: Function; enable: Function } };
  isFailed: boolean;
  statusMessage?: string;

  constructor(instance: NetworkInstance, networkModel: Level1Model, networkStoreKey: string) {
    super(instance, networkModel);
    this.name = instance.instanceName ? instance.instanceName : !networkModel.isEcompGeneratedNaming ? networkModel.modelCustomizationName : '&lt;Automatically Assigned&gt;';
    this.modelName = networkModel.modelCustomizationName;
    this.type = networkModel.type;
    this.isEcompGeneratedNaming = networkModel.isEcompGeneratedNaming;
    this.networkStoreKey = networkStoreKey;
  }
}
