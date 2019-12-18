import {Injectable} from "@angular/core";
import {GenericFormService} from "../generic-form.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../store/reducers";
import {FormControlModel, ValidatorModel, ValidatorOptions} from "../../../models/formControlModels/formControl.model";
import {DropdownFormControl} from "../../../models/formControlModels/dropdownFormControl.model";
import * as _ from 'lodash';
import {ControlGeneratorUtil} from "./control.generator.util.service";
import {AaiService} from "../../../services/aaiService/aai.service";
import {FormGroup} from "@angular/forms";
import {FormControlType} from "../../../models/formControlModels/formControlTypes.enum";
import {HttpClient} from "@angular/common/http";
import {SelectOption} from "../../../models/selectOption";
import {Observable, of} from "rxjs";
import {LogService} from "../../../utils/log/log.service";
import {ServiceModel} from "../../../models/serviceModel";

import {CheckboxFormControl} from "../../../models/formControlModels/checkboxFormControl.model";
import {VidNotions} from "../../../models/vidNotions";
import {SharedControllersService} from "./sharedControlles/shared.controllers.service";

export enum FormControlNames {
  INSTANCE_NAME = 'instanceName',
  GLOBAL_SUBSCRIBER_ID = 'globalSubscriberId',
  SUBSCRIPTION_SERVICE_TYPE = 'subscriptionServiceType',
  PRODUCT_FAMILY_ID = 'productFamilyId',
  LCPCLOUD_REGION_ID = 'lcpCloudRegionId',
  TENANT_ID = 'tenantId',
  AICZONE_ID = 'aicZoneId',
  PROJECT_NAME = 'projectName',
  OWNING_ENTITY_ID = 'owningEntityId',
  ROLLBACK_ON_FAILURE = 'rollbackOnFailure',
  PAUSE = 'pause'
}

@Injectable()
export class  ServiceControlGenerator {
  aaiService : AaiService;
  constructor(private genericFormService : GenericFormService,
              private _basicControlGenerator : ControlGeneratorUtil,
              private _sharedControllersService : SharedControllersService,
              private store: NgRedux<AppState>,
              private http: HttpClient,
              private _aaiService : AaiService,
              private _logService : LogService){
    this.aaiService = _aaiService;
  }

  getServiceInstance = (serviceId : string) : any => {
    let serviceInstance = null;
    if (_.has(this.store.getState().service.serviceInstance, serviceId)) {
      serviceInstance = Object.assign({}, this.store.getState().service.serviceInstance[serviceId]);
    }

    return serviceInstance;
  };

  getAlaCartControls(serviceId: string, dynamicInputs?: any[]) : FormControlModel[] {
    if(_.isNil(serviceId)){
      this._logService.error('should provide serviceId', serviceId);
      return [];
    }
    const serviceInstance = this.getServiceInstance(serviceId);

    let result : FormControlModel[] = [];

    const serviceModel = new ServiceModel(this.store.getState().service.serviceHierarchy[serviceId]);
    if(!_.isNil(serviceModel)){
      result.push(this._sharedControllersService.getInstanceName(serviceInstance, serviceId, serviceModel.isEcompGeneratedNaming));
      result.push(this.getGlobalSubscriberControl(serviceInstance, result));
      result.push(this.getServiceTypeControl(serviceInstance, result, false));
      result.push(this.getOwningEntityControl(serviceInstance, result));
      result.push(this.getProjectControl(serviceInstance, result));
      result.push(this.getRollbackOnFailureControl(serviceInstance, result));
    }

    this._logService.info('Generate dynamic service controls, is edit mode: ' + serviceInstance != null , result);
    return result;
  }

