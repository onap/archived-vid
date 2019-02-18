declare namespace Cypress {
  interface Chainable {
    assertComponentInfoTitleLabelsAndValues: typeof assertComponentInfoTitleLabelsAndValues
  }
}



function assertComponentInfoTitleLabelsAndValues(expectedTitle: string, labelsAndValues: string[][]) : void{
  cy.getElementByDataTestsId('component-info-section-title').should('have.text', expectedTitle);
  labelsAndValues.forEach((tuple: string[], index: number, array: string[][]) => {
    let label = tuple[0];
    let value = tuple[1];
    cy.getElementByDataTestsId('model-item-label-' + label).should('have.text', label);
    cy.getElementByDataTestsId('model-item-value-' + label).should('have.text', value);
  });
}




Cypress.Commands.add('assertComponentInfoTitleLabelsAndValues', assertComponentInfoTitleLabelsAndValues);
