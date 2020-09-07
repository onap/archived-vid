///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
import {JsonBuilder} from '../../support/jsonBuilders/jsonBuilder';
import {AsyncInstantiationModel} from '../../support/jsonBuilders/models/asyncInstantiation.model';
import * as _ from 'lodash';
import {ServiceModel} from "../../support/jsonBuilders/models/service.model";

describe('Audit information modal', function () {
  describe('basic UI tests', () => {
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
      cy.get('.instantiation-status-data tbody tr').each(function (row, index) {
        cy.get('.icon-menu').eq(index).click({force: true}).then(()=>{
          cy.getElementByDataTestsId('context-menu-audit-info').click({force:true}).then(()=>{
            cy.setViewportToSmallPopup();
            cy.get('#service-model-name').should('contain', row.find('#serviceModelName').text().trim())
              .getElementByDataTestsId('model-item-value-userId').should('contain', row.find('#userId').text().trim())
              .get('#service-instantiation-audit-info-vid').should('be.visible')
              .get('#service-instantiation-audit-info-vid').find('#vidJobStatus').should('be.visible')
              .get('#service-instantiation-audit-info-mso').should('be.visible')
              .get('#service-instantiation-audit-info-mso').find('#msoJobStatus').should('be.visible')
              .get('#cancelButton').click({force: true})
              .setViewportToDefault();
          })
        });
      });
    });

    it("shouldn't show instance name in mso table on macro service", function () {
      cy.openIframe('app/ui/#/instantiationStatus');
      cy.get('.icon-menu').eq(0).click({force:true}).then(() => {
        cy.getElementByDataTestsId('context-menu-audit-info').click({force:true}).then(() => {
          cy.setViewportToSmallPopup();
          cy.get('#service-instantiation-audit-info-mso thead tr th#instanceName').should("not.be.visible")
            .get('#service-instantiation-audit-info-mso tbody tr td.msoInstanceName').should("not.be.visible");
        })
      })
    });

    it('should show instance name in mso table on a la carte service', function () {
      cy.readFile('../vid-automation/src/test/resources/a-la-carte/auditInfoMSOALaCarte.json').then((res) => {
        cy.initAuditInfoMSOALaCarte(res);
        cy.openIframe('app/ui/#/instantiationStatus');
        cy.get('.icon-menu').eq(7).click({force:true}).then(() => {
          cy.getElementByDataTestsId('context-menu-audit-info').click({force:true}).then(() => {
            cy.setViewportToSmallPopup();
            cy.get('#service-instantiation-audit-info-mso thead tr th#instanceName').should("be.visible")
              .get('#service-instantiation-audit-info-mso tbody tr').each(function (row, index) {
              assert.equal(row.find('.request-id').text().trim(), res[index]['requestId']);
              assert.equal(row.find('.msoInstanceName').text().trim(), 'service: ' + res[index]['instanceName']);
              assert.equal(row.find('#msoJobStatus').text().trim(), _.capitalize(res[index]['jobStatus']));
              assert.equal(row.find('#msoAdditionalInfo span').text().trim(), res[index]['additionalInfo']);
            });
          });
        })
      })
    });

    it('glossary should be visible', function () {
      cy.openIframe('app/ui/#/instantiationStatus');
      cy.get('.icon-menu').eq(7).click().then(() => {
        cy.getElementByDataTestsId('context-menu-audit-info').click().then(() => {
          cy.setViewportToSmallPopup();
          cy.getElementByDataTestsId('hide-model-info').click().then(() => {
            cy.get('#model-info').should('not.be.visible');
          });
        });
      })
    });

    it('should hide modelInformationItems', function () {
      cy.openIframe('app/ui/#/instantiationStatus');
      cy.get('.icon-menu').eq(7).click().then(() => {

      });
    });

  });
});
