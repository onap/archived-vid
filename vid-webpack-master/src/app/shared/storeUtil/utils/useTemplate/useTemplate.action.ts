import {Action, ActionCreator} from "redux";
import {ServiceInstance} from "../../../models/serviceInstance";

export enum UseTemplateActions {
  CREATE_SERVICE_INSTANCE_FROM_TEMPLATE = 'CREATE_SERVICE_INSTANCE_FROM_TEMPLATE',
}

export interface CreateServiceInstanceFromTemplate extends Action {
  serviceInstantiationTemplate?: ServiceInstance;
  serviceModelId?: string;
}

export const createServiceInstanceFromTemplate: ActionCreator<CreateServiceInstanceFromTemplate> = (serviceInstantiationTemplate, serviceModelId) => ({
    type: UseTemplateActions.CREATE_SERVICE_INSTANCE_FROM_TEMPLATE,
    serviceInstantiationTemplate: serviceInstantiationTemplate,
    serviceModelId: serviceModelId
});
