import * as _ from "lodash";
import {PropertyPath} from "lodash";

describe('Drawing Board: Instantiation Templates', function () {

  describe('Instantiation templates ', () => {

    beforeEach(() => {
      cy.clearSessionStorage();
      cy.setTestApiParamToVNF();
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

      it(`Given a stored template - when click "deploy" - then a coherent request should be sent upon deploy`,  () => {

        loadDrawingBoardWithRecreateMode();

        // Then...
        cy.getElementByDataTestsId("node-vProbe_NC_VNF 0").should('be.visible');
        assertThatBodyFromDeployRequestEqualsToTemplateFromBackEnd();
      });

      it('Given a stored template - when "edit" vnf and vfmodules are opened - then template’s details are visible as expected', ()=> {

        loadDrawingBoardWithRecreateMode();

        // Then...
        editNode("node-21ae311e-432f-4c54-b855-446d0b8ded72-vProbe_NC_VNF 0")
        .getElementByDataTestsId("instanceName").should('have.value', 'hvf6arlba007')
        .getElementByDataTestsId("productFamily").should('contain', 'Emanuel')
        .getElementByDataTestsId("tenant").should('contain', 'DN5242-Nov21-T1')
        .getElementByDataTestsId("lcpRegion").should('contain', 'hvf6')
        .getElementByDataTestsId("lineOfBusiness").should('contain', 'zzz1')
        .getElementByDataTestsId("rollback").should('contain', 'Rollback')
        .checkPlatformValue('xxx1')
         .getElementByDataTestsId("cancelButton").click();

        editNode("node-c5b26cc1-a66f-4b69-aa23-6abc7c647c88-vprobe_nc_vnf0..VprobeNcVnf..FE_base_module..module-0")
        .getElementByDataTestsId("instanceName").should('have.value', 'hvf6arlba007_lba_Base_01')
        .getElementByDataTestsId("lcpRegion").should('contain', 'hvf6')
        .getElementByDataTestsId("tenant").should('contain', 'DN5242-Nov21-T1')
        .getElementByDataTestsId("rollback").should('contain', 'Rollback')
        .getElementByDataTestsId("cancelButton").click();

        editNode("node-c09e4530-8fd8-418f-9483-2f57ce927b05-vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1")
        .getElementByDataTestsId("instanceName").should('have.value', 'my_hvf6arlba007_lba_dj_01')
        .getElementByDataTestsId("volumeGroupName").should('have.value', 'my_special_hvf6arlba007_lba_dj_01_vol')
        .getElementByDataTestsId("lcpRegion").should('contain', 'hvf6')
        .getElementByDataTestsId("tenant").should('contain', 'DN5242-Nov21-T1')
        .getElementByDataTestsId("rollback").should('contain', 'Rollback')
        .getElementByDataTestsId("sdncPreLoad").should('have.value', 'on')
        .getElementByDataTestsId("cancelButton").click();



        assertThatBodyFromDeployRequestEqualsToTemplateFromBackEnd();
        });

      it(`Given a stored template - when "edit" service is opened - then template’s details are visible as expected`,  function ()  {

        loadDrawingBoardWithRecreateMode();

        cy.openServiceContextMenu()
        .getElementByDataTestsId("context-menu-header-edit-item").click()
        .getElementByDataTestsId("instanceName").should('have.value', 'vProbe_NC_Service_DG_new_SI')
        .getElementByDataTestsId("subscriberName").should('contain', 'SILVIA ROBBINS')
        .getElementByDataTestsId("serviceType").should('contain', 'TYLER SILVIA')
        .getElementByDataTestsId("owningEntity").should('contain', 'WayneHolland')
        .getElementByDataTestsId("project").should('contain', 'WATKINS')
        .getElementByDataTestsId("rollback").should('contain', 'Rollback');

      });


      it.only(`Given a stored template - when changing sdncPreLoad option and click "deploy" - then sdncPreLoad becomes false`, () => {

        loadDrawingBoardWithRecreateMode();

        editNode("node-c09e4530-8fd8-418f-9483-2f57ce927b05-vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1")
        .getElementByDataTestsId('sdncPreLoad').click()
        .getElementByDataTestsId("form-set").click();

        // Then...
        let vfModulePath = [
          "vnfs", "vProbe_NC_VNF 0", "vfModules",
          "vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1",
          "vprobe_nc_vnf0..VprobeNcVnf..FE_Add_On_Module_vlbagent_eph..module-1yprvi",
        ];

        assertThatBodyFromDeployRequestEqualsToTemplateFromBackEnd([
          {path: [...vfModulePath, "sdncPreLoad"], value: false},

          // The following values are modified when clicking "edit"->"set", regardless
          // of modified values above
          // ---------------

          // 1) Added values
          {path: [...vfModulePath, "isMissingData"], value: false},

          // 2) Removed values
          {path: [...vfModulePath, "action"], value: undefined},
          {path: [...vfModulePath, "trackById"], value: undefined},
          {path: [...vfModulePath, "isFailed"], value: undefined},
          {path: [...vfModulePath, "modelInfo", "modelType"], value: undefined},

          // 3) Updated values
          {
            path: ["existingNames"], value: {
              "my_hvf6arlba007_lba_dj_01": "",
              "my_special_hvf6arlba007_lba_dj_01_vol": ""
            }
          },
        ]);
      });

    });
  });
});

