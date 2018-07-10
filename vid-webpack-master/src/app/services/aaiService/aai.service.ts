import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Constants } from '../../shared/utils/constants';
import { ServiceType } from "../../shared/models/serviceType";
import {GetSubDetailsResponse} from "./responseInterfaces/getSubDetailsResponseInterface";
import {Observable} from "rxjs/Observable";
import * as _ from 'lodash';
import {CategoryParams} from "../../shared/models/categoryParams";
import {GetCategoryParamsResponseInterface} from "./responseInterfaces/getCategoryParamsResponseInterface";
import {Project} from "../../shared/models/project";
import {OwningEntity} from "../../shared/models/owningEntity";
import {GetServicesResponseInterface} from "./responseInterfaces/getServicesResponseInterface";
import {Subscriber} from "../../shared/models/subscriber";
import {GetSubscribersResponse} from "./responseInterfaces/getSubscribersResponseInterface";
import {AicZone} from "../../shared/models/aicZone";
import {GetAicZonesResponse} from "./responseInterfaces/getAicZonesResponseInterface";
import {LcpRegionsAndTenants} from "../../shared/models/lcpRegionsAndTenants";
import {LcpRegion} from "../../shared/models/lcpRegion";
import {Tenant} from "../../shared/models/tenant";
import {ProductFamily} from "../../shared/models/productFamily"
import {
  updateAicZones, updateCategoryParameters, updateLcpRegionsAndTenants, updateModel, updateProductFamilies,
  updateServiceTypes, updateSubscribers, updateUserId
} from '../../service.actions';
import {SelectOption} from '../../shared/models/selectOption';
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../store/reducers";
import {ResponseContentType, ResponseType} from "@angular/http";
import 'rxjs/add/operator/do';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/catch';

@Injectable()
export class AaiService {

  constructor (private http: HttpClient, private store: NgRedux<AppState>) {}

  public getServiceModelById(serviceModelId: string): Observable<any> {
    if (_.has(this.store.getState().service.serviceHierarchy,serviceModelId)){
      return Observable.of(<any> JSON.parse(JSON.stringify(this.store.getState().service.serviceHierarchy[serviceModelId])));
    }
    let pathQuery: string = Constants.Path.SERVICES_PATH + serviceModelId;
    return this.http.get(pathQuery).map(res => res )
      .do((res) => {
        this.store.dispatch(updateModel(res));
      });
  }

  public getUserId() : Observable<any>{
    return this.http.get("../../getuserID",{responseType: 'text'}).do((res)=>this.store.dispatch(updateUserId(res)));
  }


  public getCRAccordingToNetworkFunctionId(networkCollectionFunction,cloudOwner,cloudRegionId){
    return this.http.get('../../aai_get_instance_groups_by_cloudregion/'+cloudOwner+'/'+cloudRegionId+'/' + networkCollectionFunction)
    .map(res=>res).do((res)=>console.log(res));
  }

  public getCategoryParameters(familyName): Observable<CategoryParams> {
    familyName = familyName || Constants.Path.PARAMETER_STANDARDIZATION_FAMILY;
    let pathQuery: string = Constants.Path.GET_CATEGORY_PARAMETERS +"?familyName=" + familyName+ "&r=" + Math.random();

    return this.http.get<GetCategoryParamsResponseInterface>(pathQuery)
      .map(this.categoryParametersResponseToProductAndOwningEntity)
      .do(res => {
        this.store.dispatch(updateCategoryParameters(res))
      });
  }



    categoryParametersResponseToProductAndOwningEntity(res: GetCategoryParamsResponseInterface): CategoryParams  {
    if (res && res.categoryParameters) {
      const owningEntityList = res.categoryParameters.owningEntity.map(owningEntity => new OwningEntity(owningEntity));
      const projectList = res.categoryParameters.project.map(project => new Project(project));
      const lineOfBusinessList = res.categoryParameters.lineOfBusiness.map(owningEntity => new SelectOption(owningEntity));
      const platformList = res.categoryParameters.platform.map(platform => new SelectOption(platform));

      return new CategoryParams(owningEntityList, projectList, lineOfBusinessList, platformList);
    } else {
      return new CategoryParams();
    }
  }

  public getProductFamilies(): Observable<ProductFamily[]> {
    return this.getServices().map(res => res.service.map(service => new ProductFamily(service)));
  }

  public getServices(): Observable<GetServicesResponseInterface> {
    let pathQuery: string = Constants.Path.AAI_GET_SERVICES + Constants.Path.ASSIGN + Math.random();

    return this.http.get<GetServicesResponseInterface>(pathQuery);
  }

