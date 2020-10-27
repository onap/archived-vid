import {Injectable} from '@angular/core';
import {GenericPopupInterface} from "../generic-popup.interface";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {FormPopupDetails, PopupType} from "../../../../models/formControlModels/formPopupDetails.model";
import {FormGroup} from "@angular/forms";
import {ModelInformationItem} from "../../../model-information/model-information.component";
import {ServiceModel} from "../../../../models/serviceModel";
import {Subject} from "rxjs/Subject";
import {ControlGeneratorUtil} from "../../../genericForm/formControlsServices/control.generator.util.service";
import {PnfControlGenerator} from "../../../genericForm/formControlsServices/pnfGenerator/pnf.control.generator";
import {IframeService} from "../../../../utils/iframe.service";
import {DefaultDataGeneratorService} from "../../../../services/defaultDataServiceGenerator/default.data.generator.service";
import {AaiService} from "../../../../services/aaiService/aai.service";
import {BasicPopupService} from "../basic.popup.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../store/reducers";
import {Subscriber} from "../../../../models/subscriber";
import {Constants} from "../../../../utils/constants";
import {PnfInstance} from "../../../../models/pnfInstance";
import {ModelInfo} from "../../../../models/modelInfo";
import {changeInstanceCounter} from "../../../../storeUtil/utils/general/general.actions";
import {createPNFInstance, updatePNFInstance} from "../../../../storeUtil/utils/pnf/pnf.actions";
import * as _ from 'lodash';

@Injectable()
export class PnfPopupService implements GenericPopupInterface{
  dynamicInputs: any;
  instance: any;
  model:any;
  serviceModel:ServiceModel;
  modelInformations: ModelInformationItem[] = [];
  uuidData: Object;
  closeDialogEvent: Subject<any> = new Subject<any>();
  isUpdateMode: boolean;

  constructor(
    private _basicControlGenerator: ControlGeneratorUtil,
    private _pnfControlGenerator: PnfControlGenerator,
    private _iframeService: IframeService,
    private _defaultDataGeneratorService: DefaultDataGeneratorService,
    private _aaiService: AaiService,
    private _basicPopupService: BasicPopupService,
    private _store: NgRedux<AppState>) {
  }

  getGenericFormPopupDetails(serviceId: string, modelName: string, pnfStoreKey: string, node: ITreeNode, uuidData: Object, isUpdateMode: boolean): FormPopupDetails {
    this.uuidData = uuidData;
    this.isUpdateMode = isUpdateMode;
    this.instance = this.getInstance(serviceId, modelName, pnfStoreKey);
    this.getModelInformation(serviceId, modelName);

    return new FormPopupDetails(this,
      PopupType.PNF_MACRO,
      uuidData,
      this.getTitle(isUpdateMode),
      this.getSubLeftTitle(),
      this.getSubRightTitle(),
      this.getControls(serviceId, modelName, pnfStoreKey),
      this._basicPopupService.getDynamicInputs(serviceId, modelName, pnfStoreKey, 'pnfs'),
      this.modelInformations,
      (that, form: FormGroup) => {that.onSubmit(that, form);},
      (that: any, form: FormGroup) => {that.onCancel(that, form); }
      )
  }

  getControls(serviceId: string, modelName: string, pnfStoreKey: string){
    if(this._store.getState().service.serviceHierarchy[serviceId].service.vidNotions.instantiationType === 'Macro') {
      return this._pnfControlGenerator.getMacroFormControls(serviceId, pnfStoreKey, modelName);
    } else {
      return this._pnfControlGenerator.getAlaCarteFormControls(serviceId, pnfStoreKey, modelName);
    }
  }

  getInstance(serviceId: string, modelName: string, pnfStoreKey: string): any {
    if(_.isNil(pnfStoreKey)){
      return new PnfInstance();
    }
    return this._store.getState().service.serviceInstance[serviceId].pnfs[pnfStoreKey];
  }

  getModelInformation(serviceId: string, modelName: string): void {
    this._aaiService.getServiceModelById(serviceId).subscribe((result: any) => {
      this.serviceModel = new ServiceModel(result);
      this.model = this._basicPopupService.getModelFromResponse(result, 'pnfs', modelName);
      const serviceInstance = this._store.getState().service.serviceInstance[serviceId];
      this.modelInformations = [
        new ModelInformationItem("Subscriber Name", "subscriberName", [this.extractSubscriberNameBySubscriberId(serviceInstance.globalSubscriberId, this._store)], "", true),
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
        new ModelInformationItem("Minimum to instantiate", "min", [!_.isNil(this.model.min) ? this.model.min.toString() : '0'], "", false),
        this._basicPopupService.createMaximumToInstantiateModelInformationItem(this.model)
        ];
    })
  }

  getSubLeftTitle(): string {
    return "PNF MODEL: " + this._store.getState().service.serviceHierarchy[this.uuidData['serviceId']].pnfs[this.uuidData['modelName']].name;
  }

  getSubRightTitle(): string {
    return "PNF Instance Details";
  }

  storePNF = (that, formValues: any): void => {
    formValues.modelInfo = new ModelInfo(that.model);
    formValues.uuid = formValues.modelInfo.uuid;
    formValues.isMissingData = false;
    if(!that.isUpdateMode){
      that._store.dispatch(changeInstanceCounter(formValues.modelInfo.modelUniqueId, that.uuidData.serviceId, 1 , <any> {data: {type: 'PNF'}}));
      this._store.dispatch(createPNFInstance(formValues, that.uuidData['modelName'], that.uuidData['serviceId'], that.uuidData['modelName']));
    }else {
      that._store.dispatch(updatePNFInstance(formValues, that.uuidData.modelName, that.uuidData.serviceId, that.uuidData.pnfStoreKey))
    }
  };

  getTitle(isUpdateMode: boolean): string {
    return isUpdateMode  ? "Edit PNF instance": "Set a new PNF" ;
  }

  onCancel(that, form): void {
    form.reset();
    that._iframeService.removeClassCloseModal('content');
    this.closeDialogEvent.next(that);
  }

  onSubmit(that, form: FormGroup, ...args): void {
    form.value['instanceParams'] = form.value['instanceParams'] && [form.value['instanceParams']];
    that.storePNF(that, form.value);
    window.parent.postMessage( {
      eventId: 'submitIframe',
      data: {
        serviceModelId: that.uuidData.serviceId
      }
    }, "*");
    that.onCancel(that, form);
  }

  extractSubscriberNameBySubscriberId(subscriberId: string, store: NgRedux<AppState>) {
    let result: string = null;
    let filteredArray: any = _.filter(store.getState().service.subscribers, function (o: Subscriber) {
      return o.id === subscriberId
    });
    if (filteredArray.length > 0) {
      result = filteredArray[0].name;
    }
    return result;
  }

}
