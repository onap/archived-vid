import {ActivatedRouteSnapshot, Resolve} from "@angular/router";
import {Injectable} from "@angular/core";
import {Observable, of} from "rxjs";
import {AaiService} from "../../services/aaiService/aai.service";
import {forkJoin} from "rxjs/observable/forkJoin";
import {AppState} from "../../store/reducers";
import {NgRedux} from "@angular-redux/store";
import {updateSideMenuItemsAction, updateVersionAction} from "../../storeUtil/utils/global/global.actions";
import * as _ from 'lodash';

@Injectable()
export class SideMenuResolver implements Resolve<Observable<boolean>> {

  constructor(private _aaiService: AaiService,
              private _store: NgRedux<AppState>) {
  }

  resolve(route: ActivatedRouteSnapshot): Observable<boolean> {
    let menuApi = this._aaiService.getMenuItems();
    let versionApi = this._aaiService.getVidVersion();
    let streams: Observable<any>[] = [menuApi, versionApi];
    streams = streams.filter(stream => stream !== undefined);

    const menuItemsExist = !_.isNil(this._store.getState().global) && !_.isNil(this._store.getState().global.sideMenuItems);
    const applicationVersionExist = !_.isNil(this._store.getState().global.applicationVersion) && !_.isNil(this._store.getState().global.applicationVersion);
    if (menuItemsExist && applicationVersionExist) return of(true);
    return forkJoin(streams).switchMap(([menuItems, version]) => {
      this._store.dispatch(updateSideMenuItemsAction(menuItems));
      this._store.dispatch(updateVersionAction(version));
      return of(true);
    });
  }

}
