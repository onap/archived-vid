///<reference path="../../../node_modules/cypress/types/index.d.ts"/> / <reference types="Cypress" />
import {JsonBuilder} from '../../support/jsonBuilders/jsonBuilder';
import {ServiceModel} from '../../support/jsonBuilders/models/service.model';
import {AaiServiceInstancesModel} from '../../support/jsonBuilders/models/serviceInstances.model';
import {AAISubViewEditModel} from '../../support/jsonBuilders/models/aaiSubViewEdit.model';

const jsonBuilderAndMock: JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();
const SUBSCRIBER_ID = "e433710f-9217-458d-a79d-1c7aff376d89";
const SERVICE_MODEL_ID: string = '6e59c5de-f052-46fa-aa7e-2fca9d674c44';

export const initServicePlanning = function (viewOrEdit: string, customModelFilePath?: string) {
  const SERVICE_TYPE: string = "TYLER SILVIA";
  const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
  if (Cypress._.isNil(customModelFilePath)) {
    customModelFilePath = '../vid-automation/src/test/resources/aaiGetInstanceTopology/ServiceTreeWithMultipleChildren_serviceInstance.json';
  }

  cy.readFile('../vid-automation/src/test/resources/aaiGetInstanceTopology/ServiceTreeWithMultipleChildren_serviceModel.json').then((res) => {
    res.service.instantiationType = "A-La-Carte";
    res.service.vidNotions.instantiationType = "ALaCarte";
    jsonBuilderAndMock.basicJson(
      res,
      Cypress.config('baseUrl') + "/rest/models/services/6e59c5de-f052-46fa-aa7e-2fca9d674c44",
      200,
      0,
      "ServiceTreeWithMultipleChildren_serviceModel",
    )
  });

  cy.readFile(customModelFilePath).then((res) => {
    jsonBuilderAndMock.basicJson(
      res,
      Cypress.config('baseUrl') + "/aai_get_service_instance_topology/e433710f-9217-458d-a79d-1c7aff376d89/TYLER SILVIA/f8791436-8d55-4fde-b4d5-72dd2cf13cfb",
      200, 0,
      "ServiceTreeWithMultipleChildren_serviceInstance",
    );
  });
  cy.openIframe(`app/ui/#/servicePlanning/${viewOrEdit}?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}`);
};


