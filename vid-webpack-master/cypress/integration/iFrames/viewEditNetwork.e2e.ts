///<reference path="../../../node_modules/cypress/types/index.d.ts"/> / <reference types="Cypress" />


import {JsonBuilder} from "../../support/jsonBuilders/jsonBuilder";
import {ServiceModel} from "../../support/jsonBuilders/models/service.model";
import {AaiServiceInstancesModel} from "../../support/jsonBuilders/models/serviceInstances.model";
import {AAISubViewEditModel} from "../../support/jsonBuilders/models/aaiSubViewEdit.model";

const jsonBuilderAndMock: JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();
let jsonBuilderAAIService: JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();
let jsonBuilderAaiServiceInstances: JsonBuilder<AaiServiceInstancesModel> = new JsonBuilder<AaiServiceInstancesModel>();
let jsonBuilderAAISubViewEditModel: JsonBuilder<AAISubViewEditModel> = new JsonBuilder<AAISubViewEditModel>();


describe('View Edit page: Add network', () =>{
  beforeEach(() => {
    cy.clearSessionStorage();
    cy.initGetAAISubDetails();
    cy.initAAIServices();
    cy.initTenants();
    cy.setTestApiParamToGR();
    cy.initVidMock();
    cy.login();

  });

  afterEach(() => {
    cy.screenshot();
  });

  it(`Add new network to service with one existing network `, () =>{
    const serviceType = 'Emanuel';
    const subscriberId = 'a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb';
    const serviceModelId = 'f93e72e1-77fc-4f54-b207-298d766d0886';
    const serviceInstanceId = 'ce2821fc-3b28-4759-9613-1e514d7563c0';
    const serviceInvariantUuid = "8c364754-4c76-4abc-b8f3-88da5f67d588";

    cy.initDrawingBoardUserPermission();
    cy.route(`**/rest/models/services/${serviceModelId}`,
      'fixture:../support/jsonBuilders/mocks/jsons/add_Network/add_network_model.json')
    .as('serviceModelAddNetwork');

    cy.route(`**/aai_get_service_instance_topology/${subscriberId}/${serviceType}/${serviceInstanceId}`,
      'fixture:../support/jsonBuilders/mocks/jsons/add_Network/add_network_instance.json')
    .as('serviceInstanceAddNetwork');

    cy.openIframe(`app/ui/#/servicePlanning/EDIT?serviceModelId=${serviceModelId}&subscriberId=${subscriberId}&serviceType=${serviceType}&serviceInstanceId=${serviceInstanceId}`);

    cy.getElementByDataTestsId('node-OVS Provider-add-btn').click({force: true}).then(() => {

      cy.fillNetworkPopup();
    });

    mockAsyncBulkResponse();
    //click update
    cy.getElementByDataTestsId('deployBtn').should('have.text', 'UPDATE').click();

    cy.getReduxState().then((state) => {

      const networks = state.service.serviceInstance['f93e72e1-77fc-4f54-b207-298d766d0886'].networks;
      cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
        cy.readFile('../vid-app-common/src/test/resources/payload_jsons/Network/one_network_exists_add_another_network_expected_bulk.json').then((expectedResult) => {

          //set randomized trackById into bulk expected file
          expectedResult.networks['OVS Provider'].trackById = networks['OVS Provider'].trackById;

          cy.deepCompare(xhr.request.body, expectedResult);
        });
      });
    });

  });

  function mockAsyncBulkResponse() {
    cy.server().route({
      url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
      method: 'POST',
      status: 200,
      response: "[]",
    }).as("expectedPostAsyncInstantiation");
  }

});
