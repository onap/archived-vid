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

    it('should contains basic selects with required astrix', function () {
      cy.openIframe('/app/ui/#/servicePopup?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd&isCreate=true');
      cy.isElementContainsAttr('service-form-set', 'disabled');
      cy.get('label').contains('Subscriber name:').should('have.class', 'required')
        .get('label').contains('Service type:').should('have.class', 'required')
        .get('label').contains('LCP region:').should('have.class', 'required')
        .get('label').contains('Tenant:').should('have.class', 'required')
        .get('label').contains('Owning entity:').should('have.class', 'required')
        .get('label').contains('Product family:').should('have.class', 'required')
        .get('label').contains('AIC Zone:').should('not.have.class', 'required')
        .get('label').contains('Project').should('not.have.class', 'required')
        .get('label').contains('Rollback On Failure').should('have.class', 'required');
    });

    it('should be able fill all selects', function () {
      cy.openIframe('/app/ui/#/servicePopup?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd&isCreate=true');

      cy.selectDropdownOptionByText('subscriberName', 'USP VOICE');
      cy.selectDropdownOptionByText('serviceType', 'VIRTUAL USP');
      cy.selectDropdownOptionByText('productFamily', 'VIRTUAL USP');
      cy.selectDropdownOptionByText('lcpRegion', 'mtn6');
      cy.selectDropdownOptionByText('tenant', 'AIN Web Tool-15-D-testgamma');
      cy.selectDropdownOptionByText('aic_zone', 'NFTJSSSS-NFT1');
      cy.selectDropdownOptionByText('project', 'DFW');
      cy.selectDropdownOptionByText('owningEntity', 'aaa1');
      cy.selectDropdownOptionByText('rollback', 'Rollback');

    });

    it('should display error when api return empty data', function () {
      cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/categoryParametres.json').then((res)=>{
        res.categoryParameters.owningEntity = [];
        cy.initCategoryParameter(<any>res);

        cy.openIframe('/app/ui/#/servicePopup?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd&isCreate=true');

        cy.get('.message').contains('No results for this request. Please change criteria.');
        cy.get('form-general-error').contains('Page contains errors. Please see details next to the relevant fields.');
      });
    });
  });
});
