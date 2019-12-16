import {Injectable} from "@angular/core";
import {GenericFormService} from "../../generic-form.service";
import {AaiService} from "../../../../services/aaiService/aai.service";
import {NgRedux} from "@angular-redux/store";
import {HttpClient} from "@angular/common/http";
import {BasicControlGenerator} from "../basic.control.generator";
import * as _ from 'lodash';
import {Observable, of} from "rxjs";

import {
  FormControlModel,
  ValidatorModel,
  ValidatorOptions
} from "../../../../models/formControlModels/formControl.model";
import {LogService} from "../../../../utils/log/log.service";
import {AppState} from "../../../../store/reducers";
import {FormGroup} from "@angular/forms";
import {DropdownFormControl} from "../../../../models/formControlModels/dropdownFormControl.model";
import {FormControlType} from "../../../../models/formControlModels/formControlTypes.enum";
import {SelectOption} from "../../../../models/selectOption";
import {NetworkInstance} from "../../../../models/networkInstance";
import {NetworkModel} from "../../../../models/networkModel";
import {Constants} from "../../../../utils/constants";

export enum FormControlNames {
  INSTANCE_NAME = 'instanceName',
  GLOBAL_SUBSCRIBER_ID = 'globalSubscriberId',
  PRODUCT_FAMILY_ID = 'productFamilyId',
  LCPCLOUD_REGION_ID = 'lcpCloudRegionId',
  TENANT_ID = 'tenantId',
  AICZONE_ID = 'aicZoneId',
  ROLLBACK_ON_FAILURE = 'rollbackOnFailure',
  LEGACY_REGION = 'legacyRegion'
}


enum InputType {
  LCP_REGION = "lcpCloudRegionId",
  TENANT = "tenantId",
  LOB = "lineOfBusiness",
  PLATFORM = "platformName",
  ROLLBACK = "rollbackOnFailure",
  PRODUCT_FAMILY = "productFamilyId",
  VG = "volumeGroupName"
}

@Injectable()
export class NetworkControlGenerator {
  aaiService: AaiService;

  constructor(private genericFormService: GenericFormService,
              private _basicControlGenerator: BasicControlGenerator,
              private store: NgRedux<AppState>,
              private http: HttpClient,
              private _aaiService: AaiService,
              private _logService: LogService) {
    this.aaiService = _aaiService;
  }

  getNetworkInstance = (serviceId: string, networkName: string, isUpdateMode : boolean): NetworkInstance => {
    let networkInstance : NetworkInstance = null;
    if (isUpdateMode && this.store.getState().service.serviceInstance[serviceId] && _.has(this.store.getState().service.serviceInstance[serviceId].networks, networkName)) {
      networkInstance = Object.assign({}, this.store.getState().service.serviceInstance[serviceId].networks[networkName]);
    }
    return networkInstance;
  };


  getMacroFormControls(serviceId: string, networkStoreKey: string, networkName: string, isUpdateMode : boolean): FormControlModel[] {
    networkStoreKey = _.isNil(networkStoreKey) ? networkName : networkStoreKey;

    if (_.isNil(serviceId) || _.isNil(networkStoreKey) || _.isNil(networkName)) {
      this._logService.error('should provide serviceId, networkName, networkStoreKey', serviceId);
      return [];
    }
    const networkInstance = this._basicControlGenerator.retrieveInstanceIfUpdateMode(this.store, this.getNetworkInstance(serviceId, networkStoreKey, isUpdateMode));
    const networkModel = new NetworkModel(this.store.getState().service.serviceHierarchy[serviceId].networks[networkName]);
    let result: FormControlModel[] = [];

    if (!_.isNil(networkModel)) {
      result.push(this.getInstanceName(networkInstance, serviceId, networkName, networkModel.isEcompGeneratedNaming));
      result.push(this._basicControlGenerator.getProductFamilyControl(networkInstance, result, false));
      result.push(this.getLcpRegionControl(serviceId, networkInstance, result));
      result.push(this._basicControlGenerator.getLegacyRegion(networkInstance));
      result.push(this.getTenantControl(serviceId, networkInstance, result));
      result.push(this.getPlatformControl(networkInstance, result));
      result.push(this.getLineOfBusinessControl(networkInstance, result));
    }
    return result;

  }

