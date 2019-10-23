///<reference path="../../../node_modules/cypress/types/index.d.ts"/> / <reference types="Cypress" />

import {JsonBuilder} from "../../support/jsonBuilders/jsonBuilder";
import {ServiceModel} from "../../support/jsonBuilders/models/service.model";

describe('Welcome page', function () {
  var jsonBuilderAndMock : JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();

  //describe('Contact us', () => {

    beforeEach(() => {
      cy.login();
    });

  afterEach(() => {
    cy.screenshot();
  });

    it(`verifying Contact Us link"`, function () {
    cy.visit('/welcome.htm');
    cy.get('A[href="mailto:VID-Tier4@list.att.com"]').contains('Contact Us');
      });

  it(`verifying VID version"`, function () {

    const APP_VERSION = "1902.1948";
    cy.readFile('cypress/support/jsonBuilders/mocks/jsons/version.json').then((res) => {
      jsonBuilderAndMock.basicJson(res,
        Cypress.config('baseUrl') + '/version',
        200,
        0,
        'app_version');
    });
    cy.visit('/welcome.htm');
    cy.getElementByDataTestsId('app-version').should("text", APP_VERSION);
  });
  //  });
  });


