declare namespace Cypress {
  interface Chainable {
    loadDrawingBoardWithRecreateMode: typeof loadDrawingBoardWithRecreateMode ,
    loadDrawingBoardWithRecreateModeNetwork: typeof loadDrawingBoardWithRecreateModeNetwork,
    loadDrawingBoardWithRecreateModeInternal: typeof loadDrawingBoardWithRecreateModeInternal
  }
}

function loadDrawingBoardWithRecreateMode(templateWithVnfSetup : any) {
  cy.loadDrawingBoardWithRecreateModeInternal(
    '../../' + templateWithVnfSetup.instanceTemplateFile,
    templateWithVnfSetup.serviceModelId,
    templateWithVnfSetup.serviceModelFile);
}

function loadDrawingBoardWithRecreateModeNetwork(templateWithNetworkSetup : any) {
  cy.loadDrawingBoardWithRecreateModeInternal(
    '../../' + templateWithNetworkSetup.instanceTemplateFile,
    templateWithNetworkSetup.serviceModelId,
    templateWithNetworkSetup.serviceModelFile);
}

function loadDrawingBoardWithRecreateModeInternal(instanceTemplate: string, serviceModelIdToLoad: any, serviceModel: string) {
  const templateUuid = "46390edd-7100-46b2-9f18-419bd24fb60b";

  const drawingBoardAction = `RECREATE`;
  const templateTopologyEndpoint = "templateTopology";
  cy.route(`**/rest/models/services/${serviceModelIdToLoad}`,
    'fixture:' + serviceModel)
    .as('serviceModel');

  cy.route(`**/instantiationTemplates/${templateTopologyEndpoint}/${templateUuid}`,
    'fixture:' + instanceTemplate)
    .as('templateTopology');

  // When...

  cy.openIframe(`app/ui/#/servicePlanning/${drawingBoardAction}` +
    `?jobId=${templateUuid}` +
    `&serviceModelId=${serviceModelIdToLoad}`);

  cy.wait('@serviceModel');
  cy.wait('@templateTopology');
}





Cypress.Commands.add('loadDrawingBoardWithRecreateMode', loadDrawingBoardWithRecreateMode);
Cypress.Commands.add('loadDrawingBoardWithRecreateModeNetwork', loadDrawingBoardWithRecreateModeNetwork);
Cypress.Commands.add('loadDrawingBoardWithRecreateModeInternal', loadDrawingBoardWithRecreateModeInternal);
