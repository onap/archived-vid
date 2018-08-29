
import {NodeModel, NodeModelResponseInterface} from "./nodeModel";

export interface ServiceProxyModelResponseInterface extends NodeModelResponseInterface{
  sourceModelUuid: string;
  sourceModelInvariant: string;
  sourceModelName: string;
}
export class ServiceProxyModel extends NodeModel {
  sourceModelUuid: string;
  sourceModelInvariant: string;
  sourceModelName: string;

  constructor(serviceProxyJson?: ServiceProxyModelResponseInterface)  {
    if (serviceProxyJson)  {
      super(serviceProxyJson);
      this.sourceModelUuid = serviceProxyJson.sourceModelUuid;
      this.sourceModelInvariant = serviceProxyJson.sourceModelInvariant;
      this.sourceModelName = serviceProxyJson.sourceModelName;
    }
  }
}
