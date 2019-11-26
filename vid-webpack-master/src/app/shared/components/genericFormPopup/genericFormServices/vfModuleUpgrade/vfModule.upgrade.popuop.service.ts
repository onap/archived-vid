import {Injectable} from "@angular/core";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {FormGroup} from "@angular/forms";
import {VfModulePopuopService} from "../vfModule/vfModule.popuop.service";
import {FormPopupDetails} from "../../../../models/formControlModels/formPopupDetails.model";
import {updateVFModuleField, upgradeVFModule} from "../../../../storeUtil/utils/vfModule/vfModule.actions";
import {SharedTreeService} from "../../../../../drawingBoard/service-planning/objectsToTree/shared.tree.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../store/reducers";
import {BasicControlGenerator} from "../../../genericForm/formControlsServices/basic.control.generator";
import {VfModuleControlGenerator} from "../../../genericForm/formControlsServices/vfModuleGenerator/vfModule.control.generator";
import {IframeService} from "../../../../utils/iframe.service";
import {DefaultDataGeneratorService} from "../../../../services/defaultDataServiceGenerator/default.data.generator.service";
import {AaiService} from "../../../../services/aaiService/aai.service";
import {BasicPopupService} from "../basic.popup.service";
import {
  FormControlModel
} from "../../../../models/formControlModels/formControl.model";
import {CheckboxFormControl} from "../../../../models/formControlModels/checkboxFormControl.model";
import {FormControlType} from "../../../../models/formControlModels/formControlTypes.enum";

export enum UpgradeFormControlNames {
  RETAIN_VOLUME_GROUPS = 'retainVolumeGroups',
  RETAIN_ASSIGNMENTS = 'retainAssignments',
}

@Injectable()
export class VfModuleUpgradePopupService extends VfModulePopuopService {

  constructor(protected _basicControlGenerator: BasicControlGenerator,
              protected _vfModuleControlGenerator: VfModuleControlGenerator,
              protected _iframeService: IframeService,
              protected _defaultDataGeneratorService: DefaultDataGeneratorService,
              protected _aaiService: AaiService,
              protected _basicPopupService : BasicPopupService,
              protected _store: NgRedux<AppState>,
              private _sharedTreeService : SharedTreeService){
    super(_basicControlGenerator, _vfModuleControlGenerator, _iframeService, _defaultDataGeneratorService, _aaiService, _basicPopupService,_store);
  }
  node: ITreeNode;

  getGenericFormPopupDetails(serviceId: string, vnfStoreKey: string, vfModuleStoreKey: string, node: ITreeNode, uuidData: Object, isUpdateMode: boolean): FormPopupDetails {
    return super.getGenericFormPopupDetails(serviceId, vnfStoreKey, vfModuleStoreKey, node, uuidData, isUpdateMode);
  }

  getDynamicInputs = () => [];

  getControls = () : FormControlModel[] => {
    return this.getUpgradeFormControls()
  };

  getTitle = (): string => 'Upgrade Module';

  onSubmit(that, form: FormGroup) {
    const node = that.uuidData.vfModule;
    const serviceInstanceId: string = that.uuidData.serviceId;

    this._store.dispatch(upgradeVFModule(node.data.modelName,  node.parent.data.vnfStoreKey, serviceInstanceId ,node.data.dynamicModelName));
    this._sharedTreeService.upgradeBottomUp(node, serviceInstanceId);

    this.updateVFModuleField(UpgradeFormControlNames.RETAIN_VOLUME_GROUPS,node, serviceInstanceId, form);
    this.updateVFModuleField(UpgradeFormControlNames.RETAIN_ASSIGNMENTS,node, serviceInstanceId, form);

    this.postSubmitIframeMessage(that);
    this.onCancel(that, form);
  }

  private updateVFModuleField(fieldName: string, node, serviceInstanceId: string, form: FormGroup) {
    this._store.dispatch(updateVFModuleField(node.data.modelName, node.parent.data.vnfStoreKey, serviceInstanceId, node.data.dynamicModelName, fieldName, form.controls[fieldName].value));
  }


  getRetainVolumeGroupsControl = (): CheckboxFormControl => {
    return new CheckboxFormControl({
      type: FormControlType.CHECKBOX,
      controlName: UpgradeFormControlNames.RETAIN_VOLUME_GROUPS,
      displayName: 'Retain Volume Groups',
      dataTestId: UpgradeFormControlNames.RETAIN_VOLUME_GROUPS,
      value: true,
      validations: []
    })
  };

  getRetainAssignmentsControl = (): CheckboxFormControl => {
    return new CheckboxFormControl({
      type: FormControlType.CHECKBOX,
      controlName: UpgradeFormControlNames.RETAIN_ASSIGNMENTS,
      displayName: 'Retain Assignments',
      dataTestId: UpgradeFormControlNames.RETAIN_ASSIGNMENTS,
      value: true,
      validations: []
    })
  };

  getUpgradeFormControls = (): FormControlModel[] => {
    let result: FormControlModel[] = [];
    result.push(this.getRetainVolumeGroupsControl());
    result.push(this.getRetainAssignmentsControl());
    return result;
  }
}
