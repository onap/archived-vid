///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
/// <reference types="Cypress" />

import { JsonBuilder } from '../../support/jsonBuilders/jsonBuilder';
import { AsyncInstantiationModel } from '../../support/jsonBuilders/models/asyncInstantiation.model';

describe('Instantiation status page', function () {
  var jsonBuilderInstantiationBuilder : JsonBuilder<AsyncInstantiationModel> = new JsonBuilder<AsyncInstantiationModel>();
  beforeEach(() => {
    cy.window().then((win) => {
      win.sessionStorage.clear();
      cy.setReduxState();
      cy.preventErrorsOnLoading();
      cy.initAAIMock();
      cy.initVidMock();
      jsonBuilderInstantiationBuilder.basicMock('/cypress/support/jsonBuilders/mocks/jsons/asyncInstantiation.json',
        Cypress.config('baseUrl') + "/asyncInstantiation**");
      cy.login();
    })
  });

  it('should disaplay the correct icons per status', function () {
      cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/asyncInstantiation.json').then((res) => {
        cy.openIframe('app/ui/#/instantiationStatus');
        for(let i = 0 ; i < res.length; i++){
          if(res[i].project){
            cy.getTableRowByIndex('instantiation-status', i).get('td#project span').contains(res[i].project);
          }
          if(res[i].userId){
            cy.getTableRowByIndex('instantiation-status', i).get('td#userId span').contains(res[i].userId);
          }
          if(res[i].tenantName){
            cy.getTableRowByIndex('instantiation-status', i).get('td#tenantName span').contains(res[i].tenantName);
          }
          if(res[i].serviceModelName){
            cy.getTableRowByIndex('instantiation-status', i).get('td#serviceModelName span').contains(res[i].serviceModelName);
          }
          if(res[i].serviceInstanceName){
            cy.getTableRowByIndex('instantiation-status', i).get('td#serviceInstanceName span').contains(res[i].serviceInstanceName);
          }
          if(res[i].serviceModelVersion){
            cy.getTableRowByIndex('instantiation-status', i).get('td#serviceModelVersion span').contains(res[i].serviceModelVersion);
          }
          if(res[i].subscriberName){
            cy.getTableRowByIndex('instantiation-status', i).get('td#subscriberName span').contains(res[i].subscriberName);
          }
        }
      });
  });
});
