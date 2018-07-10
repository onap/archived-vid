///<reference path="../../../node_modules/cypress/types/index.d.ts"/> / <reference types="Cypress" />

describe('Tenant isolation - Test Environments Page', function () {
  describe('New Test Environment popup', () => {
    
    beforeEach(() => {
      cy.login();
    });

    it(`verifying proper text for the "Tenant Context" label; instead of "Select VSP"`, function () {
      
      cy.visit('/app/vid/scripts/modals/new-test-environment/new-test-environment.html');
      cy.get('label[for=tenantContext]').contains('Tenant Context');
      });
    });
  });


