declare namespace Cypress {
  interface Chainable {
    setViewportToDefault: typeof setViewportToDefault,
    setViewportToSmallPopup: typeof setViewportToSmallPopup,
    openPopupIframe: typeof openPopupIframe,
  }
}

function setViewportToDefault() {
  cy.viewport(Cypress.config('viewportWidth'), Cypress.config('viewportHeight'));
}

function setViewportToSmallPopup() {
  cy.viewport(1103, 691); // the iframe.popup property on common.css
}

function openPopupIframe(iframeUrl : string): Chainable<Window> {
  cy.setViewportToSmallPopup();
  return cy.visit(iframeUrl);
}

Cypress.Commands.add('setViewportToDefault', setViewportToDefault);
Cypress.Commands.add('setViewportToSmallPopup', setViewportToSmallPopup);
Cypress.Commands.add('openPopupIframe', openPopupIframe);
