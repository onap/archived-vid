import {LcpRegion} from "../../../models/lcpRegion";
import {Tenant} from "../../../models/tenant";
import {generalReducer} from "./general.reducers";
import {ChangeInstanceCounterAction, DuplicateBulkInstancesAction, GeneralActions, MergeObjectByPathAction, RemoveInstanceAction, UpdateAicZonesAction, UpdateCategoryParametersAction, UpdateNetworkCollectionFunction, UpdateProductFamiliesAction, UpdateServiceTypesAction, UpdateSubscribersAction, UpdateUserIdAction,} from "./general.actions";
import {SelectOption} from "../../../models/selectOption";
import {ServiceType} from "../../../models/serviceType";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {VnfInstance} from "../../../models/vnfInstance";
import each from "jest-each";

describe('generalReducer', () => {
  test('#UPDATE_LCP_REGIONS_AND_TENANTS : should update lcp region and tenants', () => {
    let lcpRegionsAndTenantsObj = [
      {
        lcpRegionList : [
          new LcpRegion(
            'cloudRegionID',
            'cloudRegionID (cloudOwner)',
            true,
            'cloudOwner'
          )
        ],
        lcpRegionsTenantsMap : {
          "lcpRegion" : [new Tenant({
            "tenantID" : "tenantID",
            "tenantName" : "tenantName",
            "is-permitted" : true
          })]
        }
      }
    ];
    let lcpRegionsAndTenantsState = generalReducer(<any>{serviceInstance : {}},
      <any>{
        type: GeneralActions.UPDATE_LCP_REGIONS_AND_TENANTS,
        lcpRegionsAndTenants : lcpRegionsAndTenantsObj
      })['lcpRegionsAndTenants'];

    expect(lcpRegionsAndTenantsState).toBeDefined();
  });

  test('#UPDATE_SUBSCRIBERS : should update subscribers', () => {
    let subscribersList = [
      new SelectOption({
        id : 'id',
        name : 'name',
        isPermitted : false
      })
    ];
    let subscribersState = generalReducer(<any>
        {
          serviceInstance : {}
        },
      <UpdateSubscribersAction>{
        type: GeneralActions.UPDATE_SUBSCRIBERS,
        subscribers : subscribersList
      })['subscribers'];

    expect(subscribersState).toBeDefined();
    expect(subscribersState[0].id).toEqual(subscribersList[0].id);
    expect(subscribersState[0].isPermitted).toEqual(subscribersList[0].isPermitted);
    expect(subscribersState[0].name).toEqual(subscribersList[0].name);
  });

  test('#UPDATE_PRODUCT_FAMILIES : should update product families', () => {
    let productFamiliesObj = [
      new SelectOption({
        id : 'id',
        name : 'name',
        isPermitted : false
      })
    ];
    let productFamiliesState = generalReducer(<any>{serviceInstance : {}},
      <UpdateProductFamiliesAction>{
        type: GeneralActions.UPDATE_PRODUCT_FAMILIES,
        productFamilies : productFamiliesObj
      })['productFamilies'];

    expect(productFamiliesState).toBeDefined();
    expect(productFamiliesState[0].id).toEqual(productFamiliesObj[0].id);
    expect(productFamiliesState[0].isPermitted).toEqual(productFamiliesObj[0].isPermitted);
    expect(productFamiliesState[0].name).toEqual(productFamiliesObj[0].name);
  });

  test('#UPDATE_AIC_ZONES : should update aic zones', () => {
    let aicZonesObj = [
      new SelectOption({
        id : 'id',
        name : 'name',
        isPermitted : false
      })
    ];
    let aicZonesState = generalReducer(<any>{serviceInstance : {}},
      <UpdateAicZonesAction>{
        type: GeneralActions.UPDATE_AIC_ZONES,
        aicZones : aicZonesObj
      })['aicZones'];

    expect(aicZonesState).toBeDefined();
    expect(aicZonesState[0].id).toEqual(aicZonesObj[0].id);
    expect(aicZonesState[0].isPermitted).toEqual(aicZonesObj[0].isPermitted);
    expect(aicZonesState[0].name).toEqual(aicZonesObj[0].name);
  });

  test('#UPDATE_SERVICE_TYPES : should update service types', () => {
    const subscriberId = 'subscriberId';
    let serviceTypesList : ServiceType[] = [
      new ServiceType('id',{
        'service-type' : 'name',
        'is-permitted' : true
      })
    ];
    let serviceTypesState = generalReducer(<any>
        {
          serviceTypes : {
          }
        },
      <UpdateServiceTypesAction>{
        type: GeneralActions.UPDATE_SERVICE_TYPES,
        subscriberId : subscriberId,
        serviceTypes : serviceTypesList
      })['serviceTypes'][subscriberId];

    expect(serviceTypesState).toBeDefined();
    expect(serviceTypesState[0].id).toEqual(serviceTypesList[0].id);
    expect(serviceTypesState[0].isPermitted).toEqual(serviceTypesList[0].isPermitted);
    expect(serviceTypesState[0].name).toEqual(serviceTypesList[0].name);
  });

  test('#UPDATE_CATEGORY_PARAMETERS : should update category parameters', () => {
    let list = [
      new SelectOption({
        id : 'id',
        name : 'name',
        isPermitted : false
      })
    ];

    const categoryParametersObj = {
      owningEntityList : list,
      projectList : list,
      lineOfBusinessList : list,
      platformList : list
    };

    let categoryParametersState = generalReducer(<any>{serviceInstance : {}},
      <UpdateCategoryParametersAction>{
        type: GeneralActions.UPDATE_CATEGORY_PARAMETERS,
        categoryParameters : categoryParametersObj
      })['categoryParameters'];

    expect(categoryParametersState).toBeDefined();
    expect(categoryParametersState['owningEntityList'][0].id).toEqual(list[0].id);
    expect(categoryParametersState['owningEntityList'][0].isPermitted).toEqual(list[0].isPermitted);
    expect(categoryParametersState['owningEntityList'][0].name).toEqual(list[0].name);

    expect(categoryParametersState['projectList'][0].id).toEqual(list[0].id);
    expect(categoryParametersState['projectList'][0].isPermitted).toEqual(list[0].isPermitted);
    expect(categoryParametersState['projectList'][0].name).toEqual(list[0].name);

    expect(categoryParametersState['lineOfBusinessList'][0].id).toEqual(list[0].id);
    expect(categoryParametersState['lineOfBusinessList'][0].isPermitted).toEqual(list[0].isPermitted);
    expect(categoryParametersState['lineOfBusinessList'][0].name).toEqual(list[0].name);

    expect(categoryParametersState['platformList'][0].id).toEqual(list[0].id);
    expect(categoryParametersState['platformList'][0].isPermitted).toEqual(list[0].isPermitted);
    expect(categoryParametersState['platformList'][0].name).toEqual(list[0].name);
  });

  test('#UPDATE_USER_ID : should update user id', () => {
    const userId = 'userId';
    let userState = generalReducer(<any>{serviceInstance : {}},
      <UpdateUserIdAction>{
        type: GeneralActions.UPDATE_USER_ID,
        userId : userId
      })['userId'];

    expect(userState).toBeDefined();
    expect(userState).toEqual(userId);
  });

  test('#REMOVE_INSTANCE : should delete existing vnf', () => {
    let state = generalReducer(<any>{serviceInstance : {
          'serviceModelId' : {
            vnfs : {
              'modelName' : {}
            }
          }
        }},
      <RemoveInstanceAction>{
        type: GeneralActions.REMOVE_INSTANCE,
        modelName : 'modelName',
        serviceModelId : 'serviceModelId',
        storeKey : 'modelName',
        node : {
          data : {
            type : 'VF'
          }
        }
      });

    expect(state).toBeDefined();
    expect(state.serviceInstance[ 'serviceModelId'].vnfs['modelName']).not.toBeDefined();
  });

  test('#REMOVE_INSTANCE : should delete existing network', () => {
    let state = generalReducer(<any>{serviceInstance : {
          'serviceModelId' : {
            'networks' : {
              'modelName' : {}
            }
          }
        }},
      <RemoveInstanceAction>{
        type: GeneralActions.REMOVE_INSTANCE,
        modelName : 'modelName',
        serviceModelId : 'serviceModelId',
        storeKey : 'modelName',
        node : {
          data : {
            type : 'VL'
          }
        }
      });

    expect(state).toBeDefined();
    expect(state.serviceInstance['serviceModelId'].networks['modelName']).not.toBeDefined();
  });

  test('#REMOVE_INSTANCE : remove VNF should remove VFModules ', () => {
    let state = generalReducer(<any>
        {serviceInstance : {
          'serviceModelId' : {
            'validationCounter' : 2,
            'vnfs' : {
              'SDC_Automation_15Aug 0' : {
                'isMissingData' :true,
                'modelName' : {},
                'vfModules' : {
                  'vfModule_1' : {
                    'vfModule_1_1' : {
                      'isMissingData' :true
                    }
                  }
                }
              }
            }
          }
        }},
      <RemoveInstanceAction>{
        type: GeneralActions.REMOVE_INSTANCE,
        modelName : 'modelName',
        serviceModelId : 'serviceModelId',
        storeKey : 'SDC_Automation_15Aug 0',
        node : {
          data : {
            type : 'VF'
          }
        }
      });

    expect(state).toBeDefined();
    expect(state.serviceInstance['serviceModelId'].vnfs['modelName']).not.toBeDefined();
    expect(state.serviceInstance['serviceModelId'].validationCounter).toEqual(0);
  });

  test('#CHANGE_VNF_INSTANCE_COUNTER : should init existingVNFCounterMap to 1', () => {
    let state = generalReducer(<any>{serviceInstance : {
          'serviceModelId' : {
            vnfs : {
              'vnfName' : { }
            },
            existingVNFCounterMap : {

            }
          }
        }},
      <ChangeInstanceCounterAction>{
        type: GeneralActions.CHANGE_INSTANCE_COUNTER,
        serviceUUID : 'serviceModelId',
        node : {data : {type : 'VF'}},
        UUID : 'vnfUUID',
        changeBy : 1
      });

    expect(state).toBeDefined();
    expect(state.serviceInstance['serviceModelId'].existingVNFCounterMap['vnfUUID']).toEqual(1);
  });

  test('#CHANGE_VNF_INSTANCE_COUNTER : should increase existingVNFCounterMap to 2 if VNF exist', () => {
    let state = generalReducer(<any>{serviceInstance : {
          'serviceModelId' : {
            vnfs : {
              'vnfName' : { }
            },
            existingVNFCounterMap : {
              'vnfUUID' : 1
            }
          }
        }},
      <ChangeInstanceCounterAction>{
        type: GeneralActions.CHANGE_INSTANCE_COUNTER,
        serviceUUID : 'serviceModelId',
        UUID : 'vnfUUID',
        node : {data : {type : 'VF'}},
        changeBy : 1
      });

    expect(state).toBeDefined();
    expect(state.serviceInstance['serviceModelId'].existingVNFCounterMap['vnfUUID']).toEqual(2);
  });

  test('#CHANGE_VNF_INSTANCE_COUNTER : should remove existingVNFCounterMap to 0 remove VNF', () => {

    let state = generalReducer(<any>{serviceInstance : {
          'serviceModelId' : {
            vnfs : {
              'vnfName' : { }
            },
            existingVNFCounterMap : {
              'vnfUUID' : 1
            }
          }
        }},
      <ChangeInstanceCounterAction>{
        type: GeneralActions.CHANGE_INSTANCE_COUNTER,
        serviceUUID : 'serviceModelId',
        UUID : 'vnfUUID',
        node : {data : {type : 'VF'}},
        changeBy : -1
      });

    expect(state).toBeDefined();
    expect(state.serviceInstance['serviceModelId'].existingVNFCounterMap['vnfUUID']).toEqual(0);
  });

  test('#DUPLICATE_BULK_INSTANCES : should duplicate existing VNF/Network', ()=>{
    let existingNames = {
      'vnfOriginalName' : 1
    };

    let vnfInstance: VnfInstance = new VnfInstance();
    vnfInstance.originalName = 'vnfOriginalName';
    vnfInstance.vnfStoreKey = 'vnfStoreKey';

    let cloneObjects = {
      'vnfOriginalName' : vnfInstance,
      'vnfOriginalName:0001' : vnfInstance,
      'vnfOriginalName:0002' : vnfInstance,
      'vnfOriginalName:0003' : vnfInstance
    };

    let node : ITreeNode = <any>{
      data : {
        serviceId : 'serviceModelId',
        vnfStoreKey : 'vnfStoreKey',
        type : 'VF'
      }
    };

    let vnfsState = generalReducer(<any>{serviceInstance : {
          'serviceModelId' : {
            vnfs : {
            },
            existingVNFCounterMap : {}
          }
        }},
      <DuplicateBulkInstancesAction>{
        type: GeneralActions.DUPLICATE_BULK_INSTANCES,
        serviceId: 'serviceModelId',
        modelName: 'modelName',
        originalName : 'modelName',
        objects : cloneObjects,
        existingNames: existingNames,
        node : node
      }).serviceInstance['serviceModelId'].vnfs;

    expect(vnfsState).toBeDefined();
    expect(Object.keys(vnfsState).length).toEqual(4);
  });

  test('#UPDATE_NETWORK_FUNCTION : should update network functions', ()=>{
    let state = generalReducer(<any>{serviceInstance : {
          'serviceModelId' : {
            vnfs : {
            },
            existingVNFCounterMap : {}
          }
        }},
      <UpdateNetworkCollectionFunction>{
        type: GeneralActions.UPDATE_NETWORK_FUNCTION,
        network_function : {
          results : []
        },
        networksAccordingToNetworkCollection : "networksAccordingToNetworkCollection"
      });

    expect(state).toBeDefined();
    expect(state['networkFunctions']).toBeDefined();
  });

  const originalMockObject = {
    remain: 'forever',
    obsolete: 'toBeChange'
  };

  each([
    [
      ['serviceInstance', 'serviceModelId', 'vnfs'],
      {
        remain: 'forever',
        obsolete: 'newValue',
        newField: 'newValue2'
      }
    ],
    [
      ['serviceInstance', 'nowhere', 'somewhere'],
      originalMockObject
    ],
  ]).
  test('#MERGE_OBJECT_BY_PATH: should update some object by path %s', (path, expected) => {
    let state = generalReducer(<any>{serviceInstance : {
          'serviceModelId' : {
            vnfs : originalMockObject,
            existingVNFCounterMap : {}
          }
        }},
      <MergeObjectByPathAction>{
        type: GeneralActions.MERGE_OBJECT_BY_PATH,
        path,
        payload: {
          obsolete: 'newValue',
          newField: 'newValue2'
        }
      });

    expect(state).toBeDefined();
    expect(state.serviceInstance['serviceModelId'].vnfs).toEqual(expected);
  });
});



