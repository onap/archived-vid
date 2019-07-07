import {networkReducer} from "./network.reducers";
import {
  CreateNetworkInstanceAction,
  NetworkActions,
  UpdateNetworkCollectionFunction,
  UpdateNetworkInstanceAction
} from "./network.actions";
import {NetworkInstance} from "../../../models/networkInstance";
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";
import {ActionOnFirstLevel} from "../firstLevel/firstLevel.actions";


describe('networkReducer', () => {
  test('#CREATE_SERVICE_INSTANCE', () => {
    let networkInstance: NetworkInstance = new NetworkInstance();
    networkInstance.isMissingData = false;
    networkInstance.instanceName = 'instanceName';
    let networkState = networkReducer(<any>{serviceInstance : {
          'serviceModelId' : {
            networks : {
              'networkStoreKey' : {
                isMissingData : true
              }
            }
          }
        }},
      <UpdateNetworkInstanceAction>{
        type: NetworkActions.UPDATE_NETWORK_INSTANCE,
        networkInstance : new NetworkInstance(),
        networkStoreKey : 'networkStoreKey',
        networkModelName : 'networkModelName',
        serviceUuid : 'serviceModelId'
      }).serviceInstance['serviceModelId'].networks['networkStoreKey'];

    expect(networkState).toBeDefined();
    expect(networkState.isMissingData).toBeFalsy();
  });

  test('#CREATE_NETWORK_INSTANCE', () => {
    let networkInstance: NetworkInstance = new NetworkInstance();
    networkInstance.isMissingData = false;
    networkInstance.instanceName = 'instanceName';
    let networkState = networkReducer(<any>{serviceInstance : {
          'serviceModelId' : {
            networks : {}
          }
        }},
      <CreateNetworkInstanceAction>{
        type: NetworkActions.CREATE_NETWORK_INSTANCE,
        networkInstance : new NetworkInstance(),
        networkStoreKey : null,
        networkModelName : 'networkModelName',
        serviceUuid : 'serviceModelId'
      }).serviceInstance['serviceModelId'].networks['networkModelName'];

    expect(networkState).toBeDefined();
    expect(networkState.isMissingData).toBeFalsy();
  });

  test('#UPDATE_NETWORK_FUNCTION', () => {
    let state = networkReducer(<any>{serviceInstance : {}},
      <UpdateNetworkCollectionFunction>{
        type: NetworkActions.UPDATE_NETWORK_FUNCTION,
        networksAccordingToNetworkCollection: 'networksAccordingToNetworkCollection',
        network_function: 'network_function'
      });

    expect(state).toBeDefined();
  });

  test('#DELETE_ACTION_NETWORK_INSTANCE', () => {
    let networkState = networkReducer(<any>{serviceInstance : {
          'serviceModelId' : {
            networks : {
              'networkStoreKey' : {
                isMissingData : true,
                action : 'None'
              }
            }
          }
        }},
      <ActionOnFirstLevel>{
        type: NetworkActions.DELETE_ACTION_NETWORK_INSTANCE,
        firstLevelName: 'networks',
        storeKey: 'networkStoreKey',
        serviceId: 'serviceModelId'
      }).serviceInstance['serviceModelId'].networks['networkStoreKey'];

    expect(networkState).toBeDefined();
    expect(networkState.action).toEqual(ServiceInstanceActions.None_Delete);
  });

  test('#UNDO_DELETE_ACTION_NETWORK_INSTANCE', () => {
    let networkState = networkReducer(<any>{serviceInstance : {
          'serviceModelId' : {
            networks : {
              'networkStoreKey' : {
                isMissingData : true,
                action : 'Update_Delete'
              }
            }
          }
        }},
      <ActionOnFirstLevel>{
        type: NetworkActions.UNDO_DELETE_ACTION_NETWORK_INSTANCE,
        storeKey: 'networkStoreKey',
        serviceId: 'serviceModelId'
      }).serviceInstance['serviceModelId'].networks['networkStoreKey'];

    expect(networkState).toBeDefined();
    expect(networkState.action).toEqual(ServiceInstanceActions.Update);
  });
});



