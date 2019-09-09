import {Action, ActionCreator} from "redux";

export enum VfModuleActions {
  REMOVE_VNF_MODULE_INSTANCE = 'REMOVE_VNF_MODULE_INSTANCE',
  CREATE_VF_MODULE = 'CREATE_VF_MODULE',
  UPDATE_VF_MODULE = 'UPDATE_VF_MODULE',
  DELETE_ACTION_VF_MODULE_INSTANCE = "DELETE_ACTION_VF_MODULE_INSTANCE",
  UNDO_DELETE_ACTION_VF_MODULE_INSTANCE = "UNDO_DELETE_ACTION_VF_MODULE_INSTANCE",
  UPDATE_VFMODULE_POSITION = "UPDATE_VFMODULE_POSITION",
  UPGRADE_VFMODULE = "UPGRADE_VFMODULE",
  UNDO_UPGRADE_VFMODULE_ACTION = "UNDO_UPGRADE_VFMODULE_ACTION",
}


export interface UpdateVFModluePosition extends Action {
  node: any,
  instanceId : string,
  vnfStoreKey ?: string;
}

export interface DeleteVfModuleInstanceAction extends Action {
  modelName?: string;
  serviceModelId?: string;
  vfName?: string;
  vnfStoreKey?:string;
  dynamicModelName?: string;
}

export interface CreateVFModuleInstanceAction extends Action {
  vfInstance: any;
  vfId: string;
  serviceUuid: string;
  index : number
  vnfStoreKey : string;
}

export interface UpdateVFModuleInstanceAction extends Action {
  vfInstance: any;
  vfId: string;
  serviceUuid: string;
  dynamicModelName : string;
  vnfStoreKey : string
}


export interface DeleteActionVfModuleInstanceAction extends Action {
  dynamicModelName: string;
  vnfStoreKey : string;
  serviceId?: string;
}

export interface UpgradeVfModuleInstanceAction extends Action {
  modelName : string;
  vnfStoreKey : string;
  serviceId?: string;
  dynamicModelName: string;
}
export interface UndoUpgradeVfModuleInstanceAction extends Action {
  modelName : string;
  vnfStoreKey : string;
  serviceId?: string;
  dynamicModelName: string;
}

export interface UndoDeleteActionVfModuleInstanceAction extends Action {
  dynamicModelName: string;
  vnfStoreKey : string;
  serviceId?: string;
}

export const removeVfModuleInstance: ActionCreator<DeleteVfModuleInstanceAction> = (modelName, serviceModelId, vfName, vnfStoreKey, dynamicModelName) => ({
  type: VfModuleActions.REMOVE_VNF_MODULE_INSTANCE,
  modelName: modelName,
  serviceModelId: serviceModelId,
  vfName: vfName,
  vnfStoreKey : vnfStoreKey,
  dynamicModelName:dynamicModelName
});


export const createVFModuleInstance: ActionCreator<CreateVFModuleInstanceAction> = (vfInstance, vfId, serviceUuid, index, vnfStoreKey) => ({
  type: VfModuleActions.CREATE_VF_MODULE,
  vfInstance: vfInstance,
  vfId: vfId,
  serviceUuid: serviceUuid,
  index : index,
  vnfStoreKey : vnfStoreKey
});

export const updateVFModuleInstance: ActionCreator<UpdateVFModuleInstanceAction> = (vfInstance, vfId, serviceUuid, dynamicModelName, vnfStoreKey) => ({
  type: VfModuleActions.UPDATE_VF_MODULE,
  vfInstance: vfInstance,
  vfId: vfId,
  serviceUuid: serviceUuid,
  dynamicModelName : dynamicModelName,
  vnfStoreKey : vnfStoreKey
});

export const deleteActionVfModuleInstance: ActionCreator<DeleteActionVfModuleInstanceAction> = (dynamicModelName, vnfStoreKey, serviceId) => ({
  type: VfModuleActions.DELETE_ACTION_VF_MODULE_INSTANCE,
  dynamicModelName: dynamicModelName,
  vnfStoreKey : vnfStoreKey,
  serviceId: serviceId
});

export const undoDeleteVfModuleInstance: ActionCreator<UndoDeleteActionVfModuleInstanceAction> = (dynamicModelName, vnfStoreKey, serviceId) => ({
  type: VfModuleActions.UNDO_DELETE_ACTION_VF_MODULE_INSTANCE,
  dynamicModelName: dynamicModelName,
  vnfStoreKey : vnfStoreKey,
  serviceId: serviceId
});

export const updateVFModulePosition: ActionCreator<UpdateVFModluePosition> = (node, instanceId, vnfStoreKey) => ({
  type: VfModuleActions.UPDATE_VFMODULE_POSITION,
  node: node,
  instanceId: instanceId,
  vnfStoreKey : vnfStoreKey
});

export const upgradeVFModule: ActionCreator<UpgradeVfModuleInstanceAction> = (modelName, vnfStoreKey, serviceId, dynamicModelName) => ({
  type: VfModuleActions.UPGRADE_VFMODULE,
  dynamicModelName,
  modelName,
  vnfStoreKey,
  serviceId
});

export const undoUgradeVFModule: ActionCreator<UndoUpgradeVfModuleInstanceAction> = (modelName, vnfStoreKey, serviceId, dynamicModelName) => ({
  type: VfModuleActions.UNDO_UPGRADE_VFMODULE_ACTION,
  dynamicModelName,
  modelName,
  vnfStoreKey,
  serviceId
});
