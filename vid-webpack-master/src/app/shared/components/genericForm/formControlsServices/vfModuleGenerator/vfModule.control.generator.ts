import {Injectable} from "@angular/core";
import {GenericFormService} from "../../generic-form.service";
import {AaiService} from "../../../../services/aaiService/aai.service";
import {NgRedux} from "@angular-redux/store";
import {HttpClient} from "@angular/common/http";
import {BasicControlGenerator} from "../basic.control.generator";
import {
  CustomValidatorOptions,
  FormControlModel,
  ValidatorModel,
  ValidatorOptions
} from "../../../../models/formControlModels/formControl.model";
import {LogService} from "../../../../utils/log/log.service";
import {AppState} from "../../../../store/reducers";
import {FormGroup} from "@angular/forms";
import {InputFormControl} from "../../../../models/formControlModels/inputFormControl.model";
import {VfModuleInstance} from "../../../../models/vfModuleInstance";
import {VfModule} from "../../../../models/vfModule";
import {VNFModel} from "../../../../models/vnfModel";
import {VnfInstance} from "../../../../models/vnfInstance";
import * as _ from 'lodash';

export enum FormControlNames {
  INSTANCE_NAME = 'instanceName',
  VOLUME_GROUP_NAME = 'volumeGroupName',
  LCPCLOUD_REGION_ID = 'lcpCloudRegionId',
  LEGACY_REGION = 'legacyRegion',
  TENANT_ID = 'tenantId',
  ROLLBACK_ON_FAILURE = 'rollbackOnFailure',
}


@Injectable()
export class VfModuleControlGenerator {
  aaiService: AaiService;
  vfModuleModel: VfModule;
  isUpdateMode : boolean;

  constructor(private genericFormService: GenericFormService,
              private _basicControlGenerator: BasicControlGenerator,
              private store: NgRedux<AppState>,
              private http: HttpClient,
              private _aaiService: AaiService,
              private _logService: LogService) {
    this.aaiService = _aaiService;
  }

  getVfModuleInstance = (serviceId: string, vnfStoreKey: string, UUIDData: Object, isUpdateMode: boolean): VfModuleInstance => {
    let vfModuleInstance: VfModuleInstance = null;
    if (isUpdateMode && this.store.getState().service.serviceInstance[serviceId] &&
      _.has(this.store.getState().service.serviceInstance[serviceId].vnfs, vnfStoreKey) &&
      _.has(this.store.getState().service.serviceInstance[serviceId].vnfs[vnfStoreKey].vfModules, UUIDData['modelName'])) {
       vfModuleInstance = Object.assign({},this.store.getState().service.serviceInstance[serviceId].vnfs[vnfStoreKey].vfModules[UUIDData['modelName']][UUIDData['vFModuleStoreKey']]);
    }
    return vfModuleInstance;
  };

  extractVfAccordingToVfModuleUuid(serviceId: string, UUIDData: Object): VfModule {
    const vfModule = this.store.getState().service.serviceHierarchy[serviceId].vfModules[UUIDData['modelName']];
    this.vfModuleModel = vfModule;
    return vfModule;
  }

  getMacroFormControls(serviceId: string, vnfStoreKey: string, vfModuleStoreKey: string, uuidData : Object, isUpdateMode: boolean): FormControlModel[] {
    this.isUpdateMode = isUpdateMode;
    this.extractVfAccordingToVfModuleUuid(serviceId, uuidData);
    if (_.isNil(serviceId) || _.isNil(vnfStoreKey) || _.isNil(vfModuleStoreKey)) {
      if(isUpdateMode){
        this._logService.error('should provide serviceId, vfModuleStoreKey, vnfStoreKey', serviceId);
        return [];
      }
    }

    const vfModuleInstance = this._basicControlGenerator.retrieveInstanceIfUpdateMode(this.store, this.getVfModuleInstance(serviceId, vnfStoreKey, uuidData, isUpdateMode));
    const vfModuleModel = this.vfModuleModel;
    const vnf: VnfInstance = this.store.getState().service.serviceInstance[serviceId].vnfs[vnfStoreKey];
    const vnfModelName: string = vnf.originalName;
    const vnfModel = new VNFModel(this.store.getState().service.serviceHierarchy[serviceId].vnfs[vnfModelName]);

    let result: FormControlModel[] = [];

    if (!_.isNil(vfModuleModel)) {
      result = this.pushInstanceAndVGToForm(result, vfModuleInstance, serviceId, vnfModel, false);
    }
    if(this.store.getState().global.flags['FLAG_SUPPLEMENTARY_FILE']) {
      result = this._basicControlGenerator.concatSupplementaryFile(result, vfModuleInstance);
    }
    return result;
  }

