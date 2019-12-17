declare namespace Cypress {
  interface Chainable {
    updateServiceShouldNotOverrideChild: typeof updateServiceShouldNotOverrideChild
    openServiceContextMenu: typeof openServiceContextMenu
  }
}

function updateServiceShouldNotOverrideChild() : void  {
    cy.getElementByDataTestsId('drawing-board-tree').find('.node-content-wrapper').then((elements)=>{
      let numberOfExistingElements = elements.length;
        cy.openServiceContextMenu().then(() =>{
          cy.getElementByDataTestsId('context-menu-header-edit-item').click({force : true}).then(()=>{
            cy.genericFormSubmitForm().then(()=>{
              cy.getElementByDataTestsId('drawing-board-tree').find('.node-content-wrapper').then((afterUpdateServiceElements)=>{
                chai.expect(numberOfExistingElements).equal(afterUpdateServiceElements.length);
              });
            });
          });
        });
    });

}


function openServiceContextMenu() :  Chainable<any> {
 return cy.getElementByDataTestsId('openMenuBtn').click({force: true});
}

Cypress.Commands.add('updateServiceShouldNotOverrideChild', updateServiceShouldNotOverrideChild);
Cypress.Commands.add('openServiceContextMenu', openServiceContextMenu);
