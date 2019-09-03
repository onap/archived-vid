/*-
* ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ============LICENSE_END=========================================================
*/

/**
 * The Instantiation (or View/Edit) Controller controls the instantiation/removal of
 * deployable objects (Services, VNFs, VF-Modules, Networks, and Volume-Groups)
 */

"use strict";

appDS2.controller("pnfSearchAssociationController", ["COMPONENT", "$log", "FIELD", "PARAMETER", "DataService", "CreationService", "$scope", "$window", "$location", "AaiService", "$uibModal", "UtilityService", "vidService", "$timeout",
    function (COMPONENT, $log, FIELD, PARAMETER, DataService, CreationService, $scope, $window, $location, AaiService, $uibModal, UtilityService, vidService, $timeout) {

        var requestParams = {};

        $scope.selectedMetadata = {};

        $scope.serviceMetadataFields = [];
        $scope.nodeTemplateFields = {};

        $scope.pnfInstance= false;
        $scope.notFound= false;

        $scope.pnfMetadata = [];

        $scope.errorMsg = FIELD.ERROR.INSTANCE_NAME_VALIDATE;

        $scope.modelName = DataService.getModelInfo(COMPONENT.VNF).modelCustomizationName;

        var handleGetParametersResponse = function(parameters) {
            $scope.serviceMetadataFields = parameters.summaryList;
            $scope.serviceMetadataFields.forEach(function (t, number) {
                $scope.serviceMetadataFields[number].key = $scope.serviceMetadataFields[number].name.split(' ').join('');
            });
            $scope.nodeTemplateFields = _.keyBy(parameters.userProvidedList, 'id');
        };

        CreationService.initializeComponent(COMPONENT.VNF);

        CreationService.getParameters(handleGetParametersResponse);

        $scope.back = function()  {
            $window.history.back();
        };

        $scope.searchPnf = function(pnfName) {
            $scope.pnfInstance= false;
            $scope.notFound=false;

            AaiService.getPnfByName(pnfName)
                .then(function (response) {
                    $scope.pnfInstance = response.data;
                    requestParams.pnf = response.data.pnfName;
                })
                .catch(function (error) {
                    $scope.pnfNameNotFound= pnfName;
                   $scope.notFound= true;
                });

        };
        var modalInstance;

        $scope.associate = function()  {

            requestParams.serviceModelInfo = vidService.getModel().service;
            requestParams.attuuid = DataService.getLoggedInUserId();
            requestParams.instanceId = DataService.getServiceInstanceId();

            modalInstance = $uibModal.open({
                templateUrl: 'app/vid/scripts/modals/mso-commit/mso-commit.html',
                controller: "msoCommitModalController",
                backdrop: false,
                resolve: {
                    msoType: function () {
                        return COMPONENT.MSO_CREATE_REALATIONSHIP;
                    },
                    requestParams: function () {
                        requestParams.callbackFunction = updateViewCallbackFunction;
                        return requestParams;
                    },
                    configuration: function () {
                        return null;
                    }
                }
            });
        };

        var updateViewCallbackFunction = function(response) {
            $scope.callbackResults = "";
            var color = FIELD.ID.COLOR_NONE;
            $scope.callbackStyle = {
                "background-color" : color
            };

            /*
             * This 1/2 delay was only added to visually highlight the status
             * change. Probably not needed in the real application code.
             */
            $timeout(function() {
                $scope.callbackResults = UtilityService.getCurrentTime()
                    + FIELD.STATUS.IS_SUCCESSFUL + response.isSuccessful;
                if (response.isSuccessful) {
                    color = FIELD.ID.COLOR_8F8;
                    $scope.back();
                } else {
                    color = FIELD.ID.COLOR_F88;
                }
                $scope.callbackStyle = {
                    "background-color" : color
                };
            }, 500);
        };


        $scope.cancel = function()  {
            modalInstance.dismiss('cancel');
        };


    }]);


