import {Action} from "redux";

export interface ActionOnFirstLevel extends Action {
  type: string;
  firstLevelName: string;
  storeKey : string;
  serviceId? : string;
}
