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

	appDS2.controller("ServiceModelController", function ($scope, $http, $location, COMPONENT, VIDCONFIGURATION, FIELD, DataService, vidService,
			PropertyService, UtilityService) {

		$scope.popup = {};
		
	//	var baseEndpoint = "vid";
		var pathQuery = COMPONENT.SERVICES_DIST_STATUS_PATH + VIDCONFIGURATION.ASDC_MODEL_STATUS;
		
		if ( VIDCONFIGURATION.ASDC_MODEL_STATUS === FIELD.STATUS.ALL) {
			pathQuery = COMPONENT.SERVICES_PATH;
		}
		
		$scope.getServiceModels = function() {
			$scope.status = FIELD.STATUS.FETCHING_SERVICE_CATALOG_ASDC;

			$http.get(pathQuery)
			.then(function successCallback(response) {
				$scope.services = [];
				if (response.data && angular.isArray(response.data.services)) {
					wholeData = response.data.services;
					$scope.services = $scope.filterDataWithHigerVersion(wholeData);
					$scope.viewPerPage=10;
					$scope.totalPage=$scope.services.length/$scope.viewPerPage;
					$scope.sortBy=COMPONENT.NAME;
					$scope.scrollViewPerPage=2;
					$scope.currentPage=1;
					$scope.searchCategory;
					$scope.searchString="";
					$scope.currentPageNum=1;
					$scope.isSpinnerVisible = false;
					$scope.isProgressVisible = false;
				} else {
					$scope.status = FIELD.STATUS.FAILED_SERVICE_MODELS_ASDC;
					$scope.error = true;
					$scope.isSpinnerVisible = false;
				}
				$scope.deployButtonType = response.data.readOnly ? 'disabled' : 'primary';
			}, function errorCallback(response) {
				console.log("Error: " + response);
			});
		}
		$scope.isFiltered=function(arr,obj){
			var filtered = false;
			if(arr.length>0){
				for(var i=0;i<arr.length;i++){
					if((arr[i].name == obj.name) && (obj.invariantUUID == arr[i].invariantUUID)){
						filtered = true;
					}
				}
			}
			return filtered;
		}
		var wholeData=[];
		$scope.filterDataWithHigerVersion = function(serviceData){
			var fiterDataServices = [];
			for(var i=0;i<serviceData.length;i++){
				var higherVersion = serviceData[i];
				if(!$scope.isFiltered(fiterDataServices,serviceData[i])){
					for(var j=i;j<serviceData.length;j++){
						if((serviceData[i].invariantUUID.trim() == serviceData[j].invariantUUID.trim()) && (serviceData[i].name.trim() == serviceData[j].name.trim()) && (parseFloat(serviceData[j].version.trim())>=parseFloat(serviceData[i].version.trim()))){
							var data = $scope.isThisHigher(fiterDataServices,serviceData[j]);
							if(data.isHigher){
								fiterDataServices[data.index] = serviceData[j];
							}
						}
					}
				}
			}
			return fiterDataServices;
		}

		$scope.isThisHigher = function(arr,obj){
			var returnObj = {
					isHigher:false,
					index:0
			};
			if(arr.length>0){
				var isNotMatched = true;
				for(var i=0;i<arr.length;i++){
					if((arr[i].name == obj.name) && (arr[i].invariantUUID == obj.invariantUUID ) && (arr[i].version<obj.version) ){
						isNotMatched = false;
						returnObj = {
								isHigher:true,
								index:i
						};
					}
				}
				if(isNotMatched && !$scope.isFiltered(arr,obj)){
					returnObj = {
							isHigher:true,
							index:arr.length
					};
				}
			}else{
				returnObj = {
						isHigher:true,
						index:0
				}
			}
			return returnObj;
		}

		$scope.init = function() {
    		var msecs = PropertyService.retrieveMsoMaxPollingIntervalMsec();
    		PropertyService.setMsoMaxPollingIntervalMsec(msecs);
    		
    		var polls = PropertyService.retrieveMsoMaxPolls();
    		PropertyService.setMsoMaxPolls(polls);
    		
    		//PropertyService.setMsoBaseUrl("testmso");
    		PropertyService.setServerResponseTimeoutMsec(30000);
        }
		
		$scope.prevPage = function() {
			$scope.currentPage--;
		}
		
		$scope.nextPage = function() {
			$scope.currentPage++;
		}
		
		$scope.createType = COMPONENT.A_LA_CARTE;
		$scope.deployService = function(service) {
			
			
			console.log("Instantiating SDC service " + service.uuid);
			
			$http.get(COMPONENT.SERVICES_PATH + service.uuid)
				.then(function successCallback(getServiceResponse) {
					
					var serviceModel = getServiceResponse.data;
					DataService.setServiceName(serviceModel.service.name);
					
					DataService.setModelInfo(COMPONENT.SERVICE, {
						"modelInvariantId": serviceModel.service.invariantUuid,
						"modelVersion": serviceModel.service.version,
						"serviceType" : serviceModel.service.serviceType,
						"serviceRole": serviceModel.service.serviceRole,
						"modelNameVersionId": serviceModel.service.uuid,
						"modelName": serviceModel.service.name,
						"description": serviceModel.service.description,
						"category":serviceModel.service.category
					});
					DataService.setALaCarte (true);
					$scope.createType = COMPONENT.A_LA_CARTE;
					var broadcastType = COMPONENT.CREATE_COMPONENT;
					
					if (UtilityService.arrayContains (VIDCONFIGURATION.MACRO_SERVICES, serviceModel.service.invariantUuid )) {
						DataService.setALaCarte (false);
						$scope.createType = COMPONENT.MACRO;
						var convertedAsdcModel = UtilityService.convertModel(serviceModel);
						
						//console.log ("display inputs "); 
						//console.log (JSON.stringify ( convertedAsdcModel.completeDisplayInputs));
						
						DataService.setModelInfo(COMPONENT.SERVICE, {
							"modelInvariantId": serviceModel.service.invariantUuid,
							"modelVersion": serviceModel.service.version,
							"modelNameVersionId": serviceModel.service.uuid,
							"modelName": serviceModel.service.name,
							"description": serviceModel.service.description,
							"category":serviceModel.service.category,
							"serviceEcompNaming": serviceModel.service.serviceEcompNaming,
							"inputs": serviceModel.service.inputs,
							"serviceType": serviceModel.service.serviceType,
							"serviceRole": serviceModel.service.serviceRole,
							"displayInputs": convertedAsdcModel.completeDisplayInputs
						});
					};
					
					$scope.$broadcast(broadcastType, {
					    componentId : COMPONENT.SERVICE,
					    callbackFunction : function(response) {
					    	if (response.isSuccessful) {
								vidService.setModel(serviceModel);
								
								var subscriberId = FIELD.STATUS.NOT_FOUND;
								var serviceType = FIELD.STATUS.NOT_FOUND;
								
								var serviceInstanceId = response.instanceId;
								
								for (var i = 0; i < response.control.length; i++) {
									if (response.control[i].id == COMPONENT.SUBSCRIBER_NAME) {
										subscriberId = response.control[i].value;
									} else if (response.control[i].id == FIELD.ID.SERVICE_TYPE) {
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
		
		$scope.tableData=[];
		var oldData=[];
		$scope.loadPreviousVersionData=function(invariantUUID , name, version){
			$scope.tableData =[];
			oldData=[];
			for(var i=0;i<wholeData.length;i++){
				if(wholeData[i].invariantUUID == invariantUUID && wholeData[i].name == name && version!=wholeData[i].version){
					oldData.push(wholeData[i]);
				}
			}
			$scope.tableData = oldData;
			$scope.createType = "Previous Version";
			var broadcastType = "createTableComponent";
			$scope.componentName = name;
			$scope.$broadcast(broadcastType, {
			    componentId : COMPONENT.OLDVERSION,
			    callbackFunction : function(response) {
			    }
			});
		}

		$scope.refreshSubs = function(subscriberId, serviceType, serviceInstanceId) {
			$scope.status = FIELD.STATUS.FETCHING_SUBSCRIBER_LIST_AAI;
			$scope.init();
			$http.get( FIELD.ID.AAI_REFRESH_FULL_SUBSCRIBERS, {

			},{
				timeout: $scope.responseTimeoutMsec
			}).then(function(response){
				
				if (response.data.status < 200 || response.data.status > 202) {
					$scope.showError(FIELD.ERROR.MSO)
					return;
				}

				$scope.customer = response.data.customer; // get data from json

				$scope.customerList = [];

				$scope.serviceInstanceToCustomer = [];
				
				angular.forEach($scope.customer, function(subVal, subKey) {
					var cust = { "globalCustomerId": subVal[FIELD.ID.GLOBAL_CUSTOMER_ID], "subscriberName": subVal[FIELD.ID.SUBNAME] };
					$scope.customerList.push(cust);
					if (subVal[FIELD.ID.SERVICE_SUBSCRIPTIONS] != null) {
							angular.forEach(subVal[FIELD.ID.SERVICE_SUBSCRIPTIONS][FIELD.ID.SERVICE_SUBSCRIPTION], function(serviceSubscription, key) {
								$scope.serviceInstanceId = [];
								if (serviceSubscription[FIELD.ID.SERVICETYPE] != null) {
									$scope.serviceType = serviceSubscription[FIELD.ID.SERVICETYPE];
								} else {
									$scope.serviceType = FIELD.STATUS.NO_SERVICE_SUBSCRIPTION_FOUND;
								}
								if (serviceSubscription[FIELD.ID.SERVICE_INSTANCES] != null) {
									angular.forEach(serviceSubscription[FIELD.ID.SERVICE_INSTANCES][FIELD.ID.SERVICE_INSTANCE], function(instValue, instKey) {
										var foo = { "serviceInstanceId": instValue[FIELD.ID.SERVICE_INSTANCE_ID], 
												"globalCustomerId": subVal[FIELD.ID.GLOBAL_CUSTOMER_ID],
												"subscriberName": subVal[FIELD.ID.SUBNAME] };
										$scope.serviceInstanceToCustomer.push(foo);
									});
								}
							});
					}
				});	
				DataService.setServiceInstanceToCustomer($scope.serviceInstanceToCustomer);
				var serviceIdList = [];
				$http.get( FIELD.ID.AAI_GET_SERVICES, {
				},{
					timeout: $scope.responseTimeoutMsec
				}).then(function(response) {
					angular.forEach(response.data, function(value, key) {
						angular.forEach(value, function(subVal, key) {
							var newVal = { "id" : subVal[FIELD.ID.SERVICE_ID], "description" : subVal[FIELD.ID.SERVICE_DESCRIPTION] ,"isPermitted" : subVal[FIELD.ID.IS_PERMITTED] };
							serviceIdList.push(newVal);
							DataService.setServiceIdList(serviceIdList);
							
							$location.search({
								"subscriberId": subscriberId,
								"serviceType": serviceType,
								"serviceInstanceId": serviceInstanceId,
								"isPermitted": newVal.isPermitted.toString()
							});
							
							$location.path(COMPONENT.INSTANTIATE_PATH);
						});
					});
				});	
			})
			["catch"]($scope.handleServerError);
		};
	});
})();
