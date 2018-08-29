import {LcpRegionsAndTenants} from "../../models/lcpRegionsAndTenants";
import {CategoryParams} from "../../models/categoryParams";
import {Action} from "redux";
import {ServiceActions} from "./service/service.actions";
import {GeneralActions} from "./general/general.actions";
import {NetworkActions} from "./network/network.actions";
import {VfModuleActions} from "./vfModule/vfModule.actions";
import {VNFActions} from "./vnf/vnf.actions";
import {vnfReducer} from "./vnf/vnf.reducers";
import {generalReducer} from "./general/general.reducers";
import {serviceReducer} from "./service/service.reducers";
import {networkReducer} from "./network/network.reducers";
import {vfModuleReducer} from "./vfModule/vfModule.reducers";
import {ServiceInstance} from "../../models/serviceInstance";
import {SelectOptionInterface} from "../../models/selectOption";
import {ServiceType} from "../../models/serviceType";
import {VnfGroupActions} from "./vnfGroup/vnfGroup.actions";
import {vnfGroupReducer} from "./vnfGroup/vnfGroup.reducers";
import {RelatedVnfActions} from "./relatedVnfMember/relatedVnfMember.actions";
import {relatedVnfMemeberReducer} from "./relatedVnfMember/relatedVnfMember.reducers";

export let initialState: ServiceState = {
  serviceHierarchy: {},
  serviceInstance: {},
  lcpRegionsAndTenants: new LcpRegionsAndTenants(),
  subscribers: null,
  productFamilies: null,
  serviceTypes: {},
  aicZones: null,
  categoryParameters: new CategoryParams()
};


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

export const MainReducer = function (state: ServiceState = initialState, action: Action): ServiceState {
  console.info("action name", action.type);
  if(Object.values(ServiceActions).includes(action.type)){
    return serviceReducer(state, action);
  }else if (Object.values(GeneralActions).includes(action.type)){
    return generalReducer(state, action);
  }else if (Object.values(NetworkActions).includes(action.type)){
    return networkReducer(state, action);
  }else if (Object.values(VfModuleActions).includes(action.type)){
    return vfModuleReducer(state, action);
  }else if (Object.values(VNFActions).includes(action.type)){
    return vnfReducer(state, action);
  }else if (Object.values(VnfGroupActions).includes(action.type)){
    return vnfGroupReducer(state, action);
  }else if(Object.values(RelatedVnfActions).includes(action.type)){
    return relatedVnfMemeberReducer(state, action);
  } else {
    return Object.assign({}, state);
  }
};



