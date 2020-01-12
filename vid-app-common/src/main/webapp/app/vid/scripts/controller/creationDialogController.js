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

var creationDialogController = function (COMPONENT, FIELD, PARAMETER, $scope, $http, $timeout, $log,
                                         CreationService, UtilityService, DataService, VIDCONFIGURATION, $location, $uibModal, featureFlags) {

    $scope.isDialogVisible = false;
    $scope.isServiceError = false;
    $scope.summaryControl = {};
    $scope.userProvidedControl = {};


    var callbackFunction = undefined;
    var componentId = undefined;

    $scope.showReportWindow = function() {

        let errorMsg;

        if($scope.error !== undefined && $scope.error != null) {
            errorMsg = $scope.error;
        } else {
            errorMsg = "";
        }

        const modalWindow = $uibModal.open({
            templateUrl: 'app/vid/scripts/modals/report-modal/report-modal.html',
            controller: 'reportModalController',
            controllerAs: 'vm',
            resolve: {
                errorMsg: function () {
                    return errorMsg;
                }
            }
        });

        $scope.isDialogVisible = false;
        $scope.popup.isVisible = false;
    };

    $scope.isShowErrorReport = function() {
        return featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_CREATE_ERROR_REPORTS);
    };

    $scope.shouldShowOldPopup = function () {
        return !DataService.getShouldIncludeInAsyncInstantiationFlow();
    };

    function receiveMessage(event) {
        if (event.data == 'closeIframe') {
            window.removeEventListener("message", receiveMessage, false);

            $scope.cancel();
        }
        else if (event.data.eventId == 'submitIframe') {
            {
                $location.path('/servicePlanning').search({serviceModelId: event.data.data.serviceModelId});
            }
        } else if (event.data.eventId == 'showPreviousInstantiations') {
            {
                $location.path('/instantiationStatus').search({filterText: event.data.data.serviceModelId});
            }
        }
        $scope.$apply();
    }

    $scope.$on(COMPONENT.CREATE_COMPONENT, function (event, request) {
        $scope.isSpinnerVisible = true;
        $scope.isErrorVisible = false;
        $scope.isDataVisible = false;
        $scope.isConfirmEnabled = false;
        $scope.isDialogVisible = true;
        $scope.popup.isVisible = true;


        if (!$scope.shouldShowOldPopup()) {
            let modelNameVersionId = request.modelNameVersionId ?
                request.modelNameVersionId :
                (DataService.getModelInfo(COMPONENT.SERVICE) ? DataService.getModelInfo(COMPONENT.SERVICE).modelNameVersionId : "");
            if(DataService.getIsInstantiationTemplateExists()){
                $scope.url = COMPONENT.INSTANTIATION_TEMPLATES_IFRAME_URL + modelNameVersionId;
                window.addEventListener("message", receiveMessage, false);
            }else {
                $scope.url = COMPONENT.SERVICE_POPUP_IFRAME_URL + modelNameVersionId + "&isCreate=true&r=" + Math.random();
                window.addEventListener("message", receiveMessage, false);
            }
        }
        else {
            callbackFunction = request.callbackFunction;
            componentId = request.componentId;
            CreationService.initializeComponent(request.componentId);

            CreationService.setHttpErrorHandler(function (response) {
                $scope.isServiceError = true;
                showError(FIELD.ERROR.SYSTEM_FAILURE, UtilityService
                    .getHttpErrorMessage(response));
            });

            $scope.componentName = CreationService.getComponentDisplayName();

            CreationService.getParameters(handleGetParametersResponse);
        }

    });

    var handleGetParametersResponse = function (parameters) {
        $scope.summaryControl.setList(parameters.summaryList);
        $scope.userProvidedControl.setList(parameters.userProvidedList);

        $scope.isSpinnerVisible = false;
        $scope.isDataVisible = true;
        $scope.isConfirmEnabled = true;
    };

    var validateInstanceName = function (iname) {
        var patt1 = /^([a-z])+([0-9a-z\-_\.]*)$/i;

        if (iname == null) {
            return false;
        }
        if (!iname.match(patt1)) {
            return false;
        }
        return true;
    };
    var validateMap = function (mname) {
        var patt1 = /^{(\s*\w+\s*:\s*\w+\s*)(\s*,\s*\w+\s*:\s*\w+\s*)*}$/im;
        if (mname == null) {
            return true;
        }
        if (!mname.match(patt1)) {
            return false;
        }
        return true;
    };

    var validateList = function (lname) {
        var patt1 = /^\[(\s*\w+\s*)(\s*,\s*\w+\s*)*\]$/i;
        if (lname == null) {
            return true;
        }
        if (!lname.match(patt1)) {
            return false;
        }
        return true;
    };

    $scope.userParameterChanged = function (id) {
        CreationService.updateUserParameterList(id, $scope.userProvidedControl);
    };

    $scope.confirm = function () {

        var requiredFields = $scope.userProvidedControl.getRequiredFields();
        if (requiredFields !== "") {
            showError(FIELD.ERROR.MISSING_DATA, requiredFields);
            return;
        }

        var isUploadAvailable = false;
        var uploadIndex = 0;
        var paramList = $scope.userProvidedControl.getList();
        var isAnyError = false;
        for (var i = 0; i < paramList.length; i++) {
            if (paramList[i].id === FIELD.ID.SUPPLEMENTORY_DATA_FILE) {
                isUploadAvailable = true;
                uploadIndex = i;
            }
            if (paramList[i].id === FIELD.ID.UPLOAD_SUPPLEMENTORY_DATA_FILE && paramList[i].value && document.getElementById(FIELD.ID.SUPPLEMENTORY_DATA_FILE).value == '') {
                isAnyError = true;
            }
        }

        if (isUploadAvailable && isAnyError) {
            showError(FIELD.ERROR.MISSING_DATA, FIELD.ERROR.MISSING_FILE);

        } else if (isUploadAvailable && document.getElementById(FIELD.ID.SUPPLEMENTORY_DATA_FILE).value != '') {
            var errorMsg = "";
            var fileInput = document.getElementById(FIELD.ID.SUPPLEMENTORY_DATA_FILE);
            var file = fileInput.files[0];
            var reader = new FileReader();
            reader.onload = function (e) {
                try {
                    paramList[uploadIndex].value = JSON.parse(reader.result);
                    FIELD.PARAMETER.SUPPLEMENTORY_DATA_FILE['value'] = paramList[uploadIndex].value;

                    var instanceName = "";

                    if (DataService.getALaCarte()) {
                        if (paramList != null) {
                            for (var i = 0; i < paramList.length; i++) {
                                if (paramList[i].id === FIELD.ID.INSTANCE_NAME) {
                                    instanceName = paramList[i].value;
                                    break;
                                }
                            }
                        }
                        var isValid = validateInstanceName(instanceName);
                        if (isValid) {
                            $scope.isErrorVisible = false;
                        } else {
                            showError(FIELD.ERROR.INVALID_INSTANCE_NAME + instanceName,
                                FIELD.ERROR.INSTANCE_NAME_VALIDATE);
                            return;
                        }
                    }
                    var arbitraryParametersList = DataService.getArbitraryParameters();
                    var p = null;
                    if (UtilityService.hasContents(arbitraryParametersList)) {
                        for (var i = 0; i < arbitraryParametersList.length; i++) {
                            p = arbitraryParametersList[i];
                            if (p.type === PARAMETER.MAP) {
                                //validate a map: { <entry_key_1>: <entry_value_1>, ... , <entry_key_n>: <entry_value_n> }
                                // need to find the value in paramList
                                for (var j = 0; j < paramList.length; j++) {
                                    if (paramList[j].id === p.id) {
                                        p.value = paramList[j].value;
                                        var isValid = validateMap(p.value);
                                        if (isValid) {
                                            $scope.isErrorVisible = false;
                                            break;
                                        }
                                        else {
                                            showError(FIELD.ERROR.INVALID_MAP + p.id,
                                                FIELD.ERROR.MAP_VALIDATE);
                                            return;
                                        }
                                    }
                                }
                            } else if (p.type === PARAMETER.LIST) {
                                //validate a list: { value or a list of comma separated values }
                                // need to find the value in paramList
                                for (var j = 0; j < paramList.length; j++) {
                                    if (paramList[j].id === p.id) {
                                        p.value = paramList[j].value;
                                        var isValid = validateList(p.value);
                                        if (isValid) {
                                            $scope.isErrorVisible = false;
                                            break;
                                        }
                                        else {
                                            showError(FIELD.ERROR.INVALID_LIST + p.id,
                                                FIELD.ERROR.LIST_VALIDATE);
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    var requestDetails = CreationService
                        .getMsoRequestDetails($scope.userProvidedControl.getList());

                    $scope.isDialogVisible = false;

                    $scope.$broadcast(COMPONENT.MSO_CREATE_REQ, {
                        url: CreationService.getMsoUrl(),
                        requestDetails: requestDetails,
                        componentId: componentId,
                        callbackFunction: function (response) {
                            if (response.isSuccessful) {
                                $scope.popup.isVisible = false;
                                runCallback(response);
                            } else {
                                $scope.isDialogVisible = false;
                                $scope.popup.isVisible = false;
                            }
                        }
                    });

                } catch (e) {
                    errorMsg = errorMsg + FIELD.ERROR.INVALID_DATA_FORMAT;
                }
                if (errorMsg !== "") {
                    showError(FIELD.ERROR.SYSTEM_FAILURE, errorMsg);

                }
            };
            reader.readAsText(file);
        } else {

            var paramList = $scope.userProvidedControl.getList();
            var instanceName = "";

            if (DataService.getALaCarte()) {
                if (paramList != null) {
                    for (var i = 0; i < paramList.length; i++) {
                        if (paramList[i].id === FIELD.ID.INSTANCE_NAME) {
                            instanceName = paramList[i].value;
                            break;
                        }
                    }
                }
                var isValid = validateInstanceName(instanceName);
                if (isValid) {
                    $scope.isErrorVisible = false;
                } else {
                    showError(FIELD.ERROR.INVALID_INSTANCE_NAME + instanceName,
                        FIELD.ERROR.INSTANCE_NAME_VALIDATE);
                    return;
                }
            }
            var arbitraryParametersList = DataService.getArbitraryParameters();
            var p = null;
            if (UtilityService.hasContents(arbitraryParametersList)) {
                for (var i = 0; i < arbitraryParametersList.length; i++) {
                    p = arbitraryParametersList[i];
                    if (p.type === PARAMETER.MAP) {
                        //validate a map: { <entry_key_1>: <entry_value_1>, ... , <entry_key_n>: <entry_value_n> }
                        // need to find the value in paramList
                        for (var j = 0; j < paramList.length; j++) {
                            if (paramList[j].id === p.id) {
                                p.value = paramList[j].value;
                                var isValid = validateMap(p.value);
                                if (isValid) {
                                    $scope.isErrorVisible = false;
                                    break;
                                }
                                else {
                                    showError(FIELD.ERROR.INVALID_MAP + p.id,
                                        FIELD.ERROR.MAP_VALIDATE);
                                    return;
                                }
                            }
                        }
                    } else if (p.type === PARAMETER.LIST) {
                        //validate a list: { value or a list of comma separated values }
                        // need to find the value in paramList
                        for (var j = 0; j < paramList.length; j++) {
                            if (paramList[j].id === p.id) {
                                p.value = paramList[j].value;
                                var isValid = validateList(p.value);
                                if (isValid) {
                                    $scope.isErrorVisible = false;
                                    break;
                                }
                                else {
                                    showError(FIELD.ERROR.INVALID_LIST + p.id,
                                        FIELD.ERROR.LIST_VALIDATE);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            var requestDetails = CreationService
                .getMsoRequestDetails($scope.userProvidedControl.getList());

            $scope.isDialogVisible = false;

            $scope.$broadcast(COMPONENT.MSO_CREATE_REQ, {
                url: CreationService.getMsoUrl(),
                requestDetails: requestDetails,
                componentId: componentId,
                callbackFunction: function (response) {
                    if (response.isSuccessful) {
                        $scope.popup.isVisible = false;
                        runCallback(response);
                    } else {
                        $scope.isDialogVisible = false;
                        $scope.popup.isVisible = false;
                    }
                }
            });
        }
    };

    $scope.cancel = function () {
        $scope.isDialogVisible = false;
        $scope.popup.isVisible = false;
        runCallback(false);
    };


    var runCallback = function (response) {
        if (angular.isFunction(callbackFunction)) {
            callbackFunction({
                isSuccessful: response.isSuccessful,
                control: $scope.userProvidedControl.getList(),
                instanceId: response.instanceId
            });
        }
    };

    var showError = function (summary, details) {
        var message = summary;
        if (UtilityService.hasContents(details)) {
            message += " (" + details + ")";
        }
        $scope.isSpinnerVisible = false;
        $scope.isErrorVisible = true;
        $scope.error = message;
    }

};

appDS2.controller("creationDialogController", ["COMPONENT", "FIELD", "PARAMETER", "$scope", "$http",
    "$timeout", "$log", "CreationService", "UtilityService", "DataService", "VIDCONFIGURATION", "$location",
    "$uibModal", "featureFlags",
    creationDialogController]);
