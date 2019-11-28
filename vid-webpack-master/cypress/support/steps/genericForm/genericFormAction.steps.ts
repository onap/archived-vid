declare namespace Cypress {
  interface Chainable {
    genericFormSubmitForm: typeof genericFormSubmitForm
    selelctPlatformValue: typeof selelctPlatformValue
  }
}


function selelctPlatformValue(selectOption: string) {
      cy.getElementByDataTestsId("multi-selectPlatform").get('.c-btn').click({force: true});
      cy.getElementByDataTestsId(`multi-selectPlatform-${selectOption}`).click();
      cy.getElementByDataTestsId("multi-selectPlatform").get('.c-btn').click({force: true});
}


function genericFormSubmitForm(): Chainable<any> {
  return cy.getElementByDataTestsId('form-set').click({force: true});
}


Cypress.Commands.add('genericFormSubmitForm', genericFormSubmitForm);
Cypress.Commands.add('selelctPlatformValue', selelctPlatformValue);
