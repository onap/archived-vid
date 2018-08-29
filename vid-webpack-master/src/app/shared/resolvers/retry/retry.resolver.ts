import {ActivatedRouteSnapshot, Resolve} from "@angular/router";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AaiService} from "../../services/aaiService/aai.service";
import {forkJoin} from "rxjs/observable/forkJoin";
import {AppState} from "../../store/reducers";
import {NgRedux} from "@angular-redux/store";
import {createServiceInstance} from "../../storeUtil/utils/service/service.actions";

@Injectable()
export class RetryResolver implements Resolve<Observable<boolean>> {

  constructor(private _aaiService: AaiService, private _store: NgRedux<AppState>) {}

  resolve(route: ActivatedRouteSnapshot): Observable<boolean> {
    const serviceModelId: string = route.queryParamMap.get("serviceModelId");
    const jobId: string = route.queryParamMap.get("jobId");

    let serviceModelApi = this._aaiService.getServiceModelById(serviceModelId);

    let serviceInstanceApi = this._aaiService.retrieveAndStoreServiceInstanceRetryTopology(jobId, serviceModelId);
    return forkJoin([serviceModelApi, serviceInstanceApi]).map(([serviceModel, serviceInstance ]) => {
      this._store.dispatch(createServiceInstance( serviceInstance, serviceModelId));
      return true;
    });
  }
}
