import {Injectable} from "@angular/core";
import {DropdownFormControl} from "../../../models/formControlModels/dropdownFormControl.model";
import {FormGroup} from "@angular/forms";
import {
  CustomValidatorOptions,
  FormControlModel,
  ValidatorModel,
  ValidatorOptions
} from "../../../models/formControlModels/formControl.model";
import {InputFormControl} from "../../../models/formControlModels/inputFormControl.model";
import {AppState} from "../../../store/reducers";
import {NgRedux} from "@angular-redux/store";
import {NumberFormControl} from "../../../models/formControlModels/numberFormControl.model";
import {FormControlType} from "../../../models/formControlModels/formControlTypes.enum";
import {FileFormControl} from "../../../models/formControlModels/fileFormControl.model";
import {SelectOption} from "../../../models/selectOption";
import * as _ from 'lodash';
import {DynamicInputLabelPipe} from "../../../pipes/dynamicInputLabel/dynamic-input-label.pipe";
import {AaiService} from "../../../services/aaiService/aai.service";
import {FormGeneralErrorsService} from "../../formGeneralErrors/formGeneralErrors.service";
import {Observable, of} from "rxjs";
import {NodeModel} from "../../../models/nodeModel";
import {Constants} from "../../../utils/constants";
import {FileUnit} from "../../formControls/component/file/fileUnit.enum";

const SUPPLEMENTARY_FILE = 'supplementaryFile';

@Injectable()
export class BasicControlGenerator {

  public static readonly INSTANCE_NAME_REG_EX:RegExp = /^[a-zA-Z0-9._-]*$/;
  public static readonly GENERATED_NAME_REG_EX:RegExp = /[^a-zA-Z0-9._-]/g;

  constructor(private _store : NgRedux<AppState>,
              private _aaiService : AaiService){}
  getSubscribeResult(subscribeFunction : Function, control : DropdownFormControl) : Observable<any>{
    return subscribeFunction(this).subscribe((res) => {
      control.options$ = res;
      control.hasEmptyOptions = res.length === 0;
      FormGeneralErrorsService.checkForErrorTrigger.next();
      return of(res);
    });
  }

  getSubscribeInitResult(subscribeFunction : Function, control : DropdownFormControl, form : FormGroup) : Observable<any>{
    return subscribeFunction(this).subscribe((res) => {
      if(!_.isNil(control['onInitSelectedField'])){
        let result = res;
        for(let key of control['onInitSelectedField']){
          result = !_.isNil(result[key]) ? result[key] : [];
        }
        control.options$ = result;
        control.hasEmptyOptions = _.isNil(result) || result.length === 0;
      } else{
        control.options$ = !_.isNil(res) ? res : [];
        control.hasEmptyOptions = _.isNil(res) || res.length === 0;
      }

      FormGeneralErrorsService.checkForErrorTrigger.next();
      return of(res);
    });
  }

