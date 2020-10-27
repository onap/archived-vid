import {NgModule} from '@angular/core';
import {NgReduxModule, NgRedux, DevToolsExtension} from '@angular-redux/store';
import {RootEpics} from './epics';

import rootReducer, {AppState} from "./reducers";
import {AAIEpics} from "../services/aaiService/aai.epics";
import {createEpicMiddleware} from "redux-observable";
import {AaiService} from "../services/aaiService/aai.service";
import {applyMiddleware, createStore} from "redux";
import { composeWithDevTools } from 'redux-devtools-extension';

@NgModule({
  imports: [NgReduxModule],
  providers: [RootEpics, AAIEpics],
})

export class StoreModule {
  constructor(
    public store: NgRedux<AppState>,
    private aaiService : AaiService,
    devTools: DevToolsExtension,
    rootEpics: RootEpics,
  ) {
    const epicMiddleware = createEpicMiddleware();
    const persistedState = sessionStorage.getItem('reduxState') ?
      JSON.parse(sessionStorage.getItem('reduxState')) : {};

    const composeEnhancers = composeWithDevTools({});

    const configStore = createStore(
      rootReducer,
      <any>persistedState,
      composeEnhancers(
        applyMiddleware(epicMiddleware)
      )
    );

    epicMiddleware.run(rootEpics.createEpics());
    store.provideStore(configStore);
  }
}
