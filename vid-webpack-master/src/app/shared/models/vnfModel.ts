import {
  Level1Model,
  Level1ModelProperties,
  Level1ModelResponseInterface
} from "./nodeModel";



export interface VnfProperties extends Level1ModelProperties{
  ecomp_generated_naming: string;
}

export interface VNFModelResponseInterface extends Level1ModelResponseInterface{
  properties: VnfProperties;
}

export class VNFModel extends Level1Model{
  properties: VnfProperties;

  constructor(vnfJson?: VNFModelResponseInterface) {
    super(vnfJson);
    if (vnfJson) {
      this.properties = vnfJson.properties;
    }
  }

}
