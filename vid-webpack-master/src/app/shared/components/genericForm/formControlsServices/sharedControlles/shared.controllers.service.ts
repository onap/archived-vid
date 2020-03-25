import {Injectable} from "@angular/core";
import {DropdownFormControl} from "../../../../models/formControlModels/dropdownFormControl.model";
import {FormControlType} from "../../../../models/formControlModels/formControlTypes.enum";
import {
  FormControlModel,
  ValidatorModel,
  ValidatorOptions
} from "../../../../models/formControlModels/formControl.model";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../store/reducers";
import {AaiService} from "../../../../services/aaiService/aai.service";
import {ControlGeneratorUtil, SDN_C_PRE_LOAD} from "../control.generator.util.service";
import * as _ from "lodash";
import {FormGroup} from "@angular/forms";
import {Constants} from "../../../../utils/constants";
import {CheckboxFormControl} from "../../../../models/formControlModels/checkboxFormControl.model";
import {InputFormControl} from "../../../../models/formControlModels/inputFormControl.model";
import {NodeModel} from "../../../../models/nodeModel";
import {Tenant} from "../../../../models/tenant";
import {MultiselectFormControl} from "../../../../models/formControlModels/multiselectFormControl.model";
import {MultiSelectItem} from "../../../formControls/component/multiselect/multiselect.model";

@Injectable()
export class SharedControllersService {
  constructor(private _store : NgRedux<AppState>,
              private _aaiService : AaiService,
              private _basicControlGenerator : ControlGeneratorUtil){}


  getLineOfBusinessControl = (instance?: any): DropdownFormControl => {
    return this.getLineOfBusinessControlInternal(undefined, instance);
  };

  getMultiSelectLineOfBusinessControl = (instance: any, isMultiSelected: boolean): MultiselectFormControl => {
    return this.getLobMultiselectControl(instance, isMultiSelected);
  };

