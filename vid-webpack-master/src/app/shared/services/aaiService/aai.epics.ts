import {Injectable} from '@angular/core';
import {combineEpics, ofType} from "redux-observable-es6-compat";
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/startWith';
import {
  LOAD_PRODUCT_FAMILIES,
  LOAD_LCP_TENANT,
  LOAD_AIC_ZONES,
  LOAD_CATEGORY_PARAMETERS,
  LOAD_SERVICE_MDOEL_BY_UUID,
  LOAD_NETWORK_ACCORDING_TO_NF,
  LOAD_USER_ID
} from "./aai.actions";
import {AaiService} from "./aai.service";
import {AppState} from "../../store/reducers";
import {
  updateAicZones, updateCategoryParameters,
  updateLcpRegionsAndTenants,
  updateNetworkCollectionFunction,
  updateProductFamilies, updateUserId
} from "../../storeUtil/utils/general/general.actions";
import {createServiceInstance} from "../../storeUtil/utils/service/service.actions";
import {delay, map, mapTo, mergeMap} from "rxjs/operators";

const notFetchedAlready = (state: AppState): boolean => state.service.productFamilies !== null;

@Injectable()
export class AAIEpics {
  constructor(private aaiService: AaiService) {
  }

  public createEpic() {
    return combineEpics(
        this.loadProductFamiliesEpic
      , this.loadLcpTenants
      , this.loadAicZones
      , this.loadCategoryParameters
      , this.loadServiceAccordingToUuid
      , this.loadNetworkAccordingToNetworkFunction
      , this.loadUserId)
  }


  loadLcpTenants = action$ =>
    action$.pipe(  //fixed
      ofType(LOAD_LCP_TENANT),
      map((action :any) => {
        return this.aaiService.getLcpRegionsAndTenants(action.subscriberId, action.serviceType).map(data => updateLcpRegionsAndTenants(data))
      })
    );



   loadProductFamiliesEpic = action$ =>
    action$.pipe(  //fixed
      ofType(LOAD_PRODUCT_FAMILIES),
      map(() => {
        return this.aaiService.getProductFamilies().map(data => updateProductFamilies(data))
      })
    );

  loadCategoryParameters = action$ =>
    action$.pipe(  //fixed
      ofType(LOAD_CATEGORY_PARAMETERS),
      map((action :any) => {
        return this.aaiService.getCategoryParameters(null).map(data => updateCategoryParameters(data))
      })
    );


  loadNetworkAccordingToNetworkFunction = action$ =>
    action$.pipe(  //fixed
      ofType(LOAD_NETWORK_ACCORDING_TO_NF),
      map((action :any) => {
        return this.aaiService.getCRAccordingToNetworkFunctionId(action.networkFunctions, action.cloudOwner, action.cloudRegionId)
          .map((res) => updateNetworkCollectionFunction(action.networkFunctions, res))
      })
    );

  loadServiceAccordingToUuid = action$ =>
    action$.pipe(  //fixed
      ofType(LOAD_SERVICE_MDOEL_BY_UUID),
      map((action :any) => {
        return this.aaiService.getServiceModelById(action.modelId) .map(data => createServiceInstance(action.uuid, data))
      })
    );

  loadUserId = action$ =>
    action$.pipe(  //fixed
      ofType(LOAD_USER_ID),
      map(() => {
        return this.aaiService.getUserId().map(res => updateUserId(res))
      })
    );


  loadAicZones = action$ =>
    action$.pipe(  //fixed
      ofType(LOAD_AIC_ZONES),
      map(() => {
        return this.aaiService.getAicZones().map(data => updateAicZones(data))
      })
    );

}
