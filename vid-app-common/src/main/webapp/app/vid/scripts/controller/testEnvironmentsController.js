(function () {
    'use strict';

    appDS2.controller("testEnvironmentsController", ["$uibModal", "TestEnvironmentsService", "$log", "$rootScope", "$scope", "COMPONENT", testEnvironmentsController]);

    function testEnvironmentsController($uibModal, TestEnvironmentsService, $log, $rootScope, $scope, COMPONENT) {
        var vm = this;

        var toggleValue;

        var init = function() {
            vm.loadAAIestEnvironments();
        };

        vm.loadAAIestEnvironments = function() {
            TestEnvironmentsService.loadAAIestEnvironments("VNF")
                .then(function(response) {
                    vm.environments = response.operationalEnvironment;
                    vm.connectError = false;
                    if(!vm.environments.length) {
                        vm.emptyData = true;
                    }
                })
                .catch(function (error) {
                    vm.connectError = error.message || "Unknown error";
                    $log.error(error);
                });
        };

        function handleEnvActionComplete(result) {
            if (result.isSuccessful) {
                vm.loadAAIestEnvironments();
            }
            $scope.popup.isVisible = false;
        }

        vm.onTestEnvActivateClick = function(testEnv) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/vid/scripts/modals/attach-test-env-manifest/attach-test-env-manifest.html',
                controller: 'attachTestEnvManifestController',
                controllerAs: 'vm',
                resolve: {}
            });

            modalInstance.result.then(function (result) {
                if (result) {

                    var relatedEcompEnv = _.find(testEnv.relationshipList.relationship, { relatedTo: "operational-environment" });

                    var manifest = result;
                    var envId = testEnv.operationalEnvironmentId;
                    var relatedInstanceId =
                        _.find(relatedEcompEnv.relationshipData, {"relationshipKey": "operational-environment.operational-environment-id"})
                            .relationshipValue;
                    var relatedInstanceName =
                        _.find(relatedEcompEnv.relatedToProperty, {"propertyKey": "operational-environment.operational-name"})
                            .propertyValue;
                    var workloadContext = testEnv.workloadContext;

                    $rootScope.$broadcast(COMPONENT.MSO_ACTIVATE_ENVIRONMENT, {
                        url: COMPONENT.MSO_ACTIVATE_ENVIRONMENT,
                        envId: envId,
                        relatedInstanceId: relatedInstanceId,
                        relatedInstanceName: relatedInstanceName,
                        workloadContext: workloadContext,
                        manifest: manifest,
                        callbackFunction: handleEnvActionComplete
                    });

                }
            });
        };

        vm.onTestEnvDeactivateClick = function(testEnv) {
            var envId = testEnv.operationalEnvironmentId;
            $rootScope.$broadcast(COMPONENT.MSO_DEACTIVATE_ENVIRONMENT, {
                url : COMPONENT.MSO_DEACTIVATE_ENVIRONMENT,
                envId : envId,
                callbackFunction: handleEnvActionComplete
            });
        };

        vm.isEnvActive = function(testEnv) {
            return testEnv.operationalEnvironmentStatus==='Activate';
        };

        vm.getEnvStatus = function (testEnv) {
            return this.isEnvActive(testEnv) ? "Active" : "Inactive";
        };

        vm.createNewTestEnvironment = function() {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/vid/scripts/modals/new-test-environment/new-test-environment.html',
                controller: 'newTestEnvironmentModalController',
                controllerAs: 'vm',
                resolve: {}
            });
        };

        init();
    }
})();