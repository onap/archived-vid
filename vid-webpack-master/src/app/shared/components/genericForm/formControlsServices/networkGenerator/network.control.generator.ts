import {Injectable} from "@angular/core";
import {GenericFormService} from "../../generic-form.service";
import {AaiService} from "../../../../services/aaiService/aai.service";
import {NgRedux} from "@angular-redux/store";
import {HttpClient} from "@angular/common/http";
import {ControlGeneratorUtil} from "../control.generator.util.service";
import * as _ from 'lodash';

import {FormControlModel,} from "../../../../models/formControlModels/formControl.model";
import {LogService} from "../../../../utils/log/log.service";
import {AppState} from "../../../../store/reducers";
import {NetworkInstance} from "../../../../models/networkInstance";
import {NetworkModel} from "../../../../models/networkModel";
import {SharedControllersService} from "../sharedControlles/shared.controllers.service";


export enum FormControlNames {
  INSTANCE_NAME = 'instanceName',
  PRODUCT_FAMILY_ID = 'productFamilyId',
  LCPCLOUD_REGION_ID = 'lcpCloudRegionId',
  ROLLBACK_ON_FAILURE = 'rollbackOnFailure'
}


@Injectable()
export class NetworkControlGenerator {
  aaiService: AaiService;

  constructor(private genericFormService: GenericFormService,
              private _basicControlGenerator: ControlGeneratorUtil,
              private _sharedControllersService: SharedControllersService,
              private store: NgRedux<AppState>,
              private http: HttpClient,
              private _aaiService: AaiService,
              private _logService: LogService) {
    this.aaiService = _aaiService;
  }

  getNetworkInstance = (serviceId: string, networkName: string, isUpdateMode: boolean): NetworkInstance => {
    let networkInstance: NetworkInstance = null;
    if (isUpdateMode && this.store.getState().service.serviceInstance[serviceId] && _.has(this.store.getState().service.serviceInstance[serviceId].networks, networkName)) {
      networkInstance = Object.assign({}, this.store.getState().service.serviceInstance[serviceId].networks[networkName]);
    }
    return networkInstance;
  };


  getMacroFormControls(serviceId: string, networkStoreKey: string, networkName: string, isUpdateMode: boolean): FormControlModel[] {
    networkStoreKey = _.isNil(networkStoreKey) ? networkName : networkStoreKey;

    if (_.isNil(serviceId) || _.isNil(networkStoreKey) || _.isNil(networkName)) {
      this._logService.error('should provide serviceId, networkName, networkStoreKey', serviceId);
      return [];
    }
    const networkInstance = this._basicControlGenerator.retrieveInstanceIfUpdateMode(this.store, this.getNetworkInstance(serviceId, networkStoreKey, isUpdateMode));
    const networkModel = new NetworkModel(this.store.getState().service.serviceHierarchy[serviceId].networks[networkName]);
    let result: FormControlModel[] = [];
    const flags = this.store.getState().global.flags;

    if (!_.isNil(networkModel)) {
      result.push(this.getInstanceName(networkInstance, serviceId, networkName, networkModel.isEcompGeneratedNaming));
      result.push(this._sharedControllersService.getProductFamilyControl(networkInstance, result, false));
      result.push(this._sharedControllersService.getLcpRegionControl(serviceId, networkInstance, result));
      result.push(this._sharedControllersService.getLegacyRegion(networkInstance));
      result.push(this._sharedControllersService.getTenantControl(serviceId, networkInstance));
      result.push(this._sharedControllersService.getPlatformMultiselectControl(networkInstance, result, flags['FLAG_2006_NETWORK_PLATFORM_MULTI_SELECT']));
      result.push(this._sharedControllersService.getLineOfBusinessControl(networkInstance));
    }
    return result;

  }

  getAlaCarteFormControls(serviceId: string, networkStoreKey: string, networkName: string, isUpdateMode: boolean): FormControlModel[] {
    networkStoreKey = _.isNil(networkStoreKey) ? networkName : networkStoreKey;
    if (_.isNil(serviceId) || _.isNil(networkStoreKey) || _.isNil(networkName)) {
      this._logService.error('should provide serviceId, networkName, networkStoreKey', serviceId);
      return [];
    }

    let result: FormControlModel[] = [];
    const networkInstance = this._basicControlGenerator.retrieveInstanceIfUpdateMode(this.store, this.getNetworkInstance(serviceId, networkStoreKey, isUpdateMode));
    const networkModel = new NetworkModel(this.store.getState().service.serviceHierarchy[serviceId].networks[networkName]);
    const flags = this.store.getState().global.flags;

    if (!_.isNil(networkModel)) {
      result.push(this.getInstanceName(networkInstance, serviceId, networkName, networkModel.isEcompGeneratedNaming));
      result.push(this._sharedControllersService.getProductFamilyControl(networkInstance, result, false));
      result.push(this._sharedControllersService.getLcpRegionControl(serviceId, networkInstance, result));
      result.push(this._sharedControllersService.getLegacyRegion(networkInstance));
      result.push(this._sharedControllersService.getTenantControl(serviceId, networkInstance));
      result.push(this._sharedControllersService.getPlatformMultiselectControl(networkInstance, result, flags['FLAG_2006_NETWORK_PLATFORM_MULTI_SELECT']));
      result.push(this._sharedControllersService.getLineOfBusinessControl(networkInstance));
      result.push(this._sharedControllersService.getRollbackOnFailureControl(networkInstance));
    }
    return result;
  }


  getInstanceName(instance: any, serviceId: string, networkName: string, isEcompGeneratedNaming: boolean): FormControlModel {
    const networkModel: NetworkModel = this.store.getState().service.serviceHierarchy[serviceId].networks[networkName];
    return this._sharedControllersService.getInstanceNameController(instance, serviceId, isEcompGeneratedNaming, networkModel);
  }
}
