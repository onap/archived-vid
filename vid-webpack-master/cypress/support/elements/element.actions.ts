import Chainable = Cypress.Chainable;

declare namespace Cypress {
  interface Chainable {
    isElementContainsAttr : typeof isElementContainsAttr;
    isElementDisabled : typeof isElementDisabled;
    isElementEnabled : typeof isElementEnabled;
    hasClass : typeof hasClass;
    getElementByDataTestsId : typeof getElementByDataTestsId;
    getTagElementContainsText : typeof  getTagElementContainsText;
  }
}

/*************************************************************************
 isElementContainsAttr : check if element with id contains some attribute
 *************************************************************************/
function isElementContainsAttr(id : string, attr: string) : void {
  cy.getElementByDataTestsId(id).should('have.attr', attr);
}

/*********************************************************
 isElementDisabled : check if element with id is disabled
 *********************************************************/
function isElementDisabled(id : string) : void {
  cy.getElementByDataTestsId(id).should('be:disabled');
}

function isElementEnabled(id : string) : void {
  cy.getElementByDataTestsId(id).should('be:enabled');
}

/****************************************************************
 hasClass : check if element with id contains some class name
 ****************************************************************/
function hasClass(id : string, className : string) : void {
  cy.getElementByDataTestsId(id).should('have.class', className);
}

function getElementByDataTestsId(dataTestsId : string) : Chainable<JQuery<HTMLElement>> {
  return cy.get( "[data-tests-id='" + dataTestsId +"']");
}

/**************************************************
 getTagElementContainsText : return tag with text
 **************************************************/
function getTagElementContainsText(tag : string, text : string) : Chainable<JQuery<HTMLElement>> {
  return cy.contains(tag,text);
}


Cypress.Commands.add('isElementContainsAttr', isElementContainsAttr);
Cypress.Commands.add('isElementDisabled', isElementDisabled);
Cypress.Commands.add('isElementEnabled', isElementEnabled);
Cypress.Commands.add('hasClass', hasClass);
Cypress.Commands.add('getElementByDataTestsId', getElementByDataTestsId);
Cypress.Commands.add('getTagElementContainsText', getTagElementContainsText);
