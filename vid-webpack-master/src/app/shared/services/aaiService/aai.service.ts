import {NgRedux} from "@angular-redux/store";
import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import * as _ from 'lodash';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/do';
import {of} from "rxjs";

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
import {
  updateAicZones,
  updateCategoryParameters,
  updateLcpRegionsAndTenants,
  updateServiceTypes,
  updateSubscribers,
  updateUserId
} from "../../storeUtil/utils/general/general.actions";
import {updateModel, createServiceInstance} from "../../storeUtil/utils/service/service.actions";
import {FeatureFlagsService, Features} from "../featureFlag/feature-flags.service";
import {VnfMember} from "../../models/VnfMember";
import {setOptionalMembersVnfGroupInstance} from "../../storeUtil/utils/vnfGroup/vnfGroup.actions";
import {Observable} from "rxjs";

@Injectable()
export class AaiService {
  constructor(private http: HttpClient, private store: NgRedux<AppState>, private featureFlagsService:FeatureFlagsService) {

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
    return " ("+ cloudOwner.trim().toLowerCase().replace(/^att-/, "").toUpperCase() + ")";
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
    let pathQuery: string = Constants.Path.AAI_SUB_DETAILS_PATH + subscriberId + Constants.Path.ASSIGN + Math.random();

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

  public retrieveAndStoreServiceInstanceTopology(serviceInstanceId: string, subscriberId: string, serviceType: string, serviceModeId: string):Observable<ServiceInstance> {
    return this.retrieveServiceInstanceTopology(serviceInstanceId, subscriberId, serviceType).do((service:ServiceInstance) => {
      this.store.dispatch(createServiceInstance(service, serviceModeId));
      });
  };


  public retrieveServiceInstanceRetryTopology(jobId : string) :Observable<ServiceInstance> {
    let pathQuery: string = `${Constants.Path.SERVICES_RETRY_TOPOLOGY}/${jobId}`;
    return this.http.get<ServiceInstance>(pathQuery);

    // return  of(
    //   <any>{
    //     "action": "None",
    //     "instanceName": "LXzQMx9clZl7D6ckJ",
    //     "instanceId": "service-instance-id",
    //     "orchStatus": "GARBAGE DATA",
    //     "productFamilyId": null,
    //     "lcpCloudRegionId": null,
    //     "tenantId": null,
    //     "modelInfo": {
    //       "modelInvariantId": "d27e42cf-087e-4d31-88ac-6c4b7585f800",
    //       "modelVersionId": "6e59c5de-f052-46fa-aa7e-2fca9d674c44",
    //       "modelName": "vf_vEPDG",
    //       "modelType": "service",
    //       "modelVersion": "5.0"
    //     },
    //     "globalSubscriberId": "global-customer-id",
    //     "subscriptionServiceType": "service-instance-type",
    //     "owningEntityId": null,
    //     "owningEntityName": null,
    //     "tenantName": null,
    //     "aicZoneId": null,
    //     "aicZoneName": null,
    //     "projectName": null,
    //     "rollbackOnFailure": null,
    //     "isALaCarte": false,
    //     "vnfs": {
    //       "1e918ade-3dc6-4cec-b952-3ff94ed82d1c": {
    //         "action": "None",
    //         "instanceName": "DgZuxjJy5LMIc3755",
    //         "instanceId": "1e918ade-3dc6-4cec-b952-3ff94ed82d1c",
    //         "orchStatus": null,
    //         "productFamilyId": null,
    //         "lcpCloudRegionId": null,
    //         "tenantId": null,
    //         "modelInfo": {
    //           "modelInvariantId": "vnf-instance-model-invariant-id",
    //           "modelVersionId": "vnf-instance-model-version-id",
    //           "modelType": "vnf"
    //         },
    //         "instanceType": "SXDBMhwdR9iO0g1Uv",
    //         "provStatus": null,
    //         "inMaint": false,
    //         "uuid": "vnf-instance-model-version-id",
    //         "originalName": null,
    //         "legacyRegion": null,
    //         "lineOfBusiness": null,
    //         "platformName": null,
    //         "trackById": "1e918ade-3dc6-4cec-b952-3ff94ed82d1c",
    //         "vfModules": {},
    //         "networks": {
    //           "ff464c97-ea9c-4165-996a-fe400499af3e": {
    //             "action": "None",
    //             "instanceName": "ZI0quzIpu8TNXS7nl",
    //             "instanceId": "ff464c97-ea9c-4165-996a-fe400499af3e",
    //             "orchStatus": "Assigned",
    //             "productFamilyId": null,
    //             "lcpCloudRegionId": null,
    //             "tenantId": null,
    //             "modelInfo": {
    //               "modelInvariantId": "network-instance-model-invariant-id",
    //               "modelVersionId": "network-instance-model-version-id",
    //               "modelType": "network"
    //             },
    //             "instanceType": "CONTRAIL30_BASIC",
    //             "provStatus": "prov",
    //             "inMaint": false,
    //             "uuid": "network-instance-model-version-id",
    //             "originalName": null,
    //             "legacyRegion": null,
    //             "lineOfBusiness": null,
    //             "platformName": null,
    //             "trackById": "ff464c97-ea9c-4165-996a-fe400499af3e",
    //             "isFailed": true
    //           },
    //           "3e41d57c-8bb4-443e-af02-9f86487ba938": {
    //             "action": "None",
    //             "instanceName": "0i9asscqSLm7Poeb8",
    //             "instanceId": "3e41d57c-8bb4-443e-af02-9f86487ba938",
    //             "orchStatus": "Created",
    //             "productFamilyId": null,
    //             "lcpCloudRegionId": null,
    //             "tenantId": null,
    //             "modelInfo": {
    //               "modelInvariantId": "network-instance-model-invariant-id",
    //               "modelVersionId": "network-instance-model-version-id",
    //               "modelType": "network"
    //             },
    //             "instanceType": "CONTRAIL30_BASIC",
    //             "provStatus": "prov",
    //             "inMaint": false,
    //             "uuid": "network-instance-model-version-id",
    //             "originalName": null,
    //             "legacyRegion": null,
    //             "lineOfBusiness": null,
    //             "platformName": null,
    //             "trackById": "3e41d57c-8bb4-443e-af02-9f86487ba938",
    //             "isFailed": true
    //           }
    //         },
    //         "isFailed": true
    //       },
    //       "9a9b2705-c569-4f1b-9a67-13e9f86e6c55": {
    //         "isFailed": true,
    //         "action": "None",
    //         "instanceName": "TFn0SYhrCUs7L3qWS",
    //         "instanceId": "9a9b2705-c569-4f1b-9a67-13e9f86e6c55",
    //         "orchStatus": null,
    //         "productFamilyId": null,
    //         "lcpCloudRegionId": null,
    //         "tenantId": null,
    //         "modelInfo": {
    //           "modelCustomizationName": "VF_vMee 0",
    //           "modelInvariantId": "vnf-instance-model-invariant-id",
    //           "modelVersionId": "d6557200-ecf2-4641-8094-5393ae3aae60",
    //           "modelType": "vnf"
    //         },
    //         "instanceType": "WIT68GUnH34VaGZgp",
    //         "provStatus": null,
    //         "inMaint": true,
    //         "uuid": "d6557200-ecf2-4641-8094-5393ae3aae60",
    //         "originalName": "VF_vMee 0",
    //         "legacyRegion": null,
    //         "lineOfBusiness": null,
    //         "platformName": null,
    //         "trackById": "9a9b2705-c569-4f1b-9a67-13e9f86e6c55",
    //         "vfModules": {
    //           "vf_vmee0..VfVmee..vmme_vlc..module-1": {
    //             "2c1ca484-cbc2-408b-ab86-25a2c15ce280": {
    //               "action": "None",
    //               "instanceName": "ss820f_0918_db",
    //               "instanceId": "2c1ca484-cbc2-408b-ab86-25a2c15ce280",
    //               "orchStatus": "deleted",
    //               "productFamilyId": null,
    //               "lcpCloudRegionId": null,
    //               "tenantId": null,
    //               "modelInfo": {
    //                 "modelCustomizationName": "VfVmee..vmme_vlc..module-1",
    //                 "modelCustomizationId": "b200727a-1bf9-4e7c-bd06-b5f4c9d920b9",
    //                 "modelInvariantId": "09edc9ef-85d0-4b26-80de-1f569d49e750",
    //                 "modelVersionId": "522159d5-d6e0-4c2a-aa44-5a542a12a830",
    //                 "modelType": "vfModule"
    //               },
    //               "instanceType": null,
    //               "provStatus": null,
    //               "inMaint": true,
    //               "uuid": "522159d5-d6e0-4c2a-aa44-5a542a12a830",
    //               "originalName": "VfVmee..vmme_vlc..module-1",
    //               "legacyRegion": null,
    //               "lineOfBusiness": null,
    //               "platformName": null,
    //               "trackById": "2c1ca484-cbc2-408b-ab86-25a2c15ce280",
    //               "isBase": false,
    //               "volumeGroupName": null,
    //               "isFailed": true
    //             }
    //           },
    //           "dc229cd8-c132-4455-8517-5c1787c18b14": {
    //             "3ef042c4-259f-45e0-9aba-0989bd8d1cc5": {
    //               "action": "None",
    //               "instanceName": "ss820f_0918_base",
    //               "instanceId": "3ef042c4-259f-45e0-9aba-0989bd8d1cc5",
    //               "orchStatus": "Assigned",
    //               "productFamilyId": null,
    //               "lcpCloudRegionId": null,
    //               "tenantId": null,
    //               "modelInfo": {
    //                 "modelCustomizationId": "8ad8670b-0541-4499-8101-275bbd0e8b6a",
    //                 "modelInvariantId": "1e463c9c-404d-4056-ba56-28fd102608de",
    //                 "modelVersionId": "dc229cd8-c132-4455-8517-5c1787c18b14",
    //                 "modelType": "vfModule"
    //               },
    //               "instanceType": null,
    //               "provStatus": null,
    //               "inMaint": false,
    //               "uuid": "dc229cd8-c132-4455-8517-5c1787c18b14",
    //               "originalName": null,
    //               "legacyRegion": null,
    //               "lineOfBusiness": null,
    //               "platformName": null,
    //               "trackById": "3ef042c4-259f-45e0-9aba-0989bd8d1cc5",
    //               "isBase": true,
    //               "volumeGroupName": null
    //             }
    //           }
    //         },
    //         "networks": {}
    //       }
    //     },
    //     "networks": {
    //       "e1edb09e-e68b-4ebf-adb8-e2587be56257": {
    //         "action": "None",
    //         "instanceName": "cNpGlYQDsmrUDK5iG",
    //         "instanceId": "e1edb09e-e68b-4ebf-adb8-e2587be56257",
    //         "orchStatus": "Created",
    //         "productFamilyId": null,
    //         "lcpCloudRegionId": null,
    //         "tenantId": null,
    //         "modelInfo": {
    //           "modelInvariantId": "network-instance-model-invariant-id",
    //           "modelVersionId": "ddc3f20c-08b5-40fd-af72-c6d14636b986",
    //           "modelType": "network"
    //         },
    //         "instanceType": "CONTRAIL30_HIMELGUARD",
    //         "provStatus": "preprov",
    //         "inMaint": false,
    //         "uuid": "ddc3f20c-08b5-40fd-af72-c6d14636b986",
    //         "originalName": null,
    //         "legacyRegion": null,
    //         "lineOfBusiness": null,
    //         "platformName": null,
    //         "trackById": "e1edb09e-e68b-4ebf-adb8-e2587be56257"
    //       },
    //       "de4b5203-ad1c-4f2b-8843-5236fb8dc9ba": {
    //         "action": "None",
    //         "instanceName": "EI9QlSRVK0lon54Cb",
    //         "instanceId": "de4b5203-ad1c-4f2b-8843-5236fb8dc9ba",
    //         "orchStatus": "Assigned",
    //         "productFamilyId": null,
    //         "lcpCloudRegionId": null,
    //         "tenantId": null,
    //         "modelInfo": {
    //           "modelInvariantId": "network-instance-model-invariant-id",
    //           "modelVersionId": "ddc3f20c-08b5-40fd-af72-c6d14636b986",
    //           "modelType": "network"
    //         },
    //         "instanceType": "CONTRAIL30_BASIC",
    //         "provStatus": "nvtprov",
    //         "inMaint": false,
    //         "uuid": "ddc3f20c-08b5-40fd-af72-c6d14636b986",
    //         "originalName": null,
    //         "legacyRegion": null,
    //         "lineOfBusiness": null,
    //         "platformName": null,
    //         "trackById": "de4b5203-ad1c-4f2b-8843-5236fb8dc9ba",
    //         "isFailed": true
    //       }
    //     },
    //     "vnfGroups": {},
    //     "validationCounter": 0,
    //     "existingVNFCounterMap": {
    //       "vnf-instance-model-version-id": 1,
    //       "d6557200-ecf2-4641-8094-5393ae3aae60": 1
    //     },
    //     "existingNetworksCounterMap": {
    //       "ddc3f20c-08b5-40fd-af72-c6d14636b986": 2
    //     },
    //     "existingVnfGroupCounterMap": {}
    //   }
    // );

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
    // let res = Observable.of((JSON.parse(JSON.stringify(this.loadMockMembers()))));
    // return  res;
     
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
        "lcpCloudRegionId":"mtn23b",
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
        "lcpCloudRegionId":"mtn23b",
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
