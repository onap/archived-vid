import {VfModuleMap} from "./vfModulesMap";
import {Level1Instance} from "./level1Instance";


export class VnfInstance extends Level1Instance {

  vfModules: { [vf_module_model_name: string] : VfModuleMap; };
  vnfStoreKey : string;
  position: number;
  statusMessage?: string;
  upgradedVFMSonsCounter: number;

  constructor() {
    super();
    this.vfModules = {};
    this.vnfStoreKey = null;
    this.upgradedVFMSonsCounter = 0;
  }
}
