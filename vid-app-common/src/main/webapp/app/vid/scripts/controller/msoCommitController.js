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

var msoCommitController = function(COMPONENT, FIELD, $scope, $http, $timeout, $window, $log, $uibModal,
		MsoService, PropertyService, UtilityService, TestEnvironmentsService, DataService) {

	$scope.isViewVisible = false;
	$scope.progressBarControl = {};
	$scope.popupWindowControl = {};
    var getRequestStatusFunc = getOrchestrationRequestStatus; //default
	var _this = this;

	$scope.showReportWindow = function() {
		let requestInfo = {};
		if(_this.requestId !== undefined) {
			requestInfo.requestId = _this.requestId;
		} else {
			requestInfo.requestId = null;
		}
		if($scope.service !== undefined) {
			requestInfo.serviceUuid = $scope.service.model.service.uuid;
		} else {
			requestInfo.serviceUuid = null;
		}

		const modalWindow = $uibModal.open({
			templateUrl: 'app/vid/scripts/modals/report-modal/report-modal.html',
			controller: 'reportModalInstanceController',
			controllerAs: 'vm',
			resolve: {
				errorMsg: function () {
					return $scope.log;
				},
				requestInfo: function () {
					return requestInfo;
				}
			}
		});

		$scope.isViewVisible = false;
		$scope.popup.isVisible = false;
	};

	$scope.$on("createInstance", function(event, request) {
		init(request, COMPONENT.MSO_CREATE_REQ, getOrchestrationRequestStatus );
		MsoService.createInstance(request, handleInitialResponse);
	});

	$scope.$on("deleteInstance", function(event, request) {
        init(request, COMPONENT.MSO_DELETE_REQ, getOrchestrationRequestStatus);
		MsoService.deleteInstance(request, handleInitialResponse);
	});

    $scope.$on(COMPONENT.MSO_CREATE_ENVIRONMENT, function(event, request) {
        init(request, COMPONENT.MSO_CREATE_ENVIRONMENT, getCloudResourcesRequestStatus);
        TestEnvironmentsService.createApplicationEnv(request).then(handleInitialResponse);
    });

    $scope.$on(COMPONENT.MSO_DEACTIVATE_ENVIRONMENT, function(event, request) {
        init(request, COMPONENT.MSO_DEACTIVATE_ENVIRONMENT, getCloudResourcesRequestStatus);
        TestEnvironmentsService.deactivateApplicationEnv(request).then(handleInitialResponse)
    });

    $scope.$on(COMPONENT.MSO_ACTIVATE_ENVIRONMENT, function(event, request) {
        init(request, COMPONENT.MSO_ACTIVATE_ENVIRONMENT, getCloudResourcesRequestStatus);
        TestEnvironmentsService.activateApplicationEnv(request).then(handleInitialResponse)
    });


    var init = function(request, msoRequestType, getStatusRequest ) {
    	getRequestStatusFunc = getStatusRequest;
		$scope.status = FIELD.STATUS.SUBMITTING_REQUEST;
		$scope.isSpinnerVisible = true;
		$scope.isProgressVisible = true;
		$scope.error = "";
		$scope.log = "";
		$scope.isCloseEnabled = false;
		$scope.isViewVisible = true;
		$scope.popup.isVisible = true;

		_this.pollAttempts = 0;
		_this.callbackFunction = request.callbackFunction;
		_this.componentId = request.componentId;
		_this.msoRequestType = msoRequestType;
		_this.isMsoError = false;

		if (angular.isFunction($scope.progressBarControl.reset)) {
			$scope.progressBarControl.reset();
		}
		$scope.percentProgress = 2; // Show "a little" progress

		UtilityService.setHttpErrorHandler(function(response) {
			$scope.isCloseEnabled = true;
			_this.isMsoError = true;
			showError(FIELD.ERROR.SYSTEM_FAILURE, UtilityService
					.getHttpErrorMessage(response));
		});
	};

	var handleInitialResponse = function(response) {
		try {
			updateViewAfterInitialResponse(response);
			
			_this.timer = $timeout(getRequestStatusFunc, PropertyService
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
	}

	function getOrchestrationRequestStatus() {
		MsoService.getOrchestrationRequest(_this.requestId, handleGetStatusResponse);
	}

  	function getCloudResourcesRequestStatus() {
        TestEnvironmentsService.getRequestStatus(_this.requestId, handleGetStatusResponse);
    }


    var handleGetStatusResponse = function(response) {
		try {
			if (isUpdateViewAfterGetResponseComplete(response)) {
				return;
			}
			//console.log ( "msoCommitController _this.pollAttempts=" + _this.pollAttempts + " max polls=" + PropertyService.getMsoMaxPolls());
			if (++_this.pollAttempts > PropertyService.getMsoMaxPolls()) {
				_this.isMsoError = true;
				showError(FIELD.ERROR.MAX_POLLS_EXCEEDED);
			} else {
				_this.timer = $timeout(getRequestStatusFunc, PropertyService
						.getMsoMaxPollingIntervalMsec());
			}
		} catch (error) {
			_this.isMsoError = true;
			MsoService.showResponseContentError(error, showError);
		}
	}

	var updateViewAfterInitialResponse = function(response) {
		$scope.isCloseEnabled = true;

		updateLog(response);

		_this.requestId = UtilityService.checkUndefined(FIELD.ID.REQUEST_ID,
				UtilityService.checkUndefined(FIELD.ID.REQUEST_REFERENCES,
						response.data.entity.requestReferences).requestId);

		$scope.percentProgress = 4; // Show "a little more" progress
		$scope.status = FIELD.STATUS.IN_PROGRESS;
	}

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
		var result2 = patt2.test(requestState)
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
	}

	var updateLog = function(response) {
		$scope.log = MsoService.getFormattedCommonResponse(response)
				+ $scope.log;
		UtilityService.checkUndefined("entity", response.data.entity);
		UtilityService.checkUndefined("status", response.data.status);
		MsoService.checkValidStatus(response);
	}
	var updateLogFinalResponse = function(response) {
		$scope.log = MsoService.getFormattedSingleGetOrchestrationRequestResponse(response)
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
				findNextPage(_this.componentId, _this.msoRequestType, _this.isMsoError);
			}
		} else {
			$scope.popup.isVisible = false;
		}
	}

	var showError = function(summary, details) {
		var message = summary;
		if (details === undefined) {

			message += " What do you expect to be written here";
			// message += " (" + details + ")";
		}else {
			message += " (" + details + ")";
		}
		$scope.isSpinnerVisible = false;
		$scope.isProgressVisible = false;
		$scope.error = message;
		$scope.status = FIELD.STATUS.ERROR;
	}
	
	var findNextPage = function ( cid, msoRequestType, isMsoError ) {
		//console.log ("window.location.href" + window.location.href);
		//console.log ("component id " + cid);
		var originalUrl = window.location.href;
		if  ( (cid === COMPONENT.SERVICE) && (msoRequestType === COMPONENT.MSO_CREATE_REQ) ) {
			var url = originalUrl;
			var x;
			var count = 0;
			var firstStr = "";
			for ( x = 0; x < url.length; x++) {
				if ( url.charAt(x) == '/' ) {
					count++;
					if ( count == 4 ) {
						firstStr = url.substring (0,x);
						break;
					}
				}
			}
			// By default show the search existing service instances screen
			//var newUrl = firstStr + COMPONENT.SERVICEMODELS_INSTANCES_SERVICES_PATH;
			var  newUrl = COMPONENT.SERVICEMODELS_INSTANCES_SERVICES_PATH;
			
			if ( isMsoError ) {
				//Show the Browse SDC models screen
				//newUrl = firstStr + COMPONENT.SERVICEMODELS_MODELS_SERVICES_PATH;
				newUrl = COMPONENT.SERVICEMODELS_MODELS_SERVICES_PATH;
			}
			window.location.href = newUrl;
		}	
	}
}

appDS2.controller("msoCommitController", [ "COMPONENT", "FIELD", "$scope", "$http", "$timeout",
		"$window", "$log", "$uibModal", "MsoService", "PropertyService", "UtilityService", "TestEnvironmentsService", "DataService",
		msoCommitController ]);
