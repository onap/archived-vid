///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
/// <reference types="Cypress" />
import { JsonBuilder } from '../../support/jsonBuilders/jsonBuilder';
import { AsyncInstantiationModel } from '../../support/jsonBuilders/models/asyncInstantiation.model';

describe('Spinner', function () {
  describe('spinner', () => {
    var jsonBuilderInstantiationBuilder : JsonBuilder<AsyncInstantiationModel> = new JsonBuilder<AsyncInstantiationModel>();
    beforeEach(() => {
      cy.window().then((win) => {
        win.sessionStorage.clear();
        cy.setReduxState();
        cy.preventErrorsOnLoading();
        cy.initAAIMock();
        cy.initVidMock();
        cy.login();
      })
    });

    it('spinner should display after api call', function () {
      cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/asyncInstantiation.json').then((res) => {

        jsonBuilderInstantiationBuilder.basicJson(res,
          Cypress.config('baseUrl') + "/asyncInstantiation**",
          200,
          2000,
          "error 500 asyncInstantiation");
        cy.openIframe('app/ui/#/instantiationStatus');

        cy.get('.spinner')
          .and('be.visible');

      });
    });
  });
});
