///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
/// <reference types="Cypress" />
import {JsonBuilder} from '../../support/jsonBuilders/jsonBuilder';
import {ServiceModel} from '../../support/jsonBuilders/models/service.model';

describe('search existing instance', function () {
  var jsonBuilderAndMock: JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();

  beforeEach(() => {
    cy.window().then((win) => {
      win.sessionStorage.clear();
      cy.preventErrorsOnLoading();
      cy.initAAIMock();
      cy.initVidMock();
      cy.initZones();
      cy.permissionVidMock();
      cy.login();
    });
  });

  afterEach(() => {
    cy.screenshot();
  });

  it('when find instance with resource group in the model then open new service planning in edit mode', function () {
    const SUBSCRIBER_ID: string = "e433710f-9217-458d-a79d-1c7aff376d89";
    const SUBSCRIBER_NAME:string ="SILVIA ROBBINS";
    const SERVICE_TYPE: string = "VIRTUALUSP";
    const SERVICE_INSTANCE_ID: string = "3d930d51-eed8-41b8-956f-70aa0d19940b";
    const SERVICE_MODEL_VERSION_ID: string = '5aece664-e92d-4a99-8a76-c7d7b8d23d09';
    const SERVICE_MODEL_INVARIANT_ID: string = 'd089c740-53ac-41f6-8e5b-32e862302ef1';

    var searchServiceInstancesResponse = {
      "service-instances": [{
        "serviceInstanceId":SERVICE_INSTANCE_ID,
        "globalCustomerId": SUBSCRIBER_ID,
        "serviceType": SERVICE_TYPE,
        "serviceInstanceName": "abc_CHARLOTTE_2017_1011_IST_Service_CSI",
        "subscriberName": SUBSCRIBER_NAME,
        "aaiModelInvariantId": SERVICE_MODEL_INVARIANT_ID,
        "aaiModelVersionId": SERVICE_MODEL_VERSION_ID,
        "isPermitted": true
      }
      ]
    };

    cy.readFile('../vid-automation/src/test/resources/aaiGetInstanceTopology/getServiceInstanceTopologyResult.json').then((res) => {
      jsonBuilderAndMock.basicJson(
        res,
        Cypress.config('baseUrl') + "/aai_get_service_instance_topology/e433710f-9217-458d-a79d-1c7aff376d89/TYLER SILVIA/f8791436-8d55-4fde-b4d5-72dd2cf13cfb",
        200, 0,
        "initServiceInstanceTopology",
      )
    });

    cy.server()
      .route({
        method: 'GET',
        status: 200,
        delay : 0,
        url: Cypress.config('baseUrl') + `/search_service_instances?subscriberId=${SUBSCRIBER_ID}&serviceInstanceIdentifier=${SERVICE_INSTANCE_ID}`,
        response: searchServiceInstancesResponse
      }).as('search_service_instances');

    cy.readFile('cypress/support/jsonBuilders/mocks/jsons/serviceModels/serviceForNewViewEdit.json').then((res) => {
      jsonBuilderAndMock.basicJson(
        res,
        Cypress.config('baseUrl') + `/rest/models/services/${SERVICE_MODEL_VERSION_ID}`,
        200,
        0,
        "serviceForNewViewEdit",
        (res: ServiceModel)=>{
          res.service.uuid = SERVICE_MODEL_VERSION_ID;
          res.service.invariantUuid =SERVICE_MODEL_INVARIANT_ID;
          return res;
        }
      )
    });

    cy.visit(`/serviceModels.htm#/instances/subdetails?subscriberId=${SUBSCRIBER_ID}&serviceInstanceIdentifier=${SERVICE_INSTANCE_ID}`);
    cy.getElementByDataTestsId(`view/edit-test-data-id-${SERVICE_INSTANCE_ID}`).click();
    cy.url().should('contains',`servicePlanning/EDIT?serviceModelId=${SERVICE_MODEL_VERSION_ID}&subscriberId=${SUBSCRIBER_ID}`+
      `&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}`);
  });

});