  getInstanceNameController(instance: any, serviceId: string, isEcompGeneratedNaming: boolean, model: NodeModel): FormControlModel {
    let validations: ValidatorModel[] = this.createValidationsForInstanceName(instance, serviceId, isEcompGeneratedNaming);
    return new InputFormControl({
      controlName: 'instanceName',
      displayName: 'Instance name',
      dataTestId: 'instanceName',
      placeHolder: (!isEcompGeneratedNaming) ? 'Instance name' : 'Automatically generated when not provided',
      validations: validations,
      isVisible : true,
      value : (!isEcompGeneratedNaming || (!_.isNil(instance) && !_.isNil(instance.instanceName)))
        ? this.getDefaultInstanceName(instance, model) : null,
      onKeypress : (event) => {
        const pattern:RegExp = BasicControlGenerator.INSTANCE_NAME_REG_EX;
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

  isLegacyRegionShouldBeVisible(instance : any) : boolean {
    if(!_.isNil(instance) && !_.isNil(instance.lcpCloudRegionId))  {
      return Constants.LegacyRegion.MEGA_REGION.indexOf(instance.lcpCloudRegionId) !== -1;
    }
    return false;
  }

  getLegacyRegion(instance: any): FormControlModel {
    return new InputFormControl({
      controlName: 'legacyRegion',
      displayName: 'Legacy Region',
      dataTestId: 'lcpRegionText',
      placeHolder: 'Type Legacy Region',
      validations: [],
      isVisible: this.isLegacyRegionShouldBeVisible(instance),
      isDisabled : _.isNil(instance) ? true : Constants.LegacyRegion.MEGA_REGION.indexOf(instance.lcpCloudRegionId),
      value: instance ? instance.legacyRegion : null
    });
  }

  private createValidationsForInstanceName(instance: any, serviceId: string, isEcompGeneratedNaming: boolean): ValidatorModel[] {
    let validations: ValidatorModel[] = [
      new ValidatorModel(ValidatorOptions.pattern, 'Instance name may include only alphanumeric characters and underscore.', BasicControlGenerator.INSTANCE_NAME_REG_EX),
      new ValidatorModel(CustomValidatorOptions.uniqueInstanceNameValidator, 'some error', [this._store, serviceId, instance && instance.instanceName])
    ];
    if (!isEcompGeneratedNaming) {
      validations.push(new ValidatorModel(ValidatorOptions.required, 'is required'));
    }
    return validations;
  }

  getInputsOptions = (options: any[]) : Observable<SelectOption[]> =>{
    let optionList: SelectOption[] = [];
    options.forEach((option) => {
      optionList.push(new SelectOption({
        id: option.id || option.name,
        name: option.name
      }));
    });
    return of(optionList);
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
      onInit : this.getSubscribeResult.bind(this, this._aaiService.getProductFamilies),
    })
  };



  getDynamicInputsByType(dynamicInputs : any, serviceModelId : string, storeKey : string, type: string ) : FormControlModel[] {
    let result : FormControlModel[] = [];
    if(dynamicInputs) {
      let nodeInstance = null;
      if (_.has(this._store.getState().service.serviceInstance[serviceModelId][type], storeKey)) {
        nodeInstance = Object.assign({}, this._store.getState().service.serviceInstance[serviceModelId][type][storeKey]);
      }
      result = this.getDynamicInputs(dynamicInputs, nodeInstance);
    }
    return result;
  }


  getServiceDynamicInputs(dynamicInputs : any, serviceModelId : string) : FormControlModel[] {
    let result: FormControlModel[] = [];
    if (dynamicInputs) {
      let serviceInstance = null;
      if (_.has(this._store.getState().service.serviceInstance, serviceModelId)) {
        serviceInstance = Object.assign({}, this._store.getState().service.serviceInstance[serviceModelId]);
      }
      result = this.getDynamicInputs(dynamicInputs, serviceInstance);
    }
    return result;
  }

  getDynamicInputs(dynamicInputs : any, instance :any)  : FormControlModel[]{
    let result : FormControlModel[] = [];
    if(dynamicInputs) {
      dynamicInputs.forEach((input)=> {
        let validations: ValidatorModel[] = [];
        if(input.isRequired) {
          validations.push(new ValidatorModel(ValidatorOptions.required, 'is required'))
        }
        if(input.minLength) {
          validations.push(new ValidatorModel(ValidatorOptions.minLength, '', input.minLength))
        }
        if(input.maxLength) {
          validations.push(new ValidatorModel(ValidatorOptions.maxLength, '', input.maxLength))
        }

        let dynamicInputLabelPipe: DynamicInputLabelPipe = new DynamicInputLabelPipe();
        let data:any = {
          controlName: input.name,
          displayName: dynamicInputLabelPipe.transform(input.name).slice(0, -1),
          dataTestId: input.id,
          placeHolder: input.prompt,
          tooltip: input.description,
          validations: validations,
          isVisible: input.isVisible,
          value: !_.isNil(instance) && !_.isNil(instance.instanceParams) && instance.instanceParams.length > 0 ? instance.instanceParams[0][input.name] : input.value
        };

        switch (input.type) {
          case 'select' :
          case 'boolean' :{
            data.value = data.value || input.optionList.filter((option) => option.isDefault ? option.id || option.name: null);
            data.onInit  = this.getSubscribeInitResult.bind(null, this.getInputsOptions.bind(this, input.optionList));
            result.push(new DropdownFormControl(data));
            break;
          }
          case 'checkbox': {
            data.type = FormControlType.CHECKBOX;
            result.push(new FormControlModel(data));
            break;
          }
          case 'number': {
            data.min = input.min;
            data.max = input.max;
            result.push(new NumberFormControl(data));
            break;
          }
          case 'file': {
            result.push(new FileFormControl(data));
            break;
          }
          default: {
            result.push(new InputFormControl(data));
          }
        }
      })
    }

    return result;
  }

  getDefaultInstanceName(instance: any, model: NodeModel) : string {
    const initialInstanceName = (!_.isNil(instance) && instance.instanceName) || (!_.isNil(model.name) ? model.name.replace(BasicControlGenerator.GENERATED_NAME_REG_EX, "") : model.name);
    return initialInstanceName;
  }

  getSupplementaryFile(instance: any): FileFormControl {
    return new FileFormControl({
      controlName: SUPPLEMENTARY_FILE,
      displayName: 'Supplementary Data File (JSON format)',
      dataTestId: 'SupplementaryFile',
      placeHolder: 'Choose file',
      selectedFile:  !_.isNil(instance) ? instance.supplementaryFileName: null,
      isVisible: true,
      acceptedExtentions: "application/json",
      hiddenFile : [new InputFormControl({
        controlName: SUPPLEMENTARY_FILE + "_hidden",
        isVisible: false,
        validations: [new ValidatorModel(CustomValidatorOptions.isFileTooBig, "File size exceeds 5MB.", [FileUnit.MB, 5])]
      }),
        new InputFormControl({
          controlName: SUPPLEMENTARY_FILE + "_hidden_content",
          isVisible: false,
          validations: [new ValidatorModel(CustomValidatorOptions.isValidJson,
            "File is invalid, please make sure a legal JSON file is uploaded using name:value pairs.",[]),
            new ValidatorModel(CustomValidatorOptions.isStringContainTags,
              "File is invalid, please remove tags <>.",[])],
          value: !_.isNil(instance) ? (instance.supplementaryFile_hidden_content): null,
        })
      ],
      onDelete : this.getOnDeleteForSupplementaryFile(),
      onChange : this.getOnChangeForSupplementaryFile()
    })
  };

  private getOnDeleteForSupplementaryFile() {
    return (form: FormGroup) => {
      form.controls[SUPPLEMENTARY_FILE + "_hidden"].setValue(null);
      form.controls[SUPPLEMENTARY_FILE + "_hidden_content"].setValue(null);
    };
  }

  private getOnChangeForSupplementaryFile() {
    return (files: FileList, form: FormGroup) => {
      if (files.length > 0) {
        const file = files.item(0);
        let reader = new FileReader();
        reader.onload = function (event) {
          form.controls[SUPPLEMENTARY_FILE + "_hidden_content"].setValue(reader.result);
          form.controls[SUPPLEMENTARY_FILE + "_hidden"].setValue(file);
        };
        reader.readAsText(file);
      } else {
        form.controls[SUPPLEMENTARY_FILE + "_hidden"].setValue(null);
        form.controls[SUPPLEMENTARY_FILE + "_hidden_content"].setValue(null);
      }
    };
  }
}
