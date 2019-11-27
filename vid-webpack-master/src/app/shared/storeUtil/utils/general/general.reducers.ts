import {Action} from "redux";
import {ChangeInstanceCounterAction, DuplicateBulkInstancesAction, GeneralActions, MergeObjectByPathAction, RemoveInstanceAction, UpdateAicZonesAction, UpdateCategoryParametersAction, UpdateLcpRegionsAndTenantsAction, UpdateNetworkCollectionFunction, UpdateProductFamiliesAction, UpdateServiceTypesAction, UpdateSubscribersAction, UpdateUserIdAction} from "./general.actions";
import {TypeNodeInformation} from "../../../../drawingBoard/service-planning/typeNodeInformation.model";
import * as _ from "lodash";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {ServiceInstance} from "../../../models/serviceInstance";
import {ServiceState} from "../main.reducer";
import {updateServiceValidationCounter} from "../reducersHelper";

export function generalReducer(state: ServiceState, action: Action) : ServiceState {
  switch (action.type) {
    case GeneralActions.UPDATE_LCP_REGIONS_AND_TENANTS: {
      Object.assign(state, (<UpdateLcpRegionsAndTenantsAction>action));
      return Object.assign({}, state);
    }
    case GeneralActions.UPDATE_SUBSCRIBERS: {
      Object.assign(state, (<UpdateSubscribersAction>action));
      return Object.assign({}, state);
    }
    case GeneralActions.UPDATE_AIC_ZONES: {
      Object.assign(state, (<UpdateAicZonesAction>action));
      return Object.assign({}, state);
    }
    case GeneralActions.UPDATE_PRODUCT_FAMILIES: {
      Object.assign(state, (<UpdateProductFamiliesAction>action));
      return Object.assign({}, state);
    }
    case GeneralActions.UPDATE_NETWORK_FUNCTION: {
      const networkFunctionReduxObj = state['networkFunctions'] == undefined ? {} : state['networkFunctions'];
      networkFunctionReduxObj[(<UpdateNetworkCollectionFunction>action).network_function] = (<UpdateNetworkCollectionFunction>action).networksAccordingToNetworkCollection;
      Object.assign(state, {'networkFunctions': networkFunctionReduxObj});
      return Object.assign({}, state);
    }
    case GeneralActions.UPDATE_SERVICE_TYPES: {
      state.serviceTypes[(<UpdateServiceTypesAction>action).subscriberId] = (<UpdateServiceTypesAction>action).serviceTypes;
      return Object.assign({}, state);
    }
    case GeneralActions.UPDATE_CATEGORY_PARAMETERS: {
      Object.assign(state, (<UpdateCategoryParametersAction>action));
      return Object.assign({}, state);
    }
    case GeneralActions.UPDATE_USER_ID: {
      const updateUserId: UpdateUserIdAction = <UpdateUserIdAction>action;
      let newState = _.cloneDeep(state);
      newState['userId'] = updateUserId.userId;
      return newState;
    }
    case GeneralActions.REMOVE_INSTANCE: {
      const actionData = (<RemoveInstanceAction>action);
      if (state.serviceInstance[actionData.serviceModelId]) {
        const typeNodeInformation : TypeNodeInformation = new TypeNodeInformation(actionData.node);
        updateIsMissingDataOnDelete(state, actionData.serviceModelId, actionData.storeKey, actionData.node);
        updateUniqueNames(state.serviceInstance[actionData.serviceModelId][typeNodeInformation.hierarchyName][actionData.storeKey].instanceName, null, state.serviceInstance[actionData.serviceModelId]);
        if(actionData.node.data.type === 'VF'){
          _.forOwn(state.serviceInstance[actionData.serviceModelId][typeNodeInformation.hierarchyName][actionData.storeKey].vfModules, (vfModuleMap) => {
            _.forOwn(vfModuleMap, (vfModuleInstance) => {
              updateUniqueNames(vfModuleInstance.instanceName, null, state.serviceInstance[actionData.serviceModelId]);
            })
          });
        }
        delete state.serviceInstance[actionData.serviceModelId][typeNodeInformation.hierarchyName][actionData.storeKey];
      }
      return Object.assign({}, state);
    }

    case GeneralActions.CHANGE_INSTANCE_COUNTER : {
      const changeInstanceCounterAction = <ChangeInstanceCounterAction>action;
      const typeNodeInformation : TypeNodeInformation =  new TypeNodeInformation(changeInstanceCounterAction.node);
      let newState = _.cloneDeep(state);

      let existing: number = getExistingCounter(newState, changeInstanceCounterAction.serviceUUID, changeInstanceCounterAction.UUID, typeNodeInformation);
      newState.serviceInstance[changeInstanceCounterAction.serviceUUID][typeNodeInformation.existingMappingCounterName][changeInstanceCounterAction.UUID] = existing ? existing + changeInstanceCounterAction.changeBy : changeInstanceCounterAction.changeBy;
      return newState;
    }

    case GeneralActions.DUPLICATE_BULK_INSTANCES : {
      const createInstanceAction = <DuplicateBulkInstancesAction>action;
      const typeNodeInformation : TypeNodeInformation =  new TypeNodeInformation(createInstanceAction.node);
      const serviceId = createInstanceAction.serviceId;
      const objects = createInstanceAction.objects;

      let newState = _.cloneDeep(state);
      newState.serviceInstance[serviceId].existingNames = createInstanceAction.existingNames;
      newState.serviceInstance[serviceId][typeNodeInformation.hierarchyName] = Object.assign({}, newState.serviceInstance[serviceId][typeNodeInformation.hierarchyName], objects);
      return newState;
    }

    case GeneralActions.MERGE_OBJECT_BY_PATH : {
      const mergeObjectByPathAction = <MergeObjectByPathAction>action;
      let newState = _.cloneDeep(state);
      let targetObject = _.get(newState, <any>mergeObjectByPathAction.path);
      if (targetObject) {
        targetObject = _.merge(targetObject, mergeObjectByPathAction.payload);
      }
      else {
        console.error(`Can't find object at ${mergeObjectByPathAction.path.join()}`)
      }
      return newState;
    }


  }
}

