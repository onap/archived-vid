angular.module('att.abs.helper', []);
angular.module('quantum', []);
var app=angular.module("abs", ["att.abs", "att.abs.helper","modalServices", /*'ngAnimate','ngTouch',*/ 'ui.bootstrap',
                               "att.gridster","checklist-model","ngRoute", "ngCookies", 'btorfs.multiselect','ngFileUpload','feature-flags', 'angularMoment']);

app.run(function(featureFlags, $http) {
    $http.get('flags').then(function (results) {
         var flags = [];
        for (var key in results.data) {
            flags.push({"key":key, "active":results.data[key]});
        }
        featureFlags.set(flags);
    });
});
