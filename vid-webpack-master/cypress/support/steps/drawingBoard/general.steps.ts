declare namespace Cypress {
  interface Chainable {
    updateServiceShouldNotOverrideChild: typeof updateServiceShouldNotOverrideChild
    openServiceContextMenu: typeof openServiceContextMenu,
    drawingBoardTreeClickOnContextMenuOptionByName : typeof  drawingBoardTreeClickOnContextMenuOptionByName,
    nodeAction: typeof nodeAction,
    editNode : typeof editNode
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

function nodeAction(dataTestId: string, action: string, index ?: number) {
  return cy.drawingBoardTreeOpenContextMenuByElementDataTestId(dataTestId, index)
    .drawingBoardTreeClickOnContextMenuOptionByName(action)
}

function drawingBoardTreeClickOnContextMenuOptionByName(optionName : string) : Chainable<any>  {
  switch (optionName) {
    case 'Duplicate':
      return cy.getElementByDataTestsId('context-menu-duplicate').click({force : true});
    case 'Remove':
      return cy.getElementByDataTestsId('context-menu-remove').click({force : true});
    case 'Edit':
      return cy.getElementByDataTestsId('context-menu-edit').click({force : true});
    case 'Delete':
      return cy.getElementByDataTestsId('context-menu-delete').trigger('mouseover').click();
    case 'Upgrade':
      return cy.getElementByDataTestsId('context-menu-upgrade').trigger('mouseover').click();
    case 'Undo Upgrade':
      return cy.getElementByDataTestsId('context-menu-undoUpgrade').trigger('mouseover').click();
    default:
      return cy.getElementByDataTestsId('context-menu-duplicate').click({force : true});
  }
}

function editNode(dataTestId: string, index ?: number) {
  return cy.nodeAction(dataTestId, 'Edit', index);
}

Cypress.Commands.add('updateServiceShouldNotOverrideChild', updateServiceShouldNotOverrideChild);
Cypress.Commands.add('openServiceContextMenu', openServiceContextMenu);
Cypress.Commands.add('drawingBoardTreeClickOnContextMenuOptionByName', drawingBoardTreeClickOnContextMenuOptionByName);
Cypress.Commands.add('nodeAction', nodeAction);
Cypress.Commands.add('editNode', editNode);
