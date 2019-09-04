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

appDS2.requires.push('ngRoute');
appDS2.requires.push('ui.tree');

appDS2.config(function($routeProvider) {
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

appDS2.config(function(treeConfig) {
	  treeConfig.defaultCollapsed = true; // collapse nodes by default
	});

appDS2.factory("user",function(){
    return {};
});

appDS2.controller("aaiSubscriberSearchController", [ "$scope", "$timeout", "$log", "UtilityService", "user", "PropertyService", "COMPONENT", "FIELD",
    function($scope, $timeout, $log, UtilityService, user, PropertyService, COMPONENT, FIELD) {

		$scope.baseUrl = "";
		$scope.responseTimeoutMsec = 10000;
		$scope.msoMaxPollingIntervalMsec = 1000;
		$scope.msoMaxPolls = 7;
		
		$scope.init = function(properties) {
    		var msecs = PropertyService.retrieveMsoMaxPollingIntervalMsec();
    		PropertyService.setMsoMaxPollingIntervalMsec(msecs);
    		
    		var polls = PropertyService.retrieveMsoMaxPolls();
    		PropertyService.setMsoMaxPolls(polls);
    		
    		// These two properties only added for testing
			properties.msoDefaultBaseUrl = $scope.baseUrl;
			properties.responseTimeoutMsec = $scope.responseTimeoutMsec;

			UtilityService.setProperties(properties);
		};
		
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
		};
		
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
		};
		
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
		};
		
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
		};
		
		$scope.autoStartQueryTest = function() {
			/*
			 * Optionally comment in / out one of these method calls (or add a similar
			 * entry) to auto-invoke an entry when the test screen is redrawn.
			 */
			$timeout(function() {
				// $scope.queryServiceInstance();
			}, 100);
		};
		
		$scope.queryServiceInstance = function() {
			/*
			 * Example of method call needed to query a service instance.
			 */
			$scope.$broadcast(COMPONENT.QUERY_SERVICE_INSTANCE, {
				serviceInstanceId: COMPONENT.SERVICE_INSTANCE_ID_1
			});
		};
	
		$scope.getSubscribers = function() {
			/*
			 * Example of method call needed to commit an instance creation request.
			 */
			$scope.$broadcast(COMPONENT.GET_SUBS, {
				url : FIELD.ID.AAI_GET_SUBSCRIBERS,
				requestDetails : createServiceRequestDetails
			});
		};
		
		$scope.getSubDetails = function() {
			/*
			 * Example of method call needed to commit an instance creation request.
			 */
			$scope.$broadcast(COMPONENT.GET_SUB_DETAILS, {
				url : FIELD.ID.AAI_SUB_DETAILS,
				requestDetails : createServiceRequestDetails
			});
		};
		
		$scope.getComponentList = function() {
			/*
			 * Example of method call needed to commit an instance creation request.
			 */
			$scope.$broadcast(COMPONENT.GET_COMPONENT_LIST, {
				url : FIELD.ID.AAI_SUB_VIEWEDIT,
				requestDetails : createServiceRequestDetails
			});
		};
		
		
		$scope.refreshSubscribers = function() {
			/*
			 * Example of method call needed to commit an instance creation request.
			 */
			$scope.$broadcast(COMPONENT.GET_SUBS, {
				url : FIELD.ID.AAI_REFRESH_SUBSCRIBERS,
				requestDetails : createServiceRequestDetails
			});
		};
	
		$scope.deleteServiceInstance = function() {
			/*
			 * Example of method call needed to commit an instance deletion request.
			 */
			$scope.$broadcast(COMPONENT.MSO_DELETE_REQ,	{
				url : COMPONENT.MSO_DELETE_SVC_INSTANCE_PATH + COMPONENT.SERVICE_INSTANCE_ID_1,
				requestDetails : deleteServiceRequestDetails
			});
		};
	
		$scope.createNetworkInstance = function() {
			$scope.$broadcast(COMPONENT.MSO_CREATE_REQ, {
				url : COMPONENT.MSO_CREATE_NW_INSTANCE,
				requestDetails : createNetworkRequestDetails
			});
		};
	
		$scope.deleteNetworkInstance = function() {
			$scope.$broadcast(COMPONENT.MSO_DELETE_REQ,	{
				url : COMPONENT.MSO_CREATE_NW_INSTANCE_PATH + COMPONENT.SERVICE_INSTANCE_ID_1 + COMPONENT.FORWARD_SLASH + COMPONENT.NETWORKS + COMPONENT.FORWARD_SLASH + COMPONENT.DELETE_INSTANCE_ID_1,
				requestDetails : deleteNetworkRequestDetails
			});
		};
	
		$scope.generateError = function(testName) {
			// Clone example request object
			var request = JSON.parse(JSON.stringify(createServiceRequestDetails));
			request.modelInfo.modelName = testName;
			$scope.$broadcast(COMPONENT.MSO_CREATE_REQ, {
				url : COMPONENT.MSO_CREATE_SVC_INSTANCE,
				requestDetails : request
			});
		};
	
		$scope.generateInvalidUrl404 = function() {
			var properties = UtilityService.getProperties(properties);
			properties.msoDefaultBaseUrl = COMPONENT.INVALID_STRING;
			UtilityService.setProperties(properties);
			$scope.$broadcast(COMPONENT.REFRESH_PROPERTIES);
		
			$scope.$broadcast(COMPONENT.MSO_CREATE_REQ, {
				url : COMPONENT.MSO_CREATE_SVC_INSTANCE,
				requestDetails : createServiceRequestDetails
			});

			properties.msoDefaultBaseUrl = $scope.baseUrl;
			UtilityService.setProperties(properties);
			$scope.$broadcast(COMPONENT.REFRESH_PROPERTIES);				
		};
	
		$scope.generateInvalidUrl405 = function() {
			$scope.$broadcast(COMPONENT.MSO_CREATE_REQ, {
				url : COMPONENT.INVALID_STRING_MSO_CREATE_SVC_INSTANCE,
				requestDetails : createServiceRequestDetails
			});
		};
	
		/*
		 * Test data objects:
		 */
	
		var subscriberInfo = {
			globalSubscriberId : COMPONENT.GLOBAL_SUBSCRIBER_ID_1,
			subscriberName : COMPONENT.SUBSCRIBER_NAME_GED12
		};
	
		var createServiceRequestDetails = {
			modelInfo : {
				modelType : COMPONENT.SERVICE,
				modelId : COMPONENT.MODEL_ID_1,
				modelNameVersionId : COMPONENT.MODEL_NAME_VERSION_ID_1,
				modelName : COMPONENT.MODEL_NAME_WANBONDING,
				modelVersion : COMPONENT.MODEL_VERSION_1
			},
			subscriberInfo : subscriberInfo,
			requestParameters : {
				vpnId : COMPONENT.VPN_ID_1,
				productName : COMPONENT.PRODUCT_NAME_TRINITY,
				customerId : COMPONENT.CUSTOMER_ID_1
			}
		};
	
		var deleteServiceRequestDetails = {
			modelInfo : {
				modelType : COMPONENT.SERVICE,
				modelId : COMPONENT.MODEL_ID_1,
				modelNameVersionId : COMPONENT.MODEL_NAME_VERSION_ID_1,
				modelName : COMPONENT.MODEL_NAME_WANBONDING,
				modelVersion : COMPONENT.MODEL_VERSION_1
			}
		};
	
		var createNetworkRequestDetails = {
			modelInfo : {
				modelType : COMPONENT.NETWORK,
				modelId : COMPONENT.MODEL_ID_2,
				modelNameVersionId : COMPONENT.MODEL_NAME_VERSION_ID_2,
				modelName : COMPONENT.MODEL_NAME_VISBCOAMNETWORK,
				modelVersion : COMPONENT.MODEL_VERSION_1
			},
			relatedModelList : [
					{
						relatedModel : {
							instanceId : COMPONENT.INSTANCE_ID_1,
							modelInfo : {
								modelType : COMPONENT.SERVICE,
								modelId : COMPONENT.MODEL_ID_3,
								modelNameVersionId : COMPONENT.MODEL_NAME_VERSION_ID_3,
								modelName : COMPONENT.MODEL_NAME_IISBC,
								modelVersion : COMPONENT.MODEL_VERSION_1
							}
						}
					},
					{
						relatedModel : {
							instanceId : COMPONENT.INSTANCE_ID_2,
							modelInfo : {
								modelType : COMPONENT.VNF,
								modelId : COMPONENT.MODEL_ID_4,
								modelNameVersionId : COMPONENT.MODEL_NAME_VERSION_ID_4,
								modelName : COMPONENT.MODEL_NAME_VISBC,
								modelVersion : COMPONENT.MODEL_VERSION_1
							}
						}
					},
					{
						relatedModel : {
							instanceId : COMPONENT.INSTANCE_ID_3,
							modelInfo : {
								modelType : COMPONENT.VF_MODULE,
								modelId : COMPONENT.MODEL_ID_5,
								modelNameVersionId : COMPONENT.MODEL_NAME_VERSION_ID_5,
								modelName : COMPONENT.MODEL_NAME_VISBCRTPEXPANSIONMODULE,
								modelVersion : COMPONENT.MODEL_VERSION_1
							}
						}
					} ],
			subscriberInfo : subscriberInfo,
			requestParameters : {
				/*
				 * FYI: quotes around field names are needed due to embedded "-"
				 * characters
				 */
				"cidr-mask" : COMPONENT.CIDR_MASK_1,
				"gateway-address" : COMPONENT.GATEWAY_ADDRESS_1,
				"dhcp-enabled" : COMPONENT.TRUE
			}
		};
	
		var deleteNetworkRequestDetails = {
			modelInfo : {
				modelType : COMPONENT.NETWORK,
				modelId : COMPONENT.MODEL_ID_2,
				modelNameVersionId : COMPONENT.MODEL_NAME_VERSION_ID_2,
				modelName : COMPONENT.MODEL_NAME_VISBCOAMNETWORK,
				modelVersion : COMPONENT.MODEL_VERSION_1
			},
			relatedModelList : [
					{
						relatedModel : {
							instanceId : COMPONENT.INSTANCE_ID_1,
							modelInfo : {
								modelType : COMPONENT.SERVICE,
								modelId : COMPONENT.MODEL_ID_3,
								modelNameVersionId : COMPONENT.MODEL_NAME_VERSION_ID_3,
								modelName : COMPONENT.MODEL_NAME_IISBC,
								modelVersion : COMPONENT.MODEL_VERSION_1
							}
						}
					},
					{
						relatedModel : {
							instanceId : COMPONENT.INSTANCE_ID_2,
							modelInfo : {
								modelType : COMPONENT.VNF,
								modelId : COMPONENT.MODEL_ID_4,
								modelNameVersionId : COMPONENT.MODEL_NAME_VERSION_ID_4,
								modelName : COMPONENT.MODEL_NAME_VISBC,
								modelVersion : COMPONENT.MODEL_VERSION_1
							}
						}
					},
					{
						relatedModel : {
							instanceId : COMPONENT.INSTANCE_ID_3,
							modelInfo : {
								modelType : COMPONENT.VF_MODULE,
								modelId : COMPONENT.MODEL_ID_5,
								modelNameVersionId : COMPONENT.MODEL_NAME_VERSION_ID_5,
								modelName : COMPONENT.MODEL_NAME_VISBCRTPEXPANSIONMODULE,
								modelVersion : COMPONENT.MODEL_VERSION_1
							}
						}
					} ]
		};
		$scope.getSubscriberDet = function(selectedCustomer){
			window.location.href = COMPONENT.SUBDETAILS_SELECTEDSUBSCRIBER + selectedCustomer;
		};
	}
]);
