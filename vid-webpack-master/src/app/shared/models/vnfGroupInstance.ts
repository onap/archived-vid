import {Level1Instance} from "./level1Instance";
import {VnfMember} from "./VnfMember";
import {NetworkInstance} from "./networkInstance";

export class VnfGroupInstance extends Level1Instance{
  vnfGroupStoreKey : string;
  vnfs: { [vnf_module_model_name: string]: VnfMember; };
  constructor() {
    super();
    this.vnfGroupStoreKey = null;
    this.vnfs ={};
  }
}
