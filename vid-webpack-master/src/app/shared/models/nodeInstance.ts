import {ServiceInstanceActions} from "./serviceInstanceActions";
import {ModelInfo} from "./modelInfo";
import * as _ from "lodash";

export class NodeInstance {
  instanceName: string;
  instanceType?: string;
  orchStatus?:string;
  action: ServiceInstanceActions = ServiceInstanceActions.Create;
  originalAction : ServiceInstanceActions;
  modelInfo?: ModelInfo;
  instanceId?: string;
  trackById?: string;
  isFailed?: boolean;

  modelUniqueId(): string {
    return _.isNil(this.modelInfo)
      ? undefined
      : (this.modelInfo.modelCustomizationId || this.modelInfo.modelInvariantId);
  }
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
