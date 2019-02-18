import {VfModuleTreeNode} from "./vfModuleTreeNode";
import {NetworkTreeNode} from "./networkTreeNode";
import {Level1Model} from "./nodeModel";
import {VnfInstance} from "./vnfInstance";
import {ServiceNodeTypes} from "./ServiceNodeTypes";

export class VnfTreeNode extends NetworkTreeNode{

  children: VfModuleTreeNode[];
  vnfStoreKey : string;

  constructor(instance: VnfInstance, vnfModel: Level1Model, vnfStoreKey : string){
    super(<any>instance, vnfModel, vnfStoreKey);
    this.type = ServiceNodeTypes.VF;
    this.vnfStoreKey = vnfStoreKey;
  }
}
