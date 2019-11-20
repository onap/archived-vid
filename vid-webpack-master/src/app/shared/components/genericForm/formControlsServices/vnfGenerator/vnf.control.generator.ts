import {Injectable} from "@angular/core";
import {GenericFormService} from "../../generic-form.service";
import {AaiService} from "../../../../services/aaiService/aai.service";
import {NgRedux} from "@angular-redux/store";
import {HttpClient} from "@angular/common/http";
import {BasicControlGenerator} from "../basic.control.generator";
import {
  FormControlModel,
  ValidatorModel,
  ValidatorOptions
} from "../../../../models/formControlModels/formControl.model";
import {LogService} from "../../../../utils/log/log.service";
import {VNFModel} from "../../../../models/vnfModel";
import {AppState} from "../../../../store/reducers";
import {FormGroup} from "@angular/forms";
import {DropdownFormControl} from "../../../../models/formControlModels/dropdownFormControl.model";
import {FormControlType} from "../../../../models/formControlModels/formControlTypes.enum";
import {InputFormControl} from "../../../../models/formControlModels/inputFormControl.model";
import {Observable, of} from "rxjs";
import {SelectOption} from "../../../../models/selectOption";
import * as _ from 'lodash';
import {Constants} from "../../../../utils/constants";
import {MultiselectFormControl} from "../../../../models/formControlModels/multiselectFormControl.model";
import {MultiSelectItem} from "../../../formControls/component/multiselect/multiselect.model";

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
export class VnfControlGenerator {
  aaiService: AaiService;
  constructor(private genericFormService: GenericFormService,
              private _basicControlGenerator: BasicControlGenerator,
              private store: NgRedux<AppState>,
              private http: HttpClient,
              private _aaiService: AaiService,
              private _logService: LogService) {
    this.aaiService = _aaiService;
  }

  getVnfInstance = (serviceId: string, vnfStoreKey: string): any => {
    let vnfInstance = null;
    if (this.store.getState().service.serviceInstance[serviceId] && _.has(this.store.getState().service.serviceInstance[serviceId].vnfs, vnfStoreKey)) {
      vnfInstance = Object.assign({}, this.store.getState().service.serviceInstance[serviceId].vnfs[vnfStoreKey]);
    }
    return vnfInstance;
  };

  getMacroFormControls(serviceId: string, vnfStoreKey: string, vnfName: string, dynamicInputs?: any[]): FormControlModel[] {
    vnfStoreKey = _.isNil(vnfStoreKey) ? vnfName : vnfStoreKey;

    if (_.isNil(serviceId) || _.isNil(vnfStoreKey) || _.isNil(vnfName)) {
      this._logService.error('should provide serviceId, vnfName, vnfStoreKey', serviceId);
      return [];
    }

    const vnfInstance = this.getVnfInstance(serviceId, vnfStoreKey);
    const vnfModel = new VNFModel(this.store.getState().service.serviceHierarchy[serviceId].vnfs[vnfName]);
    let result: FormControlModel[] = [];

    if (!_.isNil(vnfModel)) {
      result.push(this.getInstanceName(vnfInstance, serviceId, vnfName, vnfModel.isEcompGeneratedNaming));
      result.push(this._basicControlGenerator.getProductFamilyControl(vnfInstance, result, false));
      result.push(this.getLcpRegionControl(serviceId, vnfInstance, result));
      result.push(this._basicControlGenerator.getLegacyRegion(vnfInstance));
      result.push(this.getTenantControl(serviceId, vnfInstance, result));
      result.push(this.getPlatformControl(vnfInstance, result));
      result.push(this.getLineOfBusinessControl(vnfInstance, result));
    }
    return result;
  }

