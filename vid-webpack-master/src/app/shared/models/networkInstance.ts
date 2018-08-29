

import {Level1Instance} from "./level1Instance";

export class NetworkInstance extends Level1Instance{
  networkStoreKey : string;
  isFailed: boolean;
  statusMessage?: string;

  constructor() {
    super();
    this.networkStoreKey = null;
  }
}
