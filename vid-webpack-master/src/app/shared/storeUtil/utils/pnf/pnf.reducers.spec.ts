import {
  CreatePnfInstanceAction,
  RemovePnfInstanceAction,
  UpdatePnfPosition,
  UpdatePnfInstanceAction,
  PNFActions
} from "./pnf.actions";
import {pnfReducer} from "./pnf.reducers";
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";
import {ActionOnFirstLevel} from "../firstLevel/firstLevel.actions";
import {PnfInstance} from "../../../models/pnfInstance";

describe('pnfReducer', () => {

  test('#CREATE_PNF_INSTANCE', () => {
    let pnfInstance: PnfInstance = new PnfInstance();
    pnfInstance.isMissingData = false;
    pnfInstance.instanceName = 'instanceName';
    let pnfState = pnfReducer(<any>{
        serviceInstance: {
          'serviceModelId': {
            pnfs: {}
          }
        }
      },
      <CreatePnfInstanceAction>{
        type: PNFActions.CREATE_PNF_INSTANCE,
        pnfInstance: pnfInstance,
        pnfStoreKey: null,
        pnfModelName: 'pnfModelName',
        serviceUuid: 'serviceModelId'
      }).serviceInstance['serviceModelId'].pnfs['pnfModelName'];

    expect(pnfState).toBeDefined();
    expect(pnfState.isMissingData).toBeFalsy();
  });

  test('#UPDATE_PNF_INSTANCE_NAME', () => {
    let newInstanceName: string = "newInstanceName"
    let pnfInstance: PnfInstance = new PnfInstance();
    pnfInstance.isMissingData = false;
    pnfInstance.instanceName = newInstanceName;

    let pnfState = pnfReducer(<any>{
        serviceInstance: {
          'serviceModelId': {
            pnfs: {
              "pnfStoreKey": {
                instanceName: 'oldInstanceName'
              }
            },
            existingNames: {
              'oldinstancename': {}
            }
          }
        }
      },
      <UpdatePnfInstanceAction>{
        type: PNFActions.UPDATE_PNF_INSTANCE,
        pnfInstance: pnfInstance,
        pnfStoreKey: 'pnfStoreKey',
        pnfModelName: 'pnfModelName',
        serviceUuid: 'serviceModelId'
      }).serviceInstance['serviceModelId'];

    expect(pnfState).toBeDefined();
    expect(pnfState.pnfs['pnfStoreKey'].instanceName).toEqual(newInstanceName);
    expect(newInstanceName.toLowerCase() in pnfState.existingNames).toBeTruthy();
  });

  test('#DELETE_ACTION_PNF_INSTANCE', () => {
    let pnfState = pnfReducer(<any>{
        serviceInstance: {
          'serviceModelId': {
            pnfs: {
              'pnfStoreKey': {
                isMissingData: true,
                action: 'None'
              }
            }
          }
        }
      },
      <ActionOnFirstLevel>{
        type: PNFActions.DELETE_ACTION_PNF_INSTANCE,
        firstLevelName: 'pnfs',
        storeKey: 'pnfStoreKey',
        serviceId: 'serviceModelId'
      }).serviceInstance['serviceModelId'].pnfs['pnfStoreKey'];

    expect(pnfState).toBeDefined();
    expect(pnfState.action).toEqual(ServiceInstanceActions.None_Delete);
  });

  test('#UNDO_DELETE_ACTION_PNF_INSTANCE', () => {
    let pnfState = pnfReducer(<any>{
        serviceInstance: {
          'serviceModelId': {
            pnfs: {
              'pnfStoreKey': {
                isMissingData: true,
                action: 'Update_Delete'
              }
            }
          }
        }
      },
      <ActionOnFirstLevel>{
        type: PNFActions.UNDO_DELETE_ACTION_PNF_INSTANCE,
        storeKey: 'pnfStoreKey',
        firstLevelName: 'pnfs',
        serviceId: 'serviceModelId'
      }).serviceInstance['serviceModelId'].pnfs['pnfStoreKey'];

    expect(pnfState).toBeDefined();
    expect(pnfState.action).toEqual(ServiceInstanceActions.Update);
  });

  test('#REMOVE_PNF_INSTANCE', () => {
    let pnfs = pnfReducer(<any>{
        serviceInstance: {
          'serviceModelId': {
            pnfs: {
              'pnfStoreKey': {
                isMissingData: true,
                action: 'Update_Delete'
              },
              'pnfStoreKey_1': {
                isMissingData: true,
                action: 'Update_Delete'
              }
            }
          }
        }
      },
      <RemovePnfInstanceAction>{
        type: PNFActions.REMOVE_PNF_INSTANCE,
        pnfStoreKey: 'pnfStoreKey',
        serviceId: 'serviceModelId'
      }).serviceInstance['serviceModelId'].pnfs;

    expect(pnfs).toBeDefined();
    expect(pnfs['pnfStoreKey']).toBeUndefined();
    expect(pnfs['pnfStoreKey_1']).toBeDefined();
  });

  test('#UPDATE_PNF_POSITION', () => {
    let pnfInstance: PnfInstance = new PnfInstance();
    pnfInstance.isMissingData = false;
    pnfInstance.instanceName = 'instanceName';
    let pnfState = pnfReducer(<any>{
        serviceInstance: {
          'serviceModelId': {
            pnfs: {
              "pnfStoreKey": {}
            }
          }
        }
      },
      <UpdatePnfPosition>{
        type: PNFActions.UPDATE_PNF_POSITION,
        node: <any>{
          position: 2
        },
        pnfStoreKey: 'pnfStoreKey',
        instanceId: 'serviceModelId'
      }).serviceInstance['serviceModelId'].pnfs['pnfStoreKey'];

    expect(pnfState).toBeDefined();
    expect(pnfState.position).toEqual(2);
  });
});
