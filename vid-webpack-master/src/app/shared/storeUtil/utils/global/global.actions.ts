import {Action, ActionCreator} from "redux";
import {SideMenuModel} from "../../../components/sideMenu/side-menu.model";
import {VersionModel} from "../../../components/sideMenu/version.model";

export enum GlobalActions {
  UPDATE_NAME = 'UPDATE_NAME',
  UPDATE_FLAGS = 'UPDATE_FLAGS',
  UPDATE_DRAWING_BOARD_STATUS = 'UPDATE_DRAWING_BOARD_STATUS',
  UPDATE_GENERIC_MODAL_CRITERIA = 'UPDATE_GENERIC_MODAL_CRITERIA',
  UPDATE_GENERIC_MODAL_HELPER = 'UPDATE_GENERIC_MODAL_HELPER',
  DELETE_GENERIC_MODAL_HELPER = 'DELETE_GENERIC_MODAL_HELPER',
  CLEAR_ALL_GENERIC_MODAL_HELPER = 'CLEAR_ALL_GENERIC_MODAL_HELPER',
  UPDATE_GENERIC_MODAL_TABLE_DATA_HELPER = 'UPDATE_GENERIC_MODAL_TABLE_DATA_HELPER',
  DELETE_GENERIC_MODAL_TABLE_DATA_HELPER = 'DELETE_GENERIC_MODAL_TABLE_DATA_HELPER',
  UPDATE_GENERIC_CURRNT_VRF_HELPER = 'UPDATE_GENERIC_CURRNT_VRF_HELPER',
  DELETE_GENERIC_CURRNT_VRF_HELPER = 'DELETE_GENERIC_CURRNT_VRF_HELPER',
  UPDATE_CURRENT_MODAL_MODE = 'UzPDATE_CURRENT_MODAL_MODE',
  UPDATE_SIDE_MENU_ITEMS = 'UPDATE_SIDE_MENU_ITEMS',
  UPDATE_APPLICATION_VERSION = 'UPDATE_APPLICATION_VERSION'
}

export interface UpdateSideMenuItemsAction extends Action {
  sideMenuItems: SideMenuModel[];
}

export interface UpdateApplicationVersionAction extends Action {
  applicationVersion: VersionModel;
}


export interface UpdateCurrentModalModeAction extends Action {
  isUpdateModalMode?: boolean;
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

export interface UpdateGenericModalCriteria extends Action {
  field: any;
  values: any;
}

export interface UpdateGenericModalTableDataHelper extends Action {
  field: any;
  values: any;
}
export interface UpdateGenericModalHelper extends Action {
  field: any;
  values: any;
  uniqObjectField : string;
}

export interface DeleteGenericModalHelper extends Action {
  field: any;
  uniqObjectField : string;
}

export interface DeleteGenericModalTabelDataHelper extends Action {
  field: any;
}


export interface ClearGenericModalHelper extends Action {}

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


export const updateGenericModalCriteria: ActionCreator<UpdateGenericModalCriteria> = (field, values) => ({
  type : GlobalActions.UPDATE_GENERIC_MODAL_CRITERIA,
  field: field,
  values: values
});

export const updateGenericModalhelper: ActionCreator<UpdateGenericModalHelper> = (field, values, uniqObjectField) => ({
  type : GlobalActions.UPDATE_GENERIC_MODAL_HELPER,
  field: field,
  values: values,
  uniqObjectField : uniqObjectField
});

export const deleteGenericModalhelper: ActionCreator<DeleteGenericModalHelper> = (field, uniqObjectField) => ({
  type : GlobalActions.DELETE_GENERIC_MODAL_HELPER,
  field: field,
  uniqObjectField : uniqObjectField
});


export const updateGenericModalTableDataHelper: ActionCreator<UpdateGenericModalTableDataHelper> = (field, values) => ({
  type : GlobalActions.UPDATE_GENERIC_MODAL_TABLE_DATA_HELPER,
  field: field,
  values: values,
});

export const deleteGenericModalTableDataHelper: ActionCreator<DeleteGenericModalTabelDataHelper> = (field) => ({
  type : GlobalActions.DELETE_GENERIC_MODAL_TABLE_DATA_HELPER,
  field: field,
});

export const clearAllGenericModalhelper: ActionCreator<ClearGenericModalHelper> = (field, uniqObjectField) => ({
  type : GlobalActions.CLEAR_ALL_GENERIC_MODAL_HELPER
});

export const updateCurrentModalModeAction: ActionCreator<UpdateCurrentModalModeAction> = (isUpdateModalMode? :boolean) => ({
  type : GlobalActions.UPDATE_CURRENT_MODAL_MODE,
  isUpdateModalMode
});

export const updateSideMenuItemsAction: ActionCreator<UpdateSideMenuItemsAction> = (sideMenuItems :SideMenuModel[]) => ({
  type : GlobalActions.UPDATE_SIDE_MENU_ITEMS,
  sideMenuItems
});

export const updateVersionAction: ActionCreator<UpdateApplicationVersionAction> = (applicationVersion : VersionModel) => ({
  type : GlobalActions.UPDATE_APPLICATION_VERSION,
  applicationVersion
});
