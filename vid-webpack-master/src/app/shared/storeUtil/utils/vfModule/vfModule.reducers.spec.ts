import {
  CreateVFModuleInstanceAction,
  DeleteActionVfModuleInstanceAction, DeleteVFModuleField,
  DeleteVfModuleInstanceAction,
  UndoDeleteActionVfModuleInstanceAction,
  UpdateVFModluePosition, UpdateVFModuleField,
  UpgradeVfModuleInstanceAction,
  VfModuleActions
} from "./vfModule.actions";
import {vfModuleReducer} from "./vfModule.reducers";
import {VfModuleInstance} from "../../../models/vfModuleInstance";
import {ServiceInstanceActions} from "../../../models/serviceInstanceActions";


describe('vfModuleReducer', () => {
  test('#REMOVE_VNF_MODULE_INSTANCE : should delete existing vnf module by dynamicModelName', () => {
    let state = vfModuleReducer(<any>{
        serviceInstance: {
          'serviceModelId': {
            vnfs: {
              'vfName': {
                vfModules: {
                  'modelName': {
                    'dynamicModelName1': {},
                    'dynamicModelName2': {},
                  }
                }
              }
            }
          }
        }
      },
      <DeleteVfModuleInstanceAction>{
        type: VfModuleActions.REMOVE_VNF_MODULE_INSTANCE,
        modelName: 'modelName',
        vfName: 'vfName',
        vnfStoreKey: 'vfName',
        serviceModelId: 'serviceModelId',
        dynamicModelName: 'dynamicModelName1'
      });

    expect(state).toBeDefined();
    expect(state.serviceInstance['serviceModelId'].vnfs['vfName'].vfModules['modelName']['dynamicModelName2']).toBeDefined();
    expect(state.serviceInstance['serviceModelId'].vnfs['vfName'].vfModules['modelName']['dynamicModelName1']).not.toBeDefined();
  });

  test('#DELETE_LAST_VNF_MODULE_INSTANCE : should delete existing vnf module', () => {
    let state = vfModuleReducer(<any>{
        serviceInstance: {
          'serviceModelId': {
            vnfs: {
              'vfName': {
                vfModules: {
                  'modelName': {
                    'dynamicModelName': {}
                  }
                }
              }
            }
          }
        }
      },
      <DeleteVfModuleInstanceAction>{
        type: VfModuleActions.REMOVE_VNF_MODULE_INSTANCE,
        modelName: 'modelName',
        vfName: 'vfName',
        vnfStoreKey: 'vfName',
        serviceModelId: 'serviceModelId',
        dynamicModelName: 'dynamicModelName'
      });

    expect(state).toBeDefined();
    expect(state.serviceInstance['serviceModelId'].vnfs['vfName'].vfModules['modelName']).not.toBeDefined();
  });

  test('#CREATE_VF_MODULE: should create new vfModule to existing VNF', () => {
    let vfModuleInstance: VfModuleInstance = new VfModuleInstance();
    vfModuleInstance.instanceName = 'instanceName';
    vfModuleInstance.isMissingData = false;
    vfModuleInstance.volumeGroupName = 'volumeGroupName';
    let vfModule = vfModuleReducer(<any>{
        serviceInstance: {
          'serviceUuid': {
            vnfs: {
              'vnfStoreKey': {
                'vfModules': {}
              }
            }
          }
        }
      },
      <CreateVFModuleInstanceAction>{
        type: VfModuleActions.CREATE_VF_MODULE,
        vfId: 'vfId',
        vfInstance: new VfModuleInstance(),
        vnfStoreKey: 'vnfStoreKey',
        serviceUuid: 'serviceUuid',
        index: 1
      }).serviceInstance['serviceUuid'].vnfs['vnfStoreKey'].vfModules;

    let firstVfModuleName = Object.keys(vfModule)[0];
    expect(vfModule[firstVfModuleName]).toBeDefined();
    expect(vfModule[firstVfModuleName].isMissingData).toBeFalsy();
  });

  test('#UPDATE_VFMODULE_FEILD: should update field with some value', () => {
    const newFieldName = 'newFieldName';
    const newFieldValue = 'newFieldValue';
    let oldState = {
      serviceHierarchy: {
        'serviceModelId': {}
      },
      serviceInstance: {
        'serviceModelId': {
          vnfs: {
            'vnfStoreKey': {
              vfModules: {
                'modelName': {
                  'dynamicModelName1': {
                    isMissingData: true,
                    action: 'None'
                  },
                  'dynamicModelName2': {},
                }
              }
            }
          }
        }
      }
    };

    let newState = vfModuleReducer(<any>oldState,
      <UpdateVFModuleField>{
        type: VfModuleActions.UPDATE_VFMODULE_FEILD,
        dynamicModelName: 'dynamicModelName1',
        vnfStoreKey: 'vnfStoreKey',
        serviceId: 'serviceModelId',
        modelName: 'modelName',
        fieldName: newFieldName,
        fieldValue: newFieldValue
      });

    let vfModule = newState.serviceInstance['serviceModelId'].vnfs['vnfStoreKey'].vfModules['modelName']['dynamicModelName1'];

    expect(vfModule[newFieldName]).toEqual(newFieldValue);
  });

  test('#DELETE_VFMODULE_FEILD: should update field with some value', () => {
      const deleteFieldName = 'deleteFieldName';
      let oldState = {
        serviceHierarchy: {
          'serviceModelId': {}
        },
        serviceInstance: {
          'serviceModelId': {
            vnfs: {
              'vnfStoreKey': {
                vfModules: {
                  'modelName': {
                    'dynamicModelName1': {
                      isMissingData: true,
                      [deleteFieldName]: true,
                      action: 'None'
                    },
                    'dynamicModelName2': {},
                  }
                }
              }
            }
          }
        }
      };
      let newState = vfModuleReducer(<any>oldState,
    <DeleteVFModuleField>{
      type: VfModuleActions.DELETE_VFMODULE_FIELD,
      dynamicModelName: 'dynamicModelName1',
      vnfStoreKey: 'vnfStoreKey',
      serviceId: 'serviceModelId',
      modelName: 'modelName',
      deleteFieldName: deleteFieldName,
    });

  let vfModule = newState.serviceInstance['serviceModelId'].vnfs['vnfStoreKey'].vfModules['modelName']['dynamicModelName1'];

  expect(vfModule[deleteFieldName]).toBeUndefined();
});

test('#UPDATE_VFMODULE_POSITION: should update position', () => {
  let vfModule = vfModuleReducer(<any>{
      serviceHierarchy: {
        'serviceModelId': {}
      },
      serviceInstance: {
        'serviceModelId': {
          vnfs: {
            'vfName': {
              vfModules: {
                'modelName': {
                  'dynamicModelName': {
                    isMissingData: true
                  }
                }
              }
            }
          }
        }
      }
    },
    <UpdateVFModluePosition>{
      type: VfModuleActions.UPDATE_VFMODULE_POSITION,
      node: {
        position: 1,
        dynamicModelName: "dynamicModelName",
        modelName: "modelName"
      },
      instanceId: "serviceModelId",
      vnfStoreKey: "vfName"

    }).serviceInstance['serviceModelId'].vnfs['vfName'].vfModules["modelName"]["dynamicModelName"];

  expect(vfModule.position).toEqual(1);
});

test('#DELETE_ACTION_VF_MODULE_INSTANCE', () => {
  let vfModule = vfModuleReducer(<any>{
      serviceHierarchy: {
        'serviceModelId': {}
      },
      serviceInstance: {
        'serviceModelId': {
          vnfs: {
            'vnfStoreKey': {
              vfModules: {
                'modelName': {
                  'dynamicModelName1': {
                    isMissingData: true,
                    action: 'None'
                  },
                  'dynamicModelName2': {},
                }
              }
            }
          }
        }
      }
    },
    <DeleteActionVfModuleInstanceAction>{
      type: VfModuleActions.DELETE_ACTION_VF_MODULE_INSTANCE,
      dynamicModelName: 'dynamicModelName1',
      vnfStoreKey: 'vnfStoreKey',
      serviceId: 'serviceModelId'
    }).serviceInstance['serviceModelId'].vnfs['vnfStoreKey'].vfModules['modelName']['dynamicModelName1'];

  expect(vfModule).toBeDefined();
  expect(vfModule.isMissingData).toBeTruthy();
  expect(vfModule.action).toEqual(ServiceInstanceActions.None_Delete);
});

test('#UNDO_DELETE_ACTION_VF_MODULE_INSTANCE', () => {
  let vfModule = vfModuleReducer(<any>{
      serviceHierarchy: {
        'serviceModelId': {}
      },
      serviceInstance: {
        'serviceModelId': {
          vnfs: {
            'vnfStoreKey': {
              vfModules: {
                'modelName': {
                  'dynamicModelName1': {
                    isMissingData: true,
                    action: 'None_Delete'
                  },
                  'dynamicModelName2': {},
                }
              }
            }
          }
        }
      }
    },
    <UndoDeleteActionVfModuleInstanceAction>{
      type: VfModuleActions.UNDO_DELETE_ACTION_VF_MODULE_INSTANCE,
      dynamicModelName: 'dynamicModelName1',
      vnfStoreKey: 'vnfStoreKey',
      serviceId: 'serviceModelId'
    }).serviceInstance['serviceModelId'].vnfs['vnfStoreKey'].vfModules['modelName']['dynamicModelName1'];

  expect(vfModule).toBeDefined();
  expect(vfModule.action).toEqual(ServiceInstanceActions.None);
});

test('#UPGRADE_VFMODULE', () => {
  let vfModule = vfModuleReducer(<any>{
      serviceHierarchy: {
        'serviceModelId': {}
      },
      serviceInstance: {
        'serviceModelId': {
          vnfs: {
            'vnfStoreKey': {
              vfModules: {
                'modelName': {
                  'dynamicModelName1': {
                    isMissingData: true,
                    action: 'None'
                  },
                  'dynamicModelName2': {},
                }
              }
            }
          }
        }
      }
    },
    <UpgradeVfModuleInstanceAction>{
      type: VfModuleActions.UPGRADE_VFMODULE,
      dynamicModelName: 'dynamicModelName1',
      vnfStoreKey: 'vnfStoreKey',
      serviceId: 'serviceModelId',
      modelName: 'modelName'
    }).serviceInstance['serviceModelId'].vnfs['vnfStoreKey'].vfModules['modelName']['dynamicModelName1'];

  expect(vfModule.action).toEqual(ServiceInstanceActions.None_Upgrade);
});

test('#UNDO_UPGRADE_VFMODULE', () => {
  let vfModule = vfModuleReducer(<any>{
      serviceHierarchy: {
        'serviceModelId': {}
      },
      serviceInstance: {
        'serviceModelId': {
          vnfs: {
            'vnfStoreKey': {
              vfModules: {
                'modelName': {
                  'dynamicModelName1': {
                    isMissingData: true,
                    action: 'None_Upgrade'
                  },
                  'dynamicModelName2': {},
                }
              }
            }
          }
        }
      }
    },
    <UpgradeVfModuleInstanceAction>{
      type: VfModuleActions.UNDO_UPGRADE_VFMODULE_ACTION,
      dynamicModelName: 'dynamicModelName1',
      vnfStoreKey: 'vnfStoreKey',
      serviceId: 'serviceModelId',
      modelName: 'modelName'
    }).serviceInstance['serviceModelId'].vnfs['vnfStoreKey'].vfModules['modelName']['dynamicModelName1'];

  expect(vfModule.action).toEqual(ServiceInstanceActions.None);
});

})
;
