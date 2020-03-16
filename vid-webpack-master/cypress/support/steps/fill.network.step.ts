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
  cy.selectMultiselectValue("multi-lineOfBusiness", "multi-lineOfBusiness-zzz1");
  cy.selectMultiselectValue("multi-selectPlatform",`multi-selectPlatform-xxx1`);
  if(shouldSelectAdditionalPlatform){
    cy.selectMultiselectValue("multi-selectPlatform",`multi-selectPlatform-platform`);
  }
  if(shouldSelectAdditionalLob){
    cy.selectMultiselectValue("multi-lineOfBusiness", "multi-lineOfBusiness-ONAP");
  }
  return cy.getElementByDataTestsId('form-set').click({force : true}).then((done)=>{
    return done;
  });
}

Cypress.Commands.add('fillNetworkPopup', fillNetworkPopup);