describe('View Edit Page: Upgrade VFModule', function () {
  describe('basic UI tests', () => {
    let serviceUuid = SERVICE_MODEL_ID;
    let serviceInvariantId = "d27e42cf-087e-4d31-88ac-6c4b7585f800";

    let jsonBuilderAAIService: JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();
    let jsonBuilderAAISubViewEditModel: JsonBuilder<AAISubViewEditModel> = new JsonBuilder<AAISubViewEditModel>();
    let jsonBuilderAaiServiceInstances: JsonBuilder<AaiServiceInstancesModel> = new JsonBuilder<AaiServiceInstancesModel>();

    beforeEach(() => {
      cy.clearSessionStorage();
      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/basicService.json').then((res) => {
        jsonBuilderAAIService.basicJson(
          res,
          Cypress.config('baseUrl') + "/rest/models/services/" + serviceUuid,
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

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/aaiServiceInstances.json').then((res) => {
        jsonBuilderAaiServiceInstances.basicJson(
          res,
          Cypress.config('baseUrl') + "/search_service_instances**",
          200,
          0,
          "aai-get-service-instances")
      });

      mockAsyncBulkResponse();
      cy.initGetAAISubDetails();
      cy.initVidMock();
      cy.mockLatestVersionForService(serviceUuid, serviceInvariantId);
      cy.setReduxState();
      cy.permissionVidMock();
      cy.login();
    });

    afterEach(() => {
      cy.screenshot();
    });

    it(`should display the more actions button if user is permitted`, function () {
      let SERVICE_INSTANCE_ID="3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
      let SERVICE_TYPE="TYLER%20SILVIA";
      cy.visit(`/serviceModels.htm#/instantiate?subscriberId=${SUBSCRIBER_ID}&subscriberName=SILVIA%20ROBBINS&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}&aaiModelVersionId=${serviceUuid}&isPermitted=true`);

      cy.get("[data-tests-id='service-instanceId-th-id']").should('contain', SERVICE_INSTANCE_ID);
      cy.getElementByDataTestsId("show-new-screen").should('be.visible').should('have.text', 'More actions').click();

      cy.url().should('contain',
        `servicePlanning/EDIT?serviceModelId=${serviceUuid}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}`);
    });

    it(`Upgrade a VF Module`, function () {
      cy.initDrawingBoardUserPermission();
      initServicePlanning("EDIT",
        '../vid-automation/src/test/resources/viewEdit/ServiceTreeWithMultipleChildren_serviceInstance_withUpdatedLatestVersion.json');
      upgradeTheVFM('node-522159d5-d6e0-4c2a-aa44-5a542a12a830-vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1', true);
      assertVfModuleActionInRedux("None_Upgrade");
      undoUpgradeForVFM();
      assertVfModuleActionInRedux("None");
      upgradeTheVFM('node-522159d5-d6e0-4c2a-aa44-5a542a12a830-vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1',true);
      cy.getDrawingBoardDeployBtn().click();
      cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
        const requestBody = Object(xhr.request.body);
        const vfModuleRequest = requestBody.vnfs['VNF2_INSTANCE_ID'].vfModules['vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1']['2c1ca484-cbc2-408b-ab86-25a2c15ce280'];
        expect(requestBody.action).to.equal("None_Upgrade");
        expect(requestBody.vnfs['VNF2_INSTANCE_ID'].action).to.equal("None_Upgrade");
        expect(vfModuleRequest.action).to.equal("None_Upgrade");
      });
    });

    it(`Upgrade a VFModule, Negative - latest version doesn't exist, upgrade button shouldn't exist`, function () {
      setLatestVersionMockToEmptyResponse(serviceInvariantId);
      cy.initDrawingBoardUserPermission();
      initServicePlanning("EDIT",
        '../vid-automation/src/test/resources/viewEdit/ServiceTreeWithMultipleChildren_serviceInstance_withUpdatedLatestVersion.json');
      verifyMenuActionUpgradeDoesNotExist();
    });

  });

  describe('More UI test', () => {

    beforeEach(() => {
      cy.clearSessionStorage();
      cy.setTestApiParamToGR();
      cy.initVidMock();
      cy.login();
    });

    afterEach(() => {
      cy.screenshot();
    });
    it(`Delete not upgraded VFM and upgrade another in a single click`, () => {

      const serviceType = 'Emanuel';
      const subscriberId = 'a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb';
      const serviceModelId = 'a243da28-c11e-45a8-9f26-0284a9a789bc';
      const serviceInstanceId = 'b153e8ce-2d00-4466-adc0-14bad70f150c';
      const serviceInvariantUuid = "dd5a69b7-c50c-4dde-adc2-966b79bb8fd6";

      cy.initDrawingBoardUserPermission();
      cy.route(`**/rest/models/services/${serviceModelId}`,
        'fixture:../support/jsonBuilders/mocks/jsons/deleteVfModule/delete_vfmodule_model.json')
      .as('serviceModel2');

      cy.route(`**/aai_get_service_instance_topology/${subscriberId}/${serviceType}/${serviceInstanceId}`,
        'fixture:../support/jsonBuilders/mocks/jsons/deleteVfModule/delete_vfmodule_service_instance.json')
      .as('serviceInstance2');

      cy.route(`**/aai_get_newest_model_version_by_invariant/${serviceInvariantUuid}`, {
          "modelVersionId": "a243da28-c11e-45a8-9f26-0284a9a789bc",
          "modelName": "CHARLOTTE 01222020 Svc",
          "modelVersion": "4.0",
          "distributionStatus": "DISTRIBUTION_COMPLETE_OK",
          "resourceVersion": "1580246673596",
          "modelDescription": "test model for VF module replacement",
          "orchestrationType": null
        }
      ).as("newestModelVersion2");

      cy.openIframe(`app/ui/#/servicePlanning/EDIT?serviceModelId=${serviceModelId}&subscriberId=${subscriberId}&serviceType=${serviceType}&serviceInstanceId=${serviceInstanceId}`);

      upgradeTheVFM(`node-04b21d26-9780-4956-8329-b22b049329f4-xbitestmodulereplace0..XbiTestModuleReplace..base_ocg..module-0`, false);

      cy.getElementByDataTestsId(`node-c449aaf8-2467-41a9-9015-730ab48ca19b-mdns012220200..Mdns01222020..dns_az_01..module-1-menu-btn`).click()
      .drawingBoardTreeClickOnContextMenuOptionByName("Delete");
      cy.getElementByDataTestsId('delete-status-type').contains('Delete');

      mockAsyncBulkResponse();
      cy.getDrawingBoardDeployBtn().click();

      cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
                    cy.readFile('../vid-app-common/src/test/resources/payload_jsons/vfmodule/delete_vfmodule_expected_bulk.json').then((expectedResult) => {
                      cy.deepCompare(xhr.request.body, expectedResult);
                    });
                  });
    });

    it(`Upgrade a VFModule: another case e2e`, function () {

      const serviceType = 'Emanuel';
      const subscriberId = 'a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb';
      const serviceModelId = '06c415d8-afc2-4bcb-a131-e4db4b8e96ce';
      const serviceInstanceId = '6196ab1f-2349-4b32-9b6c-cffeb0ccc79c';
      const serviceInvariantUuid = "b3a1a119-dede-4ed0-b077-2a617fa519a3";

      cy.initDrawingBoardUserPermission();

      cy.route(`**/rest/models/services/${serviceModelId}`,
        'fixture:../support/jsonBuilders/mocks/jsons/upgradeVfModule/upgrade_vfmodule_e2e__service_model.json')
      .as('serviceModel2');

      cy.route(`**/aai_get_service_instance_topology/${subscriberId}/${serviceType}/${serviceInstanceId}`,
        'fixture:../support/jsonBuilders/mocks/jsons/upgradeVfModule/upgrade_vfmodule_e2e__service_instance.json')
      .as('serviceInstance2');

      cy.route(`**/aai_get_newest_model_version_by_invariant/${serviceInvariantUuid}`,
        {
          "modelVersionId": "d9a5b318-187e-476d-97f7-a15687a927a9",
          "modelName": "xbi test module replace",
          "modelVersion": "2.0",
          "distributionStatus": "DISTRIBUTION_COMPLETE_OK",
          "resourceVersion": "1571769586156",
          "modelDescription": "test module replacement feature",
          "orchestrationType": null
        }
      ).as("newestModelVersion2");

      cy.openIframe(`app/ui/#/servicePlanning/EDIT?serviceModelId=${serviceModelId}&subscriberId=${subscriberId}&serviceType=${serviceType}&serviceInstanceId=${serviceInstanceId}`);

      upgradeTheVFM('node-04b21d26-9780-4956-8329-b22b049329f4-xbitestmodulereplace0..XbiTestModuleReplace..base_ocg..module-0', false);

      mockAsyncBulkResponse();
      cy.getDrawingBoardDeployBtn().click();

      cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
        cy.readFile('../vid-app-common/src/test/resources/payload_jsons/vfmodule/upgrade_vfmodule_e2e__fe_input_cypress.json').then((expectedResult) => {
          cy.deepCompare(xhr.request.body, expectedResult);
        });
      });

    });

    it(`Upgrade a VFModule: upgrade vfmodule when upgraded already service, vnf and borther vfmodule e2e`, function () {

      const serviceType = 'Emanuel';
      const subscriberId = 'a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb';
      const serviceModelId = 'a243da28-c11e-45a8-9f26-0284a9a789bc';
      const serviceInstanceId = 'b153e8ce-2d00-4466-adc0-14bad70f150c';
      const serviceInvariantUuid = "dd5a69b7-c50c-4dde-adc2-966b79bb8fd6";

      cy.initDrawingBoardUserPermission();

      cy.route(`**/rest/models/services/${serviceModelId}`,
        'fixture:../support/jsonBuilders/mocks/jsons/upgradeVfModule/upgrade_vfmodule_when_service_vnf_and_brother_vfmodule_alredy_upgraded_e2e__service_model.json')
      .as('serviceModel2');

      cy.route(`**/aai_get_service_instance_topology/${subscriberId}/${serviceType}/${serviceInstanceId}`,
        'fixture:../support/jsonBuilders/mocks/jsons/upgradeVfModule/upgrade_vfmodule_when_service_vnf_and_brother_vfmodule_alredy_upgraded_e2e__service_instance.json')
      .as('serviceInstance2');

      cy.route(`**/aai_get_newest_model_version_by_invariant/${serviceInvariantUuid}`, {
          "modelVersionId": "a243da28-c11e-45a8-9f26-0284a9a789bc",
          "modelName": "CHARLOTTE 01222020 Svc",
          "modelVersion": "3.0",
          "distributionStatus": "DISTRIBUTION_COMPLETE_OK",
          "resourceVersion": "1580246673596",
          "modelDescription": "test model for VF module replacement",
          "orchestrationType": null
        }
      ).as("newestModelVersion2");

      cy.openIframe(`app/ui/#/servicePlanning/EDIT?serviceModelId=${serviceModelId}&subscriberId=${subscriberId}&serviceType=${serviceType}&serviceInstanceId=${serviceInstanceId}`);

      upgradeTheVFM('node-3412fe1f-e103-4777-90c0-f66d888f4bed-mdns012220200..Mdns01222020..dns_az_01..module-1', true);

      mockAsyncBulkResponse();
      cy.getDrawingBoardDeployBtn().click();

      cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
        cy.readFile('../vid-app-common/src/test/resources/payload_jsons/vfmodule/upgrade_vfmodule_not_related_to_current_model_e2e__fe_input_cypress.json').then((expectedResult) => {
          cy.deepCompare(xhr.request.body, expectedResult);
        });
      });

    });

  });


  function mockAsyncBulkResponse() {
    cy.server().route({
      url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
      method: 'POST',
      status: 200,
      response: "[]",
    }).as("expectedPostAsyncInstantiation");
  }

  function verifyMenuActionUpgradeDoesNotExist() {
    cy.getElementByDataTestsId('node-522159d5-d6e0-4c2a-aa44-5a542a12a830-vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1-menu-btn').click().then(() => {
      cy.getElementByDataTestsId('context-menu-upgrade').should('not.exist');
    });
  }

  function setLatestVersionMockToEmptyResponse(serviceUuid: string) {
    cy.server().route({
      url: Cypress.config('baseUrl') + '/aai_get_newest_model_version_by_invariant/' + serviceUuid,
      method: 'GET',
      status: 200,
      response: {},
    }).as("expectLatestServiceModelUpgradeVersion");
  }

  function upgradeTheVFM(treeNodeId: string, shouldVGCheckboxExist :boolean) {
    cy.getElementByDataTestsId(`${treeNodeId}-menu-btn`).click()
    .drawingBoardTreeClickOnContextMenuOptionByName("Upgrade");
    // The following is needed when enabling FLAG_2002_VFM_UPGRADE_ADDITIONAL_OPTIONS

    cy.getElementByDataTestsId('retainAssignments').click();
    if (shouldVGCheckboxExist) {
      cy.getElementByDataTestsId('retainVolumeGroups').click();
    }
    else {
      cy.getElementByDataTestsId('retainVolumeGroups').should('not.exist');
    }
    cy.getElementByDataTestsId('sdncPreLoad').click();
    cy.screenshot();
    cy.getElementByDataTestsId('form-set').click();
  }

  function undoUpgradeForVFM() {
    cy.getElementByDataTestsId('node-522159d5-d6e0-4c2a-aa44-5a542a12a830-vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1-menu-btn').click()
    .drawingBoardTreeClickOnContextMenuOptionByName("Undo Upgrade");
  }

  function changeServiceModel(serviceModel: ServiceModel) {
    serviceModel.service.uuid = "6e59c5de-f052-46fa-aa7e-2fca9d674c44";
    return serviceModel;
  }

  function assertVfModuleActionInRedux(expectedState:string) {
    cy.getReduxState().then((state) => {
      const vfModule = state.service.serviceInstance['6e59c5de-f052-46fa-aa7e-2fca9d674c44']
        .vnfs["VNF2_INSTANCE_ID"]
        .vfModules["vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1"]["2c1ca484-cbc2-408b-ab86-25a2c15ce280"];
      expect(vfModule.action).to.equal(expectedState)
    });
  }
});
