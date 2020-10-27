import {Action} from "redux";
import {PnfInstance} from "../../../models/pnfInstance";
import {
  CreatePnfInstanceAction,
  RemovePnfInstanceAction,
  UpdatePnfInstanceAction, UpdatePnfPosition,
  PNFActions
} from "./pnf.actions";
import * as _ from "lodash";
import {ServiceState} from "../main.reducer";
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";
import {deleteFirstLevel, updateServiceValidationCounter, calculateNextUniqueModelName, updateUniqueNames} from "../reducersHelper";
import {ActionOnFirstLevel} from "../firstLevel/firstLevel.actions";

export function pnfReducer(state: ServiceState, action: Action): ServiceState {
  switch (action.type) {
    case PNFActions.CREATE_PNF_INSTANCE: {
      const updatePnfInstanceAction = <CreatePnfInstanceAction>action;
      const serviceUuid = updatePnfInstanceAction.serviceUuid;
      let pnfModelName = updatePnfInstanceAction.pnfModelName;
      let newState = _.cloneDeep(state);

      updatePnfInstanceAction.pnfInstance.originalName = pnfModelName;
      updatePnfInstanceAction.pnfModelName = calculateNextUniqueModelName(pnfModelName, serviceUuid, newState, 'pnfs');

      let pnfInstance: PnfInstance = newState.serviceInstance[serviceUuid].pnfs[pnfModelName];
      pnfInstance = new PnfInstance();
      updatePnfInstanceAction.pnfInstance.pnfStoreKey = updatePnfInstanceAction.pnfModelName;
      updatePnfInstanceAction.pnfInstance.originalName = pnfModelName;
      pnfInstance.originalName = updatePnfInstanceAction.pnfInstance.originalName;
      pnfInstance.pnfStoreKey = updatePnfInstanceAction.pnfInstance.pnfStoreKey;
      updateServiceValidationCounter(newState, pnfInstance['isMissingData'], updatePnfInstanceAction.pnfInstance['isMissingData'], serviceUuid);

      newState.serviceInstance[serviceUuid].pnfs[updatePnfInstanceAction.pnfModelName] = Object.assign(pnfInstance, updatePnfInstanceAction.pnfInstance);
      return newState;
    }

    case PNFActions.UPDATE_PNF_INSTANCE: {
      const updatePnfInstanceAction = <UpdatePnfInstanceAction>action;
      const serviceUuid = updatePnfInstanceAction.serviceUuid;
      let pnfStoreKey = updatePnfInstanceAction.pnfStoreKey;

      let newState = _.cloneDeep(state);
      let pnfInstance: PnfInstance = newState.serviceInstance[serviceUuid].pnfs[pnfStoreKey];
      let oldInstanceName = pnfInstance ? pnfInstance.instanceName : null;
      updateUniqueNames(oldInstanceName, updatePnfInstanceAction.pnfInstance.instanceName, newState.serviceInstance[serviceUuid]);

      pnfInstance = pnfInstance || new PnfInstance();
      updateServiceValidationCounter(newState, pnfInstance['isMissingData'], updatePnfInstanceAction.pnfInstance['isMissingData'], serviceUuid);

      newState.serviceInstance[serviceUuid].pnfs[pnfStoreKey] = Object.assign(pnfInstance, updatePnfInstanceAction.pnfInstance);
      return newState;
    }

    case PNFActions.DELETE_ACTION_PNF_INSTANCE : {
      return deleteFirstLevel(state, <ActionOnFirstLevel>action,true);
    }

    case PNFActions.UNDO_DELETE_ACTION_PNF_INSTANCE : {
      let newState = _.cloneDeep(state);
      let pnf = newState.serviceInstance[(<ActionOnFirstLevel>action).serviceId].pnfs[(<ActionOnFirstLevel>action).storeKey];
      let oldState = pnf.action;
      newState.serviceInstance[(<ActionOnFirstLevel>action).serviceId].pnfs[(<ActionOnFirstLevel>action).storeKey].action = (oldState.split('_')[0]) as ServiceInstanceActions;
      updateServiceValidationCounter(newState, pnf['isMissingData'], false,  (<ActionOnFirstLevel>action).serviceId);
      return newState;
    }

    case PNFActions.REMOVE_PNF_INSTANCE : {
      let newState = _.cloneDeep(state);
      let pnfInstance = newState.serviceInstance[(<RemovePnfInstanceAction>action).serviceId].pnfs[(<RemovePnfInstanceAction>action).pnfStoreKey];
      updateServiceValidationCounter(newState, pnfInstance['isMissingData'], false,  (<RemovePnfInstanceAction>action).serviceId);
      delete newState.serviceInstance[(<RemovePnfInstanceAction>action).serviceId].pnfs[(<RemovePnfInstanceAction>action).pnfStoreKey];
      return newState;
    }

    case PNFActions.UPDATE_PNF_POSITION : {
      let newState = _.cloneDeep(state);
      newState.serviceInstance[(<UpdatePnfPosition>action).instanceId]
        .pnfs[(<UpdatePnfPosition>action).pnfStoreKey]
        .position = (<UpdatePnfPosition>action).node.position;
      return newState;
    }
  }
}



