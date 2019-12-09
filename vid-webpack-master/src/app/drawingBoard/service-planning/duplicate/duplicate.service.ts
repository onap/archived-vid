import {Injectable} from '@angular/core';
import {ITreeNode} from 'angular-tree-component/dist/defs/api';
import {AppState} from '../../../shared/store/reducers';
import {LogService} from '../../../shared/utils/log/log.service';
import {NgRedux} from '@angular-redux/store';
import {VnfInstance} from "../../../shared/models/vnfInstance";
import {VfModuleMap} from "../../../shared/models/vfModulesMap";
import * as _ from "lodash";
import {DefaultDataGeneratorService} from "../../../shared/services/defaultDataServiceGenerator/default.data.generator.service";
import {TypeNodeInformation} from "../typeNodeInformation.model";
import {SdcUiCommon, SdcUiServices} from "onap-ui-angular";
import {changeInstanceCounter, duplicateBulkInstances} from "../../../shared/storeUtil/utils/general/general.actions";
import {IModalConfig} from "onap-ui-angular/dist/modals/models/modal-config";

@Injectable()
export class DuplicateService {

  constructor(private _logService : LogService,  private _store: NgRedux<AppState>, modalService: SdcUiServices.ModalService) {
    this.modalService = modalService;
  }

  numberOfDuplicates:number;
  setNumberOfDuplicates(numberOfDuplicates: number) {
    this.numberOfDuplicates = numberOfDuplicates;
  }

  currentInstanceId: string = null;
  currentServiceId: string = null;
  maxNumberOfDuplicate: number = 0;
  storeKey: string = null;
  padding = '0000';
  modalService: SdcUiServices.ModalService;
  store : NgRedux<AppState>;
  existingNames : {[key: string] : any};
  currentNode : ITreeNode = null;



  canDuplicate(node: ITreeNode): boolean {
    let reduxState = <AppState>JSON.parse(sessionStorage.getItem('reduxState'));
    return node.data.type === 'VF' || node.data.type === 'VL';
  }

  isEnabled(node: ITreeNode, store: NgRedux<AppState>, serviceId : string): boolean {
    if(!_.isNil(node) && !_.isNil(node.data.menuActions['duplicate'])){
      if(this.hasMissingData(node)) return false;
      const typeNodeInformation : TypeNodeInformation = new TypeNodeInformation(node);
      const flags = store.getState().global.flags;
      if (flags && !!flags['FLAG_2002_UNLIMITED_MAX']) {
        return true;
      }else {
        const max : number = store.getState().service.serviceHierarchy[serviceId][typeNodeInformation.hierarchyName][node.data.modelName].properties['max_instances'] || 1;
        const currentExisting: number = store.getState().service.serviceInstance[serviceId][typeNodeInformation.existingMappingCounterName][node.data.modelUniqueId];

        return max - currentExisting > 0;
      }

    }else {
      return false;
    }
  }

  hasMissingData(node : ITreeNode): boolean {
    if(!_.isNil(node)){
      if(node.data.missingData) return true;
      if(!_.isNil(node.data.children)){
        for(let child of node.data.children) {
          if(child.missingData){
            return true;
          }
        }
      }

    }
    return false;
  }

  getRemainsInstance(modelId : string, modelName : string, serviceId : string, store: NgRedux<AppState>, node : ITreeNode) : number {
    const typeNodeInformation : TypeNodeInformation = new TypeNodeInformation(node);
    const properties  = store.getState().service.serviceHierarchy[serviceId][typeNodeInformation.hierarchyName][modelName].properties;
    const currentExisting : number = store.getState().service.serviceInstance[serviceId][typeNodeInformation.existingMappingCounterName][modelId];


    const flags = store.getState().global.flags;
    if (flags && !!flags['FLAG_2002_UNLIMITED_MAX']) {
      return !_.isNil(properties) && !_.isNil(properties['max_instances']) ? properties['max_instances'] - currentExisting : 10;
    }else {
      return (!_.isNil(properties) && !_.isNil(properties['max_instances'])) ? properties['max_instances'] - currentExisting : null;
    }
  }






  openDuplicateModal(currentServiceId: string, currentUuid: string, currentId: string, storeKey : string, numberOfDuplicate: number, _store : NgRedux<AppState>, node: ITreeNode): IModalConfig {
    this.currentInstanceId = currentId;
    this.currentServiceId = currentServiceId;
    this.maxNumberOfDuplicate = this.getRemainsInstance(currentUuid, currentId, currentServiceId, _store, node);
    this.storeKey = storeKey;
    this.store = _store;
    this.currentNode = node;


    return  {
      size: SdcUiCommon.ModalSize.medium,
      title: 'Duplicate Node',
      type: SdcUiCommon.ModalType.custom,
      buttons: [
        {text: 'Duplicate', callback: this.duplicate.bind(this, this.currentNode), closeModal: true},
        {text: 'Cancel', closeModal: true}
      ]
    };
  }

