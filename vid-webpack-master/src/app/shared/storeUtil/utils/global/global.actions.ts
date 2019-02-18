import {Action, ActionCreator} from "redux";

export enum GlobalActions {
  UPDATE_NAME = 'UPDATE_NAME',
  UPDATE_FLAGS = 'UPDATE_FLAGS',
  UPDATE_DRAWING_BOARD_STATUS = 'UPDATE_DRAWING_BOARD_STATUS'
}

export interface UpdateGlobalAction extends Action {
  name?: string;
}

export interface UpdateFlagsAction extends Action {
  flags?: any;
}

export interface UpdateDrawingBoardStatusAction extends Action{
  drawingBoardStatus?: any;
}

export const updateName: ActionCreator<UpdateGlobalAction> =
  (name) => ({
    type: GlobalActions.UPDATE_NAME,
    name: name
  });

export const updateFlags: ActionCreator<UpdateFlagsAction> =
  (flags) => ({
    type: GlobalActions.UPDATE_FLAGS,
    flags: flags
  });

export const updateDrawingBoardStatus: ActionCreator<UpdateDrawingBoardStatusAction> =
  (drawingBoardStatus) => ({
    type: GlobalActions.UPDATE_DRAWING_BOARD_STATUS,
    drawingBoardStatus: drawingBoardStatus
  });
