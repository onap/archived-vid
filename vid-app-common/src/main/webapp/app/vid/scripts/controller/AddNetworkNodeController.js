/*-
* ============LICENSE_START=======================================================
* VID
* ================================================================================
* Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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

/**
 * The Instantiation (or View/Edit) Controller controls the instantiation/removal of
 * deployable objects (Services, VNFs, VF-Modules, Networks, and Volume-Groups)
 */

"use strict";

appDS2.controller("AddNetworkNodeController", ["COMPONENT", "FIELD", "PARAMETER", "DataService", "CreationService", "$scope", "$window", "$location",
    function (COMPONENT, FIELD, PARAMETER, DataService, CreationService, $scope, $window, $location) {

        $scope.serviceMetadataFields = [];
        $scope.tenantList = [];
        $scope.nodeTemplateFields = {};


        $scope.regexInstanceName = /^([a-z])+([0-9a-z\-_\.]*)$/i;
        $scope.errorMsg = FIELD.ERROR.INSTANCE_NAME_VALIDATE;

        var handleGetParametersResponse = function(parameters) {
            $scope.serviceMetadataFields = parameters.summaryList;
            $scope.tenantList = DataService.getCloudRegionTenantList();
            $scope.nodeTemplateFields = _.keyBy(parameters.userProvidedList, 'id');
            $scope.modelName = DataService.getModelInfo(COMPONENT.VNF).modelCustomizationName;
        };

        CreationService.initializeComponent(COMPONENT.VNF);

        CreationService.getParameters(handleGetParametersResponse);

        $scope.setTenant = function(field)  {
            $scope.nodeTemplateFields.tenant.optionList = _.filter($scope.tenantList, {'cloudRegionId': field.value, 'isPermitted': true});
        };

        $scope.cancel = function()  {
            $window.history.back();
        };

        $scope.next = function()  {
            // DataService.setLcpRegion($scope.nodeTemplateFields.lcpRegion.value);
            // DataService.setModelInstanceName($scope.nodeTemplateFields.instanceName.value);
            // DataService.setTenant($scope.nodeTemplateFields.tenant.value);
            // var suppressRollback = ($scope.nodeTemplateFields.suppressRollback.value) ? true : false;
            // DataService.setSuppressRollback(suppressRollback);
            DataService.setPortMirroningConfigFields($scope.nodeTemplateFields);
            $location.path("/serviceProxyConfig");
        };
    }]);