  getAlaCarteFormControls(serviceId: string, networkStoreKey: string, networkName: string, isUpdateMode : boolean): FormControlModel[] {
    networkStoreKey = _.isNil(networkStoreKey) ? networkName : networkStoreKey;
    if (_.isNil(serviceId) || _.isNil(networkStoreKey) || _.isNil(networkName)) {
      this._logService.error('should provide serviceId, networkName, networkStoreKey', serviceId);
      return [];
    }

    let result: FormControlModel[] = [];
    const networkInstance = this._basicControlGenerator.retrieveInstanceIfUpdateMode(this.store, this.getNetworkInstance(serviceId, networkStoreKey, isUpdateMode));
    const networkModel = new NetworkModel(this.store.getState().service.serviceHierarchy[serviceId].networks[networkName]);

    if (!_.isNil(networkModel)) {
      result.push(this.getInstanceName(networkInstance, serviceId, networkName, networkModel.isEcompGeneratedNaming));
      result.push(this._basicControlGenerator.getProductFamilyControl(networkInstance, result, false));
      result.push(this.getLcpRegionControl(serviceId, networkInstance, result));
      result.push(this._basicControlGenerator.getLegacyRegion(networkInstance));
      result.push(this.getTenantControl(serviceId, networkInstance, result));
      result.push(this.getPlatformControl(networkInstance, result));
      result.push(this.getLineOfBusinessControl(networkInstance, result));
      result.push(this.getRollbackOnFailureControl(networkInstance, result));
    }
    return result;

  }

  isInputShouldBeShown = (inputType: any): boolean => {
    let networkInputs = [InputType.LCP_REGION, InputType.LOB, InputType.TENANT, InputType.PRODUCT_FAMILY, InputType.PLATFORM, InputType.ROLLBACK];
    return networkInputs.indexOf(inputType) > -1;
  };

  getInstanceName(instance : any, serviceId : string, networkName : string, isEcompGeneratedNaming: boolean): FormControlModel {
    const networkModel : NetworkModel = this.store.getState().service.serviceHierarchy[serviceId].networks[networkName];
    return this._basicControlGenerator.getInstanceNameController(instance, serviceId, isEcompGeneratedNaming, networkModel);
  }

  getLineOfBusinessControl = (instance: any, controls: FormControlModel[]): DropdownFormControl => {
    return new DropdownFormControl({
      type: FormControlType.DROPDOWN,
      controlName: 'lineOfBusiness',
      displayName: 'Line of business',
      dataTestId: 'lineOfBusiness',
      placeHolder: 'Select Line Of Business',
      isDisabled: false,
      name: "lineOfBusiness",
      value: instance ? instance.lineOfBusiness : null,
      validations: [new ValidatorModel(ValidatorOptions.required, 'is required')],
      onInitSelectedField: ['lineOfBusinessList'],
      onInit: this._basicControlGenerator.getSubscribeInitResult.bind(null, this._aaiService.getCategoryParameters)
    })
  };

  getPlatformControl = (instance: any, controls: FormControlModel[]): DropdownFormControl => {
    return new DropdownFormControl({
      type: FormControlType.DROPDOWN,
      controlName: 'platformName',
      displayName: 'Platform',
      dataTestId: 'platform',
      placeHolder: 'Select Platform',
      isDisabled: false,
      name: "platform",
      value: instance ? instance.platformName : null,
      validations: [],
      onInitSelectedField: ['platformList'],
      onInit: this._basicControlGenerator.getSubscribeInitResult.bind(null, this._aaiService.getCategoryParameters)
    })
  };

