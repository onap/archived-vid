import {createRequest} from './mso.factory';


describe('Vlantagging', () => {
  test('should create a correct request', () => {
    sessionStorage.setItem("msoRequestParametersTestApiValue","GR_API");
    let userInputs_withEcompGeneratedNaming = {
      "productFamily": "e433710f-9217-458d-a79d-1c7aff376d89",
      "lcpRegion": "AAIAIC25",
      "tenantId": "092eb9e8e4b7412e8787dd091bc58e86",
      "aicZone": "YYY1",
      "platformName": "plat1",
      "lineOfBusiness": "onap"
    };
    let userInputs_withoutEcompGeneratedNaming = {
      "instanceName": "New Name",
      "productFamily": "e433710f-9217-458d-a79d-1c7aff376d89",
      "lcpRegion": "AAIAIC25",
      "tenantId": "092eb9e8e4b7412e8787dd091bc58e86",
      "aicZone": "YYY1",
      "platformName": "plat1",
      "lineOfBusiness": "onap"
    };
    let service = {
      "service": {
        "uuid": "6bce7302-70bd-4057-b48e-8d5b99e686ca",
        "invariantUuid": "9aa04749-c02c-432d-a90c-18caa361c833",
        "name": "vDOROTHEA_srv",
        "version": "1.0",
        "toscaModelURL": null,
        "category": "Network L4+",
        "serviceType": "",
        "serviceRole": "",
        "description": "vDOROTHEA_srv",
        "serviceEcompNaming": "true",
        "instantiationType": "A-La-Carte",
        "inputs": {}
      },
      "vnfs": {
        "vDOROTHEA 0": {
          "uuid": "61535073-2e50-4141-9000-f66fea69b433",
          "invariantUuid": "fcdf49ce-6f0b-4ca2-b676-a484e650e734",
          "description": "vDOROTHEA",
          "name": "vDOROTHEA",
          "version": "0.2",
          "customizationUuid": "1",
          "inputs": {},
          "commands": {},
          "properties": {
            "nf_naming": "{ecomp_generated_naming=true}",
            "multi_stage_design": "false",
            "oam_vfc_instance_group_function": "oambbb",
            "availability_zone_max_count": "1",
            "oam_network_collection_function": "oamaaa",
            "ecomp_generated_naming": "true",
            "untr_vfc_instance_group_function": "untrbbb",
            "untr_network_collection_function": "untraaa"
          },
          "type": "VF",
          "modelCustomizationName": "vDOROTHEA 0",
          "vfModules": {
            "vdorothea0..Vdorothea..main..module-0": {
              "uuid": "25a4d009-2f5a-44b4-b02a-62c584c15912",
              "invariantUuid": "614afb1a-3e7e-44e9-90ab-424d0070c781",
              "customizationUuid": "3443b341-7b0b-498c-a84a-a7ee736cba7e",
              "description": null,
              "name": "Vdorothea..main..module-0",
              "version": "1",
              "modelCustomizationName": "Vdorothea..main..module-0",
              "properties": {
                "minCountInstances": 1,
                "maxCountInstances": 1,
                "initialCount": 1,
                "vfModuleLabel": "main"
              },
              "inputs": {},
              "volumeGroupAllowed": false
            }
          },
          "volumeGroups": {},
          "vfcInstanceGroups": {
            "untr_group": {
              "uuid": "5fca04e2-a889-4579-8338-f60f1bf285fa",
              "invariantUuid": "fb1e384b-117a-46ae-9ad1-bf2f1ee1e49f",
              "name": "untr_group",
              "version": "1",
              "vfcInstanceGroupProperties": {
                "vfcParentPortRole": "untr",
                "networkCollectionFunction": "untraaa",
                "vfcInstanceGroupFunction": null,
                "subinterfaceRole": "untr"
              }
            },
            "oam_group": {
              "uuid": "a0efd5fc-f7be-4502-936a-a6c6392b958f",
              "invariantUuid": "9384abf9-1231-4da4-bd8d-89e4d2f8a749",
              "name": "oam_group",
              "version": "1",
              "vfcInstanceGroupProperties": {
                "vfcParentPortRole": "untr",
                "networkCollectionFunction": "untraaa",
                "vfcInstanceGroupFunction": null,
                "subinterfaceRole": "untr"
              }
            }
          }
        }
      },
      "networks": {},
      "collectionResources": {},
      "configurations": {},
      "serviceProxies": {},
      "vfModules": {
        "vdorothea0..Vdorothea..main..module-0": {
          "uuid": "25a4d009-2f5a-44b4-b02a-62c584c15912",
          "invariantUuid": "614afb1a-3e7e-44e9-90ab-424d0070c781",
          "customizationUuid": "3443b341-7b0b-498c-a84a-a7ee736cba7e",
          "description": null,
          "name": "Vdorothea..main..module-0",
          "version": "1",
          "modelCustomizationName": "Vdorothea..main..module-0",
          "properties": {
            "minCountInstances": 1,
            "maxCountInstances": 1,
            "initialCount": 1,
            "vfModuleLabel": "main"
          },
          "inputs": {},
          "volumeGroupAllowed": false
        }
      },
      "volumeGroups": {},
      "pnfs": {}
    };
    let serviceInstanceId: string = "6bce7302-70bd-4057-b48e-8d5b99e686ca";
    let networkInstanceGroups = {
      "untr_group": {
        "instance-group": {
          "instance-group-role": "JZmha7QSS4tJ",
          "model-invariant-id": "model-id3",
          "model-version-id": "a0efd5fc-f7be-4502-936a-a6c6392b958f",
          "id": "AAI-12002-test3-vm230w",
          "description": "a9DEa0kpY",
          "instance-group-type": "type",
          "resource-version": "1520888659539",
          "instance-group-name": "wKmBXiO1xm8bK",
          "instance-group-function": "testfunction2",
          "relationship-list": {
            "relationship": [
              {
                "relationDataList": [
                  {
                    "relationship-key": "cloud-region.cloud-owner",
                    "relationship-value": "AAI-12002-vm230w"
                  },
                  {
                    "relationship-key": "cloud-region.cloud-region-id",
                    "relationship-value": "AAI-region-vm230w"
                  }
                ],
                "relatedToPropertyList": [
                  {
                    "property-key": "cloud-region.owner-defined-type",
                    "property-value": null
                  }
                ],
                "related-to": "cloud-region",
                "related-link": "/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/AAI-12002-vm230w/AAI-region-vm230w",
                "relationship-label": "org.onap.relationships.inventory.Uses",
                "relationship-data": [
                  {
                    "relationship-key": "cloud-region.cloud-owner",
                    "relationship-value": "AAI-12002-vm230w"
                  },
                  {
                    "relationship-key": "cloud-region.cloud-region-id",
                    "relationship-value": "AAI-region-vm230w"
                  }
                ],
                "related-to-property": [
                  {
                    "property-key": "cloud-region.owner-defined-type",
                    "property-value": null
                  }
                ]
              }
            ]
          }
        }
      },
      "oam_group": {
        "instance-group": {
          "instance-group-role": "JZmha7QSS4tJ",
          "model-invariant-id": "model-id3",
          "model-version-id": "a0efd5fc-f7be-4502-936a-a6c6392b958f",
          "id": "AAI-12002-test3-vm230w",
          "description": "a9DEa0kpY",
          "instance-group-type": "type",
          "resource-version": "1520888659539",
          "instance-group-name": "wKmBXiO1xm8bK",
          "instance-group-function": "testfunction2",
          "relationship-list": {
            "relationship": [
              {
                "relationDataList": [
                  {
                    "relationship-key": "cloud-region.cloud-owner",
                    "relationship-value": "AAI-12002-vm230w"
                  },
                  {
                    "relationship-key": "cloud-region.cloud-region-id",
                    "relationship-value": "AAI-region-vm230w"
                  }
                ],
                "relatedToPropertyList": [
                  {
                    "property-key": "cloud-region.owner-defined-type",
                    "property-value": null
                  }
                ],
                "related-to": "cloud-region",
                "related-link": "/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/AAI-12002-vm230w/AAI-region-vm230w",
                "relationship-label": "org.onap.relationships.inventory.Uses",
                "relationship-data": [
                  {
                    "relationship-key": "cloud-region.cloud-owner",
                    "relationship-value": "AAI-12002-vm230w"
                  },
                  {
                    "relationship-key": "cloud-region.cloud-region-id",
                    "relationship-value": "AAI-region-vm230w"
                  }
                ],
                "related-to-property": [
                  {
                    "property-key": "cloud-region.owner-defined-type",
                    "property-value": null
                  }
                ]
              }
            ]
          }
        }
      }
    };
    let expectedResult = {
      "requestInfo": {
        "productFamilyId": "e433710f-9217-458d-a79d-1c7aff376d89",
        "source": "VID",
        "requestorId": "az2016",
        "suppressRollback": false
      },
      "lineOfBusiness": Object({ lineOfBusinessName: "onap" }),
      "cloudConfiguration": {
        "lcpCloudRegionId": "AAIAIC25",
        "tenantId": "092eb9e8e4b7412e8787dd091bc58e86"
      },
      "platform": Object({ platformName: "plat1" }),
      "modelInfo": {
        modelCustomizationId :'1',
        "modelVersionId": "61535073-2e50-4141-9000-f66fea69b433",
        "modelCustomizationName": "vDOROTHEA 0",
        "modelName": "vDOROTHEA",
        "modelInvariantId": "fcdf49ce-6f0b-4ca2-b676-a484e650e734",
        "modelType": "vnf",
        "modelVersion": "0.2"
      },
      "requestParameters": {
        "userParams": [],
        "testApi": "GR_API"
      },
      "relatedInstanceList": [
        {
          "relatedInstance": {
            "instanceId": "6bce7302-70bd-4057-b48e-8d5b99e686ca",
            "modelInfo": {
              "modelVersionId": "6bce7302-70bd-4057-b48e-8d5b99e686ca",
              "modelName": "vDOROTHEA_srv",
              "modelInvariantId": "9aa04749-c02c-432d-a90c-18caa361c833",
              "modelType": "service",
              "modelVersion": "1.0"
            }
          }
        },
        {
          "relatedInstance": {
            "instanceId": "AAI-12002-test3-vm230w",
            "modelInfo": {
              "modelType": "networkInstanceGroup"
            }
          }
        },
        {
          "relatedInstance": {
            "instanceId": "AAI-12002-test3-vm230w",
            "modelInfo": {
              "modelType": "networkInstanceGroup"
            }
          }
        }
      ]
    };


    let actualResult_withEcompGeneratedNaming = <any>createRequest("az2016",userInputs_withEcompGeneratedNaming, service, serviceInstanceId, networkInstanceGroups,'vDOROTHEA 0','1');
    expect(actualResult_withEcompGeneratedNaming).toEqual(expectedResult);
    expectedResult["requestInfo"]["instanceName"] = "New Name";
    let actualResult_withoutEcompGeneratedNaming = <any>createRequest("az2016",userInputs_withoutEcompGeneratedNaming, service, serviceInstanceId, networkInstanceGroups,'vDOROTHEA 0','1');
    expect(actualResult_withoutEcompGeneratedNaming).toEqual(expectedResult);
    sessionStorage.removeItem("msoRequestParametersTestApiValue");
  });
});
