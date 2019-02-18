declare namespace Cypress {
  interface Chainable {
    drawingBoardTreeOpenContextMenuByElementDataTestId: typeof drawingBoardTreeOpenContextMenuByElementDataTestId,
    drawingBoardTreeClickOnContextMenuOptionByName: typeof drawingBoardTreeClickOnContextMenuOptionByName
  }
}

function drawingBoardTreeOpenContextMenuByElementDataTestId(dataTestId : string, index ?: number) : Chainable<any>  {
  return cy.getElementByDataTestsId(dataTestId + "-menu-btn").eq(index != null ? index : 0).click({force: true});
}

function drawingBoardTreeClickOnContextMenuOptionByName(optionName : string) : Chainable<any>  {
  switch (optionName) {
    case 'Duplicate':
      return cy.getElementByDataTestsId('context-menu-duplicate').click({force : true});
    case 'Remove':
      return cy.getElementByDataTestsId('context-menu-remove').click({force : true});
    case 'Edit':
      return cy.getElementByDataTestsId('context-menu-edit').click({force : true});
    default:
      return cy.getElementByDataTestsId('context-menu-duplicate').click({force : true});
  }
}


Cypress.Commands.add('drawingBoardTreeOpenContextMenuByElementDataTestId', drawingBoardTreeOpenContextMenuByElementDataTestId);
Cypress.Commands.add('drawingBoardTreeClickOnContextMenuOptionByName', drawingBoardTreeClickOnContextMenuOptionByName);
