///<reference path="../../../node_modules/cypress/types/index.d.ts"/> / <reference types="Cypress" />
import {JsonBuilder} from '../../support/jsonBuilders/jsonBuilder';
import {PnfModel} from '../../support/jsonBuilders/models/pnf.model';
import {ServiceModel} from '../../support/jsonBuilders/models/service.model';
import {AaiServiceInstancesModel} from '../../support/jsonBuilders/models/serviceInstances.model';
import {AAISubDetailsModel} from '../../support/jsonBuilders/models/aaiSubDetails.model';
import {AAISubViewEditModel} from '../../support/jsonBuilders/models/aaiSubViewEdit.model';

describe('View Edit Page', function () {
  describe('test view service with network', () => {
    var jsonBuilderAAIService : JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();
    const presetsPrefix : string = 'cypress/support/jsonBuilders/mocks/jsons/';

    var jsonBuilderAAISubViewEditModel: JsonBuilder<AAISubViewEditModel> = new JsonBuilder<AAISubViewEditModel>();
    var jsonBuilderAAISubDetailsModel: JsonBuilder<AAISubDetailsModel> = new JsonBuilder<AAISubDetailsModel>();
    var jsonBuilderPNF: JsonBuilder<PnfModel> = new JsonBuilder<PnfModel>();
    var jsonBuilderAaiServiceInstances: JsonBuilder<AaiServiceInstancesModel> = new JsonBuilder<AaiServiceInstancesModel>();
    var jsonBuilderEmpty: JsonBuilder<Object> = new JsonBuilder<Object>();
    beforeEach(() => {
      cy.readFile(presetsPrefix + 'serviceWithNetwork/serviceWithNetwork.json').then((res) => {
        jsonBuilderAAIService.basicJson(
          res,
          Cypress.config('baseUrl') + "/rest/models/services/5a3ad576-c01d-4bed-8194-0e72b4a3d020",
          200, 0,
          "service-complexService")
      });
      cy.readFile(presetsPrefix + 'serviceWithNetwork/aaiSubViewEditForServiceWithNetwork.json').then((res) => {
        jsonBuilderAAISubViewEditModel.basicJson(
          res,
          Cypress.config('baseUrl') + "/aai_sub_viewedit/**",
          200,
          0,
          "aai-sub-view-edit")
      });
      cy.readFile(presetsPrefix + 'serviceWithNetwork/aaiSubDetailsForServiceWithNetwork.json').then((res) => {
        jsonBuilderAAISubDetailsModel.basicJson(
          res,
          Cypress.config('baseUrl') + "/aai_sub_details/**",
          200,
          0,
          "aai-sub-details")
      });
      cy.readFile(presetsPrefix + 'aaiServiceInstancePnfs.json').then((res) => {
        jsonBuilderPNF.basicJson(
          res,
          Cypress.config('baseUrl') + "/aai_get_service_instance_pnfs/**",
          200,
          0,
          "aai-get-service-instance-pnfs")
      });
      cy.readFile(presetsPrefix + 'serviceWithNetwork/aaiServiceInstances.json').then((res) => {
        jsonBuilderAaiServiceInstances.basicJson(
          res,
          Cypress.config('baseUrl') + "/search_service_instances**",
          200,
          0,
          "aai-get-service-instances")
      });
      cy.readFile(presetsPrefix + 'emptyObjectResponse.json').then((res) => {
        jsonBuilderEmpty.basicJson(
          res,
          Cypress.config('baseUrl') + "/aai_getPortMirroringConfigsData**",
          200,
          0,
          "aai_getPortMirroringConfigsDate - empty response")
      });
      cy.readFile(presetsPrefix + 'emptyObjectResponse.json').then((res) => {
        jsonBuilderEmpty.basicJson(
          res,
          Cypress.config('baseUrl') + "/aai_getPortMirroringSourcePorts**",
          200,
          0,
          "aai_getPortMirroringSourcePorts - empty response")
      });
      cy.readFile('../vid-automation/src/test/resources/serviceWithNetwork/aaiGetNetworksToVlansByServiceInstance.json').then((res) => {
        jsonBuilderAAIService.basicJson(
          res,
          Cypress.config('baseUrl') + '/aai/standardQuery/vlansByNetworks?' +
              'globalCustomerId=a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb' +
              '&serviceType=vMOG' +
              '&serviceInstanceId=9cdd1b2a-43a7-47bc-a88e-759ba2399f0b' +
              '&sdcModelUuid=5a3ad576-c01d-4bed-8194-0e72b4a3d020',
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

    it(`should allow delete network on view edit`, function () {
      cy.visit('/serviceModels.htm#/instantiate?subscriberId=a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb&subscriberName=Emanuel&serviceType=vMOG&serviceInstanceId=9cdd1b2a-43a7-47bc-a88e-759ba2399f0b&aaiModelVersionId=5a3ad576-c01d-4bed-8194-0e72b4a3d020&isPermitted=true');
       cy.wait('@aai_getPortMirroringSourcePorts - empty response');
      cy.getElementByDataTestsId("deleteNetworkButton").should('not.have.attr', 'disabled');
      cy.get(".vlansTreeNode").should('have.length', 2);
    });
  });

});

