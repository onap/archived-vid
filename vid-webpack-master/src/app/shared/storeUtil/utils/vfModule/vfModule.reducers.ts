import {Action} from "redux";
import * as _ from "lodash";
import {
  CreateVFModuleInstanceAction, DeleteActionVfModuleInstanceAction, DeleteVFModuleField,
  DeleteVfModuleInstanceAction, UndoDeleteActionVfModuleInstanceAction, UpdateVFModluePosition, UpdateVFModuleField,
  UpdateVFModuleInstanceAction, UpgradeVfModuleInstanceAction,
  VfModuleActions,
} from "./vfModule.actions";
import {ServiceInstance} from "../../../models/serviceInstance";
import {VfModuleMap} from "../../../models/vfModulesMap";
import {ServiceState} from "../main.reducer";
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";
import {updateServiceValidationCounter} from "../reducersHelper";


export function vfModuleReducer(state: ServiceState , action: Action) : ServiceState{
  switch (action.type) {
    case VfModuleActions.CREATE_VF_MODULE: {
      const updateVFModuleInstanceAction = <CreateVFModuleInstanceAction>action;
      const vfInstance = updateVFModuleInstanceAction.vfInstance;
      const serviceUuid = updateVFModuleInstanceAction.serviceUuid;
      const vfModuleId = updateVFModuleInstanceAction.vfId;
      const vnfStoreKey = updateVFModuleInstanceAction.vnfStoreKey;

      let newState = Object.assign({}, state);

      let vfModulesMap = newState.serviceInstance[serviceUuid].vnfs[vnfStoreKey].vfModules[vfModuleId] || new VfModuleMap();
      let randomId = generateId();
      vfInstance.action = ServiceInstanceActions.Create;
      vfModulesMap[vfModuleId + randomId] = vfInstance;
      updateUniqueNames(null, vfInstance.instanceName, newState.serviceInstance[serviceUuid]);
      updateUniqueNames(null, vfInstance.volumeGroupName, newState.serviceInstance[serviceUuid]);
      updateServiceValidationCounter(newState, false, vfInstance['isMissingData'], serviceUuid);

      newState.serviceInstance[serviceUuid].vnfs[vnfStoreKey].vfModules[vfModuleId] = vfModulesMap;
      return newState;
    }
    case VfModuleActions.UPDATE_VF_MODULE: {
      const updateVFModuleInstanceAction = <UpdateVFModuleInstanceAction>action;
      const vfInstance = updateVFModuleInstanceAction.vfInstance;
      const serviceUuid = updateVFModuleInstanceAction.serviceUuid;
      const vfModuleId = updateVFModuleInstanceAction.vfId;
      const newState = _.cloneDeep(state);
      const vnfs = newState.serviceHierarchy[serviceUuid].vnfs;
      let vnfId = getVfModuleParentVnfId(vnfs, vfModuleId);
      const vnfStoreKey = updateVFModuleInstanceAction.vnfStoreKey;
      if (!_.isNil(vnfStoreKey)) {
        vnfId = vnfStoreKey;
      }
      let vfModulesMap = newState.serviceInstance[serviceUuid].vnfs[vnfId].vfModules[vfModuleId] || new VfModuleMap();
      updateServiceValidationCounter(newState, vfModulesMap[updateVFModuleInstanceAction.dynamicModelName]['isMissingData'], vfInstance.isMissingData, serviceUuid);
      updateUniqueNames(vfModulesMap[updateVFModuleInstanceAction.dynamicModelName].instanceName, vfInstance.instanceName, newState.serviceInstance[serviceUuid]);
      updateUniqueNames(vfModulesMap[updateVFModuleInstanceAction.dynamicModelName].volumeGroupName, vfInstance.volumeGroupName, newState.serviceInstance[serviceUuid]);
      vfModulesMap[updateVFModuleInstanceAction.dynamicModelName] = vfInstance;
      newState.serviceInstance[serviceUuid].vnfs[vnfId].vfModules[vfModuleId] = vfModulesMap;
      return newState;
    }
    case VfModuleActions.REMOVE_VNF_MODULE_INSTANCE: {
      const actionData = (<DeleteVfModuleInstanceAction>action);
      if (state.serviceInstance[actionData.serviceModelId]) {
        let vfModulesMap = state.serviceInstance[actionData.serviceModelId].vnfs[actionData.vnfStoreKey].vfModules;
        updateIsMissingDataOnDeleteVFModule(state, actionData.serviceModelId, actionData.vnfStoreKey, actionData.modelName);
        updateUniqueNames(vfModulesMap[actionData.modelName][actionData.dynamicModelName].instanceName, null, state.serviceInstance[actionData.serviceModelId] );
        updateUniqueNames(vfModulesMap[actionData.modelName][actionData.dynamicModelName].volumeGroupName, null, state.serviceInstance[actionData.serviceModelId] );
        delete vfModulesMap[actionData.modelName][actionData.dynamicModelName];
        if(_.isEmpty(vfModulesMap[actionData.modelName])){
          delete vfModulesMap[actionData.modelName];
        }
      }
      return Object.assign({}, state);
    }
    case VfModuleActions.DELETE_ACTION_VF_MODULE_INSTANCE : {
      let newState = _.cloneDeep(state);
      let vfModules = newState.serviceInstance[(<DeleteActionVfModuleInstanceAction>action).serviceId].vnfs[(<DeleteActionVfModuleInstanceAction>action).vnfStoreKey].vfModules;

      for(let key in vfModules){
        let firstKey = Object.keys(vfModules[key])[0];
        if(firstKey === (<DeleteActionVfModuleInstanceAction>action).dynamicModelName){
          let oldAction = newState.serviceInstance[(<DeleteActionVfModuleInstanceAction>action).serviceId].vnfs[(<DeleteActionVfModuleInstanceAction>action).vnfStoreKey].vfModules[key][firstKey].action;
          if(oldAction === ServiceInstanceActions.None_Delete || oldAction === ServiceInstanceActions.Update_Delete) return newState;
          newState.serviceInstance[(<DeleteActionVfModuleInstanceAction>action).serviceId].vnfs[(<DeleteActionVfModuleInstanceAction>action).vnfStoreKey].vfModules[key][firstKey].action = (oldAction + '_Delete') as ServiceInstanceActions;
          setLcpCloudRegionIdAndTenantIdFromVnf(newState, (<DeleteActionVfModuleInstanceAction>action).serviceId, (<DeleteActionVfModuleInstanceAction>action).vnfStoreKey, key, firstKey);
          updateIsMissingDataOnDeleteVFModule(newState, (<UndoDeleteActionVfModuleInstanceAction>action).serviceId, (<UndoDeleteActionVfModuleInstanceAction>action).vnfStoreKey, key);
          return newState;
        }
      }
      return newState;
    }
    case VfModuleActions.UNDO_DELETE_ACTION_VF_MODULE_INSTANCE : {
      let newState = _.cloneDeep(state);
      let vfModules = newState.serviceInstance[(<DeleteActionVfModuleInstanceAction>action).serviceId].vnfs[(<DeleteActionVfModuleInstanceAction>action).vnfStoreKey].vfModules;

      for(let key in vfModules){
        let firstKey = Object.keys(vfModules[key])[0];
        if(firstKey === (<UndoDeleteActionVfModuleInstanceAction>action).dynamicModelName){
          let oldAction = newState.serviceInstance[(<UndoDeleteActionVfModuleInstanceAction>action).serviceId].vnfs[(<UndoDeleteActionVfModuleInstanceAction>action).vnfStoreKey].vfModules[key][firstKey].action;
          newState.serviceInstance[(<UndoDeleteActionVfModuleInstanceAction>action).serviceId].vnfs[(<UndoDeleteActionVfModuleInstanceAction>action).vnfStoreKey].vfModules[key][firstKey].action = (oldAction.split('_')[0]) as ServiceInstanceActions;
          updateIsMissingDataOnDeleteVFModule(newState, (<UndoDeleteActionVfModuleInstanceAction>action).serviceId, (<UndoDeleteActionVfModuleInstanceAction>action).vnfStoreKey, key);
          return newState;
        }
      }
      return newState;
    }

    case VfModuleActions.UPDATE_VFMODULE_POSITION : {
      const updateVFModluePosition = <UpdateVFModluePosition>action;
      const serviceUuid = updateVFModluePosition.instanceId;
      const dynamicModelName = updateVFModluePosition.node.dynamicModelName;
      const modelName = updateVFModluePosition.node.modelName;
      const newState = _.cloneDeep(state);

      newState.serviceInstance[serviceUuid].vnfs[updateVFModluePosition.vnfStoreKey].vfModules[modelName][dynamicModelName].position = updateVFModluePosition.node.position;
      return newState;
    }

    case VfModuleActions.UPGRADE_VFMODULE : {
      let clonedState = _.cloneDeep(state);
      const upgradeAction = (<UpgradeVfModuleInstanceAction>action);
      let oldAction = clonedState
        .serviceInstance[upgradeAction.serviceId]
        .vnfs[upgradeAction.vnfStoreKey]
        .vfModules[upgradeAction.modelName][upgradeAction.dynamicModelName]
        .action;
      if(!_.isNil(oldAction) && oldAction.includes("Upgrade")) {
        return clonedState;
      }
      clonedState.serviceInstance[upgradeAction.serviceId]
        .vnfs[upgradeAction.vnfStoreKey]
        .vfModules[upgradeAction.modelName][upgradeAction.dynamicModelName]
        .action = (`${oldAction}_Upgrade`) as ServiceInstanceActions;
      return clonedState;
    }
    case VfModuleActions.UNDO_UPGRADE_VFMODULE_ACTION : {
      let clonedState = _.cloneDeep(state);
      const upgradeAction = (<UpgradeVfModuleInstanceAction>action);
      let oldAction = clonedState
        .serviceInstance[upgradeAction.serviceId]
        .vnfs[upgradeAction.vnfStoreKey]
        .vfModules[upgradeAction.modelName][upgradeAction.dynamicModelName]
        .action;
      if(!_.isNil(oldAction) && oldAction.includes("Upgrade")) {
        clonedState.serviceInstance[upgradeAction.serviceId]
          .vnfs[upgradeAction.vnfStoreKey]
          .vfModules[upgradeAction.modelName][upgradeAction.dynamicModelName]
          .action = ServiceInstanceActions.None;
      }
      return clonedState;
    }
    case VfModuleActions.UPDATE_VFMODULE_FEILD : {
      let clonedState = _.cloneDeep(state);
      let updateFieldAction =  <UpdateVFModuleField> action;

        clonedState.serviceInstance[updateFieldAction.serviceId]
          .vnfs[updateFieldAction.vnfStoreKey]
          .vfModules[updateFieldAction.modelName][updateFieldAction.dynamicModelName][updateFieldAction.fieldName] =  updateFieldAction.fieldValue;

      return clonedState;
    }
    case VfModuleActions.DELETE_VFMODULE_FIELD : {
      let clonedState = _.cloneDeep(state);
      let deleteAction =  <DeleteVFModuleField> action;

      delete clonedState.serviceInstance[deleteAction.serviceId]
        .vnfs[deleteAction.vnfStoreKey]
        .vfModules[deleteAction.modelName][deleteAction.dynamicModelName][deleteAction.deleteFieldName];

      return clonedState;
    }
  }
}

