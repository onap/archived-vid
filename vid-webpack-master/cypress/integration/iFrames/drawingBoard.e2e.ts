///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
describe('Drawing board', function () {
  describe('basic UI tests', () => {

    beforeEach(() => {
      cy.window().then((win) => {
        win.sessionStorage.clear();
        cy.setReduxState();
        cy.preventErrorsOnLoading();
        cy.initAAIMock();
        cy.initVidMock();
        cy.login();
      });
    });


    it('should display service model name', function () {
      cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/emptyServiceRedux.json').then((res) => {
        cy.setReduxState(<any>res);
        cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
        cy.get('#service-model-name').contains('action-data');
      });
    });

    it('should display icon and message if no vnf and vnfModules', function () {
      cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/emptyServiceRedux.json').then((res) => {
        cy.setReduxState(<any>res);
        cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');

        cy.get('#not-node-img-id').and('be.visible');
      });
    });

    it('should show alert on remove vnf with modules', function () {
      cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/serviceWithVnfAndVfModules.json').then((res) => {
        cy.setReduxState(<any>res);
        cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
        cy.getElementByDataTestsId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_ADIOD-vPE 0-menu-btn')
          .click({force: true});
        // assert vfModules are enabled
        cy.get('.tree-node-disabled div[data-tests-id="node-2017488_adiodvpe0..2017488AdiodVpe..ADIOD_vRE_BV..module-1"]')
          .should('not.be.visible');
        cy.get('.icon-trash').click();
        cy.get('.title').contains('Remove VNF');
        cy.get('.sdc-button').contains('Remove VNF').click();
        // assert vfModules are disabled after remove parent vnf
        cy.get('.tree-node-disabled div[data-tests-id="node-2017488_adiodvpe0..2017488AdiodVpe..ADIOD_vRE_BV..module-1"]')
          .should('be.visible');
      });
    });

    it('should not show alert on remove vnf without modules', function () {
      cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/serviceWithVnfAndVfModules.json').then((res) => {
        cy.setReduxState(<any>res);
        cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
        cy.getElementByDataTestsId('node-0903e1c0-8e03-4936-b5c2-260653b96413-2017-388_ADIOD-vPE 1-menu-btn')
          .click({force: true});
        cy.get('.icon-trash').click();
      });
    });

    it('should show <Automatically Assigned> if ecomp is true', function () {
      cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/emptyServiceRedux.json').then((res) => {
        cy.setReduxState(<any>res);
        cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
        cy.getElementByDataTestsId('node-2017-488_ADIOD-vPE 0-add-btn').click({force: true});

        cy.selectDropdownOptionByText('productFamily', 'DHV');
        cy.selectDropdownOptionByText('lcpRegion', 'mtn6');
        cy.selectDropdownOptionByText('tenant', 'AIN Web Tool-15-D-testgamma');
        cy.selectDropdownOptionByText('lineOfBusiness', 'ECOMP');
        cy.selectDropdownOptionByText('platform', 'platform');

        cy.getElementByDataTestsId('vnf-form-set').click({force: true});

        cy.getElementByDataTestsId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_ADIOD-vPE 0').contains('<Automatically Assigned>');
      });
    });

    it('should show model nameif ecomp is false', function () {
      const vnfModelName: string = '2017-488_ADIOD-vPE 0';
      cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/emptyServiceRedux.json').then((res) => {
        res.service.serviceHierarchy['2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd'].vnfs[vnfModelName].properties.ecomp_generated_naming = 'false';
        cy.setReduxState(<any>res);
        cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
        cy.getElementByDataTestsId('node-2017-488_ADIOD-vPE 0-add-btn').click({force: true});

        cy.selectDropdownOptionByText('productFamily', 'DHV');
        cy.selectDropdownOptionByText('lcpRegion', 'mtn6');
        cy.selectDropdownOptionByText('tenant', 'AIN Web Tool-15-D-testgamma');
        cy.selectDropdownOptionByText('lineOfBusiness', 'ECOMP');
        cy.selectDropdownOptionByText('platform', 'platform');

        cy.getElementByDataTestsId('vnf-form-set').click({force: true});

        cy.getElementByDataTestsId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_ADIOD-vPE 0').contains(vnfModelName);
      });
    });

    // describe('add instance open a popup', () => {
    //
    //   it('shouldn add vfModule without popup with no empty required fields', function () {
    //     cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/serviceWithVnfAndVfModules.json').then((res) => {
    //       res.service.serviceInstance["2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd"].vnfs["2017-488_ADIOD-vPE 0"].vfModules = [];
    //       cy.setReduxState(<any>res);
    //       cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
    //       cy.get('available-models-tree tree-node-expander').eq(2).click();
    //       cy.getElementByDataTestsId('node-2017488_adiodvpe0..2017488AdiodVpe..ADIOD_vRE_BV..module-1-add-btn').click();
    //       cy.get('drawing-board-tree .toggle-children').click();
    //       cy.getElementByDataTestsId('node-25284168-24bb-4698-8cb4-3f509146eca5-2017488_adiodvpe0..2017488AdiodVpe..ADIOD_vRE_BV..module-1')
    //         .and('be.visible');
    //     });
    //   });
    //
    //   it('should add vfModule with popup if empty required dynamic input', function () {
    //     cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/serviceWithVnfAndVfModules.json').then((res) => {
    //       res.service.serviceHierarchy["2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd"].vnfs["2017-488_ADIOD-vPE 0"].vfModules["2017488_adiodvpe0..2017488AdiodVpe..ADIOD_vRE_BV..module-1"].inputs["adiodvpe0_bandwidth"].default = '';
    //       res.service.serviceInstance["2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd"].vnfs["2017-488_ADIOD-vPE 0"].vfModules = [];
    //       cy.setReduxState(<any>res);
    //       cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
    //       cy.get('available-models-tree tree-node-expander').eq(2).click();
    //       cy.getElementByDataTestsId('node-2017488_adiodvpe0..2017488AdiodVpe..ADIOD_vRE_BV..module-1-add-btn').click();
    //       cy.get('#instance-popup').and('be.visible');
    //     });
    //
    //   });
    //
    // });
  });
});
