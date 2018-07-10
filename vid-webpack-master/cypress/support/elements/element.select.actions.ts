declare namespace Cypress {
  interface Chainable {
    selectDropdownOptionByText : typeof selectDropdownOptionByText;
  }
}

/************************************************
 select option with some text with select tag id
 ************************************************/
function selectDropdownOptionByText(selectId : string, optionText : string) : void {
  cy.getElementByDataTestsId(selectId)
    .select(optionText);
}

Cypress.Commands.add('selectDropdownOptionByText', selectDropdownOptionByText);

