import * as _ from "lodash";
import {PropertyPath} from "lodash";

describe('Drawing Board: Instantiation Templates', function () {

  describe('Instantiation templates ', () => {

    beforeEach(() => {
      cy.clearSessionStorage();
      cy.setTestApiParamToGR();
      cy.initAAIMock();
      cy.initGetAAISubDetails();
      cy.initVidMock();
      cy.initDrawingBoardUserPermission();
      cy.login();

      mockAsyncBulkResponse();
    });

    afterEach(() => {
      cy.screenshot();
    });

    describe('Load Page and Deploy', () => {

      it(`Given a stored template - when click "deploy" - then a coherent request should be sent upon deploy`, () => {

        cy.loadDrawingBoardWithRecreateMode(templateWithVnfSetup);

        // Then...
        cy.getElementByDataTestsId("node-vProbe_NC_VNF 0").should('be.visible');
        assertThatBodyFromDeployRequestEqualsToTemplateFromBackEnd();
      });

      it('Given a template - User can remove existing VNF', () => {

        cy.loadDrawingBoardWithRecreateMode(templateWithVnfSetup);

        removeVNFWithVFModules('node-21ae311e-432f-4c54-b855-446d0b8ded72-vProbe_NC_VNF 0');
        removeVNFWithVFModules('node-21ae311e-432f-4c54-b855-446d0b8ded72-vProbe_NC_VNF 0');

        cy.getDrawingBoardDeployBtn().click();
        cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
          cy.deepCompare(bodyOf(xhr).vnfs, {});
        });

      });

      it('Given a template - User can add new VNF', () => {
        cy.loadDrawingBoardWithRecreateMode(templateWithVnfSetup);
        // add new node
        addNewNode('node-vProbe_NC_VNF 0-add-btn')
          .fillVnfPopup()
          .getDrawingBoardDeployBtn().click()
          .wait('@expectedPostAsyncInstantiation').then(xhr => {
          const vnfRequest = bodyOf(xhr).vnfs['vProbe_NC_VNF 0_2'];

          expect(vnfRequest.action).equals("Create");
          expect(vnfRequest.rollbackOnFailure).equals("true");
          expect(vnfRequest.originalName).equals("vProbe_NC_VNF 0");
          expect(vnfRequest.productFamilyId).equals("a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb");
          expect(vnfRequest.lcpCloudRegionId).equals("hvf6");
          expect(vnfRequest.lineOfBusiness).equals("zzz1");
          expect(vnfRequest.platformName).equals("xxx1");
          expect(vnfRequest.tenantId).equals("229bcdc6eaeb4ca59d55221141d01f8e");


          // check instance name not change if empty
          cy.editNode('node-21ae311e-432f-4c54-b855-446d0b8ded72-vProbe_NC_VNF 0', 0)
            .clearInput('instanceName');
          cy.getElementByDataTestsId('form-set').click({force: true}).then((done) => {
            cy.editNode('node-21ae311e-432f-4c54-b855-446d0b8ded72-vProbe_NC_VNF 0', 0)
              .getElementByDataTestsId('instanceName').should('be.empty')
          });
        });
      });

      it('Given a template - User can Duplicate VNF', () => {
        const numberOfDuplicate: number = 4;
        cy.loadDrawingBoardWithRecreateMode(templateWithVnfSetup);
        cy.nodeAction('node-21ae311e-432f-4c54-b855-446d0b8ded72-vProbe_NC_VNF 0', 'Duplicate')
          .getElementByDataTestsId('duplicate-amount-vfmodules').select(numberOfDuplicate.toString())
          .getTagElementContainsText('button', 'Duplicate').click()
          .getDrawingBoardDeployBtn().click()
          .wait('@expectedPostAsyncInstantiation').then(xhr => {
          expect(Object.keys(bodyOf(xhr).vnfs).length).equals(numberOfDuplicate + 2);
        });
      });

      it('Given a stored template - when "edit" vnf and vfmodules are opened - then template’s details are visible as expected and deploy without changes', () => {

        cy.loadDrawingBoardWithRecreateMode(templateWithVnfSetup);

        // Then...
        cy.editNode("node-21ae311e-432f-4c54-b855-446d0b8ded72-vProbe_NC_VNF 0")
          .getElementByDataTestsId("instanceName").should('have.value', 'hvf6arlba007')
          .getElementByDataTestsId("productFamily").should('contain', 'Emanuel')
          .getElementByDataTestsId("tenant").should('contain', 'DN5242-Nov21-T1')
          .getElementByDataTestsId("lcpRegion").should('contain', 'hvf6')
          .getElementByDataTestsId("rollback").should('contain', 'Rollback')
          cy.checkLobValue('zzz1')
          cy.checkPlatformValue(`xxx1`)
          .getElementByDataTestsId("cancelButton").click();

        cy.editNode("node-c5b26cc1-a66f-4b69-aa23-6abc7c647c88-vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0")
          .getElementByDataTestsId("instanceName").should('have.value', 'hvf6arlba007_lba_Base_01')
          .getElementByDataTestsId("rollback").should('contain', 'Rollback')
          .getElementByDataTestsId("cancelButton").click();

        cy.editNode("node-c09e4530-8fd8-418f-9483-2f57ce927b05-vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1")
          .getElementByDataTestsId("instanceName").should('have.value', 'my_hvf6arlba007_lba_dj_01')
          .getElementByDataTestsId("volumeGroupName").should('have.value', 'my_special_hvf6arlba007_lba_dj_01_vol')
          .getElementByDataTestsId("rollback").should('contain', 'Rollback')
          .getElementByDataTestsId("sdncPreLoad").should('have.value', 'on')
          .getElementByDataTestsId("cancelButton").click();

        assertThatBodyFromDeployRequestEqualsToTemplateFromBackEnd([
          {path: [...vnfPath, "vnfStoreKey"], value: "vProbe_NC_VNF 0"}, // side-effect
          {path: [...vnfPath2, "vnfStoreKey"], value: "vProbe_NC_VNF 0_1"},
        ]);
      });

      it(`Given a stored template - when "edit" service is opened - then template’s details are visible as expected`, function () {

        cy.loadDrawingBoardWithRecreateMode(templateWithVnfSetup);

        cy.openServiceContextMenu()
          .getElementByDataTestsId("context-menu-header-edit-item").click()
          .getElementByDataTestsId("instanceName").should('have.value', 'vProbe_NC_Service_DG_new_SI')
          .getElementByDataTestsId("subscriberName").should('contain', 'SILVIA ROBBINS')
          .getElementByDataTestsId("serviceType").should('contain', 'TYLER SILVIA')
          .getElementByDataTestsId("owningEntity").should('contain', 'WayneHolland')
          .getElementByDataTestsId("project").should('contain', 'WATKINS')
          .getElementByDataTestsId("rollback").should('contain', 'Rollback');

      });

      it(`Given a stored template - add one VfModule, edit its details, and deploy - deploy is added with the vfModule details`, () => {
        cy.loadDrawingBoardWithRecreateMode(templateWithVnfSetup);

        let newVfModuleName = "new.vfmodule.name";
        let module1ModelId = "VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1";
        let module1CustomizationId = `vprobe_nc_vnf0..${module1ModelId}`;

        // Click target VNF on right tree
        cy.getElementByDataTestsId('node-21ae311e-432f-4c54-b855-446d0b8ded72-vProbe_NC_VNF 0').first().click();

        // Click [+] vfModule on left tree
        cy.drawingBoardPressAddButtonByElementName(`node-${module1CustomizationId}`)
          .click({force: true});

        cy.editNode(`node-c09e4530-8fd8-418f-9483-2f57ce927b05-${module1CustomizationId}`, 1);
        cy.clearInput("instanceName");
        cy.typeToInput("instanceName", newVfModuleName);
        cy.getElementByDataTestsId('form-set').click();

        // Then...
        cy.getReduxState().then((state) => {
          let vfModules_1Path = [
            ...vnfPath, "vfModules", module1CustomizationId,
          ];

          let vfModules_0Path = [
            ...vnfPath, "vfModules", "vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0"
          ];

          let serviceInstanceElementOnRedux = state.service.serviceInstance[(templateWithVnfSetup.serviceModelId)];
          let latestVfModule_1Path = findPathOfLatestVfModule(serviceInstanceElementOnRedux, vfModules_1Path);
          // This is a funny merge, as values are already there, but that way ensures
          // the values that selected are really deployed, while limiting the cost of
          // maintenance, by taking other vfModule's fields as granted.
          let latestVfModule_1ExpectedValue = _.merge(
            _.get(serviceInstanceElementOnRedux, latestVfModule_1Path),
            {
              instanceName: newVfModuleName,
              volumeGroupName: `${newVfModuleName}_vol`
            }
          );

          // const vnfPath = [
          //   "vnfs", "vProbe_NC_VNF 0"
          // ];

          let vfModule_1 = [
            ...vfModules_1Path, "vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1yprvi"
          ]
          let vfModule_0 = [
            ...vfModules_0Path, 'vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0ahubg'
          ]

          assertThatBodyFromDeployRequestEqualsToTemplateFromBackEnd([
            {path: [...vnfPath, "vnfStoreKey"], value: "vProbe_NC_VNF 0"},   // side-effect
            {path: [...vnfPath2, "vnfStoreKey"], value: "vProbe_NC_VNF 0_1"},
            {path: ["existingNames", newVfModuleName], value: ""},
            {path: ["existingNames", `${newVfModuleName}_vol`], value: ""},
            {path: latestVfModule_1Path, value: latestVfModule_1ExpectedValue},
            {path: ["validationCounter"], value: null},  // side-effect
            {path: [...vfModule_1, "position"], value: 2},
            {path: [...vfModule_0, "position"], value: 1},
          ]);
        });

      });

      it('Given a template - User can remove existing vfmodule', function () {

        cy.loadDrawingBoardWithRecreateMode(templateWithVnfSetup);
        cy.nodeAction('node-c09e4530-8fd8-418f-9483-2f57ce927b05-vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1', 'Remove');
        let removed_vfModule_Path = [
          ...vnfPath, "vfModules",
          "vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1",
        ];

        assertThatBodyFromDeployRequestEqualsToTemplateFromBackEnd([
          {path: [...vnfPath, "vnfStoreKey"], value: "vProbe_NC_VNF 0"}, // side-effect
          {path: [...vnfPath2, "vnfStoreKey"], value: "vProbe_NC_VNF 0_1"},
          {path: [...removed_vfModule_Path], value: undefined},
        ]);
      });

      [
        {desc: "with changes", modifySomeValues: true},
        {desc: "without changes", modifySomeValues: false},
      ].forEach((testCase) => {

        it(`Given a stored template - edit service vnf and vfmodule ${testCase.desc} - deploy request should be ${testCase.desc}`, function () {

          cy.loadDrawingBoardWithRecreateMode(templateWithVnfSetup);

          //edit service
          cy.openServiceContextMenu();
          cy.getElementByDataTestsId("context-menu-header-edit-item").click();
          if (testCase.modifySomeValues) {
            cy.clearInput("instanceName");
            cy.typeToInput("instanceName", "different.instance.name");
          }
          cy.getElementByDataTestsId('form-set').click();

          // edit vnf
          cy.editNode("node-21ae311e-432f-4c54-b855-446d0b8ded72-vProbe_NC_VNF 0");
          if (testCase.modifySomeValues) {
            cy.selectPlatformValue(`platform`);
            cy.selectDropdownOptionByText("tenant", "CESAR-100-D-spjg61909");
          }
          cy.getElementByDataTestsId('form-set').click();

          //edit vf module
          cy.editNode("node-c5b26cc1-a66f-4b69-aa23-6abc7c647c88-vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0");
          if (testCase.modifySomeValues) {
            cy.getElementByDataTestsId('sdncPreLoad').click();
          }
          cy.getElementByDataTestsId('form-set').click();
          // Then...
          let vfModule_0Path = [
            ...vnfPath, "vfModules",
            "vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0",
            "vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0ahubg",
          ];

          assertThatBodyFromDeployRequestEqualsToFile(testCase.modifySomeValues ? [
            {path: ["instanceName"], value: "different.instance.name"},
            {path: ["existingNames", "vprobe_nc_service_dg_new_si"], value: undefined},
            {path: ["existingNames", "different.instance.name"], value: ""},

            {path: [...vnfPath, "platformName"], value: "xxx1,platform"},
            {path: [...vnfPath, "tenantId"], value: "f2f3830e4c984d45bcd00e1a04158a79"},

            {path: [...vfModule_0Path, "sdncPreLoad"], value: true},
            {path: [...vfModule_0Path, "pauseInstantiation"], value: null}
          ] : []);
        })

      });

      it(`Given a stored template of Network - - it is loaded`, () => {

        cy.loadDrawingBoardWithRecreateModeNetwork(templateWithNetworkSetup);

        // Then...
        cy.getElementByDataTestsId("node-SR-IOV Provider 2-1").should('be.visible');
        cy.getElementByDataTestsId("node-SR-IOV Provider 2-2").should('be.visible');
        assertThatBodyFromDeployRequestEqualsToTemplateFromBackEnd_network();
      });

      it(`Given a stored template of Network - User can remove existing network`, () => {

        cy.loadDrawingBoardWithRecreateModeNetwork(templateWithNetworkSetup);

        cy.nodeAction('node-01f4c475-3f89-4f00-a2f4-39a873dba0ae-SR-IOV Provider 2-1', 'Remove');
        let removed_network_Path = [
          "networks", "SR-IOV Provider 2-1",
        ];

        let removed_network_counter_Path = [
          "existingNetworksCounterMap", "f6b6d141-0d4c-427d-ad35-797f3d1abe71",
        ];

        assertThatBodyFromDeployRequestEqualsToTemplateFromBackEnd_network([
          {path: removed_network_Path, value: undefined},
          {path: removed_network_counter_Path, value: 0},
        ]);
      });

      it('Given a template - User can add a new network', () => {

        cy.loadDrawingBoardWithRecreateModeNetwork(templateWithNetworkSetup);

        // add new node
        addNewNode('node-SR-IOV Provider 2-1-add-btn')
          .fillNetworkPopup()
          .getDrawingBoardDeployBtn().click()
          .wait('@expectedPostAsyncInstantiation').then(xhr => {
          const networkRequest = bodyOf(xhr).networks['SR-IOV Provider 2-1_1'];

          expect(networkRequest.action).equals("Create");
          expect(networkRequest.rollbackOnFailure).equals("true");
          expect(networkRequest.originalName).equals("SR-IOV Provider 2-1");
          expect(networkRequest.productFamilyId).equals("a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb");
          expect(networkRequest.lcpCloudRegionId).equals("hvf6");
          expect(networkRequest.lineOfBusiness).equals("zzz1");
          expect(networkRequest.platformName).equals("xxx1");
          expect(networkRequest.tenantId).equals("229bcdc6eaeb4ca59d55221141d01f8e");
        });
      });
    });
  });
});

