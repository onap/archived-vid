import {Injectable} from "@angular/core";
import {FormPopupDetails, PopupType} from "../../../../models/formControlModels/formPopupDetails.model";
import {ControlGeneratorUtil} from "../../../genericForm/formControlsServices/control.generator.util.service";
import {AppState} from "../../../../store/reducers";
import {NgRedux} from "@angular-redux/store";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {ModelInformationItem} from "../../../model-information/model-information.component";
import {Constants} from "../../../../utils/constants";
import {AaiService} from "../../../../services/aaiService/aai.service";
import {ServiceModel} from "../../../../models/serviceModel";
import {FormGroup} from "@angular/forms";
import {ModelInfo} from "../../../../models/modelInfo";
import {IframeService} from "../../../../utils/iframe.service";
import {GenericPopupInterface} from "../generic-popup.interface";
import {Subject} from "rxjs/Subject";
import {DefaultDataGeneratorService} from "../../../../services/defaultDataServiceGenerator/default.data.generator.service";
import {BasicPopupService} from "../basic.popup.service";
import {VfModuleInstance} from "../../../../models/vfModuleInstance";
import {VfModuleControlGenerator} from "../../../genericForm/formControlsServices/vfModuleGenerator/vfModule.control.generator";
import {FormControlModel} from "../../../../models/formControlModels/formControl.model";
import * as _ from 'lodash';
import {createVFModuleInstance, updateVFModuleInstance} from "../../../../storeUtil/utils/vfModule/vfModule.actions";
import {SharedControllersService} from "../../../genericForm/formControlsServices/sharedControlles/shared.controllers.service";
import {SharedTreeService} from "../../../../../drawingBoard/service-planning/objectsToTree/shared.tree.service";
import {PauseStatus} from "../../../../models/serviceInstanceActions";

export abstract class VfModulePopupServiceBase {
  dynamicInputs: any;
  instance: any;
  model: any;
  serviceModel: ServiceModel;
  modelInformations: ModelInformationItem[] = [];
  uuidData: Object;
  closeDialogEvent: Subject<any> = new Subject<any>();
  isUpdateMode: boolean;
  storeVFModule = (that, formValues: any): void => {
    formValues.pauseInstantiation = (formValues.pauseInstantiation || formValues.pauseInstantiation === PauseStatus.AFTER_COMPLETION)
      ? PauseStatus.AFTER_COMPLETION : null;
    formValues.modelInfo = new ModelInfo(that.model);
    formValues.uuid = formValues.modelInfo.uuid;
    formValues.isMissingData = false;
    if (!that.uuidData.vFModuleStoreKey) {
      let positionOfNextInstance = this._defaultDataGeneratorService.calculatePositionOfVfmodule(that.uuidData.serviceId);
      this._store.dispatch(createVFModuleInstance(formValues, that.uuidData.modelName, that.uuidData.serviceId, !_.isNil(positionOfNextInstance)? positionOfNextInstance : null, that.uuidData.vnfStoreKey));
    } else {
      this._store.dispatch(updateVFModuleInstance(formValues, that.uuidData.modelName, that.uuidData.serviceId, that.uuidData.vFModuleStoreKey, that.uuidData.vnfStoreKey, this.getInstance(that.uuidData.serviceId, that.uuidData.vnfStoreKey, that.uuidData.vFModuleStoreKey).position));
    }
  };

  protected constructor(
    protected _basicControlGenerator: ControlGeneratorUtil,
    protected _sharedControllersService : SharedControllersService,
    protected _vfModuleControlGenerator: VfModuleControlGenerator,
    protected _iframeService: IframeService,
    protected _defaultDataGeneratorService: DefaultDataGeneratorService,
    protected _aaiService: AaiService,
    protected _basicPopupService: BasicPopupService,
    protected _store: NgRedux<AppState>,
    protected _sharedTreeService: SharedTreeService) {
  }

  getInstance(serviceId: string, vnfStoreKey: string, vfModuleStoreKey: string): any {
    if (_.isNil(vnfStoreKey) || _.isNil(vfModuleStoreKey)) {
      return new VfModuleInstance();
    }
    const vfModules = this._store.getState().service.serviceInstance[serviceId].vnfs[vnfStoreKey].vfModules;
    return vfModules[this.uuidData['modelName']][vfModuleStoreKey];
  }

