///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
import {ServiceModel} from '../../support/jsonBuilders/models/service.model';
import {JsonBuilder} from '../../support/jsonBuilders/jsonBuilder';

describe('Permissions ',  ()=> {
  var jsonBuilderAndMock: JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();

  beforeEach(() => {
    cy.window().then((win) => {
      win.sessionStorage.clear();
      cy.preventErrorsOnLoading();
      cy.initAAIMock();
      cy.initVidMock();
      cy.login();


    });
  });

  afterEach(() => {
    cy.screenshot();
  });

  it('user without permission get mode-view : is not permitted', () => {
    const SUBSCRIBER_ID: string = "e433710f-9217-458d-a79d-1c7aff376d89";
    const SERVICE_TYPE: string = "TYLER SILVIA";
    const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
    const SERVICE_MODEL_ID: string = '6b528779-44a3-4472-bdff-9cd15ec93450';

    cy.initDrawingBoardUserPermission(<any>{isEditPermitted : false});

    cy.readFile('cypress/support/jsonBuilders/mocks/jsons/serviceModels/ecompNamingFalseModel.json').then((res) => {
      jsonBuilderAndMock.basicJson(
        res,
        Cypress.config('baseUrl') + "/rest/models/services/6b528779-44a3-4472-bdff-9cd15ec93450",
        200,
        0,
        "ecompNamingFalseModel",
      )
    });

    cy.readFile('../vid-automation/src/test/resources/VnfGroup/serviceWithVnfGroping_serviceInstance.json').then((res) => {
      jsonBuilderAndMock.basicJson(
        res,
        Cypress.config('baseUrl') + `**/aai_get_service_instance_topology/**`,
        200, 0,
        "serviceWithVnfGroping_serviceInstance",
      )
    });

    cy.openIframe(`app/ui/#/servicePlanning/EDIT?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}`);

      cy.url().should('contains', 'VIEW')
    });

  it('user without permission get mode-view : is permitted', () => {
    const SUBSCRIBER_ID: string = "e433710f-9217-458d-a79d-1c7aff376d89";
    const SERVICE_TYPE: string = "TYLER SILVIA";
    const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
    const SERVICE_MODEL_ID: string = '6b528779-44a3-4472-bdff-9cd15ec93450';

    cy.initDrawingBoardUserPermission();

    cy.readFile('cypress/support/jsonBuilders/mocks/jsons/serviceModels/ecompNamingFalseModel.json').then((res) => {
      jsonBuilderAndMock.basicJson(
        res,
        Cypress.config('baseUrl') + "/rest/models/services/6b528779-44a3-4472-bdff-9cd15ec93450",
        200,
        0,
        "ecompNamingFalseModel",
      )
    });

    cy.readFile('../vid-automation/src/test/resources/aaiGetInstanceTopology/getServiceInstanceTopologyResult.json').then((res) => {
      jsonBuilderAndMock.basicJson(
        res,
        Cypress.config('baseUrl') + "/aai_get_service_instance_topology/e433710f-9217-458d-a79d-1c7aff376d89/TYLER SILVIA/f8791436-8d55-4fde-b4d5-72dd2cf13cfb",
        200, 0,
        "initServiceInstanceTopology",
      )
    });
    cy.openIframe(`app/ui/#/servicePlanning/EDIT?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}`);

    cy.url().should('contains', 'EDIT');
    cy.getElementByDataTestsId('deployBtn').contains('UPDATE');
    cy.getElementByDataTestsId('isViewOnly-status-test').contains('IN EDITING');
  });

  it(`RETRY - drawing board mode - no permission should show retry view only mode`,  () =>{
    const SUBSCRIBER_ID: string = "e433710f-9217-458d-a79d-1c7aff376d89";
    const SERVICE_TYPE: string = "TYLER SILVIA";
    const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
    const SERVICE_MODEL_ID: string = '6b528779-44a3-4472-bdff-9cd15ec93450';
    const JOB_ID: string = '123456-44a3-4472-bdff-9cd15ec12345';
    cy.initDrawingBoardUserPermission(<any>{isEditPermitted : false});
    cy.readFile('../vid-automation/src/test/resources/aaiGetInstanceTopology/getServiceInstanceTopologyResult.json').then((res) => {

      // Adding VNF with isFailed.
      res.vnfs["2017-388_PASQUALE-vPE 0"].isFailed = true;
      res.vnfs["2017-488_PASQUALE-vPE 0"].isFailed = true;

      // Adding VFModule with isFailed.
      res.vnfs["2017-488_PASQUALE-vPE 0"].vfModules["2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0"]["2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0uvfot"].isFailed = true;

      jsonBuilderAndMock.basicJson(
        res,
        Cypress.config('baseUrl') + "/asyncInstantiation/bulkForRetry/" + JOB_ID,
        200, 0,
        "initServiceInstanceRetryTopology",
      );
      cy.openIframe(`app/ui/#/servicePlanning/RETRY_EDIT?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}&jobId=${JOB_ID}`);

      cy.url().should('contains', '/RETRY?')
    });
  });
});
