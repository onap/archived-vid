import {Level1Instance} from "./level1Instance";


export class PnfInstance extends Level1Instance {

  pnfStoreKey : string;
  statusMessage?: string;
  position: number;

  constructor() {
    super();
    this.pnfStoreKey = null;
  }
}
