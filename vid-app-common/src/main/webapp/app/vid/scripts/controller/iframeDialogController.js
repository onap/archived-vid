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

var iframeDialogController = function (COMPONENT, FIELD, PARAMETER, $scope, $http, $timeout, $log,
                                         CreationService, UtilityService, DataService, $routeParams) {

    $scope.isDialogVisible = false;

    function receiveMessage(event) {
        window.removeEventListener("message", receiveMessage, false);
        $scope.isDialogVisible = false;
        $scope.popup.isVisible = false;

        if (event.data.eventId == 'submitIframe') {
            CreationService.initializeComponent(COMPONENT.VNF);
            var instanceId = $routeParams.serviceInstanceId;
            DataService.setServiceInstanceId(instanceId);
            console.log(event.data.data);

            $scope.$broadcast(COMPONENT.MSO_CREATE_REQ, {
                url: CreationService.getMsoUrl(),
                requestDetails: event.data.data,
                componentId: COMPONENT.VNF,
                callbackFunction: function (response) {
                    // if (response.isSuccessful) {
                    //     $scope.popup.shouldShowIframePopup = false;
                    // } else {
                    $scope.isDialogVisible = false;
                    $scope.popup.isVisible = false;
                    // }
                }
            });
        }
        $scope.$apply();
    }


    $scope.$on(COMPONENT.IFRAME_DIALOG, function (event, data) {
        var queryString = Object.keys(data).map(function(key) {
            return key + '=' + data[key]
        }).join('&');
        $scope.url = COMPONENT.SUB_INTERFACE_POPUP_IFRAME_URL + queryString;
        $scope.isDialogVisible = true;
        $scope.popup.isVisible = true;

        window.addEventListener("message", receiveMessage, false);
    });
};

appDS2.controller("iframeDialogController", ["COMPONENT", "FIELD", "PARAMETER", "$scope", "$http",
    "$timeout", "$log", "CreationService", "UtilityService", "DataService", "$routeParams",
    iframeDialogController]);
