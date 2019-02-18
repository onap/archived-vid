import {
  Level1Model,
  Level1ModelProperties,
  Level1ModelResponseInterface
} from "./nodeModel";
import {ServiceProxyModel} from "./serviceProxyModel";


export interface VnfGroupProperties extends Level1ModelProperties{
  ecomp_generated_naming: string;
  role?: string;
  type?: string;
  quantity?: number;
}

export interface VnfGroupModelResponseInterface extends Level1ModelResponseInterface{
  properties: VnfGroupProperties;
  members: {[key: string]: ServiceProxyModel};
}

export class VnfGroupModel extends Level1Model{
  properties: VnfGroupProperties;
  members: {[key: string]: ServiceProxyModel};

    constructor(vnfGoupJson?: VnfGroupModelResponseInterface) {
    super(vnfGoupJson);
    if (vnfGoupJson) {
      this.properties = vnfGoupJson.properties;
      this.members = vnfGoupJson.members;
    }
  }
}
