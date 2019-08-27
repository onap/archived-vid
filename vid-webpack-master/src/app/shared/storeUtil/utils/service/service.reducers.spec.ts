import {ServiceInstance} from "../../../models/serviceInstance";
import {
  AddServiceAction,
  ChangeServiceDirty,
  CreateServiceInstanceAction,
  DeleteServiceInstanceAction,
  ServiceActions,
  UpdateServiceInstanceAction,
  UpdateServiceModelAction, UpgradeServiceAction
} from "./service.actions";
import {serviceReducer} from "./service.reducers";
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";
import {VidNotions} from "../../../models/vidNotions";

describe('serviceReducer', () => {

  test('#UPDATE_SERVICE_INSTANCE should update exiting instance without change his child', () => {
    const serviceUuid: string = 'serviceUuid';
    const actionName: ServiceInstanceActions = ServiceInstanceActions.Create;

    const elemntThatShouldNotOverideOnUpdateService = {
      vnfs: {
        "2017-388_PASQUALE-vPE 0": {
          "action": "Create",
          "inMaint": false,
          "rollbackOnFailure": "true",
          "originalName": "2017-388_PASQUALE-vPE 0",
          "isMissingData": false,
          "trackById": "eymgwlevh54",
          "vfModules": {},
          "vnfStoreKey": "2017-388_PASQUALE-vPE 0",
          "uuid": "afacccf6-397d-45d6-b5ae-94c39734b168",
          "productFamilyId": "e433710f-9217-458d-a79d-1c7aff376d89",
          "lcpCloudRegionId": "AAIAIC25",
          "tenantId": "092eb9e8e4b7412e8787dd091bc58e86",
          "lineOfBusiness": "ONAP",
          "platformName": "platform",
          "modelInfo": {
            "modelInvariantId": "72e465fe-71b1-4e7b-b5ed-9496118ff7a8",
            "modelVersionId": "afacccf6-397d-45d6-b5ae-94c39734b168",
            "modelName": "2017-388_PASQUALE-vPE",
            "modelVersion": "4.0",
            "modelCustomizationId": "b3c76f73-eeb5-4fb6-9d31-72a889f1811c",
            "modelCustomizationName": "2017-388_PASQUALE-vPE 0",
            "uuid": "afacccf6-397d-45d6-b5ae-94c39734b168",
            "modelUniqueId": "b3c76f73-eeb5-4fb6-9d31-72a889f1811c"
          },
          "instanceName": "2017-388_PASQUALE-vPEAjXzainstanceName",
          "legacyRegion": "some legacy region",
          "instanceParams": [
            {
              "vnf_config_template_version": "17.2",
              "bandwidth_units": "Gbps",
              "bandwidth": "10",
              "AIC_CLLI": "ATLMY8GA",
              "ASN": "AV_vPE",
              "vnf_instance_name": "mtnj309me6"
            }
          ]
        },
        "2017-488_PASQUALE-vPE 0": {
          "action": "Create",
          "inMaint": false,
          "rollbackOnFailure": "true",
          "originalName": "2017-488_PASQUALE-vPE 0",
          "isMissingData": false,
          "trackById": "xr6o2782z7",
          "vfModules": {
            "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0wmkjw": {
                "isMissingData": true,
                "sdncPreReload": null,
                "modelInfo": {
                  "modelType": "VFmodule",
                  "modelInvariantId": "b34833bb-6aa9-4ad6-a831-70b06367a091",
                  "modelVersionId": "f8360508-3f17-4414-a2ed-6bc71161e8db",
                  "modelName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                  "modelVersion": "5",
                  "modelCustomizationId": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3",
                  "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                  "modelUniqueId": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3"
                },
                "instanceParams": [
                  {}
                ],
                "trackById": "a19sjb1ez2"
              }
            }
          },
          "vnfStoreKey": "2017-488_PASQUALE-vPE 0",
          "uuid": "69e09f68-8b63-4cc9-b9ff-860960b5db09",
          "productFamilyId": "e433710f-9217-458d-a79d-1c7aff376d89",
          "lcpCloudRegionId": "AAIAIC25",
          "tenantId": "092eb9e8e4b7412e8787dd091bc58e86",
          "lineOfBusiness": "ONAP",
          "platformName": "platform",
          "modelInfo": {
            "modelInvariantId": "72e465fe-71b1-4e7b-b5ed-9496118ff7a8",
            "modelVersionId": "69e09f68-8b63-4cc9-b9ff-860960b5db09",
            "modelName": "2017-488_PASQUALE-vPE",
            "modelVersion": "5.0",
            "modelCustomizationId": "1da7b585-5e61-4993-b95e-8e6606c81e45",
            "modelCustomizationName": "2017-488_PASQUALE-vPE 0",
            "uuid": "69e09f68-8b63-4cc9-b9ff-860960b5db09",
            "modelUniqueId": "1da7b585-5e61-4993-b95e-8e6606c81e45"
          },
          "instanceName": "2017-488_PASQUALE-vPEVNFinstancename",
          "legacyRegion": "some legacy region",
          "instanceParams": [
            {
              "vnf_config_template_version": "17.2",
              "bandwidth_units": "Gbps",
              "bandwidth": "10",
              "AIC_CLLI": "ATLMY8GA",
              "ASN": "AV_vPE",
              "vnf_instance_name": "mtnj309me6"
            }
          ]
        }
      },
      existingVNFCounterMap: {
        "b3c76f73-eeb5-4fb6-9d31-72a889f1811c": 1,
        "1da7b585-5e61-4993-b95e-8e6606c81e45": 1
      },
      existingVnfGroupCounterMap: {},
      existingNetworksCounterMap: {},
      optionalGroupMembersMap : {},
      networks : {},
      vnfGroups : {}
    };


    let service = serviceReducer(<any>{
      "serviceInstance": {
        "6b528779-44a3-4472-bdff-9cd15ec93450": {
          "action": "Create",
          "isDirty": true,
          "vnfs": elemntThatShouldNotOverideOnUpdateService.vnfs,
          "instanceParams": [
            {
              "2017488_pasqualevpe0_ASN": "AV_vPE"
            }
          ],
          "validationCounter": 1,
          "existingNames": {
            "ajxzainstancename": "",
            "2017-488_pasquale-vpevnfinstancename": "",
            "2017-388_pasquale-vpeajxzainstancename": ""
          },
          "existingVNFCounterMap": elemntThatShouldNotOverideOnUpdateService.existingVNFCounterMap,
          "existingVnfGroupCounterMap": elemntThatShouldNotOverideOnUpdateService.existingVnfGroupCounterMap,
          "existingNetworksCounterMap": elemntThatShouldNotOverideOnUpdateService.existingNetworksCounterMap,
          "optionalGroupMembersMap":elemntThatShouldNotOverideOnUpdateService.optionalGroupMembersMap,
          "networks": elemntThatShouldNotOverideOnUpdateService.networks,
          "vnfGroups": elemntThatShouldNotOverideOnUpdateService.vnfGroups,
          "bulkSize": "3",
          "instanceName": "AjXzainstancename",
          "globalSubscriberId": "e433710f-9217-458d-a79d-1c7aff376d89",
          "subscriptionServiceType": "TYLER SILVIA",
          "owningEntityId": "d61e6f2d-12fa-4cc2-91df-7c244011d6fc",
          "productFamilyId": "e433710f-9217-458d-a79d-1c7aff376d89",
          "lcpCloudRegionId": "hvf6",
          "tenantId": "bae71557c5bb4d5aac6743a4e5f1d054",
          "aicZoneId": "NFT1",
          "projectName": "WATKINS",
          "rollbackOnFailure": "true",
          "aicZoneName": "NFTJSSSS-NFT1",
          "owningEntityName": "WayneHolland",
          "testApi": "VNF_API",
          "tenantName": "AIN Web Tool-15-D-testalexandria",
          "modelInfo": {
            "modelInvariantId": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
            "modelVersionId": "6b528779-44a3-4472-bdff-9cd15ec93450",
            "modelName": "action-data",
            "modelVersion": "1.0",
            "uuid": "6b528779-44a3-4472-bdff-9cd15ec93450",
            "modelUniqueId": "6b528779-44a3-4472-bdff-9cd15ec93450"
          },
          "isALaCarte": false,
          "name": "action-data",
          "version": "1.0",
          "description": "",
          "category": "",
          "uuid": "6b528779-44a3-4472-bdff-9cd15ec93450",
          "invariantUuid": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
          "serviceType": "",
          "serviceRole": "",
          "vidNotions": {
            "instantiationUI": "legacy",
            "modelCategory": "other",
            "viewEditUI": "legacy"
          },
          "isEcompGeneratedNaming": false,
          "isMultiStepDesign": false
        }
      },
    }, <UpdateServiceInstanceAction>{
      type: ServiceActions.UPDATE_SERVICE_INSTANCE,
      serviceUuid: "6b528779-44a3-4472-bdff-9cd15ec93450",
      serviceInstance: <any>{
        aicZoneId: "ATL53",
        aicZoneName: "AAIATLTE-ATL53",
        bulkSize: 1,
        category: "",
        description: "",
        globalSubscriberId: "e433710f-9217-458d-a79d-1c7aff376d89",
        instanceName: "yoav",
        instanceParams: [{}],
        invariantUuid: "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
        isALaCarte: false,
        isEcompGeneratedNaming: false,
        isMultiStepDesign: false,
        lcpCloudRegionId: "AAIAIC25",
        modelInfo: {
          modelInvariantId: "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
          modelVersionId: "6b528779-44a3-4472-bdff-9cd15ec93450",
          modelName: "action-data",
          modelVersion: "1.0"
        },
        name: "action-data",
        owningEntityId: "d61e6f2d-12fa-4cc2-91df-7c244011d6fc",
        owningEntityName: "WayneHolland",
        productFamilyId: "d8a6ed93-251c-47ca-adc9-86671fd19f4c",
        projectName: "WATKINS",
        rollbackOnFailure: "true",
        serviceRole: "",
        serviceType: "",
        subscriptionServiceType: "TYLER SILVIA",
        tenantId: "092eb9e8e4b7412e8787dd091bc58e86",
        tenantName: "USP-SIP-IC-24335-T-01",
        testApi: "VNF_API",
        uuid: "6b528779-44a3-4472-bdff-9cd15ec93450",
        version: "1.0",
        vidNotions: {instantiationUI: "legacy", modelCategory: "other", viewEditUI: "legacy"}
      }
    });
    expect(service.serviceInstance["6b528779-44a3-4472-bdff-9cd15ec93450"]).toBeDefined();

    for(const element in elemntThatShouldNotOverideOnUpdateService){
      expect(service.serviceInstance["6b528779-44a3-4472-bdff-9cd15ec93450"][element]).toEqual(elemntThatShouldNotOverideOnUpdateService[element]);
    }

    expect(service.serviceInstance["6b528779-44a3-4472-bdff-9cd15ec93450"].aicZoneId).toEqual("ATL53");
    expect(service.serviceInstance["6b528779-44a3-4472-bdff-9cd15ec93450"].owningEntityId).toEqual("d61e6f2d-12fa-4cc2-91df-7c244011d6fc");
    expect(service.serviceInstance["6b528779-44a3-4472-bdff-9cd15ec93450"].owningEntityName).toEqual("WayneHolland");
    expect(service.serviceInstance["6b528779-44a3-4472-bdff-9cd15ec93450"].productFamilyId).toEqual("d8a6ed93-251c-47ca-adc9-86671fd19f4c");
    expect(service.serviceInstance["6b528779-44a3-4472-bdff-9cd15ec93450"].projectName).toEqual("WATKINS");
    expect(service.serviceInstance["6b528779-44a3-4472-bdff-9cd15ec93450"].rollbackOnFailure).toEqual("true");
    expect(service.serviceInstance["6b528779-44a3-4472-bdff-9cd15ec93450"].subscriptionServiceType).toEqual("TYLER SILVIA");
    expect(service.serviceInstance["6b528779-44a3-4472-bdff-9cd15ec93450"].tenantId).toEqual("092eb9e8e4b7412e8787dd091bc58e86");

  });

  test('#ADD_SERVICE_ACTION should add action to the service', () => {
    const serviceUuid: string = 'serviceUuid';
    const actionName: ServiceInstanceActions = ServiceInstanceActions.Create;

    let service = serviceReducer(<any>{
      serviceInstance: {
        'serviceUuid': {}
      }
    }, <AddServiceAction>{
      type: ServiceActions.ADD_SERVICE_ACTION,
      serviceUuid: 'serviceUuid',
      action: actionName
    });
    expect(service.serviceInstance[serviceUuid]['action']).toEqual(actionName);
  });

  test('#CREATE_SERVICE_INSTANCE shall put the instance on state with all its values ', () => {
    const serviceUuid: string = 'serviceUuid';
    const vidNotions:VidNotions = {
      instantiationUI: "some5G",
      modelCategory: "5G Provider Network",
      viewEditUI: "legacy",
      instantiationType: "ALaCarte"
    };

    let serviceInstanceObject: ServiceInstance = <any>{
      isDirty: false,
      instanceName: 'instanceName',
      isEcompGeneratedNaming: false,
      globalSubscriberId: 'globalSubscriberId',
      productFamilyId: 'productFamilyId',
      subscriptionServiceType: 'subscriptionServiceType',
      lcpCloudRegionId: 'lcpCloudRegionId',
      tenantId: 'tenantId',
      tenantName: 'tenantName',
      aicZoneId: 'aicZoneId',
      aicZoneName: 'aicZoneName',
      projectName: 'projectName',
      owningEntityId: 'owningEntityId',
      owningEntityName: 'owningEntityName',
      existingVnfGroupCounterMap: {},
      existingVNFCounterMap: {},
      existingNetworksCounterMap: {},
      pause: false,
      bulkSize: 1,
      vnfs: {},
      vnfGroups: {},
      networks: {},
      instanceParams: [],
      rollbackOnFailure: false,
      subscriberName: 'subscriberName',
      validationCounter: 0,
      existingNames: {},
      action: ServiceInstanceActions.Create
    };

    let serviceState = serviceReducer(
      <any>{
        serviceInstance: {},
        serviceHierarchy: {
          [serviceUuid]: {
            service: {
              vidNotions: vidNotions
            }
          }
        }
      },
      <CreateServiceInstanceAction>{
        type: ServiceActions.CREATE_SERVICE_INSTANCE,
        serviceUuid: serviceUuid,
        serviceInstance: serviceInstanceObject
      }).serviceInstance[serviceUuid];

    expect(serviceState.instanceName).toEqual(serviceInstanceObject.instanceName);
    expect(serviceState.isEcompGeneratedNaming).toEqual(serviceInstanceObject.isEcompGeneratedNaming);
    expect(serviceState.globalSubscriberId).toEqual(serviceInstanceObject.globalSubscriberId);
    expect(serviceState.productFamilyId).toEqual(serviceInstanceObject.productFamilyId);
    expect(serviceState.subscriptionServiceType).toEqual(serviceInstanceObject.subscriptionServiceType);
    expect(serviceState.lcpCloudRegionId).toEqual(serviceInstanceObject.lcpCloudRegionId);
    expect(serviceState.tenantId).toEqual(serviceInstanceObject.tenantId);
    expect(serviceState.tenantName).toEqual(serviceInstanceObject.tenantName);
    expect(serviceState.aicZoneId).toEqual(serviceInstanceObject.aicZoneId);
    expect(serviceState.aicZoneName).toEqual(serviceInstanceObject.aicZoneName);
    expect(serviceState.projectName).toEqual(serviceInstanceObject.projectName);
    expect(serviceState.owningEntityId).toEqual(serviceInstanceObject.owningEntityId);
    expect(serviceState.owningEntityName).toEqual(serviceInstanceObject.owningEntityName);
    expect(serviceState.pause).toEqual(serviceInstanceObject.pause);
    expect(serviceState.bulkSize).toEqual(serviceInstanceObject.bulkSize);
    expect(serviceState.vnfs).toEqual(serviceInstanceObject.vnfs);
    expect(serviceState.instanceParams).toEqual(serviceInstanceObject.instanceParams);
    expect(serviceState.rollbackOnFailure).toEqual(serviceInstanceObject.rollbackOnFailure);
    expect(serviceState.subscriberName).toEqual(serviceInstanceObject.subscriberName);
    expect(serviceState.vidNotions).toEqual(vidNotions);
  });


  test('#DELETE_ALL_SERVICE_INSTANCES should delete all services', () => {
    const state = serviceReducer(<any>{
        serviceInstance: {
          'service-1': {},
          'service-2': {}
        }
      },
      <CreateServiceInstanceAction>{
        type: ServiceActions.DELETE_ALL_SERVICE_INSTANCES
      });

    expect(state.serviceInstance['service-1']).toBeUndefined();
    expect(state.serviceInstance['service-2']).toBeUndefined();
  });

  test('#DELETE_SERVICE_INSTANCE should delete service', () => {
    const state = serviceReducer(<any>{
        serviceInstance: {
          'service-1': {}
        }
      },
      <DeleteServiceInstanceAction>{
        type: ServiceActions.DELETE_ALL_SERVICE_INSTANCES
      });

    expect(state.serviceInstance['service-1']).toBeUndefined();
  });

  test('#UPDATE_MODEL should update service model ', () => {
    const state = serviceReducer(<any>{
        serviceHierarchy: {}
      },
      <UpdateServiceModelAction>{
        type: ServiceActions.UPDATE_MODEL,
        serviceHierarchy: {
          service: {
            uuid: 'uuid-1'
          }
        }
      });

    expect(state.serviceHierarchy['uuid-1'].service).toBeDefined();
  });

  test('#UPDATE_MODEL should update service model with vnfGroups ', () => {
    const state = serviceReducer(<any>{
        serviceHierarchy: {}
      },
      <UpdateServiceModelAction>{
        type: ServiceActions.UPDATE_MODEL,
        serviceHierarchy: {
          service: {
            uuid: 'uuid-1'
          },
          vnfs: {},
          vnfGroups: {
            'vnfGrouop_1': {
              uuid: 'vnfGroup_uuid_1',
              name: 'vnfGroup_name_1',
              type: 'Group'
            },
            'vnfGrouop_2': {
              uuid: 'vnfGroup_uuid_2',
              name: 'vnfGroup_name_2',
              type: 'Group'
            }
          }
        }
      });

    expect(state.serviceHierarchy['uuid-1']).toBeDefined();
    expect(state.serviceHierarchy['uuid-1'].vnfGroups['vnfGrouop_1']).toBeDefined();
    expect(state.serviceHierarchy['uuid-1'].vnfGroups['vnfGrouop_2']).toBeDefined();
  });

  test('#CHANGE_SERVICE_IS_DIRTY should update service isDirty flag : service is not dirty ', () => {
    const state = serviceReducer(<any>{
        serviceInstance: {
          'serviceId': {
            action: ServiceInstanceActions.None,
            'vnfs': {
              'vnf1': {
                action: ServiceInstanceActions.None
              },
              'vnf2': {
                action: ServiceInstanceActions.None
              }
            }

          }
        }
      },
      <ChangeServiceDirty>{
        type: ServiceActions.CHANGE_SERVICE_IS_DIRTY,
        nodes: [
          {
            action: ServiceInstanceActions.None
          },
          {
            action: ServiceInstanceActions.None
          }],
        serviceId: 'serviceId'

      });

    expect(state.serviceInstance['serviceId'].isDirty).toBeFalsy();
  });

  test('#CHANGE_SERVICE_IS_DIRTY should update service isDirty flag : service is dirty should return true', () => {
    const state = serviceReducer(<any>{
        serviceInstance: {
          'serviceId': {
            action: ServiceInstanceActions.Create

          }
        }
      },
      <ChangeServiceDirty>{
        type: ServiceActions.CHANGE_SERVICE_IS_DIRTY,
        nodes: [
          {
            action: ServiceInstanceActions.None
          },
          {
            action: ServiceInstanceActions.None
          }],
        serviceId: 'serviceId'

      });

    expect(state.serviceInstance['serviceId'].isDirty).toBeTruthy();
  });

  test('#CHANGE_SERVICE_IS_DIRTY should update service isDirty flag : vnf is dirty ', () => {
    const state = serviceReducer(<any>{
        serviceInstance: {
          'serviceId': {
            action: ServiceInstanceActions.None,
            'vnfs': {
              'vnf1': {
                action: ServiceInstanceActions.None
              },
              'vnf2': {
                action: ServiceInstanceActions.Create
              }
            }

          }
        }
      },
      <ChangeServiceDirty>{
        type: ServiceActions.CHANGE_SERVICE_IS_DIRTY,
        nodes: [
          {
            action: ServiceInstanceActions.None
          },
          {
            action: ServiceInstanceActions.Create
          }],
        serviceId: 'serviceId'

      });

    expect(state.serviceInstance['serviceId'].isDirty).toBeTruthy();
  });

  test('#UPGRADE_SERVICE should update service action to _Upgrade', () => {
    const state = serviceReducer(<any>{
        serviceInstance: {
          'serviceId': {
            action: ServiceInstanceActions.None,
            upgradedVFMSonsCounter: 0,
            'vnfs': {
              'vnf1': {
                action: ServiceInstanceActions.None
              },
              'vnf2': {
                action: ServiceInstanceActions.Create
              }
            }

          }
        }
      },
      <UpgradeServiceAction> {
        type: ServiceActions.UPGRADE_SERVICE_ACTION,
        serviceUuid: 'serviceId'
      });

    expect(state.serviceInstance['serviceId'].isUpgraded).toBeTruthy();
    expect(state.serviceInstance['serviceId'].action).toEqual(ServiceInstanceActions.None_Upgrade);
    expect(state.serviceInstance['serviceId'].upgradedVFMSonsCounter).toEqual(1);

  });

  test('#UNDO_UPGRADE_SERVICE should cancel the upgrade action back to None', () => {
    const state = serviceReducer(<any>{
        serviceInstance: {
          'serviceId': {
            isUpgraded: true,
            upgradedVFMSonsCounter: 1,
            action: ServiceInstanceActions.None_Upgrade,
            'vnfs': {
              'vnf1': {
                action: ServiceInstanceActions.None_Upgrade
              },
              'vnf2': {
                action: ServiceInstanceActions.Create
              }
            }

          }
        }
    },
    <UpgradeServiceAction> {
        type: ServiceActions.UNDO_UPGRADE_SERVICE_ACTION,
        serviceUuid: 'serviceId'
    });

    expect(state.serviceInstance['serviceId'].isUpgraded).toBeFalsy();
    expect(state.serviceInstance['serviceId'].action).toEqual(ServiceInstanceActions.None);
    expect(state.serviceInstance['serviceId'].upgradedVFMSonsCounter).toEqual(0);
  });

});



