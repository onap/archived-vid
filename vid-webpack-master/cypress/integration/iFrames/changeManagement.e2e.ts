///<reference path="../../../node_modules/cypress/types/index.d.ts"/> / <reference types="Cypress" />

import {JsonBuilder} from "../../support/jsonBuilders/jsonBuilder";
import {AAISubDetailsModel} from "../../support/jsonBuilders/models/aaiSubDetails.model";

function stubSubscriberDetails() {
  let jsonBuilderAAISubDetailsModel: JsonBuilder<AAISubDetailsModel> = new JsonBuilder<AAISubDetailsModel>();
  cy.readFile('cypress/support/jsonBuilders/mocks/jsons/defect710619/aaiSubDetailsE2E.json').then((res) => {
    jsonBuilderAAISubDetailsModel.basicJson(
      res,
      Cypress.config('baseUrl') + "/aai_sub_details/e433710f-9217-458d-a79d-1c7aff376d89**",
      200,
      0,
      "aai-sub-details")
  });
}

function openNewVnfChangeModal() {
  cy.visit('/serviceModels.htm#/change-management');
  cy.getElementByDataTestsId('create-new-change-management').click();
  cy.getElementByDataTestsId('newChangeManagementForm');
}

function fillSubscriberAndServiceType() {
  cy.selectDropdownOptionByText('subscriberName', 'SILVIA ROBBINS');
  cy.selectDropdownOptionByText('serviceType', 'TYLER SILVIA');
}

describe('Change management AKA VNF changes', function () {
  let jsonBuilderVnfData: JsonBuilder<any> = new JsonBuilder<any>();

  let aaiGetVNFDataUrl = Cypress.config('baseUrl') + "/get_vnf_data_by_globalid_and_service_type/e433710f-9217-458d-a79d-1c7aff376d89/TYLER SILVIA";
  //describe('Contact us', () => {

  beforeEach(() => {
    cy.login();
    cy.initAAIMock();
    stubSubscriberDetails();

    cy.route(`${Cypress.config('baseUrl')}/flags`,
      {
        "FLAG_FLASH_REDUCED_RESPONSE_CHANGEMG": true,
        "FLAG_FLASH_CLOUD_REGION_AND_NF_ROLE_OPTIONAL_SEARCH": true,
        "FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST": true,
      })
    .as('initFlags');

    cy.readFile('cypress/support/jsonBuilders/mocks/jsons/get_vnf_data.json').then((res) => {
      jsonBuilderVnfData.basicJson(
        res,
        aaiGetVNFDataUrl + '**',
        200,
        0,
        "aai_get_vnf_data")
    });

    openNewVnfChangeModal();

  });

  afterEach(() => {
    cy.screenshot();
  });

  function fillCloudRegion() {
    cy.selectDropdownOptionByText('cloudRegion', 'AAIAIC25 (AIC)');
  }

  function fillNfRole() {
    cy.getElementByDataTestsId('vnfType').type('vMobileDNS');
  }

  it(`nf role input and cloud region input and search vnfs button should be disabled  without subscriber and serviceType`, function () {
    cy.get('#searchVNF').should('be.disabled');
    cy.getElementByDataTestsId('cloudRegion').should('be.disabled');
    cy.getElementByDataTestsId('vnfType').should('be.disabled')
  });

  it(`search vnfs without nf role and cloud region`, function () {

    fillSubscriberAndServiceType();

    cy.get('#searchVNF').click();

    cy.wait('@aai_get_vnf_data')
      .its('url').should('equal', aaiGetVNFDataUrl)
  });


  it(`search vnfs by nf role and cloud region`, function () {

    fillSubscriberAndServiceType();
    fillNfRole();
    fillCloudRegion();

    cy.get('#searchVNF').click();

    cy.wait('@aai_get_vnf_data')
      .its('url').should('equal', aaiGetVNFDataUrl + "?cloudRegion=AAIAIC25&nfRole=vMobileDNS")
  });


  it(`search vnfs by cloud region`, function () {

    fillSubscriberAndServiceType();
    fillCloudRegion();
    cy.get('#searchVNF').click();

    cy.wait('@aai_get_vnf_data')
    .its('url').should('equal', aaiGetVNFDataUrl + "?cloudRegion=AAIAIC25")
  });

  it(`search vnfs by nf role`, function () {

    fillSubscriberAndServiceType();
    fillNfRole();
    cy.get('#searchVNF').click();

    cy.wait('@aai_get_vnf_data')
    .its('url').should('equal', aaiGetVNFDataUrl + "?nfRole=vMobileDNS")
  });
});

