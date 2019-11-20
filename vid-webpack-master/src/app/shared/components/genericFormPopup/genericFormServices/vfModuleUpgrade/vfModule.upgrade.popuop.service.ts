import {Injectable} from "@angular/core";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {FormGroup} from "@angular/forms";
import {VfModulePopuopService} from "../vfModule/vfModule.popuop.service";
import {FormPopupDetails} from "../../../../models/formControlModels/formPopupDetails.model";

@Injectable()
export class VfModuleUpgradePopupService extends VfModulePopuopService {
  node: ITreeNode;

  getGenericFormPopupDetails(serviceId: string, vnfStoreKey: string, vfModuleStoreKey: string, node: ITreeNode, uuidData: Object, isUpdateMode: boolean): FormPopupDetails {
    return super.getGenericFormPopupDetails(serviceId, vnfStoreKey, vfModuleStoreKey, node, uuidData, isUpdateMode);
  }

  getDynamicInputs = () => [];
  getControls = () => [];
  getTitle = (): string => 'Upgrade Module';

  onSubmit(that, form: FormGroup) {
    //that.storeVFModule(that, form.value);
    this.postSubmitIframeMessage(that);
    this.onCancel(that, form);
  }

}
