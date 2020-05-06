///<reference path="../../../node_modules/cypress/types/index.d.ts"/> / <reference types="Cypress" />

describe('Create Instance page: Pause after vfModule instantiation ', () => {

  beforeEach(() => {
    cy.clearSessionStorage();
    cy.setTestApiParamToGR();
    cy.initVidMock();
    cy.permissionVidMock();
    cy.login();

  });

  afterEach(() => {
    cy.screenshot();
  });

  it(`Pause on VFModule instantiation: create two vfmodules and pause after a first vfmodule e2e`, function () {
    cy.readFile('cypress/support/jsonBuilders/mocks/jsons/pauseVfmodule/vfmoduleCreate2AndetPauseRequestOnFirst.json').then((res) => {
      cy.setTestApiParamToGR();
      cy.setReduxState(<any>res);
      cy.openIframe('app/ui/#/servicePlanning?serviceModelId=f3862254-8df2-4a0a-8137-0a9fe985860c');

      pauseOnVfModuleInstantiation('node-da10c7fe-cf81-441c-9694-4e9ddf2054d8-vocg_1804_vf0..Vocg1804Vf..ocgapp_004..module-11', 0)
      mockAsyncBulkResponse();
      cy.getDrawingBoardDeployBtn().click();
      cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
        cy.readFile('../vid-app-common/src/test/resources/payload_jsons/vfmodule/vfModuleCreate2AndPauseOnFirstVfm.json').then((expectedResult) => {
          cy.deepCompare(xhr.request.body, expectedResult);
        });
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

  function pauseOnVfModuleInstantiation(nodeId: string, index: number) {
    cy.drawingBoardTreeOpenContextMenuByElementDataTestId(`${nodeId}`, index)
    .getElementByDataTestsId('context-menu-pause').click({force: true});
  }

});
