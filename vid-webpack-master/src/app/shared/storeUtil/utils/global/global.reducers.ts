import {Action} from 'redux';
import {GlobalActions, UpdateFlagsAction, UpdateGlobalAction, UpdateDrawingBoardStatusAction} from "./global.actions";

export interface GlobalState {
  name : string;
  flags : { [key: string]: boolean };
  drawingBoardStatus : string;
}

const initialState: GlobalState = {
  name : null,
  flags : null,
  drawingBoardStatus : null
};

export const globalReducer =
  function (state: GlobalState = initialState, action: Action): GlobalState {
    switch (action.type) {
      case GlobalActions.UPDATE_NAME:
        return Object.assign(state, state, (<UpdateGlobalAction>action));
      case GlobalActions.UPDATE_FLAGS:
        Object.assign(state, (<UpdateFlagsAction>action));
        return Object.assign({}, state);
      case GlobalActions.UPDATE_DRAWING_BOARD_STATUS:
        return Object.assign(state, state, (<UpdateDrawingBoardStatusAction>action));
      default:
        return state;
    }
  };
