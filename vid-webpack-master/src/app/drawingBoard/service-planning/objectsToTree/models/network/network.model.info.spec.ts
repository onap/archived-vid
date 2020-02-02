import {HttpClientTestingModule} from "@angular/common/http/testing";
import {getTestBed, TestBed} from "@angular/core/testing";
import {MockNgRedux, NgReduxTestingModule} from "@angular-redux/store/testing";
import {NetworkModelInfo} from "./network.model.info";
import {DynamicInputsService} from "../../dynamicInputs.service";
import {SharedTreeService} from "../../shared.tree.service";
import {NgRedux} from "@angular-redux/store";
import {NetworkPopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/network/network.popup.service";
import {DialogService} from "ng2-bootstrap-modal";
import {AvailableNodeIcons} from "../../../available-models-tree/available-models-tree.service";
import {DrawingBoardModes} from "../../../drawing-board.modes";
import {IframeService} from "../../../../../shared/utils/iframe.service";
import {DuplicateService} from "../../../duplicate/duplicate.service";
import {ModelInformationItem} from "../../../../../shared/components/model-information/model-information.component";
import {FeatureFlagsService} from "../../../../../shared/services/featureFlag/feature-flags.service";
import {NetworkModel} from "../../../../../shared/models/networkModel";

class MockAppStore<T> {
  getState() {
    return {
      global: {
        'drawingBoardStatus': DrawingBoardModes.CREATE
      },
      service: {
        serviceHierarchy: {
          'servicedId': {
            'networks': {
              'networkName': {
                'properties': {
                  'max_instances': 1
                }
              }
            }
          }
        },
        serviceInstance: {
          'servicedId': {
            'existingNetworksCounterMap': {
              'networkId': 1
            },
            'networks': {
              'networkName': {
                'action': 'Create',
                'originalName': 'networkName'
              }
            }
          }
        }
      }
    }
  }
}

class MockFeatureFlagsService extends FeatureFlagsService {
  getAllFlags(): { [p: string]: boolean } {
    return {};
  }
}


describe('Network Model Info', () => {
  let injector;
  let _dynamicInputsService: DynamicInputsService;
  let _sharedTreeService: SharedTreeService;
  let networkModel: NetworkModelInfo;
  let _dialogService: DialogService;
  let _networkPopupService: NetworkPopupService;
  let _duplicateService: DuplicateService;
  let _iframeService: IframeService;
  let _featureFlagsService: FeatureFlagsService;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NgReduxTestingModule],
      providers: [
        DynamicInputsService,
        SharedTreeService,
        DialogService,
        NetworkPopupService,
        IframeService,
        DuplicateService,
        {provide: NgRedux, useClass: MockAppStore},
        {provide: FeatureFlagsService, useClass: MockFeatureFlagsService},
        MockNgRedux]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    _sharedTreeService = injector.get(SharedTreeService);
    _featureFlagsService = injector.get(FeatureFlagsService);

    networkModel = new NetworkModelInfo(_dynamicInputsService, _sharedTreeService, _dialogService, _networkPopupService, _duplicateService, null, _iframeService, _featureFlagsService, MockNgRedux.getInstance());
  })().then(done).catch(done.fail));

  test('NetworkModelInfo should be defined', () => {
    expect(NetworkModelInfo).toBeDefined();
  });

  test('NetworkModelInfo should defined extra details', () => {
    expect(networkModel.name).toEqual('networks');
    expect(networkModel.type).toEqual('Network');
  });

  test('isEcompGeneratedNaming should return true if = isEcompGeneratedNaming is "true" ', () => {
    let isEcompGeneratedNaming: boolean = networkModel.isEcompGeneratedNaming({
      properties: {
        ecomp_generated_naming: 'true'
      }
    });
    expect(isEcompGeneratedNaming).toBeTruthy();
  });

  test('isEcompGeneratedNaming should return false if isEcompGeneratedNaming is "false"', () => {
    let isEcompGeneratedNaming: boolean = networkModel.isEcompGeneratedNaming({
      properties: {
        ecomp_generated_naming: 'false'
      }
    });
    expect(isEcompGeneratedNaming).toBeFalsy();
  });

  test('isEcompGeneratedNaming should return false if isEcompGeneratedNaming is not defined', () => {
    let isEcompGeneratedNaming: boolean = networkModel.isEcompGeneratedNaming({
      properties: {}
    });
    expect(isEcompGeneratedNaming).toBeFalsy();
  });

  test('getTooltip should return "VF"', () => {
    let tooltip: string = networkModel.getTooltip();
    expect(tooltip).toEqual('Network');
  });

  test('getType should return "VF"', () => {
    let tooltip: string = networkModel.getType();
    expect(tooltip).toEqual('Network');
  });

  test('getNextLevelObject should return null', () => {
    let nextLevel = networkModel.getNextLevelObject();
    expect(nextLevel).toBeNull();
  });

  test('updateDynamicInputsDataFromModel should return empty array', () => {
    let dynamicInputs = networkModel.updateDynamicInputsDataFromModel({});
    expect(dynamicInputs).toEqual([]);
  });

  test('getModel should return Network model', () => {
    expect(networkModel.getModel({})).toBeInstanceOf(NetworkModel);
  });

  test('showNodeIcons should return false if reachLimit of max', () => {
    let serviceId: string = 'servicedId';
    let node = {
      data: {
        id: 'networkId',
        name: 'networkName',
        modelCustomizationId: 'modelCustomizationId'
      }
    };
    jest.spyOn(_sharedTreeService, 'getExistingInstancesWithDeleteMode').mockReturnValue(0);
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global: {},
      service: {
        serviceHierarchy: {
          'servicedId': {
            'networks': {
              'networkName': {
                'properties': {
                  'max_instances': 1
                }
              }
            }
          }
        },
        serviceInstance: {
          'servicedId': {
            'existingNetworksCounterMap': {
              'modelCustomizationId': 1
            },
            'networks': {
              'networkName': {}
            }
          }
        }
      }
    });

    let result = networkModel.showNodeIcons(<any>node, serviceId);
    expect(result).toEqual(new AvailableNodeIcons(true, false));
  });

  test('showNodeIcons should return true if not reachLimit of max', () => {
    let serviceId: string = 'servicedId';
    let node = {
      data: {
        id: 'networkId',
        name: 'networkName'
      }
    };
    jest.spyOn(_sharedTreeService, 'getExistingInstancesWithDeleteMode').mockReturnValue(0);
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global: {},
      service: {
        serviceHierarchy: {
          'servicedId': {
            'networks': {
              'networkName': {
                'properties': {
                  'max_instances': 2
                }
              }
            }
          }
        },
        serviceInstance: {
          'servicedId': {
            'existingNetworksCounterMap': {
              'networkId': 1
            },
            'networks': {
              'networkName': {}
            }
          }
        }
      }
    });

    let result = networkModel.showNodeIcons(<any>node, serviceId);
    expect(result).toEqual(new AvailableNodeIcons(true, false));
  });

  test('getNodeCount should return number of nodes', () => {
    let serviceId: string = 'servicedId';
    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global: {},
      service: {
        serviceHierarchy: {
          'servicedId': {
            'networks': {
              'networkName': {
                'properties': {
                  'max_instances': 1
                }
              }
            }
          }
        },
        serviceInstance: {
          'servicedId': {
            'existingNetworksCounterMap': {
              'modelCustomizationId': 1
            },
            'networks': {
              'networkName': {
                'action': 'Create',
                'originalName': 'networkName'
              }
            }
          }
        }
      }
    });

    let node = {
      data: {
        id: 'networkId',
        name: 'networkName',
        action: 'Create',
        modelCustomizationId: "modelCustomizationId",
        modelUniqueId: "modelCustomizationId"
      }
    };
    let result = networkModel.getNodeCount(<any>node, serviceId);
    expect(result).toEqual(1);

    node.data.modelCustomizationId = 'networkId_notExist';
    node.data.modelUniqueId = 'networkId_notExist';
    result = networkModel.getNodeCount(<any>node, serviceId);
    expect(result).toEqual(0);
  });

  test('getMenuAction: showAuditInfoNetwork', () => {

    jest.spyOn(MockNgRedux.getInstance(), 'getState').mockReturnValue({
      global: {
        "drawingBoardStatus": DrawingBoardModes.RETRY
      }
    });
    jest.spyOn(_sharedTreeService, 'isRetryMode').mockReturnValue(true);
    let node = {
      data: {
        "modelId": "6b528779-44a3-4472-bdff-9cd15ec93450",
        "action": "Create",
        "isFailed": true,
      }
    };
    let serviceModelId = "6b528779-44a3-4472-bdff-9cd15ec93450";
    let result = networkModel.getMenuAction(<any>node, serviceModelId);
    spyOn(result['showAuditInfo'], 'method');
    expect(result['showAuditInfo']).toBeDefined();
    expect(result['showAuditInfo'].visible(node)).toBeTruthy();
    expect(result['showAuditInfo'].enable(node)).toBeTruthy();
    result['showAuditInfo']['method'](node, serviceModelId);
    expect(result['showAuditInfo']['method']).toHaveBeenCalledWith(node, serviceModelId);
  });

  test('Info for network should be correct', () => {
    const model = getNetworkModel();
    const instance = getNetworkInstance();
    let actualNetworkInfo = networkModel.getInfo(model, instance);
    let expectedNetworkInfo = [
      ModelInformationItem.createInstance("Min instances", "0"),
      ModelInformationItem.createInstance("Max instances", "1"),
      ModelInformationItem.createInstance('Network role', "network role 1, network role 2"),
      ModelInformationItem.createInstance("Route target id", null),
      ModelInformationItem.createInstance("Route target role", null),
    ];
    expect(actualNetworkInfo).toEqual(expectedNetworkInfo);
  });

  test('When there is no max Max instances text is: Unlimited (default)', () => {
    let actualVNFInfo = networkModel.getInfo({just:"not empty"},null);
    const maxInstancesItem = actualVNFInfo.find((item)=> item.label == 'Max instances');
    expect(maxInstancesItem.values[0]).toEqual('Unlimited (default)');
  });

  function getNetworkModel() {
    return {
      "customizationUuid": "94fdd893-4a36-4d70-b16a-ec29c54c184f",
      "name": "ExtVL",
      "version": "37.0",
      "description": "ECOMP generic virtual link (network) base type for all other service-level and global networks",
      "uuid": "ddc3f20c-08b5-40fd-af72-c6d14636b986",
      "invariantUuid": "379f816b-a7aa-422f-be30-17114ff50b7c",
      "max": 1,
      "min": 0,
      "isEcompGeneratedNaming": false,
      "type": "VL",
      "modelCustomizationName": "ExtVL 0",
      "roles": ["network role 1", " network role 2"],
      "properties": {
        "network_role": "network role 1, network role 2",
        "network_assignments":
          "{is_external_network=false, ipv4_subnet_default_assignment={min_subnets_count=1}, ecomp_generated_network_assignment=false, ipv6_subnet_default_assignment={min_subnets_count=1}}",
        "exVL_naming": "{ecomp_generated_naming=true}",
        "network_flows": "{is_network_policy=false, is_bound_to_vpn=false}",
        "network_homing": "{ecomp_selected_instance_node_target=false}"
      }
    };

  }

  function getNetworkInstance() {
    return {
      "modelCustomizationId": "94fdd893-4a36-4d70-b16a-ec29c54c184f",
      "modelId": "ddc3f20c-08b5-40fd-af72-c6d14636b986",
      "modelUniqueId": "94fdd893-4a36-4d70-b16a-ec29c54c184f",
      "missingData": true,
      "id": "NETWORK4_INSTANCE_ID",
      "action": "None",
      "orchStatus": "Created",
      "provStatus": "preprov",
      "inMaint": false,
      "instanceId": "NETWORK4_INSTANCE_ID",
      "instanceType": "CONTRAIL30_HIMELGUARD",
      "instanceName": "NETWORK4_INSTANCE_NAME",
      "name": "NETWORK4_INSTANCE_NAME",
      "modelName": "ExtVL 0",
      "type": "VL",
      "isEcompGeneratedNaming": false,
      "networkStoreKey": "NETWORK4_INSTANCE_ID",
      "typeName": "N",
      "menuActions": {"edit": {}, "showAuditInfo": {}, "duplicate": {}, "remove": {}, "delete": {}, "undoDelete": {}},
      "isFailed": false,
      "statusMessage": "",
      "statusProperties": [{"key": "Prov Status:", "value": "preprov", "testId": "provStatus"}, {
        "key": "Orch Status:",
        "value": "Created",
        "testId": "orchStatus"
      }],
      "trackById": "1wvr73xl999",
      "parentType": "",
      "componentInfoType": "Network",
      "errors": {}
    };
  }

});
