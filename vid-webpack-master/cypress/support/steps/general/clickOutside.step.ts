declare namespace Cypress {
  interface Chainable {
    clickOutside: typeof clickOutside,
  }
}

function clickOutside(testByIdClickElement : string, testBeforeClickOutside : Function, testAfterClickOutside : Function) {
  testBeforeClickOutside();
  cy.getElementByDataTestsId(testByIdClickElement).click({force: true}).then(()=>{
    testAfterClickOutside();
  });
}

Cypress.Commands.add('clickOutside', clickOutside);
