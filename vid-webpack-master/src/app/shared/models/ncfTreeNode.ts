import {TreeNodeModel} from "./treeNodeModel";
import {Level1Model} from "./nodeModel";
import {Level1Instance} from "./level1Instance";


export class NcfTreeNode extends TreeNodeModel {
  storeKey : string;
  typeName: string;
  menuActions: { [p: string]: { method: Function; visible: Function; enable: Function } };
  isFailed: boolean;
  statusMessage?: string;
  instanceGroupRole: string;
  instanceGroupFunction: string;
  numberOfNetworks: number;
  modelVersion: string;

  constructor(instance: Level1Instance, ncfModel: Level1Model, storeKey: string, modelVersion: string) {
    super(instance, ncfModel);
    this.name = instance.instanceName? instance.instanceName: !ncfModel.isEcompGeneratedNaming ? ncfModel.modelCustomizationName : '&lt;Automatically Assigned&gt;';
    this.modelName = ncfModel.modelCustomizationName;
    this.type = ncfModel.type;
    this.isEcompGeneratedNaming = ncfModel.isEcompGeneratedNaming;
    this.storeKey = storeKey;
    this.instanceGroupRole = instance['instanceGroupRole'];
    this.instanceGroupFunction = instance['instanceGroupFunction'];
    this.numberOfNetworks = instance['numberOfNetworks'];
    this.modelVersion = modelVersion;
  }
}
