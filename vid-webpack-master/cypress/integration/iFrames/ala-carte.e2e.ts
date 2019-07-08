///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
/// <reference types="Cypress" />
import {JsonBuilder} from '../../support/jsonBuilders/jsonBuilder';
import {ServiceModel} from '../../support/jsonBuilders/models/service.model';
import * as _ from 'lodash';

describe('A la carte', function () {
  describe('check service name', () => {
    let jsonBuilderAAIService: JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();


    beforeEach(() => {
      cy.window().then((win) => {
        win.sessionStorage.clear();
        cy.setReduxState();
        cy.preventErrorsOnLoading();
        cy.initAAIMock();
        cy.initVidMock();
        cy.initAlaCarteService();
        cy.initZones();
        cy.login();
      });
    });

    afterEach(() => {
      cy.screenshot();
    });

    const SERVICE_ID: string = '4d71990b-d8ad-4510-ac61-496288d9078e';
    const INSTANCE_NAME_MANDATORY_MESSAGE: string = 'Missing data ("Instance Name" and 3 other fields';
    const INSTANCE_NAME_NOT_MANDATORY_MESSAGE: string = 'Missing data ("Subscriber Name" and 2 other fields)';
    const CONFIRM_BUTTON: string = 'confirmButton';


    it(`service name should be mandatory : serviceEcompNaming = true`, ()=> {
      cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/basicService.json').then((res) => {
        jsonBuilderAAIService.basicJson(res,
          Cypress.config('baseUrl') + '/rest/models/services/4d71990b-d8ad-4510-ac61-496288d9078e',
          200,
          0,
          SERVICE_ID + ' - service',
          changeServiceEcompNamingToTrue);
        checkServiceNameInputIdMandatory();
      });
    });

    it(`Service a-la-carte`, ()=> {

      const subscriptionServiceType : string = "TYLER SILVIA";
      const owningEntityName : string = "WayneHolland";
      const rollbackOnFailure : string = "true";
      const projectName : string = "WATKINS";
      const instanceName : string = "serviceInstanceName";
      cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/emptyServiceRedux.json').then((res) => {

        cy.setTestApiParamToGR();
        res.service.serviceHierarchy['2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd'].service.vidNotions.instantiationType = 'ALaCarte';
        res.service.serviceHierarchy['2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd'].service.inputs = null;
        cy.setReduxState(<any>res);
        cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');

        cy.getElementByDataTestsId("openMenuBtn").click({force: true})
          .getElementByDataTestsId("context-menu-header-edit-item").click({force: true})
          .getElementByDataTestsId("instanceName")
          .getElementByDataTestsId("subscriberName")
          .getElementByDataTestsId("serviceType")
          .getElementByDataTestsId("owningEntity")
          .getElementByDataTestsId("project")
          .getElementByDataTestsId("rollback");

        cy.selectDropdownOptionByText("subscriberName", "SILVIA ROBBINS");
        cy.selectDropdownOptionByText("serviceType", subscriptionServiceType);
        cy.selectDropdownOptionByText("owningEntity", owningEntityName);
        cy.selectDropdownOptionByText("rollback", rollbackOnFailure);
        cy.selectDropdownOptionByText("project", projectName);
        cy.typeToInput("instanceName", instanceName);

        cy.get('#quantity-select').should('have.attr', 'disabled');
        cy.getElementByDataTestsId('form-set').click({force: true}).then(() => {
          cy.getReduxState().then((state) => {
            const service = state.service.serviceInstance['2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd'];

            cy.readFile('../vid-automation/src/test/resources/a-la-carte/redux-a-la-carte.json').then((file) => {
              expect(service.subscriptionServiceType).to.equals(subscriptionServiceType);
              expect(service.owningEntityName).to.equals(owningEntityName);
              expect(service.rollbackOnFailure).to.equals(rollbackOnFailure);
              expect(service.projectName).to.equals(projectName);
              expect(service.instanceName).to.equals(instanceName);
            });
          });
        });
      });
    });


    it(`VNF a-la-carte`, ()=> {
      cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/emptyServiceRedux.json').then((res) => {
        cy.setTestApiParamToGR();
        res.service.serviceHierarchy['2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd'].service.vidNotions.instantiationType = 'ALaCarte';
        res.service.serviceHierarchy['2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd'].service.inputs = null;
        cy.setReduxState(<any>res);
        cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');

        cy.getElementByDataTestsId("openMenuBtn").click({force: true})
          .getElementByDataTestsId("context-menu-header-edit-item").click({force: true})
          .getElementByDataTestsId("instanceName")
          .getElementByDataTestsId("subscriberName")
          .getElementByDataTestsId("serviceType")
          .getElementByDataTestsId("owningEntity")
          .getElementByDataTestsId("project")
          .getElementByDataTestsId("rollback");

        cy.selectDropdownOptionByText("subscriberName", "SILVIA ROBBINS");
        cy.selectDropdownOptionByText("serviceType", "TYLER SILVIA");
        cy.selectDropdownOptionByText("owningEntity", "WayneHolland");
        cy.selectDropdownOptionByText("rollback", "true");
        cy.selectDropdownOptionByText("project", "WATKINS");
        cy.typeToInput("instanceName", "serviceInstanceName");

        cy.get('#quantity-select').should('have.attr', 'disabled');
        cy.getElementByDataTestsId('form-set').click({force: true}).then(() => {
          cy.getElementByDataTestsId('node-2017-488_PASQUALE-vPE 0-add-btn').click({force: true}).then(() => {
            cy.selectDropdownOptionByText('productFamily', 'Emanuel');
            cy.selectDropdownOptionByText('lcpRegion', 'AAIAIC25');
            cy.typeToInput("lcpRegionText", "just another region");
            cy.selectDropdownOptionByText('tenant', 'USP-SIP-IC-24335-T-01');
            cy.selectDropdownOptionByText('lineOfBusiness', 'zzz1');
            cy.selectDropdownOptionByText('platform', 'xxx1');
            cy.getElementByDataTestsId('form-set').click({force: true}).then(() => {
              cy.getReduxState().then((state) => {

                const vnf = state.service.serviceInstance['2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd'].vnfs['2017-488_PASQUALE-vPE 0'];

                cy.readFile('../vid-automation/src/test/resources/a-la-carte/redux-a-la-carte.json').then((file) => {
                  file.vnfs['2017-488_PASQUALE-vPE 0'].trackById = vnf.trackById;
                  file.vnfs['2017-488_PASQUALE-vPE 0'].vfModules = {};
                  cy.deepCompare(vnf, file.vnfs['2017-488_PASQUALE-vPE 0'])
                });
              });
            });

          });
        });
      });
    });

    it(`Network a-la-carte`, ()=> {
      cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/emptyServiceRedux.json').then((res) => {
        cy.setTestApiParamToGR();
        res.service.serviceHierarchy['2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd'].service.vidNotions.instantiationType = 'ALaCarte';
        res.service.serviceHierarchy['2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd'].service.inputs = null;
        res.service.serviceHierarchy['2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd'].networks = {
          "ExtVL 0": {
            "uuid": "ddc3f20c-08b5-40fd-af72-c6d14636b986",
            "invariantUuid": "379f816b-a7aa-422f-be30-17114ff50b7c",
            "description": "ECOMP generic virtual link (network) base type for all other service-level and global networks",
            "name": "ExtVL",
            "version": "37.0",
            "customizationUuid": "94fdd893-4a36-4d70-b16a-ec29c54c184f",
            "inputs": {},
            "commands": {},
            "properties": {
              "network_assignments": "{is_external_network=false, ipv4_subnet_default_assignment={min_subnets_count=1}, ecomp_generated_network_assignment=false, ipv6_subnet_default_assignment={min_subnets_count=1}}",
              "ecomp_generated_naming": "false",
              "exVL_naming": "{ecomp_generated_naming=true}",
              "network_flows": "{is_network_policy=false, is_bound_to_vpn=false}",
              "network_homing": "{ecomp_selected_instance_node_target=false}"
            },
            "type": "VL",
            "modelCustomizationName": "ExtVL 0"
          }
        };
        res.service.serviceInstance['2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd'].networks = {};
        cy.setReduxState(<any>res);
        cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
        cy.getElementByDataTestsId("node-ExtVL 0-add-btn").click({force: true});
        cy.selectDropdownOptionByText("platform", "xxx1");
        cy.selectDropdownOptionByText("lcpRegion", "AAIAIC25");
        cy.selectDropdownOptionByText("tenant", "USP-SIP-IC-24335-T-01");
        cy.selectDropdownOptionByText("productFamily", "ERICA");
        cy.selectDropdownOptionByText("lineOfBusiness", "zzz1");
        cy.typeToInput("lcpRegionText", "lcpRegionText");

        cy.getElementByDataTestsId('form-set').click({force: true}).then(() => {
          cy.getReduxState().then((state) => {
            const network = state.service.serviceInstance['2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd'].networks['ExtVL 0'];

            cy.readFile('../vid-automation/src/test/resources/a-la-carte/redux-a-la-carte.json').then((file) => {
              file.networks['ExtVL 0'].trackById = network.trackById;
              cy.deepCompare(network, file.networks['ExtVL 0']);
            });
          });
        });
      });
    });

    it(`VFModule a-la-carte`, ()=> {
      var timeBomb = new Date('12/09/2018');
      if (new Date() < timeBomb) {
        return;
      }
      cy.readFile('/cypress/support/jsonBuilders/mocks/jsons/emptyServiceRedux.json').then((res) => {
        cy.setTestApiParamToGR();
        res.service.serviceHierarchy['2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd'].service.vidNotions.instantiationType = 'ALaCarte';
        res.service.serviceHierarchy['2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd'].service.inputs = null;
        cy.setReduxState(<any>res);
        cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');

        cy.getElementByDataTestsId('node-2017-488_PASQUALE-vPE 0-add-btn').click({force: true}).then(() => {
          cy.selectDropdownOptionByText('productFamily', 'Emanuel');
          cy.selectDropdownOptionByText('lcpRegion', 'hvf6');
          cy.selectDropdownOptionByText('tenant', 'AIN Web Tool-15-D-STTest2');
          cy.selectDropdownOptionByText('lineOfBusiness', 'zzz1');
          cy.selectDropdownOptionByText('platform', 'xxx1');
          cy.getElementByDataTestsId('form-set').click({force: true}).then(() => {
            const vnfName = '2017-488_PASQUALE-vPE 0';
            let vfModulesNames: Array<string> = [
              '2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0',
              '2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1',
              '2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2',
            ];

            addVfModule(vnfName, vfModulesNames[0], 'mimazepubi', 'hvf6', '', 'AINWebTool-15-D-iftach', false, false, false)
              .then(() => {
                addVfModule(vnfName, vfModulesNames[1], 'puwesovabe', 'AAIAIC25', 'my region', 'USP-SIP-IC-24335-T-01', true, true, false)
                  .then(() => {
                    addVfModule(vnfName, vfModulesNames[2], 'bnmgtrx', 'hvf6', '', 'AINWebTool-15-D-iftach', false, false, true)
                      .then(() => {
                        cy.getReduxState().then((state) => {
                          const vfModules = state.service.serviceInstance['2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd'].vnfs[vnfName].vfModules;
                          cy.readFile('../vid-automation/src/test/resources/a-la-carte/redux-a-la-carte.json').then((file) => {
                            for (let vfModulesName of vfModulesNames) {
                              const vfModule = vfModules[vfModulesName];
                              let vfModuleObject = vfModule[Object.keys(vfModule)[0]];
                              cy.deepCompare(vfModuleObject, file.vnfs[vnfName].vfModules[vfModulesName][vfModulesName]);
                            }
                          });
                        });
                      });
                  });
              });
          });
        });
      });
    });


    function changeServiceEcompNamingToTrue(obj: ServiceModel) {
      obj.service.serviceEcompNaming = "true";
      return obj;
    }

    function checkServiceNameInputIdMandatory() {
      cy.get('span').contains('Browse ASDC Service Models').click({force: true})
        .getElementByDataTestsId('deploy-' + SERVICE_ID).click({force: true})
        .wait(1000).getElementByDataTestsId(CONFIRM_BUTTON).click({force: true})
        .get('.error').contains(INSTANCE_NAME_MANDATORY_MESSAGE)
        .typeToInput('instanceName', 'testService');

      cy.getElementByDataTestsId(CONFIRM_BUTTON).click({force: true})
        .get('.error').contains(INSTANCE_NAME_NOT_MANDATORY_MESSAGE);
    }

    function addVfModule(vnfName: string, vfModuleName: string, instanceName: string, lcpRegion: string, legacyRegion: string, tenant: string, rollback: boolean, sdncPreLoad: boolean, deleteVgName: boolean): Chainable<any> {
      return cy.getElementByDataTestsId('node-' + vnfName).click({force: true}).then(() => {
        cy.getElementByDataTestsId('node-' + vfModuleName + '-add-btn').click({force: true}).then(() => {
          cy.getElementByDataTestsId('instanceName').clear().type(instanceName, {force: true}).then(() => {
            if (deleteVgName) {
              cy.getElementByDataTestsId('volumeGroupName').clear();
            }
          }).then(() => {
            cy.selectDropdownOptionByText('lcpRegion', lcpRegion);
            if (!_.isEmpty(legacyRegion)) {
              cy.typeToInput("lcpRegionText", legacyRegion);
            }
            cy.selectDropdownOptionByText('tenant', tenant);
            cy.selectDropdownOptionByText('rollback', String(rollback));
            if (sdncPreLoad) {
              cy.getElementByDataTestsId('sdncPreLoad').check();
            }
            cy.getElementByDataTestsId('form-set').click({force: true});
          });
        });
      });
    }

  });
});
