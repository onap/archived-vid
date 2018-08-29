import {Action, ActionCreator} from "redux";
import {VnfGroupInstance} from "../../../models/vnfGroupInstance";
import {VnfMember} from "../../../models/VnfMember";

export enum VnfGroupActions {
  CREATE_VNF_GROUP_INSTANCE = "CREATE_VNF_GROUP_INSTANCE",
  UPDATE_VNF_GROUP_INSTANCE = "UPDATE_VNF_GROUP_INSTANCE",
  DELETE_ACTION_VNF_GROUP_INSTANCE = "DELETE_VNF_GROUP_INSTANCE",
  UNDO_DELETE_ACTION_VNF_GROUP_INSTANCE = "UNDO_DELETE_VNF_GROUP_INSTANCE",
  SET_OPTIONAL_MEMBERS_VNF_GROUP_INSTANCE = "SET_OPTIONAL_MEMBERS_VNF_GROUP_INSTANCE"
}


export interface CreateVnfGroupInstanceAction extends Action {
  vnfGroupInstance?: VnfGroupInstance;
  vnfGroupModelName?: string;
  serviceUuid?: string;
  vnfGroupStoreKey?:string;
}

export interface UpdateVnfGroupInstanceAction extends Action {
  vnfGroupInstance?: VnfGroupInstance;
  vnfGroupModelName?: string;
  serviceUuid?: string;
  vnfGroupStoreKey?:string;
}

export interface DeleteActionVnfGroupInstanceAction extends Action {
  vnfGroupStoreKey: string;
  serviceId?: string;
}

export interface UndoDeleteActionVnfGroupInstanceAction extends Action {
  vnfGroupStoreKey: string;
  serviceId?: string;
}

export interface SetOptionalMembersVnfGroupInstanceAction extends Action{
  path?: string;
  serviceId?: string;
  vnfMembers?: VnfMember[]
}

export const createVnfGroupInstance: ActionCreator<CreateVnfGroupInstanceAction> = (vnfGroupInstance, vnfGroupModelName, serviceUuid, vnfGroupStoreKey) => ({
  type: VnfGroupActions.CREATE_VNF_GROUP_INSTANCE,
  vnfGroupInstance: vnfGroupInstance,
  vnfGroupModelName: vnfGroupModelName,
  serviceUuid: serviceUuid,
  vnfGroupStoreKey : vnfGroupStoreKey
});


export const updateVnfGroupInstance: ActionCreator<UpdateVnfGroupInstanceAction> = (vnfGroupInstance, vnfGroupModelName, serviceUuid, vnfGroupStoreKey) => ({
  type: VnfGroupActions.UPDATE_VNF_GROUP_INSTANCE,
  vnfGroupInstance: vnfGroupInstance,
  vnfGroupModelName: vnfGroupModelName,
  serviceUuid: serviceUuid,
  vnfGroupStoreKey : vnfGroupStoreKey
});

export const deleteActionVnfGroupInstance: ActionCreator<DeleteActionVnfGroupInstanceAction> = (vnfGroupStoreKey, serviceId) => ({
  type: VnfGroupActions.DELETE_ACTION_VNF_GROUP_INSTANCE,
  vnfGroupStoreKey: vnfGroupStoreKey,
  serviceId: serviceId
});

export const undoDeleteActionVnfGroupInstance: ActionCreator<UndoDeleteActionVnfGroupInstanceAction> = (vnfGroupStoreKey, serviceId) => ({
  type: VnfGroupActions.UNDO_DELETE_ACTION_VNF_GROUP_INSTANCE,
  vnfGroupStoreKey: vnfGroupStoreKey,
  serviceId: serviceId
});

export const setOptionalMembersVnfGroupInstance: ActionCreator<SetOptionalMembersVnfGroupInstanceAction> = ( serviceId: string, path: string, vnfMembers: VnfMember[]) => ({
  type: VnfGroupActions.SET_OPTIONAL_MEMBERS_VNF_GROUP_INSTANCE,
  path: path,
  serviceId: serviceId,
  vnfMembers: vnfMembers
});




