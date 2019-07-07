import {Level1Instance} from "./level1Instance";

export class VrfInstance extends Level1Instance{
  vrfStoreKey : string;
  statusMessage?: string;
  vpns : any;
  networks : any;

  constructor() {
    super();
    this.vrfStoreKey = null;
    this.vpns = {};
    this.networks = {};
  }
}
