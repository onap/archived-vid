import {ChildNodeInstance} from "./nodeInstance";

export class VfModuleInstance extends ChildNodeInstance{
  volumeGroupName: string;
  instanceParams: { [key: string] : string; };
  position: any;
  statusMessage?: string;
  tenantId? :string;
  lcpCloudRegionId?: string;
  pauseInstantiation?: string;

  constructor() {
    super();
    this.instanceParams = {};
  }
}
