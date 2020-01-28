import {NgRedux} from "@angular-redux/store";
import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import * as _ from 'lodash';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/do';
import {Observable, of} from "rxjs";

import {AicZone} from "../../models/aicZone";
import {CategoryParams} from "../../models/categoryParams";
import {LcpRegion} from "../../models/lcpRegion";
import {LcpRegionsAndTenants} from "../../models/lcpRegionsAndTenants";
import {OwningEntity} from "../../models/owningEntity";
import {ProductFamily} from "../../models/productFamily";
import {Project} from "../../models/project";
import {SelectOption} from '../../models/selectOption';
import {ServiceType} from "../../models/serviceType";
import {Subscriber} from "../../models/subscriber";
import {Tenant} from "../../models/tenant";
import {Constants} from '../../utils/constants';
import {AppState} from "../../store/reducers";
import {GetAicZonesResponse} from "./responseInterfaces/getAicZonesResponseInterface";
import {GetCategoryParamsResponseInterface} from "./responseInterfaces/getCategoryParamsResponseInterface";
import {GetServicesResponseInterface} from "./responseInterfaces/getServicesResponseInterface";
import {GetSubDetailsResponse} from "./responseInterfaces/getSubDetailsResponseInterface";
import {GetSubscribersResponse} from "./responseInterfaces/getSubscribersResponseInterface";
import {Root} from "./model/crawledAaiService";
import {VnfInstance} from "../../models/vnfInstance";
import {VfModuleInstance} from "../../models/vfModuleInstance";
import {ServiceInstance} from "../../models/serviceInstance";
import {VfModuleMap} from "../../models/vfModulesMap";
import {updateAicZones, updateCategoryParameters, updateLcpRegionsAndTenants, updateServiceTypes, updateSubscribers, updateUserId} from "../../storeUtil/utils/general/general.actions";
import {createServiceInstance, updateModel,} from "../../storeUtil/utils/service/service.actions";
import {FeatureFlagsService, Features} from "../featureFlag/feature-flags.service";
import {VnfMember} from "../../models/VnfMember";
import {setOptionalMembersVnfGroupInstance} from "../../storeUtil/utils/vnfGroup/vnfGroup.actions";
import {NetworkModalRow} from "../../../drawingBoard/service-planning/objectsToTree/models/vrf/vrfModal/networkStep/network.step.model";
import {VPNModalRow} from "../../../drawingBoard/service-planning/objectsToTree/models/vrf/vrfModal/vpnStep/vpn.step.model";
import {ModelInfo} from "../../models/modelInfo";
import {SideMenuModel} from "../../components/sideMenu/side-menu.model";
import {VersionModel} from "../../components/sideMenu/version.model";

@Injectable()
export class AaiService {
  constructor(private http: HttpClient, private store: NgRedux<AppState>, private featureFlagsService:FeatureFlagsService) {

  }

  getVidVersion() : Observable<VersionModel>{
    return of({
      features:"2002.features.properties",
      build:"1.0.5421",
      displayVersion:"2002.5421"
    });
  }

  getMenuItems() : Observable<SideMenuModel[]> {
    return of([
      {
        label : "VID Home",
        action : "welcome"
      },
      {
        label : "Search for Existing Service Instances",
        action : "serviceModels.htm#/instances/services"
      },
      {
        label : "Create New Service Instance",
        action : "serviceModels.htm#/instances/subscribers"
      },
      {
        label : "Browse SDC Service Models",
        action : "serviceModels.htm"
      },
      {
        label : "Instantiation Status",
        action : "app/ui/#/instantiationStatus"
      },
      {
        label : "VNF Changes",
        action : "serviceModels.htm#/change-management"
      },
      {
        label : "Test Environments",
        action : "serviceModels.htm#/testEnvironments"
      },
      {
        label : "Admin",
        action : "admin"
      }
    ])
  }

  sdncPreload(): Observable<boolean> {
    let pathQuery: string = Constants.Path.PRE_LOAD;
    return this.http.post<boolean>(pathQuery, {})
  }

  getServiceModelById = (serviceModelId: string): Observable<any> => {
    if (_.has(this.store.getState().service.serviceHierarchy, serviceModelId)) {
      return of(<any> JSON.parse(JSON.stringify(this.store.getState().service.serviceHierarchy[serviceModelId])));
    }
    let pathQuery: string = Constants.Path.SERVICES_PATH + serviceModelId;
    return this.http.get(pathQuery).map(res => res)
      .do((res) => {
        this.store.dispatch(updateModel(res));
      });
  };

