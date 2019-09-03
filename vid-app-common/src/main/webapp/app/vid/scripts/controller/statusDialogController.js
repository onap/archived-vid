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

var statusDialogController = function(COMPONENT, FIELD, $scope, $http, $timeout, $log, MsoService, StatusService, DataService, PropertyService, UtilityService) {

    $scope.isDialogVisible = false;
    $scope.summaryControl = {};
    $scope.detailsControl = {};
    $scope.userProvidedControl = {};
    
    var callbackFunction = undefined;
    var componentId = undefined;
    
    $scope.$on("ComponentStatus", function(event, request) {

	$scope.log = "";
	$scope.isSpinnerVisible = true;
	$scope.isErrorVisible = false;
	$scope.isSuccessVisible = false;
	$scope.isSubmitEnabled = false;
	$scope.isDataVisible = false;
	$scope.isDialogVisible = true;
	$scope.popup.isVisible = true;
	$scope.isCancelEnabled = true;
	$scope.success = "";
	$scope.error = "";

	$scope.vnfid = undefined;
	$scope.targetProvStatus = undefined;
	
	callbackFunction = request.callbackFunction;
	componentId = request.componentId;
	
	StatusService.initializeComponent(request.componentId);

	/*StatusService.setHttpErrorHandler(function(response) {
		showError("System failure", UtilityService
				.getHttpErrorMessage(response));
	});*/

	$scope.componentName = StatusService.getComponentDisplayName();

	$scope.summaryControl.setList(StatusService.getSummaryList());

	$scope.detailsControl.setList(StatusService.getVNFStatusList());

	StatusService.getParameters(handleGetParametersResponse);
	
	//UtilityService.setHttpErrorHandler(function(response) {
	//    showError("System failure", UtilityService
	//	    .getHttpErrorMessage(response));
	//});


    });
    
    var handleGetParametersResponse = function(parameters, dontshow) {
		$scope.summaryControl.setList(parameters.summaryList);
		$scope.userProvidedControl.setList(parameters.userProvidedList);

		$scope.isSpinnerVisible = false;
		if (dontshow)
		  $scope.isDataVisible = false;
		else
			$scope.isDataVisible = true;
		$scope.isSubmitEnabled = true;
	};

	$scope.userParameterChanged = function(id) {
		StatusService.updateUserParameterList(id, $scope.userProvidedControl);
	};

    /*$scope.submit = function() {

    	var requiredFields = $scope.userProvidedControl.getRequiredFields();
		if (requiredFields === "") {
			$scope.isErrorVisible = false;
		} else {
			showError("Missing data", requiredFields);
			return;
		}
		var paramList = $scope.userProvidedControl.getList();
		var targetprovstatus = "";
		
		if ( paramList != null ) {
			for (var i = 0; i < paramList.length; i++) {
				if (paramList[i].id === FIELD.ID.VNF_TARGETPROVSTATUS) {
					targetprovstatus = paramList[i].value;
					break;
				}
			}
		}
        
		$scope.vnfid = DataService.getVnfInstanceId();
		$scope.targetProvStatus = StatusService.getTargetProvParameterText(targetprovstatus);
		
		$scope.setVnfProvStatus($scope.vnfid, $scope.targetProvStatus);
	
	   // $scope.isDialogVisible = false;

	  //  $scope.popup.isVisible = false;
	
    }*/
    
    $scope.init = function() {
		
    	StatusService.updateVnfProvStatus = "";
		var msecs = PropertyService.retrieveMsoMaxPollingIntervalMsec();
		PropertyService.setMsoMaxPollingIntervalMsec(msecs);
		
		var polls = PropertyService.retrieveMsoMaxPolls();
		PropertyService.setMsoMaxPolls(polls);
		
		$scope.isSpinnerVisible = true;
	
		
		$scope.error = "";
		$scope.success = "";
		$scope.pollAttempts = 0;
		$scope.log = "";				
		$scope.isSubmitEnabled = false;
		$scope.isCancelEnabled = false;
		
	};
    
    $scope.handleInitialResponse = function(response) {
		try {
			$scope.isCancelEnabled = true;
			
			if (response.data.status < 200 || response.data.status > 202) {
				showError(FIELD.ERROR.AAI_ERROR, "");
				return;
			}
			else
			{
				DataService.setUpdatedVNFProvStatus($scope.targetProvStatus);
				$scope.detailsControl.setList(StatusService.getVNFStatusList());
				showSuccess(FIELD.STATUS.SUCCESS_VNF_PROV_STATUS, $scope.targetProvStatus);
				
			}
			$scope.status = FIELD.STATUS.DONE;
			$scope.isSpinnerVisible = false;
//			DataService.setServiceInstanceToCustomer($scope.serviceInstanceToCustomer);
		} catch (error) {
			$scope.showContentError(error);
		}
	};
    
	/* $scope.setVnfProvStatus = function(vnfId, targetProvStatus) {
		
		$scope.init();
		 $log
		    .debug("AaiService:setVnfProvStatus: vnf-id: "
			    + vnfId + " Target Prov_Status: " + targetProvStatus);
	    var url =  "aai_vnf_update/"
	    + vnfId + "/" + targetProvStatus + "?r=" + Math.random();

	    $http.get(url,
		    {
			timeout : PropertyService
				.getServerResponseTimeoutMsec()
		    }).then(function(response) {
		    	
		    	$scope.handleInitialResponse(response);
		    })["catch"]
		    (UtilityService.runHttpErrorHandler);
	   
	};*/
	

	$scope.cancel = function() {
		$scope.isDialogVisible = false;
		$scope.popup.isVisible = false;
		runCallback(false);
	};

	var runCallback = function(response) {
		if (angular.isFunction(callbackFunction)) {
			callbackFunction({
				isSuccessful : response.isSuccessful,
				control : $scope.userProvidedControl.getList(),
				instanceId : response.instanceId
			});
		}
	};
   
	var showSuccess = function(summary, details) {
		var message = summary;
		if (UtilityService.hasContents(details)) {
		    message += " (" + details + ")";
		}
		$scope.isSpinnerVisible = false;
		$scope.isSuccessVisible = true;
		$scope.success = message;
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

appDS2.controller("statusDialogController", [ "COMPONENT", "FIELD", "$scope", "$http", "$timeout",
	"$log", "MsoService", "StatusService", "DataService", "PropertyService", "UtilityService",
	statusDialogController ]);
