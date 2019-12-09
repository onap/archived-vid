import {
  Level1Model,
  Level1ModelProperties,
  Level1ModelResponseInterface
} from "./nodeModel";
import * as _ from "lodash";


export interface VnfProperties extends Level1ModelProperties {
  ecomp_generated_naming: string;
}

export interface VNFModelResponseInterface extends Level1ModelResponseInterface {
  properties: VnfProperties;
}

export class VNFModel extends Level1Model {
  properties: VnfProperties;

  constructor(vnfJson?: VNFModelResponseInterface, flags?: { [key: string]: boolean }) {
    super(vnfJson);
    if (vnfJson) {
      this.properties = vnfJson.properties;
      this.max = this.getMax(this.properties, flags);
    }
  }


  getMax(properties, flags: { [key: string]: boolean }) {
    if (flags && !!flags['FLAG_2002_UNLIMITED_MAX']) {
      return !_.isNil(properties) && !_.isNil(properties.max_instances) ? properties.max_instances : null;
    } else {
      return properties.max_instances || 1
    }
  }

}
