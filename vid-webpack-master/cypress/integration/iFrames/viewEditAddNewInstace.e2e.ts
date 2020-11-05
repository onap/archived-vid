///<reference path="../../../node_modules/cypress/types/index.d.ts"/> / <reference types="Cypress" />


import {JsonBuilder} from "../../support/jsonBuilders/jsonBuilder";
import {ServiceModel} from "../../support/jsonBuilders/models/service.model";
import {AaiServiceInstancesModel} from "../../support/jsonBuilders/models/serviceInstances.model";
import {AAISubViewEditModel} from "../../support/jsonBuilders/models/aaiSubViewEdit.model";

const jsonBuilderAndMock: JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();
let jsonBuilderAAIService: JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();
let jsonBuilderAaiServiceInstances: JsonBuilder<AaiServiceInstancesModel> = new JsonBuilder<AaiServiceInstancesModel>();
let jsonBuilderAAISubViewEditModel: JsonBuilder<AAISubViewEditModel> = new JsonBuilder<AAISubViewEditModel>();


describe('View Edit page: Add a second instance', () =>{
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

  it(`Add new VNF to service with one existing VNF `, () =>{
    const serviceType = 'Mobility';
    const subscriberId = 'a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb';
    const serviceModelId = '82255513-e19f-46e5-bdfb-957c6bf57b82';
    const serviceInstanceId = 'e6cc1c4f-05f7-49bc-8e86-ac2eb92baaaa';

    cy.initDrawingBoardUserPermission();
    cy.route(`**/rest/models/services/${serviceModelId}`,
      'fixture:../support/jsonBuilders/mocks/jsons/add_vnf/add_vnf_model.json')
    .as('serviceModelAddVnf');

    cy.route(`**/aai_get_service_instance_topology/${subscriberId}/${serviceType}/${serviceInstanceId}`,
      'fixture:../support/jsonBuilders/mocks/jsons/add_vnf/add_vnf_instance.json')
    .as('serviceInstanceAddVnf');

    cy.openIframe(`app/ui/#/servicePlanning/EDIT?serviceModelId=${serviceModelId}&subscriberId=${subscriberId}&serviceType=${serviceType}&serviceInstanceId=${serviceInstanceId}`);

    cy.getElementByDataTestsId('node-iperf_vnf_2002_by5924 0-add-btn').click({force: true}).then(() => {

      cy.fillVnfPopup();
    });

    mockAsyncBulkResponse();
    // click update
    cy.getElementByDataTestsId('deployBtn').should('have.text', 'UPDATE').click();

    cy.getReduxState().then((state) => {

      const vnfs = state.service.serviceInstance['82255513-e19f-46e5-bdfb-957c6bf57b82'].vnfs;
      cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
        cy.readFile('../vid-app-common/src/test/resources/payload_jsons/vnf/one_vnf_exists_add_another_vnf_expected_bulk.json').then((expectedResult) => {

          //set randomized trackById into bulk expected file
          expectedResult.vnfs['iperf_vnf_2002_by5924 0'].trackById = vnfs['iperf_vnf_2002_by5924 0'].trackById;

          cy.deepCompare(xhr.request.body, expectedResult);
        });
      });
    });

  });

  it(`Open Audit info modal for the service instance `, () =>{
    const serviceType = 'Mobility';
    const subscriberId = 'a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb';
    const serviceModelId = '82255513-e19f-46e5-bdfb-957c6bf57b82';
    const serviceInstanceId = 'e6cc1c4f-05f7-49bc-8e86-ac2eb92baaaa';

    cy.initDrawingBoardUserPermission();
    cy.route(`**/rest/models/services/${serviceModelId}`,
      'fixture:../support/jsonBuilders/mocks/jsons/add_vnf/add_vnf_model.json')
    .as('serviceModelAddVnf');

    cy.route(`**/aai_get_service_instance_topology/${subscriberId}/${serviceType}/${serviceInstanceId}`,
      'fixture:../support/jsonBuilders/mocks/jsons/add_vnf/add_vnf_instance.json')
    .as('serviceInstanceAddVnf');

    cy.openIframe(`app/ui/#/servicePlanning/EDIT?serviceModelId=${serviceModelId}&subscriberId=${subscriberId}&serviceType=${serviceType}&serviceInstanceId=${serviceInstanceId}`);
    cy.readFile('../vid-automation/src/test/resources/a-la-carte/auditInfoMSOALaCarteNew.json').then((res) => {
    cy.initAuditInfoMSOALaCarteNew(res);
    cy.getElementByDataTestsId('openMenuBtn').click({force:true}).then(() => {
      cy.getElementByDataTestsId('context-menu-header-audit-item').click({force: true}).then(() => {

        cy.setViewportToSmallPopup();
        })
    })

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
