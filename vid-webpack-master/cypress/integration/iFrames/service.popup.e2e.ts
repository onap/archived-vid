///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
describe('Service popup', function () {
  describe('basic UI tests', () => {

    beforeEach(() => {
      cy.window().then((win) => {
        win.sessionStorage.clear();
        cy.setReduxState();
        cy.preventErrorsOnLoading();
        cy.initAAIMock();
        cy.initVidMock();
        cy.login();
      })
    });

    afterEach(() => {
      cy.screenshot();
    });

    it('a-la-carte service instantiation popup has all required fields ', function () {
      cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/emptyServiceRedux.json').then((res1) => {
        res1.service.serviceHierarchy["2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd"].service.instantiationType = 'A-LA-Carte';
        cy.setReduxState(<any>res1);
        cy.openIframe('/app/ui/#/servicePopup?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd&isCreate=true');
        cy.isElementContainsAttr('form-set', 'disabled');
        cy.get('label').contains('Instance name:').should('not.have.class', 'required')
          .get('label').contains('Subscriber name:').should('have.class', 'required')
          .get('label').contains('Service type:').should('have.class', 'required')
          .get('label').contains('Owning entity:').should('have.class', 'required')
          .get('label').contains('Project').should('not.have.class', 'required')
          .get('label').contains('Rollback on failure').should('have.class', 'required');
      });
    });

    it('a-la-carte service instantiation popup has Instance name as required', function () {
      cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/emptyServiceRedux.json').then((res1) => {
        res1.service.serviceHierarchy["2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd"].service.instantiationType = 'A-LA-Carte';
        let isEcompNaming = false;
        res1.service.serviceHierarchy["2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd"].service.serviceEcompNaming = isEcompNaming.toString();
        cy.setReduxState(<any>res1);
        cy.openIframe('/app/ui/#/servicePopup?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd&isCreate=true');
        cy.isElementContainsAttr('form-set', 'disabled');
        cy.get('label').contains('Instance name:').should('have.class', 'required');
      });
    });

    it('should contains basic selects with required astrix', function () {
      cy.openIframe('/app/ui/#/servicePopup?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd&isCreate=true');
      cy.isElementContainsAttr('form-set', 'disabled');
      cy.get('label').contains('Subscriber name:').should('have.class', 'required')
        .get('label').contains('Service type:').should('have.class', 'required')
        .get('label').contains('LCP region:').should('have.class', 'required')
        .get('label').contains('Tenant:').should('have.class', 'required')
        .get('label').contains('Owning entity:').should('have.class', 'required')
        .get('label').contains('Product family:').should('have.class', 'required')
        .get('label').contains('AIC zone:').should('not.have.class', 'required')
        .get('label').contains('Project').should('not.have.class', 'required')
        .get('label').contains('Rollback on failure').should('have.class', 'required');
    });

    it('should be able fill all selects', function () {
      cy.openIframe('/app/ui/#/servicePopup?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd&isCreate=true');

      cy.selectDropdownOptionByText('subscriberName', 'SILVIA ROBBINS');
      cy.selectDropdownOptionByText('serviceType', 'TYLER SILVIA');
      cy.selectDropdownOptionByText('productFamily', 'TYLER SILVIA');
      cy.selectDropdownOptionByText('lcpRegion', 'hvf6');
      cy.selectDropdownOptionByText('tenant', 'AIN Web Tool-15-D-testalexandria');
      cy.selectDropdownOptionByText('aic_zone', 'NFTJSSSS-NFT1');
      cy.selectDropdownOptionByText('project', 'WATKINS');
      cy.selectDropdownOptionByText('owningEntity', 'aaa1');
      cy.selectDropdownOptionByText('rollback', 'Rollback');

    });

    it('should display error when api return empty data', function () {
      cy.initCategoryParameter(<any>{});
      cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/emptyServiceRedux.json').then((res1) => {
        res1.service.categoryParameters.owningEntityList = [];
        cy.setReduxState(<any>res1);
        cy.openIframe('/app/ui/#/servicePopup?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd&isCreate=true');

        cy.get('.message').contains('No results for this request. Please change criteria.');
        cy.get('form-general-error').contains('Page contains errors. Please see details next to the relevant fields.');
      });
    });
  });
});

