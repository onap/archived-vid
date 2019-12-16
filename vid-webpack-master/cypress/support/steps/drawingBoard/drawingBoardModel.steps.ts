declare namespace Cypress {
  interface Chainable {
    drawingBoardPressAddButtonByElementName: typeof drawingBoardPressAddButtonByElementName,
    drawingBoardNumberOfExistingElementsShouldContains: typeof drawingBoardNumberOfExistingElementsShouldContains
    getDrawingBoardDeployBtn: typeof getDrawingBoardDeployBtn
  }
}

function drawingBoardPressAddButtonByElementName(elementName : string) : Chainable<any>  {
  return cy.getElementByDataTestsId(elementName + '-add-btn');
}

function drawingBoardNumberOfExistingElementsShouldContains(expectedElements : number) : void   {
  cy.getElementByDataTestsId('numberButton').contains(expectedElements);
}

function getDrawingBoardDeployBtn() : Chainable<any> {
  return cy.getElementByDataTestsId('deployBtn');
}


Cypress.Commands.add('drawingBoardPressAddButtonByElementName', drawingBoardPressAddButtonByElementName);
Cypress.Commands.add('drawingBoardNumberOfExistingElementsShouldContains', drawingBoardNumberOfExistingElementsShouldContains);
Cypress.Commands.add('getDrawingBoardDeployBtn', getDrawingBoardDeployBtn);