let apiTestResources = '../vid-automation/src/test/resources/asyncInstantiation/';

const templateWithVnfSetup = {
  serviceModelId: '6cfeeb18-c2b0-49df-987a-da47493c8e38',
  instanceTemplateFile: apiTestResources + 'templates__instance_template.json',
  instanceTemplateSetWithoutModifyFile: apiTestResources + 'templates__instance_from_template__set_without_modify1.json',
  serviceModelFile: '../support/jsonBuilders/mocks/jsons/instantiationTemplates/templates__service_model.json',
};

const templateWithNetworkSetup = {
  serviceModelId: 'a1a14610-ee40-4049-8007-0608a20dd1fa',
  instanceTemplateFile: apiTestResources + 'templates__instance_template_network.json',
  serviceModelFile: '../support/jsonBuilders/mocks/jsons/instantiationTemplates/templates__service_model_network.json',
};

const vnfPath = [
  "vnfs", "vProbe_NC_VNF 0"
];

const vnfPath2 = [
  "vnfs", "vProbe_NC_VNF 0_1"
];



function addNewNode(dataTestId: string) {
  return cy.getElementByDataTestsId(dataTestId).click({force: true})
}

function removeVNFWithVFModules(dataTestId: string) {
  return cy.nodeAction(dataTestId, 'Remove')
    .getTagElementContainsText('button', 'Remove VNF').click()
}

