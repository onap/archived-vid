declare namespace Cypress {
  interface Chainable {
    fillVFModulePopup: typeof FillVFModulePopup
  }
}

function FillVFModulePopup(vnfName: string, vfModuleName: string, instanceName: string, lcpRegion: string, tenant: string, rollback: boolean, sdncPreLoad: boolean): Chainable<any> {
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

Cypress.Commands.add('fillVFModulePopup', FillVFModulePopup);
