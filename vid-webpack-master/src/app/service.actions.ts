import { Action, ActionCreator } from "redux";
import { LcpRegionsAndTenants } from "./shared/models/lcpRegionsAndTenants";
import { ServiceInstance } from "./shared/models/serviceInstance";
import { SelectOptionInterface } from "./shared/models/selectOption";
import { ServiceType } from "./shared/models/serviceType";
import { VnfInstance } from "./shared/models/vnfInstance";

export const UPDATE_MODEL = "[MODEL] Update";
export const UPDATE_SERVICE_INSTANCE = "[SERVICE INSTANCE] Update";
export const UPDATE_VNF_INSTANCE = "[VNF INSTANCE] Update";
export const UPDATE_VF_MODULE = "[VF MODULE] Update";
export const CREATE_VF_MODULE = "[VF MODULE] Create";

export const UPDATE_LCP_REGIONS_AND_TENANTS = "[LCP_REGIONS_AND_TENANTS] Update";
export const UPDATE_SUBSCRIBERS = "[SUBSCRIBERS] Update";
export const UPDATE_PRODUCT_FAMILIES = "[PRODUCT_FAMILIES] Update";
export const UPDATE_SERVICE_TYPES = "[SERVICE_TYPE] Update";
export const UPDATE_AIC_ZONES = "[AIC_ZONES] Update";
export const DELETE_SERVICE_INSTANCE = "[SERVICE_INSTANCE] Delete";
export const DELETE_VNF_INSTANCE = "[VNF_INSTANCE] Delete";
export const DELETE_VNF_MODULE_INSTANCE = "[VNF_MODULE_INSTANCE] Delete";
export const UPDATE_CATEGORY_PARAMETERS = "[CATEGORY_PARAMETERS] Update";
export const UPDATE_NETWORK_FUNCTION = "[UPDATE_NETWORK_FUNCTION] Update";
export const UPDATE_USER_ID = "[UPDATE_USER_ID] Update";


export interface UpdateServiceModelAction extends Action {
  serviceHierarchy?: any;
}

export interface UpdateNetworkCollectionFunction extends Action {
  networksAccordingToNetworkCollection: any;
  network_function: any;
}

export interface UpdateServiceInstanceAction extends Action {
  serviceUuid?: string;
  serviceInstance?: ServiceInstance;
}

export interface UpdateVFModuleInstanceAction extends Action {
  vfInstance: any;
  vfId: string;
  serviceUuid: string;
}

export interface CreateVFModuleInstanceAction extends Action {
  vfInstance: any;
  vfId: string;
  serviceUuid: string;
  index : number
}

export interface UpdateUserIdAction extends Action {
  userId: string;
}

export interface UpdateVnfInstanceAction extends Action {
  vnfInstance?: VnfInstance;
  vnfModelName?: string;
  serviceUuid?: string;
}

export interface UpdateLcpRegionsAndTenantsAction extends Action {
  lcpRegionsAndTenants?: LcpRegionsAndTenants;
}

export interface UpdateSubscribersAction extends Action {
  subscribers?: SelectOptionInterface[];
}

export interface UpdateProductFamiliesAction extends Action {
  productFamilies?: SelectOptionInterface[];
}

export interface UpdateAicZonesAction extends Action {
  aicZones?: SelectOptionInterface[];
}

export interface UpdateServiceTypesAction extends Action {
  serviceTypes?: ServiceType[];
  subscriberId: string;
}

export interface DeleteServiceInstanceAction extends Action {
  serviceUuid?: string;
}

export interface DeleteVnfInstanceAction extends Action {
  modelName?: string;
  serviceModelId: string;
}

export interface DeleteVfModuleInstanceAction extends Action {
  modelName?: string;
  serviceModelId?: string;
  vfName?: string;
}

export interface UpdateCategoryParametersAction extends Action {
  categoryParameters?: string;
}

