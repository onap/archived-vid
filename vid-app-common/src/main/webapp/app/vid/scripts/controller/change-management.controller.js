(function () {
    'use strict';

    appDS2.controller("changeManagementController", ["$uibModal", "changeManagementService", "_", "$log", changeManagementController]);

    function changeManagementController($uibModal, changeManagementService, _, $log) {
        var vm = this;

        var init = function() {
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
                        var callbackData = _.filter(changeManagement.scheduleRequest.domainData, {name: "CallbackData"});
                        if(callbackData) {
                            var parsedModel = {};
                            try {
                                parsedModel = JSON.parse(callbackData[0].value);
                            } catch(exception) {
                                $log.error(exception);
                            }

                            changeManagement.workflow = parsedModel.requestType || 'No workflow';
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

        vm.openScheduler = function() {
            console.log("function for opening the scheduler app")
        };

        vm.searchChanges = function() {
            console.log("function for searching changes: " + vm.searchChangesTerm)
        };

        vm.openFailedModal = function(jobInfo) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/vid/scripts/modals/failed-change-management/failed-change-management.html',
                controller: 'changeManagementManualTasksController',
                controllerAs: 'vm',
                resolve: {
                    jobInfo: function () {
                        return jobInfo;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                console.log("This is the result of the failed change management modal.", result);
            });
        };

        vm.openInProgressModal = function(jobInfo) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/vid/scripts/modals/in-progress-modal-management/in-progress-change-management.html',
                controller: 'changeManagementManualTasksController',
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
                controller: 'changeManagementManualTasksController',
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

        vm.openPendingModal = function(jobInfo) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/vid/scripts/modals/pending-change-management/pending-change-management.html',
                controller: 'changeManagementManualTasksController',
                controllerAs: 'vm',
                resolve: {
                    jobInfo: function () {
                        return jobInfo;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                console.log("This is the result of the pending change management modal.", result);
            });
        };

        init();
    }
})();