const updateIsMissingDataOnDeleteVFModule = (state: any, serviceModelId: string, vnfStoreKey: string, vfModuleName): void => {
  const vfModules = state.serviceInstance[serviceModelId].vnfs[vnfStoreKey].vfModules[vfModuleName];

  _.forOwn(vfModules, (vfModuleInstance) => {
    let isMissingData: boolean = vfModuleInstance.isMissingData;
    updateServiceValidationCounter(state, isMissingData, false, serviceModelId);
  });
};

const  setLcpCloudRegionIdAndTenantIdFromVnf = (state: any, serviceModelId: string, vnfStoreKey: string, key: string, firstKey: string) :void => {
  let tenantId = state.serviceInstance[serviceModelId].vnfs[vnfStoreKey].tenantId;
  let lcpCloudRegion = state.serviceInstance[serviceModelId].vnfs[vnfStoreKey].lcpCloudRegionId;

  state.serviceInstance[serviceModelId].vnfs[vnfStoreKey].vfModules[key][firstKey].tenantId = tenantId;
  state.serviceInstance[serviceModelId].vnfs[vnfStoreKey].vfModules[key][firstKey].lcpCloudRegionId = lcpCloudRegion;
}

const updateUniqueNames = (oldName : string, newName : string, serviceInstance : ServiceInstance) : void => {
  let existingNames = serviceInstance.existingNames;
  if (!_.isNil(oldName) && oldName.toLowerCase() in existingNames) {
    delete existingNames[oldName.toLowerCase()];
  }
  if(!_.isNil(newName)) {
    existingNames[newName.toLowerCase()] = "";
  }
};


const generateId = () => {
  return Math.random().toString(36).replace(/[^a-z]+/g, '').substr(0, 5);
};
const getVfModuleParentVnfId = (vnfs: object, vfModuleId: string) => {
  let vnfId = undefined;
  _.forOwn(vnfs, (value, key) => {
    if (vnfs[key].vfModules && vnfs[key].vfModules[vfModuleId]) {
      vnfId = vnfs[key].modelCustomizationName;
      return false;
    }
  });
  return vnfId;
};










