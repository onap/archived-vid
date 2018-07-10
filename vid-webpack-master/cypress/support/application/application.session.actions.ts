declare namespace Cypress {
  interface Chainable {
    setReduxState : typeof setReduxState;
  }
}

/**********************************
 Type to input with id some text
 *********************************/
function setReduxState(state?: string) : void {
  cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/basicRedux.json').then((res) => {
    cy.window().then((win) => {
      win.sessionStorage.setItem('reduxState',  JSON.stringify(state ? state : res));
    });
  });
}



Cypress.Commands.add('setReduxState', setReduxState);

