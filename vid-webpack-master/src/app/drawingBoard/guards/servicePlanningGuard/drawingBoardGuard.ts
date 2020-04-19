import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {NgRedux} from "@angular-redux/store";
import {Observable, of} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {AppState} from "../../../shared/store/reducers";
import {updateDrawingBoardStatus} from "../../../shared/storeUtil/utils/global/global.actions";
import {DrawingBoardModes} from "../../service-planning/drawing-board.modes";

@Injectable()
export class DrawingBoardGuard implements CanActivate {
  constructor(private store: NgRedux<AppState>, private _http: HttpClient, private _router : Router) { }
  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
      let url : string = `../../roles/service_permissions?subscriberId=${next.queryParams['subscriberId']}&serviceType=${next.queryParams['serviceType']}`;
      const viewMode = state.url.includes(DrawingBoardModes.RETRY_EDIT)? DrawingBoardModes.RETRY: DrawingBoardModes.VIEW;
      return this._http.get(url)
        .map((result : DrawingBoardPermissions) => {
          if(result.isEditPermitted){
            this.navigateToNewViewEdit(next.queryParams, viewMode);
            return false;
          }else {
            return true;
          }
        }).catch(err => {
          this.navigateToNewViewEdit(next.queryParams, viewMode);
          return of(false);
        });
  }

  navigateToNewViewEdit(queryParams: any, mode: DrawingBoardModes): void{
    this.store.dispatch(updateDrawingBoardStatus(mode));
    const viewEditUrlTree = this.getNewViewEditUrlTree(queryParams, mode);
    this._router.navigateByUrl(viewEditUrlTree);
    window.parent.location.assign('../../serviceModels.htm#'+viewEditUrlTree.toString());
  }

  getNewViewEditUrlTree(queryParams: any, mode: DrawingBoardModes) {
    return this._router.createUrlTree(
      ['/servicePlanning/' +  mode],
      {
        queryParams:
          {
            serviceModelId: queryParams['serviceModelId'],
            serviceInstanceId: queryParams['serviceInstanceId'],
            serviceType : queryParams['serviceType'],
            subscriberId : queryParams['subscriberId'],
            jobId : queryParams['jobId'],
          }
      });
  }
}

export class DrawingBoardPermissions {
  isEditPermitted : boolean
}
