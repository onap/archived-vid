

import {Level1Instance} from "./level1Instance";

export class NetworkInstance extends Level1Instance{
  networkStoreKey : string;
  statusMessage?: string;
  routeTarget: any;

  constructor() {
    super();
    this.networkStoreKey = null;
  }
}
