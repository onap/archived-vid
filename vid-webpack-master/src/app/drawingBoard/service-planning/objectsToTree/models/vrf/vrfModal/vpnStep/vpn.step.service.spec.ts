import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../../../../shared/store/reducers";
import {getTestBed, TestBed} from "@angular/core/testing";
import {NgReduxTestingModule} from "@angular-redux/store/testing";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {FeatureFlagsService} from "../../../../../../../shared/services/featureFlag/feature-flags.service";
import {AaiService} from "../../../../../../../shared/services/aaiService/aai.service";
import {VpnStepService} from "./vpn.step.service";
import {NetworkStepService} from "../networkStep/network.step.service";
import {ITableContent} from "../../../../../../../shared/components/searchMembersModal/members-table/element-table-row.model";
import {SearchElementsModalComponent} from "../../../../../../../shared/components/searchMembersModal/search-elements-modal.component";
import {ElementsTableService} from "../../../../../../../shared/components/searchMembersModal/members-table/elements-table.service";
import {DataFilterPipe} from "../../../../../../../shared/pipes/dataFilter/data-filter.pipe";
import * as _ from "lodash";

describe('VPN step service', () => {
  let injector;
  let service: VpnStepService;
  let memberTableService: ElementsTableService;
  let store: NgRedux<AppState>;
  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [NgReduxTestingModule, HttpClientTestingModule],
      providers: [
        VpnStepService,
        NetworkStepService,
        FeatureFlagsService,
        AaiService,
        ElementsTableService,
        DataFilterPipe
      ]
    });
    await TestBed.compileComponents();
    injector = getTestBed();
    service = injector.get(VpnStepService);
    store = injector.get(NgRedux);
    memberTableService = injector.get(ElementsTableService);
  })().then(done).catch(done.fail));

  test('service should be defined', () => {
    expect(service).toBeDefined();
  });

  test('getVPNStepHeaders', () => {
    let vpnHeaders = service.getVPNStepHeaders();
    expect(vpnHeaders).toEqual([
      {displayName: `VPN instance name`, key: ['instanceName']},
      {displayName: `Version`, key: ['modelInfo', 'modelVersionId']},
      {displayName: `Instance ID`, key: ['instanceId']},
      {displayName: `Platform`, key: ['platformName']},
      {displayName: 'Region', key: ['region']},
      {displayName: 'Route target', key: ['routeTargets', 'globalRouteTarget'], type: "LIST"},
      {displayName: 'Route target role', key: ['routeTargets', 'routeTargetRole'], type: "LIST"},
      {displayName: 'Customer VPN ID', key: ['customerId']},
    ]);
  });


  test('vpnStep', () => {
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
      }, "service": {}
    });
    const networkStep = service.getVpnStep(
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

    expect(networkStep.type).toEqual('VPN');
    expect(networkStep.title).toEqual('Associate VPN');
    expect(networkStep.description).toEqual('Select a VPN to associate to the VRF Entry');
    expect(networkStep.noElementsMsg).toEqual('No VPN instances were found.');
    expect(networkStep.maxSelectRow).toEqual(1);
    expect(networkStep.uniqObjectField).toEqual('instanceId');
    expect(networkStep.topButton.text).toEqual('SET VPN');
    expect(networkStep.criteria).toHaveLength(0);

  });

  test('getsVPNStepSearchFields', () => {
    const networkStepSearchFields = service.getsVPNStepSearchFields();
    expect(networkStepSearchFields[0].value).toEqual("SERVICE-INFRASTRUCTURE");
  });

  test('getVpnStepHeaders should return the correct headers for vpn popup', () => {
    let headers = service.getVPNStepHeaders();
    expect(headers[0].displayName).toEqual('VPN instance name');
    expect(headers[0].key).toEqual(['instanceName']);

    expect(headers[1].displayName).toEqual('Version');
    expect(headers[1].key).toEqual(['modelInfo', 'modelVersionId']);

    expect(headers[2].displayName).toEqual('Instance ID');
    expect(headers[2].key).toEqual(['instanceId']);

    expect(headers[3].displayName).toEqual('Platform');
    expect(headers[3].key).toEqual(['platformName']);

    expect(headers[4].displayName).toEqual('Region');
    expect(headers[4].key).toEqual(['region']);

    expect(headers[5].displayName).toEqual('Route target');
    expect(headers[5].key).toEqual(['routeTargets', 'globalRouteTarget']);

    expect(headers[6].displayName).toEqual('Route target role');
    expect(headers[6].key).toEqual(['routeTargets', 'routeTargetRole']);

    expect(headers[7].displayName).toEqual('Customer VPN ID');
    expect(headers[7].key).toEqual(['customerId']);

  });

  test('getElementsFirstStep should return sort vpns by name', () => {
    let vpns = [
      {"name": "B"},
      {"name": "A"},
      {"name": "D"},
      {"name": "C"},
      {"name": "E"}
    ];

    let sortedNetworkByInstanceName = service.sortElementsResultByField(vpns, "name");
    expect(sortedNetworkByInstanceName).toEqual([
      {"name": "A"},
      {"name": "B"},
      {"name": "C"},
      {"name": "D"},
      {"name": "E"}
    ])
  });

  test('getVpnTableContent', () => {
    let tableContent: ITableContent[] = service.getVPNTableContent();
    expect(tableContent).toEqual(
      [
        {
          id: 'vpn-name',
          contents: [{
            id: ['vpn-name'],
            value: ['instanceName']
          }]
        },
        {
          id: 'model-version-id',
          contents: [{
            id: ['model-version-id'],
            value: ['modelInfo', 'modelVersionId']
          }]
        },
        {
          id: 'vpn-id',
          contents: [{
            id: ['vpn-id'],
            value: ['instanceId']
          }]
        },
        {
          id: 'vpn-platform',
          contents: [{
            id: ['vpn-platform'],
            value: ['platformName']
          }]
        },
        {
          id: 'vpn-region',
          contents: [{
            id: ['vpn-region'],
            value: ['region']
          }]
        },
        {
          id: 'global-route-target',
          contents: [{
            id: ['global-route-target'],
            value: ['routeTargets', 'globalRouteTarget'],
            type: 'LIST'
          }]
        },
        {
          id: 'route-target-role',
          contents: [{
            id: ['route-target-role'],
            value: ['routeTargets', 'routeTargetRole'],
            type: 'LIST'
          }]
        },
        {
          id: 'customer-vpn-id',
          contents: [{
            id: ['customer-vpn-id'],
            value: ['customerId']
          }]
        }
      ]
    )
  });

  test('associateVrfNetworkMember', () => {
    const vrfStoreKey: string = 'vrfStoreKey';
    const serviceModelId: string = 'serviceModelId';
    const reduxState = {
      "global": {
        "genericModalHelper": {
          "selectedNetwork": {
            "10a74149-c9d7-4918-bbcf-d5fb9b1799ce": true
          }
        }
      }
    };
    spyOn(store, 'dispatch');
    spyOn(store, 'getState').and.returnValue(reduxState);
    let searchElementsModalComponent = new SearchElementsModalComponent(null, null, null, null, null);

    searchElementsModalComponent.modalInformation = <any>{
      serviceModelId: serviceModelId
    };
    service.associateVrfNetworkMember(searchElementsModalComponent, vrfStoreKey);
    expect(store.dispatch).toHaveBeenCalledTimes(Object.keys(reduxState.global.genericModalHelper.selectedNetwork).length);
  });

  test('associateVrfVPNMember', () => {
    const vrfStoreKey: string = 'vrfStoreKey';
    const serviceModelId: string = 'serviceModelId';
    const members = {
      "120d39fb-3627-473d-913c-d228dd0f8e5b": {
        "instanceId": "120d39fb-3627-473d-913c-d228dd0f8e5b",
        "instanceName": "LPPVPN",
        "platformName": "AVPN",
        "instanceType": "SERVICE-INFRASTRUCTURE",
        "region": "USA,EMEA",
        "customerId": "VPN1260",
        "modelInfo": {
          "modelCustomizationId": null,
          "modelInvariantId": null,
          "modelVersionId": null,
        },
        "routeTargets": [
          {
            "globalRouteTarget": "globalRouteTarget_1",
            "routeTargetRole": "routeTargetRole_1"
          },
          {
            "globalRouteTarget": "globalRouteTarget_2",
            "routeTargetRole": "routeTargetRole_2"
          },
          {
            "globalRouteTarget": "globalRouteTarget_3",
            "routeTargetRole": "routeTargetRole_3"
          }
        ],
        "isSelected": true
      },
      "c70391f3-a6e3-4874-9834-cbe12d7bf8b6": {
        "instanceId": "c70391f3-a6e3-4874-9834-cbe12d7bf8b6",
        "instanceName": "LPPVPN",
        "platformName": "AVPN",
        "instanceType": "SERVICE-INFRASTRUCTURE",
        "region": "USA,EMEA",
        "customerId": "VPN1274",
        "modelInfo": {
          "modelCustomizationId": null,
          "modelInvariantId": null,
          "modelVersionId": null
        },
        "routeTargets": null,
        "isSelected": false
      },
      "4776516b-7da2-446c-9ba7-47ca8c30c571": {
        "instanceId": "4776516b-7da2-446c-9ba7-47ca8c30c571",
        "instanceName": "LPPVPN",
        "platformName": "AVPN",
        "instanceType": "SERVICE-INFRASTRUCTURE",
        "region": "USA,EMEA",
        "customerId": "VPN1275",
        "modelInfo": {
          "modelCustomizationId": null,
          "modelInvariantId": null,
          "modelVersionId": null
        },

        "routeTargets": null,
        "isSelected": false
      },
      "46fcb25a-e7ba-4d96-99ba-3bb6eae6aba7": {
        "instanceId": "46fcb25a-e7ba-4d96-99ba-3bb6eae6aba7",
        "instanceName": "LPPVPN",
        "platformName": "AVPN",
        "instanceType": "SERVICE-INFRASTRUCTURE",
        "region": "USA,EMEA",
        "customerId": "VPN1271",
        "modelInfo": {
          "modelCustomizationId": null,
          "modelInvariantId": null,
          "modelVersionId": null,
        },
        "routeTargets": null,
        "isSelected": false
      },
      "ffefbe38-3087-418a-87ae-f6582a15be78": {
        "instanceId": "ffefbe38-3087-418a-87ae-f6582a15be78",
        "instanceName": "LPPVPN",
        "platformName": "AVPN",
        "instanceType": "SERVICE-INFRASTRUCTURE",
        "region": "USA,EMEA",
        "customerId": "VPN1272",
        "modelInfo": {
          "modelCustomizationId": null,
          "modelInvariantId": null,
          "modelVersionId": null,
        },
        "routeTargets": null,
        "isSelected": false
      },
      "961d05be-ee41-40a2-8653-f603fc495175": {
        "id": "961d05be-ee41-40a2-8653-f603fc495175",
        "name": "LPPVPN",
        "platform": "AVPN",
        "type": "SERVICE-INFRASTRUCTURE",
        "region": "USA,EMEA",
        "customerId": "VPN1273",
        "modelCustomizationId": null,
        "modelInvariantId": null,
        "modelVersionId": null,
        "routeTargets": null,
        "isSelected": false
      },
      "14bcfc2f-bbee-4fd9-89a5-42eb5dbb08d5": {
        "instanceId": "14bcfc2f-bbee-4fd9-89a5-42eb5dbb08d5",
        "instanceName": "LPPVPN",
        "platformName": "AVPN",
        "instanceType": "SERVICE-INFRASTRUCTURE",
        "region": "USA,EMEA",
        "customerId": "913443",
        "modelInfo": {
          "modelCustomizationId": null,
          "modelInvariantId": null,
          "modelVersionId": null,
        },
        "routeTargets": null,
        "isSelected": false
      },
      "89d4c968-158c-4722-a22c-c5c2ccc17fd5": {
        "instanceId": "89d4c968-158c-4722-a22c-c5c2ccc17fd5",
        "instanceName": "LPPVPN",
        "platformName": "AVPN",
        "instanceType": "SERVICE-INFRASTRUCTURE",
        "region": "USA,EMEA",
        "customerId": "VPN1276",
        "modelInfo": {
          "modelCustomizationId": null,
          "modelInvariantId": null,
          "modelVersionId": null,
        },
        "routeTargets": null,
        "isSelected": false
      },
      "3e7834fb-a8e0-4243-a837-5352ccab4602": {
        "instanceId": "3e7834fb-a8e0-4243-a837-5352ccab4602",
        "instanceName": "LPPVPN",
        "platformName": "AVPN",
        "instanceType": "SERVICE-INFRASTRUCTURE",
        "region": "USA,EMEA",
        "customerId": "VPN1259",
        "modelInfo": {
          "modelCustomizationId": null,
          "modelInvariantId": null,
          "modelVersionId": null,
        },
        "routeTargets": null,
        "isSelected": false
      },
      "844a1ea7-556a-4e49-8aa3-171f1db4ea02": {
        "instanceId": "844a1ea7-556a-4e49-8aa3-171f1db4ea02",
        "instanceName": "LPPVPN",
        "platformName": "AVPN",
        "instanceType": "SERVICE-INFRASTRUCTURE",
        "region": "USA,EMEA",
        "customerId": "VPN1277",
        "modelInfo": {
          "modelCustomizationId": null,
          "modelInvariantId": null,
          "modelVersionId": null,
        },
        "routeTargets": null,
        "isSelected": false
      }
    };
    spyOn(store, 'dispatch');
    memberTableService.allElementsStatusMap = <any>members;
    let searchElementsModalComponent = new SearchElementsModalComponent(null, null, null, memberTableService, null);

    searchElementsModalComponent.modalInformation = <any>{
      modalInformation: {
        serviceModelId: serviceModelId
      }
    };

    let memberArr = _.values(members);
    service.associateVrfVPNMember(searchElementsModalComponent, vrfStoreKey);
    expect(store.dispatch).toHaveBeenCalledTimes(memberArr.filter((item) => item.isSelected).length * 2);
  });
});
