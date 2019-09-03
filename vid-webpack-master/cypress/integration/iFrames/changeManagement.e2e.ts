///<reference path="../../../node_modules/cypress/types/index.d.ts"/> / <reference types="Cypress" />

import {JsonBuilder} from "../../support/jsonBuilders/jsonBuilder";
import {AAISubDetailsModel} from "../../support/jsonBuilders/models/aaiSubDetails.model";

describe('Change management AKA VNF changes', function () {
  var jsonBuilderAAISubDetailsModel: JsonBuilder<AAISubDetailsModel> = new JsonBuilder<AAISubDetailsModel>();

  //describe('Contact us', () => {

  beforeEach(() => {
    cy.login();
    cy.initAAIMock();

    cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/defect710619/aaiSubDetailsE2E.json').then((res) => {
      jsonBuilderAAISubDetailsModel.basicJson(
        res,
        Cypress.config('baseUrl') + "/aai_sub_details/e433710f-9217-458d-a79d-1c7aff376d89**",
        200,
        0,
        "aai-sub-details")
    });

  });

  afterEach(() => {
    cy.screenshot();
  });

  it(`verifying + VNF changes`, function () {
    cy.visit('/serviceModels.htm#/change-management');
    cy.getElementByDataTestsId('create-new-change-management').click();
    cy.getElementByDataTestsId('newChangeManagementForm');
    cy.selectDropdownOptionByText('subscriberName', 'SILVIA ROBBINS');
    cy.selectDropdownOptionByText('serviceType', 'TYLER SILVIA');
  });
});


