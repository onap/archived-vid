import ObjectLike = Cypress.ObjectLike;

describe('Drawing Board: Instantiation Templates', function () {

  describe('Instantiation templates ', () => {

    beforeEach(() => {
      cy.clearSessionStorage();
      cy.setTestApiParamToVNF();
      cy.initAAIMock();
      cy.initVidMock();
      cy.initDrawingBoardUserPermission();
      cy.login();

      mockAsyncBulkResponse();
    });

    afterEach(() => {
      cy.screenshot();
    });

    describe('Load Page and Deploy', () => {

      const serviceModelId = '6cfeeb18-c2b0-49df-987a-da47493c8e38';
      const templateUuid = "46390edd-7100-46b2-9f18-419bd24fb60b";

      const drawingBoardAction = `RECREATE`;
      const templateTopologyEndpoint = "templateTopology";


      it(`Given a stored template - when click "deploy" - then a coherent request should be sent upon deploy`, function () {

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

      it('View a template’s details as expected', ()=> {

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

        cy.drawingBoardTreeOpenContextMenuByElementDataTestId("node-21ae311e-432f-4c54-b855-446d0b8ded72-vProbe_NC_VNF 0")
        .drawingBoardTreeClickOnContextMenuOptionByName('Edit')
        .getElementByDataTestsId("lcpRegion").should('contain', 'hvf6')
        .getElementByDataTestsId("cancelButton").click();

        cy.getDrawingBoardDeployBtn().click();

        // Then...
        cy.wait('@expectedPostAsyncInstantiation').then( xhr=> {
            cy.readFile('../vid-automation/src/test/resources/asyncInstantiation/templates__instance_template.json').then((expectedResult) => {
              convertRollbackOnFailureValueFromStringToBoolean(expectedResult);

              let xhrBodyWithoutIsDirtyField = removeIsDirtyFieldFromXhrRequestBody(xhr);
              cy.deepCompare(xhrBodyWithoutIsDirtyField, expectedResult);
            });
          });
        });
      });

    });

  });

  //We use this function because the deployService() on drawing-board-header.component class
  // changes rollbackOnFailure value from string type to boolean.
  function convertRollbackOnFailureValueFromStringToBoolean(expectedResult: any) {
    expectedResult.rollbackOnFailure = Boolean(expectedResult.rollbackOnFailure);
  }

function removeIsDirtyFieldFromXhrRequestBody(xhr : any) {
  let xhrTempBody = JSON.parse(JSON.stringify(xhr.request.body));
  delete xhrTempBody.isDirty;
  return xhrTempBody;
}

  function mockAsyncBulkResponse() {
    cy.server().route({
      url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
      method: 'POST',
      status: 200,
      response: "[]",
    }).as("expectedPostAsyncInstantiation");
  }
