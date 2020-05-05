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

    appDS2.controller("changeManagementController", ["$uibModal", "changeManagementService", "_", "$log",  "SchedulerService", "$filter", "VIDCONFIGURATION", changeManagementController]);

    function changeManagementController($uibModal, changeManagementService, _, $log, SchedulerService, $filter, VIDCONFIGURATION, featureFlags) {
        var vm = this;

        vm.lastTimeUpdated = "";
        vm.hasScheduler = !featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_GUILIN_CHANGEMG_SUBMIT_TO_SO) && !!VIDCONFIGURATION.SCHEDULER_PORTAL_URL;
        vm.currModal = null;

        vm.init = function() {
            vm.lastTimeUpdated = $filter('date')(new Date(), "MM/dd/yyyy | HH:mm:ss");
            loadMSOChangeManagements();
            loadSchedulerChangeManagements();
        };

        var fuseMsoAndSchedulerTaks = function() {
            if (vm.changeManagements && vm.pendingChangeManagements) {
                var requestIdToVnfName = {};
                vm.pendingChangeManagements.forEach(function(schedulerItem) {
                    if (schedulerItem.msoRequestId && schedulerItem.vnfName) {
                        requestIdToVnfName[schedulerItem.msoRequestId] = schedulerItem.vnfName;
                    }
                });
                $log.debug("requestIdToVnfName", requestIdToVnfName);

                vm.changeManagements = vm.changeManagements.map(function(msoItem) {
                    msoItem['vnfNameFromScheduler'] = requestIdToVnfName[msoItem.requestId];
                    return msoItem;
                });
            }
        };

        var loadMSOChangeManagements = function() {
            changeManagementService.getMSOChangeManagements()
                .then(function(response) {
                    vm.changeManagements = response.data;
                })
                .then(function () {
                    fuseMsoAndSchedulerTaks();
                })
                .catch(function (error) {
                    $log.error(error);
                });
        };

        var loadSchedulerChangeManagements = function() {
            changeManagementService.getSchedulerChangeManagements()
                .then(function(response) {
                    vm.pendingChangeManagements = response.data;
                    _.forEach(vm.pendingChangeManagements, function(changeManagement) {
                        var callbackData = _.filter(changeManagement.scheduleRequest.domainData, {name: "WorkflowName"});
                        if(callbackData) {
                            var parsedModel = {};
                            try {
                                parsedModel = callbackData[0].value;
                            } catch(exception) {
                                $log.error(exception);
                            }

                            changeManagement.workflow = parsedModel;
                        }
                    });
                })
                .then(function () {
                    fuseMsoAndSchedulerTaks();
                })
                .catch(function(error) {
                    $log.error(error);
                });
        };

        vm.createNewChange = function() {
            vm.closeCurrentModalIfOpen();
            var modalInstance = $uibModal.open({
                templateUrl: 'app/vid/scripts/modals/new-change-management/new-change-management.html',
                controller: 'newChangeManagementModalController',
                controllerAs: 'vm',
                resolve: {}
            });

            vm.currModal = modalInstance;

            modalInstance.result.then(function (result) {
                console.log("This is the result of the new change management modal.", result);
            });
        };

        vm.searchChanges = function() {
            console.log("function for searching changes: " + vm.searchChangesTerm);
        };


        vm.openManualTasksPopup = function($event, jobInfo, templateUrl, message) {

            vm.closeCurrentModalIfOpen();

            var modalInstance = $uibModal.open({
                templateUrl: templateUrl,
                controller: 'changeManagementManualTasksController',
                controllerAs: 'vm',
                backdrop: false,
                animation: true,
                appendTo: angular.element($event.currentTarget).parent(),
                resolve: {
                    jobInfo: function () {
                        return jobInfo;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                console.log(message, result);
            });

            vm.currModal = modalInstance;
        };

        vm.openFailedModal = function($event, jobInfo) {
            vm.openManualTasksPopup($event, jobInfo,
                'app/vid/scripts/modals/failed-change-management/failed-change-management.html',
                "This is the result of the failed change management modal.");
        };

        vm.openInProgressModal = function($event, jobInfo) {
            vm.openManualTasksPopup($event, jobInfo,
                'app/vid/scripts/modals/in-progress-modal-management/in-progress-change-management.html',
                "This is the result of the in progress change management modal.");
        };

        vm.openAlertModal = function($event, jobInfo) {
            vm.openManualTasksPopup($event, jobInfo,
                'app/vid/scripts/modals/alert-change-management/alert-change-management.html',
                "This is the result of the alert change management modal.");
        };

        vm.openBasicAlertModal = function(jobInfo) {
            vm.closeCurrentModalIfOpen();
            var modalInstance = $uibModal.open({
                templateUrl: 'app/vid/scripts/modals/alert-modal/alert-modal.html',
                controller: 'alertModalController',
                controllerAs: 'vm',
                backdrop: false,
                animation: true,
                appendTo: angular.element(".jobs-table").eq(0),
                resolve: {
                    jobInfo: function () {
                        return jobInfo;
                    }
                }
            });
            vm.currModal = modalInstance;

            modalInstance.result.then(function (result) {
                console.log("This is the result of the alert change management modal.", result);
            });
        };
        vm.openPendingModal = function($event, changeManagement) {

            vm.closeCurrentModalIfOpen();

            var modalInstance = $uibModal.open({
                templateUrl: 'app/vid/scripts/modals/cancel-pending-workflow/cancel-pending-workflow.html',
                controller: 'cancelPendingWorkflowController',
                controllerAs: 'vm',
                backdrop: false,
                animation: true,
                appendTo: angular.element($event.currentTarget).parent(),
                resolve: {
                    changeManagement: function () {
                        return changeManagement;
                    }
                }
            });

            vm.currModal = modalInstance;

            modalInstance.result.then(function (result) {
                // send to service
             if(result){
                    SchedulerService.cancelScheduleRequest(changeManagement.scheduleRequest.scheduleId,
                        function(response) {

                            //success popup
                            var jobInfo= {
                                status:"success",
                                message: "Cancel workflow "+changeManagement.scheduleRequest.scheduleName+" succeeded."};
                            vm.openBasicAlertModal(jobInfo);

                            return response;
                        }, function(error) {

                            //failed popup
                            var jobInfo = {
                                status:"failed",
                                message: "Cancel workflow "+changeManagement.scheduleRequest.scheduleName+" failed due to an unexpected error."};
                            vm.openBasicAlertModal(jobInfo);

                        });
            }});

        };
        vm.isChangeManagementDeleted = function(changeManagement) {
            return changeManagement.scheduleRequest.status!=='Deleted'
        };

        vm.closeCurrentModalIfOpen = function() {
            if (vm.currModal != null) {
                vm.currModal.close();
                vm.currModal = null;
            }
        };


        vm.init();
    }
})();
