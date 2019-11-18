import {Injectable} from "@angular/core";
import {GenericFormService} from "../../generic-form.service";
import {AaiService} from "../../../../services/aaiService/aai.service";
import {NgRedux} from "@angular-redux/store";
import {HttpClient} from "@angular/common/http";
import {BasicControlGenerator} from "../basic.control.generator";
import * as _ from 'lodash';
import {Observable, of} from "rxjs";

import {
  CustomValidatorOptions,
  FormControlModel,
  ValidatorModel,
  ValidatorOptions
} from "../../../../models/formControlModels/formControl.model";
import {LogService} from "../../../../utils/log/log.service";
import {AppState} from "../../../../store/reducers";
import {FormGroup} from "@angular/forms";
import {DropdownFormControl} from "../../../../models/formControlModels/dropdownFormControl.model";
import {FormControlType} from "../../../../models/formControlModels/formControlTypes.enum";
import {InputFormControl} from "../../../../models/formControlModels/inputFormControl.model";
import {SelectOption} from "../../../../models/selectOption";
import {VfModuleInstance} from "../../../../models/vfModuleInstance";
import {VfModule} from "../../../../models/vfModule";
import {VNFModel} from "../../../../models/vnfModel";
import {VnfInstance} from "../../../../models/vnfInstance";
import {FileFormControl} from "../../../../models/formControlModels/fileFormControl.model";
import {CheckboxFormControl} from "../../../../models/formControlModels/checkboxFormControl.model";
import {FileUnit} from "../../../formControls/component/file/fileUnit.enum";
import {Constants} from "../../../../utils/constants";


export enum FormControlNames {
  INSTANCE_NAME = 'instanceName',
  VOLUME_GROUP_NAME = 'volumeGroupName',
  LCPCLOUD_REGION_ID = 'lcpCloudRegionId',
  LEGACY_REGION = 'legacyRegion',
  TENANT_ID = 'tenantId',
  ROLLBACK_ON_FAILURE = 'rollbackOnFailure',
  SDN_C_PRE_LOAD = 'sdncPreLoad',
  SUPPLEMENTARY_FILE = 'supplementaryFile'
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

  setVFModuleStoreKey = (serviceId: string, vfModuleUuid: string) => {
      const vfModules = this.store.getState().service.serviceHierarchy[serviceId].vfModules;
      const vfModulesKeys = Object.keys(vfModules);
      for(let key of  vfModulesKeys){
        if(vfModules[key].uuid === vfModuleUuid){
          return;
        }
      }
  };


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

    const vfModuleInstance = this.getVfModuleInstance(serviceId, vnfStoreKey, uuidData, isUpdateMode);
    const vfModuleModel = this.vfModuleModel;
    const vnf: VnfInstance = this.store.getState().service.serviceInstance[serviceId].vnfs[vnfStoreKey];
    const vnfModelName: string = vnf.originalName;
    const vnfModel = new VNFModel(this.store.getState().service.serviceHierarchy[serviceId].vnfs[vnfModelName]);

    let result: FormControlModel[] = [];