function assertThatBodyFromDeployRequestEqualsToTemplateFromBackEnd(deviationFromExpected: { path: PropertyPath, value: any }[] = []) {
  assertThatBodyFromDeployRequestEqualsToTemplateFromBackEndInternal(templateWithVnfSetup.instanceTemplateFile, deviationFromExpected);
}

function assertThatBodyFromDeployRequestEqualsToTemplateFromBackEnd_network(deviationFromExpected: { path: PropertyPath, value: any }[] = []) {
  assertThatBodyFromDeployRequestEqualsToTemplateFromBackEndInternal(templateWithNetworkSetup.instanceTemplateFile, deviationFromExpected);
}

function assertThatBodyFromDeployRequestEqualsToTemplateFromBackEndInternal(filePathOfExpected: string, deviationFromExpected: { path: PropertyPath; value: any }[]) {
  cy.getDrawingBoardDeployBtn().click();
  cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
    cy.readFile(filePathOfExpected).then((expectedResult) => {
      convertRollbackOnFailureValueFromStringToBoolean(expectedResult);

      let xhrBodyWithoutIsDirtyField = removeIsDirtyFieldFromXhrRequestBody(xhr);
      setDeviationInExpected(expectedResult, deviationFromExpected);
      cy.deepCompare(xhrBodyWithoutIsDirtyField, expectedResult);
    });
  });
}


