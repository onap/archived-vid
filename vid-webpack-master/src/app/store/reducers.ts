import {Reducer, combineReducers} from 'redux';
import {GlobalReducer, GlobalState} from "../global.reducer";
import {ServiceReducer, ServiceState} from "../service.reducer";


export interface AppState {
  global: GlobalState;
  service: ServiceState;

}

const rootReducer: Reducer<AppState> = combineReducers<AppState>({
  global: GlobalReducer,
  service: ServiceReducer
});

export default rootReducer;
