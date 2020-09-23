import {JsonBuilder} from "../../support/jsonBuilders/jsonBuilder";
import {AsyncInstantiationModel} from "../../support/jsonBuilders/models/asyncInstantiation.model";
import {ServiceModel} from "../../support/jsonBuilders/models/service.model";

describe('Audit Information model', function () {
  describe('Export MSO status', function (){
    var jsonBuilderInstantiationBuilder: JsonBuilder<AsyncInstantiationModel> = new JsonBuilder<AsyncInstantiationModel>();
    var jsonBuilderAndMock: JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();
    beforeEach(() => {
      cy.clearSessionStorage();
      cy.setReduxState();
      cy.preventErrorsOnLoading();
      jsonBuilderInstantiationBuilder.basicMock('cypress/support/jsonBuilders/mocks/jsons/asyncInstantiation.json',
        Cypress.config('baseUrl') + "/asyncInstantiation**");
      cy.initAAIMock();
      cy.initVidMock();
      cy.initAsyncInstantiation();
      cy.login();
    });

    afterEach(() => {
      cy.screenshot();
    });

    it(`should display 2 tables with information's`, function () {
      cy.initAuditInfoMSOALaCarte();
      cy.openIframe('app/ui/#/instantiationStatus');

      cy.get('#b1ff271b-829a-43f9-a2e3-23987a34f261 > #jobStatus > .menu-div > .icon-menu').click({force: true}).then(() =>{
        cy.getElementByDataTestsId('context-menu-audit-info').click();
        cy.wait(1000);
        cy.getElementByDataTestsId('export-button').click();
      });


    });
  })
})