  getMacroFormControls(serviceId: string, dynamicInputs?: any[]) : FormControlModel[] {
    if(_.isNil(serviceId)){
      this._logService.error('should provide serviceId', serviceId);
      return [];
    }

    const serviceInstance = this.getServiceInstance(serviceId);

    let result : FormControlModel[] = [];
    const serviceModel = new ServiceModel(this.store.getState().service.serviceHierarchy[serviceId]);
    if(!_.isNil(serviceModel)){
      result.push(this._sharedControllersService.getInstanceName(serviceInstance, serviceId, serviceModel.isEcompGeneratedNaming));
      result.push(this.getGlobalSubscriberControl(serviceInstance, result));
      result.push(this.getServiceTypeControl(serviceInstance, result, true));
      result.push(this.getOwningEntityControl(serviceInstance, result));
      result.push(this.getProductFamilyControl(serviceInstance, result));
      result.push(this.getLcpRegionControl(serviceInstance, result, serviceModel.vidNotions));
      result.push(this.getTenantControl(serviceInstance, result, serviceModel.vidNotions),);
      result.push(this.getAICZoneControl(serviceInstance, result));



      if(serviceModel.isMultiStepDesign){
        result.push(new CheckboxFormControl({
          controlName : FormControlNames.PAUSE,
          displayName : 'Pause on pause points',
          dataTestId : 'Pause',
          isDisabled : false,
          validations : [new ValidatorModel(ValidatorOptions.required, 'is required')],
          value : serviceInstance ? serviceInstance.pause : null,
        }));
      }

      result.push(this.getProjectControl(serviceInstance, result));
      result.push(this.getRollbackOnFailureControl(serviceInstance, result));
    }


    this._logService.info('Generate dynamic service controls, is edit mode: ' + serviceInstance != null , result);
    return result;
  }

  getRollBackOnFailureOptions = () : Observable<SelectOption[]> =>{
    return of([
      new SelectOption({id: 'true', name: 'Rollback'}),
      new SelectOption({id: 'false', name: 'Don\'t Rollback'})
    ]);
  };

  getGlobalSubscriberControl = (serviceInstance : any, controls : FormControlModel[]) : DropdownFormControl  => {
    return new DropdownFormControl({
      type : FormControlType.DROPDOWN,
      controlName : FormControlNames.GLOBAL_SUBSCRIBER_ID,
      displayName : 'Subscriber name',
      dataTestId : 'subscriberName',
      placeHolder : 'Select Subscriber Name',
      isDisabled : false,
      name : "subscriber-name-select",
      value : serviceInstance ? serviceInstance.globalSubscriberId : null,
      validations : [new ValidatorModel(ValidatorOptions.required, 'is required')],
      onInit : this._basicControlGenerator.getSubscribeInitResult.bind(this._aaiService, this.aaiService.getSubscribers),
      onChange :  (param: string, form : FormGroup) => {
        form.controls[FormControlNames.SUBSCRIPTION_SERVICE_TYPE].reset();
        if(!_.isNil(param)){
          form.controls[FormControlNames.SUBSCRIPTION_SERVICE_TYPE].enable();
          this._basicControlGenerator.getSubscribeResult.bind(this, this._aaiService.getServiceTypes(param).subscribe(res =>{
            controls.find(item => item.controlName === FormControlNames.SUBSCRIPTION_SERVICE_TYPE)['options$'] = res;
          }));
        }
        else {
          form.controls[FormControlNames.SUBSCRIPTION_SERVICE_TYPE].disable();
        }
      }
    })
  };

