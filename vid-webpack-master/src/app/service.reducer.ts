import { Action } from 'redux';
import {
  CREATE_VF_MODULE,
  CreateVFModuleInstanceAction,
  DELETE_SERVICE_INSTANCE,
  DELETE_VNF_INSTANCE,
  DELETE_VNF_MODULE_INSTANCE,
  DeleteServiceInstanceAction,
  DeleteVfModuleInstanceAction,
  DeleteVnfInstanceAction,
  UPDATE_AIC_ZONES,
  UPDATE_CATEGORY_PARAMETERS,
  UPDATE_LCP_REGIONS_AND_TENANTS,
  UPDATE_MODEL,
  UPDATE_NETWORK_FUNCTION,
  UPDATE_PRODUCT_FAMILIES,
  UPDATE_SERVICE_INSTANCE,
  UPDATE_SERVICE_TYPES,
  UPDATE_SUBSCRIBERS,
  UPDATE_USER_ID,
  UPDATE_VF_MODULE,
  UPDATE_VNF_INSTANCE,
  UpdateAicZonesAction,
  UpdateCategoryParametersAction,
  UpdateLcpRegionsAndTenantsAction,
  UpdateNetworkCollectionFunction,
  UpdateProductFamiliesAction,
  UpdateServiceInstanceAction,
  UpdateServiceModelAction,
  UpdateServiceTypesAction,
  UpdateSubscribersAction,
  UpdateUserIdAction,
  UpdateVFModuleInstanceAction,
  UpdateVnfInstanceAction,
} from './service.actions';
import { LcpRegionsAndTenants } from './shared/models/lcpRegionsAndTenants';
import * as _ from 'lodash';
import { ServiceInstance } from './shared/models/serviceInstance';
import { CategoryParams } from './shared/models/categoryParams';
import { SelectOptionInterface } from './shared/models/selectOption';
import { ServiceType } from './shared/models/serviceType';
import { VnfInstance } from './shared/models/vnfInstance';
import { VfModuleMap } from './shared/models/vfModulesMap';

export interface ServiceState {
  serviceHierarchy: any;
  serviceInstance: { [uuid: string]: ServiceInstance; };
  lcpRegionsAndTenants: LcpRegionsAndTenants;
  subscribers: SelectOptionInterface[];
  productFamilies: any;
  serviceTypes: { [subscriberId: string]: ServiceType[]; };
  aicZones: SelectOptionInterface[];
  categoryParameters: CategoryParams;
}

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

