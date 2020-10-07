import {FormControlModel} from "./formControl.model";
import {ModelInformationItem} from "../../components/model-information/model-information.component";
import {FormGroup} from "@angular/forms";

export class FormPopupDetails {
  popupTypeName: PopupType;
  that : any;
  UUIDData : Object = {}; // TODO uuid tree
  title: string;
  leftSubTitle: string;
  rightSubTitle: string;
  formControlList: FormControlModel[];
  dynamicInputsControlList: FormControlModel[];
  modelInformationItems: ModelInformationItem[];
  onSubmit : (that : any, form: FormGroup , ...args) =>  void;
  onCancel : (that : any, form: FormGroup) => void;

  constructor(that : any,
              popupTypeName : PopupType ,
              UUIDData : Object,
              title : string,
              leftSubTitle : string,
              rightSubTitle : string,
              formControlList : FormControlModel[],
              dynamicInputsControlList : FormControlModel[],
              modelInformationItems : ModelInformationItem[],
              onSubmit : (that : any, form : FormGroup, ...args) =>  void,
              onCancel: (that: any, form: FormGroup) => void) {
    this.title = title;
    this.leftSubTitle = leftSubTitle;
    this.rightSubTitle = rightSubTitle;
    this.formControlList = formControlList;
    this.dynamicInputsControlList = dynamicInputsControlList;
    this.modelInformationItems = modelInformationItems;
    this.onSubmit = onSubmit;
    this.onCancel = onCancel;
    this.popupTypeName = popupTypeName;
    this.UUIDData = UUIDData;
    this.that = that;
  }
}



export enum PopupType {
  SERVICE_MACRO = 'service macro',
  SERVICE_A_LA_CART = 'service a-la-cart',
  SERVICE = 'service',
  VNF_MACRO  ='vnf macro',
  PNF_MACRO  ='pnf macro',
  VNF_A_LA_CARTE = 'vnf a-la-carte',
  VFMODULE = 'vfModule',
  VFMODULE_UPGRADE = 'vfModule_upgrade',
  NETWORK_MACRO = 'network_macro',
  VNF_GROUP = 'vnfGroup'
}

