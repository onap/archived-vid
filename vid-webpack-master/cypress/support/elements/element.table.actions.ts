declare namespace Cypress {
  interface Chainable {
    getTableRowByIndex : typeof getTableRowByIndex;
  }
}

/***************************************
  get table row by table id and index
 *************************************/
function getTableRowByIndex(id : string, index : number) : Chainable<JQuery<HTMLElement>> {
  return cy.get('table#' + id + ' tbody tr').eq(index);
}

Cypress.Commands.add('getTableRowByIndex', getTableRowByIndex);

