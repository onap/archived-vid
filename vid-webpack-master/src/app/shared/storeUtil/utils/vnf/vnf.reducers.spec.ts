import {VnfInstance} from "../../../models/vnfInstance";
import {
  CreateVnfInstanceAction,
  RemoveVnfInstanceAction,
  UpdateVnfPosition, UpgradeVnfAction,
  VNFActions
} from "./vnf.actions";
import {vnfReducer} from "./vnf.reducers";
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";
import {ActionOnFirstLevel} from "../firstLevel/firstLevel.actions";

describe('vnfReducer', () => {
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
      <ActionOnFirstLevel>{
        type: VNFActions.DELETE_ACTION_VNF_INSTANCE,
        firstLevelName: 'vnfs',
        storeKey: 'vnfStoreKey',
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
      <ActionOnFirstLevel>{
        type: VNFActions.UNDO_DELETE_ACTION_VNF_INSTANCE,
        storeKey: 'vnfStoreKey',
        firstLevelName: 'vnfs',
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

  test('#UPGRADE_VNF_ACTION', () => {
    const vnfStoreKey: string  = 'vnfStoreKey';
    const serviceModelId: string  = 'serviceModelId';
    let vnfs = vnfReducer(<any>{serviceInstance : {
          [serviceModelId] : {
            vnfs : {
              [vnfStoreKey] : {
                isMissingData : true,
                action : 'None'
              }
            }
          }
        }},
      <UpgradeVnfAction>{
        type: VNFActions.UPGRADE_VNF_ACTION,
        vnfStoreKey: vnfStoreKey,
        serviceUuid: serviceModelId
      }).serviceInstance[serviceModelId].vnfs[vnfStoreKey];

    expect(vnfs).toBeDefined();
    expect(vnfs[vnfStoreKey]).toBeUndefined();
  });
  test('#UNDO_UPGRADE_VNF_ACTION', () => {
    const vnfStoreKey: string  = 'vnfStoreKey';
    const serviceModelId: string  = 'serviceModelId';
    let vnfs = vnfReducer(<any>{serviceInstance : {
          [serviceModelId] : {
            vnfs : {
              [vnfStoreKey] : {
                isMissingData : true,
                action : 'None_Upgrade'
              }
            }
          }
        }},
      <UpgradeVnfAction>{
        type: VNFActions.UNDO_UPGRADE_VNF_ACTION,
        vnfStoreKey: vnfStoreKey,
        serviceUuid: serviceModelId
      }).serviceInstance[serviceModelId].vnfs[vnfStoreKey];

    expect(vnfs).toBeDefined();
    expect(vnfs[vnfStoreKey]).toBeUndefined();
  });

});



