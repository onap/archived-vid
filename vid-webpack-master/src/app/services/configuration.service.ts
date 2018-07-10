import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Constants} from "../shared/utils/constants";
import {Observable} from 'rxjs/Observable';
import {updateFlags} from "../global.actions";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../store/reducers";

@Injectable()
export class ConfigurationService {
  store : NgRedux<AppState>;

  constructor(private _http: HttpClient, _store : NgRedux<AppState>) {
    this.store = _store;
  }

  getConfiguration(key : string): Observable<any> {
    let pathQuery = Constants.Path.CONFIGURATION_PATH;
    pathQuery = pathQuery.replace("{name}",key);
    return this._http.get(pathQuery).map(response => response);
  }

  getFlags(): Observable<{[key: string] : boolean}> {
    let flags = this.store.getState().global.flags;
    if (flags) {
      return Observable.of(flags);
    }
    let pathQuery = Constants.Path.FEATURES_FLAG_PATH;
    return this._http.get<{[key: string] : boolean}>(pathQuery).map(response => {
      this.store.dispatch(updateFlags(response));
      return response;
    });
  }
}
