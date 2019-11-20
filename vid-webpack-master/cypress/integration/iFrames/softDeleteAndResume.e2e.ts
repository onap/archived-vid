///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
import {JsonBuilder} from '../../support/jsonBuilders/jsonBuilder';
import {ServiceModel} from '../../support/jsonBuilders/models/service.model';
import {AaiServiceInstancesModel} from '../../support/jsonBuilders/models/serviceInstances.model';
import {AAISubDetailsModel} from '../../support/jsonBuilders/models/aaiSubDetails.model';
import {AAISubViewEditModel} from '../../support/jsonBuilders/models/aaiSubViewEdit.model';

describe('Soft delete tests', function () {
  describe('basic UI tests', () => {

    var jsonBuilderAAIService: JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();
    var jsonBuilderAAISubViewEditModel: JsonBuilder<AAISubViewEditModel> = new JsonBuilder<AAISubViewEditModel>();
    var jsonBuilderAAISubDetailsModel: JsonBuilder<AAISubDetailsModel> = new JsonBuilder<AAISubDetailsModel>();
    var jsonBuilderAaiServiceInstances: JsonBuilder<AaiServiceInstancesModel> = new JsonBuilder<AaiServiceInstancesModel>();
    beforeEach(() => {
      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/basicService.json').then((res) => {
        jsonBuilderAAIService.basicJson(
          res,
          Cypress.config('baseUrl') + "/rest/models/services/6e59c5de-f052-46fa-aa7e-2fca9d674c44",
          200, 0,
          "service-complexService",
          changeServiceModel)
      });


      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/aaiSubViewEditForServiceWithSomeVFModule.json').then((res) => {
        jsonBuilderAAISubViewEditModel.basicJson(
          res,
          Cypress.config('baseUrl') + "/aai_sub_viewedit/**/**/**/3f93c7cb-2fd0-4557-9514-e189b7b04f9d",
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

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/aaiServiceInstances.json').then((res) => {
        jsonBuilderAaiServiceInstances.basicJson(
          res,
          Cypress.config('baseUrl') + "/search_service_instances**",
          200,
          0,
          "aai-get-service-instances")
      });

      cy.initVidMock();

      cy.initTenants();

      cy.login();
    });

    afterEach(() => {
      cy.screenshot();
    });

    it(`Soft delete button will be display also if base module is true`, function () {
      cy.visit('/serviceModels.htm#/instantiate?subscriberId=e433710f-9217-458d-a79d-1c7aff376d89&subscriberName=SILVIA%20ROBBINS&serviceType=TYLER%20SILVIA&serviceInstanceId=3f93c7cb-2fd0-4557-9514-e189b7b04f9d&aaiModelVersionId=6e59c5de-f052-46fa-aa7e-2fca9d674c44&isPermitted=true');
      cy.wait('@service-complexService');
      checkSoftDeleteAndDeletePopup('aa', 'vfModuleTreeNode-pendingactivation', true, true);
    });

    it(`Soft delete button not display in assigned orch status`, function () {
      cy.visit('/serviceModels.htm#/instantiate?subscriberId=e433710f-9217-458d-a79d-1c7aff376d89&subscriberName=SILVIA%20ROBBINS&serviceType=TYLER%20SILVIA&serviceInstanceId=3f93c7cb-2fd0-4557-9514-e189b7b04f9d&aaiModelVersionId=6e59c5de-f052-46fa-aa7e-2fca9d674c44&isPermitted=true');
      cy.wait('@service-complexService');
      checkSoftDeleteAndDeletePopup('gg', 'vfModuleTreeNode-assigned', false, true);
    });

    it(`Resume button display in orch status - pendingactivation, assigned - feature FLAG_VF_MODULE_RESUME_STATUS_CREATE - is OFF`, function () {

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/flags.cypress.json').then(() => {
        cy.server()
          .route({
            method: 'GET',
            delay :  0,
            status :  200,
            url : Cypress.config('baseUrl') + "/flags**",
            response : {
              "FLAG_1810_CR_SOFT_DELETE_ALACARTE_VF_MODULE": true,
              "FLAG_VF_MODULE_RESUME_STATUS_CREATE": false
            }
          }).as('initFlags');
      });

      cy.visit('/serviceModels.htm#/instantiate?subscriberId=e433710f-9217-458d-a79d-1c7aff376d89&subscriberName=SILVIA%20ROBBINS&serviceType=TYLER%20SILVIA&serviceInstanceId=3f93c7cb-2fd0-4557-9514-e189b7b04f9d&aaiModelVersionId=6e59c5de-f052-46fa-aa7e-2fca9d674c44&isPermitted=true');
      cy.wait('@service-complexService');

      checkResumeAndPopup('aa', 'vfModuleTreeNode-pendingactivation');
      checkResumeAndPopup('gg', 'vfModuleTreeNode-assigned');

      cy.get('.vfModuleTreeNode-created')
        .getElementByDataTestsId('resumeVFModuleButton-ABC').should('not.be.visible');
      cy.get('.vfModuleTreeNode-pending-delete')
        .getElementByDataTestsId('resumeVFModuleButton-my_vfModule').should('not.be.visible');

    });

    it(`Resume button display in orch status - pendingactivation, assigned, created - feature FLAG_VF_MODULE_RESUME_STATUS_CREATE - is ON`, function () {

      cy.visit('/serviceModels.htm#/instantiate?subscriberId=e433710f-9217-458d-a79d-1c7aff376d89&subscriberName=SILVIA%20ROBBINS&serviceType=TYLER%20SILVIA&serviceInstanceId=3f93c7cb-2fd0-4557-9514-e189b7b04f9d&aaiModelVersionId=6e59c5de-f052-46fa-aa7e-2fca9d674c44&isPermitted=true');
      cy.wait('@service-complexService');

      checkResumeAndPopup('aa', 'vfModuleTreeNode-pendingactivation');
      checkResumeAndPopup('gg', 'vfModuleTreeNode-assigned');
      checkResumeAndPopup('ABC', 'vfModuleTreeNode-created');

      cy.get('.vfModuleTreeNode-pending-delete')
        .getElementByDataTestsId('resumeVFModuleButton-my_vfModule').should('not.be.visible');

    });

    it(`Delete popup with not homing data from AAI`, function () {
      cy.visit('/serviceModels.htm#/instantiate?subscriberId=e433710f-9217-458d-a79d-1c7aff376d89&subscriberName=SILVIA%20ROBBINS&serviceType=TYLER%20SILVIA&serviceInstanceId=3f93c7cb-2fd0-4557-9514-e189b7b04f9d&aaiModelVersionId=6e59c5de-f052-46fa-aa7e-2fca9d674c44&isPermitted=true');
      cy.wait('@service-complexService');
      checkSoftDeleteAndDeletePopup('gg', 'vfModuleTreeNode-assigned', false, true);
      cy.selectDropdownOptionByText('lcpRegion', 'AAIAIC25 (AIC)');
      cy.getElementByDataTestsId('confirmResumeDeleteButton').should('have.attr', 'disabled');
      cy.typeToInput("lcpRegionText", "just another region");
      cy.getElementByDataTestsId('confirmResumeDeleteButton').should('have.attr', 'disabled');
      cy.selectDropdownOptionByText('tenant', 'USP-SIP-IC-24335-T-01');
      cy.getElementByDataTestsId('confirmResumeDeleteButton').should('not.have.attr', 'disabled');
      cy.selectDropdownOptionByText('lcpRegion', 'hvf6 (AIC)');
      cy.getElementByDataTestsId('confirmResumeDeleteButton').should('have.attr', 'disabled');
      cy.selectDropdownOptionByText('tenant', 'AIN Web Tool-15-D-testalexandria');
      cy.getElementByDataTestsId('confirmResumeDeleteButton').should('not.have.attr', 'disabled');
      cy.getElementByDataTestsId('cancel').click({force: true});
      cy.getElementByDataTestsId('confirmResumeDeleteButton').should('not.be.visible');
    });

    it(`Soft delete button display with partial homing data from AAI`, function () {

      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/aaiGetHomingData.json').then((res) => {
        jsonBuilderAaiServiceInstances.basicJson(
          res,
          Cypress.config('baseUrl') + "/aai_get_homing_by_vfmodule/c015cc0f-0f37-4488-aabf-53795fd93cd3/a231a99c-7e75-4d6d-a0fb-5c7d26f30f77",
          200,
          0,
          "aai-get-homing-data")
      });


      cy.visit('/serviceModels.htm#/instantiate?subscriberId=e433710f-9217-458d-a79d-1c7aff376d89&subscriberName=SILVIA%20ROBBINS&serviceType=TYLER%20SILVIA&serviceInstanceId=3f93c7cb-2fd0-4557-9514-e189b7b04f9d&aaiModelVersionId=6e59c5de-f052-46fa-aa7e-2fca9d674c44&isPermitted=true');
      cy.wait('@service-complexService');
      checkSoftDeleteAndDeletePopup('my_vfModule', 'vfModuleTreeNode-pending-delete', true, true);
      cy.getElementByDataTestsId('lcpRegionText').should('be.visible');
      cy.getElementByDataTestsId('lcpRegion').contains('AAIAIC25');
      cy.getElementByDataTestsId('tenant').contains('USP-SIP-IC-24335-T-01');
    });

    it(`Soft delete button display with with homing data from AAI`, function () {

      cy.readFile('../vid-automation/src/test/resources/viewEdit/aaiHomingDataResponse.json').then((res) => {
        jsonBuilderAaiServiceInstances.basicJson(
          res,
          Cypress.config('baseUrl') + "/aai_get_homing_by_vfmodule/0846287b-65bf-45a6-88f6-6a1af4149fac/a9b70ac0-5917-4203-a308-0e6920e6d09b",
          200,
          0,
          "aai-get-homing-data")
      });

      cy.visit('/serviceModels.htm#/instantiate?subscriberId=e433710f-9217-458d-a79d-1c7aff376d89&subscriberName=SILVIA%20ROBBINS&serviceType=TYLER%20SILVIA&serviceInstanceId=3f93c7cb-2fd0-4557-9514-e189b7b04f9d&aaiModelVersionId=6e59c5de-f052-46fa-aa7e-2fca9d674c44&isPermitted=true');
      cy.wait('@service-complexService');
      checkSoftDeleteAndDeletePopup('vf_module2', 'vfModuleTreeNode-pendingcreate', true, false);
    });

    it(`Soft delete and Delete - Mega region is AAIAIC25 - not 'olson3', 'olson5a'`, function () {
      cy.visit('/serviceModels.htm#/instantiate?subscriberId=e433710f-9217-458d-a79d-1c7aff376d89&subscriberName=SILVIA%20ROBBINS&serviceType=TYLER%20SILVIA&serviceInstanceId=3f93c7cb-2fd0-4557-9514-e189b7b04f9d&aaiModelVersionId=6e59c5de-f052-46fa-aa7e-2fca9d674c44&isPermitted=true');
      cy.wait('@service-complexService');
      //Delete
      checkSoftDeleteAndDeletePopup('gg', 'vfModuleTreeNode-assigned', false, true);
      checkLegacyRegion();
      cy.getElementByDataTestsId('cancel').click();
      //Soft delete
      checkSoftDeleteAndDeletePopup('aa', 'vfModuleTreeNode-pendingactivation', true, true);
      checkLegacyRegion();
      cy.getElementByDataTestsId('cancel').click();
      //Resume
      cy.get('div').find('.vfModuleTreeNode-pendingactivation')
        .getElementByDataTestsId('resumeVFModuleButton-aa').click().then(()=> {
        checkLegacyRegion();
      });
    });

    function checkLegacyRegion() {
      checkIsLegacyRegionTextIsDisplay('AAIAIC25 (AIC)', true);
      checkIsLegacyRegionTextIsDisplay('olson3 (AIC)', false);
      checkIsLegacyRegionTextIsDisplay('olson5a (AIC)', false);
      checkIsLegacyRegionTextIsDisplay('hvf6 (AIC)', false);
    }

    function checkIsLegacyRegionTextIsDisplay(lcpRegionName: string, isVisible: boolean) {
      const isVisibleText = isVisible ? 'be.visible' : 'not.be.visible';
      cy.selectDropdownOptionByText('lcpRegion', lcpRegionName);
      cy.getElementByDataTestsId('lcpRegionText').should(isVisibleText);

    }

    function checkResumeAndPopup(vfModuleName:string, vfModuleClassName:string)  {
      cy.get('div').find('.' + vfModuleClassName)
        .getElementByDataTestsId('resumeVFModuleButton-' + vfModuleName).click().then(()=> {
        cy.getElementByDataTestsId('confirmResumeDeleteButton').should('be.visible')
          .getElementByDataTestsId('softDeleteButton').should('not.be.visible')
          .getElementByDataTestsId('lcpRegion').should('be.visible')
          .getElementByDataTestsId('tenant').should('be.visible')
          .getElementByDataTestsId('modalTitle').contains('Instantiate')
          .getElementByDataTestsId('confirmResumeDeleteButton').contains('Instantiate')
          .getElementByDataTestsId('cancel').click();
      });
    }

    function checkSoftDeleteAndDeletePopup(vfModuleName:string, vfModuleClassName:string, softDeleteEnable:boolean, isNoHomingData:boolean)  {
      const visibleString = 'be.visible';
      const NOT = 'not.';
      const softDeleteVisibleString = (softDeleteEnable) ? visibleString : NOT + visibleString;
      const isNoHomingDataVisibleString = (isNoHomingData) ? visibleString : NOT + visibleString;
      cy.get('div').find('.' + vfModuleClassName)
        .getElementByDataTestsId('deleteVFModuleButton-' + vfModuleName).click({force: true}).then(()=> {
        cy.getElementByDataTestsId('confirmResumeDeleteButton').should('be.visible')
          .getElementByDataTestsId('softDeleteButton').should(softDeleteVisibleString)
          .getElementByDataTestsId('lcpRegion').should(isNoHomingDataVisibleString)
          .getElementByDataTestsId('tenant').should(isNoHomingDataVisibleString);
      });
    }
  });



  function changeServiceModel(serviceModel: ServiceModel) {
    serviceModel.service.uuid = "6e59c5de-f052-46fa-aa7e-2fca9d674c44";
    return serviceModel;
  }
});