  duplicate(node : ITreeNode): void {
    const typeNodeInformation : TypeNodeInformation = new TypeNodeInformation(node);
    this.existingNames = this.store.getState().service.serviceInstance[this.currentServiceId].existingNames;
    const toClone  = this.store.getState().service.serviceInstance[this.currentServiceId][typeNodeInformation.hierarchyName][this.storeKey];
    let newObjects = {};
    for(let i = 0; i < this.numberOfDuplicates; i++) {
      const uniqueStoreKey = this.generateUniqueStoreKey(this.currentServiceId, this.currentInstanceId, this.store.getState().service.serviceInstance[this.currentServiceId][typeNodeInformation.hierarchyName], newObjects);
      const clone = this.cloneVnf(toClone, this.currentInstanceId);
      newObjects[uniqueStoreKey] = clone;
    }
    this.store.dispatch(duplicateBulkInstances(this.currentServiceId, newObjects, this.existingNames, node));
    this.store.dispatch(changeInstanceCounter(toClone.modelInfo.modelUniqueId, this.currentServiceId, this.numberOfDuplicates, node));
    this._logService.info("Duplicate " + this.storeKey + " serviceId: " + this.currentServiceId + "number of duplicate: " + this.numberOfDuplicates, toClone);
  }


  cloneVnf(vnf : VnfInstance, originalName: string): VnfInstance {
    let newUniqueVnf : VnfInstance = _.cloneDeep(vnf);

    newUniqueVnf.originalName = originalName;
    newUniqueVnf.trackById = DefaultDataGeneratorService.createRandomTrackById();
    if (!_.isNil(vnf.instanceName)){
      newUniqueVnf.instanceName = this.ensureUniqueNameOrGenerateOne(vnf.instanceName);
    }

    for (let vf_module_model_name in  vnf.vfModules) {
      const vfModuleModel: VfModuleMap = vnf.vfModules[vf_module_model_name];
      for (let vfModule in vfModuleModel) {
        newUniqueVnf.vfModules[vf_module_model_name][vfModule].trackById = DefaultDataGeneratorService.createRandomTrackById();
        if (!_.isNil(vfModuleModel[vfModule].instanceName)){
          newUniqueVnf.vfModules[vf_module_model_name][vfModule].instanceName = this.ensureUniqueNameOrGenerateOne(vfModuleModel[vfModule].instanceName);
        }
        if (!_.isNil(vfModuleModel[vfModule].volumeGroupName)){
          newUniqueVnf.vfModules[vf_module_model_name][vfModule].volumeGroupName = this.ensureUniqueNameOrGenerateOne(vfModuleModel[vfModule].volumeGroupName);
        }
      }
    }
    return newUniqueVnf;
  }

  ensureUniqueNameOrGenerateOne(instanceName){
    let uniqueInstanceName = instanceName;
    if (this.isAlreadyExists(instanceName, this.existingNames)) {
      uniqueInstanceName = this.generateNextUniqueName(instanceName, this.existingNames);
      this.existingNames[uniqueInstanceName.toLowerCase()] = "";
    }
    return uniqueInstanceName;
  }


  isAlreadyExists(name : string, existingNames : {[key: string] : any}){
    return _.has(existingNames, name.toLowerCase());
  }

  generateNextUniqueName(name : string, existingNames : {[key: string] : any})  :string{
    let suffix = "000";
    let counter = 1;
    if (name.match(/^.*_[\d]{3}$/)){
      name = name.substring(0, name.length - 4);
    }

    while(true){
      let paddingNumber : string = this.getNumberAsPaddingString(counter, suffix);
      let candidateUniqueName = name + '_' + paddingNumber;
      if(!this.isAlreadyExists(candidateUniqueName, existingNames)){
        return candidateUniqueName;
      }
      counter++;
    }
  }

  generateUniqueStoreKey(serviceId : string, objectName : string, existing : any, newObjects: any) : string {
    let counter = 1;
    while(true){
      let paddingNumber : string = this.getNumberAsPaddingString(counter, this.padding);
      const name = objectName + ':' + paddingNumber;
      if(_.isNil(existing[name]) && _.isNil(newObjects[name])){
        return name;
      }
      counter++;
    }
  }

  getNumberAsPaddingString(val: number, padding: string): string {
    const str = "" + val;
    return padding.substring(0, padding.length - str.length) + str;
  }
}