  retrieveServiceLatestUpdateableVersion = (modelInvariantId: string): Observable<ModelInfo> => {
    let pathQuery: string = Constants.Path.SERVICE_LATEST_VERSION + modelInvariantId;
    return this.http.get<ModelInfo>(pathQuery)
  };

  getUserId = (): Observable<any> => {
    return this.http.get("../../getuserID", {responseType: 'text'}).do((res) => this.store.dispatch(updateUserId(res)));
  };


  resolve = (root: Root, serviceInstance: ServiceInstance) => {
    if (root.type === 'service-instance') {
      serviceInstance.instanceName = root.name;
      serviceInstance.orchStatus = root.orchestrationStatus;
      serviceInstance.modelInavariantId = root.modelInvariantId;
      for (let i = 0; i < root.children.length; i++) {
        let child = root.children[i];
        if (child.type === 'generic-vnf') {
          let vnf = new VnfInstance();
          vnf.originalName = child.name;
          vnf.orchStatus = child.orchestrationStatus
          if (child.children.length > 0) {
            let vfModuleMap = new VfModuleMap();
            for (let j = 0; j < child.children.length; j++) {
              let child = root.children[i];
              if (child.type === 'vf-module') {
                let vfModule = new VfModuleInstance();
                vfModule.instanceName = child.name;
                vfModule.orchStatus = child.orchestrationStatus;
                vfModuleMap.vfModules[child.name] = vfModule;
              }
            }
            vnf.vfModules = {"a": vfModuleMap};
          }
          serviceInstance.vnfs[child.name] = vnf;

        }
      }

    }
  };


  getCRAccordingToNetworkFunctionId = (networkCollectionFunction, cloudOwner, cloudRegionId) => {
    return this.http.get('../../aai_get_instance_groups_by_cloudregion/' + cloudOwner + '/' + cloudRegionId + '/' + networkCollectionFunction)
      .map(res => res).do((res) => console.log(res));
  };

  getCategoryParameters = (familyName): Observable<CategoryParams> => {
    familyName = familyName || Constants.Path.PARAMETER_STANDARDIZATION_FAMILY;
    let pathQuery: string = Constants.Path.GET_CATEGORY_PARAMETERS + "?familyName=" + familyName + "&r=" + Math.random();

    return this.http.get<GetCategoryParamsResponseInterface>(pathQuery)
      .map(this.categoryParametersResponseToProductAndOwningEntity)
      .do(res => {
        this.store.dispatch(updateCategoryParameters(res))
      });
  };


  categoryParametersResponseToProductAndOwningEntity = (res: GetCategoryParamsResponseInterface): CategoryParams => {
    if (res && res.categoryParameters) {
      const owningEntityList = res.categoryParameters.owningEntity.map(owningEntity => new OwningEntity(owningEntity));
      const projectList = res.categoryParameters.project.map(project => new Project(project));
      const lineOfBusinessList = res.categoryParameters.lineOfBusiness.map(owningEntity => new SelectOption(owningEntity));
      const platformList = res.categoryParameters.platform.map(platform => new SelectOption(platform));

      return new CategoryParams(owningEntityList, projectList, lineOfBusinessList, platformList);
    } else {
      return new CategoryParams();
    }
  };

  getProductFamilies = (): Observable<ProductFamily[]> => {

    let pathQuery: string = Constants.Path.AAI_GET_SERVICES + Constants.Path.ASSIGN + Math.random();

    return this.http.get<GetServicesResponseInterface>(pathQuery).map(res => res.service.map(service => new ProductFamily(service)));
  };

  getServices = (): Observable<GetServicesResponseInterface> => {
    let pathQuery: string = Constants.Path.AAI_GET_SERVICES + Constants.Path.ASSIGN + Math.random();

    return this.http.get<GetServicesResponseInterface>(pathQuery);
  };

  getSubscribers = (): Observable<Subscriber[]> => {

    if (this.store.getState().service.subscribers) {
      return of(<any> JSON.parse(JSON.stringify(this.store.getState().service.subscribers)));
    }

    let pathQuery: string = Constants.Path.AAI_GET_SUBSCRIBERS + Constants.Path.ASSIGN + Math.random();

    return this.http.get<GetSubscribersResponse>(pathQuery).map(res =>
      res.customer.map(subscriber => new Subscriber(subscriber))).do((res) => {
      this.store.dispatch(updateSubscribers(res));
    });
  };

