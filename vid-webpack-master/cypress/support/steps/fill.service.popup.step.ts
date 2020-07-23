
declare namespace Cypress {
  interface Chainable {
    fillServicePopup: typeof FillServicePopup,
    addAlacarteService: typeof addAlacarteService;
  }
}

function FillServicePopup(): Chainable<any> {
  cy.openIframe('/app/ui/#/servicePopup?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd&isCreate=true');
  cy.selectDropdownOptionByText('subscriberName', 'SILVIA ROBBINS');
  cy.selectDropdownOptionByText('serviceType', 'TYLER SILVIA');
  cy.selectDropdownOptionByText('productFamily', 'TYLER SILVIA');
  cy.selectDropdownOptionByText('lcpRegion', 'hvf6');
  cy.selectDropdownOptionByText('tenant', 'AIN Web Tool-15-D-testalexandria');
  cy.selectDropdownOptionByText('aic_zone', 'NFTJSSSS-NFT1');
  cy.selectDropdownOptionByText('project', 'WATKINS');
  cy.selectDropdownOptionByText('owningEntity', 'aaa1');
  cy.selectDropdownOptionByText('rollback', 'Rollback');
  return cy.getElementByDataTestsId('form-set').click({force : true}).then((done)=>{
    return done;
  });

}
function addAlacarteService(): Chainable<any> {
  cy.openIframe('/app/ui/#/servicePopup?serviceModelId=2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd&isCreate=true');
  cy.selectDropdownOptionByText('subscriberName', 'SILVIA ROBBINS');
  cy.selectDropdownOptionByText('serviceType', 'TYLER SILVIA');
  cy.selectDropdownOptionByText('project', 'WATKINS');
  cy.selectDropdownOptionByText('owningEntity', 'aaa1');
  cy.selectDropdownOptionByText('rollback', 'Rollback');
  return cy.getElementByDataTestsId('form-set').click({force : true}).then((done)=>{
    return done;
  });

}

Cypress.Commands.add('fillServicePopup', FillServicePopup);
Cypress.Commands.add('addAlacarteService', addAlacarteService);
