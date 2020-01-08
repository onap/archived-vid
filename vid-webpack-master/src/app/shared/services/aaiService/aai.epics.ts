import {Injectable} from '@angular/core';
import {combineEpics, createEpicMiddleware, ofType} from 'redux-observable';
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

  private loadLcpTenants = (action$, store) =>
   action$.ofType(LOAD_LCP_TENANT)
    .switchMap((action) => this.aaiService.getLcpRegionsAndTenants(action.subscriberId, action.serviceType)
    .map(data => updateLcpRegionsAndTenants(data)));

  private loadProductFamiliesEpic = (action$, store) => action$
    .ofType(LOAD_PRODUCT_FAMILIES)
    .switchMap(() =>
      this.aaiService.getProductFamilies()
        .map(data =>
          updateProductFamilies(data)
      ));

  private loadCategoryParameters = (action$, store) => action$
    .ofType(LOAD_CATEGORY_PARAMETERS)
    .switchMap(() => this.aaiService.getCategoryParameters(null).map(data => updateCategoryParameters(data)));


  private loadNetworkAccordingToNetworkFunction = (action$, store) => action$
    .ofType(LOAD_NETWORK_ACCORDING_TO_NF)
    .flatMap((action) => this.aaiService.getCRAccordingToNetworkFunctionId(action.networkFunctions, action.cloudOwner, action.cloudRegionId).map((res) =>
      updateNetworkCollectionFunction(action.networkFunctions, res)));

  private loadServiceAccordingToUuid = (action$, store) => action$
    .ofType(LOAD_SERVICE_MDOEL_BY_UUID)
    .switchMap((action) => this.aaiService.getServiceModelById(action.modelId)
      .map(data =>
        createServiceInstance(action.uuid, data))
    );

  private loadUserId = (action$, store) => action$
    .ofType(LOAD_USER_ID)
    .switchMap(() => this.aaiService.getUserId()
      .map(res => updateUserId(res)));


  private loadAicZones = (action$, store) => action$
    .ofType(LOAD_AIC_ZONES)
    .switchMap(() => this.aaiService.getAicZones().map(data => updateAicZones(data)));
  // .catch(response => of(this.actions.loadFailed(status)))
  // .startWith(this.actions.loadStarted()));

}
