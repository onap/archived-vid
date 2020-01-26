///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
describe('Template', () => {
  const templateJobIdFromE2EFile = 'ef3430f8-6350-454c-a7c2-89ba301522c1';

  const aSecondTemplateInfo = {
    id: 7,
    created: 1525075968000,
    modified: 1525075971000,
    action: "INSTANTIATE",
    createdId: null,
    modifiedId: null,
    rowNum: null,
    auditUserId: null,
    auditTrail: null,
    jobId: "13063a83-924e-4500-a3a1-e53d1b58450b",
    templateId: "d42ba7c8-9e19-4e34-ae2c-d8af3f24498e",
    userId: "17807000",
    aLaCarte: false,
    msoRequestId: "c0011670-0e1a-4b74-945d-8bf5aede1d9d",
    jobStatus: "IN_PROGRESS",
    statusModifiedDate: 1525075968000,
    hidden: false,
    pause: false,
    owningEntityId: "d61e6f2d-12fa-4cc2-91df-7c244011d6fc",
    owningEntityName: "WayneHolland",
    project: "WATKINS",
    aicZoneId: "NFT1",
    aicZoneName: "NFTJSSSS-NFT1",
    tenantId: "bae71557c5bb4d5aac6743a4e5f1d054",
    tenantName: "AIN Web Tool-15-D-testalexandria",
    regionId: "hvf6",
    regionName: null,
    serviceType: "TYLER SILVIA",
    subscriberName: "e433710f-9217-458d-a79d-1c7aff376d89",
    serviceInstanceId: null,
    serviceInstanceName: "nWUfl instance name_001",
    serviceModelId: "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
    serviceModelName: "action-data",
    serviceModelVersion: "1.0",
    createdBulkDate: 1525075968000,
    isRetryEnabled: false
  };

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

    cy.readFile('fixture:../../../vid-automation/src/test/resources/asyncInstantiation/vidRequestCreateBulkOfMacro__template_info.json').then((templateInfoFromE2EFile)=>{
      cy.route(Cypress.config('baseUrl') + "/instantiationTemplates?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd",
        [ templateInfoFromE2EFile, aSecondTemplateInfo ]
      )
    });

    cy.route(Cypress.config('baseUrl') + "/getuserID", 'us16807000');

    cy.openPopupIframe('/app/ui/#/servicePopup?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd&isCreate=true&isInstantiationTemplateExists=true');

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
      .getElementByDataTestsId('description-part-3').contains('Once you selecting one allows you to change the data before start instantiating.');

    //check table headers
    cy.get(`#header-userId`).contains('User ID');
    cy.get(`#header-createDate`).contains('Date');
    cy.get(`#header-instanceName`).contains('Instance Name');
    cy.get(`#header-instantiationStatus`).contains('Instantiation Status');
    cy.get(`#header-region`).contains('Region');
    cy.get(`#header-tenant`).contains('Tenant');

    // check table body row
    cy.getElementByDataTestsId(`userId-${templateJobIdFromE2EFile}`).contains('16807000');
    cy.getElementByDataTestsId(`createDate-${templateJobIdFromE2EFile}`).contains(/2019-12-26 [0-9]{1,2}:57:05/); //timezone insensitive
    cy.getElementByDataTestsId(`instanceName-${templateJobIdFromE2EFile}`).contains('SERVICE_NAME');
    cy.getElementByDataTestsId(`instantiationStatus-${templateJobIdFromE2EFile}`).contains('IN_PROGRESS');
    cy.getElementByDataTestsId(`summary-${templateJobIdFromE2EFile}`).contains('vnf: 1, vfModule: 2, volumeGroup: 1');
    cy.getElementByDataTestsId(`region-${templateJobIdFromE2EFile}`).contains('hvf3 (SOMENAME)');
    cy.getElementByDataTestsId(`tenant-${templateJobIdFromE2EFile}`).contains('greatTenant');


    //check load button is disabled
    cy.getElementByDataTestsId('LoadTemplateButton').should('be.disabled');
    cy.getElementByDataTestsId(`row-${templateJobIdFromE2EFile}`).click();
    cy.getElementByDataTestsId('LoadTemplateButton').should('not.be.disabled');

    //filter by userId
    cy.get('.member-table-row').should('have.length', 2);
    cy.getElementByDataTestsId('filterByUserIdTestId').click();
    cy.get('.member-table-row').should('have.length', 1);


    // navigate to service modal -> should show Template button
    cy.getElementByDataTestsId('startFromScratchButton').click().then(()=>{
      cy.getElementByDataTestsId('templateButton').should('be.visible')
    });

  });

  it('clicking on load template button, go to expected url', function () {
    cy.getElementByDataTestsId('templateButton').contains('Template')
    .getElementByDataTestsId('templateButton').click({force: true}) // Open template Modal

    const serviceModelId = '5c9e863f-2716-467b-8799-4a67f378dcaa';
    const vidBaseUrl = `${Cypress.config().baseUrl}/serviceModels.htm`;

    cy.getElementByDataTestsId(`row-${templateJobIdFromE2EFile}`).click();
    cy.getElementByDataTestsId('LoadTemplateButton').click().setViewportToDefault();

    cy.location().should((loc) => {
      expect(loc.toString()).to.eq(`${vidBaseUrl}#/servicePlanning/RECREATE?serviceModelId=${serviceModelId}&jobId=${templateJobIdFromE2EFile}`);
    });
  });

});

