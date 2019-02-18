import {MainReducer, ServiceState} from "../storeUtil/utils/main.reducer";
import {globalReducer, GlobalState} from "../storeUtil/utils/global/global.reducers";
import {combineReducers, Reducer} from "redux";


export interface AppState {
  global: GlobalState;
  service: ServiceState;
}

const rootReducer: Reducer<AppState> = combineReducers<AppState>({
  global: globalReducer,
  service: MainReducer
});

export default rootReducer;
