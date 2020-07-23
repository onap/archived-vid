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
  UPDATE_VFMODULE_FEILD = "UPDATE_VFMODULE_FEILD",
  DELETE_VFMODULE_FIELD = "DELETE_VFMODULE_FEILD",
  PAUSE_ACTION_VFMODULE_INSTANCE = "PAUSE_ACTION_VFMODULE_INSTANCE",
  REMOVE_PAUSE_ON_VFMODULE_INSTANCE = "REMOVE_PAUSE_ON_VFMODULE_INSTANCE"
}


export interface UpdateVFModluePosition extends Action {
  vfKey: string,
  dynamicModelName?: string,
  position: number,
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
  vnfStoreKey : string,
  position : number
}


export interface DeleteActionVfModuleInstanceAction extends Action {
  dynamicModelName: string;
  vnfStoreKey : string;
  serviceId?: string;
  vfModuleModelName: string;
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

export interface UpdateVFModuleField extends Action {
  modelName : string;
  vnfStoreKey : string;
  serviceId: string;
  dynamicModelName: string;
  fieldName: string;
  fieldValue : any;
}

export interface DeleteVFModuleField extends Action {
  modelName : string;
  vnfStoreKey : string;
  serviceId: string;
  dynamicModelName: string;
  deleteFieldName: string;
}

export interface UndoDeleteActionVfModuleInstanceAction extends Action {
  dynamicModelName: string;
  vnfStoreKey : string;
  serviceId?: string;
  vfModuleModelName: string;
}

export interface PauseVFModuleInstanciationAction extends Action {
  dynamicModelName: string;
  vnfStoreKey : string;
  serviceId: string;
  vfModuleModelName : string;
}

export interface RemovePauseOnVFModuleInstanciationAction extends Action {
  dynamicModelName: string;
  vnfStoreKey : string;
  serviceId: string;
  vfModuleModelName : string;
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

export const updateVFModuleInstance: ActionCreator<UpdateVFModuleInstanceAction> = (vfInstance, vfId, serviceUuid, dynamicModelName, vnfStoreKey, position) => ({
  type: VfModuleActions.UPDATE_VF_MODULE,
  vfInstance: vfInstance,
  vfId: vfId,
  serviceUuid: serviceUuid,
  dynamicModelName : dynamicModelName,
  vnfStoreKey : vnfStoreKey,
  position : position
});

export const deleteActionVfModuleInstance: ActionCreator<DeleteActionVfModuleInstanceAction> = (dynamicModelName, vnfStoreKey, serviceId, vfModuleModelName) => ({
  type: VfModuleActions.DELETE_ACTION_VF_MODULE_INSTANCE,
  dynamicModelName: dynamicModelName,
  vnfStoreKey : vnfStoreKey,
  serviceId: serviceId,
  vfModuleModelName
});

export const undoDeleteVfModuleInstance: ActionCreator<UndoDeleteActionVfModuleInstanceAction> = (dynamicModelName, vnfStoreKey, serviceId, vfModuleModelName) => ({
  type: VfModuleActions.UNDO_DELETE_ACTION_VF_MODULE_INSTANCE,
  dynamicModelName: dynamicModelName,
  vnfStoreKey : vnfStoreKey,
  serviceId: serviceId,
  vfModuleModelName
});

export const updateVFModulePosition: ActionCreator<UpdateVFModluePosition> = (vfKey,dynamicModelName,position,instanceId, vnfStoreKey) => {
  return ({
    type: VfModuleActions.UPDATE_VFMODULE_POSITION,
    vfKey: vfKey,
    dynamicModelName: dynamicModelName,
    position: position,
    instanceId: instanceId,
    vnfStoreKey: vnfStoreKey
  });
};

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

export const updateVFModuleField: ActionCreator<UpdateVFModuleField> = (modelName, vnfStoreKey, serviceId, dynamicModelName, fieldName, fieldValue) => ({
  type: VfModuleActions.UPDATE_VFMODULE_FEILD,
  dynamicModelName,
  modelName,
  vnfStoreKey,
  serviceId,
  fieldName,
  fieldValue
});

export const deleteVFModuleField: ActionCreator<DeleteVFModuleField> = (modelName, vnfStoreKey, serviceId, dynamicModelName, deleteFieldName) => ({
  type: VfModuleActions.DELETE_VFMODULE_FIELD,
  dynamicModelName,
  modelName,
  vnfStoreKey,
  serviceId,
  deleteFieldName
});

export const pauseActionVFModuleInstance: ActionCreator<PauseVFModuleInstanciationAction> = (dynamicModelName, vnfStoreKey, serviceId, vfModuleModelName) => ({
  type: VfModuleActions.PAUSE_ACTION_VFMODULE_INSTANCE,
  dynamicModelName,
  vnfStoreKey,
  serviceId,
  vfModuleModelName
});

export const removePauseActionVFModuleInstance: ActionCreator<RemovePauseOnVFModuleInstanciationAction> = (dynamicModelName, vnfStoreKey, serviceId, vfModuleModelName) => ({
  type: VfModuleActions.REMOVE_PAUSE_ON_VFMODULE_INSTANCE,
  dynamicModelName,
  vnfStoreKey,
  serviceId,
  vfModuleModelName
});
