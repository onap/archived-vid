import {
  GlobalActions,
  UpdateFlagsAction,
  UpdateGlobalAction,
  UpdateDrawingBoardStatusAction,
  UpdateGenericModalCriteria,
  UpdateGenericModalHelper,
  DeleteGenericModalHelper,
  ClearGenericModalHelper,
  UpdateGenericModalTableDataHelper, UpdateCurrentModalModeAction, UpdateAppNameAction, UpdateApplicationVersionAction
} from "./global.actions";
import {globalReducer} from "./global.reducers";


describe('globalReducer', () => {

  test('#UPDATE_FLAGS : should update global flags', () => {
    const flags = {
      'A' : 'A',
      'B' : 'B',
      'C' : 'C',
      'D' : 'D'
    };
    let flagsState = globalReducer(<any>{global : {}},
      <UpdateFlagsAction>{
        type: GlobalActions.UPDATE_FLAGS,
        flags : flags
      }).flags;

    expect(flagsState).toBeDefined();
    expect(<any>flagsState).toEqual(<any>flags);
  });

  test('#UPDATE_NAME : should update global name', () => {
    const name = 'name';
    let globalState = globalReducer(<any>{global : {}},
      <UpdateGlobalAction>{
        type: GlobalActions.UPDATE_NAME,
        name : name
      });
    expect(globalState).toBeDefined();
    expect(globalState.name).toEqual(name);
  });

  test('#UPDATE_DRAWING_BOARD_STATUS : should update global drawing board status',  ()=> {
    const drawingBoardStatus = 'EDIT';
    let globalDrawingBoardState = globalReducer(<any>{global : {}},
      <UpdateDrawingBoardStatusAction>{
        type: GlobalActions.UPDATE_DRAWING_BOARD_STATUS,
        drawingBoardStatus : drawingBoardStatus
      });
    expect(globalDrawingBoardState).toBeDefined();
    expect(globalDrawingBoardState.drawingBoardStatus).toEqual(drawingBoardStatus);
  });

  test('#UPDATE_GENERIC_MODAL_CRITERIA : should update updateGenericModalCriteria object',  ()=> {
    const values  = ["value1", "value2"];
    const fieldName = "someFieldName";
    let globalDrawingBoardState = globalReducer(<any>{global : {},genericModalCriteria : {}},
      <UpdateGenericModalCriteria>{
        type: GlobalActions.UPDATE_GENERIC_MODAL_CRITERIA,
        field : fieldName,
        values : values
      });
    expect(globalDrawingBoardState).toBeDefined();
    expect(globalDrawingBoardState.genericModalCriteria[fieldName]).toEqual(values);
  });

  test('#UPDATE_GENERIC_MODAL_HELPER : should update updateGenericModalHelper object',  ()=> {
    const uniqObjectField = "uniqObjectField";
    const values  = {name : "value1", uniqObjectField: uniqObjectField};
    const fieldName = "someFieldName";

    let globalDrawingBoardState = globalReducer(<any>{global : {},genericModalHelper : {}},
      <UpdateGenericModalHelper>{
        type: GlobalActions.UPDATE_GENERIC_MODAL_HELPER,
        field : fieldName,
        values : values,
        uniqObjectField : uniqObjectField
      });
    expect(globalDrawingBoardState).toBeDefined();
    expect(globalDrawingBoardState.genericModalHelper[fieldName][uniqObjectField]).toEqual({"name": "value1", "uniqObjectField": "uniqObjectField"});
  });

  test('#DELETE_GENERIC_MODAL_HELPER : should delete exist ',  ()=> {
    const uniqObjectField = "uniqObjectField";
    const fieldName = "someFieldName";

    let globalDrawingBoardState = globalReducer(<any>{global : {},genericModalHelper : {
          "someFieldName" : {
            "uniqObjectField" : true
          }
        }},
      <DeleteGenericModalHelper>{
        type: GlobalActions.DELETE_GENERIC_MODAL_HELPER,
        field : fieldName,
        uniqObjectField : uniqObjectField
      });
    expect(globalDrawingBoardState).toBeDefined();
    expect(globalDrawingBoardState.genericModalHelper[fieldName][uniqObjectField]).toBeUndefined();
  });

  test('#CLEAR_ALL_GENERIC_MODAL_HELPER : should clear  generic modal object',  ()=> {

    let globalDrawingBoardState = globalReducer(<any>{global : {},genericModalHelper : {
          "someFieldName" : {
            "uniqObjectField" : true
          }
        }},
      <ClearGenericModalHelper>{
        type: GlobalActions.CLEAR_ALL_GENERIC_MODAL_HELPER
      });
    expect(globalDrawingBoardState.genericModalHelper).toEqual({});
  });

  test('#UPDATE_GENERIC_MODAL_TABLE_DATA_HELPER : should update table data with some values',  ()=> {
    const keyName: string = 'VPN_DATA';
    const someValue: string = 'SOME_VALUE';

    let globalDrawingBoardState = globalReducer(<any>{global : {},genericModalHelper : {

        }},
      <UpdateGenericModalTableDataHelper>{
        type: GlobalActions.UPDATE_GENERIC_MODAL_TABLE_DATA_HELPER,
        field : keyName,
        values : someValue
      });
    expect(globalDrawingBoardState.genericModalHelper[keyName]).toEqual(someValue);
  });

  test('#DELETE_GENERIC_MODAL_TABLE_DATA_HELPER : should delete table data',  ()=> {
    const keyName: string = 'VPN_DATA';
    const someValue: string = 'SOME_VALUE';

    let globalDrawingBoardState = globalReducer(<any>{global : {},genericModalHelper : {
          [keyName] : someValue
        }},
      <UpdateGenericModalTableDataHelper>{
        type: GlobalActions.DELETE_GENERIC_MODAL_TABLE_DATA_HELPER,
        field : keyName
      });
    expect(globalDrawingBoardState.genericModalHelper[keyName]).toBeUndefined();
  });



  test('#UPDATE_CURRENT_MODAL_MODE : should update current modal mode: true',  ()=> {
    let globalDrawingBoardState = globalReducer(<any>{global : {},genericModalHelper : {
          isUpdateModalMode : null
        }},
      <UpdateCurrentModalModeAction>{
        type: GlobalActions.UPDATE_CURRENT_MODAL_MODE,
        isUpdateModalMode : true
      });
    expect(globalDrawingBoardState.isUpdateModalMode).toBeTruthy();
  });


  test('#UPDATE_CURRENT_MODAL_MODE : should update current modal mode: false',  ()=> {
    let globalDrawingBoardState = globalReducer(<any>{global : {},genericModalHelper : {
          isUpdateModalMode : true
        }},
      <UpdateCurrentModalModeAction>{
        type: GlobalActions.UPDATE_CURRENT_MODAL_MODE,
        isUpdateModalMode : false
      });
    expect(globalDrawingBoardState.isUpdateModalMode).toBeFalsy();
  });

  test('#UPDATE_CURRENT_MODAL_MODE : should delete modal mode if value is null',  ()=> {
    let globalDrawingBoardState = globalReducer(<any>{global : {},genericModalHelper : {
          isUpdateModalMode : true
        }},
      <UpdateCurrentModalModeAction>{
        type: GlobalActions.UPDATE_CURRENT_MODAL_MODE,
        isUpdateModalMode : null
      });
    expect(globalDrawingBoardState.isUpdateModalMode).toBeUndefined();
  });

  test('#UPDATE_APP_NAME : should update application name',  ()=> {
    const appName = 'appName';
    let globalDrawingBoardState = globalReducer(<any>{global : {}},
      <UpdateAppNameAction>{
        type: GlobalActions.UPDATE_APP_NAME,
        appName
      });
    expect(globalDrawingBoardState.appName).toEqual(appName);
  });

  test('#UPDATE_APPLICATION_VERSION : should update application version',  ()=> {
    const applicationVersion = {"features":"2002.features.properties","build":"1.0.5512","displayVersion":"2002.5512"};
    let globalDrawingBoardState = globalReducer(<any>{global : {}},
      <UpdateApplicationVersionAction>{
        type: GlobalActions.UPDATE_APPLICATION_VERSION,
        applicationVersion
      });
    expect(globalDrawingBoardState.applicationVersion).toEqual(applicationVersion);
  });


});



