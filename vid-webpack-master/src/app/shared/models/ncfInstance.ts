import {Level1Instance} from "./level1Instance";

export class NcfInstance extends Level1Instance{
  storeKey : string;
  statusMessage?: string;

  constructor() {
    super();
    this.storeKey = null;
  }
}
