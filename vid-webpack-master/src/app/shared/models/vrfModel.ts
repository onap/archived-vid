import {Level1Model, Level1ModelProperties, Level1ModelResponseInterface} from "./nodeModel";

export interface VrfProperties extends Level1ModelProperties{
}

export interface VrfResponseInterface extends Level1ModelResponseInterface{
  properties: VrfProperties;
}

export class VrfModel extends Level1Model{


  properties: VrfProperties;

  constructor(vrfJson?: VrfResponseInterface){
    super(vrfJson);
    if(vrfJson && vrfJson.properties){
      this.properties = vrfJson.properties;
    }
    this.min = 1;
    this.max = 1;
  }


}
