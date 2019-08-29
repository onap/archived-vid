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

"use strict";

var deleteResumeDialogController = function( COMPONENT, FIELD, $scope, $http, $timeout, $log, DataService,
                                             DeleteResumeService, CreationService, UtilityService) {

    $scope.isDialogVisible = false;
    $scope.summaryControl = {};
    $scope.userProvidedControl = {};

    var callbackFunction = undefined;
    var componentId = undefined;
    var volumeGroups = undefined;
    $scope.dialogMethod = COMPONENT.DELETE;

    $scope.$on(COMPONENT.DELETE_RESUME_COMPONENT, function(event, request) {

        $scope.isE2EService = false;
        $scope.isDataVisible = false;
        $scope.isSpinnerVisible = false;
        $scope.isErrorVisible = false;
        $scope.isDialogVisible = true;
        $scope.popup.isVisible = true;
        $scope.isConfirmEnabled = false;
        $scope.dialogMethod = request.dialogMethod;
        $scope.serviceStatus = request.serviceStatus;
        callbackFunction = request.callbackFunction;
        componentId = request.componentId;
        volumeGroups = request.volumeGroups ? request.volumeGroups : [];
        DeleteResumeService.initializeComponent(request.componentId);

        $scope.componentName = DeleteResumeService.getComponentDisplayName();
        $scope.summaryControl.setList(DeleteResumeService.getSummaryList());

        DeleteResumeService.getParameters(handleGetParametersResponse);

    });

    var handleGetParametersResponse = function(parameters, dontshow) {
        $scope.summaryControl.setList(parameters.summaryList);
        $scope.userProvidedControl.setList(parameters.userProvidedList);

        $scope.isSpinnerVisible = false;
        if (dontshow)
            $scope.isDataVisible = false;
        else
            $scope.isDataVisible = true;
        $scope.isConfirmEnabled = true;
    };

    $scope.userParameterChanged = function(id) {
        DeleteResumeService.updateUserParameterList(id, $scope.userProvidedControl);
    };

    $scope.confirm = function() {
        DataService.setE2EService($scope.isE2EService); //VoLTE support
        var requiredFields = $scope.userProvidedControl.getRequiredFields();
        if (requiredFields === "") {
            $scope.isErrorVisible = false;
        } else {
            showError(FIELD.ERROR.MISSING_DATA, requiredFields);
            return;
        }



        var  callbackAfterMSO = function(isSuccessful) {
            if (isSuccessful) {
                $scope.popup.isVisible = false;
                runCallback(true);
            } else {
                $scope.isDialogVisible = true;
            }
        };


        $scope.isDialogVisible = false;

        if ($scope.dialogMethod == COMPONENT.DELETE)
        {

            var requestDetails = DeleteResumeService.getMsoRequestDetails($scope.userProvidedControl.getList());

            if(!DataService.getE2EService() && DeleteResumeService.isMacro === true){
            	requestDetails.requestParameters.aLaCarte = false;
            }

            $scope.$broadcast(COMPONENT.MSO_DELETE_REQ, {
                url : DeleteResumeService.getMsoUrl($scope.serviceStatus),
                requestDetails : requestDetails,
                componentId: componentId,
                callbackFunction : callbackAfterMSO
            });
        }
        else
        if ($scope.dialogMethod == COMPONENT.RESUME)
        {
            CreationService.initializeComponent(componentId);
            CreationService.setInventoryInfo();

            var parameterList = $scope.userProvidedControl.getList();

            if (volumeGroups && volumeGroups.length > 0) {
                var volumeGroupList = FIELD.PARAMETER.AVAILABLE_VOLUME_GROUP;
                volumeGroupList.value = _.map(volumeGroups, function (volumeGroup) {
                    return volumeGroup.name;
                });
                parameterList.push(volumeGroupList);
            }

            var requestDetails = CreationService.getMsoRequestDetails(parameterList);

            $scope.$broadcast(COMPONENT.MSO_CREATE_REQ, {
                url : CreationService.getMsoUrl(),
                requestDetails : requestDetails,
                componentId: componentId,
                callbackFunction : callbackAfterMSO
            });


        }

    };

    $scope.cancel = function() {
        $scope.isDialogVisible = false;
        $scope.popup.isVisible = false;
        runCallback(false);
    };

    var runCallback = function(isSuccessful) {
        if (angular.isFunction(callbackFunction)) {
            callbackFunction({
                isSuccessful : isSuccessful
            });
        }
    };
};

appDS2.controller("deleteResumeDialogController", [ "COMPONENT", "FIELD", "$scope", "$http",
    "$timeout", "$log", "DataService", "DeleteResumeService","CreationService", "UtilityService",
    deleteResumeDialogController]);
