
(function () {
    'use strict';

    appDS2.controller("reportWindowController", ["$uibModalInstance", "$scope", reportWindowController]);

    function reportWindowController($uibModalInstance, $scope) {

        var vm = this;

        vm.close = function () {
            $uibModalInstance.close();
        };

    }
})();