  getAicZones = (): Observable<AicZone[]> => {
    if (this.store.getState().service.aicZones) {
      return of(<any> JSON.parse(JSON.stringify(this.store.getState().service.aicZones)));
    }

    let pathQuery: string = Constants.Path.AAI_GET_AIC_ZONES + Constants.Path.ASSIGN + Math.random();

    return this.http.get<GetAicZonesResponse>(pathQuery).map(res =>
      res.zone.map(aicZone => new AicZone(aicZone))).do((res) => {
      this.store.dispatch(updateAicZones(res));
    });
  };

  getLcpRegionsAndTenants = (globalCustomerId, serviceType): Observable<LcpRegionsAndTenants> => {

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
  };

  tenantResponseToLcpRegionsAndTenants = (cloudRegionTenantList): LcpRegionsAndTenants => {

    const lcpRegionsTenantsMap = {};

    const lcpRegionList = _.uniqBy(cloudRegionTenantList, 'cloudRegionID').map((cloudRegionTenant) => {
      const cloudOwner:string = cloudRegionTenant["cloudOwner"];
      const cloudRegionId:string = cloudRegionTenant["cloudRegionID"];
      const name:string = this.extractLcpRegionName(cloudRegionId, cloudOwner);
      const isPermitted:boolean = cloudRegionTenant["is-permitted"];
      return new LcpRegion(cloudRegionId, name, isPermitted, cloudOwner);
    });

    lcpRegionList.forEach(region => {
      lcpRegionsTenantsMap[region.id] = _.filter(cloudRegionTenantList, {'cloudRegionID': region.id})
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
  };

  public extractLcpRegionName(cloudRegionId: string, cloudOwner: string):string {
    return this.featureFlagsService.getFlagState(Features.FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST) ?
      cloudRegionId+AaiService.formatCloudOwnerTrailer(cloudOwner) : cloudRegionId;
  };

  public static formatCloudOwnerTrailer(cloudOwner: string):string {
    return " ("+ cloudOwner.trim().toLowerCase().replace(/^[^-]*-/, "").toUpperCase() + ")";
  }

  getServiceTypes = (subscriberId): Observable<ServiceType[]> => {

    console.log("AaiService:getSubscriptionServiceTypeList: globalCustomerId: " + subscriberId);
    if (_.has(this.store.getState().service.serviceTypes, subscriberId)) {
      return of(<ServiceType[]> JSON.parse(JSON.stringify(this.store.getState().service.serviceTypes[subscriberId])));
    }

    return this.getSubscriberDetails(subscriberId)
      .map(this.subDetailsResponseToServiceTypes)
      .do((res) => {
        this.store.dispatch(updateServiceTypes(res, subscriberId));
      });
  };

  getSubscriberDetails = (subscriberId): Observable<GetSubDetailsResponse> => {
    let pathQuery: string = Constants.Path.AAI_SUB_DETAILS_PATH + subscriberId + Constants.Path.ASSIGN + Math.random() + Constants.Path.AAI_OMIT_SERVICE_INSTANCES + true;

    if (subscriberId != null) {
      return this.http.get<GetSubDetailsResponse>(pathQuery);
    }
  };

  subDetailsResponseToServiceTypes = (res: GetSubDetailsResponse): ServiceType[] => {
    if (res && res['service-subscriptions']) {
      const serviceSubscriptions = res['service-subscriptions']['service-subscription'];
      return serviceSubscriptions.map((subscription, index) => new ServiceType(String(index), subscription))
    } else {
      return [];
    }
  };


  public retrieveServiceInstanceTopology(serviceInstanceId : string, subscriberId: string, serviceType: string):Observable<ServiceInstance> {
    let pathQuery: string = `${Constants.Path.AAI_GET_SERVICE_INSTANCE_TOPOLOGY_PATH}${subscriberId}/${serviceType}/${serviceInstanceId}`;
    return this.http.get<ServiceInstance>(pathQuery);
  }

  public retrieveActiveNetwork(cloudRegion : string, tenantId: string) : Observable<NetworkModalRow[]>{
    let pathQuery: string = `${Constants.Path.AAI_GET_ACTIVE_NETWORKS_PATH}?cloudRegion=${cloudRegion}&tenantId=${tenantId}`;
    return this.http.get<NetworkModalRow[]>(pathQuery);
  }

  public retrieveActiveVPNs() : Observable<VPNModalRow[]>{
    let pathQuery: string = `${Constants.Path.AAI_GET_VPNS_PATH}`;
    return this.http.get<VPNModalRow[]>(pathQuery);
  }

  public retrieveAndStoreServiceInstanceTopology(serviceInstanceId: string, subscriberId: string, serviceType: string, serviceModeId: string):Observable<ServiceInstance> {
    return this.retrieveServiceInstanceTopology(serviceInstanceId, subscriberId, serviceType).do((service:ServiceInstance) => {
      this.store.dispatch(createServiceInstance(service, serviceModeId));
    });
  };


  public retrieveServiceInstanceRetryTopology(jobId : string) :Observable<ServiceInstance> {
    let pathQuery: string = `${Constants.Path.SERVICES_RETRY_TOPOLOGY}/${jobId}`;
    return this.http.get<ServiceInstance>(pathQuery);

  }

  public retrieveAndStoreServiceInstanceRetryTopology(jobId: string, serviceModeId : string):Observable<ServiceInstance> {
    return this.retrieveServiceInstanceRetryTopology(jobId).do((service:ServiceInstance) => {
      this.store.dispatch(createServiceInstance(service, serviceModeId));
    });
  };

  public getOptionalGroupMembers(serviceModelId: string, subscriberId: string, serviceType: string, serviceInvariantId: string, groupType: string, groupRole: string): Observable<VnfMember[]> {
    let pathQuery: string = `${Constants.Path.AAI_GET_SERVICE_GROUP_MEMBERS_PATH}${subscriberId}/${serviceType}/${serviceInvariantId}/${groupType}/${groupRole}`;
    if(_.has(this.store.getState().service.serviceInstance[serviceModelId].optionalGroupMembersMap,pathQuery)){
      return of(<VnfMember[]> JSON.parse(JSON.stringify(this.store.getState().service.serviceInstance[serviceModelId].optionalGroupMembersMap[pathQuery])));
    }
    return this.http.get<VnfMember[]>(pathQuery)
      .do((res) => {
        this.store.dispatch(setOptionalMembersVnfGroupInstance(serviceModelId, pathQuery, res))
      });
  }

  //TODO: make other places use this function
  extractSubscriberNameBySubscriberId(subscriberId: string) {
    let result: string = null;
    let filteredArray: any = _.filter(this.store.getState().service.subscribers, function (o: Subscriber) {
      return o.id === subscriberId
    });
    if (filteredArray.length > 0) {
      result = filteredArray[0].name;
    }
    return result;
  }

  loadMockMembers(): any {
    return [
      {
        "action":"None",
        "instanceName":"VNF1_INSTANCE_NAME",
        "instanceId":"VNF1_INSTANCE_ID",
        "orchStatus":null,
        "productFamilyId":null,
        "lcpCloudRegionId":"hvf23b",
        "tenantId":"3e9a20a3e89e45f884e09df0cc2d2d2a",
        "tenantName":"APPC-24595-T-IST-02C",
        "modelInfo":{
          "modelInvariantId":"vnf-instance-model-invariant-id",
          "modelVersionId":"7a6ee536-f052-46fa-aa7e-2fca9d674c44",
          "modelVersion":"2.0",
          "modelName":"vf_vEPDG",
          "modelType":"vnf"
        },
        "instanceType":"VNF1_INSTANCE_TYPE",
        "provStatus":null,
        "inMaint":false,
        "uuid":"7a6ee536-f052-46fa-aa7e-2fca9d674c44",
        "originalName":null,
        "legacyRegion":null,
        "lineOfBusiness":null,
        "platformName":null,
        "trackById":"7a6ee536-f052-46fa-aa7e-2fca9d674c44:002",
        "serviceInstanceId":"service-instance-id1",
        "serviceInstanceName":"service-instance-name"
      },
      {
        "action":"None",
        "instanceName":"VNF2_INSTANCE_NAME",
        "instanceId":"VNF2_INSTANCE_ID",
        "orchStatus":null,
        "productFamilyId":null,
        "lcpCloudRegionId":"hvf23b",
        "tenantId":"3e9a20a3e89e45f884e09df0cc2d2d2a",
        "tenantName":"APPC-24595-T-IST-02C",
        "modelInfo":{
          "modelInvariantId":"vnf-instance-model-invariant-id",
          "modelVersionId":"eb5f56bf-5855-4e61-bd00-3e19a953bf02",
          "modelVersion":"1.0",
          "modelName":"vf_vEPDG",
          "modelType":"vnf"
        },
        "instanceType":"VNF2_INSTANCE_TYPE",
        "provStatus":null,
        "inMaint":true,
        "uuid":"eb5f56bf-5855-4e61-bd00-3e19a953bf02",
        "originalName":null,
        "legacyRegion":null,
        "lineOfBusiness":null,
        "platformName":null,
        "trackById":"eb5f56bf-5855-4e61-bd00-3e19a953bf02:003",
        "serviceInstanceId":"service-instance-id2",
        "serviceInstanceName":"service-instance-name"
      }
    ]

  }


}
