import {Action, ActionCreator} from "redux";

export enum NcfActions{
  DELETE_ACTION_NCF_INSTANCE = "DELETE_ACTION_NCF_INSTANCE",
  UNDO_DELETE_ACTION_NCF_INSTANCE = "UNDO_DELETE_ACTION_NCF_INSTANCE"

}
export interface DeleteActionNcfInstanceAction extends Action {
  collectionResourceStoreKey: string,
  ncfStoreKey : string;
  serviceId : string;
}

export interface UndoDeleteActionNcfInstanceAction extends Action {
  collectionResourceStoreKey: string,
  ncfStoreKey: string;
  serviceId?: string;
}


export const deleteActionNcfInstance: ActionCreator<DeleteActionNcfInstanceAction> = (collectionResourceStoreKey, ncfStoreKey, serviceId) => ({
  type: NcfActions.DELETE_ACTION_NCF_INSTANCE,
  collectionResourceStoreKey: collectionResourceStoreKey,
  ncfStoreKey: ncfStoreKey,
  serviceId: serviceId
});

export const undoDeleteActionNcfInstance: ActionCreator<UndoDeleteActionNcfInstanceAction> = (collectionResourceStoreKey, ncfStoreKey, serviceId) => ({
  type: NcfActions.UNDO_DELETE_ACTION_NCF_INSTANCE,
  collectionResourceStoreKey: collectionResourceStoreKey,
  ncfStoreKey: ncfStoreKey,
  serviceId: serviceId
});
