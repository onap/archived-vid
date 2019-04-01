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
        "$log", "moment", changeManagementManualTasksController]);

    function changeManagementManualTasksController($uibModalInstance, jobInfo, MsoService, COMPONENT, $log, moment) {
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
            return MsoService.getManualTasks(requestId)
                .then(function(response) {
                    vm.task = response.data[0];
                    vm.manualTasks = vm.task && vm.task.validResponses;
                    vm.description = vm.task && vm.task.description || null;
                    vm.timeout = vm.task && vm.task.timeout || null;
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

        vm.timeoutHumanized = function() {
            // moment.duration() can parse ISO 8601 time-intervals,
            // e.g. "P1Y2M10DT2H30M"
            // https://en.wikipedia.org/wiki/ISO_8601#Time_intervals
            let duration = moment.duration(vm.timeout);

            return isDurationValid()
                ? durationAsHoursAndMinutes() + ' hours (' + vm.timeout + ')'
                : vm.timeout;


            function isDurationValid() {
                return duration.isValid() && duration.toISOString() !== 'P0D';
            }

            function durationAsHoursAndMinutes() {
                return ''
                    + Math.floor(duration.asHours())
                    + ':'
                    + withLeadingZero(duration.minutes());
            }

            function withLeadingZero(x) {
                return ("00" + Math.round(x)).slice(-2);
            }
        };

        vm.__test_only__ = {
            loadAvailableTasks: loadAvailableTasks,
        };

        init();
    }
})();
