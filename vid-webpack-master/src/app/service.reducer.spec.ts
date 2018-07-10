import { LcpRegionsAndTenants } from './shared/models/lcpRegionsAndTenants';
import { ServiceReducer, ServiceState } from './service.reducer';
import { CategoryParams } from './shared/models/categoryParams';
import {
  DELETE_VNF_INSTANCE, DELETE_VNF_MODULE_INSTANCE, DeleteVfModuleInstanceAction,
  DeleteVnfInstanceAction,
  UPDATE_AIC_ZONES,
  UPDATE_LCP_REGIONS_AND_TENANTS,
  UPDATE_PRODUCT_FAMILIES,
  UPDATE_SERVICE_INSTANCE,
  UPDATE_SUBSCRIBERS,
  UPDATE_USER_ID,
  UPDATE_VNF_INSTANCE,
  UpdateAicZonesAction,
  UpdateProductFamiliesAction, UpdateServiceInstanceAction,
  UpdateSubscribersAction, UpdateUserIdAction,
  UpdateVnfInstanceAction
} from './service.actions';
import { VnfInstance } from './shared/models/vnfInstance';
import { ServiceInstance } from './shared/models/serviceInstance';
import { LcpRegion } from './shared/models/lcpRegion';
import { Tenant } from './shared/models/tenant';
import { SelectOption } from './shared/models/selectOption';


const initialState: ServiceState = {
  serviceHierarchy: {},
  serviceInstance: {},
  lcpRegionsAndTenants: new LcpRegionsAndTenants(),
  subscribers: null,
  productFamilies: null,
  serviceTypes: {},
  aicZones: null,
  categoryParameters: new CategoryParams()
};


