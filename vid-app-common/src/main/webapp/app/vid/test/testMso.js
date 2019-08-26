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

app.config(function($logProvider){
    // Optionally set to "false" to disable debug logging.
    $logProvider.debugEnabled(true);
});

app.controller("testController", [ "$scope", "$timeout", "$cookieStore", "$log", "COMPONENT", "DataService", "PropertyService",
    function($scope, $timeout, $cookieStore, $log, COMPONENT, DataService, PropertyService) {

		$scope.popup = new Object();
		$scope.isTestMode = false;

		$scope.init = function(properties) {
	    		/*
	    		 * These 2 statements should be included in non-test code.
	    		 */
			PropertyService.setMsoMaxPollingIntervalMsec(properties.msoMaxPollingIntervalMsec);
			PropertyService.setMsoMaxPolls(properties.msoMaxPolls);

			/*
			 * "setTestMode" is only used for testing.
			 */

			setTestMode();
			
			DataService.setSubscriberName("Mobility");
			DataService.setGlobalCustomerId("CUSTID12345");
			DataService.setServiceType("Mobility Type 1");
			DataService.setServiceName("Mobility Service 1");
			DataService.setServiceInstanceId("mmsc-test-service-instance");
			DataService.setVnfInstanceId("abcd-12345-56789");
			DataService.setVfModuleInstanceId("xye-99990123213");
		};
		
		var TEST_MODE_COOKIE = "isTestModeEnabled";

		var defaultMsoBaseUrl = PropertyService.getMsoBaseUrl();

		var setTestMode = function() {
		    setTestMsoMode($cookieStore.get(TEST_MODE_COOKIE));
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
		
		$scope.autoStartCommitTest = function() {
			/*
			 * Optionally comment in / out one of these method calls (or add a similar
			 * entry) to auto-invoke an entry when the test screen is redrawn.
			 */
			$timeout(function() {
				// $scope.createServiceInstance();
				// $scope.deleteServiceInstance();
				// $scope.generateInvalidUrl404();			
			}, 500);
		};
		
		$scope.autoStartQueryTest = function() {
			/*
			 * Optionally comment in / out one of these method calls (or add a similar
			 * entry) to auto-invoke an entry when the test screen is redrawn.
			 */
			$timeout(function() {
				// $scope.queryServiceInstance();
			}, 500);
		};
		
		$scope.queryServiceInstance = function() {
			/*
			 * Example of method call needed to show service instance details.
			 */
			$scope.$broadcast("showComponentDetails", {
			    componentId : COMPONENT.SERVICE
			});
		};
	
		$scope.createServiceInstance = function() {
			/*
			 * Example of method call needed to commit an instance creation request.
			 */
			$scope.$broadcast("createInstance", {
				url : "mso_create_svc_instance",
				requestDetails : createServiceRequestDetails
			});
		};
	
		$scope.deleteServiceInstance = function() {
			/*
			 * Example of method call needed to commit an instance deletion request.
			 */
			$scope.$broadcast("deleteInstance",	{
				url : "mso_delete_svc_instance/bc305d54-75b4-431b-adb2-eb6b9e546014",
				requestDetails : deleteServiceRequestDetails
			});
		};
	
		$scope.createVNFInstance = function() {
			/*
			 * Example of method call needed to commit an instance creation request.
			 */
			$scope.$broadcast("createInstance", {
				url : "mso_create_vnf_instance/bc305d54-75b4-431b-adb2-eb6b9e546099",
				requestDetails : createVnfRequestDetails
			});
		};
	
		$scope.deleteVNFInstance = function() {
			/*
			 * Example of method call needed to commit an instance deletion request.
			 */
			$scope.$broadcast("deleteInstance",	{
				url : "mso_delete_vnf_instance/bc305d54-75b4-431b-adb2-eb6b9e546014/vnfs/ab9000-0009-9999",
				requestDetails : deleteVnfRequestDetails
			});
		};
		
		$scope.createVolumeGroupInstance = function() {
			/*
			 * Example of method call needed to commit an instance creation request.
			 */
			$scope.$broadcast("createInstance", {
				url : "mso_create_volumegroup_instance/bc305d54-75b4-431b-adb2-eb6b9e546099/vnfs/fe305d54-75b4-431b-adb2-eb6b9e546fea",
				requestDetails : createVolumeGroupRequestDetails
			});
		};
	
		$scope.deleteVolumeGroupInstance = function() {
			/*
			 * Example of method call needed to commit an instance deletion request.
			 */
			$scope.$broadcast("deleteInstance",	{
				url : "mso_delete_volumegroup_instance/bc305d54-75b4-431b-adb2-eb6b9e546014/vnfs/fe305d54-75b4-431b-adb2-eb6b9e546fea/volumeGroups/fe9000-0009-9999",
				requestDetails : deleteVolumeGroupRequestDetails
			});
		};
		$scope.createVFModuleInstance = function() {
			/*
			 * Example of method call needed to commit an instance creation request.
			 */
			$scope.$broadcast("createInstance", {
				url : "mso_create_vfmodule_instance/bc305d54-75b4-431b-adb2-eb6b9e546099/vnfs/111-111-111-111",
				requestDetails : createVFModuleRequestDetails
			});
		};
	
		$scope.deleteVFModuleInstance = function() {
			/*
			 * Example of method call needed to commit an instance deletion request.
			 * 
			 */
			$scope.$broadcast("deleteInstance",	{
				url : "mso_delete_vfmodule_instance/2/vnfs/ab/vfModules/a1",
				requestDetails : deleteVFModuleRequestDetails
			});
		}
		$scope.createNetworkInstance = function() {
			$scope.$broadcast("createInstance", {
				url : "mso_create_nw_instance/789098877777",
				requestDetails : createNetworkRequestDetails
			});
		};
	
		$scope.deleteNetworkInstance = function() {
			$scope.$broadcast("deleteInstance",	{
				url : "mso_delete_nw_instance/bc305d54-75b4-431b-adb2-eb6b9e546014/networks/ff305d54-75b4-ff1b-fff1-eb6b9e5460ff",
				requestDetails : deleteNetworkRequestDetails
			});
		};
	
		$scope.generateError = function(testName) {
			// Clone example request object
			var request = JSON.parse(JSON.stringify(createServiceRequestDetails));
			request.modelInfo.modelName = testName;
			$scope.$broadcast("createInstance", {
				url : "mso_create_svc_instance",
				requestDetails : request
			});
		};
	
		$scope.generateInvalidUrl404 = function() {
		    	var baseUrl = PropertyService.getMsoBaseUrl();
		    	PropertyService.setMsoBaseUrl("/INVALID_STRING/");

			$scope.$broadcast("createInstance", {
				url : "mso_create_svc_instance",
				requestDetails : createServiceRequestDetails,
				callbackFunction : function() {
				    PropertyService.setMsoBaseUrl(baseUrl);
				    $scope.popup.isVisible = false;
				}
			});	
		};
	
		$scope.generateInvalidUrl405 = function() {
			$scope.$broadcast("createInstance", {
				url : "INVALID_STRING_mso_create_svc_instance",
				requestDetails : createServiceRequestDetails
			});
		};
	
		/*
		 * Test data objects:
		 */
		/*var requestParameters = {
				"subscriptionServiceType":"ef5256d1-5a33-aadf-13ab-12abad84e764",
				"userParams":[{"name":"goldenr","value":"Willie"}, {"name":"lab","value":"Jackson"}, {"name":"goldend","value":"Max"}]} */
		var requestParameters = { "subscriptionServiceType":"ef5256d1-5a33-aadf-13ab-12abad84e764", userParams:[] };
		var requestInfo = {
				instanceName: "sn5256d1-5a33-55df-13ab-12abad84e764",
				productFamilyId: "sn5256d1-5a33-55df-13ab-12abad84edde",
				source: "VID",
				suppressRollback: true
		};
		var subscriberInfo = {
			globalSubscriberId : "C12345",
			subscriberName : "General Electric Division 12"
		};
	
		var cloudConfiguration = {
				lcpCloudRegionId: "cloudregion1",
				tenantId: "df5256d1-5a33-55df-13ab-12abad843456"
				
		};
		var createServiceRequestDetails = {
			modelInfo : {
				modelType : "service",
				modelInvariantId : "sn5256d1-5a33-55df-13ab-12abad84e764",
				modelNameVersionId : "ab6478e4-ea33-3346-ac12-ab121484adca",
				modelName : "WanBonding",
				modelVersion : "1",
				modelCustomizationName: ""
			},
			subscriberInfo : subscriberInfo,
			requestInfo : requestInfo,
			requestParameters : requestParameters
		};
	
		var deleteServiceRequestDetails = {
				modelInfo : {
					modelType : "service",
					modelInvariantId : "sn5256d1-5a33-55df-13ab-12abad84e764",
					modelNameVersionId : "ab6478e4-ea33-3346-ac12-ab121484adca",
					modelName : "WanBonding",
					modelVersion : "1",
					modelCustomizationName: ""
				},
				requestInfo : requestInfo
		};
		
		var createVFModuleRequestDetails = {
				modelInfo : {
					modelType : "VFModule",
					modelInvariantId : "ab5256d1-5a33-55df-13ab-12abad84e764",
					modelNameVersionId : "9b6478e4-ea33-3346-ac12-ab121484adc2",
					modelName : "model1",
					modelVersion : "1",
					modelCustomizationName: ""
				},
				cloudConfiguration: cloudConfiguration,
				requestInfo : requestInfo,
				relatedInstanceList: [
										{
										    relatedInstance: {
										       instanceId: "c3514e3-5a33-55df-13ab-12abad84e7cc",
										       modelInfo: {   
										          modelType: "volumeGroup",
										          modelInvariantId: "ff3514e3-5a33-55df-13ab-12abad84e7ff",
										          modelNameVersionId: "fe6985cd-ea33-3346-ac12-ab121484a3fe",
										          modelName: "parentServiceModelName",
										          modelVersion: "1.0"
										       }
										    }
										 },
				                        {
				                           relatedInstance: {
				                              instanceId: "c3514e3-5a33-55df-13ab-12abad84e7cc",
				                              modelInfo: {   
				                                 modelType: "service",
				                                 modelInvariantId: "ff3514e3-5a33-55df-13ab-12abad84e7ff",
				                                 modelNameVersionId: "fe6985cd-ea33-3346-ac12-ab121484a3fe",
				                                 modelName: "parentServiceModelName",
				                                 modelVersion: "1.0"
				                              }
				                           }
				                        },
				                        {
				                           relatedInstance: {
				                              instanceId: "fab256d1-5a33-55df-13ab-12abad8445ff34",
				                              modelInfo: {
				                                 modelType: "vnf",
				                                 modelInvariantId: "ff5256d1-5a33-55df-13ab-12abad84e7ff",
				                                 modelNameVersionId: "fe6478e4-ea33-3346-ac12-ab121484a3fe",
				                                 modelName: "vSAMP12",
				                                 modelVersion: "1.0",
				                                 modelCustomizationName: "vSAMP12 1"
				                              }
				                           }
				                        }

				                     ],
				                     requestParameters : requestParameters
			};
		
			var deleteVFModuleRequestDetails = {
					modelInfo : {
						modelType : "VFModule",
						modelInvariantId : "ab5256d1-5a33-55df-13ab-12abad84e764",
						modelNameVersionId : "9b6478e4-ea33-3346-ac12-ab121484adc2",
						modelName : "model1",
						modelVersion : "1",
						modelCustomizationName: ""
					},
					cloudConfiguration: cloudConfiguration,
					requestInfo : requestInfo,
					
			};
			
		var createVnfRequestDetails = {
				modelInfo : {
					modelType : "vnf",
					modelInvariantId : "ab5256d1-5a33-55df-13ab-12abad84efc2",
					modelNameVersionId : "9b6478e4-ea33-3346-ac12-ab1214847890",
					modelName : "model1",
					modelVersion : "1",
					modelCustomizationName: ""
				},
				requestInfo : requestInfo,
				cloudConfiguration : cloudConfiguration,
				relatedInstanceList: [
					{
					    relatedInstance: {
					       instanceId: "c3514e3-5a09-55df-13ab-1babad84e7cc",
					       modelInfo: {   
					          modelType: "service",
					          modelInvariantId: "ff3514e3-5a33-55df-13ab-12abad84e7ff",
					          modelNameVersionId: "fe6985cd-ea33-3346-ac12-ab121484a3fe",
					          modelName: "parentServiceModelName",
					          modelVersion: "1.0"
					       }
					    }
					 }
					]
			};
		
			var deleteVnfRequestDetails = {
					modelInfo : {
						modelType : "vnf",
						modelInvariantId : "ab5256d1-5a33-55df-13ab-12abad84efc2",
						modelNameVersionId : "9b6478e4-ea33-3346-ac12-ab1214847890",
						modelName : "model1",
						modelVersion : "1",
						modelCustomizationName: ""
					},
					cloudConfiguration : cloudConfiguration,
					requestInfo : requestInfo	
			};
			var createVolumeGroupRequestDetails = {
				modelInfo : {
						modelType : "volumeGroup",
						modelInvariantId : "ab5256d1-5a33-55df-13ab-12abad84efda",
						modelNameVersionId : "9b6478e4-ea33-3346-ac12-ab12148478fa",
						modelName : "model1",
						modelVersion : "1",
						modelCustomizationName: ""
					},
					cloudConfiguration : cloudConfiguration,
					requestInfo : requestInfo,
					relatedInstanceList: [
                      {
                    	  relatedInstance: {
                    		  instanceId: "c3514e3-5a33-55df-13ab-12abad84e7cc",
                    		  modelInfo: {   
                    			  modelType: "service",
                    			  modelInvariantId: "ff3514e3-5a33-55df-13ab-12abad84e7ff",
                    			  modelNameVersionId: "fe6985cd-ea33-3346-ac12-ab121484a3fe",
                    			  modelName: "parentServiceModelName",
                    			  modelVersion: "1.0"
                    		  }
                    	  }
                      },
                      {
                    	  relatedInstance: {
                    		  instanceId: "fab256d1-5a33-55df-13ab-12abad8445ff34",
                    		  modelInfo: {
                    			  modelType: "vnf",
                    			  modelInvariantId: "ff5256d1-5a33-55df-13ab-12abad84e7ff",
                    			  modelNameVersionId: "fe6478e4-ea33-3346-ac12-ab121484a3fe",
                    			  modelName: "vSAMP12",
                    			  modelVersion: "1.0",
                    			  modelCustomizationName: "vSAMP12 1"
                    		  }
                    	  }
                      }
                      ]

				};
			
				var deleteVolumeGroupRequestDetails = {
					modelInfo : {
						modelType : "volumeGroup",
						modelInvariantId : "ab5256d1-5a33-55df-13ab-12abad84efda",
						modelNameVersionId : "9b6478e4-ea33-3346-ac12-ab12148478fa",
						modelName : "vIsbcOamNetwork",
						modelVersion : "1",
						modelCustomizationName: ""
					},
					cloudConfiguration : cloudConfiguration,
					requestInfo : requestInfo
				};
			
		var createNetworkRequestDetails = {
			modelInfo : {
				modelType : "network",
				modelInvariantId : "ab5256d1-5a33-55df-13ab-12abad84e890",
				modelNameVersionId : "fe6478e4-ea33-3346-aaaa-ab121484a3fa",
				modelName : "vIsbcOamNetwork",
				modelVersion : "1",
				modelCustomizationName: ""
			},
			cloudConfiguration: cloudConfiguration,
			requestInfo : requestInfo
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
	}
]);