  getServiceTypeControl = (serviceInstance : any, controls : FormControlModel[], isMacro?: boolean) : DropdownFormControl => {
    return new DropdownFormControl({
      type : FormControlType.DROPDOWN,
      controlName : FormControlNames.SUBSCRIPTION_SERVICE_TYPE,
      displayName : 'Service type',
      dataTestId : 'serviceType',
      placeHolder : 'Select Service Type',
      selectedField : 'name',
      name : "service-type",
      isDisabled : _.isNil(serviceInstance),
      value : serviceInstance ? serviceInstance.subscriptionServiceType : null,
      validations : [new ValidatorModel(ValidatorOptions.required, 'is required')],
      onInit : serviceInstance ? this._basicControlGenerator.getSubscribeInitResult.bind(
        this._aaiService,
        this.aaiService.getServiceTypes.bind(this, serviceInstance.globalSubscriberId)) : ()=>{},
      onChange :  (param: string, form : FormGroup) => {
        if(isMacro){
          form.controls[FormControlNames.LCPCLOUD_REGION_ID].reset();
          if(!_.isNil(param)) {
            form.controls[FormControlNames.LCPCLOUD_REGION_ID].enable();
            const globalCustomerId: string = form.controls[FormControlNames.GLOBAL_SUBSCRIBER_ID].value;
            if (!_.isNil(globalCustomerId)) {
              this._basicControlGenerator.getSubscribeResult.bind(this, this._aaiService.getLcpRegionsAndTenants(globalCustomerId, param).subscribe(res => {
                controls.find(item => item.controlName === FormControlNames.LCPCLOUD_REGION_ID)['options$'] = res.lcpRegionList;
              }));
            }
          }
          else {
            form.controls[FormControlNames.LCPCLOUD_REGION_ID].disable();
          }
        }

      }
    })
  };

  getOwningEntityControl = (serviceInstance : any, controls : FormControlModel[]) : DropdownFormControl => {
    return new DropdownFormControl({
      type : FormControlType.DROPDOWN,
      controlName : FormControlNames.OWNING_ENTITY_ID,
      displayName : 'Owning entity',
      dataTestId : 'owningEntity',
      placeHolder : 'Select Owning Entity',
      name :"owningEntity",
      isDisabled : false,
      validations : [new ValidatorModel(ValidatorOptions.required, 'is required'),],
      onInitSelectedField : ['owningEntityList'],
      value : serviceInstance ? serviceInstance.owningEntityId : null,
      onInit : this._basicControlGenerator.getSubscribeInitResult.bind(null, this._aaiService.getCategoryParameters)
    })
  };

  getProductFamilyControl = (serviceInstance : any, controls : FormControlModel[]) : DropdownFormControl => {
    return new DropdownFormControl({
      type : FormControlType.DROPDOWN,
      controlName : FormControlNames.PRODUCT_FAMILY_ID,
      displayName : 'Product family',
      dataTestId : 'productFamily',
      placeHolder : 'Select Product Family',
      isDisabled : false,
      name : "product-family-select",
      value : serviceInstance ? serviceInstance.productFamilyId : null,
      validations : [new ValidatorModel(ValidatorOptions.required, 'is required')],
      onInit : this._basicControlGenerator.getSubscribeResult.bind(this, this._aaiService.getProductFamilies),
    })
  };

  isRegionAndTenantOptional = (vidNotions?: VidNotions) : boolean => {
    return !_.isNil(vidNotions) && vidNotions.modelCategory === "Transport"
  };

  getLcpRegionControl = (serviceInstance: any, controls: FormControlModel[], vidNotions?: VidNotions) : DropdownFormControl => {
    return new DropdownFormControl({
      type : FormControlType.DROPDOWN,
      controlName : FormControlNames.LCPCLOUD_REGION_ID,
      displayName : 'LCP region',
      dataTestId : 'lcpRegion',
      placeHolder : 'Select LCP Region',
      name : "lcpRegion",
      isDisabled : _.isNil(serviceInstance),
      value : serviceInstance ? serviceInstance.lcpCloudRegionId : null,
      validations : this.isRegionAndTenantOptional(vidNotions) ? [] :
          [new ValidatorModel(ValidatorOptions.required, 'is required')],
      onInitSelectedField : ['lcpRegionList'],
      onInit : serviceInstance ? this._basicControlGenerator.getSubscribeInitResult.bind(
        this._aaiService,
        this.aaiService.getLcpRegionsAndTenants.bind(this, serviceInstance.globalSubscriberId, serviceInstance.subscriptionServiceType)) : ()=>{},
      onChange :  (param: string, form : FormGroup) => {
        form.controls[FormControlNames.TENANT_ID].reset();
        if(param) {
          form.controls[FormControlNames.TENANT_ID].enable();
        }
        else {
          form.controls[FormControlNames.TENANT_ID].disable();
        }
        const globalCustomerId : string = form.controls[FormControlNames.GLOBAL_SUBSCRIBER_ID].value;
        const serviceType : string = form.controls[FormControlNames.SUBSCRIPTION_SERVICE_TYPE].value;
        if(!_.isNil(globalCustomerId) && !_.isNil(serviceType)){
          this._basicControlGenerator.getSubscribeResult.bind(this, this._aaiService.getLcpRegionsAndTenants(globalCustomerId, serviceType).subscribe(res =>{
            controls.find(item => item.controlName === FormControlNames.TENANT_ID)['options$'] = res.lcpRegionsTenantsMap[param];
          }));
        }
      }
    })
  };

