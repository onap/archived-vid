import {Injectable} from "@angular/core";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {FormGroup} from "@angular/forms";
import {FormControlModel} from "../../../../models/formControlModels/formControl.model";
import {VfModulePopuopService} from "../vfModule/vfModule.popuop.service";
import {FormPopupDetails} from "../../../../models/formControlModels/formPopupDetails.model";

@Injectable()
export class VfModuleUpgradePopupService extends VfModulePopuopService {
  node: ITreeNode;

  getGenericFormPopupDetails(serviceId: string, vnfStoreKey: string, vfModuleStoreKey: string, node: ITreeNode, uuidData: Object, isUpdateMode: boolean): FormPopupDetails {
    this.node = node;
    return super.getGenericFormPopupDetails(serviceId, vnfStoreKey, vfModuleStoreKey, node, uuidData, isUpdateMode);
  }

  getDynamicInputs(): FormControlModel[] {
    return [];
  }

  getControls() {
   return [];
  }

  onSubmit(that, form: FormGroup) {
    this.upgradeVFM(that,form);
    //that.storeVFModule(that, form.value);
    this.postSubmitIframeMessage(that);
    this.onCancel(that, form);
  }

  upgradeVFM(that, form) {
    // currently nothing
  }

  getTitle(): string {
    return 'Upgrade Module';
  }

}
