declare namespace Cypress {
  interface Chainable {
    initAAIMock: typeof initAAIMock;
    initAlaCarteService : typeof initAlaCarteService;
    initZones : typeof initZones;
  }
}

function initGetSubscribers(response? : JSON) : void {
  cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/subscribers.json').then((res) => {
    cy.server()
      .route({
        method: 'GET',
        status : 200,
        url : Cypress.config('baseUrl') + "/aai_get_subscribers**",
        response : response ? response : res
      }).as('initGetSubscribers')
  });
}

function initGetAAISubDetails(response? : JSON) : void {
  cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/aaiSubDetails.json').then((res) => {
    cy.server()
      .route({
        method: 'GET',
        status: 200,
        url: Cypress.config('baseUrl') + "/aai_sub_details**",
        response: response ? response : res
      })
  });
}

function initAlaCarteService(response? : JSON) : void {
  cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/a-la-carteService.json').then((res) => {
    cy.server()
      .route({
        method: 'GET',
        status: 200,
        url: Cypress.config('baseUrl') + "/rest/models/services**",
        response: response ? response : res
      }).as('initAlaCarteService')
  });
}

function initAAIServices(response? : JSON) : void {
  cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/aaiServices.json').then((res) => {
    cy.server()
      .route({
        method: 'GET',
        status : 200,
        url : Cypress.config('baseUrl') + "/aai_get_services**",
        response : response ? response : res
      }).as(('initAAIServices'));
  });
}

function initZones(response? : JSON) : void {
  cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/zones.json').then((res) => {
    cy.server()
      .route({
        method: 'GET',
        status : 200,
        url : Cypress.config('baseUrl') + "/aai_get_aic_zones**",
        response : response ? response : res
      }).as(('zones'));
  });
}


function initAAIMock(): void {
  initGetSubscribers();
  initAAIServices();
}


Cypress.Commands.add('initAAIMock', initAAIMock);
Cypress.Commands.add('initAlaCarteService', initAlaCarteService);
Cypress.Commands.add('initZones', initZones);


