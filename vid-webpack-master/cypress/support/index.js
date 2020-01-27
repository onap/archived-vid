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
import 'cypress-file-upload';
import './steps/login.step';
import './steps/fill.service.popup.step';
import './steps/fill.vnf.popup.step';
import './steps/fill.network.step';
import './steps/fill.vfModule.step';
import './steps/menu.step';
import './steps/openInstanceAuditInfoModal.step';
import './elements/element.actions';
import './elements/element.input.actions';
import './elements/element.select.actions';
import './application/application.session.actions';
import './elements/element.table.actions';
import './jsonBuilders/mocks/aai.mock';
import './jsonBuilders/mocks/vid.mock';
import './jsonBuilders/mocks/permission.mock';

import './steps/general/compareDeepObjects.step';
import './steps/drawingBoard/drawingBoardModel.steps';
import './steps/drawingBoard/drawingBoardTree.steps';
import './steps/genericForm/genericFormAction.steps';
import './steps/genericForm/popupViewport.step';
import './steps/drawingBoard/general.steps';
import './steps/drawingBoard/drawingBoardRecreate.steps';
import './steps/drawingBoard/drawingBoardHeader.steps';
import './steps/general/clickOutside.step';
import './steps/drawingBoard/drawingBoardComponentInfo.steps';
import './steps/genericForm/checkPopover.step';


// Alternatively you can use CommonJS syntax:
// require('./commands')

Cypress.Screenshot.defaults({
  capture: 'runner'
});