describe('Change management AKA VNF changes with SO workflows', function () {
  beforeEach(() => {
    cy.login();
    cy.initAAIMock();
    stubSubscriberDetails();

    cy.route(`${Cypress.config('baseUrl')}/flags**`,
      {
        "FLAG_FLASH_REDUCED_RESPONSE_CHANGEMG": true,
        "FLAG_FLASH_CLOUD_REGION_AND_NF_ROLE_OPTIONAL_SEARCH": false,
        "FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST": true,
        "FLAG_HANDLE_SO_WORKFLOWS": true,
        "FLAG_DISABLE_HOMING": true,
        "FLAG_ADD_MSO_TESTAPI_FIELD": true,
      })
    .as('initFlags');

    cy.route(`${Cypress.config('baseUrl')}/get_vnf_data_by_globalid_and_service_type/e433710f-9217-458d-a79d-1c7aff376d89/TYLER SILVIA`,
      'fixture:../support/jsonBuilders/mocks/jsons/get_vnf_data_by_globalid_and_service_type.json')
    .as('aai_get_vnf_data');

    cy.route(`${Cypress.config('baseUrl')}/rest/models/services?distributionStatus=DISTRIBUTED`,
      'fixture:../support/jsonBuilders/mocks/jsons/bug616888/list-services.json')
    .as('list_services');

    cy.route(`${Cypress.config('baseUrl')}/rest/models/services/74fa72dd-012b-49c3-800d-06b12bcaf1a0`,
      'fixture:../support/jsonBuilders/mocks/jsons/bug616888/Dror_service1806_Macro1.json')
    .as('get_service_model');

    cy.route('POST', `${Cypress.config('baseUrl')}/aai_get_version_by_invariant_id`,
      {
        "model": [
          {
            "model-invariant-id": "e88d6582-b7e5-4dc6-ac9c-6f7130727a92",
            "model-type": "resource",
            "resource-version": "1507747452923",
            "model-vers": {
              "model-ver": [
                {
                  "model-version-id": "1ecfa5f1-78c5-41f6-ad34-753ca1fcdc5d",
                  "model-name": "092017_VSP_GINGERby11",
                  "model-version": "1.0",
                  "model-description": "092017_VSP_GINGERby11",
                }
              ]
            }
          },
        ]
      }
    )
    .as('aai_get_version_by_invariant_id');

    openNewVnfChangeModal();
  });

  afterEach(() => {
    cy.screenshot();
  });


  it(`vnf software upgrade: given vnf with matching workflow -- payload is as expected`, function () {

    cy.route(`${Cypress.config('baseUrl')}/workflows-management/workflows?vnfModelId=1ecfa5f1-78c5-41f6-ad34-753ca1fcdc5d`,
      [{
        "id": "ab6478e4-ea33-3346-ac12-ab121484a333",
        "workflowName": "inPlaceSoftwareUpdate",
        "name": "VNF In Place Software Update",
        "source": "native",
        "workflowInputParameters": [
          {
            "label": "New Software Version",
            "inputType": "text",
            "required": true,
            "soFieldName": "new_software_version",
            "soPayloadLocation": "userParams",
            "validation": []
          }
        ]
      }])
    .as('workflows_inPlaceSoftwareUpdate');

    fillSubscriberAndServiceType();
    selectNfRole();
    selectVnf();

    cy.get('#workflow').select('VNF In Place Software Update');

    cy.get('#internal-workflow-parameter-text-2-operations-timeout')
      .type("1");
    cy.get('#internal-workflow-parameter-text-3-existing-software-version')
      .type("1.0");
    cy.get('#internal-workflow-parameter-text-4-new-software-version')
      .type("3.0");

    cy.route('POST', `${Cypress.config('baseUrl')}/change-management/workflow/zolson3amdns02test2`)
      .as('apply_workflow');

    cy.get('#submit').click();

    cy.wait('@apply_workflow').then(xhr => {
      cy.deepCompare(xhr.request.body, {
        "requestType": "VNF In Place Software Update",
        "requestDetails": [
          {
            "vnfName": "zolson3amdns02test2",
            "vnfInstanceId": "8e5e3ba1-3fe6-4d86-966e-f9f03dab4855",
            "modelInfo": {
              "modelType": "vnf",
              "modelInvariantId": "e88d6582-b7e5-4dc6-ac9c-6f7130727a92",
              "modelVersionId": "1ecfa5f1-78c5-41f6-ad34-753ca1fcdc5d",
              "modelName": "zolson3amdns02test2",
              "modelCustomizationId": "cc40295f-a834-4f42-b634-32fff9d6489b"
            },
            "cloudConfiguration": {
              "lcpCloudRegionId": "mdt1",
              "tenantId": "88a6ca3ee0394ade9403f075db23167e"
            },
            "requestInfo": {
              "source": "VID",
              "suppressRollback": false,
              "requestorId": "demo"
            },
            "relatedInstanceList": [
              {
                "relatedInstance": {
                  "instanceId": "97315a05-e6f3-4c47-ae7e-d850c327aa08",
                  "modelInfo": {
                    "modelType": "service",
                    "modelInvariantId": "bceeaff9-5716-42bf-8fa1-8c6f720cf61d",
                    "modelVersionId": "74fa72dd-012b-49c3-800d-06b12bcaf1a0",
                    "modelName": "Dror_service1806_Macro1",
                    "modelVersion": "2.0"
                  }
                }
              }
            ],
            "requestParameters": {
              "payload": "{\"existing_software_version\":\"1.0\",\"new_software_version\":\"3.0\",\"operations_timeout\":\"1\"}"
            }
          }
        ]
      });
    });

  });

  it(`pnf software upgrade: given pnf with matching workflow -- payload is as expected`, function () {

    cy.route(`${Cypress.config('baseUrl')}/workflows-management/workflows?vnfModelId=1ecfa5f1-78c5-41f6-ad34-753ca1fcdc5d`,
      [{
        "id": "ab6478e4-ea33-3346-ac12-ab121484a333",
        "workflowName": "PNFSoftwareUpgrade",
        "name": "PNF Software Upgrade",
        "source": "native",
        "workflowInputParameters": [
          {
            "label": "Target Software Version",
            "inputType": "text",
            "required": true,
            "soFieldName": "targetSoftwareVersion",
            "soPayloadLocation": "userParams",
            "validation": []
          }
        ]
      }])
      .as('workflows_PNFSoftwareUpgrade');

    fillSubscriberAndServiceType();
    selectNfRole();
    selectPnf();

    cy.get('#workflow').select('PNF Software Upgrade');

    cy.get('#internal-workflow-parameter-text-6-target-software-version')
      .type("demo-sw-ver2.0.0");

    cy.route('POST', `${Cypress.config('baseUrl')}/change-management/workflow/zolson3amdns02test2`)
      .as('apply_workflow');

    cy.get('#submit').click();

    cy.wait('@apply_workflow').then(xhr => {
      cy.deepCompare(xhr.request.body, {
        "requestType": "PNF Software Upgrade",
        "requestDetails": [
          {
            "pnfInstanceId": "8e5e3ba1-3fe6-4d86-966e-f9f03dab4855",
            "modelInfo": {
              "modelType": "pnf",
              "modelInvariantId": "e88d6582-b7e5-4dc6-ac9c-6f7130727a92",
              "modelVersionId": "1ecfa5f1-78c5-41f6-ad34-753ca1fcdc5d",
              "modelName": "zolson3amdns02test2",
              "modelCustomizationId": "cc40295f-a834-4f42-b634-32fff9d6489b"
            },
            "cloudConfiguration": {
              "lcpCloudRegionId": "mdt1",
              "tenantId": "88a6ca3ee0394ade9403f075db23167e"
            },
            "requestInfo": {
              "source": "VID",
              "suppressRollback": false,
              "requestorId": "demo"
            },
            "requestParameters": {
              "userParams":[
                {
                  "name":"pnfId",
                  "value":"8e5e3ba1-3fe6-4d86-966e-f9f03dab4855"
                },
                {
                  "name":"pnfName",
                  "value":"zolson3amdns02test2"
                },
                {
                  "name":"targetSoftwareVersion",
                  "value":"demo-sw-ver2.0.0"
                }
              ]
            }
          }
        ]
      });
    });

  });

  function selectNfRole() {
    cy.get('#vnfType').select('vMobileDNS');
  }

  function selectVnf() {
    cy.get('#fromVNFVersion').select('1.0');

    cy.log("open the Available VNF list").get('#vnfName').click();
    cy.get('li').contains('zolson3amdns02test2').click();
    cy.log("collapse the Available VNF list").get('#vnfName').click();
  }

  function selectPnf() {
    cy.get('#fromVNFVersion').select('1.0');

    cy.log("open the Available VNF list").get('#vnfName').click();
    cy.get('li').contains('zolson3amdns02test2').click();
    cy.log("collapse the Available VNF list").get('#vnfName').click();
  }


});
