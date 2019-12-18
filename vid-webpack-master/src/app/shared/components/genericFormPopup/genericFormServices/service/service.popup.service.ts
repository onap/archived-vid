import {Injectable} from "@angular/core";
import {GenericPopupInterface} from "../generic-popup.interface";
import {ServiceModel} from "../../../../models/serviceModel";
import {ModelInformationItem} from "../../../model-information/model-information.component";
import {Subject} from "rxjs";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {FormPopupDetails, PopupType} from "../../../../models/formControlModels/formPopupDetails.model";
import {ControlGeneratorUtil} from "../../../genericForm/formControlsServices/control.generator.util.service";
import {IframeService} from "../../../../utils/iframe.service";
import {DefaultDataGeneratorService} from "../../../../services/defaultDataServiceGenerator/default.data.generator.service";
import {AaiService} from "../../../../services/aaiService/aai.service";
import {BasicPopupService} from "../basic.popup.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../store/reducers";
import {ServiceControlGenerator} from "../../../genericForm/formControlsServices/service.control.generator";
import {FormGroup} from "@angular/forms";
import {Constants} from "../../../../utils/constants";
import {ServiceInstance} from "../../../../models/serviceInstance";
import {ModelInfo} from "../../../../models/modelInfo";
import {FormControlModel} from "../../../../models/formControlModels/formControl.model";
import {createServiceInstance, updateServiceInstance} from "../../../../storeUtil/utils/service/service.actions";
import * as _ from 'lodash';
import {Utils} from "../../../../utils/utils";

@Injectable()
export class ServicePopupService implements GenericPopupInterface {
  dynamicInputs: any;
  instance: any;
  model: any;
  serviceModel: ServiceModel;
  modelInformations: ModelInformationItem[] = [];
  uuidData: Object;
  closeDialogEvent: Subject<any> = new Subject<any>();
  isUpdateMode: boolean;



  constructor(private _basicControlGenerator: ControlGeneratorUtil,
              private _serviceControlGenerator: ServiceControlGenerator,
              private _iframeService: IframeService,
              private _defaultDataGeneratorService: DefaultDataGeneratorService,
              private _aaiService: AaiService,
              private _basicPopupService : BasicPopupService,
              private _store: NgRedux<AppState>) {

  }

  getGenericFormPopupDetails(serviceId: string, modelName: string, storeKey: string, node: ITreeNode, uuidData: Object, isUpdateMode: boolean): FormPopupDetails {
    this.uuidData = uuidData;
    this.instance = this.getInstance(serviceId);
    this.getModelInformation(serviceId);
    return new FormPopupDetails(this,
      PopupType.SERVICE,
      uuidData,
      this.getTitle(isUpdateMode),
      this.getSubLeftTitle(),
      this.getSubRightTitle(),
      this.getControls(serviceId),
      this.getDynamicInputs(serviceId),
      this.modelInformations,
      (that, form: FormGroup) => {that.onSubmit(that, form);},
      (that: any, form: FormGroup) => {
        that.onCancel(that, form);
      },
      (that: any, form: FormGroup) => {
        that.showPreviousInstantiations(that, form);
      }
    );
  }

  getDynamicInputs(serviceId) : FormControlModel[]{
    let dynamic = this._defaultDataGeneratorService.getArbitraryInputs(this._store.getState().service.serviceHierarchy[serviceId].service.inputs);
    return this._basicControlGenerator.getServiceDynamicInputs(dynamic, serviceId);
  }


  getControls(serviceId: string) {
    if(this._store.getState().service.serviceHierarchy[serviceId].service.vidNotions.instantiationType === 'Macro') {
      return this._serviceControlGenerator.getMacroFormControls(serviceId, this.dynamicInputs);
    }else {
      return this._serviceControlGenerator.getAlaCartControls(serviceId, this.dynamicInputs);
    }
  }

  getInstance(serviceId): ServiceInstance {
    let result: ServiceInstance = null;
    if(!_.isNil(this._store.getState().service.serviceInstance[serviceId])){
      result = this._store.getState().service.serviceInstance[serviceId];
    }
    return result;
  }

