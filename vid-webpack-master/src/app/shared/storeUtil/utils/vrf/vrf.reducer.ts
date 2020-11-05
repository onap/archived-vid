import {ServiceState} from "../main.reducer";
import {Action} from "redux";
import * as _ from "lodash";
import {
  AssociateVRFMemberInstanceAction,
  ClearAssociateVRFMemberInstanceAction,
  CreateVRFInstanceAction, DeleteActionVrfInstanceAction, UndoDeleteActionVrfInstanceAction,
  VrfActions
} from "./vrf.actions";
import {calculateNextUniqueModelName} from "../reducersHelper";
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";

export function vrfReducer(state: ServiceState, action: Action): ServiceState {
  switch (action.type) {
    case VrfActions.CREATE_VRF_INSTANCE: {
      const createVRFInstanceAction = <CreateVRFInstanceAction>action;
      const serviceUuid = createVRFInstanceAction.serviceModelId;

      let newState = _.cloneDeep(state);

      const vrfModelName = calculateNextUniqueModelName(createVRFInstanceAction.vrfModel['name'], serviceUuid, newState, 'vrfs');
      newState.serviceInstance[serviceUuid].vrfs[vrfModelName] = <any>{
        ...createVRFInstanceAction.vrfModel
      };
      return newState;
    }
    case VrfActions.VRF_ASSOCIATE_MEMBER: {
      const associateVRFMemberInstanceAction = <AssociateVRFMemberInstanceAction>action;
      const serviceUuid = associateVRFMemberInstanceAction.serviceModelId;
      const member = associateVRFMemberInstanceAction.member;
      const vrfStoreKey = associateVRFMemberInstanceAction.vrfStoreKey;
      const memberType = associateVRFMemberInstanceAction.memberType;

      let newState = _.cloneDeep(state);

      if(_.isNil(newState.serviceInstance[serviceUuid].vrfs[vrfStoreKey][memberType])){
        newState.serviceInstance[serviceUuid].vrfs[vrfStoreKey][memberType] = {};
      }

      const numberOfKeys = Object.keys(newState.serviceInstance[serviceUuid].vrfs[vrfStoreKey][memberType]).length;
      newState.serviceInstance[serviceUuid].vrfs[vrfStoreKey][memberType][`${vrfStoreKey} ${memberType} ${numberOfKeys+1}`] = member;

      return newState;
    }
    case VrfActions.VRF_ASSOCIATE_MEMBER: {
      const associateVRFMemberInstanceAction = <AssociateVRFMemberInstanceAction>action;
      const serviceUuid = associateVRFMemberInstanceAction.serviceModelId;
      const member = associateVRFMemberInstanceAction.member;
      const vrfStoreKey = associateVRFMemberInstanceAction.vrfStoreKey;
      const memberType = associateVRFMemberInstanceAction.memberType;

      let newState = _.cloneDeep(state);

      if(_.isNil(newState.serviceInstance[serviceUuid].vrfs[vrfStoreKey][memberType])){
        newState.serviceInstance[serviceUuid].vrfs[vrfStoreKey][memberType] = {};
      }

      const numberOfKeys = Object.keys(newState.serviceInstance[serviceUuid].vrfs[vrfStoreKey][memberType]).length;
      newState.serviceInstance[serviceUuid].vrfs[vrfStoreKey][memberType][`${vrfStoreKey} ${memberType} ${numberOfKeys+1}`] = member;

      return newState;
    }
    case VrfActions.CLAER_ASSOCIATE__VRF_MEMBERS: {
      const clearAssociateVRFMemberInstanceAction = <ClearAssociateVRFMemberInstanceAction>action;
      const serviceUuid = clearAssociateVRFMemberInstanceAction.serviceModelId;
      const vrfStoreKey = clearAssociateVRFMemberInstanceAction.vrfStoreKey;
      const memberType = clearAssociateVRFMemberInstanceAction.memberType;

      let newState = _.cloneDeep(state);
      newState.serviceInstance[serviceUuid].vrfs[vrfStoreKey][memberType] = {};
      return newState;
    }
    case VrfActions.DELETE_ACTION_VRF_INSTANCE : {
      let newState = _.cloneDeep(state);
      let vrf = newState.serviceInstance[(<DeleteActionVrfInstanceAction>action).serviceId].vrfs[(<DeleteActionVrfInstanceAction>action).vrfStoreKey];
      let oldAction = vrf.action;
      if(oldAction === ServiceInstanceActions.None_Delete || oldAction === ServiceInstanceActions.Update_Delete) return newState;
      newState.serviceInstance[(<DeleteActionVrfInstanceAction>action).serviceId].vrfs[(<DeleteActionVrfInstanceAction>action).vrfStoreKey].action = (oldAction + '_Delete') as ServiceInstanceActions;
      return newState;
    }

    case VrfActions.UNDO_DELETE_ACTION_VRF_INSTANCE : {
      let newState = _.cloneDeep(state);
      let vnf = newState.serviceInstance[(<UndoDeleteActionVrfInstanceAction>action).serviceId].vrfs[(<UndoDeleteActionVrfInstanceAction>action).vrfStoreKey];
      let oldState = vnf.action;
      newState.serviceInstance[(<UndoDeleteActionVrfInstanceAction>action).serviceId].vrfs[(<UndoDeleteActionVrfInstanceAction>action).vrfStoreKey].action = (oldState.split('_')[0]) as ServiceInstanceActions;
      return newState;
    }
  }
}
