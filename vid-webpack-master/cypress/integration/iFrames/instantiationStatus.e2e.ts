///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
/// <reference types="Cypress" />

import {JsonBuilder} from '../../support/jsonBuilders/jsonBuilder';
import {AsyncInstantiationModel} from '../../support/jsonBuilders/models/asyncInstantiation.model';
import {
  COMPLETED_WITH_ERRORS,
  INPROGRESS,
  PAUSE,
  PAUSE_UPON_COMPLETION,
  PENDING,
  STOPPED,
  SUCCESS_CIRCLE,
  UNKNOWN,
  X_O
} from "../../../src/app/instantiationStatus/instantiationStatus.component.service";

describe('Instantiation status', function () {
  var jsonBuilderInstantiationBuilder : JsonBuilder<AsyncInstantiationModel> = new JsonBuilder<AsyncInstantiationModel>();
  var asyncRes: Array<any>;
  const contextMenuCreateAnotherOne = 'context-menu-create-another-one';
  const contextMenuNewViewEdit = 'context-menu-new-view-edit';

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
        cy.getTableRowByIndex('instantiation-status', i).get(`td custom-icon#jobStatusIcon-${i} div`)
        .should('have.class', `__${getJobIconClass(asyncRes[i].jobStatus)}`);

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

  function getJobIconClass(status: string) : string{
    switch(`${status}`.toUpperCase()) {
      case  'PENDING' :
        return PENDING;
      case  'IN_PROGRESS' :
        return  INPROGRESS;
      case  'PAUSED' :
        return PAUSE;
      case  'FAILED' :
        return X_O;
      case  'COMPLETED' :
        return SUCCESS_CIRCLE;
      case  'STOPPED' :
        return STOPPED;
      case  'COMPLETED_WITH_ERRORS' :
        return COMPLETED_WITH_ERRORS;
      case  'COMPLETED_AND_PAUSED' :
        return PAUSE_UPON_COMPLETION;
      default:
        return UNKNOWN;
    }
  }

  it('should filter rows by filter text', function () {
    cy.openIframe('app/ui/#/instantiationStatus');
    cy.getElementByDataTestsId("instantiation-status-filter").type("ComplexService");
    cy.get('table#instantiation-status tbody tr').should('have.length', 2);
  });

  it('should filter rows by url filter text', function () {
    cy.openIframe('app/ui/#/instantiationStatus?filterText=ComplexService');
    cy.getElementByDataTestsId("instantiation-status-filter").should('have.value','ComplexService');
    cy.get('table#instantiation-status tbody tr').should('have.length', 2);
  });

  function getDisabledDropDownItemByDataTestId(testId:String) {
    return cy.get('.dropdown-menu').find('.disabled').find(`[data-tests-id='${testId}']`);
  }

  function clickOnTitleAndThenOnMenuWithJobId(jobId: string) {
    cy.getElementByDataTestsId("instantiation-status-title").click();
    cy.get('#' + jobId).find('.menu-div').click();
  }

  it.only('should disabled correct menu items', function () {

    cy.openIframe('app/ui/#/instantiationStatus');

    // Instantiate action with Job status FAILED - isRetry = true
    clickOnTitleAndThenOnMenuWithJobId('5c2cd8e5-27d0-42e3-85a1-85db5eaba459');
    getDisabledDropDownItemByDataTestId('context-menu-retry').should('not.exist');
    getDisabledDropDownItemByDataTestId('context-menu-remove').should('exist');
    getDisabledDropDownItemByDataTestId('context-menu-open').should('exist');
    getDisabledDropDownItemByDataTestId('context-menu-hide').should('not.exist');
    getDisabledDropDownItemByDataTestId('context-menu-audit-info').should('not.exist');
    getDisabledDropDownItemByDataTestId(contextMenuCreateAnotherOne).should('not.exist');

    // Instantiate action with Job status FAILED - isRetry = false
    clickOnTitleAndThenOnMenuWithJobId('e1db03c3-6274-4ff7-84cf-7bd3a3946de7');
    getDisabledDropDownItemByDataTestId('context-menu-retry').should('not.be.visible');
    getDisabledDropDownItemByDataTestId('context-menu-open').should('exist');
    getDisabledDropDownItemByDataTestId(contextMenuCreateAnotherOne).should('not.exist');

    //Delete action with Job status IN_PROGRESS
    clickOnTitleAndThenOnMenuWithJobId('850dc7d2-5240-437f-9bcd-b1ed7dc339c2');
    getDisabledDropDownItemByDataTestId('context-menu-remove').should('exist');
    getDisabledDropDownItemByDataTestId('context-menu-open').should('exist');
    getDisabledDropDownItemByDataTestId('context-menu-hide').should('exist');
    getDisabledDropDownItemByDataTestId('context-menu-audit-info').should('not.exist');
    getDisabledDropDownItemByDataTestId(contextMenuCreateAnotherOne).should('exist');

    //Update action with Job status COMPLETED
    clickOnTitleAndThenOnMenuWithJobId('850dc7d2-5240-437f-9bcd-b1ed7dc339c1');
    getDisabledDropDownItemByDataTestId('context-menu-remove').should('exist');
    getDisabledDropDownItemByDataTestId('context-menu-open').should('not.exist');
    getDisabledDropDownItemByDataTestId('context-menu-hide').should('not.exist');
    getDisabledDropDownItemByDataTestId('context-menu-audit-info').should('not.exist');
    getDisabledDropDownItemByDataTestId(contextMenuCreateAnotherOne).should('exist');

    //COMPLETED_AND_PAUSED
    clickOnTitleAndThenOnMenuWithJobId('850dc7d2-5240-437f-9bcd-b1ed7dc339d9');
    getDisabledDropDownItemByDataTestId('context-menu-retry').should('exist');
    getDisabledDropDownItemByDataTestId('context-menu-open').should('exist');
    getDisabledDropDownItemByDataTestId('context-menu-audit-info').should('exist');
    getDisabledDropDownItemByDataTestId(contextMenuCreateAnotherOne).should('exist');
  });

  it('clicking on create another one item, go to expected url', function () {
    //see cypress/support/jsonBuilders/mocks/jsons/asyncInstantiation.json id:8
    const jobId = '5c2cd8e5-27d0-42e3-85a1-85db5eaba459';
    const serviceModelId = 'e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0';
    const vidBaseUrl = `${Cypress.config().baseUrl}/serviceModels.htm`;

    cy.openIframe('app/ui/#/instantiationStatus');

    clickOnTitleAndThenOnMenuWithJobId(jobId);
    cy.get('.dropdown-menu').getElementByDataTestsId(contextMenuCreateAnotherOne).contains('Create another one');
    cy.get('.dropdown-menu').getElementByDataTestsId(contextMenuCreateAnotherOne).click();
    cy.location().should((loc) => {
      expect(loc.toString()).to.eq(`${vidBaseUrl}#/servicePlanning/RECREATE?serviceModelId=${serviceModelId}&jobId=${jobId}`);
    });
  });

  it('clicking on new view edit, go to expected url', function () {
    //see cypress/support/jsonBuilders/mocks/jsons/asyncInstantiation.json id:10
    const jobId = '850dc7d2-5240-437f-9bcd-b1ed7dc339c1';
    const serviceModelId = 'e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0';
    const vidBaseUrl = `${Cypress.config().baseUrl}/serviceModels.htm`;
    const serviceType = 'TYLER%20SILVIA';

    cy.openIframe('app/ui/#/instantiationStatus');
    clickOnTitleAndThenOnMenuWithJobId(jobId);
    cy.get('.dropdown-menu').getElementByDataTestsId(contextMenuNewViewEdit).contains('New View/Edit');
    cy.get('.dropdown-menu').getElementByDataTestsId(contextMenuNewViewEdit).click();
    cy.location().should((location) => {
      expect(location.toString()).to.eq(`${vidBaseUrl}#/servicePlanning/EDIT?serviceModelId=${serviceModelId}&serviceType=${serviceType}&jobId=${jobId}`);
    });
  });
});