    if (!_.isNil(vfModuleModel)) {
      result = this.pushInstanceAndVGToForm(result, vfModuleInstance, serviceId, vnfModel, false);
    }
    if(this.store.getState().global.flags['FLAG_SUPPLEMENTARY_FILE']) {
      let suppFileInput:FileFormControl = <FileFormControl>(this.getSupplementaryFile(vfModuleInstance));
      result.push(suppFileInput);
      result = result.concat(suppFileInput.hiddenFile);
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
    const vnf: VnfInstance = this.store.getState().service.serviceInstance[serviceId].vnfs[vnfStoreKey];
    const vnfModelName: string = vnf.originalName;
    const vnfModel = new VNFModel(this.store.getState().service.serviceHierarchy[serviceId].vnfs[vnfModelName]);

    const vfModuleInstance = this.getVfModuleInstance(serviceId, vnfStoreKey, uuidData, isUpdateMode);
    let result: FormControlModel[] = [];
    this.pushInstanceAndVGToForm(result, vfModuleInstance, serviceId, vnfModel, true);
    result.push(this.getLcpRegionControl(serviceId, vfModuleInstance, result));
    result.push(this._basicControlGenerator.getLegacyRegion(vfModuleInstance));
    result.push(this.getTenantControl(serviceId, vfModuleInstance, result));
    result.push(this.getRollbackOnFailureControl(vfModuleInstance, result));
    result.push(this.getSDNCControl(vfModuleInstance, result));
    if(this.store.getState().global.flags['FLAG_SUPPLEMENTARY_FILE']) {
      let suppFileInput:FileFormControl = <FileFormControl>(this.getSupplementaryFile(vfModuleInstance));
      result.push(suppFileInput);
      result = result.concat(suppFileInput.hiddenFile);
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
    // comment out because if not provided vid won't create VG
    // if (!isEcompGeneratedNaming)  {
    //   validations.push(new ValidatorModel(ValidatorOptions.required, 'is required'));
    // }
    return new InputFormControl({
      controlName: 'volumeGroupName',
      displayName: 'Volume Group Name',
      dataTestId: 'volumeGroupName',
      // placeHolder: (!isEcompGeneratedNaming) ? 'Volume Group Name' : 'Automatically generated when not provided',
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

  getSupplementaryFile(instance: any): FormControlModel {
    return new FileFormControl({
      controlName: FormControlNames.SUPPLEMENTARY_FILE,
      displayName: 'Supplementary Data File (JSON format)',
      dataTestId: 'SupplementaryFile',
      placeHolder: 'Choose file',
      selectedFile:  !_.isNil(instance) ? instance.supplementaryFileName: null,
      isVisible: true,
      acceptedExtentions: "application/json",
      hiddenFile : [new InputFormControl({
        controlName: FormControlNames.SUPPLEMENTARY_FILE + "_hidden",
        isVisible: false,
        validations: [new ValidatorModel(CustomValidatorOptions.isFileTooBig, "File size exceeds 5MB.", [FileUnit.MB, 5])]
      }),
        new InputFormControl({
          controlName: FormControlNames.SUPPLEMENTARY_FILE + "_hidden_content",
          isVisible: false,
          validations: [new ValidatorModel(CustomValidatorOptions.isValidJson,
            "File is invalid, please make sure a legal JSON file is uploaded using name:value pairs.",[]),
            new ValidatorModel(CustomValidatorOptions.isStringContainTags,
              "File is invalid, please remove tags <>.",[])],
          value: !_.isNil(instance) ? (instance.supplementaryFile_hidden_content): null,
        })
      ],
      onDelete : (form : FormGroup) => {
        form.controls[FormControlNames.SUPPLEMENTARY_FILE + "_hidden"].setValue(null);
        form.controls[FormControlNames.SUPPLEMENTARY_FILE + "_hidden_content"].setValue(null);
      },
      onChange : (files: FileList, form : FormGroup)  => {
        if (files.length > 0) {
          const file = files.item(0);
          let reader = new FileReader();
          reader.onload = function(event) {
            form.controls[FormControlNames.SUPPLEMENTARY_FILE + "_hidden_content"].setValue(reader.result);
            form.controls[FormControlNames.SUPPLEMENTARY_FILE + "_hidden"].setValue(file);
          };
          reader.readAsText(file);
        }
        else {
          form.controls[FormControlNames.SUPPLEMENTARY_FILE + "_hidden"].setValue(null);
          form.controls[FormControlNames.SUPPLEMENTARY_FILE + "_hidden_content"].setValue(null);
        }
      }
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
      onInit: instance ? this._basicControlGenerator.getSubscribeInitResult.bind(
        this._aaiService,
        this.aaiService.getLcpRegionsAndTenants.bind(this, globalCustomerId, serviceType)) : () => {
      },
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
            if (res.lcpRegionsTenantsMap[param]) {
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

  getSDNCControl = (instance: any, controls: FormControlModel[]): CheckboxFormControl => {
    return new CheckboxFormControl({
      type: FormControlType.CHECKBOX,
      controlName: 'sdncPreLoad',
      displayName: 'SDN-C pre-load',
      dataTestId: 'sdncPreLoad',
      value: instance ? instance.sdncPreLoad : false,
      validations: [new ValidatorModel(ValidatorOptions.required, 'is required')]
    })
  };

  getRollbackOnFailureControl = (instance: any, controls: FormControlModel[]): DropdownFormControl => {
    return new DropdownFormControl({
      type: FormControlType.DROPDOWN,
      controlName: FormControlNames.ROLLBACK_ON_FAILURE,
      displayName: 'Rollback on failure',
      dataTestId: 'rollback',
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
