import {Action, ActionCreator} from "redux";
import {ActionOnFirstLevel} from "../firstLevel/firstLevel.actions";

export enum CrActions{
  DELETE_ACTION_CR_INSTANCE = "DELETE_ACTION_CR_INSTANCE",
  UNDO_DELETE_ACTION_CR_INSTANCE = "UNDO_DELETE_ACTION_CR_INSTANCE"
}

export const deleteActionCrInstance: ActionCreator<ActionOnFirstLevel> = (collectionResourceStoreKey, serviceId) => ({
  type: CrActions.DELETE_ACTION_CR_INSTANCE,
  firstLevelName: 'collectionResources',
  storeKey: collectionResourceStoreKey,
  serviceId: serviceId
});

export const undoDeleteActionCrInstance: ActionCreator<ActionOnFirstLevel> = (collectionResourceStoreKey, serviceId) => ({
  type: CrActions.UNDO_DELETE_ACTION_CR_INSTANCE,
  firstLevelName: 'collectionResources',
  storeKey: collectionResourceStoreKey,
  serviceId: serviceId
});
