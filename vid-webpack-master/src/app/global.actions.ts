import {Action, ActionCreator} from "redux";
export const UPDATE_NAME= '[NAME] Update';
export const UPDATE_FLAGS= '[FLAGS] Update';

export interface UpdateGlobalAction extends Action {
  name?: string;
}

export interface UpdateFlagsAction extends Action {
  flags?: any;
}

export const updateName: ActionCreator<UpdateGlobalAction> =
  (name) => ({
    type: UPDATE_NAME,
    name: name
  });

export const updateFlags: ActionCreator<UpdateFlagsAction> =
  (flags) => ({
    type: UPDATE_FLAGS,
    flags: flags
  });
