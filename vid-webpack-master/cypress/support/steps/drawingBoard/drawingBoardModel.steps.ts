declare namespace Cypress {
  interface Chainable {
    drawingBoardPressAddButtonByElementName: typeof drawingBoardPressAddButtonByElementName,
    drawingBoardNumberOfExistingElementsShouldContains: typeof drawingBoardNumberOfExistingElementsShouldContains
  }
}

function drawingBoardPressAddButtonByElementName(elementName : string) : Chainable<any>  {
  return cy.getElementByDataTestsId(elementName + '-add-btn');
}

function drawingBoardNumberOfExistingElementsShouldContains(expectedElements : number) : void   {
  cy.getElementByDataTestsId('numberButton').contains(expectedElements);
}

Cypress.Commands.add('drawingBoardPressAddButtonByElementName', drawingBoardPressAddButtonByElementName);
Cypress.Commands.add('drawingBoardNumberOfExistingElementsShouldContains', drawingBoardNumberOfExistingElementsShouldContains);
