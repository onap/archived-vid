import {
  Level1Model, Level1ModelProperties,
  Level1ModelResponseInterface
} from "./nodeModel";
import {Utils} from "../utils/utils";


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
      this.max = Utils.getMaxFirstLevel(this.properties, flags);
    }
  }


}
