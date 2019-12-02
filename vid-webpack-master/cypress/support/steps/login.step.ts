// add new command to the existing Cypress interface
declare namespace Cypress {
  interface Chainable {
    login: typeof login,
    openIframe : typeof openIframe
  }
}

let currentLoginCookies : any = null;


function login(): void {
  if(currentLoginCookies){
    cy.setCookie('JSESSIONID', currentLoginCookies.value, {
      expiry : currentLoginCookies.expiry,
      path : currentLoginCookies.path,
      domain : currentLoginCookies.domain
    });
  }else {
    const constant = {
      "LOGIN_ID" : "#loginId",
      "PASSWORD_ID" : "#password",
      "LOGIN_BTN_ID" : "#loginBtn"
    };

    cy.visit('/login_external.htm');
    cy.get(constant.LOGIN_ID).type("us16807000")
      .get(constant.PASSWORD_ID).type("us16807000")
      .get(constant.LOGIN_BTN_ID).click();

    cy.getCookie("JSESSIONID").then((res) => {
      currentLoginCookies = res;
    });
  }
}

function openIframe(iframeUrl : string): Chainable<Window> {
  return cy.visit(iframeUrl);
}

Cypress.Commands.add('login', login);
Cypress.Commands.add('openIframe', openIframe);
