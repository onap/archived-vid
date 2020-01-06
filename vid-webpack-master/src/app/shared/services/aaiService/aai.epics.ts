import {Injectable} from '@angular/core';
import {combineEpics, ofType} from 'redux-observable-es6-compat';
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
import {
  updateAicZones, updateCategoryParameters,
  updateLcpRegionsAndTenants,
  updateNetworkCollectionFunction,
  updateProductFamilies, updateUserId
} from "../../storeUtil/utils/general/general.actions";
import {createServiceInstance} from "../../storeUtil/utils/service/service.actions";
import {switchMap} from "rxjs/operators";


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


  loadLcpTenants = (action$) => {
    return action$.pipe(
      ofType(LOAD_LCP_TENANT),
      switchMap((action: { subscriberId: string, serviceType: string }) =>
        this.aaiService.getLcpRegionsAndTenants(action.subscriberId, action.serviceType).map(data => updateLcpRegionsAndTenants(data))
      )
    );
  };

  loadProductFamiliesEpic = (action$) => {
    return action$.pipe(
      ofType(LOAD_PRODUCT_FAMILIES),
      switchMap(() =>
        this.aaiService.getProductFamilies().map(data => updateProductFamilies(data))
      )
    );
  };


  loadCategoryParameters = (action$) => {
    return action$.pipe(
      ofType(LOAD_CATEGORY_PARAMETERS),
      switchMap((action: { subscriberId: string, serviceType: string }) =>
        this.aaiService.getCategoryParameters(null).map(data => updateCategoryParameters(data))
      )
    );
  };


  loadNetworkAccordingToNetworkFunction = (action$) => {
    return action$.pipe(
      ofType(LOAD_NETWORK_ACCORDING_TO_NF),
      switchMap((action: { networkFunctions: any, cloudOwner: any, cloudRegionId: any }) =>
        this.aaiService.getCRAccordingToNetworkFunctionId(action.networkFunctions, action.cloudOwner, action.cloudRegionId).map(data => updateNetworkCollectionFunction(action.networkFunctions, data))
      )
    );
  };

  loadServiceAccordingToUuid = (action$) => {
    return action$.pipe(
      ofType(LOAD_SERVICE_MDOEL_BY_UUID),
      switchMap((action: { modelId: any, uuid: string }) =>
        this.aaiService.getServiceModelById(action.modelId).map(data => createServiceInstance(action.uuid, data))
      )
    );
  };

  loadUserId = (action$) => {
    return action$.pipe(
      ofType(LOAD_USER_ID),
      switchMap((action: { modelId: any, uuid: string }) =>
        this.aaiService.getUserId().map(data => updateUserId(data))
      )
    );
  };

  loadAicZones = (action$) => {
    return action$.pipe(
      ofType(LOAD_AIC_ZONES),
      switchMap((action: { modelId: any, uuid: string }) =>
        this.aaiService.getAicZones().map(data => updateAicZones(data))
      )
    );
  };

}

