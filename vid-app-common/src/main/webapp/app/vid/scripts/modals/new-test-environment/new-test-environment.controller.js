(function () {
    'use strict';

    appDS2.controller("newTestEnvironmentModalController", ["$uibModalInstance", "$uibModal", "AaiService", "TestEnvironmentsService",
        "$log", "$scope", "_", "COMPONENT","$rootScope", newTestEnvironmentsModalController]);

    function newTestEnvironmentsModalController($uibModalInstance, $uibModal, AaiService, TestEnvironmentsService, $log, $scope, _, COMPONENT, $rootScope) {
        var vm = this;
        vm.newEnvironment = {};

        var init = function () {
            vm.newEnvironment.operationalEnvironmentType = "VNF";
            getEnvironmentsIDsList();
            getEnvironmentsTypesList();
            getWorkloadContextList();
        };

        var getEnvironmentsIDsList = function () {
            TestEnvironmentsService.loadMSOTestEnvironments("ECOMP")
            .then(function(response) {
                vm.environments = response.operationalEnvironment;
            })
            .catch(function (error) {
                $log.error(error);
            });
        };

        var getEnvironmentsTypesList = function () {
            vm.environmentsTypesList = TestEnvironmentsService.getEnvironmentsTypesList();
        };

        var getWorkloadContextList = function () {
            vm.workloadContextList = TestEnvironmentsService.getWorkloadContextList();
        };

        vm.setEcompEnvironment = function () {
            var ecompEnvironment = vm.environments[vm.selectedIndex];
            vm.newEnvironment.ecompInstanceId = ecompEnvironment.operationalEnvironmentId;
            vm.newEnvironment.ecompInstanceName = ecompEnvironment.operationalEnvironmentName;
            vm.newEnvironment.tenantContext = ecompEnvironment.tenantContext;
        };

        vm.close = function () {
            $uibModalInstance.close();
        };

        vm.createEnvironment = function () {
            if($scope.newTestEnvironment.$valid) {
                var requestDetails = vm.newEnvironment;
                $rootScope.$broadcast(COMPONENT.MSO_CREATE_ENVIRONMENT, {
                    url : COMPONENT.MSO_CREATE_ENVIRONMENT,
                    requestDetails : requestDetails
                });
                vm.close();
            }
        };



        init();
    }
})();