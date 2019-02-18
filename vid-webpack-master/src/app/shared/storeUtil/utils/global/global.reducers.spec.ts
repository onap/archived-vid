import {GlobalActions, UpdateFlagsAction, UpdateGlobalAction, UpdateDrawingBoardStatusAction} from "./global.actions";
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
});



