import {Action, ActionCreator} from "redux";
import {NetworkInstance} from "../../../models/networkInstance";

export enum NetworkActions {
  UPDATE_NETWORK_INSTANCE = "UPDATE_NETWORK_INSTANCE",
  UPDATE_NETWORK_FUNCTION  = 'UPDATE_NETWORK_FUNCTION',
  CREATE_NETWORK_INSTANCE = 'CREATE_NETWORK_INSTANCE',
  DELETE_ACTION_NETWORK_INSTANCE = "DELETE_ACTION_NETWORK_INSTANCE",
  UNDO_DELETE_ACTION_NETWORK_INSTANCE = "UNDO_DELETE_ACTION_NETWORK_INSTANCE",
  UPDATE_NETWORK_POSITION = "UPDATE_NETWORK_POSITION"

}


export interface UpdateNetworkPosition extends Action {
  node: any,
  instanceId : string,
  networkStoreKey?: string;
}

export interface UpdateNetworkInstanceAction extends Action {
  networkInstance?: NetworkInstance;
  networkModelName?: string;
  serviceUuid?: string;
  networkStoreKey?:string;
}

export interface UpdateNetworkCollectionFunction extends Action {
  networksAccordingToNetworkCollection: any;
  network_function: any;
}

export interface CreateNetworkInstanceAction extends Action {
  networkInstance?: NetworkInstance;
  networkModelName?: string;
  serviceUuid?: string;
  networkStoreKey?:string;
}

export interface DeleteActionNetworkInstanceAction extends Action {
  networkStoreKey: string;
  serviceId?: string;
}

export interface UndoDeleteActionNetworkInstanceAction extends Action {
  networkStoreKey: string;
  serviceId?: string;
}

export const updateNetworkInstance: ActionCreator<UpdateNetworkInstanceAction> = (networkInstance, networkfModelName, serviceUuid, networkStoreKey) => ({
  type: NetworkActions.UPDATE_NETWORK_INSTANCE,
  networkInstance: networkInstance,
  networkModelName: networkfModelName,
  serviceUuid: serviceUuid,
  networkStoreKey : networkStoreKey
});


export const updateNetworkCollectionFunction: ActionCreator<UpdateNetworkCollectionFunction> = (ncf, networksAccordingToNetworkCollection) => ({
  type: NetworkActions.UPDATE_NETWORK_FUNCTION,
  networksAccordingToNetworkCollection: networksAccordingToNetworkCollection["results"],
  network_function: ncf
});

export const createNetworkInstance: ActionCreator<CreateNetworkInstanceAction> = (networkInstance, networkModelName, serviceUuid, networkStoreKey) => ({
  type: NetworkActions.CREATE_NETWORK_INSTANCE,
  networkInstance: networkInstance,
  networkModelName: networkModelName,
  serviceUuid: serviceUuid,
  networkStoreKey : networkStoreKey
});


export const deleteActionNetworkInstance: ActionCreator<DeleteActionNetworkInstanceAction> = (networkStoreKey, serviceId) => ({
  type: NetworkActions.DELETE_ACTION_NETWORK_INSTANCE,
  networkStoreKey: networkStoreKey,
  serviceId: serviceId
});

export const undoDeleteActionNetworkInstance: ActionCreator<UndoDeleteActionNetworkInstanceAction> = (networkStoreKey, serviceId) => ({
  type: NetworkActions.UNDO_DELETE_ACTION_NETWORK_INSTANCE,
  networkStoreKey: networkStoreKey,
  serviceId: serviceId
});


export const updateNetworkPosition: ActionCreator<UpdateNetworkPosition> = (node, instanceId, networkStoreKey) => ({
  type: NetworkActions.UPDATE_NETWORK_POSITION,
  node: node,
  instanceId: instanceId,
  networkStoreKey : networkStoreKey
});


