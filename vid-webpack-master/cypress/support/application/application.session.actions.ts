declare namespace Cypress {
  interface Chainable {
    setReduxState : typeof setReduxState;
    getReduxState : typeof getReduxState;
    setTestApiParamToGR: typeof setTestApiParamToGR;
    setTestApiParamToVNF: typeof setTestApiParamToVNF;
    buildReduxStateWithServiceRespone: typeof buildReduxStateWithServiceRespone;
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

function updateObject(obj: any, key: string, val: any, value:any) {
  return JSON.parse(JSON.stringify(obj)
    .replace(new RegExp(`"${key}":"${val}"`), `"${key}":"${value}"`))
}

function buildReduxStateWithServiceRespone(res: any, serviceId:string, isEcompGeneratedNaming:boolean) :void {
  res = updateObject(res, "ecomp_generated_naming", !isEcompGeneratedNaming, isEcompGeneratedNaming);
  cy.window().then((win) => {
    win.sessionStorage.setItem('reduxState',  JSON.stringify({
      "global": {
        "name": null
      },
      "service": {
        "serviceHierarchy": {
          [serviceId] : res
        },
        "serviceInstance": {
          [serviceId]: {
            "existingVNFCounterMap": {},
            "existingVnfGroupCounterMap": {},
            "existingNetworksCounterMap": {},
            "vnfs": {},
            "vnfGroups": {},
            "isEcompGeneratedNaming": isEcompGeneratedNaming,
            "existingNames": {},
            "vidNotions": res.service.vidNotions
          }
        }
      }
    }));
  });
}

Cypress.Commands.add('setReduxState', setReduxState);
Cypress.Commands.add('getReduxState', getReduxState);
Cypress.Commands.add('setTestApiParamToGR', setTestApiParamToGR);
Cypress.Commands.add('setTestApiParamToVNF',setTestApiParamToVNF);
Cypress.Commands.add('buildReduxStateWithServiceRespone', buildReduxStateWithServiceRespone);
