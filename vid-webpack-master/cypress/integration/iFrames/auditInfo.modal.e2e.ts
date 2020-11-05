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
              cy.get('#service-instantiation-audit-info-mso').should('be.visible')
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
                let instanceColumn :any = res[index]['instanceName'] + " | undefined";
              assert.equal(row.find('#msoRequestId').text().trim(), res[index]['requestId']);
              assert.equal(row.find('.msoInstanceName').text().trim(),instanceColumn );
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
          cy.get('#glossary_link').should('be.visible');
        });
      })
    });

    it('Refresh link should be visible and clicking refresh should fetch latest data', function () {
      cy.initAuditInfoMSOALaCarte(); 
      cy.openIframe('app/ui/#/instantiationStatus');
      cy.get('.instantiation-status-data tbody tr').each(function (row, index) {
        cy.get('.icon-menu').eq(index).click({force: true}).then(()=>{
          cy.getElementByDataTestsId('context-menu-audit-info').click({force:true}).then(()=>{
            cy.setViewportToSmallPopup();
            cy.get('#refreshButton').should('be.visible')
            cy.get('#refreshButton').click({force: true}).then(() => {
              cy.initAuditInfoMSOALaCarteNew();
            })
          })
  
          })
        });
      });

    it('Check if the table data is sorted in descending order by start time', function () {
        const expectedResult = getExpectedResult();
        cy.readFile('../vid-automation/src/test/resources/a-la-carte/auditInfoMSOALaCarteNew.json').then((res) => {
        cy.initAuditInfoMSOALaCarteNew(res);
        cy.openIframe('app/ui/#/instantiationStatus');
        cy.get('.icon-menu').eq(7).click({force:true}).then(() => {
          cy.getElementByDataTestsId('context-menu-audit-info').click({force:true}).then(() => {
            cy.setViewportToSmallPopup();
            cy.get('#service-instantiation-audit-info-mso thead tr th#instanceName').should("be.visible")
              .get('#service-instantiation-audit-info-mso tbody tr').each(function (row, index) {
                const instanceColumn :any = expectedResult[index]['instanceName'] + " | " +expectedResult[index]['instanceId'];
              assert.equal(row.find('#msoRequestId').text().trim(), expectedResult[index]['requestId']);
              assert.equal(row.find('.msoInstanceName').text().trim(), instanceColumn);
              assert.equal(row.find('#msostartTime').text().trim(), expectedResult[index]['startTime']);
            });
          });
        })
        });
      }); 

  });
});



function getExpectedResult() {
  return [
    {
      "requestId": "1fc2ef3b-26f0-4e62-a00a-6a31502d39e2",
      "instanceName": "zrdm54cfmgw01_sup_1",
      "instanceId":"5fd7eb77-34c6-4cb9-adf7-03297d85e7ed",
      "modelType": "vfModule",
      "instanceType": "createInstance",
      "startTime": "Mon, 24 Aug 2020 22:54:29 GMT",
      "finishTime": "Mon, 24 Aug 2020 22:56:35 GMT",
      "jobStatus": "ROLLED_BACK_TO_ASSIGNED",
      "additionalInfo": "<b>Source:</b> VID</br><b>StatusMessage:</b>STATUS: Error Source: OPENSTACK, Error Message: Received vfModuleException from VnfAdapter: category='INTERNAL' message='Exception during create VF 400 Bad Request: The server could not comply with the request since it is either malformed or otherwise incorrect., error.type=UserParameterMissing, error.message=The Parameter (VSFO_CP0_compute_node) was not provided.' rolledBack='true'</br><b>FlowStatus:</b> All Rollback flows have completed successfully</br><b>TestAPI:</b> GR_API</br><b>TenantId:</b> ad299b37da30413391e9c28138f0b0cd</br><b>TenantName:</b> FNCORE-30052-D-MC-RDM54c</br><b>CloudOwner:</b> att-nc</br>"
    },
    {
      "requestId": "a4e43d9e-4813-42e4-94bf-c5c6f22ed0bc",
      "instanceName": "zrdm54cfmgw01_base",
      "instanceId":"5fd7eb77-34c6-4cb9-adf7-03297d85e9cc",
      "modelType": "vfModule",
      "instanceType": "createInstance",
      "startTime": "Mon, 24 Aug 2020 22:44:42 GMT",
      "finishTime": "Mon, 24 Aug 2020 22:54:17 GMT",
      "jobStatus": "COMPLETE",
      "additionalInfo": "<b>Source:</b> VID</br><b>StatusMessage:</b>STATUS: ALaCarte-VfModule-createInstance request was executed correctly.</br><b>FlowStatus:</b> Successfully completed all Building Blocks</br><b>TestAPI:</b> GR_API</br><b>TenantId:</b> ad299b37da30413391e9c28138f0b0cd</br><b>TenantName:</b> FNCORE-30052-D-MC-RDM54c</br><b>CloudOwner:</b> att-nc</br>"
    },
    {
      "requestId": "f1aa7175-c237-4b56-ba64-7cb728a38ff2",
      "instanceName": "zrdm54cfmgw01",
      "instanceId":"f5c72b2c-8e32-43db-9c42-f2b7901c69d8",
      "modelType": "vnf",
      "instanceType": "createInstance",
      "startTime": "Mon, 24 Aug 2020 22:38:18 GMT",
      "finishTime": "Mon, 24 Aug 2020 22:44:24 GMT",
      "jobStatus": "COMPLETE",
      "additionalInfo": "<b>Source:</b> VID</br><b>StatusMessage:</b>STATUS: ALaCarte-Vnf-createInstance request was executed correctly.</br><b>FlowStatus:</b> Successfully completed all Building Blocks</br><b>TestAPI:</b> GR_API</br><b>TenantId:</b> ad299b37da30413391e9c28138f0b0cd</br><b>TenantName:</b> FNCORE-30052-D-MC-RDM54c</br><b>CloudOwner:</b> att-nc</br><b>PlatformName:</b> FIRSTNET-DEDICATED,NETWORK-CLOUD</br><b>LineOfBusiness:</b> FIRSTNET</br>"
    },
    {
      "requestId": "7ba7900c-3e51-4d87-b1b4-3c53bdfaaa7d",
      "instanceName": "zrdm54cfmgw01_svc",
      "instanceId":"de46c407-9f9b-4f2f-b1c3-be1c6599b957",
      "modelType": "service",
      "instanceType": "createInstance",
      "startTime": "Mon, 24 Aug 2020 22:37:53 GMT",
      "finishTime": "Mon, 24 Aug 2020 22:38:10 GMT",
      "jobStatus": "COMPLETE",
      "additionalInfo": "<b>Source:</b> VID</br><b>StatusMessage:</b>STATUS: ALaCarte-Service-createInstance request was executed correctly.</br><b>FlowStatus:</b> Successfully completed all Building Blocks</br><b>SubscriptionServiceType:</b> FIRSTNET</br><b>Alacarte:</b> true</br><b>TestAPI:</b> GR_API</br><b>ProjectName: FIRSTNET</br><b>OwningEntityId:</b> 10c645f5-9924-4b89-bec0-b17cf49d3cad</br><b>OwningEntityName:</b> MOBILITY-CORE</br>"
    }
  ];
}
