import {relatedVnfMemeberReducer} from "./relatedVnfMember.reducers";
import {
  CreateRelatedVnfMemeberInstanceAction,
  DeleteActionRelatedVnfMemeberInstanceAction,
  RelatedVnfActions,
  UndoDeleteActionRelatedVnfMemeberInstanceAction
} from "./relatedVnfMember.actions";
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";


describe('relatedVnfMemberReducer', () => {

  test('#DELETE_ACTION_RELATED_VNF_MEMBER_INSTANCE should change action to delete',() => {
    const serviceUuid: string = 'serviceUuid';
    const actionName: ServiceInstanceActions = ServiceInstanceActions.None;

    let service = relatedVnfMemeberReducer(<any>{
      serviceInstance: {
        'serviceUuid': {
          'vnfGroups' : {
            'vnfGroupStoreKey' : {
              'vnfs' : {
                'vnfStoreKey1' : {
                  'action' : actionName
                }
              }
            }

          }
        }
      }
    }, <DeleteActionRelatedVnfMemeberInstanceAction>{
      type: RelatedVnfActions.DELETE_ACTION_RELATED_VNF_MEMBER_INSTANCE,
      vnfGroupStoreKey: 'vnfGroupStoreKey',
      relatedVnfMemeberStoreKey : 'vnfStoreKey1',
      serviceId : 'serviceUuid'
    });

    expect(service.serviceInstance[serviceUuid].vnfGroups['vnfGroupStoreKey']['vnfs']['vnfStoreKey1'].action).toEqual('None_Delete');
  });

  test('#UNDO_DELETE_ACTION_RELATED_VNF_MEMBER_INSTANCE should undo change action to delete', () => {
    const serviceUuid: string = 'serviceUuid';
    const actionName: ServiceInstanceActions = ServiceInstanceActions.None_Delete;

    let service = relatedVnfMemeberReducer(<any>{
      serviceInstance: {
        'serviceUuid': {
          'vnfGroups' : {
            'vnfGroupStoreKey' : {
              'vnfs' : {
                'vnfStoreKey1' : {
                  'action' : actionName
                }
              }
            }

          }
        }
      }
    }, <UndoDeleteActionRelatedVnfMemeberInstanceAction>{
      type: RelatedVnfActions.UNDO_DELETE_ACTION_RELATED_VNF_MEMBER_INSTANCE,
      vnfGroupStoreKey: 'vnfGroupStoreKey',
      relatedVnfMemeberStoreKey : 'vnfStoreKey1',
      serviceId : 'serviceUuid'
    });

    expect(service.serviceInstance[serviceUuid].vnfGroups['vnfGroupStoreKey']['vnfs']['vnfStoreKey1'].action).toEqual('None');
  });

  test('#REMOVE_RELATED_VNF_MEMBER_INSTANCE should remove instance', () => {
    const serviceUuid: string = 'serviceUuid';
    const actionName: ServiceInstanceActions = ServiceInstanceActions.None_Delete;

    let service = relatedVnfMemeberReducer(<any>{
      serviceInstance: {
        'serviceUuid': {
          'vnfGroups' : {
            'vnfGroupStoreKey' : {
              'vnfs' : {
                'vnfStoreKey1' : {
                  'action' : actionName
                }
              }
            }

          }
        }
      }
    }, <UndoDeleteActionRelatedVnfMemeberInstanceAction>{
      type: RelatedVnfActions.UNDO_DELETE_ACTION_RELATED_VNF_MEMBER_INSTANCE,
      vnfGroupStoreKey: 'vnfGroupStoreKey',
      relatedVnfMemeberStoreKey : 'vnfStoreKey1',
      serviceId : 'serviceUuid'
    });

    expect(service.serviceInstance[serviceUuid].vnfGroups['vnfGroupStoreKey']['vnfs']['vnfStoreKey1'].action).toEqual('None');
  });

  test('#CREATE_RELATED_VNF_MEMBER_INSTANCE should add new relatedVNF instance', () => {
    const serviceUuid: string = 'serviceUuid';
    const actionName: ServiceInstanceActions = ServiceInstanceActions.None_Delete;
    let relatedVnfMember =  {
      "instanceName":"VNF1_INSTANCE_NAME",
      "instanceId":"VNF1_INSTANCE_ID",
      "orchStatus":null,
      "productFamilyId":null,
      "lcpCloudRegionId":"hvf23b",
      "tenantId":"3e9a20a3e89e45f884e09df0cc2d2d2a",
      "tenantName":"APPC-24595-T-IST-02C",
      "modelInfo":{
        "modelInvariantId":"vnf-instance-model-invariant-id",
        "modelVersionId":"7a6ee536-f052-46fa-aa7e-2fca9d674c44",
        "modelVersion":"2.0",
        "modelName":"vf_vEPDG",
        "modelType":"vnf"
      },
      "instanceType":"VNF1_INSTANCE_TYPE",
      "provStatus":null,
      "inMaint":false,
      "uuid":"7a6ee536-f052-46fa-aa7e-2fca9d674c44",
      "originalName":null,
      "legacyRegion":null,
      "lineOfBusiness":null,
      "platformName":null,
      "trackById":"7a6ee536-f052-46fa-aa7e-2fca9d674c44:002",
      "serviceInstanceId":"service-instance-id1",
      "serviceInstanceName":"service-instance-name"
    };

    let service = relatedVnfMemeberReducer(<any>{
      serviceInstance: {
        'serviceUuid': {
          'vnfGroups' : {
            'vnfGroupStoreKey' : { }
          }
        }
      }
    }, <CreateRelatedVnfMemeberInstanceAction>{
      type: RelatedVnfActions.CREATE_RELATED_VNF_MEMBER_INSTANCE,
      relatedVnfMember: relatedVnfMember,
      vnfGroupStoreKey: 'vnfGroupStoreKey',
      serviceId: serviceUuid
    });

    expect(service.serviceInstance[serviceUuid].vnfGroups['vnfGroupStoreKey']['vnfs']['VNF1_INSTANCE_ID'].action).toEqual('Create');
    expect(service.serviceInstance[serviceUuid].vnfGroups['vnfGroupStoreKey']['vnfs']['VNF1_INSTANCE_ID'].instanceId).toEqual('VNF1_INSTANCE_ID');
  });

});



