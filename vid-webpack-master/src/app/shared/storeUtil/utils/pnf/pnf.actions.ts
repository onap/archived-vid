import {Action, ActionCreator} from "redux";
import {PnfInstance} from "../../../models/pnfInstance";
import {ActionOnFirstLevel} from "../firstLevel/firstLevel.actions";

export enum PNFActions {
  CREATE_PNF_INSTANCE = "CREATE_PNF_INSTANCE",
  UPDATE_PNF_INSTANCE = "UPDATE_PNF_INSTANCE",
  REMOVE_PNF_INSTANCE = "REMOVE_PNF_INSTANCE",
  DELETE_ACTION_PNF_INSTANCE = "DELETE_PNF_INSTANCE",
  UNDO_DELETE_ACTION_PNF_INSTANCE = "UNDO_DELETE_PNF_INSTANCE",
  UPDATE_PNF_POSITION = "UPDATE_PNF_POISTION"
}

export enum PNFMethods{
  UPGRADE = "upgrade",
  UNDO_UPGRADE = "undoUpgrade"
}


export interface CreatePnfInstanceAction extends Action {
  pnfInstance?: PnfInstance;
  pnfModelName?: string;
  serviceUuid?: string;
  pnfStoreKey?:string;
}

export interface UpdatePnfPosition extends Action {
  node: any,
  instanceId : string,
  pnfStoreKey?: string;
}

export interface UpdatePnfInstanceAction extends Action {
  pnfInstance?: PnfInstance;
  pnfModelName?: string;
  serviceUuid?: string;
  pnfStoreKey?:string;
}

export interface RemovePnfInstanceAction extends Action {
  pnfStoreKey: string;
  serviceId?: string;
}

export const createPNFInstance: ActionCreator<CreatePnfInstanceAction> = (pnfInstance, pnfModelName, serviceUuid, pnfStoreKey) => ({
  type: PNFActions.CREATE_PNF_INSTANCE,
  pnfInstance: pnfInstance,
  pnfModelName: pnfModelName,
  serviceUuid: serviceUuid,
  pnfStoreKey : pnfStoreKey
});


export const updatePNFInstance: ActionCreator<UpdatePnfInstanceAction> = (pnfInstance, pnfModelName, serviceUuid, pnfStoreKey) => ({
  type: PNFActions.UPDATE_PNF_INSTANCE,
  pnfInstance: pnfInstance,
  pnfModelName: pnfModelName,
  serviceUuid: serviceUuid,
  pnfStoreKey : pnfStoreKey
});


export const deleteActionPnfInstance: ActionCreator<ActionOnFirstLevel> = (pnfStoreKey, serviceId) => ({
  type: PNFActions.DELETE_ACTION_PNF_INSTANCE,
  firstLevelName: 'pnfs',
  storeKey: pnfStoreKey,
  serviceId: serviceId
});

export const undoDeleteActionPnfInstance: ActionCreator<ActionOnFirstLevel> = (pnfStoreKey, serviceId) => ({
  type: PNFActions.UNDO_DELETE_ACTION_PNF_INSTANCE,
  firstLevelName: 'pnfs',
  storeKey: pnfStoreKey,
  serviceId: serviceId
});

export const removePnfInstance: ActionCreator<RemovePnfInstanceAction> = (pnfStoreKey, serviceId) => ({
  type: PNFActions.REMOVE_PNF_INSTANCE,
  pnfStoreKey: pnfStoreKey,
  serviceId: serviceId
});

export const updatePnfPosition: ActionCreator<UpdatePnfPosition> = (node, instanceId, pnfStoreKey) => ({
  type: PNFActions.UPDATE_PNF_POSITION,
  node: node,
  instanceId: instanceId,
  pnfStoreKey : pnfStoreKey
});
