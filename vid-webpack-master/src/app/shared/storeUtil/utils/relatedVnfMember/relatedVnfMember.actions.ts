import {Action, ActionCreator} from "redux";

export enum RelatedVnfActions {
  CREATE_RELATED_VNF_MEMBER_INSTANCE = "CREATE_RELATED_VNF_MEMBER_INSTANCE",
  REMOVE_RELATED_VNF_MEMBER_INSTANCE = "REMOVE_RELATED_VNF_MEMBER_INSTANCE",
  DELETE_ACTION_RELATED_VNF_MEMBER_INSTANCE = "DELETE_ACTION_RELATED_VNF_MEMBER_INSTANCE",
  UNDO_DELETE_ACTION_RELATED_VNF_MEMBER_INSTANCE = "UNDO_DELETE_ACTION_RELATED_VNF_MEMBER_INSTANCE"
}

export interface RemoveRelatedVnfMemebrInstance extends Action {
  vnfGroupStoreKey: string;
  relatedVnfMemeberStoreKey: string;
  serviceId?: string;
}

export interface DeleteRelatedVnfMemebrInstanceAction extends Action {
  vnfGroupStoreKey: string;
  relatedVnfMemeberStoreKey: string;
  serviceId?: string;
}

export interface DeleteActionRelatedVnfMemeberInstanceAction extends Action {
  vnfGroupStoreKey: string;
  relatedVnfMemeberStoreKey: string;
  serviceId?: string;
}

export interface UndoDeleteActionRelatedVnfMemeberInstanceAction extends Action {
  vnfGroupStoreKey: string;
  relatedVnfMemeberStoreKey: string;
  serviceId?: string;
}

export interface CreateRelatedVnfMemeberInstanceAction extends Action {
  relatedVnfMember: any;
  vnfGroupStoreKey: string;
  serviceId?: string;
}


export const removeRelatedVnfMemberInstance: ActionCreator<RemoveRelatedVnfMemebrInstance> = (vnfGroupStoreKey, relatedVnfMemeberStoreKey, serviceId) => ({
  type: RelatedVnfActions.REMOVE_RELATED_VNF_MEMBER_INSTANCE,
  vnfGroupStoreKey: vnfGroupStoreKey,
  relatedVnfMemeberStoreKey: relatedVnfMemeberStoreKey,
  serviceId: serviceId
});

export const deleteActionRelatedVnfMemberInstance: ActionCreator<DeleteActionRelatedVnfMemeberInstanceAction> = (vnfGroupStoreKey, relatedVnfMemeberStoreKey, serviceId) => ({
  type: RelatedVnfActions.DELETE_ACTION_RELATED_VNF_MEMBER_INSTANCE,
  vnfGroupStoreKey: vnfGroupStoreKey,
  relatedVnfMemeberStoreKey: relatedVnfMemeberStoreKey,
  serviceId: serviceId
});

export const undoDeleteActionRelatedVnfMemberInstance: ActionCreator<UndoDeleteActionRelatedVnfMemeberInstanceAction> = (vnfGroupStoreKey, relatedVnfMemeberStoreKey, serviceId) => ({
  type: RelatedVnfActions.UNDO_DELETE_ACTION_RELATED_VNF_MEMBER_INSTANCE,
  vnfGroupStoreKey: vnfGroupStoreKey,
  relatedVnfMemeberStoreKey: relatedVnfMemeberStoreKey,
  serviceId: serviceId
});


export const createRelatedVnfMemberInstance: ActionCreator<CreateRelatedVnfMemeberInstanceAction> = (vnfGroupStoreKey, serviceId, relatedVnfMember) => ({
  type: RelatedVnfActions.CREATE_RELATED_VNF_MEMBER_INSTANCE,
  relatedVnfMember: relatedVnfMember,
  vnfGroupStoreKey: vnfGroupStoreKey,
  serviceId: serviceId
});