function assertThatBodyFromDeployRequestEqualsToFile(deviationFromExpected: { path: PropertyPath, value: any }[] = []) {
  cy.getDrawingBoardDeployBtn().click();
  cy.wait('@expectedPostAsyncInstantiation').then(xhr => {

    cy.readFile(templateWithVnfSetup.instanceTemplateSetWithoutModifyFile).then((expectedResult) => {
      setDeviationInExpected(expectedResult, deviationFromExpected);
      let actualObj = xhr.request.body;
      // @ts-ignore
      let actual_vprobe_0 = xhr.request.body['vnfs']['vProbe_NC_VNF 0']['vfModules'];
      // @ts-ignore
      let actual_vprobe_1 = xhr.request.body['vnfs']['vProbe_NC_VNF 0_1']['vfModules'];
      actual_vprobe_0['vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0']['vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0ahubg'].pauseInstantiation = null;
      actual_vprobe_0['vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1']['vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1yprvi'].pauseInstantiation = null;
      actual_vprobe_1['vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0']['vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0ahubg'].pauseInstantiation = null;
      actual_vprobe_1['vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1']['vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1yprvi'].pauseInstantiation = null;

      actual_vprobe_0['vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0']['vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0ahubg'].position = null;
      actual_vprobe_0['vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1']['vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1yprvi'].position = null;
      actual_vprobe_1['vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0']['vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0ahubg'].position = null;
      actual_vprobe_1['vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1']['vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1yprvi'].position = null;
      let vnfs = expectedResult['vnfs'];
      let expected_vProbe_NC_VNF_0_vfModules = vnfs['vProbe_NC_VNF 0']['vfModules'];
      let expected_vProbe_NC_VNF_0_1_vfModules = vnfs['vProbe_NC_VNF 0_1']['vfModules'];
      expected_vProbe_NC_VNF_0_vfModules['vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0']['vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0ahubg'].position = null;
      expected_vProbe_NC_VNF_0_vfModules['vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1']['vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1yprvi'].position = null;
      expected_vProbe_NC_VNF_0_1_vfModules['vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0']['vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0ahubg'].position = null;
      expected_vProbe_NC_VNF_0_1_vfModules['vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1']['vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1yprvi'].position = null;
      expected_vProbe_NC_VNF_0_vfModules['vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0']['vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0ahubg'].pauseInstantiation = null;
      expected_vProbe_NC_VNF_0_vfModules['vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1']['vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1yprvi'].pauseInstantiation = null;
      expected_vProbe_NC_VNF_0_1_vfModules['vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0']['vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0ahubg'].pauseInstantiation = null;
      expected_vProbe_NC_VNF_0_1_vfModules['vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1']['vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1yprvi'].pauseInstantiation = null;
      cy.deepCompare(xhr.request.body, expectedResult);
    });

  });
}

