import {ActivatedRouteSnapshot, Resolve} from "@angular/router";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AaiService} from "../../services/aaiService/aai.service";
import {forkJoin} from "rxjs/observable/forkJoin";
import {AppState} from "../../store/reducers";
import {NgRedux} from "@angular-redux/store";
import {createServiceInstance} from "../../storeUtil/utils/service/service.actions";

@Injectable()
export class ViewEditResolver implements Resolve<Observable<boolean>> {

  constructor(private _aaiService: AaiService, private _store: NgRedux<AppState>) {
  }

  resolve(route: ActivatedRouteSnapshot): Observable<boolean> {
    const serviceModeId: string = route.queryParamMap.get("serviceModelId");
    const serviceInstanceId: string = route.queryParamMap.get("serviceInstanceId");
    const subscriberId: string = route.queryParamMap.get("subscriberId");
    const serviceType: string = route.queryParamMap.get("serviceType");
      let serviceModelApi = this._aaiService.getServiceModelById(serviceModeId);
      let serviceInstanceApi = this._aaiService.retrieveAndStoreServiceInstanceTopology(serviceInstanceId, subscriberId, serviceType, serviceModeId);
      return forkJoin([serviceModelApi, serviceInstanceApi]).map(([serviceModel, serviceInstance ]) => {
        this.setIsALaCarte(serviceInstance,serviceModel.service.vidNotions.instantiationType );
        this.setTestApi(serviceInstance);
        this._store.dispatch(createServiceInstance( serviceInstance, serviceModeId));
          return true;
        });
  }

  setTestApi = (service: any) => {
    if (this._store.getState().global.flags['FLAG_ADD_MSO_TESTAPI_FIELD'] && service.isALaCarte) {
      service.testApi = sessionStorage.getItem("msoRequestParametersTestApiValue");
    }
  };
  setIsALaCarte = (service: any, instantiationType) => {
    service.isALaCarte = instantiationType === 'ALaCarte';
  };

}
