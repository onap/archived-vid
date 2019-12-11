import {Level1Model, Level1ModelProperties, Level1ModelResponseInterface} from "./nodeModel";
import {Utils} from "../utils/utils";

export interface VnfProperties extends Level1ModelProperties {
  ecomp_generated_naming: string;
}

export interface VNFModelResponseInterface extends Level1ModelResponseInterface {
  properties: VnfProperties;
}

export class VNFModel extends Level1Model {
  properties: VnfProperties;

  public static readonly NO_UTILS: Utils = <any>{ specialCase: true };

  constructor(utils: Utils, vnfJson?: VNFModelResponseInterface) {
    super(vnfJson);
    if (vnfJson) {
      this.properties = vnfJson.properties;

      // If utils not given, go to static Utils, stateless
      // This is a special case reserved for VlanTaggingComponent
      this.max = utils !== VNFModel.NO_UTILS
        ? utils.getMaxFirstLevel(this.properties)
        : Utils.getMaxFirstLevel(this.properties, undefined);
    }
  }
}
