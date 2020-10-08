declare namespace Cypress {
  interface Chainable {
    initVidMock: typeof initVidMock;
    preventErrorsOnLoading : typeof preventErrorsOnLoading;
    initCategoryParameter : typeof initCategoryParameter;
    initAuditInfoMSO: typeof initAuditInfoMSO;
    initAuditInfoMSOALaCarte: typeof initAuditInfoMSOALaCarte;
    initAsyncInstantiation : typeof  initAsyncInstantiation;
    mockLatestVersionForService : typeof  mockLatestVersionForService;
    initAuditInfoMSOALaCarteNew: typeof initAuditInfoMSOALaCarteNew;
  }
}

function preventErrorsOnLoading() : void {
  cy.on('uncaught:exception', (err, runnable) => {
    return false
  });
}

function initGetToMenuInfo(response? : JSON) : void {
    cy.readFile('cypress/support/jsonBuilders/mocks/jsons/topMenuInfo.json').then((res) => {
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
    cy.readFile('cypress/support/jsonBuilders/mocks/jsons/categoryParametres.json').then((res) => {
      cy.server()
        .route({
          method: 'GET',
          status : 200,
          url : Cypress.config('baseUrl') + "/category_parameter**",
          response : response ? response : res
        }).as('initCategoryParameters');
    })
}

function initFlags(response? : JSON, delay?: number, status?: number) : void {
  cy.readFile('cypress/support/jsonBuilders/mocks/jsons/flags.cypress.json').then((res) => {
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
  cy.readFile('cypress/support/jsonBuilders/mocks/jsons/auditInfoVid.json').then((res) => {
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
  cy.readFile('cypress/support/jsonBuilders/mocks/jsons/auditInfoMSO.json').then((res) => {
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

function initAuditInfoMSOALaCarte(response? : JSON, delay?: number, status?: number) : void {
  cy.readFile('../vid-automation/src/test/resources/a-la-carte/auditInfoMSOALaCarte.json').then((res) => {
    cy.server()
      .route({
        method: 'GET',
        delay : delay ? delay : 0,
        status : status ? status : 200,
        url : Cypress.config('baseUrl') + "/asyncInstantiation/auditStatus/**/mso**",
        response : response ? response : res
      }).as('initAuditInfoMSOAlaCarte');
  })
}

function initAuditInfoMSOALaCarteNew(response? : JSON, delay?: number, status?: number) : void {
  cy.readFile('../vid-automation/src/test/resources/a-la-carte/auditInfoMSOALaCarteNew.json').then((res) => {
    cy.server()
      .route({
        method: 'GET',
        delay : delay ? delay : 0,
        status : status ? status : 200,
        url : Cypress.config('baseUrl') + "/asyncInstantiation/auditStatus/**/mso**",
        response : response ? response : res
      }).as('initAuditInfoMSOALaCarteNew');
  })
}

function initAsyncInstantiation(response? : JSON, delay?: number, status?: number) : void {
  cy.readFile('cypress/support/jsonBuilders/mocks/jsons/basicAsyncInstantiation.json').then((res) => {
    cy.server()
      .route({
        method: 'GET',
        delay : delay ? delay : 0,
        status : status ? status : 200,
        url : Cypress.config('baseUrl') + "/asyncInstantiation",
        response : response ? response : res
      }).as('initAsyncInstantiation');
  })
}

function mockLatestVersionForService(serviceUuid: string, invariantId: string) {
    cy.server().route({
      url: Cypress.config('baseUrl') + '/aai_get_newest_model_version_by_invariant/' + invariantId,
      method: 'GET',
      status: 200,
      response: {
        "modelVersionId": serviceUuid,
        "modelName": "SERVICE_INSTANCE_NAME",
        "modelVersion": "2.0",
        "distributionStatus": "DISTRIBUTION_COMPLETE_OK",
        "resourceVersion": "resourceVersion",
        "modelDescription": "modelDescription"
      },
    }).as("expectLatestServiceModelUpgradeVersion")
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
Cypress.Commands.add('initAuditInfoMSO', initAuditInfoMSO);
Cypress.Commands.add('initAuditInfoMSOALaCarte', initAuditInfoMSOALaCarte);
Cypress.Commands.add('initAuditInfoMSOALaCarteNew', initAuditInfoMSOALaCarteNew);
Cypress.Commands.add('initAsyncInstantiation', initAsyncInstantiation);
Cypress.Commands.add('mockLatestVersionForService', mockLatestVersionForService);

