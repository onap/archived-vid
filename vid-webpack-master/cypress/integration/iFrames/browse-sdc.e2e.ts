///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
/// <reference types="Cypress" />
import {JsonBuilder} from '../../support/jsonBuilders/jsonBuilder';
import {ServiceModel} from '../../support/jsonBuilders/models/service.model';

describe('Browse SDC', function () {
  const jsonBuilderAndMock: JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();


  beforeEach(() => {
    cy.clearSessionStorage();
    cy.preventErrorsOnLoading();
    cy.initAAIMock();
    cy.initVidMock();
    cy.initZones();
    cy.login();
    cy.visit("welcome.htm")
  });

  afterEach(() => {
    cy.screenshot();
  });

  it(`browse sdc open macro with network and then macro for new flow`, function () {
    // const MACRO_WITH_CONFIGURATION_ID: string = 'ee6d61be-4841-4f98-8f23-5de9da846ca7';
    const MACRO_WITH_NETWORK_ID: string = "bd8ffd14-da36-4f62-813c-6716ba9f4354";
    const MACRO_FOR_NEW_FLOW_ID: string = '74fa72dd-012b-49c3-800d-06b12bcaf1a0';
    const CANCEL_BUTTON = "cancelButton";

    cy.readFile('cypress/support/jsonBuilders/mocks/jsons/bug616888/list-services.json').then((res) => {
      jsonBuilderAndMock.basicJson(res,
        Cypress.config('baseUrl') + '/rest/models/services?distributionStatus=DISTRIBUTED',
        200,
        0,
        'list_services');
    });

    cy.readFile('cypress/support/jsonBuilders/mocks/jsons/bug616888/service-with-configuration.json').then((res) => {
      jsonBuilderAndMock.basicJson(res,
        Cypress.config('baseUrl') + '/rest/models/services/' + MACRO_WITH_NETWORK_ID,
        200,
        0,
        'MACRO_WITH_CONFIGURATION');
    });

    cy.readFile('cypress/support/jsonBuilders/mocks/jsons/bug616888/Dror_service1806_Macro1.json').then((res) => {
      jsonBuilderAndMock.basicJson(res,
        Cypress.config('baseUrl') + '/rest/models/services/' + MACRO_FOR_NEW_FLOW_ID,
        200,
        0,
        'MACRO_FOR_NEW_FLOW');
    });

    cy.get('span').contains('Browse SDC Service Models').click({force: true});
    cy.wait("@list_services").then(() => {
      cy.getElementByDataTestsId('deploy-' + MACRO_WITH_NETWORK_ID).click({force: true})
        .getElementByDataTestsId(CANCEL_BUTTON).click({force: true});
      cy.getElementByDataTestsId('deploy-' + MACRO_FOR_NEW_FLOW_ID).click({force: true});
      cy.get("iframe");
    });

    cy.visit("welcome.htm"); //relaod page to not break the following tests

  });

  it(`browse sdc should open instantiation template modal if service isInstantiationTemplateExists is true`, function () {
    const SERVICE_MODEL_ID: string = '74fa72dd-012b-49c3-800d-06b12bcaf1a0';

    cy.readFile('cypress/support/jsonBuilders/mocks/jsons/bug616888/list-services.json').then((res) => {
      res.services = res.services.map((service: { uuid: string, isInstantiationTemplateExists: boolean }) => {
        if (service.uuid === SERVICE_MODEL_ID) {
          service.isInstantiationTemplateExists = true;
        }
        return service;
      });
      jsonBuilderAndMock.basicJson(res,
        Cypress.config('baseUrl') + '/rest/models/services?distributionStatus=DISTRIBUTED',
        200,
        0,
        'list_services');
    });

    cy.readFile('cypress/support/jsonBuilders/mocks/jsons/bug616888/Dror_service1806_Macro1.json').then((res) => {
      jsonBuilderAndMock.basicJson(res,
        Cypress.config('baseUrl') + '/rest/models/services/' + SERVICE_MODEL_ID,
        200,
        0,
        'MACRO_FOR_NEW_FLOW');
    });


    cy.get('span').contains('Browse SDC Service Models').click({force: true});
    cy.wait("@list_services").then(() => {
      cy.getElementByDataTestsId('deploy-' + SERVICE_MODEL_ID).click({force: true});
      cy.get('iframe').then(function ($iframe) {
        expect($iframe.attr('src')).to.contain(`app/ui/#/instantiationTemplatesPopup?serviceModelId=${SERVICE_MODEL_ID}`);
      });
    });
  });

  it(`browse sdc open create new service instance flow`, function () {
    const MACRO_FOR_NEW_FLOW_ID: string = '745d1bf1-9ed1-413f-8111-f1e984ad63fb';

    cy.initGetAAISubDetails();

    cy.readFile('../vid-automation/src/main/resources/registration_to_simulator/create_new_instance/aai_get_models_by_service_type_SILVIA_ROBBINS.json').then((res) => {
      jsonBuilderAndMock.basicJson(res.simulatorResponse.body,
        Cypress.config('baseUrl') + '/aai_get_models_by_service_type/**',
        200,
        0,
        'aaiGetModelByServiceType');
    });

    cy.readFile('cypress/support/jsonBuilders/mocks/jsons/bug616888/Dror_service1806_Macro1.json').then((res) => {
      jsonBuilderAndMock.basicJson(res,
        Cypress.config('baseUrl') + '/rest/models/services/' + MACRO_FOR_NEW_FLOW_ID,
        200,
        0,
        'MACRO_FOR_NEW_FLOW');
    });

    cy.get('span').contains('Create New Service Instance').click({force: true})
      .selectDropdownOptionByText('subscriberName', 'SILVIA ROBBINS');
    cy.get('button').contains('Submit').click({force: true});
    cy.selectDropdownOptionByText('serviceType', 'TYLER SILVIA');
    cy.get('button').contains('Submit').click({force: true});
    cy.getElementByDataTestsId('deploy-' + MACRO_FOR_NEW_FLOW_ID).click({force: true});
    cy.wait("@aaiGetModelByServiceType").then(() => {
      cy.get('button').contains('Deploy').eq(0).click({force: true});
      cy.get('iframe').then(function ($iframe) {
        expect($iframe.attr('src')).to.contain(`app/ui/#/servicePopup?serviceModelId=74fa72dd-012b-49c3-800d-06b12bcaf1a0`);
      });
    });

    cy.visit("welcome.htm"); //relaod page to not break the following tests

  });


  it(`browse sdc of service without instantiationType open aLaCarte popup`, function () {
    const VERY_OLD_SERVICE_UUID: string = "09c476c7-91ae-44b8-a731-04d8d8fa3695";
    const TEST_MOCKS_PATH = "cypress/support/jsonBuilders/mocks/jsons/bug_aLaCarteServiceWrongPopup/";

    cy.readFile(TEST_MOCKS_PATH + 'list-services.json').then((res) => {
      jsonBuilderAndMock.basicJson(res,
        Cypress.config('baseUrl') + '/rest/models/services?distributionStatus=DISTRIBUTED',
        200,
        0,
        'list_services');
    });

    cy.readFile(TEST_MOCKS_PATH + 'serviceWithoutInstantiationType.json').then((res) => {
      jsonBuilderAndMock.basicJson(res,
        Cypress.config('baseUrl') + '/rest/models/services/' + VERY_OLD_SERVICE_UUID,
        200,
        0,
        'MACRO_WITH_CONFIGURATION');
    });

    cy.get('span').contains('Browse SDC Service Models').click({force: true});
    cy.wait("@list_services").then(() => {
      cy.getElementByDataTestsId('deploy-' + VERY_OLD_SERVICE_UUID).click({force: true})
        .getElementByDataTestsId('create-modal-title').contains("Create Service Instance -- a la carte");
    });

    cy.visit("welcome.htm"); //relaod page to not break the following tests

  });


});
