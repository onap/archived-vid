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

var detailsDialogController = function($scope, $http, $timeout, $log,
	MsoService, DetailsService, UtilityService, COMPONENT, FIELD) {

    $scope.isDialogVisible = false;
    $scope.summaryControl = {};
    $scope.detailsControl = {};

    $scope.$on(COMPONENT.SHOW_COMPONENT_DETAILS, function(event, request) {

	$scope.log = "";
	$scope.isSpinnerVisible = true;
	$scope.isErrorVisible = false;
	$scope.isDialogVisible = true;
	$scope.popup.isVisible = true;

	DetailsService.initializeComponent(request.componentId);

	$scope.componentName = DetailsService.getComponentDisplayName();

	$scope.summaryControl.setList(DetailsService.getSummaryList());

	$scope.detailsControl.setList(DetailsService.getDetailsList());

	UtilityService.setHttpErrorHandler(function(response) {
	    showError(FIELD.ERROR.SYSTEM_FAILURE, UtilityService
		    .getHttpErrorMessage(response));
	});

	MsoService.getOrchestrationRequests(
		DetailsService.getMsoFilterString(), handleGetResponse);
    });

    var handleGetResponse = function(response) {
	$scope.isSpinnerVisible = false;
	try {
	    $scope.log = MsoService
		    .getFormattedGetOrchestrationRequestsResponse(response);
	} catch (error) {
	    $scope.log = MsoService.getFormattedCommonResponse(response);
	    MsoService.showResponseContentError(error, showError);
	}
    };

    $scope.close = function() {
	$scope.isDialogVisible = false;
	$scope.popup.isVisible = false;
    };

    var showError = function(summary, details) {
	var message = summary;
	if (UtilityService.hasContents(details)) {
	    message += " (" + details + ")";
	}
	$scope.isSpinnerVisible = false;
	$scope.isErrorVisible = true;
	$scope.error = message;
    };
};

appDS2.controller("detailsDialogController", [ "$scope", "$http", "$timeout",
	"$log", "MsoService", "DetailsService", "UtilityService", "COMPONENT", "FIELD",
	detailsDialogController ]);