const getExistingCounter = (state: any, serviceModelId: string, modelId: string, typeNodeInformation : TypeNodeInformation ) : number => {
  const serviceExistingCounterMap = state.serviceInstance[serviceModelId][typeNodeInformation.existingMappingCounterName];
  if(serviceExistingCounterMap && !_.isNil(serviceExistingCounterMap[modelId])){
    return serviceExistingCounterMap[modelId];
  }else {
    return null;
  }
};

const updateIsMissingDataOnDelete = (state: any, serviceModelId: string, storeKey: string, node: ITreeNode): void => {
  const typeNodeInformation : TypeNodeInformation = new TypeNodeInformation(node);
  let vnf = state.serviceInstance[serviceModelId][typeNodeInformation.hierarchyName][storeKey];
  if(node.children){
    _.forOwn(vnf.vfModules, (vfModules, vfModulesKey) => {
      updateIsMissingDataOnDeleteVFModule(state, serviceModelId, storeKey, vfModulesKey);
    });
  }


  let isMissingData: boolean = state.serviceInstance[serviceModelId][typeNodeInformation.hierarchyName][storeKey].isMissingData;
  updateServiceValidationCounter(state, isMissingData, false, serviceModelId);
};

const updateUniqueNames = (oldName : string, newName : string, serviceInstance : ServiceInstance) : void => {
  let existingNames = serviceInstance.existingNames;
  if (!_.isNil(oldName) && oldName.toLowerCase() in existingNames) {
    delete existingNames[oldName.toLowerCase()];
  }
  if(!_.isNil(newName)) {
    existingNames[newName.toLowerCase()] = "";
  }
};

const updateIsMissingDataOnDeleteVFModule = (state: any, serviceModelId: string, vnfStoreKey: string, vfModuleName): void => {
  const vfModules = state.serviceInstance[serviceModelId].vnfs[vnfStoreKey].vfModules[vfModuleName];

  _.forOwn(vfModules, (vfModuleInstance) => {
    let isMissingData: boolean = vfModuleInstance.isMissingData;
    updateServiceValidationCounter(state, isMissingData, false, serviceModelId);
  });
};




