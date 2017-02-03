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

app.controller("aaiSubscriberController", [ "COMPONENT", "DataService", "PropertyService", "$scope", "$http", "$timeout", "$location", "$log", "$route", "UtilityService", "vidService",
                                            function(COMPONENT, DataService, PropertyService, $scope, $http, $timeout, $location, $log, $route, UtilityService, vidService) {

	$scope.showVnfDetails = function(vnf) {
		console.log("showVnfDetails");
		DataService.setVnfInstanceId("VNF_INSTANCE_ID_12345");
		DataService
		.setInventoryItem(aaiResult["inventory-response-items"]["inventory-response-item"][0]);

		$scope.$broadcast("showComponentDetails", {
			componentId : COMPONENT.VNF,
			callbackFunction : callbackFunction
		});
	}
	$scope.popup = new Object();


	$scope.isPopupVisible = false;
	$scope.defaultBaseUrl = "";
	$scope.responseTimeoutMsec = 60000;

	$scope.serviceTypes=[ "Select a service type" ];
	$scope.defaultSubscriberName=[ "Select a subscriber name" ];

	var callbackFunction = function(response) {
		alert(response);
	};

	$scope.getSubs = function() {
		$scope.status = "Fetching subscriber list from A&AI...";
		$scope.init();
		$http.get(  PropertyService.getAaiBaseUrl() + "/aai_get_full_subscribers" + '?r=' + Math.random(), {

		},{
			timeout: $scope.responseTimeoutMsec
		}).then($scope.handleInitialResponse)
		["catch"]($scope.handleServerError);
		var serviceIdList = [];
		$http.get(PropertyService.getAaiBaseUrl() + "/aai_get_services" + '?r=' + Math.random(), {
		},{
			timeout: $scope.responseTimeoutMsec
		}).then(function(response) {
			angular.forEach(response.data, function(value, key) {
				angular.forEach(value, function(subVal, key) {
					var newVal = { "id" : subVal["service-id"], "description" : subVal["service-description"] };
					serviceIdList.push(newVal);
					DataService.setServiceIdList(serviceIdList);
				});
			});
		});	
	};

	$scope.refreshSubs = function() {
		$scope.status = "Fetching subscriber list from A&AI...";
		$scope.init();
		$http.get(PropertyService.getAaiBaseUrl() + "/aai_refresh_full_subscribers" + '?r=' + Math.random(), {

		},{
			timeout: $scope.responseTimeoutMsec
		}).then($scope.handleInitialResponse)
		["catch"]($scope.handleServerError);
	};
	
	$scope.getSubDetails = function(request) {

		$scope.init();
		$scope.selectedSubscriber = $location.search().selectedSubscriber;
		$scope.selectedServiceInstance = $location.search().selectedServiceInstance;
		$scope.status = "Fetching subscriber details from A&AI for " + $scope.selectedSubscriber;
		$http.get(PropertyService.getAaiBaseUrl() + "/aai_sub_details/"  + $scope.selectedSubscriber + '?r=' + Math.random(), {

		},{
			timeout: $scope.responseTimeoutMsec
		}).then(function(response) {
			$scope.subscriber = response.data;
			$scope.selectedSubscriberName = $scope.subscriber["subscriber-name"];
			
			$scope.displayData= [];
			if ($scope.subscriber["service-subscriptions"] != null) {
				angular.forEach($scope.subscriber["service-subscriptions"]["service-subscription"], function(serviceSubscription, key) {
					$scope.serviceInstanceId = [];
					if (serviceSubscription["service-type"] != null) {
						$scope.serviceType = serviceSubscription["service-type"];
					} else {
						$scope.serviceType = "No Service Subscription Found";
					}
					if (serviceSubscription["service-instances"] != null) {
						angular.forEach(serviceSubscription["service-instances"]["service-instance"], function(instValue, instKey) {
							// put them together, i guess
							var inst = { "serviceInstanceId": instValue["service-instance-id"], 
										 "personaModelId": instValue["persona-model-id"],
										 "personaModelVersion": instValue["persona-model-version"],
									     "serviceInstanceName": instValue["service-instance-name"]
							};
							if ($scope.selectedServiceInstance != null) {
								if (instValue["service-instance-id"] == $scope.selectedServiceInstance) {
									$scope.serviceInstanceId.push(inst);
								}
							} else {
								$scope.serviceInstanceId.push(inst);
							}
						});
					} else {
						if ($scope.serviceInstanceId == []) {
							$scope.serviceInstanceId = [ "No Service Instance Found" ];
						}
					}
					angular.forEach($scope.serviceInstanceId, function(subVal, subKey) {
						$scope.displayData.push({
							globalCustomerId 	: $scope.selectedSubscriber,
							subscriberName   	: $scope.selectedSubscriberName,
							serviceType 		: $scope.serviceType,
							serviceInstanceId 	: subVal.serviceInstanceId,
							personaModelId		: subVal.personaModelId,
							personaModelVersion : subVal.personaModelVersion,
							serviceInstanceName	: subVal.serviceInstanceName
						});
					});
				});
			} else { 
				$scope.displayData.push({
					globalCustomerId 	: $scope.selectedSubscriber,
					subscriberName   	: $scope.selectedSubscriberName,
					serviceType 		: "No Service Subscription Found",
					serviceInstanceId 	: "No Service Instance Found"
				});  
			}
			$scope.viewPerPage=10;
			$scope.totalPage=$scope.displayData.length/$scope.viewPerPage;
			$scope.scrollViewPerPage=2;
			$scope.currentPage=1;
			$scope.searchCategory;
			$scope.searchString="";
			$scope.currentPageNum=1;
			$scope.defaultSort="subscriberName"
			$scope.setProgress(100); // done
			$scope.status = "Done";
			$scope.isSpinnerVisible = false;
		});
	}


	$scope.$on("deleteInstance", function(event, request) {
		// $log.debug("deleteInstance: request:");
		// $log.debug(request);
		$scope.init();
		// Use this line instead of the subsequent $http.post to change from POST to DELETE
		// $http["delete"]($scope.baseUrl + request.url,{timeout: $scope.responseTimeoutMsec}).then($scope.requestOkay
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
		var msecs = PropertyService.retrieveMsoMaxPollingIntervalMsec(1000);
		PropertyService.setMsoMaxPollingIntervalMsec(msecs);
		
		// takes a default value, retrieves the prop value from the file system and sets it
		var polls = PropertyService.retrieveMsoMaxPolls(7);
		PropertyService.setMsoMaxPolls(polls);
		
		//PropertyService.setMsoBaseUrl("testmso");
		PropertyService.setServerResponseTimeoutMsec(10000);

		/*
		 * Common parameters that would typically be set when the page is
		 * displayed for a specific service instance id.
		 */

//		DataService.setSubscriberName("Mobility");
//		DataService.setGlobalCustomerId("CUSTID12345")
//		DataService.setServiceType("Mobility Type 1");
//		DataService.setServiceName("Mobility Service 1");
//		DataService.setServiceInstanceId("mmsc-test-service-instance");

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

		//subscriberId=jimmy-testing&serviceType=gamma-01%2F1&serviceInstanceId=jimmy2-01%2F01%2F%2F01
		$scope.globalCustomerId = $location.search().subscriberId;
		$scope.serviceType = $location.search().serviceType;
		$scope.serviceInstanceId = $location.search().serviceInstanceId;
		
		//$scope.getAsdcModel($location.search().modelUuid);
		
		// this should be in a config file?
		$scope.namedQueryId = "ed0a0f5b-cf79-4784-88b2-911cd726cd3d";
		$scope.url = PropertyService.getAaiBaseUrl() + "/aai_sub_viewedit" +
		"/" + encodeURIComponent($scope.namedQueryId) +
		"/" + encodeURIComponent($scope.globalCustomerId) + 
		"/" + encodeURIComponent($scope.serviceType) + 
		"/" + encodeURIComponent($scope.serviceInstanceId) + '?r=' + Math.random();

		$scope.status = "Fetching service instance data from A&AI for service-instance-id=" + $scope.serviceInstanceId;
		$http.get($scope.url, {
		},{
			timeout: $scope.responseTimeoutMsec
		}).then($scope.handleInitialResponseInventoryItems)
		["catch"]($scope.handleServerError);
	}

	$scope.handleServerError = function(response, status) {				
		alert(response.statusText);
	}
	
	$scope.getAsdcModel = function(disData) {
		$http.get(PropertyService.getAaiBaseUrl() + '/rest/models/services')
		.then(function successCallback(response) {
			var myUuid = null;
			var lastVersion = -1;
			angular.forEach(response.data, function(model, key) {
				if (angular.equals(model.invariantUUID,disData.personaModelId)) {
					if (model.version > lastVersion) {
						lastVersion = model.version;
						myUuid = model.uuid;
					}
					
				}
			});
			if (myUuid == null)
			{
				console.log("aaiSubscriber getAsdcModel - No matching model found matching the persona Model Id = " + disData.personaModelId);
			}
			else
			{
				console.log(myUuid);
				$http.get(PropertyService.getAaiBaseUrl() + '/rest/models/services/' + myUuid)
				.then(function successCallback(response2) {
					vidService.setModel(response2.data);
					window.location.href = "#/instantiate?subscriberId=" + disData.globalCustomerId + "&serviceType=" + disData.serviceType + "&serviceInstanceId=" + disData.serviceInstanceId;
					console.log("aaiSubscriber getAsdcModel DONE!!!!");
				});
			}
		}, function errorCallback(response) {
			//TODO
		});
	}
	
	$scope.getTenants = function(globalCustomerId) {
		$http.get(PropertyService.getAaiBaseUrl() + '/aai_get_tenants' + globalCustomerId + '?r=' + Math.random())
		.then(function successCallback(response) {
			return response.data;
			//$location.path("/instantiate");
		}, function errorCallback(response) {
			//TODO
		});
	}
	
	$scope.handleInitialResponseInventoryItems = function(response) {
		// $log.debug("handleInitialResponse: response contents:");
		// $log.debug(response);
		try {

			if (response.status < 200 || response.status > 202) {
				$scope.handleServerError(response, response.status);
				return;
			}

			$scope.inventoryResponseItemList = response.data["inventory-response-item"]; // get data from json
			console.log($scope.inventoryResponseItemList.toString());

			$scope.displayData = [];
			$scope.vnfs = [];

			$scope.counter = 100;

			angular.forEach($scope.inventoryResponseItemList, function(inventoryResponseItem, key) {

				$scope.inventoryResponseItem = inventoryResponseItem;

				$scope.serviceInstanceToCustomer = DataService.getServiceInstanceToCustomer();
				var subscriberName = "";
				angular.forEach($scope.serviceInstanceToCustomer, function(servInst, key2) {
					if (servInst.serviceInstanceId === $scope.serviceInstanceId) {
						subscriberName = servInst.subscriberName;
					}
				});
				$scope.service.instance = {
						"name": $scope.inventoryResponseItem["service-instance"]["service-instance-name"],
						"serviceInstanceId": $scope.serviceInstanceId,
						"serviceType": $scope.serviceType,
						"globalCustomerId": $scope.globalCustomerId,
						"subscriberName": subscriberName,
						"id": $scope.serviceInstanceId,
						"inputs": {
							"a": {
								"type": "String",
								"description": "This variable is 'a'",
								"default": "A default"
							},
							"b": {
								"type": "String",
								"description": "This variable is 'b'",
								"default": "B default"
							},
						},
						"object": $scope.inventoryResponseItem["service-instance"],
						"vnfs": [],
						"networks": []
				}

				if (inventoryResponseItem["inventory-response-items"] != null) {

					angular.forEach(inventoryResponseItem["inventory-response-items"]["inventory-response-item"], function(subInventoryResponseItem, key) {
						// i expect to find vnfs now

						if (subInventoryResponseItem["l3-network"] != null) { 
							var l3NetworkObject = subInventoryResponseItem["l3-network"];
							var l3Network = { "id": $scope.counter++, 
									"name": l3NetworkObject["network-name"],
									"itemType": "l3-network",
									"nodeId": l3NetworkObject["network-id"],
									"nodeType": l3NetworkObject["network-type"],
									"nodeStatus": l3NetworkObject["orchestration-status"],
									"object": l3NetworkObject,
									"nodes": []
							};
							$scope.service.instance["networks"].push(l3Network);
						}

						if (subInventoryResponseItem["generic-vnf"] != null) {
							var genericVnfObject = subInventoryResponseItem["generic-vnf"];

							var genericVnf = {
									"name": genericVnfObject["vnf-name"],
									"id": $scope.counter++, 
									"itemType": "vnf", 
									"nodeType": genericVnfObject["vnf-type"],
									"nodeId": genericVnfObject["vnf-id"],
									"nodeStatus": genericVnfObject["orchestration-status"],
									"object": genericVnfObject,
									"vfModules": [],
									"volumeGroups": [],
									"availableVolumeGroups": []
							};
							$scope.service.instance["vnfs"].push(genericVnf);

							// look for volume-groups
							if (subInventoryResponseItem["inventory-response-items"] != null) {
								angular.forEach(subInventoryResponseItem["inventory-response-items"]["inventory-response-item"], function(vfmodules, key) {

									if (vfmodules["volume-group"] != null) { 
										var volumeGroupObject = vfmodules["volume-group"];
										var volumeGroup = { "id": $scope.counter++, 
												"name": volumeGroupObject["volume-group-name"],
												"itemType": "volume-group",
												"nodeId": volumeGroupObject["volume-group-id"],
												"nodeType": volumeGroupObject["vnf-type"],
												"nodeStatus": volumeGroupObject["orchestration-status"],
												"object": volumeGroupObject,
												"nodes": []
										};
										genericVnf["volumeGroups"].push(volumeGroup);
										genericVnf["availableVolumeGroups"].push(volumeGroup);
									}
								});
							}
							// now we've loaded up the availableVolumeGroups, we can use it
							if (subInventoryResponseItem["inventory-response-items"] != null) {
								angular.forEach(subInventoryResponseItem["inventory-response-items"]["inventory-response-item"], function(vfmodules, key) {

									if (vfmodules["vf-module"] != null) { 
										var vfModuleObject = vfmodules["vf-module"];
										var vfModule = { "id": $scope.counter++,
												"name": vfModuleObject["vf-module-name"],
												"itemType": "vf-module",
												"nodeType": "vf-module", 
												"nodeStatus": vfModuleObject["orchestration-status"],
												"volumeGroups": [],
												"object": vfModuleObject,
												"networks": []
										};
										genericVnf["vfModules"].push(vfModule);
										if (vfmodules["inventory-response-items"] != null) {
											angular.forEach(vfmodules["inventory-response-items"]["inventory-response-item"], function(networks, key) {
												if (networks["l3-network"] != null) { 
													var l3NetworkObject = networks["l3-network"];
													var l3Network = { "id": $scope.counter++, 
															"name": l3NetworkObject["network-name"],
															"itemType": "l3-network",
															"nodeId": l3NetworkObject["network-id"],
															"nodeType": l3NetworkObject["network-type"],
															"nodeStatus": l3NetworkObject["orchestration-status"],
															"object": l3NetworkObject,
															"nodes": []
													};
													vfModule["networks"].push(l3Network);
												}
												if (networks["volume-group"] != null) { 
													var volumeGroupObject = networks["volume-group"];

													var volumeGroup = { "id": $scope.counter++, 
															"name": volumeGroupObject["volume-group-name"],
															"itemType": "volume-group",
															"nodeId": volumeGroupObject["volume-group-id"],
															"nodeType": volumeGroupObject["vnf-type"],
															"nodeStatus": volumeGroupObject["orchestration-status"],
															"object": volumeGroupObject,
															"nodes": []
													};
													var tmpVolGroup = [];

													angular.forEach(genericVnf["availableVolumeGroups"], function(avgroup, key) {
														if (avgroup.name != volumeGroup.name) { 
															tmpVolGroup.push(avgroup);
														}
													});

													genericVnf["availableVolumeGroups"] = tmpVolGroup;

													vfModule["volumeGroups"].push(volumeGroup);
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
			
			$scope.setProgress(100); // done
			$scope.status = "Done";
			$scope.isSpinnerVisible = false;
			console.log("HERE!!!");
		} catch (error) {
			console.log(error);
		}
	}

	$scope.handleInitialResponse = function(response) {
		try {
			$scope.enableCloseButton(true);
			$scope.updateLog(response);
			if (response.data.status < 200 || response.data.status > 202) {
				$scope.showError("MSO failure - see log below for details")
				return;
			}

			$scope.setProgress(100); // done
			$scope.status = "Done";
			$scope.isSpinnerVisible = false;

			$scope.customer = response.data.customer; // get data from json

			$scope.customerList = [];

			$scope.serviceInstanceToCustomer = [];
			//$scope.serviceIdList = [];
			angular.forEach($scope.customer, function(subVal, subKey) {
				var cust = { "globalCustomerId": subVal["global-customer-id"], "subscriberName": subVal["subscriber-name"] };
				$scope.customerList.push(cust);
				if (subVal["service-subscriptions"] != null) {
				
					
						angular.forEach(subVal["service-subscriptions"]["service-subscription"], function(serviceSubscription, key) {
							$scope.serviceInstanceId = [];
							if (serviceSubscription["service-type"] != null) {
//								var newVal = { "id" : serviceSubscription["service-type"], "description" : serviceSubscription["service-type"] };
//								if ($scope.serviceIdList.indexOf(newVal) == -1) { 
//									$scope.serviceIdList.push(newVal);
//								}
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
//			DataService.setServiceIdList($scope.serviceIdList);
			DataService.setServiceInstanceToCustomer($scope.serviceInstanceToCustomer);
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

	$scope.pollStatus = function () {
		/*
		 * The "?r=" argument overrides caching. This was needed for Internet Explorer 11.
		 * 
		 * Ideally this should NOT be needed and appears to be an Angular bug.
		 */
		$http.get($scope.baseUrl + "mso_get_orch_req/" + $scope.requestId + "?r=" + Math.random(), {
			cache: false, // This alternative did NOT seem to disable caching but was retained as a reference
			timeout: $scope.responseTimeoutMsec
		}).then($scope.handlePollResponse)
		["catch"]($scope.handleServerError);
	}

	$scope.handlePollResponse = function(response) {
		try {
			// $log.debug("handlePollResponse: response contents:");
			// $log.debug(response);
			$scope.updateLog(response);

			if (response.data.status < 200 || response.data.status > 202) {
				$scope.showError("MSO failure - see log below for details")
				return;
			}

//			UtilityService.checkUndefined("request", response.data.entity.request);
//			UtilityService.checkUndefined("requestStatus", response.data.entity.request.requestStatus);

//			$scope.setProgress(response.data.entity.request.requestStatus.percentProgress);

//			var requestState = response.data.entity.request.requestStatus.requestState;
//			if (requestState == "InProgress") {
//			requestState = "In Progress";
//			}
//			var statusMessage = response.data.entity.request.requestStatus.statusMessage;
//			if (UtilityService.hasContents(statusMessage)) {
//			$scope.status = requestState + " - " + statusMessage;
//			} else {
//			$scope.status = requestState;
//			}
//			if (requestState == "Complete") {
//			$scope.isSpinnerVisible = false;
//			return;
//			}
//			if (requestState == "Failure") {
//			$scope.showError("MSO failure - see log below for details")
//			return;
//			}
//			if (++$scope.pollAttempts > $scope.properties.msoMaxPolls) {
//			$scope.showError("Maximum number of poll attempts exceeded");
//			} else {
//			$scope.timer = $timeout($scope.pollStatus, $scope.properties.msoMaxPollingIntervalMsec);
//			}
		} catch (error) {
			$scope.showContentError(error);
		}				
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
		$scope.showError("System failure" + message);
	}

	$scope.showContentError = function(message) {
		// $log.debug(message);
		console.log(message);
		if (UtilityService.hasContents(message)) {
			$scope.showError("System failure (" + message + ")");
		} else {
			$scope.showError("System failure");
		}
	}

	$scope.showError = function(message) {
		$scope.isSpinnerVisible = false;
		$scope.isProgressVisible = false;
		$scope.error = message;
		$scope.status = "Error";
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
		var selector = "div[ng-controller=msoCommitController] button";

		$scope.isCloseEnabled = isEnabled;

		if (isEnabled) {
			$(selector).addClass("button--primary").removeClass("button--inactive").attr("btn-type", "primary");
		} else {
			$(selector).removeClass("button--primary").addClass("button--inactive").attr("btn-type", "disabled");					
		}
	}

	$scope.resetProgress = function() {
		$scope.percentProgress = 0;
		$scope.progressClass = "progress-bar progress-bar-info";
	}

	$scope.setProgress = function(percentProgress) {
		percentProgress = parseInt(percentProgress);
		if (percentProgress >= 100) {
			$scope.progressClass = "progress-bar progress-bar-success";					
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
	$scope.getSubscriberDet = function(selectedCustomer,selectedServiceInstance){
		if (selectedCustomer != null) { 
			window.location.href = '#/instances/subdetails?selectedSubscriber=' + selectedCustomer;
		} else if (selectedServiceInstance != null) {
			selectedServiceInstance.trim();
			var serviceInstanceToCustomer = $scope.serviceInstanceToCustomer;
			var notFound = true;
			angular.forEach(serviceInstanceToCustomer, function(inst, key) {
				if (inst.serviceInstanceId == selectedServiceInstance) {
					
					notFound = false;
					window.location.href = '#/instances/subdetails?selectedSubscriber=' + inst.globalCustomerId + '&selectedServiceInstance=' + selectedServiceInstance;
				}
			});
			if (notFound) {
				alert("That service instance does not exist.  Please try again.");
			}
		} else {
			alert("Please select a subscriber or enter a service instance");
		}
	};
}
]);


app.controller('TreeCtrl', ['$scope', function ($scope) {
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
		$scope.$broadcast('angular-ui-tree:collapse-all');
	};

	$scope.expandAll = function () {
		$scope.$broadcast('angular-ui-tree:expand-all');
	};


}]);



