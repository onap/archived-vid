import Chainable = Cypress.Chainable;

declare namespace Cypress {
  interface Chainable {
    isElementContainsAttr : typeof isElementContainsAttr;
    isElementDisabled : typeof isElementDisabled;
    isElementEnabled : typeof isElementEnabled;
    hasClass : typeof hasClass;
    getElementByDataTestsId : typeof getElementByDataTestsId;
  }
}

/*************************************************************************
 isElementContainsAttr : check if element with id contains some attribute
 *************************************************************************/
function isElementContainsAttr(id : string, attr: string) : void {
  cy.get("[data-tests-id='" + id +"']")
    .should('have.attr', attr);
}

/*********************************************************
 isElementDisabled : check if element with id is disabled
 *********************************************************/
function isElementDisabled(id : string) : void {
  cy.get( "[data-tests-id='" + id +"']").should('be:disabled');
}

function isElementEnabled(id : string) : void {
  cy.get( "button[data-tests-id='" + id +"']").should('be:enabled');
}

/****************************************************************
 hasClass : check if element with id contains some class name
 ****************************************************************/
function hasClass(id : string, className : string) : void {
  cy.get( "[data-tests-id='" + id +"']")
    .should('have.class', className);
}

function getElementByDataTestsId(dataTestsId : string) : Chainable<JQuery<HTMLElement>> {
  return cy.get( "[data-tests-id='" + dataTestsId +"']");
}


Cypress.Commands.add('isElementContainsAttr', isElementContainsAttr);
Cypress.Commands.add('isElementDisabled', isElementDisabled);
Cypress.Commands.add('isElementEnabled', isElementEnabled);
Cypress.Commands.add('hasClass', hasClass);
Cypress.Commands.add('getElementByDataTestsId', getElementByDataTestsId);
