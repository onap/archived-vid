declare namespace Cypress {
  interface Chainable {
    typeToInput : typeof typeToInput;
    blurInput : typeof blurInput;
    focusInput : typeof focusInput;
    shouldInputContainsText: typeof shouldInputContainsText;
    clearInput: typeof clearInput;
  }
}

/**********************************
  Type to input with id some text
 *********************************/
function typeToInput(id : string, text : string) : void {
  cy.get( "[data-tests-id='" + id +"']")
    .type(text, {force: true});
}

/**********************************
 clear input
 *********************************/
function clearInput(id : string) : void {
  cy.get( "[data-tests-id='" + id +"']")
    .clear();
}

/********************
 blur input with id
 ********************/
function blurInput(id : string) : void {
  cy.getElementByDataTestsId(id).blur();
}

/********************
 focus input with id
 ********************/
function focusInput(id : string) : void {
  cy.getElementByDataTestsId(id).focus();
}

/*****************************************
 test if input with id contains some text
 ****************************************/
function shouldInputContainsText(id : string, text : string) : void {
  cy.getElementByDataTestsId(id).contains(text)
}

Cypress.Commands.add('typeToInput', typeToInput);
Cypress.Commands.add('blurInput', blurInput);
Cypress.Commands.add('focusInput', focusInput);
Cypress.Commands.add('shouldInputContainsText', shouldInputContainsText);
Cypress.Commands.add('clearInput', clearInput);

