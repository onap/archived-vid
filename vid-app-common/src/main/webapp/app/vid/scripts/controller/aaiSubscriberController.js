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

appDS2.controller("aaiSubscriberController", [ "COMPONENT", "FIELD", "PARAMETER", "DataService", "PropertyService", "$scope", "$http", "$timeout", "$location", "$log", "$route", "VIDCONFIGURATION", "UtilityService", "vidService","AaiService",
                                            function(COMPONENT, FIELD, PARAMETER, DataService, PropertyService, $scope, $http, $timeout, $location, $log, $route, VIDCONFIGURATION, UtilityService, vidService, AaiService) {

	$scope.showVnfDetails = function(vnf) {
		console.log("showVnfDetails");
		DataService.setVnfInstanceId(COMPONENT.VNF_INSTANCE_ID);
		DataService
		.setInventoryItem(aaiResult[FIELD.ID.INVENTORY_RESPONSE_ITEMS][FIELD.ID.INVENTORY_RESPONSE_ITEM][0]);

		$scope.$broadcast(COMPONENT.SHOW_COMPONENT_DETAILS, {
			componentId : COMPONENT.VNF,
			callbackFunction : callbackFunction
		});
	}
	$scope.popup = new Object();


	$scope.isPopupVisible = false;
	$scope.defaultBaseUrl = "";
	$scope.responseTimeoutMsec = 60000;

	$scope.serviceTypes=[ FIELD.PROMPT.SELECT_SERVICE];
	$scope.defaultSubscriberName=[ FIELD.PROMPT.SELECT_SUB ];

	var callbackFunction = function(response) {
		alert(response);
	};

	$scope.getSubs = function() {
		$scope.init();
		$scope.fetchSubs(FIELD.PROMPT.FETCHING_SUBS);
		$scope.fetchServices();

	};
	
	 $scope.cancelCreateSI = function(){
	 		
			window.location.href = COMPONENT.WELCOME_PATH;
			
		};
	 
		$scope.getServiceTypes = function(globalCustomerId){
			DataService.setGlobalCustomerId(globalCustomerId);
			DataService.setServiceIdList($scope.customerList)

			if (globalCustomerId !== "" && globalCustomerId !== undefined ) {
				window.location.href = COMPONENT.SERVICE_TYPE_LIST_PATH + $scope.serviceTypeList;
			}
		}
		
		$scope.refreshServiceTypes = function(globalCustomerId){
			DataService.setGlobalCustomerId(globalCustomerId);
			
			$scope.getServiceTypesList();
		}
		
		$scope.subId="";
		$scope.createSubscriberName="";
		$scope.serviceTypeList={};
		$scope.custSubList=[];
		$scope.getServiceTypesList = function(){
			var notFound = true;
			var globalCustomerId = DataService.getGlobalCustomerId();
			$scope.custSubList = DataService.getServiceIdList();
			if(globalCustomerId !== "" && globalCustomerId !== undefined ){
				$scope.subId=globalCustomerId;
				$scope.init();
				$scope.status = FIELD.PROMPT.FETCHING_SERVICE_TYPES;
				DataService.setGlobalCustomerId(globalCustomerId);
				
				AaiService.getSubscriptionServiceTypeList(DataService
						.getGlobalCustomerId(), function(response) {
					notFound = false;
					$scope.setProgress(100); // done
					$scope.status = FIELD.STATUS.DONE;
					$scope.isSpinnerVisible = false;
					$scope.serviceTypeList = response;
					for(var i=0; i<$scope.custSubList.length;i++){
						if(globalCustomerId === $scope.custSubList[i].globalCustomerId){
							$scope.createSubscriberName = $scope.custSubList[i].subscriberName;
						}
					}
				}, function(response) { // failure
					$scope.showError(FIELD.ERROR.AAI);
					$scope.errorMsg = FIELD.ERROR.FETCHING_SERVICE_TYPES + response.status;
					$scope.errorDetails = response.data;
				});
			} else {
				alert(FIELD.ERROR.SELECT);
			}
			
		};
		
		$scope.subList = [];
		$scope.getAaiServiceModels = function(selectedServicetype,subName){
			DataService.setGlobalCustomerId(selectedServicetype);
			DataService.setServiceIdList($scope.serviceTypeList)
			DataService.setSubscriberName(subName);
			
			DataService.setSubscribers($scope.custSubList);

			if (selectedServicetype !== "" && selectedServicetype !== 'undefined') {
				$location.path(COMPONENT.CREATE_INSTANCE_PATH);
			}			
		};
		
		$scope.serviceTypeName="";
		$scope.getAaiServiceModelsList = function(){
			var globalCustomerId="";
             var serviceTypeId = DataService.getGlobalCustomerId();			
			$scope.serviceTypeList = DataService.getServiceIdList();
			$scope.createSubscriberName = DataService.getSubscriberName();
			$scope.status = FIELD.STATUS.FETCHING_SERVICE_CATALOG;
			$scope.custSubList = DataService.getSubscribers();
			for(var i=0; i<$scope.serviceTypeList.length;i++){
						if(parseInt(serviceTypeId) === i ){
							$scope.serviceTypeName = $scope.serviceTypeList[i];
						}
					};
		    for(var i=0; i<$scope.custSubList.length;i++){
						if($scope.createSubscriberName === $scope.custSubList[i].subscriberName){
							globalCustomerId = $scope.custSubList[i].globalCustomerId;
							globalCustId = globalCustomerId;
						}
					};
			
			var pathQuery ="";
			
			if(null !== globalCustomerId && "" !== globalCustomerId && undefined !== globalCustomerId
					&& null !== serviceTypeId && "" !== serviceTypeId && undefined !== serviceTypeId){
				pathQuery = COMPONENT.SERVICES_PATH +globalCustomerId+"/"+$scope.serviceTypeName;
			}
			
			var namedQueryId='6e806bc2-8f9b-4534-bb68-be91267ff6c8';
			AaiService.getServiceModelsByServiceType(namedQueryId,globalCustomerId,$scope.serviceTypeName,function(response) { // success
				$scope.services = [];
				if (angular.isArray(response.data['inventory-response-item'])) {
					$scope.services = response.data['inventory-response-item'][0]['inventory-response-items']['inventory-response-item'];
					$scope.serviceType = response.data['inventory-response-item'][0]['service-subscription']['service-type'];
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
				DataService.setServiceIdList(response);
			},  function(response) { // failure
				$scope.showError(FIELD.ERROR.AAI);
				$scope.errorMsg = FIELD.ERROR.FETCHING_SERVICES+ response.status;
				$scope.errorDetails = response.data;
			});
			
			};
			
			var globalCustId;// This value will be assigned only on create new service instance screen-macro
			$scope.createType = "a la carte";
		$scope.deployService = function(service,hideServiceFields) {
			hideServiceFields = hideServiceFields|| false;
			var temp = service;
			service.uuid = service['service-instance']['model-version-id'];
			
			console.log("Instantiating ASDC service " + service.uuid);
			
			$http.get('rest/models/services/' + service.uuid)
				.then(function successCallback(getServiceResponse) {
					getServiceResponse.data['service'].serviceTypeName =$scope.serviceTypeName ;
					getServiceResponse.data['service'].createSubscriberName =$scope.createSubscriberName ;
					var serviceModel = getServiceResponse.data;
					DataService.setServiceName(serviceModel.service.name);
					
					DataService.setModelInfo(COMPONENT.SERVICE, {
						"modelInvariantId": serviceModel.service.invariantUuid,
						"modelVersion": serviceModel.service.version,
						"modelNameVersionId": serviceModel.service.uuid,
						"modelName": serviceModel.service.name,
						"description": serviceModel.service.description,
						"category":serviceModel.service.category,
						"serviceTypeName":serviceModel.service.serviceTypeName,
						"createSubscriberName":serviceModel.service.createSubscriberName
					});
					DataService.setHideServiceFields(hideServiceFields);
					if(hideServiceFields){
						DataService.setServiceType($scope.serviceTypeName);
						DataService.setGlobalCustomerId(globalCustId);
					}

					DataService.setALaCarte (true);
					$scope.createType = "a la carte";
					var broadcastType = "createComponent";
					
					if (UtilityService.arrayContains (VIDCONFIGURATION.MACRO_SERVICES, serviceModel.service.invariantUuid )) {
						DataService.setALaCarte (false);
						$scope.createType = "Macro";
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
							"displayInputs": convertedAsdcModel.completeDisplayInputs,
							"serviceTypeName":serviceModel.service.serviceTypeName,
							"createSubscriberName":serviceModel.service.createSubscriberName
						});
					};
					
					$scope.$broadcast(broadcastType, {
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
		
		 $scope.cancelCreateSIType = function(){
		 		
				window.location.href = COMPONENT.SERVICE_MODLES_INSTANCES_SUBSCRIBERS_PATH;
				
			}

	$scope.fetchServices = function() { 
		var serviceIdList = [];

		AaiService.getServices2(function(response) { // success
			DataService.setServiceIdList(response);
		},  function(response) { // failure
			$scope.showError(FIELD.ERROR.AAI);
			$scope.errorMsg = FIELD.ERROR.FETCHING_SERVICES + response.status;
			$scope.errorDetails = response.data;
		});
	}

	$scope.refreshSubs = function() {
		$scope.init();
		$scope.fetchSubs(FIELD.PROMPT.REFRESH_SUB_LIST);
		$scope.fetchServices();
	};

	$scope.fetchSubs = function(status) {
		$scope.status = status;

		AaiService.getSubList(function(response) { // sucesss
			$scope.setProgress(100); // done
			$scope.status = FIELD.STATUS.DONE;
			$scope.isSpinnerVisible = false;
			$scope.customerList = response;
		},  function(response) { // failure
			$scope.showError(FIELD.ERROR.AAI);
			$scope.errorMsg = FIELD.ERROR.AAI_FETCHING_CUST_DATA + response.status;
			$scope.errorDetails = response.data;
		});
	}


	$scope.getSubDetails = function(request) {

		$scope.init();
		$scope.selectedSubscriber = $location.search().selectedSubscriber;
		$scope.selectedServiceInstance = $location.search().selectedServiceInstance;
		$scope.status = FIELD.STATUS.FETCHING_SUB_DETAILS + $scope.selectedSubscriber;

		$scope.displayData = [];
		AaiService.getSubDetails($scope.selectedSubscriber, $scope.selectedServiceInstance, function(displayData, subscriberName) {
			$scope.displayData = displayData;
			$scope.viewPerPage=10;
			$scope.totalPage=$scope.displayData.length/$scope.viewPerPage;
			$scope.scrollViewPerPage=2;
			$scope.currentPage=1;
			$scope.searchCategory;
			$scope.searchString="";
			$scope.currentPageNum=1;
			$scope.defaultSort=COMPONENT.SUBSCRIBER_NAME;
				$scope.setProgress(100); // done
			$scope.status = FIELD.STATUS.DONE;
			$scope.isSpinnerVisible = false;
			$scope.subscriberName = subscriberName;
		}, function(response) { 
			$scope.showError(FIELD.ERROR.AAI);
			$scope.errorMsg = FIELD.ERROR.AAI_FETCHING_CUST_DATA + response.status;
			$scope.errorDetails = response.data;
		});
	}


	$scope.$on(COMPONENT.MSO_DELETE_REQ, function(event, request) {
		// $log.debug("deleteInstance: request:");
		// $log.debug(request);
		$scope.init();

		$http.post($scope.baseUrl + request.url, {
			requestDetails: request.requestDetails
		},{
			timeout: $scope.responseTimeoutMsec
		}).then($scope.handleInitialResponse)
		["catch"]($scope.handleServerError);
	});

	$scope.init = function() {

		//PropertyService.setAaiBaseUrl("testaai");
		//PropertyService.setAsdcBaseUrl("testasdc");

		// takes a default value, retrieves the prop value from the file system and sets it
		var msecs = PropertyService.retrieveMsoMaxPollingIntervalMsec();
		PropertyService.setMsoMaxPollingIntervalMsec(msecs);

		// takes a default value, retrieves the prop value from the file system and sets it
		var polls = PropertyService.retrieveMsoMaxPolls();
		PropertyService.setMsoMaxPolls(polls);

		//PropertyService.setMsoBaseUrl("testmso");
		PropertyService.setServerResponseTimeoutMsec();

		/*
		 * Common parameters that would typically be set when the page is
		 * displayed for a specific service instance id.
		 */

		$scope.baseUrl = $scope.defaultBaseUrl;

		$scope.isSpinnerVisible = true;
		$scope.isProgressVisible = true;
		$scope.isPopupVisible = true;
		$scope.requestId = "";
		$scope.error = "";
		$scope.pollAttempts = 0;
		$scope.log = "";				
		$scope.enableCloseButton(false);
		$scope.resetProgress();
		$scope.setProgress(2); // Show "a little" progress
	}

	$scope.getComponentList = function(event, request) {

		$scope.isSpinnerVisible = true;
		$scope.isProgressVisible = true;
		$scope.isPopupVisible = true;
		$scope.requestId = "";
		$scope.error = "";
		$scope.pollAttempts = 0;
		$scope.log = "";				

		$scope.resetProgress();
		$scope.setProgress(2); // Show "a little" progress

		$scope.globalCustomerId = $location.search().subscriberId;
		$scope.serviceType = $location.search().serviceType;
		$scope.serviceInstanceId = $location.search().serviceInstanceId;
		$scope.subscriberName = $location.search().subscriberName;

		//$scope.getAsdcModel($location.search().modelUuid);

		$scope.namedQueryId = VIDCONFIGURATION.COMPONENT_LIST_NAMED_QUERY_ID;
		$scope.status = FIELD.STATUS.FETCHING_SERVICE_INST_DATA + $scope.serviceInstanceId;

		AaiService.runNamedQuery($scope.namedQueryId, $scope.globalCustomerId, $scope.serviceType, $scope.serviceInstanceId, 
				function(response) { //success
			$scope.handleInitialResponseInventoryItems(response);
			$scope.setProgress(100); // done
			$scope.status = FIELD.STATUS.DONE;
			$scope.isSpinnerVisible = false;
		}, 
		function(response){ //failure
			$scope.showError(FIELD.ERROR.AAI);
			$scope.errorMsg = FIELD.ERROR.FETCHING_SERVICE_INSTANCE_DATA + response.status;
			$scope.errorDetails = response.data;
		}
		);

	}

	$scope.handleServerError = function(response, status) {				
		alert(response.statusText);
	}
	
	$scope.getAsdcModel = function(disData) {

		console.log ("disData"); console.log (JSON.stringify (disData, null, 4));
		
		if ( !(UtilityService.hasContents (disData.aaiModelVersionId)) ) {
			$scope.errorMsg = FIELD.ERROR.MODEL_VERSION_ID_MISSING;
			alert($scope.errorMsg);
			return;
		}
		
		// aaiModelVersionId is the model uuid
		var pathQuery = COMPONENT.SERVICES_PATH + disData.aaiModelVersionId;
		$http({
			  method: 'GET',
			  url: pathQuery
			}).then(function successCallback(response) {
				vidService.setModel(response.data);
				window.location.href = COMPONENT.INSTANTIATE_ROOT_PATH + disData.globalCustomerId + COMPONENT.SUBSCRIBERNAME_SUB_PATH + disData.subscriberName + COMPONENT.SERVICETYPE_SUB_PATH + disData.serviceType + COMPONENT.SERVICEINSTANCEID_SUB_PATH + disData.serviceInstanceId;
				console.log("aaiSubscriber getAsdcModel DONE!!!!");
			  }, function errorCallback(response) {
				  console.log("aaiSubscriber getAsdcModel - No matching model found matching the A&AI model version ID = " + disData.aaiModelVersionId);
					$scope.errorMsg = FIELD.ERROR.NO_MATCHING_MODEL_AAI + disData.aaiModelVersionId;
					alert($scope.errorMsg);
			  });

	}

	$scope.getTenants = function(globalCustomerId) {
		$http.get(FIELD.ID.AAI_GET_TENTANTS + globalCustomerId)
		.then(function successCallback(response) {
			return response.data;
			//$location.path("/instantiate");
		}, function errorCallback(response) {
			//TODO
		});
	}

	$scope.handleInitialResponseInventoryItems = function(response) {

		$scope.inventoryResponseItemList = response.data[FIELD.ID.INVENTORY_RESPONSE_ITEM]; // get data from json
		console.log($scope.inventoryResponseItemList.toString());

		$scope.displayData = [];
		$scope.vnfs = [];

		$scope.counter = 100;

		$scope.subscriberName = "";
		// just look up the subscriber name in A&AI here...
		AaiService.getSubscriberName($scope.globalCustomerId, function(response) {
			$scope.subscriberName = response;
			DataService.setSubscriberName($scope.subscriberName);

			angular.forEach($scope.inventoryResponseItemList, function(inventoryResponseItem, key) {

				$scope.inventoryResponseItem = inventoryResponseItem;

				$scope.service.instance = {
						"name": $scope.inventoryResponseItem[FIELD.ID.SERVICE_INSTANCE][FIELD.ID.SERVICE_INSTANCE_NAME],
						"serviceInstanceId": $scope.serviceInstanceId,
						"serviceType": $scope.serviceType,
						"globalCustomerId": $scope.globalCustomerId,
						"subscriberName": $scope.subscriberName,
						"id": $scope.serviceInstanceId,
						"inputs": {
							"a": {
								"type": PARAMETER.STRING,
								"description": FIELD.PROMPT.VAR_DESCRIPTION_A,
								"default": FIELD.PROMPT.DEFAULT_A
							},
							"b": {
								"type": PARAMETER.STRING,
								"description": FIELD.PROMPT.VAR_DESCRIPTION_B,
								"default": FIELD.PROMPT.DEFAULT_B
							},
						},
						"object": $scope.inventoryResponseItem[FIELD.ID.SERVICE_INSTANCE],
						"vnfs": [],
						"networks": []
				}

				if (inventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS] != null) {

					angular.forEach(inventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS][FIELD.ID.INVENTORY_RESPONSE_ITEM], function(subInventoryResponseItem, key) {
						// i expect to find vnfs now

						if (subInventoryResponseItem[FIELD.ID.L3_NETWORK] != null) { 
							var l3NetworkObject = subInventoryResponseItem[FIELD.ID.L3_NETWORK];
							var l3Network = { "id": $scope.counter++, 
									"name": l3NetworkObject[FIELD.ID.NETWORK_NAME],
									"itemType": FIELD.ID.L3_NETWORK,
									"nodeId": l3NetworkObject[FIELD.ID.NETWORK_ID],
									"nodeType": l3NetworkObject[FIELD.ID.NETWORK_TYPE],
									"nodeStatus": l3NetworkObject[FIELD.ID.ORCHESTRATION_STATUS],
									"object": l3NetworkObject,
									"nodes": [],
									"subnets": []
							};
							if (subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS] != null) {
								//console.log ("subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS]=");
								//console.log (JSON.stringify (subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS], null, 4 ));
								angular.forEach(subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS][FIELD.ID.INVENTORY_RESPONSE_ITEM], function(subSubInventoryResponseItem, key) {
									//console.log (JSON.stringify (subSubInventoryResponseItem, null, 4 ));
									var subnet = {};
									var subnetObject;
									if (subSubInventoryResponseItem[FIELD.ID.SUB_NET] != null) {
										subnetObject = subSubInventoryResponseItem[FIELD.ID.SUB_NET];
										subnet = {
												"subnet-id": subnetObject[FIELD.ID.SUBNET_ID],
												"subnet-name": subnetObject[FIELD.ID.SUBNET_NAME],
												"gateway-address": subnetObject[FIELD.ID.GATEWAY_ADDRESS],
												"network-start-address": subnetObject[FIELD.ID.NETWORK_START_ADDRESS],
												"cidr-mask": subnetObject[FIELD.ID.CIDR_MASK]
										};
										l3Network.subnets.push(subnet);
									}
								});
							}
							$scope.service.instance[FIELD.ID.NETWORKS].push(l3Network);
						}

						if (subInventoryResponseItem[FIELD.ID.GENERIC_VNF] != null) {
							var genericVnfObject = subInventoryResponseItem[FIELD.ID.GENERIC_VNF];

							var genericVnf = {
									"name": genericVnfObject[FIELD.ID.VNF_NAME],
									"id": $scope.counter++, 
									"itemType": COMPONENT.VNF, 
									"nodeType": genericVnfObject[FIELD.ID.VNF_TYPE],
									"nodeId": genericVnfObject[FIELD.ID.VNF_ID],
									"nodeStatus": genericVnfObject[FIELD.ID.ORCHESTRATION_STATUS],
									"object": genericVnfObject,
									"vfModules": [],
									"volumeGroups": [],
									"availableVolumeGroups": []
							};
							$scope.service.instance[FIELD.ID.VNFS].push(genericVnf);

							// look for volume-groups
							if (subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS] != null) {
								angular.forEach(subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS][FIELD.ID.INVENTORY_RESPONSE_ITEM], function(vfmodules, key) {

									if (vfmodules[FIELD.ID.VOLUME_GROUP] != null) { 
										var volumeGroupObject = vfmodules[FIELD.ID.VOLUME_GROUP];
										var volumeGroup = { "id": $scope.counter++, 
												"name": volumeGroupObject[FIELD.ID.VOLUME_GROUP_NAME],
												"itemType": FIELD.ID.VOLUME_GROUP,
												"nodeId": volumeGroupObject[FIELD.ID.VOLUME_GROUP_ID],
												"nodeType": volumeGroupObject[FIELD.ID.VNF_TYPE],
												"nodeStatus": volumeGroupObject[FIELD.ID.ORCHESTRATION_STATUS],
												"object": volumeGroupObject,
												"nodes": []
										};
										genericVnf[FIELD.ID.VOLUMEGROUPS].push(volumeGroup);
										genericVnf[FIELD.ID.AVAILABLEVOLUMEGROUPS].push(volumeGroup);
									}
								});
							}
							// now we've loaded up the availableVolumeGroups, we can use it
							if (subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS] != null) {
								angular.forEach(subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS][FIELD.ID.INVENTORY_RESPONSE_ITEM], function(vfmodules, key) {

									if (vfmodules[FIELD.ID.VF_MODULE] != null) { 
										var vfModuleObject = vfmodules[FIELD.ID.VF_MODULE];
										var vfModule = { "id": $scope.counter++,
												"name": vfModuleObject[FIELD.ID.VF_MODULE_NAME],
												"itemType": FIELD.ID.VF_MODULE,
												"nodeType": FIELD.ID.VF_MODULE, 
												"nodeStatus": vfModuleObject[FIELD.ID.ORCHESTRATION_STATUS],
												"volumeGroups": [],
												"object": vfModuleObject,
												"networks": []
										};
										genericVnf[FIELD.ID.VF_MODULES].push(vfModule);
										if (vfmodules[FIELD.ID.INVENTORY_RESPONSE_ITEMS] != null) {
											angular.forEach(vfmodules[FIELD.ID.INVENTORY_RESPONSE_ITEMS][FIELD.ID.INVENTORY_RESPONSE_ITEM], function(networks, key) {
												if (networks[FIELD.ID.L3_NETWORK] != null) { 
													var l3NetworkObject = networks[FIELD.ID.L3_NETWORK];
													var l3Network = { "id": $scope.counter++, 
															"name": l3NetworkObject[FIELD.ID.NETWORK_NAME],
															"itemType": FIELD.ID.L3_NETWORK,
															"nodeId": l3NetworkObject[FIELD.ID.NETWORK_ID],
															"nodeType": l3NetworkObject[FIELD.ID.NETWORK_TYPE],
															"nodeStatus": l3NetworkObject[FIELD.ID.ORCHESTRATION_STATUS],
															"object": l3NetworkObject,
															"nodes": []
													};
													vfModule[FIELD.ID.NETWORKS].push(l3Network);
												}
												if (networks[FIELD.ID.VOLUME_GROUP] != null) { 
													var volumeGroupObject = networks[FIELD.ID.VOLUME_GROUP];

													var volumeGroup = { "id": $scope.counter++, 
															"name": volumeGroupObject[FIELD.ID.VOLUME_GROUP_NAME],
															"itemType": FIELD.ID.VOLUME_GROUP,
															"nodeId": volumeGroupObject[FIELD.ID.VOLUME_GROUP_ID],
															"nodeType": volumeGroupObject[FIELD.ID.VNF_TYPE],
															"nodeStatus": volumeGroupObject[FIELD.ID.ORCHESTRATION_STATUS],
															"object": volumeGroupObject,
															"nodes": []
													};
													var tmpVolGroup = [];

													angular.forEach(genericVnf[FIELD.ID.AVAILABLEVOLUMEGROUPS], function(avgroup, key) {
														if (avgroup.name != volumeGroup.name) { 
															tmpVolGroup.push(avgroup);
														}
													});

													genericVnf[FIELD.ID.AVAILABLEVOLUMEGROUPS] = tmpVolGroup;

													vfModule[FIELD.ID.VOLUMEGROUPS].push(volumeGroup);
												}

											});
										}
									}
								});
							}
						}
					});
				}
			});
		});
	}

	$scope.handleInitialResponse = function(response) {
		try {
			$scope.enableCloseButton(true);
			$scope.updateLog(response);
			if (response.data.status < 200 || response.data.status > 202) {
				$scope.showError(FIELD.ERROR.MSO);
				$scope.status = FIELD.ERROR.AAI_FETCHING_CUST_DATA + response.data.status;

				return;
			}

			$scope.setProgress(100); // done
			$scope.status = FIELD.STATUS.DONE;
			$scope.isSpinnerVisible = false;

			$scope.customer = response.data.customer; // get data from json

			$scope.customerList = [];

			angular.forEach($scope.customer, function(subVal, subKey) {
				var cust = { "globalCustomerId": subVal[FIELD.ID.GLOBAL_CUSTOMER_ID], "subscriberName": subVal[FIELD.ID.SUBNAME] };
				$scope.customerList.push(cust);
			});	

		} catch (error) {
			$scope.showContentError(error);
		}
	}

	$scope.autoGetSubs = function() {
		/*
		 * Optionally comment in / out one of these method calls (or add a similar
		 * entry) to auto-invoke an entry when the test screen is redrawn.
		 */
		$scope.getSubs();

	}

	$scope.updateLog = function(response) {
//		$scope.log = UtilityService.getCurrentTime() + " HTTP Status: " + 
//		UtilityService.getHttpStatusText(response.data.status) + "\n" +
//		angular.toJson(response.data.entity, true) + "\n\n" + $scope.log;
//		UtilityService.checkUndefined("entity", response.data.entity);
//		UtilityService.checkUndefined("status", response.data.status);				
	}

	$scope.handleServerError = function(response, status) {				
		$scope.enableCloseButton(true);
		var message = UtilityService.getHttpErrorMessage(response);
		if (message != ""){
			message = " (" + message + ")";
		}
		$scope.showError(FIELD.ERROR.SYSTEM_ERROR + message);
	}

	$scope.showContentError = function(message) {
		// $log.debug(message);
		console.log(message);
		if (UtilityService.hasContents(message)) {
			$scope.showError("System failure (" + message + ")");
		} else {
			$scope.showError(FIELD.ERROR.SYSTEM_ERROR);
		}
	}

	$scope.showError = function(message) {
		$scope.isSpinnerVisible = false;
		$scope.isProgressVisible = false;
		$scope.error = message;
		$scope.status = FIELD.STATUS.ERROR;
	}

	$scope.close = function() {
		if ($scope.timer != undefined) {
			$timeout.cancel($scope.timer);					
		}
		$scope.isPopupVisible = false;
	}



	/*
	 * Consider converting the progress bar mechanism, the disabled button handling
	 * and the following methods to generic Angular directive(s) and/or approach.
	 */

	$scope.enableCloseButton = function(isEnabled) {
		var selector = FIELD.STYLE.MSO_CTRL_BTN;

		$scope.isCloseEnabled = isEnabled;

		if (isEnabled) {
			$(selector).addClass(FIELD.STYLE.BTN_PRIMARY).removeClass(FIELD.STYLE.BTN_INACTIVE).attr(FIELD.STYLE.BTN_TYPE, FIELD.STYLE.PRIMARY);
		} else {
			$(selector).removeClass(FIELD.STYLE.BTN_PRIMARY).addClass(FIELD.STYLE.BTN_INACTIVE).attr(FIELD.STYLE.BTN_TYPE, FIELD.STYLE.DISABLED);					
		}
	}

	$scope.resetProgress = function() {
		$scope.percentProgress = 0;
		$scope.progressClass = FIELD.STYLE.PROGRESS_BAR_INFO;
	}

	$scope.setProgress = function(percentProgress) {
		percentProgress = parseInt(percentProgress);
		if (percentProgress >= 100) {
			$scope.progressClass = FIELD.STYLE.PROGRESS_BAR_SUCCESS;					
		}

		if (percentProgress < $scope.percentProgress) {
			return;
		}

		$scope.percentProgress = percentProgress;
		$scope.progressWidth = {width: percentProgress + "%"};
		if (percentProgress >= 5) {
			$scope.progressText =  percentProgress + " %";
		} else {
			// Hidden since color combination is barely visible when progress portion is narrow.
			$scope.progressText = "";
		}
	}

	$scope.reloadRoute = function() {
		$route.reload();
	}

	$scope.prevPage = function() {
		$scope.currentPage--;
	}

	$scope.nextPage = function() {
		$scope.currentPage++;
	}
	$scope.serviceInstanceses = [{"sinstance":FIELD.NAME.SERVICE_INSTANCE_Id},{"sinstance":FIELD.NAME.SERVICE_INSTANCE_NAME}]
	$scope.getSubscriberDet = function(selectedCustomer,selectedserviceinstancetype,selectedServiceInstance){
		
		var sintype =selectedserviceinstancetype;
		if (selectedServiceInstance != "" && selectedServiceInstance != undefined) {
			selectedServiceInstance.trim();

			// check with A&AI
			$http.get(COMPONENT.AAI_GET_SERVICE_INSTANCE_PATH  + selectedServiceInstance+"/"+sintype + "?r=" + Math.random(), {

			},{
				timeout: $scope.responseTimeoutMsec
			}).then(function(response) {
				var notFound = true;
				if (angular.isArray(response.data[FIELD.ID.RESULT_DATA])) {
					var item = [];
					var urlParts = [];
					item = response.data[FIELD.ID.RESULT_DATA][0];
					var url = item[FIELD.ID.RESOURCE_LINK];
					var globalCustomerId = "";
					var serviceSubscription = "";
					// split it and find the customer Id and service-subscription
					urlParts = url.split("/");
					if (urlParts[7] === FIELD.ID.CUSTOMER) { 
						globalCustomerId = urlParts[8];
					}
					if (urlParts[10] === FIELD.ID.SERVICE_SUBSCRIPTION) { 
						serviceSubscription = urlParts[11];
					}

					if (globalCustomerId !== "") {
						notFound = false;
						window.location.href = COMPONENT.SELECTED_SERVICE_SUB_PATH + serviceSubscription + COMPONENT.SELECTEDSUBSCRIBER_SUB_PATH + globalCustomerId + COMPONENT.SELECTEDSERVICEINSTANCE_SUB_PATH + selectedServiceInstance;
					}
				}
				if (notFound) {
					alert(FIELD.ERROR.SERVICE_INST_DNE);
				}
			}); // add a failure callback...
		} else if (selectedCustomer != null) { 
			window.location.href = COMPONENT.SELECTED_SUB_PATH + selectedCustomer;
		} else {
			alert(FIELD.ERROR.SELECT);
		}
	};
  }]).directive('restrictInput', function(){
		
		return {
			
			restrict: 'A',
			require: 'ngModel',
			link: function($scope, element, attr, ctrl){
				ctrl.$parsers.unshift(function(viewValue){
					
					var types = $scope.$eval(attr.restrictInput);
					if(!types.regex && types.type){
						
						switch(types.type){
							case 'Service Instance Name' : types.regex = '^[a-zA-Z0-9-_]*$'; break;
							default: types.regex= '';
						}
					}
					var reg = new RegExp(types.regex);
					if(reg.test(viewValue)){
						return viewValue;
					} else {
						var overrideValue = (reg.test(viewValue) ? viewValue : '');
						element.val(overrideValue);
						return overrideValue;
					}
				});
			}
		};

            });
appDS2.controller('TreeCtrl', ['$scope', function ($scope) {
	$scope.remove = function (scope) {
		scope.remove();
	};

	$scope.toggle = function (scope) {
		scope.toggle();
	};

	$scope.moveLastToTheBeginning = function () {
		var a = $scope.data.pop();
		$scope.data.splice(0, 0, a);
	};

	$scope.newSubItem = function (scope) {
		var nodeData = scope.$modelValue;
		nodeData.nodes.push({
			id: nodeData.id * 10 + nodeData.nodes.length,
			title: nodeData.title + '.' + (nodeData.nodes.length + 1),
			nodes: []
		});
	};

	$scope.collapseAll = function () {
		$scope.$broadcast(FIELD.ID.ANGULAR_UI_TREE_COLLAPSEALL);
	};

	$scope.expandAll = function () {
		$scope.$broadcast(FIELD.ID.ANGULAR_UI_TREE_EXPANDALL);
	};


}]);



