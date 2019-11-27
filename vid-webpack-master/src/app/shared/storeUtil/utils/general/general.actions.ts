import {Action, ActionCreator} from "redux";
import {LcpRegionsAndTenants} from "../../../models/lcpRegionsAndTenants";
import {SelectOptionInterface} from "../../../models/selectOption";
import {ServiceType} from "../../../models/serviceType";
import {ITreeNode} from "angular-tree-component/dist/defs/api";

export enum GeneralActions {
  MERGE_OBJECT_BY_PATH = "MERGE_OBJECT_BY_PATH",
  UPDATE_LCP_REGIONS_AND_TENANTS = "UPDATE_LCP_REGIONS_AND_TENANTS",
  UPDATE_SUBSCRIBERS = "UPDATE_SUBSCRIBERS",
  UPDATE_PRODUCT_FAMILIES = "UPDATE_PRODUCT_FAMILIES",
  UPDATE_SERVICE_TYPES = "UPDATE_SERVICE_TYPES",
  UPDATE_AIC_ZONES = "UPDATE_AIC_ZONES",
  UPDATE_USER_ID = "UPDATE_USER_ID",
  UPDATE_NETWORK_FUNCTION = "UPDATE_NETWORK_FUNCTION",
  UPDATE_CATEGORY_PARAMETERS = "UPDATE_CATEGORY_PARAMETERS",
  REMOVE_INSTANCE = 'REMOVE_INSTANCE',
  CHANGE_INSTANCE_COUNTER = 'CHANGE_INSTANCE_COUNTER',
  DUPLICATE_BULK_INSTANCES = 'DUPLICATE_BULK_INSTANCES'
}
export interface UpdateLcpRegionsAndTenantsAction extends Action {
  lcpRegionsAndTenants?: LcpRegionsAndTenants;
}

export interface RemoveInstanceAction extends Action {
  modelName?: string;
  serviceModelId: string;
  storeKey : string;
  node : ITreeNode;
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
export interface UpdateUserIdAction extends Action {
  userId: string;
}

export interface UpdateNetworkCollectionFunction extends Action {
  networksAccordingToNetworkCollection: any;
  network_function: any;
}

export interface UpdateCategoryParametersAction extends Action {
  categoryParameters?: Object;
}

export interface ChangeInstanceCounterAction extends Action{
  serviceUUID : string;
  UUID : string;
  changeBy : number;
  node : ITreeNode;
}

export interface DuplicateBulkInstancesAction extends Action {
  serviceId?: string;
  modelName?: string;
  originalName? : string;
  objects? : {};
  existingNames: {[key: string] : any};
  node : ITreeNode;
}

export interface UpdateServiceTypesAction extends Action {
  serviceTypes?: ServiceType[];
  subscriberId: string;
}

export interface MergeObjectByPathAction extends Action{
  path: String[];
  payload: object;
}

export const updateLcpRegionsAndTenants: ActionCreator<UpdateLcpRegionsAndTenantsAction> = lcpRegionsAndTenants => ({
  type: GeneralActions.UPDATE_LCP_REGIONS_AND_TENANTS,
  lcpRegionsAndTenants: lcpRegionsAndTenants
});

export const updateSubscribers: ActionCreator<UpdateSubscribersAction> = subscribers => ({
  type: GeneralActions.UPDATE_SUBSCRIBERS,
  subscribers: subscribers
});

export const updateProductFamilies: ActionCreator<UpdateProductFamiliesAction> = productFamilies => ({
  type: GeneralActions.UPDATE_PRODUCT_FAMILIES,
  productFamilies: productFamilies
});

export const updateAicZones: ActionCreator<UpdateAicZonesAction> = aicZones => ({
  type: GeneralActions.UPDATE_AIC_ZONES,
  aicZones: aicZones
});

export const updateUserId: ActionCreator<UpdateUserIdAction> = userId => ({
  type: GeneralActions.UPDATE_USER_ID,
  userId: userId
});

export const updateNetworkCollectionFunction: ActionCreator<UpdateNetworkCollectionFunction> = (ncf, networksAccordingToNetworkCollection) => ({
  type: GeneralActions.UPDATE_NETWORK_FUNCTION,
  networksAccordingToNetworkCollection: networksAccordingToNetworkCollection["results"],
  network_function: ncf
});

export const updateCategoryParameters: ActionCreator<UpdateCategoryParametersAction> = categoryParameters => ({
  type: GeneralActions.UPDATE_CATEGORY_PARAMETERS,
  categoryParameters: categoryParameters
});

export const removeInstance: ActionCreator<RemoveInstanceAction> = (modelName, serviceModelId, storeKey, node : ITreeNode) => ({
  type: GeneralActions.REMOVE_INSTANCE,
  modelName: modelName,
  serviceModelId: serviceModelId,
  storeKey: storeKey,
  node : node
});


export const changeInstanceCounter: ActionCreator<ChangeInstanceCounterAction> = (UUID, serviceUUID , changeBy, node) => ({
  type: GeneralActions.CHANGE_INSTANCE_COUNTER,
  UUID: UUID,
  serviceUUID: serviceUUID,
  changeBy : changeBy || 1,
  node : node
});


export const duplicateBulkInstances: ActionCreator<DuplicateBulkInstancesAction> = (serviceId, objects, existingNames, node) => ({
  type: GeneralActions.DUPLICATE_BULK_INSTANCES,
  serviceId: serviceId,
  objects : objects,
  existingNames: existingNames,
  node : node
});


export const updateServiceTypes: ActionCreator<UpdateServiceTypesAction> = (serviceTypes, subscriberId) => ({
  type: GeneralActions.UPDATE_SERVICE_TYPES,
  serviceTypes: serviceTypes,
  subscriberId: subscriberId
});

export const mergeObjectByPathAction : ActionCreator<MergeObjectByPathAction> = (path, payload) => ({
  type: GeneralActions.MERGE_OBJECT_BY_PATH,
  path,
  payload
});


