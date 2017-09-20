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
	}
	
	
}

app
		.controller("previousVersionContoller", [ "COMPONENT", "FIELD", "$scope", "$http",
				"$timeout", "$log", "CreationService", "UtilityService", "DataService","VIDCONFIGURATION",
				previousVersionContoller ]);
