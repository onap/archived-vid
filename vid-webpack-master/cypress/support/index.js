// ***********************************************************
// This example support/index.js is processed and
// loaded automatically before your test files.
//
// This is a great place to put global configuration and
// behavior that modifies Cypress.
//
// You can change the location of this file or turn off
// automatically serving support files with the
// 'supportFile' configuration option.
//
// You can read more here:
// https://on.cypress.io/configuration
// ***********************************************************

// Import commands.js using ES2015 syntax:
import './commands';
import './steps/login.step';
import './elements/element.actions';
import './elements/element.input.actions';
import './elements/element.select.actions';
import './application/application.session.actions';
import './elements/element.table.actions';
import './jsonBuilders/mocks/aai.mock';
import './jsonBuilders/mocks/vid.mock';

// Alternatively you can use CommonJS syntax:
// require('./commands')


