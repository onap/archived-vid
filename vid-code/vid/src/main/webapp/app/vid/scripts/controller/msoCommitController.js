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

/*
 * "msoCommitController.js" provides controller code to commit MSO requests.
 * 
 * HIGHLIGHTS:
 * 
 * Parent HTML/JSP code is expected to include "msoCommit.htm" (via
 * "ng-include") and this file (via "<script>"). "msoCommit.jsp" (displayed
 * when the parent code includes "msoCommit.htm") renders a popup window, but
 * initially hides the display.
 * 
 * The parent controller code is expected to broadcast either the
 * "createInstance" or "deleteInstance" events when it is ready to commit the
 * code.
 * 
 * This controller receives these events (via "$scope.on" declarations), calls
 * "$scope.init()" to "unhide" and initialize the popup display and then makes a
 * REST request to the appropriate server Java controller.
 * 
 * The initial REST response is handled by "handleInitialResponse". This method
 * then polls the server (via "getRequestStatus") if the request is successful.
 * 
 * The subsequent "getRequestStatus" responses are handled by
 * "handleGetResponse".
 * 
 * "handleInitialResponse" and "handleGetResponse" primarily filter response
 * data, manipulate the display and provide error handling.
 * 
 * The mechanism has these dependencies (in addition to "msoCommit.htm"):
 * 
 * 1) Three services: MsoService, PropertyService and UtilityService
 * 
 * 2) The popup window directive found in "popupWindow.htm" and
 * "popupWindowDirective.js"
 * 
 * 2) Display styling defined in "dialogs.css"
 * 
 * CAVEATS:
 * 
 * The parent HTML is assumed to be the "popup-window" directive and the
 * corresponding parent controller is assumed to define the object
 * "$scope.popup".
 */

var msoCommitController = function($scope, $http, $timeout, $window, $log,
		MsoService, PropertyService, UtilityService, DataService) {

	$scope.isViewVisible = false;
	$scope.progressBarControl = {};
	$scope.popupWindowControl = {};

	var _this = this;

	$scope.$on("createInstance", function(event, request) {
		init(request);
		MsoService.createInstance(request, handleInitialResponse);
	});

	$scope.$on("deleteInstance", function(event, request) {
		init(request);
		MsoService.deleteInstance(request, handleInitialResponse);
	});

	var init = function(request) {
		$scope.status = "Submitting Request";
		$scope.isSpinnerVisible = true;
		$scope.isProgressVisible = true;
		$scope.error = "";
		$scope.log = "";
		$scope.isCloseEnabled = false;
		$scope.isViewVisible = true;
		$scope.popup.isVisible = true;

		_this.pollAttempts = 0;
		_this.callbackFunction = request.callbackFunction;

		if (angular.isFunction($scope.progressBarControl.reset)) {
			$scope.progressBarControl.reset();
		}
		$scope.percentProgress = 2; // Show "a little" progress

		UtilityService.setHttpErrorHandler(function(response) {
			$scope.isCloseEnabled = true;
			showError("System failure", UtilityService
					.getHttpErrorMessage(response));
		});
	}

	var handleInitialResponse = function(response) {
		try {
			updateViewAfterInitialResponse(response);
			_this.timer = $timeout(getRequestStatus, PropertyService
					.getMsoMaxPollingIntervalMsec());

			$scope.instanceId = response.data.entity.instanceId;
			if ($scope.instanceId == null) { 
				$scope.instanceId = response.data.entity.requestReferences.instanceId;
			}
		} catch (error) {
			MsoService.showResponseContentError(error, showError);
		}
	}

	var getRequestStatus = function() {
		MsoService.getOrchestrationRequest(_this.requestId, handleGetResponse);
	}

	var handleGetResponse = function(response) {
		try {
			if (isUpdateViewAfterGetResponseComplete(response)) {
				return;
			}
			console.log ( "msoCommitController _this.pollAttempts=" + _this.pollAttempts + " max polls=" + PropertyService.getMsoMaxPolls());
			if (++_this.pollAttempts > PropertyService.getMsoMaxPolls()) {
				showError("Maximum number of poll attempts exceeded");
			} else {
				_this.timer = $timeout(getRequestStatus, PropertyService
						.getMsoMaxPollingIntervalMsec());
			}
		} catch (error) {
			MsoService.showResponseContentError(error, showError);
		}
	}

	var updateViewAfterInitialResponse = function(response) {
		$scope.isCloseEnabled = true;

		updateLog(response);

		_this.requestId = UtilityService.checkUndefined("requestId",
				UtilityService.checkUndefined("requestReferences",
						response.data.entity.requestReferences).requestId);

		$scope.percentProgress = 4; // Show "a little more" progress
		$scope.status = "In Progress";
	}

	/*
	 * Updates the view and returns "true" if the MSO operation has returned a
	 * "Complete" status.
	 */
	var isUpdateViewAfterGetResponseComplete = function(response) {
		console.log("msoCommitController isUpdateViewAfterGetResponseComplete");
		updateLog(response);

		var requestStatus = UtilityService.checkUndefined("requestStatus",
				UtilityService.checkUndefined("request",
						response.data.entity.request).requestStatus);

		var requestState = requestStatus.requestState;
		console.log("msoCommitController requestState=" + requestState);
		// look for "progress" or "pending"
		var patt1 = /progress/i;
		var patt2 = /pending/i;
		var result1 = patt1.test(requestState);
		var result2 = patt2.test(requestState)
		if (result1 || result2) {
			requestState = "In Progress";
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

		if (requestState.toLowerCase() === "Failed".toLowerCase()) {
			throw {
				type : "msoFailure"
			};
		}

		if (requestState.toLowerCase() === "Complete".toLowerCase()) {
			$scope.isSpinnerVisible = false;
			return true;
		}

		return false;
	}

	var updateLog = function(response) {
		$scope.log = MsoService.getFormattedCommonResponse(response)
				+ $scope.log;
		UtilityService.checkUndefined("entity", response.data.entity);
		UtilityService.checkUndefined("status", response.data.status);
		MsoService.checkValidStatus(response);
	}

	$scope.close = function() {
		if (_this.timer !== undefined) {
			$timeout.cancel(_this.timer);
		}
		$scope.isViewVisible = false;
		if (angular.isFunction(_this.callbackFunction)) {
			if ($scope.error === "") {
				_this.callbackFunction({
					isSuccessful : true,
					instanceId : $scope.instanceId
				});
			} else {
				_this.callbackFunction({
					isSuccessful : false
				});
			}
		} else {
			$scope.popup.isVisible = false;
		}
	}

	var showError = function(summary, details) {
		var message = summary;
		if (UtilityService.hasContents(details)) {
			message += " (" + details + ")";
		}
		$scope.isSpinnerVisible = false;
		$scope.isProgressVisible = false;
		$scope.error = message;
		$scope.status = "Error";
	}
}

app.controller("msoCommitController", [ "$scope", "$http", "$timeout",
		"$window", "$log", "MsoService", "PropertyService", "UtilityService",
		msoCommitController ]);
