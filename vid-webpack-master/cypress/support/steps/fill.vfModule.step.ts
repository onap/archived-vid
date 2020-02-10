declare namespace Cypress {
  interface Chainable {
    fillVFModulePopup: typeof fillVFModulePopup;
    addALaCarteVfModule: typeof addALaCarteVfModule;
    addMacroVfModule: typeof addMacroVfModule;
  }
}

function fillVFModulePopup(vnfName: string, vfModuleName: string, instanceName: string, lcpRegion: string, tenant: string, rollback: boolean, sdncPreLoad: boolean): Chainable<any> {
  cy.getElementByDataTestsId('node-' + vnfName).click({force: true});
  cy.getElementByDataTestsId('node-' + vfModuleName + '-add-btn').click({force: true});
  cy.getElementByDataTestsId('instanceName').last().type(instanceName, {force: true});
  cy.getElementByDataTestsId('lcpRegion').last().select(lcpRegion);
  cy.getElementByDataTestsId('tenant').last().select(tenant);
  cy.getElementByDataTestsId('lcpRegion').last().select(lcpRegion);
  cy.getElementByDataTestsId('rollback').last().select(String(rollback));
  if (sdncPreLoad) {
    cy.getElementByDataTestsId('sdncPreLoad').last().check();
  }
  return cy.getElementByDataTestsId('form-set').last().click({force: true}).then((done) => {
    return done;
  });

}

function addMacroVfModule(vnfName: string, vfModuleName: string, instanceName: string): Chainable<any> {
  return cy.getElementByDataTestsId('node-' + vnfName).click({force: true}).then(() => {
    cy.getElementByDataTestsId('node-' + vfModuleName + '-add-btn').click({force: true}).then(() => {
      cy.getElementByDataTestsId('instanceName').clear().type(instanceName, {force: true}).then(() => {
        cy.getElementByDataTestsId('form-set').click({force: true});
      })
    })
  });
}

function addALaCarteVfModule(vnfName: string, vfModuleName: string, instanceName: string, lcpRegion: string, legacyRegion: string,
                     tenant: string, rollback: boolean, sdncPreLoad: boolean, deleteVgName: boolean, flag: boolean): Chainable<any> {
  return cy.getElementByDataTestsId('node-' + vnfName).click({force: true}).then(() => {
    cy.getElementByDataTestsId('node-' + vfModuleName + '-add-btn').click({force: true}).then(() => {
      cy.getElementByDataTestsId('instanceName').clear().type(instanceName, {force: true}).then(() => {
        if (deleteVgName) {
          cy.getElementByDataTestsId('volumeGroupName').clear();
        }
      }).then(() => {
        if(!flag) {
          cy.selectDropdownOptionByText('lcpRegion', lcpRegion);
          if (legacyRegion) {
            cy.typeToInput("lcpRegionText", legacyRegion);
          }
          cy.selectDropdownOptionByText('tenant', tenant);
        }
        cy.selectDropdownOptionByText('rollback', String(rollback));
        if (sdncPreLoad) {
          cy.getElementByDataTestsId('sdncPreLoad').check();
        }
        cy.getElementByDataTestsId('form-set').click({force: true});
      });
    });
  });
}

Cypress.Commands.add('fillVFModulePopup', fillVFModulePopup);
Cypress.Commands.add('addALaCarteVfModule', addALaCarteVfModule);
Cypress.Commands.add('addMacroVfModule', addMacroVfModule);
