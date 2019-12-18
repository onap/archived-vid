import {Injectable} from '@angular/core';
import {GenericPopupInterface} from "../generic-popup.interface";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {FormPopupDetails, PopupType} from "../../../../models/formControlModels/formPopupDetails.model";
import {FormGroup} from "@angular/forms";
import {ModelInformationItem} from "../../../model-information/model-information.component";
import {ServiceModel} from "../../../../models/serviceModel";
import {Subject} from "rxjs/Subject";
import {ControlGeneratorUtil} from "../../../genericForm/formControlsServices/control.generator.util.service";
import {IframeService} from "../../../../utils/iframe.service";
import {DefaultDataGeneratorService} from "../../../../services/defaultDataServiceGenerator/default.data.generator.service";
import {AaiService} from "../../../../services/aaiService/aai.service";
import {BasicPopupService} from "../basic.popup.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../store/reducers";
import {Subscriber} from "../../../../models/subscriber";
import {Constants} from "../../../../utils/constants";
import {ModelInfo} from "../../../../models/modelInfo";
import {changeInstanceCounter} from "../../../../storeUtil/utils/general/general.actions";
import * as _ from 'lodash';
import {VnfGroupControlGenerator} from "../../../genericForm/formControlsServices/vnfGroupGenerator/vnfGroup.control.generator";
import {VnfGroupInstance} from "../../../../models/vnfGroupInstance";
import {createVnfGroupInstance, updateVnfGroupInstance} from "../../../../storeUtil/utils/vnfGroup/vnfGroup.actions";

@Injectable()
export class VnfGroupPopupService implements GenericPopupInterface{
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
    private _vnfGroupControlGenerator: VnfGroupControlGenerator,
    private _iframeService: IframeService,
    private _defaultDataGeneratorService: DefaultDataGeneratorService,
    private _aaiService: AaiService,
    private _basicPopupService: BasicPopupService,
    private _store: NgRedux<AppState>) {
  }

  getGenericFormPopupDetails(serviceId: string, modelName: string, vnfGroupStoreKey: string, node: ITreeNode, uuidData: Object, isUpdateMode: boolean): FormPopupDetails {
    this.uuidData = uuidData;
    this.isUpdateMode = isUpdateMode;
    this.instance = this.getInstance(serviceId, modelName, vnfGroupStoreKey);
    this.getModelInformation(serviceId, modelName);

    return new FormPopupDetails(this,
      PopupType.VNF_GROUP,
      uuidData,
      this.getTitle(isUpdateMode),
      this.getSubLeftTitle(),
      this.getSubRightTitle(),
      this.getControls(serviceId, modelName, vnfGroupStoreKey),
      this._basicPopupService.getDynamicInputs(serviceId, modelName, vnfGroupStoreKey, 'vnfGroups'),
      this.modelInformations,
      (that, form: FormGroup) => {that.onSubmit(that, form);},
      (that: any, form: FormGroup) => {that.onCancel(that, form); }
      )
  }

  getControls(serviceId: string, modelName: string, vnfGroupStoreKey: string){
    if(this._store.getState().service.serviceHierarchy[serviceId].service.vidNotions.instantiationType === 'Macro') {
      return this._vnfGroupControlGenerator.getMacroFormControls(serviceId, vnfGroupStoreKey, modelName);
    } else {
      return this._vnfGroupControlGenerator.getAlaCarteFormControls(serviceId, vnfGroupStoreKey, modelName);
    }
  }

  getInstance(serviceId: string, modelName: string, vnfGroupStoreKey: string): any {
    if(_.isNil(vnfGroupStoreKey)){
      return new VnfGroupInstance();
    }
    return this._store.getState().service.serviceInstance[serviceId].vnfGroups[vnfGroupStoreKey];
  }

  getModelInformation(serviceId: string, modelName: string): void {
    this._aaiService.getServiceModelById(serviceId).subscribe((result: any) => {
      this.serviceModel = new ServiceModel(result);
      this.model = this._basicPopupService.getModelFromResponse(result, 'vnfGroups', modelName);
      const serviceInstance = this._store.getState().service.serviceInstance[serviceId];
      this.modelInformations = [
        new ModelInformationItem("Model version", "modelVersion", [this.model.version], "", true),
        new ModelInformationItem("Description", "description", [this.model.description]),
        new ModelInformationItem("Category", "category", [this.model.category]),
        new ModelInformationItem("Sub Category", "subCategory", [this.model.subCategory]),
        new ModelInformationItem("UUID", "uuid", [this.model.uuid], Constants.ServicePopup.TOOLTIP_UUID, true),
        new ModelInformationItem("Invariant UUID", "invariantUuid", [this.model.invariantUuid], Constants.ServicePopup.TOOLTIP_INVARIANT_UUID, true),
        new ModelInformationItem("Type", "type", [this.model.properties.type], "", true),
        new ModelInformationItem("Role", "role", [this.model.properties.role]),
        new ModelInformationItem("Function", "function", [this.model.properties.function]),
        new ModelInformationItem("Member resource type", "contained_resource_type", ["VNF"], "", true),
        new ModelInformationItem("Members service Invariant UUID", "Members service Invariant UUID", _.toArray(_.mapValues(this.model.members, 'sourceModelInvariant'))),
        new ModelInformationItem("Members service model name", "sourceModelName", _.toArray(_.mapValues(this.model.members, 'sourceModelName'))),
        new ModelInformationItem("Minimum to instantiate", "vnfGroup-min", ['0'], "", false),
        new ModelInformationItem("Maximum to instantiate", "vnfGroup-max", ['Unlimited'], "", false)
        ];
    })
  }

  getSubLeftTitle(): string {
    return "VNF Group : " + this._store.getState().service.serviceHierarchy[this.uuidData['serviceId']].vnfGroups[this.uuidData['modelName']].name;
  }

  getSubRightTitle(): string {
    return "VNF Group Instance Details";
  }

  storeVnfGroup = (that, formValues: any): void => {
    formValues.modelInfo = new ModelInfo(that.model);
    formValues.uuid = formValues.modelInfo.uuid;
    formValues.isMissingData = false;
    if(!that.isUpdateMode){
      that._store.dispatch(changeInstanceCounter(formValues.modelInfo.modelCustomizationId || formValues.uuid, that.uuidData.serviceId, 1 , <any> {data: {type: 'VnfGroup'}}));
      this._store.dispatch(createVnfGroupInstance(formValues, that.uuidData['modelName'], that.uuidData['serviceId'], that.uuidData['modelName']));
    }else {
      that._store.dispatch(updateVnfGroupInstance(formValues, that.uuidData.modelName, that.uuidData.serviceId, that.uuidData.vnfGroupStoreKey))
    }
  };

  getTitle(isUpdateMode: boolean): string {
    return isUpdateMode  ? "Edit VNF Group instance": "Set a new VNF Group" ;
  }

  onCancel(that, form): void {
    form.reset();
    that._iframeService.removeClassCloseModal('content');
    this.closeDialogEvent.next(that);
  }

  onSubmit(that, form: FormGroup, ...args): void {
    form.value['instanceParams'] = [{}];
    that.storeVnfGroup(that, form.value);
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
