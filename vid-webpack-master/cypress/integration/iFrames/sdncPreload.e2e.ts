describe('SDNC preload ', () => {

  beforeEach(() => {
    cy.clearSessionStorage();
    cy.setTestApiParamToGR();
    cy.initAAIMock();
    cy.initGetAAISubDetails();
    cy.initVidMock();
    cy.initDrawingBoardUserPermission();
    cy.login();
  });

  afterEach(() => {
    cy.screenshot();
  });

  it('feature toggle is on and SDNC is checked then SDNC preload file is enable : upload success' , () => {
    mockPreloadResult(true, 200);
    cy.loadDrawingBoardWithRecreateMode(templateWithVnfSetup);
    cy.editNode("node-c09e4530-8fd8-418f-9483-2f57ce927b05-vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1");
    checkUploadLinkLogic();

    uploadFile().then(() => {
      cy.get('.sdc-modal__content').should('contain', 'The pre-load file(s) have been uploaded successfully.');
      cy.getElementByDataTestsId('button-ok').click()
        .getElementByDataTestsId('sdnc_pereload_upload_link').should('contain', 'Upload another')
    });
  });

  it('feature toggle is on and SDNC is checked then SDNC preload file is enable : upload fail', () => {
    mockPreloadResult(false, 200);
    cy.loadDrawingBoardWithRecreateMode(templateWithVnfSetup);

    cy.editNode("node-c09e4530-8fd8-418f-9483-2f57ce927b05-vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1");
    checkUploadLinkLogic();

    uploadFile().then(() => {
      cy.get('.sdc-modal__content').should('contain', 'Failed to upload one or more of the files, please retry.');
      cy.getElementByDataTestsId('button-ok').click()
        .getElementByDataTestsId('sdnc_pereload_upload_link').should('contain', 'Upload')
    });
  });
});


let apiTestResources = '../vid-automation/src/test/resources/asyncInstantiation/';

const templateWithVnfSetup = {
  serviceModelId: '6cfeeb18-c2b0-49df-987a-da47493c8e38',
  instanceTemplateFile: apiTestResources + 'templates__instance_template.json',
  instanceTemplateSetWithoutModifyFile: apiTestResources + 'templates__instance_from_template__set_without_modify1.json',
  serviceModelFile: '../support/jsonBuilders/mocks/jsons/instantiationTemplates/templates__service_model.json',
};

function mockAsyncBulkResponse() {
  cy.server().route({
    url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
    method: 'POST',
    status: 200,
    response: true,
  }).as("expectedPostAsyncInstantiation");
}

function mockPreloadResult(response: boolean, status?: number) {
  cy.server().route({
    url: Cypress.config('baseUrl') + '/preload',
    method: 'POST',
    status: status ? status : 200,
    response: response,
  }).as("preload");
}


function uploadFile() {
  // @ts-ignore
  return new Promise((resolve) => {
    const fileName = '../support/uploadFiles/sdncPreLoadFileExample.json';
    cy.fixture(fileName).then(fileContent => {
      // @ts-ignore
      cy.get('input[type=file]').eq(0).upload({fileContent, fileName, mimeType: 'application/json'}).then(() => {
        resolve();
      });
    })
  });
}

function checkUploadLinkLogic() {
  cy.getElementByDataTestsId('sdnc_pereload_upload_link').should('contain', 'Upload').should('not.have.class', 'disabled')
    .getElementByDataTestsId('sdncPreLoad').click()
    .getElementByDataTestsId('sdnc_pereload_upload_link').should('contain', 'Upload').should('have.class', 'disabled')
    .getElementByDataTestsId('sdncPreLoad').click()
}
