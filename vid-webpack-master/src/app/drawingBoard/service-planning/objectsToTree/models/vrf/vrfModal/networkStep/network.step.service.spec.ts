import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../../../../shared/store/reducers";
import {getTestBed, TestBed} from "@angular/core/testing";
import {NgReduxTestingModule} from "@angular-redux/store/testing";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {FeatureFlagsService} from "../../../../../../../shared/services/featureFlag/feature-flags.service";
import {AaiService} from "../../../../../../../shared/services/aaiService/aai.service";
import {NetworkStepService} from "./network.step.service";
import {updateGenericModalCriteria} from "../../../../../../../shared/storeUtil/utils/global/global.actions";
import {ITableContent} from "../../../../../../../shared/components/searchMembersModal/members-table/element-table-row.model";

describe('Network step service', () => {
  let injector;
  let service: NetworkStepService;
  let store: NgRedux<AppState>;
  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [NgReduxTestingModule, HttpClientTestingModule],
      providers: [
        NetworkStepService,
        FeatureFlagsService,
        AaiService
      ]
    });
    await TestBed.compileComponents();
    injector = getTestBed();
    service = injector.get(NetworkStepService);
    store = injector.get(NgRedux);
  })().then(done).catch(done.fail));

  test('service should be defined', () => {
    expect(service).toBeDefined();
  });


  test('networkStep', () => {
    spyOn(store, 'getState').and.returnValue({
      "global": {
        "genericModalHelper": {
          "selectedNetwork": {
            "action": "None",
            "instanceName": "NETWORK_1_INSTANCE_NAME",
            "instanceId": "NETWORK_1_INSTANCE_ID",
            "orchStatus": null,
            "productFamilyId": null,
            "lcpCloudRegionId": "hvf23b",
            "tenantId": "3e9a20a3e89e45f884e09df0cc2d2d2a",
            "tenantName": "APPC-24595-T-IST-02C",
            "modelInfo": {
              "modelInvariantId": "network-instance-model-invariant-id",
              "modelVersionId": "7a6ee536-f052-46fa-aa7e-2fca9d674c44",
              "modelVersion": "2.0",
              "modelName": "vf_vEPDG",
              "modelType": "vnf"
            },
            "roles": ["network role 1", "network role 2"],
            "instanceType": "NETWORK_1_INSTANCE_TYPE",
            "provStatus": null,
            "inMaint": false,
            "uuid": "7a6ee536-f052-46fa-aa7e-2fca9d674c44",
            "originalName": null,
            "legacyRegion": null,
            "lineOfBusiness": null,
            "platformName": null,
            "trackById": "VNF1_INSTANCE_ID",
            "serviceInstanceId": "service-instance-id1",
            "serviceInstanceName": "service-instance-name",
            "isSelected": true
          },
          "selectedVpn": {
            "vpn-id": "120d39fb-3627-473d-913c-d228dd0f8e5b",
            "vpn-name": "LPPVPN",
            "vpn-platform": "AVPN",
            "vpn-type": "SERVICE-INFRASTRUCTURE",
            "vpn-region": "USA,EMEA",
            "customer-vpn-id": "VPN1260",
            "model-customization-id": null,
            "model-invariant-id": null,
            "model-version-id": null,
            "route-targets": null,
            "isSelected": true
          }
        },
        "genericModalCriteria": {"roles": ["-- select an option --", "network role 1", "network role 2", "network role 3", "network role 4", "network role 5"]},
        "name": null,
        "flags": {
          "EMPTY_DRAWING_BOARD_TEST": false,
          "FLAG_NETWORK_TO_ASYNC_INSTANTIATION": false,
          "FLAG_ENABLE_WEBPACK_MODERN_UI": true,
          "FLAG_ASYNC_JOBS": true,
          "FLAG_ADD_MSO_TESTAPI_FIELD": true,
          "FLAG_UNASSIGN_SERVICE": false,
          "FLAG_SERVICE_MODEL_CACHE": false,
          "FLAG_SHOW_ASSIGNMENTS": true,
          "FLAG_FABRIC_CONFIGURATION_ASSIGNMENTS": true,
          "FLAG_DUPLICATE_VNF": true,
          "FLAG_DEFAULT_VNF": true,
          "FLAG_SETTING_DEFAULTS_IN_DRAWING_BOARD": true,
          "FLAG_A_LA_CARTE_AUDIT_INFO": true,
          "FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST": true,
          "FLAG_PRESENT_PROVIDER_NETWORKS_ASSOCIATIONS": true,
          "FLAG_1810_CR_SOFT_DELETE_ALACARTE_VF_MODULE": true,
          "FLAG_1902_NEW_VIEW_EDIT": true,
          "FLAG_VF_MODULE_RESUME_STATUS_CREATE": true,
          "FLAG_1906_COMPONENT_INFO": true
        },
        "drawingBoardStatus": "CREATE",
        "type": "UPDATE_DRAWING_BOARD_STATUS"
      }, "service": {
        "serviceHierarchy": {
          "f028b2e2-7080-4b13-91b2-94944d4c42d8": {
            "service": {
              "uuid": "f028b2e2-7080-4b13-91b2-94944d4c42d8",
              "invariantUuid": "dfc2c44c-2429-44ca-ae26-1e6dc1f207fb",
              "name": "infraVPN",
              "version": "1.0",
              "toscaModelURL": null,
              "category": "Network Service",
              "serviceType": "BONDING",
              "serviceRole": "INFRASTRUCTURE-VPN",
              "description": "ddd",
              "serviceEcompNaming": "true",
              "instantiationType": "A-La-Carte",
              "inputs": {},
              "vidNotions": {"instantiationUI": "macroService", "modelCategory": "other", "viewEditUI": "legacy"}
            },
            "vnfs": {},
            "networks": {},
            "collectionResources": {},
            "configurations": {},
            "fabricConfigurations": {},
            "serviceProxies": {
              "misvpn_service_proxy 0": {
                "uuid": "35186eb0-e6b6-4fa5-86bb-1501b342a7b1",
                "invariantUuid": "73f89e21-b96c-473f-8884-8b93bcbd2f76",
                "description": "A Proxy for Service MISVPN_SERVICE",
                "name": "MISVPN_SERVICE Service Proxy",
                "version": "3.0",
                "customizationUuid": "4c2fb7e0-a0a5-4b32-b6ed-6a974e55d923",
                "inputs": {},
                "commands": {},
                "properties": {"ecomp_generated_naming": "false"},
                "type": "Service Proxy",
                "sourceModelUuid": "d5cc7d15-c842-450e-95ae-2a69e66dd23b",
                "sourceModelInvariant": "c126ec86-59fe-48c0-9532-e39a9b3e5272",
                "sourceModelName": "MISVPN_SERVICE"
              }
            },
            "vfModules": {},
            "volumeGroups": {},
            "pnfs": {},
            "vnfGroups": {},
            "vrfs": {
              "VRF Entry Configuration 0": {
                "uuid": "9cac02be-2489-4374-888d-2863b4511a59",
                "invariantUuid": "b67a289b-1688-496d-86e8-1583c828be0a",
                "description": "VRF Entry configuration object",
                "name": "VRF Entry Configuration",
                "version": "30.0",
                "customizationUuid": "dd024d73-9bd1-425d-9db5-476338d53433",
                "inputs": {},
                "commands": {},
                "properties": {"ecomp_generated_naming": "false"},
                "type": "Configuration",
                "modelCustomizationName": "VRF Entry Configuration 0",
                "sourceNodes": [],
                "collectorNodes": null,
                "configurationByPolicy": false
              }
            }
          }
        },
        "serviceInstance": {
          "f028b2e2-7080-4b13-91b2-94944d4c42d8": {
            "action": "Create",
            "isDirty": false,
            "vnfs": {},
            "vrfs": {},
            "instanceParams": [{}],
            "validationCounter": 0,
            "existingNames": {"dfd": ""},
            "existingVNFCounterMap": {},
            "existingVRFCounterMap": {},
            "existingVnfGroupCounterMap": {},
            "existingNetworksCounterMap": {},
            "optionalGroupMembersMap": {},
            "networks": {},
            "vnfGroups": {},
            "bulkSize": 1,
            "instanceName": "dfd",
            "globalSubscriberId": "e433710f-9217-458d-a79d-1c7aff376d89",
            "subscriptionServiceType": "TYLER SILVIA",
            "owningEntityId": "d61e6f2d-12fa-4cc2-91df-7c244011d6fc",
            "projectName": "WATKINS",
            "rollbackOnFailure": "true",
            "aicZoneName": null,
            "owningEntityName": "WayneHolland",
            "testApi": "VNF_API",
            "tenantName": null,
            "modelInfo": {
              "modelInvariantId": "dfc2c44c-2429-44ca-ae26-1e6dc1f207fb",
              "modelVersionId": "f028b2e2-7080-4b13-91b2-94944d4c42d8",
              "modelName": "infraVPN",
              "modelVersion": "1.0",
              "uuid": "f028b2e2-7080-4b13-91b2-94944d4c42d8",
              "modelUniqueId": "f028b2e2-7080-4b13-91b2-94944d4c42d8"
            },
            "isALaCarte": true,
            "name": "infraVPN",
            "version": "1.0",
            "description": "ddd",
            "category": "Network Service",
            "uuid": "f028b2e2-7080-4b13-91b2-94944d4c42d8",
            "invariantUuid": "dfc2c44c-2429-44ca-ae26-1e6dc1f207fb",
            "serviceType": "BONDING",
            "serviceRole": "INFRASTRUCTURE-VPN",
            "vidNotions": {"instantiationUI": "macroService", "modelCategory": "other", "viewEditUI": "legacy"},
            "isEcompGeneratedNaming": true,
            "isMultiStepDesign": false
          }
        },
        "type": "UPDATE_CATEGORY_PARAMETERS",
        "categoryParameters": {
          "owningEntityList": [{
            "id": "d61e6f2d-12fa-4cc2-91df-7c244011d6fc",
            "name": "WayneHolland"
          }, {"id": "Melissa", "name": "Melissa"}, {"id": "aaa1", "name": "aaa1"}],
          "projectList": [{"id": "WATKINS", "name": "WATKINS"}, {"id": "x1", "name": "x1"}, {"id": "yyy1", "name": "yyy1"}],
          "lineOfBusinessList": [{"id": "ONAP", "name": "ONAP"}, {"id": "zzz1", "name": "zzz1"}],
          "platformList": [{"id": "platform", "name": "platform"}, {"id": "xxx1", "name": "xxx1"}]
        }
      }
    });
    const networkStep = service.getNetworkStep(
      "f028b2e2-7080-4b13-91b2-94944d4c42d8",
      <any>{
        data: {
          children: [],
          componentInfoType: "VRF",
          count: 0,
          disabled: false,
          id: "dd024d73-9bd1-425d-9db5-476338d53433",
          isEcompGeneratedNaming: false,
          max: 1,
          modelCustomizationId: "dd024d73-9bd1-425d-9db5-476338d53433",
          modelUniqueId: "dd024d73-9bd1-425d-9db5-476338d53433",
          modelVersionId: "9cac02be-2489-4374-888d-2863b4511a59",
          name: "VRF Entry Configuration 0",
          tooltip: "VRF",
          type: "VRF",
          typeName: "VRF"
        }
      });


    expect(networkStep.type).toEqual('Network');
    expect(networkStep.title).toEqual('Associate network to VRF Entry');
    expect(networkStep.description).toEqual('Select a network to associate to the VRF Entry');
    expect(networkStep.noElementsMsg).toEqual('No network were found.');
    expect(networkStep.maxSelectRow).toEqual(1);
    expect(networkStep.uniqObjectField).toEqual('instanceId');
    expect(networkStep.topButton.text).toEqual('Next');
    expect(networkStep.criteria).toHaveLength(1);

  });

  test('getsNetworkStepSearchFields', () => {
    const networkStepSearchFields = service.getsNetworkStepSearchFields({
      "lcpCloudRegionId": "lcpCloudRegionId",
      "tenantName": "tenantName"
    });

    expect(networkStepSearchFields[0].value).toEqual("lcpCloudRegionId");
    expect(networkStepSearchFields[1].value).toEqual("tenantName");
    expect(networkStepSearchFields[2].value).toEqual("Active");
    expect(networkStepSearchFields[3].value).toEqual("VPN binding");
  });

  test('generateRolesOptions should sort networks roles, check if should add "Not assigned" option', () => {
    const networks = [
      {"role": "2"},
      {"role": "3"},
      {"role": "4"},
      {"role": "1"},
      {"role": "6"}
    ];

    spyOn(store, 'dispatch');

    service.generateRolesOptions(networks);
    expect(store.dispatch).toHaveBeenCalledWith(updateGenericModalCriteria("roles", ["-- select an option --", "1", "2", "3", "4", "6"]))

    networks.push({"role": null});
    service.generateRolesOptions(networks);
    expect(store.dispatch).toHaveBeenCalledWith(updateGenericModalCriteria("roles", ["-- select an option --", "Not assigned", "1", "2", "3", "4", "6"]))
  });

  test('shouldAddNotAssigned- check if "Not Assigned" option to roles', () => {
    const networks = [
      {"role": "5"},
      {"role": "3"},
      {"role": "4"},
      {"role": "1"},
      {"role": "6"}
    ];

    let result: boolean = service.shouldAddNotAssigned(networks);
    expect(result).toBeFalsy();

    networks.push({"role": null});

    result = service.shouldAddNotAssigned(networks);
    expect(result).toBeTruthy();
  });


  test('getNetworkStepHeaders should return the correct headers for network popup', () => {
    let headers = service.getNetworkStepHeaders();

    expect(headers[0].displayName).toEqual('Name');
    expect(headers[0].key).toEqual(['instanceName']);

    expect(headers[1].displayName).toEqual('Type');
    expect(headers[1].key).toEqual(['instanceType']);

    expect(headers[2].displayName).toEqual('Role');
    expect(headers[2].key).toEqual(['role']);

    expect(headers[3].displayName).toEqual('Orch. Status');
    expect(headers[3].key).toEqual(['orchStatus']);

    expect(headers[4].displayName).toEqual('Physical name');
    expect(headers[4].key).toEqual(['physicalName']);

    expect(headers[5].displayName).toEqual('Instance ID');
    expect(headers[5].key).toEqual(['instanceId']);

    expect(headers[6].displayName).toEqual('Model UUID');
    expect(headers[6].key).toEqual(['modelInfo', 'modelVersionId']);

    expect(headers[7].displayName).toEqual('Service name');
    expect(headers[7].key).toEqual(['serviceName']);

    expect(headers[8].displayName).toEqual('Service UUID');
    expect(headers[8].key).toEqual(['serviceUUID']);

    expect(headers[9].displayName).toEqual('Tenant');
    expect(headers[9].key).toEqual(['tenantName']);

    expect(headers[10].displayName).toEqual('Region');
    expect(headers[10].key).toEqual(['lcpCloudRegionId']);
  });

  test('getElementsFirstStep should return sort networks by instance name', () => {
    let networks = [
      {"instanceUUID": "B"},
      {"instanceUUID": "A"},
      {"instanceUUID": "D"},
      {"instanceUUID": "C"},
      {"instanceUUID": "E"}
    ];

    let sortedNetworkByInstanceName = service.sortElementsResultByField(networks, "instanceUUID");
    expect(sortedNetworkByInstanceName).toEqual([
      {"instanceUUID": "A"},
      {"instanceUUID": "B"},
      {"instanceUUID": "C"},
      {"instanceUUID": "D"},
      {"instanceUUID": "E"}
    ])
  });


  test('getNetworkTableContent', () => {
    let tableContent: ITableContent[] = service.getNetworkTableContent();
    expect(tableContent).toEqual(
    [
      {
        id: 'instanceName',
        contents: [{
          id: ['name'],
          value: ["instanceName"]
        }]
      },
      {
        id: 'instanceType',
        contents: [{
          id: ['type'],
          value: ['instanceType']
        }]
      },
      {
        id: 'role',
        contents: [{
          id: ['role'],
          value: ['role']
        }]
      },
      {
        id: 'network-orch-status',
        contents: [{
          id: ['orchStatus'],
          value: ['orchStatus']
        }]
      },
      {
        id: 'network-physical-name',
        contents: [{
          id: ['physicalName'],
          value: ['physicalName']
        }]
      },
      {
        id: 'network-instance-id',
        contents: [{
          id: ['instanceID'],
          value: ['instanceId']
        }]
      },
      {
        id: 'network-model-uuid',
        contents: [{
          id: ['modelUUID'],
          value: ['modelInfo', 'modelVersionId']
        }]
      },
      {
        id: 'network-serviceName',
        contents: [{
          id: ['serviceName'],
          value: ['serviceName']
        }]
      },
      {
        id: 'network-service-id',
        contents: [{
          id: ['serviceUUID'],
          value: ['serviceUUID']
        }]
      },
      {
        id: 'network-tenantName',
        contents: [{
          id: ['tenant'],
          value: ['tenantName']
        }]
      },
      {
        id: 'network-region',
        contents: [{
          id: ['region'],
          value: ['lcpCloudRegionId']
        }]
      }
    ]);
  });
});
