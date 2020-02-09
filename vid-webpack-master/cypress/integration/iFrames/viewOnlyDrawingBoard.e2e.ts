///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
/// <reference types="Cypress" />
import {JsonBuilder} from '../../support/jsonBuilders/jsonBuilder';
import {ServiceModel} from '../../support/jsonBuilders/models/service.model';
import {AsyncInstantiationModel} from "../../support/jsonBuilders/models/asyncInstantiation.model";

var jsonBuilderAndMock: JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();
var jsonBuilderInstantiationBuilder: JsonBuilder<AsyncInstantiationModel> = new JsonBuilder<AsyncInstantiationModel>();
const SERVICE_MODEL_ID: string = '6e59c5de-f052-46fa-aa7e-2fca9d674c44';
const SERVICE_INVARIANT_ID: string = "d27e42cf-087e-4d31-88ac-6c4b7585f800";

export const initServicePlanning = function (viewOrEdit: string, customModelFilePath?: string ){
  const SUBSCRIBER_ID: string = "e433710f-9217-458d-a79d-1c7aff376d89";
  const SERVICE_TYPE: string = "TYLER SILVIA";
  const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
  if (Cypress._.isNil(customModelFilePath)){
    customModelFilePath = '../vid-automation/src/test/resources/aaiGetInstanceTopology/ServiceTreeWithMultipleChildren_serviceInstance.json';
  }

  cy.readFile('../vid-automation/src/test/resources/aaiGetInstanceTopology/ServiceTreeWithMultipleChildren_serviceModel.json').then((res) => {
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
}

describe('View only drawing board', function () {
  const _VIEW = "VIEW";

  beforeEach(() => {
      cy.clearSessionStorage();
      cy.preventErrorsOnLoading();
      cy.initAAIMock();
      cy.initVidMock();
      cy.mockLatestVersionForService(SERVICE_MODEL_ID, SERVICE_INVARIANT_ID);
      cy.initZones();
      cy.permissionVidMock();
      cy.login();
  });

  afterEach(() => {
    cy.screenshot();
  });

  it('error should display on api error', function () {
    const SUBSCRIBER_ID: string = "e433710f-9217-458d-a79d-1c7aff376d89";
    const SERVICE_TYPE: string = "TYLER SILVIA";
    const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
    const SERVICE_MODEL_ID: string = '6b528779-44a3-4472-bdff-9cd15ec93450';

    cy.readFile('cypress/support/jsonBuilders/mocks/jsons/serviceModels/ecompNamingFalseModel.json').then((res) => {
      jsonBuilderInstantiationBuilder.basicJson(
        res,
        Cypress.config('baseUrl') + "/rest/models/services/6b528779-44a3-4472-bdff-9cd15ec93450",
        500,
        0,
        "error 500 getServiceInstanceTopology"
      );

      cy.openIframe(`app/ui/#/servicePlanning/VIEW?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}`);

      cy.get('div.title')
        .contains('Server not available');

    });
  });

  it(`when open service planning in view mode service instance is shown as expected`, function () {
    const SUBSCRIBER_ID: string = "e433710f-9217-458d-a79d-1c7aff376d89";
    const SERVICE_TYPE: string = "TYLER SILVIA";
    const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
    const SERVICE_MODEL_ID: string = '6b528779-44a3-4472-bdff-9cd15ec93450';

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

    cy.openIframe(`app/ui/#/servicePlanning/VIEW?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}`);
    //cy.visit("welcome.htm"); //relaod page to not break the following tests

    //testing left side
    cy.getElementByDataTestsId('node-2017-388_PASQUALE-vPE 1').find(`[data-tests-id='node-type-indicator']`).should('have.text', 'VNF');
    cy.getElementByDataTestsId('node-2017-488_PASQUALE-vPE 0').click({force: true});
    cy.getElementByDataTestsId('node-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1').find(`[data-tests-id='node-type-indicator']`).should('have.text', 'M');

    //testing right side
    cy.getElementByDataTestsId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_PASQUALE-vPE 0').find(`[data-tests-id='node-type-indicator']`).should('have.length', 3).and('have.text', 'VNFVNFVNF');
    cy.getElementByDataTestsId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_PASQUALE-vPE 0').eq(0).click({force: true});
    cy.getElementByDataTestsId('node-f8360508-3f17-4414-a2ed-6bc71161e8db-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0').eq(0).find(`[data-tests-id='node-type-indicator']`).should('have.text', 'M');

    //check vnf node tree sub header
    cy.getElementByDataTestsId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_PASQUALE-vPE 0').find("[data-tests-id='status-property-orchStatus']").eq(0).should('have.text', 'Created');
    cy.getElementByDataTestsId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_PASQUALE-vPE 0').find("[data-tests-id='status-property-provStatus']").eq(0).should('have.text', '');
    cy.getElementByDataTestsId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_PASQUALE-vPE 0').find("[data-tests-id='status-property-inMaint']").should('not.exist');

    //check vf Module node tree sub header
    cy.getElementByDataTestsId('node-f8360508-3f17-4414-a2ed-6bc71161e8db-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0').find("[data-tests-id='status-property-provStatus']").eq(0).should('have.text', 'Prov Status');
    cy.getElementByDataTestsId('node-f8360508-3f17-4414-a2ed-6bc71161e8db-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0').find("[data-tests-id='status-property-orchStatus']").eq(0).should('have.text', 'Active');
    cy.getElementByDataTestsId('node-f8360508-3f17-4414-a2ed-6bc71161e8db-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0').find("[data-tests-id='status-property-inMaint']").eq(0).should('have.text', '');

    cy.getElementByDataTestsId("serviceInstance").should('have.text', 'Service instance:');
    cy.getElementByDataTestsId("serviceName").should('have.text', 'mCaNkinstancename');
    cy.getElementByDataTestsId("orchStatusLabel").should('have.text', 'Orch Status:');
    cy.getElementByDataTestsId("orchStatusValue").should('have.text', 'Active');
    cy.getElementByDataTestsId("quantityLabel").should('be.visible');
    cy.getElementByDataTestsId("servicesQuantity").should('have.text', ' 1 ');


  });

  it('check component info for 2 trees for vnf, vf-module, and network', function(){
    initServicePlanning(_VIEW);
    testComponentInfoForVNF();
    testComponentInfoForVFMODULE();
    testComponentInfoForNetwork();
  });

  it(`when open service planning in view mode service instance is shown as expected - e2e with API's ServiceTreeWithMultipleChildren`, function () {
    initServicePlanning(_VIEW);
    /*
    0. title area -> generic stuff
                     instance name
                     orch status
                     service name
    */
    cy.getElementByDataTestsId("serviceInstance").should('have.text', 'Service instance:');
    cy.getElementByDataTestsId("orchStatusLabel").should('have.text', 'Orch Status:');
    cy.getElementByDataTestsId("quantityLabel").should('be.visible');
    cy.getElementByDataTestsId("servicesQuantity").should('have.text', ' 1 ');

    // specific
    cy.getElementByDataTestsId("serviceName").should('have.text', 'SERVICE_INSTANCE_NAME');
    cy.getElementByDataTestsId("orchStatusValue").should('have.text', 'GARBAGE DATA');
    cy.get('span#service-model-name').should('have.text', 'ComplexService');

    // test component info of service-level
    let labelsAndValues = [
      ['Model version', '1.0'],
      ['Instance ID', 'service-instance-id'],
      ['Service type', 'service-instance-type'],
    ];
    cy.assertComponentInfoTitleLabelsAndValues('Service Instance INFO', labelsAndValues);


    // expand all
    cy.get('available-models-tree').find('.toggle-children').click({ multiple: true });

    /*
    1. Left tree -> VNF with 3 vf modules
                    Network
                    Configuration
    */
    const leftShouldHaves: { [dataTestId: string]: { [dataTestId: string]: string; }; } = {
      'node-VF_vGeraldine 0': {
        'node-type-indicator': 'VNF',
        'node-name': 'VF_vGeraldine 0',
        'numberButton': '1',
      },
      'node-vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0': {
        'node-type-indicator': 'M',
        'node-name': 'vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0',
        'numberButton': '',
      },
      'node-vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1': {
        'node-type-indicator': 'M',
        'node-name': 'vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1',
        'numberButton': '1',
      },
      'node-vf_vgeraldine0..VfVgeraldine..vflorence_gpb..module-2': {
        'node-type-indicator': 'M',
        'node-name': 'vf_vgeraldine0..VfVgeraldine..vflorence_gpb..module-2',
        'numberButton': '',
      },
      'node-ExtVL 0': {
        'node-type-indicator': 'N',
        'node-name': 'ExtVL 0',
        'numberButton': '2',
      },
      'node-Port Mirroring Configuration By Policy 0': {
        'node-type-indicator': 'C',
        'node-name': 'Port Mirroring Configuration By Policy 0',
        'numberButton': '',
      },
    };

    for (let node in leftShouldHaves) {
      for (let span in leftShouldHaves[node]) {
        const expected = leftShouldHaves[node][span];
        cy.getElementByDataTestsId(node).find(`[data-tests-id='${span}']`).should(expected ? 'have.text' : 'not.exist', expected);
      }
    }

    /*
    2. Right tree -> VNF with 2 vf modules
                     2 networks
                     IGNORE, don't check: first node, which have no Service connection
    */
    const rightShouldHaves: { [dataTestId: string]: { [dataTestId: string]: string; }; } = {
      'node-d6557200-ecf2-4641-8094-5393ae3aae60-VF_vGeraldine 0:0': {
        'node-type-indicator': 'VNF',
        'node-name': 'VNF2_INSTANCE_NAME',
        'status-property-orchStatus': '',
        'status-property-provStatus': '',
        'status-property-inMaint': '',
      },
      'node-dc229cd8-c132-4455-8517-5c1787c18b14-dc229cd8-c132-4455-8517-5c1787c18b14:0': {
        'node-type-indicator': 'M',
        'node-name': 'ss820f_0918_base',
        'status-property-orchStatus': 'Assigned',
        'status-property-provStatus': '',
      },
      'node-522159d5-d6e0-4c2a-aa44-5a542a12a830-vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1:0': {
        'node-type-indicator': 'M',
        'node-name': 'ss820f_0918_db',
        'status-property-orchStatus': 'deleted',
        'status-property-provStatus': '',
        'status-property-inMaint': '',
      },
      'node-ddc3f20c-08b5-40fd-af72-c6d14636b986-ExtVL 0:0': {
        'node-type-indicator': 'N',
        'node-name': 'NETWORK3_INSTANCE_NAME',
        'status-property-orchStatus': 'Assigned',
        'status-property-provStatus': 'nvtprov',
      },
      'node-ddc3f20c-08b5-40fd-af72-c6d14636b986-ExtVL 0:1': {
        'node-type-indicator': 'N',
        'node-name': 'NETWORK4_INSTANCE_NAME',
        'status-property-orchStatus': 'Created',
        'status-property-provStatus': 'preprov',
      },
    };

    for (let node in rightShouldHaves) {
      var [nodeName, nodeEq] = node.split(":");
      for (let span in rightShouldHaves[node]) {
        cy.getElementByDataTestsId(nodeName).eq(+nodeEq).find(`[data-tests-id='${span}']`).should('have.text', rightShouldHaves[node][span]);
      }
    }

    /*
    3. Left to right connections ->
                    vnf: # of instances = 1, click -> vnf selected
                    vf module1: # of instances = 1, click -> 1 vfmodule "ss820f_0918_db" selected
                    vf module2: # of instances = 0, click -> nothing
                    vf module3: # of instances = 0, click -> nothing
                    network: # of instances = 2, click -> 2 networks selected
                    configuration: # of instances = 0, click -> nothing
    */
    const leftShouldHighlight: { [text: string]: string[] } = {
      'VF_vGeraldine 0': ['VNF2_INSTANCE_NAME'],
      'vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0': [],
      'vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1': ['ss820f_0918_db'],
      'vf_vgeraldine0..VfVgeraldine..vflorence_gpb..module-2': [],
      'ExtVL 0': ['NETWORK3_INSTANCE_NAME', 'NETWORK4_INSTANCE_NAME'],
      'Port Mirroring Configuration By Policy 0': [],
    };

    for (let text in leftShouldHighlight) {
      cy.get('available-models-tree').contains(text).click();

      cy.get('.node-content-wrapper-active').find(`[data-tests-id='node-name']`).should('have.text', text + leftShouldHighlight[text].join(''));
    }

    /*
    4. Right to left connections ->
                    vnf: click -> vnf selected
                    vf module1: click -> nothing
                    vf module2: click -> vemme0 selected
                    network1: click -> network selected
                    network2: click -> network selected
     */
    const rightShouldHighlight: { [text: string]: string[] } = {
      'VNF2_INSTANCE_NAME': ['VF_vGeraldine 0'],
      'ss820f_0918_base': [],
      'ss820f_0918_db': ['vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1'],
      'NETWORK3_INSTANCE_NAME': ['ExtVL 0'],
      'NETWORK4_INSTANCE_NAME': ['ExtVL 0'],
    };

    for (let node in rightShouldHighlight) {
      cy.get('drawing-board-tree').contains(node).click();
      cy.get('.node-content-wrapper-active').find(`[data-tests-id='node-name']`).should('have.text', rightShouldHighlight[node].join('') + node);
    }
    cy.getElementByDataTestsId('isViewOnly-status-test').contains('VIEW ONLY');


    /*
      5. Click outside should remove highlight from all trees.
     */

    cy.clickOutside('search-left-tree-input', ()=>{
      cy.get('.node-content-wrapper-active.node-content-wrapper-focused').should('have.length', 2)
    },  ()=>{
      cy.get('.node-content-wrapper-active.node-content-wrapper-focused').should('have.length', 0);
    });


  });

  function testComponentInfoForVNF(){
    const labelsAndValuesForModel = [
      ['Model version', '2.0'],
      ['Model customization ID', '91415b44-753d-494c-926a-456a9172bbb9'],
      ['Min instances', '0'],
      ['Max instances', 'Unlimited (default)']
    ];
    const extraLabelsAndValuesForInstance = [['Instance type', 'VNF2_INSTANCE_TYPE'],['In maintenance','true'], ['Instance ID', 'VNF2_INSTANCE_ID']];
    testComponentInfoByType('node-VF_vGeraldine 0', labelsAndValuesForModel,'VNF INFO',
      'VNF2_INSTANCE_NAME', extraLabelsAndValuesForInstance,'VNF Instance INFO');

  }

  function testComponentInfoForVFMODULE(){
    const labelsAndValuesForModel = [
      ['Model version', '2'],
      ['Model customization ID', '55b1be94-671a-403e-a26c-667e9c47d091'],
      ['Base module', 'false'],
      ['Min instances', '0'],
      ['Max instances', 'Unlimited (default)'],
      ['Initial instances count', '0']
    ];
    const extraLabelsAndValuesForInstance = [['In maintenance','true'], ['Instance ID', '2c1ca484-cbc2-408b-ab86-25a2c15ce280']];
    testComponentInfoByType('node-vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1',labelsAndValuesForModel,'VFModule INFO',
      'ss820f_0918_db',extraLabelsAndValuesForInstance,'VFModule Instance INFO')

  }

  function testComponentInfoForNetwork(){
    const labelsAndValuesForModel = [
      ['Model version', '37.0'],
      ['Min instances', '0'],
      ['Max instances', 'Unlimited (default)'],
      ['Model customization ID', '94fdd893-4a36-4d70-b16a-ec29c54c184f'],
      ['Network role','network role 1, network role 2']
    ];
    const extraLabelsAndValuesForInstance = [['In maintenance','false'], ['Instance ID', 'NETWORK3_INSTANCE_ID'],['Instance type', 'CONTRAIL30_BASIC']];
    testComponentInfoByType('node-ExtVL 0',labelsAndValuesForModel,'Network INFO',
      'NETWORK3_INSTANCE_NAME',extraLabelsAndValuesForInstance,'Network Instance INFO');

  }

  function testComponentInfoByType(leftNode:string, labelsAndValuesForModel: string[][], expectedTitleForModel:string, rightNode:string, extraLabelsAndValuesForInstance: string[][], expectedTitleForInstance:string){
    cy.getElementByDataTestsId(leftNode).eq(0).click({force: true});
    cy.assertComponentInfoTitleLabelsAndValues(expectedTitleForModel, labelsAndValuesForModel);

    let labelsAndValuesForInstance = labelsAndValuesForModel.concat(extraLabelsAndValuesForInstance);
    cy.get('drawing-board-tree').contains(rightNode).click();
    cy.assertComponentInfoTitleLabelsAndValues(expectedTitleForInstance, labelsAndValuesForInstance);
  }


});
