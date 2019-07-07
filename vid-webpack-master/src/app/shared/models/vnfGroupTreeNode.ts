import {NetworkInstance} from "./networkInstance";
import {Level1Model} from "./nodeModel";
import {FirstLevelTreeNode} from "./firstLevelTreeNode";

export class VnfGroupTreeNode extends FirstLevelTreeNode {
  vnfGroupStoreKey : string;
  limitMembers: number;
  constructor(instance: NetworkInstance, vnfGroupModel: Level1Model, vnfGroupStoreKey : string){
    super(instance, vnfGroupModel, vnfGroupStoreKey);
    this.vnfGroupStoreKey = vnfGroupStoreKey;
  }
}
