declare namespace Cypress {
  interface Chainable {
    checkPopoverContentOnMouseEvent: typeof checkPopoverContentOnMouseEvent
  }
}

function checkPopoverContentOnMouseEvent(dataTestId: string, klass: string, mouseEvent: string, index: number) : Chainable<any> {
  index = index || 0;
  let element = cy.getElementByDataTestsId(dataTestId).eq(index);
  element.trigger(mouseEvent).click().get(klass);
  return element;
}

Cypress.Commands.add('checkPopoverContentOnMouseEvent', checkPopoverContentOnMouseEvent);
