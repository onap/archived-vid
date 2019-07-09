import {Level1Model, Level1ModelProperties, Level1ModelResponseInterface} from "./nodeModel";
import {NcfModelInterface} from "./ncfModel";

export interface CollectionResourceProperties extends Level1ModelProperties{
}

export interface CollectionResourceModelResponseInterface extends Level1ModelResponseInterface{
  properties: CollectionResourceProperties;
  networksCollection: { [key: string]: NcfModelInterface };
}

export class CollectionResourceModel extends Level1Model{

  roles: string[] = [];
  properties: CollectionResourceProperties;
  networksCollection: { [key: string]: NcfModelInterface };

  constructor(collectionResourceJson?: CollectionResourceModelResponseInterface){
    super(collectionResourceJson);
    if(collectionResourceJson && collectionResourceJson.properties){
      this.properties = collectionResourceJson.properties;
    }
    if (collectionResourceJson && collectionResourceJson.networksCollection) {
      this.networksCollection = collectionResourceJson.networksCollection;
    }
  }

}
