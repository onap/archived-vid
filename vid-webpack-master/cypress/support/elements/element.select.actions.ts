declare namespace Cypress {
  interface Chainable {
    selectDropdownOptionByText : typeof selectDropdownOptionByText;
    checkIsOptionSelected : typeof checkIsOptionSelected;
  }
}

/************************************************
 select option with some text with select tag id
 ************************************************/
function selectDropdownOptionByText(selectId : string, optionText : string) : void {
  cy.getElementByDataTestsId(selectId)
    .select(optionText);
}

/************************************************
 check if  option is selected
 ************************************************/
function checkIsOptionSelected(selectId : string, optionText : string) : void {
  cy.getElementByDataTestsId(selectId)
    .should('have.value', optionText)
}

Cypress.Commands.add('selectDropdownOptionByText', selectDropdownOptionByText);
Cypress.Commands.add('checkIsOptionSelected', checkIsOptionSelected);

