
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";
import { crReducer} from "./cr.reducer";
import {ActionOnFirstLevel} from "../firstLevel/firstLevel.actions";
import {CrActions} from "./cr.actions";
describe('crReducer', () => {

  test('#DELETE_ACTION_CR_INSTANCE', () => {
    let crState = crReducer(<any>{
        serviceInstance: {
          'serviceModelId': {
            collectionResources: {
              'crStoreKey': {
                isMissingData: true,
                action: 'None'
              }
            }
          }
        }
      },
      <ActionOnFirstLevel>{
        type: CrActions.DELETE_ACTION_CR_INSTANCE,
        storeKey: 'crStoreKey',
        firstLevelName: 'collectionResources',
        serviceId: 'serviceModelId'
      }).serviceInstance['serviceModelId']['collectionResources']['crStoreKey'];

    expect(crState).toBeDefined();
    expect(crState.action).toEqual(ServiceInstanceActions.None_Delete);
  });

  test('#UNDO_DELETE_ACTION_CR_INSTANCE', () => {
    let crState = crReducer(<any>{
        serviceInstance: {
          'serviceModelId': {
            collectionResources: {
              'crStoreKey': {
                isMissingData: true,
                action: 'Update_Delete'
              }
            }
          }
        }
      },
      <ActionOnFirstLevel>{
        type: CrActions.UNDO_DELETE_ACTION_CR_INSTANCE,
        storeKey: 'crStoreKey',
        firstLevelName: 'collectionResources',
        serviceId: 'serviceModelId'
      }).serviceInstance['serviceModelId']['collectionResources']['crStoreKey'];

    expect(crState).toBeDefined();
    expect(crState.action).toEqual(ServiceInstanceActions.Update);
  });

});
