declare namespace Cypress {
  interface Chainable {
    selectDropdownOptionByText : typeof selectDropdownOptionByText;
    checkIsOptionSelected : typeof checkIsOptionSelected;
    validateSelectOptions: typeof validateSelectOptions;
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


/************************************************
 validate the options of selected drop down
 ************************************************/
function validateSelectOptions(rolesCriteria: string, values: string[]){
  values.forEach((value)=>{
    selectDropdownOptionByText(rolesCriteria, value);
    checkIsOptionSelected(rolesCriteria, value);
  });
}

Cypress.Commands.add('selectDropdownOptionByText', selectDropdownOptionByText);
Cypress.Commands.add('checkIsOptionSelected', checkIsOptionSelected);
Cypress.Commands.add('validateSelectOptions', validateSelectOptions);

