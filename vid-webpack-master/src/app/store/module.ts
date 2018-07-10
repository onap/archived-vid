import { NgModule } from '@angular/core';
import { NgReduxModule, NgRedux, DevToolsExtension } from '@angular-redux/store';
import { RootEpics } from './epics';

import rootReducer, {AppState} from "./reducers";
import {AAIEpics} from "../services/aaiService/aai.epics";

@NgModule({
  imports: [NgReduxModule],
  providers: [RootEpics, AAIEpics],
})

export class StoreModule {
  constructor(
    public store: NgRedux<AppState>,
    devTools: DevToolsExtension,
    rootEpics: RootEpics,
  ) {

    const persistedState = sessionStorage.getItem('reduxState') ?
      JSON.parse(sessionStorage.getItem('reduxState')) : {};

    store.configureStore(
      rootReducer,
      persistedState,
      rootEpics.createEpics(),
      devTools.isEnabled() ? [ devTools.enhancer() ] : []);
    }
}