  getModelInformation(serviceId: string, modelName: string, vfModuleModeNode:ITreeNode) {
    this._aaiService.getServiceModelById(serviceId).subscribe((result: any) => {
      this.serviceModel = new ServiceModel(result);
      this.model = this._basicPopupService.getModelFromResponse(result, 'vfModules', modelName);
      const serviceInstance = this._store.getState().service.serviceInstance[serviceId];
      this.modelInformations = [
        new ModelInformationItem("Subscriber Name", "subscriberName", [this._basicPopupService.extractSubscriberNameBySubscriberId(serviceInstance.globalSubscriberId)], "", true),
        new ModelInformationItem("Service Name", "serviceModelName", [this.serviceModel.name], "", true),
        new ModelInformationItem("Service Instance Name", "serviceName", [serviceInstance.instanceName], "", false),
        new ModelInformationItem("Model Name", "modelName", [this.model.name], "", true),
        new ModelInformationItem("Model version", "modelVersion", [this._sharedTreeService.getModelVersionEitherFromInstanceOrFromHierarchy(vfModuleModeNode.data, this.model) ], "", true),
        new ModelInformationItem("Description", "description", [this.model.description]),
        new ModelInformationItem("Category", "category", [this.model.category]),
        new ModelInformationItem("Sub Category", "subCategory", [this.model.subCategory]),
        new ModelInformationItem("UUID", "uuid", [this._sharedTreeService.getModelVersionIdEitherFromInstanceOrFromHierarchy(vfModuleModeNode.data, this.model)], Constants.ServicePopup.TOOLTIP_UUID, true),
        new ModelInformationItem("Invariant UUID", "invariantUuid", [this._sharedTreeService.getModelInvariantIdEitherFromInstanceOrFromHierarchy(vfModuleModeNode, this.model)], Constants.ServicePopup.TOOLTIP_INVARIANT_UUID, true),
        new ModelInformationItem("Service type", "serviceType", [this.serviceModel.serviceType]),
        new ModelInformationItem("Service role", "serviceRole", [this.serviceModel.serviceRole]),
        new ModelInformationItem("Minimum to instantiate", "min", this.model.min == undefined ? ['0'] : [this.model.min.toString()], "", true),
        this._basicPopupService.createMaximumToInstantiateModelInformationItem(this.model),
        new ModelInformationItem("Recommended to instantiate", "initial", [this.model.initial])
      ];
    });
  }

  protected postSubmitIframeMessage(that) {
    window.parent.postMessage({
      eventId: 'submitIframe',
      data: {
        serviceModelId: that.serviceModel.uuid
      }
    }, "*");
  }

  onCancel(that, form) {
    form.reset();
    that._iframeService.removeClassCloseModal('content');
    this.closeDialogEvent.next(that);
  }

  getSubLeftTitle(): string {
    return this.model.name;
  }

  getSubRightTitle(): string {
    return "Module (Heat stack) Instance Details";
  }

  abstract getTitle(isUpdateMode : boolean) : string;
  abstract getControls(serviceId: string, vnfStoreKey: string, vfModuleStoreKey: string, isUpdateMode: boolean);
  abstract getDynamicInputs(UUIDData : Object) : FormControlModel[];

  getGenericFormPopupDetails(serviceId: string, vnfStoreKey: string, vfModuleStoreKey: string, vfModuleNode: ITreeNode, uuidData: Object, isUpdateMode: boolean): FormPopupDetails {

    this.uuidData = uuidData;
    this.instance = this.getInstance(serviceId, vnfStoreKey, vfModuleStoreKey);
    this.getModelInformation(serviceId, uuidData['modelName'], vfModuleNode);

    return new FormPopupDetails(this,
      PopupType.VFMODULE,
      uuidData,
      this.getTitle(isUpdateMode),
      this.getSubLeftTitle(),
      this.getSubRightTitle(),
      this.getControls(serviceId, vnfStoreKey, vfModuleStoreKey, isUpdateMode),
      this.getDynamicInputs(uuidData),
      this.modelInformations,
      (that, form: FormGroup) => {
        that.onSubmit(that, form);
      },
      (that: any, form: FormGroup) => {
        that.onCancel(that, form);
      }
    );
  }

