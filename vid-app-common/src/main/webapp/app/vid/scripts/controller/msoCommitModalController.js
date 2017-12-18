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

"use strict";

var msoCommitModalController = function(COMPONENT, FIELD, $scope, $http, $timeout, $window, $log,
		MsoService, PropertyService, UtilityService, DataService, $uibModalInstance, msoType, componentId, vidService) {

    $scope.isSpinnerVisible = true;
    $scope.isProgressVisible = true;
    $scope.status = FIELD.STATUS.SUBMITTING_REQUEST;
    $scope.error = "";
    $scope.log = "";
    $scope.progressBarControl = {};

    var _this = this;
    _this.pollAttempts = 0;
    _this.isMsoError = false;


    if (angular.isFunction($scope.progressBarControl.reset)) {
        $scope.progressBarControl.reset();
    }
    $scope.percentProgress = 2; // Show "a little" progress


    /*
	 * Updates the view and returns "true" if the MSO operation has returned a
	 * "Complete" status.
	 */
    var isUpdateViewAfterGetResponseComplete = function(response) {
        //console.log("msoCommitController isUpdateViewAfterGetResponseComplete");
        updateLogFinalResponse(response);

        var requestStatus = UtilityService.checkUndefined(FIELD.ID.REQUEST_STATUS,
            UtilityService.checkUndefined(FIELD.ID.REQUEST,
                response.data.entity.request).requestStatus);

        var requestState = requestStatus.requestState;
        console.log("msoCommitController requestState=" + requestState);
        // look for "progress" or "pending"
        var patt1 = /progress/i;
        var patt2 = /pending/i;
        var result1 = patt1.test(requestState);
        var result2 = patt2.test(requestState);
        if (result1 || result2) {
            requestState = FIELD.STATUS.IN_PROGRESS;
        }
        var statusMessage = requestStatus.statusMessage;
        console.log("msoCommitController statusMessage=" + statusMessage);
        if (UtilityService.hasContents(statusMessage)) {
            $scope.status = requestState + " - " + statusMessage;
        } else {
            $scope.status = requestState;
        }
        if (UtilityService.hasContents(requestStatus.percentProgress)) {
            $scope.percentProgress = requestStatus.percentProgress;
        }

        if ( (requestState.toLowerCase() === FIELD.STATUS.FAILED.toLowerCase()) || (requestState.toLowerCase() === FIELD.STATUS.UNLOCKED.toLowerCase())) {
            throw {
                type : FIELD.STATUS.MSO_FAILURE
            };
        }

        if (requestState.toLowerCase() === FIELD.STATUS.COMPLETE.toLowerCase()) {
            $scope.isSpinnerVisible = false;
            return true;
        }

        return false;
    };

    var handleGetResponse = function(response) {
        try {
            if (isUpdateViewAfterGetResponseComplete(response)) {
                return;
            }
            if (++_this.pollAttempts > PropertyService.getMsoMaxPolls()) {
                _this.isMsoError = true;
                showError(FIELD.ERROR.MAX_POLLS_EXCEEDED);
            } else {
                _this.timer = $timeout(getRequestStatus, PropertyService
                    .getMsoMaxPollingIntervalMsec());
            }
        } catch (error) {
            _this.isMsoError = true;
            MsoService.showResponseContentError(error, showError);
        }
    };

    var showError = function(summary, details) {
        var message = summary;
        if (UtilityService.hasContents(details)) {
            message += " (" + details + ")";
        }
        $scope.isSpinnerVisible = false;
        $scope.isProgressVisible = false;
        $scope.error = message;
        $scope.status = FIELD.STATUS.ERROR;
    };

    var getRequestStatus = function() {
        MsoService.getOrchestrationRequest(_this.requestId, handleGetResponse);
    };

    var updateLog = function(response) {
        $scope.log = MsoService.getFormattedCommonResponse(response)
            + $scope.log;
        UtilityService.checkUndefined("entity", response.data.entity);
        UtilityService.checkUndefined("status", response.data.status);
        MsoService.checkValidStatus(response);
    };

    var updateLogFinalResponse = function(response) {
        $scope.log = MsoService.getFormattedSingleGetOrchestrationRequestResponse(response)
            + $scope.log;
        UtilityService.checkUndefined("entity", response.data.entity);
        UtilityService.checkUndefined("status", response.data.status);
        MsoService.checkValidStatus(response);
    };

    var updateViewAfterInitialResponse = function(response) {

        updateLog(response);

        _this.requestId = UtilityService.checkUndefined(FIELD.ID.REQUEST_ID,
            UtilityService.checkUndefined(FIELD.ID.REQUEST_REFERENCES,
                response.data.entity.requestReferences).requestId);

        $scope.percentProgress = 4; // Show "a little more" progress
        $scope.status = FIELD.STATUS.IN_PROGRESS;
    };

    _this.msoRequestType = msoType;
    _this.componentId = componentId;

    if (msoType === COMPONENT.MSO_CREATE_REQ &&
        componentId === COMPONENT.VNF)  {

        var vnfModelInfo = DataService.getModelInfo(COMPONENT.VNF);
        var serviceModelInfo = DataService.getModelInfo(COMPONENT.SERVICE);
        var portMirroringConfigFields = DataService.getPortMirroningConfigFields();
        var attuuid = DataService.getLoggedInUserId();
        var serviceInstanceId = DataService.getServiceInstanceId();
        var serviceModel = vidService.getModel().service;

        $log.debug("getModelInfo VNF", vnfModelInfo);
        $log.debug("getModelInfo SERVICE", serviceModelInfo);
        $log.debug("getPortMirroningConfigFields", portMirroringConfigFields);
        $log.debug("getLoggedInUserId", attuuid);
        // $log.debug("sourceInstance", $scope.sourceInstance);
        // $log.debug("collectorInstance", $scope.collectorInstance);
        $log.debug("getServiceInstanceId", serviceInstanceId);
        $log.debug("vidService.getModel()", serviceModel);
        $log.debug("vidService.getInstance()", vidService.getInstance());

        MsoService.createConfigurationInstance(
            vnfModelInfo,
            portMirroringConfigFields, attuuid,
            serviceModelInfo, serviceInstanceId
        )
            .then(function (response) {
                successCallbackFunction(response);
            })
            .catch(function (error) {
                errorCallbackFunction(error);
            });
    } else if (msoType === COMPONENT.MSO_CHANGE_STATUS_REQ &&
        componentId === COMPONENT.CONFIGURATION) {

        MsoService.toggleConfigurationStatus(
            DataService.getServiceInstanceId(),
            DataService.getConfigurationInstanceId(),
            DataService.getConfigurationStatus(),
            DataService.getModelInfo(COMPONENT.SERVICE),
            DataService.getModelInfo(COMPONENT.CONFIGURATION)
        ).then(function (response) {
                successCallbackFunction(response);
            })
            .catch(function (error) {
                errorCallbackFunction(error);
            });
    } else if (msoType === COMPONENT.MSO_CHANGE_STATUS_REQ &&
        componentId === COMPONENT.PORT) {

        MsoService.togglePortStatus(
            DataService.getServiceInstanceId(),
            DataService.getConfigurationInstanceId(),
            DataService.getPortId(),
            DataService.getPortStatus(),
            DataService.getModelInfo(COMPONENT.SERVICE),
            DataService.getModelInfo(COMPONENT.CONFIGURATION)
        ).then(function (response) {
                successCallbackFunction(response);
            })
            .catch(function (error) {
                errorCallbackFunction(error);
            });
    }

    var successCallbackFunction = function(response) {
        try {
            updateViewAfterInitialResponse(response);
            _this.timer = $timeout(getRequestStatus, PropertyService
                .getMsoMaxPollingIntervalMsec());

            $scope.instanceId = response.data.entity.instanceId;
            if ($scope.instanceId == null) {
                $scope.instanceId = response.data.entity.requestReferences.instanceId;
            }
        } catch (error) {
            if ( response.data != null && response.data.status != null ) {
                if (response.data.status > 299 || response.data.status < 200 ) {
                    // MSO returned an error
                    _this.isMsoError = true;
                }
            }
            MsoService.showResponseContentError(error, showError);
        }
    };

    var errorCallbackFunction = function (error) {
        UtilityService.setHttpErrorHandler(function(error) {
            _this.isMsoError = true;
            showError(FIELD.ERROR.SYSTEM_FAILURE, UtilityService
                .getHttpErrorMessage(error));
        });
    };

    $scope.close = function() {
        $uibModalInstance.dismiss('cancel');
        if (msoType === COMPONENT.MSO_CREATE_REQ) {
            $window.history.go(-2);
        }
    };
};

appDS2.controller("msoCommitModalController", [ "COMPONENT", "FIELD", "$scope", "$http", "$timeout",
		"$window", "$log", "MsoService", "PropertyService", "UtilityService", "DataService", "$uibModalInstance", "msoType", "componentId", "vidService",
    msoCommitModalController ]);
