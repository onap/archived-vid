///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
import {ServiceModel} from '../../support/jsonBuilders/models/service.model';
import {JsonBuilder} from '../../support/jsonBuilders/jsonBuilder';

describe('Vnf Groups', function () {
  var jsonBuilderAndMock: JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();

  beforeEach(() => {
      cy.clearSessionStorage();
      cy.setReduxState();
      cy.preventErrorsOnLoading();
      cy.initAAIMock();
      cy.initVidMock();
      cy.permissionVidMock();
      cy.setTestApiParamToGR;
      cy.login();
  });

  afterEach(() => {
    cy.screenshot();
  });

  describe('Vnf Group model basic view', function () {

      it('Vnf group open new view edit', function () {
        const instanceName: string = 'ABC';
        const groupName = 'groupingservicefortest..ResourceInstanceGroup..0';
        const nodeId = 'daeb6568-cef8-417f-9075-ed259ce59f48';
        const serviceId = '4117a0b6-e234-467d-b5b9-fe2f68c8b0fc';
        cy.readFile('../vid-automation/src/test/resources/VnfGroup/groupingServiceRoleResponse.json').then((res) => {
          jsonBuilderAndMock.basicJson(
            res,
            Cypress.config('baseUrl') + "/rest/models/services/" + serviceId,
            200,
            0,
            "ServiceWithVnfGroup",
          );

          cy.buildReduxStateWithServiceRespone(res, serviceId, false);
          cy.openIframe('app/ui/#/servicePlanning?serviceModelId=' + serviceId);
          cy.getElementByDataTestsId('node-' + groupName).find(`[data-tests-id='node-type-indicator']`).contains('G');
          cy.getElementByDataTestsId('node-' + groupName).contains('' + groupName);
          cy.getElementByDataTestsId('node-' + groupName + '-add-btn').get('i').should('have.class', 'fa-plus-circle');
          cy.getElementByDataTestsId('node-' + groupName + '-add-btn').click({force: true});
          cy.getElementByDataTestsId('instanceName').clear();
          cy.typeToInput('instanceName', instanceName);
          cy.getElementByDataTestsId('form-set').click({force: true}).then(() => {
            cy.getElementByDataTestsId('numberButton').contains('1');
            cy.getElementByDataTestsId('node-' + nodeId + '-' + groupName).contains(instanceName);
            cy.getElementByDataTestsId('node-' + nodeId + '-' + groupName + '-menu-btn')
              .click({force: true});
            cy.getElementByDataTestsId('context-menu-edit').click();
            cy.getElementByDataTestsId('instanceName').clear();
            cy.typeToInput('instanceName', instanceName + instanceName);
            cy.getElementByDataTestsId('form-set').click({force: true}).then(() => {
              cy.getElementByDataTestsId('node-' + nodeId + '-' + groupName + '-menu-btn').click({force: true})
                .getElementByDataTestsId('context-menu-remove').click();
            });
          });
        });
      });

      it('Create new service with vnf group', () => {

        const SERVICE_MODEL_ID: string = '4117a0b6-e234-467d-b5b9-fe2f68c8b0fc';
        const GROUP_NAME_TO_DEPLOY = 'groupingservicefortest..ResourceInstanceGroup..1';
        const NODE_ID = 'c2b300e6-45de-4e5e-abda-3032bee2de56';
        let serviceModel: JSON;
        let basicServiceInstance: JSON;

        cy.server().route({
          url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
          method: 'POST',
          status: 200,
          response: "[]",
        }).as("expectedPostAsyncInstantiation");

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/groupingServiceRoleResponse.json').then((res) => {
          serviceModel = res;
        });

        cy.readFile('cypress/support/jsonBuilders/mocks/jsons/vnfGroupBasicServiceInstance.json').then((res) => {
          basicServiceInstance = res;
        });

        cy.readFile('cypress/support/jsonBuilders/mocks/jsons/emptyServiceRedux.json').then((reduxRes) => {

          reduxRes.service.serviceHierarchy[SERVICE_MODEL_ID] = serviceModel;
          reduxRes.service.serviceInstance[SERVICE_MODEL_ID] = basicServiceInstance;

          cy.setReduxState(<any>reduxRes);

          cy.openIframe('app/ui/#/servicePlanning?serviceModelId=' + SERVICE_MODEL_ID);
          cy.getElementByDataTestsId('node-' + GROUP_NAME_TO_DEPLOY).find(`[data-tests-id='node-type-indicator']`).contains('G');
          cy.getElementByDataTestsId('node-' + GROUP_NAME_TO_DEPLOY).contains('' + GROUP_NAME_TO_DEPLOY);
          cy.getElementByDataTestsId('node-' + GROUP_NAME_TO_DEPLOY + '-add-btn').get('i').should('have.class', 'fa-plus-circle');
          cy.getElementByDataTestsId('node-' + GROUP_NAME_TO_DEPLOY + '-add-btn').click({force: true});
          cy.getElementByDataTestsId('node-' + NODE_ID + '-' + GROUP_NAME_TO_DEPLOY + '-menu-btn')
            .click({force: true});
          cy.getElementByDataTestsId('context-menu-edit').click();
          cy.getElementByDataTestsId('instanceName').clear();
          cy.typeToInput('instanceName', "ABC");
          cy.getElementByDataTestsId('form-set').click({force: true});
          cy.getElementByDataTestsId('deployBtn').should('have.text', 'DEPLOY').click();
          cy.getElementByDataTestsId('isViewOnly-status-test').contains('IN DESIGN');
          cy.getReduxState().then((state) => {
            const vnfGroup = state.service.serviceInstance[SERVICE_MODEL_ID].vnfGroups[GROUP_NAME_TO_DEPLOY];

            cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
                cy.readFile('../vid-automation/src/test/resources/VnfGroup/serviceWithVnfGroupCreateRequest.json').then((expectedResult) => {
                expectedResult.vnfGroups[GROUP_NAME_TO_DEPLOY].trackById = vnfGroup.trackById;
                cy.deepCompare(xhr.request.body, expectedResult);
              });
            });
          });

        });

      });

      it('Delete vnf group with members', () => {
        cy.initSearchVNFMemebers();
        const SERVICE_MODEL_ID: string = '4117a0b6-e234-467d-b5b9-fe2f68c8b0fc';
        let serviceModel: JSON;
        let basicServiceInstance: JSON;

        cy.server().route({
          url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
          method: 'POST',
          status: 200,
          response: "[]",
        }).as("expectedPostAsyncInstantiation");

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/groupingServiceRoleResponse.json').then((res) => {
          serviceModel = res;
        });

        cy.readFile('cypress/support/jsonBuilders/mocks/jsons/vnfGroupBasicServiceInstance.json').then((res) => {
          basicServiceInstance = res;
        });

        cy.readFile('cypress/support/jsonBuilders/mocks/jsons/emptyServiceRedux.json').then((reduxRes) => {

          reduxRes.service.serviceHierarchy[SERVICE_MODEL_ID] = serviceModel;
          reduxRes.service.serviceInstance[SERVICE_MODEL_ID] = basicServiceInstance;
          reduxRes.global.genericModalHelper = {};

          cy.setReduxState(<any>reduxRes);

          cy.openIframe('app/ui/#/servicePlanning?serviceModelId=' + SERVICE_MODEL_ID);
          cy.getElementByDataTestsId('node-groupingservicefortest..ResourceInstanceGroup..0-add-btn').click({force: true});
          removeVnfGroup();
          cy.getElementByDataTestsId('node-groupingservicefortest..ResourceInstanceGroup..0-add-btn').click({force: true});
          cy.getElementByDataTestsId('node-daeb6568-cef8-417f-9075-ed259ce59f48-groupingservicefortest..ResourceInstanceGroup..0-menu-btn')
            .click({force: true});
          cy.getElementByDataTestsId('context-menu-addGroupMember').click({force: true}).then(() => {
            cy.get('.allCheckboxAreSelected input').click({force: true});
            cy.getElementByDataTestsId('setMembersBtn').click({force: true})
          });
          cy.getElementByDataTestsId('node-daeb6568-cef8-417f-9075-ed259ce59f48-groupingservicefortest..ResourceInstanceGroup..0').click({force: true});
          removeVnfGroup();
          cy.getElementByDataTestsId('button-remove-group').click({force: true});
          cy.get('#model-actions').should('not.exist');

        });

      });

      it('Vnf group edit mode delete empty service instance', () => {
        const SUBSCRIBER_ID: string = "e433710f-9217-458d-a79d-1c7aff376d89";
        const SERVICE_TYPE: string = "TYLER SILVIA";
        const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
        const SERVICE_MODEL_ID: string = '4117a0b6-e234-467d-b5b9-fe2f68c8b0fc';
        let expectedResult: JSON;

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/groupingServiceRoleResponse.json').then((res) => {
          jsonBuilderAndMock.basicJson(
            res,
            Cypress.config('baseUrl') + `/rest/models/services/${SERVICE_MODEL_ID}`,
            200,
            0,
            "ServiceWithVnfGrouping_serviceModel",
          );
        });

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/serviceWithVnfGroping_serviceInstance.json').then((res) => {
          jsonBuilderAndMock.basicJson(
            res,
            Cypress.config('baseUrl') + `/aai_get_service_instance_topology/${SUBSCRIBER_ID}/${SERVICE_TYPE}/${SERVICE_INSTANCE_ID}`,
            200, 0,
            "serviceWithVnfGroping_serviceInstance",
          )
        });

        cy.server().route({
          url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
          method: 'POST',
          status: 200,
          response: "[]",
        }).as("expectedPostAsyncInstantiation");

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/ServiceWithVnfGroupsDeleteRequest.json').then((res) => {
          expectedResult = res;
        });

        cy.openIframe(`app/ui/#/servicePlanning/EDIT?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}`);
        cy.getElementByDataTestsId('orchStatusValue').should('not.have.class', 'tag-status-value');
        cy.getElementByDataTestsId('openMenuBtn').click();
        cy.getElementByDataTestsId('context-menu-header-delete-item').should('have.text', 'Delete');
        cy.getElementByDataTestsId('context-menu-header-delete-item').click();
        cy.getElementByDataTestsId('serviceName').should('have.css', 'text-decoration');
        cy.getElementByDataTestsId('openMenuBtn').click();
        cy.getElementByDataTestsId('context-menu-header-delete-item').should('have.text', 'Undo delete');
        cy.getElementByDataTestsId('deployBtn').should('have.text', 'UPDATE').click();

        cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
          cy.deepCompare(xhr.request.body, expectedResult);

        });
      });

      it('Delete service with two vnf groups', () => {
        const SUBSCRIBER_ID: string = "e433710f-9217-458d-a79d-1c7aff376d89";
        const SERVICE_TYPE: string = "TYLER SILVIA";
        const SERVICE_MODEL_ID: string = '4117a0b6-e234-467d-b5b9-fe2f68c8b0fc';
        const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
        let expectedResult: JSON;

        cy.server().route({
          url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
          method: 'POST',
          status: 200,
          response: "[]",
        }).as("expectedPostAsyncInstantiation");

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/serviceWithVnfGroupsChildren_serviceInstance.json').then((res) => {
          jsonBuilderAndMock.basicJson(
            res,
            Cypress.config('baseUrl') + `/aai_get_service_instance_topology/${SUBSCRIBER_ID}/${SERVICE_TYPE}/${SERVICE_INSTANCE_ID}`,
            200, 0,
            "serviceWithVnfGroupsChildren_serviceInstance",
          )
        });

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/groupingServiceRoleResponse.json').then((res) => {
          jsonBuilderAndMock.basicJson(
            res,
            Cypress.config('baseUrl') + `/rest/models/services/${SERVICE_MODEL_ID}`,
            200,
            0,
            "ServiceTreeWithMultipleChildren_serviceModel",
          )
        });

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/deleteServiceWith2VnfGroupsRequest_AndThreeGroupMembers.json').then((expectedResult) => {
          cy.openIframe(`app/ui/#/servicePlanning/EDIT?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}`);
          cy.getElementByDataTestsId('openMenuBtn').click();
          cy.getElementByDataTestsId('context-menu-header-delete-item').should('have.text', 'Delete').click();
          cy.getElementByDataTestsId('deployBtn').should('have.text', 'UPDATE').click();
          cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
            cy.deepCompare(xhr.request.body, expectedResult);
          });
        });
      });

      it(`when open service with group in EDIT mode, service instance is shown as expected - e2e with input and output API's tests"`, function () {
        const SUBSCRIBER_ID: string = "e433710f-9217-458d-a79d-1c7aff376d89";
        const SERVICE_TYPE: string = "TYLER SILVIA";
        const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
        const SERVICE_MODEL_ID: string = '4117a0b6-e234-467d-b5b9-fe2f68c8b0fc';

        cy.server().route({
          url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
          method: 'POST',
          status: 200,
          response: "[]",
        }).as("expectedPostAsyncInstantiation");

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/groupingServiceRoleResponse.json').then((res) => {
          jsonBuilderAndMock.basicJson(
            res,
            Cypress.config('baseUrl') + `/rest/models/services/${SERVICE_MODEL_ID}`,
            200,
            0,
            "ServiceTreeWithMultipleChildren_serviceModel",
          )
        });

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/serviceWithVnfGroupsChildren_serviceInstance.json').then((res) => {
          jsonBuilderAndMock.basicJson(
            res,
            Cypress.config('baseUrl') + `/aai_get_service_instance_topology/${SUBSCRIBER_ID}/${SERVICE_TYPE}/${SERVICE_INSTANCE_ID}`,
            200, 0,
            "serviceWithVnfGroupsChildren_serviceInstance",
          )
        });

        cy.openIframe(`app/ui/#/servicePlanning/EDIT?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}`);

        /*
        0. title area -> generic stuff
                         instance name
                         orch status
                         service name
        */
        cy.getElementByDataTestsId("serviceInstance").should('have.text', 'Service instance:');
        cy.getElementByDataTestsId("orchStatusLabel").should('have.text', 'Orch Status:');
        // ERROR! cy.getElementByDataTestsId("quantityLabel").should('not.be.visible');
        // ERROR! cy.getElementByDataTestsId("servicesQuantity").should('not.be.visible');

        // specific
        cy.getElementByDataTestsId("serviceName").should('have.text', 'SERVICE_INSTANCE_NAME');
        cy.getElementByDataTestsId("orchStatusValue").should('have.text', 'GARBAGE DATA');
        cy.get('span#service-model-name').contains('Grouping Service for Test');

        // no need to expand anything
        cy.get('available-models-tree').find('.toggle-children').should('not.exist');

        /*
        1. Left tree
        */
        const leftShouldHaves: { [dataTestId: string]: { [dataTestId: string]: string; }; } = {
          'node-groupingservicefortest..ResourceInstanceGroup..0': {
            'node-type-indicator': 'G',
            'node-name': 'groupingservicefortest..ResourceInstanceGroup..0',
            'numberButton': '2',
          },
          'node-groupingservicefortest..ResourceInstanceGroup..1': {
            'node-type-indicator': 'G',
            'node-name': 'groupingservicefortest..ResourceInstanceGroup..1',
            'numberButton': '',
          },
        };
        cy.getElementByDataTestsId('search-left-tree-input').type('group');
        for (let node in leftShouldHaves) {
          cy.getElementByDataTestsId(node).find(`[data-tests-id= '${'node-name'}']`).find('.highlight').eq(0).should('have.text', 'group');

          for (let span in leftShouldHaves[node]) {
            const expected = leftShouldHaves[node][span];
            cy.getElementByDataTestsId(node).find(`[data-tests-id='${span}']`).should(expected ? 'have.text' : 'not.exist', expected);
          }
        }


        /*
        2. Right tree
        */
        const rightShouldHaves: { [dataTestId: string]: { [dataTestId: string]: string; }; } = {
          'node-daeb6568-cef8-417f-9075-ed259ce59f48-groupingservicefortest..ResourceInstanceGroup..0:0': {
            'node-type-indicator': 'G',
            'node-name': 'VNF_GROUP1_INSTANCE_NAME',
            'status-property-orchStatus': '',
            'status-property-provStatus': '',
            // 'status-property-inMaint': '', not exists for false in maint
          },
          'node-daeb6568-cef8-417f-9075-ed259ce59f48-groupingservicefortest..ResourceInstanceGroup..0:1': {
            'node-type-indicator': 'G',
            'node-name': 'VNF_GROUP2_INSTANCE_NAME',
            'status-property-orchStatus': '',
            'status-property-provStatus': '',
            // 'status-property-inMaint': 'false',
          },
        };
        cy.getElementByDataTestsId('search-right-tree-input').type('vnf');

        for (let node in rightShouldHaves) {
          var [nodeName, nodeEq] = node.split(":");
          for (let span in rightShouldHaves[node]) {
            cy.getElementByDataTestsId(nodeName).eq(+nodeEq).find(`[data-tests-id='${span}']`).should('have.text', rightShouldHaves[node][span]);
            cy.getElementByDataTestsId(nodeName).eq(+nodeEq).find(`[data-tests-id= '${'node-name'}']`).find('.highlight').eq(0).should('have.text', 'VNF');

          }
        }

        /*
        3. Left to right connections
        */
        const leftShouldHighlight: { [text: string]: string[] } = {
          'groupingservicefortest..ResourceInstanceGroup..0': ['VNF_GROUP1_INSTANCE_NAME', 'VNF_GROUP2_INSTANCE_NAME'],
          'groupingservicefortest..ResourceInstanceGroup..1': [],
        };

        for (let text in leftShouldHighlight) {
          cy.get('available-models-tree').contains(text).click();
          cy.get('.node-content-wrapper-active').find(`[data-tests-id='node-name']`).should('have.text', text + leftShouldHighlight[text].join(''));
        }

        /*
        4. Right to left connections
         */
        const rightShouldHighlight: { [text: string]: string[] } = {
          'VNF_GROUP1_INSTANCE_NAME': ['groupingservicefortest..ResourceInstanceGroup..0'],
          'VNF_GROUP2_INSTANCE_NAME': ['groupingservicefortest..ResourceInstanceGroup..0'],
        };

        for (let node in rightShouldHighlight) {
          cy.get('drawing-board-tree').contains(node).click();
          cy.get('.node-content-wrapper-active').find(`[data-tests-id='node-name']`).should('have.text', rightShouldHighlight[node].join('') + node);
        }

        /*
        Menus
         */
        // SETUP: delete one group on the right; add one group from the left
        const myNode = 'node-daeb6568-cef8-417f-9075-ed259ce59f48-groupingservicefortest..ResourceInstanceGroup..0';

        cy.getElementByDataTestsId(`${myNode}-menu-btn`).eq(0).click({force: true})
          .getElementByDataTestsId('context-menu-delete').click();

        cy.getElementByDataTestsId('node-groupingservicefortest..ResourceInstanceGroup..0-add-btn').click({force: true});
        cy.getElementByDataTestsId(`${myNode}-menu-btn`).eq(2).click({force: true})
          .getElementByDataTestsId('context-menu-edit').click();
        cy.getElementByDataTestsId('instanceName').clear();
        cy.typeToInput('instanceName', 'VNF_GROUP3_INSTANCE_NAME');
        cy.getElementByDataTestsId('form-set').click({force: true});

        // TEST:
        // #2 is just added
        // #1 is from AAI
        // #0 is deleted
        const menuShouldHave: { [text: string]: string[] } = {
          [`${myNode}-menu-btn:2`]: ['remove', 'addGroupMember'],
          [`${myNode}-menu-btn:1`]: ['showAuditInfo', 'addGroupMember', 'delete'],
          [`${myNode}-menu-btn:0`]: ['showAuditInfo', 'undoDelete'],
        };

        for (let node in menuShouldHave) {
          const [nodeName, nodeEq] = node.split(":");
          let enabledActions:string[] = menuShouldHave[node];
          cy.assertMenuItemsForNode(enabledActions, nodeName, +nodeEq);
        }

        const GROUP_NAME_TO_DEPLOY: string = 'groupingservicefortest..ResourceInstanceGroup..0';
        cy.getElementByDataTestsId('deployBtn').should('have.text', 'UPDATE').click();

        cy.getReduxState().then((state) => {
          const addedVnfGroup = state.service.serviceInstance[SERVICE_MODEL_ID].vnfGroups[GROUP_NAME_TO_DEPLOY];

          cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
            cy.readFile('../vid-automation/src/test/resources/VnfGroup/VnfGroupCreate1Delete1None1Request.json').then((expectedResult) => {
              expectedResult.vnfGroups[GROUP_NAME_TO_DEPLOY].trackById = addedVnfGroup.trackById;
              cy.deepCompare(xhr.request.body, expectedResult);
            });
          });
        });
      });

      it(`vnf group with vnf members should display correctly and should have delete option"`, function () {
        const SUBSCRIBER_ID: string = "e433710f-9217-458d-a79d-1c7aff376d89";
        const SERVICE_TYPE: string = "TYLER SILVIA";
        const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
        const SERVICE_MODEL_ID: string = '4117a0b6-e234-467d-b5b9-fe2f68c8b0fc';

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/groupingServiceRoleResponse.json').then((res) => {
          jsonBuilderAndMock.basicJson(
            res,
            Cypress.config('baseUrl') + `/rest/models/services/${SERVICE_MODEL_ID}`,
            200,
            0,
            "ServiceTreeWithMultipleChildren_serviceModel",
          )
        });

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/serviceWithVnfGroupsChildren_serviceInstance.json').then((res) => {
          jsonBuilderAndMock.basicJson(
            res,
            Cypress.config('baseUrl') + `/aai_get_service_instance_topology/${SUBSCRIBER_ID}/${SERVICE_TYPE}/${SERVICE_INSTANCE_ID}`,
            200, 0,
            "serviceWithVnfGroupsChildren_serviceInstance",
          )
        });

        cy.openIframe(`app/ui/#/servicePlanning/EDIT?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}&mode=EDIT`);
        cy.getElementByDataTestsId('deployBtn').should('have.attr', 'disabled');
        cy.get('#VnfGroup_VNF .icon-browse').eq(0).click({force: true}).then(() => {
          cy.getElementByDataTestsId('context-menu-delete').click({force: true});
          cy.getElementByDataTestsId('deployBtn').should('not.have.attr', 'disabled');

          cy.get('#VnfGroup_VNF .icon-browse').eq(0).click({force: true}).then(() => {
            cy.getElementByDataTestsId('context-menu-undoDelete').click({force: true});
            cy.getElementByDataTestsId('deployBtn').should('have.attr', 'disabled');
          });
        });


        cy.getElementByDataTestsId('node-daeb6568-cef8-417f-9075-ed259ce59f48-groupingservicefortest..ResourceInstanceGroup..0-menu-btn')
          .eq(0).click({force: true}).then(() => {
          cy.getElementByDataTestsId('context-menu-delete').click({force: true});
          cy.getElementByDataTestsId('deployBtn').should('not.have.attr', 'disabled');
        })
      });

      it('Delete 2 from 3 VNF members and check request body of deploy', function () {
        const SUBSCRIBER_ID: string = "e433710f-9217-458d-a79d-1c7aff376d89";
        const SERVICE_TYPE: string = "TYLER SILVIA";
        const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
        const SERVICE_MODEL_ID: string = '4117a0b6-e234-467d-b5b9-fe2f68c8b0fc';

        cy.server().route({
          url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
          method: 'POST',
          status: 200,
          response: "[]",
        }).as("expectedPostAsyncInstantiation");

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/groupingServiceRoleResponse.json').then((res) => {
          jsonBuilderAndMock.basicJson(
            res,
            Cypress.config('baseUrl') + `/rest/models/services/${SERVICE_MODEL_ID}`,
            200,
            0,
            "ServiceTreeWithMultipleChildren_serviceModel",
          )
        });

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/serviceWithVnfGroupsChildren_serviceInstance.json').then((res) => {

          jsonBuilderAndMock.basicJson(
            res,
            Cypress.config('baseUrl') + `/aai_get_service_instance_topology/${SUBSCRIBER_ID}/${SERVICE_TYPE}/${SERVICE_INSTANCE_ID}`,
            200, 0,
            "serviceWithVnfGroupsChildren_serviceInstance",
          )
        });

        cy.openIframe(`app/ui/#/servicePlanning/EDIT?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}&mode=EDIT`);
        cy.getElementByDataTestsId('deployBtn').should('have.attr', 'disabled');

        for (let index = 0; index < 2; index++) {
          cy.get('#VnfGroup_VNF .icon-browse').eq(index).click({force: true}).then(() => {
            cy.getElementByDataTestsId('context-menu-delete').click({force: true});
            cy.getElementByDataTestsId('deployBtn').should('not.have.attr', 'disabled');
          });
        }

        cy.getElementByDataTestsId('deployBtn').should('have.text', 'UPDATE').click();

        cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
          cy.readFile('../vid-automation/src/test/resources/VnfGroup/payloadTemplate1VnfGroupWith3MembersRequest.json').then((expectedResult) => {
            expectedResult.vnfGroups.VNF_GROUP1_INSTANCE_ID.action = 'None';
            expectedResult.vnfGroups.VNF_GROUP1_INSTANCE_ID.vnfs.RELATED_VNF1_INSTANCE_ID.action = 'None_Delete';
            expectedResult.vnfGroups.VNF_GROUP1_INSTANCE_ID.vnfs.RELATED_VNF2_INSTANCE_ID.action = 'None_Delete';
            cy.deepCompare(xhr.request.body, expectedResult);
          });
        });
      });

      it(`vnf group with vnf members, add group members option open modal`, () => {
        const SUBSCRIBER_ID: string = "global-customer-id";
        const SERVICE_TYPE: string = "service-instance-type";
        const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
        const SERVICE_MODEL_ID: string = '4117a0b6-e234-467d-b5b9-fe2f68c8b0fc';
        const SERVICE_INVARIANT_ID: string = '24632e6b-584b-4f45-80d4-fefd75fd9f14';
        const GROUP_ROLE: string = 'SERVICE-ACCESS';
        const GROUP_TYPE: string = 'LOAD-GROUP';

        cy.server().route({
          url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
          method: 'POST',
          status: 200,
          response: "[]",
        }).as("expectedPostAsyncInstantiation");

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/groupingServiceRoleResponse.json').then((res) => {
          jsonBuilderAndMock.basicJson(
            res,
            Cypress.config('baseUrl') + `/rest/models/services/${SERVICE_MODEL_ID}`,
            200,
            0,
            "ServiceTreeWithMultipleChildren_serviceModel",
          )
        });

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/serviceWithVnfGroupsChildren_serviceInstance.json').then((res) => {

          jsonBuilderAndMock.basicJson(
            res,
            Cypress.config('baseUrl') + `/aai_get_service_instance_topology/${SUBSCRIBER_ID}/${SERVICE_TYPE}/${SERVICE_INSTANCE_ID}`,
            200, 0,
            "serviceWithVnfGroupsChildren_serviceInstance",
          )
        });
        cy.readFile('../vid-automation/src/test/resources/VnfGroup/searchMembersResponse.json').then((res) => {

          jsonBuilderAndMock.basicJson(
            res,
            Cypress.config('baseUrl') + `/aai_search_group_members/${SUBSCRIBER_ID}/${SERVICE_TYPE}/${SERVICE_INVARIANT_ID}/${GROUP_TYPE}/${GROUP_ROLE}`,
            200, 0,
            "VnfGroup_searchMembersResponse",
          )
        });
        cy.openIframe(`app/ui/#/servicePlanning/EDIT?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}&mode=EDIT`);

        cy.getElementByDataTestsId('node-daeb6568-cef8-417f-9075-ed259ce59f48-groupingservicefortest..ResourceInstanceGroup..0-menu-btn')
          .eq(0).click({force: true}).then(() => {
          cy.getElementByDataTestsId('context-menu-addGroupMember').click({force: true}).then(() => {
            cy.getElementByDataTestsId('sourceModelName').contains("vDOROTHEA_Svc_vPRS");
            cy.getElementByDataTestsId('sourceModelInvariant').contains("24632e6b-584b-4f45-80d4-fefd75fd9f14");
            cy.get('#vnfName').eq(0).get('#VNF1_INSTANCE_NAME').contains('VNF1_INSTANCE_NAME');
            cy.get('#vnfName').eq(0).get('#VNF1_INSTANCE_ID').contains('VNF1_INSTANCE_ID');
            cy.getElementByDataTestsId('total-amount').should('have.text', '4 VNFs match your criteria |');
            cy.getElementByDataTestsId('total-selected').should('have.text', '0 VNF selected');
            cy.getElementByDataTestsId('setMembersBtn').should('have.attr', 'disabled');
            testCheckbox();
            testResetAddMemberModal();
            testUpdateBtn();
          });
        });
      });

      it(`vnf group add member get optional members return empty table"`, () => {
        const SUBSCRIBER_ID: string = "global-customer-id";
        const SERVICE_TYPE: string = "service-instance-type";
        const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
        const SERVICE_MODEL_ID: string = '4117a0b6-e234-467d-b5b9-fe2f68c8b0fc';
        const SERVICE_INVARIANT_ID: string = '24632e6b-584b-4f45-80d4-fefd75fd9f14';
        const GROUP_ROLE: string = 'SERVICE-ACCESS';
        const GROUP_TYPE: string = 'LOAD-GROUP';

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/groupingServiceRoleResponse.json').then((res) => {
          jsonBuilderAndMock.basicJson(
            res,
            Cypress.config('baseUrl') + `/rest/models/services/${SERVICE_MODEL_ID}`,
            200,
            0,
            "ServiceTreeWithMultipleChildren_serviceModel",
          )
        });

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/serviceWithVnfGroupsChildren_serviceInstance.json').then((res) => {

          jsonBuilderAndMock.basicJson(
            res,
            Cypress.config('baseUrl') + `/aai_get_service_instance_topology/${SUBSCRIBER_ID}/${SERVICE_TYPE}/${SERVICE_INSTANCE_ID}`,
            200, 0,
            "serviceWithVnfGroupsChildren_serviceInstance",
          )
        });
        cy.server()
          .route({
            method: 'GET',
            status: 200,
            url: Cypress.config('baseUrl') + `/aai_search_group_members/${SUBSCRIBER_ID}/${SERVICE_TYPE}/${SERVICE_INVARIANT_ID}/${GROUP_TYPE}/${GROUP_ROLE}`,
            response: []
          }).as('VnfGroup_searchMembersResponse');

        cy.openIframe(`app/ui/#/servicePlanning/EDIT?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}&mode=EDIT`);

        cy.getElementByDataTestsId('node-daeb6568-cef8-417f-9075-ed259ce59f48-groupingservicefortest..ResourceInstanceGroup..0-menu-btn')
          .eq(0).click({force: true}).then(() => {
          cy.getElementByDataTestsId('context-menu-addGroupMember').click({force: true}).then(() => {
            cy.getElementByDataTestsId('total-amount').should('have.text', '0 VNFs match your criteria |');
            cy.getElementByDataTestsId('total-selected').should('have.text', '0 VNF selected');
            cy.get('.no-result').should('have.text', 'No VNFs were found that can belong to this group.');
          });
        })
      });

      it('Check scaling policy - in Create new service mode', () => {
        const SUBSCRIBER_ID: string = "e433710f-9217-458d-a79d-1c7aff376d89";
        const SERVICE_TYPE: string = "TYLER SILVIA";
        const SERVICE_MODEL_ID: string = '4117a0b6-e234-467d-b5b9-fe2f68c8b0fc';
        const SERVICE_INVARIANT_ID: string = 'c989ab9a-33c7-46ec-b521-1b2daef5f047';
        const GROUP_ROLE: string = 'SERVICE-ACCESS';
        const GROUP_TYPE: string = 'LOAD-GROUP';
        const GROUP_NAME_TO_DEPLOY = 'groupingservicefortest..ResourceInstanceGroup..1';
        const NODE_ID = 'c2b300e6-45de-4e5e-abda-3032bee2de56';
        const FULL_GROUP_NAME = 'node-' + NODE_ID + '-' + GROUP_NAME_TO_DEPLOY;
        let serviceModel: JSON;
        let basicServiceInstance: JSON;

        cy.server().route({
          url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
          method: 'POST',
          status: 200,
          response: "[]",
        }).as("expectedPostAsyncInstantiation");

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/groupingServiceRoleResponse.json').then((res) => {
          serviceModel = res;
        });

        cy.readFile('./cypress/support/jsonBuilders/mocks/jsons/vnfGroupBasicServiceInstance.json').then((res) => {
          res.optionalGroupMembersMap = {};
          basicServiceInstance = res;
        });

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/searchMembersResponse.json').then((res) => {
          jsonBuilderAndMock.basicJson(
            res,
            Cypress.config('baseUrl') + `/aai_search_group_members/${SUBSCRIBER_ID}/${SERVICE_TYPE}/${SERVICE_INVARIANT_ID}/${GROUP_TYPE}/${GROUP_ROLE}`,
            200, 0,
            "VnfGroup_searchMembersResponse",
          )
        });

        cy.readFile('cypress/support/jsonBuilders/mocks/jsons/emptyServiceRedux.json').then((reduxRes) => {

          reduxRes.service.serviceHierarchy[SERVICE_MODEL_ID] = serviceModel;

          reduxRes.service.serviceInstance[SERVICE_MODEL_ID] = basicServiceInstance;

          cy.setReduxState(<any>reduxRes);

          cy.openIframe('app/ui/#/servicePlanning?serviceModelId=' + SERVICE_MODEL_ID);
          cy.getElementByDataTestsId('node-' + GROUP_NAME_TO_DEPLOY).find(`[data-tests-id='node-type-indicator']`).contains('G');
          cy.getElementByDataTestsId('node-' + GROUP_NAME_TO_DEPLOY).contains('' + GROUP_NAME_TO_DEPLOY);
          cy.getElementByDataTestsId('node-' + GROUP_NAME_TO_DEPLOY + '-add-btn').get('i').should('have.class', 'fa-plus-circle');

          cy.getElementByDataTestsId('node-' + GROUP_NAME_TO_DEPLOY + '-add-btn').click({force: true});
          cy.getElementByDataTestsId('node-' + GROUP_NAME_TO_DEPLOY + '-add-btn').click({force: true});

          cy.getElementByDataTestsId('node-' + NODE_ID + '-' + GROUP_NAME_TO_DEPLOY + '-menu-btn').each((row, index) => {
            cy.wrap(row).click({force: true}).then(() => {
              cy.getElementByDataTestsId('context-menu-addGroupMember').click({force: true}).then(() => {

              });
            });
          });
        });
      });

      it(`Check scaling policy in Edit mode`, () => {
        const SUBSCRIBER_ID: string = "global-customer-id";
        const SERVICE_TYPE: string = "service-instance-type";
        const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
        const SERVICE_MODEL_ID: string = '4117a0b6-e234-467d-b5b9-fe2f68c8b0fc';
        const SERVICE_INVARIANT_ID: string = '24632e6b-584b-4f45-80d4-fefd75fd9f14';
        const GROUP_ROLE: string = 'SERVICE-ACCESS';
        const GROUP_TYPE: string = 'LOAD-GROUP';
        const NODE_ID = 'daeb6568-cef8-417f-9075-ed259ce59f48';
        const GROUP_NAME_TO_DEPLOY = 'groupingservicefortest..ResourceInstanceGroup..0';
        const FULL_GROUP_NAME = 'node-' + NODE_ID + '-' + GROUP_NAME_TO_DEPLOY;

        cy.server().route({
          url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
          method: 'POST',
          status: 200,
          response: "[]",
        }).as("expectedPostAsyncInstantiation");

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/groupingServiceRoleResponse.json').then((res) => {
          jsonBuilderAndMock.basicJson(
            res,
            Cypress.config('baseUrl') + `/rest/models/services/${SERVICE_MODEL_ID}`,
            200,
            0,
            "ServiceTreeWithMultipleChildren_serviceModel",
          )
        });

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/serviceWithVnfGroupsChildren_serviceInstance.json').then((res) => {

          jsonBuilderAndMock.basicJson(
            res,
            Cypress.config('baseUrl') + `/aai_get_service_instance_topology/${SUBSCRIBER_ID}/${SERVICE_TYPE}/${SERVICE_INSTANCE_ID}`,
            200, 0,
            "serviceWithVnfGroupsChildren_serviceInstance",
          )
        });

        cy.readFile('../vid-automation/src/test/resources/VnfGroup/searchMembersResponse.json').then((res) => {

          jsonBuilderAndMock.basicJson(
            res,
            Cypress.config('baseUrl') + `/aai_search_group_members/${SUBSCRIBER_ID}/${SERVICE_TYPE}/${SERVICE_INVARIANT_ID}/${GROUP_TYPE}/${GROUP_ROLE}`,
            200, 0,
            "VnfGroup_searchMembersResponse",
          )
        });

        cy.openIframe(`app/ui/#/servicePlanning/EDIT?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}&mode=EDIT`);
        cy.getElementByDataTestsId('node-' + NODE_ID + '-' + GROUP_NAME_TO_DEPLOY + '-menu-btn')
          .eq(0).click({force: true}).then(() => {
          cy.getElementByDataTestsId('context-menu-addGroupMember').click({force: true}).then(() => {
            cy.getElementByDataTestsId('total-amount').should('have.text', '4 VNFs match your criteria |');
            cy.get('.allCheckboxAreSelected input').click({force: true});
            cy.getElementByDataTestsId('setMembersBtn').click({force: true}).then(() => {
              checkIsErrorAppear(true, FULL_GROUP_NAME, 4, 0);

              clickOnVnfMemberAndAssertScaling(6, 'context-menu-remove', true, FULL_GROUP_NAME, 4, 0);
              clickOnVnfMemberAndAssertScaling(5, 'context-menu-remove', true, FULL_GROUP_NAME, 4, 0);
              clickOnVnfMemberAndAssertScaling(0, 'context-menu-delete', false, FULL_GROUP_NAME, 4, 0);
              clickOnVnfMemberAndAssertScaling(0, 'context-menu-undoDelete', true, FULL_GROUP_NAME, 4, 0);
              clickOnVnfMemberAndAssertScaling(4, 'context-menu-remove', false, FULL_GROUP_NAME, 4, 0);
              testResetAddMemberModal();
              testUpdateBtn();
            })
          });
        });
      });

      function removeVnfGroup() {
        cy.getElementByDataTestsId('node-daeb6568-cef8-417f-9075-ed259ce59f48-groupingservicefortest..ResourceInstanceGroup..0-menu-btn')
          .click({force: true});
        cy.getElementByDataTestsId('context-menu-remove').click({force: true});
      }

      function testUpdateBtn() {
        cy.getElementByDataTestsId('deployBtn').should('have.text', 'UPDATE').click();
        cy.getReduxState().then((state) => {
          cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
            cy.readFile('../vid-automation/src/test/resources/VnfGroup/vnfGroupWithExistingAndNewVnfMembers.json').then((expectedResult) => {
              cy.deepCompare(xhr.request.body, expectedResult);
            });
          });
        });
      }

      function testCheckbox() {
        let totalNumber = 4;
        cy.get('.allCheckboxAreSelected input').click({force: true});
        cy.getElementByDataTestsId('setMembersBtn').should('not.have.attr', 'disabled');
        cy.getElementByDataTestsId('numberOfNotHideRows').contains(totalNumber);
        cy.getElementByDataTestsId('numberOfSelectedRows').contains(totalNumber);
        cy.get('.sdcCheckboxMember input').eq(0).click({force: true});
        cy.getElementByDataTestsId('numberOfSelectedRows').contains(totalNumber - 1);
        cy.get('.sdcCheckboxMember input').eq(1).click({force: true});
        cy.getElementByDataTestsId('numberOfSelectedRows').contains(totalNumber - 2);
        cy.get('.allCheckboxAreSelected input').click({force: true});
        cy.getElementByDataTestsId('numberOfSelectedRows').contains(totalNumber);
        cy.get('.allCheckboxAreSelected input').click({force: true});
        cy.getElementByDataTestsId('numberOfSelectedRows').contains(0);

        cy.getElementByDataTestsId("vnf-members-search").find('input').type("2.0");
        cy.getElementByDataTestsId('numberOfNotHideRows').contains(1);
        cy.getElementByDataTestsId("vnf-members-search").find('input').clear().type("vnf1");
        cy.getElementByDataTestsId('numberOfNotHideRows').contains(1);
        cy.get('.allCheckboxAreSelected input').click({force: true});
        cy.getElementByDataTestsId('numberOfSelectedRows').contains(1);
        cy.getElementByDataTestsId("vnf-members-search").find('input').clear();
        cy.getElementByDataTestsId('numberOfSelectedRows').contains(1);

        cy.getElementByDataTestsId('setMembersBtn').click({force: true});
        //check second time behavior (selected VNFs should be filtered)
        cy.getElementByDataTestsId('node-daeb6568-cef8-417f-9075-ed259ce59f48-groupingservicefortest..ResourceInstanceGroup..0-menu-btn')
          .eq(1).click({force: true}).then(() => {
          cy.getElementByDataTestsId('context-menu-addGroupMember').click({force: true}).then(() => {
            cy.getElementByDataTestsId('numberOfNotHideRows').contains(totalNumber - 1);
            cy.getElementByDataTestsId('cancelBtn').click({force: true});
          });
        });

      }
      function testResetAddMemberModal(): void{
        cy.getElementByDataTestsId('node-groupingservicefortest..ResourceInstanceGroup..1-add-btn').click({force: true})
          .getElementByDataTestsId('node-c2b300e6-45de-4e5e-abda-3032bee2de56-groupingservicefortest..ResourceInstanceGroup..1-menu-btn').eq(0).click({force: true}).then(() => {
          cy.getElementByDataTestsId('context-menu-addGroupMember').click({force: true}).then(() => {
            cy.getElementByDataTestsId('numberOfNotHideRows').contains(0);
            cy.getElementByDataTestsId('cancelBtn').click({force: true});
          });
          cy.getElementByDataTestsId('node-c2b300e6-45de-4e5e-abda-3032bee2de56-groupingservicefortest..ResourceInstanceGroup..1-menu-btn').eq(0).click({force: true}).then(() => {
            cy.getElementByDataTestsId('context-menu-remove').click({force: true});
          });
        });
      }
      function checkIsErrorAppear(isError: boolean, groupRowId: string, limit: number, index: number) {
        const isErrorPrefix = isError ? '' : 'not.';
        cy.getElementByDataTestsId('error-msg-wrapper').should(isErrorPrefix + 'exist');
        cy.getElementByDataTestsId('deployBtn').should(isErrorPrefix + 'have.attr', 'disabled');
        cy.getElementByDataTestsId(groupRowId).eq(index).find("[data-tests-id='scaling-policy']").should(isErrorPrefix + 'have.text', 'Limit' + limit);
        cy.getElementByDataTestsId('error-msg-title').should(isErrorPrefix + 'exist');
        cy.getElementByDataTestsId('error-msg-sub-title').should(isErrorPrefix + 'exist');
        cy.getElementByDataTestsId('error-msg-description').should(isErrorPrefix + 'exist');
      }

      function clickOnVnfMemberAndAssertScaling(childIndex: number, menuItemTestId: string, isError: boolean, groupRowId: string, limit: number, index: number) {
        cy.get('.tree-children').find('.node-wrapper').eq(childIndex).find('.icon-browse').click({force: true})
          .getElementByDataTestsId(menuItemTestId).click({force: true}).then(() => {
          checkIsErrorAppear(isError, groupRowId, limit, index);
        });
      }
    }
  );
});