  getTenantControl = (serviceInstance: any, controls: FormControlModel[], vidNotions?: VidNotions) : DropdownFormControl => {
    return  new DropdownFormControl({
      type : FormControlType.DROPDOWN,
      controlName : FormControlNames.TENANT_ID,
      displayName : 'Tenant',
      dataTestId : 'tenant',
      placeHolder : 'Select Tenant',
      name : "tenant",
      isDisabled : _.isNil(serviceInstance),
      onInitSelectedField : serviceInstance ? ['lcpRegionsTenantsMap', serviceInstance.lcpCloudRegionId] : null,
      onInit : serviceInstance ? this._basicControlGenerator.getSubscribeInitResult.bind(
        this._aaiService,
        this.aaiService.getLcpRegionsAndTenants.bind(this, serviceInstance.globalSubscriberId, serviceInstance.subscriptionServiceType)) : ()=>{},
      value : serviceInstance ? serviceInstance.tenantId : null,
      validations :  this.isRegionAndTenantOptional(vidNotions) ? [] : [new ValidatorModel(ValidatorOptions.required, 'is required')],
    })
  };

  getAICZoneControl = (serviceInstance : any, controls : FormControlModel[]) : DropdownFormControl => {
    return new DropdownFormControl({
      type : FormControlType.DROPDOWN,
      controlName : FormControlNames.AICZONE_ID,
      displayName : 'AIC zone',
      dataTestId : 'aic_zone',
      placeHolder : 'Select AIC zone',
      name : "aicZone",
      value : serviceInstance ? serviceInstance.aicZoneId : null,
      isDisabled : false,
      validations : [],
      onInit : this._basicControlGenerator.getSubscribeInitResult.bind(null, this._aaiService.getAicZones)
    })
  };

  getPauseControl = (serviceInstance : any, controls : FormControlModel[]) :CheckboxFormControl => {
    return  new CheckboxFormControl({
      controlName : FormControlNames.PAUSE,
      displayName : 'Pause on pause points',
      dataTestId : 'Pause',
      isDisabled : false,
      value : serviceInstance ? serviceInstance.pause : null,
    })
  };

  getProjectControl = (serviceInstance : any, controls : FormControlModel[]) :DropdownFormControl =>{
    return new DropdownFormControl({
      type : FormControlType.DROPDOWN,
      controlName : FormControlNames.PROJECT_NAME,
      displayName : 'Project',
      dataTestId : 'project',
      placeHolder : 'Select Project',
      name : "project",
      isDisabled : false,
      validations : [],
      value : serviceInstance ? serviceInstance.projectName : null,
      onInitSelectedField : ['projectList'],
      onInit : this._basicControlGenerator.getSubscribeInitResult.bind(null, this._aaiService.getCategoryParameters)
    })
  };

  getRollbackOnFailureControl = (serviceInstance : any, controls : FormControlModel[]) : DropdownFormControl => {
    return new DropdownFormControl({
      type : FormControlType.DROPDOWN,
      controlName : FormControlNames.ROLLBACK_ON_FAILURE,
      displayName : 'Rollback on failure',
      dataTestId : 'rollback',
      isDisabled : false,
      validations : [new ValidatorModel(ValidatorOptions.required, 'is required')],
      value : serviceInstance ? serviceInstance.rollbackOnFailure : 'true',
      onInit : this._basicControlGenerator.getSubscribeInitResult.bind(null, this.getRollBackOnFailureOptions)
    })
  };
}


