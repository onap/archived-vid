import {Component, Inject} from '@angular/core';

import { ApiService } from './shared';

import '../style/app.scss';
import {NgRedux} from "@angular-redux/store";
import {AppState} from "./store/reducers";
import { LogService } from './shared/utils/log/log.service';

@Component({
  selector: 'vid-app', // <vid-app></vid-app>
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  url = 'https://github.com/preboot/angular2-webpack';
  title: string;


  constructor(private api: ApiService, private store: NgRedux<AppState>, private _logService : LogService) {
    this.title = this.api.title;
    store.subscribe(()=>{
      sessionStorage.setItem('reduxState', JSON.stringify(store.getState()));
    });

    this._logService.info("testing new log service");
  }
}
