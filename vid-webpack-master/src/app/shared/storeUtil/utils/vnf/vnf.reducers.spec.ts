import {VnfInstance} from "../../../models/vnfInstance";
import {
  CreateVnfInstanceAction,
  DeleteActionVnfInstanceAction, RemoveVnfInstanceAction,
  UndoDeleteActionVnfInstanceAction, UpdateVnfPosition,
  VNFActions
} from "./vnf.actions";
import {vnfReducer} from "./vnf.reducers";
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";

describe('networkReducer', () => {
  test('#UPDATE_VNF_POSITION', () => {
    let vnfInstance: VnfInstance = new VnfInstance();
    vnfInstance.isMissingData = false;
    vnfInstance.instanceName = 'instanceName';
    let vnfState = vnfReducer(<any>{
      serviceInstance : {
          'serviceModelId' : {
            vnfs : {
              "vnfStoreKey" : {

              }
            }
          }
        }},
      <UpdateVnfPosition>{
        type: VNFActions.UPDATE_VNF_POSITION,
        node : <any>{
          position : 1
        },
        vnfStoreKey : 'vnfStoreKey',
        instanceId : 'serviceModelId'
      }).serviceInstance['serviceModelId'].vnfs['vnfStoreKey'];

    expect(vnfState).toBeDefined();
    expect(vnfState.position).toEqual(1);
  });

  test('#CREATE_NETWORK_INSTANCE', () => {
    let vnfInstance: VnfInstance = new VnfInstance();
    vnfInstance.isMissingData = false;
    vnfInstance.instanceName = 'instanceName';
    let vnfState = vnfReducer(<any>{serviceInstance : {
          'serviceModelId' : {
            vnfs : {

            }
          }
        }},
      <CreateVnfInstanceAction>{
        type: VNFActions.CREATE_VNF_INSTANCE,
        vnfInstance : vnfInstance,
        vnfStoreKey : null,
        vnfModelName : 'vnfModelName',
        serviceUuid : 'serviceModelId'
      }).serviceInstance['serviceModelId'].vnfs['vnfModelName'];

    expect(vnfState).toBeDefined();
    expect(vnfState.isMissingData).toBeFalsy();
  });

  test('#DELETE_ACTION_VNF_INSTANCE', () => {
    let vnfState = vnfReducer(<any>{serviceInstance : {
          'serviceModelId' : {
            vnfs : {
              'vnfStoreKey' : {
                isMissingData : true,
                action : 'None'
              }
            }
          }
        }},
      <DeleteActionVnfInstanceAction>{
        type: VNFActions.DELETE_ACTION_VNF_INSTANCE,
        vnfStoreKey: 'vnfStoreKey',
        serviceId: 'serviceModelId'
      }).serviceInstance['serviceModelId'].vnfs['vnfStoreKey'];

    expect(vnfState).toBeDefined();
    expect(vnfState.action).toEqual(ServiceInstanceActions.None_Delete);
  });

  test('#UNDO_DELETE_ACTION_VNF_INSTANCE', () => {
    let vnfState = vnfReducer(<any>{serviceInstance : {
          'serviceModelId' : {
            vnfs : {
              'vnfStoreKey' : {
                isMissingData : true,
                action : 'Update_Delete'
              }
            }
          }
        }},
      <UndoDeleteActionVnfInstanceAction>{
        type: VNFActions.UNDO_DELETE_ACTION_VNF_INSTANCE,
        vnfStoreKey: 'vnfStoreKey',
        serviceId: 'serviceModelId'
      }).serviceInstance['serviceModelId'].vnfs['vnfStoreKey'];

    expect(vnfState).toBeDefined();
    expect(vnfState.action).toEqual(ServiceInstanceActions.Update);
  });

  test('#REMOVE_VNF_INSTANCE', () => {
    let vnfs = vnfReducer(<any>{serviceInstance : {
          'serviceModelId' : {
            vnfs : {
              'vnfStoreKey' : {
                isMissingData : true,
                action : 'Update_Delete'
              },
              'vnfStoreKey_1' : {
                isMissingData : true,
                action : 'Update_Delete'
              }
            }
          }
        }},
      <RemoveVnfInstanceAction>{
        type: VNFActions.REMOVE_VNF_INSTANCE,
        vnfStoreKey: 'vnfStoreKey',
        serviceId: 'serviceModelId'
      }).serviceInstance['serviceModelId'].vnfs;

    expect(vnfs).toBeDefined();
    expect(vnfs['vnfStoreKey']).toBeUndefined();
  });

});



