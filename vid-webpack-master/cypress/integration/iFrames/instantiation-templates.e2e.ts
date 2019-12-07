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
        'fixture:../../../vid-automation/src/test/resources/asyncInstantiation/templates__instance_template.json')
      .as('templateTopology');

      // When...

      cy.openIframe(`app/ui/#/servicePlanning/${drawingBoardAction}` +
        `?jobId=${templateUuid}` +
        `&serviceModelId=${serviceModelId}`);

      cy.wait('@serviceModel');
      cy.wait('@templateTopology');
      cy.getElementByDataTestsId("node-vProbe_NC_VNF 0").should('be.visible');

      cy.getDrawingBoardDeployBtn().click();

      // Then...

      cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
         cy.readFile('../vid-automation/src/test/resources/asyncInstantiation/templates__instance_template.json').then((expectedResult) => {
           convertRollbackOnFailureValueFromStringToBoolean(expectedResult);
            cy.deepCompare(xhr.request.body, expectedResult);
        });
      });

    });

  });

  //We use this function because the deployService() on drawing-board-header.component class
  // changes rollbackOnFailure value from string type to boolean.
  function convertRollbackOnFailureValueFromStringToBoolean(expectedResult: any) {
    expectedResult.rollbackOnFailure = Boolean(expectedResult.rollbackOnFailure);
  }

  function mockAsyncBulkResponse() {
    cy.server().route({
      url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
      method: 'POST',
      status: 200,
      response: "[]",
    }).as("expectedPostAsyncInstantiation");
  }

});
