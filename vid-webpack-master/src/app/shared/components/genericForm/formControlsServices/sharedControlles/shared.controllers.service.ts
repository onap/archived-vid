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
import {LcpRegion} from "../../../../models/lcpRegion";
import {Tenant} from "../../../../models/tenant";

@Injectable()
export class SharedControllersService {
  constructor(private _store : NgRedux<AppState>,
              private _aaiService : AaiService,
              private _basicControlGenerator : ControlGeneratorUtil){}


  getLineOfBusinessControl = (instance?: any): DropdownFormControl => {
    return this.getLineOfBusinessControlInternal(undefined, instance);
  };

  getLineOfBusinessControlByOwningEntity = (instance?: any, serviceId?: string,  controls?: FormControlModel[]): DropdownFormControl => {
    const service = this._store.getState().service.serviceInstance[serviceId];
    const owningEntityName: string = service.owningEntityName;

    const changeLcpRegionOptions = (lineOfBusinessNameParam: string, form: FormGroup) => {
      form.controls['lcpCloudRegionId'].enable();
      form.controls['lcpCloudRegionId'].reset();
      this._aaiService.getLcpRegionsByOwningEntityAndLineOfBusiness(owningEntityName, lineOfBusinessNameParam).subscribe(res => {
        controls.find(item => item.controlName === 'lcpCloudRegionId')['options$'] = res;
      });
    };

    return this.getLineOfBusinessControlInternal(changeLcpRegionOptions, instance);
  };

