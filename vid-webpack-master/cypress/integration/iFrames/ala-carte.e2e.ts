///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
/// <reference types="Cypress" />
import { JsonBuilder } from '../../support/jsonBuilders/jsonBuilder';
import { ServiceModel } from '../../support/jsonBuilders/models/service.model';

describe('A la carte', function () {
  describe('check service name', () => {
    var jsonBuilderAAIService : JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();


    beforeEach(() => {
      cy.window().then((win) => {
        win.sessionStorage.clear();
        cy.setReduxState();
        cy.preventErrorsOnLoading();
        cy.initAAIMock();
        cy.initVidMock();
        cy.initAlaCarteService();
        cy.initZones();
        cy.login();
      });
    });

    const SERVICE_ID: string = '4d71990b-d8ad-4510-ac61-496288d9078e';
    const INSTANCE_NAME_MANDATORY_MESSAGE: string = 'Missing data ("Instance Name" and 3 other fields';
    const INSTANCE_NAME_NOT_MANDATORY_MESSAGE: string = 'Missing data ("Subscriber Name" and 2 other fields)';
    const CONFIRM_BUTTON : string = 'confirmButton';


    // function changeServiceName(obj : AAIServiceModel){
    //   obj.service.version = "NEW VALUE";
    //   return obj;
    // }
    it(`service name should be mandatory : serviceEcompNaming = false`, function () {
      cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/basicService.json').then((res) => {
        jsonBuilderAAIService.basicJson(res,
          Cypress.config('baseUrl') + '/rest/models/services/4d71990b-d8ad-4510-ac61-496288d9078e',
          200,
          0,
          SERVICE_ID + ' - service',
          changeServiceEcompNamingToFalse);

          checkServiceNameInputIdMandatory();
      });
    });

    it(`service name should be mandatory : serviceEcompNaming = true`, function () {
      cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/basicService.json').then((res) => {
        jsonBuilderAAIService.basicJson(res,
          Cypress.config('baseUrl') + '/rest/models/services/4d71990b-d8ad-4510-ac61-496288d9078e',
          200,
          0,
          SERVICE_ID + ' - service',
          changeServiceEcompNamingToTrue);
          checkServiceNameInputIdMandatory();
      });
    });

    function changeServiceEcompNamingToTrue(obj : ServiceModel){
      obj.service.serviceEcompNaming = "true";
      return obj;
    }

    function changeServiceEcompNamingToFalse(obj : ServiceModel){
      obj.service.serviceEcompNaming = "false";
      return obj;
    }

    function checkServiceNameInputIdMandatory(){
      cy.get('span').contains('Browse ASDC Service Models').click({force: true})
        .getElementByDataTestsId('deploy-' + SERVICE_ID).click({force: true})
        .wait(1000).getElementByDataTestsId(CONFIRM_BUTTON).click({force: true})
        .get('.error').contains(INSTANCE_NAME_MANDATORY_MESSAGE)
        .typeToInput('instanceName', 'testService');

      cy.getElementByDataTestsId(CONFIRM_BUTTON).click({force: true})
        .get('.error').contains(INSTANCE_NAME_NOT_MANDATORY_MESSAGE);
    }
  });
});
