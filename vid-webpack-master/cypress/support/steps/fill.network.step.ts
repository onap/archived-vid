// add new command to the existing Cypress interface
declare namespace Cypress {
  interface Chainable {
    fillNetworkPopup: typeof fillNetworkPopup,
  }
}

function fillNetworkPopup(): Chainable<any> {
  cy.selectDropdownOptionByText('productFamily', 'Emanuel');
  cy.selectDropdownOptionByText('lcpRegion', 'hvf6');
  cy.selectDropdownOptionByText('tenant', 'AIN Web Tool-15-D-STTest2');
  cy.selectDropdownOptionByText('lineOfBusiness', 'zzz1');
  cy.selectDropdownOptionByText('platform', 'xxx1');
  return cy.getElementByDataTestsId('form-set').click({force : true}).then((done)=>{
    return done;
  });
}

Cypress.Commands.add('fillNetworkPopup', fillNetworkPopup);