  public getSubscribers(): Observable<Subscriber[]> {
    if (this.store.getState().service.subscribers){
      return Observable.of(<any> JSON.parse(JSON.stringify(this.store.getState().service.subscribers)));
    }

    let pathQuery: string = Constants.Path.AAI_GET_SUBSCRIBERS + Constants.Path.ASSIGN + Math.random();

    return this.http.get<GetSubscribersResponse>(pathQuery).map(res =>
       res.customer.map( subscriber => new Subscriber(subscriber))).do((res) => {
      this.store.dispatch(updateSubscribers(res));
    });
  }

  public getAicZones(): Observable<AicZone[]> {
    if (this.store.getState().service.aicZones){
      return Observable.of(<any> JSON.parse(JSON.stringify(this.store.getState().service.aicZones)));
    }

    let pathQuery: string = Constants.Path.AAI_GET_AIC_ZONES + Constants.Path.ASSIGN + Math.random();

    return this.http.get<GetAicZonesResponse>(pathQuery).map(res =>
       res.zone.map(aicZone => new AicZone(aicZone))).do((res) => {
      this.store.dispatch(updateAicZones(res));
    });
  }

  public getLcpRegionsAndTenants(globalCustomerId, serviceType): Observable<LcpRegionsAndTenants> {
    if (this.store.getState().service.lcpRegionsAndTenants.lcpRegionList.length !== 0){
      return Observable.of(<any> JSON.parse(JSON.stringify(this.store.getState().service.lcpRegionsAndTenants)));
    }
    let pathQuery: string = Constants.Path.AAI_GET_TENANTS
      + globalCustomerId + Constants.Path.FORWARD_SLASH + serviceType + Constants.Path.ASSIGN + Math.random();

    console.log("AaiService:getSubscriptionServiceTypeList: globalCustomerId: "
      + globalCustomerId);
    if (globalCustomerId != null) {
      return this.http.get(pathQuery)
        .map(this.tenantResponseToLcpRegionsAndTenants).do((res) => {
          this.store.dispatch(updateLcpRegionsAndTenants(res));
        });
    }
  }

  tenantResponseToLcpRegionsAndTenants(cloudRegionTenantList): LcpRegionsAndTenants {

    const lcpRegionsTenantsMap = {};

    const lcpRegionList = _.uniqBy(cloudRegionTenantList, 'cloudRegionID').map((cloudRegionTenant) => {
           return new LcpRegion(cloudRegionTenant)
         });

    lcpRegionList.forEach(region => {
      lcpRegionsTenantsMap[region.id] = _.filter(cloudRegionTenantList, {'cloudRegionID' : region.id})
                                                        .map((cloudRegionTenant) => {
                                                            return new Tenant(cloudRegionTenant)
                                                        });
      const reducer = (accumulator, currentValue) => {
          accumulator.isPermitted = accumulator.isPermitted || currentValue.isPermitted;

         return accumulator;
      };
      region.isPermitted = lcpRegionsTenantsMap[region.id].reduce(reducer).isPermitted;
    });

    return new LcpRegionsAndTenants(lcpRegionList, lcpRegionsTenantsMap);
  }

  public getServiceTypes(subscriberId): Observable<ServiceType[]> {
    console.log("AaiService:getSubscriptionServiceTypeList: globalCustomerId: " + subscriberId);
    if (_.has(this.store.getState().service.serviceTypes, subscriberId)){
      return Observable.of(<any> JSON.parse(JSON.stringify(this.store.getState().service.serviceTypes[subscriberId])));
    }

    return this.getSubscriberDetails(subscriberId)
      .map(this.subDetailsResponseToServiceTypes)
      .do((res) => {this.store.dispatch(updateServiceTypes(res, subscriberId));});
  }

  public getSubscriberDetails(subscriberId): Observable<GetSubDetailsResponse> {
    let pathQuery: string = Constants.Path.AAI_SUB_DETAILS_PATH + subscriberId + Constants.Path.ASSIGN + Math.random();

    if (subscriberId != null) {
      return this.http.get<GetSubDetailsResponse>(pathQuery);
    }
  }

  subDetailsResponseToServiceTypes(res: GetSubDetailsResponse): ServiceType[] {
    if (res && res['service-subscriptions']) {
      const serviceSubscriptions = res['service-subscriptions']['service-subscription'];
      return serviceSubscriptions.map((subscription, index) => new ServiceType(String(index), subscription))
    } else {
      return [];
    }
  }
}
