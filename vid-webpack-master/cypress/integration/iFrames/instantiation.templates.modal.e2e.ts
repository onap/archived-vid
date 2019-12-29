///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
describe('Template', () => {
  let instantiationTemplates : any[] = [];


  beforeEach(() => {
    cy.clearSessionStorage();
    cy.setReduxState();
    cy.preventErrorsOnLoading();
    cy.initAAIMock();
    cy.initVidMock();
    cy.login();

    cy.readFile('cypress/support/jsonBuilders/mocks/jsons/flags.cypress.json').then((flags) => {
      cy.server()
      .route({
        method: 'GET',
        delay: 0,
        status: 200,
        url: Cypress.config('baseUrl') + "/flags**",
        response: {
          "FLAG_VF_MODULE_RESUME_STATUS_CREATE": false,
          "FLAG_2004_INSTANTIATION_TEMPLATES_POPUP": true
        }
      }).as('initFlags');
    });

    cy.route(Cypress.config('baseUrl') + `**/instantiationTemplates?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd**`,
      'fixture:../../../vid-automation/src/test/resources/asyncInstantiation/templates__instance_teplate_for_modal.json');

    cy.route(Cypress.config('baseUrl') + "/getuserID", '16807000');

    cy.readFile('fixture:../../../vid-automation/src/test/resources/asyncInstantiation/templates__instance_teplate_for_modal.json').then((res)=>{
      instantiationTemplates = res;
    });

    cy.openPopupIframe('/app/ui/#/servicePopup?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd&isCreate=true');

  });

  afterEach(() => {
    cy.screenshot();
  });

  it('when open service popup should show template button', function () {
    cy.getElementByDataTestsId('templateButton').contains('Template')
      .getElementByDataTestsId('templateButton').click({force: true}) // Open template Modal
      .getElementByDataTestsId('template-modal-title').contains('Templates') // Check Modal header
      .getElementByDataTestsId('description-part-1').contains('The following list presents previous instantiations done for this model in this version.')
      .getElementByDataTestsId('description-part-2').contains('You may use one of them as a baseline for your instantiation or start from scratch.')
      .getElementByDataTestsId('description-part-3').contains('Once you selecting one allows you to change the data before start instantiating.')
    // cy.wait('@templateTopology');

    //check table headers
    cy.get(`#header-userId`).contains('User ID');
    cy.get(`#header-createDate`).contains('Date');
    cy.get(`#header-instanceName`).contains('Instance Name');
    cy.get(`#header-instantiationStatus`).contains('Instantiation Status');
    cy.get(`#header-region`).contains('Region');
    cy.get(`#header-tenant`).contains('Tenant');
    cy.get(`#header-aicZone`).contains('AIC Zone');

    let a = instantiationTemplates[0];
    // check table body row
    cy.getElementByDataTestsId(`userId-${instantiationTemplates[0].jobId}`).contains('16807000');
    cy.getElementByDataTestsId(`createDate-${instantiationTemplates[0].jobId}`).contains('2019-12-26 11:57:05');
    cy.getElementByDataTestsId(`instanceName-${instantiationTemplates[0].jobId}`).contains('SERVICE_NAME');
    cy.getElementByDataTestsId(`instantiationStatus-${instantiationTemplates[0].jobId}`).contains('IN_PROGRESS');
    cy.getElementByDataTestsId(`summary-${instantiationTemplates[0].jobId}`).contains('vfModule: 2, volumeGroup: 1, vnf: 1');
    cy.getElementByDataTestsId(`region-${instantiationTemplates[0].jobId}`).contains('hvf3 (SOMENAME)');
    cy.getElementByDataTestsId(`tenant-${instantiationTemplates[0].jobId}`).contains('greatTenant');
    cy.getElementByDataTestsId(`aicZone-${instantiationTemplates[0].jobId}`).contains('NFTJSSSS-NFT1');


    //check load button is disabled
    cy.getElementByDataTestsId('LoadTemplateButton').should('be.disabled');
    cy.getElementByDataTestsId('row-ef3430f8-6350-454c-a7c2-89ba301522c1').click();
    cy.getElementByDataTestsId('LoadTemplateButton').should('not.be.disabled');

    //filter by userId
    cy.get('.member-table-row').should('have.length', 2);
    cy.getElementByDataTestsId('filterByUserIdTestId').click();
    cy.get('.member-table-row').should('have.length', 1);

  });

  it('clicking on load template button, go to expected url', function () {

    cy.getElementByDataTestsId('templateButton').contains('Template')
    .getElementByDataTestsId('templateButton').click({force: true}) // Open template Modal

    const serviceModelId = '5c9e863f-2716-467b-8799-4a67f378dcaa';
    const jobId = 'ef3430f8-6350-454c-a7c2-89ba301522c1';
    const vidBaseUrl = `http://localhost:8080/vid/serviceModels.htm`;

    cy.getElementByDataTestsId('row-ef3430f8-6350-454c-a7c2-89ba301522c1').click();
    cy.getElementByDataTestsId('LoadTemplateButton').click().setViewportToDefault();

    cy.location().should((loc) => {
      expect(loc.toString()).to.eq(`${vidBaseUrl}#/servicePlanning/RECREATE?serviceModelId=${serviceModelId}&jobId=${jobId}`);
    });
  });

});

