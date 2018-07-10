import {ModelInformationItem} from "../../shared/components/model-information/model-information.component";


export interface InstancePopup {
  onCancelClick():void;
  createModelInformationItems(): Array<ModelInformationItem>;
  getModelName():string;
}

