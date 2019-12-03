describe('Drawing Board: Instantiation Templates', function () {

  describe('Load Page and Deploy', () => {

    beforeEach(() => {
      cy.clearSessionStorage();
      cy.setTestApiParamToVNF();
      cy.initVidMock();
      cy.initDrawingBoardUserPermission();
      cy.login();

      mockAsyncBulkResponse();
    });

    afterEach(() => {
      cy.screenshot();
    });

    it(`Given a stored template - when click "deploy" - then a coherent request should be sent upon deploy`, function () {
      const serviceModelId = '6cfeeb18-c2b0-49df-987a-da47493c8e38';
      const templateUuid = "46390edd-7100-46b2-9f18-419bd24fb60b";

      const drawingBoardAction = `RECREATE`;
      const templateTopologyEndpoint = "templateTopology";

      // Given...

      cy.route(`**/rest/models/services/${serviceModelId}`,
        'fixture:../support/jsonBuilders/mocks/jsons/instantiationTemplates/templates__service_model.json')
      .as('serviceModel');

      cy.route(`**/asyncInstantiation/${templateTopologyEndpoint}/${templateUuid}`,
        'fixture:../support/jsonBuilders/mocks/jsons/instantiationTemplates/templates__instance_template.json')
      .as('templateTopology');

      // When...

      cy.openIframe(`app/ui/#/servicePlanning/${drawingBoardAction}` +
        `?jobId=${templateUuid}` +
        `&serviceModelId=${serviceModelId}`);

      cy.wait('@serviceModel');
      cy.wait('@templateTopology');

      cy.getDrawingBoardDeployBtn().click();

      // Then...

      cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
        // cy.readFile('cypress/support/jsonBuilders/mocks/jsons/instantiationTemplates/templates__instance_template.json').then((expectedResult) => {
        //   cy.deepCompare(xhr.request.body, expectedResult);
        // });
      });

    });

  });


  function mockAsyncBulkResponse() {
    cy.server().route({
      url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
      method: 'POST',
      status: 200,
      response: "[]",
    }).as("expectedPostAsyncInstantiation");
  }

});