  private getLineOfBusinessControlInternal = (onChange: Function, instance?: any): DropdownFormControl  => {
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

  getTenantControl = (serviceId: string, instance?: any): DropdownFormControl => {
    const service = this._store.getState().service.serviceInstance[serviceId];
    const globalCustomerId: string = service.globalSubscriberId;
    const serviceType: string = service.subscriptionServiceType;
    return new DropdownFormControl({
      type: FormControlType.DROPDOWN,
      controlName: 'tenantId',
      displayName: 'Tenant',
      dataTestId: 'tenant',
      placeHolder: 'Select Tenant',
      name: 'tenant',
      isDisabled: _.isNil(instance) || _.isNil(instance.lcpCloudRegionId),
      onInitSelectedField: instance ? ['lcpRegionsTenantsMap', instance.lcpCloudRegionId] : null,
      value: instance ? instance.tenantId : null,
      validations: [new ValidatorModel(ValidatorOptions.required, 'is required')],
      onInit: instance ? this._basicControlGenerator.getSubscribeInitResult.bind(
        this._aaiService,
        this._aaiService.getLcpRegionsAndTenants.bind(this, globalCustomerId, serviceType)) : () => {
      }
    })
  };

  getRollbackOnFailureControl = (instance?: any): DropdownFormControl => {
    return new DropdownFormControl({
      type: FormControlType.DROPDOWN,
      controlName: 'rollbackOnFailure',
      displayName: 'Rollback on failure',
      dataTestId: 'rollback',
      placeHolder: 'Rollback on failure',
      isDisabled: false,
      validations: [new ValidatorModel(ValidatorOptions.required, 'is required')],
      value: instance ? instance.rollbackOnFailure : 'true',
      onInit: this._basicControlGenerator.getSubscribeInitResult.bind(null, this._basicControlGenerator.getRollBackOnFailureOptions)
    })
  };

  getLegacyRegion(instance: any): FormControlModel {
    return new InputFormControl({
      controlName: 'legacyRegion',
      displayName: 'Legacy Region',
      dataTestId: 'lcpRegionText',
      placeHolder: 'Type Legacy Region',
      validations: [],
      isVisible: this._basicControlGenerator.isLegacyRegionShouldBeVisible(instance),
      isDisabled : _.isNil(instance) ? true : Constants.LegacyRegion.MEGA_REGION.indexOf(instance.lcpCloudRegionId),
      value: instance ? instance.legacyRegion : null
    });
  }

  getLcpRegionControl = (serviceId: string, instance: any, controls: FormControlModel[]): DropdownFormControl => {
    const service = this._store.getState().service.serviceInstance[serviceId];
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
        form.controls['tenantId'].enable();
        form.controls['tenantId'].reset();
        if (!_.isNil(globalCustomerId) && !_.isNil(serviceType)) {
          this._basicControlGenerator.getSubscribeResult.bind(this, this._aaiService.getLcpRegionsAndTenants(globalCustomerId, serviceType).subscribe(res => {
            controls.find(item => item.controlName === 'tenantId')['options$'] = res.lcpRegionsTenantsMap[param];
            if (res.lcpRegionsTenantsMap[param]) {
              controls.find(item => item.controlName === 'tenantId')['hasEmptyOptions'] = res.lcpRegionsTenantsMap[param].length === 0;
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




  getSDNCControl = (instance: any, checkedByDefault: boolean, extraContents? : object[]): FormControlModel => {
    return new CheckboxFormControl({
      controlName: SDN_C_PRE_LOAD,
      displayName: 'SDN-C pre-load',
      dataTestId: 'sdncPreLoad',
      value: instance ? instance.sdncPreLoad : checkedByDefault,
      validations: [new ValidatorModel(ValidatorOptions.required, 'is required')],
      extraContents
    })
  };

  getProductFamilyControl = (instance : any, controls : FormControlModel[], isMandatory?: boolean) : DropdownFormControl => {
    return new DropdownFormControl({
      type : FormControlType.DROPDOWN,
      controlName : 'productFamilyId',
      displayName : 'Product family',
      dataTestId : 'productFamily',
      placeHolder : 'Select Product Family',
      isDisabled : false,
      name : "product-family-select",
      value : instance ? instance.productFamilyId : null,
      validations : _.isNil(isMandatory) || isMandatory === true ? [new ValidatorModel(ValidatorOptions.required, 'is required')]: [],
      onInit : this._basicControlGenerator.getSubscribeResult.bind(this, this._aaiService.getProductFamilies),
    })
  };

  getInstanceNameController(instance: any, serviceId: string, isEcompGeneratedNaming: boolean, model: NodeModel): FormControlModel {
    let validations: ValidatorModel[] = this._basicControlGenerator.createValidationsForInstanceName(instance, serviceId, isEcompGeneratedNaming);
    return new InputFormControl({
      controlName: 'instanceName',
      displayName: 'Instance name',
      dataTestId: 'instanceName',
      placeHolder: (!isEcompGeneratedNaming) ? 'Instance name' : 'Automatically generated when not provided',
      validations: validations,
      isVisible : true,
      value : (!isEcompGeneratedNaming || (!_.isNil(instance) && !_.isNil(instance.instanceName) && instance.instanceName !== ""))
        ? this._basicControlGenerator.getDefaultInstanceName(instance, model) : null,
      onKeypress : (event) => {
        const pattern:RegExp = ControlGeneratorUtil.INSTANCE_NAME_REG_EX;
        if(pattern){
          if(!pattern.test(event['key'])){
            event.preventDefault();
          }
        }
        return event;
      }
    });
  }

  getInstanceName(instance : any, serviceId : string, isEcompGeneratedNaming: boolean): FormControlModel {
    let formControlModel:FormControlModel = this.getInstanceNameController(instance, serviceId, isEcompGeneratedNaming, new NodeModel());
    formControlModel.value = instance ? instance.instanceName : null;
    return formControlModel;
  }

  getPlatformMultiselectControl = (instance: any, controls: FormControlModel[], isMultiSelected: boolean) : MultiselectFormControl => {
    return this.getMultiSelectFormControl(
      'platformName',
      'Platform',
      'multi-selectPlatform',
      'Select Platform',
      "platform",
      instance,
      instance ? instance.platformName : null,
      isMultiSelected,
      'platformList'
    );
  };

  getLobMultiselectControl = (instance: any, isMultiSelected: boolean) : MultiselectFormControl => {
    return this.getMultiSelectFormControl(
      'lineOfBusiness',
      'Line of business',
      'multi-lineOfBusiness',
      'Select Line Of Business',
      "lineOfBusiness",
      instance,
      instance ? instance.lineOfBusiness : null,
      isMultiSelected,
      'lineOfBusinessList');
  };

  private getMultiSelectFormControl(controlName: string, displayName: string, dataTestId: string, placeholder: string,
                                    name: string, instance: any, defaultValue, isMultiSelected: boolean, catagoryParamResponseFieldName: string) {
    return new MultiselectFormControl({
      type: FormControlType.MULTI_SELECT,
      controlName,
      displayName,
      dataTestId,
      selectedFieldName: 'name',
      ngValue: 'name',
      placeHolder: placeholder,
      isDisabled: false,
      name: name,
      value: instance ? defaultValue : '',
      limitSelection: isMultiSelected ? 1000 : 1,
      validations: [new ValidatorModel(ValidatorOptions.required, 'is required')],
      onInitSelectedField: [catagoryParamResponseFieldName],
      onInit: this._basicControlGenerator.getSubscribeInitResult.bind(null, this._aaiService.getCategoryParameters),
      onChange: (param: MultiSelectItem[], form: FormGroup) => {
        form.controls[controlName].setValue(param.map((multiSelectItem: MultiSelectItem) => {
          return multiSelectItem.itemName
        }).join(','));
      },
      convertOriginalDataToArray: (value?: string) => {
        if (_.isNil(value)) return [];
        return value.split(',');
      }
    });
  }
}