  private getLineOfBusinessControlInternal = (onChange: Function, instance?: any): DropdownFormControl => {
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
      onChange,
      onInit: this._basicControlGenerator.getSubscribeInitResult.bind(null, this._aaiService.getCategoryParameters)
    })
  };

  getTenantControl = (serviceId: string, instance?: any): DropdownFormControl => {
    const service = this._store.getState().service.serviceInstance[serviceId];
    const globalCustomerId: string = service.globalSubscriberId;
    const serviceType: string = service.subscriptionServiceType;

    const onInit = instance
      ? this._basicControlGenerator.getSubscribeInitResult.bind(
          this._aaiService,
          this._aaiService.getLcpRegionsAndTenants.bind(this, globalCustomerId, serviceType))
      : () => {};
    return this.getTenantControlInternal(onInit, instance);
  };

  getTenantControlByLcpRegion = (serviceId: string, instance?: any): DropdownFormControl => {
    return this.getTenantControlInternal(() => {}, instance);
  };

  private getTenantControlInternal = (onInit: Function, instance?: any): DropdownFormControl => {
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
      onInit,
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

    const onInit = this._basicControlGenerator.getSubscribeInitResult.bind(
      this._aaiService,
      this._aaiService.getLcpRegionsAndTenants.bind(this, globalCustomerId, serviceType)
    );

    const changeTenantsOptionsByServiceTypeOnChange = (
      (globalCustomerId, serviceType, lcpCloudRegionIdParam) => {
        if (!_.isNil(globalCustomerId) && !_.isNil(serviceType)) {
          this._basicControlGenerator.getSubscribeResult.bind(this,
            this._aaiService.getLcpRegionsAndTenants(globalCustomerId, serviceType)
            .subscribe(res => this.setTenantsOptions(controls, res.lcpRegionsTenantsMap[lcpCloudRegionIdParam])));
        }

      }
    ).bind(this, globalCustomerId, serviceType);

    return this.getLcpRegionControlInternal(instance, controls,
      false, onInit, ['lcpRegionList'], changeTenantsOptionsByServiceTypeOnChange)
  };

  getLcpRegionControlByLineOfBusiness = (serviceId: string, instance: any, controls: FormControlModel[]): DropdownFormControl => {
    const service = this._store.getState().service.serviceInstance[serviceId];
    const owningEntityName: string = service.owningEntityName;

    const loadLcpRegionOptionsOnInit = (_.isNil(instance) || _.isNil(instance.lineOfBusiness))
      ? () => {}
      : this._basicControlGenerator.getSubscribeInitResult.bind(
        this,
        () => {
          return this._aaiService.getLcpRegionsByOwningEntityAndLineOfBusiness(owningEntityName, instance.lineOfBusiness)
          .do(res => this.changeTenantsOptionsByCloudRegionId2(controls, instance.lcpCloudRegionId, res));
        }
      );

    const changeTenantsOptionsByCloudRegionIdOnChange = (
      (controls, lcpCloudRegionIdParam) => this.changeTenantsOptionsByCloudRegionId(controls, lcpCloudRegionIdParam)
    ).bind(this, controls);

    return this.getLcpRegionControlInternal(instance, controls,
      _.isNil(instance) || _.isNil(instance.lineOfBusiness),
      loadLcpRegionOptionsOnInit, null, changeTenantsOptionsByCloudRegionIdOnChange
      )
  };

  private getLcpRegionControlInternal = (instance: any, controls: FormControlModel[], isDisabled: boolean,
                                         onInit: Function, onInitSelectedField: string[], changeTenantsOptionsOnChange: Function
  ): DropdownFormControl => {
    return new DropdownFormControl({
      type: FormControlType.DROPDOWN,
      controlName: 'lcpCloudRegionId',
      displayName: 'LCP region',
      dataTestId: 'lcpRegion',
      placeHolder: 'Select LCP Region',
      name: "lcpRegion",
      isDisabled,
      value: instance ? instance.lcpCloudRegionId : null,
      validations: [new ValidatorModel(ValidatorOptions.required, 'is required')],
      onInitSelectedField,
      onInit,

      onChange: (lcpCloudRegionIdParam: string, form: FormGroup) => {
        form.controls['tenantId'].enable();
        form.controls['tenantId'].reset();

        changeTenantsOptionsOnChange(lcpCloudRegionIdParam);

        if (Constants.LegacyRegion.MEGA_REGION.indexOf(lcpCloudRegionIdParam) !== -1) {
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

  private setTenantsOptions = (controls: FormControlModel[], tenants: Tenant[]) => {
    const tenantsControl = controls.find(item => item.controlName === 'tenantId');
    tenantsControl['options$'] = tenants;
    tenantsControl['hasEmptyOptions'] = tenants && tenants.length === 0;
  };

  private changeTenantsOptionsByCloudRegionId(controls: FormControlModel[], cloudRegionId) {
    this.changeTenantsOptionsByCloudRegionId2(controls, cloudRegionId, this.lcpRegionOptionsListList(controls));
  }

  private changeTenantsOptionsByCloudRegionId2(controls: FormControlModel[], cloudRegionId, lcpRegionList: LcpRegion[]) {
    const lcpRegionOption = (_.isNil(lcpRegionList) || _.isNil(cloudRegionId))
      ? null
      : lcpRegionList.find(({id}) => id === cloudRegionId);

    const cloudOwner = _.isNil(lcpRegionOption) ? null : lcpRegionOption.cloudOwner;

    if (_.isNil(cloudOwner)) {
      return;
    } else {
      this._aaiService.getTenantsByCloudOwnerAndCloudRegionId(cloudOwner, cloudRegionId)
        .subscribe(res => this.setTenantsOptions(controls, res));
    }
  }

  private lcpRegionOptionsListList(controls: FormControlModel[]): LcpRegion[] {
    if (_.isNil(controls)) {
      return null;
    }

    const lcpCloudRegionIdControl =
      controls.find(({controlName}) => controlName === 'lcpCloudRegionId');

    return _.isNil(lcpCloudRegionIdControl) ? null : lcpCloudRegionIdControl['options$'];
  }

  getSDNCControl = (instance: any, extraContents? : object[]): FormControlModel => {
    return new CheckboxFormControl({
      controlName: SDN_C_PRE_LOAD,
      displayName: 'SDN-C pre-load',
      dataTestId: 'sdncPreLoad',
      value: instance ? instance.sdncPreLoad : false,
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
}
