(function () {
    'use strict';

    appDS2.controller("cancelPendingWorkflowController", ["$scope", "$uibModalInstance", "changeManagement",
        "$log", cancelPendingWorkflowController]);

    function cancelPendingWorkflowController($scope, $uibModalInstance, changeManagement, $log) {
        var vm = this;

        function init() {
            if (changeManagement) {
                vm.workflow = changeManagement;
            } else {
                console.log("Pending Workflow is undefined: ", changeManagement);
                vm.workflow = null;
            }
        }

        vm.close = function () {
            $uibModalInstance.close();
        };

        vm.ok = function () {
            $uibModalInstance.close(true);
        };

        init();
    }
})();