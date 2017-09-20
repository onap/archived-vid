(function () {
    'use strict';

    appDS2.controller("alertNewSchedulerController", ["$uibModalInstance", "jobInfo", "MsoService", "COMPONENT",
        "$log", alertNewSchedulerController]);

    function alertNewSchedulerController($uibModalInstance, jobInfo, MsoService, COMPONENT, $log) {
        var vm = this;

        vm.manualTasks = [];
        vm.MANUAL_TASKS = COMPONENT.MANUAL_TASKS;
        var init = function() {
            if (jobInfo) {
                vm.content = jobInfo;
            } else {
                vm.content = "Successfully";
            }

          

        };

        

      

        vm.close = function () {
            $uibModalInstance.close();
        };

        init();
    }
})();