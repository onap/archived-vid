import {JsonBuilder} from "../../support/jsonBuilders/jsonBuilder";
import {ServiceModel} from "../../support/jsonBuilders/models/service.model";
import {AsyncInstantiationModel} from "../../support/jsonBuilders/models/asyncInstantiation.model";

describe('Create instance : from service instance, from drawing board, with order number', function () {
  var jsonBuilderInstantiationBuilder: JsonBuilder<AsyncInstantiationModel> = new JsonBuilder<AsyncInstantiationModel>();
  var asyncRes: Array<any>;
  beforeEach(() => {
    cy.clearSessionStorage();
    cy.setReduxState();
    cy.preventErrorsOnLoading();
    cy.initAAIMock();
    cy.initVidMock();
    cy.initActiveNetworks();
    cy.setTestApiParamToGR();
    cy.permissionVidMock();
    jsonBuilderInstantiationBuilder.basicMock('cypress/support/jsonBuilders/mocks/jsons/asyncInstantiation.json',
      Cypress.config('baseUrl') + "/asyncInstantiation**",
      (res: any) => {
        asyncRes = res;
        return res;
      });
    cy.login();
  });

  afterEach(() => {
    cy.screenshot();
  });

  describe('multiple scenario tests', () => {


    it('should create VF modules automatically with order number as 1 for the base module,' +
      'for service instance with 1 VNF with a required VF module', () => {
      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/basicReduxForTestingPosition.json').then((res) => {
        res.service.serviceHierarchy["2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd"].vnfs["2017-488_PASQUALE-vPE 0"].properties['min_instances'] = 3;
        cy.setReduxState(<any>res);
        cy.addAlacarteService().then(() => {
          cy.wait(2000);
          cy.visit("welcome.htm").then(() => {
            cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
            cy.getElementByDataTestsId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_PASQUALE-vPE 0').should('exist');
            cy.getElementByDataTestsId('node-f8360508-3f17-4414-a2ed-6bc71161e8db-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0').should('have.length', 1);
            cy.getElementByDataTestsId('node-f8360508-3f17-4414-a2ed-6bc71161e8db-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0').find(`[data-tests-id='node-position-indicator']`).should('have.text', '1');
            cy.getElementByDataTestsId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_PASQUALE-vPE 0-menu-btn').click({force: true}).then(() => {
              cy.getElementByDataTestsId('context-menu-edit').click().then(() => {
                cy.fillVnfPopup();
              })
            })

          });
        });
      });
    });

    it('should Add 2 more VF module instances with the order number is calculated properly', () => {
      let res = getReduxWithVNFS();
      cy.setReduxState(<any>res);
      cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
      cy.wait(3000);
      cy.getElementByDataTestsId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_PASQUALE-vPE 0').should('exist');
      cy.getElementByDataTestsId('node-f8360508-3f17-4414-a2ed-6bc71161e8db-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0').should('have.length', 1);
      cy.getElementByDataTestsId('node-f8360508-3f17-4414-a2ed-6bc71161e8db-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0').find(`[data-tests-id='node-position-indicator']`).should('have.text', '1');
      cy.getElementByDataTestsId('node-f8360508-3f17-4414-a2ed-6bc71161e8db-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0-menu-btn')
        .click({force: true}).then(() => {
        cy.getElementByDataTestsId('context-menu-edit').click().then(() => {
          cy.getElementByDataTestsId('form-set').click();
        })
      })
      const vnfName = "2017-488_PASQUALE-vPE 0";
      let vfModulesNames: Array<string> = [
        '2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1',
        '2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2'
      ];
      const uuidAndVfModuleNames: Array<string> = [
        '25284168-24bb-4698-8cb4-3f509146eca5-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1',
        '0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2'
      ];
      addALaCarteVfModuleEcompGeneratedNamingTrue(vnfName, vfModulesNames[0], uuidAndVfModuleNames[0]);
      cy.getElementByDataTestsId('node-25284168-24bb-4698-8cb4-3f509146eca5-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1').find(`[data-tests-id='node-position-indicator']`).should('have.text', '2');
      cy.getElementByDataTestsId('node-25284168-24bb-4698-8cb4-3f509146eca5-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1-menu-btn')
        .click({force: true}).then(() => {
        cy.wait(1000);
        cy.getElementByDataTestsId('context-menu-edit').click().then(() => {
          cy.getElementByDataTestsId('form-set').click();
        })
      })
      addALaCarteVfModuleEcompGeneratedNamingTrue(vnfName, vfModulesNames[1], uuidAndVfModuleNames[1]);
      cy.getElementByDataTestsId('node-0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2').find(`[data-tests-id='node-position-indicator']`).should('have.text', '3');
      cy.getElementByDataTestsId('node-0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2-menu-btn')
        .click({force: true}).then(() => {
          cy.wait(1000);
        cy.getElementByDataTestsId('context-menu-edit').click().then(() => {
          cy.getElementByDataTestsId('form-set').click();
        })
      })
      });

    it('should Add 1 more VF module instance of module 1', () => {
      let res = getReduxWithVNFS(); //get with 3 module instances
      (<any> res.service.serviceInstance)['2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd'].vnfs['2017-488_PASQUALE-vPE 0'].vfModules = {
        "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
          "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0dcclk": {
            "instanceName": null,
            "rollbackOnFailure": true,
            "sdncPreLoad": null,
            "instanceParams": [
              {}
            ],
            "modelInfo": {
              "modelInvariantId": "b34833bb-6aa9-4ad6-a831-70b06367a091",
              "modelVersionId": "f8360508-3f17-4414-a2ed-6bc71161e8db",
              "modelName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
              "modelVersion": "5",
              "modelCustomizationId": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3",
              "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
              "uuid": "f8360508-3f17-4414-a2ed-6bc71161e8db",
              "modelUniqueId": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3"
            },
            "uuid": "f8360508-3f17-4414-a2ed-6bc71161e8db",
            "isMissingData": false,
            "position": 1
          }
        },
        "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
          "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1nbuwm": {
            "instanceName": null,
            "volumeGroupName": null,
            "rollbackOnFailure": true,
            "sdncPreLoad": null,
            "instanceParams": [
              {
                "pasqualevpe0_bandwidth": "10",
                "2017488_pasqualevpe0_vnf_instance_name": "mtnj309me6",
                "2017488_pasqualevpe0_vnf_config_template_version": "17.2",
                "2017488_pasqualevpe0_AIC_CLLI": "ATLMY8GA",
                "pasqualevpe0_bandwidth_units": "Gbps"
              }
            ],
            "modelInfo": {
              "modelInvariantId": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
              "modelVersionId": "25284168-24bb-4698-8cb4-3f509146eca5",
              "modelName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
              "modelVersion": "6",
              "modelCustomizationId": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401",
              "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
              "uuid": "25284168-24bb-4698-8cb4-3f509146eca5",
              "modelUniqueId": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401"
            },
            "uuid": "25284168-24bb-4698-8cb4-3f509146eca5",
            "isMissingData": false,
            "position": 2
          }
        },
        "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2": {
          "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2tleon": {
            "instanceName": null,
            "volumeGroupName": null,
            "rollbackOnFailure": true,
            "sdncPreLoad": null,
            "instanceParams": [
              {}
            ],
            "modelInfo": {
              "modelInvariantId": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
              "modelVersionId": "0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a",
              "modelName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
              "modelVersion": "6",
              "modelCustomizationId": "3cd946bb-50e0-40d8-96d3-c9023520b557",
              "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
              "uuid": "0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a",
              "modelUniqueId": "3cd946bb-50e0-40d8-96d3-c9023520b557"
            },
            "uuid": "0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a",
            "isMissingData": false,
            "position": 3
          }
        }
      };
      cy.setReduxState(<any>res);
      cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');

      const vnfName = "2017-488_PASQUALE-vPE 0";
      let vfModulesNames: Array<string> = [
        '2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1'
      ];
      const uuidAndVfModuleNames: Array<string> = [
        '25284168-24bb-4698-8cb4-3f509146eca5-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1'
      ];

      cy.getElementByDataTestsId('node-' + vnfName).click({force: true}).then(() => {
        cy.getElementByDataTestsId('node-' + vfModulesNames[0] + '-add-btn').click({force: true})
      });
      //addALaCarteVfModuleEcompGeneratedNamingTrue(vnfName, vfModulesNames[0], uuidAndVfModuleNames[0]);
      cy.getElementByDataTestsId('node-'+uuidAndVfModuleNames[0]).should('have.length', '2');


    });


    it('should not display order number on drawing board if the flag is set to false', () => {
      let res = getReduxWithVNFS();
      (<any> res.global.flags)['FLAG_2008_DISABLE_DRAG_FOR_BASE_MODULE'] = false;
      (<any> res.global.flags)['FLAG_2008_CREATE_VFMODULE_INSTANTIATION_ORDER_NUMBER']=false;

      cy.setReduxState(<any>res);
      cy.visit("welcome.htm").then(() => {
        cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
        cy.get('#node-position-indicator').and('not.be.visible');
      });

      //cy.getElementByDataTestsId('node-f8360508-3f17-4414-a2ed-6bc71161e8db-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0').find(`[data-tests-id='node-position-indicator']`).should('have.text', '1');

    });

    it('should click on deploy and verify the positions of all instances', () => {
      let res = getReduxWithVNFS(); //get with 3 module instances
      (<any> res.service.serviceInstance)['2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd'].vnfs['2017-488_PASQUALE-vPE 0'].vfModules = {

        "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
          "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0dcclk": {
            "instanceName": null,
            "rollbackOnFailure": true,
            "sdncPreLoad": null,
            "instanceParams": [
              {}
            ],
            "modelInfo": {
              "modelInvariantId": "b34833bb-6aa9-4ad6-a831-70b06367a091",
              "modelVersionId": "f8360508-3f17-4414-a2ed-6bc71161e8db",
              "modelName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
              "modelVersion": "5",
              "modelCustomizationId": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3",
              "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
              "uuid": "f8360508-3f17-4414-a2ed-6bc71161e8db",
              "modelUniqueId": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3"
            },
            "uuid": "f8360508-3f17-4414-a2ed-6bc71161e8db",
            "isMissingData": false,
            "position": 1
          }
        },
        "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
          "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1nbuwm": {
            "instanceName": null,
            "volumeGroupName": null,
            "rollbackOnFailure": true,
            "sdncPreLoad": null,
            "instanceParams": [
              {
                "pasqualevpe0_bandwidth": "10",
                "2017488_pasqualevpe0_vnf_instance_name": "mtnj309me6",
                "2017488_pasqualevpe0_vnf_config_template_version": "17.2",
                "2017488_pasqualevpe0_AIC_CLLI": "ATLMY8GA",
                "pasqualevpe0_bandwidth_units": "Gbps"
              }
            ],
            "modelInfo": {
              "modelInvariantId": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
              "modelVersionId": "25284168-24bb-4698-8cb4-3f509146eca5",
              "modelName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
              "modelVersion": "6",
              "modelCustomizationId": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401",
              "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
              "uuid": "25284168-24bb-4698-8cb4-3f509146eca5",
              "modelUniqueId": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401"
            },
            "uuid": "25284168-24bb-4698-8cb4-3f509146eca5",
            "isMissingData": false,
            "position": 2
          },
          "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1owfay": {
            "isMissingData": false,
            "sdncPreReload": null,
            "modelInfo": {
              "modelType": "VFmodule",
              "modelInvariantId": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
              "modelVersionId": "25284168-24bb-4698-8cb4-3f509146eca5",
              "modelName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
              "modelVersion": "6",
              "modelCustomizationId": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401",
              "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
              "modelUniqueId": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401"
            },
            "instanceParams": [
              {
                "pasqualevpe0_bandwidth": "10",
                "2017488_pasqualevpe0_vnf_instance_name": "mtnj309me6",
                "2017488_pasqualevpe0_vnf_config_template_version": "17.2",
                "2017488_pasqualevpe0_AIC_CLLI": "ATLMY8GA",
                "pasqualevpe0_bandwidth_units": "Gbps"
              }
            ],
            "trackById": "u3xrxb65ff",
            "rollbackOnFailure": true,
            "position": 4,
            "action": "Create"
          }
        },
        "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2": {
          "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2tleon": {
            "instanceName": null,
            "volumeGroupName": null,
            "rollbackOnFailure": true,
            "sdncPreLoad": null,
            "instanceParams": [
              {}
            ],
            "modelInfo": {
              "modelInvariantId": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
              "modelVersionId": "0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a",
              "modelName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
              "modelVersion": "6",
              "modelCustomizationId": "3cd946bb-50e0-40d8-96d3-c9023520b557",
              "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
              "uuid": "0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a",
              "modelUniqueId": "3cd946bb-50e0-40d8-96d3-c9023520b557"
            },
            "uuid": "0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a",
            "isMissingData": false,
            "position": 3
          }
        }
      };
      cy.setReduxState(<any>res);
      cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');

      const vnfName = "2017-488_PASQUALE-vPE 0";
      let vfModulesNames: Array<string> = [
        '2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0',
        '2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1',
        '2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2'
      ];


      mockAsyncBulkResponse();
      cy.wait(1000);
      cy.getDrawingBoardDeployBtn().click();

      cy.wait('@expectedPostAsyncInstantiation')
        .then(xhr => {
          let vfModules = bodyOf(xhr).vnfs['2017-488_PASQUALE-vPE 0'].vfModules;

          let baseVModule = vfModules[vfModulesNames[0]];
          const baseVModuleObject = baseVModule[Object.keys(baseVModule)[0]];
          expect(baseVModuleObject.position).equals(1);

          let secondVModule = vfModules[vfModulesNames[1]];
          const secondVModuleObject = secondVModule[Object.keys(secondVModule)[0]];
          expect(secondVModuleObject.position).equals(2);

          let thirdVModule = vfModules[vfModulesNames[2]];
          const thirdVModuleObject = thirdVModule[Object.keys(thirdVModule)[0]];
          expect(thirdVModuleObject.position).equals(3);

          let secondVModuleInstance2 = vfModules[vfModulesNames[1]];
          const secondVModuleObjectInstance2 = secondVModuleInstance2[Object.keys(secondVModuleInstance2)[1]];
          expect(secondVModuleObjectInstance2.position).equals(4);

        });
      cy.openIframe('app/ui/#/instantiationStatus');


    });

    function getJobIconClass(status: string) : string{
      switch(`${status}`.toUpperCase()) {
        case  'PENDING' :
          return "pending";
        case  'IN_PROGRESS' :
          return  "in_progress";
        case  'PAUSED' :
          return "pause";
        case  'FAILED' :
          return "x-circle-o";
        case  'COMPLETED' :
          return "success-circle-o";
        case  'STOPPED' :
          return "stop";
        case  'COMPLETED_WITH_ERRORS' :
          return "success_with_warning";
        case  'COMPLETED_AND_PAUSED' :
          return "stopped-upon-success";
        default:
          return "question-mark-circle-o";
      }
    }

    function bodyOf(xhr: Cypress.WaitXHR) {
      return JSON.parse(JSON.stringify(xhr.request.body));
    }

    function mockAsyncBulkResponse() {
      cy.server().route({
        url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
        method: 'POST',
        status: 200,
        response: "[]",
      }).as("expectedPostAsyncInstantiation");
    }

    function getReduxWithVNFS() {
      return {
        "global": {
          "name": null,
          "type": "UPDATE_DRAWING_BOARD_STATUS",
          "flags": {
            "EMPTY_DRAWING_BOARD_TEST": false,
            "FLAG_NETWORK_TO_ASYNC_INSTANTIATION": false,
            "FLAG_ADD_MSO_TESTAPI_FIELD": true,
            "FLAG_SERVICE_MODEL_CACHE": false,
            "FLAG_SHOW_ASSIGNMENTS": true,
            "FLAG_FABRIC_CONFIGURATION_ASSIGNMENTS": true,
            "FLAG_A_LA_CARTE_AUDIT_INFO": true,
            "FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST": true,
            "FLAG_PRESENT_PROVIDER_NETWORKS_ASSOCIATIONS": true,
            "FLAG_1902_NEW_VIEW_EDIT": true,
            "FLAG_VF_MODULE_RESUME_STATUS_CREATE": true,
            "FLAG_1906_COMPONENT_INFO": true,
            "FLAG_1908_RESUME_MACRO_SERVICE": true,
            "FLAG_ENABLE_WEBPACK_MODERN_UI": true,
            "FLAG_FLASH_REPLACE_VF_MODULE": true,
            "FLAG_FLASH_MORE_ACTIONS_BUTTON_IN_OLD_VIEW_EDIT": true,
            "FLAG_1911_INSTANTIATION_ORDER_IN_ASYNC_ALACARTE": true,
            "FLAG_1911_INSTANTIATION_ORDER_BUTTON_IN_ASYNC_ALACARTE": true,
            "FLAG_2002_VNF_PLATFORM_MULTI_SELECT": true,
            "FLAG_2002_VFM_UPGRADE_ADDITIONAL_OPTIONS": true,
            "FLAG_2004_INSTANTIATION_STATUS_FILTER": true,
            "FLAG_2004_INSTANTIATION_TEMPLATES_POPUP": true,
            "FLAG_2002_UNLIMITED_MAX": true,
            "FLAG_2004_CREATE_ANOTHER_INSTANCE_FROM_TEMPLATE": true,
            "FLAG_2006_VFM_SDNC_PRELOAD_FILES": true,
            "FLAG_2006_VFMODULE_TAKES_TENANT_AND_REGION_FROM_VNF": true,
            "FLAG_2006_NETWORK_PLATFORM_MULTI_SELECT": true,
            "FLAG_2006_NETWORK_LOB_MULTI_SELECT": true,
            "FLAG_2006_NEW_VIEW_EDIT_BUTTON_IN_INSTANTIATION_STATUS": true,
            "FLAG_2006_PAUSE_VFMODULE_INSTANTIATION_CREATION": true,
            "FLAG_2008_DISABLE_DRAG_FOR_BASE_MODULE": true,
            "FLAG_2008_CREATE_VFMODULE_INSTANTIATION_ORDER_NUMBER": true
          },
          "drawingBoardStatus": "CREATE"
        },
        "service": {
          "serviceHierarchy": {
            "2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd": {
              "service": {
                "uuid": "2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd",
                "invariantUuid": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
                "name": "action-data",
                "version": "1.0",
                "toscaModelURL": null,
                "category": "",
                "serviceType": "",
                "serviceRole": "",
                "description": "",
                "serviceEcompNaming": "true",
                "instantiationType": "A-La-Carte",
                "vidNotions": {
                  "instantiationType": "A-La-Carte"
                },
                "inputs": {
                  "2017488_pasqualevpe0_ASN": {
                    "type": "string",
                    "description": "AV/PE",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "AV_vPE"
                  }
                }
              },
              "vnfs": {
                "2017-488_PASQUALE-vPE 0": {
                  "uuid": "69e09f68-8b63-4cc9-b9ff-860960b5db09",
                  "invariantUuid": "72e465fe-71b1-4e7b-b5ed-9496118ff7a8",
                  "description": "Name PASQUALE vPE Description The provider edge function for the PASQUALE service supported by the Junipers VMX product Category Router Vendor Juniper Vendor Release Code 17.2 Owners Mary Fragale. Updated 9-25 to use v8.0 of the Juniper Valid 2 VLM",
                  "name": "2017-488_PASQUALE-vPE",
                  "version": "5.0",
                  "customizationUuid": "1da7b585-5e61-4993-b95e-8e6606c81e45",
                  "inputs": {},
                  "commands": {},
                  "properties": {
                    "vmxvre_retype": "RE-VMX",
                    "vnf_config_template_version": "get_input:2017488_pasqualevpe0_vnf_config_template_version",
                    "sriov44_net_id": "48d399b3-11ee-48a8-94d2-f0ea94d6be8d",
                    "int_ctl_net_id": "2f323477-6936-4d01-ac53-d849430281d9",
                    "vmxvpfe_sriov41_0_port_mac": "00:11:22:EF:AC:DF",
                    "int_ctl_net_name": "VMX-INTXI",
                    "vmx_int_ctl_prefix": "10.0.0.10",
                    "sriov43_net_id": "da349ca1-6de9-4548-be88-2d88e99bfef5",
                    "sriov42_net_id": "760669ba-013d-4d9b-b0e7-4151fe2e6279",
                    "sriov41_net_id": "25ad52d5-c165-40f8-b3b0-ddfc2373280a",
                    "nf_type": "vPE",
                    "vmxvpfe_int_ctl_ip_1": "10.0.0.10",
                    "is_AVPN_service": "false",
                    "vmx_RSG_name": "vREXI-affinity",
                    "vmx_int_ctl_forwarding": "l2",
                    "vmxvre_oam_ip_0": "10.0.0.10",
                    "vmxvpfe_sriov44_0_port_mac": "00:11:22:EF:AC:DF",
                    "vmxvpfe_sriov41_0_port_vlanstrip": "false",
                    "vmxvpfe_sriov42_0_port_vlanfilter": "4001",
                    "vmxvpfe_sriov44_0_port_unknownunicastallow": "true",
                    "vmxvre_image_name_0": "VRE-ENGINE_17.2-S2.1.qcow2",
                    "vmxvre_instance": "0",
                    "vmxvpfe_sriov43_0_port_mac": "00:11:22:EF:AC:DF",
                    "vmxvre_flavor_name": "ns.c1r16d32.v5",
                    "vmxvpfe_volume_size_0": "40.0",
                    "vmxvpfe_sriov43_0_port_vlanfilter": "4001",
                    "nf_naming": "{ecomp_generated_naming=true}",
                    "nf_naming_code": "Navneet",
                    "vmxvre_name_0": "vREXI",
                    "vmxvpfe_sriov42_0_port_vlanstrip": "false",
                    "vmxvpfe_volume_name_0": "vPFEXI_FBVolume",
                    "vmx_RSG_id": "bd89a33c-13c3-4a04-8fde-1a57eb123141",
                    "vmxvpfe_image_name_0": "VPE_ROUTING-ENGINE_17.2R1-S2.1.qcow2",
                    "vmxvpfe_sriov43_0_port_unknownunicastallow": "true",
                    "vmxvpfe_sriov44_0_port_unknownmulticastallow": "true",
                    "vmxvre_console": "vidconsole",
                    "vmxvpfe_sriov44_0_port_vlanfilter": "4001",
                    "vmxvpfe_sriov42_0_port_mac": "00:11:22:EF:AC:DF",
                    "vmxvpfe_volume_id_0": "47cede15-da2f-4397-a101-aa683220aff3",
                    "vmxvpfe_sriov42_0_port_unknownmulticastallow": "true",
                    "vmxvpfe_sriov44_0_port_vlanstrip": "false",
                    "vf_module_id": "123",
                    "nf_function": "JAI",
                    "vmxvpfe_sriov43_0_port_unknownmulticastallow": "true",
                    "vmxvre_int_ctl_ip_0": "10.0.0.10",
                    "ecomp_generated_naming": "true",
                    "AIC_CLLI": "get_input:2017488_pasqualevpe0_AIC_CLLI",
                    "vnf_name": "mtnj309me6vre",
                    "vmxvpfe_sriov41_0_port_unknownunicastallow": "true",
                    "vmxvre_volume_type_1": "HITACHI",
                    "vmxvpfe_sriov44_0_port_broadcastallow": "true",
                    "vmxvre_volume_type_0": "HITACHI",
                    "vmxvpfe_volume_type_0": "HITACHI",
                    "vmxvpfe_sriov43_0_port_broadcastallow": "true",
                    "bandwidth_units": "get_input:pasqualevpe0_bandwidth_units",
                    "vnf_id": "123",
                    "vmxvre_oam_prefix": "24",
                    "availability_zone_0": "mtpocfo-kvm-az01",
                    "ASN": "get_input:2017488_pasqualevpe0_ASN",
                    "vmxvre_chassis_i2cid": "161",
                    "vmxvpfe_name_0": "vPFEXI",
                    "bandwidth": "get_input:pasqualevpe0_bandwidth",
                    "availability_zone_max_count": "1",
                    "vmxvre_volume_size_0": "45.0",
                    "vmxvre_volume_size_1": "50.0",
                    "vmxvpfe_sriov42_0_port_broadcastallow": "true",
                    "vmxvre_oam_gateway": "10.0.0.10",
                    "vmxvre_volume_name_1": "vREXI_FAVolume",
                    "vmxvre_ore_present": "0",
                    "vmxvre_volume_name_0": "vREXI_FBVolume",
                    "vmxvre_type": "0",
                    "vnf_instance_name": "get_input:2017488_pasqualevpe0_vnf_instance_name",
                    "vmxvpfe_sriov41_0_port_unknownmulticastallow": "true",
                    "oam_net_id": "b95eeb1d-d55d-4827-abb4-8ebb94941429",
                    "vmx_int_ctl_len": "24",
                    "vmxvpfe_sriov43_0_port_vlanstrip": "false",
                    "vmxvpfe_sriov41_0_port_broadcastallow": "true",
                    "vmxvre_volume_id_1": "6e86797e-03cd-4fdc-ba72-2957119c746d",
                    "vmxvpfe_sriov41_0_port_vlanfilter": "4001",
                    "nf_role": "Testing",
                    "vmxvre_volume_id_0": "f4eacb79-f687-4e9d-b760-21847c8bb15a",
                    "vmxvpfe_sriov42_0_port_unknownunicastallow": "true",
                    "vmxvpfe_flavor_name": "ns.c20r16d25.v5",
                    "min_instances": 3
                  },
                  "type": "VF",
                  "modelCustomizationName": "2017-488_PASQUALE-vPE 0",
                  "vfModules": {
                    "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
                      "uuid": "25284168-24bb-4698-8cb4-3f509146eca5",
                      "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                      "customizationUuid": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401",
                      "description": null,
                      "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                      "version": "6",
                      "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                      "properties": {
                        "minCountInstances": 0,
                        "maxCountInstances": null,
                        "initialCount": 0,
                        "vfModuleLabel": "PASQUALE_vRE_BV",
                        "baseModule": false
                      },
                      "inputs": {
                        "pasqualevpe0_bandwidth": {
                          "type": "string",
                          "description": "Requested VPE bandwidth",
                          "entry_schema": null,
                          "inputProperties": {
                            "sourceType": "HEAT",
                            "vfModuleLabel": "PASQUALE_vRE_BV",
                            "paramName": "bandwidth"
                          },
                          "constraints": null,
                          "required": true,
                          "default": "10"
                        },
                        "2017488_pasqualevpe0_vnf_instance_name": {
                          "type": "string",
                          "description": "The hostname assigned to the vpe.",
                          "entry_schema": null,
                          "inputProperties": {
                            "sourceType": "HEAT",
                            "vfModuleLabel": "PASQUALE_vRE_BV",
                            "paramName": "vnf_instance_name"
                          },
                          "constraints": null,
                          "required": true,
                          "default": "mtnj309me6"
                        },
                        "2017488_pasqualevpe0_vnf_config_template_version": {
                          "type": "string",
                          "description": "VPE Software Version",
                          "entry_schema": null,
                          "inputProperties": {
                            "sourceType": "HEAT",
                            "vfModuleLabel": "PASQUALE_vRE_BV",
                            "paramName": "vnf_config_template_version"
                          },
                          "constraints": null,
                          "required": true,
                          "default": "17.2"
                        },
                        "2017488_pasqualevpe0_AIC_CLLI": {
                          "type": "string",
                          "description": "AIC Site CLLI",
                          "entry_schema": null,
                          "inputProperties": {
                            "sourceType": "HEAT",
                            "vfModuleLabel": "PASQUALE_vRE_BV",
                            "paramName": "AIC_CLLI"
                          },
                          "constraints": null,
                          "required": true,
                          "default": "ATLMY8GA"
                        },
                        "pasqualevpe0_bandwidth_units": {
                          "type": "string",
                          "description": "Units of bandwidth",
                          "entry_schema": null,
                          "inputProperties": {
                            "sourceType": "HEAT",
                            "vfModuleLabel": "PASQUALE_vRE_BV",
                            "paramName": "bandwidth_units"
                          },
                          "constraints": null,
                          "required": true,
                          "default": "Gbps"
                        }
                      },
                      "volumeGroupAllowed": true
                    },
                    "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
                      "uuid": "f8360508-3f17-4414-a2ed-6bc71161e8db",
                      "invariantUuid": "b34833bb-6aa9-4ad6-a831-70b06367a091",
                      "customizationUuid": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3",
                      "description": null,
                      "name": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                      "version": "5",
                      "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                      "properties": {
                        "minCountInstances": 1,
                        "maxCountInstances": 1,
                        "initialCount": 1,
                        "vfModuleLabel": "base_vPE_BV",
                        "baseModule": true
                      },
                      "inputs": {},
                      "volumeGroupAllowed": false
                    },
                    "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2": {
                      "uuid": "0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a",
                      "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
                      "customizationUuid": "3cd946bb-50e0-40d8-96d3-c9023520b557",
                      "description": null,
                      "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                      "version": "6",
                      "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                      "properties": {
                        "minCountInstances": 0,
                        "maxCountInstances": null,
                        "initialCount": 0,
                        "vfModuleLabel": "PASQUALE_vPFE_BV",
                        "baseModule": false
                      },
                      "inputs": {},
                      "volumeGroupAllowed": true
                    }
                  },
                  "volumeGroups": {
                    "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
                      "uuid": "25284168-24bb-4698-8cb4-3f509146eca5",
                      "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                      "customizationUuid": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401",
                      "description": null,
                      "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                      "version": "6",
                      "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                      "properties": {
                        "minCountInstances": 0,
                        "maxCountInstances": null,
                        "initialCount": 0,
                        "vfModuleLabel": "PASQUALE_vRE_BV"
                      },
                      "inputs": {
                        "pasqualevpe0_bandwidth": {
                          "type": "string",
                          "description": "Requested VPE bandwidth",
                          "entry_schema": null,
                          "inputProperties": {
                            "sourceType": "HEAT",
                            "vfModuleLabel": "PASQUALE_vRE_BV",
                            "paramName": "bandwidth"
                          },
                          "constraints": null,
                          "required": true,
                          "default": "10"
                        },
                        "2017488_pasqualevpe0_vnf_instance_name": {
                          "type": "string",
                          "description": "The hostname assigned to the vpe.",
                          "entry_schema": null,
                          "inputProperties": {
                            "sourceType": "HEAT",
                            "vfModuleLabel": "PASQUALE_vRE_BV",
                            "paramName": "vnf_instance_name"
                          },
                          "constraints": null,
                          "required": true,
                          "default": "mtnj309me6"
                        },
                        "2017488_pasqualevpe0_vnf_config_template_version": {
                          "type": "string",
                          "description": "VPE Software Version",
                          "entry_schema": null,
                          "inputProperties": {
                            "sourceType": "HEAT",
                            "vfModuleLabel": "PASQUALE_vRE_BV",
                            "paramName": "vnf_config_template_version"
                          },
                          "constraints": null,
                          "required": true,
                          "default": "17.2"
                        },
                        "2017488_pasqualevpe0_AIC_CLLI": {
                          "type": "string",
                          "description": "AIC Site CLLI",
                          "entry_schema": null,
                          "inputProperties": {
                            "sourceType": "HEAT",
                            "vfModuleLabel": "PASQUALE_vRE_BV",
                            "paramName": "AIC_CLLI"
                          },
                          "constraints": null,
                          "required": true,
                          "default": "ATLMY8GA"
                        },
                        "pasqualevpe0_bandwidth_units": {
                          "type": "string",
                          "description": "Units of bandwidth",
                          "entry_schema": null,
                          "inputProperties": {
                            "sourceType": "HEAT",
                            "vfModuleLabel": "PASQUALE_vRE_BV",
                            "paramName": "bandwidth_units"
                          },
                          "constraints": null,
                          "required": true,
                          "default": "Gbps"
                        }
                      }
                    },
                    "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2": {
                      "uuid": "0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a",
                      "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
                      "customizationUuid": "3cd946bb-50e0-40d8-96d3-c9023520b557",
                      "description": null,
                      "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                      "version": "6",
                      "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                      "properties": {
                        "minCountInstances": 0,
                        "maxCountInstances": null,
                        "initialCount": 0,
                        "vfModuleLabel": "PASQUALE_vPFE_BV"
                      },
                      "inputs": {}
                    }
                  },
                  "vfcInstanceGroups": {}
                }
              },
              "networks": {},
              "collectionResources": {},
              "configurations": {},
              "serviceProxies": {},
              "vfModules": {
                "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
                  "uuid": "25284168-24bb-4698-8cb4-3f509146eca5",
                  "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                  "customizationUuid": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401",
                  "description": null,
                  "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                  "version": "6",
                  "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                  "properties": {
                    "minCountInstances": 0,
                    "maxCountInstances": null,
                    "initialCount": 0,
                    "vfModuleLabel": "PASQUALE_vRE_BV"
                  },
                  "inputs": {
                    "pasqualevpe0_bandwidth": {
                      "type": "string",
                      "description": "Requested VPE bandwidth",
                      "entry_schema": null,
                      "inputProperties": {
                        "sourceType": "HEAT",
                        "vfModuleLabel": "PASQUALE_vRE_BV",
                        "paramName": "bandwidth"
                      },
                      "constraints": null,
                      "required": true,
                      "default": "10"
                    },
                    "2017488_pasqualevpe0_vnf_instance_name": {
                      "type": "string",
                      "description": "The hostname assigned to the vpe.",
                      "entry_schema": null,
                      "inputProperties": {
                        "sourceType": "HEAT",
                        "vfModuleLabel": "PASQUALE_vRE_BV",
                        "paramName": "vnf_instance_name"
                      },
                      "constraints": null,
                      "required": true,
                      "default": "mtnj309me6"
                    },
                    "2017488_pasqualevpe0_vnf_config_template_version": {
                      "type": "string",
                      "description": "VPE Software Version",
                      "entry_schema": null,
                      "inputProperties": {
                        "sourceType": "HEAT",
                        "vfModuleLabel": "PASQUALE_vRE_BV",
                        "paramName": "vnf_config_template_version"
                      },
                      "constraints": null,
                      "required": true,
                      "default": "17.2"
                    },
                    "2017488_pasqualevpe0_AIC_CLLI": {
                      "type": "string",
                      "description": "AIC Site CLLI",
                      "entry_schema": null,
                      "inputProperties": {
                        "sourceType": "HEAT",
                        "vfModuleLabel": "PASQUALE_vRE_BV",
                        "paramName": "AIC_CLLI"
                      },
                      "constraints": null,
                      "required": true,
                      "default": "ATLMY8GA"
                    },
                    "pasqualevpe0_bandwidth_units": {
                      "type": "string",
                      "description": "Units of bandwidth",
                      "entry_schema": null,
                      "inputProperties": {
                        "sourceType": "HEAT",
                        "vfModuleLabel": "PASQUALE_vRE_BV",
                        "paramName": "bandwidth_units"
                      },
                      "constraints": null,
                      "required": true,
                      "default": "Gbps"
                    }
                  },
                  "volumeGroupAllowed": true
                },
                "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
                  "uuid": "f8360508-3f17-4414-a2ed-6bc71161e8db",
                  "invariantUuid": "b34833bb-6aa9-4ad6-a831-70b06367a091",
                  "customizationUuid": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3",
                  "description": null,
                  "name": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                  "version": "5",
                  "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                  "properties": {
                    "minCountInstances": 1,
                    "maxCountInstances": 1,
                    "initialCount": 1,
                    "vfModuleLabel": "PASQUALE_base_vPE_BV"
                  },
                  "inputs": {},
                  "volumeGroupAllowed": false
                },
                "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2": {
                  "uuid": "0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a",
                  "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
                  "customizationUuid": "3cd946bb-50e0-40d8-96d3-c9023520b557",
                  "description": null,
                  "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                  "version": "6",
                  "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                  "properties": {
                    "minCountInstances": 0,
                    "maxCountInstances": null,
                    "initialCount": 0,
                    "vfModuleLabel": "PASQUALE_vPFE_BV"
                  },
                  "inputs": {},
                  "volumeGroupAllowed": true
                }
              },
              "volumeGroups": {
                "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
                  "uuid": "25284168-24bb-4698-8cb4-3f509146eca5",
                  "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                  "customizationUuid": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401",
                  "description": null,
                  "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                  "version": "6",
                  "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                  "properties": {
                    "minCountInstances": 0,
                    "maxCountInstances": null,
                    "initialCount": 0,
                    "vfModuleLabel": "PASQUALE_vRE_BV"
                  },
                  "inputs": {
                    "pasqualevpe0_bandwidth": {
                      "type": "string",
                      "description": "Requested VPE bandwidth",
                      "entry_schema": null,
                      "inputProperties": {
                        "sourceType": "HEAT",
                        "vfModuleLabel": "PASQUALE_vRE_BV",
                        "paramName": "bandwidth"
                      },
                      "constraints": null,
                      "required": true,
                      "default": "10"
                    },
                    "2017488_pasqualevpe0_vnf_instance_name": {
                      "type": "string",
                      "description": "The hostname assigned to the vpe.",
                      "entry_schema": null,
                      "inputProperties": {
                        "sourceType": "HEAT",
                        "vfModuleLabel": "PASQUALE_vRE_BV",
                        "paramName": "vnf_instance_name"
                      },
                      "constraints": null,
                      "required": true,
                      "default": "mtnj309me6"
                    },
                    "2017488_pasqualevpe0_vnf_config_template_version": {
                      "type": "string",
                      "description": "VPE Software Version",
                      "entry_schema": null,
                      "inputProperties": {
                        "sourceType": "HEAT",
                        "vfModuleLabel": "PASQUALE_vRE_BV",
                        "paramName": "vnf_config_template_version"
                      },
                      "constraints": null,
                      "required": true,
                      "default": "17.2"
                    },
                    "2017488_pasqualevpe0_AIC_CLLI": {
                      "type": "string",
                      "description": "AIC Site CLLI",
                      "entry_schema": null,
                      "inputProperties": {
                        "sourceType": "HEAT",
                        "vfModuleLabel": "PASQUALE_vRE_BV",
                        "paramName": "AIC_CLLI"
                      },
                      "constraints": null,
                      "required": true,
                      "default": "ATLMY8GA"
                    },
                    "pasqualevpe0_bandwidth_units": {
                      "type": "string",
                      "description": "Units of bandwidth",
                      "entry_schema": null,
                      "inputProperties": {
                        "sourceType": "HEAT",
                        "vfModuleLabel": "PASQUALE_vRE_BV",
                        "paramName": "bandwidth_units"
                      },
                      "constraints": null,
                      "required": true,
                      "default": "Gbps"
                    }
                  }
                },
                "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2": {
                  "uuid": "0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a",
                  "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
                  "customizationUuid": "3cd946bb-50e0-40d8-96d3-c9023520b557",
                  "description": null,
                  "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                  "version": "6",
                  "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                  "properties": {
                    "minCountInstances": 0,
                    "maxCountInstances": null,
                    "initialCount": 0,
                    "vfModuleLabel": "PASQUALE_vPFE_BV"
                  },
                  "inputs": {}
                }
              },
              "pnfs": {}
            }
          },
          "serviceInstance": {
            "2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd": {
              "action": "Create",
              "isDirty": true,
              "vnfs": {
                "2017-488_PASQUALE-vPE 0": {
                  "action": "Create",
                  "inMaint": false,
                  "rollbackOnFailure": "true",
                  "originalName": "2017-488_PASQUALE-vPE 0",
                  "isMissingData": false,
                  "trackById": "v7t52jd77ve",
                  "vfModules": {
                    "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
                      "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0dcclk": {
                        "isMissingData": true,
                        "sdncPreReload": null,
                        "modelInfo": {
                          "modelType": "VFmodule",
                          "modelInvariantId": "b34833bb-6aa9-4ad6-a831-70b06367a091",
                          "modelVersionId": "f8360508-3f17-4414-a2ed-6bc71161e8db",
                          "modelName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                          "modelVersion": "5",
                          "modelCustomizationId": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3",
                          "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                          "modelUniqueId": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3"
                        },
                        "instanceParams": [
                          {}
                        ],
                        "trackById": "j671h5gcc89",
                        "rollbackOnFailure": true,
                        "action": "Create",
                        "position": 1
                      }
                    }
                  },
                  "vnfStoreKey": "2017-488_PASQUALE-vPE 0",
                  "upgradedVFMSonsCounter": 0,
                  "uuid": "69e09f68-8b63-4cc9-b9ff-860960b5db09",
                  "lcpCloudRegionId": "hvf6",
                  "tenantId": "229bcdc6eaeb4ca59d55221141d01f8e",
                  "lineOfBusiness": "zzz1",
                  "platformName": "xxx1",
                  "modelInfo": {
                    "modelInvariantId": "72e465fe-71b1-4e7b-b5ed-9496118ff7a8",
                    "modelVersionId": "69e09f68-8b63-4cc9-b9ff-860960b5db09",
                    "modelName": "2017-488_PASQUALE-vPE",
                    "modelVersion": "5.0",
                    "modelCustomizationId": "1da7b585-5e61-4993-b95e-8e6606c81e45",
                    "modelCustomizationName": "2017-488_PASQUALE-vPE 0",
                    "uuid": "69e09f68-8b63-4cc9-b9ff-860960b5db09",
                    "modelUniqueId": "1da7b585-5e61-4993-b95e-8e6606c81e45"
                  },
                  "instanceName": null,
                  "productFamilyId": "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
                  "instanceParams": [
                    {}
                  ]
                }
              },
              "vrfs": {},
              "instanceParams": [
                {
                  "2017488_pasqualevpe0_ASN": "AV_vPE"
                }
              ],
              "validationCounter": 0,
              "existingNames": {},
              "existingVNFCounterMap": {
                "1da7b585-5e61-4993-b95e-8e6606c81e45": 1
              },
              "existingVRFCounterMap": {},
              "existingVnfGroupCounterMap": {},
              "existingNetworksCounterMap": {},
              "optionalGroupMembersMap": {},
              "networks": {},
              "vnfGroups": {},
              "bulkSize": 1,
              "isUpgraded": false,
              "upgradedVFMSonsCounter": 0,
              "instanceName": null,
              "globalSubscriberId": "e433710f-9217-458d-a79d-1c7aff376d89",
              "subscriptionServiceType": "TYLER SILVIA",
              "owningEntityId": "aaa1",
              "projectName": "WATKINS",
              "rollbackOnFailure": "true",
              "aicZoneName": null,
              "owningEntityName": "aaa1",
              "testApi": "GR_API",
              "tenantName": null,
              "modelInfo": {
                "modelInvariantId": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
                "modelVersionId": "2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd",
                "modelName": "action-data",
                "modelVersion": "1.0",
                "uuid": "2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd",
                "modelUniqueId": "2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd"
              },
              "isALaCarte": true,
              "name": "action-data",
              "version": "1.0",
              "description": "",
              "category": "",
              "uuid": "2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd",
              "invariantUuid": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
              "serviceType": "",
              "serviceRole": "",
              "vidNotions": {
                "instantiationType": "A-La-Carte"
              },
              "isEcompGeneratedNaming": true,
              "isMultiStepDesign": false
            }
          },
          "lcpRegionsAndTenants": {
            "lcpRegionList": [
              {
                "id": "AAIAIC25",
                "name": "AAIAIC25 (AIC)",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "hvf6",
                "name": "hvf6 (AIC)",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "olson3",
                "name": "olson3 (AIC)",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "olson5a",
                "name": "olson5a (AIC)",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              }
            ],
            "lcpRegionsTenantsMap": {
              "AAIAIC25": [
                {
                  "id": "092eb9e8e4b7412e8787dd091bc58e86",
                  "name": "USP-SIP-IC-24335-T-01",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                }
              ],
              "hvf6": [
                {
                  "id": "bae71557c5bb4d5aac6743a4e5f1d054",
                  "name": "AIN Web Tool-15-D-testalexandria",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "229bcdc6eaeb4ca59d55221141d01f8e",
                  "name": "AIN Web Tool-15-D-STTest2",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "1178612d2b394be4834ad77f567c0af2",
                  "name": "AIN Web Tool-15-D-SSPtestcustome",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "19c5ade915eb461e8af52fb2fd8cd1f2",
                  "name": "AIN Web Tool-15-D-UncheckedEcopm",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "de007636e25249238447264a988a927b",
                  "name": "AIN Web Tool-15-D-dfsdf",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "62f29b3613634ca6a3065cbe0e020c44",
                  "name": "AIN/SMS-16-D-Multiservices1",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "649289e30d3244e0b48098114d63c2aa",
                  "name": "AIN Web Tool-15-D-SSPST66",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "3f21eeea6c2c486bba31dab816c05a32",
                  "name": "AIN Web Tool-15-D-ASSPST47",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "f60ce21d3ee6427586cff0d22b03b773",
                  "name": "CESAR-100-D-sspjg67246",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "8774659e425f479895ae091bb5d46560",
                  "name": "CESAR-100-D-sspjg68359",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "624eb554b0d147c19ff8885341760481",
                  "name": "AINWebTool-15-D-iftach",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "214f55f5fc414c678059c383b03e4962",
                  "name": "CESAR-100-D-sspjg612401",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "c90666c291664841bb98e4d981ff1db5",
                  "name": "CESAR-100-D-sspjg621340",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "ce5b6bc5c7b348e1bf4b91ac9a174278",
                  "name": "sspjg621351cloned",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "b386b768a3f24c8e953abbe0b3488c02",
                  "name": "AINWebTool-15-D-eteancomp",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "dc6c4dbfd225474e9deaadd34968646c",
                  "name": "AINWebTool-15-T-SPFET",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "02cb5030e9914aa4be120bd9ed1e19eb",
                  "name": "AINWebTool-15-X-eeweww",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "f2f3830e4c984d45bcd00e1a04158a79",
                  "name": "CESAR-100-D-spjg61909",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "05b91bd5137f4929878edd965755c06d",
                  "name": "CESAR-100-D-sspjg621512cloned",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "7002fbe8482d4a989ddf445b1ce336e0",
                  "name": "AINWebTool-15-X-vdr",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "4008522be43741dcb1f5422022a2aa0b",
                  "name": "AINWebTool-15-D-ssasa",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "f44e2e96a1b6476abfda2fa407b00169",
                  "name": "AINWebTool-15-D-PFNPT",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "b69a52bec8a84669a37a1e8b72708be7",
                  "name": "AINWebTool-15-X-vdre",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "fac7d9fd56154caeb9332202dcf2969f",
                  "name": "AINWebTool-15-X-NONPODECOMP",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "2d34d8396e194eb49969fd61ffbff961",
                  "name": "DN5242-Nov16-T5",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "cb42a77ff45b48a8b8deb83bb64acc74",
                  "name": "ro-T11",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "fa45ca53c80b492fa8be5477cd84fc2b",
                  "name": "ro-T112",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "4914ab0ab3a743e58f0eefdacc1dde77",
                  "name": "DN5242-Nov21-T1",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "d0a3e3f2964542259d155a81c41aadc3",
                  "name": "test-hvf6-09",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                },
                {
                  "id": "cbb99fe4ada84631b7baf046b6fd2044",
                  "name": "DN5242-Nov16-T3",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                }
              ],
              "olson3": [
                {
                  "id": "cbb99fe4ada84631b7baf046b6fd2XXX",
                  "name": "DN5242-Nov16-T3XXX",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                }
              ],
              "olson5a": [
                {
                  "id": "cbb99fe4ada84631b7baf046b6fd2YYY",
                  "name": "DN5242-Nov16-T3YYY",
                  "isPermitted": true,
                  "cloudOwner": "irma-aic"
                }
              ]
            }
          },
          "subscribers": [
            {
              "id": "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
              "name": "Emanuel",
              "isPermitted": false
            },
            {
              "id": "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fc",
              "name": "JULIO ERICKSON",
              "isPermitted": false
            },
            {
              "id": "e433710f-9217-458d-a79d-1c7aff376d89",
              "name": "SILVIA ROBBINS",
              "isPermitted": true
            }
          ],
          "productFamilies": [
            {
              "id": "ebc3bc3d-62fd-4a3f-a037-f619df4ff034",
              "name": "ERICA",
              "isPermitted": true
            },
            {
              "id": "17cc1042-527b-11e6-beb8-9e71128cae77",
              "name": "IGNACIO",
              "isPermitted": true
            },
            {
              "id": "36b4733a-53f4-4cc8-8ff0-9172e5fc4b8e",
              "name": "Christie",
              "isPermitted": true
            },
            {
              "id": "a4f6f2ae-9bf5-4ed7-b904-06b2099c4bd7",
              "name": "Enhanced Services",
              "isPermitted": true
            },
            {
              "id": "vTerrance",
              "name": "vTerrance",
              "isPermitted": true
            },
            {
              "id": "323d69d9-2efe-4r45-ay0a-89ea7ard4e6f",
              "name": "vEsmeralda",
              "isPermitted": true
            },
            {
              "id": "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
              "name": "Emanuel",
              "isPermitted": true
            },
            {
              "id": "d8a6ed93-251c-47ca-adc9-86671fd19f4c",
              "name": "BVOIP",
              "isPermitted": true
            },
            {
              "id": "db171b8f-115c-4992-a2e3-ee04cae357e0",
              "name": "LINDSEY",
              "isPermitted": true
            },
            {
              "id": "LRSI-OSPF",
              "name": "LRSI-OSPF",
              "isPermitted": true
            },
            {
              "id": "vRosemarie",
              "name": "HNGATEWAY",
              "isPermitted": true
            },
            {
              "id": "vHNPaas",
              "name": "WILKINS",
              "isPermitted": true
            },
            {
              "id": "e433710f-9217-458d-a79d-1c7aff376d89",
              "name": "TYLER SILVIA",
              "isPermitted": true
            },
            {
              "id": "b6a3f28c-eebf-494c-a900-055cc7c874ce",
              "name": "VROUTER",
              "isPermitted": true
            },
            {
              "id": "vMuriel",
              "name": "vMuriel",
              "isPermitted": true
            },
            {
              "id": "0ee8c1bc-7cbd-4b0a-a1ac-e9999255abc1",
              "name": "CARA Griffin",
              "isPermitted": true
            },
            {
              "id": "c7611ebe-c324-48f1-8085-94aef0c6ef3d",
              "name": "DARREN MCGEE",
              "isPermitted": true
            },
            {
              "id": "e30755dc-5673-4b6b-9dcf-9abdd96b93d1",
              "name": "Transport",
              "isPermitted": true
            },
            {
              "id": "vSalvatore",
              "name": "vSalvatore",
              "isPermitted": true
            },
            {
              "id": "d7bb0a21-66f2-4e6d-87d9-9ef3ced63ae4",
              "name": "JOSEFINA",
              "isPermitted": true
            },
            {
              "id": "vHubbard",
              "name": "vHubbard",
              "isPermitted": true
            },
            {
              "id": "12a96a9d-4b4c-4349-a950-fe1159602621",
              "name": "DARREN MCGEE",
              "isPermitted": true
            }
          ],
          "serviceTypes": {
            "e433710f-9217-458d-a79d-1c7aff376d89": [
              {
                "id": "0",
                "name": "vRichardson",
                "isPermitted": false
              },
              {
                "id": "1",
                "name": "TYLER SILVIA",
                "isPermitted": true
              },
              {
                "id": "2",
                "name": "Emanuel",
                "isPermitted": false
              },
              {
                "id": "3",
                "name": "vJamie",
                "isPermitted": false
              },
              {
                "id": "4",
                "name": "vVoiceMail",
                "isPermitted": false
              },
              {
                "id": "5",
                "name": "Kennedy",
                "isPermitted": false
              },
              {
                "id": "6",
                "name": "vPorfirio",
                "isPermitted": false
              },
              {
                "id": "7",
                "name": "vVM",
                "isPermitted": false
              },
              {
                "id": "8",
                "name": "vOTA",
                "isPermitted": false
              },
              {
                "id": "9",
                "name": "vFLORENCE",
                "isPermitted": false
              },
              {
                "id": "10",
                "name": "vMNS",
                "isPermitted": false
              },
              {
                "id": "11",
                "name": "vEsmeralda",
                "isPermitted": false
              },
              {
                "id": "12",
                "name": "VPMS",
                "isPermitted": false
              },
              {
                "id": "13",
                "name": "vWINIFRED",
                "isPermitted": false
              },
              {
                "id": "14",
                "name": "SSD",
                "isPermitted": false
              },
              {
                "id": "15",
                "name": "vMOG",
                "isPermitted": false
              },
              {
                "id": "16",
                "name": "LINDSEY",
                "isPermitted": false
              },
              {
                "id": "17",
                "name": "JOHANNA_SANTOS",
                "isPermitted": false
              },
              {
                "id": "18",
                "name": "vCarroll",
                "isPermitted": false
              }
            ]
          },
          "aicZones": [
            {
              "id": "NFT1",
              "name": "NFTJSSSS-NFT1"
            },
            {
              "id": "JAG1",
              "name": "YUDFJULP-JAG1"
            },
            {
              "id": "YYY1",
              "name": "UUUAIAAI-YYY1"
            },
            {
              "id": "AVT1",
              "name": "AVTRFLHD-AVT1"
            },
            {
              "id": "ATL34",
              "name": "ATLSANAI-ATL34"
            }
          ],
          "categoryParameters": {
            "owningEntityList": [
              {
                "id": "aaa1",
                "name": "aaa1"
              },
              {
                "id": "d61e6f2d-12fa-4cc2-91df-7c244011d6fc",
                "name": "WayneHolland"
              },
              {
                "id": "Melissa",
                "name": "Melissa"
              }
            ],
            "projectList": [
              {
                "id": "WATKINS",
                "name": "WATKINS"
              },
              {
                "id": "x1",
                "name": "x1"
              },
              {
                "id": "yyy1",
                "name": "yyy1"
              }
            ],
            "lineOfBusinessList": [
              {
                "id": "ONAP",
                "name": "ONAP",
                "index": 1
              },
              {
                "id": "zzz1",
                "name": "zzz1",
                "index": 2
              }
            ],
            "platformList": [
              {
                "id": "platform",
                "name": "platform"
              },
              {
                "id": "xxx1",
                "name": "xxx1"
              }
            ]
          },
          "type": "UPDATE_LCP_REGIONS_AND_TENANTS"
        }
      }
      }

      function addALaCarteVfModuleEcompGeneratedNamingTrue(vnfName: string, vfModulesName: string, uuidAndVfModuleName: string): Chainable<any> {
        return cy.getElementByDataTestsId('node-' + vnfName).click({force: true}).then(() => {
          cy.getElementByDataTestsId('node-' + vfModulesName + '-add-btn').click({force: true}).then(() => {
            cy.getElementByDataTestsId('node-' + uuidAndVfModuleName + '-menu-btn')
              .click({force: true, multiple:true}).then(() => {
              cy.getElementByDataTestsId('context-menu-edit').click().then(() => {
                cy.getElementByDataTestsId('form-set').click();
              })
            })
          })
        })
      }




  });
});