  getTenantControl = (serviceId: string, instance: any, controls: FormControlModel[]): DropdownFormControl => {
    const service = this.store.getState().service.serviceInstance[serviceId];
    const globalCustomerId: string = service.globalSubscriberId;
    const serviceType: string = service.subscriptionServiceType;
    return new DropdownFormControl({
      type: FormControlType.DROPDOWN,
      controlName: FormControlNames.TENANT_ID,
      displayName: 'Tenant',
      dataTestId: 'tenant',
      placeHolder: 'Select Tenant',
      name: "tenant",
      isDisabled: _.isNil(instance) || _.isNil(instance.lcpCloudRegionId),
      onInitSelectedField: instance ? ['lcpRegionsTenantsMap', instance.lcpCloudRegionId] : null,
      value: instance ? instance.tenantId : null,
      validations: [new ValidatorModel(ValidatorOptions.required, 'is required')],
      onInit : instance ? this._basicControlGenerator.getSubscribeInitResult.bind(
        this._aaiService,
        this.aaiService.getLcpRegionsAndTenants.bind(this, globalCustomerId, serviceType)) : ()=>{},
    })
  };

  getLcpRegionControl = (serviceId: string, instance: any, controls: FormControlModel[]): DropdownFormControl => {
    const service = this.store.getState().service.serviceInstance[serviceId];
    const globalCustomerId: string = service.globalSubscriberId;
    const serviceType: string = service.subscriptionServiceType;
    return new DropdownFormControl({
      type: FormControlType.DROPDOWN,
      controlName: 'lcpCloudRegionId',
      displayName: 'LCP region',
      dataTestId: 'lcpRegion',
      placeHolder: 'Select LCP Region',
      name: "lcpRegion",
      isDisabled: false,
      value: instance ? instance.lcpCloudRegionId : null,
      validations: [new ValidatorModel(ValidatorOptions.required, 'is required')],
      onInitSelectedField: ['lcpRegionList'],
      onInit: this._basicControlGenerator.getSubscribeInitResult.bind(
        this._aaiService,
        this._aaiService.getLcpRegionsAndTenants.bind(this, globalCustomerId, serviceType)),
      onChange: (param: string, form: FormGroup) => {
        form.controls[FormControlNames.TENANT_ID].enable();
        form.controls[FormControlNames.TENANT_ID].reset();
        if (!_.isNil(globalCustomerId) && !_.isNil(serviceType)) {
          this._basicControlGenerator.getSubscribeResult.bind(this, this._aaiService.getLcpRegionsAndTenants(globalCustomerId, serviceType).subscribe(res => {
            controls.find(item => item.controlName === FormControlNames.TENANT_ID)['options$'] = res.lcpRegionsTenantsMap[param];
            if(res.lcpRegionsTenantsMap[param]){
              controls.find(item => item.controlName === FormControlNames.TENANT_ID)['hasEmptyOptions'] = res.lcpRegionsTenantsMap[param].length === 0;
            }
          }));
        }

        if (Constants.LegacyRegion.MEGA_REGION.indexOf(param) !== -1) {
          form.controls['legacyRegion'].enable();
          controls.find(item => item.controlName === 'legacyRegion').isVisible = true;

        } else {
          controls.find(item => item.controlName === 'legacyRegion').isVisible = false;
          form.controls['legacyRegion'].setValue(null);
          form.controls['legacyRegion'].reset();
          form.controls['legacyRegion'].disable();
        }
      }
    })
  };

  getRollbackOnFailureControl = (instance: any, controls: FormControlModel[]): DropdownFormControl => {
    return new DropdownFormControl({
      type: FormControlType.DROPDOWN,
      controlName: FormControlNames.ROLLBACK_ON_FAILURE,
      displayName: 'Rollback on failure',
      dataTestId: 'rollback',
      placeHolder: 'Rollback on failure',
      isDisabled: false,
      validations: [new ValidatorModel(ValidatorOptions.required, 'is required')],
      value: instance ? instance.rollbackOnFailure : 'true',
      onInit: this._basicControlGenerator.getSubscribeInitResult.bind(null, this.getRollBackOnFailureOptions)
    })
  };

  getRollBackOnFailureOptions = (): Observable<SelectOption[]> => {
    return of([
      new SelectOption({id: 'true', name: 'Rollback'}),
      new SelectOption({id: 'false', name: 'Don\'t Rollback'})
    ]);
  };
}
