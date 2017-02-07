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

var creationDialogController = function( COMPONENT, FIELD, $scope, $http, $timeout, $log,
		CreationService, UtilityService) {

	$scope.isDialogVisible = false;
	$scope.summaryControl = {};
	$scope.userProvidedControl = {};

	var callbackFunction = undefined;
	var componentId = undefined;

	$scope.$on("createComponent", function(event, request) {

		$scope.isSpinnerVisible = true;
		$scope.isErrorVisible = false;
		$scope.isDataVisible = false;
		$scope.isConfirmEnabled = false;
		$scope.isDialogVisible = true;
		$scope.popup.isVisible = true;

		callbackFunction = request.callbackFunction;
		componentId = request.componentId;
		CreationService.initializeComponent(request.componentId);

		CreationService.setHttpErrorHandler(function(response) {
			showError("System failure", UtilityService
					.getHttpErrorMessage(response));
		});

		$scope.componentName = CreationService.getComponentDisplayName();

		CreationService.getParameters(handleGetParametersResponse);

	});

	var handleGetParametersResponse = function(parameters) {
		$scope.summaryControl.setList(parameters.summaryList);
		$scope.userProvidedControl.setList(parameters.userProvidedList);

		$scope.isSpinnerVisible = false;
		$scope.isDataVisible = true;
		$scope.isConfirmEnabled = true;
	};
	
	var validateInstanceName = function(iname) {
		var patt1 = /^([a-z])+([0-9a-z\-_]*)$/i;
		
		if ( iname == null ){
			return false;
		}
		if ( !iname.match(patt1) ) {
			return false;
		}
		return true;
	}

	$scope.userParameterChanged = function(id) {
		CreationService.updateUserParameterList(id, $scope.userProvidedControl);
	}

	$scope.confirm = function() {

		var requiredFields = $scope.userProvidedControl.getRequiredFields();
		if (requiredFields !== "") {
			showError("Missing data", requiredFields);
			return;
		}
		// validate the instance names for volumeGroup, vfModule, network
		
		if  ( componentId != COMPONENT.SERVICE ) {
			var paramList = $scope.userProvidedControl.getList();
			var instanceName = "";
			
			if ( paramList != null ) {
				for (var i = 0; i < paramList.length; i++) {
					if (paramList[i].id === FIELD.ID.INSTANCE_NAME) {
						instanceName = paramList[i].value;
						break;
					}
				}
			}
			var isValid = validateInstanceName (instanceName);
			if ( isValid ) {
				$scope.isErrorVisible = false;
			} else {
				showError("Invalid instance name: " + instanceName, 
						"The instance name must contain only alphanumeric or \"_-.\" characters, and must start with an alphabetic character");
				return;
			}
		}
		var requestDetails = CreationService
				.getMsoRequestDetails($scope.userProvidedControl.getList());

		$scope.isDialogVisible = false;

		$scope.$broadcast("createInstance", {
			url : CreationService.getMsoUrl(),
			requestDetails : requestDetails,
			callbackFunction : function(response) {
				if (response.isSuccessful) {
					$scope.popup.isVisible = false;
					runCallback(response);
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

	var runCallback = function(response) {
		if (angular.isFunction(callbackFunction)) {
			callbackFunction({
				isSuccessful : response.isSuccessful,
				control : $scope.userProvidedControl.getList(),
				instanceId : response.instanceId
			});
		}
	}

	var showError = function(summary, details) {
		var message = summary;
		if (UtilityService.hasContents(details)) {
			message += " (" + details + ")";
		}
		$scope.isSpinnerVisible = false;
		$scope.isErrorVisible = true;
		$scope.error = message;
	}
	
}

app
		.controller("creationDialogController", [ "COMPONENT", "FIELD", "$scope", "$http",
				"$timeout", "$log", "CreationService", "UtilityService",
				creationDialogController ]);
