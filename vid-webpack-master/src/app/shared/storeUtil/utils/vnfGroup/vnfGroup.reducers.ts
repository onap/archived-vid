import {Action} from "redux";
import {VnfGroupInstance} from "../../../models/vnfGroupInstance";
import * as _ from "lodash";
import {ServiceInstance} from "../../../models/serviceInstance";
import {ServiceState} from "../main.reducer";
import {
  CreateVnfGroupInstanceAction,
  DeleteActionVnfGroupInstanceAction, SetOptionalMembersVnfGroupInstanceAction,
  UpdateVnfGroupInstanceAction,
  VnfGroupActions
} from "./vnfGroup.actions";
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";

export function vnfGroupReducer(state: ServiceState, action: Action): ServiceState {
  switch (action.type) {
    case VnfGroupActions.CREATE_VNF_GROUP_INSTANCE: {
      const updateVnfGroupInstanceAction = <CreateVnfGroupInstanceAction>action;
      const serviceUuid = updateVnfGroupInstanceAction.serviceUuid;
      let vnfGroupModelName = updateVnfGroupInstanceAction.vnfGroupModelName;
      let newState = _.cloneDeep(state);

      updateVnfGroupInstanceAction.vnfGroupInstance.originalName = vnfGroupModelName;
      updateVnfGroupInstanceAction.vnfGroupModelName = calculateNextUniqueModelName(vnfGroupModelName, serviceUuid, newState, 'vnfGroups');

      let vnfGroupInstance: VnfGroupInstance = newState.serviceInstance[serviceUuid].vnfGroups[vnfGroupModelName];
      vnfGroupInstance = new VnfGroupInstance();
      updateVnfGroupInstanceAction.vnfGroupInstance.vnfGroupStoreKey = updateVnfGroupInstanceAction.vnfGroupModelName;
      updateVnfGroupInstanceAction.vnfGroupInstance.originalName = vnfGroupModelName;
      vnfGroupInstance.originalName = updateVnfGroupInstanceAction.vnfGroupInstance.originalName;
      vnfGroupInstance.vnfGroupStoreKey = updateVnfGroupInstanceAction.vnfGroupInstance.vnfGroupStoreKey;
      updateServiceValidationCounter(newState, vnfGroupInstance['isMissingData'], updateVnfGroupInstanceAction.vnfGroupInstance['isMissingData'], serviceUuid);

      newState.serviceInstance[serviceUuid].vnfGroups[updateVnfGroupInstanceAction.vnfGroupModelName] = Object.assign(vnfGroupInstance, updateVnfGroupInstanceAction.vnfGroupInstance);
      return newState;
    }
    case VnfGroupActions.UPDATE_VNF_GROUP_INSTANCE: {
      const updateVnfInstanceAction = <UpdateVnfGroupInstanceAction>action;
      const serviceUuid = updateVnfInstanceAction.serviceUuid;
      let vnfGroupStoreKey = updateVnfInstanceAction.vnfGroupStoreKey;


      let newState = _.cloneDeep(state);
      let vnfGroupInstance: VnfGroupInstance = newState.serviceInstance[serviceUuid].vnfGroups[vnfGroupStoreKey];
      updateUniqueNames(vnfGroupInstance ? vnfGroupInstance.instanceName : null, updateVnfInstanceAction.vnfGroupInstance.instanceName, newState.serviceInstance[serviceUuid]);

      vnfGroupInstance = vnfGroupInstance || new VnfGroupInstance();
      updateServiceValidationCounter(newState, vnfGroupInstance['isMissingData'], updateVnfInstanceAction.vnfGroupInstance['isMissingData'], serviceUuid);

      newState.serviceInstance[serviceUuid].vnfGroups[vnfGroupStoreKey] = Object.assign(vnfGroupInstance, updateVnfInstanceAction.vnfGroupInstance);
      return newState;
    }
    case VnfGroupActions.DELETE_ACTION_VNF_GROUP_INSTANCE : {
      let newState = _.cloneDeep(state);
      let oldAction = newState.serviceInstance[(<DeleteActionVnfGroupInstanceAction>action).serviceId].vnfGroups[(<DeleteActionVnfGroupInstanceAction>action).vnfGroupStoreKey].action;
      if(oldAction === ServiceInstanceActions.None_Delete || oldAction === ServiceInstanceActions.Update_Delete) return newState;
      newState.serviceInstance[(<DeleteActionVnfGroupInstanceAction>action).serviceId].vnfGroups[(<DeleteActionVnfGroupInstanceAction>action).vnfGroupStoreKey].action = (oldAction + '_Delete') as ServiceInstanceActions;
      return newState;
    }
    case VnfGroupActions.UNDO_DELETE_ACTION_VNF_GROUP_INSTANCE : {
      let newState = _.cloneDeep(state);
      let oldState = newState.serviceInstance[(<DeleteActionVnfGroupInstanceAction>action).serviceId].vnfGroups[(<DeleteActionVnfGroupInstanceAction>action).vnfGroupStoreKey].action;
      newState.serviceInstance[(<DeleteActionVnfGroupInstanceAction>action).serviceId].vnfGroups[(<DeleteActionVnfGroupInstanceAction>action).vnfGroupStoreKey].action = (oldState.split('_')[0]) as ServiceInstanceActions;
      return newState;
    }
    case VnfGroupActions.SET_OPTIONAL_MEMBERS_VNF_GROUP_INSTANCE:{
      let newState = _.cloneDeep(state);
      newState.serviceInstance[(<SetOptionalMembersVnfGroupInstanceAction>action).serviceId].optionalGroupMembersMap[(<SetOptionalMembersVnfGroupInstanceAction>action).path] = (<SetOptionalMembersVnfGroupInstanceAction>action).vnfMembers;
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


export const calculateNextUniqueModelName = (vnfGroupModelName: string, serviceId: string, state: any, levelName: string): string => {
  let counter: number = null;
  while (true) {
    let pattern = !_.isNil(counter) ? ("_" + counter) : "";
    if (!_.isNil(state.serviceInstance[serviceId][levelName][vnfGroupModelName + pattern])) {
      counter = counter ? (counter + 1) : 1;
    } else {
      return vnfGroupModelName + pattern;
    }
  }
};





