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

    appDS2.controller("newTestEnvironmentModalController", ["$uibModalInstance", "$uibModal", "AaiService", "TestEnvironmentsService","OwningEntityService",
        "$log", "$scope", "_", "COMPONENT","$rootScope", "featureFlags", newTestEnvironmentsModalController]);

    function newTestEnvironmentsModalController($uibModalInstance, $uibModal, AaiService, TestEnvironmentsService, OwningEntityService, $log, $scope, _, COMPONENT, $rootScope, featureFlags ) {
        var vm = this;
        vm.newEnvironment = {};
        vm.releaseVersions = {};

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
                vm.environmentsTypesList = response["operational-environment-type"].map(function (environmentType){
                    return environmentType.name;});
                vm.workloadContextList = response["workload-context"].map(function (context){
                    return context.name;});
                vm.releaseVersions = response["release"].map(function (releaseOptions){
                    return releaseOptions.name;});
            },COMPONENT.TENANT_ISOLATION_FAMILY);
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
                var requestDetails = vm.newEnvironment;
                delete vm.newEnvironment['release'];
                $rootScope.$broadcast(COMPONENT.MSO_CREATE_ENVIRONMENT, {
                    url : COMPONENT.OPERATIONAL_ENVIRONMENT_CREATE,
                    requestDetails : requestDetails
                });
                vm.close();
            }
        };

        vm.isShowReleaseEnabled = function () {
            return featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_1908_RELEASE_TENANT_ISOLATION);
        };

        init();
    }
})();
