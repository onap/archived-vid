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
    return [
      new CheckboxFormControl({
        type: FormControlType.CHECKBOX,
        controlName: 'retainAssignments',
        displayName: 'Retain Assignments',
        dataTestId: 'retainAssignments',
        value: false,
        validations: []
      })
    ];
  };


  getTitle = (): string => 'Upgrade Module';

  onSubmit(that, form: FormGroup) {
    const node = that.uuidData.vfModule;
    const serviceInstanceId: string = that.uuidData.serviceId;

    this._store.dispatch(upgradeVFModule(node.data.modelName,  node.parent.data.vnfStoreKey, serviceInstanceId ,node.data.dynamicModelName));
    this._sharedTreeService.upgradeBottomUp(node, serviceInstanceId);
    this._store.dispatch(updateVFModuleField(node.data.modelName,  node.parent.data.vnfStoreKey, serviceInstanceId ,node.data.dynamicModelName, 'retainAssignments', form.controls['retainAssignments'].value));

    this.postSubmitIframeMessage(that);
    this.onCancel(that, form);
  }

}
