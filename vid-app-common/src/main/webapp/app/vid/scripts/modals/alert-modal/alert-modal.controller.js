(function () {
    'use strict';

    appDS2.controller("alertModalController", ["$uibModalInstance", "jobInfo",
        "$log", alertModalController]);

    function alertModalController($uibModalInstance, jobInfo,  $log) {
        var vm = this;
        var init = function() {
            if (jobInfo) {
                vm.content = jobInfo.message;
                vm.mode = jobInfo.status;
                if (vm.mode == "failed") {
                    vm.Header = "Failed"
                } else if (vm.mode == "confirm") {
                    vm.Header = "Confirm"
                } else {
                    vm.Header = "Success"
                }
            }
        };

        vm.close = function () {
            $uibModalInstance.close();
        };

        vm.ok = function () {
            $uibModalInstance.close(true);
        };

        init();
    }
})();