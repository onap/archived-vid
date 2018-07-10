import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ServicePopupDataModel} from './servicePopupDataModel';
import {AaiService} from '../../../services/aaiService/aai.service';
import {updateServiceInstance} from "../../../service.actions";
import * as _ from 'lodash';
import {ServiceModel} from "../../../shared/models/serviceModel";
import {ModelInfo} from "../../../shared/models/modelInfo";
import {loadProductFamiliesAction} from "../../../services/aaiService/aai.actions";
import {Observable} from "rxjs/Observable";
import {SelectOptionInterface} from "../../../shared/models/selectOption";
import {NgRedux, select} from "@angular-redux/store";
import {AppState} from "../../../store/reducers";
import {isNullOrUndefined} from 'util';
import {ServiceInstanceDetailsService} from './service-instance-details.service';
import {NumbersLettersUnderscoreValidator} from '../../../shared/components/validators/numbersLettersUnderscore/numbersLettersUnderscore.validator';
import {DefaultDataGeneratorService} from '../../../shared/services/defaultDataServiceGenerator/default.data.generator.service';


@Component({
  selector: 'service-instance-details',
  templateUrl: 'service-instance-details.html',
  styleUrls: ['service-instance-details.scss'],
  providers: [AaiService]
})

export class ServiceInstanceDetailsComponent implements OnInit, OnChanges {
  ngOnChanges(changes: SimpleChanges): void {
    if (changes["serviceInstance"] !== undefined && changes["serviceInstance"].currentValue !== changes["serviceInstance"].previousValue && changes["serviceInstance"].currentValue !== null) {
      this.oldServiceInstance = Object.assign({}, this.serviceInstance);
    }
  }
  _serviceModel: ServiceModel;
  @Input () serviceInstance: any;
  @Input () dynamicInputs;
  @Input () servicesQty: number;
  @Input ()
  set serviceModel(serviceModel: ServiceModel) {
    this._serviceModel = serviceModel;
    this.updateFormGroupControlsWithServiceModel(serviceModel);
  }
  @ViewChild('serviceForm') serviceForm: 'ServiceForm';
  @Output() closePopup : EventEmitter<any> = new EventEmitter<any>();
  @Output() onDataChanged: EventEmitter<any> = new EventEmitter<any>();
  oldServiceInstance = {};

  //todo: implement Epics and use @select to fetch the rest of the form's data as done with productFamilies.
  //that way we can loose the updateFormData function and the subscription to store in the constructor.
  @select(['service','productFamilies'])
  readonly productFamilies : Observable<SelectOptionInterface[]>;
  serviceDetails:any = {

  };
  servicePopupDataModel: ServicePopupDataModel = new ServicePopupDataModel();
  serviceInstanceDetailsFormGroup: FormGroup;
  serviceInstanceDetailsService : ServiceInstanceDetailsService;

  constructor(private _aaiService: AaiService, private store: NgRedux<AppState>, private _serviceInstanceDetailsService : ServiceInstanceDetailsService, private _defaultDataGeneratorService : DefaultDataGeneratorService) {
    this.store.subscribe(() => {this.updateFormData()});
    this.serviceInstanceDetailsService = this._serviceInstanceDetailsService;
    this.serviceInstanceDetailsFormGroup = this.createFormGroup();

    this.serviceInstanceDetailsFormGroup.valueChanges.subscribe(()=> {
      this.onDataChanged.next();
    })
  }

  ngOnInit() {
    this.subscribeToFormChanges();
    this._aaiService.getSubscribers().subscribe();
    this._aaiService.getCategoryParameters(null).subscribe();
    this._aaiService.getAicZones().subscribe();
    this.store.dispatch(loadProductFamiliesAction());
  }


  createFormGroup(): FormGroup {
    const formGroup = new FormGroup({
      globalSubscriberId: new FormControl(
        Validators.compose([Validators.required])
      ),
      productFamilyId: new FormControl(),
      subscriptionServiceType: new FormControl({value: null,  disabled: true}, Validators.compose([Validators.required])),
      lcpCloudRegionId: new FormControl({value: null,  disabled: true}, Validators.compose([Validators.required])),
      tenantId: new FormControl({value: null,  disabled: true}, Validators.compose([Validators.required])),
      aicZoneId: new FormControl(),
      projectName: new FormControl(),
      owningEntityId: new FormControl(Validators.compose([Validators.required])),
      rollbackOnFailure: new FormControl(null, Validators.required),
    });

    return formGroup;
  }

  updateFormGroupControlsWithServiceModel(serviceModel: ServiceModel) {
    this.serviceInstanceDetailsFormGroup.markAsUntouched();

    if (serviceModel) {
      this.serviceDetails.isUserProvidedNaming = serviceModel.isUserProvidedNaming;
      if (serviceModel.isUserProvidedNaming) {
        this.serviceInstanceDetailsFormGroup.addControl('instanceName', new FormControl('', Validators.compose([Validators.required, NumbersLettersUnderscoreValidator.valid])))
      }else{
        this.serviceInstanceDetailsFormGroup.removeControl('instanceName');
      }

      if (serviceModel.isMultiStepDesign) {
        this.serviceInstanceDetailsFormGroup.addControl('pause', new FormControl(true));
      }else{
        this.serviceInstanceDetailsFormGroup.removeControl('pause');
      }
    }
  }

