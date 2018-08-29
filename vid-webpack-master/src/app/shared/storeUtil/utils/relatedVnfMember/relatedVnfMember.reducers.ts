import {Action} from "redux";
import * as _ from "lodash";
import {ServiceState} from "../main.reducer";
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";
import {
  CreateRelatedVnfMemeberInstanceAction,
  DeleteActionRelatedVnfMemeberInstanceAction, DeleteRelatedVnfMemebrInstanceAction,
  RelatedVnfActions,
  UndoDeleteActionRelatedVnfMemeberInstanceAction
} from "./relatedVnfMember.actions";

export function relatedVnfMemeberReducer(state: ServiceState, action: Action): ServiceState {
  switch (action.type) {

    case RelatedVnfActions.DELETE_ACTION_RELATED_VNF_MEMBER_INSTANCE : {
      let newState = _.cloneDeep(state);
      let relatedVnfMember = newState.serviceInstance[(<DeleteActionRelatedVnfMemeberInstanceAction>action).serviceId].vnfGroups[(<DeleteActionRelatedVnfMemeberInstanceAction>action).vnfGroupStoreKey]['vnfs'][(<DeleteActionRelatedVnfMemeberInstanceAction>action).relatedVnfMemeberStoreKey];
      let oldAction = relatedVnfMember.action;
      if(oldAction === ServiceInstanceActions.None_Delete || oldAction === ServiceInstanceActions.Update_Delete) return newState;
      newState.serviceInstance[(<DeleteActionRelatedVnfMemeberInstanceAction>action).serviceId].vnfGroups[(<DeleteActionRelatedVnfMemeberInstanceAction>action).vnfGroupStoreKey]['vnfs'][(<DeleteActionRelatedVnfMemeberInstanceAction>action).relatedVnfMemeberStoreKey].action = (oldAction + '_Delete') as ServiceInstanceActions;
      return newState;
    }

    case RelatedVnfActions.UNDO_DELETE_ACTION_RELATED_VNF_MEMBER_INSTANCE : {
      let newState = _.cloneDeep(state);
      let relatedVnfMember = newState.serviceInstance[(<UndoDeleteActionRelatedVnfMemeberInstanceAction>action).serviceId].vnfGroups[(<UndoDeleteActionRelatedVnfMemeberInstanceAction>action).vnfGroupStoreKey]['vnfs'][(<UndoDeleteActionRelatedVnfMemeberInstanceAction>action).relatedVnfMemeberStoreKey];
      let oldState = relatedVnfMember.action;
      newState.serviceInstance[(<DeleteActionRelatedVnfMemeberInstanceAction>action).serviceId].vnfGroups[(<DeleteActionRelatedVnfMemeberInstanceAction>action).vnfGroupStoreKey]['vnfs'][(<DeleteActionRelatedVnfMemeberInstanceAction>action).relatedVnfMemeberStoreKey].action = (oldState.split('_')[0]) as ServiceInstanceActions;
      return newState;
    }

    case RelatedVnfActions.REMOVE_RELATED_VNF_MEMBER_INSTANCE : {
      let newState = _.cloneDeep(state);
      delete newState.serviceInstance[(<DeleteRelatedVnfMemebrInstanceAction>action).serviceId].vnfGroups[(<DeleteRelatedVnfMemebrInstanceAction>action).vnfGroupStoreKey]['vnfs'][(<DeleteRelatedVnfMemebrInstanceAction>action).relatedVnfMemeberStoreKey];
      return newState;
    }

    case RelatedVnfActions.CREATE_RELATED_VNF_MEMBER_INSTANCE : {
      let newState = _.cloneDeep(state);
      let relatedVnfMember = (<CreateRelatedVnfMemeberInstanceAction>action).relatedVnfMember;
      relatedVnfMember['action'] = 'Create';
      relatedVnfMember['vnfStoreKey'] = relatedVnfMember.instanceId;
      relatedVnfMember['trackById'] = relatedVnfMember.instanceId;
      relatedVnfMember['instanceName'] = relatedVnfMember.instanceName;
      if(_.isNil(newState.serviceInstance[(<CreateRelatedVnfMemeberInstanceAction>action).serviceId].vnfGroups[(<CreateRelatedVnfMemeberInstanceAction>action).vnfGroupStoreKey]['vnfs'])){
        newState.serviceInstance[(<CreateRelatedVnfMemeberInstanceAction>action).serviceId].vnfGroups[(<CreateRelatedVnfMemeberInstanceAction>action).vnfGroupStoreKey]['vnfs'] = {};
      }
      newState.serviceInstance[(<CreateRelatedVnfMemeberInstanceAction>action).serviceId].vnfGroups[(<CreateRelatedVnfMemeberInstanceAction>action).vnfGroupStoreKey]['vnfs'][(<CreateRelatedVnfMemeberInstanceAction>action).relatedVnfMember['instanceId']] = relatedVnfMember;
      newState.serviceInstance[(<CreateRelatedVnfMemeberInstanceAction>action).serviceId].isDirty = true;
      return newState;
    }
  }
}





