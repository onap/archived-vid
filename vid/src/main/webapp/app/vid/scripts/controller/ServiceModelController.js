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

(function () {
	'use strict';

	app.controller("ServiceModelController", function ($scope, $http, $location, COMPONENT, DataService, vidService,
			PropertyService) {

		$scope.popup = {};
		var re = /.*?:\/\/.*?:.*?\/(.*?)\//g;
		var baseEndpoint = re.exec($location.absUrl())[1];
		
		$scope.getServiceModels = function() {
			$scope.status = "Fetching service catalog from ASDC.  Please wait.";
			$http.get('/' + baseEndpoint + '/rest/models/services?distributionStatus=DISTRIBUTED')
			.then(function(response) {
				$scope.services = [];
				if (angular.isArray(response.data)) {
					$scope.services = response.data;
					$scope.viewPerPage=10;
					$scope.totalPage=$scope.services.length/$scope.viewPerPage;
					$scope.sortBy="name";
					$scope.scrollViewPerPage=2;
					$scope.currentPage=1;
					$scope.searchCategory;
					$scope.searchString="";
					$scope.currentPageNum=1;
					$scope.isSpinnerVisible = false;
					$scope.isProgressVisible = false;
				} else {
					$scope.status = "Failed to get service models from ASDC.";
					$scope.error = true;
					$scope.isSpinnerVisible = false;
				}
			}, function (response) {
				console.log("Error: " + response);
			});
		}
		$scope.init = function() {
        	// takes a default value, retrieves the prop value from the file system and sets it
    		var msecs = PropertyService.retrieveMsoMaxPollingIntervalMsec(1000);
    		PropertyService.setMsoMaxPollingIntervalMsec(msecs);
    		
    		// takes a default value, retrieves the prop value from the file system and sets it
    		var polls = PropertyService.retrieveMsoMaxPolls(7);
    		PropertyService.setMsoMaxPolls(polls);
    		
    		//PropertyService.setMsoBaseUrl("testmso");
    		PropertyService.setServerResponseTimeoutMsec(10000);
        }
		
		$scope.prevPage = function() {
			$scope.currentPage--;
		}
		
		$scope.nextPage = function() {
			$scope.currentPage++;
		}
		
		$scope.deployService = function(service) {

			console.log("Instantiating ASDC service " + service.uuid);
			
			$http.get('/' + baseEndpoint + '/rest/models/services/' + service.uuid)
				.then(function successCallback(getServiceResponse) {
					
					var serviceModel = getServiceResponse.data;
					DataService.setServiceName(serviceModel.service.name);
					
					DataService.setModelInfo(COMPONENT.SERVICE, {
						"modelInvariantId": serviceModel.service.invariantUuid,
						"modelVersion": serviceModel.service.version,
						"modelNameVersionId": serviceModel.service.uuid,
						"modelName": serviceModel.service.name,
						"description": serviceModel.service.description,
						"category":serviceModel.service.category
					});
					
					$scope.$broadcast("createComponent", {
					    componentId : COMPONENT.SERVICE,
					    callbackFunction : function(response) {
					    	if (response.isSuccessful) {
								vidService.setModel(serviceModel);
								
								var subscriberId = "Not Found";
								var serviceType = "Not Found";
								
								var serviceInstanceId = response.instanceId;
								
								for (var i = 0; i < response.control.length; i++) {
									if (response.control[i].id == "subscriberName") {
										subscriberId = response.control[i].value;
									} else if (response.control[i].id == "serviceType") {
										serviceType = response.control[i].value;
									}
								}
								
								
								$scope.refreshSubs(subscriberId,serviceType,serviceInstanceId);
							
					    	}
					    }
					});
					
				}, function errorCallback(response) {
					console.log("Error: " + response);
				});
		};
		
		$scope.refreshSubs = function(subscriberId, serviceType, serviceInstanceId) {
			$scope.status = "Fetching subscriber list from A&AI...";
			$scope.init();
			$http.get(  PropertyService.getAaiBaseUrl() + "/aai_refresh_full_subscribers", {

			},{
				timeout: $scope.responseTimeoutMsec
			}).then(function(response){
				
				if (response.data.status < 200 || response.data.status > 202) {
					$scope.showError("MSO failure - see log below for details")
					return;
				}

				$scope.customer = response.data.customer; // get data from json
				
				$scope.customerList = [];

				$scope.serviceInstanceToCustomer = [];
				
				angular.forEach($scope.customer, function(subVal, subKey) {
					var cust = { "globalCustomerId": subVal["global-customer-id"], "subscriberName": subVal["subscriber-name"] };
					$scope.customerList.push(cust);
					if (subVal["service-subscriptions"] != null) {
							angular.forEach(subVal["service-subscriptions"]["service-subscription"], function(serviceSubscription, key) {
								$scope.serviceInstanceId = [];
								if (serviceSubscription["service-type"] != null) {
									$scope.serviceType = serviceSubscription["service-type"];
								} else {
									$scope.serviceType = "No Service Subscription Found";
								}
								if (serviceSubscription["service-instances"] != null) {
									angular.forEach(serviceSubscription["service-instances"]["service-instance"], function(instValue, instKey) {
										var foo = { "serviceInstanceId": instValue["service-instance-id"], 
												"globalCustomerId": subVal["global-customer-id"],
												"subscriberName": subVal["subscriber-name"] };
										$scope.serviceInstanceToCustomer.push(foo);
									});
								}
							});
					}
				});	
				DataService.setServiceInstanceToCustomer($scope.serviceInstanceToCustomer);
				var serviceIdList = [];
				$http.get(PropertyService.getAaiBaseUrl() + "/aai_get_services", {
				},{
					timeout: $scope.responseTimeoutMsec
				}).then(function(response) {
				angular.forEach(response.data, function(value, key) {
					angular.forEach(value, function(subVal, key) {
						var newVal = { "id" : subVal["service-id"], "description" : subVal["service-description"] };
						serviceIdList.push(newVal);
						DataService.setServiceIdList(serviceIdList);
						
						$location.search({
							"subscriberId": subscriberId,
							"serviceType": serviceType,
							"serviceInstanceId": serviceInstanceId
						});
						
						$location.path("/instantiate");
					});
					});
				});
			})
			["catch"]($scope.handleServerError);
		};
	});
})();
