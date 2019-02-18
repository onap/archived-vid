import {ServiceInstanceActions} from "./serviceInstanceActions";
import {ModelInfo} from "./modelInfo";

export class NodeInstance {
  instanceName: string;
  orchStatus?:string;
  action: ServiceInstanceActions = ServiceInstanceActions.Create;
  originalAction : ServiceInstanceActions;
  modelInfo?: ModelInfo;
  instanceId?: string;
  trackById?: string;
}
export class ChildNodeInstance extends NodeInstance {
  isMissingData: boolean;
  provStatus?:string;
  inMaint?:boolean;
  constructor() {
    super();
    this.inMaint = false;
  }
}
