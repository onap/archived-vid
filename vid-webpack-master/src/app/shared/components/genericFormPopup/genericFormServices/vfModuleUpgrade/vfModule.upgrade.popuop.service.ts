import {Injectable} from "@angular/core";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {FormGroup} from "@angular/forms";
import {VfModulePopupServiceBase} from "../vfModule/vfModule.popup.service";
import {upgradeVFModule} from "../../../../storeUtil/utils/vfModule/vfModule.actions";
import {SharedTreeService} from "../../../../../drawingBoard/service-planning/objectsToTree/shared.tree.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../store/reducers";
import {ControlGeneratorUtil} from "../../../genericForm/formControlsServices/control.generator.util.service";
import {VfModuleControlGenerator} from "../../../genericForm/formControlsServices/vfModuleGenerator/vfModule.control.generator";
import {IframeService} from "../../../../utils/iframe.service";
import {DefaultDataGeneratorService} from "../../../../services/defaultDataServiceGenerator/default.data.generator.service";
import {AaiService} from "../../../../services/aaiService/aai.service";
import {BasicPopupService} from "../basic.popup.service";
import {FormControlModel} from "../../../../models/formControlModels/formControl.model";
import {CheckboxFormControl} from "../../../../models/formControlModels/checkboxFormControl.model";
import {FormControlType} from "../../../../models/formControlModels/formControlTypes.enum";
import {mergeObjectByPathAction} from "../../../../storeUtil/utils/general/general.actions";
import {VfModuleInstance} from "../../../../models/vfModuleInstance";
import {SharedControllersService} from "../../../genericForm/formControlsServices/sharedControlles/shared.controllers.service";

export enum UpgradeFormControlNames {
  RETAIN_VOLUME_GROUPS = 'retainVolumeGroups',
  RETAIN_ASSIGNMENTS = 'retainAssignments',
}

@Injectable()
export class VfModuleUpgradePopupService extends VfModulePopupServiceBase {
  constructor(protected _basicControlGenerator: ControlGeneratorUtil,
              protected _sharedControllersService : SharedControllersService,
              protected _vfModuleControlGenerator: VfModuleControlGenerator,
              protected _iframeService: IframeService,
              protected _defaultDataGeneratorService: DefaultDataGeneratorService,
              protected _aaiService: AaiService,
              protected _basicPopupService: BasicPopupService,
              protected _store: NgRedux<AppState>,
              protected _sharedTreeService: SharedTreeService) {
    super(_basicControlGenerator, _sharedControllersService, _vfModuleControlGenerator, _iframeService, _defaultDataGeneratorService, _aaiService, _basicPopupService, _store, _sharedTreeService);
  }

  node: ITreeNode;

  getDynamicInputs = () => null;

  getControls(serviceId: string, vnfStoreKey: string, vfModuleStoreKey: string, isUpdateMode: boolean): FormControlModel[]  {
    let result: FormControlModel[] =[
      this.getRetainAssignmentsControl(),
      this._sharedControllersService.getSDNCControl(null)
    ];
    const vfModuleInstance :VfModuleInstance = this._vfModuleControlGenerator.getVfModuleInstance(serviceId, vnfStoreKey, this.uuidData, isUpdateMode);

    let volumeGroupAllowed = this._store.getState().service.serviceHierarchy[serviceId].vfModules[this.uuidData['modelName']].volumeGroupAllowed;
    let vfModuleNotExistsOnModel = this._sharedTreeService.isVfModuleCustomizationIdNotExistsOnModel(this.uuidData['vfModule'], serviceId);
    if(volumeGroupAllowed|| vfModuleNotExistsOnModel){
      result.push(this.getRetainVolumeGroupsControl());
    }
    result = this._basicControlGenerator.concatSupplementaryFile(result, vfModuleInstance);
    return result;
  };

  getTitle = (): string => 'Upgrade Module';

  onSubmit(that, form: FormGroup) {
    const node = that.uuidData.vfModule;
    const serviceInstanceId: string = that.uuidData.serviceId;
    const vnfStoreKey = node.parent.data.vnfStoreKey;
    const modelName = node.data.modelName;
    const dynamicModelName = node.data.dynamicModelName;

    this.updateFormValueWithSupplementaryFile(form, that);

    this._store.dispatch(upgradeVFModule(modelName, vnfStoreKey, serviceInstanceId, dynamicModelName));
    this._store.dispatch(mergeObjectByPathAction(['serviceInstance', serviceInstanceId, 'vnfs', vnfStoreKey, 'vfModules', modelName, dynamicModelName], form.value));
    this._sharedTreeService.upgradeBottomUp(node, serviceInstanceId);

    this.postSubmitIframeMessage(that);
    this.onCancel(that, form);
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
}
