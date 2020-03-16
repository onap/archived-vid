///<reference path="../../../node_modules/cypress/types/index.d.ts"/>
import {JsonBuilder} from "../../support/jsonBuilders/jsonBuilder";
import {ServiceModel} from "../../support/jsonBuilders/models/service.model";

describe('Drawing board', function () {
  beforeEach(() => {
      cy.clearSessionStorage();
      cy.setReduxState();
      cy.preventErrorsOnLoading();
      cy.initAAIMock();
      cy.initVidMock();
      cy.initActiveNetworks();
      cy.login();
  });

  afterEach(() => {
    cy.screenshot();
  });

  function addSameVnfMultipleTimes() {
    cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
    const vnfNodeName = 'node-2017-488_PASQUALE-vPE 0';
    cy.drawingBoardPressAddButtonByElementName(vnfNodeName).get('i').should('have.class', 'fa-plus-circle');
    cy.drawingBoardPressAddButtonByElementName(vnfNodeName).click({force: true});
    cy.fillVnfPopup().then(() => {
      cy.drawingBoardPressAddButtonByElementName(vnfNodeName).click({force: true});
      cy.fillVnfPopup().then(() => {
        cy.drawingBoardPressAddButtonByElementName(vnfNodeName).click({force: true});
        cy.fillVnfPopup().then(() => {
          cy.drawingBoardNumberOfExistingElementsShouldContains(3);
        });
      });
    });
  }

  describe('duplicate', () => {

    it('delete vf module reduce the number of vf modules ', function () {
      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/emptyServiceRedux.json').then((res) => {
        cy.setReduxState(<any>res);
        cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
        cy.drawingBoardPressAddButtonByElementName('node-2017-488_PASQUALE-vPE 0').get('i').should('have.class', 'fa-plus-circle');
        cy.drawingBoardPressAddButtonByElementName('node-2017-488_PASQUALE-vPE 0').click({force: true});
        cy.fillVnfPopup().then(() => {
          cy.drawingBoardPressAddButtonByElementName('node-2017-488_PASQUALE-vPE 0').click({force: true});
          cy.fillVnfPopup().then(() => {
            cy.drawingBoardNumberOfExistingElementsShouldContains(2);
            cy.drawingBoardTreeOpenContextMenuByElementDataTestId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_PASQUALE-vPE 0', 1)
              .drawingBoardTreeClickOnContextMenuOptionByName('Remove');
            cy.drawingBoardNumberOfExistingElementsShouldContains(1);
          });
        })
      });
    });

    it('create new  vf module  update the number of vf modules ', () => {
      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/emptyServiceRedux.json').then((res) => {
        cy.setReduxState(<any>res);
        addSameVnfMultipleTimes();
      });
    });

    it('duplicate vnf multi - should update number of vf modules on left side and disable duplicate when created max', () => {
      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/emptyServiceRedux.json').then((res) => {
        cy.setReduxState(<any>res);
        cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
        cy.drawingBoardPressAddButtonByElementName('node-2017-488_PASQUALE-vPE 0').get('i').should('have.class', 'fa-plus-circle')
          .drawingBoardPressAddButtonByElementName('node-2017-488_PASQUALE-vPE 0').click({force: true});
        cy.fillVnfPopup().then(() => {
          cy.drawingBoardTreeOpenContextMenuByElementDataTestId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_PASQUALE-vPE 0')
            .drawingBoardTreeClickOnContextMenuOptionByName('Duplicate')
            .get('.quantity-select option').should('have.length', 9)
            .getElementByDataTestsId('duplicate-amount-vfmodules').select('4')
            .getTagElementContainsText('button', 'Duplicate').click({force: true});
          cy.drawingBoardTreeOpenContextMenuByElementDataTestId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_PASQUALE-vPE 0', 2)
            .drawingBoardTreeClickOnContextMenuOptionByName('Duplicate')
            .get('.quantity-select option').should('have.length', 5)
            .getElementByDataTestsId('duplicate-amount-vfmodules').select('5')
            .getTagElementContainsText('button', 'Duplicate').click({force: true});
          cy.getElementByDataTestsId('node-2017-488_PASQUALE-vPE 0').get("span").should('have.class', 'icon-v ng-star-inserted');
          cy.drawingBoardTreeOpenContextMenuByElementDataTestId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_PASQUALE-vPE 0', 9)
            .get('ul.dropdown-menu li:nth-child(2)').should('have.class', 'disabled');
          // close menu
          cy.get('body').click();
          cy.drawingBoardNumberOfExistingElementsShouldContains(10);
          cy.drawingBoardTreeOpenContextMenuByElementDataTestId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_PASQUALE-vPE 0', 1)
            .drawingBoardTreeClickOnContextMenuOptionByName('Remove');
          cy.drawingBoardNumberOfExistingElementsShouldContains(9);
          cy.drawingBoardTreeOpenContextMenuByElementDataTestId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_PASQUALE-vPE 0', 3)
            .get('ul.dropdown-menu li:nth-child(2)').should('not.have.class', 'disabled');
        })
      });
    });

    it('cancel duplicate multi vnf - shouldn\'t duplicate', () => {
      let res = getReduxWithVNFS(true);
      cy.setReduxState(<any>res);
      cy.openIframe('app/ui/#/servicePlanning?serviceModelId=6e59c5de-f052-46fa-aa7e-2fca9d674c44');
      const vnfNode = 'node-d6557200-ecf2-4641-8094-5393ae3aae60-VF_vGeraldine 0';
      cy.getElementByDataTestsId(vnfNode).should('have.length', 1)
        .drawingBoardTreeOpenContextMenuByElementDataTestId(vnfNode)
        .drawingBoardTreeClickOnContextMenuOptionByName('Duplicate')
        .getElementByDataTestsId('duplicate-amount-vfmodules').select('5')
        .getTagElementContainsText('button', 'Cancel').click({force: true})
        .getElementByDataTestsId(vnfNode).should('have.length', 1);
    });

    it('service with 2 network - can add unlimited number of network instances', () => {
      let res = getReduxWith2Networks();
      cy.setReduxState(<any>res);
      cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2ab1da67-39cc-425f-ba52-59a64d0ea04a');
      cy.get('drawing-board-tree tree-node-content').should('have.have.length', 1);

      addNetworkFromModel('node-SR-IOV Provider 2-1').then(() => {
      });
    });

    it('duplicate vnf macro', () => {
      let res = getReduxWithVNFS(true);
      cy.setReduxState(<any>res);
      cy.openIframe('app/ui/#/servicePlanning?serviceModelId=6e59c5de-f052-46fa-aa7e-2fca9d674c44');
      const vnfNode = 'node-d6557200-ecf2-4641-8094-5393ae3aae60-VF_vGeraldine 0';
      const vfModuleNode = 'node-522159d5-d6e0-4c2a-aa44-5a542a12a830-vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1';
      cy.duplicateVnf(vnfNode, 1);
      //edit the second vnf lineOfBusiness to be ECOMP
      editSecondVnf(vnfNode);
      assertEditvfModuleShowFile(vfModuleNode, "sample.json");
      //assert that each vnf has it's own lineOfBusiness
      cy.getReduxState().then((state) => {
        const serviceInstance = state.service.serviceInstance['6e59c5de-f052-46fa-aa7e-2fca9d674c44'];
        chai.expect(serviceInstance.vnfs['VF_vGeraldine 0'].lineOfBusiness).equal("zzz1");
        chai.expect(serviceInstance.vnfs['VF_vGeraldine 0:0001'].lineOfBusiness).equal("ONAP");
      });
    });

    it('delete duplicate vnf ', () => {
      let res = getReduxWith2VNFS();
      cy.setReduxState(<any>res);
      cy.openIframe('app/ui/#/servicePlanning?serviceModelId=f4d84bb4-a416-4b4e-997e-0059973630b9');
      cy.getElementByDataTestsId('node-ea81d6f7-0861-44a7-b7d5-d173b562c350-2017-488_PASQUALE-vPE 0').should('have.length', 2);

      cy.drawingBoardTreeOpenContextMenuByElementDataTestId('node-ea81d6f7-0861-44a7-b7d5-d173b562c350-2017-488_PASQUALE-vPE 0', 1)
        .drawingBoardTreeClickOnContextMenuOptionByName('Duplicate')
        .getTagElementContainsText('button', 'Duplicate').click({force: true})
        .get('#drawing-board-tree .toggle-children').should('have.length', 1);

      cy.drawingBoardTreeOpenContextMenuByElementDataTestId('node-ea81d6f7-0861-44a7-b7d5-d173b562c350-2017-488_PASQUALE-vPE 0', 2)
        .drawingBoardTreeClickOnContextMenuOptionByName('Duplicate')
        .getElementByDataTestsId('node-ea81d6f7-0861-44a7-b7d5-d173b562c350-2017-488_PASQUALE-vPE 0').should('have.length', 3);
    });

    it('check the instanceParams set to instance ', () => {
      let res = getReduxWith2VNFS();
      let instanceName = "InstanceName";
      cy.setReduxState(<any>res);
      cy.openIframe('app/ui/#/servicePlanning?serviceModelId=f4d84bb4-a416-4b4e-997e-0059973630b9');
      cy.drawingBoardTreeOpenContextMenuByElementDataTestId('node-ea81d6f7-0861-44a7-b7d5-d173b562c350-2017-488_PASQUALE-vPE 0', 1)
        .drawingBoardTreeClickOnContextMenuOptionByName('Edit')
        .getElementByDataTestsId('instanceName').type(instanceName)
        .genericFormSubmitForm();

      checkDynamicInputs();

      cy.drawingBoardTreeOpenContextMenuByElementDataTestId('node-ea81d6f7-0861-44a7-b7d5-d173b562c350-2017-488_PASQUALE-vPE 0', 1)
        .drawingBoardTreeClickOnContextMenuOptionByName('Edit');
      cy.getElementByDataTestsId('instanceName').should('have.value', instanceName);
      checkDynamicInputs();

    });

    it('delete duplicate vfModule ', () => {
      let res = getReduxWith2VNFS();
      cy.setReduxState(<any>res);
      cy.openIframe('app/ui/#/servicePlanning?serviceModelId=f4d84bb4-a416-4b4e-997e-0059973630b9');
      cy.getElementByDataTestsId('node-ea81d6f7-0861-44a7-b7d5-d173b562c350-2017-488_PASQUALE-vPE 0').should('have.length', 2)
        .drawingBoardTreeOpenContextMenuByElementDataTestId('node-040e591e-5d30-4e0d-850f-7266e5a8e013-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0')
        .drawingBoardTreeClickOnContextMenuOptionByName('Remove')
        .getElementByDataTestsId('node-ea81d6f7-0861-44a7-b7d5-d173b562c350-2017-488_PASQUALE-vPE 0').should('have.length', 2);
    });

    it('duplicate unique vnf ', () => {
      let res = getReduxWith2VNFS();
      cy.setReduxState(<any>res);
      cy.openIframe('app/ui/#/servicePlanning?serviceModelId=f4d84bb4-a416-4b4e-997e-0059973630b9');
      cy.getElementByDataTestsId('node-ea81d6f7-0861-44a7-b7d5-d173b562c350-2017-488_PASQUALE-vPE 0').should('have.length', 2)
        .drawingBoardTreeOpenContextMenuByElementDataTestId('node-ea81d6f7-0861-44a7-b7d5-d173b562c350-2017-488_PASQUALE-vPE 0', 1)
        .drawingBoardTreeClickOnContextMenuOptionByName('Duplicate')
        .getTagElementContainsText('button', 'Duplicate').click({force: true})
        .get('#drawing-board-tree .toggle-children').should('have.length', 1);
    });

    it('duplicate a-la-carte vnf + networks', () => {
      let res = getReduxWithVNFS(false);
      cy.setTestApiParamToGR();

      res.service.serviceHierarchy['6e59c5de-f052-46fa-aa7e-2fca9d674c44'].service.vidNotions.instantiationType = "ALaCarte";

      //remove VfModules since they are not fit to a-la-carte scenario
      delete res.service.serviceHierarchy['6e59c5de-f052-46fa-aa7e-2fca9d674c44'].vnfs['VF_vGeraldine 0'].vfModules;
      delete res.service.serviceInstance['6e59c5de-f052-46fa-aa7e-2fca9d674c44'].vnfs['VF_vGeraldine 0'].vfModules;

      res.service.serviceInstance['6e59c5de-f052-46fa-aa7e-2fca9d674c44'].vnfs['VF_vGeraldine 0'].instanceName = "VFvGeraldine00001";
      cy.setReduxState(<any>res);
      cy.openIframe('app/ui/#/servicePlanning?serviceModelId=6e59c5de-f052-46fa-aa7e-2fca9d674c44');
      const vnfNode = 'node-d6557200-ecf2-4641-8094-5393ae3aae60-VF_vGeraldine 0';
      cy.duplicateVnf(vnfNode, 1);

      //edit the second vnf lineOfBusiness to be ECOMP
      editSecondVnf(vnfNode);
      cy.drawingBoardPressAddButtonByElementName("node-ExtVL 0").click({force: true}).then(() => {
        cy.fillNetworkPopup();
      });

      // compare state with the json file
      cy.getReduxState().then((state) => {
        let serviceInstance = state.service.serviceInstance['6e59c5de-f052-46fa-aa7e-2fca9d674c44'];
        //assert that each vnf has it's own lineOfBusiness
        chai.expect(serviceInstance.vnfs['VF_vGeraldine 0'].lineOfBusiness).equal("zzz1");
        chai.expect(serviceInstance.vnfs['VF_vGeraldine 0:0001'].lineOfBusiness).equal("ONAP");
        cy.readFile('../vid-automation/src/test/resources/a-la-carte/redux-multiple-vnf-network.json').then((file) => {
          const vnfs = file.vnfs;
          var vnfNames = Object.keys(vnfs);

          for (var i: number = 0; i < vnfNames.length; i++) {
            chai.expect(serviceInstance.vnfs).to.have.any.keys(vnfNames[i]);
          }

          chai.expect(serviceInstance.vnfs['VF_vGeraldine 0'].lineOfBusiness).equal(vnfs['VF_vGeraldine 0'].lineOfBusiness);
          chai.expect(serviceInstance.vnfs['VF_vGeraldine 0'].instanceName).equal(vnfs['VF_vGeraldine 0'].instanceName);

          vnfs['VF_vGeraldine 0:0001'].trackById = serviceInstance.vnfs['VF_vGeraldine 0:0001'].trackById;

          cy.deepCompare(serviceInstance.vnfs['VF_vGeraldine 0:0001'], vnfs['VF_vGeraldine 0:0001']);
          cy.deepCompare(serviceInstance.vnfs['VF_vGeraldine 0'], vnfs['VF_vGeraldine 0']);

          const network = serviceInstance.networks['ExtVL 0'];

          file.networks['ExtVL 0'].trackById = network.trackById;
          cy.deepCompare(network, file.networks['ExtVL 0']);
        });
      });
    });

  });

  describe('default max instances value', () => {

    it('when there is no maxCountInstances for vfModule, it can be added unlimited times', () => {
      let reduxState = getReduxWithVNFS(false);
      (<any> reduxState.global.flags)['FLAG_2002_UNLIMITED_MAX'] =  true;
      cy.setReduxState(<any>reduxState);
      cy.openIframe('app/ui/#/servicePlanning?serviceModelId=6e59c5de-f052-46fa-aa7e-2fca9d674c44');
      const vfModuleName = 'vf_vgeraldine0..VfVgeraldine..vflorence_gpb..module-2';
      const vnfName = "VF_vGeraldine 0";
      cy.addMacroVfModule(vnfName, vfModuleName, 'module-1');
      cy.addMacroVfModule(vnfName, vfModuleName, 'module-2');
      cy.addMacroVfModule(vnfName, vfModuleName, 'module-3');
      cy.getElementByDataTestsId('node-d6557200-ecf2-4641-8094-5393ae3aae60-VF_vGeraldine 0').click();
      cy.getElementByDataTestsId('node-41708296-e443-4c71-953f-d9a010f059e1-vf_vgeraldine0..VfVgeraldine..vflorence_gpb..module-2').should('have.length', 3);

      //make sure max instances in model info show Unlimited (default)
      cy.drawingBoardTreeOpenContextMenuByElementDataTestId('node-41708296-e443-4c71-953f-d9a010f059e1-vf_vgeraldine0..VfVgeraldine..vflorence_gpb..module-2', 0)
        .drawingBoardTreeClickOnContextMenuOptionByName('Edit')
        .getElementByDataTestsId('model-item-value-max').contains('Unlimited (default)')
        .getElementByDataTestsId("cancelButton").click();
    });

    it('when there is no max_instances for VNF, it can be added multiple times ', () => {
      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/emptyServiceRedux.json').then((reduxState) => {
        reduxState.global['flags'] = { 'FLAG_2002_UNLIMITED_MAX' : true };
        delete reduxState.service.serviceHierarchy['2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd'].vnfs['2017-488_PASQUALE-vPE 0'].properties.max_instances;
        cy.setReduxState(<any>reduxState);
        addSameVnfMultipleTimes();
      });
    });
  });

  describe('multiple tests', () => {
    it('remove vfModule with missing data should update deploy button status', () => {
      let res = getReduxWithVFModuleMissingData();
      cy.setReduxState(<any>res);
      cy.openIframe('app/ui/#/servicePlanning?serviceModelId=f4d84bb4-a416-4b4e-997e-0059973630b9');
      cy.getElementByDataTestsId('node-040e591e-5d30-4e0d-850f-7266e5a8e013-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0-alert-icon').should('have.class', 'icon-alert')
        .drawingBoardTreeOpenContextMenuByElementDataTestId('node-040e591e-5d30-4e0d-850f-7266e5a8e013-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0')
        .drawingBoardTreeClickOnContextMenuOptionByName('Remove')
        .getElementByDataTestsId('deployBtn').should('not.have.attr', 'disabled');

      cy.updateServiceShouldNotOverrideChild();
    });

    it('remove VNF with missing data should update deploy button status ', () => {
      let res = getReduxWithVNFMissingData();
      cy.setReduxState(<any>res);
      cy.openIframe('app/ui/#/servicePlanning?serviceModelId=f4d84bb4-a416-4b4e-997e-0059973630b9');

      cy.getElementByDataTestsId('node-ea81d6f7-0861-44a7-b7d5-d173b562c350-2017-488_PASQUALE-vPE 0-alert-icon').should('have.class', 'icon-alert')
        .drawingBoardTreeOpenContextMenuByElementDataTestId('node-ea81d6f7-0861-44a7-b7d5-d173b562c350-2017-488_PASQUALE-vPE 0', 1)
        .drawingBoardTreeClickOnContextMenuOptionByName('Remove')
        .getTagElementContainsText('button', 'Remove VNF').click({force: true})
        .getElementByDataTestsId('deployBtn').should('not.have.attr', 'disabled');
      cy.updateServiceShouldNotOverrideChild();
    });


    it('should show vfModule missong data icon ', () => {
      const serviceModelId : string = 'f4d84bb4-a416-4b4e-997e-0059973630b9';
      let res = getReduxWithVNFMissingData();
      res.service.serviceInstance['f4d84bb4-a416-4b4e-997e-0059973630b9'].vnfs['2017-488_PASQUALE-vPE 0:0001'].vfModules['2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0']['2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0jkyqv'].isMissingData = true;
      cy.setReduxState(<any>res);
      cy.openIframe(`app/ui/#/servicePlanning?serviceModelId=${serviceModelId}`);
      cy.getElementByDataTestsId('node-040e591e-5d30-4e0d-850f-7266e5a8e013-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0-alert-icon').should('have.class', 'icon-alert');

      cy.getElementByDataTestsId('node-040e591e-5d30-4e0d-850f-7266e5a8e013-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0-menu-btn').eq(1).click({force:true})
        .getElementByDataTestsId('context-menu-edit').click()
        .getElementByDataTestsId('form-set').click()
        .getElementByDataTestsId('node-040e591e-5d30-4e0d-850f-7266e5a8e013-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0-alert-icon').should('not.have.class', 'icon-alert');
    });

    xit('should display service model name', () => {
      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/emptyServiceRedux.json').then((res) => {
        cy.setReduxState(<any>res);
        cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
        cy.get('#service-model-name').contains('action-data');
      });
    });

    it('should display icon and message if no vnf and vnfModules', () => {
      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/emptyServiceRedux.json').then((res) => {
        res.global.drawingBoardStatus = "CREATE";
        cy.setReduxState(<any>res);
        cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
        cy.getElementByDataTestsId("text-title").contains("Please add objects (VNFs, network, modules etc.)");
        cy.getElementByDataTestsId("text-title2").contains("from the left tree to design the service instance");
        cy.getElementByDataTestsId("text-subtitle").contains("Once done, click Deploy to start instantiation");
        cy.get('#not-node-img-id').and('be.visible');
        cy.getElementByDataTestsId("no-content-icon").should('have.class', "no-content-icon");
        cy.getElementByDataTestsId("no-content-icon").should('have.class', "upload-icon-service-planing");
        cy.getElementByDataTestsId("no-content-icon").should('have.attr', "src", "./assets/img/UPLOAD.svg");
      });
    });

    it('should show alert on remove vnf with modules', () => {
      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/serviceWithVnfAndVfModules.json').then((res) => {
        cy.setReduxState(<any>res);
        cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
        cy.drawingBoardTreeOpenContextMenuByElementDataTestId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_PASQUALE-vPE 0');
        // assert vfModules are enabled
        cy.get('.tree-node-disabled div[data-tests-id="node-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1"]')
          .should('not.be.visible');
        cy.drawingBoardTreeClickOnContextMenuOptionByName('Remove');

        cy.get('.title').contains('Remove VNF');
        cy.get('.custom-button').contains('Remove VNF').click();
        // assert vfModules are disabled after remove parent vnf
        cy.get('.tree-node-disabled div[data-tests-id="node-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1"]')
          .should('be.visible');
        cy.updateServiceShouldNotOverrideChild();
      });
    });

    it('should not show alert on remove vnf without modules', () => {
      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/serviceWithVnfAndVfModules.json').then((res) => {
        cy.setReduxState(<any>res);
        cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
        cy.drawingBoardTreeOpenContextMenuByElementDataTestId('node-0903e1c0-8e03-4936-b5c2-260653b96413-2017-388_PASQUALE-vPE 1');
        cy.drawingBoardTreeClickOnContextMenuOptionByName('Remove')
      });
    });

    it('should show <Automatically Assigned> if ecomp is true', () => {
      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/emptyServiceRedux.json').then((res) => {
        cy.setReduxState(<any>res);
        cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
        cy.drawingBoardPressAddButtonByElementName('node-2017-388_PASQUALE-vPE 0').click({force: true});

        cy.selectDropdownOptionByText('productFamily', 'ERICA');
        cy.selectDropdownOptionByText('lcpRegion', 'hvf6');
        cy.selectDropdownOptionByText('tenant', 'AIN Web Tool-15-D-testalexandria');
        cy.selectDropdownOptionByText('lineOfBusiness', 'ONAP');
        cy.selectPlatformValue(`platform`);
        cy.genericFormSubmitForm();

        cy.getElementByDataTestsId('node-afacccf6-397d-45d6-b5ae-94c39734b168-2017-388_PASQUALE-vPE 0').contains('<Automatically Assigned>');
        cy.updateServiceShouldNotOverrideChild();
      });
    });

    it('should show model name if ecomp is false', () => {
      const vnfModelKey: string = '2017-488_PASQUALE-vPE 0',
        vnfModelName: string = '2017-488_PASQUALE-vPE';
      cy.readFile('cypress/support/jsonBuilders/mocks/jsons/emptyServiceRedux.json').then((res) => {
        res.service.serviceHierarchy['2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd'].vnfs[vnfModelKey].properties.ecomp_generated_naming = 'false';
        cy.setReduxState(<any>res);
        cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
        cy.drawingBoardPressAddButtonByElementName('node-2017-488_PASQUALE-vPE 0').click({force: true});

        cy.selectDropdownOptionByText('productFamily', 'ERICA');
        cy.selectDropdownOptionByText('lcpRegion', 'hvf6');
        cy.selectDropdownOptionByText('tenant', 'AIN Web Tool-15-D-testalexandria');
        cy.selectLobValue("ONAP");
        cy.selectPlatformValue(`platform`);

        cy.genericFormSubmitForm();

        cy.getElementByDataTestsId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_PASQUALE-vPE 0').contains(vnfModelName);
      });
    });

    describe('add instance open a popup', () => {

      it('should add vfModule with popup with empty required instance name', () => {
        cy.readFile('cypress/support/jsonBuilders/mocks/jsons/serviceWithVnfAndVfModules.json').then((res) => {
          res.service.serviceHierarchy["2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd"].vnfs["2017-488_PASQUALE-vPE 0"].properties.ecomp_generated_naming = "false";
          res.service.serviceInstance["2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd"].vnfs["2017-488_PASQUALE-vPE 0"].vfModules = [];
          cy.setReduxState(<any>res);
          cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
          cy.get('available-models-tree tree-node-expander').eq(2).click();
          cy.drawingBoardPressAddButtonByElementName('node-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1').click({force: true});
        });
      });

      it('should add vfModule with popup if empty required dynamic input', () => {
        cy.readFile('cypress/support/jsonBuilders/mocks/jsons/serviceWithVnfAndVfModules.json').then((res) => {
          res.service.serviceHierarchy["2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd"].vnfs["2017-488_PASQUALE-vPE 0"].vfModules["2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1"].inputs["pasqualevpe0_bandwidth"].default = '';
          res.service.serviceInstance["2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd"].vnfs["2017-488_PASQUALE-vPE 0"].vfModules = [];
          cy.setReduxState(<any>res);
          cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
          cy.get('available-models-tree tree-node-expander').eq(2).click();
          cy.drawingBoardPressAddButtonByElementName('node-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1').click({force: true});
          cy.get('#instance-popup').and('be.visible');
        });
      });

    });


    describe('show warning and disable deploy button on vnf missing data', () => {
      it('show warning on vnf, and disable button, remove warning and enable button after edit', () => {
        cy.readFile('cypress/support/jsonBuilders/mocks/jsons/serviceWithVnfAndVfModules.json').then((res) => {
          res.service.serviceInstance['2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd'].existingVNFCounterMap['0903e1c0-8e03-4936-b5c2-260653b96413'] = 1;
          res.service.serviceInstance["2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd"].validationCounter = 1;
          res.service.serviceInstance["2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd"].vnfs["2017-388_PASQUALE-vPE 1"] = {
            "rollbackOnFailure": "false",
            "vfModules": {},
            "productFamilyId": "36b4733a-53f4-4cc8-8ff0-9172e5fc4b8e",
            "lcpCloudRegionId": "",
            "tenantId": "",
            "lineOfBusiness": "zzz1",
            "platformName": "platform",
            "isMissingData": true,
            "modelInfo": {
              "modelType": "service",
              "modelInvariantId": "00beb8f9-6d39-452f-816d-c709b9cbb87d",
              "modelVersionId": "0903e1c0-8e03-4936-b5c2-260653b96413",
              "modelName": "2017-388_PASQUALE-vPE",
              "modelVersion": "1.0",
              "modelCustomizationId": "280dec31-f16d-488b-9668-4aae55d6648a",
              "modelCustomizationName": "2017-388_PASQUALE-vPE 1"
            }
          };
          cy.setReduxState(<any>res);
          cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
          cy.getElementByDataTestsId("node-0903e1c0-8e03-4936-b5c2-260653b96413-2017-388_PASQUALE-vPE 1-alert-icon").and('be.visible');
          cy.isElementContainsAttr('deployBtn', 'disabled');
          cy.drawingBoardTreeOpenContextMenuByElementDataTestId('node-0903e1c0-8e03-4936-b5c2-260653b96413-2017-388_PASQUALE-vPE 1')
            .drawingBoardTreeClickOnContextMenuOptionByName('Edit')
          cy.selectDropdownOptionByText('lcpRegion', 'hvf6');
          cy.selectDropdownOptionByText('tenant', 'AIN Web Tool-15-D-testalexandria');
          cy.genericFormSubmitForm();
          cy.getElementByDataTestsId("node-0903e1c0-8e03-4936-b5c2-260653b96413-2017-388_PASQUALE-vPE 1-alert-icon").should('not.be.visible');
          cy.getElementByDataTestsId('deployBtn').should('not.have.attr', 'disabled');
          cy.updateServiceShouldNotOverrideChild();
        });
      });
    });

    describe('vnf should automatically displayed or not according its min value and its vf-modules min value', () => {

      it('vnf with min_instances value > 0 without required VF modules, should be created automatically without children', () => {
        cy.readFile('cypress/support/jsonBuilders/mocks/jsons/basicRedux.json').then((res) => {
          res.service.serviceHierarchy["2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd"].vnfs["2017-488_PASQUALE-vPE 0"].properties['min_instances'] = 1;
          res.service.serviceHierarchy["2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd"].vnfs["2017-488_PASQUALE-vPE 0"].vfModules["2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0"].properties['initialCount'] = 0;
          res.service.serviceHierarchy["2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd"].vfModules["2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0"].properties['initialCount'] = 0;
          cy.setReduxState(<any>res);
          cy.fillServicePopup().then(() => {
            cy.visit("welcome.htm").then(() => {
              cy.visit('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd').then(() => {
                cy.getElementByDataTestsId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_PASQUALE-vPE 0').should('exist');
                cy.getElementByDataTestsId('node-25284168-24bb-4698-8cb4-3f509146eca5-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1').should('not.exist');
                cy.getElementByDataTestsId('node-f8360508-3f17-4414-a2ed-6bc71161e8db-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0').should('not.exist');
              })
            })
          });
        });

      });

      it('vnf with min_instances value > 1 with required VF modules, should be created automatically with children only once', () => {
        cy.readFile('cypress/support/jsonBuilders/mocks/jsons/basicRedux.json').then((res) => {
          res.service.serviceHierarchy["2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd"].vnfs["2017-488_PASQUALE-vPE 0"].properties['min_instances'] = 3;
          cy.setReduxState(<any>res);
          cy.fillServicePopup().then(() => {
            cy.visit("welcome.htm").then(() => {
              cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
              cy.getElementByDataTestsId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_PASQUALE-vPE 0').should('exist');
              cy.getElementByDataTestsId('node-f8360508-3f17-4414-a2ed-6bc71161e8db-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0').should('have.length', 1);

            });
          });
        });
      });


      it('vnf with min_instances value = 0 with required VF modules should be created automatically with its children', () => {
        cy.readFile('cypress/support/jsonBuilders/mocks/jsons/basicRedux.json').then((res) => {
          res.service.serviceHierarchy["2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd"].vnfs["2017-488_PASQUALE-vPE 0"].properties['min_instances'] = 0;
          cy.setReduxState(<any>res);
          cy.fillServicePopup().then(() => {
            cy.visit("welcome.htm").then(() => {
              cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
              cy.getElementByDataTestsId('node-69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_PASQUALE-vPE 0').should('exist');
              cy.getElementByDataTestsId('node-f8360508-3f17-4414-a2ed-6bc71161e8db-2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0').should('exist');
              cy.updateServiceShouldNotOverrideChild();
            });
          });
        });

      });


      it('vnf without min_instances and without required VF modules, should not exist automatically in right side', () => {
        cy.readFile('cypress/support/jsonBuilders/mocks/jsons/basicRedux.json').then((res) => {
          cy.setReduxState(<any>res);
          cy.fillServicePopup().then(() => {
            cy.visit("welcome.htm").then(() => {
              cy.openIframe('app/ui/#/servicePlanning?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd');
              cy.getElementByDataTestsId('node-afacccf6-397d-45d6-b5ae-94c39734b168-2017-388_PASQUALE-vPE 0').should('not.exist');
            });
          });
        });
      });
    });
  });

  describe('supplementary file', () => {
    it('delete file', () => {
      let res = getReduxWithVNFS(true);
      let instanceName = 'instanceName';
      cy.setReduxState(<any>res);
      cy.openIframe('app/ui/#/servicePlanning?serviceModelId=6e59c5de-f052-46fa-aa7e-2fca9d674c44');
      const vfModuleNode = 'node-522159d5-d6e0-4c2a-aa44-5a542a12a830-vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1';
      assertEditvfModuleShowFile(vfModuleNode, "sample.json");
      cy.getElementByDataTestsId('remove-uploaded-file').click({force: true});
      cy.getElementByDataTestsId('form-set').should('not.have.attr', 'disabled');
      cy.typeToInput('instanceName', instanceName);
      cy.genericFormSubmitForm();
      cy.getReduxState().then((state) => {
        const serviceInstance = state.service.serviceInstance['6e59c5de-f052-46fa-aa7e-2fca9d674c44'];
        const vfModuleInstance = serviceInstance.vnfs['VF_vGeraldine 0'].vfModules['vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1']['vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1dcudx'];
        chai.expect(vfModuleInstance.supplementaryFile_hidden).to.be.null;
        chai.expect(vfModuleInstance.supplementaryFile_hidden_content).to.be.null;
        chai.expect(vfModuleInstance.supplementaryFileContent).to.be.undefined;
        chai.expect(vfModuleInstance.supplementaryFileName).to.be.undefined;
      });
      cy.getElementByDataTestsId('node-d6557200-ecf2-4641-8094-5393ae3aae60-VF_vGeraldine 0').click();
      assertEditvfModuleShowFile(vfModuleNode, "Choose file");
      cy.getElementByDataTestsId('instanceName').should('have.value', instanceName);
    });
  });

  describe('component info', () => {

    var jsonBuilderAndMock: JsonBuilder<ServiceModel> = new JsonBuilder<ServiceModel>();

    const longText = 'Im a very long text for verify wrapping so please dont make me shorter';

    function testIfComponentInfoShown(flagState:boolean) {
      let res = getReduxWith2VNFS();
      res.global.flags.FLAG_1906_COMPONENT_INFO = flagState;
      res.service.serviceHierarchy["f4d84bb4-a416-4b4e-997e-0059973630b9"].service.serviceRole = longText;
      cy.setReduxState(<any>res);
      cy.openIframe('app/ui/#/servicePlanning?serviceModelId=f4d84bb4-a416-4b4e-997e-0059973630b9');
      let conditionStr = flagState ? "" : "not.";
      cy.get('component-info').should(conditionStr+"be.visible");
      const otherComponentWidth = flagState ? 5 : 6;
      const otherComponentCss = 'col-md-'+otherComponentWidth ;
      cy.get('available-models-tree').should("have.class", otherComponentCss);
      cy.get('drawing-board-tree').should("have.class", otherComponentCss);
      if (flagState) {
        cy.get('component-info').should("have.class", 'col-md-2');
      }


    }

    it('component info is shown and relevant fields are shown', () => {
      testIfComponentInfoShown(true);

      let labelsAndValues = [
        ['Model version', '1.0'],
        ['Subscriber name', 'SILVIA ROBBINS'],
        ['Service type', 'TYLER SILVIA'],
        ['Service role', longText]
      ];
      const expectedTitle = 'Service Instance INFO';
      cy.assertComponentInfoTitleLabelsAndValues(expectedTitle, labelsAndValues);
      cy.getElementByDataTestsId('model-item-value-Service role').should('have.css', 'height', '32px'); //assert that long text is wrap
    });

  it('component info is not shown when feature flag is false', () => {
      testIfComponentInfoShown(false);
    });
  });

  function addNetworkFromModel(instanceName: string) {
    return cy.get('drawing-board-tree').find('tree-node-content').then((elemets) => {
      cy.get('drawing-board-tree tree-node-content').should('have.have.length', elemets.length);
      cy.drawingBoardPressAddButtonByElementName(instanceName).click({force: true}).then(() => {
        cy.fillNetworkPopup();
        cy.get('drawing-board-tree tree-node-content').should('have.have.length', (elemets.length + 1));
        cy.updateServiceShouldNotOverrideChild();
      });
    });
  }

  function getReduxWith2VNFS() {
    return {
      "global": {
        "name": null,
        "flags": {
          "FLAG_SHOW_ASSIGNMENTS": true,
          "FLAG_SHOW_VERIFY_SERVICE": false,
          "FLAG_ADD_MSO_TESTAPI_FIELD": true,
          "FLAG_NETWORK_TO_ASYNC_INSTANTIATION": false,
          "FLAG_FABRIC_CONFIGURATION_ASSIGNMENTS": true,
          "FLAG_SERVICE_MODEL_CACHE": true,
          "FLAG_1906_COMPONENT_INFO" : false,
          "FLAG_2002_VNF_PLATFORM_MULTI_SELECT" : false,
          "FLAG_2002_UNLIMITED_MAX" : true
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
              "serviceEcompNaming": "true",
              "instantiationType": "Macro",
              "vidNotions": {
                "instantiationType": "Macro"
              },
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
                  "gpb2_Internal2_mac": "00:11:22:EF:AC:DF",
                  "sctp-a-ipv6-egress_rule_application": "any",
                  "sctp-b-ipv6-egress_src_start_port": "0",
                  "Internal2_allow_transit": "true",
                  "sctp-b-IPv6_ethertype": "IPv6",
                  "ncb2_Internal1_mac": "00:11:22:EF:AC:DF",
                  "sctp-b-ingress_rule_protocol": "icmp",
                  "sctp-b-ingress_action": "pass",
                  "sctp-a-egress_rule_application": "any",
                  "sctp-b-ipv6-ingress-src_start_port": "0.0",
                  "ncb1_Internal2_mac": "00:11:22:EF:AC:DF",
                  "sctp-b-egress_src_addresses": "local",
                  "fsb_volume_size_0": "320.0",
                  "sctp-a-ipv6-ingress-dst_start_port": "0",
                  "sctp-a-ipv6-ingress_ethertype": "IPv4",
                  "sctp-b-ipv6-ingress_rule_application": "any",
                  "domain_name": "default-domain",
                  "sctp-a-egress_src_addresses": "local",
                  "sctp-b-egress-src_start_port": "0.0",
                  "sctp-a-ingress_rule_protocol": "icmp",
                  "sctp-b-display_name": "epc-sctp-b-ipv4v6-sec-group",
                  "sctp-b-ipv6-ingress-dst_end_port": "65535",
                  "sctp-a-ingress_ethertype": "IPv4",
                  "sctp-a-egress-src_start_port": "0.0",
                  "sctp-b-dst_subnet_prefix_v6": "::",
                  "nf_naming": "{ecomp_generated_naming=false}",
                  "sctp-a-ipv6-ingress_src_subnet_prefix": "0.0.0.0",
                  "sctp-b-egress-dst_start_port": "0.0",
                  "ncb_flavor_name": "nv.c20r64d1",
                  "sctp-b-egress_dst_subnet_prefix_len": "0.0",
                  "gpb1_Internal1_mac": "00:11:22:EF:AC:DF",
                  "Internal2_net_cidr": "10.0.0.10",
                  "sctp-a-ingress-dst_start_port": "0.0",
                  "fsb1_Internal2_mac": "00:11:22:EF:AC:DF",
                  "sctp-a-egress-dst_start_port": "0.0",
                  "sctp-a-egress_ethertype": "IPv4",
                  "vlc_st_service_mode": "in-network-nat",
                  "sctp-a-ipv6-egress_ethertype": "IPv4",
                  "sctp-a-egress-src_end_port": "65535.0",
                  "sctp-b-egress_action": "pass",
                  "sctp-b-ipv6-egress_rule_application": "any",
                  "sctp-a-ingress-src_subnet_prefix_len": "0.0",
                  "sctp-b-ipv6-ingress-src_end_port": "65535.0",
                  "sctp-a-ipv6-ingress-src_start_port": "0.0",
                  "fsb2_Internal1_mac": "00:11:22:EF:AC:DF",
                  "sctp-b-name": "epc-sctp-b-ipv4v6-sec-group",
                  "sctp-b-ipv6-egress_ethertype": "IPv4",
                  "Internal1_net_cidr": "10.0.0.10",
                  "sctp-a-egress_dst_subnet_prefix": "0.0.0.0",
                  "fsb_flavor_name": "nv.c20r64d1",
                  "sctp_rule_protocol": "132",
                  "sctp-a-ipv6-ingress_rule_application": "any",
                  "sctp-b-ipv6-ingress_src_subnet_prefix_len": "0",
                  "ecomp_generated_naming": "false",
                  "sctp-a-IPv6_ethertype": "IPv6",
                  "vlc_st_virtualization_type": "virtual-machine",
                  "vlc2_Internal1_mac": "00:11:22:EF:AC:DF",
                  "sctp-b-ingress-dst_end_port": "65535.0",
                  "sctp-b-ingress-dst_start_port": "0.0",
                  "sctp-a-ipv6-ingress-src_end_port": "65535.0",
                  "sctp-a-display_name": "epc-sctp-a-ipv4v6-sec-group",
                  "sctp-b-ingress_rule_application": "any",
                  "vlc_flavor_name": "nd.c16r64d1",
                  "int2_sec_group_name": "int2-sec-group",
                  "sctp-b-ipv6-egress_src_addresses": "local",
                  "vlc_st_interface_type_int1": "other1",
                  "vlc_st_interface_type_int2": "other2",
                  "sctp-a-ipv6-egress-dst_start_port": "0",
                  "sctp-b-egress-src_end_port": "65535.0",
                  "sctp-a-ipv6-egress_dst_subnet_prefix_len": "0",
                  "Internal2_shared": "false",
                  "sctp-a-ipv6-egress_rule_protocol": "any",
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
                  "fsb2_Internal2_mac": "00:11:22:EF:AC:DF",
                  "Internal2_forwarding_mode": "l2",
                  "sctp-a-ipv6-egress_dst_end_port": "65535",
                  "sctp-b-egress_dst_subnet_prefix": "0.0.0.0",
                  "Internal1_net_cidr_len": "17",
                  "gpb2_Internal1_mac": "00:11:22:EF:AC:DF",
                  "sctp-a-ingress_dst_addresses": "local",
                  "sctp-b-ingress-src_subnet_prefix_len": "0.0",
                  "sctp-a-egress_action": "pass",
                  "fsb_volume_type_0": "SF-Default-SSD",
                  "ncb2_Internal2_mac": "00:11:22:EF:AC:DF",
                  "vlc_st_interface_type_sctp_a": "left",
                  "vlc_st_version": "2",
                  "sctp-a-src_subnet_prefix_v6": "::",
                  "vlc_st_interface_type_sctp_b": "right",
                  "sctp-a-ingress_rule_application": "any",
                  "sctp-b-egress_ethertype": "IPv4",
                  "sctp-a-ipv6-egress_src_start_port": "0",
                  "instance_ip_family_v6": "v6",
                  "gpb1_Internal2_mac": "00:11:22:EF:AC:DF",
                  "sctp-b-ingress-src_start_port": "0.0",
                  "fsb1_Internal1_mac": "00:11:22:EF:AC:DF",
                  "sctp-b-ingress_dst_addresses": "local",
                  "vlc_st_interface_type_oam": "management",
                  "multi_stage_design": "false",
                  "oam_sec_group_name": "oam-sec-group",
                  "Internal2_net_gateway": "10.0.0.10",
                  "sctp-a-ipv6-ingress-dst_end_port": "65535",
                  "Internal1_net_gateway": "10.0.0.10",
                  "sctp-b-ipv6-egress-dst_start_port": "0",
                  "sctp-b-ipv6-egress_rule_protocol": "any",
                  "gtp_sec_group_name": "gtp-sec-group",
                  "sctp-a-ipv6-egress_dst_subnet_prefix": "0.0.0.0",
                  "sctp-a-ipv6-ingress_dst_addresses": "local",
                  "sctp-b-ipv6-egress_dst_subnet_prefix_len": "0",
                  "sctp-b-ipv6-egress_action": "pass",
                  "sctp-a-egress_rule_protocol": "icmp",
                  "sctp-a-ipv6-egress_action": "pass",
                  "Internal1_shared": "false",
                  "sctp-b-ipv6-ingress_rule_protocol": "any",
                  "Internal2_net_cidr_len": "17",
                  "sctp-a-name": "epc-sctp-a-ipv4v6-sec-group",
                  "sctp-a-ingress-src_end_port": "65535.0",
                  "sctp-b-ipv6-ingress_src_subnet_prefix": "0.0.0.0",
                  "sctp-a-egress-dst_end_port": "65535.0",
                  "sctp-b-egress_rule_protocol": "icmp",
                  "sctp-a-ingress_action": "pass",
                  "sctp-b-ipv6-ingress_action": "pass",
                  "vlc_st_service_type": "firewall",
                  "sctp-b-ipv6-egress_dst_end_port": "65535",
                  "vlc2_Internal2_mac": "00:11:22:EF:AC:DF",
                  "sctp-b-ipv6-ingress-dst_start_port": "0",
                  "vlc_st_availability_zone": "true",
                  "sctp-b-ingress-src_subnet_prefix": "0.0.0.0",
                  "fsb_volume_image_name_1": "MME_FSB2_16ACP04_GA.qcow2",
                  "sctp-a-ipv6-ingress_src_subnet_prefix_len": "0",
                  "gpb_flavor_name": "nv.c20r64d1",
                  "Internal1_allow_transit": "true",
                  "availability_zone_max_count": "1",
                  "fsb_volume_image_name_0": "MME_FSB1_16ACP04_GA.qcow2",
                  "sctp-b-ipv6-ingress_dst_addresses": "local",
                  "sctp-b-ipv6-ingress_ethertype": "IPv4",
                  "sctp-b-ipv6-egress_dst_subnet_prefix": "0.0.0.0",
                  "sctp-a-ingress-src_subnet_prefix": "0.0.0.0",
                  "vlc1_Internal2_mac": "00:11:22:EF:AC:DF",
                  "sctp-a-ipv6-ingress_action": "pass",
                  "Internal1_rpf": "disable",
                  "sctp-b-ingress_ethertype": "IPv4",
                  "sctp-b-ingress-src_end_port": "65535.0",
                  "sctp-b-egress_rule_application": "any",
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
            "pnfs": {}
          },
          "f4d84bb4-a416-4b4e-997e-0059973630b9": {
            "service": {
              "uuid": "f4d84bb4-a416-4b4e-997e-0059973630b9",
              "invariantUuid": "598e3f9e-3244-4d8f-a8e0-0e5d7a29eda9",
              "name": "PASQUALE vMX vPE_BV Service 488",
              "version": "1.0",
              "toscaModelURL": null,
              "category": "Network L1-3",
              "serviceType": "",
              "serviceRole": "",
              "description": "PASQUALE vMX vPE based on Juniper 17.2 release. Updated with updated VF for v8.0 of VLM",
              "serviceEcompNaming": "true",
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
              "2017-488_PASQUALE-vPE 0": {
                "uuid": "ea81d6f7-0861-44a7-b7d5-d173b562c350",
                "invariantUuid": "5be7e99e-8eb2-4d97-be63-8081ff3cd10e",
                "description": "Name PASQUALE vPE Description The provider edge function for the PASQUALE service supported by the Junipers VMX product Category Router Vendor Juniper Vendor Release Code 17.2 Owners Mary Fragale. Updated 9-25 to use v8.0 of the Juniper Valid 2 VLM",
                "name": "2017-488_PASQUALE-vPE",
                "version": "9.0",
                "customizationUuid": "41516cc6-5098-4b40-a619-f8d5f55fc4d8",
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
                  "availability_zone_0": {
                    "type": "string",
                    "description": "The Availability Zone to launch the instance.",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "mtpocfo-kvm-az01"
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
                    "inputName": "2017488_pasqualevpe0_bandwidth_units"
                  },
                  "bandwidth": {
                    "displayName": "bandwidth",
                    "command": "get_input",
                    "inputName": "2017488_pasqualevpe0_bandwidth"
                  },
                  "AIC_CLLI": {
                    "displayName": "AIC_CLLI",
                    "command": "get_input",
                    "inputName": "2017488_pasqualevpe0_AIC_CLLI"
                  },
                  "availability_zone_0": {
                    "displayName": "availability_zone_0",
                    "command": "get_input",
                    "inputName": "2017488_pasqualevpe0_availability_zone_0"
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
                  "max_instances": 10,
                  "min_instances": 1,
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
                  "nf_type": "ROUTER",
                  "vmxvpfe_int_ctl_ip_1": "10.0.0.10",
                  "is_AVPN_service": "false",
                  "vmx_RSG_name": "vREXI-affinity",
                  "vmx_int_ctl_forwarding": "l2",
                  "vmxvre_oam_ip_0": "10.0.0.10",
                  "vmxvpfe_sriov44_0_port_mac": "00:11:22:EF:AC:DF",
                  "vmxvpfe_sriov41_0_port_vlanstrip": "false",
                  "vmxvpfe_sriov42_0_port_vlanfilter": "4001",
                  "vmxvpfe_sriov44_0_port_unknownunicastallow": "true",
                  "vmxvre_image_name_0": "vre172_nova_img",
                  "vmxvre_instance": "0",
                  "vmxvpfe_sriov43_0_port_mac": "00:11:22:EF:AC:DF",
                  "vmxvre_flavor_name": "ns.c1r16d32.v5",
                  "vmxvpfe_volume_size_0": "40.0",
                  "vmxvpfe_sriov43_0_port_vlanfilter": "4001",
                  "nf_naming": "{ecomp_generated_naming=true}",
                  "multi_stage_design": "false",
                  "nf_naming_code": "me6",
                  "vmxvre_name_0": "vREXI",
                  "vmxvpfe_sriov42_0_port_vlanstrip": "false",
                  "vmxvpfe_volume_name_0": "vPFEXI_FBVolume",
                  "vmx_RSG_id": "bd89a33c-13c3-4a04-8fde-1a57eb123141",
                  "vmxvpfe_image_name_0": "vpfe172_nova_img",
                  "vmxvpfe_sriov43_0_port_unknownunicastallow": "true",
                  "vmxvpfe_sriov44_0_port_unknownmulticastallow": "true",
                  "vmxvre_console": "vidconsole",
                  "vmxvpfe_sriov44_0_port_vlanfilter": "4001",
                  "vmxvpfe_sriov42_0_port_mac": "00:11:22:EF:AC:DF",
                  "vmxvpfe_volume_id_0": "47cede15-da2f-4397-a101-aa683220aff3",
                  "vmxvpfe_sriov42_0_port_unknownmulticastallow": "true",
                  "vmxvpfe_sriov44_0_port_vlanstrip": "false",
                  "vf_module_id": "123",
                  "nf_function": "PASQUALE vPE",
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
                  "bandwidth_units": "get_input:2017488_pasqualevpe0_bandwidth_units",
                  "vnf_id": "123",
                  "vmxvre_oam_prefix": "24",
                  "availability_zone_0": "get_input:2017488_pasqualevpe0_availability_zone_0",
                  "ASN": "get_input:2017488_pasqualevpe0_ASN",
                  "vmxvre_chassis_i2cid": "161",
                  "vmxvpfe_name_0": "vPFEXI",
                  "bandwidth": "get_input:2017488_pasqualevpe0_bandwidth",
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
                  "nf_role": "vPE",
                  "vmxvre_volume_id_0": "f4eacb79-f687-4e9d-b760-21847c8bb15a",
                  "vmxvpfe_sriov42_0_port_unknownunicastallow": "true",
                  "vmxvpfe_flavor_name": "ns.c20r16d25.v5"
                },
                "type": "VF",
                "modelCustomizationName": "2017-488_PASQUALE-vPE 0",
                "vfModules": {
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
                    "uuid": "a5d8df05-11cb-4351-96e0-b6d4168ea4df",
                    "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                    "customizationUuid": "f3d97417-0c8d-424e-8ff7-b2eb4fbcecc3",
                    "description": null,
                    "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                    "version": "8",
                    "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                    "properties": {
                      "minCountInstances": 0,
                      "maxCountInstances": null,
                      "initialCount": 0,
                      "vfModuleLabel": "PASQUALE_vRE_BV"
                    },
                    "inputs": {
                      "2017488_pasqualevpe0_bandwidth_units": {
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
                      },
                      "2017488_pasqualevpe0_bandwidth": {
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
                      }
                    },
                    "volumeGroupAllowed": true
                  },
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
                    "uuid": "040e591e-5d30-4e0d-850f-7266e5a8e013",
                    "invariantUuid": "b34833bb-6aa9-4ad6-a831-70b06367a091",
                    "customizationUuid": "5c5f91f9-5e31-4120-b892-5536587ec258",
                    "description": null,
                    "name": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                    "version": "6",
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
                    "uuid": "b3e8b26e-cff0-49fc-a4e6-f3e16c8440fe",
                    "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
                    "customizationUuid": "6e410843-257c-46d9-ba8a-8d94e1362452",
                    "description": null,
                    "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                    "version": "8",
                    "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                    "properties": {
                      "minCountInstances": 0,
                      "maxCountInstances": null,
                      "initialCount": 0,
                      "vfModuleLabel": "PASQUALE_vPFE_BV"
                    },
                    "inputs": {
                      "2017488_pasqualevpe0_availability_zone_0": {
                        "type": "string",
                        "description": "The Availability Zone to launch the instance.",
                        "entry_schema": null,
                        "inputProperties": {
                          "sourceType": "HEAT",
                          "vfModuleLabel": "PASQUALE_vPFE_BV",
                          "paramName": "availability_zone_0"
                        },
                        "constraints": null,
                        "required": true,
                        "default": "mtpocfo-kvm-az01"
                      }
                    },
                    "volumeGroupAllowed": true
                  }
                },
                "volumeGroups": {
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
                    "uuid": "a5d8df05-11cb-4351-96e0-b6d4168ea4df",
                    "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                    "customizationUuid": "f3d97417-0c8d-424e-8ff7-b2eb4fbcecc3",
                    "description": null,
                    "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                    "version": "8",
                    "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                    "properties": {
                      "minCountInstances": 0,
                      "maxCountInstances": null,
                      "initialCount": 0,
                      "vfModuleLabel": "PASQUALE_vRE_BV"
                    },
                    "inputs": {
                      "2017488_pasqualevpe0_bandwidth_units": {
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
                      },
                      "2017488_pasqualevpe0_bandwidth": {
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
                      }
                    }
                  },
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2": {
                    "uuid": "b3e8b26e-cff0-49fc-a4e6-f3e16c8440fe",
                    "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
                    "customizationUuid": "6e410843-257c-46d9-ba8a-8d94e1362452",
                    "description": null,
                    "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                    "version": "8",
                    "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                    "properties": {
                      "minCountInstances": 0,
                      "maxCountInstances": null,
                      "initialCount": 0,
                      "vfModuleLabel": "PASQUALE_vPFE_BV"
                    },
                    "inputs": {
                      "2017488_pasqualevpe0_availability_zone_0": {
                        "type": "string",
                        "description": "The Availability Zone to launch the instance.",
                        "entry_schema": null,
                        "inputProperties": {
                          "sourceType": "HEAT",
                          "vfModuleLabel": "PASQUALE_vPFE_BV",
                          "paramName": "availability_zone_0"
                        },
                        "constraints": null,
                        "required": true,
                        "default": "mtpocfo-kvm-az01"
                      }
                    }
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
                "uuid": "a5d8df05-11cb-4351-96e0-b6d4168ea4df",
                "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                "customizationUuid": "f3d97417-0c8d-424e-8ff7-b2eb4fbcecc3",
                "description": null,
                "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                "version": "8",
                "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                "properties": {
                  "minCountInstances": 0,
                  "maxCountInstances": null,
                  "initialCount": 0,
                  "vfModuleLabel": "PASQUALE_vRE_BV"
                },
                "inputs": {
                  "2017488_pasqualevpe0_bandwidth_units": {
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
                  },
                  "2017488_pasqualevpe0_bandwidth": {
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
                  }
                },
                "volumeGroupAllowed": true
              },
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
                "uuid": "040e591e-5d30-4e0d-850f-7266e5a8e013",
                "invariantUuid": "b34833bb-6aa9-4ad6-a831-70b06367a091",
                "customizationUuid": "5c5f91f9-5e31-4120-b892-5536587ec258",
                "description": null,
                "name": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                "version": "6",
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
                "uuid": "b3e8b26e-cff0-49fc-a4e6-f3e16c8440fe",
                "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
                "customizationUuid": "6e410843-257c-46d9-ba8a-8d94e1362452",
                "description": null,
                "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                "version": "8",
                "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                "properties": {
                  "minCountInstances": 0,
                  "maxCountInstances": null,
                  "initialCount": 0,
                  "vfModuleLabel": "PASQUALE_vPFE_BV"
                },
                "inputs": {
                  "2017488_pasqualevpe0_availability_zone_0": {
                    "type": "string",
                    "description": "The Availability Zone to launch the instance.",
                    "entry_schema": null,
                    "inputProperties": {
                      "sourceType": "HEAT",
                      "vfModuleLabel": "PASQUALE_vPFE_BV",
                      "paramName": "availability_zone_0"
                    },
                    "constraints": null,
                    "required": true,
                    "default": "mtpocfo-kvm-az01"
                  }
                },
                "volumeGroupAllowed": true
              }
            },
            "volumeGroups": {
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
                "uuid": "a5d8df05-11cb-4351-96e0-b6d4168ea4df",
                "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                "customizationUuid": "f3d97417-0c8d-424e-8ff7-b2eb4fbcecc3",
                "description": null,
                "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                "version": "8",
                "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                "properties": {
                  "minCountInstances": 0,
                  "maxCountInstances": null,
                  "initialCount": 0,
                  "vfModuleLabel": "PASQUALE_vRE_BV"
                },
                "inputs": {
                  "2017488_pasqualevpe0_bandwidth_units": {
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
                  },
                  "2017488_pasqualevpe0_bandwidth": {
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
                  }
                }
              },
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2": {
                "uuid": "b3e8b26e-cff0-49fc-a4e6-f3e16c8440fe",
                "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
                "customizationUuid": "6e410843-257c-46d9-ba8a-8d94e1362452",
                "description": null,
                "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                "version": "8",
                "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                "properties": {
                  "minCountInstances": 0,
                  "maxCountInstances": null,
                  "initialCount": 0,
                  "vfModuleLabel": "PASQUALE_vPFE_BV"
                },
                "inputs": {
                  "2017488_pasqualevpe0_availability_zone_0": {
                    "type": "string",
                    "description": "The Availability Zone to launch the instance.",
                    "entry_schema": null,
                    "inputProperties": {
                      "sourceType": "HEAT",
                      "vfModuleLabel": "PASQUALE_vPFE_BV",
                      "paramName": "availability_zone_0"
                    },
                    "constraints": null,
                    "required": true,
                    "default": "mtpocfo-kvm-az01"
                  }
                }
              }
            },
            "pnfs": {}
          }
        },
        "serviceInstance": {
          "f4d84bb4-a416-4b4e-997e-0059973630b9": {
            "vnfs": {
              "2017-488_PASQUALE-vPE 0": {
                "rollbackOnFailure": "true",
                "vfModules": {
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
                    "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0eknhp": {
                      "modelInfo": {
                        "modelInvariantId": "b34833bb-6aa9-4ad6-a831-70b06367a091",
                        "modelVersionId": "040e591e-5d30-4e0d-850f-7266e5a8e013",
                        "modelName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                        "modelVersion": "6",
                        "modelCustomizationId": "5c5f91f9-5e31-4120-b892-5536587ec258",
                        "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0"
                      },
                      "isMissingData": false,
                      "instanceParams": [
                        {}
                      ]
                    }
                  }
                },
                "isMissingData": false,
                "originalName": null,
                "productFamilyId": "ebc3bc3d-62fd-4a3f-a037-f619df4ff034",
                "lcpCloudRegionId": "hvf6",
                "tenantId": "bae71557c5bb4d5aac6743a4e5f1d054",
                "lineOfBusiness": "ONAP",
                "platformName": "platform",
                "modelInfo": {
                  "modelInvariantId": "5be7e99e-8eb2-4d97-be63-8081ff3cd10e",
                  "modelVersionId": "ea81d6f7-0861-44a7-b7d5-d173b562c350",
                  "modelName": "2017-488_PASQUALE-vPE",
                  "modelVersion": "9.0",
                  "modelCustomizationId": "41516cc6-5098-4b40-a619-f8d5f55fc4d8",
                  "modelCustomizationName": "2017-488_PASQUALE-vPE 0"
                }
              },
              "2017-488_PASQUALE-vPE 0:0001": {
                "rollbackOnFailure": "true",
                "vfModules": {},
                "isMissingData": false,
                "originalName": "2017-488_PASQUALE-vPE 0",
                "productFamilyId": "ebc3bc3d-62fd-4a3f-a037-f619df4ff034",
                "lcpCloudRegionId": "hvf6",
                "tenantId": "bae71557c5bb4d5aac6743a4e5f1d054",
                "lineOfBusiness": "ONAP",
                "platformName": "platform",
                "modelInfo": {
                  "modelInvariantId": "5be7e99e-8eb2-4d97-be63-8081ff3cd10e",
                  "modelVersionId": "ea81d6f7-0861-44a7-b7d5-d173b562c350",
                  "modelName": "2017-488_PASQUALE-vPE",
                  "modelVersion": "9.0",
                  "modelCustomizationId": "41516cc6-5098-4b40-a619-f8d5f55fc4d8",
                  "modelCustomizationName": "2017-488_PASQUALE-vPE 0"
                }
              }
            },
            "instanceParams": [
              {
                "2017488_pasqualevpe0_ASN": "AV_vPE"
              }
            ],
            "validationCounter": 0,
            "globalSubscriberId": "e433710f-9217-458d-a79d-1c7aff376d89",
            "productFamilyId": "ebc3bc3d-62fd-4a3f-a037-f619df4ff034",
            "subscriptionServiceType": "TYLER SILVIA",
            "lcpCloudRegionId": "AAIAIC25",
            "tenantId": "092eb9e8e4b7412e8787dd091bc58e86",
            "aicZoneId": "JAG1",
            "projectName": "x1",
            "owningEntityId": "aaa1",
            "rollbackOnFailure": "false",
            "bulkSize": 1,
            "modelInfo": {
              "modelInvariantId": "598e3f9e-3244-4d8f-a8e0-0e5d7a29eda9",
              "modelVersionId": "f4d84bb4-a416-4b4e-997e-0059973630b9",
              "modelName": "PASQUALE vMX vPE_BV Service 488",
              "modelVersion": "1.0"
            },
            "tenantName": "USP-SIP-IC-24335-T-01",
            "existingVNFCounterMap": {
              "41516cc6-5098-4b40-a619-f8d5f55fc4d8": 1
            },
            "existingNames": {},
            "aicZoneName": "YUDFJULP-JAG1"
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
        "type": "[CATEGORY_PARAMETERS] Update"
      }
    }
  }

  function getReduxWithVNFMissingData() {
    return {
      "global": {
        "name": null,
        "flags": {
          "FLAG_NETWORK_TO_ASYNC_INSTANTIATION": false,
          "FLAG_SHOW_ASSIGNMENTS": true,
          "FLAG_FABRIC_CONFIGURATION_ASSIGNMENTS": true,
          "FLAG_SHOW_VERIFY_SERVICE": false,
          "FLAG_SERVICE_MODEL_CACHE": true,
          "FLAG_ADD_MSO_TESTAPI_FIELD": true
        },
        "type": "[FLAGS] Update"
      },
      "service": {
        "serviceHierarchy": {
          "f4d84bb4-a416-4b4e-997e-0059973630b9": {
            "service": {
              "uuid": "f4d84bb4-a416-4b4e-997e-0059973630b9",
              "invariantUuid": "598e3f9e-3244-4d8f-a8e0-0e5d7a29eda9",
              "name": "PASQUALE vMX vPE_BV Service 488",
              "version": "1.0",
              "toscaModelURL": null,
              "category": "Network L1-3",
              "serviceType": "",
              "serviceRole": "",
              "description": "PASQUALE vMX vPE based on Juniper 17.2 release. Updated with updated VF for v8.0 of VLM",
              "serviceEcompNaming": "true",
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
              "2017-488_PASQUALE-vPE 0": {
                "uuid": "ea81d6f7-0861-44a7-b7d5-d173b562c350",
                "invariantUuid": "5be7e99e-8eb2-4d97-be63-8081ff3cd10e",
                "description": "Name PASQUALE vPE Description The provider edge function for the PASQUALE service supported by the Junipers VMX product Category Router Vendor Juniper Vendor Release Code 17.2 Owners Mary Fragale. Updated 9-25 to use v8.0 of the Juniper Valid 2 VLM",
                "name": "2017-488_PASQUALE-vPE",
                "version": "9.0",
                "customizationUuid": "41516cc6-5098-4b40-a619-f8d5f55fc4d8",
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
                  "availability_zone_0": {
                    "type": "string",
                    "description": "The Availability Zone to launch the instance.",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "mtpocfo-kvm-az01"
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
                    "inputName": "2017488_pasqualevpe0_bandwidth_units"
                  },
                  "bandwidth": {
                    "displayName": "bandwidth",
                    "command": "get_input",
                    "inputName": "2017488_pasqualevpe0_bandwidth"
                  },
                  "AIC_CLLI": {
                    "displayName": "AIC_CLLI",
                    "command": "get_input",
                    "inputName": "2017488_pasqualevpe0_AIC_CLLI"
                  },
                  "availability_zone_0": {
                    "displayName": "availability_zone_0",
                    "command": "get_input",
                    "inputName": "2017488_pasqualevpe0_availability_zone_0"
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
                  "nf_type": "ROUTER",
                  "vmxvpfe_int_ctl_ip_1": "10.0.0.10",
                  "is_AVPN_service": "false",
                  "vmx_RSG_name": "vREXI-affinity",
                  "vmx_int_ctl_forwarding": "l2",
                  "vmxvre_oam_ip_0": "10.0.0.10",
                  "vmxvpfe_sriov44_0_port_mac": "00:11:22:EF:AC:DF",
                  "vmxvpfe_sriov41_0_port_vlanstrip": "false",
                  "vmxvpfe_sriov42_0_port_vlanfilter": "4001",
                  "vmxvpfe_sriov44_0_port_unknownunicastallow": "true",
                  "vmxvre_image_name_0": "vre172_nova_img",
                  "vmxvre_instance": "0",
                  "vmxvpfe_sriov43_0_port_mac": "00:11:22:EF:AC:DF",
                  "vmxvre_flavor_name": "ns.c1r16d32.v5",
                  "vmxvpfe_volume_size_0": "40.0",
                  "vmxvpfe_sriov43_0_port_vlanfilter": "4001",
                  "nf_naming": "{ecomp_generated_naming=true}",
                  "multi_stage_design": "false",
                  "nf_naming_code": "me6",
                  "vmxvre_name_0": "vREXI",
                  "vmxvpfe_sriov42_0_port_vlanstrip": "false",
                  "vmxvpfe_volume_name_0": "vPFEXI_FBVolume",
                  "vmx_RSG_id": "bd89a33c-13c3-4a04-8fde-1a57eb123141",
                  "vmxvpfe_image_name_0": "vpfe172_nova_img",
                  "vmxvpfe_sriov43_0_port_unknownunicastallow": "true",
                  "vmxvpfe_sriov44_0_port_unknownmulticastallow": "true",
                  "vmxvre_console": "vidconsole",
                  "vmxvpfe_sriov44_0_port_vlanfilter": "4001",
                  "vmxvpfe_sriov42_0_port_mac": "00:11:22:EF:AC:DF",
                  "vmxvpfe_volume_id_0": "47cede15-da2f-4397-a101-aa683220aff3",
                  "vmxvpfe_sriov42_0_port_unknownmulticastallow": "true",
                  "vmxvpfe_sriov44_0_port_vlanstrip": "false",
                  "vf_module_id": "123",
                  "nf_function": "PASQUALE vPE",
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
                  "bandwidth_units": "get_input:2017488_pasqualevpe0_bandwidth_units",
                  "vnf_id": "123",
                  "vmxvre_oam_prefix": "24",
                  "availability_zone_0": "get_input:2017488_pasqualevpe0_availability_zone_0",
                  "ASN": "get_input:2017488_pasqualevpe0_ASN",
                  "vmxvre_chassis_i2cid": "161",
                  "vmxvpfe_name_0": "vPFEXI",
                  "bandwidth": "get_input:2017488_pasqualevpe0_bandwidth",
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
                  "nf_role": "vPE",
                  "vmxvre_volume_id_0": "f4eacb79-f687-4e9d-b760-21847c8bb15a",
                  "vmxvpfe_sriov42_0_port_unknownunicastallow": "true",
                  "vmxvpfe_flavor_name": "ns.c20r16d25.v5"
                },
                "type": "VF",
                "modelCustomizationName": "2017-488_PASQUALE-vPE 0",
                "vfModules": {
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
                    "uuid": "a5d8df05-11cb-4351-96e0-b6d4168ea4df",
                    "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                    "customizationUuid": "f3d97417-0c8d-424e-8ff7-b2eb4fbcecc3",
                    "description": null,
                    "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                    "version": "8",
                    "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                    "properties": {
                      "minCountInstances": 2,
                      "maxCountInstances": null,
                      "initialCount": 0,
                      "vfModuleLabel": "PASQUALE_vRE_BV"
                    },
                    "inputs": {
                      "2017488_pasqualevpe0_bandwidth_units": {
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
                      },
                      "2017488_pasqualevpe0_bandwidth": {
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
                      }
                    },
                    "volumeGroupAllowed": true
                  },
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
                    "uuid": "040e591e-5d30-4e0d-850f-7266e5a8e013",
                    "invariantUuid": "b34833bb-6aa9-4ad6-a831-70b06367a091",
                    "customizationUuid": "5c5f91f9-5e31-4120-b892-5536587ec258",
                    "description": null,
                    "name": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                    "version": "6",
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
                    "uuid": "b3e8b26e-cff0-49fc-a4e6-f3e16c8440fe",
                    "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
                    "customizationUuid": "6e410843-257c-46d9-ba8a-8d94e1362452",
                    "description": null,
                    "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                    "version": "8",
                    "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                    "properties": {
                      "minCountInstances": 0,
                      "maxCountInstances": null,
                      "initialCount": 0,
                      "vfModuleLabel": "PASQUALE_vPFE_BV"
                    },
                    "inputs": {
                      "2017488_pasqualevpe0_availability_zone_0": {
                        "type": "string",
                        "description": "The Availability Zone to launch the instance.",
                        "entry_schema": null,
                        "inputProperties": {
                          "sourceType": "HEAT",
                          "vfModuleLabel": "PASQUALE_vPFE_BV",
                          "paramName": "availability_zone_0"
                        },
                        "constraints": null,
                        "required": true,
                        "default": "mtpocfo-kvm-az01"
                      }
                    },
                    "volumeGroupAllowed": true
                  }
                },
                "volumeGroups": {
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
                    "uuid": "a5d8df05-11cb-4351-96e0-b6d4168ea4df",
                    "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                    "customizationUuid": "f3d97417-0c8d-424e-8ff7-b2eb4fbcecc3",
                    "description": null,
                    "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                    "version": "8",
                    "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                    "properties": {
                      "minCountInstances": 0,
                      "maxCountInstances": null,
                      "initialCount": 0,
                      "vfModuleLabel": "PASQUALE_vRE_BV"
                    },
                    "inputs": {
                      "2017488_pasqualevpe0_bandwidth_units": {
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
                      },
                      "2017488_pasqualevpe0_bandwidth": {
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
                      }
                    }
                  },
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2": {
                    "uuid": "b3e8b26e-cff0-49fc-a4e6-f3e16c8440fe",
                    "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
                    "customizationUuid": "6e410843-257c-46d9-ba8a-8d94e1362452",
                    "description": null,
                    "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                    "version": "8",
                    "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                    "properties": {
                      "minCountInstances": 0,
                      "maxCountInstances": null,
                      "initialCount": 0,
                      "vfModuleLabel": "PASQUALE_vPFE_BV"
                    },
                    "inputs": {
                      "2017488_pasqualevpe0_availability_zone_0": {
                        "type": "string",
                        "description": "The Availability Zone to launch the instance.",
                        "entry_schema": null,
                        "inputProperties": {
                          "sourceType": "HEAT",
                          "vfModuleLabel": "PASQUALE_vPFE_BV",
                          "paramName": "availability_zone_0"
                        },
                        "constraints": null,
                        "required": true,
                        "default": "mtpocfo-kvm-az01"
                      }
                    }
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
                "uuid": "a5d8df05-11cb-4351-96e0-b6d4168ea4df",
                "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                "customizationUuid": "f3d97417-0c8d-424e-8ff7-b2eb4fbcecc3",
                "description": null,
                "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                "version": "8",
                "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                "properties": {
                  "minCountInstances": 0,
                  "maxCountInstances": null,
                  "initialCount": 0,
                  "vfModuleLabel": "PASQUALE_vRE_BV"
                },
                "inputs": {
                  "2017488_pasqualevpe0_bandwidth_units": {
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
                  },
                  "2017488_pasqualevpe0_bandwidth": {
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
                  }
                },
                "volumeGroupAllowed": true
              },
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
                "uuid": "040e591e-5d30-4e0d-850f-7266e5a8e013",
                "invariantUuid": "b34833bb-6aa9-4ad6-a831-70b06367a091",
                "customizationUuid": "5c5f91f9-5e31-4120-b892-5536587ec258",
                "description": null,
                "name": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                "version": "6",
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
                "uuid": "b3e8b26e-cff0-49fc-a4e6-f3e16c8440fe",
                "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
                "customizationUuid": "6e410843-257c-46d9-ba8a-8d94e1362452",
                "description": null,
                "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                "version": "8",
                "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                "properties": {
                  "minCountInstances": 0,
                  "maxCountInstances": null,
                  "initialCount": 0,
                  "vfModuleLabel": "PASQUALE_vPFE_BV"
                },
                "inputs": {
                  "2017488_pasqualevpe0_availability_zone_0": {
                    "type": "string",
                    "description": "The Availability Zone to launch the instance.",
                    "entry_schema": null,
                    "inputProperties": {
                      "sourceType": "HEAT",
                      "vfModuleLabel": "PASQUALE_vPFE_BV",
                      "paramName": "availability_zone_0"
                    },
                    "constraints": null,
                    "required": true,
                    "default": "mtpocfo-kvm-az01"
                  }
                },
                "volumeGroupAllowed": true
              }
            },
            "volumeGroups": {
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
                "uuid": "a5d8df05-11cb-4351-96e0-b6d4168ea4df",
                "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                "customizationUuid": "f3d97417-0c8d-424e-8ff7-b2eb4fbcecc3",
                "description": null,
                "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                "version": "8",
                "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                "properties": {
                  "minCountInstances": 0,
                  "maxCountInstances": null,
                  "initialCount": 0,
                  "vfModuleLabel": "PASQUALE_vRE_BV"
                },
                "inputs": {
                  "2017488_pasqualevpe0_bandwidth_units": {
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
                  },
                  "2017488_pasqualevpe0_bandwidth": {
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
                  }
                }
              },
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2": {
                "uuid": "b3e8b26e-cff0-49fc-a4e6-f3e16c8440fe",
                "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
                "customizationUuid": "6e410843-257c-46d9-ba8a-8d94e1362452",
                "description": null,
                "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                "version": "8",
                "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                "properties": {
                  "minCountInstances": 0,
                  "maxCountInstances": null,
                  "initialCount": 0,
                  "vfModuleLabel": "PASQUALE_vPFE_BV"
                },
                "inputs": {
                  "2017488_pasqualevpe0_availability_zone_0": {
                    "type": "string",
                    "description": "The Availability Zone to launch the instance.",
                    "entry_schema": null,
                    "inputProperties": {
                      "sourceType": "HEAT",
                      "vfModuleLabel": "PASQUALE_vPFE_BV",
                      "paramName": "availability_zone_0"
                    },
                    "constraints": null,
                    "required": true,
                    "default": "mtpocfo-kvm-az01"
                  }
                }
              }
            },
            "pnfs": {}
          },
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
              "serviceEcompNaming": "true",
              "instantiationType": "Macro",
              "vidNotions": {
                "instantiationType": "Macro"
              },
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
            "pnfs": {}
          }
        },
        "serviceInstance": {
          "f4d84bb4-a416-4b4e-997e-0059973630b9": {
            "vnfs": {
              "2017-488_PASQUALE-vPE 0": {
                "rollbackOnFailure": "true",
                "vfModules": {
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
                    "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0jkyqv": {
                      "isMissingData": false,
                      "sdncPreReload": null,
                      "modelInfo": {
                        "modelType": "VFmodule",
                        "modelInvariantId": "b34833bb-6aa9-4ad6-a831-70b06367a091",
                        "modelVersionId": "040e591e-5d30-4e0d-850f-7266e5a8e013",
                        "modelName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                        "modelVersion": "6",
                        "modelCustomizationId": "5c5f91f9-5e31-4120-b892-5536587ec258",
                        "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0"
                      },
                      "instanceParams": [
                        {}
                      ],
                      "trackById": "n2ydptuy9lj"
                    }
                  }
                },
                "isMissingData": false,
                "originalName": null,
                "productFamilyId": "ebc3bc3d-62fd-4a3f-a037-f619df4ff034",
                "lcpCloudRegionId": null,
                "tenantId": null,
                "lineOfBusiness": null,
                "platformName": null,
                "modelInfo": {
                  "modelType": "VF",
                  "modelInvariantId": "5be7e99e-8eb2-4d97-be63-8081ff3cd10e",
                  "modelVersionId": "ea81d6f7-0861-44a7-b7d5-d173b562c350",
                  "modelName": "2017-488_PASQUALE-vPE",
                  "modelVersion": "9.0",
                  "modelCustomizationName": "2017-488_PASQUALE-vPE 0"
                },
                "trackById": "iapflwk8bip"
              },
              "2017-488_PASQUALE-vPE 0:0001": {
                "rollbackOnFailure": "true",
                "vfModules": {
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
                    "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0jkyqv": {
                      "isMissingData": false,
                      "sdncPreReload": null,
                      "modelInfo": {
                        "modelType": "VFmodule",
                        "modelInvariantId": "b34833bb-6aa9-4ad6-a831-70b06367a091",
                        "modelVersionId": "040e591e-5d30-4e0d-850f-7266e5a8e013",
                        "modelName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                        "modelVersion": "6",
                        "modelCustomizationId": "5c5f91f9-5e31-4120-b892-5536587ec258",
                        "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0"
                      },
                      "instanceParams": [
                        {}
                      ],
                      "trackById": "wh18xgy0dc"
                    }
                  }
                },
                "isMissingData": true,
                "originalName": "2017-488_PASQUALE-vPE 0",
                "productFamilyId": "ebc3bc3d-62fd-4a3f-a037-f619df4ff034",
                "lcpCloudRegionId": null,
                "tenantId": null,
                "lineOfBusiness": null,
                "platformName": null,
                "modelInfo": {
                  "modelType": "VF",
                  "modelInvariantId": "5be7e99e-8eb2-4d97-be63-8081ff3cd10e",
                  "modelVersionId": "ea81d6f7-0861-44a7-b7d5-d173b562c350",
                  "modelName": "2017-488_PASQUALE-vPE",
                  "modelVersion": "9.0",
                  "modelCustomizationName": "2017-488_PASQUALE-vPE 0"
                },
                "trackById": "iapflwk8bip"
              }
            },
            "instanceParams": [
              {
                "2017488_pasqualevpe0_ASN": "AV_vPE"
              }
            ],
            "validationCounter": 1,
            "globalSubscriberId": "e433710f-9217-458d-a79d-1c7aff376d89",
            "productFamilyId": "ebc3bc3d-62fd-4a3f-a037-f619df4ff034",
            "subscriptionServiceType": "TYLER SILVIA",
            "lcpCloudRegionId": "AAIAIC25",
            "tenantId": "092eb9e8e4b7412e8787dd091bc58e86",
            "aicZoneId": "JAG1",
            "projectName": "x1",
            "owningEntityId": "aaa1",
            "rollbackOnFailure": "true",
            "bulkSize": 1,
            "modelInfo": {
              "modelInvariantId": "598e3f9e-3244-4d8f-a8e0-0e5d7a29eda9",
              "modelVersionId": "f4d84bb4-a416-4b4e-997e-0059973630b9",
              "modelName": "PASQUALE vMX vPE_BV Service 488",
              "modelVersion": "1.0"
            },
            "existingVNFCounterMap": {
              "91415b44-753d-494c-926a-456a9172bbb9": 1
            },
            "existingNetworksCounterMap": {},
            "tenantName": "USP-SIP-IC-24335-T-01",
            "aicZoneName": "YUDFJULP-JAG1"
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
        "type": "[LCP_REGIONS_AND_TENANTS] Update"
      }
    }
  }

  function getReduxWithVFModuleMissingData() {
    return {
      "global": {
        "name": null,
        "flags": {
          "FLAG_NETWORK_TO_ASYNC_INSTANTIATION": false,
          "FLAG_SHOW_ASSIGNMENTS": true,
          "FLAG_FABRIC_CONFIGURATION_ASSIGNMENTS": true,
          "FLAG_SHOW_VERIFY_SERVICE": false,
          "FLAG_SERVICE_MODEL_CACHE": true,
          "FLAG_ADD_MSO_TESTAPI_FIELD": true
        },
        "type": "[FLAGS] Update"
      },
      "service": {
        "serviceHierarchy": {
          "f4d84bb4-a416-4b4e-997e-0059973630b9": {
            "service": {
              "uuid": "f4d84bb4-a416-4b4e-997e-0059973630b9",
              "invariantUuid": "598e3f9e-3244-4d8f-a8e0-0e5d7a29eda9",
              "name": "PASQUALE vMX vPE_BV Service 488",
              "version": "1.0",
              "toscaModelURL": null,
              "category": "Network L1-3",
              "serviceType": "",
              "serviceRole": "",
              "description": "PASQUALE vMX vPE based on Juniper 17.2 release. Updated with updated VF for v8.0 of VLM",
              "serviceEcompNaming": "true",
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
              "2017-488_PASQUALE-vPE 0": {
                "uuid": "ea81d6f7-0861-44a7-b7d5-d173b562c350",
                "invariantUuid": "5be7e99e-8eb2-4d97-be63-8081ff3cd10e",
                "description": "Name PASQUALE vPE Description The provider edge function for the PASQUALE service supported by the Junipers VMX product Category Router Vendor Juniper Vendor Release Code 17.2 Owners Mary Fragale. Updated 9-25 to use v8.0 of the Juniper Valid 2 VLM",
                "name": "2017-488_PASQUALE-vPE",
                "version": "9.0",
                "customizationUuid": "41516cc6-5098-4b40-a619-f8d5f55fc4d8",
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
                  "availability_zone_0": {
                    "type": "string",
                    "description": "The Availability Zone to launch the instance.",
                    "entry_schema": null,
                    "inputProperties": null,
                    "constraints": [],
                    "required": true,
                    "default": "mtpocfo-kvm-az01"
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
                    "inputName": "2017488_pasqualevpe0_bandwidth_units"
                  },
                  "bandwidth": {
                    "displayName": "bandwidth",
                    "command": "get_input",
                    "inputName": "2017488_pasqualevpe0_bandwidth"
                  },
                  "AIC_CLLI": {
                    "displayName": "AIC_CLLI",
                    "command": "get_input",
                    "inputName": "2017488_pasqualevpe0_AIC_CLLI"
                  },
                  "availability_zone_0": {
                    "displayName": "availability_zone_0",
                    "command": "get_input",
                    "inputName": "2017488_pasqualevpe0_availability_zone_0"
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
                  "nf_type": "ROUTER",
                  "vmxvpfe_int_ctl_ip_1": "10.0.0.10",
                  "is_AVPN_service": "false",
                  "vmx_RSG_name": "vREXI-affinity",
                  "vmx_int_ctl_forwarding": "l2",
                  "vmxvre_oam_ip_0": "10.0.0.10",
                  "vmxvpfe_sriov44_0_port_mac": "00:11:22:EF:AC:DF",
                  "vmxvpfe_sriov41_0_port_vlanstrip": "false",
                  "vmxvpfe_sriov42_0_port_vlanfilter": "4001",
                  "vmxvpfe_sriov44_0_port_unknownunicastallow": "true",
                  "vmxvre_image_name_0": "vre172_nova_img",
                  "vmxvre_instance": "0",
                  "vmxvpfe_sriov43_0_port_mac": "00:11:22:EF:AC:DF",
                  "vmxvre_flavor_name": "ns.c1r16d32.v5",
                  "vmxvpfe_volume_size_0": "40.0",
                  "vmxvpfe_sriov43_0_port_vlanfilter": "4001",
                  "nf_naming": "{ecomp_generated_naming=true}",
                  "multi_stage_design": "false",
                  "nf_naming_code": "me6",
                  "vmxvre_name_0": "vREXI",
                  "vmxvpfe_sriov42_0_port_vlanstrip": "false",
                  "vmxvpfe_volume_name_0": "vPFEXI_FBVolume",
                  "vmx_RSG_id": "bd89a33c-13c3-4a04-8fde-1a57eb123141",
                  "vmxvpfe_image_name_0": "vpfe172_nova_img",
                  "vmxvpfe_sriov43_0_port_unknownunicastallow": "true",
                  "vmxvpfe_sriov44_0_port_unknownmulticastallow": "true",
                  "vmxvre_console": "vidconsole",
                  "vmxvpfe_sriov44_0_port_vlanfilter": "4001",
                  "vmxvpfe_sriov42_0_port_mac": "00:11:22:EF:AC:DF",
                  "vmxvpfe_volume_id_0": "47cede15-da2f-4397-a101-aa683220aff3",
                  "vmxvpfe_sriov42_0_port_unknownmulticastallow": "true",
                  "vmxvpfe_sriov44_0_port_vlanstrip": "false",
                  "vf_module_id": "123",
                  "nf_function": "PASQUALE vPE",
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
                  "bandwidth_units": "get_input:2017488_pasqualevpe0_bandwidth_units",
                  "vnf_id": "123",
                  "vmxvre_oam_prefix": "24",
                  "availability_zone_0": "get_input:2017488_pasqualevpe0_availability_zone_0",
                  "ASN": "get_input:2017488_pasqualevpe0_ASN",
                  "vmxvre_chassis_i2cid": "161",
                  "vmxvpfe_name_0": "vPFEXI",
                  "bandwidth": "get_input:2017488_pasqualevpe0_bandwidth",
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
                  "nf_role": "vPE",
                  "vmxvre_volume_id_0": "f4eacb79-f687-4e9d-b760-21847c8bb15a",
                  "vmxvpfe_sriov42_0_port_unknownunicastallow": "true",
                  "vmxvpfe_flavor_name": "ns.c20r16d25.v5"
                },
                "type": "VF",
                "modelCustomizationName": "2017-488_PASQUALE-vPE 0",
                "vfModules": {
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
                    "uuid": "a5d8df05-11cb-4351-96e0-b6d4168ea4df",
                    "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                    "customizationUuid": "f3d97417-0c8d-424e-8ff7-b2eb4fbcecc3",
                    "description": null,
                    "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                    "version": "8",
                    "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                    "properties": {
                      "minCountInstances": 2,
                      "maxCountInstances": null,
                      "initialCount": 0,
                      "vfModuleLabel": "PASQUALE_vRE_BV"
                    },
                    "inputs": {
                      "2017488_pasqualevpe0_bandwidth_units": {
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
                      },
                      "2017488_pasqualevpe0_bandwidth": {
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
                      }
                    },
                    "volumeGroupAllowed": true
                  },
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
                    "uuid": "040e591e-5d30-4e0d-850f-7266e5a8e013",
                    "invariantUuid": "b34833bb-6aa9-4ad6-a831-70b06367a091",
                    "customizationUuid": "5c5f91f9-5e31-4120-b892-5536587ec258",
                    "description": null,
                    "name": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                    "version": "6",
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
                    "uuid": "b3e8b26e-cff0-49fc-a4e6-f3e16c8440fe",
                    "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
                    "customizationUuid": "6e410843-257c-46d9-ba8a-8d94e1362452",
                    "description": null,
                    "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                    "version": "8",
                    "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                    "properties": {
                      "minCountInstances": 0,
                      "maxCountInstances": null,
                      "initialCount": 0,
                      "vfModuleLabel": "PASQUALE_vPFE_BV"
                    },
                    "inputs": {
                      "2017488_pasqualevpe0_availability_zone_0": {
                        "type": "string",
                        "description": "The Availability Zone to launch the instance.",
                        "entry_schema": null,
                        "inputProperties": {
                          "sourceType": "HEAT",
                          "vfModuleLabel": "PASQUALE_vPFE_BV",
                          "paramName": "availability_zone_0"
                        },
                        "constraints": null,
                        "required": true,
                        "default": "mtpocfo-kvm-az01"
                      }
                    },
                    "volumeGroupAllowed": true
                  }
                },
                "volumeGroups": {
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
                    "uuid": "a5d8df05-11cb-4351-96e0-b6d4168ea4df",
                    "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                    "customizationUuid": "f3d97417-0c8d-424e-8ff7-b2eb4fbcecc3",
                    "description": null,
                    "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                    "version": "8",
                    "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                    "properties": {
                      "minCountInstances": 0,
                      "maxCountInstances": null,
                      "initialCount": 0,
                      "vfModuleLabel": "PASQUALE_vRE_BV"
                    },
                    "inputs": {
                      "2017488_pasqualevpe0_bandwidth_units": {
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
                      },
                      "2017488_pasqualevpe0_bandwidth": {
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
                      }
                    }
                  },
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2": {
                    "uuid": "b3e8b26e-cff0-49fc-a4e6-f3e16c8440fe",
                    "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
                    "customizationUuid": "6e410843-257c-46d9-ba8a-8d94e1362452",
                    "description": null,
                    "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                    "version": "8",
                    "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                    "properties": {
                      "minCountInstances": 0,
                      "maxCountInstances": null,
                      "initialCount": 0,
                      "vfModuleLabel": "PASQUALE_vPFE_BV"
                    },
                    "inputs": {
                      "2017488_pasqualevpe0_availability_zone_0": {
                        "type": "string",
                        "description": "The Availability Zone to launch the instance.",
                        "entry_schema": null,
                        "inputProperties": {
                          "sourceType": "HEAT",
                          "vfModuleLabel": "PASQUALE_vPFE_BV",
                          "paramName": "availability_zone_0"
                        },
                        "constraints": null,
                        "required": true,
                        "default": "mtpocfo-kvm-az01"
                      }
                    }
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
                "uuid": "a5d8df05-11cb-4351-96e0-b6d4168ea4df",
                "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                "customizationUuid": "f3d97417-0c8d-424e-8ff7-b2eb4fbcecc3",
                "description": null,
                "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                "version": "8",
                "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                "properties": {
                  "minCountInstances": 0,
                  "maxCountInstances": null,
                  "initialCount": 0,
                  "vfModuleLabel": "PASQUALE_vRE_BV"
                },
                "inputs": {
                  "2017488_pasqualevpe0_bandwidth_units": {
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
                  },
                  "2017488_pasqualevpe0_bandwidth": {
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
                  }
                },
                "volumeGroupAllowed": true
              },
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
                "uuid": "040e591e-5d30-4e0d-850f-7266e5a8e013",
                "invariantUuid": "b34833bb-6aa9-4ad6-a831-70b06367a091",
                "customizationUuid": "5c5f91f9-5e31-4120-b892-5536587ec258",
                "description": null,
                "name": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                "version": "6",
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
                "uuid": "b3e8b26e-cff0-49fc-a4e6-f3e16c8440fe",
                "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
                "customizationUuid": "6e410843-257c-46d9-ba8a-8d94e1362452",
                "description": null,
                "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                "version": "8",
                "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                "properties": {
                  "minCountInstances": 0,
                  "maxCountInstances": null,
                  "initialCount": 0,
                  "vfModuleLabel": "PASQUALE_vPFE_BV"
                },
                "inputs": {
                  "2017488_pasqualevpe0_availability_zone_0": {
                    "type": "string",
                    "description": "The Availability Zone to launch the instance.",
                    "entry_schema": null,
                    "inputProperties": {
                      "sourceType": "HEAT",
                      "vfModuleLabel": "PASQUALE_vPFE_BV",
                      "paramName": "availability_zone_0"
                    },
                    "constraints": null,
                    "required": true,
                    "default": "mtpocfo-kvm-az01"
                  }
                },
                "volumeGroupAllowed": true
              }
            },
            "volumeGroups": {
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
                "uuid": "a5d8df05-11cb-4351-96e0-b6d4168ea4df",
                "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                "customizationUuid": "f3d97417-0c8d-424e-8ff7-b2eb4fbcecc3",
                "description": null,
                "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                "version": "8",
                "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                "properties": {
                  "minCountInstances": 0,
                  "maxCountInstances": null,
                  "initialCount": 0,
                  "vfModuleLabel": "PASQUALE_vRE_BV"
                },
                "inputs": {
                  "2017488_pasqualevpe0_bandwidth_units": {
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
                  },
                  "2017488_pasqualevpe0_bandwidth": {
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
                  }
                }
              },
              "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2": {
                "uuid": "b3e8b26e-cff0-49fc-a4e6-f3e16c8440fe",
                "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
                "customizationUuid": "6e410843-257c-46d9-ba8a-8d94e1362452",
                "description": null,
                "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                "version": "8",
                "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                "properties": {
                  "minCountInstances": 0,
                  "maxCountInstances": null,
                  "initialCount": 0,
                  "vfModuleLabel": "PASQUALE_vPFE_BV"
                },
                "inputs": {
                  "2017488_pasqualevpe0_availability_zone_0": {
                    "type": "string",
                    "description": "The Availability Zone to launch the instance.",
                    "entry_schema": null,
                    "inputProperties": {
                      "sourceType": "HEAT",
                      "vfModuleLabel": "PASQUALE_vPFE_BV",
                      "paramName": "availability_zone_0"
                    },
                    "constraints": null,
                    "required": true,
                    "default": "mtpocfo-kvm-az01"
                  }
                }
              }
            },
            "pnfs": {}
          },
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
              "serviceEcompNaming": "true",
              "instantiationType": "Macro",
              "vidNotions": {
                "instantiationType": "Macro"
              },
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
            "pnfs": {}
          }
        },
        "serviceInstance": {
          "f4d84bb4-a416-4b4e-997e-0059973630b9": {
            "vnfs": {
              "2017-488_PASQUALE-vPE 0": {
                "rollbackOnFailure": "true",
                "vfModules": {
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
                    "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0jkyqv": {
                      "isMissingData": true,
                      "sdncPreReload": null,
                      "modelInfo": {
                        "modelType": "VFmodule",
                        "modelInvariantId": "b34833bb-6aa9-4ad6-a831-70b06367a091",
                        "modelVersionId": "040e591e-5d30-4e0d-850f-7266e5a8e013",
                        "modelName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                        "modelVersion": "6",
                        "modelCustomizationId": "5c5f91f9-5e31-4120-b892-5536587ec258",
                        "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0"
                      },
                      "instanceParams": [
                        {}
                      ],
                      "trackById": "n2ydptuy9lj"
                    }
                  }
                },
                "isMissingData": false,
                "originalName": "2017-488_PASQUALE-vPE 0",
                "productFamilyId": "ebc3bc3d-62fd-4a3f-a037-f619df4ff034",
                "lcpCloudRegionId": null,
                "tenantId": null,
                "lineOfBusiness": null,
                "platformName": null,
                "modelInfo": {
                  "modelType": "VF",
                  "modelInvariantId": "5be7e99e-8eb2-4d97-be63-8081ff3cd10e",
                  "modelVersionId": "ea81d6f7-0861-44a7-b7d5-d173b562c350",
                  "modelName": "2017-488_PASQUALE-vPE",
                  "modelVersion": "9.0",
                  "modelCustomizationName": "2017-488_PASQUALE-vPE 0"
                },
                "trackById": "iapflwk8bip"
              }
            },
            "instanceParams": [
              {
                "2017488_pasqualevpe0_ASN": "AV_vPE"
              }
            ],
            "validationCounter": 1,
            "globalSubscriberId": "e433710f-9217-458d-a79d-1c7aff376d89",
            "productFamilyId": "ebc3bc3d-62fd-4a3f-a037-f619df4ff034",
            "subscriptionServiceType": "TYLER SILVIA",
            "lcpCloudRegionId": "AAIAIC25",
            "tenantId": "092eb9e8e4b7412e8787dd091bc58e86",
            "aicZoneId": "JAG1",
            "projectName": "x1",
            "owningEntityId": "aaa1",
            "rollbackOnFailure": "true",
            "bulkSize": 1,
            "modelInfo": {
              "modelInvariantId": "598e3f9e-3244-4d8f-a8e0-0e5d7a29eda9",
              "modelVersionId": "f4d84bb4-a416-4b4e-997e-0059973630b9",
              "modelName": "PASQUALE vMX vPE_BV Service 488",
              "modelVersion": "1.0"
            },
            "existingVNFCounterMap": {
              "41516cc6-5098-4b40-a619-f8d5f55fc4d8": 1
            },
            "existingNetworksCounterMap": {},
            "tenantName": "USP-SIP-IC-24335-T-01",
            "aicZoneName": "YUDFJULP-JAG1"
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
                "id": "229bcdc6eaeb4ca59d55221141d01f8e",
                "name": "AIN Web Tool-15-D-STTest2",
                "isPermitted": true
              },
              {
                "id": "1178612d2b394be4834ad77f567c0af2",
                "name": "AIN Web Tool-15-D-SSPtestcustome",
                "isPermitted": true
              },
              {
                "id": "19c5ade915eb461e8af52fb2fd8cd1f2",
                "name": "AIN Web Tool-15-D-UncheckedEcopm",
                "isPermitted": true
              },
              {
                "id": "de007636e25249238447264a988a927b",
                "name": "AIN Web Tool-15-D-dfsdf",
                "isPermitted": true
              },
              {
                "id": "62f29b3613634ca6a3065cbe0e020c44",
                "name": "AIN/SMS-16-D-Multiservices1",
                "isPermitted": true
              },
              {
                "id": "649289e30d3244e0b48098114d63c2aa",
                "name": "AIN Web Tool-15-D-SSPST66",
                "isPermitted": true
              },
              {
                "id": "3f21eeea6c2c486bba31dab816c05a32",
                "name": "AIN Web Tool-15-D-ASSPST47",
                "isPermitted": true
              },
              {
                "id": "f60ce21d3ee6427586cff0d22b03b773",
                "name": "CESAR-100-D-sspjg67246",
                "isPermitted": true
              },
              {
                "id": "8774659e425f479895ae091bb5d46560",
                "name": "CESAR-100-D-sspjg68359",
                "isPermitted": true
              },
              {
                "id": "624eb554b0d147c19ff8885341760481",
                "name": "AINWebTool-15-D-iftach",
                "isPermitted": true
              },
              {
                "id": "214f55f5fc414c678059c383b03e4962",
                "name": "CESAR-100-D-sspjg612401",
                "isPermitted": true
              },
              {
                "id": "c90666c291664841bb98e4d981ff1db5",
                "name": "CESAR-100-D-sspjg621340",
                "isPermitted": true
              },
              {
                "id": "ce5b6bc5c7b348e1bf4b91ac9a174278",
                "name": "sspjg621351cloned",
                "isPermitted": true
              },
              {
                "id": "b386b768a3f24c8e953abbe0b3488c02",
                "name": "AINWebTool-15-D-eteancomp",
                "isPermitted": true
              },
              {
                "id": "dc6c4dbfd225474e9deaadd34968646c",
                "name": "AINWebTool-15-T-SPFET",
                "isPermitted": true
              },
              {
                "id": "02cb5030e9914aa4be120bd9ed1e19eb",
                "name": "AINWebTool-15-X-eeweww",
                "isPermitted": true
              },
              {
                "id": "f2f3830e4c984d45bcd00e1a04158a79",
                "name": "CESAR-100-D-spjg61909",
                "isPermitted": true
              },
              {
                "id": "05b91bd5137f4929878edd965755c06d",
                "name": "CESAR-100-D-sspjg621512cloned",
                "isPermitted": true
              },
              {
                "id": "7002fbe8482d4a989ddf445b1ce336e0",
                "name": "AINWebTool-15-X-vdr",
                "isPermitted": true
              },
              {
                "id": "4008522be43741dcb1f5422022a2aa0b",
                "name": "AINWebTool-15-D-ssasa",
                "isPermitted": true
              },
              {
                "id": "f44e2e96a1b6476abfda2fa407b00169",
                "name": "AINWebTool-15-D-PFNPT",
                "isPermitted": true
              },
              {
                "id": "b69a52bec8a84669a37a1e8b72708be7",
                "name": "AINWebTool-15-X-vdre",
                "isPermitted": true
              },
              {
                "id": "fac7d9fd56154caeb9332202dcf2969f",
                "name": "AINWebTool-15-X-NONPODECOMP",
                "isPermitted": true
              },
              {
                "id": "2d34d8396e194eb49969fd61ffbff961",
                "name": "DN5242-Nov16-T5",
                "isPermitted": true
              },
              {
                "id": "cb42a77ff45b48a8b8deb83bb64acc74",
                "name": "ro-T11",
                "isPermitted": true
              },
              {
                "id": "fa45ca53c80b492fa8be5477cd84fc2b",
                "name": "ro-T112",
                "isPermitted": true
              },
              {
                "id": "4914ab0ab3a743e58f0eefdacc1dde77",
                "name": "DN5242-Nov21-T1",
                "isPermitted": true
              },
              {
                "id": "d0a3e3f2964542259d155a81c41aadc3",
                "name": "test-hvf6-09",
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
        "type": "[LCP_REGIONS_AND_TENANTS] Update"
      }
    }
  }

  function getReduxWith2Networks() {
    return {
      "global": {
        "name": null,
        "type": "UPDATE_DRAWING_BOARD_STATUS",
        "flags": {
          "EMPTY_DRAWING_BOARD_TEST": false,
          "FLAG_NETWORK_TO_ASYNC_INSTANTIATION": false,
          "FLAG_ADD_MSO_TESTAPI_FIELD": true,
          "FLAG_SERVICE_MODEL_CACHE": true,
          "FLAG_SHOW_ASSIGNMENTS": true,
          "FLAG_FABRIC_CONFIGURATION_ASSIGNMENTS": true,
          "FLAG_DEFAULT_VNF": true,
          "FLAG_A_LA_CARTE_AUDIT_INFO": true,
          "FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST": true,
          "FLAG_PRESENT_PROVIDER_NETWORKS_ASSOCIATIONS": true,
          "FLAG_1810_CR_SOFT_DELETE_ALACARTE_VF_MODULE": false,
          "FLAG_1902_NEW_VIEW_EDIT": false,
          "FLAG_1810_IDENTIFY_SERVICE_FOR_NEW_UI": false,
          "FLAG_1902_VNF_GROUPING": false,
          "FLAG_SHOW_VERIFY_SERVICE": false,
          "FLAG_ASYNC_ALACARTE_VFMODULE": true,
          "FLAG_ASYNC_ALACARTE_VNF": true,
          "FLAG_1810_AAI_LOCAL_CACHE": true,
          "FLAG_EXP_USE_DEFAULT_HOST_NAME_VERIFIER": false,
          "FLAG_EXP_ANY_ALACARTE_NEW_INSTANTIATION_UI": false,
          "FLAG_SUPPLEMENTARY_FILE": true,
          "FLAG_5G_IN_NEW_INSTANTIATION_UI": true,
          "FLAG_RESTRICTED_SELECT": false,
          "FLAG_1810_CR_LET_SELECTING_COLLECTOR_TYPE_UNCONDITIONALLY": true
        },
        "drawingBoardStatus": "CREATE"
      },
      "service": {
        "serviceHierarchy": {
          "2ab1da67-39cc-425f-ba52-59a64d0ea04a": {
            "service": {
              "uuid": "2ab1da67-39cc-425f-ba52-59a64d0ea04a",
              "invariantUuid": "712b3447-f096-42f6-ae4c-4bdc8988feb6",
              "name": "sgi_direct_net NC SRIOV network",
              "version": "1.0",
              "toscaModelURL": null,
              "category": "Network Service",
              "serviceType": "INFRASTRUCTURE",
              "serviceRole": "PROVIDER-NETWORK",
              "description": "SRIOV network model for NC 1.0, VLAN ID 103",
              "serviceEcompNaming": "false",
              "instantiationType": "A-La-Carte",
              "inputs": {},
              "vidNotions": {
                "instantiationUI": "networkWithPropertyNetworkTechnologyEqualsStandardSriovOrOvs",
                "modelCategory": "5G Provider Network",
                "viewEditUI": "legacy",
                "instantiationType": "ALaCarte"
              }
            },
            "vnfs": {},
            "networks": {
              "SR-IOV Provider 2-1": {
                "uuid": "01f4c475-3f89-4f00-a2f4-39a873dba0ae",
                "invariantUuid": "ffb9e45c-e674-4289-aad3-00040ad746e4",
                "description": "NETWORK_CLOUD_PROVIDER_NETWORK",
                "name": "NETWORK_CLOUD_PROVIDER_NETWORK",
                "version": "1.0",
                "customizationUuid": "42551d11-b8d1-460d-8795-3e1363ad7736",
                "inputs": {},
                "commands": {},
                "properties": {
                  "network_role": "sgi_direct_net_1",
                  "network_assignments": "{is_external_network=false, is_shared_network=true, is_trunked=false, ipv4_subnet_default_assignment={dhcp_enabled=false, ip_version=4, min_subnets_count=1, use_ipv4=true}, ecomp_generated_network_assignment=false, ipv6_subnet_default_assignment={dhcp_enabled=false, use_ipv6=true, ip_version=6, min_subnets_count=1}, related_networks=[{related_network_role=sgi_direct_net_1_tenant}]}",
                  "exVL_naming": "{ecomp_generated_naming=false}",
                  "network_flows": "{is_network_policy=false, is_bound_to_vpn=false}",
                  "network_scope": "GLOBAL",
                  "ecomp_generated_naming": "false",
                  "network_type": "SR-IOV-PROVIDER2-1",
                  "provider_network": "{physical_network_name=sriovnet1, is_provider_network=true}",
                  "network_technology": "STANDARD-SR-IOV",
                  "network_homing": "{ecomp_selected_instance_node_target=false}"
                },
                "type": "VL",
                "modelCustomizationName": "SR-IOV Provider 2-1"
              },
              "SR-IOV Provider 2-2": {
                "uuid": "01f4c475-3f89-4f00-a2f4-39a873dba0ae",
                "invariantUuid": "ffb9e45c-e674-4289-aad3-00040ad746e4",
                "description": "NETWORK_CLOUD_PROVIDER_NETWORK",
                "name": "NETWORK_CLOUD_PROVIDER_NETWORK",
                "version": "1.0",
                "customizationUuid": "14d2dc2b-4e85-4ef5-b4da-fe996e2a5d33",
                "inputs": {},
                "commands": {},
                "properties": {
                  "network_role": "sgi_direct_net_2",
                  "network_assignments": "{is_external_network=false, is_shared_network=true, is_trunked=false, ipv4_subnet_default_assignment={dhcp_enabled=false, ip_version=4, min_subnets_count=1, use_ipv4=true}, ecomp_generated_network_assignment=false, ipv6_subnet_default_assignment={dhcp_enabled=false, use_ipv6=true, ip_version=6, min_subnets_count=1}, related_networks=[{related_network_role=sgi_direct_net_2_tenant}]}",
                  "exVL_naming": "{ecomp_generated_naming=false}",
                  "network_flows": "{is_network_policy=false, is_bound_to_vpn=false}",
                  "network_scope": "GLOBAL",
                  "ecomp_generated_naming": "false",
                  "network_type": "SR-IOV-PROVIDER2-2",
                  "provider_network": "{physical_network_name=sriovnet2, is_provider_network=true}",
                  "network_technology": "STANDARD-SR-IOV",
                  "network_homing": "{ecomp_selected_instance_node_target=false}"
                },
                "type": "VL",
                "modelCustomizationName": "SR-IOV Provider 2-2"
              }
            },
            "collectionResources": {},
            "configurations": {},
            "fabricConfigurations": {},
            "serviceProxies": {},
            "vfModules": {},
            "volumeGroups": {},
            "pnfs": {},
            "vnfGroups": {}
          }
        },
        "serviceInstance": {
          "2ab1da67-39cc-425f-ba52-59a64d0ea04a": {
            "action": "Create",
            "isDirty": false,
            "vnfs": {},
            "instanceParams": [
              {}
            ],
            "validationCounter": 0,
            "existingNames": {
              "myname": ""
            },
            "existingVNFCounterMap": {},
            "existingVnfGroupCounterMap": {},
            "existingNetworksCounterMap": {
              "01f4c475-3f89-4f00-a2f4-39a873dba0ae": 1
            },
            "optionalGroupMembersMap": {},
            "networks": {
              "SR-IOV Provider 2-2": {
                "action": "Create",
                "inMaint": false,
                "rollbackOnFailure": "true",
                "originalName": "SR-IOV Provider 2-2",
                "isMissingData": false,
                "trackById": "83ad9rv48px",
                "networkStoreKey": "SR-IOV Provider 2-2",
                "instanceName": "NETWORK_CLOUD_PROVIDER_NETWORK",
                "productFamilyId": null,
                "lcpCloudRegionId": "olson5b",
                "tenantId": "db1818f7f2e34862b378bfb2cc520f91",
                "platformName": "APPLICATIONS-SERVICES",
                "lineOfBusiness": null,
                "instanceParams": [
                  {}
                ],
                "modelInfo": {
                  "modelInvariantId": "ffb9e45c-e674-4289-aad3-00040ad746e4",
                  "modelVersionId": "01f4c475-3f89-4f00-a2f4-39a873dba0ae",
                  "modelName": "NETWORK_CLOUD_PROVIDER_NETWORK",
                  "modelVersion": "1.0",
                  "modelCustomizationId": "14d2dc2b-4e85-4ef5-b4da-fe996e2a5d33",
                  "modelCustomizationName": "SR-IOV Provider 2-2",
                  "uuid": "01f4c475-3f89-4f00-a2f4-39a873dba0ae"
                },
                "uuid": "01f4c475-3f89-4f00-a2f4-39a873dba0ae"
              }
            },
            "vnfGroups": {},
            "bulkSize": 1,
            "instanceName": "myname",
            "globalSubscriberId": "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
            "subscriptionServiceType": "LINDSEY",
            "owningEntityId": "2d097967-10d4-4c7f-b23c-89978249ae17",
            "projectName": null,
            "rollbackOnFailure": "true",
            "aicZoneName": null,
            "owningEntityName": "CRAIG-ROBERTSELLANEOUS",
            "testApi": "VNF_API",
            "tenantName": null,
            "modelInfo": {
              "modelInvariantId": "712b3447-f096-42f6-ae4c-4bdc8988feb6",
              "modelVersionId": "2ab1da67-39cc-425f-ba52-59a64d0ea04a",
              "modelName": "sgi_direct_net NC SRIOV network",
              "modelVersion": "1.0",
              "uuid": "2ab1da67-39cc-425f-ba52-59a64d0ea04a"
            },
            "isALaCarte": true,
            "name": "sgi_direct_net NC SRIOV network",
            "version": "1.0",
            "description": "SRIOV network model for NC 1.0, VLAN ID 103",
            "category": "Network Service",
            "uuid": "2ab1da67-39cc-425f-ba52-59a64d0ea04a",
            "invariantUuid": "712b3447-f096-42f6-ae4c-4bdc8988feb6",
            "serviceType": "INFRASTRUCTURE",
            "serviceRole": "PROVIDER-NETWORK",
            "vidNotions": {
              "instantiationUI": "networkWithPropertyNetworkTechnologyEqualsStandardSriovOrOvs",
              "modelCategory": "5G Provider Network",
              "viewEditUI": "legacy"
            },
            "isEcompGeneratedNaming": false,
            "isMultiStepDesign": false
          }
        },
        "lcpRegionsAndTenants": {
          "lcpRegionList": [
            {
              "id": "olson5a",
              "name": "olson5a (AIC)",
              "isPermitted": true,
              "cloudOwner": "irma-aic"
            },
            {
              "id": "olson5b",
              "name": "olson5b (AIC)",
              "isPermitted": true,
              "cloudOwner": "irma-aic"
            },
            {
              "id": "olson6a",
              "name": "olson6a (AIC)",
              "isPermitted": true,
              "cloudOwner": "irma-aic"
            }
          ],
          "lcpRegionsTenantsMap": {
            "olson5a": [
              {
                "id": "51e7dc5db9bb4c7b94766aacb8a3e72f",
                "name": "Mobitools-FN-27099-T-01",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "d5b3c05cffa645dd9951bf2dd9ef5416",
                "name": "Mobisupport-FN-27099-T-01",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              }
            ],
            "olson5b": [
              {
                "id": "db1818f7f2e34862b378bfb2cc520f91",
                "name": "Mobisupport-FN-27099-T-02",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              }
            ],
            "olson6a": [
              {
                "id": "1dcd712850414fbd91f8a9fc9cca7fd4",
                "name": "FNvEPC-27099-T-MS-olson6A",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              },
              {
                "id": "26af9ed85a004932822a607d5e9973d5",
                "name": "ssf-28239-T-olson6A",
                "isPermitted": true,
                "cloudOwner": "irma-aic"
              }
            ]
          }
        },
        "subscribers": [
          {
            "id": "31739f3e-526b-11e6-beb8-9e71128cae77",
            "name": "CRAIG/ROBERTS",
            "isPermitted": false
          },
          {
            "id": "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
            "name": "Emanuel",
            "isPermitted": true
          },
          {
            "id": "21014aa2-526b-11e6-beb8-9e71128cae77",
            "name": "JULIO ERICKSON",
            "isPermitted": false
          },
          {
            "id": "e433710f-9217-458d-a79d-1c7aff376d89",
            "name": "SILVIA ROBBINS",
            "isPermitted": false
          },
          {
            "id": "VidE2ETest",
            "name": "VidTest20161020",
            "isPermitted": false
          }
        ],
        "productFamilies": null,
        "serviceTypes": {
          "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb": [
            {
              "id": "22",
              "name": "JOHANNA_SANTOS",
              "isPermitted": true
            },
            {
              "id": "11",
              "name": "RAMSEY",
              "isPermitted": false
            },
            {
              "id": "3",
              "name": "LINDSEY",
              "isPermitted": true
            },
            {
              "id": "6",
              "name": "INFRASTRUCTURE",
              "isPermitted": false
            },
            {
              "id": "4",
              "name": "BROOKE-RODRIQUEZ",
              "isPermitted": false
            },
            {
              "id": "0",
              "name": "Emanuel",
              "isPermitted": true
            },
            {
              "id": "15",
              "name": "Kennedy",
              "isPermitted": true
            },
            {
              "id": "21",
              "name": "SSD",
              "isPermitted": true
            },
            {
              "id": "18",
              "name": "VPMS",
              "isPermitted": true
            },
            {
              "id": "1",
              "name": "vJamie",
              "isPermitted": true
            },
            {
              "id": "27",
              "name": "vEPDG",
              "isPermitted": false
            },
            {
              "id": "23",
              "name": "vRichardson",
              "isPermitted": true
            },
            {
              "id": "7",
              "name": "vGDF",
              "isPermitted": false
            },
            {
              "id": "2",
              "name": "vCarroll",
              "isPermitted": true
            },
            {
              "id": "17",
              "name": "vMGCF",
              "isPermitted": false
            },
            {
              "id": "9",
              "name": "vFLORENCE",
              "isPermitted": true
            },
            {
              "id": "25",
              "name": "vWINIFRED",
              "isPermitted": true
            },
            {
              "id": "8",
              "name": "vMNS",
              "isPermitted": true
            },
            {
              "id": "14",
              "name": "vMOG",
              "isPermitted": true
            },
            {
              "id": "10",
              "name": "vOTA",
              "isPermitted": true
            },
            {
              "id": "16",
              "name": "vEsmeralda",
              "isPermitted": true
            },
            {
              "id": "24",
              "name": "vPorfirio",
              "isPermitted": true
            },
            {
              "id": "12",
              "name": "vSILB",
              "isPermitted": false
            },
            {
              "id": "19",
              "name": "vSON",
              "isPermitted": false
            },
            {
              "id": "13",
              "name": "vSSF",
              "isPermitted": false
            },
            {
              "id": "26",
              "name": "vUDR",
              "isPermitted": false
            },
            {
              "id": "20",
              "name": "vVM",
              "isPermitted": true
            },
            {
              "id": "5",
              "name": "vVoiceMail",
              "isPermitted": true
            }
          ]
        },
        "aicZones": null,
        "categoryParameters": {
          "owningEntityList": [
            {
              "id": "1ae27f5e-c0b3-4daf-8561-b25fc1c716e4",
              "name": "AIC-ECOMP"
            },
            {
              "id": "2e51ed6c-1fac-43d4-8f84-9ec405eb7f35",
              "name": "ENTERTAINMENT-VIDEO"
            },
            {
              "id": "2d097967-10d4-4c7f-b23c-89978249ae17",
              "name": "CRAIG-ROBERTSELLANEOUS"
            },
            {
              "id": "aedf37e2-acda-4976-b89b-fd6d4ddffbc6",
              "name": "IP-COMMUNICATIONS"
            },
            {
              "id": "9463675f-6a75-4cc8-8054-c6cb2e67ad51",
              "name": "METRO-JULIO-ERICKSON"
            },
            {
              "id": "92ddf9af-acae-484c-a786-ad7e9c0da26f",
              "name": "EMANUEL-ACCESS"
            },
            {
              "id": "10c645f5-9924-4b89-bec0-b17cf49d3cad",
              "name": "EMANUEL-CORE"
            },
            {
              "id": "048eb6e7-fa94-4f3b-ae03-3175a750dc57",
              "name": "OPTICAL-TRANSPORT"
            },
            {
              "id": "0efc70be-d674-4777-a0fa-329eae187ca0",
              "name": "JULIO-ERICKSON"
            },
            {
              "id": "0463287b-b133-46ef-a0f5-9ce62be3a053",
              "name": "PREMISES"
            },
            {
              "id": "ae4505ad-2961-4395-8659-df2253af4fa8",
              "name": "WIRELINE-ACCESS"
            }
          ],
          "projectList": [
            {
              "id": "G.FAST",
              "name": "G.FAST"
            },
            {
              "id": "GigaPower",
              "name": "GigaPower"
            },
            {
              "id": "Kennedy",
              "name": "Kennedy"
            },
            {
              "id": "Trinity",
              "name": "Trinity"
            },
            {
              "id": "USP",
              "name": "USP"
            }
          ],
          "lineOfBusinessList": [
            {
              "id": "",
              "name": ""
            },
            {
              "id": "ADI",
              "name": "ADI"
            },
            {
              "id": "ADIG",
              "name": "ADIG"
            },
            {
              "id": "PASQUALE",
              "name": "PASQUALE"
            },
            {
              "id": "AT&TLEGACYDATASERVICES",
              "name": "AT&TLEGACYDATASERVICES"
            },
            {
              "id": "AT&TSWITCHEDETHERNET",
              "name": "AT&TSWITCHEDETHERNET"
            },
            {
              "id": "AVPN",
              "name": "AVPN"
            },
            {
              "id": "AVPN-MOW",
              "name": "AVPN-MOW"
            },
            {
              "id": "CALEA",
              "name": "CALEA"
            },
            {
              "id": "COLLABORATE",
              "name": "COLLABORATE"
            },
            {
              "id": "DIRECT-TV",
              "name": "DIRECT-TV"
            },
            {
              "id": "LINDSEY",
              "name": "LINDSEY"
            },
            {
              "id": "FLEXWARE",
              "name": "FLEXWARE"
            },
            {
              "id": "INFRASTRUCTURE",
              "name": "INFRASTRUCTURE"
            },
            {
              "id": "IOT",
              "name": "IOT"
            },
            {
              "id": "IP-FLEXIBLE-REACH",
              "name": "IP-FLEXIBLE-REACH"
            },
            {
              "id": "IP-TOLL-FREE",
              "name": "IP-TOLL-FREE"
            },
            {
              "id": "EMANUEL-ABS",
              "name": "EMANUEL-ABS"
            },
            {
              "id": "EMANUEL-CONSUMER",
              "name": "EMANUEL-CONSUMER"
            },
            {
              "id": "EMANUEL-RESELLER",
              "name": "EMANUEL-RESELLER"
            },
            {
              "id": "NETBOND",
              "name": "NETBOND"
            },
            {
              "id": "SD-WAN",
              "name": "SD-WAN"
            },
            {
              "id": "UVERSE",
              "name": "UVERSE"
            },
            {
              "id": "UVERSE-VOICE",
              "name": "UVERSE-VOICE"
            },
            {
              "id": "VIRTUAL-EDGE",
              "name": "VIRTUAL-EDGE"
            },
            {
              "id": "VOLTE",
              "name": "VOLTE"
            }
          ],
          "platformList": [
            {
              "id": "3rdPartyCloud",
              "name": "3rdPartyCloud"
            },
            {
              "id": "ACCESS",
              "name": "ACCESS"
            },
            {
              "id": "AIC",
              "name": "AIC"
            },
            {
              "id": "APPLICATIONS-SERVICES",
              "name": "APPLICATIONS-SERVICES"
            },
            {
              "id": "BVOIP",
              "name": "BVOIP"
            },
            {
              "id": "CALEA-DEDICATED",
              "name": "CALEA-DEDICATED"
            },
            {
              "id": "CBB-MPLS-CORE",
              "name": "CBB-MPLS-CORE"
            },
            {
              "id": "D1",
              "name": "D1"
            },
            {
              "id": "D1.5",
              "name": "D1.5"
            },
            {
              "id": "EPC",
              "name": "EPC"
            },
            {
              "id": "FIRSTNET-DEDICATED",
              "name": "FIRSTNET-DEDICATED"
            },
            {
              "id": "IMS-USP",
              "name": "IMS-USP"
            },
            {
              "id": "IPAG",
              "name": "IPAG"
            },
            {
              "id": "MNS",
              "name": "MNS"
            },
            {
              "id": "NETWORK-CLOUD",
              "name": "NETWORK-CLOUD"
            },
            {
              "id": "RADIO-WIRELESSENGINEERING",
              "name": "RADIO-WIRELESSENGINEERING"
            },
            {
              "id": "RAN",
              "name": "RAN"
            },
            {
              "id": "UCPE",
              "name": "UCPE"
            },
            {
              "id": "VNI",
              "name": "VNI"
            }
          ]
        },
        "type": "UPDATE_LCP_REGIONS_AND_TENANTS"
      }
    }
  }

  function getReduxWithVNFS(isEcompGeneratedNaming: boolean){
    return {
      "global": {
        "name": null,
        "flags": {
          "FLAG_NETWORK_TO_ASYNC_INSTANTIATION": false,
          "FLAG_SHOW_ASSIGNMENTS": true,
          "FLAG_FABRIC_CONFIGURATION_ASSIGNMENTS": true,
          "FLAG_SHOW_VERIFY_SERVICE": false,
          "FLAG_SERVICE_MODEL_CACHE": true,
          "FLAG_ADD_MSO_TESTAPI_FIELD": true,
          "FLAG_SUPPLEMENTARY_FILE": true
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
              "serviceEcompNaming": "true",
              "instantiationType": "Macro",
              "vidNotions": {
                "instantiationType": "Macro"
              },
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
                  "ecomp_generated_naming": isEcompGeneratedNaming.toString(),
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
                  "ecomp_generated_naming": "false",
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
            "pnfs": {}
          }
        },
        "serviceInstance": {
          "6e59c5de-f052-46fa-aa7e-2fca9d674c44": {
            "vnfs": {
              "VF_vGeraldine 0": {
                "originalName": "VF_vGeraldine 0",
                "rollbackOnFailure": "true",
                "instanceName": "",
                "vfModules": {
                  "vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1": {
                    "vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1dcudx": {
                      "modelInfo": {
                        "modelInvariantId": "98a7c88b-b577-476a-90e4-e25a5871e02b",
                        "modelVersionId": "522159d5-d6e0-4c2a-aa44-5a542a12a830",
                        "modelName": "VfVgeraldine..vflorence_vlc..module-1",
                        "modelVersion": "2",
                        "modelCustomizationId": "55b1be94-671a-403e-a26c-667e9c47d091",
                        "modelCustomizationName": "VfVgeraldine..vflorence_vlc..module-1"
                      },
                      "isMissingData": false,
                      "supplementaryFile": "C:\\fakepath\\sample.json",
                      "supplementaryFile_hidden": {},
                      "supplementaryFile_hidden_content": "{\r\n  \"name\": \"a\",\r\n  \"value\": \"32\"\r\n}",
                      "supplementaryFileContent": {
                        "name": "a",
                        "value": "32"
                      },
                      "supplementaryFileName": "sample.json",
                      "instanceParams": [
                        {}
                      ]
                    }
                  }
                },
                "isMissingData": false,
                "modelName": "VF_vGeraldine 0",
                "productFamilyId": "36b4733a-53f4-4cc8-8ff0-9172e5fc4b8e",
                "lcpCloudRegionId": "hvf6",
                "tenantId": "bae71557c5bb4d5aac6743a4e5f1d054",
                "lineOfBusiness": "zzz1",
                "platformName": "platform",
                "modelInfo": {
                  "modelInvariantId": "4160458e-f648-4b30-a176-43881ffffe9e",
                  "modelVersionId": "d6557200-ecf2-4641-8094-5393ae3aae60",
                  "modelName": "VF_vGeraldine",
                  "modelVersion": "2.0",
                  "modelCustomizationId": "91415b44-753d-494c-926a-456a9172bbb9",
                  "modelCustomizationName": "VF_vGeraldine 0"
                },
                "legacyRegion": null
              }
            },
            "networks": {},
            "instanceParams": [
              {}
            ],
            "validationCounter": 0,
            "globalSubscriberId": "e433710f-9217-458d-a79d-1c7aff376d89",
            "productFamilyId": "36b4733a-53f4-4cc8-8ff0-9172e5fc4b8e",
            "subscriptionServiceType": "TYLER SILVIA",
            "lcpCloudRegionId": "hvf6",
            "tenantId": "1178612d2b394be4834ad77f567c0af2",
            "aicZoneId": "YYY1",
            "projectName": "yyy1",
            "owningEntityId": "aaa1",
            "owningEntityName": "aaa1",
            "rollbackOnFailure": "true",
            "isALaCarte": false,
            "bulkSize": 1,
            "modelInfo": {
              "modelInvariantId": "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0",
              "modelVersionId": "6e59c5de-f052-46fa-aa7e-2fca9d674c44",
              "modelName": "ComplexService",
              "modelVersion": "1.0"
            },
            "instanceName": "",
            "existingNames": {
              "serviceinstancename": "",
              "vfvgeraldine00001": ""
            },
            "existingVNFCounterMap": {
              "91415b44-753d-494c-926a-456a9172bbb9": 1
            },
            "existingNetworksCounterMap": {},
            "tenantName": "AIN Web Tool-15-D-SSPtestcustome",
            "aicZoneName": "UUUAIAAI-YYY1"
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
                "id": "229bcdc6eaeb4ca59d55221141d01f8e",
                "name": "AIN Web Tool-15-D-STTest2",
                "isPermitted": true
              },
              {
                "id": "1178612d2b394be4834ad77f567c0af2",
                "name": "AIN Web Tool-15-D-SSPtestcustome",
                "isPermitted": true
              },
              {
                "id": "19c5ade915eb461e8af52fb2fd8cd1f2",
                "name": "AIN Web Tool-15-D-UncheckedEcopm",
                "isPermitted": true
              },
              {
                "id": "de007636e25249238447264a988a927b",
                "name": "AIN Web Tool-15-D-dfsdf",
                "isPermitted": true
              },
              {
                "id": "62f29b3613634ca6a3065cbe0e020c44",
                "name": "AIN/SMS-16-D-Multiservices1",
                "isPermitted": true
              },
              {
                "id": "649289e30d3244e0b48098114d63c2aa",
                "name": "AIN Web Tool-15-D-SSPST66",
                "isPermitted": true
              },
              {
                "id": "3f21eeea6c2c486bba31dab816c05a32",
                "name": "AIN Web Tool-15-D-ASSPST47",
                "isPermitted": true
              },
              {
                "id": "f60ce21d3ee6427586cff0d22b03b773",
                "name": "CESAR-100-D-sspjg67246",
                "isPermitted": true
              },
              {
                "id": "8774659e425f479895ae091bb5d46560",
                "name": "CESAR-100-D-sspjg68359",
                "isPermitted": true
              },
              {
                "id": "624eb554b0d147c19ff8885341760481",
                "name": "AINWebTool-15-D-iftach",
                "isPermitted": true
              },
              {
                "id": "214f55f5fc414c678059c383b03e4962",
                "name": "CESAR-100-D-sspjg612401",
                "isPermitted": true
              },
              {
                "id": "c90666c291664841bb98e4d981ff1db5",
                "name": "CESAR-100-D-sspjg621340",
                "isPermitted": true
              },
              {
                "id": "ce5b6bc5c7b348e1bf4b91ac9a174278",
                "name": "sspjg621351cloned",
                "isPermitted": true
              },
              {
                "id": "b386b768a3f24c8e953abbe0b3488c02",
                "name": "AINWebTool-15-D-eteancomp",
                "isPermitted": true
              },
              {
                "id": "dc6c4dbfd225474e9deaadd34968646c",
                "name": "AINWebTool-15-T-SPFET",
                "isPermitted": true
              },
              {
                "id": "02cb5030e9914aa4be120bd9ed1e19eb",
                "name": "AINWebTool-15-X-eeweww",
                "isPermitted": true
              },
              {
                "id": "f2f3830e4c984d45bcd00e1a04158a79",
                "name": "CESAR-100-D-spjg61909",
                "isPermitted": true
              },
              {
                "id": "05b91bd5137f4929878edd965755c06d",
                "name": "CESAR-100-D-sspjg621512cloned",
                "isPermitted": true
              },
              {
                "id": "7002fbe8482d4a989ddf445b1ce336e0",
                "name": "AINWebTool-15-X-vdr",
                "isPermitted": true
              },
              {
                "id": "4008522be43741dcb1f5422022a2aa0b",
                "name": "AINWebTool-15-D-ssasa",
                "isPermitted": true
              },
              {
                "id": "f44e2e96a1b6476abfda2fa407b00169",
                "name": "AINWebTool-15-D-PFNPT",
                "isPermitted": true
              },
              {
                "id": "b69a52bec8a84669a37a1e8b72708be7",
                "name": "AINWebTool-15-X-vdre",
                "isPermitted": true
              },
              {
                "id": "fac7d9fd56154caeb9332202dcf2969f",
                "name": "AINWebTool-15-X-NONPODECOMP",
                "isPermitted": true
              },
              {
                "id": "2d34d8396e194eb49969fd61ffbff961",
                "name": "DN5242-Nov16-T5",
                "isPermitted": true
              },
              {
                "id": "cb42a77ff45b48a8b8deb83bb64acc74",
                "name": "ro-T11",
                "isPermitted": true
              },
              {
                "id": "fa45ca53c80b492fa8be5477cd84fc2b",
                "name": "ro-T112",
                "isPermitted": true
              },
              {
                "id": "4914ab0ab3a743e58f0eefdacc1dde77",
                "name": "DN5242-Nov21-T1",
                "isPermitted": true
              },
              {
                "id": "d0a3e3f2964542259d155a81c41aadc3",
                "name": "test-hvf6-09",
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
        "type": "[PRODUCT_FAMILIES] Update"
      }
    }
  }

  function editSecondVnf(vnfNode: string) {
    cy.drawingBoardTreeOpenContextMenuByElementDataTestId(vnfNode, 1)
      .drawingBoardTreeClickOnContextMenuOptionByName('Edit');
    cy.selectDropdownOptionByText('lineOfBusiness', 'ONAP');
    cy.genericFormSubmitForm();
  }

  function checkDynamicInputs() {
    cy.getReduxState().then((state) => {
      let dynamicInputs = state.service.serviceHierarchy['f4d84bb4-a416-4b4e-997e-0059973630b9'].vnfs['2017-488_PASQUALE-vPE 0'].inputs;

      chai.expect(dynamicInputs.vnf_config_template_version.description).equal("VPE Software Version");
      chai.expect(dynamicInputs.bandwidth_units.description).equal("Units of bandwidth");
      chai.expect(dynamicInputs.bandwidth.description).equal("Requested VPE bandwidth");
      chai.expect(dynamicInputs.AIC_CLLI.description).equal("AIC Site CLLI");
      chai.expect(dynamicInputs.availability_zone_0.description).equal("The Availability Zone to launch the instance.")
      chai.expect(dynamicInputs.ASN.description).equal("AV/PE");
      chai.expect(dynamicInputs.vnf_instance_name.description).equal("The hostname assigned to the vpe.");

    });
  }

  function assertEditvfModuleShowFile(vfModuleNode: string, content: string) {
    cy.drawingBoardTreeOpenContextMenuByElementDataTestId(vfModuleNode)
      .drawingBoardTreeClickOnContextMenuOptionByName('Edit');
    cy.get(".file-name").contains(content);

  }
});
