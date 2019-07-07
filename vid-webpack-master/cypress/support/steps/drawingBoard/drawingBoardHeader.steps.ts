declare namespace Cypress {
  interface Chainable {
    serviceActionDelete: typeof serviceActionDelete
    serviceActionUndoDelete: typeof serviceActionUndoDelete
    serviceActionResume: typeof serviceActionResume
    serviceActionUndoResume: typeof serviceActionResume
  }
}

function serviceActionDelete() : void  {
  cy.getElementByDataTestsId('openMenuBtn').click();
  cy.getElementByDataTestsId('context-menu-header-delete-item').click();
  cy.getElementByDataTestsId('openMenuBtn').click();
  cy.getElementByDataTestsId('context-menu-header-delete-item').should("have.text", "Undo delete");
}

function serviceActionUndoDelete() : void  {
  cy.getElementByDataTestsId('openMenuBtn').click();
  cy.getElementByDataTestsId('context-menu-header-delete-item').click();
  cy.getElementByDataTestsId('openMenuBtn').click();
  cy.getElementByDataTestsId('context-menu-header-delete-item').should("have.text", "Delete");
}

function serviceActionResume() : void {
  cy.getElementByDataTestsId('openMenuBtn').click();
  cy.getElementByDataTestsId('context-menu-header-resume-item').click();
}


Cypress.Commands.add('serviceActionUndoDelete', serviceActionUndoDelete);
Cypress.Commands.add('serviceActionDelete', serviceActionDelete);
Cypress.Commands.add('serviceActionResume', serviceActionResume);
Cypress.Commands.add('serviceActionUndoResume', serviceActionResume);
