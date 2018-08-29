declare namespace Cypress {
  interface Chainable {
    permissionVidMock: typeof permissionVidMock;
    initDrawingBoardUserPermission : typeof  initDrawingBoardUserPermission;
  }
}



function initDrawingBoardUserPermission( response?: any , delay?: number, status?: number) : void {
    cy.server()
      .route({
        method: 'GET',
        delay : delay ? delay : 0,
        status : status ? status : 200,
        url : Cypress.config('baseUrl') + "/roles/service_permissions?**",
        response : response ? response : {
          isEditPermitted : true
        }
      }).as('isEditPermitted');
}

function permissionVidMock(): void {
  initDrawingBoardUserPermission();
}


Cypress.Commands.add('permissionVidMock', permissionVidMock);
Cypress.Commands.add('initDrawingBoardUserPermission', initDrawingBoardUserPermission);
