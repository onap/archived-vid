import {ActivatedRouteSnapshot, Resolve} from "@angular/router";
import {Injectable} from "@angular/core";
import {Observable, of} from "rxjs";
import {AaiService} from "../../services/aaiService/aai.service";
import {forkJoin} from "rxjs/observable/forkJoin";
import {AppState} from "../../store/reducers";
import {NgRedux} from "@angular-redux/store";
import {
  updateAppNameAction,
  updateSideMenuItemsAction,
  updateVersionAction
} from "../../storeUtil/utils/global/global.actions";
import * as _ from 'lodash';

@Injectable()
export class SideMenuResolver implements Resolve<Observable<boolean>> {

  constructor(private _aaiService: AaiService,
              private _store: NgRedux<AppState>) {
  }

  resolve(route: ActivatedRouteSnapshot): Observable<boolean> {
    let menuApi = this._aaiService.getMenuItems();
    let versionApi = this._aaiService.getVidVersion();
    let appNameApi = this._aaiService.getAppName();
    let streams: Observable<any>[] = [menuApi, versionApi, appNameApi];
    streams = streams.filter(stream => stream !== undefined);

    const menuItemsExist = !_.isNil(this._store.getState().global.sideMenuItems);
    const applicationVersionExist =  !_.isNil(this._store.getState().global.applicationVersion);
    const appNameExist =  !_.isNil(this._store.getState().global.appName);
    if (menuItemsExist && applicationVersionExist && appNameExist) return of(true);
    return forkJoin(streams).switchMap(([menuItems, version, appName]) => {
      this._store.dispatch(updateSideMenuItemsAction(menuItems));
      this._store.dispatch(updateVersionAction(version));
      this._store.dispatch(updateVersionAction(version));
      this._store.dispatch(updateAppNameAction(appName.data));
      return of(true);
    });
  }

}
