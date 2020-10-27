import {Injectable} from "@angular/core";
import {GenericFormService} from "../../generic-form.service";
import {AaiService} from "../../../../services/aaiService/aai.service";
import {NgRedux} from "@angular-redux/store";
import {HttpClient} from "@angular/common/http";
import {ControlGeneratorUtil} from "../control.generator.util.service";
import {FormControlModel} from "../../../../models/formControlModels/formControl.model";
import {LogService} from "../../../../utils/log/log.service";
import {PNFModel} from "../../../../models/pnfModel";
import {AppState} from "../../../../store/reducers";
import * as _ from 'lodash';
import {SharedControllersService} from "../sharedControlles/shared.controllers.service";

@Injectable()
export class PnfControlGenerator {
  aaiService: AaiService;
  constructor(private genericFormService: GenericFormService,
              private _basicControlGenerator: ControlGeneratorUtil,
              private _sharedControllersService : SharedControllersService,
              private store: NgRedux<AppState>,
              private http: HttpClient,
              private _aaiService: AaiService,
              private _logService: LogService) {
    this.aaiService = _aaiService;
  }

  getPnfInstance = (serviceId: string, pnfStoreKey: string): any => {
    let pnfInstance = null;
    if (this.store.getState().service.serviceInstance[serviceId] && _.has(this.store.getState().service.serviceInstance[serviceId].pnfs, pnfStoreKey)) {
      pnfInstance = Object.assign({}, this.store.getState().service.serviceInstance[serviceId].pnfs[pnfStoreKey]);
    }
    return pnfInstance;
  };

  getMacroFormControls(serviceId: string, pnfStoreKey: string, pnfName: string, dynamicInputs?: any[]): FormControlModel[] {
    pnfStoreKey = _.isNil(pnfStoreKey) ? pnfName : pnfStoreKey;

    if (_.isNil(serviceId) || _.isNil(pnfStoreKey) || _.isNil(pnfName)) {
      this._logService.error('should provide serviceId, pnfName, pnfStoreKey', serviceId);
      return [];
    }
    const pnfInstance = this._basicControlGenerator.retrieveInstanceIfUpdateMode(this.store,this.getPnfInstance(serviceId, pnfStoreKey));
    const pnfModel = new PNFModel(this.store.getState().service.serviceHierarchy[serviceId].pnfs[pnfName]);
    let result: FormControlModel[] = [];
    // const flags = this.store.getState().global.flags;

    if (!_.isNil(pnfModel)) {
      // const isPlatformMultiSelected = flags['FLAG_2002_PNF_PLATFORM_MULTI_SELECT'];
      // const isLobMultiSelected = flags['FLAG_2006_PNF_LOB_MULTI_SELECT'];

      result.push(this.getInstanceName(pnfInstance, serviceId, pnfName, pnfModel.isEcompGeneratedNaming));
      result.push(this._sharedControllersService.getProductFamilyControl(pnfInstance, result, true));
      result.push(this._sharedControllersService.getLcpRegionControl(serviceId, pnfInstance, result));
      result.push(this._sharedControllersService.getLegacyRegion(pnfInstance));
      result.push(this._sharedControllersService.getTenantControl(serviceId, pnfInstance));
      // result.push(this._sharedControllersService.getPlatformMultiselectControl(pnfInstance, result, isPlatformMultiSelected));
      // result.push(this._sharedControllersService.getLobMultiselectControl(pnfInstance, isLobMultiSelected));
    }
    return result;
  }

  getAlaCarteFormControls(serviceId: string, pnfStoreKey: string, pnfName: string, dynamicInputs?: any[]): FormControlModel[] {
    pnfStoreKey = _.isNil(pnfStoreKey) ? pnfName : pnfStoreKey;
    if (_.isNil(serviceId) || _.isNil(pnfStoreKey) || _.isNil(pnfName)) {
      this._logService.error('should provide serviceId, pnfName, pnfStoreKey', serviceId);
      return [];
    }

    let result: FormControlModel[] = [];
    const pnfInstance = this._basicControlGenerator.retrieveInstanceIfUpdateMode(this.store,this.getPnfInstance(serviceId, pnfStoreKey));
    const pnfModel = new PNFModel(this.store.getState().service.serviceHierarchy[serviceId].pnfs[pnfName]);
    const flags = this.store.getState().global.flags;

    if (!_.isNil(pnfModel)) {
      const isPlatformMultiSelected = flags['FLAG_2002_VNF_PLATFORM_MULTI_SELECT'];
      const isLobMultiSelected = flags['FLAG_2006_VNF_LOB_MULTI_SELECT'];
      result.push(this.getInstanceName(pnfInstance, serviceId, pnfName, pnfModel.isEcompGeneratedNaming));
      result.push(this._sharedControllersService.getProductFamilyControl(pnfInstance, result, true));
      result.push(this._sharedControllersService.getLcpRegionControl(serviceId, pnfInstance, result));
      result.push(this._sharedControllersService.getLegacyRegion(pnfInstance));
      result.push(this._sharedControllersService.getTenantControl(serviceId, pnfInstance));
      result.push(this._sharedControllersService.getPlatformMultiselectControl(pnfInstance, result, isPlatformMultiSelected));
      result.push(this._sharedControllersService.getLobMultiselectControl(pnfInstance,isLobMultiSelected));
      result.push(this._sharedControllersService.getRollbackOnFailureControl(pnfInstance));
    }
    return result;
  }

  getInstanceName(instance : any, serviceId : string, pnfName : string, isEcompGeneratedNaming: boolean): FormControlModel {
    const pnfModel : PNFModel = this.store.getState().service.serviceHierarchy[serviceId].pnfs[pnfName];
    return this._sharedControllersService.getInstanceNameController(instance, serviceId, isEcompGeneratedNaming, pnfModel);
  }
}
