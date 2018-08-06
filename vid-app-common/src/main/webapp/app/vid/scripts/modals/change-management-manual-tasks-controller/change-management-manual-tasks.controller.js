(function () {
    'use strict';

    appDS2.controller("changeManagementManualTasksController", ["$uibModalInstance", "jobInfo", "MsoService", "COMPONENT",
        "$log", changeManagementManualTasksController]);

    function changeManagementManualTasksController($uibModalInstance, jobInfo, MsoService, COMPONENT, $log) {
        var vm = this;

        vm.manualTasks = [];
        vm.MANUAL_TASKS = COMPONENT.MANUAL_TASKS;
        var init = function() {
            vm.requestState = jobInfo.requestState;

            if (jobInfo && jobInfo.details) {
                vm.content = jobInfo.details;
            } else {
                vm.content = "The VNF change alerted due to unknown reason.";
            }

            loadAvailableTasks(jobInfo.job.requestId);

        };

        function loadAvailableTasks(requestId) {
            MsoService.getManualTasks(requestId)
                .then(function(response) {
                    vm.task = response.data[0];
                    vm.manualTasks = vm.task && vm.task.validResponses;
                })
                .catch(function(error) {
                    $log.error(error);
                });
        }

        vm.completeTask = function(task) {
            MsoService.completeTask(vm.task.taskId, task)
                .then(function(response) {
                    vm.manualTasks = response.data;
                    $uibModalInstance.close(task + " action completed successfully.");
                })
                .catch(function(error) {
                    $uibModalInstance.close(task + " action failed.");
                    $log.error(error);
                });
        };

        vm.close = function () {
            $uibModalInstance.close();
        };

        vm.isTaskAvailable = function(task) {
            return vm.manualTasks.includes(task);
        };

        init();
    }
})();