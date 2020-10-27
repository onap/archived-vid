import {
  Level1Model,
  Level1ModelProperties,
  Level1ModelResponseInterface
} from "./nodeModel";
import {VNFModelResponseInterface} from "./vnfModel";
import {Utils} from "../utils/utils";



export interface PnfProperties extends Level1ModelProperties{
  ecomp_generated_naming: string;
}

export interface PNFModelResponseInterface extends Level1ModelResponseInterface{
  properties: PnfProperties;
}

export class PNFModel extends Level1Model{
  roles: string[] = [];
  properties: PnfProperties;

  constructor(pnfJson?: PNFModelResponseInterface, flags?: { [key: string]: boolean }) {
    super(pnfJson);
    if (pnfJson && pnfJson.properties) {
      this.properties = pnfJson.properties;
      this.max = Utils.getMaxFirstLevel(this.properties, flags);
    }
  }

}
