declare namespace Cypress {
  interface Chainable {
    genericFormSubmitForm: typeof genericFormSubmitForm
    selectMultiselectValue: typeof selectMultiselectValue
    checkMultiSelectValue: typeof checkMultiSelectValue
  }
}


function selectMultiselectValue(dataTestsId: string , selectOptionId: string) {
      cy.getElementByDataTestsId(dataTestsId).find('.c-btn').eq(0).click({force: true})
      cy.getElementByDataTestsId(selectOptionId).click();
      cy.getElementByDataTestsId(dataTestsId).find('.c-btn').eq(0).click({force: true})
}

function checkMultiSelectValue(dataTestsId: string, value: string) {
  return cy.getElementByDataTestsId(dataTestsId).should("contain", value)
}

function genericFormSubmitForm(): Chainable<any> {
  return cy.getElementByDataTestsId('form-set').click({force: true});
}


Cypress.Commands.add('genericFormSubmitForm', genericFormSubmitForm);
Cypress.Commands.add('selectMultiselectValue', selectMultiselectValue);
Cypress.Commands.add('checkMultiSelectValue', checkMultiSelectValue);
