import {ServiceState} from "../main.reducer";
import {Action} from "redux";
import * as _ from "lodash";
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";
import {NcfActions, DeleteActionNcfInstanceAction, UndoDeleteActionNcfInstanceAction} from "./ncf.actions";

export function ncfReducer(state: ServiceState, action: Action): ServiceState {
  switch (action.type) {
    case NcfActions.DELETE_ACTION_NCF_INSTANCE : {
      let newState = _.cloneDeep(state);
      let ncf = newState.serviceInstance[(<DeleteActionNcfInstanceAction>action).serviceId]['collectionResources'][(<DeleteActionNcfInstanceAction>action).collectionResourceStoreKey]['ncfs'][(<DeleteActionNcfInstanceAction>action).ncfStoreKey];
      let oldAction = ncf.action;
      if(oldAction === ServiceInstanceActions.None_Delete || oldAction === ServiceInstanceActions.Update_Delete) return newState;
      newState.serviceInstance[(<DeleteActionNcfInstanceAction>action).serviceId]['collectionResources'][(<DeleteActionNcfInstanceAction>action).collectionResourceStoreKey]['ncfs'][(<DeleteActionNcfInstanceAction>action).ncfStoreKey].action = (oldAction + '_Delete') as ServiceInstanceActions;
      return newState;
    }

    case NcfActions.UNDO_DELETE_ACTION_NCF_INSTANCE : {
      let newState = _.cloneDeep(state);
      let ncf = newState.serviceInstance[(<UndoDeleteActionNcfInstanceAction>action).serviceId]['collectionResources'][(<DeleteActionNcfInstanceAction>action).collectionResourceStoreKey]['ncfs'][(<UndoDeleteActionNcfInstanceAction>action).ncfStoreKey];
      let oldState = ncf.action;
      newState.serviceInstance[(<UndoDeleteActionNcfInstanceAction>action).serviceId]['collectionResources'][(<DeleteActionNcfInstanceAction>action).collectionResourceStoreKey]['ncfs'][(<UndoDeleteActionNcfInstanceAction>action).ncfStoreKey].action = (oldState.split('_')[0]) as ServiceInstanceActions;
      return newState;
    }
  }
}
