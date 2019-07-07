import {ChildNodeInstance} from "./nodeInstance";

export class VfModuleInstance extends ChildNodeInstance{
  volumeGroupName: string;
  instanceParams: { [key: string] : string; };
  position: any;
  statusMessage?: string;

  constructor() {
    super();
    this.instanceParams = {};
  }
}
