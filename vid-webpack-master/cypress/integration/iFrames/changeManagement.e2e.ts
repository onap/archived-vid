///<reference path="../../../node_modules/cypress/types/index.d.ts"/> / <reference types="Cypress" />

import {JsonBuilder} from "../../support/jsonBuilders/jsonBuilder";
import {AAISubDetailsModel} from "../../support/jsonBuilders/models/aaiSubDetails.model";

describe('Change management AKA VNF changes', function () {
  let jsonBuilderAAISubDetailsModel: JsonBuilder<AAISubDetailsModel> = new JsonBuilder<AAISubDetailsModel>();
  let jsonBuilderVnfData: JsonBuilder<any> = new JsonBuilder<any>();

  let aaiGetVNFDataUrl = Cypress.config('baseUrl') + "/get_vnf_data_by_globalid_and_service_type/e433710f-9217-458d-a79d-1c7aff376d89/TYLER SILVIA";
  //describe('Contact us', () => {

  beforeEach(() => {
    cy.login();
    cy.initAAIMock();

    cy.readFile('cypress/support/jsonBuilders/mocks/jsons/defect710619/aaiSubDetailsE2E.json').then((res) => {
      jsonBuilderAAISubDetailsModel.basicJson(
        res,
        Cypress.config('baseUrl') + "/aai_sub_details/e433710f-9217-458d-a79d-1c7aff376d89**",
        200,
        0,
        "aai-sub-details")
    });

    cy.readFile('cypress/support/jsonBuilders/mocks/jsons/flags.cypress.json').then(() => {
      cy.server()
        .route({
          method: 'GET',
          delay: 0,
          status: 200,
          url: Cypress.config('baseUrl') + "/flags**",
          response: {
            "FLAG_FLASH_REDUCED_RESPONSE_CHANGEMG": true,
            "FLAG_FLASH_CLOUD_REGION_AND_NF_ROLE_OPTIONAL_SEARCH": true,
            "FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST": true
          }
        }).as('initFlags');
    });

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

  function openNewVnfChangeModal() {
    cy.visit('/serviceModels.htm#/change-management');
    cy.getElementByDataTestsId('create-new-change-management').click();
    cy.getElementByDataTestsId('newChangeManagementForm');
  }

  function fillSubscriberAndServiceType() {
    cy.selectDropdownOptionByText('subscriberName', 'SILVIA ROBBINS');
    cy.selectDropdownOptionByText('serviceType', 'TYLER SILVIA');
  }

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
