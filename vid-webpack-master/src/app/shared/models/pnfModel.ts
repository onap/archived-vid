import {
  Level1Model,
  Level1ModelProperties,
  Level1ModelResponseInterface
} from "./nodeModel";



export interface PnfProperties extends Level1ModelProperties{
  ecomp_generated_naming: string;
}

export interface PNFModelResponseInterface extends Level1ModelResponseInterface{
  properties: PnfProperties;
}

export class PNFModel extends Level1Model{
  roles: string[] = [];
  properties: PnfProperties;

  constructor(pnfJson?: PNFModelResponseInterface) {
    super(pnfJson);
    if (pnfJson && pnfJson.properties) {
      this.properties = pnfJson.properties;
    }
  }

}
