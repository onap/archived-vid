import {VnfGroupInstance} from "../../../models/vnfGroupInstance";
import {
  CreateVnfGroupInstanceAction,
  DeleteActionVnfGroupInstanceAction,
  SetOptionalMembersVnfGroupInstanceAction,
  UpdateVnfGroupInstanceAction,
  VnfGroupActions
} from "./vnfGroup.actions";
import {vnfGroupReducer} from "./vnfGroup.reducers";
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";
import {VnfMember} from "../../../models/VnfMember";


describe('vnfGroupReducer', () => {
  test('#CREATE_VNF_GROUP_INSTANCE', () => {
    let vnfGroupInstance: VnfGroupInstance = new VnfGroupInstance();
    vnfGroupInstance.isMissingData = false;
    vnfGroupInstance.instanceName = 'instanceName';
    let vnfGroupState = vnfGroupReducer(<any>{serviceInstance : {
          'serviceModelId' : {
            vnfGroups : {

            }
          }
        }},
      <CreateVnfGroupInstanceAction>{
        type: VnfGroupActions.CREATE_VNF_GROUP_INSTANCE,
        vnfGroupInstance : vnfGroupInstance,
        vnfGroupStoreKey : null,
        vnfGroupModelName : 'vnfGroupModelName',
        serviceUuid : 'serviceModelId'
      }).serviceInstance['serviceModelId'].vnfGroups['vnfGroupModelName'];

    expect(vnfGroupState).toBeDefined();
    expect(vnfGroupState.isMissingData).toBeFalsy();
  });

  test('#UPDATE_VNF_GROUP_INSTANCE', () => {
    let vnfGroupInstance: VnfGroupInstance = new VnfGroupInstance();
    vnfGroupInstance.isMissingData = false;
    vnfGroupInstance.instanceName = 'instanceName';
    let vnfGroupState = vnfGroupReducer(<any>{serviceInstance : {
        'serviceModelId' : {
          vnfGroups : {
            'vnfGroupStoreKey' : {
              isMissingData : true
            }
          }
        }
      }},
      <UpdateVnfGroupInstanceAction>{
        type: VnfGroupActions.UPDATE_VNF_GROUP_INSTANCE,
        vnfGroupInstance : new VnfGroupInstance(),
        vnfGroupStoreKey : 'vnfGroupStoreKey',
        vnfGroupModelName : 'vnfGroupModelName',
        serviceUuid : 'serviceModelId'
      }).serviceInstance['serviceModelId'].vnfGroups['vnfGroupStoreKey'];

    expect(vnfGroupState).toBeDefined();
    expect(vnfGroupState.isMissingData).toBeFalsy();
  });

  test('#DELETE_ACTION_VNF_GROUP_INSTANCE', () => {
    let vnfGroupInstance: VnfGroupInstance = new VnfGroupInstance();
    vnfGroupInstance.isMissingData = false;
    vnfGroupInstance.instanceName = 'instanceName';
    vnfGroupInstance.action = ServiceInstanceActions.None;
    let vnfGroupState = vnfGroupReducer(<any>{serviceInstance : {
        'serviceModelId' : {
          vnfGroups : {
            'vnfGroupStoreKey' : {
              isMissingData : true,
              action : 'None'
            }
          }
        }
      }},
      <DeleteActionVnfGroupInstanceAction>{
        type: VnfGroupActions.DELETE_ACTION_VNF_GROUP_INSTANCE,
        vnfGroupStoreKey: 'vnfGroupStoreKey',
        serviceId: 'serviceModelId'
      }).serviceInstance['serviceModelId'].vnfGroups['vnfGroupStoreKey'];

    expect(vnfGroupState).toBeDefined();
    expect(vnfGroupState.action).toEqual(ServiceInstanceActions.None_Delete);
  });

  test('#UNDO_DELETE_ACTION_VNF_GROUP_INSTANCE', () => {
    let vnfGroupInstance: VnfGroupInstance = new VnfGroupInstance();
    vnfGroupInstance.isMissingData = false;
    vnfGroupInstance.instanceName = 'instanceName';
    vnfGroupInstance.action = ServiceInstanceActions.None_Delete;
    let vnfGroupState = vnfGroupReducer(<any>{serviceInstance : {
          'serviceModelId' : {
            vnfGroups : {
              'vnfGroupStoreKey' : {
                isMissingData : true,
                action : 'None_Delete'
              }
            }
          }
        }},
      <DeleteActionVnfGroupInstanceAction>{
        type: VnfGroupActions.UNDO_DELETE_ACTION_VNF_GROUP_INSTANCE,
        vnfGroupStoreKey: 'vnfGroupStoreKey',
        serviceId: 'serviceModelId'
      }).serviceInstance['serviceModelId'].vnfGroups['vnfGroupStoreKey'];

    expect(vnfGroupState).toBeDefined();
    expect(vnfGroupState.action).toEqual(ServiceInstanceActions.None);
  });

  test('#SET_OPTIONAL_MEMBERS_VNF_GROUP_INSTANCE', () => {
    let vnf1: VnfMember = new VnfMember();
    vnf1.serviceInstanceId = 'aa';
    vnf1.instanceId = 'aaa';
    let vnf2: VnfMember = new VnfMember();
    vnf2.serviceInstanceId = 'bb';
    vnf2.instanceId = 'bbb';
    let optionalGroupMembersMap = vnfGroupReducer(<any>{
        serviceInstance: {
          'serviceModelId': {
            optionalGroupMembersMap : {}
          }
        }
      },
      <SetOptionalMembersVnfGroupInstanceAction>{
        type: VnfGroupActions.SET_OPTIONAL_MEMBERS_VNF_GROUP_INSTANCE,
        path: 'path1',
        serviceId: 'serviceModelId',
        vnfMembers: [vnf1, vnf2]
      }).serviceInstance['serviceModelId'].optionalGroupMembersMap;

    optionalGroupMembersMap['path1']= [vnf1, vnf2];
    expect(optionalGroupMembersMap).toEqual({'path1':[vnf1, vnf2]});
  });
});



