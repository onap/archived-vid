import {Action, ActionCreator} from "redux";
import {VnfInstance} from "../../../models/vnfInstance";
import {ActionOnFirstLevel} from "../firstLevel/firstLevel.actions";

export enum VNFActions {
  CREATE_VNF_INSTANCE = "CREATE_VNF_INSTANCE",
  UPDATE_VNF_INSTANCE = "UPDATE_VNF_INSTANCE",
  REMOVE_VNF_INSTANCE = "REMOVE_VNF_INSTANCE",
  DELETE_ACTION_VNF_INSTANCE = "DELETE_VNF_INSTANCE",
  UNDO_DELETE_ACTION_VNF_INSTANCE = "UNDO_DELETE_VNF_INSTANCE",
  UPDATE_VNF_POSITION = "UPDATE_VNF_POISTION",
  UPGRADE_VNF_ACTION = "UPGRADE_VNF_ACTION",
  UNDO_UPGRADE_VNF_ACTION = "UNDO_UPGRADE_VNF_ACTION"
}

export enum VNFMethods{
  UPGRADE = "upgrade",
  UNDO_UPGRADE = "undoUpgrade"
}


export interface CreateVnfInstanceAction extends Action {
  vnfInstance?: VnfInstance;
  vnfModelName?: string;
  serviceUuid?: string;
  vnfStoreKey?:string;
}

export interface UpdateVnfPosition extends Action {
  node: any,
  instanceId : string,
  vnfStoreKey?: string;
}

export interface UpdateVnfInstanceAction extends Action {
  vnfInstance?: VnfInstance;
  vnfModelName?: string;
  serviceUuid?: string;
  vnfStoreKey?:string;
}

export interface UpgradeVnfAction extends Action {
  serviceUuid: string;
  vnfStoreKey:string;
}

export interface UndoUpgradeVnfAction extends Action {
  serviceUuid: string;
  vnfStoreKey:string;
}

export interface RemoveVnfInstanceAction extends Action {
  vnfStoreKey: string;
  serviceId?: string;
}

export const createVNFInstance: ActionCreator<CreateVnfInstanceAction> = (vnfInstance, vnfModelName, serviceUuid, vnfStoreKey) => ({
  type: VNFActions.CREATE_VNF_INSTANCE,
  vnfInstance: vnfInstance,
  vnfModelName: vnfModelName,
  serviceUuid: serviceUuid,
  vnfStoreKey : vnfStoreKey
});


export const updateVNFInstance: ActionCreator<UpdateVnfInstanceAction> = (vnfInstance, vnfModelName, serviceUuid, vnfStoreKey) => ({
  type: VNFActions.UPDATE_VNF_INSTANCE,
  vnfInstance: vnfInstance,
  vnfModelName: vnfModelName,
  serviceUuid: serviceUuid,
  vnfStoreKey : vnfStoreKey
});


export const deleteActionVnfInstance: ActionCreator<ActionOnFirstLevel> = (vnfStoreKey, serviceId) => ({
  type: VNFActions.DELETE_ACTION_VNF_INSTANCE,
  firstLevelName: 'vnfs',
  storeKey: vnfStoreKey,
  serviceId: serviceId
});

export const undoDeleteActionVnfInstance: ActionCreator<ActionOnFirstLevel> = (vnfStoreKey, serviceId) => ({
  type: VNFActions.UNDO_DELETE_ACTION_VNF_INSTANCE,
  firstLevelName: 'vnfs',
  storeKey: vnfStoreKey,
  serviceId: serviceId
});

export const removeVnfInstance: ActionCreator<RemoveVnfInstanceAction> = (vnfStoreKey, serviceId) => ({
  type: VNFActions.REMOVE_VNF_INSTANCE,
  vnfStoreKey: vnfStoreKey,
  serviceId: serviceId
});

export const updateVnfPosition: ActionCreator<UpdateVnfPosition> = (node, instanceId, vnfStoreKey) => ({
  type: VNFActions.UPDATE_VNF_POSITION,
  node: node,
  instanceId: instanceId,
  vnfStoreKey : vnfStoreKey
});

export const upgradeVnf: ActionCreator<UpgradeVnfAction> = (vnfStoreKey, serviceUuid) => ({
  type: VNFActions.UPGRADE_VNF_ACTION,
  serviceUuid,
  vnfStoreKey
});

export const undoUpgradeVnf: ActionCreator<UndoUpgradeVnfAction> = (vnfStoreKey, serviceUuid) => ({
  type: VNFActions.UNDO_UPGRADE_VNF_ACTION,
  serviceUuid,
  vnfStoreKey
});







