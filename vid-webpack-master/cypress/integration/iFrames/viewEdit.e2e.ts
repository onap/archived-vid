///<reference path="../../../node_modules/cypress/types/index.d.ts"/> / <reference types="Cypress" />
import {JsonBuilder} from '../../support/jsonBuilders/jsonBuilder';
import {PnfModel} from '../../support/jsonBuilders/models/pnf.model';
import {ServiceModel} from '../../support/jsonBuilders/models/service.model';
import {AaiServiceInstancesModel} from '../../support/jsonBuilders/models/serviceInstances.model';
import {AAISubViewEditModel} from '../../support/jsonBuilders/models/aaiSubViewEdit.model';


describe('View Edit Page', function () {
  describe('basic UI tests', () => {
    let jsonBuilderAAIService : JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();
    let commonUuid = "6e59c5de-f052-46fa-aa7e-2fca9d674c44";
    let serviceInvariantId = "d27e42cf-087e-4d31-88ac-6c4b7585f800";

    let jsonBuilderAAISubViewEditModel: JsonBuilder<AAISubViewEditModel> = new JsonBuilder<AAISubViewEditModel>();
    let jsonBuilderPNF: JsonBuilder<PnfModel> = new JsonBuilder<PnfModel>();
    let jsonBuilderAaiServiceInstances: JsonBuilder<AaiServiceInstancesModel> = new JsonBuilder<AaiServiceInstancesModel>();
    let jsonBuilderEmpty: JsonBuilder<Object> = new JsonBuilder<Object>();
    beforeEach(() => {
      cy.clearSessionStorage();
      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/basicService.json').then((res) => {
        jsonBuilderAAIService.basicJson(
          res,
          Cypress.config('baseUrl') + "/rest/models/services/" + commonUuid,
          200, 0,
          "service-complexService",
          changeServiceModel)
      });

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/basicFabricConfigService.json').then((res) => {
        jsonBuilderAAIService.basicJson(
          res,
          Cypress.config('baseUrl') + "/rest/models/services/6e59c5de-f052-46fa-aa7e-2fca9d671234",
          200, 0,
          "service-FabricConfig", changeFabric)
      });

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/activeFabricConfigService.json').then((res) => {
        jsonBuilderAAIService.basicJson(
          res,
          Cypress.config('baseUrl') + "/rest/models/services/6e59c5de-f052-46fa-aa7e-2fca9d675678",
          200, 0,
          "service-FabricConfig", changeFabric)
      });

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/createdFabricConfigService.json').then((res) => {
        jsonBuilderAAIService.basicJson(
          res,
          Cypress.config('baseUrl') + "/rest/models/services/6e59c5de-f052-46fa-aa7e-2fca9d679000",
          200, 0,
          "service-FabricConfig", changeFabric)
      });

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/deactivatedFabricConfigService.json').then((res) => {
        jsonBuilderAAIService.basicJson(
          res,
          Cypress.config('baseUrl') + "/rest/models/services/6e59c5de-f052-46fa-aa7e-2fca9d671000",
          200, 0,
          "service-FabricConfig", changeFabric)
      });

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/aaiSubViewEditForComplexService.json').then((res) => {
        jsonBuilderAAISubViewEditModel.basicJson(
          res,
          Cypress.config('baseUrl') + "/aai_sub_viewedit/**/**/**/3f93c7cb-2fd0-4557-9514-e189b7b04f9d",
          200,
          0,
          "aai-sub-view-edit")
      });

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/aaiSubViewEditForFabricConfigService.json').then((res) => {
        jsonBuilderAAISubViewEditModel.basicJson(
          res,
          Cypress.config('baseUrl') + "/aai_sub_viewedit/**/**/**/c187e9fe-40c3-4862-b73e-84ff056205f61234",
          200,
          0,
          "aai-sub-view-edit")
      });

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/aaiSubViewEditForActiveFabricConfigService.json').then((res) => {
        jsonBuilderAAISubViewEditModel.basicJson(
          res,
          Cypress.config('baseUrl') + "/aai_sub_viewedit/**/**/**/c187e9fe-40c3-4862-b73e-84ff056205f65678",
          200,
          0,
          "aai-sub-view-edit")
      });

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/aaiSubViewEditForCreatedFabricConfigService.json').then((res) => {
        jsonBuilderAAISubViewEditModel.basicJson(
          res,
          Cypress.config('baseUrl') + "/aai_sub_viewedit/**/**/**/c187e9fe-40c3-4862-b73e-84ff056205f69000",
          200,
          0,
          "aai-sub-view-edit")
      });

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/aaiSubViewEditForDeactivatedFabricConfigService.json').then((res) => {
        jsonBuilderAAISubViewEditModel.basicJson(
          res,
          Cypress.config('baseUrl') + "/aai_sub_viewedit/**/**/**/c187e9fe-40c3-4862-b73e-84ff056205f61000",
          200,
          0,
          "aai-sub-view-edit")
      });

      cy.initGetAAISubDetails();

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/aaiServiceInstancePnfs.json').then((res) => {
        jsonBuilderPNF.basicJson(
          res,
          Cypress.config('baseUrl') + "/aai_get_service_instance_pnfs/**",
          200,
          0,
          "aai-get-service-instance-pnfs")
      });

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/aaiServiceInstances.json').then((res) => {
        jsonBuilderAaiServiceInstances.basicJson(
          res,
          Cypress.config('baseUrl') + "/search_service_instances**",
          200,
          0,
          "aai-get-service-instances")
      });

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/emptyObjectResponse.json').then((res) => {
        jsonBuilderEmpty.basicJson(
          res,
          Cypress.config('baseUrl') + "/aai_getPortMirroringConfigsData**",
          200,
          0,
          "aai_getPortMirroringConfigsDate - empty response")
      });

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/emptyObjectResponse.json').then((res) => {
        jsonBuilderEmpty.basicJson(
          res,
          Cypress.config('baseUrl') + "/aai_getPortMirroringSourcePorts**",
          200,
          0,
          "aai_getPortMirroringSourcePorts - empty response")
      });
      cy.initVidMock();
      cy.mockLatestVersionForService(commonUuid, serviceInvariantId);
      cy.initAAIServices();
      cy.setReduxState();
      cy.permissionVidMock();
      cy.login();
    });

    afterEach(() => {
      cy.screenshot();
    });

    it(`should display service model name and version on each info form`, function () {
      let typesToIncludeModel:Array<string> = ['service', 'vnf', 'vfmodule', 'volume-group', 'network'];
      cy.visit('/serviceModels.htm#/instantiate?subscriberId=e433710f-9217-458d-a79d-1c7aff376d89&subscriberName=SILVIA%20ROBBINS&serviceType=TYLER%20SILVIA&serviceInstanceId=3f93c7cb-2fd0-4557-9514-e189b7b04f9d&aaiModelVersionId=6e59c5de-f052-46fa-aa7e-2fca9d674c44&isPermitted=true');
      cy.wait('@service-complexService');
      cy.wait('@aai_getPortMirroringConfigsDate - empty response');
      cy.get('div').contains('VOLUME GROUP: f'); // waits for the view/edit to be ready after ports' redraw
      typesToIncludeModel.forEach((type) => {
        cy.get('.' + type + '-info').click({force: true});
        cy.getElementByDataTestsId("Model Version").contains('1.0');
        cy.getElementByDataTestsId("Model Name").contains('vidmacrofalsenaming');
        cy.getElementByDataTestsId("detailsCloseBtn").click();
      });
    });

    it(`when using direct url should select elements in productFamily dropdown `, function () {
      cy.visit('/serviceModels.htm#/instantiate?subscriberId=e433710f-9217-458d-a79d-1c7aff376d89&subscriberName=SILVIA%20ROBBINS&serviceType=TYLER%20SILVIA&serviceInstanceId=3f93c7cb-2fd0-4557-9514-e189b7b04f9d&aaiModelVersionId=6e59c5de-f052-46fa-aa7e-2fca9d674c44&isPermitted=true');
      cy.wait('@service-complexService');
      cy.wait('@aai_getPortMirroringConfigsDate - empty response');
      cy.wait('@initAAIServices');
      cy.getElementByDataTestsId('addNetworkButton').click();
      cy.getElementByDataTestsId('addNetworkOption-MULTI_PROVIDER_PORT_GROUP 0').click();
      cy.getElementByDataTestsId('productFamily').select('WILKINS');
    });



    it(`Check fabric configuration service with some configuration with diff orchStatus`, function () {
      cy.visit('/serviceModels.htm#/instantiate?subscriberId=e433710f-9217-458d-a79d-1c7aff376d89&subscriberName=SILVIA%20ROBBINS&serviceType=TYLER%20SILVIA&serviceInstanceId=c187e9fe-40c3-4862-b73e-84ff056205f61234&aaiModelVersionId=6e59c5de-f052-46fa-aa7e-2fca9d671234&isPermitted=true');
      cy.wait('@service-FabricConfig');
      cy.get('.error-msg').should("be.visible").should('contain','Activate fabric configuration button is not available as some of the configuration objects are not in Assigned status. Check MSO logs for the reasons for this abnormal case.');
      cy.getElementByDataTestsId("activateFabricConfigurationButton").should('have.attr', 'disabled');
      cy.getElementByDataTestsId("activateButton").should("not.be.visible");
    });

    it(`Check fabric configuration service with active status`, function () {
      cy.visit('/serviceModels.htm#/instantiate?subscriberId=e433710f-9217-458d-a79d-1c7aff376d89&subscriberName=SILVIA%20ROBBINS&serviceType=TYLER%20SILVIA&serviceInstanceId=c187e9fe-40c3-4862-b73e-84ff056205f65678&aaiModelVersionId=6e59c5de-f052-46fa-aa7e-2fca9d671234&isPermitted=true');
      cy.wait('@service-FabricConfig');
      cy.getElementByDataTestsId("activateFabricConfigurationButton").should('not.be.visible');
      cy.getElementByDataTestsId("activateButton").should('have.attr', 'disabled');
      cy.getElementByDataTestsId("deactivateButton").should("be.visible");
    });

    it(`Check fabric configuration service with created status`, function () {
      cy.visit('/serviceModels.htm#/instantiate?subscriberId=e433710f-9217-458d-a79d-1c7aff376d89&subscriberName=SILVIA%20ROBBINS&serviceType=TYLER%20SILVIA&serviceInstanceId=c187e9fe-40c3-4862-b73e-84ff056205f69000&aaiModelVersionId=6e59c5de-f052-46fa-aa7e-2fca9d679000&isPermitted=true');
      cy.wait('@service-FabricConfig');
      cy.getElementByDataTestsId("activateFabricConfigurationButton").should('not.be.visible');
      cy.getElementByDataTestsId("activateButton").should('have.attr', 'disabled');
      cy.getElementByDataTestsId("deactivateButton").should("be.visible");
    });

    it(`Check fabric configuration service with pendingdeLete status`, function () {
      cy.visit('/serviceModels.htm#/instantiate?subscriberId=e433710f-9217-458d-a79d-1c7aff376d89&subscriberName=SILVIA%20ROBBINS&serviceType=TYLER%20SILVIA&serviceInstanceId=c187e9fe-40c3-4862-b73e-84ff056205f61000&aaiModelVersionId=6e59c5de-f052-46fa-aa7e-2fca9d671000&isPermitted=true');
      cy.wait('@service-FabricConfig');
      cy.getElementByDataTestsId("activateFabricConfigurationButton").should('not.be.visible');
      cy.getElementByDataTestsId("activateButton").should('not.have.attr', 'disabled');
      cy.getElementByDataTestsId("deactivateButton").should('have.attr', 'disabled');
    });

  });

  function changeFabric(serviceModel: ServiceModel) {
    serviceModel.service.uuid = "6e59c5de-f052-46fa-aa7e-2fca9d671234";
    return serviceModel;
  }

  function changeServiceModel(serviceModel: ServiceModel) {
    serviceModel.service.uuid = "6e59c5de-f052-46fa-aa7e-2fca9d674c44";
    return serviceModel;
  }
});
