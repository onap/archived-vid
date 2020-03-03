///<reference path="../../../node_modules/cypress/types/index.d.ts"/>

import {JsonBuilder} from "../../support/jsonBuilders/jsonBuilder";
import {ServiceModel} from "../../support/jsonBuilders/models/service.model";

describe('Network popup', function () {

  var jsonBuilderAndMock: JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();

  beforeEach(() => {
      cy.clearSessionStorage();
      cy.setReduxState();
      cy.preventErrorsOnLoading();
      cy.permissionVidMock();
      cy.setTestApiParamToGR();
      cy.initAAIMock();
      cy.initVidMock();
      cy.login();
  });

  afterEach(() => {
    cy.screenshot();
  });

  describe('basic UI tests', () => {

    it('Network(A-La-Carte) - create / edit / duplicate / delete', function () {
      let redux = getReduxWithVNFSAndNetwork();
      redux.service.serviceHierarchy['6b528779-44a3-4472-bdff-9cd15ec93450'].service.vidNotions.instantiationType = 'ALaCarte';

      cy.setReduxState(<any>redux);
      cy.openIframe('app/ui/#/servicePlanning?serviceModelId=6b528779-44a3-4472-bdff-9cd15ec93450');
      //create
      cy.getElementByDataTestsId('node-ExtVL 0-add-btn').click({force: true}).then(() => {


        verifyServiceModelNameInModelInfo();
        cy.fillNetworkPopup();
      });
    });

    it('Network (Macro) - create / edit / duplicate / delete', function () {

      cy.setReduxState(<any>getReduxWithVNFSAndNetwork());
      cy.openIframe('app/ui/#/servicePlanning?serviceModelId=6b528779-44a3-4472-bdff-9cd15ec93450');
      //create
      cy.getElementByDataTestsId('node-ExtVL 0-add-btn').click({force: true}).then(() => {
        verifyServiceModelNameInModelInfo();
        cy.fillNetworkPopup().then(()=>{
          cy.getElementByDataTestsId('node-ddc3f20c-08b5-40fd-af72-c6d14636b986-ExtVL 0').should('be.visible');
          cy.getElementByDataTestsId('numberButton').contains('1');

          //Edit
          cy.getElementByDataTestsId('node-ddc3f20c-08b5-40fd-af72-c6d14636b986-ExtVL 0-menu-btn').click({force: true})
            .getElementByDataTestsId('context-menu-edit').click({force: true}).then(() => {
            cy.checkIsOptionSelected('productFamily', 'a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb');
            cy.checkIsOptionSelected('lcpRegion', 'hvf6');
            cy.checkIsOptionSelected('tenant', '229bcdc6eaeb4ca59d55221141d01f8e');
            cy.checkIsOptionSelected('lineOfBusiness', 'zzz1');
            cy.checkIsOptionSelected('platform', 'xxx1');
            //change platform value
            cy.selectPlatformValue('platform');
            cy.getElementByDataTestsId('form-set').click({force: true}).then(() => {
              cy.getElementByDataTestsId('numberButton').contains('1');
              cy.getElementByDataTestsId('node-ddc3f20c-08b5-40fd-af72-c6d14636b986-ExtVL 0-menu-btn').click({force: true})
                .getElementByDataTestsId('context-menu-edit').click({force: true}).then(() => {
                cy.checkIsOptionSelected('platform', 'platform');
                // cancel button should close the dialog
                cy.getElementByDataTestsId('cancelButton').click({force: true}).then(() => {
                  // duplicate network
                  cy.getElementByDataTestsId('node-ddc3f20c-08b5-40fd-af72-c6d14636b986-ExtVL 0-menu-btn').click({force: true})
                    .getElementByDataTestsId('context-menu-duplicate').click({force: true}).then(() => {
                    cy.getTagElementContainsText('button', 'Duplicate').click({force: true});
                    cy.getElementByDataTestsId('node-ddc3f20c-08b5-40fd-af72-c6d14636b986-ExtVL 0').should('have.length', 2);
                    cy.getElementByDataTestsId('numberButton').contains('2');
                    //delete
                    cy.getElementByDataTestsId('node-ddc3f20c-08b5-40fd-af72-c6d14636b986-ExtVL 0-menu-btn').eq(1).click({force: true})
                    cy.getElementByDataTestsId('context-menu-remove').click({force: true}).then(()=>{
                      cy.getElementByDataTestsId('numberButton').contains('1');
                      cy.getElementByDataTestsId('node-ddc3f20c-08b5-40fd-af72-c6d14636b986-ExtVL 0').should('have.length', 1);

                    })

                  });
                });
              })
            })
          });
        });
        });

    });

    it('Network dynamic inputs', function () {
      let redux = getReduxWithVNFSAndNetwork();
      redux.service.serviceHierarchy['6b528779-44a3-4472-bdff-9cd15ec93450'].networks['ExtVL 0'].inputs = <any>{
        "vnf_config_template_version": {
          "type": "string",
          "description": "VPE Software Version",
          'entry_schema': null,
          "inputProperties": null,
          "constraints": [],
          "required": true,
          "default": "17.2"
        },
        "bandwidth_units": {
          "type": "string",
          "description": "Units of bandwidth",
          "entry_schema": null,
          "inputProperties": null,
          "constraints": [],
          "required": false,
          "default": "Gbps"
        }
      };
      cy.setReduxState(<any>redux);
      cy.openIframe('app/ui/#/servicePlanning?serviceModelId=6b528779-44a3-4472-bdff-9cd15ec93450');

      cy.getElementByDataTestsId('node-ExtVL 0-add-btn').click({force: true}).then(() => {
        //check if dynamic inputs exist
        cy.getElementByDataTestsId('vnf_config_template_version');
        cy.getElementByDataTestsId('bandwidth_units');

        //check if required icon exists
        cy.get('label').contains('VNF config template version:').should('have.class', 'required');
        cy.get('label').contains('Bandwidth units:').should('not.have.class', 'required');

        //set button should be disabled when some dynamic inputs not valid
        cy.selectDropdownOptionByText('productFamily', 'Emanuel');
        cy.selectDropdownOptionByText('lcpRegion', 'hvf6');
        cy.selectDropdownOptionByText('tenant', 'AIN Web Tool-15-D-STTest2');
        cy.selectDropdownOptionByText('lineOfBusiness', 'zzz1');
        cy.selectPlatformValue('xxx1');
        cy.getElementByDataTestsId('form-set').should('not.have.attr', 'disabled');

        // clear required dynamic input.
        cy.getElementByDataTestsId('vnf_config_template_version').clear();
        cy.getElementByDataTestsId('form-set').should('have.attr', 'disabled');

      });
    });

    it.only('Edit service with network - delete 1 create 1', function () {
      const SUBSCRIBER_ID: string = "e433710f-9217-458d-a79d-1c7aff376d89";
      const SERVICE_TYPE: string = "TYLER SILVIA";
      const SERVICE_INSTANCE_ID: string = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
      const SERVICE_MODEL_ID: string = '6b528779-44a3-4472-bdff-9cd15ec93450';

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/basicNetworkModel.json').then((res) => {
        jsonBuilderAndMock.basicJson(res,
          Cypress.config('baseUrl') + '/rest/models/services/6b528779-44a3-4472-bdff-9cd15ec93450',
          200,
          0,
          'initServiceModel');
      });

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/basicNetworkInstance.json').then((res) => {
        jsonBuilderAndMock.basicJson(
          res,
          Cypress.config('baseUrl') + "/aai_get_service_instance_topology/e433710f-9217-458d-a79d-1c7aff376d89/TYLER SILVIA/f8791436-8d55-4fde-b4d5-72dd2cf13cfb",
          200, 0,
          "initServiceInstanceTopology"
        )
      });

      cy.server().route({
        url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
        method: 'POST',
        status: 200,
        response: "[]",
      }).as("expectedPostAsyncInstantiation");

      cy.openIframe(`app/ui/#/servicePlanning/EDIT?serviceModelId=${SERVICE_MODEL_ID}&subscriberId=${SUBSCRIBER_ID}&serviceType=${SERVICE_TYPE}&serviceInstanceId=${SERVICE_INSTANCE_ID}`);

      //add one network
      cy.getElementByDataTestsId('node-ExtVL 0-add-btn').click({force: true}).then(() => {
        verifyServiceModelNameInModelInfo();
        cy.fillNetworkPopup(true);
      });


      const networkNodeToDelete = "node-ddc3f20c-08b5-40fd-af72-c6d14636b986-ExtVL 0";
      cy.getElementByDataTestsId(`${networkNodeToDelete}-menu-btn`).eq(0).click({force: true})
        .getElementByDataTestsId('context-menu-delete').click();

      //click update
      cy.getElementByDataTestsId('deployBtn').should('have.text', 'UPDATE').click();

      cy.getReduxState().then((state) => {
        const network = state.service.serviceInstance[SERVICE_MODEL_ID].networks["ExtVL 0_1"];
        cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
          cy.readFile('../vid-automation/src/test/resources/asyncInstantiation/vidRequestDelete1Create1Network.json').then((expectedResult) => {
            expectedResult.networks["ExtVL 0_1"].trackById = network.trackById;
            cy.deepCompare(xhr.request.body, expectedResult);
          });
        });
      });

    });

    function verifyServiceModelNameInModelInfo() {
      cy.getElementByDataTestsId('model-item-label-serviceModelName').contains('Service Name');
      cy.getElementByDataTestsId('model-item-value-serviceModelName').contains('action-data');
    }

    function getReduxWithVNFSAndNetwork() {
      return {
        "global": {
          "name": null,
          "flags": {
            "FLAG_NETWORK_TO_ASYNC_INSTANTIATION": false,
            "FLAG_SHOW_ASSIGNMENTS": true,
            "FLAG_FABRIC_CONFIGURATION_ASSIGNMENTS": true,
            "FLAG_SHOW_VERIFY_SERVICE": false,
            "FLAG_SERVICE_MODEL_CACHE": true,
            "FLAG_ADVANCED_PORTS_FILTER": true,
            "FLAG_REGION_ID_FROM_REMOTE": true,
            "FLAG_ADD_MSO_TESTAPI_FIELD": true,
            "FLAG_2006_NETWORK_PLATFORM_MULTI_SELECT": true
          },
          "type": "[FLAGS] Update"
        },
        "service": {
          "serviceHierarchy": {
            "6e59c5de-f052-46fa-aa7e-2fca9d674c44": {
              "service": {
                "uuid": "6e59c5de-f052-46fa-aa7e-2fca9d674c44",
                "invariantUuid": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
                "name": "ComplexService",
                "version": "1.0",
                "toscaModelURL": null,
                "category": "Emanuel",
                "serviceType": "",
                "serviceRole": "",
                "description": "ComplexService",
                "serviceEcompNaming": "false",
                "instantiationType": "Macro",
                "inputs": {}
              },
              "vnfs": {
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
                    "max_instances": 10,
                    "min_instances": 1,
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
                    "ecomp_generated_naming": "false",
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
                    "multi_stage_design": "true",
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
                      "volumeGroupAllowed": true
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
              },
              "networks": {
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
                    "exVL_naming": "{ecomp_generated_naming=true}",
                    "network_flows": "{is_network_policy=false, is_bound_to_vpn=false}",
                    "network_homing": "{ecomp_selected_instance_node_target=false}"
                  },
                  "type": "VL",
                  "modelCustomizationName": "ExtVL 0"
                }
              },
              "collectionResources": {},
              "configurations": {
                "Port Mirroring Configuration By Policy 0": {
                  "uuid": "b4398538-e89d-4f13-b33d-ca323434ba50",
                  "invariantUuid": "6ef0ca40-f366-4897-951f-abd65d25f6f7",
                  "description": "A port mirroring configuration by policy object",
                  "name": "Port Mirroring Configuration By Policy",
                  "version": "27.0",
                  "customizationUuid": "3c3b7b8d-8669-4b3b-8664-61970041fad2",
                  "inputs": {},
                  "commands": {},
                  "properties": {},
                  "type": "Configuration",
                  "modelCustomizationName": "Port Mirroring Configuration By Policy 0",
                  "sourceNodes": [],
                  "collectorNodes": null,
                  "configurationByPolicy": false
                }
              },
              "serviceProxies": {},
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
                  "volumeGroupAllowed": true
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
              "pnfs": {}
            },
            "6b528779-44a3-4472-bdff-9cd15ec93450": {
              "service": {
                "uuid": "6b528779-44a3-4472-bdff-9cd15ec93450",
                "invariantUuid": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
                "name": "action-data",
                "version": "1.0",
                "toscaModelURL": null,
                "category": "",
                "serviceType": "",
                "serviceRole": "",
                "description": "",
                "serviceEcompNaming": "false",
                "instantiationType": "Macro",
                "vidNotions": {
                  "instantiationType": "Macro"
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
                "2017-388_PASQUALE-vPE 1": {
                  "uuid": "0903e1c0-8e03-4936-b5c2-260653b96413",
                  "invariantUuid": "00beb8f9-6d39-452f-816d-c709b9cbb87d",
                  "description": "Name PASQUALE vPE Description The provider edge function for the PASQUALE service supported by the Junipers VMX product Category Router Vendor Juniper Vendor Release Code 17.2 Owners Mary Fragale. Updated 9-25 to use v8.0 of the Juniper Valid 2 VLM",
                  "name": "2017-388_PASQUALE-vPE",
                  "version": "1.0",
                  "customizationUuid": "280dec31-f16d-488b-9668-4aae55d6648a",
                  "inputs": {
                    "vnf_config_template_version": {
                      "type": "string",
                      "description": "VPE Software Version",
                      "entry_schema": null,
                      "inputProperties": null,
                      "constraints": [],
                      "required": true,
                      "default": "17.2"
                    },
                    "bandwidth_units": {
                      "type": "string",
                      "description": "Units of bandwidth",
                      "entry_schema": null,
                      "inputProperties": null,
                      "constraints": [],
                      "required": true,
                      "default": "Gbps"
                    },
                    "bandwidth": {
                      "type": "string",
                      "description": "Requested VPE bandwidth",
                      "entry_schema": null,
                      "inputProperties": null,
                      "constraints": [],
                      "required": true,
                      "default": "10"
                    },
                    "AIC_CLLI": {
                      "type": "string",
                      "description": "AIC Site CLLI",
                      "entry_schema": null,
                      "inputProperties": null,
                      "constraints": [],
                      "required": true,
                      "default": "ATLMY8GA"
                    },
                    "ASN": {
                      "type": "string",
                      "description": "AV/PE",
                      "entry_schema": null,
                      "inputProperties": null,
                      "constraints": [],
                      "required": true,
                      "default": "AV_vPE"
                    },
                    "vnf_instance_name": {
                      "type": "string",
                      "description": "The hostname assigned to the vpe.",
                      "entry_schema": null,
                      "inputProperties": null,
                      "constraints": [],
                      "required": true,
                      "default": "mtnj309me6"
                    }
                  },
                  "commands": {
                    "vnf_config_template_version": {
                      "displayName": "vnf_config_template_version",
                      "command": "get_input",
                      "inputName": "2017488_pasqualevpe0_vnf_config_template_version"
                    },
                    "bandwidth_units": {
                      "displayName": "bandwidth_units",
                      "command": "get_input",
                      "inputName": "pasqualevpe0_bandwidth_units"
                    },
                    "bandwidth": {
                      "displayName": "bandwidth",
                      "command": "get_input",
                      "inputName": "pasqualevpe0_bandwidth"
                    },
                    "AIC_CLLI": {
                      "displayName": "AIC_CLLI",
                      "command": "get_input",
                      "inputName": "2017488_pasqualevpe0_AIC_CLLI"
                    },
                    "ASN": {
                      "displayName": "ASN",
                      "command": "get_input",
                      "inputName": "2017488_pasqualevpe0_ASN"
                    },
                    "vnf_instance_name": {
                      "displayName": "vnf_instance_name",
                      "command": "get_input",
                      "inputName": "2017488_pasqualevpe0_vnf_instance_name"
                    }
                  },
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
                    "nf_naming": "{ecomp_generated_naming=false}",
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
                    "ecomp_generated_naming": "false",
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
                    "vmxvpfe_flavor_name": "ns.c20r16d25.v5"
                  },
                  "type": "VF",
                  "modelCustomizationName": "2017-388_PASQUALE-vPE 1",
                  "vfModules": {},
                  "volumeGroups": {},
                  "vfcInstanceGroups": {}
                },
                "2017-388_PASQUALE-vPE 0": {
                  "uuid": "afacccf6-397d-45d6-b5ae-94c39734b168",
                  "invariantUuid": "72e465fe-71b1-4e7b-b5ed-9496118ff7a8",
                  "description": "Name PASQUALE vPE Description The provider edge function for the PASQUALE service supported by the Junipers VMX product Category Router Vendor Juniper Vendor Release Code 17.2 Owners Mary Fragale. Updated 9-25 to use v8.0 of the Juniper Valid 2 VLM",
                  "name": "2017-388_PASQUALE-vPE",
                  "version": "4.0",
                  "customizationUuid": "b3c76f73-eeb5-4fb6-9d31-72a889f1811c",
                  "inputs": {
                    "vnf_config_template_version": {
                      "type": "string",
                      "description": "VPE Software Version",
                      "entry_schema": null,
                      "inputProperties": null,
                      "constraints": [],
                      "required": true,
                      "default": "17.2"
                    },
                    "bandwidth_units": {
                      "type": "string",
                      "description": "Units of bandwidth",
                      "entry_schema": null,
                      "inputProperties": null,
                      "constraints": [],
                      "required": true,
                      "default": "Gbps"
                    },
                    "bandwidth": {
                      "type": "string",
                      "description": "Requested VPE bandwidth",
                      "entry_schema": null,
                      "inputProperties": null,
                      "constraints": [],
                      "required": true,
                      "default": "10"
                    },
                    "AIC_CLLI": {
                      "type": "string",
                      "description": "AIC Site CLLI",
                      "entry_schema": null,
                      "inputProperties": null,
                      "constraints": [],
                      "required": true,
                      "default": "ATLMY8GA"
                    },
                    "ASN": {
                      "type": "string",
                      "description": "AV/PE",
                      "entry_schema": null,
                      "inputProperties": null,
                      "constraints": [],
                      "required": true,
                      "default": "AV_vPE"
                    },
                    "vnf_instance_name": {
                      "type": "string",
                      "description": "The hostname assigned to the vpe.",
                      "entry_schema": null,
                      "inputProperties": null,
                      "constraints": [],
                      "required": true,
                      "default": "mtnj309me6"
                    }
                  },
                  "commands": {
                    "vnf_config_template_version": {
                      "displayName": "vnf_config_template_version",
                      "command": "get_input",
                      "inputName": "2017488_pasqualevpe0_vnf_config_template_version"
                    },
                    "bandwidth_units": {
                      "displayName": "bandwidth_units",
                      "command": "get_input",
                      "inputName": "pasqualevpe0_bandwidth_units"
                    },
                    "bandwidth": {
                      "displayName": "bandwidth",
                      "command": "get_input",
                      "inputName": "pasqualevpe0_bandwidth"
                    },
                    "AIC_CLLI": {
                      "displayName": "AIC_CLLI",
                      "command": "get_input",
                      "inputName": "2017488_pasqualevpe0_AIC_CLLI"
                    },
                    "ASN": {
                      "displayName": "ASN",
                      "command": "get_input",
                      "inputName": "2017488_pasqualevpe0_ASN"
                    },
                    "vnf_instance_name": {
                      "displayName": "vnf_instance_name",
                      "command": "get_input",
                      "inputName": "2017488_pasqualevpe0_vnf_instance_name"
                    }
                  },
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
                    "nf_naming": "{ecomp_generated_naming=false}",
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
                    "min_instances": "1",
                    "vmxvpfe_sriov44_0_port_vlanstrip": "false",
                    "vf_module_id": "123",
                    "nf_function": "JAI",
                    "vmxvpfe_sriov43_0_port_unknownmulticastallow": "true",
                    "vmxvre_int_ctl_ip_0": "10.0.0.10",
                    "ecomp_generated_naming": "false",
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
                    "vmxvpfe_flavor_name": "ns.c20r16d25.v5"
                  },
                  "type": "VF",
                  "modelCustomizationName": "2017-388_PASQUALE-vPE 0",
                  "vfModules": {},
                  "volumeGroups": {},
                  "vfcInstanceGroups": {}
                },
                "2017-488_PASQUALE-vPE 0": {
                  "uuid": "69e09f68-8b63-4cc9-b9ff-860960b5db09",
                  "invariantUuid": "72e465fe-71b1-4e7b-b5ed-9496118ff7a8",
                  "description": "Name PASQUALE vPE Description The provider edge function for the PASQUALE service supported by the Junipers VMX product Category Router Vendor Juniper Vendor Release Code 17.2 Owners Mary Fragale. Updated 9-25 to use v8.0 of the Juniper Valid 2 VLM",
                  "name": "2017-488_PASQUALE-vPE",
                  "version": "5.0",
                  "customizationUuid": "1da7b585-5e61-4993-b95e-8e6606c81e45",
                  "inputs": {
                    "vnf_config_template_version": {
                      "type": "string",
                      "description": "VPE Software Version",
                      "entry_schema": null,
                      "inputProperties": null,
                      "constraints": [],
                      "required": true,
                      "default": "17.2"
                    },
                    "bandwidth_units": {
                      "type": "string",
                      "description": "Units of bandwidth",
                      "entry_schema": null,
                      "inputProperties": null,
                      "constraints": [],
                      "required": true,
                      "default": "Gbps"
                    },
                    "bandwidth": {
                      "type": "string",
                      "description": "Requested VPE bandwidth",
                      "entry_schema": null,
                      "inputProperties": null,
                      "constraints": [],
                      "required": true,
                      "default": "10"
                    },
                    "AIC_CLLI": {
                      "type": "string",
                      "description": "AIC Site CLLI",
                      "entry_schema": null,
                      "inputProperties": null,
                      "constraints": [],
                      "required": true,
                      "default": "ATLMY8GA"
                    },
                    "ASN": {
                      "type": "string",
                      "description": "AV/PE",
                      "entry_schema": null,
                      "inputProperties": null,
                      "constraints": [],
                      "required": true,
                      "default": "AV_vPE"
                    },
                    "vnf_instance_name": {
                      "type": "string",
                      "description": "The hostname assigned to the vpe.",
                      "entry_schema": null,
                      "inputProperties": null,
                      "constraints": [],
                      "required": true,
                      "default": "mtnj309me6"
                    }
                  },
                  "commands": {
                    "vnf_config_template_version": {
                      "displayName": "vnf_config_template_version",
                      "command": "get_input",
                      "inputName": "2017488_pasqualevpe0_vnf_config_template_version"
                    },
                    "bandwidth_units": {
                      "displayName": "bandwidth_units",
                      "command": "get_input",
                      "inputName": "pasqualevpe0_bandwidth_units"
                    },
                    "bandwidth": {
                      "displayName": "bandwidth",
                      "command": "get_input",
                      "inputName": "pasqualevpe0_bandwidth"
                    },
                    "AIC_CLLI": {
                      "displayName": "AIC_CLLI",
                      "command": "get_input",
                      "inputName": "2017488_pasqualevpe0_AIC_CLLI"
                    },
                    "ASN": {
                      "displayName": "ASN",
                      "command": "get_input",
                      "inputName": "2017488_pasqualevpe0_ASN"
                    },
                    "vnf_instance_name": {
                      "displayName": "vnf_instance_name",
                      "command": "get_input",
                      "inputName": "2017488_pasqualevpe0_vnf_instance_name"
                    }
                  },
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
                    "nf_naming": "{ecomp_generated_naming=false}",
                    "nf_naming_code": "Navneet",
                    "vmxvre_name_0": "vREXI",
                    "vmxvpfe_sriov42_0_port_vlanstrip": "false",
                    "vmxvpfe_volume_name_0": "vPFEXI_FBVolume",
                    "max_instances": "3",
                    "vmx_RSG_id": "bd89a33c-13c3-4a04-8fde-1a57eb123141",
                    "vmxvpfe_image_name_0": "VPE_ROUTING-ENGINE_17.2R1-S2.1.qcow2",
                    "vmxvpfe_sriov43_0_port_unknownunicastallow": "true",
                    "vmxvpfe_sriov44_0_port_unknownmulticastallow": "true",
                    "vmxvre_console": "vidconsole",
                    "vmxvpfe_sriov44_0_port_vlanfilter": "4001",
                    "vmxvpfe_sriov42_0_port_mac": "00:11:22:EF:AC:DF",
                    "vmxvpfe_volume_id_0": "47cede15-da2f-4397-a101-aa683220aff3",
                    "vmxvpfe_sriov42_0_port_unknownmulticastallow": "true",
                    "min_instances": "1",
                    "vmxvpfe_sriov44_0_port_vlanstrip": "false",
                    "vf_module_id": "123",
                    "nf_function": "JAI",
                    "vmxvpfe_sriov43_0_port_unknownmulticastallow": "true",
                    "vmxvre_int_ctl_ip_0": "10.0.0.10",
                    "ecomp_generated_naming": "false",
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
                    "vmxvpfe_flavor_name": "ns.c20r16d25.v5"
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
                  "vfcInstanceGroups": {}
                }
              },
              "networks": {
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
                    "min_instances": 1,
                    "max_instances": 10,
                    "network_assignments": "{is_external_network=false, ipv4_subnet_default_assignment={min_subnets_count=1}, ecomp_generated_network_assignment=false, ipv6_subnet_default_assignment={min_subnets_count=1}}",
                    "exVL_naming": "{ecomp_generated_naming=true}",
                    "network_flows": "{is_network_policy=false, is_bound_to_vpn=false}",
                    "network_homing": "{ecomp_selected_instance_node_target=false}"
                  },
                  "type": "VL",
                  "modelCustomizationName": "ExtVL 0"
                }
              },
              "collectionResources": {},
              "configurations": {},
              "fabricConfigurations": {},
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
            "6e59c5de-f052-46fa-aa7e-2fca9d674c44": {
              "vnfs": {
                "VF_vGeraldine 0": {
                  "rollbackOnFailure": "true",
                  "vfModules": {
                    "vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0": {
                      "vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0vmvzo": {
                        "isMissingData": false,
                        "sdncPreReload": null,
                        "modelInfo": {
                          "modelType": "VFmodule",
                          "modelInvariantId": "a6f9e51a-2b35-416a-ae15-15e58d61f36d",
                          "modelVersionId": "a27f5cfc-7f12-4f99-af08-0af9c3885c87",
                          "modelName": "VfVgeraldine..base_vflorence..module-0",
                          "modelVersion": "2",
                          "modelCustomizationId": "f8c040f1-7e51-4a11-aca8-acf256cfd861",
                          "modelCustomizationName": "VfVgeraldine..base_vflorence..module-0"
                        },
                        "instanceParams": [
                          {}
                        ],
                        "trackById": "wmtm6sy2uj"
                      }
                    }
                  },
                  "isMissingData": true,
                  "originalName": "VF_vGeraldine 0",
                  "vnfStoreKey": "VF_vGeraldine 0",
                  "trackById": "p3wk448m5do",
                  "uuid": "d6557200-ecf2-4641-8094-5393ae3aae60",
                  "productFamilyId": "36b4733a-53f4-4cc8-8ff0-9172e5fc4b8e",
                  "lcpCloudRegionId": null,
                  "tenantId": null,
                  "lineOfBusiness": null,
                  "platformName": null,
                  "modelInfo": {
                    "modelType": "VF",
                    "modelInvariantId": "4160458e-f648-4b30-a176-43881ffffe9e",
                    "modelVersionId": "6e59c5de-f052-46fa-aa7e-2fca9d674c44",
                    "modelName": "VF_vGeraldine",
                    "modelVersion": "2.0",
                    "modelCustomizationName": "VF_vGeraldine 0"
                  }
                }
              },
              "networks": {},
              "instanceParams": [
                {}
              ],
              "validationCounter": 1,
              "existingNames": {},
              "existingVNFCounterMap": {
                "d91415b44-753d-494c-926a-456a9172bbb9": 1
              },
              "globalSubscriberId": "e433710f-9217-458d-a79d-1c7aff376d89",
              "subscriptionServiceType": "TYLER SILVIA",
              "owningEntityId": "d61e6f2d-12fa-4cc2-91df-7c244011d6fc",
              "productFamilyId": "36b4733a-53f4-4cc8-8ff0-9172e5fc4b8e",
              "lcpCloudRegionId": "hvf6",
              "tenantId": "229bcdc6eaeb4ca59d55221141d01f8e",
              "aicZoneId": "JAG1",
              "projectName": "x1",
              "rollbackOnFailure": "true",
              "bulkSize": 1,
              "modelInfo": {
                "modelInvariantId": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
                "modelVersionId": "6e59c5de-f052-46fa-aa7e-2fca9d674c44",
                "modelName": "ComplexService",
                "modelVersion": "1.0",
                "uuid": "6e59c5de-f052-46fa-aa7e-2fca9d674c44"
              },
              "isALaCarte": false,
              "name": "ComplexService",
              "version": "1.0",
              "description": "ComplexService",
              "category": "Emanuel",
              "uuid": "6e59c5de-f052-46fa-aa7e-2fca9d674c44",
              "invariantUuid": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
              "serviceType": "",
              "serviceRole": "",
              "isMultiStepDesign": false
            },
            "6b528779-44a3-4472-bdff-9cd15ec93450": {
              "networks": {},
              "vnfs": {},
              "instanceParams": [
                {
                  "2017488_pasqualevpe0_ASN": "AV_vPE"
                }
              ],
              "validationCounter": 0,
              "existingNames": {
                "123": "",
                "instancename": "",
                "yoav": ""
              },
              "existingVNFCounterMap": {},
              "existingNetworksCounterMap": {},
              "instanceName": "InstanceName",
              "globalSubscriberId": "e433710f-9217-458d-a79d-1c7aff376d89",
              "subscriptionServiceType": "TYLER SILVIA",
              "owningEntityId": "d61e6f2d-12fa-4cc2-91df-7c244011d6fc",
              "productFamilyId": "17cc1042-527b-11e6-beb8-9e71128cae77",
              "lcpCloudRegionId": "AAIAIC25",
              "tenantId": "092eb9e8e4b7412e8787dd091bc58e86",
              "aicZoneId": "JAG1",
              "projectName": null,
              "rollbackOnFailure": "true",
              "aicZoneName": "YUDFJULP-JAG1",
              "owningEntityName": "WayneHolland",
              "testApi": "GR_API",
              "tenantName": "USP-SIP-IC-24335-T-01",
              "bulkSize": 1,
              "modelInfo": {
                "modelInvariantId": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
                "modelVersionId": "6b528779-44a3-4472-bdff-9cd15ec93450",
                "modelName": "action-data",
                "modelVersion": "1.0",
                "uuid": "6b528779-44a3-4472-bdff-9cd15ec93450"
              },
              "isALaCarte": false,
              "name": "action-data",
              "version": "1.0",
              "description": "",
              "category": "",
              "uuid": "6b528779-44a3-4472-bdff-9cd15ec93450",
              "invariantUuid": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
              "serviceType": "",
              "serviceRole": "",
              "isMultiStepDesign": false
            }
          },
          "lcpRegionsAndTenants": {
            "lcpRegionList": [
              {
                "id": "AAIAIC25",
                "name": "AAIAIC25",
                "isPermitted": true
              },
              {
                "id": "hvf6",
                "name": "hvf6",
                "isPermitted": true
              }
            ],
            "lcpRegionsTenantsMap": {
              "AAIAIC25": [
                {
                  "id": "092eb9e8e4b7412e8787dd091bc58e86",
                  "name": "USP-SIP-IC-24335-T-01",
                  "isPermitted": true
                }
              ],
              "hvf6": [
                {
                  "id": "bae71557c5bb4d5aac6743a4e5f1d054",
                  "name": "AIN Web Tool-15-D-testalexandria",
                  "isPermitted": true
                },
                {
                  "id": "d0a3e3f2964542259d155a81c41aadc3",
                  "name": "test-hvf6-09",
                  "isPermitted": true
                },
                {
                  "id": "fa45ca53c80b492fa8be5477cd84fc2b",
                  "name": "ro-T112",
                  "isPermitted": true
                },
                {
                  "id": "cbb99fe4ada84631b7baf046b6fd2044",
                  "name": "DN5242-Nov16-T3",
                  "isPermitted": true
                }
              ]
            }
          },
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
                "name": "ONAP"
              },
              {
                "id": "zzz1",
                "name": "zzz1"
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
          "type": "[LCP_REGIONS_AND_TENANTS] Update",
          "subscribers": [
            {
              "id": "CAR_2020_ER",
              "name": "CAR_2020_ER",
              "isPermitted": true
            },
            {
              "id": "21014aa2-526b-11e6-beb8-9e71128cae77",
              "name": "JULIO ERICKSON",
              "isPermitted": false
            },
            {
              "id": "DHV1707-TestSubscriber-2",
              "name": "DALE BRIDGES",
              "isPermitted": false
            },
            {
              "id": "DHV1707-TestSubscriber-1",
              "name": "LLOYD BRIDGES",
              "isPermitted": false
            },
            {
              "id": "jimmy-example",
              "name": "JimmyExampleCust-20161102",
              "isPermitted": false
            },
            {
              "id": "jimmy-example2",
              "name": "JimmyExampleCust-20161103",
              "isPermitted": false
            },
            {
              "id": "ERICA5779-TestSub-PWT-102",
              "name": "ERICA5779-TestSub-PWT-102",
              "isPermitted": false
            },
            {
              "id": "ERICA5779-TestSub-PWT-101",
              "name": "ERICA5779-TestSub-PWT-101",
              "isPermitted": false
            },
            {
              "id": "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
              "name": "Emanuel",
              "isPermitted": false
            },
            {
              "id": "ERICA5779-Subscriber-4",
              "name": "ERICA5779-Subscriber-5",
              "isPermitted": false
            },
            {
              "id": "ERICA5779-TestSub-PWT-103",
              "name": "ERICA5779-TestSub-PWT-103",
              "isPermitted": false
            },
            {
              "id": "ERICA5779-Subscriber-2",
              "name": "ERICA5779-Subscriber-2",
              "isPermitted": false
            },
            {
              "id": "e433710f-9217-458d-a79d-1c7aff376d89",
              "name": "SILVIA ROBBINS",
              "isPermitted": true
            },
            {
              "id": "ERICA5779-Subscriber-3",
              "name": "ERICA5779-Subscriber-3",
              "isPermitted": false
            },
            {
              "id": "31739f3e-526b-11e6-beb8-9e71128cae77",
              "name": "CRAIG/ROBERTS",
              "isPermitted": false
            }
          ]
        }
      }
    }

  });
});