export const updateModel: ActionCreator<UpdateServiceModelAction> = serviceHierarchy => ({
  type: UPDATE_MODEL,
  serviceHierarchy: serviceHierarchy
});

export const updateServiceInstance: ActionCreator<UpdateServiceInstanceAction> = (serviceInstance, serviceUuid) => ({
  type: UPDATE_SERVICE_INSTANCE,
  serviceInstance: serviceInstance,
  serviceUuid: serviceUuid
});

export const updateVFModuleInstance: ActionCreator<UpdateVFModuleInstanceAction> = (vfInstance, vfId, serviceUuid) => ({
  type: UPDATE_VF_MODULE,
  vfInstance: vfInstance,
  vfId: vfId,
  serviceUuid: serviceUuid
})

export const createVFModuleInstance: ActionCreator<CreateVFModuleInstanceAction> = (vfInstance, vfId, serviceUuid, index) => ({
  type: CREATE_VF_MODULE,
  vfInstance: vfInstance,
  vfId: vfId,
  serviceUuid: serviceUuid,
  index : index
})



export const updateVNFInstance: ActionCreator<UpdateVnfInstanceAction> = (vnfInstance, vnfModelName, serviceUuid) => ({
  type: UPDATE_VNF_INSTANCE,
  vnfInstance: vnfInstance,
  vnfModelName: vnfModelName,
  serviceUuid: serviceUuid
});

export const updateLcpRegionsAndTenants: ActionCreator<UpdateLcpRegionsAndTenantsAction> = lcpRegionsAndTenants => ({
  type: UPDATE_LCP_REGIONS_AND_TENANTS,
  lcpRegionsAndTenants: lcpRegionsAndTenants
});

export const updateSubscribers: ActionCreator<UpdateSubscribersAction> = subscribers => ({
  type: UPDATE_SUBSCRIBERS,
  subscribers: subscribers
});

export const updateProductFamilies: ActionCreator<UpdateProductFamiliesAction> = productFamilies => ({
  type: UPDATE_PRODUCT_FAMILIES,
  productFamilies: productFamilies
});

export const updateAicZones: ActionCreator<UpdateAicZonesAction> = aicZones => ({
  type: UPDATE_AIC_ZONES,
  aicZones: aicZones
});

export const updateUserId: ActionCreator<UpdateUserIdAction> = userId => ({
  type: UPDATE_USER_ID,
  userId: userId
});

export const updateCategoryParameters: ActionCreator<UpdateCategoryParametersAction> = categoryParameters => ({
  type: UPDATE_CATEGORY_PARAMETERS,
  categoryParameters: categoryParameters
});

export const updateServiceTypes: ActionCreator<UpdateServiceTypesAction> = (serviceTypes, subscriberId) => ({
  type: UPDATE_SERVICE_TYPES,
  serviceTypes: serviceTypes,
  subscriberId: subscriberId
});

export const updateNetworkCollectionFunction: ActionCreator<UpdateNetworkCollectionFunction> = (ncf, networksAccordingToNetworkCollection) => ({
  type: UPDATE_NETWORK_FUNCTION,
  networksAccordingToNetworkCollection: networksAccordingToNetworkCollection["results"],
  network_function: ncf
});

export const deleteServiceInstance: ActionCreator<DeleteServiceInstanceAction> = serviceUuid => ({
  type: DELETE_SERVICE_INSTANCE,
  serviceUuid: serviceUuid
});

export const deleteVnfInstance: ActionCreator<DeleteVnfInstanceAction> = (modelName, serviceModelId) => ({
  type: DELETE_VNF_INSTANCE,
  modelName: modelName,
  serviceModelId: serviceModelId
});

export const deleteVfModuleInstance: ActionCreator<DeleteVfModuleInstanceAction> = (modelName, serviceModelId, vfName) => ({
  type: DELETE_VNF_MODULE_INSTANCE,
  modelName: modelName,
  serviceModelId: serviceModelId,
  vfName: vfName
});
