import {vrfReducer} from "./vrf.reducer";
import {
  AssociateVRFMemberInstanceAction,
  ClearAssociateVRFMemberInstanceAction,
  CreateVRFInstanceAction, DeleteActionVrfInstanceAction, UndoDeleteActionVrfInstanceAction,
  VrfActions
} from "./vrf.actions";
import {vnfReducer} from "../vnf/vnf.reducers";
import {DeleteActionVnfInstanceAction, UndoDeleteActionVnfInstanceAction, VNFActions} from "../vnf/vnf.actions";
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";


describe('vrfReducer', () => {
  test('#VRF_ASSOCIATE_MEMBER ', () => {

    const serviceModelId: string = 'serviceModelID';
    const vrfStoreKey : string = 'vrfStoreKey';
    const memberType : string = 'networks';
    const member = {a : 1 , b : 2};
    let vrfState = vrfReducer(<any>{
         serviceInstance : {
          [serviceModelId] : {
            vrfs : {
              [vrfStoreKey] : {
                [memberType] : {

                }
              }

            }
          }
        }},
      <AssociateVRFMemberInstanceAction>{
        type: VrfActions.VRF_ASSOCIATE_MEMBER,
        vrfStoreKey : vrfStoreKey,
        serviceModelId : serviceModelId,
        memberType : memberType,
        member : member
      });

    expect(vrfState).toBeDefined();
    expect(vrfState.serviceInstance[serviceModelId].vrfs[vrfStoreKey][memberType][`${vrfStoreKey} ${memberType} 1`]).toBeDefined();
    expect(vrfState.serviceInstance[serviceModelId].vrfs[vrfStoreKey][memberType][`${vrfStoreKey} ${memberType} 1`]).toEqual(member);
  });

  test('#CLAER_ASSOCIATE__VRF_MEMBERS ', () => {

    const serviceModelId: string = 'serviceModelID';
    const vrfStoreKey : string = 'vrfStoreKey';
    const memberType : string = 'networks';
    let vrfState = vrfReducer(<any>{
        serviceInstance : {
          [serviceModelId] : {
            vrfs : {
              [vrfStoreKey] : {
                [memberType] : {
                  a : 1,
                  b : 2
                }
              }

            }
          }
        }},
      <ClearAssociateVRFMemberInstanceAction>{
        type: VrfActions.CLAER_ASSOCIATE__VRF_MEMBERS,
        vrfStoreKey : vrfStoreKey,
        serviceModelId : serviceModelId,
        memberType : memberType
      }).serviceInstance[serviceModelId].vrfs[vrfStoreKey];

    expect(vrfState).toBeDefined();
    expect(vrfState[memberType]).toBeDefined();
    expect(vrfState[memberType]).toEqual({});
  });

  test('#CREATE_VRF_INSTANCE ', () => {

    const serviceModelId: string = 'serviceModelID';
    const vrfStoreKey : string = 'vrfStoreKey';
    const vrfModelName : string = 'vrfModelName';
    const memberType : string = 'networks';
    let vrfState = vrfReducer(<any>{
         serviceInstance : {
          [serviceModelId] : {
            vrfs : {
              [vrfStoreKey] : {
                [memberType] : {

                }
              }

            }
          }
        }},
      <CreateVRFInstanceAction>{
        type: VrfActions.CREATE_VRF_INSTANCE,
        vrfModel : {
          name : vrfModelName
        },
        serviceModelId : serviceModelId,
        vrfStoreKey : vrfStoreKey
      }).serviceInstance[serviceModelId].vrfs;

    expect(vrfState).toBeDefined();
    expect(vrfState[vrfModelName]).toBeDefined();
  });

  test('#DELETE_ACTION_VRF_INSTANCE', () => {
    let vnfState = vrfReducer(<any>{serviceInstance : {
          'serviceModelId' : {
            vrfs : {
              'vrfStoreKey' : {
                isMissingData : true,
                action : 'None'
              }
            }
          }
        }},
      <DeleteActionVrfInstanceAction>{
        type: VrfActions.DELETE_ACTION_VRF_INSTANCE,
        vrfStoreKey: 'vrfStoreKey',
        serviceId: 'serviceModelId'
      }).serviceInstance['serviceModelId'].vrfs['vrfStoreKey'];

    expect(vnfState).toBeDefined();
    expect(vnfState.action).toEqual(ServiceInstanceActions.None_Delete);
  });

  test('#UNDO_DELETE_ACTION_VRF_INSTANCE', () => {
    let vnfState = vrfReducer(<any>{serviceInstance : {
          'serviceModelId' : {
            vrfs : {
              'vrfStoreKey' : {
                isMissingData : true,
                action : 'Update_Delete'
              }
            }
          }
        }},
      <UndoDeleteActionVrfInstanceAction>{
        type: VrfActions.UNDO_DELETE_ACTION_VRF_INSTANCE,
        vrfStoreKey: 'vrfStoreKey',
        serviceId: 'serviceModelId'
      }).serviceInstance['serviceModelId'].vrfs['vrfStoreKey'];

    expect(vnfState).toBeDefined();
    expect(vnfState.action).toEqual(ServiceInstanceActions.Update);
  });
});