  updateFormValueWithSupplementaryFile(form: FormGroup, that) {
    if (!_.isNil(form.controls['supplementaryFile_hidden_content']) && form.controls['supplementaryFile_hidden_content'].value) {
      form.value['supplementaryFileContent'] = JSON.parse(form.controls['supplementaryFile_hidden_content'].value);
      if (!_.isNil(form.controls['supplementaryFile_hidden'].value)) {
        form.value['supplementaryFileName'] = form.controls['supplementaryFile_hidden'].value.name;
      } else {
        form.value['supplementaryFileName'] = that.instance.supplementaryFileName;
      }
    } else {
      delete form.value['supplementaryFileContent'];
      delete form.value['supplementaryFileName'];
    }
  }
}

@Injectable()
export class VfModulePopupService extends VfModulePopupServiceBase implements GenericPopupInterface {


  constructor(_basicControlGenerator: ControlGeneratorUtil,
              _sharedControllersService : SharedControllersService,
              _vfModuleControlGenerator: VfModuleControlGenerator,
              _iframeService: IframeService,
              _defaultDataGeneratorService: DefaultDataGeneratorService,
              _aaiService: AaiService,
              _basicPopupService : BasicPopupService,
              _store: NgRedux<AppState>,
              _sharedTreeService: SharedTreeService) {
    super(_basicControlGenerator, _sharedControllersService, _vfModuleControlGenerator, _iframeService, _defaultDataGeneratorService, _aaiService, _basicPopupService, _store, _sharedTreeService);

  }

  getDynamicInputs(UUIDData : Object) : FormControlModel[]{
    let dynamic = this._defaultDataGeneratorService.getArbitraryInputs(this._store.getState().service.serviceHierarchy[UUIDData['serviceId']].vfModules[UUIDData['modelName']].inputs);
    return this.getVFModuleDynamicInputs(dynamic, UUIDData);
  }

  getVFModuleDynamicInputs(dynamicInputs : any, UUIDData : Object) : FormControlModel[] {
    let result : FormControlModel[] = [];
    if(dynamicInputs) {
      let vfModuleInstance = null;
      if (_.has(this._store.getState().service.serviceInstance[UUIDData['serviceId']].vnfs, UUIDData['vnfStoreKey']) &&
        _.has(this._store.getState().service.serviceInstance[UUIDData['serviceId']].vnfs[UUIDData['vnfStoreKey']].vfModules, UUIDData['modelName'])) {
        vfModuleInstance = Object.assign({},this._store.getState().service.serviceInstance[UUIDData['serviceId']].vnfs[UUIDData['vnfStoreKey']].vfModules[UUIDData['modelName']][UUIDData['vfModuleStoreKey']]);
      }
      result = this._basicControlGenerator.getDynamicInputs(dynamicInputs, vfModuleInstance);
    }
    return result;
  }


  getControls(serviceId: string, vnfStoreKey: string, vfModuleStoreKey: string, isUpdateMode: boolean) {
    if (this._store.getState().service.serviceHierarchy[serviceId].service.vidNotions.instantiationType === 'Macro') {
      return this._vfModuleControlGenerator.getMacroFormControls(serviceId, vnfStoreKey, vfModuleStoreKey, this.uuidData, isUpdateMode);
    } else {
      return this._vfModuleControlGenerator.getAlaCarteFormControls(serviceId, vnfStoreKey, vfModuleStoreKey,  this.uuidData, isUpdateMode);
    }
  }


  onSubmit(that, form: FormGroup) {
    form.value['instanceParams'] = form.value['instanceParams'] && [form.value['instanceParams']];
    this.updateFormValueWithSupplementaryFile(form, that);
    that.storeVFModule(that, form.value);
    this.postSubmitIframeMessage(that);
    this.onCancel(that, form);
  }

  getTitle(isUpdateMode : boolean) : string {
    return isUpdateMode ? 'Edit Module (Heat stack)' : 'Set new Module (Heat stack)';
  }

}
