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

app.requires.push('ngRoute');
app.requires.push('ui.tree');

app.config(function($routeProvider) {
	$routeProvider
	.when("/subviewedit", {
		templateUrl : "app/vid/scripts/view-models/aaiSubViewEdit.htm",
		controller : "aaiSubscriberSearchController"

	})
	.when("/subdetails", {
		templateUrl : "app/vid/scripts/view-models/aaiSubDetails.htm",
		controller : "aaiSubscriberController"

	})
	.otherwise({
		templateUrl : "app/vid/scripts/view-models/aaiGetSubs.htm", 
		controller : "aaiSubscriberSearchController"
	});
});

app.config(function(treeConfig) {
	  treeConfig.defaultCollapsed = true; // collapse nodes by default
	});

app.factory("user",function(){
    return {};
});

app.controller("aaiSubscriberSearchController", [ "$scope", "$timeout", "$log", "UtilityService", "user", "PropertyService",
    function($scope, $timeout, $log, UtilityService, user, PropertyService) {

		$scope.baseUrl = "";
		$scope.responseTimeoutMsec = 10000;
		$scope.msoMaxPollingIntervalMsec = 1000;
		$scope.msoMaxPolls = 7;
		
		$scope.init = function(properties) {
			// takes a default value, retrieves the prop value from the file system and sets it
    		var msecs = PropertyService.retrieveMsoMaxPollingIntervalMsec(1000);
    		PropertyService.setMsoMaxPollingIntervalMsec(msecs);
    		
    		// takes a default value, retrieves the prop value from the file system and sets it
    		var polls = PropertyService.retrieveMsoMaxPolls(7);
    		PropertyService.setMsoMaxPolls(polls);
    		
    		PropertyService.setServerResponseTimeoutMsec(10000);

			// These two properties only added for testing	
			properties.msoDefaultBaseUrl = $scope.baseUrl;
			properties.responseTimeoutMsec = $scope.responseTimeoutMsec;

			UtilityService.setProperties(properties);
		}
		
		$scope.autoGetSubs = function() {
			/*
			 * Optionally comment in / out one of these method calls (or add a similar
			 * entry) to auto-invoke an entry when the test screen is redrawn.
			 */
			$timeout(function() {
				$scope.getSubscribers();
				// $scope.deleteServiceInstance();
				// $scope.generateInvalidUrl405();			
			}, 100);
		}
		
		$scope.autoGetSubDetails = function() {
			/*
			 * Optionally comment in / out one of these method calls (or add a similar
			 * entry) to auto-invoke an entry when the test screen is redrawn.
			 */
			$timeout(function() {
				$scope.getSubDetails();
				// $scope.deleteServiceInstance();
				// $scope.generateInvalidUrl405();			
			}, 100);
		}
		
		$scope.autoPopulateViewEdit = function() {
			/*
			 * Optionally comment in / out one of these method calls (or add a similar
			 * entry) to auto-invoke an entry when the test screen is redrawn.
			 */
			$timeout(function() {
			$scope.getComponentList();
				// $scope.deleteServiceInstance();
				// $scope.generateInvalidUrl405();			
			}, 100);
		}
		
		$scope.refreshSubs = function() {
			/*
			 * Optionally comment in / out one of these method calls (or add a similar
			 * entry) to auto-invoke an entry when the test screen is redrawn.
			 */
			$timeout(function() {
				$scope.refreshSubscribers();
				// $scope.deleteServiceInstance();
				// $scope.generateInvalidUrl405();			
			}, 100);
		}
		
		$scope.autoStartQueryTest = function() {
			/*
			 * Optionally comment in / out one of these method calls (or add a similar
			 * entry) to auto-invoke an entry when the test screen is redrawn.
			 */
			$timeout(function() {
				// $scope.queryServiceInstance();
			}, 100);
		}
		
		$scope.queryServiceInstance = function() {
			/*
			 * Example of method call needed to query a service instance.
			 */
			$scope.$broadcast("queryServiceInstance", {
				serviceInstanceId: "bc305d54-75b4-431b-adb2-eb6b9e546014"
			});
		}
	
		$scope.getSubscribers = function() {
			/*
			 * Example of method call needed to commit an instance creation request.
			 */
			$scope.$broadcast("getSubs", {
				url : "aai_get_subscribers",
				requestDetails : createServiceRequestDetails
			});
		}
		
		$scope.getSubDetails = function() {
			/*
			 * Example of method call needed to commit an instance creation request.
			 */
			$scope.$broadcast("getSubDetails", {
				url : "aai_sub_details",
				requestDetails : createServiceRequestDetails
			});
		}
		
		$scope.getComponentList = function() {
			/*
			 * Example of method call needed to commit an instance creation request.
			 */
			$scope.$broadcast("getComponentList", {
				url : "aai_sub_viewedit",
				requestDetails : createServiceRequestDetails
			});
		}
		
		
		$scope.refreshSubscribers = function() {
			/*
			 * Example of method call needed to commit an instance creation request.
			 */
			$scope.$broadcast("getSubs", {
				url : "aai_refresh_subscribers",
				requestDetails : createServiceRequestDetails
			});
		}
	
		$scope.deleteServiceInstance = function() {
			/*
			 * Example of method call needed to commit an instance deletion request.
			 */
			$scope.$broadcast("deleteInstance",	{
				url : "mso_delete_svc_instance/bc305d54-75b4-431b-adb2-eb6b9e546014",
				requestDetails : deleteServiceRequestDetails
			});
		}
	
		$scope.createNetworkInstance = function() {
			$scope.$broadcast("createInstance", {
				url : "mso_create_nw_instance",
				requestDetails : createNetworkRequestDetails
			});
		}
	
		$scope.deleteNetworkInstance = function() {
			$scope.$broadcast("deleteInstance",	{
				url : "mso_delete_nw_instance/bc305d54-75b4-431b-adb2-eb6b9e546014/networks/ff305d54-75b4-ff1b-fff1-eb6b9e5460ff",
				requestDetails : deleteNetworkRequestDetails
			});
		}
	
		$scope.generateError = function(testName) {
			// Clone example request object
			var request = JSON.parse(JSON.stringify(createServiceRequestDetails));
			request.modelInfo.modelName = testName;
			$scope.$broadcast("createInstance", {
				url : "mso_create_svc_instance",
				requestDetails : request
			});
		}
	
		$scope.generateInvalidUrl404 = function() {
			var properties = UtilityService.getProperties(properties);
			properties.msoDefaultBaseUrl = "/INVALID_STRING/";
			UtilityService.setProperties(properties);
			$scope.$broadcast("refreshProperties");
		
			$scope.$broadcast("createInstance", {
				url : "mso_create_svc_instance",
				requestDetails : createServiceRequestDetails
			});

			properties.msoDefaultBaseUrl = $scope.baseUrl;
			UtilityService.setProperties(properties);
			$scope.$broadcast("refreshProperties");				
		}
	
		$scope.generateInvalidUrl405 = function() {
			$scope.$broadcast("createInstance", {
				url : "INVALID_STRING_mso_create_svc_instance",
				requestDetails : createServiceRequestDetails
			});
		}
	
		/*
		 * Test data objects:
		 */
	
		var subscriberInfo = {
			globalSubscriberId : "C12345",
			subscriberName : "General Electric Division 12"
		};
	
		var createServiceRequestDetails = {
			modelInfo : {
				modelType : "service",
				modelId : "sn5256d1-5a33-55df-13ab-12abad84e764",
				modelNameVersionId : "ab6478e4-ea33-3346-ac12-ab121484a333",
				modelName : "WanBonding",
				modelVersion : "1"
			},
			subscriberInfo : subscriberInfo,
			requestParameters : {
				vpnId : "1a2b3c4d5e6f",
				productName : "Trinity",
				customerId : "icore9883749"
			}
		};
	
		var deleteServiceRequestDetails = {
			modelInfo : {
				modelType : "service",
				modelId : "sn5256d1-5a33-55df-13ab-12abad84e764",
				modelNameVersionId : "ab6478e4-ea33-3346-ac12-ab121484a333",
				modelName : "WanBonding",
				modelVersion : "1"
			}
		};
	
		var createNetworkRequestDetails = {
			modelInfo : {
				modelType : "network",
				modelId : "ff5256d1-5a33-55df-aaaa-12abad84e7ff",
				modelNameVersionId : "fe6478e4-ea33-3346-aaaa-ab121484a3fe",
				modelName : "vIsbcOamNetwork",
				modelVersion : "1"
			},
			relatedModelList : [
					{
						relatedModel : {
							instanceId : "ff305d54-75b4-431b-adb2-eb6b9e5ff000",
							modelInfo : {
								modelType : "service",
								modelId : "ff3514e3-5a33-55df-13ab-12abad84e7ff",
								modelNameVersionId : "fe6985cd-ea33-3346-ac12-ab121484a3fe",
								modelName : "Intercarrier Interconnect Session Border Controller",
								modelVersion : "1"
							}
						}
					},
					{
						relatedModel : {
							instanceId : "ff305d54-75b4-ff1b-adb2-eb6b9e5460ff",
							modelInfo : {
								modelType : "vnf",
								modelId : "ff5256d1-5a33-55df-13ab-12abad84e7ff",
								modelNameVersionId : "fe6478e4-ea33-3346-ac12-ab121484a3fe",
								modelName : "vIsbc",
								modelVersion : "1"
							}
						}
					},
					{
						relatedModel : {
							instanceId : "ff305d54-75b4-ff1b-bdb2-eb6b9e5460ff",
							modelInfo : {
								modelType : "vfModule",
								modelId : "ff5256d1-5a33-55df-13ab-22abad84e7ff",
								modelNameVersionId : "fe6478e4-ea33-3346-bc12-ab121484a3fe",
								modelName : "vIsbcRtpExpansionModule",
								modelVersion : "1"
							}
						}
					} ],
			subscriberInfo : subscriberInfo,
			requestParameters : {
				/*
				 * FYI: quotes around field names are needed due to embedded "-"
				 * characters
				 */
				"cidr-mask" : "255.255.255.000",
				"gateway-address" : "10.10.125.1",
				"dhcp-enabled" : "true"
			}
		};
	
		var deleteNetworkRequestDetails = {
			modelInfo : {
				modelType : "network",
				modelId : "ff5256d1-5a33-55df-aaaa-12abad84e7ff",
				modelNameVersionId : "fe6478e4-ea33-3346-aaaa-ab121484a3fe",
				modelName : "vIsbcOamNetwork",
				modelVersion : "1"
			},
			relatedModelList : [
					{
						relatedModel : {
							instanceId : "ff305d54-75b4-431b-adb2-eb6b9e5ff000",
							modelInfo : {
								modelType : "service",
								modelId : "ff3514e3-5a33-55df-13ab-12abad84e7ff",
								modelNameVersionId : "fe6985cd-ea33-3346-ac12-ab121484a3fe",
								modelName : "Intercarrier Interconnect Session Border Controller",
								modelVersion : "1"
							}
						}
					},
					{
						relatedModel : {
							instanceId : "ff305d54-75b4-ff1b-adb2-eb6b9e5460ff",
							modelInfo : {
								modelType : "vnf",
								modelId : "ff5256d1-5a33-55df-13ab-12abad84e7ff",
								modelNameVersionId : "fe6478e4-ea33-3346-ac12-ab121484a3fe",
								modelName : "vIsbc",
								modelVersion : "1"
							}
						}
					},
					{
						relatedModel : {
							instanceId : "ff305d54-75b4-ff1b-bdb2-eb6b9e5460ff",
							modelInfo : {
								modelType : "vfModule",
								modelId : "ff5256d1-5a33-55df-13ab-22abad84e7ff",
								modelNameVersionId : "fe6478e4-ea33-3346-bc12-ab121484a3fe",
								modelName : "vIsbcRtpExpansionModule",
								modelVersion : "1"
							}
						}
					} ]
		};
		$scope.getSubscriberDet = function(selectedCustomer){
			window.location.href = '#subdetails?selectedSubscriber=' + selectedCustomer;
		};
	}
]);