function loadDrawingBoardWithRecreateMode() {
  const serviceModelId = '6cfeeb18-c2b0-49df-987a-da47493c8e38';
  const templateUuid = "46390edd-7100-46b2-9f18-419bd24fb60b";

  const drawingBoardAction = `RECREATE`;
  const templateTopologyEndpoint = "templateTopology";
  cy.route(`**/rest/models/services/${serviceModelId}`,
    'fixture:../support/jsonBuilders/mocks/jsons/instantiationTemplates/templates__service_model.json')
  .as('serviceModel');

  cy.route(`**/asyncInstantiation/${templateTopologyEndpoint}/${templateUuid}`,
    'fixture:../../../vid-automation/src/test/resources/asyncInstantiation/templates__instance_template.json')
  .as('templateTopology');

  // When...

  cy.openIframe(`app/ui/#/servicePlanning/${drawingBoardAction}` +
    `?jobId=${templateUuid}` +
    `&serviceModelId=${serviceModelId}`);

  cy.wait('@serviceModel');
  cy.wait('@templateTopology');
}

function editNode(dataTestId: string) {
  return cy.drawingBoardTreeOpenContextMenuByElementDataTestId(dataTestId)
    .drawingBoardTreeClickOnContextMenuOptionByName('Edit')
}

function assertThatBodyFromDeployRequestEqualsToTemplateFromBackEnd(caveatsFromExpected: { path: PropertyPath, value: any }[] = []) {
  cy.getDrawingBoardDeployBtn().click();
  cy.wait('@expectedPostAsyncInstantiation').then(xhr => {
    cy.readFile('../vid-automation/src/test/resources/asyncInstantiation/templates__instance_template.json').then((expectedResult) => {
      convertRollbackOnFailureValueFromStringToBoolean(expectedResult);
      setCaveatsInExpected(expectedResult, caveatsFromExpected);

      let xhrBodyWithoutIsDirtyField = removeIsDirtyFieldFromXhrRequestBody(xhr);
      deleteAllFieldsNamed(xhrBodyWithoutIsDirtyField, "uuid");
      deleteAllFieldsNamed(xhrBodyWithoutIsDirtyField, "modelUniqueId");
      cy.deepCompare(xhrBodyWithoutIsDirtyField, expectedResult);
    });
  });
}

function setCaveatsInExpected(expectedResult: any, caveats: { path: PropertyPath; value: any }[]) {
  for (const caveat of caveats) {
    _.set(expectedResult, caveat.path, caveat.value);
  }
}

  //We use this function because the deployService() on drawing-board-header.component class
  // changes rollbackOnFailure value from string type to boolean.
  function convertRollbackOnFailureValueFromStringToBoolean(expectedResult: any) {
    expectedResult.rollbackOnFailure = Boolean(expectedResult.rollbackOnFailure);
  }

function deleteAllFieldsNamed(obj: any, name: string) {
  _.keys(obj).forEach(key=> {
    console.log(key, obj[key]);
    if (key === name)
      delete obj[key];
    else if (typeof obj[key] === typeof {})
      deleteAllFieldsNamed(obj[key], name);
  });
}

function removeIsDirtyFieldFromXhrRequestBody(xhr : any) {
  let xhrTempBody = JSON.parse(JSON.stringify(xhr.request.body));
  delete xhrTempBody.isDirty;
  return xhrTempBody;
}

  function mockAsyncBulkResponse() {
    cy.server().route({
      url: Cypress.config('baseUrl') + '/asyncInstantiation/bulk',
      method: 'POST',
      status: 200,
      response: "[]",
    }).as("expectedPostAsyncInstantiation");
  }
