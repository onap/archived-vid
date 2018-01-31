(function () {
    'use strict';

    appDS2.controller("changeManagementController", ["$uibModal", "changeManagementService", "_", "$log",  "SchedulerService", "$filter", changeManagementController]);

    function changeManagementController($uibModal, changeManagementService, _, $log, SchedulerService, $filter) {
        var vm = this;

        vm.lastTimeUpdated = "";

        vm.init = function() {
            vm.lastTimeUpdated = $filter('date')(new Date(), "MM/dd/yyyy | HH:mm:ss");
            loadMSOChangeManagements();
            loadSchedulerChangeManagements();
        };

        var loadMSOChangeManagements = function() {
            changeManagementService.getMSOChangeManagements()
                .then(function(response) {
                    vm.changeManagements = response.data;
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
                .catch(function(error) {
                    $log.error(error);
                });
        };

        vm.createNewChange = function() {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/vid/scripts/modals/new-change-management/new-change-management.html',
                controller: 'newChangeManagementModalController',
                controllerAs: 'vm',
                resolve: {}
            });

            modalInstance.result.then(function (result) {
                console.log("This is the result of the new change management modal.", result);
            });
        };

        vm.searchChanges = function() {
            console.log("function for searching changes: " + vm.searchChangesTerm)
        };

        vm.openFailedModal = function(jobInfo) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/vid/scripts/modals/failed-change-management/failed-change-management.html',
                controller: 'ChangeManagementManualTasksController',
                controllerAs: 'vm',
                resolve: {
                    jobInfo: function () {
                        return jobInfo;
                    }
                },

            });

            modalInstance.result.then(function (result) {
                console.log("This is the result of the failed change management modal.", result);
            });
        };

        vm.openInProgressModal = function(jobInfo) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/vid/scripts/modals/in-progress-modal-management/in-progress-change-management.html',
                controller: 'ChangeManagementManualTasksController',
                controllerAs: 'vm',
                resolve: {
                    jobInfo: function () {
                        return jobInfo;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                console.log("This is the result of the in progress change management modal.", result);
            });
        };

        vm.openAlertModal = function(jobInfo) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/vid/scripts/modals/alert-change-management/alert-change-management.html',
                controller: 'ChangeManagementManualTasksController',
                controllerAs: 'vm',
                resolve: {
                    jobInfo: function () {
                        return jobInfo;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                console.log("This is the result of the alert change management modal.", result);
            });
        };
        vm.openBasicAlertModal = function(jobInfo) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/vid/scripts/modals/alert-modal/alert-modal.html',
                controller: 'alertModalController',
                controllerAs: 'vm',
                appendTo: angular.element(".jobs-table").eq(0),
                resolve: {
                    jobInfo: function () {
                        return jobInfo;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                console.log("This is the result of the alert change management modal.", result);
            });
        };
        vm.openPendingModal = function($event, changeManagement) {
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


        vm.init();
    }
})();