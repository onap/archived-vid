describe('Create Instance Page : Pause after instantiation from vfModule Popup window', ()=> {

  beforeEach( () => {
    cy.clearSessionStorage();
    cy.setTestApiParamToGR();
    cy.initVidMock();
    cy.permissionVidMock();
    cy.login();
  });

  afterEach( () =>{
    cy.screenshot();
  });

  it('Pause after Instantiation : Create a vf module and and Pause from vfModule popup', () => {
    cy.readFile('cypress/support/jsonBuilders/mocks/jsons/flags.cypress.json').then((flags) => {
      cy.server()
      .route({
        method: 'GET',
        delay: 0,
        status: 200,
        url: Cypress.config('baseUrl') + "/flags**",
        response: flags
      }).as('initFlags');
    });

    cy.readFile('cypress/support/jsonBuilders/mocks/jsons/reduxModelOneVnfAndTwoVfmodulesInstanceOneVnf.json').then((reduxState) => {

      cy.setReduxState(<any>reduxState);
      cy.openIframe('app/ui/#/servicePlanning?serviceModelId=f3862254-8df2-4a0a-8137-0a9fe985860c');
      const vnfName = "vOCG_1804_VF 0";
      let vfModulesNames: Array<string> = [
        'vocg_1804_vf0..Vocg1804Vf..base_ocg..module-0',
        'vocg_1804_vf0..Vocg1804Vf..ocgapp_004..module-11'
      ];
      const uuidAndVfModuleNames: Array<string> = [
        '815db6e5-bdfd-4cb6-9575-82c36df8747a-vocg_1804_vf0..Vocg1804Vf..base_ocg..module-0',
        'da10c7fe-cf81-441c-9694-4e9ddf2054d8-vocg_1804_vf0..Vocg1804Vf..ocgapp_004..module-11'
      ];

      addALaCarteVfModuleEcompGeneratedNamingTrue(vnfName, vfModulesNames[0], uuidAndVfModuleNames[0]);

    });

  });

  function addALaCarteVfModuleEcompGeneratedNamingTrue(vnfName: string, vfModulesName: string, uuidAndVfModuleName: string): Chainable<any> {
    return cy.getElementByDataTestsId('node-' + vnfName).click({force: true}).then(() => {
      cy.getElementByDataTestsId('node-' + vfModulesName + '-add-btn').click({force: true}).then(() => {
        cy.getElementByDataTestsId('node-' + uuidAndVfModuleName + '-menu-btn')
        .click({force: true}).then(() => {
          cy.getElementByDataTestsId('context-menu-edit').click().then(() => {
            cy.getElementByDataTestsId('pauseInstantiation').click();
            cy.getElementByDataTestsId('form-set').click();
          })
        })
      })
    })
  }
})
