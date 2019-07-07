import {Action, ActionCreator} from "redux";

export enum VrfActions {
  CREATE_VRF_INSTANCE = "CREATE_VRF_INSTANCE",
  EDIT_VRF_INSTANCE = "EDIT_VRF_INSTANCE",
  DELETE_VRF_INSTANCE = "DELETE_VRF_INSTANCE",
  VRF_ASSOCIATE_MEMBER = "VRF_ASSOCIATE_MEMBER",
  CLAER_ASSOCIATE__VRF_MEMBERS = "CLAER_ASSOCIATE__VRF_MEMBERS",
  DELETE_ACTION_VRF_INSTANCE = "DELETE_ACTION_VRF_INSTANCE",
  UNDO_DELETE_ACTION_VRF_INSTANCE = "UNDO_DELETE_ACTION_VRF_INSTANCE"
}


export interface CreateVRFInstanceAction extends Action {
  vrfModel : any;
  serviceModelId : string;
  vrfStoreKey : string;
}

export interface AssociateVRFMemberInstanceAction extends Action {
  vrfStoreKey : string;
  serviceModelId : string;
  memberType : string;
  member : any;
}

export interface ClearAssociateVRFMemberInstanceAction extends Action {
  vrfStoreKey : string;
  serviceModelId : string;
  memberType : string;
}

export interface DeleteActionVrfInstanceAction extends Action {
  vrfStoreKey : string;
  serviceId : string;
}

export interface UndoDeleteActionVrfInstanceAction extends Action {
  vrfStoreKey: string;
  serviceId?: string;
}

export const createVrfInstance: ActionCreator<CreateVRFInstanceAction> = (vrfModel, serviceModelId, vrfStoreKey) => ({
  type: VrfActions.CREATE_VRF_INSTANCE,
  vrfModel : vrfModel,
  serviceModelId : serviceModelId,
  vrfStoreKey : vrfStoreKey
});


export const associateVRFMember: ActionCreator<AssociateVRFMemberInstanceAction> = (vrfStoreKey, serviceModelId, member, memberType) => ({
  type: VrfActions.VRF_ASSOCIATE_MEMBER,
  vrfStoreKey : vrfStoreKey,
  serviceModelId : serviceModelId,
  member : member,
  memberType : memberType
});

export const clearAssociateVRFMemberInstance: ActionCreator<ClearAssociateVRFMemberInstanceAction> = (vrfStoreKey, serviceModelId, memberType) => ({
  type: VrfActions.CLAER_ASSOCIATE__VRF_MEMBERS,
  vrfStoreKey : vrfStoreKey,
  serviceModelId : serviceModelId,
  memberType : memberType
});

export const deleteActionVrfInstance: ActionCreator<DeleteActionVrfInstanceAction> = (vrfStoreKey, serviceId) => ({
  type: VrfActions.DELETE_ACTION_VRF_INSTANCE,
  vrfStoreKey : vrfStoreKey,
  serviceId : serviceId
});

export const undoDeleteActionVrfInstance: ActionCreator<UndoDeleteActionVrfInstanceAction> = (vrfStoreKey, serviceId) => ({
  type: VrfActions.UNDO_DELETE_ACTION_VRF_INSTANCE,
  vrfStoreKey: vrfStoreKey,
  serviceId: serviceId
});








