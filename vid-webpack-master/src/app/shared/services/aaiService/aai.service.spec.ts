import {ServiceInstance} from "../../models/serviceInstance";
import {RootObject} from "./model/crawledAaiService";
import {AaiService} from "./aai.service";
import {instance, mock, when} from "ts-mockito";
import {FeatureFlagsService, Features} from "../featureFlag/feature-flags.service";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {getTestBed, TestBed} from "@angular/core/testing";
import {NgRedux} from "@angular-redux/store";
import {Constants} from "../../utils/constants";
import {AppState} from "../../store/reducers";
import {setOptionalMembersVnfGroupInstance} from "../../storeUtil/utils/vnfGroup/vnfGroup.actions";
import each from 'jest-each';

class MockAppStore<T> {
  dispatch(){}
  getState(){}
}

describe("AaiService", () => {

  let injector;
  let httpMock: HttpTestingController;
  let aaiService: AaiService;
  let mockFeatureFlagsService: FeatureFlagsService = mock(FeatureFlagsService);
  let store: NgRedux<AppState>;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        AaiService,
        {provide: NgRedux, useClass: MockAppStore},
        {provide: FeatureFlagsService, useValue: instance(mockFeatureFlagsService)}
      ]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    httpMock = injector.get(HttpTestingController);
    aaiService = injector.get(AaiService);
    store = injector.get(NgRedux);

  })().then(done).catch(done.fail));

  describe('#resolve tests', () => {
    test('aai service resolve should return the right object', () => {
      let serviceInstance = new ServiceInstance();
      aaiService.resolve(aaiServiceInstnace.root, serviceInstance);

      expectedResult.vnfs['DROR_vsp'].trackById = serviceInstance.vnfs['DROR_vsp'].trackById;
      expect(JSON.parse(JSON.stringify(serviceInstance.vnfs))).toEqual(expectedResult.vnfs);
      expect(JSON.parse(JSON.stringify(serviceInstance.networks))).toEqual(expectedResult.networks);
    });
  });

  describe('#serviceInstanceTopology tests', () => {
    test('aai service get serviceInstanceTopolgetServiceInstanceTopologyResult.jsonogy from backend, and return ServiceInstance', () => {

      const mockedResult = getTopology();
      const serviceInstanceId: string = "id";
      const subscriberId: string = "fakeSunId";
      const serviceType: string = "justServiceType";
      aaiService.retrieveServiceInstanceTopology(serviceInstanceId, subscriberId, serviceType).subscribe((result: ServiceInstance) => {
        expect(result.instanceName).toEqual("mCaNkinstancename");
        expect(result.modelInavariantId).toEqual("6b528779-44a3-4472-bdff-9cd15ec93450");
        expect(result.vnfs["2017-388_PASQUALE-vPE 0"].instanceName).toEqual("2017388_PASQUALEvPEmCaNkinstanceName");
        expect(result.vnfs["2017-488_PASQUALE-vPE 0"].
          vfModules["2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0"]
          ["2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0uvfot"].instanceName
        ).toEqual("VFinstancenameZERO");
      });

      const req = httpMock.expectOne(`${Constants.Path.AAI_GET_SERVICE_INSTANCE_TOPOLOGY_PATH}${subscriberId}/${serviceType}/${serviceInstanceId}`);
      expect(req.request.method).toEqual('GET');
      req.flush(mockedResult);
    });
  });


  describe('#retrieveAndStoreServiceInstanceRetryTopology tests', () => {
    test('aai service get retrieveAndStoreServiceInstanceRetryTopology.jsonogy from backend, and return ServiceInstance', () => {

      let mockedResult = getTopology();

      const jobId: string = "jobId";
      aaiService.retrieveServiceInstanceRetryTopology(jobId).subscribe((result: ServiceInstance) => {
        expect(result.instanceName).toEqual("mCaNkinstancename");
        expect(result.modelInavariantId).toEqual("6b528779-44a3-4472-bdff-9cd15ec93450");
        expect(result.vnfs["2017-388_PASQUALE-vPE 0"].instanceName).toEqual("2017388_PASQUALEvPEmCaNkinstanceName");
        expect(result.vnfs["2017-488_PASQUALE-vPE 0"].
          vfModules["2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0"]
          ["2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0uvfot"].instanceName
        ).toEqual("VFinstancenameZERO");
      });

      const req = httpMock.expectOne(`${Constants.Path.SERVICES_RETRY_TOPOLOGY}/${jobId}`);
      expect(req.request.method).toEqual('GET');
      req.flush(mockedResult);
    });
  });

  describe('# get optional group members tests', () =>{
    test('aai service get getOptionalGroupMembers and return list of VnfMember', () => {
      jest.spyOn(store, 'dispatch');
      jest.spyOn(store, 'getState').mockReturnValue( <any>{
        service :{
          serviceInstance :{
            "serviceModelId" : {
              optionalGroupMembersMap : {}
            }
          }
        }
      });
      const mockedResult = getMockMembers();
      const serviceInvariantId: string = "serviceInvariantId";
      const subscriberId: string = "subscriberId";
      const serviceType: string = "serviceType";
      const groupType: string = "groupType";
      const groupRole: string = "groupRole";
      const serviceModelId: string = "serviceModelId";
       aaiService.getOptionalGroupMembers(serviceModelId, subscriberId, serviceType, serviceInvariantId, groupType, groupRole).subscribe((res)=>{
         const path = `${Constants.Path.AAI_GET_SERVICE_GROUP_MEMBERS_PATH}${subscriberId}/${serviceType}/${serviceInvariantId}/${groupType}/${groupRole}`;
         expect(store.dispatch).toHaveBeenCalledWith(setOptionalMembersVnfGroupInstance(serviceModelId, path, res));
         expect(res.length).toEqual(2);
        });


      const req = httpMock.expectOne(`${Constants.Path.AAI_GET_SERVICE_GROUP_MEMBERS_PATH}${subscriberId}/${serviceType}/${serviceInvariantId}/${groupType}/${groupRole}`);
      expect(req.request.method).toEqual('GET');
      req.flush(mockedResult);
    });
  });


  describe('# get active networks', () =>{
    test('aai service get active networks', () => {
      const mockedResult = getMockActiveNetworks();
      const cloudRegion: string = "cloudRegion";
      const tenantId: string = "tenantId";
      aaiService.retrieveActiveNetwork(cloudRegion, tenantId).subscribe((res)=>{
        expect(res.length).toEqual(mockedResult.length);
      });

      const req = httpMock.expectOne(`${Constants.Path.AAI_GET_ACTIVE_NETWORKS_PATH}?cloudRegion=${cloudRegion}&tenantId=${tenantId}`);
      expect(req.request.method).toEqual('GET');
      req.flush(mockedResult);
    });
  });


  describe('#cloud owner tests', () => {
    let featureFlagToLcpRegionName = [
      ['aai service extract lcpRegion, flag is true=> lcp region include cloud owner', true, 'id (OWNER)' ],
      ['aai service extract lcpRegion, flag is false=> lcp region doesnt include cloud owner', false, 'id']
    ];

    each(featureFlagToLcpRegionName).test("%s", (desc: string, flag: boolean, expectedName: string ) => {
        when(mockFeatureFlagsService.getFlagState(Features.FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST)).thenReturn(flag);
        let name: string = aaiService.extractLcpRegionName("id", "att-owner");
        expect(name).toEqual(expectedName);
    });

    let cloudOwnerFormattingDataProvider = [
      ['classic cloud owner', 'irma-aic', ' (AIC)'],
      ['upper case cloud owner', 'IRMA-AIC', ' (AIC)'],
      ['no att cloud owner', 'nc', ' (NC)'],
    ];

    each(cloudOwnerFormattingDataProvider).test('test cloudOwner trailer formatting %s', (desc: string, cloudOwner: string, expectedTrailer: string) => {
      expect(AaiService.formatCloudOwnerTrailer(cloudOwner)).toEqual(expectedTrailer);
    });
  });

  describe('#Pnf modelCustomizationName initialization tests', () => {

    test('initializePnfModelCustomizationName should not reinitialize modelCustomizationName when it exists', () => {
      let serviceHierarchy = {
        "pnfs": {
          "pnfInstance": {
            "modelCustomizationName": "existingName"
          }
        }
      }

      aaiService.initializePnfModelCustomizationName(serviceHierarchy);

      expect(serviceHierarchy.pnfs["pnfInstance"].modelCustomizationName).toBe("existingName");
    });

    test('initializePnfModelCustomizationName should initialize modelCustomizationName when it doesnt exist', () => {
      let serviceHierarchy = {
        "pnfs": {
          "pnfInstance": {}
        }
      }

      aaiService.initializePnfModelCustomizationName(serviceHierarchy);

      expect((serviceHierarchy.pnfs["pnfInstance"] as any).modelCustomizationName).toBe("pnfInstance");
    });
  });

  function getTopology() {
    return {
      "vnfs": {
        "2017-388_PASQUALE-vPE 0": {
          "vfModules": {},
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
            "uuid": "afacccf6-397d-45d6-b5ae-94c39734b168"
          },
          "instanceName": "2017388_PASQUALEvPEmCaNkinstanceName",
          "legacyRegion": "some legacy region"
        },
        "2017-488_PASQUALE-vPE 0": {
          "vfModules": {
            "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0uvfot": {
                "instanceName": "VFinstancenameZERO",
                "modelInfo": {
                  "modelInvariantId": "b34833bb-6aa9-4ad6-a831-70b06367a091",
                  "modelVersionId": "f8360508-3f17-4414-a2ed-6bc71161e8db",
                  "modelName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                  "modelVersion": "5",
                  "modelCustomizationId": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3",
                  "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                  "uuid": "f8360508-3f17-4414-a2ed-6bc71161e8db"
                },
                "uuid": "f8360508-3f17-4414-a2ed-6bc71161e8db",
                "provStatus": "Prov Status",
                "orchStatus": "Active",
                "inMaint": true
              }
            },
            "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1fshmc": {
                "instanceName": "VFinstancename",
                "volumeGroupName": "VFinstancename_vol_abc",
                "orchStatus": "Create",
                "provStatus": "Prov Status",
                "inMaint": false,
                "modelInfo": {
                  "modelInvariantId": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                  "modelVersionId": "25284168-24bb-4698-8cb4-3f509146eca5",
                  "modelName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                  "modelVersion": "6",
                  "modelCustomizationId": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401",
                  "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                  "uuid": "25284168-24bb-4698-8cb4-3f509146eca5"
                },
                "uuid": "25284168-24bb-4698-8cb4-3f509146eca5"
              }
            }
          },
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
            "uuid": "69e09f68-8b63-4cc9-b9ff-860960b5db09"
          },
          "orchStatus": "Created",
          "inMaint": false,
          "instanceName": "2017488_PASQUALEvPEVNFinstancename",
          "legacyRegion": "some legacy region"
        },
        "2017-488_PASQUALE-vPE 0:0001": {
          "vfModules": {
            "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0uvfot": {
                "instanceName": "VFinstancenameZERO_001",
                "provStatus": "Prov Status",
                "inMaint": true,
                "modelInfo": {
                  "modelInvariantId": "b34833bb-6aa9-4ad6-a831-70b06367a091",
                  "modelVersionId": "f8360508-3f17-4414-a2ed-6bc71161e8db",
                  "modelName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                  "modelVersion": "5",
                  "modelCustomizationId": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3",
                  "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                  "uuid": "f8360508-3f17-4414-a2ed-6bc71161e8db"
                },
                "uuid": "f8360508-3f17-4414-a2ed-6bc71161e8db"
              }
            },
            "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1fshmc": {
                "instanceName": "VFinstancename_001",
                "volumeGroupName": "VFinstancename_vol_abc_001",
                "modelInfo": {
                  "modelInvariantId": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                  "modelVersionId": "25284168-24bb-4698-8cb4-3f509146eca5",
                  "modelName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                  "modelVersion": "6",
                  "modelCustomizationId": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401",
                  "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                  "uuid": "25284168-24bb-4698-8cb4-3f509146eca5"
                },
                "uuid": "25284168-24bb-4698-8cb4-3f509146eca5"
              }
            }
          },

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
            "uuid": "69e09f68-8b63-4cc9-b9ff-860960b5db09"
          },
          "instanceName": "2017488_PASQUALEvPEVNFinstancename_001",
          "legacyRegion": "some legacy region"
        },
        "2017-488_PASQUALE-vPE 0:0002": {
          "vfModules": {
            "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0uvfot": {
                "instanceName": "VFinstancenameZERO_002",
                "modelInfo": {
                  "modelInvariantId": "b34833bb-6aa9-4ad6-a831-70b06367a091",
                  "modelVersionId": "f8360508-3f17-4414-a2ed-6bc71161e8db",
                  "modelName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                  "modelVersion": "5",
                  "modelCustomizationId": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3",
                  "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                  "uuid": "f8360508-3f17-4414-a2ed-6bc71161e8db"
                },
                "uuid": "f8360508-3f17-4414-a2ed-6bc71161e8db"
              }
            },
            "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1fshmc": {
                "instanceName": "VFinstancename_002",
                "volumeGroupName": "VFinstancename_vol_abc_002",
                "modelInfo": {
                  "modelInvariantId": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                  "modelVersionId": "25284168-24bb-4698-8cb4-3f509146eca5",
                  "modelName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                  "modelVersion": "6",
                  "modelCustomizationId": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401",
                  "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                  "uuid": "25284168-24bb-4698-8cb4-3f509146eca5"
                },
                "uuid": "25284168-24bb-4698-8cb4-3f509146eca5"
              }
            }
          },
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
            "uuid": "69e09f68-8b63-4cc9-b9ff-860960b5db09"
          },
          "instanceName": "2017488_PASQUALEvPEVNFinstancename_002",
          "legacyRegion": "some legacy region"
        }
      },
      "vnfGroups": {},
      "existingVnfGroupCounterMap": {},
      "validationCounter": 0,
      "existingVNFCounterMap": {
        "afacccf6-397d-45d6-b5ae-94c39734b168": 1,
        "69e09f68-8b63-4cc9-b9ff-860960b5db09": 3
      },
      "existingNetworksCounterMap": {},
      "instanceName": "mCaNkinstancename",
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
      "tenantName": "AIN Web Tool-15-D-testalexandria",
      "modelInfo": {
        "modelInvariantId": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
        "modelVersionId": "6b528779-44a3-4472-bdff-9cd15ec93450",
        "modelName": "action-data",
        "modelVersion": "1.0",
        "uuid": "6b528779-44a3-4472-bdff-9cd15ec93450"
      },
      "isALaCarte": false,
      "orchStatus": "Active",
      "modelInavariantId": "6b528779-44a3-4472-bdff-9cd15ec93450"
    }
  }

  function getMockActiveNetworks() {
    return [
      {
        networkInstanceName: "networkInstanceName",
        instanceVersion: "instanceVersion",
        networkModel: "networkModel",
        instanceUUID: "instanceUUID_1",
        provStatus: "provStatus",
        orchStatus: "orchStatus",
        serviceInstanceName: "serviceInstanceName",
        serviceUUID: "serviceUUID",
        roles: ["network role 1", "network role 2"],
      },
      {
        networkInstanceName: "networkInstanceName",
        instanceVersion: "instanceVersion",
        networkModel: "networkModel",
        instanceUUID: "instanceUUID_2",
        provStatus: "provStatus",
        orchStatus: "orchStatus",
        serviceInstanceName: "serviceInstanceName",
        serviceUUID: "serviceUUID",
        roles: ["network role 1", "network role 3"],
      },
      {
        networkInstanceName: "networkInstanceName",
        instanceVersion: "instanceVersion",
        networkModel: "networkModel",
        instanceUUID: "instanceUUID_3",
        provStatus: "provStatus",
        orchStatus: "orchStatus",
        serviceInstanceName: "serviceInstanceName",
        serviceUUID: "serviceUUID",
        roles: ["network role 4", "network role 8"],
      },
      {
        networkInstanceName: "networkInstanceName",
        instanceVersion: "instanceVersion",
        networkModel: "networkModel",
        instanceUUID: "instanceUUID_4",
        provStatus: "provStatus",
        orchStatus: "orchStatus",
        serviceInstanceName: "serviceInstanceName",
        serviceUUID: "serviceUUID",
        roles: [],
      },
      {
        networkInstanceName: "networkInstanceName",
        instanceVersion: "instanceVersion",
        networkModel: "networkModel",
        instanceUUID: "instanceUUID_5",
        provStatus: "provStatus",
        orchStatus: "orchStatus",
        serviceInstanceName: "serviceInstanceName",
        serviceUUID: "serviceUUID",
        roles: ["network role 6", "network role 1"],
      },
      {
        networkInstanceName: "networkInstanceName",
        instanceVersion: "instanceVersion",
        networkModel: "networkModel",
        instanceUUID: "instanceUUID_6",
        provStatus: "provStatus",
        orchStatus: "orchStatus",
        serviceInstanceName: "serviceInstanceName",
        serviceUUID: "serviceUUID",
        roles: ["network role 5"],
      },
      {
        networkInstanceName: "networkInstanceName",
        instanceVersion: "instanceVersion",
        networkModel: "networkModel",
        instanceUUID: "instanceUUID_7",
        provStatus: "provStatus",
        orchStatus: "orchStatus",
        serviceInstanceName: "serviceInstanceName",
        serviceUUID: "serviceUUID",
        roles: ["network role 3"],
      },
      {
        networkInstanceName: "networkInstanceName",
        instanceVersion: "instanceVersion",
        networkModel: "networkModel",
        instanceUUID: "instanceUUID_8",
        provStatus: "provStatus",
        orchStatus: "orchStatus",
        serviceInstanceName: "serviceInstanceName",
        serviceUUID: "serviceUUID",
        roles: ["network role 4"],
      },
      {
        networkInstanceName: "networkInstanceName",
        instanceVersion: "instanceVersion",
        networkModel: "networkModel",
        instanceUUID: "instanceUUID_9",
        provStatus: "provStatus",
        orchStatus: "orchStatus",
        serviceInstanceName: "serviceInstanceName",
        serviceUUID: "serviceUUID",
        roles: [],
      }
    ]
  }

  const getMockMembers = (): any[] => {
    return [
      {
        "action":"None",
        "instanceName":"VNF1_INSTANCE_NAME",
        "instanceId":"VNF1_INSTANCE_ID",
        "orchStatus":null,
        "productFamilyId":null,
        "lcpCloudRegionId":null,
        "tenantId":null,
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
      },
      {
        "action":"None",
        "instanceName":"VNF2_INSTANCE_NAME",
        "instanceId":"VNF2_INSTANCE_ID",
        "orchStatus":null,
        "productFamilyId":null,
        "lcpCloudRegionId":null,
        "tenantId":null,
        "modelInfo":{
          "modelInvariantId":"vnf-instance-model-invariant-id",
          "modelVersionId":"eb5f56bf-5855-4e61-bd00-3e19a953bf02",
          "modelVersion":"1.0",
          "modelName":"vf_vEPDG",
          "modelType":"vnf"
        },
        "instanceType":"VNF2_INSTANCE_TYPE",
        "provStatus":null,
        "inMaint":true,
        "uuid":"eb5f56bf-5855-4e61-bd00-3e19a953bf02",
        "originalName":null,
        "legacyRegion":null,
        "lineOfBusiness":null,
        "platformName":null,
        "trackById":"eb5f56bf-5855-4e61-bd00-3e19a953bf02:003",
        "serviceInstanceId":"service-instance-id2",
        "serviceInstanceName":"service-instance-name"
      }];
  };
});


