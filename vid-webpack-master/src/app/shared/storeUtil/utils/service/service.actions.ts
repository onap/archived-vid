import {ServiceInstance} from "../../../models/serviceInstance";
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";
import {Action, ActionCreator} from "redux";

export enum ServiceActions {
  CREATE_SERVICE_INSTANCE = 'CREATE_SERVICE_INSTANCE',
  UPDATE_SERVICE_INSTANCE = 'UPDATE_SERVICE_INSTANCE',
  DELETE_ALL_SERVICE_INSTANCES = 'DELETE_ALL_SERVICE_INSTANCES',
  UPDATE_MODEL  = 'UPDATE_MODEL',
  ADD_SERVICE_ACTION = 'ADD_SERVICE_ACTION',
  DELETE_ACTION_SERVICE_INSTANCE = "DELETE_ACTION_SERVICE_INSTANCE",
  UNDO_DELETE_ACTION_SERVICE_INSTANCE = "UNDO_DELETE_ACTION_SERVICE_INSTANCE",
  CHANGE_SERVICE_IS_DIRTY = "CHANGE_SERVICE_IS_DIRTY",
  UPGRADE_SERVICE_ACTION = "UPGRADE_SERVICE_ACTION",
  UPDATE_SERVICE_INFO_MODEL = "UPDATE_SERVICE_INFO_MODEL",
  UNDO_UPGRADE_SERVICE_ACTION = "UNDO_UPGRADE_SERVICE_ACTION"
}

export interface CreateServiceInstanceAction extends Action {
  serviceUuid?: string;
  serviceInstance?: ServiceInstance;
}

export interface UpdateServiceInstanceAction extends Action {
  serviceUuid?: string;
  serviceInstance?: ServiceInstance;
}

export interface DeleteServiceInstanceAction extends Action {
  serviceUuid?: string;
}

export interface DeleteServiceInstanceAction extends Action {
  serviceUuid?: string;
}

export interface UpdateServiceModelAction extends Action {
  serviceHierarchy?: any;
}

export interface AddServiceAction extends Action{
  serviceUuid: string;
  action: ServiceInstanceActions;
}

export interface UpgradeServiceAction extends Action{
  serviceUuid: string;
}

export interface UndoUpgradeServiceAction extends Action{
  serviceUuid: string;
}

export interface DeleteActionServiceInstanceAction extends Action {
  serviceId?: string;
}

export interface UndoDeleteActionServiceInstanceAction extends Action {
  serviceId?: string;
}

export interface ChangeServiceDirty extends Action {
  nodes: any[];
  serviceId : string;
}

export interface UpdateServiceModelInfoAction extends  Action {
  serviceInfoModel?: any;
}

export const addServiceAction: ActionCreator<AddServiceAction> = (serviceUuid : string, actionName : ServiceInstanceActions) => ({
  type: ServiceActions.ADD_SERVICE_ACTION,
  serviceUuid: serviceUuid,
  action : actionName
});


export const deleteAllServiceInstances: ActionCreator<DeleteServiceInstanceAction> = () => ({
  type: ServiceActions.DELETE_ALL_SERVICE_INSTANCES
});

export const createServiceInstance: ActionCreator<CreateServiceInstanceAction> = (serviceInstance, serviceUuid) => ({
  type: ServiceActions.CREATE_SERVICE_INSTANCE,
  serviceInstance: serviceInstance,
  serviceUuid: serviceUuid
});

export const updateServiceInstance: ActionCreator<UpdateServiceInstanceAction> = (serviceInstance, serviceUuid) => ({
  type: ServiceActions.UPDATE_SERVICE_INSTANCE,
  serviceInstance: serviceInstance,
  serviceUuid: serviceUuid
});

export const updateModel: ActionCreator<UpdateServiceModelAction> = serviceHierarchy => ({
  type: ServiceActions.UPDATE_MODEL,
  serviceHierarchy: serviceHierarchy
});


export const deleteActionServiceInstance: ActionCreator<DeleteActionServiceInstanceAction> = (vnfStoreKey, serviceId) => ({
  type: ServiceActions.DELETE_ACTION_SERVICE_INSTANCE,
  serviceId: serviceId
});

export const undoDeleteActionServiceInstance: ActionCreator<UndoDeleteActionServiceInstanceAction> = (vnfStoreKey, serviceId) => ({
  type: ServiceActions.UNDO_DELETE_ACTION_SERVICE_INSTANCE,
  serviceId: serviceId
});

export const changeServiceIsDirty: ActionCreator<ChangeServiceDirty> = (nodes, serviceId) => ({
  type: ServiceActions.CHANGE_SERVICE_IS_DIRTY,
  nodes: nodes,
  serviceId : serviceId
});

export const upgradeService: ActionCreator<UpgradeServiceAction> = (serviceUuid : string) => ({
  type: ServiceActions.UPGRADE_SERVICE_ACTION,
  serviceUuid
});

export const undoUpgradeService: ActionCreator<UndoUpgradeServiceAction> = (serviceUuid : string) => ({
  type: ServiceActions.UNDO_UPGRADE_SERVICE_ACTION,
  serviceUuid
});

export const updateServiceInfoModel : ActionCreator<UpdateServiceModelInfoAction> = (serviceInfoModel : any) => ({
  type: ServiceActions.UPDATE_SERVICE_INFO_MODEL,
  serviceInfoModel
});
