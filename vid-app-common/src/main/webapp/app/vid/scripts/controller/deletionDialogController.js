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

var deletionDialogController = function( COMPONENT, FIELD, $scope, $http, $timeout, $log,
	DeletionService, UtilityService) {

    $scope.isDialogVisible = false;
    $scope.summaryControl = {};
    $scope.userProvidedControl = {};
    
    var callbackFunction = undefined;
    var componentId = undefined;

    $scope.$on(COMPONENT.DELETE_COMPONENT, function(event, request) {

    $scope.isDataVisible = false;
	$scope.isSpinnerVisible = false;
	$scope.isErrorVisible = false;
	$scope.isDialogVisible = true;
	$scope.popup.isVisible = true;
	$scope.isConfirmEnabled = false;

	callbackFunction = request.callbackFunction;
	componentId = request.componentId;

	DeletionService.initializeComponent(request.componentId);

	$scope.componentName = DeletionService.getComponentDisplayName();

	$scope.summaryControl.setList(DeletionService.getSummaryList());
	
	DeletionService.getParameters(handleGetParametersResponse);

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
		DeletionService.updateUserParameterList(id, $scope.userProvidedControl);
	}

    $scope.confirm = function() {

    	var requiredFields = $scope.userProvidedControl.getRequiredFields();
		if (requiredFields === "") {
			$scope.isErrorVisible = false;
		} else {
			showError(FIELD.ERROR.MISSING_DATA, requiredFields);
			return;
		}

		
	var requestDetails = DeletionService.getMsoRequestDetails($scope.userProvidedControl.getList());

	$scope.isDialogVisible = false;

	$scope.$broadcast(COMPONENT.MSO_DELETE_REQ, {
	    url : DeletionService.getMsoUrl(),
	    requestDetails : requestDetails,
		componentId: componentId,
	    callbackFunction : function(isSuccessful) {
		if (isSuccessful) {
		    $scope.popup.isVisible = false;
		    runCallback(true);
		} else {
		    $scope.isDialogVisible = true;
		}
	    }
	});

    }

    $scope.cancel = function() {
	$scope.isDialogVisible = false;
	$scope.popup.isVisible = false;
	runCallback(false);
    }

    var runCallback = function(isSuccessful) {
	if (angular.isFunction(callbackFunction)) {
	    callbackFunction({
		isSuccessful : isSuccessful
	    });
	}
    }
}

appDS2.controller("deletionDialogController", [ "COMPONENT", "FIELD", "$scope", "$http",
		"$timeout", "$log", "DeletionService", "UtilityService",
		deletionDialogController ]);
