import {Action} from 'redux';
import {
  GlobalActions,
  UpdateFlagsAction,
  UpdateGlobalAction,
  UpdateDrawingBoardStatusAction,
  UpdateGenericModalCriteria,
  UpdateGenericModalHelper,
  DeleteGenericModalHelper,
  DeleteGenericModalTabelDataHelper,
  UpdateGenericModalTableDataHelper,
  UpdateCurrentModalModeAction,
  UpdateSideMenuItemsAction,
  UpdateApplicationVersionAction
} from "./global.actions";
import * as _ from "lodash";
import {VersionModel} from "../../../components/sideMenu/version.model";
import {SideMenuModel} from "../../../components/sideMenu/side-menu.model";

export interface GlobalState {
  name : string;
  flags : { [key: string]: boolean };
  drawingBoardStatus : string;
  genericModalCriteria : { [key: string]: any };
  genericModalHelper : { [key: string]: any };
  isUpdateModalMode?: boolean;
  applicationVersion?: VersionModel;
  sideMenuItems : SideMenuModel[]
}

const initialState: GlobalState = {
  name : null,
  flags : null,
  drawingBoardStatus : null,
  genericModalCriteria : {
    roles : []
  },
  genericModalHelper : {},
  isUpdateModalMode : null,
  applicationVersion : null,
  sideMenuItems : null
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
      case GlobalActions.UPDATE_GENERIC_MODAL_CRITERIA : {
        const updateGenericModalCriteria = <UpdateGenericModalCriteria>action;
        let newState = _.cloneDeep(state);
        if(_.isNil(newState.genericModalCriteria)){
          newState.genericModalCriteria = {};
        }
        newState.genericModalCriteria[updateGenericModalCriteria.field] = updateGenericModalCriteria.values;
        return newState;
      }
      case GlobalActions.UPDATE_GENERIC_MODAL_HELPER : {
        const updateGenericModalHelper= <UpdateGenericModalHelper>action;
        let newState = _.cloneDeep(state);
        if(_.isNil(newState.genericModalHelper) ){newState.genericModalHelper = {}; }
        if(_.isNil(newState.genericModalHelper[updateGenericModalHelper.field])){
          newState.genericModalHelper[updateGenericModalHelper.field] = {};
        }
        newState.genericModalHelper[updateGenericModalHelper.field][updateGenericModalHelper.values[updateGenericModalHelper.uniqObjectField]] = updateGenericModalHelper.values;
        return newState;
      }
      case GlobalActions.DELETE_GENERIC_MODAL_HELPER : {
        const deleteGenericModalHelper= <DeleteGenericModalHelper>action;
        let newState = _.cloneDeep(state);
        delete newState.genericModalHelper[deleteGenericModalHelper.field][deleteGenericModalHelper.uniqObjectField];
        return newState;
      }
      case GlobalActions.CLEAR_ALL_GENERIC_MODAL_HELPER : {
        let newState = _.cloneDeep(state);
        newState.genericModalHelper = {};
        return newState;
      }
      case GlobalActions.DELETE_GENERIC_MODAL_TABLE_DATA_HELPER : {
        let newState = _.cloneDeep(state);
        delete newState.genericModalHelper[(<DeleteGenericModalTabelDataHelper>action).field];
        return newState;
      }
      case GlobalActions.UPDATE_GENERIC_MODAL_TABLE_DATA_HELPER : {
        let newState = _.cloneDeep(state);
        newState.genericModalHelper[(<UpdateGenericModalTableDataHelper>action).field] = (<UpdateGenericModalTableDataHelper>action).values ;
        return newState;
      }
      case GlobalActions.UPDATE_CURRENT_MODAL_MODE : {
        let newState = {...state};
        if ((<UpdateCurrentModalModeAction>action).isUpdateModalMode === null){
          delete newState.isUpdateModalMode;
        } else {
          newState.isUpdateModalMode = (<UpdateCurrentModalModeAction>action).isUpdateModalMode;
        }
        return newState;
      }
      case GlobalActions.UPDATE_SIDE_MENU_ITEMS : {
        Object.assign(state, (<UpdateSideMenuItemsAction>action));
        return Object.assign({}, state);
      }
      case GlobalActions.UPDATE_APPLICATION_VERSION : {
        Object.assign(state, (<UpdateApplicationVersionAction>action));
        return Object.assign({}, state);
      }
      default:
        return state;
    }
  };