  updateFormData() {
    let service = this.store.getState().service;
    this.servicePopupDataModel.subscribers = service.subscribers;
    this.servicePopupDataModel.serviceTypes = service.serviceTypes[this.servicePopupDataModel.globalCustomerId];
    this.servicePopupDataModel.lcpRegions = service.lcpRegionsAndTenants.lcpRegionList;
    if (this.serviceInstance) {
      this.servicePopupDataModel.tenants = service.lcpRegionsAndTenants.lcpRegionsTenantsMap[this.serviceInstance.lcpCloudRegionId];
    }
    this.servicePopupDataModel.aicZones = service.aicZones;
    this.servicePopupDataModel.owningEntities = _.get(service.categoryParameters, 'owningEntityList');
    this.servicePopupDataModel.projects = _.get(service.categoryParameters, 'projectList');
    this.onDataChanged.next();
  }

  subscribeToFormChanges(): void {
    this.serviceInstanceDetailsFormGroup.get('globalSubscriberId').valueChanges.subscribe(val => {
      this.updateServiceTypes(val);
      this.setDisabledState(val, 'subscriptionServiceType');

    });
    this.serviceInstanceDetailsFormGroup.get('subscriptionServiceType').valueChanges.subscribe(val => {
      this.getTenants(val);
      this.setDisabledState(val, 'lcpCloudRegionId');

    });
    this.serviceInstanceDetailsFormGroup.get('lcpCloudRegionId').valueChanges.subscribe(val => {
      this.setDisabledState(val, 'tenantId');
      this.updateTenantList(val);

    });

    this.serviceInstanceDetailsFormGroup.get('tenantId').valueChanges.subscribe(val => {
      this.serviceDetails.tenantName = this.getNameFromListById(this.servicePopupDataModel.tenants, val);
      this.onDataChanged.next();
    });

    this.serviceInstanceDetailsFormGroup.get('aicZoneId').valueChanges.subscribe(val => {
      this.serviceDetails.aicZoneName = this.getNameFromListById(this.servicePopupDataModel.aicZones, val);
      this.onDataChanged.next();
    });
  }

  getNameFromListById(list, id:string ) {
    if(list && id) {
      let filterItem = list.filter(item => {
        return item.id == id;
      })
      return filterItem && filterItem[0].name;
    }
    return null;
  }

  setDisabledState(val, field: string): void {
    if(val) {
      this.serviceInstanceDetailsFormGroup.controls[field].enable();
    } else {
      this.serviceInstanceDetailsFormGroup.controls[field].disable();
    }
  }

  isShowingNotificationArea(): boolean {
    return this.servicesQty > 1;
  }

  updateServiceTypes(subscriberId) {
    if (subscriberId) {
      this.servicePopupDataModel.globalCustomerId = subscriberId;
      this._aaiService.getServiceTypes(subscriberId).subscribe(() => {
        this.updateFormData();
        this.onDataChanged.next();
      }, (error) => {

      });
    }
  }

  updateTenantList(cloudRegionId) {
    this.servicePopupDataModel.tenants = this.store.getState().service.lcpRegionsAndTenants.lcpRegionsTenantsMap[cloudRegionId];
    this.onDataChanged.next();
  }

  getTenants(serviceType) {
    if (serviceType) {
      this._aaiService.getLcpRegionsAndTenants(this.servicePopupDataModel.globalCustomerId, serviceType).subscribe(()=>{
        this.onDataChanged.next();
      });
    }
  }

  onSubmit(formValues): void {
    formValues.bulkSize = this.servicesQty;
    let dynamicFields: { [dynamicField: string] : string; };
    dynamicFields = {};
    this.dynamicInputs.map(function (x) {
      let dynamicField: string = x.id;
      dynamicFields[dynamicField] = formValues[dynamicField];
      delete formValues[dynamicField];
    });
    formValues.instanceParams = [];
    formValues.instanceParams.push(dynamicFields);
    formValues.modelInfo = new ModelInfo(this._serviceModel);
    Object.assign(formValues, this.serviceDetails);
    this.store.dispatch(updateServiceInstance(formValues, this._serviceModel.uuid));
    if (this.store.getState().global.flags['FLAG_SETTING_DEFAULTS_IN_DRAWING_BOARD']){
      this._defaultDataGeneratorService.updateReduxOnFirstSet(this._serviceModel.uuid,formValues);
    }
    window.parent.postMessage( {
      eventId: 'submitIframe',
      data: {
        serviceModelId: this._serviceModel.uuid
      }
    }, "*");
    this.closePopup.emit(this._serviceModel.uuid);
  }

  hasApiError(controlName : string, data : Array<any>){
    if(!isNullOrUndefined(this.servicePopupDataModel) && !isNullOrUndefined(data)){
      if(!this.serviceInstanceDetailsFormGroup.controls[controlName].disabled && data.length === 0){
          return true;
      }
    }
    return false;
  }

}
