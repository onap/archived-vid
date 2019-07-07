import {VfModuleTreeNode} from "./vfModuleTreeNode";
import {Level1Model} from "./nodeModel";
import {VnfInstance} from "./vnfInstance";
import {ServiceNodeTypes} from "./ServiceNodeTypes";
import {FirstLevelTreeNode} from "./firstLevelTreeNode";

export class VnfTreeNode extends FirstLevelTreeNode {

  children: VfModuleTreeNode[];
  vnfStoreKey : string;

  constructor(instance: VnfInstance, vnfModel: Level1Model, vnfStoreKey : string){
    super(<any>instance, vnfModel, vnfStoreKey);
    this.type = ServiceNodeTypes.VF;
    this.vnfStoreKey = vnfStoreKey;
  }
}