  getAlaCarteFormControls(serviceId: string, vnfStoreKey: string, vnfName: string, dynamicInputs?: any[]): FormControlModel[] {
    vnfStoreKey = _.isNil(vnfStoreKey) ? vnfName : vnfStoreKey;
    if (_.isNil(serviceId) || _.isNil(vnfStoreKey) || _.isNil(vnfName)) {
      this._logService.error('should provide serviceId, vnfName, vnfStoreKey', serviceId);
      return [];
    }

    let result: FormControlModel[] = [];
    const vnfInstance = this.getVnfInstance(serviceId, vnfStoreKey);
    const vnfModel = new VNFModel(this.store.getState().service.serviceHierarchy[serviceId].vnfs[vnfName]);

    if (!_.isNil(vnfModel)) {
      const flags = this.store.getState().global.flags;
      result.push(this.getInstanceName(vnfInstance, serviceId, vnfName, vnfModel.isEcompGeneratedNaming));
      result.push(this._basicControlGenerator.getProductFamilyControl(vnfInstance, result, false));
      result.push(this.getLcpRegionControl(serviceId, vnfInstance, result));
      result.push(this._basicControlGenerator.getLegacyRegion(vnfInstance));
      result.push(this.getTenantControl(serviceId, vnfInstance, result));
      result.push(this.getPlatformControl(vnfInstance, result, flags['FLAG_A_LA_CARTE_PLATFORM_MULTI_SELECT']));
      result.push(this.getLineOfBusinessControl(vnfInstance, result));
      result.push(this.getRollbackOnFailureControl(vnfInstance, result));
    }
    return result;
  }

  isInputShouldBeShown = (inputType: any): boolean => {
    let vnfInputs = [InputType.LCP_REGION, InputType.LOB, InputType.TENANT, InputType.PRODUCT_FAMILY, InputType.PLATFORM, InputType.ROLLBACK];
    return vnfInputs.indexOf(inputType) > -1;
  };

  getInstanceName(instance : any, serviceId : string, vnfName : string, isEcompGeneratedNaming: boolean): FormControlModel {
    const vnfModel : VNFModel = this.store.getState().service.serviceHierarchy[serviceId].vnfs[vnfName];
    return this._basicControlGenerator.getInstanceNameController(instance, serviceId, isEcompGeneratedNaming, vnfModel);
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



  getPlatformDropdownControl = (instance: any, controls: FormControlModel[]) : DropdownFormControl => {
    return new DropdownFormControl({
      type: FormControlType.DROPDOWN ,
      controlName: 'platformName',
      displayName: 'Platform',
      dataTestId: 'platform',
      selectedFieldName :  null ,
      ngValue :  null,
      placeHolder: 'Select Platform',
      isDisabled: false,
      name: "platform",
      value: instance ? instance.platformName : null,
      validations: [new ValidatorModel(ValidatorOptions.required, 'is required')],
      onInitSelectedField: ['platformList'],
      onInit: this._basicControlGenerator.getSubscribeInitResult.bind(null, this._aaiService.getCategoryParameters),
    });
  };

  getPlatformMultiselectControl = (instance: any, controls: FormControlModel[]) : MultiselectFormControl => {
    return new MultiselectFormControl({
      type: FormControlType.MULTI_SELECT ,
      controlName: 'platformName',
      displayName: 'Platform',
      dataTestId: 'multi-selectPlatform',
      selectedFieldName :  'name' ,
      ngValue :  'name',
      placeHolder: 'Select Platform',
      isDisabled: false,
      name: "platform",
      value: instance ? instance.platformName : '',
      validations: [new ValidatorModel(ValidatorOptions.required, 'is required')],
      onInitSelectedField: ['platformList'],
      onInit: this._basicControlGenerator.getSubscribeInitResult.bind(null, this._aaiService.getCategoryParameters),
      onChange : (param: MultiSelectItem[], form: FormGroup) => {
        form.controls['platformName'].setValue(param.map((multiSelectItem: MultiSelectItem)=>{
          return multiSelectItem.itemName
        }).join(','));
      },
      convertOriginalDataToArray : (value?: string) => {
        if(_.isNil(value)) return [];
        return value.split(',');
      }
    });
  };

  getPlatformControl = (instance: any, controls: FormControlModel[], isMultiSelect?: boolean): MultiselectFormControl | DropdownFormControl => {
    const shouldGenerateDropdown =  isMultiSelect === undefined || isMultiSelect === false;
    if(shouldGenerateDropdown){
      return this.getPlatformDropdownControl(instance, controls);
    }
    return this.getPlatformMultiselectControl(instance, controls);
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