var expectedResult =
  {
    'vnfs': {
      'DROR_vsp': {
        'upgradedVFMSonsCounter': 0,
        'rollbackOnFailure': 'true',
        'vfModules': {},
        'isMissingData': false,
        'originalName': 'DROR_vsp',
        'orchStatus': 'Created',
        'inMaint': false,
        'vnfStoreKey' : null,
        'trackById' : 'abc',
        'action': 'Create'
      }
    },
    "vnfGroups" :{},
    "existingVNFCounterMap" : {},
    "existingVnfGroupCounterMap" : {},
    "existingNetworksCounterMap" : {},
    'instanceParams': {},
    'validationCounter': 0,
    'existingNames': {},
    'networks': {},
    'instanceName': 'Dror123',
    'orchStatus': 'Active',
    'modelInavariantId': '35340388-0b82-4d3a-823d-cbddf842be52',
    'action': 'Create'
  };


var aaiServiceInstnace: RootObject = {
  "root": {
    "type": "service-instance",
    "orchestrationStatus": "Active",
    "modelVersionId": "4e799efd-fd78-444d-bc25-4a3cde2f8cb0",
    "modelCustomizationId": "4e799efd-fd78-444d-bc25-4a3cde2f8cb0",
    "modelInvariantId": "35340388-0b82-4d3a-823d-cbddf842be52",
    "id": "62888f15-6d24-4f7b-92a7-c3f35beeb215",
    "name": "Dror123",
    "children": [
      {
        "type": "generic-vnf",
        "orchestrationStatus": "Created",
        "provStatus": "PREPROV",
        "inMaint": true,
        "modelVersionId": "11c6dc3e-cd6a-41b3-a50e-b5a10f7157d0",
        "modelCustomizationId": "11c6dc3e-cd6a-41b3-a50e-b5a10f7157d0",
        "modelInvariantId": "55628ce3-ed56-40bd-9b27-072698ce02a9",
        "id": "59bde732-9b84-46bd-a59a-3c45fee0538b",
        "name": "DROR_vsp",
        "children": []
      }
    ]
  }
};
