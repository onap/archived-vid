import {ModelInformationItem} from "../../../shared/components/model-information/model-information.component";

export class ComponentInfoModel {
  type: ComponentInfoType;
  modelInfoItems: ModelInformationItem[];
  additionalInfoItems: ModelInformationItem[];
  title: string;


  constructor(type: ComponentInfoType, modelInfoItems: ModelInformationItem[], additionalInfoItems: ModelInformationItem[], isInstance:boolean=true) {
    this.type = type;
    this.modelInfoItems = modelInfoItems;
    this.additionalInfoItems = additionalInfoItems;
    this.title=this.type+(isInstance ? " Instance" : "");
  }
}


export enum ComponentInfoType {
  SERVICE = "Service",
  VNF = "VNF",
  NETWORK = "Network",
  VFMODULE = "VFModule",
  VNFGROUP = "Group",
  VNFMEMBER = "VNF"
}
