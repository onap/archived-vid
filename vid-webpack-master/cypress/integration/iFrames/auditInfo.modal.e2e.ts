///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
import { JsonBuilder } from '../../support/jsonBuilders/jsonBuilder';
import { AsyncInstantiationModel } from '../../support/jsonBuilders/models/asyncInstantiation.model';

describe('Audit information modal', function () {
  describe('basic UI tests', () => {
    var jsonBuilderInstantiationBuilder : JsonBuilder<AsyncInstantiationModel> = new JsonBuilder<AsyncInstantiationModel>();
    beforeEach(() => {
      cy.window().then((win) => {
        win.sessionStorage.clear();
        cy.setReduxState();
        cy.preventErrorsOnLoading();
        jsonBuilderInstantiationBuilder.basicMock('/cypress/support/jsonBuilders/mocks/jsons/asyncInstantiation.json',
          Cypress.config('baseUrl') + "/asyncInstantiation**");
        cy.initAAIMock();
        cy.initVidMock();
        cy.login();
      })
    });

    it(`should display 2 tables with information's`, function () {

        cy.openIframe('app/ui/#/instantiationStatus');
        cy.get('.instantiation-status-data tbody tr').each(function (row, index) {
          cy.get('.icon-menu').eq(index).click()
             .get('.audit-icon').click()
            .get('#service-model-name').should('contain', row.find('#serviceModelName').text().trim())
          .getElementByDataTestsId('model-item-value-userId').should('contain', row.find('#userId').text().trim())
          .get('#service-instantiation-audit-info-vid').should('be.visible')
          .get('#service-instantiation-audit-info-vid').find('#vidJobStatus').should('be.visible')
          .get('#service-instantiation-audit-info-mso').should('be.visible')
          .get('#service-instantiation-audit-info-mso').find('#msoJobStatus').should('be.visible')
          .get('#cancelButton').click();
        });
    });
  });
});
