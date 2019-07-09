import {getTestBed, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {NgRedux} from "@angular-redux/store";
import {ActivatedRouteSnapshot, convertToParamMap} from "@angular/router";
import {AppState} from "../../store/reducers";
import {RetryResolver} from "./retry.resolver";
import {AaiService} from "../../services/aaiService/aai.service";
import {FeatureFlagsService} from "../../services/featureFlag/feature-flags.service";
import {Observable, of} from "rxjs";

class MockAppStore<T> {
  getState() {
    return {
      global:{
        drawingBoardStatus: "VIEW"
      },
      service: {
        serviceInstance: {}
      }
    }
  }
  dispatch(){

  }
}


describe('View Edit resolver', () => {
  let injector;
  let aaiService: AaiService;
  let resolver: RetryResolver;
  let httpMock: HttpTestingController;
  let store : NgRedux<AppState>;
  let activatedRouteSnapshot: ActivatedRouteSnapshot;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        FeatureFlagsService,
        RetryResolver,
        AaiService,
        {provide: NgRedux, useClass: MockAppStore},
        {
          provide: ActivatedRouteSnapshot, useValue: {
            queryParamMap:
              convertToParamMap({
                serviceModelId: 'serviceModelId',
                subscriberId: 'subscriberId',
                serviceType: 'serviceType',
                serviceInstanceId : 'serviceInstanceId',
                jobId : 'jobId'
              })
          },

        }
      ]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    aaiService = injector.get(AaiService);
    resolver = injector.get(RetryResolver);
    httpMock = injector.get(HttpTestingController);
    activatedRouteSnapshot = injector.get(ActivatedRouteSnapshot);
    store = injector.get(NgRedux)

  })().then(done).catch(done.fail));


  test("should call get all parameter's from url", () => {
      expect(activatedRouteSnapshot.queryParamMap.get("serviceModelId")).toBe('serviceModelId');
      expect(activatedRouteSnapshot.queryParamMap.get("subscriberId")).toBe('subscriberId');
      expect(activatedRouteSnapshot.queryParamMap.get("serviceType")).toBe('serviceType');
      expect(activatedRouteSnapshot.queryParamMap.get("serviceInstanceId")).toBe('serviceInstanceId');
      expect(activatedRouteSnapshot.queryParamMap.get("jobId")).toBe('jobId');
  });


  test("should return retry topology", () => {
      jest.spyOn(aaiService, 'getServiceModelById').mockReturnValue(of({}));
      jest.spyOn(aaiService, 'retrieveAndStoreServiceInstanceTopology').mockReturnValue(of({
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
      }));
  });

});
