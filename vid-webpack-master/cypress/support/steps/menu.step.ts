declare namespace Cypress {
  interface Chainable {
    assertMenuItemsForNode: typeof assertMenuItemsForNode
  }
}
 function assertMenuItemsForNode(enabledActions: string[], nodeName: string, index: number = 0) : Chainable<any> {
  let node = cy.getElementByDataTestsId(nodeName).eq(+index);
  node.trigger('mouseover').click().then(()=> {

    //waiting to the menu to appear to catch options that shall not exist but actually exist
    cy.get('.ngx-contextmenu').should('exist').then(()=> {
      for (let option of ['duplicate', 'showAuditInfo', 'addGroupMember', 'delete', 'undoDelete', 'remove', ...enabledActions]) {
        cy.getElementByDataTestsId(`context-menu-${option}`).should(enabledActions.some(s => s === option) ? 'exist' : 'not.exist');
    }});
  });
  return node;
}

Cypress.Commands.add('assertMenuItemsForNode', assertMenuItemsForNode);
