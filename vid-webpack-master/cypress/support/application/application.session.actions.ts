declare namespace Cypress {
  interface Chainable {
    setReduxState : typeof setReduxState;
    getReduxState : typeof getReduxState;
    clearSessionStorage: typeof clearSessionStorage;
    setTestApiParamToGR: typeof setTestApiParamToGR;
    setTestApiParamToVNF: typeof setTestApiParamToVNF;
  }
}

/**********************************
 Type to input with id some text
 *********************************/
function setReduxState(state?: string) : void {
  cy.readFile('cypress/support/jsonBuilders/mocks/jsons/basicRedux.json').then((res) => {
    cy.window().then((win) => {
      win.sessionStorage.setItem('reduxState',  JSON.stringify(state ? state : res));
    });
  });
}
function getReduxState(): Chainable<any> {
  return cy.window().then((win) => {
    let stateRaw = win.sessionStorage.getItem('reduxState');
    return JSON.parse(stateRaw ?  stateRaw : '{}');
  });
}

function clearSessionStorage() : Cypress.Chainable<any> {
  return cy.window().then((win) => {
    win.sessionStorage.clear();
  });
}

function setTestApiParamToGR() : void {
  cy.window().then((win) => {
    win.sessionStorage.setItem('msoRequestParametersTestApiValue', 'GR_API');
  });
}

function setTestApiParamToVNF() : void {
  cy.window().then((win) => {
    win.sessionStorage.setItem('msoRequestParametersTestApiValue', 'VNF_API');
  });
}

Cypress.Commands.add('setReduxState', setReduxState);
Cypress.Commands.add('getReduxState', getReduxState);
Cypress.Commands.add('clearSessionStorage', clearSessionStorage);
Cypress.Commands.add('setTestApiParamToGR', setTestApiParamToGR);
Cypress.Commands.add('setTestApiParamToVNF',setTestApiParamToVNF);
