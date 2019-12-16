import {Injectable} from "@angular/core";
import {AaiService} from "../../../../services/aaiService/aai.service";
import {NgRedux} from "@angular-redux/store";
import {BasicControlGenerator} from "../basic.control.generator";
import {
  FormControlModel,
  ValidatorModel,
  ValidatorOptions
} from "../../../../models/formControlModels/formControl.model";
import {LogService} from "../../../../utils/log/log.service";
import {AppState} from "../../../../store/reducers";
import {DropdownFormControl} from "../../../../models/formControlModels/dropdownFormControl.model";
import {FormControlType} from "../../../../models/formControlModels/formControlTypes.enum";
import {SelectOption} from "../../../../models/selectOption";
import {VnfGroupModel} from "../../../../models/vnfGroupModel";
import * as _ from 'lodash';
import {Observable, of} from "rxjs";


export enum FormControlNames {
  INSTANCE_NAME = 'instanceName',
  ROLLBACK_ON_FAILURE = 'rollbackOnFailure',
}

enum InputType {
  ROLLBACK = "rollbackOnFailure"
}

@Injectable()
export class VnfGroupControlGenerator {
  aaiService: AaiService;
  constructor(private _basicControlGenerator: BasicControlGenerator,
              private store: NgRedux<AppState>,
              private _aaiService: AaiService,
              private _logService: LogService) {
    this.aaiService = _aaiService;
  }

  getVnfGroupInstance = (serviceId: string, vnfGroupStoreKey: string): any => {
    let vnfGroupInstance = null;
    if (this.store.getState().service.serviceInstance[serviceId] && _.has(this.store.getState().service.serviceInstance[serviceId].vnfGroups, vnfGroupStoreKey)) {
      vnfGroupInstance = Object.assign({}, this.store.getState().service.serviceInstance[serviceId].vnfGroups[vnfGroupStoreKey]);
    }
    return vnfGroupInstance;
  };

  getMacroFormControls(serviceId: string, vnfGroupStoreKey: string, vnfGroupName: string, dynamicInputs?: Array<any>): Array<FormControlModel> {
    vnfGroupStoreKey = _.isNil(vnfGroupStoreKey) ? vnfGroupName : vnfGroupStoreKey;

    if (_.isNil(serviceId) || _.isNil(vnfGroupStoreKey) || _.isNil(vnfGroupName)) {
      this._logService.error('should provide serviceId, vnfGroupName, vnfGroupStoreKey', serviceId);
      return [];
    }

    const vnfGroupInstance = this._basicControlGenerator.retrieveInstanceIfUpdateMode(this.store, this.getVnfGroupInstance(serviceId, vnfGroupStoreKey));
    const vnfGroupModel = new VnfGroupModel(this.store.getState().service.serviceHierarchy[serviceId].vnfGroups[vnfGroupName]);
    let result: FormControlModel[] = [];

    if (!_.isNil(vnfGroupModel)) {
      result.push(this.getInstanceName(vnfGroupInstance, serviceId, vnfGroupName, vnfGroupModel.isEcompGeneratedNaming));
    }
    return result;
  }

  getAlaCarteFormControls(serviceId: string, vnfGroupStoreKey: string, vnfGroupName: string, dynamicInputs?: any[]): FormControlModel[] {
    vnfGroupStoreKey = _.isNil(vnfGroupStoreKey) ? vnfGroupName : vnfGroupStoreKey;
    if (_.isNil(serviceId) || _.isNil(vnfGroupStoreKey) || _.isNil(vnfGroupName)) {
      this._logService.error('should provide serviceId, vnfGroupName, vnfGroupStoreKey', serviceId);
      return [];
    }

    let result: FormControlModel[] = [];
    const vnfGroupInstance = this._basicControlGenerator.retrieveInstanceIfUpdateMode(this.store, this.getVnfGroupInstance(serviceId, vnfGroupStoreKey));
    const vnfGroupModel = new VnfGroupModel(this.store.getState().service.serviceHierarchy[serviceId].vnfGroups[vnfGroupName]);

    if (!_.isNil(vnfGroupModel)) {
      result.push(this.getInstanceName(vnfGroupInstance, serviceId, vnfGroupName, vnfGroupModel.isEcompGeneratedNaming));
      result.push(this.getRollbackOnFailureControl(vnfGroupInstance, result));
    }
    return result;
  }

  isInputShouldBeShown = (inputType: any): boolean => {
    let vnfGroupInputs = [InputType.ROLLBACK];
    return vnfGroupInputs.indexOf(inputType) > -1;
  };

  getDefaultInstanceName(instance : any, serviceId : string, vnfGroupName : string) : string {
    const vnfGroupModel: VnfGroupModel = this.store.getState().service.serviceHierarchy[serviceId].vnfGroups[vnfGroupName];
    return this._basicControlGenerator.getDefaultInstanceName(instance, vnfGroupModel);
  }

  getInstanceName(instance : any, serviceId : string, vnfGroupName : string, isEcompGeneratedNaming: boolean): FormControlModel {
    const vnfGroupModel : VnfGroupModel = this.store.getState().service.serviceHierarchy[serviceId].vnfGroups[vnfGroupName];
    return this._basicControlGenerator.getInstanceNameController(instance, serviceId, isEcompGeneratedNaming, vnfGroupModel);
  }

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
