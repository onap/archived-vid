import {ServiceInstance} from "../../../models/serviceInstance";
import {useTemplateReducer} from "./useTemplate.reducer";
import {CreateServiceInstanceFromTemplate, UseTemplateActions} from "./useTemplate.action";

test('#CREATE_SERVICE_INSTANCE_FROM_TEMPLATE should add new service instance from template to redux ', () => {
  let serviceFromTemplateInstance: ServiceInstance = <any>{
    instanceName: 'templateInstanceName'
  };
  let serviceState = useTemplateReducer(<any>{
    serviceInstance:{}},
    <CreateServiceInstanceFromTemplate> {
    type: UseTemplateActions.CREATE_SERVICE_INSTANCE_FROM_TEMPLATE,
      serviceModelId: 'serviceModelID',
      serviceInstantiationTemplate: serviceFromTemplateInstance,
  })
  expect (serviceState).toBeDefined();
  expect (serviceState.serviceInstance['serviceModelID'].instanceName).toEqual('templateInstanceName');
});
