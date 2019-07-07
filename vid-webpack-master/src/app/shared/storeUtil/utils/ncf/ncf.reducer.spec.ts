
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";
import {ncfReducer} from "./ncf.reducer";
import {DeleteActionNcfInstanceAction, NcfActions, UndoDeleteActionNcfInstanceAction} from "./ncf.actions";
describe('ncfReducer', () => {

  test('#DELETE_ACTION_NCF_INSTANCE', () => {
    let ncfState = ncfReducer(<any>{
        serviceInstance: {
          'serviceModelId': {
            collectionResources: {
              'collectionResourceStoreKey': {
                ncfs: {
                  'ncfStoreKey': {
                    isMissingData: true,
                    action: 'None'
                  }
                }
              }
            }
          }
        }
      },
      <DeleteActionNcfInstanceAction>{
        type: NcfActions.DELETE_ACTION_NCF_INSTANCE,
        collectionResourceStoreKey: 'collectionResourceStoreKey',
        ncfStoreKey: 'ncfStoreKey',
        serviceId: 'serviceModelId'
      }).serviceInstance['serviceModelId']['collectionResources']['collectionResourceStoreKey']['ncfs']['ncfStoreKey'];

    expect(ncfState).toBeDefined();
    expect(ncfState.action).toEqual(ServiceInstanceActions.None_Delete);
  });

  test('#UNDO_DELETE_ACTION_NCF_INSTANCE', () => {
    let ncfState = ncfReducer(<any>{
      serviceInstance: {
          'serviceModelId': {
            collectionResources: {
              'collectionResourceStoreKey': {
                ncfs: {
                  'ncfStoreKey': {
                    isMissingData: true,
                    action: 'Update_Delete'
                  }
                }
              }

            }
          }
        }
      },
      <UndoDeleteActionNcfInstanceAction>{
        type: NcfActions.UNDO_DELETE_ACTION_NCF_INSTANCE,
        collectionResourceStoreKey: 'collectionResourceStoreKey',
        ncfStoreKey: 'ncfStoreKey',
        serviceId: 'serviceModelId'
      }).serviceInstance['serviceModelId']['collectionResources']['collectionResourceStoreKey']['ncfs']['ncfStoreKey'];

    expect(ncfState).toBeDefined();
    expect(ncfState.action).toEqual(ServiceInstanceActions.Update);
  });

});
