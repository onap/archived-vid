declare namespace Cypress {
  interface Chainable {
    genericFormSubmitForm: typeof genericFormSubmitForm
    selelctPlatformValue: typeof selelctPlatformValue
  }
}




function selelctPlatformValue(isDropdown: boolean, selectOption: string){
  if (isDropdown) {
    cy.selectDropdownOptionByText('platform', selectOption);
  } else {
    cy.getElementByDataTestsId("multi-selectPlatform").get('.c-btn').click({force: true})
      .getElementByDataTestsId(`multi-selectPlatform-${selectOption}`).click()
      .getElementByDataTestsId("multi-selectPlatform").get('.c-btn').click({force: true});

  }
}


function genericFormSubmitForm(): Chainable<any> {
  return cy.getElementByDataTestsId('form-set').click({force: true});
}


Cypress.Commands.add('genericFormSubmitForm', genericFormSubmitForm);
Cypress.Commands.add('selelctPlatformValue', selelctPlatformValue);
