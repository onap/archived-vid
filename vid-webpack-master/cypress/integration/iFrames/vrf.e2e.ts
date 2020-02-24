import {JsonBuilder} from "../../support/jsonBuilders/jsonBuilder";
import {ServiceModel} from "../../support/jsonBuilders/models/service.model";

describe('Drawing board : VRF', function () {

  var jsonBuilderAndMock: JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();

  beforeEach(() => {
      cy.clearSessionStorage();
      cy.setReduxState();
      cy.preventErrorsOnLoading();
      cy.initAAIMock();
      cy.initActiveNetworks();
      cy.initActiveVPNs();
      cy.initVidMock();
      cy.login();

      cy.server().route({
        url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
        method: 'POST',
        status: 200,
        response: "[]",
      }).as("expectedPostAsyncInstantiation");
  });

  afterEach(() => {
    cy.screenshot();
  });


  describe('vrf drawing board', () => {
    it('should show vrf model and vrf instance correctly', () => {
      const serviceModelId: string = "f028b2e2-7080-4b13-91b2-94944d4c42d8";
      const vrfEntryName: string = "VRF Entry Configuration 0";
      initDrawingBoardWithColectionResource(serviceModelId);
      cy.get('.vf-type').contains('VRF');
      cy.getElementByDataTestsId('available-models-tree').getElementByDataTestsId('node-name').contains(vrfEntryName);

      /*
      Right tree
      */

      const rightShouldHaves: { [dataTestId: string]: { [dataTestId: string]: string; }; } = {
        'node-9cac02be-2489-4374-888d-2863b4511a59-VRF Entry Configuration 0:0': {
          'node-type-indicator': 'VRF',
          'node-name': '<Automatically Assigned>'
        },
        'node-network-instance-model-version-id-undefined:0': {
          'node-type-indicator': 'N',
          'node-name': 'NETWORK1_INSTANCE_NAME',
          'status-property-orchStatus': 'Assigned',
          'status-property-provStatus': 'prov'
        },
        'node-vpn-model-version-id-undefined:0': {
          'node-type-indicator': 'VPN',
          'node-name': 'VPN1_INSTANCE_NAME',
          'status-property-orchStatus': 'Assigned',
          'status-property-provStatus': 'prov'
        }
      };

      for (let node in rightShouldHaves) {
        var [nodeName, nodeEq] = node.split(":");
        for (let span in rightShouldHaves[node]) {
          cy.getElementByDataTestsId(nodeName).eq(+nodeEq).find(`[data-tests-id='${span}']`).should('have.text', rightShouldHaves[node][span]);

        }
      }
    });


    it('vrf on click add should show generic modal ', () => {
      const serviceModelId: string = "f028b2e2-7080-4b13-91b2-94944d4c42d8";
      initDrawingBoardWithColectionResource(serviceModelId);
      cy.getElementByDataTestsId('searchByNetworkRole').click({force: true}).then(() => {
        cy.get('.allCheckboxAreSelected input').should('have.attr', 'disabled');
        cy.get('.sdcCheckboxMember input').eq(0).should('not.have.attr', 'disabled');

        cy.get('.sdcCheckboxMember input').eq(0).click({force: true});
        cy.get('table thead .allCheckboxAreSelected input').should('have.attr', 'disabled');
        cy.get('.sdcCheckboxMember input').eq(1).should('not.have.attr', 'disabled');
        cy.getElementByDataTestsId("vnf-members-search").find('input').type("2.0");
      });
    });

    it('check VRF popup', () => {
      const serviceModelId: string = "f028b2e2-7080-4b13-91b2-94944d4c42d8";
      initDrawingBoardWithColectionResource(serviceModelId);
      cy.getElementByDataTestsId('searchByNetworkRole').click({force: true}).then(() => {
        checkNetworkTableHeaders();
        checkNetworkPopupTitles();
        cy.validateSelectOptions('roles-criteria', roleOptions);
        // set Network should be disabled
        cy.isElementContainsAttr("setMembersBtn", "disabled");
        cy.get('.sdcCheckboxMember input').eq(0).click({force: true}).then(() => {
          cy.isElementNotContainsAttr("setMembersBtn", "disabled");
          cy.getElementByDataTestsId('setMembersBtn').click({force: true}).then(() => {
            checkVPNTableHeaders();
            checkVPNPopupTitles();
            cy.isElementContainsAttr("setMembersBtn", "disabled");
            cy.get('.sdcCheckboxMember[data-tests-id=\'120d39fb-3627-473d-913c-d228dd0f8e5b\'] input').eq(0).click({force: true}).then(() => {
              cy.isElementNotContainsAttr("setMembersBtn", "disabled");
              cy.getElementByDataTestsId('setMembersBtn').click({force: true}).then(() => {
                cy.isElementNotContainsAttr("deployBtn", "disabled");
                cy.getElementByDataTestsId('deployBtn').click({force: true}).then(() => {
                  cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
                    cy.readFile('../vid-automation/src/test/resources/vrf/vrfServiceCreateRequest.json').then((expectedResult) => {
                      cy.deepCompare(xhr.request.body, expectedResult);
                    });
                  });
                });
              });
            });
          });
        })
      });
    });

    it('create new VRF and change associations', () => {

      const oldVPNDataTestId: string = '120d39fb-3627-473d-913c-d228dd0f8e5b';
      const oldNETWORKDataTestId: string = '10a74149-c9d7-4918-bbcf-d5fb9b1799ce';
      const newVPNDataTestId: string = '46fcb25a-e7ba-4d96-99ba-3bb6eae6aba7';
      const newNETWORKDataTestId: string = '3b3308d4-0cd3-43e4-9a7b-d1925c861135';

      const serviceModelId: string = "f028b2e2-7080-4b13-91b2-94944d4c42d8";

      const redux = reduxWithVrf(serviceModelId);
      redux.service.serviceInstance[serviceModelId].vrfs = <any>{};
      cy.setReduxState(<any>redux);
      cy.openIframe(`app/ui/#/servicePlanning?serviceModelId=${serviceModelId}`);

      cy.getElementByDataTestsId('searchByNetworkRole').click({force: true}).then(() => {
        cy.isElementContainsAttr("setMembersBtn", "disabled");
        cy.get('.sdcCheckboxMember[data-tests-id="' + newNETWORKDataTestId + '"] input').eq(0).click({force: true}).then(() => {
          cy.isElementNotContainsAttr("setMembersBtn", "disabled");
          cy.getElementByDataTestsId('setMembersBtn').click({force: true}).then(() => {
            cy.isElementContainsAttr("setMembersBtn", "disabled");
            cy.get('.sdcCheckboxMember[data-tests-id="' + newVPNDataTestId + '"] input').eq(0).click({force: true}).then(() => {
              cy.isElementNotContainsAttr("setMembersBtn", "disabled");
              cy.getElementByDataTestsId('setMembersBtn').click({force: true}).then(() => {
                cy.get('#VRF').should('have.length', 1);
                cy.get('#VRF .icon-browse').click({force: true}).then(() => {
                  cy.getElementByDataTestsId('context-menu-changeAssociations').click(); // click on change associations
                  cy.getElementByDataTestsId(oldNETWORKDataTestId).get('input').should('be.checked'); // check if selected network is checked.
                  cy.getElementByDataTestsId('setMembersBtn').click({force: true}).then(() => { // click 'NEXT' (set network)
                    cy.getElementByDataTestsId(oldVPNDataTestId).get('input').should('be.checked'); // check if selected VPN is checked

                    cy.get(".sdcCheckboxMember[data-tests-id='" + newVPNDataTestId + "'] input").check({force: true}).then(() => { // select other VPN
                      cy.getElementByDataTestsId('cancelBtn').click().then(() => {
                        cy.get(".sdcCheckboxMember[data-tests-id='" + newNETWORKDataTestId + "'] input").check({force: true}).then(() => { // select other VPN
                          cy.getElementByDataTestsId('setMembersBtn').click();
                          cy.get(".sdcCheckboxMember[data-tests-id='" + newVPNDataTestId + "'] input")
                          cy.getElementByDataTestsId('setMembersBtn').click();

                          checkSelectedRows(newNETWORKDataTestId, newVPNDataTestId);
                        })
                      });
                    })
                  });
                });
              })
            });
          });
        })
      });
    });

    it('delete vrf', () => {
      cy.permissionVidMock();
      const SUBSCRIBER_ID: string = "e433710f-9217-458d-a79d-1c7aff376d89";
      const SERVICE_TYPE: string = "TYLER SILVIA";
      const SERVICE_MODEL_ID: string = '4117a0b6-e234-467d-b5b9-fe2f68c8b0fc';
      const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";

      configExpectedPostAsyncInstantiationDelete();
      configServiceTopologyWithVRF(SUBSCRIBER_ID, SERVICE_TYPE, SERVICE_INSTANCE_ID);
      configServiceTreeWithMultipleChildren_serviceModel(SERVICE_MODEL_ID);

      cy.readFile('../vid-automation/src/test/resources/VnfGroup/deleteServiceWith2VnfGroupsRequest_AndThreeGroupMembers.json').then((expectedResult) => {
        cy.openIframe(`app/ui/#/servicePlanning/EDIT?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}`);
        cy.serviceActionDelete();
        cy.getElementByDataTestsId('delete-status-type-header').should('exist');
        cy.serviceActionUndoDelete();
        cy.getElementByDataTestsId('delete-status-type-header').should('not.exist');
        cy.serviceActionDelete();
        cy.isNodeDeleted(0);
        cy.isNodeNotDeleted(1);
        cy.isNodeNotDeleted(2);
        cy.isElementNotContainsAttr("deployBtn", "disabled");
        cy.getDrawingBoardDeployBtn().click();
        /*cy.wait('@expectedPostAsyncInstantiationDelete').then(xhr => {
          cy.readFile('../vid-automation/src/test/resources/asyncInstantiation/vidRequestDeleteMacroService.json').then((expectedResult) => {
            cy.deepCompare(xhr.request.body, expectedResult);
          });
        });*/
      });
    });

    it('vpn component info', () => {
      cy.permissionVidMock();
      const SUBSCRIBER_ID: string = "e433710f-9217-458d-a79d-1c7aff376d89";
      const SERVICE_TYPE: string = "TYLER SILVIA";
      const SERVICE_MODEL_ID: string = '4117a0b6-e234-467d-b5b9-fe2f68c8b0fc';
      const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";

      configServiceTopologyWithVRF(SUBSCRIBER_ID, SERVICE_TYPE, SERVICE_INSTANCE_ID);
      configServiceTreeWithMultipleChildren_serviceModel(SERVICE_MODEL_ID);

      cy.openIframe(`app/ui/#/servicePlanning/EDIT?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}`);
      cy.getElementByDataTestsId("node-vpn-model-version-id-undefined").eq(0).click();
      cy.getElementByDataTestsId("model-item-value-Route target id").should("have.text","mock-global-1");
      cy.getElementByDataTestsId("model-item-value-Route target role").should("have.text","mock-role-x");
      cy.getElementByDataTestsId("model-item-value-Customet VPN ID").should("have.text","VPN1260");
      cy.getElementByDataTestsId("model-item-value-Region").should("have.text","USA,EMEA");
    });

  });

  it('network component info', () => {
    cy.permissionVidMock();
    const SUBSCRIBER_ID: string = "e433710f-9217-458d-a79d-1c7aff376d89";
    const SERVICE_TYPE: string = "TYLER SILVIA";
    const SERVICE_MODEL_ID: string = '4117a0b6-e234-467d-b5b9-fe2f68c8b0fc';
    const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";

    configServiceTopologyWithVRF(SUBSCRIBER_ID, SERVICE_TYPE, SERVICE_INSTANCE_ID);
    configServiceTreeWithMultipleChildren_serviceModel(SERVICE_MODEL_ID);

    cy.openIframe(`app/ui/#/servicePlanning/EDIT?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}`);
    cy.getElementByDataTestsId("node-network-instance-model-version-id-undefined").eq(0).click();
    cy.getElementByDataTestsId("model-item-value-Route target id").should("have.text","mock-global-1");
    cy.getElementByDataTestsId("model-item-value-Route target role").should("have.text","mock-role-x");
  });

  function configServiceTopologyWithVRF(SUBSCRIBER_ID: string, SERVICE_TYPE: string, SERVICE_INSTANCE_ID: string) {
    cy.readFile('../vid-automation/src/test/resources/aaiGetInstanceTopology/serviceWithVrfTopology.json').then((res) => {
      jsonBuilderAndMock.basicJson(
        res,
        Cypress.config('baseUrl') + `/aai_get_service_instance_topology/${SUBSCRIBER_ID}/${SERVICE_TYPE}/${SERVICE_INSTANCE_ID}`,
        200, 0,
        "serviceWithVRF",
      )
    });
  }

  function configExpectedPostAsyncInstantiationDelete()
  {
    cy.server().route({
      url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
      method: 'POST',
      status: 200,
      response: "[]",
    }).as("expectedPostAsyncInstantiationDelete");
  }

  function configServiceTreeWithMultipleChildren_serviceModel(SERVICE_MODEL_ID: string) {
    cy.readFile('../vid-automation/src/test/resources/vrf/vrfServiceRoleResponse.json').then((res) => {
      jsonBuilderAndMock.basicJson(
        res,
        Cypress.config('baseUrl') + `/rest/models/services/${SERVICE_MODEL_ID}`,
        200,
        0,
        "ServiceTreeWithMultipleChildren_serviceModel",
      )
    });
  }

  let roleOptions: string[] = [
    '-- select an option --',
    'Not assigned',
    'oam_calea_net_0',
    'oam_calea_net_1',
    'oam_calea_net_2',
    'oam_calea_net_3',
  ];

  function checkSelectedRows(networkFataTestId: string, vpnDataTestId: string) {
    cy.get('#VRF .icon-browse').click({force: true}).then(() => {
      cy.getElementByDataTestsId('context-menu-changeAssociations').click(); // click on change associations
      cy.getElementByDataTestsId(networkFataTestId).get('input').should('be.checked'); // check if selected network is checked.
      cy.getElementByDataTestsId('setMembersBtn').click({force: true}).then(() => { // click 'NEXT' (set network)
        cy.getElementByDataTestsId(vpnDataTestId).get('input').should('be.checked'); // check if selected VPN is checked
        cy.getElementByDataTestsId('setMembersBtn').click();
        cy.getElementByDataTestsId('setMembersBtn').click();
      });
    });
  }

  function reduxWithVrf(serviceModelId: string) {
    return {
      "service": {
        "subscribers": [
          {
            "id": "e433710f-9217-458d-a79d-1c7aff376d89",
            "name": "SILVIA ROBBINS",
            "isPermitted": true
          },
        ],
        "serviceHierarchy": {
          [serviceModelId]: {
            "service": {
              "uuid": serviceModelId,
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
              "vidNotions": {
                "instantiationUI": "macroService",
                "modelCategory": "other",
                "viewEditUI": "legacy",
                "instantiationType": "Macro"
              }
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
                "properties": {
                  "ecomp_generated_naming": "false"
                },
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
                "properties": {
                  "ecomp_generated_naming": "false"
                },
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
          [serviceModelId]: {
            "action": "Create",
            "isDirty": false,
            "vrfs": {
              "VRF Entry Configuration 0": {
                "originalName": null,
                "trackById": "VRF1_INSTANCE_ID",
                "action": "None",
                "instanceId": "VRF1_INSTANCE_ID",
                "instanceType": "VRF1_INSTANCE_TYPE",
                "orchStatus": null,
                "provStatus": null,
                "inMaint": false,
                "modelInfo": {
                  "modelInvariantId": "b67a289b-1688-496d-86e8-1583c828be0a",
                  "modelVersionId": "9cac02be-2489-4374-888d-2863b4511a59",
                  "modelCustomizationId": "dd024d73-9bd1-425d-9db5-476338d53433",
                  "modelType": "vrf"
                },
                "uuid": "9cac02be-2489-4374-888d-2863b4511a59",
                "productFamilyId": null,
                "lcpCloudRegionId": "olson3",
                "cloudOwner": "att-nc",
                "legacyRegion": null,
                "tenantId": "229bcdc6eaeb4ca59d55221141d01f8e",
                "lineOfBusiness": null,
                "platformName": null,
                "vfModules": {},
                "networks": {
                  "NETWORK1_INSTANCE_ID": {
                    "originalName": null,
                    "trackById": "NETWORK1_INSTANCE_ID",
                    "instanceName": "NETWORK1_INSTANCE_NAME",
                    "action": "None",
                    "instanceId": "NETWORK1_INSTANCE_ID",
                    "instanceType": "CONTRAIL30_BASIC",
                    "orchStatus": "Assigned",
                    "provStatus": "prov",
                    "inMaint": false,
                    "modelInfo": {
                      "modelInvariantId": "network-instance-model-invariant-id",
                      "modelVersionId": "network-instance-model-version-id",
                      "modelCustomizationId": "network-instance-model-customization-id",
                      "modelName": "modelName",
                      "modelType": "network"
                    },
                    "uuid": "network-instance-model-version-id",
                    "productFamilyId": null,
                    "lcpCloudRegionId": null,
                    "legacyRegion": null,
                    "tenantId": null,
                    "lineOfBusiness": null,
                    "platformName": null
                  }
                },
                "vpns": {
                  "VPN1_INSTANCE_ID": {
                    "originalName": null,
                    "trackById": "VPN1_INSTANCE_ID",
                    "instanceName": "VPN1_INSTANCE_NAME",
                    "action": "None",
                    "instanceId": "VPN1_INSTANCE_ID",
                    "instanceType": "CONTRAIL30_BASIC",
                    "orchStatus": "Assigned",
                    "provStatus": "prov",
                    "inMaint": false,
                    "modelInfo": {
                      "modelInvariantId": "vpn-instance-model-invariant-id",
                      "modelVersionId": "vpn-model-version-id",
                      "modelCustomizationId": "vpn-instance-model-customization-id",
                      "modelName": "modelName",
                      "modelType": "vpnBinding"
                    },
                    "uuid": "vpn-model-version-id",
                    "productFamilyId": null,
                    "lcpCloudRegionId": null,
                    "legacyRegion": null,
                    "tenantId": null,
                    "lineOfBusiness": null,
                    "platformName": null
                  }
                }
              }
            },
            "vnfs": {},
            "instanceParams": [
              {}
            ],
            "validationCounter": 0,
            "existingNames": {
              "dfd": ""
            },
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
            "lcpCloudRegionId": "lcpCloudRegionId",
            "tenantName": "tenantName",
            "projectName": "WATKINS",
            "rollbackOnFailure": "true",
            "aicZoneName": null,
            "owningEntityName": "WayneHolland",
            "testApi": "GR_API",
            "modelInfo": {
              "modelInvariantId": "dfc2c44c-2429-44ca-ae26-1e6dc1f207fb",
              "modelVersionId": "f028b2e2-7080-4b13-91b2-94944d4c42d8",
              "modelName": "infraVPN",
              "modelVersion": "1.0",
              "uuid": serviceModelId,
              "modelUniqueId": "f028b2e2-7080-4b13-91b2-94944d4c42d8"
            },
            "isALaCarte": true,
            "name": "infraVPN",
            "version": "1.0",
            "description": "ddd",
            "category": "Network Service",
            "uuid": serviceModelId,
            "invariantUuid": "dfc2c44c-2429-44ca-ae26-1e6dc1f207fb",
            "serviceType": "BONDING",
            "serviceRole": "INFRASTRUCTURE-VPN",
            "vidNotions": {
              "instantiationUI": "macroService",
              "modelCategory": "other",
              "viewEditUI": "legacy",
              "instantiationType": "Macro"
            },
            "isEcompGeneratedNaming": true,
            "isMultiStepDesign": false
          }
        },
        "categoryParameters": {
          "owningEntityList": [
            {
              "id": "d61e6f2d-12fa-4cc2-91df-7c244011d6fc",
              "name": "WayneHolland"
            }
          ]
        },
      }
    }
  }

  function initDrawingBoardWithColectionResource(serviceModelId: string) {
    const redux = reduxWithVrf(serviceModelId);
    cy.setReduxState(<any>redux);
    cy.openIframe(`app/ui/#/servicePlanning?serviceModelId=${serviceModelId}`);
    return redux;
  }


  function checkNetworkTableHeaders() {
    cy.get('.header-title').contains('Name');
    cy.get('.header-title').contains('Type');
    cy.get('.header-title').contains('Role');
    cy.get('.header-title').contains('Orch. Status');
    cy.get('.header-title').contains('Physical name');
    cy.get('.header-title').contains('Instance ID');
    cy.get('.header-title').contains('Model UUID');
    cy.get('.header-title').contains('Service name');
    cy.get('.header-title').contains('Service UUID');
    cy.get('.header-title').contains('Tenant');
    cy.get('.header-title').contains('Region');

    const headerTitles: string[] = [
      'Network instance name',
      'Instance version',
      'Network model'
    ];
  }


  function checkVPNTableHeaders() {
    const headerTitles: string[] = [
      'VPN instance name',
      'Version',
      'Instance ID',
      'Platform',
      'Region',
      'Route target',
      'Route target role',
      'Customer VPN ID'
    ];

    headerTitles.forEach((title: string) => {
      cy.get('.header-title').contains(title);
    });
  }


  function checkNetworkPopupTitles() {
    cy.get('.title').contains('Associate network');
    cy.get('.title-header').contains('Select a network to associate to the VRF Entry');
    cy.getElementByDataTestsId('setMembersBtn').contains('Next');
    cy.getElementByDataTestsId('Orch_status').contains('Active');
    cy.getElementByDataTestsId('Region').contains('lcpCloudRegionId');
    cy.getElementByDataTestsId('Tenant').contains('tenantName');
  }


  function checkVPNPopupTitles() {
    cy.get('.title').contains('Associate VPN');
    cy.get('.title-header').contains('Select a VPN to associate to the VRF Entry');
    cy.getElementByDataTestsId('setMembersBtn').contains('SET VPN');
  }
});
