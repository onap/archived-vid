///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
/// <reference types="Cypress" />
import {JsonBuilder} from '../../support/jsonBuilders/jsonBuilder';
import {AsyncInstantiationModel} from '../../support/jsonBuilders/models/asyncInstantiation.model';

describe('Spinner', function () {
  describe('spinner', () => {
    var jsonBuilderInstantiationBuilder : JsonBuilder<AsyncInstantiationModel> = new JsonBuilder<AsyncInstantiationModel>();
    beforeEach(() => {
        cy.clearSessionStorage();
        cy.setReduxState();
        cy.preventErrorsOnLoading();
        cy.initAAIMock();
        cy.initVidMock();
        cy.login();
    });

    afterEach(() => {
      cy.screenshot();
    });

    it('spinner should display after api call', function () {
      const timeBomb:Date = new Date(2018,6,10,0,0,0); //month 6 is July
      if (new Date(Date.now()) > timeBomb) {

        cy.readFile('cypress/support/jsonBuilders/mocks/jsons/asyncInstantiation.json').then((res) => {

          jsonBuilderInstantiationBuilder.basicJson(res,
            Cypress.config('baseUrl') + "/asyncInstantiation**",
            200,
            4000,
            "error 500 asyncInstantiation");
          cy.openIframe('app/ui/#/instantiationStatus');

          cy.get('.custom-loader')
            .and('be.visible');

        });
      }
    });
  });
});
