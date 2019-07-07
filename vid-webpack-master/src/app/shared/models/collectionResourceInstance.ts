import {Level1Instance} from "./level1Instance";

export class CollectionResourceInstance extends Level1Instance{
  collectionResourceStoreKey : string;
  statusMessage?: string;

  constructor() {
    super();
    this.collectionResourceStoreKey = null;
  }
}