  getModelInformation(serviceId: string): void {
    this._aaiService.getServiceModelById(serviceId).subscribe((result: any) => {
      this.serviceModel = new ServiceModel(result);

      this.model = this._store.getState().service.serviceHierarchy[serviceId];
      this.modelInformations = [
          new ModelInformationItem("Model version", "modelVersion", [this.serviceModel.version], "", true),
          new ModelInformationItem("Description", "description", [this.serviceModel.description]),
          new ModelInformationItem("Category", "category", [this.serviceModel.category]),
          new ModelInformationItem("UUID", "uuid", [this.serviceModel.uuid], Constants.ServicePopup.TOOLTIP_UUID, true),
          new ModelInformationItem("Invariant UUID", "invariantUuid", [this.serviceModel.invariantUuid], Constants.ServicePopup.TOOLTIP_INVARIANT_UUID, true),
          new ModelInformationItem("Service type", "serviceType", [this.serviceModel.serviceType]),
          new ModelInformationItem("Service role", "serviceRole", [this.serviceModel.serviceRole])
      ];
    });
  }

  getSubLeftTitle(): string {
    return this._store.getState().service.serviceHierarchy[this.uuidData['serviceId']].service.name;
  }

  getSubRightTitle(): string {
    return "Service Instance Details";
  }

  getTitle(isUpdateMode: boolean): string {
    return isUpdateMode ? "Edit service instance" :  "Set a new service instance";
  }

  onCancel(that, form): void {
    form.reset();
    that._iframeService.removeClassCloseModal('content');
    this.closeDialogEvent.next(that);
  }

  onSubmit(that, form: FormGroup, ...args): void {
    form = that.updateExtraValues(that, form);
    that.storeServiceInstance(form.value, args[0], [], new ModelInfo(that.serviceModel), that.serviceModel);
    const eventId = 'submitIframe';
    this.postMessageToWindowParent(eventId, that.serviceModel.uuid);
    this.onCancel(that, form);
  }

  showPreviousInstantiations(that, form: FormGroup,): void {
    const eventId = 'showPreviousInstantiations';
    this.postMessageToWindowParent(eventId, that.serviceModel.uuid);
    this.onCancel(that, form);
  }

  private postMessageToWindowParent(eventId: string, serviceModelId:string) {
    window.parent.postMessage({
      eventId: eventId,
      data: {
        serviceModelId
      }
    }, "*");
  }

  updateExtraValues = (that, form) : any => {
    const service = that._store.getState().service;
    form.value['bulkSize'] = that.uuidData['bulkSize'];
    form.value['instanceParams'] = form.value['instanceParams'] && [form.value['instanceParams']];
    form.value['aicZoneName'] = !_.isNil(form.value['aicZoneId']) ?this.getNameFromListById(service.aicZones, form.value['aicZoneId']) : null;
    form.value['owningEntityName'] = !_.isNil(form.value['owningEntityId']) ?this.getNameFromListById(service.categoryParameters.owningEntityList, form.value['owningEntityId']) : null;
    form.value['testApi'] = sessionStorage.getItem("msoRequestParametersTestApiValue");
    form.value['tenantName'] = this.getNameFromListById(service.lcpRegionsAndTenants.lcpRegionsTenantsMap[form.value['lcpCloudRegionId']], form.value['tenantId']);
    return form;
  };

  getNameFromListById(list, id) {
    if(list && id) {
      return list.find(item => item.id === id).name;
    }
    return null;
  }

  storeServiceInstance = (formValues:any, servicesQty:number, dynamicInputs:any, serviceModel:ModelInfo, serviceDetails: any ) => {
    formValues.bulkSize = this.uuidData['bulkSize'];
    formValues.modelInfo = serviceModel;
    let instantiationType = this._store.getState().service.serviceHierarchy[serviceModel.modelVersionId].service.vidNotions.instantiationType;
    this.setIsALaCarte(formValues, instantiationType);
    this.setTestApi(formValues);
    Object.assign(formValues, serviceDetails);
    let isCreateMode: boolean = this._store.getState().service.serviceInstance[serviceModel.modelVersionId] == null;
    if(isCreateMode){
      this._store.dispatch(createServiceInstance(formValues, serviceModel.modelVersionId));
    }else {
      this._store.dispatch(updateServiceInstance(formValues, serviceModel.modelVersionId));
    }

    if (isCreateMode) {
      this._defaultDataGeneratorService.updateReduxOnFirstSet(serviceModel.modelVersionId, formValues);
    }
  };

  setIsALaCarte = (formValues: any, instantiationType) => {
    formValues.isALaCarte = Utils.isALaCarte(instantiationType);
  };

  setTestApi = (formValues: any) =>{
    if (this._store.getState().global.flags['FLAG_ADD_MSO_TESTAPI_FIELD'] && formValues.isAlaCarte) {
      formValues.testApi = sessionStorage.getItem("msoRequestParametersTestApiValue");
    }
  };
}
