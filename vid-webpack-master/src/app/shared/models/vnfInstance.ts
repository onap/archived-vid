import {VfModuleMap} from "./vfModulesMap";
import {Level1Instance} from "./level1Instance";


export class VnfInstance extends Level1Instance {

  vfModules: { [vf_module_model_name: string] : VfModuleMap; };
  vnfStoreKey : string;
  isFailed: boolean;
  position: number;
  statusMessage?: string;

  constructor() {
    super();
    this.vfModules = {};
    this.vnfStoreKey = null;
  }
}