function bodyOf(xhr: Cypress.WaitXHR) {
  return JSON.parse(JSON.stringify(xhr.request.body));
}

function setDeviationInExpected(expectedResult: any, deviations: { path: PropertyPath; value: any }[]) {
  for (const deviation of deviations) {
    _.set(expectedResult, deviation.path, deviation.value);
  }
}

function findPathOfLatestVfModule(serviceInstanceElementFromRedux: any, vfModulesContainerPath: string[]) {
  let latestVfModuleRandomlySelectedKey: string = _.last(_.keys(
    _.get(serviceInstanceElementFromRedux, vfModulesContainerPath)
  )) as string;

  return [...vfModulesContainerPath, latestVfModuleRandomlySelectedKey];
}

//We use this function because the deployService() on drawing-board-header.component class
// changes rollbackOnFailure value from string type to boolean.
function convertRollbackOnFailureValueFromStringToBoolean(expectedResult: any) {
  expectedResult.rollbackOnFailure = Boolean(expectedResult.rollbackOnFailure);
}

function removeIsDirtyFieldFromXhrRequestBody(xhr: any) {
  let xhrTempBody = bodyOf(xhr);
  delete xhrTempBody.isDirty;
  return xhrTempBody;
}

function mockAsyncBulkResponse() {
  cy.server().route({
    url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
    method: 'POST',
    status: 200,
    response: true,
  }).as("expectedPostAsyncInstantiation");
}

