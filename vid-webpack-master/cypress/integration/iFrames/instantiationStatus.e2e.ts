///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
/// <reference types="Cypress" />

import {JsonBuilder} from '../../support/jsonBuilders/jsonBuilder';
import {AsyncInstantiationModel} from '../../support/jsonBuilders/models/asyncInstantiation.model';

describe('Instantiation status', function () {
  var jsonBuilderInstantiationBuilder : JsonBuilder<AsyncInstantiationModel> = new JsonBuilder<AsyncInstantiationModel>();
  var asyncRes: Array<any>;

  beforeEach(() => {
      cy.clearSessionStorage();
      cy.setReduxState();
      cy.preventErrorsOnLoading();
      cy.initAAIMock();
      cy.initVidMock();
      jsonBuilderInstantiationBuilder.basicMock('cypress/support/jsonBuilders/mocks/jsons/asyncInstantiation.json',
        Cypress.config('baseUrl') + "/asyncInstantiation**",
        (res: any) => {
            asyncRes = res;
            return res;
      });
      cy.login();
  });

  afterEach(() => {
    cy.screenshot();
  });

  it('should display the correct icons per status', function () {
      const serviceAction:any = {INSTANTIATE : 'Instantiate', DELETE: 'Delete', UPDATE: 'Update', UPGRADE: 'Upgrade'};
      cy.openIframe('app/ui/#/instantiationStatus');
      for(let i = 0 ; i < asyncRes.length; i++){
        if(asyncRes[i].project){
          cy.getTableRowByIndex('instantiation-status', i).get('td#project span').contains(asyncRes[i].project);
        }
        if(asyncRes[i].userId){
          cy.getTableRowByIndex('instantiation-status', i).get('td#userId span').contains(asyncRes[i].userId);
        }
        if(asyncRes[i].tenantName){
          cy.getTableRowByIndex('instantiation-status', i).get('td#tenantName span').contains(asyncRes[i].tenantName);
        }
        if(asyncRes[i].serviceModelName){
          cy.getTableRowByIndex('instantiation-status', i).get('td#serviceModelName span').contains(asyncRes[i].serviceModelName);
        }
        if(asyncRes[i].serviceInstanceName){
          cy.getTableRowByIndex('instantiation-status', i).get('td#serviceInstanceName span').contains(asyncRes[i].serviceInstanceName);
        }
        if(asyncRes[i].serviceModelVersion){
          cy.getTableRowByIndex('instantiation-status', i).get('td#serviceModelVersion span').contains(asyncRes[i].serviceModelVersion);
        }
        if(asyncRes[i].subscriberName){
          cy.getTableRowByIndex('instantiation-status', i).get('td#subscriberName span').contains(asyncRes[i].subscriberName);
        }
        if(asyncRes[i].action) {
          cy.getTableRowByIndex('instantiation-status', i).get('td#action span').contains(serviceAction[asyncRes[i].action]);
        }
    }
  });

  it.only('should filter rows by search text', function () {
    cy.openIframe('app/ui/#/instantiationStatus')
    cy.getElementByDataTestsId("instantiationStatusSearch").type("ComplexService");

    cy.get('table#instantiation-status tbody tr').should((elements) => {
      expect(elements).to.have.length(3);
    });
  });

  it('should enable correct menu items', function () {
        cy.openIframe('app/ui/#/instantiationStatus');

        // Instantiate action with Job status FAILED - isRetry = true
        cy.get( '#5c2cd8e5-27d0-42e3-85a1-85db5eaba459').find('.menu-div').click();
        cy.get('.dropdown-menu').find('.disabled').find(`[data-tests-id='context-menu-retry']`).should('not.exist');
        cy.get('.dropdown-menu').find('.disabled').find(`[data-tests-id='context-menu-remove']`).should('exist');
        cy.get('.dropdown-menu').find('.disabled').find(`[data-tests-id='context-menu-open']`).should('exist');
        cy.get('.dropdown-menu').find('.disabled').find(`[data-tests-id='context-menu-hide']`).should('not.exist');
        cy.get('.dropdown-menu').find('.disabled').find(`[data-tests-id='context-menu-audit-info']`).should('not.exist');

        // Instantiate action with Job status FAILED - isRetry = false
        cy.get( '#e1db03c3-6274-4ff7-84cf-7bd3a3946de7').find('.menu-div').click();
        cy.get('.dropdown-menu').find(`[data-tests-id='context-menu-retry']`).should('not.be.visible');
        cy.get('.dropdown-menu').find('.disabled').find(`[data-tests-id='context-menu-open']`).should('exist');

        //Delete action with Job status IN_PROGRESS
        cy.get( '#850dc7d2-5240-437f-9bcd-b1ed7dc339c2').find('.menu-div').click();
        cy.get('.dropdown-menu').find('.disabled').find(`[data-tests-id='context-menu-remove']`).should('exist');
        cy.get('.dropdown-menu').find('.disabled').find(`[data-tests-id='context-menu-open']`).should('exist');
        cy.get('.dropdown-menu').find('.disabled').find(`[data-tests-id='context-menu-hide']`).should('exist');
        cy.get('.dropdown-menu').find('.disabled').find(`[data-tests-id='context-menu-audit-info']`).should('not.exist');
        //Update action with Job status COMPLETED
        cy.get( '#850dc7d2-5240-437f-9bcd-b1ed7dc339c1').find('.menu-div').click();
        cy.get('.dropdown-menu').find('.disabled').find(`[data-tests-id='context-menu-remove']`).should('exist');
        cy.get('.dropdown-menu').find('.disabled').find(`[data-tests-id='context-menu-open']`).should('not.exist');
        cy.get('.dropdown-menu').find('.disabled').find(`[data-tests-id='context-menu-hide']`).should('not.exist');
        cy.get('.dropdown-menu').find('.disabled').find(`[data-tests-id='context-menu-audit-info']`).should('not.exist');
  });

});
