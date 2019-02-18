import {Action} from "redux";
import {VnfInstance} from "../../../models/vnfInstance";
import {
  CreateVnfInstanceAction,
  DeleteActionVnfInstanceAction, RemoveVnfInstanceAction, UndoDeleteActionVnfInstanceAction,
  UpdateVnfInstanceAction, UpdateVnfPosition,
  VNFActions
} from "./vnf.actions";
import * as _ from "lodash";
import {ServiceInstance} from "../../../models/serviceInstance";
import {ServiceState} from "../main.reducer";
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";

export function vnfReducer(state: ServiceState, action: Action): ServiceState {
  switch (action.type) {
    case VNFActions.CREATE_VNF_INSTANCE: {
      const updateVnfInstanceAction = <CreateVnfInstanceAction>action;
      const serviceUuid = updateVnfInstanceAction.serviceUuid;
      let vnfModelName = updateVnfInstanceAction.vnfModelName;
      let newState = _.cloneDeep(state);

      updateVnfInstanceAction.vnfInstance.originalName = vnfModelName;
      updateVnfInstanceAction.vnfModelName = calculateNextUniqueModelName(vnfModelName, serviceUuid, newState, 'vnfs');

      let vnfInstance: VnfInstance = newState.serviceInstance[serviceUuid].vnfs[vnfModelName];
      vnfInstance = new VnfInstance();
      updateVnfInstanceAction.vnfInstance.vnfStoreKey = updateVnfInstanceAction.vnfModelName;
      updateVnfInstanceAction.vnfInstance.originalName = vnfModelName;
      vnfInstance.originalName = updateVnfInstanceAction.vnfInstance.originalName;
      vnfInstance.vnfStoreKey = updateVnfInstanceAction.vnfInstance.vnfStoreKey;
      updateServiceValidationCounter(newState, vnfInstance['isMissingData'], updateVnfInstanceAction.vnfInstance['isMissingData'], serviceUuid);

      newState.serviceInstance[serviceUuid].vnfs[updateVnfInstanceAction.vnfModelName] = Object.assign(vnfInstance, updateVnfInstanceAction.vnfInstance);
      return newState;
    }

    case VNFActions.UPDATE_VNF_INSTANCE: {
      const updateVnfInstanceAction = <UpdateVnfInstanceAction>action;
      const serviceUuid = updateVnfInstanceAction.serviceUuid;
      let vnfStoreKey = updateVnfInstanceAction.vnfStoreKey;


      let newState = _.cloneDeep(state);
      let vnfInstance: VnfInstance = newState.serviceInstance[serviceUuid].vnfs[vnfStoreKey];
      updateUniqueNames(vnfInstance ? vnfInstance.instanceName : null, updateVnfInstanceAction.vnfInstance.instanceName, newState.serviceInstance[serviceUuid]);

      vnfInstance = vnfInstance || new VnfInstance();
      updateServiceValidationCounter(newState, vnfInstance['isMissingData'], updateVnfInstanceAction.vnfInstance['isMissingData'], serviceUuid);

      newState.serviceInstance[serviceUuid].vnfs[vnfStoreKey] = Object.assign(vnfInstance, updateVnfInstanceAction.vnfInstance);
      return newState;
    }

    case VNFActions.DELETE_ACTION_VNF_INSTANCE : {
      let newState = _.cloneDeep(state);
      let vnf = newState.serviceInstance[(<DeleteActionVnfInstanceAction>action).serviceId].vnfs[(<DeleteActionVnfInstanceAction>action).vnfStoreKey];
      let oldAction = vnf.action;
      if(oldAction === ServiceInstanceActions.None_Delete || oldAction === ServiceInstanceActions.Update_Delete) return newState;
      newState.serviceInstance[(<DeleteActionVnfInstanceAction>action).serviceId].vnfs[(<DeleteActionVnfInstanceAction>action).vnfStoreKey].action = (oldAction + '_Delete') as ServiceInstanceActions;
      updateServiceValidationCounter(newState, vnf['isMissingData'], false,  (<RemoveVnfInstanceAction>action).serviceId);
      return newState;
    }

    case VNFActions.UNDO_DELETE_ACTION_VNF_INSTANCE : {
      let newState = _.cloneDeep(state);
      let vnf = newState.serviceInstance[(<UndoDeleteActionVnfInstanceAction>action).serviceId].vnfs[(<UndoDeleteActionVnfInstanceAction>action).vnfStoreKey];
      let oldState = vnf.action;
      newState.serviceInstance[(<UndoDeleteActionVnfInstanceAction>action).serviceId].vnfs[(<UndoDeleteActionVnfInstanceAction>action).vnfStoreKey].action = (oldState.split('_')[0]) as ServiceInstanceActions;
      updateServiceValidationCounter(newState, vnf['isMissingData'], false,  (<UndoDeleteActionVnfInstanceAction>action).serviceId);
      return newState;
    }

    case VNFActions.REMOVE_VNF_INSTANCE : {
      let newState = _.cloneDeep(state);
      let vnfInstance = newState.serviceInstance[(<RemoveVnfInstanceAction>action).serviceId].vnfs[(<RemoveVnfInstanceAction>action).vnfStoreKey];
      updateServiceValidationCounter(newState, vnfInstance['isMissingData'], false,  (<RemoveVnfInstanceAction>action).serviceId);
      delete newState.serviceInstance[(<RemoveVnfInstanceAction>action).serviceId].vnfs[(<RemoveVnfInstanceAction>action).vnfStoreKey];
      return newState;
    }

    case VNFActions.UPDATE_VNF_POSITION : {
      let newState = _.cloneDeep(state);
      newState.serviceInstance[(<UpdateVnfPosition>action).instanceId].vnfs[(<UpdateVnfPosition>action).vnfStoreKey].position = (<UpdateVnfPosition>action).node.position;
      return newState;
    }
  }
}

const updateServiceValidationCounter = (newState: any, oldValidationState: boolean, newValidationState: boolean, serviceUuid: string) => {
  if (oldValidationState && !newValidationState) {
    newState.serviceInstance[serviceUuid].validationCounter--;
  } else if (!oldValidationState && newValidationState) {
    newState.serviceInstance[serviceUuid].validationCounter++;
  }
};


const updateUniqueNames = (oldName: string, newName: string, serviceInstance: ServiceInstance): void => {
  let existingNames = serviceInstance.existingNames;
  if (!_.isNil(oldName) && oldName.toLowerCase() in existingNames) {
    delete existingNames[oldName.toLowerCase()];
  }
  if (!_.isNil(newName)) {
    existingNames[newName.toLowerCase()] = "";
  }
};


export const calculateNextUniqueModelName = (vnfModelName: string, serviceId: string, state: any, levelName: string): string => {
  let counter: number = null;
  while (true) {
    let pattern = !_.isNil(counter) ? ("_" + counter) : "";
    if (!_.isNil(state.serviceInstance[serviceId][levelName][vnfModelName + pattern])) {
      counter = counter ? (counter + 1) : 1;
    } else {
      return vnfModelName + pattern;
    }
  }
};





