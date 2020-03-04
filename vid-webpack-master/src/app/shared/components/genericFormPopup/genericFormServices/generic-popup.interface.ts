/***********************************************************
 onSubmit : action when set button is clicked
 onCancel : action when cancel/X button is clicked
 getModelInformation : should generate the model information
 getGenericFormPopupDetails : returns list of controls
 getInstance :  returns exists instance (if exist)
 getTitle :  returns generic popup title
 getSubLeftTitle :  returns generic popup left sub title
 getSubRightTitle :  returns generic popup right sub title
 ***********************************************************/

import {FormGroup} from "@angular/forms";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {FormPopupDetails} from "../../../models/formControlModels/formPopupDetails.model";

export interface GenericPopupInterface {
  onSubmit(that, form: FormGroup, ...args) : void;
  onCancel(that, form) : void;
  getModelInformation(serviceId : string, modelName : string, node: ITreeNode) : void;
  getGenericFormPopupDetails(serviceId : string, modelName : string , storeKey : string, node : ITreeNode, uuidData : Object, isUpdateMode : boolean) : FormPopupDetails;
  getInstance(serviceId : string, modelName : string , storeKey : string) : any;
  getTitle(isUpdateMode : boolean) : string;
  getSubLeftTitle() : string;
  getSubRightTitle() : string;
}
