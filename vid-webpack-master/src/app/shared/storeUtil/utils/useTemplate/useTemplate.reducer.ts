import {ServiceState} from "../main.reducer";
import {Action} from "redux";
import {
  createServiceInstanceFromTemplate,
  CreateServiceInstanceFromTemplate,
  UseTemplateActions
} from "./useTemplate.action";
import * as _ from "lodash";

export function useTemplateReducer(state: ServiceState, action: Action) : ServiceState {
   switch (action.type) {
     case UseTemplateActions.CREATE_SERVICE_INSTANCE_FROM_TEMPLATE : {
        const updateServiceInstanceFromTemplateAction = <CreateServiceInstanceFromTemplate>action;
        const uuid = updateServiceInstanceFromTemplateAction.serviceModelId;
        let newState = _.cloneDeep(state);
        newState.serviceInstance[uuid] = updateServiceInstanceFromTemplateAction.serviceInstantiationTemplate;
        return newState;
     }
   }
}
