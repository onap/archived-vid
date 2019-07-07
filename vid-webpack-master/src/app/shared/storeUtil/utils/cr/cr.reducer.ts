import {ServiceState} from "../main.reducer";
import {Action} from "redux";
import * as _ from "lodash";
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";
import {CrActions} from "./cr.actions";
import {ActionOnFirstLevel} from "../firstLevel/firstLevel.actions";
import {deleteFirstLevel} from "../reducersHelper";

export function crReducer(state: ServiceState, action: Action): ServiceState {
  switch (action.type) {
    case CrActions.DELETE_ACTION_CR_INSTANCE : {
      return deleteFirstLevel(state, <ActionOnFirstLevel>action, false);
    }

    case CrActions.UNDO_DELETE_ACTION_CR_INSTANCE : {
      let newState = _.cloneDeep(state);
      let vnf = newState.serviceInstance[(<ActionOnFirstLevel>action).serviceId]['collectionResources'][(<ActionOnFirstLevel>action).storeKey];
      let oldState = vnf.action;
      newState.serviceInstance[(<ActionOnFirstLevel>action).serviceId]['collectionResources'][(<ActionOnFirstLevel>action).storeKey].action = (oldState.split('_')[0]) as ServiceInstanceActions;
      return newState;
    }
  }
 }
