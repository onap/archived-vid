///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
/// <reference types="Cypress" />

import {JsonBuilder} from '../../support/jsonBuilders/jsonBuilder';
import {AsyncInstantiationModel} from '../../support/jsonBuilders/models/asyncInstantiation.model';

describe('Error message popup', function () {
  describe('show error on status 500', () => {
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

    it('error should display on api error', function () {
      // adding call with delay of 2000 sec
      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/asyncInstantiation.json').then((res) => {
        jsonBuilderInstantiationBuilder.basicJson(res, Cypress.config('baseUrl') + "/asyncInstantiation**", 500,0, "error 500 asyncInstantiation");

        cy.openIframe('app/ui/#/instantiationStatus');
        cy.get('div.title')
          .contains('Server not available');

      });
    });
  });
});