  pushInstanceAndVGToForm(result: FormControlModel[], vfModuleElement: any, serviceId: string, vnfModel: any, isALaCarte: boolean) :FormControlModel[]{
    result.push(this.getInstanceName(vfModuleElement, serviceId, vnfModel.isEcompGeneratedNaming));
    if (this.vfModuleModel.volumeGroupAllowed) {
      result.push(this.getVolumeGroupData(vfModuleElement, serviceId, vnfModel.isEcompGeneratedNaming, isALaCarte));
    }
    return result;
  }

  getAlaCarteFormControls(serviceId: string, vnfStoreKey: string, vfModuleStoreKey: string, uuidData : Object, isUpdateMode: boolean): FormControlModel[] {
    this.isUpdateMode = isUpdateMode;
    this.extractVfAccordingToVfModuleUuid(serviceId, uuidData);
    if (_.isNil(serviceId) || _.isNil(vnfStoreKey) || _.isNil(vfModuleStoreKey)) {
      if(isUpdateMode){
        this._logService.error('should provide serviceId, vfModuleStoreKey, vnfStoreKey', serviceId);
        return [];
      }
    }
    const vnf: VnfInstance = this.store.getState().service.serviceInstance[serviceId].vnfs[vnfStoreKey] ;
    const vnfModelName: string = vnf.originalName;
    const vnfModel = new VNFModel(this.store.getState().service.serviceHierarchy[serviceId].vnfs[vnfModelName]);

    const vfModuleInstance = this._basicControlGenerator.retrieveInstanceIfUpdateMode(this.store, this.getVfModuleInstance(serviceId, vnfStoreKey, uuidData, isUpdateMode));
    let result: FormControlModel[] = [];
    this.pushInstanceAndVGToForm(result, vfModuleInstance, serviceId, vnfModel, true);
    result.push(this._basicControlGenerator.getLcpRegionControl(serviceId, vfModuleInstance, result));
    result.push(this._basicControlGenerator.getLegacyRegion(vfModuleInstance));
    result.push(this._basicControlGenerator.getTenantControl(serviceId, vfModuleInstance));
    result.push(this._basicControlGenerator.getRollbackOnFailureControl(vfModuleInstance));
    result.push(this._basicControlGenerator.getSDNCControl(vfModuleInstance));
    if(this.store.getState().global.flags['FLAG_SUPPLEMENTARY_FILE']) {
      result = this._basicControlGenerator.concatSupplementaryFile(result, vfModuleInstance);
    }
    return result;
  }

  getInstanceName(instance: any, serviceId: string, isEcompGeneratedNaming: boolean): FormControlModel {
    let formControlModel:FormControlModel = this._basicControlGenerator.getInstanceNameController(instance, serviceId, isEcompGeneratedNaming, this.vfModuleModel);
    formControlModel.onBlur = (event, form : FormGroup) => {
        if(!_.isNil(form.controls['volumeGroupName'])&& event.target.value.length > 0){
          form.controls['volumeGroupName'].setValue(event.target.value + "_vol");
        }
      };

    return formControlModel;
  }

  getDefaultVolumeGroupName(instance: any, isEcompGeneratedNaming: boolean): string {
    if ((!_.isNil(instance) && instance.volumeGroupName))  {
      return instance.volumeGroupName;
    }
    if (isEcompGeneratedNaming) {
      return null;
    }
    return this._basicControlGenerator.getDefaultInstanceName(instance, this.vfModuleModel) + "_vol";
  }

  getVolumeGroupData(instance: any, serviceId: string, isEcompGeneratedNaming: boolean, isALaCarte: boolean): FormControlModel {
    let validations: ValidatorModel[] = [
      new ValidatorModel(ValidatorOptions.pattern, 'Instance name may include only alphanumeric characters and underscore.', BasicControlGenerator.INSTANCE_NAME_REG_EX),
      new ValidatorModel(CustomValidatorOptions.uniqueInstanceNameValidator, 'Volume Group instance name is already in use, please pick another name', [this.store, serviceId, instance && instance.volumeGroupName])
    ];

    return new InputFormControl({
      controlName: 'volumeGroupName',
      displayName: 'Volume Group Name',
      dataTestId: 'volumeGroupName',
      validations: validations,
      tooltip : 'When filled, VID will create a Volume Group by this name and associate with this module.\n' +
                'When empty, the module is created without a Volume Group.',
      isVisible: this.shouldVGNameBeVisible(isEcompGeneratedNaming,isALaCarte),
      value: this.getDefaultVolumeGroupName(instance, isEcompGeneratedNaming),
      onKeypress: (event) => {
        const pattern:RegExp = BasicControlGenerator.INSTANCE_NAME_REG_EX;
        if (pattern) {
          if (!pattern.test(event['key'])) {
            event.preventDefault();
          }
        }
        return event;
      }
    });
  }

  private shouldVGNameBeVisible(isEcompGeneratedNaming: boolean, isALaCarte: boolean) {
    if((!isALaCarte && !isEcompGeneratedNaming) || isALaCarte){
      return true;
    }
    return false;

  }
}