describe('service reducer', () => {
  const userId: string = 'userId';
  it('should handle initial state', () => {
    expect(ServiceReducer(undefined, <any>{})).toEqual(initialState);
  });

  it('#UPDATE_USER_ID : should update userId ', (done: DoneFn) => {
    expect(ServiceReducer(<any>{},
      <UpdateUserIdAction>{
        type: UPDATE_USER_ID,
        userId: userId
      }
    )['userId']).toEqual(userId);
    done();
  });

  it('#UPDATE_SERVICE_INSTANCE : should update service instance with service id ', (done: DoneFn) => {
    const serviceUuid:string = 'serviceUuid';

    let serviceInstanceObject : ServiceInstance = {
      instanceName: 'instanceName',
      isUserProvidedNaming: false,
      globalSubscriberId: 'globalSubscriberId',
      productFamilyId: 'productFamilyId',
      subscriptionServiceType: 'subscriptionServiceType',
      lcpCloudRegionId: 'lcpCloudRegionId',
      tenantId: 'tenantId',
      tenantName: 'tenantName',
      aicZoneId: 'aicZoneId',
      aicZoneName: 'aicZoneName',
      projectName: 'projectName',
      owningEntityId: 'owningEntityId',
      owningEntityName: 'owningEntityName',
      pause: false,
      bulkSize: 1,
      vnfs: {},
      instanceParams : {},
      rollbackOnFailure: false,
      subscriberName: 'subscriberName'
    };

    let serviceState = ServiceReducer(<any>{serviceInstance : {}},
      <UpdateServiceInstanceAction>{
        type: UPDATE_SERVICE_INSTANCE,
        serviceUuid: serviceUuid,
        serviceInstance : serviceInstanceObject
      }).serviceInstance['serviceUuid'];

    expect(serviceState.instanceName).toEqual(serviceInstanceObject.instanceName);
    expect(serviceState.isUserProvidedNaming).toEqual(serviceInstanceObject.isUserProvidedNaming);
    expect(serviceState.globalSubscriberId).toEqual(serviceInstanceObject.globalSubscriberId);
    expect(serviceState.productFamilyId).toEqual(serviceInstanceObject.productFamilyId);
    expect(serviceState.subscriptionServiceType).toEqual(serviceInstanceObject.subscriptionServiceType);
    expect(serviceState.lcpCloudRegionId).toEqual(serviceInstanceObject.lcpCloudRegionId);
    expect(serviceState.tenantId).toEqual(serviceInstanceObject.tenantId);
    expect(serviceState.tenantName).toEqual(serviceInstanceObject.tenantName);
    expect(serviceState.aicZoneId).toEqual(serviceInstanceObject.aicZoneId);
    expect(serviceState.aicZoneName).toEqual(serviceInstanceObject.aicZoneName);
    expect(serviceState.projectName).toEqual(serviceInstanceObject.projectName);
    expect(serviceState.owningEntityId).toEqual(serviceInstanceObject.owningEntityId);
    expect(serviceState.owningEntityName).toEqual(serviceInstanceObject.owningEntityName);
    expect(serviceState.pause).toEqual(serviceInstanceObject.pause);
    expect(serviceState.bulkSize).toEqual(serviceInstanceObject.bulkSize);
    expect(serviceState.vnfs).toEqual(serviceInstanceObject.vnfs);
    expect(serviceState.instanceParams).toEqual(serviceInstanceObject.instanceParams);
    expect(serviceState.rollbackOnFailure).toEqual(serviceInstanceObject.rollbackOnFailure);
    expect(serviceState.subscriberName).toEqual(serviceInstanceObject.subscriberName);

    done();
  });

  it('#UPDATE_VNF_INSTANCE : should update vnf instance with service id and vnfModelName ', (done: DoneFn) => {
    let vnfInstanceObj : VnfInstance = {
      instanceName: 'instanceName',
      isUserProvidedNaming: false,
      productFamilyId: 'productFamilyId',
      lcpCloudRegionId: 'lcpCloudRegionId',
      legacyRegion: 'legacyRegion',
      tenantId: 'tenantId',
      platformName: 'platformName',
      lineOfBusiness: 'lineOfBusiness',
      rollbackOnFailure: 'false',
      vfModules: {}
    };

    let vnfState = ServiceReducer(<any>{serviceInstance : {
          'serviceUuid' : {
            vnfs : {}
          }
        }},
      <UpdateVnfInstanceAction>{
        type: UPDATE_VNF_INSTANCE,
        serviceUuid : 'serviceUuid',
        vnfInstance: vnfInstanceObj,
        vnfModelName : 'vnfModelName'
      }).serviceInstance['serviceUuid'].vnfs['vnfModelName'];

    expect(vnfState.instanceName).toEqual(vnfInstanceObj.instanceName);
    expect(vnfState.isUserProvidedNaming).toEqual(vnfInstanceObj.isUserProvidedNaming);
    expect(vnfState.productFamilyId).toEqual(vnfInstanceObj.productFamilyId);
    expect(vnfState.lcpCloudRegionId).toEqual(vnfInstanceObj.lcpCloudRegionId);
    expect(vnfState.legacyRegion).toEqual(vnfInstanceObj.legacyRegion);
    expect(vnfState.tenantId).toEqual(vnfInstanceObj.tenantId);
    expect(vnfState.platformName).toEqual(vnfInstanceObj.platformName);
    expect(vnfState.lineOfBusiness).toEqual(vnfInstanceObj.lineOfBusiness);
    expect(vnfState.vfModules).toEqual(vnfInstanceObj.vfModules);
    expect(vnfState.rollbackOnFailure).toEqual(vnfInstanceObj.rollbackOnFailure);

    done();
  });

  it('#UPDATE_LCP_REGIONS_AND_TENANTS : should update lcp region and tenants', (done: DoneFn) => {
    let lcpRegionsAndTenantsObj = [
      {
        lcpRegionList : [
          new LcpRegion({
            "cloudRegionID" : 'cloudRegionID',
            "is-permitted" : "is-permitted"
          })
        ],
        lcpRegionsTenantsMap : {
          "lcpRegion" : [new Tenant({
            "tenantID" : "tenantID",
            "tenantName" : "tenantName",
            "is-permitted" : true
          })]
        }
      }
    ];
    let lcpRegionsAndTenantsState = ServiceReducer(<any>{serviceInstance : {}},
      <any>{
        type: UPDATE_LCP_REGIONS_AND_TENANTS,
        lcpRegionsAndTenants : lcpRegionsAndTenantsObj
      })['lcpRegionsAndTenants'];

    expect(lcpRegionsAndTenantsState).toBeDefined();
    done();
  });

  it('#UPDATE_SUBSCRIBERS : should update subscribers', (done: DoneFn) => {
    let subscribersList = [
      new SelectOption({
        id : 'id',
        name : 'name',
        isPermitted : false
      })
    ];
    let subscribersState = ServiceReducer(<any>{serviceInstance : {}},
      <UpdateSubscribersAction>{
        type: UPDATE_SUBSCRIBERS,
        subscribers : subscribersList
      })['subscribers'];

    expect(subscribersState).toBeDefined();
    expect(subscribersState[0].id).toEqual(subscribersList[0].id);
    expect(subscribersState[0].isPermitted).toEqual(subscribersList[0].isPermitted);
    expect(subscribersState[0].name).toEqual(subscribersList[0].name);

    done();
  });

  it('#UpdateProductFamiliesAction : should update product families', (done: DoneFn) => {
    let productFamiliesObj = [
      new SelectOption({
        id : 'id',
        name : 'name',
        isPermitted : false
      })
    ];
    let productFamiliesState = ServiceReducer(<any>{serviceInstance : {}},
      <UpdateProductFamiliesAction>{
        type: UPDATE_PRODUCT_FAMILIES,
        productFamilies : productFamiliesObj
      })['productFamilies'];

    expect(productFamiliesState).toBeDefined();
    expect(productFamiliesState[0].id).toEqual(productFamiliesObj[0].id);
    expect(productFamiliesState[0].isPermitted).toEqual(productFamiliesObj[0].isPermitted);
    expect(productFamiliesState[0].name).toEqual(productFamiliesObj[0].name);

    done();
  });

  it('#UPDATE_AIC_ZONES : should update aic zones', (done: DoneFn) => {
    let aicZonesObj = [
      new SelectOption({
        id : 'id',
        name : 'name',
        isPermitted : false
      })
    ];
    let aicZonesState = ServiceReducer(<any>{serviceInstance : {}},
      <UpdateAicZonesAction>{
        type: UPDATE_AIC_ZONES,
        aicZones : aicZonesObj
      })['aicZones'];

    expect(aicZonesState).toBeDefined();
    expect(aicZonesState[0].id).toEqual(aicZonesObj[0].id);
    expect(aicZonesState[0].isPermitted).toEqual(aicZonesObj[0].isPermitted);
    expect(aicZonesState[0].name).toEqual(aicZonesObj[0].name);

    done();
  });

  it('#DELETE_VNF_INSTANCE : should delete existing vnf', (done: DoneFn) => {
    let state = ServiceReducer(<any>{serviceInstance : {
          'serviceModelId' : {
            vnfs : {
              'modelName' : {}
            }
          }
        }},
      <DeleteVnfInstanceAction>{
        type: DELETE_VNF_INSTANCE,
        modelName : 'modelName',
        serviceModelId : 'serviceModelId'
      });

    expect(state).toBeDefined();
    expect(state.serviceInstance[ 'serviceModelId'].vnfs['modelName']).not.toBeDefined();
    done();
  });

  it('#DELETE_VNF_MODULE_INSTANCE : should delete existing vnf module', (done: DoneFn) => {
    let state = ServiceReducer(<any>{serviceInstance : {
          'serviceModelId' : {
            vnfs : {
              'vfName' : {
                vfModules : {
                  'modelName' : {}
                }
              }
            }
          }
        }},
      <DeleteVfModuleInstanceAction>{
        type: DELETE_VNF_MODULE_INSTANCE,
        modelName : 'modelName',
        vfName : 'vfName',
        serviceModelId : 'serviceModelId'
      });

    expect(state).toBeDefined();
    expect(state.serviceInstance['serviceModelId'].vnfs['vfName'].vfModules['modelName']).not.toBeDefined();
    done();
  });

});
