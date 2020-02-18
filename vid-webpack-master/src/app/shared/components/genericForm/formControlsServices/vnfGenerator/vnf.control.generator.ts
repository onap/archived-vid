import {Injectable} from "@angular/core";
import {GenericFormService} from "../../generic-form.service";
import {AaiService} from "../../../../services/aaiService/aai.service";
import {NgRedux} from "@angular-redux/store";
import {HttpClient} from "@angular/common/http";
import {ControlGeneratorUtil} from "../control.generator.util.service";
import {
  FormControlModel,
  ValidatorModel,
  ValidatorOptions
} from "../../../../models/formControlModels/formControl.model";
import {LogService} from "../../../../utils/log/log.service";
import {VNFModel} from "../../../../models/vnfModel";
import {AppState} from "../../../../store/reducers";
import {FormGroup} from "@angular/forms";
import {FormControlType} from "../../../../models/formControlModels/formControlTypes.enum";
import * as _ from 'lodash';
import {MultiselectFormControl} from "../../../../models/formControlModels/multiselectFormControl.model";
import {MultiSelectItem} from "../../../formControls/component/multiselect/multiselect.model";
import {SharedControllersService} from "../sharedControlles/shared.controllers.service";
import {FeatureFlagsService, Features} from "../../../../services/featureFlag/feature-flags.service";

@Injectable()
export class VnfControlGenerator {
  aaiService: AaiService;
  constructor(private genericFormService: GenericFormService,
              private _basicControlGenerator: ControlGeneratorUtil,
              private _sharedControllersService : SharedControllersService,
              private _featureFlagsService: FeatureFlagsService,
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
    const vnfInstance = this._basicControlGenerator.retrieveInstanceIfUpdateMode(this.store,this.getVnfInstance(serviceId, vnfStoreKey));
    const vnfModel = new VNFModel(this.store.getState().service.serviceHierarchy[serviceId].vnfs[vnfName]);
    let result: FormControlModel[] = [];
    const flags = this.store.getState().global.flags;

    if (!_.isNil(vnfModel)) {
      result.push(this.getInstanceName(vnfInstance, serviceId, vnfName, vnfModel.isEcompGeneratedNaming));
      result.push(this._sharedControllersService.getProductFamilyControl(vnfInstance, result, true));
      result.push(this._sharedControllersService.getLcpRegionControl(serviceId, vnfInstance, result));
      result.push(this._sharedControllersService.getLegacyRegion(vnfInstance));
      result.push(this._sharedControllersService.getTenantControl(serviceId, vnfInstance));
      result.push(this.getPlatformMultiselectControl(vnfInstance, result, flags['FLAG_2002_VNF_PLATFORM_MULTI_SELECT']));
      result.push(this._sharedControllersService.getLineOfBusinessControl(vnfInstance));
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
    const vnfInstance = this._basicControlGenerator.retrieveInstanceIfUpdateMode(this.store,this.getVnfInstance(serviceId, vnfStoreKey));
    const vnfModel = new VNFModel(this.store.getState().service.serviceHierarchy[serviceId].vnfs[vnfName]);

    if (!_.isNil(vnfModel)) {
      const isMultiSelected =
        this._featureFlagsService.getFlagState(Features.FLAG_2002_VNF_PLATFORM_MULTI_SELECT);
      const lcpRegionsByLineOFBusiness =
        this._featureFlagsService.getFlagState(Features.FLAG_2006_LCP_REGIONS_BY_LINE_OF_BUSINESS);

      result.push(this.getInstanceName(vnfInstance, serviceId, vnfName, vnfModel.isEcompGeneratedNaming));
      result.push(this._sharedControllersService.getProductFamilyControl(vnfInstance, result, true));

      if (lcpRegionsByLineOFBusiness) {
        result.push(this._sharedControllersService.getLineOfBusinessByOwningEntityControl(vnfInstance, serviceId, result));
        result.push(this._sharedControllersService.getLcpRegionByLineOfBusinessControl(serviceId, vnfInstance, result));
        result.push(this._sharedControllersService.getLegacyRegion(vnfInstance));
        result.push(this._sharedControllersService.getTenantByLcpRegionControl(serviceId, vnfInstance));
        result.push(this.getPlatformMultiselectControl(vnfInstance, result, isMultiSelected));
      } else {
        result.push(this._sharedControllersService.getLcpRegionControl(serviceId, vnfInstance, result));
        result.push(this._sharedControllersService.getLegacyRegion(vnfInstance));
        result.push(this._sharedControllersService.getTenantControl(serviceId, vnfInstance));
        result.push(this.getPlatformMultiselectControl(vnfInstance, result, isMultiSelected));
        result.push(this._sharedControllersService.getLineOfBusinessControl(vnfInstance));
      }

      result.push(this._sharedControllersService.getRollbackOnFailureControl(vnfInstance));
    }
    return result;
  }

  getInstanceName(instance : any, serviceId : string, vnfName : string, isEcompGeneratedNaming: boolean): FormControlModel {
    const vnfModel : VNFModel = this.store.getState().service.serviceHierarchy[serviceId].vnfs[vnfName];
    return this._sharedControllersService.getInstanceNameController(instance, serviceId, isEcompGeneratedNaming, vnfModel);
  }

  getPlatformMultiselectControl = (instance: any, controls: FormControlModel[], isMultiSelected: boolean) : MultiselectFormControl => {
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
      limitSelection : isMultiSelected ? 1000 : 1,
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
}
