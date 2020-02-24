///<reference path="../../../node_modules/cypress/types/index.d.ts"/>

import {JsonBuilder} from "../../support/jsonBuilders/jsonBuilder";
import {ServiceModel} from "../../support/jsonBuilders/models/service.model";

describe('Drawing board : Collection resource', function () {

  let jsonBuilderAndMock: JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();
  const serviceModelId: string = "6e59c5de-f052-46fa-aa7e-2fca9d674c44";
  beforeEach(() => {
      cy.clearSessionStorage();
      cy.preventErrorsOnLoading();
      cy.initAAIMock();
      cy.initVidMock();
      cy.login();
  });

  afterEach(() => {
    cy.screenshot();
  });


  describe('should show collection resource model correctly', () => {
    it('collection type + name', () => {

      const collectionResourceName: string = "CR_sanity 0";
      initDrawingBoardWithColectionResource(serviceModelId, collectionResourceName);
      cy.get('.vf-type').contains('CR');
      cy.getElementByDataTestsId('available-models-tree').getElementByDataTestsId('node-name').contains(collectionResourceName);
    });

    it('collection resource component info', () => {
      const collectionResourceName: string = "CR_sanity 0";
      const redux = initDrawingBoardWithColectionResource(serviceModelId, collectionResourceName)
      cy.getElementByDataTestsId(`node-${collectionResourceName}`).click().then(()=>{
        cy.getElementByDataTestsId('component-info-section-title').contains('Collection Resource INFO');
        const customizationUuid: string = redux["service"]["serviceHierarchy"][serviceModelId]["collectionResources"][collectionResourceName].customizationUuid;
        cy.getElementByDataTestsId('model-item-value-Model customization ID').contains(customizationUuid);
      });
    });
  });

  describe('collection resource in view edit mode',()=>{
    const SUBSCRIBER_ID: string = "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb";
    const SERVICE_TYPE: string = "Emanuel";
    const SERVICE_MODEL_ID: string = "6e0bec91-09f3-43aa-9cf3-e617cd0146be";
    const SERVICE_INSTANCE_ID: string = "a565e6ad-75d1-4493-98f1-33234b5c17e2";

    function verifyOnlyStatusDelete(){
      cy.getElementByDataTestsId('delete-status-type-header').should('exist');
      cy.getElementByDataTestsId('resume-status-type-header').should('not.exist');
      cy.getElementByDataTestsId('serviceName').should('have.css', 'text-decoration').and('contain', 'line-through');
      cy.isNodeDeleted(0);
      cy.isNodeDeleted(1);
    }

    function verifyOnlyStatusResume(){
      cy.getElementByDataTestsId('resume-status-type-header').should('exist');
      cy.getElementByDataTestsId('delete-status-type-header').should('not.exist');
      cy.isNodeNotDeleted(0);
      cy.isNodeNotDeleted(1);
    }

    function verifyEmptyStatus(){
      cy.getElementByDataTestsId('resume-status-type-header').should('not.exist');
      cy.getElementByDataTestsId('delete-status-type-header').should('not.exist');
      cy.isNodeNotDeleted(0);
      cy.isNodeNotDeleted(1);
    }

    function initCrViewEdit() {
        cy.permissionVidMock();
        cy.readFile('../vid-app-common/src/test/resources/getTopology/serviceWithCR/getTopologyWithCR.json').then((res) => {
          jsonBuilderAndMock.basicJson(
            res,
            Cypress.config('baseUrl') + `/aai_get_service_instance_topology/${SUBSCRIBER_ID}/${SERVICE_TYPE}/${SERVICE_INSTANCE_ID}`,
            200, 0,
            "getTopologyWithCR",
          )
        });


      cy.readFile('../vid-app-common/src/test/resources/getTopology/serviceWithCR/serviceWithCRModel.json').then((res) => {
          jsonBuilderAndMock.basicJson(
            res,
            Cypress.config('baseUrl') + `/rest/models/services/${SERVICE_MODEL_ID}`,
            200,
            0,
            "serviceModelWithCR",
          )
        });
      cy.openIframe(`app/ui/#/servicePlanning/EDIT?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}`);
    }

    it.only('should show left and right trees correctly', () => {
      initCrViewEdit();
      cy.hasClass('orchStatusValue','tag-status-value');
      cy.get('.vf-type').contains('CR');
      const crModelName = "NCM_VLAN_ym161f 0";
      cy.getElementByDataTestsId('available-models-tree').getElementByDataTestsId('node-name').contains(crModelName);

      /*
      Right tree
      */

      const rightShouldHaves: { [dataTestId: string]: { [dataTestId: string]: string; }; } = {
        'node-ce8c98bc-4691-44fb-8ff0-7a47487c11c4-undefined': {
          'node-type-indicator': 'CR',
          'node-name': 'NcmVlanSvcYm161f_77_vTSBC Customer Landing Network Collection'
        },
        'node-dd182d7d-6949-4b90-b3cc-5befe400742e-undefined': {
          'node-type-indicator': 'NCF',
          'node-name': 'NcmVlanSvcYm161f_77_vTSBC Customer Landing Network Collection'
        }
      };

      const expectedComponentInfo: { [key: string]: any } = {
        'node-ce8c98bc-4691-44fb-8ff0-7a47487c11c4-undefined': {
          labelsAndValues: [
            //['Model version', "5.0"] //to add once fix in code
            ['Instance ID', '84a351ae-3601-45e2-98df-878d6c816abc'],
            ['In maintenance', 'false']
          ],
          title: "Collection Resource Instance INFO"
        },
        'node-undefined-undefined': {
          labelsAndValues: [
            ['Model version', "1"],
            ['Instance ID', '6b3536cf-3a12-457f-abb5-fa2203e0d923'],
            ['Instance type', 'L3-NETWORK'],
            ['In maintenance', 'false'],
            ['Role', 'SUB_INTERFACE'],
            ['Collection function', 'vTSBC Customer Landing Network Collection'],
            ['Number of networks', '1']
          ],
          title: "Network Collection Function Instance INFO"
        }
      };

      for (let node in rightShouldHaves) {
        for (let span in rightShouldHaves[node]) {
          cy.getElementByDataTestsId(node).find(`[data-tests-id='${span}']`).should('have.text', rightShouldHaves[node][span]);
        }
        cy.getElementByDataTestsId(node).click().then(() => {
          let expected = <any>expectedComponentInfo[node];
          cy.assertComponentInfoTitleLabelsAndValues(expected['title'], expected['labelsAndValues']);
        })
      }
    });


    it('resume service with cr', () => {
      cy.server().route({
        url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
        method: 'POST',
        status: 200,
        response: "[]",
      }).as("expectedPostAsyncInstantiation");
      initCrViewEdit();
      cy.serviceActionDelete();
      verifyOnlyStatusDelete();
      cy.serviceActionResume();
      verifyOnlyStatusResume();
      cy.serviceActionDelete();
      verifyOnlyStatusDelete();
      cy.serviceActionUndoDelete();
      verifyEmptyStatus();
      cy.serviceActionResume();
      cy.serviceActionUndoResume();
      verifyEmptyStatus();
      cy.serviceActionResume();
      cy.getDrawingBoardDeployBtn().click();
      cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
        cy.readFile('../vid-app-common/src/test/resources/payload_jsons/resume/feRequestResumeMacroService.json').then((expectedResult) => {
           cy.deepCompare(xhr.request.body, expectedResult);
       });
      });

    });
  });

  function initDrawingBoardWithColectionResource(serviceModelId: string, collectionResourceName: string) {
      const redux = reduxWithCollectionResource(serviceModelId, collectionResourceName);
      cy.setReduxState(<any>redux);
      cy.openIframe(`app/ui/#/servicePlanning?serviceModelId=${serviceModelId}`);
      return redux;
  }


  function reduxWithCollectionResource(serviceModelId: string, collectionResourceName: string) {
    return {
      "service": {
        "serviceHierarchy": {
          [serviceModelId]: {
            "service": {
              "uuid": serviceModelId,
              "invariantUuid": "cfef8302-d90f-475f-87cc-3f49a62ef14c",
              "name": "ComplexService",
              "version": "1.0",
              "toscaModelURL": null,
              "category": "Emanuel",
              "serviceType": "",
              "serviceRole": "",
              "description": "ComplexService",
              "serviceEcompNaming": "true",
              "instantiationType": "Macro",
              "inputs": {},
              "vidNotions": {
                "instantiationUI": "legacy",
                "modelCategory": "other",
                "viewEditUI": "legacy"
              }
            },
            "vnfs": {},
            "networks": {},
            "collectionResources": {
              [collectionResourceName]: {
                "uuid": "3467f91f-1a2a-4013-a5ed-8ad99d4e06ad",
                "invariantUuid": "d0060da6-82b8-4ca0-9758-5eb2b111b926",
                "description": "CR_sanity",
                "name": "CR_sanity",
                "version": "1.0",
                "customizationUuid": "7160c618-9314-4c09-8717-b77f3d29d946",
                "inputs": {},
                "commands": {},
                "properties": {
                  "cr_sanity..Fixed..0_quantity": "10",
                  "cr_sanity..NetworkCollection..0_network_collection_function": "ABCD",
                  "ecomp_generated_naming": "false",
                  "cr_sanity..NetworkCollection..0_network_collection_description": "ABCD"
                },
                "type": "CR",
                "category": "Network L2-3",
                "subcategory": "Infrastructure",
                "resourceVendor": "ATT",
                "resourceVendorRelease": "2018.06",
                "resourceVendorModelNumber": "",
                "customizationUUID": "7160c618-9314-4c09-8717-b77f3d29d946",
                "networksCollection": {
                  "cr_sanity..NetworkCollection..0": {
                    "uuid": "445d7fa8-3e59-4606-bd76-30ba5fc677d3",
                    "invariantUuid": "9dc623b8-0ae8-47ad-a791-a21b8d8e94a8",
                    "name": "cr_sanity..NetworkCollection..0",
                    "version": "1",
                    "networkCollectionProperties": {
                      "networkCollectionFunction": "ABCD",
                      "networkCollectionDescription": "ABCD"
                    }
                  }
                }
              }
            },
            "configurations": {},
            "fabricConfigurations": {},
            "serviceProxies": {},
            "vfModules": {},
            "volumeGroups": {},
            "pnfs": {},
            "vnfGroups": {}
          }
        },
        "serviceInstance": {
          "6e59c5de-f052-46fa-aa7e-2fca9d674c44": {
            "action": "Create",
            "isDirty": true,
            "vnfs": {},
            "instanceParams": [
              {}
            ],
            "validationCounter": 0,
            "existingNames": {
              "11": "",
              "yoav": ""
            },
            "existingVNFCounterMap": {
              "91415b44-753d-494c-926a-456a9172bbb9": 1
            },
            "existingVnfGroupCounterMap": {},
            "existingNetworksCounterMap": {},
            "optionalGroupMembersMap": {},
            "networks": {},
            "vnfGroups": {},
            "bulkSize": 1,
            "instanceName": "serviceInstanceName",
            "globalSubscriberId": "e433710f-9217-458d-a79d-1c7aff376d89",
            "subscriptionServiceType": "TYLER SILVIA",
            "owningEntityId": "d61e6f2d-12fa-4cc2-91df-7c244011d6fc",
            "productFamilyId": "d8a6ed93-251c-47ca-adc9-86671fd19f4c",
            "lcpCloudRegionId": "AAIAIC25",
            "tenantId": "092eb9e8e4b7412e8787dd091bc58e86",
            "aicZoneId": "ATL53",
            "projectName": "WATKINS",
            "rollbackOnFailure": "true",
            "aicZoneName": "AAIATLTE-ATL53",
            "owningEntityName": "WayneHolland",
            "testApi": "VNF_API",
            "tenantName": "USP-SIP-IC-24335-T-01",
            "modelInfo": {
              "modelInvariantId": "cfef8302-d90f-475f-87cc-3f49a62ef14c",
              "modelVersionId": "6e59c5de-f052-46fa-aa7e-2fca9d674c44",
              "modelName": "ComplexService",
              "modelVersion": "1.0",
              "uuid": "6e59c5de-f052-46fa-aa7e-2fca9d674c44",
              "modelUniqueId": "6e59c5de-f052-46fa-aa7e-2fca9d674c44"
            },
            "isALaCarte": false,
            "name": "ComplexService",
            "version": "1.0",
            "description": "ComplexService",
            "category": "Emanuel",
            "uuid": "6e59c5de-f052-46fa-aa7e-2fca9d674c44",
            "invariantUuid": "cfef8302-d90f-475f-87cc-3f49a62ef14c",
            "serviceType": "",
            "serviceRole": "",
            "vidNotions": {
              "instantiationUI": "legacy",
              "modelCategory": "other",
              "viewEditUI": "legacy"
            },
            "isEcompGeneratedNaming": true,
            "isMultiStepDesign": false
          }
        },
        "lcpRegionsAndTenants": {
          "lcpRegionList": [
            {
              "id": "AAIAIC25",
              "name": "AAIAIC25 (AIC)",
              "isPermitted": true,
              "cloudOwner": "irma-aic"
            },
            {
              "id": "hvf6",
              "name": "hvf6 (AIC)",
              "isPermitted": true,
              "cloudOwner": "irma-aic"
            }
          ],
          "lcpRegionsTenantsMap": {
            "AAIAIC25": [
              {
                "id": "092eb9e8e4b7412e8787dd091bc58e86",
                "name": "USP-SIP-IC-24335-T-01",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              }
            ],
            "hvf6": [
              {
                "id": "bae71557c5bb4d5aac6743a4e5f1d054",
                "name": "AIN Web Tool-15-D-testalexandria",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "229bcdc6eaeb4ca59d55221141d01f8e",
                "name": "AIN Web Tool-15-D-STTest2",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "1178612d2b394be4834ad77f567c0af2",
                "name": "AIN Web Tool-15-D-SSPtestcustome",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "19c5ade915eb461e8af52fb2fd8cd1f2",
                "name": "AIN Web Tool-15-D-UncheckedEcopm",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "de007636e25249238447264a988a927b",
                "name": "AIN Web Tool-15-D-dfsdf",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "62f29b3613634ca6a3065cbe0e020c44",
                "name": "AIN/SMS-16-D-Multiservices1",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "649289e30d3244e0b48098114d63c2aa",
                "name": "AIN Web Tool-15-D-SSPST66",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "3f21eeea6c2c486bba31dab816c05a32",
                "name": "AIN Web Tool-15-D-ASSPST47",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "f60ce21d3ee6427586cff0d22b03b773",
                "name": "CESAR-100-D-sspjg67246",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "8774659e425f479895ae091bb5d46560",
                "name": "CESAR-100-D-sspjg68359",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "624eb554b0d147c19ff8885341760481",
                "name": "AINWebTool-15-D-iftach",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "214f55f5fc414c678059c383b03e4962",
                "name": "CESAR-100-D-sspjg612401",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "c90666c291664841bb98e4d981ff1db5",
                "name": "CESAR-100-D-sspjg621340",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "ce5b6bc5c7b348e1bf4b91ac9a174278",
                "name": "sspjg621351cloned",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "b386b768a3f24c8e953abbe0b3488c02",
                "name": "AINWebTool-15-D-eteancomp",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "dc6c4dbfd225474e9deaadd34968646c",
                "name": "AINWebTool-15-T-SPFET",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "02cb5030e9914aa4be120bd9ed1e19eb",
                "name": "AINWebTool-15-X-eeweww",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "f2f3830e4c984d45bcd00e1a04158a79",
                "name": "CESAR-100-D-spjg61909",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "05b91bd5137f4929878edd965755c06d",
                "name": "CESAR-100-D-sspjg621512cloned",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "7002fbe8482d4a989ddf445b1ce336e0",
                "name": "AINWebTool-15-X-vdr",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "4008522be43741dcb1f5422022a2aa0b",
                "name": "AINWebTool-15-D-ssasa",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "f44e2e96a1b6476abfda2fa407b00169",
                "name": "AINWebTool-15-D-PFNPT",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "b69a52bec8a84669a37a1e8b72708be7",
                "name": "AINWebTool-15-X-vdre",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "fac7d9fd56154caeb9332202dcf2969f",
                "name": "AINWebTool-15-X-NONPODECOMP",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "2d34d8396e194eb49969fd61ffbff961",
                "name": "DN5242-Nov16-T5",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "cb42a77ff45b48a8b8deb83bb64acc74",
                "name": "ro-T11",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "fa45ca53c80b492fa8be5477cd84fc2b",
                "name": "ro-T112",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "4914ab0ab3a743e58f0eefdacc1dde77",
                "name": "DN5242-Nov21-T1",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "d0a3e3f2964542259d155a81c41aadc3",
                "name": "test-hvf6-09",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "cbb99fe4ada84631b7baf046b6fd2044",
                "name": "DN5242-Nov16-T3",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              }
            ]
          }
        },
        "subscribers": [
          {
            "id": "ERICA5779-Subscriber-2",
            "name": "ERICA5779-Subscriber-2",
            "isPermitted": false
          },
          {
            "id": "ERICA5779-Subscriber-3",
            "name": "ERICA5779-Subscriber-3",
            "isPermitted": false
          },
          {
            "id": "ERICA5779-Subscriber-4",
            "name": "ERICA5779-Subscriber-5",
            "isPermitted": false
          },
          {
            "id": "ERICA5779-TestSub-PWT-101",
            "name": "ERICA5779-TestSub-PWT-101",
            "isPermitted": false
          },
          {
            "id": "ERICA5779-TestSub-PWT-102",
            "name": "ERICA5779-TestSub-PWT-102",
            "isPermitted": false
          },
          {
            "id": "ERICA5779-TestSub-PWT-103",
            "name": "ERICA5779-TestSub-PWT-103",
            "isPermitted": false
          },
          {
            "id": "31739f3e-526b-11e6-beb8-9e71128cae77",
            "name": "CRAIG/ROBERTS",
            "isPermitted": false
          },
          {
            "id": "DHV1707-TestSubscriber-2",
            "name": "DALE BRIDGES",
            "isPermitted": false
          },
          {
            "id": "jimmy-example",
            "name": "JimmyExampleCust-20161102",
            "isPermitted": false
          },
          {
            "id": "jimmy-example2",
            "name": "JimmyExampleCust-20161103",
            "isPermitted": false
          },
          {
            "id": "CAR_2020_ER",
            "name": "CAR_2020_ER",
            "isPermitted": true
          },
          {
            "id": "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
            "name": "Emanuel",
            "isPermitted": true
          },
          {
            "id": "21014aa2-526b-11e6-beb8-9e71128cae77",
            "name": "JULIO ERICKSON",
            "isPermitted": false
          },
          {
            "id": "DHV1707-TestSubscriber-1",
            "name": "LLOYD BRIDGES",
            "isPermitted": false
          },
          {
            "id": "e433710f-9217-458d-a79d-1c7aff376d89",
            "name": "SILVIA ROBBINS",
            "isPermitted": true
          },
          {
            "id": "global-customer-id",
            "name": "global-customer-id",
            "isPermitted": true
          }
        ],
        "productFamilies": null,
        "serviceTypes": {
          "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb": [
            {
              "id": "3",
              "name": "Emanuel",
              "isPermitted": false
            },
            {
              "id": "7",
              "name": "Kennedy",
              "isPermitted": false
            },
            {
              "id": "6",
              "name": "vJamie",
              "isPermitted": false
            },
            {
              "id": "0",
              "name": "vWINIFRED",
              "isPermitted": false
            },
            {
              "id": "1",
              "name": "vMNS",
              "isPermitted": false
            },
            {
              "id": "4",
              "name": "vMOG",
              "isPermitted": false
            },
            {
              "id": "5",
              "name": "vEsmeralda",
              "isPermitted": false
            },
            {
              "id": "2",
              "name": "vVoiceMail",
              "isPermitted": false
            }
          ],
          "e433710f-9217-458d-a79d-1c7aff376d89": [
            {
              "id": "17",
              "name": "JOHANNA_SANTOS",
              "isPermitted": false
            },
            {
              "id": "16",
              "name": "LINDSEY",
              "isPermitted": false
            },
            {
              "id": "2",
              "name": "Emanuel",
              "isPermitted": false
            },
            {
              "id": "5",
              "name": "Kennedy",
              "isPermitted": false
            },
            {
              "id": "14",
              "name": "SSD",
              "isPermitted": false
            },
            {
              "id": "1",
              "name": "TYLER SILVIA",
              "isPermitted": true
            },
            {
              "id": "12",
              "name": "VPMS",
              "isPermitted": false
            },
            {
              "id": "3",
              "name": "vJamie",
              "isPermitted": false
            },
            {
              "id": "0",
              "name": "vRichardson",
              "isPermitted": false
            },
            {
              "id": "18",
              "name": "vCarroll",
              "isPermitted": false
            },
            {
              "id": "9",
              "name": "vFLORENCE",
              "isPermitted": false
            },
            {
              "id": "13",
              "name": "vWINIFRED",
              "isPermitted": false
            },
            {
              "id": "10",
              "name": "vMNS",
              "isPermitted": false
            },
            {
              "id": "15",
              "name": "vMOG",
              "isPermitted": false
            },
            {
              "id": "8",
              "name": "vOTA",
              "isPermitted": false
            },
            {
              "id": "11",
              "name": "vEsmeralda",
              "isPermitted": false
            },
            {
              "id": "6",
              "name": "vPorfirio",
              "isPermitted": false
            },
            {
              "id": "7",
              "name": "vVM",
              "isPermitted": false
            },
            {
              "id": "4",
              "name": "vVoiceMail",
              "isPermitted": false
            }
          ]
        },
        "aicZones": [
          {
            "id": "ATL53",
            "name": "AAIATLTE-ATL53"
          },
          {
            "id": "ABC15",
            "name": "AAITESAN-ABC15"
          },
          {
            "id": "TES36",
            "name": "ABCEETES-TES36"
          },
          {
            "id": "ATL54",
            "name": "AICFTAAI-ATL54"
          },
          {
            "id": "ATL43",
            "name": "AICLOCID-ATL43"
          },
          {
            "id": "AMD15",
            "name": "AMDFAA01-AMD15"
          },
          {
            "id": "AMF11",
            "name": "AMDOCS01-AMF11"
          },
          {
            "id": "RCT1",
            "name": "AMSTERNL-RCT1"
          },
          {
            "id": "AMS1",
            "name": "AMSTNLBW-AMS1"
          },
          {
            "id": "HJH1",
            "name": "AOEEQQQD-HJH1"
          },
          {
            "id": "HJE1",
            "name": "AOEEWWWD-HJE1"
          },
          {
            "id": "MCS1",
            "name": "ASACMAMS-MCS1"
          },
          {
            "id": "AUG1",
            "name": "ASDFGHJK-AUG1"
          },
          {
            "id": "LUC1",
            "name": "ATLDFGYC-LUC1"
          },
          {
            "id": "ATL1",
            "name": "ATLNGAMA-ATL1"
          },
          {
            "id": "ATL2",
            "name": "ATLNGANW-ATL2"
          },
          {
            "id": "HPO1",
            "name": "ATLNGAUP-HPO1"
          },
          {
            "id": "ANI1",
            "name": "ATLNGTRE-ANI1"
          },
          {
            "id": "ATL44",
            "name": "ATLSANAB-ATL44"
          },
          {
            "id": "ATL56",
            "name": "ATLSANAC-ATL56"
          },
          {
            "id": "ABC11",
            "name": "ATLSANAI-ABC11"
          },
          {
            "id": "ATL34",
            "name": "ATLSANAI-ATL34"
          },
          {
            "id": "ATL63",
            "name": "ATLSANEW-ATL63"
          },
          {
            "id": "ABC12",
            "name": "ATLSECIA-ABC12"
          },
          {
            "id": "AMD18",
            "name": "AUDIMA01-AMD18"
          },
          {
            "id": "AVT1",
            "name": "AVTRFLHD-AVT1"
          },
          {
            "id": "KIT1",
            "name": "BHYJFGLN-KIT1"
          },
          {
            "id": "BHY17",
            "name": "BHYTFRF3-BHY17"
          },
          {
            "id": "RTW5",
            "name": "BHYTFRY4-RTW5"
          },
          {
            "id": "RTZ4",
            "name": "BHYTFRZ6-RTZ4"
          },
          {
            "id": "RTD2",
            "name": "BHYTFRk4-RTD2"
          },
          {
            "id": "BNA1",
            "name": "BNARAGBK-BNA1"
          },
          {
            "id": "VEL1",
            "name": "BNMLKUIK-VEL1"
          },
          {
            "id": "BOT1",
            "name": "BOTHWAKY-BOT1"
          },
          {
            "id": "CAL33",
            "name": "CALIFORN-CAL33"
          },
          {
            "id": "ATL84",
            "name": "CANTTCOC-ATL84"
          },
          {
            "id": "HSD1",
            "name": "CHASKCDS-HSD1"
          },
          {
            "id": "CHI1",
            "name": "CHILLIWE-CHI1"
          },
          {
            "id": "XCP12",
            "name": "CHKGH123-XCP12"
          },
          {
            "id": "JNL1",
            "name": "CJALSDAC-JNL1"
          },
          {
            "id": "KJN1",
            "name": "CKALDKSA-KJN1"
          },
          {
            "id": "CLG1",
            "name": "CLGRABAD-CLG1"
          },
          {
            "id": "CKL1",
            "name": "CLKSKCKK-CKL1"
          },
          {
            "id": "ATL66",
            "name": "CLLIAAII-ATL66"
          },
          {
            "id": "CQK1",
            "name": "CQKSCAKK-CQK1"
          },
          {
            "id": "CWY1",
            "name": "CWYMOWBS-CWY1"
          },
          {
            "id": "DKJ1",
            "name": "DKJSJDKA-DKJ1"
          },
          {
            "id": "DSF45",
            "name": "DSFBG123-DSF45"
          },
          {
            "id": "DSL12",
            "name": "DSLFK242-DSL12"
          },
          {
            "id": "FDE55",
            "name": "FDERT555-FDE55"
          },
          {
            "id": "VEN2",
            "name": "FGHJUHIL-VEN2"
          },
          {
            "id": "ATL64",
            "name": "FORLOAAJ-ATL64"
          },
          {
            "id": "GNV1",
            "name": "GNVLSCTL-GNV1"
          },
          {
            "id": "SAN22",
            "name": "GNVLSCTL-SAN22"
          },
          {
            "id": "KAP1",
            "name": "HIOUYTRQ-KAP1"
          },
          {
            "id": "LIS1",
            "name": "HOSTPROF-LIS1"
          },
          {
            "id": "HRG1",
            "name": "HRGHRGGS-HRG1"
          },
          {
            "id": "HST25",
            "name": "HSTNTX01-HST25"
          },
          {
            "id": "STN27",
            "name": "HSTNTX01-STN27"
          },
          {
            "id": "HST70",
            "name": "HSTNTX70-HST70"
          },
          {
            "id": "KOR1",
            "name": "HYFLNBVT-KOR1"
          },
          {
            "id": "RAD10",
            "name": "INDIPUNE-RAD10"
          },
          {
            "id": "REL1",
            "name": "INGERFGT-REL1"
          },
          {
            "id": "JAD1",
            "name": "JADECLLI-JAD1"
          },
          {
            "id": "HKA1",
            "name": "JAKHLASS-HKA1"
          },
          {
            "id": "JCS1",
            "name": "JCSJSCJS-JCS1"
          },
          {
            "id": "JCV1",
            "name": "JCVLFLBW-JCV1"
          },
          {
            "id": "KGM2",
            "name": "KGMTNC20-KGM2"
          },
          {
            "id": "KJF12",
            "name": "KJFDH123-KJF12"
          },
          {
            "id": "JGS1",
            "name": "KSJKKKKK-JGS1"
          },
          {
            "id": "LAG1",
            "name": "LARGIZON-LAG1"
          },
          {
            "id": "LAG1a",
            "name": "LARGIZON-LAG1a"
          },
          {
            "id": "LAG45",
            "name": "LARGIZON-LAG1a"
          },
          {
            "id": "LAG1b",
            "name": "LARGIZON-LAG1b"
          },
          {
            "id": "WAN1",
            "name": "LEIWANGW-WAN1"
          },
          {
            "id": "DSA1",
            "name": "LKJHGFDS-DSA1"
          },
          {
            "id": "LON1",
            "name": "LONEENCO-LON1"
          },
          {
            "id": "SITE",
            "name": "LONEENCO-SITE"
          },
          {
            "id": "ZXL1",
            "name": "LWLWCANN-ZXL1"
          },
          {
            "id": "hvf20",
            "name": "MDTWNJ21-hvf20"
          },
          {
            "id": "hvf32",
            "name": "MDTWNJ21-hvf32"
          },
          {
            "id": "AMD13",
            "name": "MEMATLAN-AMD13"
          },
          {
            "id": "MIC54",
            "name": "MICHIGAN-MIC54"
          },
          {
            "id": "MAR1",
            "name": "MNBVCXZM-MAR1"
          },
          {
            "id": "NCA1",
            "name": "NCANCANN-NCA1"
          },
          {
            "id": "NFT1",
            "name": "NFTJSSSS-NFT1"
          },
          {
            "id": "GAR1",
            "name": "NGFVSJKO-GAR1"
          },
          {
            "id": "NYC1",
            "name": "NYCMNY54-NYC1"
          },
          {
            "id": "OKC1",
            "name": "OKCBOK55-OKC1"
          },
          {
            "id": "OLG1",
            "name": "OLHOLHOL-OLG1"
          },
          {
            "id": "OLK1",
            "name": "OLKOLKLS-OLK1"
          },
          {
            "id": "NIR1",
            "name": "ORFLMANA-NIR1"
          },
          {
            "id": "JAN1",
            "name": "ORFLMATT-JAN1"
          },
          {
            "id": "ORL1",
            "name": "ORLDFLMA-ORL1"
          },
          {
            "id": "PAR1",
            "name": "PARSFRCG-PAR1"
          },
          {
            "id": "PBL1",
            "name": "PBLAPBAI-PBL1"
          },
          {
            "id": "mac10",
            "name": "PKGTESTF-mac10"
          },
          {
            "id": "mac20",
            "name": "PKGTESTF-mac20"
          },
          {
            "id": "TIR2",
            "name": "PLKINHYI-TIR2"
          },
          {
            "id": "IBB1",
            "name": "PLMKOIJU-IBB1"
          },
          {
            "id": "COM1",
            "name": "PLMKOPIU-COM1"
          },
          {
            "id": "POI1",
            "name": "PLMNJKIU-POI1"
          },
          {
            "id": "PLT1",
            "name": "PLTNCA60-PLT1"
          },
          {
            "id": "POI22",
            "name": "POIUY123-POI22"
          },
          {
            "id": "DCC1",
            "name": "POIUYTGH-DCC1"
          },
          {
            "id": "DCC1a",
            "name": "POIUYTGH-DCC1a"
          },
          {
            "id": "DCC1b",
            "name": "POIUYTGH-DCC1b"
          },
          {
            "id": "DCC2",
            "name": "POIUYTGH-DCC2"
          },
          {
            "id": "DCC3",
            "name": "POIUYTGH-DCC3"
          },
          {
            "id": "IAA1",
            "name": "QAZXSWED-IAA1"
          },
          {
            "id": "QWE1",
            "name": "QWECLLI1-QWE1"
          },
          {
            "id": "NUM1",
            "name": "QWERTYUI-NUM1"
          },
          {
            "id": "RAD1",
            "name": "RADICAL1-RAD1"
          },
          {
            "id": "RJN1",
            "name": "RJNRBZAW-RJN1"
          },
          {
            "id": "SAA13",
            "name": "SAIT1AA9-SAA13"
          },
          {
            "id": "SAA14",
            "name": "SAIT1AA9-SAA14"
          },
          {
            "id": "SDD81",
            "name": "SAIT1DD6-SDD81"
          },
          {
            "id": "SDD82",
            "name": "SAIT1DD9-SDD82"
          },
          {
            "id": "SAA11",
            "name": "SAIT9AA2-SAA11"
          },
          {
            "id": "SAA80",
            "name": "SAIT9AA3-SAA80"
          },
          {
            "id": "SAA12",
            "name": "SAIT9AF8-SAA12"
          },
          {
            "id": "SCC80",
            "name": "SAIT9CC3-SCC80"
          },
          {
            "id": "ATL75",
            "name": "SANAAIRE-ATL75"
          },
          {
            "id": "ICC1",
            "name": "SANJITAT-ICC1"
          },
          {
            "id": "SCK1",
            "name": "SCKSCKSK-SCK1"
          },
          {
            "id": "EHH78",
            "name": "SDCSHHH5-EHH78"
          },
          {
            "id": "SAA78",
            "name": "SDCTAAA1-SAA78"
          },
          {
            "id": "SAX78",
            "name": "SDCTAXG1-SAX78"
          },
          {
            "id": "SBX78",
            "name": "SDCTBXG1-SBX78"
          },
          {
            "id": "SEE78",
            "name": "SDCTEEE4-SEE78"
          },
          {
            "id": "SGG78",
            "name": "SDCTGGG1-SGG78"
          },
          {
            "id": "SXB78",
            "name": "SDCTGXB1-SXB78"
          },
          {
            "id": "SJJ78",
            "name": "SDCTJJJ1-SJJ78"
          },
          {
            "id": "SKK78",
            "name": "SDCTKKK1-SKK78"
          },
          {
            "id": "SLF78",
            "name": "SDCTLFN1-SLF78"
          },
          {
            "id": "SLL78",
            "name": "SDCTLLL1-SLL78"
          },
          {
            "id": "MAD11",
            "name": "SDFQWGKL-MAD11"
          },
          {
            "id": "HGD1",
            "name": "SDFQWHGD-HGD1"
          },
          {
            "id": "SBB78",
            "name": "SDIT1BBB-SBB78"
          },
          {
            "id": "SDG78",
            "name": "SDIT1BDG-SDG78"
          },
          {
            "id": "SBU78",
            "name": "SDIT1BUB-SBU78"
          },
          {
            "id": "SHH78",
            "name": "SDIT1HHH-SHH78"
          },
          {
            "id": "SJU78",
            "name": "SDIT1JUB-SJU78"
          },
          {
            "id": "SNA1",
            "name": "SNANTXCA-SNA1"
          },
          {
            "id": "SAM1",
            "name": "SNDGCA64-SAN1"
          },
          {
            "id": "SNG1",
            "name": "SNGPSIAU-SNG1"
          },
          {
            "id": "SSA56",
            "name": "SSIT2AA7-SSA56"
          },
          {
            "id": "STG1",
            "name": "STTGGE62-STG1"
          },
          {
            "id": "STT1",
            "name": "STTLWA02-STT1"
          },
          {
            "id": "SYD1",
            "name": "SYDNAUBV-SYD1"
          },
          {
            "id": "ATL99",
            "name": "TEESTAAI-ATL43"
          },
          {
            "id": "ATL98",
            "name": "TEESTAAI-ATL43"
          },
          {
            "id": "ATL76",
            "name": "TELEPAAI-ATL76"
          },
          {
            "id": "ABC14",
            "name": "TESAAISA-ABC14"
          },
          {
            "id": "TAT33",
            "name": "TESAAISA-TAT33"
          },
          {
            "id": "TAT34",
            "name": "TESAAISB-TAT34"
          },
          {
            "id": "TAT37",
            "name": "TESAAISD-TAT37"
          },
          {
            "id": "ATL62",
            "name": "TESSASCH-ATL62"
          },
          {
            "id": "TLP1",
            "name": "TLPNXM18-TLP1"
          },
          {
            "id": "SAN13",
            "name": "TOKYJPFA-SAN13"
          },
          {
            "id": "TOK1",
            "name": "TOKYJPFA-TOK1"
          },
          {
            "id": "TOL1",
            "name": "TOLDOH21-TOL1"
          },
          {
            "id": "TOR1",
            "name": "TOROONXN-TOR1"
          },
          {
            "id": "TOY1",
            "name": "TORYONNZ-TOY1"
          },
          {
            "id": "ATL35",
            "name": "TTESSAAI-ATL35"
          },
          {
            "id": "TUF1",
            "name": "TUFCLLI1-TUF1"
          },
          {
            "id": "SAI1",
            "name": "UBEKQLPD-SAI1"
          },
          {
            "id": "UUU4",
            "name": "UUUAAAUU-UUU4"
          },
          {
            "id": "YYY1",
            "name": "UUUAIAAI-YYY1"
          },
          {
            "id": "BAN1",
            "name": "VSDKYUTP-BAN1"
          },
          {
            "id": "WAS1",
            "name": "WASHDCSW-WAS1"
          },
          {
            "id": "APP1",
            "name": "WBHGTYUI-APP1"
          },
          {
            "id": "SUL2",
            "name": "WERTYUJK-SUL2"
          },
          {
            "id": "DEF2",
            "name": "WSBHGTYL-DEF2"
          },
          {
            "id": "DHA12",
            "name": "WSXEDECF-DHA12"
          },
          {
            "id": "MNT11",
            "name": "WSXEFBTH-MNT11"
          },
          {
            "id": "RAJ1",
            "name": "YGBIJNLQ-RAJ1"
          },
          {
            "id": "JAG1",
            "name": "YUDFJULP-JAG1"
          },
          {
            "id": "ZEN1",
            "name": "ZENCLLI1-ZEN1"
          },
          {
            "id": "ZOG1",
            "name": "ZOGASTRO-ZOG1"
          },
          {
            "id": "SDE1",
            "name": "ZXCVBNMA-SDE1"
          },
          {
            "id": "SIP1",
            "name": "ZXCVBNMK-SIP1"
          },
          {
            "id": "JUL1",
            "name": "ZXCVBNMM-JUL1"
          },
          {
            "id": "ERT1",
            "name": "ertclli1-ERT1"
          },
          {
            "id": "IOP1",
            "name": "iopclli1-IOP1"
          },
          {
            "id": "OPA1",
            "name": "opaclli1-OPA1"
          },
          {
            "id": "RAI1",
            "name": "poiuytre-RAI1"
          },
          {
            "id": "PUR1",
            "name": "purelyde-PUR1"
          },
          {
            "id": "RTY1",
            "name": "rtyclli1-RTY1"
          },
          {
            "id": "SDF1",
            "name": "sdfclli1-SDF1"
          },
          {
            "id": "SSW56",
            "name": "ss8126GT-SSW56"
          },
          {
            "id": "UIO1",
            "name": "uioclli1-UIO1"
          }
        ],
        "categoryParameters": {
          "owningEntityList": [
            {
              "id": "aaa1",
              "name": "aaa1"
            },
            {
              "id": "d61e6f2d-12fa-4cc2-91df-7c244011d6fc",
              "name": "WayneHolland"
            },
            {
              "id": "Melissa",
              "name": "Melissa"
            }
          ],
          "projectList": [
            {
              "id": "WATKINS",
              "name": "WATKINS"
            },
            {
              "id": "x1",
              "name": "x1"
            },
            {
              "id": "yyy1",
              "name": "yyy1"
            }
          ],
          "lineOfBusinessList": [
            {
              "id": "ONAP",
              "name": "ONAP"
            },
            {
              "id": "zzz1",
              "name": "zzz1"
            }
          ],
          "platformList": [
            {
              "id": "platform",
              "name": "platform"
            },
            {
              "id": "xxx1",
              "name": "xxx1"
            }
          ]
        },
        "type": "UPDATE_LCP_REGIONS_AND_TENANTS"
      },
      "global": {
        "name": null,
        "flags": {
          "FLAG_1810_IDENTIFY_SERVICE_FOR_NEW_UI": false,
          "FLAG_1902_VNF_GROUPING": true,
          "FLAG_1906_COMPONENT_INFO": true,
          "FLAG_SHOW_ASSIGNMENTS": true,
          "FLAG_A_LA_CARTE_AUDIT_INFO": true,
          "FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST": true,
          "FLAG_SHOW_VERIFY_SERVICE": true,
          "FLAG_1902_NEW_VIEW_EDIT": true,
          "FLAG_ADD_MSO_TESTAPI_FIELD": true,
          "FLAG_1906_INSTANTIATION_API_USER_VALIDATION": true,
          "FLAG_EXP_CREATE_RESOURCES_IN_PARALLEL": false,
          "FLAG_PRESENT_PROVIDER_NETWORKS_ASSOCIATIONS": true,
          "FLAG_NETWORK_TO_ASYNC_INSTANTIATION": false,
          "FLAG_ASYNC_ALACARTE_VFMODULE": true,
          "FLAG_FABRIC_CONFIGURATION_ASSIGNMENTS": true,
          "FLAG_ASYNC_ALACARTE_VNF": true,
          "FLAG_1810_AAI_LOCAL_CACHE": true,
          "FLAG_EXP_USE_DEFAULT_HOST_NAME_VERIFIER": false,
          "FLAG_SERVICE_MODEL_CACHE": true,
          "FLAG_1902_RETRY_JOB": true,
          "FLAG_EXP_ANY_ALACARTE_NEW_INSTANTIATION_UI": true,
          "FLAG_1906_AAI_SUB_DETAILS_REDUCE_DEPTH": true,
          "FLAG_VF_MODULE_RESUME_STATUS_CREATE": true,
          "FLAG_SUPPLEMENTARY_FILE": true,
          "FLAG_5G_IN_NEW_INSTANTIATION_UI": true,
          "FLAG_RESTRICTED_SELECT": false,
          "FLAG_1810_CR_LET_SELECTING_COLLECTOR_TYPE_UNCONDITIONALLY": true
        },
        "drawingBoardStatus": "CREATE",
        "type": "UPDATE_DRAWING_BOARD_STATUS"
      }
    }
  }
});
