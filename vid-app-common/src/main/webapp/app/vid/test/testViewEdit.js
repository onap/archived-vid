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

app.config(function($logProvider) {
    // Optionally set to "false" to disable debug logging.
    $logProvider.debugEnabled(true);
});

var testViewEditController = function(COMPONENT, DataService, PropertyService,
	UtilityService, $scope, $timeout, $cookieStore, $log) {

    $scope.popup = new Object();
    $scope.isTestMode = false;

    $scope.init = function(properties) {

	/*
	 * These 2 statements should be included in non-test code.
	 */
	PropertyService
		.setMsoMaxPollingIntervalMsec(properties.msoMaxPollingIntervalMsec);
	PropertyService.setMsoMaxPolls(properties.msoMaxPolls);

	/*
	 * Common parameters that shows an example of how the view edit screen
	 * is expected to pass some common service instance values to the
	 * popups.
	 */

	DataService.setSubscriberName("Mobility");
	DataService.setGlobalCustomerId("CUSTID12345");
	DataService.setServiceType("Mobility Type 1");
	DataService.setServiceInstanceName("Example Service Instance Name");
	DataService.setServiceName("Mobility Service 1");
	DataService.setServiceInstanceId("mmsc-test-service-instance");
	DataService.setServiceUuid("XXXX-YYYY-ZZZZ");
	DataService.setUserServiceInstanceName("USER_SERVICE_INSTANCE_NAME");

	/*
	 * "setTestMode" is only used for testing.
	 */

	setTestMode();

    };

    $scope.autoStartTest = function() {
	/*
	 * Optionally comment in / out one of these method calls (or add a
	 * similar entry) to auto-invoke an entry as soon as the page is
	 * refreshed.
	 */
	$timeout(function() {
	    // $scope.showServiceDetails();
	    // $scope.showVnfDetails();
	    // $scope.createService();
	    // $scope.deleteService();
	    // $scope.createNetwork();
	    // $scope.createVnf();
	    // $scope.createVfModule();
	    // $scope.deleteVnf();
	    // $scope.createVfModule();
	}, 500);
    };

    /*
     * This block of code is only used for testing.
     */

    /*
     * The first 3 functions override default values set in the server
     * properties file.
     * 
     * 1) The URL for the MSO controller is set to either the "real" controller
     * or the test version depending on the "Use test MSO controller" checkbox.
     * 
     * 2) SDC and AAI are set to use test controller versions.
     * 
     * 3) Maximum polling and timeout values are set to lower values to lessen
     * the time required to run tests.
     * 
     */

    var TEST_MODE_COOKIE = "isTestModeEnabled";

    var defaultMsoBaseUrl = PropertyService.getMsoBaseUrl();

    var setTestMode = function() {
	setTestMsoMode($cookieStore.get(TEST_MODE_COOKIE));
	PropertyService.setAaiBaseUrl("testaai");
	PropertyService.setAsdcBaseUrl("testasdc");
	PropertyService.setMsoMaxPollingIntervalMsec(1000);
	PropertyService.setMsoMaxPolls(7);
    };

    $scope.testMsoModeChanged = function() {
	setTestMsoMode($scope.isTestMsoMode);
    };

    var setTestMsoMode = function(isEnabled) {
	$scope.isTestMsoMode = isEnabled;
	$cookieStore.put(TEST_MODE_COOKIE, isEnabled);
	if (isEnabled) {
	    PropertyService.setMsoBaseUrl("testmso");
	} else {
	    PropertyService.setMsoBaseUrl(defaultMsoBaseUrl);
	}
    };

    var callbackFunction = function(response) {
	$scope.callbackResults = "";
	var color = "none";
	$scope.callbackStyle = {
	    "background-color" : color
	};
	/*
	 * This 1/2 delay was only added to visually highlight the status
	 * change. Probably not needed in the real application code.
	 */
	$timeout(function() {
	    $scope.callbackResults = UtilityService.getCurrentTime()
		    + " isSuccessful: " + response.isSuccessful;
	    if (response.isSuccessful) {
		color = "#8F8";
	    } else {
		color = "#F88";
	    }
	    $scope.callbackStyle = {
		"background-color" : color
	    };
	}, 500);
    };

    /*
     * End of block of test-specific code
     */

    /*
     * Create functions
     */
    $scope.createService = function() {

	DataService.setModelId("91238134091820938018230918230989");

	$scope.$broadcast("createComponent", {
	    componentId : COMPONENT.SERVICE,
	    callbackFunction : callbackFunction
	});
    };

    $scope.createVnf = function() {

	DataService.setModelId("91238134091820938018230918230989");
	DataService.setModelInstanceName("VNF_MODEL_INSTANCE_NAME");

	DataService.setCloudRegionTenantList(exampleCloudRegionTenantList);
	DataService.setServiceIdList(exampleServiceIdList);

	// Data used to create MSO "relatedInstanceList" object

	DataService.setModelInfo(COMPONENT.SERVICE, exampleServiceModelInfo);

	$scope.$broadcast("createComponent", {
	    componentId : COMPONENT.VNF,
	    callbackFunction : callbackFunction
	});
    };

    $scope.createVfModule = function() {
	DataService
		.setInventoryItem(exampleAaiResult["inventory-response-items"][0]);

	DataService.setModelId("91238134091820938018230918230989");
	DataService.setModelInstanceName("VF_MODULE_MODEL_INSTANCE_NAME");

	DataService.setLcpRegion("Region2");
	DataService.setTenant("Tenant2");
	// Data used to create MSO "relatedInstanceList" object

	DataService.setModelInfo(COMPONENT.SERVICE, exampleServiceModelInfo);

	DataService.setVnfInstanceId("VNF_INSTANCE_ID_12345");
	DataService.setModelInfo(COMPONENT.VNF, exampleVnfModelInfo);

	DataService.setVolumeGroupInstanceId("VOLUME_GROUP_INSTANCE_ID_12345");
	DataService
		.setAvailableVolumeGroupList(exampleAvailableVolumeGroupList);
	DataService.setModelInfo(COMPONENT.VOLUME_GROUP,
		exampleVolumeGroupModelInfo);

	$scope.$broadcast("createComponent", {
	    componentId : COMPONENT.VF_MODULE,
	    callbackFunction : callbackFunction
	});
    };

    $scope.createVolumeGroup = function() {

	DataService.setModelId("91238134091820938018230918230989");
	DataService.setModelInstanceName("VOLUME_GROUP_MODEL_INSTANCE_NAME");
	DataService.setLcpRegion("Region1");
	DataService.setTenant("Tenant1");
	// Data used to create MSO "relatedInstanceList" object

	DataService.setModelInfo(COMPONENT.SERVICE, exampleServiceModelInfo);

	DataService.setVnfInstanceId("VNF_INSTANCE_ID_12345");
	DataService.setModelInfo(COMPONENT.VNF, exampleVnfModelInfo);

	$scope.$broadcast("createComponent", {
	    componentId : COMPONENT.VOLUME_GROUP,
	    callbackFunction : callbackFunction
	});
    };

    $scope.createNetwork = function() {

	DataService.setModelId("91238134091820938018230918230989");
	DataService.setModelInstanceName("NETWORK_MODEL_INSTANCE_NAME");

	DataService.setCloudRegionTenantList(exampleCloudRegionTenantList)
	DataService.setServiceIdList(exampleServiceIdList);

	// Data used to create MSO "relatedInstanceList" object

	DataService.setModelInfo(COMPONENT.SERVICE, exampleServiceModelInfo);

	$scope.$broadcast("createComponent", {
	    componentId : COMPONENT.NETWORK,
	    callbackFunction : callbackFunction
	});
    };

    /*
     * Delete functions
     */
    $scope.deleteService = function() {

	DataService.setInventoryItem(exampleServiceItem);

	DataService.setModelInfo(COMPONENT.SERVICE, exampleServiceModelInfo);

	$scope.$broadcast("deleteComponent", {
	    componentId : COMPONENT.SERVICE,
	    callbackFunction : callbackFunction
	});
    };

    $scope.deleteVnf = function() {

	DataService
		.setInventoryItem(exampleAaiResult["inventory-response-items"]["inventory-response-item"][0]);

	DataService.setVnfInstanceId("VNF_INSTANCE_ID_12345");
	DataService.setModelInfo(COMPONENT.VNF, exampleVnfModelInfo);
	DataService.setLcpRegion("Region3");
	DataService.setTenant("Tenant3");

	$scope.$broadcast("deleteComponent", {
	    componentId : COMPONENT.VNF,
	    callbackFunction : callbackFunction
	});
    };

    $scope.deleteVfModule = function() {

	DataService.setInventoryItem(exampleVfModuleItem);

	DataService.setVnfInstanceId("VNF_INSTANCE_ID_12345");

	DataService.setVfModuleInstanceId("VF_MODULE_INSTANCE_ID_12345");
	DataService.setModelInfo(COMPONENT.VF_MODULE, exampleVfModuleModelInfo);
	DataService.setLcpRegion("Region4");
	DataService.setTenant("Tenant4");

	$scope.$broadcast("deleteComponent", {
	    componentId : COMPONENT.VF_MODULE,
	    callbackFunction : callbackFunction
	});
    };

    $scope.deleteVolumeGroup = function() {

	DataService.setInventoryItem(exampleVolumeGroupItem);

	DataService.setVolumeGroupInstanceId("VOLUME_GROUP_INSTANCE_ID_12345");
	DataService.setModelInfo(COMPONENT.VOLUME_GROUP,
		exampleVolumeGroupModelInfo);
	DataService.setLcpRegion("Region3");
	DataService.setTenant("Tenant3");


	$scope.$broadcast("deleteComponent", {
	    componentId : COMPONENT.VOLUME_GROUP,
	    callbackFunction : callbackFunction
	});
    };

    $scope.deleteNetwork = function() {

	DataService.setInventoryItem(exampleNetworkItem);

	DataService.setNetworkInstanceId("NETWORK_INSTANCE_ID_12345");
	DataService.setModelInfo(COMPONENT.NETWORK, exampleNetworkModelInfo);
	DataService.setLcpRegion("Region5");
	DataService.setTenant("Tenant5");

	$scope.$broadcast("deleteComponent", {
	    componentId : COMPONENT.NETWORK,
	    callbackFunction : callbackFunction
	});
    };

    /*
     * Show Details functions
     */
    $scope.showServiceDetails = function() {

	DataService.setInventoryItem(exampleServiceItem);

	$scope.$broadcast("showComponentDetails", {
	    componentId : COMPONENT.SERVICE,
	    callbackFunction : callbackFunction
	});
    };

    $scope.showVnfDetails = function() {

	DataService.setVnfInstanceId("VNF_INSTANCE_ID_12345");
	DataService
		.setInventoryItem(exampleAaiResult["inventory-response-items"]["inventory-response-item"][0]);

	$scope.$broadcast("showComponentDetails", {
	    componentId : COMPONENT.VNF,
	    callbackFunction : callbackFunction
	});
    };

    $scope.showVfModuleDetails = function() {

	DataService.setVnfInstanceId("VNF_INSTANCE_ID_12345");
	DataService.setVfModuleInstanceId("VF_MODULE_INSTANCE_ID_12345");
	DataService.setInventoryItem(exampleVfModuleItem);

	$scope.$broadcast("showComponentDetails", {
	    componentId : COMPONENT.VF_MODULE,
	    callbackFunction : callbackFunction
	});
    };

    $scope.showVolumeGroupDetails = function() {

	DataService.setVolumeGroupInstanceId("VOLUME_GROUP_INSTANCE_ID_12345");
	DataService.setInventoryItem(exampleVolumeGroupItem);

	$scope.$broadcast("showComponentDetails", {
	    componentId : COMPONENT.VOLUME_GROUP,
	    callbackFunction : callbackFunction
	});
    };

    $scope.showNetworkDetails = function() {

	DataService.setNetworkInstanceId("NETWORK_INSTANCE_ID_12345");
	DataService.setInventoryItem(exampleNetworkItem);

	$scope.$broadcast("showComponentDetails", {
	    componentId : COMPONENT.NETWORK,
	    callbackFunction : callbackFunction
	});
    };
};

app.controller("testViewEditController", [ "COMPONENT", "DataService",
	"PropertyService", "UtilityService", "$scope", "$timeout",
	"$cookieStore", "$log", testViewEditController ]);
