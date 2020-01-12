/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 IBM.
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

	appDS2.controller("ServiceModelController",function ($uibModal, $scope, $http, $location, COMPONENT, VIDCONFIGURATION, FIELD, DataService, vidService,
			PropertyService, UtilityService, AsdcService, $timeout, featureFlags) {

		$scope.popup = {};
		var defaultViewPerPage = 10;
	//	var baseEndpoint = "vid";
		var pathQuery = COMPONENT.SERVICES_DIST_STATUS_PATH + VIDCONFIGURATION.ASDC_MODEL_STATUS;
		
		if ( VIDCONFIGURATION.ASDC_MODEL_STATUS === FIELD.STATUS.ALL) {
			pathQuery = COMPONENT.SERVICES_PATH;
		}
        window.addEventListener("message", receiveMessage, false);

		function receiveMessage(event){
            if(event.data == 'navigateTo') {
                $location.path('/models/services').search({});
                $scope.$apply();
                $scope.rememberFilter = true;
            }
            if(event.data == 'navigateToInstantiationStatus') {
                $location.path('/instantiationStatus').search({});
                $scope.$apply();
            }
		}

		$scope.getServiceModels = function() {
			$scope.status = FIELD.STATUS.FETCHING_SERVICE_CATALOG_ASDC;

			$http.get(pathQuery)
			.then(function (response) {
				$scope.services = [];
				if (response.data && angular.isArray(response.data.services)) {
					wholeData = response.data.services;
                    $scope.services = $scope.filterDataWithHigherVersion(wholeData);
                    $scope.viewPerPage = defaultViewPerPage;
                    $scope.totalPage=$scope.services.length/$scope.viewPerPage;
                    $scope.sortBy=COMPONENT.NAME;
                    $scope.scrollViewPerPage=2;
                    $scope.currentPage=1;
                    $scope.currentPageNum=1;
                    $scope.isSpinnerVisible = false;
                    $scope.isProgressVisible = false;
					var searchKey = sessionStorage.getItem("searchKey");
                    if (searchKey != 'undefined' && searchKey!=null && ($scope.rememberFilter)) {
                        var searchKey = JSON.parse(sessionStorage.getItem("searchKey"));
                        $scope.searchString = searchKey.searchString || '';
                        $scope.viewPerPage = searchKey.viewPerPage || defaultViewPerPage;
                        $scope.totalPage = $scope.services.length / $scope.viewPerPage;
                        $timeout(function () {
                        	// the table controller handles the current page once
							// data is loaded, therefore we're delying the paging
							// override
                            $scope.currentPage = $scope.currentPageNum = searchKey.currentPage ? parseInt(searchKey.currentPage) : 1;
                        }, 0);
                        $scope.rememberFilter = false;
                    }
				} else {
					$scope.status = FIELD.STATUS.FAILED_SERVICE_MODELS_ASDC;
					$scope.error = true;
					$scope.isSpinnerVisible = false;
				}
				$scope.deployButtonType = response.data.readOnly ? 'disabled' : 'primary';
			}, function (response) {
				console.log("Error: " + response);
			});
		};

		$scope.isShowOrchestrationType = function() {
			return featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_SHOW_ORCHESTRATION_TYPE);
		};

		var wholeData=[];

		$scope.filterDataWithHigherVersion = function(serviceData){
			var delimiter = '$$';
			var fiterDataServices = {};
			for(var i=0;i<serviceData.length;i++) {
				var index = serviceData[i].invariantUUID.trim() + delimiter + serviceData[i].name.trim();
				if(!fiterDataServices[index]) {
                    var hasPreviousVersion = false;
                    fiterDataServices[index] = {
                        service: serviceData[i],
                        hasPreviousVersion: false
                    };
                }
                else {
                    fiterDataServices[index].hasPreviousVersion = true;
                    if(parseFloat(serviceData[i].version.trim())>parseFloat(fiterDataServices[index].service.version.trim())) {
                        fiterDataServices[index].service = serviceData[i];
					}
				}
			}
            return Object.keys(fiterDataServices).map(function(key) {
            	var service = fiterDataServices[key].service;
            	service.hasPreviousVersion = fiterDataServices[key].hasPreviousVersion;
                return  service;
            });
		};

		$scope.init = function() {
    		var msecs = PropertyService.retrieveMsoMaxPollingIntervalMsec();
    		PropertyService.setMsoMaxPollingIntervalMsec(msecs);
    		
    		var polls = PropertyService.retrieveMsoMaxPolls();
    		PropertyService.setMsoMaxPolls(polls);
        };
		
		$scope.prevPage = function() {
			$scope.currentPage--;
		};
		
		$scope.nextPage = function() {
			$scope.currentPage++;
		};

		$scope.showReportWindow = function() {

			const modalWindow = $uibModal.open({
				templateUrl: 'app/vid/scripts/modals/report-modal/report-modal.html',
				controller: 'reportModalController',
				controllerAs: 'vm',
				resolve: {
					errorMsg: function () {
						return $scope.status;
					}
				}
			});

		};

		$scope.isShowErrorReport = function() {
			return featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_CREATE_ERROR_REPORTS);
		};
		
		$scope.createType = COMPONENT.A_LA_CARTE;
		$scope.deployService = function(service) {
			var searchKey = {
				searchString: $scope.searchString,
                viewPerPage: $scope.viewPerPage,
                currentPage: $scope.currentPage
			};
			DataService.setIsInstantiationTemplateExists(service.isInstantiationTemplateExists);
			sessionStorage.setItem("searchKey",JSON.stringify(searchKey));

			console.log("Instantiating SDC service " + service.uuid);

			$http.get(COMPONENT.SERVICES_PATH + service.uuid)
				.then(function (getServiceResponse) {

					var serviceModel = getServiceResponse.data;

					//VID-233 bug fix when models doesn't exists
					if(typeof(serviceModel)==="string"){  //not an object
						$scope.status = FIELD.STATUS.FAILED_SERVICE_MODELS_ASDC;
						$scope.error = true;
						$scope.isSpinnerVisible = false;
						$scope.isProgressVisible = true;
						return;
					} else{ //clean error message
						$scope.status = "";
						$scope.error = false;
						$scope.isSpinnerVisible = false;
						$scope.isProgressVisible = false;
					}

					DataService.setServiceName(serviceModel.service.name);

					//VOLTE services need input list generated and macro style
					DataService.setE2EService(false);
					if(serviceModel.service.category === 'E2E Service') {
						DataService.setE2EService(true);
						DataService.setHideServiceFields(false);
						VIDCONFIGURATION.MACRO_SERVICES.push(serviceModel.service.invariantUuid);
					}

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

                    var shouldTakeTheAsyncInstantiationFlow = AsdcService.shouldTakeTheAsyncInstantiationFlow(serviceModel);
                    DataService.setShouldIncludeInAsyncInstantiationFlow(shouldTakeTheAsyncInstantiationFlow);

                    DataService.setALaCarte (true);
                    DataService.setPnf(!angular.equals(serviceModel.pnfs, {}));
					$scope.createType = COMPONENT.A_LA_CARTE;
					var broadcastType = COMPONENT.CREATE_COMPONENT;
                    if (AsdcService.isMacro(serviceModel) || DataService.getE2EService()) {
                        DataService.setALaCarte(false);
                        if(!shouldTakeTheAsyncInstantiationFlow){
                            $scope.createType = COMPONENT.MACRO;
                            var convertedAsdcModel = UtilityService.convertModel(serviceModel);

                            DataService.setModelInfo(COMPONENT.SERVICE, {
                                "modelInvariantId": serviceModel.service.invariantUuid,
                                "modelVersion": serviceModel.service.version,
                                "modelNameVersionId": serviceModel.service.uuid,
                                "modelName": serviceModel.service.name,
                                "description": serviceModel.service.description,
                                "category": serviceModel.service.category,
                                "serviceEcompNaming": serviceModel.service.serviceEcompNaming,
                                "inputs": serviceModel.service.inputs,
                                "serviceType": serviceModel.service.serviceType,
                                "serviceRole": serviceModel.service.serviceRole,
                                "displayInputs": convertedAsdcModel.completeDisplayInputs
                            });
                        }
                    }

					$scope.$broadcast(broadcastType, {
					    componentId : COMPONENT.SERVICE,
                        modelNameVersionId: serviceModel.service.uuid,
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
				}, function (response) {
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
		};

		$scope.refreshSubs = function(subscriberId, serviceType, serviceInstanceId) {
			$scope.status = FIELD.STATUS.FETCHING_SUBSCRIBER_LIST_AAI;
			$scope.init();
			$http.get( FIELD.ID.AAI_REFRESH_FULL_SUBSCRIBERS, {

			},{
				timeout: $scope.responseTimeoutMsec
			}).then(function(response){
				
				if (response.data.status < 200 || response.data.status > 202) {
					$scope.showError(FIELD.ERROR.MSO);
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
