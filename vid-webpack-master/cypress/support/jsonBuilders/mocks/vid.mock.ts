declare namespace Cypress {
  interface Chainable {
    initVidMock: typeof initVidMock;
    preventErrorsOnLoading : typeof preventErrorsOnLoading;
    initCategoryParameter : typeof initCategoryParameter;
  }
}

function preventErrorsOnLoading() : void {
  cy.on('uncaught:exception', (err, runnable) => {
    return false
  });
}

function initGetToMenuInfo(response? : JSON) : void {
    cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/topMenuInfo.json').then((res) => {
      cy.server()
        .route({
          method: 'GET',
          status : 200,
          url : Cypress.config('baseUrl') + "/get_topMenuInfo",
          response : response ? response : res
        });
    });

}


function initCategoryParameter(response? : JSON) : void {
    cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/categoryParametres.json').then((res) => {
      cy.server()
        .route({
          method: 'GET',
          status : 200,
          url : Cypress.config('baseUrl') + "/category_parameter**",
          response : response ? response : res
        });
    })
}

function initFlags(response? : JSON, delay?: number, status?: number) : void {
  cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/flags.json').then((res) => {
    cy.server()
      .route({
        method: 'GET',
        delay : delay ? delay : 0,
        status : status ? status : 200,
        url : Cypress.config('baseUrl') + "/flags**",
        response : response ? response : res
      }).as('initFlags');
  })
}

function initAuditInfoVID(response? : JSON, delay?: number, status?: number) : void {
  cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/auditInfoVid.json').then((res) => {
    cy.server()
      .route({
        method: 'GET',
        delay : delay ? delay : 0,
        status : status ? status : 200,
        url : Cypress.config('baseUrl') + "/asyncInstantiation/auditStatus/**?source=VID",
        response : response ? response : res
      }).as('initAuditInfoVID');
  })
}

function initAuditInfoMSO(response? : JSON, delay?: number, status?: number) : void {
  cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/auditInfoMSO.json').then((res) => {
    cy.server()
      .route({
        method: 'GET',
        delay : delay ? delay : 0,
        status : status ? status : 200,
        url : Cypress.config('baseUrl') + "/asyncInstantiation/auditStatus/**?source=MSO",
        response : response ? response : res
      }).as('initAuditInfoMSO');
  })
}

function initVidMock(): void {
  initGetToMenuInfo();
  initCategoryParameter();
  initFlags();
  initAuditInfoVID();
  initAuditInfoMSO();
}


Cypress.Commands.add('initVidMock', initVidMock);
Cypress.Commands.add('preventErrorsOnLoading', preventErrorsOnLoading);
Cypress.Commands.add('initCategoryParameter', initCategoryParameter);
