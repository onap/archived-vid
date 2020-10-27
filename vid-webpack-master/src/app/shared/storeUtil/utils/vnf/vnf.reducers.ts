import {Action} from "redux";
import {VnfInstance} from "../../../models/vnfInstance";
import {
  CreateVnfInstanceAction,
  RemoveVnfInstanceAction,
  UpdateVnfInstanceAction, UpdateVnfPosition, UpgradeVnfAction,
  VNFActions
} from "./vnf.actions";
import * as _ from "lodash";
import {ServiceState} from "../main.reducer";
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";
import {deleteFirstLevel, updateServiceValidationCounter, calculateNextUniqueModelName, updateUniqueNames} from "../reducersHelper";
import {ActionOnFirstLevel} from "../firstLevel/firstLevel.actions";

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
      return deleteFirstLevel(state, <ActionOnFirstLevel>action,true);
    }

    case VNFActions.UNDO_DELETE_ACTION_VNF_INSTANCE : {
      let newState = _.cloneDeep(state);
      let vnf = newState.serviceInstance[(<ActionOnFirstLevel>action).serviceId].vnfs[(<ActionOnFirstLevel>action).storeKey];
      let oldState = vnf.action;
      newState.serviceInstance[(<ActionOnFirstLevel>action).serviceId].vnfs[(<ActionOnFirstLevel>action).storeKey].action = (oldState.split('_')[0]) as ServiceInstanceActions;
      updateServiceValidationCounter(newState, vnf['isMissingData'], false,  (<ActionOnFirstLevel>action).serviceId);
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
      newState.serviceInstance[(<UpdateVnfPosition>action).instanceId]
        .vnfs[(<UpdateVnfPosition>action).vnfStoreKey]
        .position = (<UpdateVnfPosition>action).node.position;
      return newState;
    }

    case VNFActions.UPGRADE_VNF_ACTION: {
      let clonedState = _.cloneDeep(state);
      const castingAction = <UpgradeVnfAction>action;
      let oldAction = clonedState
        .serviceInstance[castingAction.serviceUuid]
        .vnfs[castingAction.vnfStoreKey].action;
      if(!oldAction.includes("Upgrade")) {
        clonedState.serviceInstance[castingAction.serviceUuid]
          .vnfs[castingAction.vnfStoreKey]
          .action = (`${oldAction}_Upgrade`) as ServiceInstanceActions;
      }

      if(_.isNil(clonedState.serviceInstance[castingAction.serviceUuid]
        .vnfs[castingAction.vnfStoreKey].upgradedVFMSonsCounter)) {
        clonedState.serviceInstance[castingAction.serviceUuid]
          .vnfs[castingAction.vnfStoreKey].upgradedVFMSonsCounter = 1;
        return clonedState;
      }
      clonedState.serviceInstance[castingAction.serviceUuid]
        .vnfs[castingAction.vnfStoreKey].upgradedVFMSonsCounter++;
      return clonedState;
    }

    case VNFActions.UNDO_UPGRADE_VNF_ACTION: {
      let clonedState = _.cloneDeep(state);
      const castingAction = <UpgradeVnfAction>action;
      if(clonedState.serviceInstance[castingAction.serviceUuid]
        .vnfs[castingAction.vnfStoreKey]
        .action.includes("Upgrade")) {
        clonedState
          .serviceInstance[castingAction.serviceUuid]
          .vnfs[castingAction.vnfStoreKey].upgradedVFMSonsCounter--;
        if(clonedState.serviceInstance[castingAction.serviceUuid].vnfs[castingAction.vnfStoreKey]
          .upgradedVFMSonsCounter === 0){
          clonedState.serviceInstance[castingAction.serviceUuid]
            .vnfs[castingAction.vnfStoreKey]
            .action = ServiceInstanceActions.None;
        }
      }
      return clonedState;
    }

  }
}
