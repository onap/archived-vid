///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
import {JsonBuilder} from '../../support/jsonBuilders/jsonBuilder';
import {ServiceModel} from '../../support/jsonBuilders/models/service.model';
import {AaiServiceInstancesModel} from '../../support/jsonBuilders/models/serviceInstances.model';
import {AAISubDetailsModel} from '../../support/jsonBuilders/models/aaiSubDetails.model';
import {AAISubViewEditModel} from '../../support/jsonBuilders/models/aaiSubViewEdit.model';

describe('Resume tests', function () {
  describe('Resume Defect 710619 - VID doesn\'t prompt to allow the user to attach the volume group on clicking Resume', () => {

    let jsonBuilderAAIService: JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();
    let jsonBuilderAAISubViewEditModel: JsonBuilder<AAISubViewEditModel> = new JsonBuilder<AAISubViewEditModel>();
    let jsonBuilderAAISubDetailsModel: JsonBuilder<AAISubDetailsModel> = new JsonBuilder<AAISubDetailsModel>();
    let jsonBuilderAaiServiceInstances: JsonBuilder<AaiServiceInstancesModel> = new JsonBuilder<AaiServiceInstancesModel>();
    beforeEach(() => {
      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/defect710619/serviceE2E.json').then((res) => {
        jsonBuilderAAIService.basicJson(
          res,
          Cypress.config('baseUrl') + "/rest/models/services/**",
          200, 0,
          "service-complexService")
      });


      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/defect710619/aaiSubViewEditForServiceWithSomeVFModuleE2E.json').then((res) => {
        jsonBuilderAAISubViewEditModel.basicJson(
          res,
          Cypress.config('baseUrl') + "/aai_sub_viewedit/**",
          200,
          0,
          "aai-sub-view-edit")
      });

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/defect710619/aaiSubDetailsE2E.json').then((res) => {
        jsonBuilderAAISubDetailsModel.basicJson(
          res,
          Cypress.config('baseUrl') + "/aai_sub_details/**",
          200,
          0,
          "aai-sub-details")
      });

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/defect710619/aaiServiceInstancesE2E.json').then((res) => {
        jsonBuilderAaiServiceInstances.basicJson(
          res,
          Cypress.config('baseUrl') + "/search_service_instances**",
          200,
          0,
          "aai-get-service-instances")
      });

      cy.server().route({
        url: Cypress.config('baseUrl') + '/mso/mso_create_vfmodule_instance/dedd680f-f3bd-46d8-97c7-b6fc28db9317/vnfs/1d45e992-e282-4ba5-b17a-9ca6562169e3',
        method: 'POST',
        status: 200,
        response: "[]",
      }).as("actualResumeCall");

      cy.initVidMock();

      cy.initTenants();

      cy.login();
    });

    afterEach(() => {
      cy.screenshot();
    });

    it(`Resume Defect 710619`, function () {
      cy.visit('/serviceModels.htm#/instantiate?subscriberId=e433710f-9217-458d-a79d-1c7aff376d89&subscriberName=SILVIA ROBBINS&serviceType=TYLER%20SILVIA&serviceInstanceId=dedd680f-f3bd-46d8-97c7-b6fc28db9317&aaiModelVersionId=4b60252a-bf6c-40df-9db5-98b4c363fdf4&isPermitted=true').then(()=>{
        cy.wait('@service-complexService');
        checkResumeAndPopup("dpa2bccfx5992v_base_module","vfModuleTreeNode-assigned");
      });
     });

    function checkResumeAndPopup(vfModuleName:string, vfModuleClassName:string)  {
      cy.get('div').find('.' + vfModuleClassName)
        .getElementByDataTestsId('resumeVFModuleButton-' + vfModuleName).click().then(()=> {
        cy.getElementByDataTestsId('confirmResumeDeleteButton').should('be.visible')
          .getElementByDataTestsId('softDeleteButton').should('not.be.visible')
          .getElementByDataTestsId('lcpRegion').should('be.visible').select("option-irma-aic-hvf6")
          .getElementByDataTestsId('tenant').should('be.visible').select("bae71557c5bb4d5aac6743a4e5f1d054");
        cy.getElementByDataTestsId('confirmResumeDeleteButton').not('.button--inactive').click().then(()=> {
            cy.wait('@actualResumeCall').then(xhr => {
              cy.readFile('cypress/support/jsonBuilders/mocks/jsons/defect710619/expectedResumeWithVGResults.json').then((expectedResult) => {
                cy.deepCompare(xhr.request.body, expectedResult);
              });
            });
        });
      });
    }
  });
});

