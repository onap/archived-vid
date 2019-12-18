declare namespace Cypress {
  interface Chainable {
    initAAIMock: typeof initAAIMock;
    initAlaCarteService : typeof initAlaCarteService;
    initZones : typeof initZones;
    initTenants : typeof initTenants;
    initSearchVNFMemebers : typeof  initSearchVNFMemebers;
    initActiveNetworks : typeof  initActiveNetworks;
    initActiveVPNs : typeof  initActiveVPNs;
    initGetAAISubDetails : typeof  initGetAAISubDetails;
    initAAIServices: typeof initAAIServices;
  }
}

function initGetSubscribers(response? : JSON) : void {
  cy.readFile('cypress/support/jsonBuilders/mocks/jsons/subscribers.json').then((res) => {
    cy.server()
      .route({
        method: 'GET',
        status : 200,
        url : Cypress.config('baseUrl') + "/aai_get_subscribers**",
        response : response ? response : res
      }).as('initGetSubscribers')
  });
}

function initAaiGetFullSubscribers(response? : JSON) : void {
  cy.readFile('cypress/support/jsonBuilders/mocks/jsons/subscribers.json').then((res) => {
    cy.server()
      .route({
        method: 'GET',
        status : 200,
        url : Cypress.config('baseUrl') + "/aai_get_full_subscribers**",
        response : response ? response : res
      }).as('initGetSubscribers')
  });
}

function initGetAAISubDetails(response? : JSON) : void {
  cy.readFile('cypress/support/jsonBuilders/mocks/jsons/aaiSubDetails.json').then((res) => {
    cy.server()
      .route({
        method: 'GET',
        status: 200,
        url: Cypress.config('baseUrl') + "/aai_sub_details/**",
        response: response ? response : res
      }).as('aai-sub-details')
  });
}

function initAlaCarteService(response? : JSON) : void {
  cy.readFile('cypress/support/jsonBuilders/mocks/jsons/a-la-carteService.json').then((res) => {
    cy.server()
      .route({
        method: 'GET',
        status: 200,
        url: Cypress.config('baseUrl') + "/rest/models/services**",
        response: response ? response : res
      }).as('initAlaCarteService')
  });
}





function initTenants(response? : JSON) : void {
  cy.readFile('cypress/support/jsonBuilders/mocks/jsons/tenants.json').then((res) => {
    cy.server()
      .route({
        method: 'GET',
        status: 200,
        url: Cypress.config('baseUrl') + "/aai_get_tenants/**",
        response: response ? response : res
      }).as('initTenants')
  });
}

function initAAIServices(response? : JSON) : void {
  cy.readFile('cypress/support/jsonBuilders/mocks/jsons/aaiServices.json').then((res) => {
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
  cy.readFile('cypress/support/jsonBuilders/mocks/jsons/zones.json').then((res) => {
    cy.server()
      .route({
        method: 'GET',
        status : 200,
        url : Cypress.config('baseUrl') + "/aai_get_aic_zones**",
        response : response ? response : res
      }).as(('zones'));
  });
}

//Mock of vnf's that members for VNF Group
function initSearchVNFMemebers(response? : JSON) : void {
  cy.readFile('../vid-automation/src/test/resources/VnfGroup/searchMembersResponse.json').then((res) => {
    cy.server()
      .route({
        method: 'GET',
        status : 200,
        url : Cypress.config('baseUrl') + "/aai_search_group_members/**",
        response : response ? response : res
      }).as(('searchVNFMembers'));
  });
}

function initActiveNetworks(response? : JSON) : void {
  cy.readFile('../vid-automation/src/test/resources/viewEdit/aaiGetActiveNetworks.json').then((res) => {
    cy.server()
      .route({
        method: 'GET',
        status : 200,
        url : Cypress.config('baseUrl') + "/aai_get_active_networks/**",
        response : response ? response : res
      }).as(('getActiveNetworks'));
  });
}

function initActiveVPNs(response? : JSON) : void {
  cy.readFile('cypress/support/jsonBuilders/mocks/jsons/aaiActiveVPNs.json').then((res) => {
    cy.server()
      .route({
        method: 'GET',
        status : 200,
        url : Cypress.config('baseUrl') + "/aai_get_vpn_list/**",
        response : response ? response : res
      }).as(('getVPNs'));
  });
}

function initAAIMock(): void {
  initAaiGetFullSubscribers();
  initGetSubscribers();
  initAAIServices();
  initTenants();

}


Cypress.Commands.add('initAAIMock', initAAIMock);
Cypress.Commands.add('initAlaCarteService', initAlaCarteService);
Cypress.Commands.add('initZones', initZones);
Cypress.Commands.add('initTenants', initTenants);
Cypress.Commands.add('initAaiGetFullSubscribers', initAaiGetFullSubscribers);
Cypress.Commands.add('initGetAAISubDetails', initGetAAISubDetails);
Cypress.Commands.add('initSearchVNFMemebers', initSearchVNFMemebers);
Cypress.Commands.add('initActiveNetworks', initActiveNetworks);
Cypress.Commands.add('initActiveVPNs', initActiveVPNs);
Cypress.Commands.add('initAAIServices', initAAIServices);



