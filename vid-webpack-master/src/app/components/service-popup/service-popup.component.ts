import {Component, ViewChild} from '@angular/core';
import {DialogComponent, DialogService} from 'ng2-bootstrap-modal';
import {ServiceModel} from '../../shared/models/serviceModel';
import {Constants} from '../../shared/utils/constants';
import {ServiceInstanceDetailsComponent} from './service-instance-details/service-instance-details.component';
import {ActivatedRoute} from "@angular/router";
import {AaiService} from "../../services/aaiService/aai.service";
import {Utils} from "../../utils/utils";
import {ServicePlanningService} from "../../services/service-planning.service";
import * as _ from 'lodash';
import {ModelInformationItem} from '../../shared/components/model-information/model-information.component';
import {deleteServiceInstance} from '../../service.actions';

import {InstancePopup} from "../instance-popup/instance-popup.components";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../store/reducers";
import {ServicePopupService} from './service-popup.service';
import {IframeService} from "../../shared/utils/iframe.service";

export interface ServicePopupModel {
  serviceModel: ServiceModel
}

@Component({
  selector: 'service-popup',
  templateUrl: 'service-popup.html',
  styleUrls: ['service-popup.scss'],
  providers: [AaiService, ServicePopupService]
})

export class ServicePopupComponent extends DialogComponent<ServicePopupModel, boolean>
                                    implements ServicePopupModel, InstancePopup{
  @ViewChild(ServiceInstanceDetailsComponent) serviceInstanceDetails: ServiceInstanceDetailsComponent;

  serviceModel: ServiceModel;
  serviceModelId: string;
  serviceInstance: any = {
    'rollbackOnFailure' : 'false'
  };
  title: string = Constants.ServicePopup.TITLE;
  dynamicInputs: any[] = null;

  maxServiceQty:number = 50;
  minServiceQty:number = 1;
  servicesQty = 1; //default
  quantityOptions = this.getQuantityOptions();

  modelInformationItems: Array<ModelInformationItem> = [];
  hasGeneralApiError : boolean = false;
  parentElementClassName = 'content';

  constructor(dialogService: DialogService, private route: ActivatedRoute, private _aaiService: AaiService,
              private _iframeService : IframeService,
              private _servicePlanningService: ServicePlanningService, private store: NgRedux<AppState>, private _servicePopupService : ServicePopupService) {
    super(dialogService);
    this.title = Constants.ServicePopup.TITLE;
  }

  updateGeneralErrorSection() : void {
    this.hasGeneralApiError = this._servicePopupService.onControlError(this.serviceInstanceDetails, this.serviceInstanceDetails.serviceInstanceDetailsFormGroup);
  }


  ngOnInit() {
    this.route
      .queryParams
      .subscribe(params => {
        this.serviceModelId = params['serviceModelId'];
        if(params['isCreate']=="true") {
          this.store.dispatch(deleteServiceInstance(this.serviceModelId));
        }
        this.updateServiceModelById(this.serviceModelId);
        this.updateInstanceFromStore();
      });
  }

  updateInstanceFromStore() {
    let serviceInstance;
    if (_.has(this.store.getState().service.serviceInstance, this.serviceModelId)) {
      serviceInstance = Object.assign({}, this.store.getState().service.serviceInstance[this.serviceModelId]);
    }

    this.serviceInstance = serviceInstance ? serviceInstance : this.serviceInstance;
    this.servicesQty = serviceInstance ? serviceInstance.bulkSize : 1;
    if (serviceInstance && serviceInstance.instanceParams && serviceInstance.instanceParams[0]) {
      this.dynamicInputs = this.dynamicInputs.map(function (x) {
        x.value = (serviceInstance.instanceParams[0][x.id]) ? serviceInstance.instanceParams[0][x.id] : x.value;
        return x;
      });
    }
  }

  updateServiceModelById(serviceModelId) {
    this._aaiService.getServiceModelById(serviceModelId).subscribe(
      value => {
        const convertedModel = Utils.convertModel(value);
        this.serviceModel = new ServiceModel(convertedModel);
        let displayInputs = Object.assign({},convertedModel.service.inputs);
        this.dynamicInputs = _.isEmpty(displayInputs)? [] : this._servicePlanningService.getArbitraryInputs(displayInputs);
        this.modelInformationItems = this.createModelInformationItems();
      },
      error => {console.log('error is ', error)},
      () => {console.log('completed')}
    );
  }

  createModelInformationItems() : Array<ModelInformationItem> {
     return [
      new ModelInformationItem("Model version", "modelVersion", [this.serviceModel.version], "", true),
      new ModelInformationItem("Description", "description", [this.serviceModel.description]),
      new ModelInformationItem("Category", "category", [this.serviceModel.category]),
      new ModelInformationItem("UUID", "uuid", [this.serviceModel.uuid], Constants.ServicePopup.TOOLTIP_UUID, true),
      new ModelInformationItem("Invariant UUID", "invariantUuid", [this.serviceModel.invariantUuid], Constants.ServicePopup.TOOLTIP_INVARIANT_UUID, true),
      new ModelInformationItem("Service type", "serviceType", [this.serviceModel.serviceType]),
      new ModelInformationItem("Service role", "serviceRole", [this.serviceModel.serviceRole])
    ];
  }

  onCancelClick() {
    this._iframeService.removeClassCloseModal(this.parentElementClassName);
    this.dialogService.removeDialog(this);
    this.serviceInstance = this.serviceInstanceDetails.oldServiceInstance;

    this._servicePopupService.resetDynamicInputs(this.serviceInstanceDetails, this.dynamicInputs);
    // Delaying the iframe close in few milliseconds.
    // This should workaround a problem in Selenium tests' that
    // blocks after click because the iframe goes out before
    // the driver understands it was clicked. Similar bug is
    // described here:
    //  - https://github.com/mozilla/geckodriver/issues/611
    //  - https://bugzilla.mozilla.org/show_bug.cgi?id=1223277
    setTimeout(() => {
      window.parent.postMessage("closeIframe", "*");
    }, 15);
  }

  getModelName(): string {
    return (this.serviceModel && this.serviceModel.name) || "";
  }

  getQuantityOptions(){
    return _.range(this.minServiceQty, this.maxServiceQty + 1);
  }
}
