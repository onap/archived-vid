declare namespace Cypress {
  interface Chainable {
    typeToInput : typeof typeToInput;
    blurInput : typeof blurInput;
    focusInput : typeof focusInput;
    shouldInputContainsText: typeof shouldInputContainsText;
  }
}

/**********************************
  Type to input with id some text
 *********************************/
function typeToInput(id : string, text : string) : void {
  cy.get( "[data-tests-id='" + id +"']")
    .type(text, {force: true});
}

/********************
 blur input with id
 ********************/
function blurInput(id : string) : void {
  cy.get( "[data-tests-id='" + id +"']")
    .blur();
}

/********************
 focus input with id
 ********************/
function focusInput(id : string) : void {
  cy.get( "[data-tests-id='" + id +"']")
    .focus();
}

/*****************************************
 test if input with id contains some text
 ****************************************/
function shouldInputContainsText(id : string, text : string) : void {
  cy.get( "[data-tests-id='" + id +"']")
    .contains('text')
}

Cypress.Commands.add('typeToInput', typeToInput);
Cypress.Commands.add('blurInput', blurInput);
Cypress.Commands.add('focusInput', focusInput);
Cypress.Commands.add('shouldInputContainsText', shouldInputContainsText);

