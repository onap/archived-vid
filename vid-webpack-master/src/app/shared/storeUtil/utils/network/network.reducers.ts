import {Action} from "redux";
import * as _ from "lodash";
import {NetworkInstance} from "../../../models/networkInstance";
import {
  CreateNetworkInstanceAction,
  NetworkActions,
  UpdateNetworkCollectionFunction,
  UpdateNetworkInstanceAction
} from "./network.actions";
import {ServiceInstance} from "../../../models/serviceInstance";
import {calculateNextUniqueModelName} from "../reducersHelper";
import {ServiceState} from "../main.reducer";
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";
import {deleteFirstLevel, updateServiceValidationCounter} from "../reducersHelper";
import {ActionOnFirstLevel} from "../firstLevel/firstLevel.actions";


export function networkReducer(state: ServiceState , action: Action) : ServiceState {
  switch (action.type) {
    case NetworkActions.CREATE_NETWORK_INSTANCE: {
      const updateNetworkInstanceAction = <CreateNetworkInstanceAction>action;
      const serviceUuid = updateNetworkInstanceAction.serviceUuid;
      let networkModelName = updateNetworkInstanceAction.networkModelName;


      let newState = _.cloneDeep(state);

      updateNetworkInstanceAction.networkInstance.originalName = networkModelName;
      updateNetworkInstanceAction.networkModelName = calculateNextUniqueModelName(networkModelName, serviceUuid, newState, 'networks');

      let networkInstance: NetworkInstance = newState.serviceInstance[serviceUuid].networks[networkModelName];
      networkInstance = new NetworkInstance();
      updateNetworkInstanceAction.networkInstance.networkStoreKey = updateNetworkInstanceAction.networkModelName;
      updateNetworkInstanceAction.networkInstance.originalName = networkModelName;
      networkInstance.originalName = updateNetworkInstanceAction.networkInstance.originalName;
      networkInstance.networkStoreKey = updateNetworkInstanceAction.networkInstance.networkStoreKey;
      updateServiceValidationCounter(newState, networkInstance['isMissingData'], updateNetworkInstanceAction.networkInstance['isMissingData'], serviceUuid);

      newState.serviceInstance[serviceUuid].networks[updateNetworkInstanceAction.networkModelName] = Object.assign(networkInstance, updateNetworkInstanceAction.networkInstance);
      return newState;
    }
    case NetworkActions.UPDATE_NETWORK_INSTANCE: {
      const updateNetworkInstanceAction = <UpdateNetworkInstanceAction>action;
      const serviceUuid = updateNetworkInstanceAction.serviceUuid;
      let networkStoreKey = updateNetworkInstanceAction.networkStoreKey;

      let newState = _.cloneDeep(state);
      let networkInstance: NetworkInstance = newState.serviceInstance[serviceUuid].networks[networkStoreKey];
      updateUniqueNames(networkInstance? networkInstance.instanceName : null, updateNetworkInstanceAction.networkInstance.instanceName, newState.serviceInstance[serviceUuid]);

      networkInstance = networkInstance || new NetworkInstance();
      updateServiceValidationCounter(newState, networkInstance['isMissingData'], updateNetworkInstanceAction.networkInstance['isMissingData'], serviceUuid);

      newState.serviceInstance[serviceUuid].networks[networkStoreKey] = Object.assign(networkInstance, updateNetworkInstanceAction.networkInstance);
      return newState;
    }
    case NetworkActions.UPDATE_NETWORK_FUNCTION: {
      let networkFunctionReduxObj = state['networkFunctions'] == undefined ? {} : state['networkFunctions'];
      networkFunctionReduxObj[(<UpdateNetworkCollectionFunction>action).network_function] = (<UpdateNetworkCollectionFunction>action).networksAccordingToNetworkCollection;
      Object.assign(state, {'networkFunctions': networkFunctionReduxObj});
      return Object.assign({}, state);
    }

    case NetworkActions.DELETE_ACTION_NETWORK_INSTANCE : {
      return deleteFirstLevel(state, <ActionOnFirstLevel>action,true);

    }

    case NetworkActions.UNDO_DELETE_ACTION_NETWORK_INSTANCE : {
      let newState = _.cloneDeep(state);
      let network = newState.serviceInstance[(<ActionOnFirstLevel>action).serviceId].networks[(<ActionOnFirstLevel>action).storeKey];
      let oldState = network.action;
      newState.serviceInstance[(<ActionOnFirstLevel>action).serviceId].networks[(<ActionOnFirstLevel>action).storeKey].action = (oldState.split('_')[0]) as ServiceInstanceActions;
      updateServiceValidationCounter(newState, network['isMissingData'], false , (<ActionOnFirstLevel>action).serviceId);
      return newState;
    }
  }
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




