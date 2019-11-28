///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
import {JsonBuilder} from '../../support/jsonBuilders/jsonBuilder';
import {ServiceModel} from '../../support/jsonBuilders/models/service.model';
import {AsyncInstantiationModel} from "../../support/jsonBuilders/models/asyncInstantiation.model";

describe('Delete vnf instance', function () {
  let jsonBuilderAndMock: JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();

  let jsonBuilderInstantiationBuilder: JsonBuilder<AsyncInstantiationModel> = new JsonBuilder<AsyncInstantiationModel>();

  beforeEach(() => {
      cy.clearSessionStorage();
      cy.preventErrorsOnLoading();
      cy.initAAIMock();
      cy.initVidMock();
      cy.initZones();
      cy.permissionVidMock();
      cy.login();
  });

  afterEach(() => {
    cy.screenshot();
  });

  it(`Update vnf - add 1 delete 1`, function () {
    const SUBSCRIBER_ID: string = "e433710f-9217-458d-a79d-1c7aff376d89";
    const SERVICE_TYPE: string = "TYLER SILVIA";
    const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
    const SERVICE_MODEL_ID: string = '6b528779-44a3-4472-bdff-9cd15ec93450';

    cy.server().route({
      url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
      method: 'POST',
      status: 200,
      response: "[]",
    }).as("expectedPostAsyncInstantiation");

    cy.readFile('cypress/support/jsonBuilders/mocks/jsons/serviceModels/ecompNamingFalseModel.json').then((res) => {
      res.service.vidNotions.instantiationType = 'ALaCarte';
      jsonBuilderAndMock.basicJson(
        res,
        Cypress.config('baseUrl') + "/rest/models/services/6b528779-44a3-4472-bdff-9cd15ec93450",
        200,
        0,
        "ecompNamingFalseModel",
      )
    });

    cy.readFile('../vid-automation/src/test/resources/aaiGetInstanceTopology/getServiceInstanceTopologyResult.json').then((res) => {
      res.networks = {};
      res.isALaCarte = true;
      res.instanceId = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";

      const vnf = res.vnfs['2017-488_PASQUALE-vPE 0'];

      vnf.instanceId = "VNF_INSTANCE_ID";
      vnf.vfModules['2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0']['2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0uvfot'].instanceId = "VF_MODULE_BASE_INSTANCE_ID";
      vnf.vfModules['2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1']['2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1fshmc'].instanceId = "VF_MODULE_INSTANCE_ID";


      jsonBuilderAndMock.basicJson(
        res,
        Cypress.config('baseUrl') + "/aai_get_service_instance_topology/e433710f-9217-458d-a79d-1c7aff376d89/TYLER SILVIA/f8791436-8d55-4fde-b4d5-72dd2cf13cfb",
        200, 0,
        "initServiceInstanceTopology",
      )
    });

    cy.openIframe(`app/ui/#/servicePlanning/EDIT?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}`);

    // add a vnf on update mode
    cy.drawingBoardPressAddButtonByElementName('node-2017-388_PASQUALE-vPE 0').click({force: true});
    cy.selectDropdownOptionByText('rollback', 'Rollback');
    cy.fillVnfPopup(true);

    // delete VNF
    cy.drawingBoardTreeOpenContextMenuByElementDataTestId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_PASQUALE-vPE 0', 0)
      .drawingBoardTreeClickOnContextMenuOptionByName('Delete');

    // update service
    cy.getDrawingBoardDeployBtn().click();

    cy.getReduxState().then((state) => {
      const vnf = state.service.serviceInstance[SERVICE_MODEL_ID].vnfs["2017-388_PASQUALE-vPE 0_1"];
      cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
        cy.readFile('../vid-automation/src/test/resources/asyncInstantiation/vidRequestDelete1Create1Vnf.json').then((expectedResult) => {
          expectedResult.vnfs["2017-388_PASQUALE-vPE 0_1"].trackById = vnf.trackById;
          expectedResult.vnfs["2017-388_PASQUALE-vPE 0_1"].platformName = 'platform,xxx1';
          expectedResult.testApi = null;
          cy.deepCompare(expectedResult, xhr.request.body);
        });
      });
    });
  });

});