export const ServiceReducer =
  function (state: ServiceState = initialState, action: Action): ServiceState {
    switch (action.type) {
      case UPDATE_MODEL: {
        let uuid = (<UpdateServiceModelAction>action).serviceHierarchy.service.uuid;
        state.serviceHierarchy[uuid] = _.cloneDeep((<UpdateServiceModelAction>action).serviceHierarchy);
        return Object.assign({}, state);
      }
      case UPDATE_SERVICE_INSTANCE: {
        const updateServiceInstanceAction = <UpdateServiceInstanceAction>action;
        const uuid = updateServiceInstanceAction.serviceUuid;
        const newState = _.cloneDeep(state);

        const serviceInstance: ServiceInstance = newState.serviceInstance[uuid] || new ServiceInstance();

        newState.serviceInstance[uuid] = Object.assign(serviceInstance, updateServiceInstanceAction.serviceInstance);
        return newState;
      }
      case UPDATE_VNF_INSTANCE: {
        const updateVnfInstanceAction = <UpdateVnfInstanceAction>action;
        const serviceUuid = updateVnfInstanceAction.serviceUuid;
        const vnfModelName = updateVnfInstanceAction.vnfModelName;

        const newState = _.cloneDeep(state);
        const vnfInstance: VnfInstance = newState.serviceInstance[serviceUuid].vnfs[vnfModelName] || new VnfInstance();

        newState.serviceInstance[serviceUuid].vnfs[vnfModelName] = Object.assign(vnfInstance, updateVnfInstanceAction.vnfInstance);
        return newState;
      }


      case UPDATE_USER_ID: {
        const updateUserId : UpdateUserIdAction = <UpdateUserIdAction>action;
        // var newState2 = {...state,'userId':updateUserId.userId}
        var newState = _.cloneDeep(state);
        newState['userId'] = updateUserId.userId;
        return newState;

        // state = (...  {userId:action["userId"]},state]}
      }

      case UPDATE_VF_MODULE: {
        const updateVFModuleInstanceAction = <UpdateVFModuleInstanceAction>action;
        const vfInstance = updateVFModuleInstanceAction.vfInstance;
        const serviceUuid = updateVFModuleInstanceAction.serviceUuid;
        const vfModuleId = updateVFModuleInstanceAction.vfId;
        const newState = _.cloneDeep(state);
        const vnfs = newState.serviceHierarchy[serviceUuid].vnfs;
        const vnfId = getVfModuleParentVnfId(vnfs, vfModuleId);
        let vfModulesMap = newState.serviceInstance[serviceUuid].vnfs[vnfId].vfModules[vfModuleId] || new VfModuleMap();
        vfModulesMap[vfModuleId] = vfInstance;
        newState.serviceInstance[serviceUuid].vnfs[vnfId].vfModules[vfModuleId] = vfModulesMap;
        return newState;
      }

      case CREATE_VF_MODULE: {
        const updateVFModuleInstanceAction = <CreateVFModuleInstanceAction>action;
        const vfInstance = updateVFModuleInstanceAction.vfInstance;
        const serviceUuid = updateVFModuleInstanceAction.serviceUuid;
        const vfModuleId = updateVFModuleInstanceAction.vfId;
        const index = updateVFModuleInstanceAction.index;
        let newState = Object.assign({}, state);
        const vnfs = newState.serviceHierarchy[serviceUuid].vnfs;
        const vnfId = getVfModuleParentVnfId(vnfs, vfModuleId);
        let vfModulesMap = newState.serviceInstance[serviceUuid].vnfs[vnfId].vfModules[vfModuleId] || new VfModuleMap();
        let randomId  = generateId();
        vfModulesMap[vfModuleId + randomId] = vfInstance;

        newState.serviceInstance[serviceUuid].vnfs[vnfId].vfModules[vfModuleId] = vfModulesMap;
        return newState;
      }


      case UPDATE_LCP_REGIONS_AND_TENANTS: {
        Object.assign(state, (<UpdateLcpRegionsAndTenantsAction>action));
        return Object.assign({}, state);
      }
      case UPDATE_SUBSCRIBERS: {
        Object.assign(state, (<UpdateSubscribersAction>action));
        return Object.assign({}, state);
      }
      case UPDATE_AIC_ZONES: {
        Object.assign(state, (<UpdateAicZonesAction>action));
        return Object.assign({}, state);
      }
      case UPDATE_PRODUCT_FAMILIES: {
        Object.assign(state, (<UpdateProductFamiliesAction>action));
        return Object.assign({}, state);
      }
      case UPDATE_NETWORK_FUNCTION: {
        let networkFunctionReduxObj = state["networkFunctions"] == undefined ? {} : state["networkFunctions"];
        networkFunctionReduxObj[(<UpdateNetworkCollectionFunction>action).network_function] = (<UpdateNetworkCollectionFunction>action).networksAccordingToNetworkCollection;
        Object.assign(state, {"networkFunctions":networkFunctionReduxObj});
        return Object.assign({}, state);
      }
      case UPDATE_SERVICE_TYPES: {
        let subscriberId = (<UpdateServiceTypesAction>action).subscriberId;
        let serviceTypes = (<UpdateServiceTypesAction>action).serviceTypes;
        state.serviceTypes[subscriberId] = serviceTypes;
        return Object.assign({}, state);
      }
      case UPDATE_CATEGORY_PARAMETERS: {
        Object.assign(state, (<UpdateCategoryParametersAction>action));
        return Object.assign({}, state);
      }
      case DELETE_SERVICE_INSTANCE: {
        const uuid = (<DeleteServiceInstanceAction>action).serviceUuid;
        if (state.serviceHierarchy[uuid]) {
          let newState = _.omit(state, ['serviceInstance[' + uuid + ']']);
          return Object.assign({}, state, newState);
        }
        return Object.assign({}, state);
      }
      case DELETE_VNF_INSTANCE: {
        const actionData =(<DeleteVnfInstanceAction>action);
        if(state.serviceInstance[actionData.serviceModelId]){
          delete state.serviceInstance[actionData.serviceModelId].vnfs[actionData.modelName];
        }
        return Object.assign({}, state);
      }

      case DELETE_VNF_MODULE_INSTANCE: {
        const actionData =(<DeleteVfModuleInstanceAction>action);
        if(state.serviceInstance[actionData.serviceModelId]){
          delete state.serviceInstance[actionData.serviceModelId].vnfs[actionData.vfName].vfModules[actionData.modelName];
        }
        return Object.assign({}, state);
      }
      default:
        return Object.assign({}, state);
    }
  };

const generateId = () => {
  return Math.random().toString(36).replace(/[^a-z]+/g, '').substr(0, 5);
};


const getVfModuleParentVnfId = (vnfs: object, vfModuleId: string) => {
  let vnfId = undefined;
  _.forOwn(vnfs, (value, key) => {
    if (vnfs[key].vfModules && vnfs[key].vfModules[vfModuleId]) {
      vnfId = vnfs[key].modelCustomizationName;
      return false;
    }
  });
  return vnfId;
};
