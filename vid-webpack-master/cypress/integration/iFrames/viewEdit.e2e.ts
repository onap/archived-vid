///<reference path="../../../node_modules/cypress/types/index.d.ts"/> / <reference types="Cypress" />
import {JsonBuilder} from '../../support/jsonBuilders/jsonBuilder';
import {PnfModel} from '../../support/jsonBuilders/models/pnf.model';
import {ServiceModel} from '../../support/jsonBuilders/models/service.model';
import {AaiServiceInstancesModel} from '../../support/jsonBuilders/models/serviceInstances.model';
import {AAISubDetailsModel} from '../../support/jsonBuilders/models/aaiSubDetails.model';
import {AAISubViewEditModel} from '../../support/jsonBuilders/models/aaiSubViewEdit.model';
import {initServicePlanning} from "./viewOnlyDrawingBoard.e2e";


describe('View Edit Page', function () {
  describe('basic UI tests', () => {
    let jsonBuilderAAIService : JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();
    let commonUuid = "6e59c5de-f052-46fa-aa7e-2fca9d674c44";
    let serviceInvariantId = "d27e42cf-087e-4d31-88ac-6c4b7585f800";

    let jsonBuilderAAISubViewEditModel: JsonBuilder<AAISubViewEditModel> = new JsonBuilder<AAISubViewEditModel>();
    let jsonBuilderAAISubDetailsModel: JsonBuilder<AAISubDetailsModel> = new JsonBuilder<AAISubDetailsModel>();
    let jsonBuilderPNF: JsonBuilder<PnfModel> = new JsonBuilder<PnfModel>();
    let jsonBuilderAaiServiceInstances: JsonBuilder<AaiServiceInstancesModel> = new JsonBuilder<AaiServiceInstancesModel>();
    let jsonBuilderEmpty: JsonBuilder<Object> = new JsonBuilder<Object>();
    beforeEach(() => {
      cy.window().then((win) => {
        win.sessionStorage.clear();
      });
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

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/aaiSubDetails.json').then((res) => {
        jsonBuilderAAISubDetailsModel.basicJson(
          res,
          Cypress.config('baseUrl') + "/aai_sub_details/**",
          200,
          0,
          "aai-sub-details")
      });

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
      mockAsyncBulkResponse();
      cy.initVidMock({serviceUuid: commonUuid, invariantId: serviceInvariantId});
      cy.setReduxState();
      cy.permissionVidMock();
      cy.login();
    });

    afterEach(() => {
      cy.screenshot();
    });

    it(`should display the more actions button if user is permitted`, function () {
      cy.visit('/serviceModels.htm#/instantiate?subscriberId=e433710f-9217-458d-a79d-1c7aff376d89&subscriberName=USP%20VOICE&serviceType=VIRTUAL%20USP&serviceInstanceId=3f93c7cb-2fd0-4557-9514-e189b7b04f9d&aaiModelVersionId=6e59c5de-f052-46fa-aa7e-2fca9d674c44&isPermitted=true');
      cy.wait('@service-complexService');
      cy.wait('@aai_getPortMirroringConfigsDate - empty response');
      cy.getElementByDataTestsId("show-new-screen").should('be.visible').should('have.text', 'More actions').click();
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

    it(`Upgrade a VFModule`, function(){
      cy.initDrawingBoardUserPermission();
      initServicePlanning("EDIT",
        '../vid-automation/src/test/resources/viewEdit/ServiceTreeWithMultipleChildren_serviceInstance_withUpdatedLatestVersion.json');
      upgradeTheVFM();
      undoUpgradeForVFM();
      upgradeTheVFM();
      cy.getDrawingBoardDeployBtn().click();
      cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
        expect(Object(xhr.request.body).action).to.equal("None_Upgrade");
        expect(Object(xhr.request.body).vnfs['VNF2_INSTANCE_ID'].action).to.equal("None_Upgrade");
        expect(Object(xhr.request.body).vnfs['VNF2_INSTANCE_ID'].vfModules['dc229cd8-c132-4455-8517-5c1787c18b14']['3ef042c4-259f-45e0-9aba-0989bd8d1cc5'].action).to.equal("None_Upgrade");
      });
    });

    it(`Upgrade a VFModule, Negative - latest version doesn't exist, upgrade button shouldn't exist`, function(){
      setLatestVersionMockToEmptyResponse(serviceInvariantId);
      cy.initDrawingBoardUserPermission();
      initServicePlanning("EDIT",
        '../vid-automation/src/test/resources/viewEdit/ServiceTreeWithMultipleChildren_serviceInstance_withUpdatedLatestVersion.json');
      verifyMenuActionUpgradeDoesNotExist();
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
    cy.getElementByDataTestsId('node-undefined-dc229cd8-c132-4455-8517-5c1787c18b14-menu-btn').click()
      .getElementByDataTestsId('context-menu-upgrade').should('not.exist');
  }

  function setLatestVersionMockToEmptyResponse(serviceUuid :string){
    cy.server().route({
      url: Cypress.config('baseUrl') + '/aai_get_newest_model_version_by_invariant/' + serviceUuid,
      method: 'GET',
      status: 200,
      response: {},
    }).as("expectLatestServiceModelUpgradeVersion")
  }

  function upgradeTheVFM() :Chainable<any>{
    return cy.getElementByDataTestsId('node-undefined-dc229cd8-c132-4455-8517-5c1787c18b14-menu-btn').click()
      .drawingBoardTreeClickOnContextMenuOptionByName("Upgrade");
  }

  function undoUpgradeForVFM() {
    cy.getElementByDataTestsId('node-undefined-dc229cd8-c132-4455-8517-5c1787c18b14-menu-btn').click()
      .drawingBoardTreeClickOnContextMenuOptionByName("Undo Upgrade");
  }

  function changeFabric(serviceModel: ServiceModel) {
    serviceModel.service.uuid = "6e59c5de-f052-46fa-aa7e-2fca9d671234";
    return serviceModel;
  }

  function changeServiceModel(serviceModel: ServiceModel) {
    serviceModel.service.uuid = "6e59c5de-f052-46fa-aa7e-2fca9d674c44";
    serviceModel.vnfs = {
      "VF_vGeraldine 0": {
        "uuid": "d6557200-ecf2-4641-8094-5393ae3aae60",
        "invariantUuid": "4160458e-f648-4b30-a176-43881ffffe9e",
        "description": "VSP_vGeraldine",
        "name": "VF_vGeraldine",
        "version": "2.0",
        "customizationUuid": "91415b44-753d-494c-926a-456a9172bbb9",
        "inputs": {},
        "commands": {},
        "properties": {
          "gpb2_Internal2_mac": "00:11:22:EF:AC:DF",
          "sctp-b-ipv6-egress_src_start_port": "0",
          "sctp-a-ipv6-egress_rule_application": "any",
          "Internal2_allow_transit": "true",
          "sctp-b-IPv6_ethertype": "IPv6",
          "sctp-a-egress_rule_application": "any",
          "sctp-b-ingress_action": "pass",
          "sctp-b-ingress_rule_protocol": "icmp",
          "ncb2_Internal1_mac": "00:11:22:EF:AC:DF",
          "sctp-b-ipv6-ingress-src_start_port": "0.0",
          "ncb1_Internal2_mac": "00:11:22:EF:AC:DF",
          "fsb_volume_size_0": "320.0",
          "sctp-b-egress_src_addresses": "local",
          "sctp-a-ipv6-ingress_ethertype": "IPv4",
          "sctp-a-ipv6-ingress-dst_start_port": "0",
          "sctp-b-ipv6-ingress_rule_application": "any",
          "domain_name": "default-domain",
          "sctp-a-ingress_rule_protocol": "icmp",
          "sctp-b-egress-src_start_port": "0.0",
          "sctp-a-egress_src_addresses": "local",
          "sctp-b-display_name": "epc-sctp-b-ipv4v6-sec-group",
          "sctp-a-egress-src_start_port": "0.0",
          "sctp-a-ingress_ethertype": "IPv4",
          "sctp-b-ipv6-ingress-dst_end_port": "65535",
          "sctp-b-dst_subnet_prefix_v6": "::",
          "nf_naming": "{ecomp_generated_naming=true}",
          "sctp-a-ipv6-ingress_src_subnet_prefix": "0.0.0.0",
          "sctp-b-egress-dst_start_port": "0.0",
          "ncb_flavor_name": "nv.c20r64d1",
          "gpb1_Internal1_mac": "00:11:22:EF:AC:DF",
          "sctp-b-egress_dst_subnet_prefix_len": "0.0",
          "Internal2_net_cidr": "10.0.0.10",
          "sctp-a-ingress-dst_start_port": "0.0",
          "sctp-a-egress-dst_start_port": "0.0",
          "fsb1_Internal2_mac": "00:11:22:EF:AC:DF",
          "sctp-a-egress_ethertype": "IPv4",
          "vlc_st_service_mode": "in-network-nat",
          "sctp-a-ipv6-egress_ethertype": "IPv4",
          "sctp-a-egress-src_end_port": "65535.0",
          "sctp-b-ipv6-egress_rule_application": "any",
          "sctp-b-egress_action": "pass",
          "sctp-a-ingress-src_subnet_prefix_len": "0.0",
          "sctp-b-ipv6-ingress-src_end_port": "65535.0",
          "sctp-b-name": "epc-sctp-b-ipv4v6-sec-group",
          "fsb2_Internal1_mac": "00:11:22:EF:AC:DF",
          "sctp-a-ipv6-ingress-src_start_port": "0.0",
          "sctp-b-ipv6-egress_ethertype": "IPv4",
          "Internal1_net_cidr": "10.0.0.10",
          "sctp-a-egress_dst_subnet_prefix": "0.0.0.0",
          "fsb_flavor_name": "nv.c20r64d1",
          "sctp_rule_protocol": "132",
          "sctp-b-ipv6-ingress_src_subnet_prefix_len": "0",
          "sctp-a-ipv6-ingress_rule_application": "any",
          "ecomp_generated_naming": "true",
          "sctp-a-IPv6_ethertype": "IPv6",
          "vlc2_Internal1_mac": "00:11:22:EF:AC:DF",
          "vlc_st_virtualization_type": "virtual-machine",
          "sctp-b-ingress-dst_start_port": "0.0",
          "sctp-b-ingress-dst_end_port": "65535.0",
          "sctp-a-ipv6-ingress-src_end_port": "65535.0",
          "sctp-a-display_name": "epc-sctp-a-ipv4v6-sec-group",
          "sctp-b-ingress_rule_application": "any",
          "int2_sec_group_name": "int2-sec-group",
          "vlc_flavor_name": "nd.c16r64d1",
          "sctp-b-ipv6-egress_src_addresses": "local",
          "vlc_st_interface_type_int1": "other1",
          "sctp-b-egress-src_end_port": "65535.0",
          "sctp-a-ipv6-egress-dst_start_port": "0",
          "vlc_st_interface_type_int2": "other2",
          "sctp-a-ipv6-egress_rule_protocol": "any",
          "Internal2_shared": "false",
          "sctp-a-ipv6-egress_dst_subnet_prefix_len": "0",
          "Internal2_rpf": "disable",
          "vlc1_Internal1_mac": "00:11:22:EF:AC:DF",
          "sctp-b-ipv6-egress_src_end_port": "65535",
          "sctp-a-ipv6-egress_src_addresses": "local",
          "sctp-a-ingress-dst_end_port": "65535.0",
          "sctp-a-ipv6-egress_src_end_port": "65535",
          "Internal1_forwarding_mode": "l2",
          "Internal2_dhcp": "false",
          "sctp-a-dst_subnet_prefix_v6": "::",
          "pxe_image_name": "MME_PXE-Boot_16ACP04_GA.qcow2",
          "vlc_st_interface_type_gtp": "other0",
          "ncb1_Internal1_mac": "00:11:22:EF:AC:DF",
          "sctp-b-src_subnet_prefix_v6": "::",
          "sctp-a-egress_dst_subnet_prefix_len": "0.0",
          "int1_sec_group_name": "int1-sec-group",
          "Internal1_dhcp": "false",
          "sctp-a-ipv6-egress_dst_end_port": "65535",
          "Internal2_forwarding_mode": "l2",
          "fsb2_Internal2_mac": "00:11:22:EF:AC:DF",
          "sctp-b-egress_dst_subnet_prefix": "0.0.0.0",
          "Internal1_net_cidr_len": "17",
          "gpb2_Internal1_mac": "00:11:22:EF:AC:DF",
          "sctp-b-ingress-src_subnet_prefix_len": "0.0",
          "sctp-a-ingress_dst_addresses": "local",
          "sctp-a-egress_action": "pass",
          "fsb_volume_type_0": "SF-Default-SSD",
          "ncb2_Internal2_mac": "00:11:22:EF:AC:DF",
          "vlc_st_interface_type_sctp_a": "left",
          "vlc_st_interface_type_sctp_b": "right",
          "sctp-a-src_subnet_prefix_v6": "::",
          "vlc_st_version": "2",
          "sctp-b-egress_ethertype": "IPv4",
          "sctp-a-ingress_rule_application": "any",
          "gpb1_Internal2_mac": "00:11:22:EF:AC:DF",
          "instance_ip_family_v6": "v6",
          "sctp-a-ipv6-egress_src_start_port": "0",
          "sctp-b-ingress-src_start_port": "0.0",
          "sctp-b-ingress_dst_addresses": "local",
          "fsb1_Internal1_mac": "00:11:22:EF:AC:DF",
          "vlc_st_interface_type_oam": "management",
          "multi_stage_design": "false",
          "oam_sec_group_name": "oam-sec-group",
          "Internal2_net_gateway": "10.0.0.10",
          "sctp-a-ipv6-ingress-dst_end_port": "65535",
          "sctp-b-ipv6-egress-dst_start_port": "0",
          "Internal1_net_gateway": "10.0.0.10",
          "sctp-b-ipv6-egress_rule_protocol": "any",
          "gtp_sec_group_name": "gtp-sec-group",
          "sctp-a-ipv6-egress_dst_subnet_prefix": "0.0.0.0",
          "sctp-b-ipv6-egress_dst_subnet_prefix_len": "0",
          "sctp-a-ipv6-ingress_dst_addresses": "local",
          "sctp-a-egress_rule_protocol": "icmp",
          "sctp-b-ipv6-egress_action": "pass",
          "sctp-a-ipv6-egress_action": "pass",
          "Internal1_shared": "false",
          "sctp-b-ipv6-ingress_rule_protocol": "any",
          "Internal2_net_cidr_len": "17",
          "sctp-a-name": "epc-sctp-a-ipv4v6-sec-group",
          "sctp-a-ingress-src_end_port": "65535.0",
          "sctp-b-ipv6-ingress_src_subnet_prefix": "0.0.0.0",
          "sctp-a-egress-dst_end_port": "65535.0",
          "sctp-a-ingress_action": "pass",
          "sctp-b-egress_rule_protocol": "icmp",
          "sctp-b-ipv6-ingress_action": "pass",
          "vlc_st_service_type": "firewall",
          "sctp-b-ipv6-egress_dst_end_port": "65535",
          "sctp-b-ipv6-ingress-dst_start_port": "0",
          "vlc2_Internal2_mac": "00:11:22:EF:AC:DF",
          "vlc_st_availability_zone": "true",
          "fsb_volume_image_name_1": "MME_FSB2_16ACP04_GA.qcow2",
          "sctp-b-ingress-src_subnet_prefix": "0.0.0.0",
          "sctp-a-ipv6-ingress_src_subnet_prefix_len": "0",
          "Internal1_allow_transit": "true",
          "gpb_flavor_name": "nv.c20r64d1",
          "availability_zone_max_count": "1",
          "fsb_volume_image_name_0": "MME_FSB1_16ACP04_GA.qcow2",
          "sctp-b-ipv6-ingress_dst_addresses": "local",
          "sctp-b-ipv6-egress_dst_subnet_prefix": "0.0.0.0",
          "sctp-b-ipv6-ingress_ethertype": "IPv4",
          "vlc1_Internal2_mac": "00:11:22:EF:AC:DF",
          "sctp-a-ingress-src_subnet_prefix": "0.0.0.0",
          "sctp-a-ipv6-ingress_action": "pass",
          "Internal1_rpf": "disable",
          "sctp-b-ingress_ethertype": "IPv4",
          "sctp-b-egress_rule_application": "any",
          "sctp-b-ingress-src_end_port": "65535.0",
          "sctp-a-ipv6-ingress_rule_protocol": "any",
          "sctp-a-ingress-src_start_port": "0.0",
          "sctp-b-egress-dst_end_port": "65535.0"
        },
        "type": "VF",
        "modelCustomizationName": "VF_vGeraldine 0",
        "vfModules": {
          "vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1": {
            "uuid": "522159d5-d6e0-4c2a-aa44-5a542a12a830",
            "invariantUuid": "98a7c88b-b577-476a-90e4-e25a5871e02b",
            "customizationUuid": "55b1be94-671a-403e-a26c-667e9c47d091",
            "description": null,
            "name": "VfVgeraldine..vflorence_vlc..module-1",
            "version": "2",
            "modelCustomizationName": "VfVgeraldine..vflorence_vlc..module-1",
            "properties": {
              "minCountInstances": 0,
              "maxCountInstances": null,
              "initialCount": 0,
              "vfModuleLabel": "vflorence_vlc"
            },
            "inputs": {},
            "volumeGroupAllowed": false
          },
          "vf_vgeraldine0..VfVgeraldine..vflorence_gpb..module-2": {
            "uuid": "41708296-e443-4c71-953f-d9a010f059e1",
            "invariantUuid": "1cca90b8-3490-495e-87da-3f3e4c57d5b9",
            "customizationUuid": "6add59e0-7fe1-4bc4-af48-f8812422ae7c",
            "description": null,
            "name": "VfVgeraldine..vflorence_gpb..module-2",
            "version": "2",
            "modelCustomizationName": "VfVgeraldine..vflorence_gpb..module-2",
            "properties": {
              "minCountInstances": 0,
              "maxCountInstances": null,
              "initialCount": 0,
              "vfModuleLabel": "vflorence_gpb"
            },
            "inputs": {},
            "volumeGroupAllowed": false
          },
          "vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0": {
            "uuid": "a27f5cfc-7f12-4f99-af08-0af9c3885c87",
            "invariantUuid": "a6f9e51a-2b35-416a-ae15-15e58d61f36d",
            "customizationUuid": "f8c040f1-7e51-4a11-aca8-acf256cfd861",
            "description": null,
            "name": "VfVgeraldine..base_vflorence..module-0",
            "version": "2",
            "modelCustomizationName": "VfVgeraldine..base_vflorence..module-0",
            "properties": {
              "minCountInstances": 1,
              "maxCountInstances": 1,
              "initialCount": 1,
              "vfModuleLabel": "base_vflorence"
            },
            "inputs": {},
            "volumeGroupAllowed": true
          }
        },
        "volumeGroups": {
          "vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0": {
            "uuid": "a27f5cfc-7f12-4f99-af08-0af9c3885c87",
            "invariantUuid": "a6f9e51a-2b35-416a-ae15-15e58d61f36d",
            "customizationUuid": "f8c040f1-7e51-4a11-aca8-acf256cfd861",
            "description": null,
            "name": "VfVgeraldine..base_vflorence..module-0",
            "version": "2",
            "modelCustomizationName": "VfVgeraldine..base_vflorence..module-0",
            "properties": {
              "minCountInstances": 1,
              "maxCountInstances": 1,
              "initialCount": 1,
              "vfModuleLabel": "base_vflorence"
            },
            "inputs": {}
          }
        },
        "vfcInstanceGroups": {}
      }
    };
    serviceModel.vfModules = {
      "vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1": {
        "uuid": "522159d5-d6e0-4c2a-aa44-5a542a12a830",
        "invariantUuid": "98a7c88b-b577-476a-90e4-e25a5871e02b",
        "customizationUuid": "55b1be94-671a-403e-a26c-667e9c47d091",
        "description": null,
        "name": "VfVgeraldine..vflorence_vlc..module-1",
        "version": "2",
        "modelCustomizationName": "VfVgeraldine..vflorence_vlc..module-1",
        "properties": {
          "minCountInstances": 0,
          "maxCountInstances": null,
          "initialCount": 0,
          "vfModuleLabel": "vflorence_vlc"
        },
        "inputs": {},
        "volumeGroupAllowed": false
      },
      "vf_vgeraldine0..VfVgeraldine..vflorence_gpb..module-2": {
        "uuid": "41708296-e443-4c71-953f-d9a010f059e1",
        "invariantUuid": "1cca90b8-3490-495e-87da-3f3e4c57d5b9",
        "customizationUuid": "6add59e0-7fe1-4bc4-af48-f8812422ae7c",
        "description": null,
        "name": "VfVgeraldine..vflorence_gpb..module-2",
        "version": "2",
        "modelCustomizationName": "VfVgeraldine..vflorence_gpb..module-2",
        "properties": {
          "minCountInstances": 0,
          "maxCountInstances": null,
          "initialCount": 0,
          "vfModuleLabel": "vflorence_gpb"
        },
        "inputs": {},
        "volumeGroupAllowed": false
      },
      "vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0": {
        "uuid": "a27f5cfc-7f12-4f99-af08-0af9c3885c87",
        "invariantUuid": "a6f9e51a-2b35-416a-ae15-15e58d61f36d",
        "customizationUuid": "f8c040f1-7e51-4a11-aca8-acf256cfd861",
        "description": null,
        "name": "VfVgeraldine..base_vflorence..module-0",
        "version": "2",
        "modelCustomizationName": "VfVgeraldine..base_vflorence..module-0",
        "properties": {
          "minCountInstances": 1,
          "maxCountInstances": 1,
          "initialCount": 1,
          "vfModuleLabel": "base_vflorence"
        },
        "inputs": {},
        "volumeGroupAllowed": true
      }
    };
    serviceModel.volumeGroups = {
      "vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0": {
        "uuid": "a27f5cfc-7f12-4f99-af08-0af9c3885c87",
        "invariantUuid": "a6f9e51a-2b35-416a-ae15-15e58d61f36d",
        "customizationUuid": "f8c040f1-7e51-4a11-aca8-acf256cfd861",
        "description": null,
        "name": "VfVgeraldine..base_vflorence..module-0",
        "version": "2",
        "modelCustomizationName": "VfVgeraldine..base_vflorence..module-0",
        "properties": {
          "minCountInstances": 1,
          "maxCountInstances": 1,
          "initialCount": 1,
          "vfModuleLabel": "base_vflorence"
        },
        "inputs": {}
      }
    };
    return serviceModel;
  }
});
