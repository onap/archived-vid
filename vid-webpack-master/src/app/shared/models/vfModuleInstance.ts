import {ChildNodeInstance} from "./nodeInstance";
import {PauseStatus} from "./serviceInstanceActions";

export class VfModuleInstance extends ChildNodeInstance{
  volumeGroupName: string;
  instanceParams: { [key: string] : string; };
  position: any;
  statusMessage?: string;
  tenantId? :string;
  lcpCloudRegionId?: string;
  pauseInstantiation?: PauseStatus;

  constructor() {
    super();
    this.instanceParams = {};
  }
}
