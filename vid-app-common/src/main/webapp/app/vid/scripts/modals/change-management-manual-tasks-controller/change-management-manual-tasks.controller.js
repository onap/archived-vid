/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

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
