///<reference path="../../../node_modules/cypress/types/index.d.ts"/> / <reference types="Cypress" />
import {JsonBuilder} from '../../support/jsonBuilders/jsonBuilder';
import {PnfModel} from '../../support/jsonBuilders/models/pnf.model';
import {ServiceModel} from '../../support/jsonBuilders/models/service.model';
import {AaiServiceInstancesModel} from '../../support/jsonBuilders/models/serviceInstances.model';
import {AAISubDetailsModel} from '../../support/jsonBuilders/models/aaiSubDetails.model';
import {AAISubViewEditModel} from '../../support/jsonBuilders/models/aaiSubViewEdit.model';

describe('Service With VNF network', function () {
  describe('basic UI tests', () => {
    var jsonBuilderAAIService: JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();


    var jsonBuilderAAISubViewEditModel: JsonBuilder<AAISubViewEditModel> = new JsonBuilder<AAISubViewEditModel>();
    var jsonBuilderAAISubDetailsModel: JsonBuilder<AAISubDetailsModel> = new JsonBuilder<AAISubDetailsModel>();
    var jsonBuilderPNF: JsonBuilder<PnfModel> = new JsonBuilder<PnfModel>();
    var jsonBuilderAaiServiceInstances: JsonBuilder<AaiServiceInstancesModel> = new JsonBuilder<AaiServiceInstancesModel>();
    var jsonBuilderEmpty: JsonBuilder<Object> = new JsonBuilder<Object>();
    beforeEach(() => {
      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/basicService.json').then((res) => {
        jsonBuilderAAIService.basicJson(
          res,
          Cypress.config('baseUrl') + "/rest/models/services/6e59c5de-f052-46fa-aa7e-2fca9d674c44",
          200, 0,
          "service-complexService",
          changeServiceModel)
      });


      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/aaiSubViewEditForComplexService.json').then((res) => {
        jsonBuilderAAISubViewEditModel.basicJson(
          res,
          Cypress.config('baseUrl') + "/aai_sub_viewedit/**/**/**/3f93c7cb-2fd0-4557-9514-e189b7b04f9d",
          200,
          0,
          "aai-sub-view-edit")
      });

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/aaiSubDetails.json').then((res) => {
        jsonBuilderAAISubDetailsModel.basicJson(
          res,
          Cypress.config('baseUrl') + "/aai_sub_details/**",
          200,
          0,
          "aai-sub-details")
      });

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/aaiServiceInstances.json').then((res) => {
        jsonBuilderAaiServiceInstances.basicJson(
          res,
          Cypress.config('baseUrl') + "/search_service_instances**",
          200,
          0,
          "aai-get-service-instances")
      });

      cy.readFile('../vid-automation/src/test/resources/serviceWithNetwork/aaiGetNetworksWithVlansToVnfByServiceInstance.json').then((res) => {
        jsonBuilderAAIService.basicJson(
          res,
          Cypress.config('baseUrl') + '/aai/standardQuery/vlansByNetworks?' +
          'globalCustomerId=e433710f-9217-458d-a79d-1c7aff376d89' +
          '&serviceType=TYLER SILVIA' +
          '&serviceInstanceId=3f93c7cb-2fd0-4557-9514-e189b7b04f9d' +
          '&sdcModelUuid=6e59c5de-f052-46fa-aa7e-2fca9d674c44',
          200,
          0,
          "aai-aaiGetNetworksToVlans-By-Service-Instance")
      });

      cy.initVidMock(); // just for subsequent "initFlags()"

      cy.login();
    });

    afterEach(() => {
      cy.screenshot();
    });

    it(`Display VNF's network and its vlans hierarchically`, function () {
      cy.visit('/serviceModels.htm#/instantiate?subscriberId=e433710f-9217-458d-a79d-1c7aff376d89&subscriberName=SILVIA%20ROBBINS&serviceType=TYLER%20SILVIA&serviceInstanceId=3f93c7cb-2fd0-4557-9514-e189b7b04f9d&aaiModelVersionId=6e59c5de-f052-46fa-aa7e-2fca9d674c44&isPermitted=true');
      cy.wait('@service-complexService');
      cy.get('div').contains('VNF: fsd')
        .get('div').contains('NETWORK: AAAAABBBBCCCC')
        .get('div').contains('NETWORK: DDDEEEE')
        .get('.vlansTreeNode').should('have.length', 4);
    });
  });

  function changeServiceModel(serviceModel: ServiceModel) {
    serviceModel.service.uuid = "6e59c5de-f052-46fa-aa7e-2fca9d674c44";
    return serviceModel;
  }
});

