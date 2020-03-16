// add new command to the existing Cypress interface
declare namespace Cypress {
  interface Chainable {
    fillNetworkPopup: typeof fillNetworkPopup,
  }
}

function fillNetworkPopup(shouldSelectAdditionalPlatform: boolean = false, shouldSelectAdditionalLob: boolean = false): Cypress.Chainable<any> {
  cy.selectDropdownOptionByText('productFamily', 'Emanuel');
  cy.selectDropdownOptionByText('lcpRegion', 'hvf6');
  cy.selectDropdownOptionByText('tenant', 'AIN Web Tool-15-D-STTest2');
  cy.selectLobValue("zzz1");
  cy.selectPlatformValue(`xxx1`);
  if(shouldSelectAdditionalPlatform){
    cy.selectPlatformValue(`platform`);
  }
  if(shouldSelectAdditionalLob){
    cy.selectLobValue("ONAP");
  }
  return cy.getElementByDataTestsId('form-set').click({force : true}).then((done)=>{
    return done;
  });
}

Cypress.Commands.add('fillNetworkPopup', fillNetworkPopup);
