angular.module('att.abs.helper', []);
angular.module('quantum', []);
angular.module('ui.bootstrap', []);
var app=angular.module("abs", ["att.abs", "att.abs.helper","modalServices",
                               "att.gridster","checklist-model","ngRoute", "ui.bootstrap","ngCookies"]);

