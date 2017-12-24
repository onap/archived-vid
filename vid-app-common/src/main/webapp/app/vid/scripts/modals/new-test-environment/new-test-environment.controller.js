(function () {
    'use strict';

    appDS2.controller("newTestEnvironmentModalController", ["$uibModalInstance", "$uibModal", "AaiService", "TestEnvironmentsService",
        "$log", "$scope", "_", "COMPONENT","$rootScope", newTestEnvironmentsModalController]);

    function newTestEnvironmentsModalController($uibModalInstance, $uibModal, AaiService, TestEnvironmentsService, $log, $scope, _, COMPONENT, $rootScope) {
        var vm = this;
        vm.newEnvironment = {};

        var init = function () {
            vm.newEnvironment.operationalEnvironmentType = "VNF";
            loadEcompEnvironmentsList();
            loadEnvironmentsTypesList();
            loadWorkloadContextList();
        };

        var loadEcompEnvironmentsList = function () {
            TestEnvironmentsService.loadAAIestEnvironments("ECOMP")
            .then(function(response) {
                vm.environments = response.operationalEnvironment;
            })
            .catch(function (error) {
                vm.aaiConnectError = error.message;
                $log.error(error);
            });
        };

        var loadEnvironmentsTypesList = function () {
            vm.environmentsTypesList = TestEnvironmentsService.getEnvironmentsTypesList();
        };

        var loadWorkloadContextList = function () {
            vm.workloadContextList = TestEnvironmentsService.getWorkloadContextList();
        };

        vm.setEcompEnvironment = function (selectedIndex) {
            var ecompEnvironment = vm.environments[selectedIndex];
            vm.newEnvironment.ecompInstanceId = ecompEnvironment.operationalEnvironmentId;
            vm.newEnvironment.ecompInstanceName = ecompEnvironment.operationalEnvironmentName;
            vm.newEnvironment.tenantContext = ecompEnvironment.tenantContext;
        };

        vm.close = function () {
            $uibModalInstance.close();
        };

        vm.createEnvironment = function () {
            if($scope.newTestEnvironment.$valid) {
                vm.newEnvironment.workloadContext = vm.newEnvironment.operationalEnvironmentType + '_' + vm.newEnvironment.workloadContext;
                var requestDetails = vm.newEnvironment;
                $rootScope.$broadcast(COMPONENT.MSO_CREATE_ENVIRONMENT, {
                    url : COMPONENT.OPERATIONAL_ENVIRONMENT_CREATE,
                    requestDetails : requestDetails
                });
                vm.close();
            }
        };



        init();
    }
})();