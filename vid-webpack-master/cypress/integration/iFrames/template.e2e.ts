///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
describe('Template', () => {

  beforeEach(() => {
    cy.clearSessionStorage();
    cy.setReduxState();
    cy.preventErrorsOnLoading();
    cy.initAAIMock();
    cy.initVidMock();
    cy.login();
  });

  afterEach(() => {
    cy.screenshot();
  });

  it('when open service popup should show template button', function () {
    cy.readFile('cypress/support/jsonBuilders/mocks/jsons/flags.cypress.json').then((flags) => {
      cy.server()
        .route({
          method: 'GET',
          delay :  0,
          status :  200,
          url : Cypress.config('baseUrl') + "/flags**",
          response : {
            "FLAG_VF_MODULE_RESUME_STATUS_CREATE": false,
            "FLAG_2002_ENABLE_SERVICE_TEMPLATE" : true
          }
        }).as('initFlags');
    });

    cy.openIframe('/app/ui/#/servicePopup?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd&isCreate=true');
    cy.getElementByDataTestsId('templateButton').contains('Template');


  });
});

