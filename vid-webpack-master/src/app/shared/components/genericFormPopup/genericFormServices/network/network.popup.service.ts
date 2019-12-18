import {Injectable} from "@angular/core";
import {FormPopupDetails, PopupType} from "../../../../models/formControlModels/formPopupDetails.model";
import {ControlGeneratorUtil} from "../../../genericForm/formControlsServices/control.generator.util.service";
import {NetworkInstance} from "../../../../models/networkInstance";
import {AppState} from "../../../../store/reducers";
import {NgRedux} from "@angular-redux/store";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {NetworkControlGenerator} from "../../../genericForm/formControlsServices/networkGenerator/network.control.generator";
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
import * as _ from 'lodash';
import {BasicPopupService} from "../basic.popup.service";
import {changeInstanceCounter} from "../../../../storeUtil/utils/general/general.actions";
import {createNetworkInstance, updateNetworkInstance} from "../../../../storeUtil/utils/network/network.actions";

@Injectable()
export class NetworkPopupService implements GenericPopupInterface {
  dynamicInputs: any;
  instance: any;
  model: any;
  serviceModel: ServiceModel;
  modelInformations: ModelInformationItem[] = [];
  uuidData: Object;
  closeDialogEvent: Subject<any> = new Subject<any>();
  isUpdateMode: boolean;


  constructor(private _basicControlGenerator: ControlGeneratorUtil,
              private _networkControlGenerator: NetworkControlGenerator,
              private _iframeService: IframeService,
              private _defaultDataGeneratorService: DefaultDataGeneratorService,
              private _aaiService: AaiService,
              private _basicPopupService : BasicPopupService,
              private _store: NgRedux<AppState>) {
  }

  getInstance(serviceId: string, networkName: string, networkStoreKey: string): any {
    if (_.isNil(networkStoreKey)) {
      return new NetworkInstance();
    }
    return this._store.getState().service.serviceInstance[serviceId].networks[networkStoreKey];
  }

  getGenericFormPopupDetails(serviceId: string, networkName: string, networkStoreKey: string, node: ITreeNode, uuidData: Object, isUpdateMode: boolean): FormPopupDetails {
    this.uuidData = uuidData;
    this.instance = this.getInstance(serviceId, networkName, networkStoreKey);


    this.getModelInformation(serviceId, networkName);

    return new FormPopupDetails(this,
      PopupType.NETWORK_MACRO,
      uuidData,
      this.getTitle(isUpdateMode),
      this.getSubLeftTitle(),
      this.getSubRightTitle(),
      this.getControls(serviceId, networkName, networkStoreKey, isUpdateMode),
      this._basicPopupService.getDynamicInputs(serviceId,networkName, networkStoreKey, 'networks'),
      this.modelInformations,
      (that, form: FormGroup) => {that.onSubmit(that, form);},
      (that: any, form: FormGroup) => {that.onCancel(that, form); }
    );
  }

  getModelInformation(serviceId: string, networkName: string) {
    this._aaiService.getServiceModelById(serviceId).subscribe((result: any) => {
      this.serviceModel = new ServiceModel(result);
      this.model = this._basicPopupService.getModelFromResponse(result, 'networks', networkName);

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
        new ModelInformationItem("Network roles", "role", this.model.roles, "", false)
      ];
    });
  }

  getControls(serviceId: string, networkName: string, networkStoreKey: string, isUpdateMode: boolean) {
    if (this._store.getState().service.serviceHierarchy[serviceId].service.vidNotions.instantiationType === 'Macro') {
      return this._networkControlGenerator.getMacroFormControls(serviceId, networkStoreKey, networkName, isUpdateMode);
    } else {
      return this._networkControlGenerator.getAlaCarteFormControls(serviceId, networkStoreKey, networkName, isUpdateMode);
    }
  }


  onSubmit(that, form: FormGroup) {
    form.value['instanceParams'] = form.value['instanceParams'] && [form.value['instanceParams']];
    that.storeNetwork(that, form.value);
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

  storeNetwork = (that, formValues: any): void => {
    formValues.modelInfo = new ModelInfo(that.model);
    formValues.uuid = formValues.modelInfo.uuid;
    formValues.isMissingData = false;

    if (!that.uuidData.networkStoreKey) {
      this._store.dispatch(changeInstanceCounter(formValues.modelInfo.modelUniqueId , that.uuidData.serviceId, 1, <any>{data: {type: 'Network'}}));
      this._store.dispatch(createNetworkInstance(formValues, that.uuidData.networkId, that.uuidData.serviceId, that.uuidData.networkId));
    } else {
      this._store.dispatch(updateNetworkInstance(formValues, that.uuidData.networkId, that.uuidData.serviceId, that.uuidData.networkStoreKey));
    }
  };


  getTitle(isUpdateMode : boolean) : string {
    return isUpdateMode ? 'Edit Network' : 'Set new Network';
  }

  getSubLeftTitle(): string {
    return this.uuidData['networkId'];
  }

  getSubRightTitle(): string {
    return "Network Instance Details";
  }
}
