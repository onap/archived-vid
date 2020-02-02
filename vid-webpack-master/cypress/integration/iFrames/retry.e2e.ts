///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
/// <reference types="Cypress" />
import {JsonBuilder} from '../../support/jsonBuilders/jsonBuilder';
import {ServiceModel} from '../../support/jsonBuilders/models/service.model';

describe('Retry Page', function () {
  let jsonBuilderAndMock: JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();
  beforeEach(() => {
      cy.clearSessionStorage();
      cy.preventErrorsOnLoading();
      cy.initAAIMock();
      jsonBuilderAndMock.basicMock('cypress/support/jsonBuilders/mocks/jsons/serviceModels/ecompNamingFalseModel.json',
        Cypress.config('baseUrl') + "/rest/models/services/6b528779-44a3-4472-bdff-9cd15ec93450");
      cy.initVidMock();
      cy.initZones();
      cy.permissionVidMock();
      cy.login();
  });

  afterEach(() => {
    cy.screenshot();
  });

  it(`RETRY - drawing board mode - should show correct failed icon with children + Retry button / Mock Data`,  ()=> {
    const SUBSCRIBER_ID: string = "e433710f-9217-458d-a79d-1c7aff376d89";
    const SERVICE_TYPE: string = "TYLER SILVIA";
    const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
    const SERVICE_MODEL_ID: string = '6b528779-44a3-4472-bdff-9cd15ec93450';
    const JOB_ID: string = '123456-44a3-4472-bdff-9cd15ec12345';
    cy.readFile('../vid-automation/src/test/resources/aaiGetInstanceTopology/getServiceInstanceTopologyResult.json').then((res) => {
      // Service with isFailed.
      res.isFailed= true;
      res.action = 'Create';
      res.statusMessage = 'Service instantiation has failed.'
      // Adding VNF with isFailed.
      res.vnfs["2017-388_PASQUALE-vPE 0"].isFailed = true;
      res.vnfs["2017-488_PASQUALE-vPE 0"].isFailed = true;
      res.vnfs["2017-488_PASQUALE-vPE 0"].statusMessage = 'VNF instantiation failed message';
      res.vnfs["2017-388_PASQUALE-vPE 0"].action = 'Create';
      res.vnfs["2017-488_PASQUALE-vPE 0"].action = 'Create';
      res.networks["ExtVL 0"].action = 'Create';
      res.networks["ExtVL 0"].isFailed = true;
      res.networks["ExtVL 0"].statusMessage = 'Network instantiation failed message';

      // Adding VFModule with isFailed.
      res.vnfs["2017-488_PASQUALE-vPE 0"].vfModules["2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0"]["2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0uvfot"].isFailed = true;
      res.vnfs["2017-488_PASQUALE-vPE 0"].vfModules["2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0"]["2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0uvfot"].statusMessage = 'Short vfModule Failure Message';
      res.vnfs["2017-488_PASQUALE-vPE 0"].vfModules["2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0"]["2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0uvfot"].action = 'Create';

      jsonBuilderAndMock.basicJson(
        res,
        Cypress.config('baseUrl') + "/asyncInstantiation/bulkForRetry/" + JOB_ID,
        200, 0,
        "initServiceInstanceRetryTopology",
      )
    });

    cy.openIframe(`app/ui/#/servicePlanning/RETRY_EDIT?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}&jobId=${JOB_ID}`);

    cy.getElementByDataTestsId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_PASQUALE-vPE 0').eq(0).click();

    cy.get('.failed-msg').should('have.length', 5);
    cy.get('.newIcon').should('have.length', 4);

    //cy.drawingBoardNumberOfExistingElementsShouldContains(4);
    cy.get('.toggle-children-wrapper.toggle-children-wrapper-expanded').eq(0).click().then(()=>{
      cy.get('.failed-msg').should('have.length', 5);
      cy.get('.newIcon').should('have.length', 4);
    });

    cy.drawingBoardTreeOpenContextMenuByElementDataTestId('node-afacccf6-397d-45d6-b5ae-94c39734b168-2017-388_PASQUALE-vPE 0')
      .drawingBoardTreeClickOnContextMenuOptionByName('Edit')
      .getElementByDataTestsId('cancelButton').click({force: true});
    cy.drawingBoardTreeOpenContextMenuByElementDataTestId('node-afacccf6-397d-45d6-b5ae-94c39734b168-2017-388_PASQUALE-vPE 0')
      .drawingBoardTreeClickOnContextMenuOptionByName('Remove');

    cy.get('.newIcon').should('have.length', 2);
    cy.getElementByDataTestsId('isViewOnly-status-test').contains('IN EDITING');

    cy.getElementByDataTestsId("openMenuBtn").click({force: true})
      .getElementByDataTestsId("context-menu-header-edit-item").click({force: true})
      .getElementByDataTestsId("serviceName").should('have.text','mCaNkinstancename')
      .getElementByDataTestsId("subscriberName")
      .getElementByDataTestsId("serviceType")
      .getElementByDataTestsId("owningEntity")
      .getElementByDataTestsId("project")
      .getElementByDataTestsId("rollback")
      .getElementByDataTestsId('cancelButton').click({force: true});
    cy.getElementByDataTestsId("openMenuBtn").click({force: true})
      .getElementByDataTestsId("context-menu-header-audit-item");
    // button should be RETRY
    cy.getElementByDataTestsId('deployBtn').should('contain', 'REDEPLOY');
    cy.getElementByDataTestsId('deployBtn').should('not.have.attr', 'disabled');

    cy.checkPopoverContentOnMouseEvent('service-failed-msg',  '.popover-content.popover-body','mouseenter', 0)
      .should('contain', 'Service instantiation has failed');
  });

  it(`RETRY - view mode- should show failed icon with no actions enabled`, ()=> {
    const SUBSCRIBER_ID: string = "e433710f-9217-458d-a79d-1c7aff376d89";
    const SERVICE_TYPE: string = "TYLER SILVIA";
    const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
    const SERVICE_MODEL_ID: string = '6b528779-44a3-4472-bdff-9cd15ec93450';
    const JOB_ID: string = '123456-44a3-4472-bdff-9cd15ec12345';
    cy.readFile('../vid-automation/src/test/resources/aaiGetInstanceTopology/getServiceInstanceTopologyResult.json').then((res) => {

      // Adding VNF with isFailed.
      res.vnfs["2017-388_PASQUALE-vPE 0"].isFailed = true;
      res.vnfs["2017-488_PASQUALE-vPE 0"].isFailed = true;
      res.vnfs["2017-388_PASQUALE-vPE 0"].action = 'Create';
      res.vnfs["2017-488_PASQUALE-vPE 0"].action = 'Create';
      res.vnfs["2017-488_PASQUALE-vPE 0"].statusMessage = 'Very long message that checks the popoverwindow can show very very long messagewithout problem.as-erfderfd-rfghthth-yjyjyj-ukuk. For more details go to audit show window';
      // Adding VFModule with isFailed.
      res.vnfs["2017-488_PASQUALE-vPE 0"].vfModules["2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0"]["2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0uvfot"].isFailed = true;
      res.vnfs["2017-488_PASQUALE-vPE 0"].vfModules["2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0"]["2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0uvfot"].action = 'Create';
      res.networks["ExtVL 0"].isFailed = true;

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/responceForFailedInstance.json').then((res) => {
        jsonBuilderAndMock.basicJson(
          res,
          Cypress.config('baseUrl') + "/asyncInstantiation/auditStatusForRetry**",
          200,
          0,
          "msoStatusForFailedInstance",
        )
      });

      jsonBuilderAndMock.basicJson(
        res,
        Cypress.config('baseUrl') + "/asyncInstantiation/bulkForRetry/" + JOB_ID,
        200, 0,
        "initServiceInstanceRetryTopology",
      )
    });

    cy.openIframe(`app/ui/#/servicePlanning/RETRY?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}&jobId=${JOB_ID}`);

    cy.getElementByDataTestsId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_PASQUALE-vPE 0').eq(0).click();

    cy.get('.failed-msg').should('have.length', 4);
    cy.get('.newIcon').should('have.length', 4);

    cy.get('.toggle-children-wrapper.toggle-children-wrapper-expanded').eq(0).click().then(()=>{
      cy.get('.failed-msg').should('have.length', 4);
      cy.get('.newIcon').should('have.length', 4);
    });
    cy.assertMenuItemsForNode(['showAuditInfo'],'node-afacccf6-397d-45d6-b5ae-94c39734b168-2017-388_PASQUALE-vPE 0-menu-btn');
    cy.getElementByDataTestsId('isViewOnly-status-test').contains('VIEW ONLY');

    // deploy button should be 'REDEPLOY' and disabled.
    cy.getElementByDataTestsId('editBtn').should('contain', 'REDEPLOY').should('have.attr', 'disabled');

    cy.checkPopoverContentOnMouseEvent('failed-error-message',  '.popover-content.popover-body','mouseenter', 0)
      .should('not.be.visible');
    cy.checkPopoverContentOnMouseEvent('failed-error-message',  '.popover-content.popover-body','mouseenter', 1)
      .should('contain', 'Very long');

  });

  it(`RETRY - drawing board mode - should show correct failed icon + Retry button`,  ()=> {
    const SUBSCRIBER_ID: string = "e433710f-9217-458d-a79d-1c7aff376d89";
    const SERVICE_TYPE: string = "TYLER SILVIA";
    const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
    const SERVICE_MODEL_ID: string = '6b528779-44a3-4472-bdff-9cd15ec93450';
    const JOB_ID: string = '123456-44a3-4472-bdff-9cd15ec12345';
    let expectedResult: JSON;

    cy.readFile('../vid-automation/src/test/resources/asyncInstantiation/ServiceTreeForRetry_serviceInstance.json').then((res) => {

      jsonBuilderAndMock.basicJson(
        res,
        Cypress.config('baseUrl') + "/asyncInstantiation/bulkForRetry/" + JOB_ID,
        200, 0,
        "initServiceInstanceRetryTopology",
      )
    });

    //TODO - join this to correct API test



    cy.openIframe(`app/ui/#/servicePlanning/RETRY_EDIT?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}&jobId=${JOB_ID}`);

    cy.get('.failed-msg').should('have.length', 1);
    cy.get('.newIcon').should('have.length', 1);
    //TODO

    cy.getElementByDataTestsId('deployBtn').should('not.have.attr', 'disabled');

    // button should be RETRY
    cy.getElementByDataTestsId('deployBtn').should('contain', 'REDEPLOY').click();


    //TODO - join this to correct API test


  });

  it('RETRY- edit mode- failed service with vnf', ()=>{
    const SUBSCRIBER_ID: string = "e433710f-9217-458d-a79d-1c7aff376d89";
    const SERVICE_TYPE: string = "TYLER SILVIA";
    const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
    const SERVICE_MODEL_ID: string = '6b528779-44a3-4472-bdff-9cd15ec93450';
    const JOB_ID: string = '123456-44a3-4472-bdff-9cd15ec12345';
    const TRACK_BY_ID = '14561234';
    let expectedResult: JSON;
    cy.readFile('../vid-automation/src/test/resources/asyncInstantiation/ServiceWithFailedServiceInstance.json').then((res) => {

      cy.readFile('../vid-automation/src/test/resources/asyncInstantiation/auditModalFailedServiceInstance.json').then((res) => {
        jsonBuilderAndMock.basicJson(
          res,
          Cypress.config('baseUrl') + "/asyncInstantiation/auditStatusForRetry/TRACK_BY_ID",
          200,
          0,
          "msoStatusForFailedInstance",
        )
      });

      jsonBuilderAndMock.basicJson(
        res,
        Cypress.config('baseUrl') + "/asyncInstantiation/bulkForRetry/" + JOB_ID,
        200, 0,
        "initFAiledServiceInstanceRetryTopology",
      )
    });

    cy.openIframe(`app/ui/#/servicePlanning/RETRY_EDIT?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}&jobId=${JOB_ID}`);
    cy.get('.newIcon').should('have.length', 1);
    cy.getElementByDataTestsId('isViewOnly-status-test').contains('IN EDITING');
    cy.getElementByDataTestsId("openMenuBtn").click({force: true});
    cy.getElementByDataTestsId("context-menu-header-edit-item").click({force: true})
      .getElementByDataTestsId("serviceName").should('have.text','INSTANCE_NAME')
      .getElementByDataTestsId("subscriberName")
      .getElementByDataTestsId("serviceType")
      .getElementByDataTestsId("owningEntity")
      .getElementByDataTestsId("project")
      .getElementByDataTestsId("rollback")
      .getElementByDataTestsId('cancelButton').click({force: true});
    cy.getElementByDataTestsId("openMenuBtn").click({force: true})
      .getElementByDataTestsId("context-menu-header-audit-item").click({force: true});
    cy.getElementByDataTestsId('requestId').should('contain', 'e5f93320-cce6-424d-adc6-259a4ee8b342');
    cy.getElementByDataTestsId('jobStatus').should('contain', 'Failed');
    cy.getElementByDataTestsId('additionalInfo').should('contain', 'The service instantiation is failed');
    cy.getElementByDataTestsId('close-button').click({force: true});
    // // button should be RETRY
    cy.getElementByDataTestsId('deployBtn').should('contain', 'REDEPLOY');
    cy.getElementByDataTestsId('deployBtn').should('not.have.attr', 'disabled');
    cy.get('.failed-msg').should('have.length', 1);
    cy.get('.newIcon').should('have.length', 1);

    cy.checkPopoverContentOnMouseEvent('service-failed-msg',  '.popover-content.popover-body','mouseenter', 0)
      .should('contain', 'The service instantiation is failed');
    });
});
