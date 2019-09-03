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

var previousVersionContoller = function( COMPONENT, FIELD, $scope, $http, $timeout, $log,
		CreationService, UtilityService, DataService,VIDCONFIGURATION) {
	$scope.isTableDialogVisible = false;
	$scope.summaryControl = {};
	$scope.userProvidedControl = {};

	var callbackFunction = undefined;
	var componentId = undefined;

	$scope.$on("createTableComponent", function(event, request) {

		$scope.isTableSpinnerVisible = true; 
		$scope.isTableErrorVisible = false;
		$scope.isTableDialogVisible = true;
		$scope.popup.isTablePopUpVisible = true;
		componentId = request.componentId;
		CreationService.initializeComponent(request.componentId);
		callbackFunction = request.callbackFunction;
		CreationService.setHttpErrorHandler(function(response) {
			showError("System failure", UtilityService
					.getHttpErrorMessage(response));
		});
		$scope.isTableSpinnerVisible = false;
	});
	
	
	$scope.cancelTable = function(){
		$scope.isTableDialogVisible = false;
		$scope.popup.isTablePopUpVisible = false;
	};	
};

app
		.controller("previousVersionContoller", [ "COMPONENT", "FIELD", "$scope", "$http",
				"$timeout", "$log", "CreationService", "UtilityService", "DataService","VIDCONFIGURATION",
				previousVersionContoller ]);
