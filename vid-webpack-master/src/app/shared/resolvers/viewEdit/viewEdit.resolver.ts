import {ActivatedRouteSnapshot, Resolve} from "@angular/router";
import {Injectable} from "@angular/core";
import {from, Observable, of} from "rxjs";
import {AaiService} from "../../services/aaiService/aai.service";
import {forkJoin} from "rxjs/observable/forkJoin";
import {AppState} from "../../store/reducers";
import {NgRedux} from "@angular-redux/store";
import {createServiceInstance} from "../../storeUtil/utils/service/service.actions";
import {ServiceInstance} from "../../models/serviceInstance";
import * as _ from "lodash";
import {ModelInfo} from "../../models/modelInfo";
import {FeatureFlagsService, Features} from "../../services/featureFlag/feature-flags.service";
import {Utils} from "../../utils/utils";

@Injectable()
export class ViewEditResolver implements Resolve<Observable<boolean>> {

  constructor(private _aaiService: AaiService,
              private featureFlagsService:FeatureFlagsService,
              private _store: NgRedux<AppState>) {
  }

  resolve(route: ActivatedRouteSnapshot): Observable<boolean> {
    const serviceModelId: string = route.queryParamMap.get("serviceModelId");
    const serviceInstanceId: string = route.queryParamMap.get("serviceInstanceId");
    const subscriberId: string = route.queryParamMap.get("subscriberId");
    const serviceType: string = route.queryParamMap.get("serviceType");
    let serviceModelApi = this._aaiService.getServiceModelById(serviceModelId);
    let serviceInstanceApi = this._aaiService.retrieveAndStoreServiceInstanceTopology(serviceInstanceId, subscriberId, serviceType, serviceModelId);
    let streams: Observable<any>[] = [serviceModelApi, serviceInstanceApi];
    streams = streams.filter( stream => stream !== undefined);
    return forkJoin(streams).switchMap(([serviceModel, serviceInstance]) => {
      if (this.featureFlagsService.getFlagState(Features.FLAG_FLASH_REPLACE_VF_MODULE)) {
        return from(this.retrieveLatestVersionAndSetServiceInstance(serviceInstance.modelInfo.modelInvariantId).then((response) => {
          this.setServiceLatestAvailableVersion(serviceInstance, response);
          this.applyRequestsResponsesToStateAndInitServiceInstance(serviceModelId, serviceInstance, serviceModel);
          return true;
        }));
      }
      else {
        return of(true);
      }
    });
  }

  private retrieveLatestVersionAndSetServiceInstance(modelInvariantId: string) :Promise<ModelInfo>{
    return this._aaiService.retrieveServiceLatestUpdateableVersion(modelInvariantId).toPromise();
  }

  applyRequestsResponsesToStateAndInitServiceInstance(serviceModelId: string,
                                                      serviceInstance, serviceModel) {
    this.setIsALaCarte(serviceInstance, serviceModel.service.vidNotions.instantiationType);
    this.setTestApi(serviceInstance);
    this._store.dispatch(createServiceInstance(serviceInstance, serviceModelId));
  }

  setServiceLatestAvailableVersion(serviceInstance :ServiceInstance, modelInfoObject: ModelInfo) :void{
    if(!_.isNil(modelInfoObject) && !_.isNil(modelInfoObject.modelVersion)){
      serviceInstance.latestAvailableVersion = Number(modelInfoObject.modelVersion);
    }
    else {
      serviceInstance.latestAvailableVersion = 0;
    }
  }

  setTestApi(service: any) :void{
    if (this._store.getState().global.flags['FLAG_ADD_MSO_TESTAPI_FIELD'] && service.isALaCarte) {
      service.testApi = sessionStorage.getItem("msoRequestParametersTestApiValue");
    }
  };

  setIsALaCarte(service: any, instantiationType) :void{
    service.isALaCarte = Utils.isALaCarte(instantiationType);
  };

}
