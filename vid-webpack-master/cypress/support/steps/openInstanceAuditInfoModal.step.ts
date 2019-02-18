declare namespace Cypress {
  interface Chainable {
    openInstanceAuditInfoModal: typeof openInstanceAuditInfoModal,
  }
}

function openInstanceAuditInfoModal(iconTextId: string): void {
  cy.getElementByDataTestsId(iconTextId).click({force: true})
     .getElementByDataTestsId('context-menu-showAuditInfo').click({force : true})
     .getElementByDataTestsId('audit-info-title').contains('Instantiation Information');
     cy.get('.table-title').contains('MSO status');
     cy.get('#cancelButton').click({force: true})

}


Cypress.Commands.add('openInstanceAuditInfoModal', openInstanceAuditInfoModal);

