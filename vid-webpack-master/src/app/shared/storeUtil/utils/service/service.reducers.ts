import {Action} from "redux";
import {
  AddServiceAction,
  ChangeServiceDirty,
  CreateServiceInstanceAction,
  ServiceActions,
  UndoUpgradeServiceAction,
  UpdateServiceInstanceAction,
  UpdateServiceModelAction,
  UpgradeServiceAction,
  UpdateServiceModelInfoAction
} from "./service.actions";
import {ServiceInstance} from "../../../models/serviceInstance";
import {ServiceState} from "../main.reducer";
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";
import * as _ from "lodash";
import {ServiceInfoModel} from "../../../server/serviceInfo/serviceInfo.model";

export function serviceReducer(state: ServiceState, action: Action) : ServiceState{

  switch (action.type) {
    case ServiceActions.UPDATE_SERVICE_INSTANCE : {
      let newState = _.cloneDeep(state);
      const updateServiceInstanceAction = <UpdateServiceInstanceAction>action;
      const uuid = updateServiceInstanceAction.serviceUuid;
      const serviceInstance = updateServiceInstanceAction.serviceInstance;

      updateUniqueNames(serviceInstance.instanceName, updateServiceInstanceAction.serviceInstance.instanceName, newState.serviceInstance[uuid]);

      newState.serviceInstance[uuid] = _.merge(newState.serviceInstance[uuid], serviceInstance);
      return newState;
    }
    case ServiceActions.CREATE_SERVICE_INSTANCE : {
      const updateServiceInstanceAction = <CreateServiceInstanceAction>action;
      const uuid = updateServiceInstanceAction.serviceUuid;
      let newState = _.cloneDeep(state);

      const serviceInstance: ServiceInstance =  new ServiceInstance();
      const currentInstaceName = state.serviceInstance[uuid] ? serviceInstance.instanceName : null;

      newState.serviceInstance[uuid] = Object.assign(serviceInstance, updateServiceInstanceAction.serviceInstance);
      newState.serviceInstance[uuid].vidNotions = _.get(state,`serviceHierarchy[${uuid}].service.vidNotions`);
      if (!_.isNil(updateServiceInstanceAction.serviceInstance)) {
        updateUniqueNames(currentInstaceName, updateServiceInstanceAction.serviceInstance.instanceName, newState.serviceInstance[uuid]);
      }
      return newState;
    }
    case ServiceActions.DELETE_ALL_SERVICE_INSTANCES: {
      if (state.serviceInstance) {
        let newState = _.cloneDeep(state);
        newState.serviceInstance = {};
        return Object.assign({}, state, newState);
      }
      return Object.assign({}, state);
    }
    case ServiceActions.UPDATE_MODEL: {
      let uuid = (<UpdateServiceModelAction>action).serviceHierarchy.service.uuid;
      state.serviceHierarchy[uuid] = _.cloneDeep((<UpdateServiceModelAction>action).serviceHierarchy);
      return Object.assign({}, state);
    }
    case ServiceActions.ADD_SERVICE_ACTION: {
      const uuid: string = (<AddServiceAction>action).serviceUuid;
      const actionToAdd: ServiceInstanceActions =  (<AddServiceAction>action).action;
      state.serviceInstance[uuid].action =  actionToAdd;
      return Object.assign({}, state);
    }
    case ServiceActions.CHANGE_SERVICE_IS_DIRTY : {
      let newState = _.cloneDeep(state);
      let serviceInstanceAction: ServiceInstanceActions = newState.serviceInstance[(<ChangeServiceDirty>action).serviceId].action;

      if(serviceInstanceAction !== ServiceInstanceActions.None){
        newState.serviceInstance[(<ChangeServiceDirty>action).serviceId].isDirty = true;
        return newState;
      }

      const nodes =  (<ChangeServiceDirty>action).nodes;
      for(let node of nodes){
        const dirty = isDirty(node);
        if(dirty) {
          newState.serviceInstance[(<ChangeServiceDirty>action).serviceId].isDirty = true;
          return newState;
        }
      }
      newState.serviceInstance[(<ChangeServiceDirty>action).serviceId].isDirty = false;
      return newState;
    }
    case ServiceActions.UPGRADE_SERVICE_ACTION: {
      let clonedState = _.cloneDeep(state);
      let oldServiceAction: string = ServiceInstanceActions.None;
      const castingAction = <UpgradeServiceAction>action;
      const uuid: string = castingAction.serviceUuid;
      return upgradeServiceInstance(clonedState, uuid, oldServiceAction);
    }

    case ServiceActions.UNDO_UPGRADE_SERVICE_ACTION: {
      let clonedState = _.cloneDeep(state);
      const castingAction = <UndoUpgradeServiceAction>action;
      const uuid: string = castingAction.serviceUuid;
      if(!_.isNil(clonedState.serviceInstance[uuid].action) && clonedState.serviceInstance[uuid].action.includes("Upgrade")) {
        return undoUpgradeServiceInstance(clonedState, uuid);
      }
    }

    case ServiceActions.UPDATE_SERVICE_INFO_MODEL: {
      const updateServiceInfoModel = <UpdateServiceModelInfoAction>action;
      let newState = _.cloneDeep(state);
      const serviceInfoModel : ServiceInfoModel = new ServiceInfoModel();
      const currentServiceInfoModel = state.serviceInfoModel ? serviceInfoModel : null;
      newState.serviceInfoModel = Object.assign(serviceInfoModel, updateServiceInfoModel.serviceInfoModel);
      return newState
    }
  }
}

const isDirty = (node) : boolean => {
  if(node.action !== ServiceInstanceActions.None) return true;
  if(!_.isNil(node.children) && node.children.length > 0){
    for(let child of node.children){
      const dirty: boolean = isDirty(child);
      if(dirty) return true;
    }
  }
  return false;
};

const updateUniqueNames = (oldName : string, newName : string, serviceInstance : ServiceInstance) : void => {
  let existingNames = serviceInstance.existingNames;
  if (!_.isNil(oldName) && oldName.toLowerCase() in existingNames) {
    delete existingNames[oldName.toLowerCase()];
  }
  if(!_.isNil(newName)) {
    existingNames[newName.toLowerCase()] = "";
  }
};

function upgradeServiceInstance(clonedState, uuid: string, oldServiceAction: string) {
  if(!clonedState.serviceInstance[uuid].action.includes("Upgrade")){
    clonedState.serviceInstance[uuid].action = (`${oldServiceAction}_Upgrade`) as ServiceInstanceActions;
  }
  clonedState.serviceInstance[uuid].isUpgraded = true;
  clonedState.serviceInstance[uuid].upgradedVFMSonsCounter++;
  return clonedState;
}

function undoUpgradeServiceInstance(clonedState, uuid: string) {
  clonedState.serviceInstance[uuid].upgradedVFMSonsCounter--;
  if(clonedState.serviceInstance[uuid].upgradedVFMSonsCounter == 0){
    clonedState.serviceInstance[uuid].action = ServiceInstanceActions.None;
    clonedState.serviceInstance[uuid].isUpgraded = false;
  }
  return clonedState;
}



