import {
  Level1Model, Level1ModelProperties,
  Level1ModelResponseInterface
} from "./nodeModel";
import {VfcInstanceGroupMap} from "./vfcInstanceGroupMap";
import {VNFModelResponseInterface} from "./vnfModel";
import * as _ from "lodash";


export interface NetworkProperties extends Level1ModelProperties {
  ecomp_generated_naming: string;
  network_role: string;
}

export interface NetworkModelResponseInterface extends Level1ModelResponseInterface {
  properties: NetworkProperties;
}

export class NetworkModel extends Level1Model {

  roles: string[] = [];
  properties: NetworkProperties;

  constructor(networkJson?: NetworkModelResponseInterface, flags?: { [key: string]: boolean }) {
    super(networkJson);
    if (networkJson && networkJson.properties) {
      this.properties = networkJson.properties;
      // expecting network_role to be a comma-saparated list
      this.roles = networkJson.properties.network_role ? networkJson.properties.network_role.split(',') : [];
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
