(function () {
    'use strict';

    appDS2.controller("newTestEnvironmentModalController", ["$uibModalInstance", "$uibModal", "AaiService", "TestEnvironmentsService","OwningEntityService",
        "$log", "$scope", "_", "COMPONENT","$rootScope", newTestEnvironmentsModalController]);

    function newTestEnvironmentsModalController($uibModalInstance, $uibModal, AaiService, TestEnvironmentsService,OwningEntityService, $log, $scope, _, COMPONENT, $rootScope) {
        var vm = this;
        vm.newEnvironment = {};

        var init = function () {
            vm.newEnvironment.operationalEnvironmentType = "VNF";
            loadCategoryParameters();
            loadEcompEnvironmentsList();
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

        var loadCategoryParameters = function () {
            OwningEntityService.getOwningEntityProperties(function(response){
               vm.environmentsTypesList = response["operational-environment-type"].map(function (x){
                    return x.name;});
               vm.workloadContextList = response["workload-context"].map(function (x){
                   return x.name;});
            },COMPONENT.TENANT_ISOLATION_FAMILY);
        }


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