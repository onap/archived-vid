/* tslint:disable no-switch-case-fall-through */
import {Action} from 'redux';
import {UPDATE_FLAGS, UPDATE_NAME, UpdateFlagsAction, UpdateGlobalAction} from "./global.actions";



export interface GlobalState {
  name : string;
  flags : { [key: string]: boolean };
}

const initialState: GlobalState = {
  name : null,
  flags : null
};


export const GlobalReducer =
  function (state: GlobalState = initialState, action: Action): GlobalState {
    switch (action.type) {
      case UPDATE_NAME:
        return Object.assign(state, state, (<UpdateGlobalAction>action));
      case UPDATE_FLAGS:
        Object.assign(state, (<UpdateFlagsAction>action));
        return Object.assign({}, state);
      default:
        return state;
    }
  };
