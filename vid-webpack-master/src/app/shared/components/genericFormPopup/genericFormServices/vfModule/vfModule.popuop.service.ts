import {Injectable} from "@angular/core";
import {FormPopupDetails, PopupType} from "../../../../models/formControlModels/formPopupDetails.model";
import {BasicControlGenerator} from "../../../genericForm/formControlsServices/basic.control.generator";
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

@Injectable()
export class VfModulePopuopService implements GenericPopupInterface {
  dynamicInputs: any;
  instance: any;
  model: any;
  serviceModel: ServiceModel;
  modelInformations: ModelInformationItem[] = [];
  uuidData: Object;
  closeDialogEvent: Subject<any> = new Subject<any>();
  isUpdateMode: boolean;


  constructor(private _basicControlGenerator: BasicControlGenerator,
              private _vfModuleControlGenerator: VfModuleControlGenerator,
              private _iframeService: IframeService,
              private _defaultDataGeneratorService: DefaultDataGeneratorService,
              private _aaiService: AaiService,
              private _basicPopupService : BasicPopupService,
              private _store: NgRedux<AppState>) {

  }

  getInstance(serviceId: string, vnfStoreKey: string, vfModuleStoreKey: string): any {
    if (_.isNil(vnfStoreKey) || _.isNil(vfModuleStoreKey)) {
      return new VfModuleInstance();
    }
    const vfModules = this._store.getState().service.serviceInstance[serviceId].vnfs[vnfStoreKey].vfModules;
    return vfModules[this.uuidData['modelName']][vfModuleStoreKey];
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


  getGenericFormPopupDetails(serviceId: string, vnfStoreKey: string, vfModuleStoreKey: string, node: ITreeNode, uuidData: Object, isUpdateMode: boolean): FormPopupDetails {

    this.uuidData = uuidData;
    this.instance = this.getInstance(serviceId, vnfStoreKey, vfModuleStoreKey);
    this.getModelInformation(serviceId, uuidData['modelName']);

    return new FormPopupDetails(this,
      PopupType.VFMODULE,
      uuidData,
      this.getTitle(isUpdateMode),
      this.getSubLeftTitle(),
      this.getSubRightTitle(),
      this.getControls(serviceId, vnfStoreKey, vfModuleStoreKey, isUpdateMode),
      this.getDynamicInputs(uuidData),
      this.modelInformations,
      (that, form: FormGroup) => {that.onSubmit(that, form);},
      (that: any, form: FormGroup) => {that.onCancel(that, form); }
    );
  }

  getModelInformation(serviceId: string, modelName: string) {
    this._aaiService.getServiceModelById(serviceId).subscribe((result: any) => {
      this.serviceModel = new ServiceModel(result);

      this.model = this._basicPopupService.getModelFromResponse(result, 'vfModules', modelName);
      const serviceInstance = this._store.getState().service.serviceInstance[serviceId];
      this.modelInformations = [
        new ModelInformationItem("Subscriber Name", "subscriberName", [this._basicPopupService.extractSubscriberNameBySubscriberId(serviceInstance.globalSubscriberId)], "", true),
        new ModelInformationItem("Service Name", "serviceModelName", [this.serviceModel.name], "", true),
        new ModelInformationItem("Service Instance Name", "serviceName", [serviceInstance.instanceName], "", false),
        new ModelInformationItem("Model Name", "modelName", [this.model.name], "", true),
        new ModelInformationItem("Model version", "modelVersion", [this.model.version], "", true),
        new ModelInformationItem("Description", "description", [this.model.description]),
        new ModelInformationItem("Category", "category", [this.model.category]),
        new ModelInformationItem("Sub Category", "subCategory", [this.model.subCategory]),
        new ModelInformationItem("UUID", "uuid", [this.model.uuid], Constants.ServicePopup.TOOLTIP_UUID, true),
        new ModelInformationItem("Invariant UUID", "invariantUuid", [this.model.invariantUuid], Constants.ServicePopup.TOOLTIP_INVARIANT_UUID, true),
        new ModelInformationItem("Service type", "serviceType", [this.serviceModel.serviceType]),
        new ModelInformationItem("Service role", "serviceRole", [this.serviceModel.serviceRole]),
        new ModelInformationItem("Minimum to instantiate", "min", this.model.min == undefined ? ['0'] : [this.model.min.toString()], "", true),
        new ModelInformationItem("Maximum to instantiate", "max", this.model.max == undefined ? ['1'] : [this.model.max.toString()], "", true),
        new ModelInformationItem("Recommended to instantiate", "initial", [this.model.initial])
      ];
    });
  }

  getControls(serviceId: string, vnfStoreKey: string, vfModuleStoreKey: string, isUpdateMode: boolean) {
    if (this._store.getState().service.serviceHierarchy[serviceId].service.instantiationType === 'Macro') {
      return this._vfModuleControlGenerator.getMacroFormControls(serviceId, vnfStoreKey, vfModuleStoreKey, this.uuidData, isUpdateMode);
    } else {
      return this._vfModuleControlGenerator.getAlaCarteFormControls(serviceId, vnfStoreKey, vfModuleStoreKey,  this.uuidData, isUpdateMode);
    }
  }


  onSubmit(that, form: FormGroup) {
    form.value['instanceParams'] = form.value['instanceParams'] && [form.value['instanceParams']];
    if (!_.isNil(form.controls['supplementaryFile_hidden_content']) && form.controls['supplementaryFile_hidden_content'].value) {
      form.value['supplementaryFileContent'] = JSON.parse(form.controls['supplementaryFile_hidden_content'].value);
      if (!_.isNil(form.controls['supplementaryFile_hidden'].value)) {
        form.value['supplementaryFileName'] = form.controls['supplementaryFile_hidden'].value.name;
      }
      else {
        form.value['supplementaryFileName'] = that.instance.supplementaryFileName;
      }
    }
    else {
      delete form.value['supplementaryFileContent'];
      delete form.value['supplementaryFileName'];
    }
    that.storeVFModule(that, form.value);
    window.parent.postMessage({
      eventId: 'submitIframe',
      data: {
        serviceModelId: that.serviceModel.uuid
      }
    }, "*");
    this.onCancel(that, form);
  }


  onCancel(that, form) {
    form.reset();
    that._iframeService.removeClassCloseModal('content');
    this.closeDialogEvent.next(that);
  }

  storeVFModule = (that, formValues: any): void => {
    formValues.modelInfo = new ModelInfo(that.model);
    formValues.uuid = formValues.modelInfo.uuid;
    formValues.isMissingData = false;
    const vnf =  that._store.getState().service.serviceInstance[that.uuidData.serviceId].vnfs[that.uuidData.vnfStoreKey];

    if (!that.uuidData.vFModuleStoreKey) {
      this._store.dispatch(createVFModuleInstance(formValues, that.uuidData.modelName, that.uuidData.serviceId, 0, that.uuidData.vnfStoreKey));
    } else {
      this._store.dispatch(updateVFModuleInstance(formValues, that.uuidData.modelName, that.uuidData.serviceId, that.uuidData.vFModuleStoreKey, that.uuidData.vnfStoreKey));
    }
  };

  getTitle(isUpdateMode : boolean) : string {
    return isUpdateMode ? 'Edit Module (Heat stack)' : 'Set new Module (Heat stack)';
  }

  getSubLeftTitle(): string {
    return this.model.name;
  }

  getSubRightTitle(): string {
    return "Module (Heat stack) Instance Details";
  }
}
