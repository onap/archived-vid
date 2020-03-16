// add new command to the existing Cypress interface


declare namespace Cypress {
  interface Chainable {
    fillVnfPopup: typeof FillVnfPopup,
    duplicateVnf: typeof DuplicateVnf,
  }
}
function FillVnfPopup(): Chainable<any> {
  cy.selectDropdownOptionByText('productFamily', 'Emanuel');
  cy.selectDropdownOptionByText('lcpRegion', 'hvf6');
  cy.selectDropdownOptionByText('tenant', 'AIN Web Tool-15-D-STTest2');
  cy.selectMultiselectValue("multi-lineOfBusiness", "multi-lineOfBusiness-zzz1");
  cy.selectMultiselectValue("multi-selectPlatform",`multi-selectPlatform-xxx1`);
  return cy.getElementByDataTestsId('form-set').click({force : true}).then((done)=>{
    return done;
  });
}
function DuplicateVnf( vnfNode: string, amountBefore: number): Chainable<any> {
  return cy.getElementByDataTestsId(vnfNode).should('have.length', amountBefore)
    .getElementByDataTestsId(vnfNode+"-menu-btn").click({force:true})
    .getElementByDataTestsId('context-menu-duplicate').click({force : true})
    .getTagElementContainsText('button','Duplicate').click({force:true})
    .getElementByDataTestsId(vnfNode).should('have.length', amountBefore+1).then((done)=>{
      return done;
    });
}
Cypress.Commands.add('fillVnfPopup', FillVnfPopup);
Cypress.Commands.add('duplicateVnf', DuplicateVnf);
