import {Component, OnInit, ViewChild} from "@angular/core";
import {AaiService} from "../../services/aaiService/aai.service";
import {ModelInformationItem} from "../../shared/components/model-information/model-information.component";
import {ActivatedRoute} from "@angular/router";
import {DialogComponent, DialogService} from "ng2-bootstrap-modal";
import {InstancePopup} from "../instance-popup/instance-popup.components";
import {ServiceModel} from "../../shared/models/serviceModel";
import {Constants} from "../../shared/utils/constants";
import * as _ from "lodash";
import {VnfInstance} from "../../shared/models/vnfInstance";
import {ServiceInstance} from "../../shared/models/serviceInstance";
import {VnfInstanceDetailsComponent} from "./vnf-instance-details/vnf-instance-details.component";
import {Subscriber} from "../../shared/models/subscriber";
import {ServiceNodeTypes} from "../../shared/models/ServiceNodeTypes";
import {AppState} from "../../store/reducers";
import {NgRedux} from "@angular-redux/store";
import {VfModuleInstance} from "../../shared/models/vfModuleInstance";
import {VnfPopupService} from './vnf-popup-service';
import {IframeService} from "../../shared/utils/iframe.service";

export interface VnfPopupModel {
  serviceModelId: string;
  modelName: string;
  parentModelName: string;
  modelType: string;
  dynamicInputs: any;
  userProvidedNaming: boolean;
  isNewVfModule : boolean;
}

@Component({
  selector: 'vnf-popup',
  templateUrl: 'vnf-popup.html',
  styleUrls: ['vnf-popup.scss'],
  providers: [AaiService, VnfPopupService]
})

export class VnfPopupComponent extends DialogComponent<VnfPopupModel, boolean> implements VnfPopupModel, InstancePopup, OnInit {

  @ViewChild(VnfInstanceDetailsComponent) vnfInstanceDetails: VnfInstanceDetailsComponent;

  serviceModelId: string;
  modelName: string;
  parentModelName: string;
  modelType: string;
  isNewVfModule : boolean;
  model: any;
  serviceModel: ServiceModel;
  popupTypeName: string;
  serviceInstance: ServiceInstance;
  vnfInstance: VnfInstance;
  dynamicInputs;
  userProvidedNaming: boolean;
  typeMapperForTitle = {
    VF: "VNF",
    VFmodule: "Module (Heat stack)"
  };

  modelInformationItems: Array<ModelInformationItem> = [];
  isNotUniqueInstanceName : boolean = false;
  isNotUniqueVolumeGroupName : boolean = false;
  hasGeneralApiError : boolean = false;

  parentElementClassName = 'content';

  constructor(dialogService: DialogService, protected route: ActivatedRoute, protected _aaiService: AaiService,
              private store: NgRedux<AppState>,
              private _iframeService : IframeService,
              private _vnfPopupService: VnfPopupService) {
    super(dialogService);
    this.vnfInstance = new VnfInstance();
  }

  updateGeneralErrorSection() : void {
    this.hasGeneralApiError = this._vnfPopupService.onControlError(
      this.vnfInstanceDetails,
      this.vnfInstanceDetails.instanceFormGroup,
      this.vnfInstanceDetails.isNotUniqueInstanceName,
      this.vnfInstanceDetails.isNotUniqueVolumeGroupName);
  }

  ngOnInit(): void {
    this.updateServiceModelById();
    this.popupTypeName = this.getModelTypeForPopupTitle();
    this.updateServiceModelById();
    this.updateInstanceFromStore();
  }

  onCancelClick() {
    this._iframeService.removeClassCloseModal(this.parentElementClassName);
    super.close();
  }

  onServiceInstanceNameChanged(isNotUniqueInstanceName: boolean) : void {
    this.isNotUniqueInstanceName = isNotUniqueInstanceName;
  }

  onVolumeGroupNameChanged(isNotUniqueVolumeGroupName: boolean) : void {
    this.isNotUniqueVolumeGroupName = isNotUniqueVolumeGroupName;
  }

  onSetClick() {
      this._iframeService.removeClassCloseModal(this.parentElementClassName);
      this.result = true;
      super.close();
  }

  updateServiceModelById() {
    this._aaiService.getServiceModelById(this.serviceModelId).subscribe(
      result => {
        this.serviceModel = new ServiceModel(result);
        this.model = this._vnfPopupService.getModelFromResponse(result, this.modelType, this.modelName);
        this.modelInformationItems = this.createModelInformationItems();
      },
      error => {
        console.log('error is ', error)
      }
    );
  }

  updateInstanceFromStore() {
    let instance;
    const serviceInstance = this.store.getState().service.serviceInstance[this.serviceModelId];
    if (this.modelType === ServiceNodeTypes.VF) {
      instance = serviceInstance.vnfs[this.modelName] || new VnfInstance();
    } else {
      instance = new VfModuleInstance();
    }

    if (instance.instanceParams && instance.instanceParams[0]) {
      this.dynamicInputs = this.dynamicInputs.map(x => {
        x.value = (instance.instanceParams[0][x.id]) ? instance.instanceParams[0][x.id] : x.value;
        return x;
      });
    }
    this.vnfInstance = instance;
  }

  getModelName(): string {
    return this.modelName;
  }

  getModelTypeForPopupTitle(): string {
    if (_.has(this.typeMapperForTitle, this.modelType)) {
      return this.typeMapperForTitle[this.modelType];
    }
    return this.modelType;
  }

  extractSubscriberNameBySubscriberId(subsriberId: string) {
    var result: string = null;
    var filteredArray: any = _.filter(this.store.getState().service.subscribers, function (o: Subscriber) {
      return o.id === subsriberId
    })
    if (filteredArray.length > 0) {
      result = filteredArray[0].name;
    }
    return result;
  }

  createModelInformationItems(): Array<ModelInformationItem> {
    var serviceInstance = this.store.getState().service.serviceInstance[this.serviceModelId];

    let items = [
      new ModelInformationItem("Subscriber Name", "subscriberName", [this.extractSubscriberNameBySubscriberId(serviceInstance.globalSubscriberId)], "", true),
      new ModelInformationItem("Service Name", "serviceModelName", [this.serviceModel.name], "", true),

      new ModelInformationItem("Service Instance Name", "serviceName", [serviceInstance.instanceName], "", false),
      new ModelInformationItem("Model Name", "modelName", [this.model.name], "", true),
      new ModelInformationItem("Model version", "modelVersion", [this.model.version], "", true),
      new ModelInformationItem("Description", "description", [this.model.description]),
      new ModelInformationItem("Category", "category", [this.model.category]),
      new ModelInformationItem("Sub Category", "subCategory",[this.model.subCategory]),
      new ModelInformationItem("UUID", "uuid", [this.model.uuid], Constants.ServicePopup.TOOLTIP_UUID, true),
      new ModelInformationItem("Invariant UUID", "invariantUuid", [this.model.invariantUuid], Constants.ServicePopup.TOOLTIP_INVARIANT_UUID, true),
      new ModelInformationItem("Service type", "serviceType", [this.serviceModel.serviceType]),
      new ModelInformationItem("Service role", "serviceRole", [this.serviceModel.serviceRole]),


    ];
    if (this.modelType === 'VFmodule') {
      items.push(new ModelInformationItem("Minimum to instantiate", "min", [this.model.min], "", true),
        new ModelInformationItem("Maximum to instantiate", "max", this.model.max == undefined ? [1] : [this.model.max], "", true),
        new ModelInformationItem("Recommended to instantiate", "initial", [this.model.initial]));

    }

    return items;
  }
}
