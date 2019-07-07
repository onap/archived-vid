import {NodeModelResponseInterface} from "./nodeModel";

export interface NcfProperties {
  networkCollectionFunction: string;
  networkCollectionDescription: string;
}

export interface NcfModelInterface extends NodeModelResponseInterface {
  networkCollectionProperties: NcfProperties;
}
