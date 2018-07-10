"use strict";

var iframeController = function($scope, $location) {
    $scope.url = "app/ui/#" +  $location.$$url;
}

app.controller("iframeController", [ "$scope", "$location", iframeController ]);