import {NetworkTreeNode} from "./networkTreeNode";
import {NetworkInstance} from "./networkInstance";
import {Level1Model} from "./nodeModel";

export class VnfGroupTreeNode extends NetworkTreeNode{
  vnfGroupStoreKey : string;
  limitMembers: number;
  constructor(instance: NetworkInstance, vnfGroupModel: Level1Model, vnfGroupStoreKey : string){
    super(instance, vnfGroupModel, vnfGroupStoreKey);
    this.vnfGroupStoreKey = vnfGroupStoreKey;
  }
}
