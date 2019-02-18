declare namespace Cypress {
  interface Chainable {
    genericFormSubmitForm: typeof genericFormSubmitForm
  }
}

function genericFormSubmitForm() : Chainable<any>  {
  return  cy.getElementByDataTestsId('form-set').click({force: true});
}


Cypress.Commands.add('genericFormSubmitForm', genericFormSubmitForm);
