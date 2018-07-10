// Cypress.Commands.add('login', () => {
//   const constant = {
//     'LOGIN_ID': '#loginId',
//     'PASSWORD_ID': '#password'
//   };
//
//   cy.on('fail', (err, runnable) => { return false});
//   cy.visit('/login_external.htm')
//     .get(constant.LOGIN_ID).type('16807000')
//     .get(constant.PASSWORD_ID).type('16807000')
//     .get('#loginBtn').click()
//     .getCookies().then((cookies) => {
//         console.log(cookies[0].value)
//     })
// });
