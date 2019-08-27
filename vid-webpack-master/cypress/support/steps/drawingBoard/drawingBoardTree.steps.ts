declare namespace Cypress {
  interface Chainable {
    drawingBoardTreeOpenContextMenuByElementDataTestId: typeof drawingBoardTreeOpenContextMenuByElementDataTestId,
    drawingBoardTreeClickOnContextMenuOptionByName: typeof drawingBoardTreeClickOnContextMenuOptionByName,
    nodeWithLineThrough: typeof nodeWithLineThrough,
    nodeWithoutLineThrough: typeof nodeWithoutLineThrough,
    IsDeleteTagShownOnNode: typeof IsDeleteTagShownOnNode,
    IsDeleteTagNotShownOnNode: typeof IsDeleteTagNotShownOnNode,
    isNodeDeleted: typeof isNodeDeleted,
    isNodeNotDeleted: typeof isNodeNotDeleted
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

function isNodeDeleted(index: number)
{
  cy.get('.tree-node').eq(1).find('[data-tests-id="node-name"]').eq(index).should('have.css', 'text-decoration').and('contain', 'line-through');
  cy.getElementByDataTestsId('delete-status-type').eq(index).should("contain.text", "Delete").should("contain.css", "opacity", "1");
}

function isNodeNotDeleted(index: number)
{
  cy.get('.tree-node').eq(1).find('[data-tests-id="node-name"]').eq(index).should('have.css', 'text-decoration').and('not.contain', 'line-through');
  cy.getElementByDataTestsId('delete-status-type').eq(index).should("contain.text", "Delete").should("contain.css", "opacity", "0");
}
function nodeWithLineThrough(index: number)
{
  cy.getElementByDataTestsId('node-name').eq(index).should('have.css', 'text-decoration').and('contain', 'line-through')
}

function nodeWithoutLineThrough(index: number)
{
  cy.getElementByDataTestsId('node-name').eq(index).should('have.css', 'text-decoration').and('not.contain', 'line-through')
}


function IsDeleteTagShownOnNode(index: number)
{
  cy.getElementByDataTestsId('delete-status-type').eq(index).should("contain.text", "Delete").should("contain.css", "opacity", "1");
}

function IsUpgradeTagShownOnNode(index: number)
{
  cy.getElementByDataTestsId('upgrade-status-type').eq(index).should("contain.text", "Upgrade").should("contain.css", "opacity", "1");
}

function IsDeleteTagNotShownOnNode(index: number)
{
  cy.getElementByDataTestsId('delete-status-type').eq(index).should("contain.text", "Delete").should("contain.css", "opacity", "0");
}

Cypress.Commands.add('drawingBoardTreeOpenContextMenuByElementDataTestId', drawingBoardTreeOpenContextMenuByElementDataTestId);
Cypress.Commands.add('drawingBoardTreeClickOnContextMenuOptionByName', drawingBoardTreeClickOnContextMenuOptionByName);
Cypress.Commands.add('nodeWithLineThrough', nodeWithLineThrough);
Cypress.Commands.add('nodeWithoutLineThrough', nodeWithoutLineThrough);
Cypress.Commands.add('IsDeleteTagShownOnNode', IsDeleteTagShownOnNode);
Cypress.Commands.add('IsDeleteTagNotShownOnNode', IsDeleteTagNotShownOnNode);
Cypress.Commands.add('isNodeDeleted', isNodeDeleted);
Cypress.Commands.add('isNodeNotDeleted', isNodeNotDeleted);
