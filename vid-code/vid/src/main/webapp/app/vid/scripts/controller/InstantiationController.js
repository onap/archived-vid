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

/**
 * The Instantiation (or View/Edit) Controller controls the instantiation/removal of
 * deployable objects (Services, VNFs, VF-Modules, Networks, and Volume-Groups)
 */
(function () {
	"use strict";

	app.requires.push('ui.tree');
	
	app.controller("InstantiationController", function ($scope, $route, $location, $timeout, COMPONENT, DataService, PropertyService, UtilityService, $http, vidService) {
		
		$scope.popup = new Object();
		$scope.defaultBaseUrl = "";
		$scope.responseTimeoutMsec = 60000;
		$scope.properties = UtilityService.getProperties();
        $scope.init = function() {

            /*
             * These 2 statements should be included in non-test code.
             */
        	// takes a default value, retrieves the prop value from the file system and sets it
    		var msecs = PropertyService.retrieveMsoMaxPollingIntervalMsec(1000);
    		PropertyService.setMsoMaxPollingIntervalMsec(msecs);
    		
    		// takes a default value, retrieves the prop value from the file system and sets it
    		var polls = PropertyService.retrieveMsoMaxPolls(7);
    		PropertyService.setMsoMaxPolls(polls);
    		
    		//PropertyService.setMsoBaseUrl("testmso");
    		PropertyService.setServerResponseTimeoutMsec(10000);

            /*
             * Common parameters that shows an example of how the view edit screen
             * is expected to pass some common service instance values to the
             * popups.
             */

//            DataService.setSubscriberName("Mobility");
//            DataService.setGlobalCustomerId("CUSTID12345")
//            DataService.setServiceType("Mobility Type 1");
//            DataService.setServiceInstanceName("Example Service Instance Name");
//            DataService.setServiceName("Mobility Service 1");
//            DataService.setServiceInstanceId("mmsc-test-service-instance");
//            DataService.setServiceUuid("XXXX-YYYY-ZZZZ");
//            DataService.setUserServiceInstanceName("USER_SERVICE_INSTANCE_NAME");
        }
        
        //PropertyService.setMsoBaseUrl("testmso");

		$scope.convertModel = function(asdcModel) {
			
			if (!asdcModel) return undefined;
			
			var convertedAsdcModel = {
				"service": asdcModel.service,
				"networks": {},
				"vnfs": {}
			};
			
			for (var networkUuid in asdcModel.networks) {
				var networkModel = asdcModel.networks[networkUuid];
				convertedAsdcModel.networks[networkModel.invariantUuid] = {};
				convertedAsdcModel.networks[networkModel.invariantUuid][networkModel.version] = networkModel;
			}
			
			for (var vnfUuid in asdcModel.vnfs) {
				var vnfModel = asdcModel.vnfs[vnfUuid];
				convertedAsdcModel.vnfs[vnfModel.invariantUuid] = {};
				convertedAsdcModel.vnfs[vnfModel.invariantUuid][vnfModel.version] = {
						"uuid": vnfModel.uuid,
						"invariantUuid": vnfModel.invariantUuid,
						"version": vnfModel.version,
						"name": vnfModel.name,
						"modelCustomizationName": vnfModel.modelCustomizationName,
						"inputs": "",
						"description": vnfModel.description,
						"vfModules": {},
						"volumeGroups": {}
				}
				
				for (var vfModuleUuid in asdcModel.vnfs[vnfUuid].vfModules) {
					var vfModuleModel = asdcModel.vnfs[vnfUuid].vfModules[vfModuleUuid];
					convertedAsdcModel.vnfs[vnfModel.invariantUuid][vnfModel.version].vfModules[vfModuleModel.invariantUuid] = {};
					convertedAsdcModel.vnfs[vnfModel.invariantUuid][vnfModel.version].vfModules[vfModuleModel.invariantUuid][vfModuleModel.version] = vfModuleModel;
				}
				
				for (var volumeGroupUuid in asdcModel.vnfs[vnfUuid].volumeGroups) {
					var volumeGroupModel = asdcModel.vnfs[vnfUuid].volumeGroups[volumeGroupUuid];
					convertedAsdcModel.vnfs[vnfModel.invariantUuid][vnfModel.version].volumeGroups[volumeGroupModel.invariantUuid] = {};
					convertedAsdcModel.vnfs[vnfModel.invariantUuid][vnfModel.version].volumeGroups[volumeGroupModel.invariantUuid][volumeGroupModel.version] = volumeGroupModel;
				}
			}
			console.log ("convertedAsdcModel: "); console.log (JSON.stringify ( convertedAsdcModel, null, 4 ) );
			return convertedAsdcModel;
		};
		
		$scope.service = {
			"model": vidService.getModel(),
			"modelByInvariantUuid": $scope.convertModel(vidService.getModel()),
			"instance": vidService.getInstance()
		};

		$scope.deleteNetwork = function(serviceObject, network) {

			console.log("Removing Network " + network.name);

			//Send delete network request to MSO

			//var networks = this.service.instance.networks;

			//networks.splice(networks.indexOf(network), 1);

			//Retrieve updated data from A&AI
            var serviceInstance = serviceObject.object;
			
			DataService.setInventoryItem(network);
			DataService.setModelInfo(COMPONENT.NETWORK, $scope.service.model);
			
			DataService.setSubscriberName(serviceObject['subscriberName']);
			DataService.setServiceType(serviceObject['serviceType']);
			DataService.setServiceInstanceId(serviceInstance['service-instance-id']);
	
			DataService.setGlobalCustomerId(serviceObject['globalCustomerId']);
			DataService.setServiceInstanceName($scope.service.instance.name);
			
			//DataService.setServiceName($scope.service.model.name);
			
			//DataService.setServiceUuid("XXXX-YYYY-ZZZZ");
			//DataService.setUserServiceInstanceName("USER_SERVICE_INSTANCE_NAME");
			
			$scope.$broadcast("deleteComponent", {
			    componentId : COMPONENT.NETWORK,
			    callbackFunction : deleteCallbackFunction
			});
		};

		$scope.deleteService = function(serviceObject) {

			var serviceInstance = serviceObject.object;
			
			console.log("Removing Service " + $scope.service.instance.name);

			DataService.setInventoryItem(serviceInstance);
			//DataService.setModelInfo(COMPONENT.SERVICE, $scope.service.model);
			
			DataService.setModelInfo(COMPONENT.SERVICE, {
				"modelInvariantId": $scope.service.model.service.invariantUuid,
				"modelVersion": $scope.service.model.service.version,
				"modelNameVersionId": $scope.service.model.service.uuid,
				"modelCustomizationName": "",
				"modelName": $scope.service.model.service.name,
				"inputs": ""
			});
			
			DataService.setSubscriberName(serviceObject['subscriberName']);
			DataService.setServiceType(serviceObject['serviceType']);
			DataService.setServiceInstanceId(serviceInstance['service-instance-id']);
	
			DataService.setGlobalCustomerId(serviceObject['globalCustomerId']);
			DataService.setServiceInstanceName($scope.service.instance.name);
			
			DataService.setServiceName($scope.service.model.service.name);
			
			DataService.setServiceUuid($scope.service.model.service.uuid);
			//DataService.setUserServiceInstanceName("USER_SERVICE_INSTANCE_NAME");
			
			$scope.$broadcast("deleteComponent", {
			    componentId : COMPONENT.SERVICE,
			    callbackFunction : deleteServiceInstanceCallbackFunction
			});
		
		};

		$scope.deleteVfModule = function(serviceObject, vfModule, vnf) {

			console.log("Removing VF-Module " + vfModule.name);
			
            var serviceInstance = serviceObject.object;

			DataService.setInventoryItem(vfModule.object);
			
			var svcModel = $scope.service.modelByInvariantUuid;
			var vnfModelInvariantUuid = vnf.object["persona-model-id"];
			var vnfModelVersion = vnf.object["persona-model-version"];
			if (svcModel != null && vnfModelInvariantUuid != null && vnfModelVersion != null )
		    {
				if ( ( UtilityService.hasContents(svcModel.vnfs) && UtilityService.hasContents(svcModel.vnfs[vnfModelInvariantUuid] ) ) &&
						( UtilityService.hasContents(svcModel.vnfs[vnfModelInvariantUuid][vnfModelVersion] ) ) ) {
					var vnfModel = svcModel.vnfs[vnfModelInvariantUuid][vnfModelVersion];
					
					// volume groups don't have persona-model-id/version in a&ai.
					// Their persona-model-id/version is the one for the associated vfModule
					
					var vfModuleInvariantUuid = vfModule.object["persona-model-id"];
					var vfModuleModelVersion = vfModule.object["persona-model-version"];
					
					if ( UtilityService.hasContents(vnfModel) && UtilityService.hasContents(vnfModel.vfModules) && UtilityService.hasContents(vfModuleInvariantUuid)  &&  UtilityService.hasContents(vfModuleModelVersion) ) 
					{
						var vfModelGroupModel = vnfModel.vfModules[vfModuleInvariantUuid][vfModuleModelVersion];
						
						var vfModeluuid = vfModelGroupModel.uuid;
						if (vfModeluuid == null)
							vfModeluuid = "";
						
						var vnfModelCustomizationName = vnfModel.modelCustomizationName;
						if (vnfModelCustomizationName == null)
							vnfModelCustomizationName = "";
						
						var vfModelName = vfModelGroupModel.name;
						if (vfModelName == null)
							vfModelName = "";
						
						var vfModelVersionID = vfModule.object['vf-module-id'];
						if (vfModelVersionID == null)
							vfModelVersionID = "";
						
						DataService.setModelInfo(COMPONENT.VF_MODULE, {
							"modelInvariantId": vfModuleInvariantUuid,
							"modelVersion": vfModuleModelVersion,
							"modelNameVersionId": vfModeluuid,
							"modelCustomizationName": vnfModelCustomizationName,
							"modelName": vfModelName,
							"inputs": ""
						});
						
						DataService.setVnfInstanceId(vnf.object['vnf-id']);
						DataService.setVfModuleInstanceId(vfModelVersionID);
						
						DataService.setSubscriberName(serviceObject['subscriberName']);
						DataService.setServiceType(serviceObject['serviceType']);
						DataService.setServiceInstanceId(serviceInstance['service-instance-id']);
				
						DataService.setGlobalCustomerId(serviceObject['globalCustomerId']);
						DataService.setServiceInstanceName($scope.service.instance.name);
						
						DataService.setServiceName($scope.service.model.service.name);
						
						DataService.setServiceUuid($scope.service.model.service.uuid);
						//DataService.setUserServiceInstanceName("USER_SERVICE_INSTANCE_NAME");
						
						$scope.$broadcast("deleteComponent", {
						    componentId : COMPONENT.VF_MODULE,
						    callbackFunction : deleteCallbackFunction
						});
						
						return;
					}
					
				}
		    }
		
		   console.log("Removing VNF " + vnf.name + " could not proceed due to missing ASDC model information.");
		
			
			//Retrieve updated data from A&AI
		};

		$scope.deleteVnf = function(serviceObject, vnf) {

			console.log("Removing VNF " + vnf.name);
			
			var serviceInstance = serviceObject.object;
			
			DataService.setInventoryItem(vnf.object);
			
			var vnftype = vnf.object['vnf-type'];
			if (vnftype == null)
				vnftype = "";
			else
			{
				var n = vnftype.search("/");
				if (n >= 0)
					vnftype = vnftype.substring(n+1);
			}
			

			var svcModel = $scope.service.modelByInvariantUuid;
			var vnfModelInvariantUuid = vnf.object["persona-model-id"];
			var vnfModelVersion = vnf.object["persona-model-version"];
			if (svcModel != null && vnfModelInvariantUuid != null && vnfModelVersion != null )
		    {
				
				console.log ( "vnf models: "); console.log ( JSON.stringify ($scope.service.modelByInvariantUuid.vnfs, null, 4) );

				var vnfModel = $scope.service.modelByInvariantUuid.vnfs[vnfModelInvariantUuid][vnfModelVersion];
				if (vnfModel == null)
					{
						DataService.setModelInfo(COMPONENT.VNF, {
							"modelInvariantId": vnfModelInvariantUuid,
							"modelVersion": vnfModelVersion,
							"modelNameVersionId": "",
							"modelCustomizationName": vnftype,
							"modelName": "",
							"inputs": ""
						});
					}
				else
					{
						DataService.setModelInfo(COMPONENT.VNF, {
							"modelInvariantId": vnfModelInvariantUuid,
							"modelVersion": vnfModelVersion,
							"modelNameVersionId": vnfModel.uuid,
							"modelCustomizationName": vnftype,
							"modelName": vnfModel.name,
							"inputs": ""
						});
					}
		    }
			else
			{
			   console.log("Removing VNF name = " + vnf.name + " didn't get the correponding model details so sending empty model values to MSO!");
			   DataService.setModelInfo(COMPONENT.VNF, {
					"modelInvariantId": "",
					"modelVersion": "",
					"modelNameVersionId": "",
					"modelCustomizationName": vnftype,
					"modelName": "",
					"inputs": ""
				});
			}
				
			DataService.setVnfInstanceId(vnf.object['vnf-id']);
	
			DataService.setSubscriberName(serviceObject['subscriberName']);
			DataService.setServiceType(serviceObject['serviceType']);
			DataService.setServiceInstanceId(serviceInstance['service-instance-id']);
	
			DataService.setGlobalCustomerId(serviceObject['globalCustomerId']);
			DataService.setServiceInstanceName($scope.service.instance.name);
			
			DataService.setServiceName($scope.service.model.service.name);
			
			DataService.setServiceUuid($scope.service.model.service.uuid);
			//DataService.setUserServiceInstanceName("USER_SERVICE_INSTANCE_NAME");
			
			$scope.$broadcast("deleteComponent", {
			    componentId : COMPONENT.VNF,
			    callbackFunction : deleteCallbackFunction
			});
		
		};
		
		/*
		$scope.deleteVnf = function(serviceObject, vnf) {

			console.log("Removing VNF " + vnf.name);

			//Send delete VF-Module request to MSO

			var svcModel = $scope.service.modelByInvariantUuid;
			var vnfModelInvariantUuid = vnf.object["persona-model-id"];
			var vnfModelVersion = vnf.object["persona-model-version"];
			console.log ( "vnf models: "); console.log ( JSON.stringify ($scope.service.modelByInvariantUuid.vnfs, null, 4) );

			DataService.setInventoryItem(vnf);
			
			var vnfModel = $scope.service.modelByInvariantUuid.vnfs[vnfModelInvariantUuid][vnfModelVersion];
			
			DataService.setModelInfo(COMPONENT.VNF, {
				"modelInvariantId": vnfModelInvariantUuid,
				"modelVersion": vnfModelVersion,
				"modelNameVersionId": vnfModel.uuid,
				"modelCustomizationName": vnfModel.modelCustomizationName,
				"modelName": vnfModel.name,
				"inputs": vnfModel.inputs
			});
			
			DataService.setSubscriberName(serviceObject['globalCustomerId']);
			DataService.setServiceType(serviceObject['serviceType']);
			DataService.setServiceInstanceId(serviceObject['service-instance-id']);
	
			DataService.setGlobalCustomerId(serviceObject['globalCustomerId']);
			DataService.setServiceInstanceName($scope.service.instance.name);
			
			DataService.setServiceName(vnf.name);
			
			DataService.setServiceUuid($scope.service.model.service.uuid);
			//DataService.setUserServiceInstanceName("USER_SERVICE_INSTANCE_NAME");
			
			$scope.$broadcast("deleteComponent", {
			    componentId : COMPONENT.VNF,
			    callbackFunction : deleteCallbackFunction
			});
			
			//var vnfs = this.service.instance.vnfs;

			//vnfs.splice(vnfs.indexOf(vnf), 1);

			//Retrieve updated data from A&AI
		};*/

		$scope.deleteVolumeGroup = function(serviceObject, vnf, vfModule, volumeGroup) {

			console.log("Removing Volume Group " + volumeGroup.name);
			var haveModel = false;
			var svcModel = $scope.service.modelByInvariantUuid;
			
			var vnfModelInvariantUuid = vnf.object["persona-model-id"]; 
			var vnfModelVersion = vnf.object["persona-model-version"];
			
			if ( ( UtilityService.hasContents(vnfModelInvariantUuid) ) && (UtilityService.hasContents(vnfModelVersion) ) ) {
				if ( UtilityService.hasContents(svcModel) && UtilityService.hasContents($scope.service.modelByInvariantUuid.vnfs) ) {
					//console.log ( "vnf models "); console.log (JSON.stringify ($scope.service.modelByInvariantUuid.vnfs, null, 4) );
					if ( ( UtilityService.hasContents($scope.service.modelByInvariantUuid.vnfs[vnfModelInvariantUuid] ) ) &&
							( UtilityService.hasContents($scope.service.modelByInvariantUuid.vnfs[vnfModelInvariantUuid][vnfModelVersion] ) ) ) {
						var vnfModel = $scope.service.modelByInvariantUuid.vnfs[vnfModelInvariantUuid][vnfModelVersion];
						
						// volume groups don't have persona-model-id/version in a&ai.
						// Their persona-model-id/version is the one for the associated vfModule
						
						var vfModuleInvariantUuid = vfModule.object["persona-model-id"];
						var vfModuleModelVersion = vfModule.object["persona-model-version"];
				
						if ( UtilityService.hasContents(vnfModel.volumeGroups) && UtilityService.hasContents(vfModuleInvariantUuid)  &&  UtilityService.hasContents(vfModuleModelVersion) ) {
							
							if ( ( UtilityService.hasContents (vnfModel.volumeGroups[vfModuleInvariantUuid]) ) && 
									(UtilityService.hasContents (vnfModel.volumeGroups[vfModuleInvariantUuid][vfModuleModelVersion]) ) ) {
								var volGroupModel = vnfModel.volumeGroups[vfModuleInvariantUuid][vfModuleModelVersion];
								
								DataService.setModelInfo(COMPONENT.VOLUME_GROUP, {
									"modelInvariantId": vfModuleInvariantUuid,
									"modelVersion": vfModuleModelVersion,
									"modelNameVersionId": volGroupModel.uuid,
									"modelCustomizationName": vnfModel.modelCustomizationName,
									"modelName": volGroupModel.name,
									"inputs": ""
								});
								haveModel = true;
						
							}
						}
					}
				}
			}
			if ( !haveModel ) {
				DataService.setModelInfo(COMPONENT.VOLUME_GROUP, {
					"modelInvariantId": "",
					"modelVersion": "",
					"modelNameVersionId": "",
					"modelCustomizationName": "",
					"modelName": "",
					"inputs": ""
				});
			}

			var serviceInstance = serviceObject.object;
			
            DataService.setInventoryItem(volumeGroup.object);
		
			DataService.setSubscriberName(serviceObject['subscriberName']);
			DataService.setServiceType(serviceObject['serviceType']);
			DataService.setServiceInstanceId(serviceInstance['service-instance-id']);
	
			DataService.setGlobalCustomerId(serviceObject['globalCustomerId']);
			DataService.setServiceInstanceName($scope.service.instance.name);
			
			DataService.setServiceName($scope.service.model.service.name);
			
			DataService.setServiceUuid($scope.service.model.service.uuid);
			DataService.setVnfInstanceId(vnf.nodeId);
			DataService.setVolumeGroupInstanceId(volumeGroup.nodeId);
		
			//DataService.setUserServiceInstanceName("USER_SERVICE_INSTANCE_NAME");
			
			$scope.$broadcast("deleteComponent", {
			    componentId : COMPONENT.VOLUME_GROUP,
			    callbackFunction : deleteCallbackFunction
			});
		};
		
		$scope.deleteVnfVolumeGroup = function(serviceObject, vnf, volumeGroup) {

			console.log("Removing Volume Group " + volumeGroup.name);
            var serviceInstance = serviceObject.object;
			
            DataService.setInventoryItem(volumeGroup.object);
        
			var svcModel = $scope.service.modelByInvariantUuid;
			var vnfModelInvariantUuid = vnf.object["persona-model-id"];
			var vnfModelVersion = vnf.object["persona-model-version"];
			var vnfModel = null;
			
			var volGroupModelInvariantUuid = null;
			var volGroupModelVersion = null;
			
			// send an empty model by default since model is not required for deletes
			DataService.setModelInfo(COMPONENT.VOLUME_GROUP, {});
			
			if ( (volumeGroup.object != null) && ( volumeGroup.object["persona-model-id"] != null ) && 
					(volumeGroup.object["persona-model-version"] != null) ) {
				
				volGroupModelInvariantUuid = volumeGroup.object["persona-model-id"];
				volGroupModelVersion = volumeGroup.object["persona-model-version"];
				
				if (svcModel != null && vnfModelInvariantUuid != null && vnfModelVersion != null ) {
					console.log ( "vnf models: "); console.log ( JSON.stringify ($scope.service.modelByInvariantUuid.vnfs, null, 4) );
					if ( ($scope.service.modelByInvariantUuid.vnfs[vnfModelInvariantUuid] != null) && 
							($scope.service.modelByInvariantUuid.vnfs[vnfModelInvariantUuid][vnfModelVersion]) != null ) {
						
						vnfModel = $scope.service.modelByInvariantUuid.vnfs[vnfModelInvariantUuid][vnfModelVersion];
						if ( (vnfModel.volumeGroups != null) && ( vnfModel.volumeGroups[volGroupModelInvariantUuid] != null ) 
								&& ( vnfModel.volumeGroups[volGroupModelInvariantUuid][volGroupModelVersion] != null ) ) {
							
							var volumeGroupModel = vnfModel.volumeGroups[volGroupModelInvariantUuid][volGroupModelVersion];
						
							DataService.setModelInfo(COMPONENT.VOLUME_GROUP, {		
								"modelInvariantId": volumeGroupModel.invariantUuid,
								"modelVersion": volumeGroupModel.version,
								"modelNameVersionId": volumeGroupModel.uuid,
								"modelName": volumeGroupModel.name,
								"modelCustomizationName": volumeGroupModel.modelCustomizationName,
								"inputs": ""
							});		
						}
					}
			    }
			}
				
			DataService.setVnfInstanceId(vnf.object['vnf-id']);
		
			DataService.setSubscriberName(serviceObject['subscriberName']);
			DataService.setServiceType(serviceObject['serviceType']);
			DataService.setServiceInstanceId(serviceInstance['service-instance-id']);
	
			DataService.setGlobalCustomerId(serviceObject['globalCustomerId']);
			DataService.setServiceInstanceName($scope.service.instance.name);
			
			DataService.setServiceName($scope.service.model.service.name);
			
			DataService.setServiceUuid($scope.service.model.service.uuid);
			DataService.setVnfInstanceId(vnf.nodeId);
			DataService.setVolumeGroupInstanceId(volumeGroup.nodeId);
		
			//DataService.setUserServiceInstanceName("USER_SERVICE_INSTANCE_NAME");
			
			$scope.$broadcast("deleteComponent", {
			    componentId : COMPONENT.VOLUME_GROUP,
			    callbackFunction : deleteCallbackFunction
			});
		};

		$scope.describeNetwork = function(serviceObject, networkObject) {
			var serviceInstance = serviceObject.object;
			var network = networkObject.object;

			//Display popup with additional network information
			DataService.setNetworkInstanceId(network['network-id']);
			DataService.setInventoryItem(network);
			//DataService.setModelInfo(network['network-id'], network);
			
			DataService.setSubscriberName(serviceObject['subscriber-name']);
			DataService.setServiceType(serviceObject['serviceType']);
			DataService.setServiceInstanceId(serviceInstance['service-instance-id']);
			
			//DataService.setVnfInstanceId("Testing");
			$scope.$broadcast("showComponentDetails", {
			    componentId : COMPONENT.NETWORK
			});
		};

		// for service instance id - no need for this!
		$scope.describeService = function(serviceObject) {
			var serviceInstance = serviceObject.object;
		
			DataService.setInventoryItem(serviceInstance);
			//DataService.setModelInfo(serviceInstance['service-instance-id'], serviceInstance);
			
			DataService.setSubscriberName(serviceObject['subscriberName']);
			DataService.setServiceType(serviceObject['serviceType']);
			DataService.setServiceInstanceId(serviceInstance['service-instance-id']);
			
			//Display popup with additional service information
			$scope.$broadcast("showComponentDetails", {
			    componentId : COMPONENT.SERVICE
			});
			
		};

		$scope.describeVfModule = function(serviceObject, vfModuleObject) {
			var serviceInstance = serviceObject.object;
			var vfModule = vfModuleObject.object;

			//Display popup with additional VF-Module information
			DataService.setVfModuleInstanceId(vfModule['vf-module-id']);
			DataService.setInventoryItem(vfModule);
			//DataService.setModelInfo(vfModule['vf-module-id'], vfModule);
			
			DataService.setSubscriberName(serviceObject['subscriberName']);
			DataService.setServiceType(serviceObject['serviceType']);
			DataService.setServiceInstanceId(serviceInstance['service-instance-id']);
			
			$scope.$broadcast("showComponentDetails", {
			    componentId : COMPONENT.VF_MODULE
			});
		};

		$scope.describeVnf = function(serviceObject, vnfObject) {
			var serviceInstance = serviceObject.object;
			var vnf = vnfObject.object;

			//Display popup with additional VNF information
			DataService.setVnfInstanceId(vnf['vnf-id']);
			DataService.setInventoryItem(vnf);
			//DataService.setModelInfo(vnf['vnf-id'], vnf);
			
			DataService.setSubscriberName(serviceObject['subscriberName']);
			DataService.setServiceType(serviceObject['serviceType']);
			DataService.setServiceInstanceId(serviceInstance['service-instance-id']);
			
			$scope.$broadcast("showComponentDetails", {
			    componentId : COMPONENT.VNF
			});
		};

		$scope.describeVolumeGroup = function(serviceObject, volumeGroupObject) {
			var serviceInstance = serviceObject.object;
			var volumeGroup = volumeGroupObject.object;

			DataService.setVolumeGroupInstanceId(volumeGroup['volume-group-id']);
			DataService.setInventoryItem(volumeGroup);
			DataService.setModelInfo(volumeGroup['volume-group-id'], volumeGroup);
			
			DataService.setSubscriberName(serviceObject['subscriberName']);
			DataService.setServiceType(serviceObject['serviceType']);
			DataService.setServiceInstanceId(serviceInstance['service-instance-id']);
			
			//Display popup with additional Volume Group information
			//DataService.setVnfInstanceId("Testing");
			$scope.$broadcast("showComponentDetails", {
			    componentId : COMPONENT.VOLUME_GROUP
			});
		};

		$scope.addNetworkInstance = function(network) {
			console.log("Unsupported in 1610: Adding Network instance of type " + network.name + " to service instance" + this.service.instance.name);
		};

		$scope.addVnfInstance = function(vnf) {
			//console.log ("addVnfInstance invoked VNF="); console.log (JSON.stringify (vnf,null,4));
				
			DataService.setSubscriberName($scope.service.instance.subscriberName);
			DataService.setGlobalCustomerId($scope.service.instance.globalCustomerId);
			DataService.setServiceType($scope.service.instance.serviceType);
			DataService.setServiceInstanceName($scope.service.instance.name);
			DataService.setServiceInstanceId($scope.service.instance.id);
			DataService.setServiceName($scope.service.model.service.name);
			
			DataService.setModelInfo(COMPONENT.VNF, {
				"modelType": "vnf",
				"modelInvariantId": vnf.invariantUuid,
				"modelVersion": vnf.version,
				"modelNameVersionId": vnf.uuid,
				"modelName": vnf.name,
				"modelCustomizationName": vnf.modelCustomizationName,
				"inputs": ""
			});
			
			DataService.setModelInstanceName($scope.service.model.service.name);
			
			DataService.setModelInfo(COMPONENT.SERVICE, {
				"modelInvariantId": $scope.service.model.service.invariantUuid, 
				"modelVersion": $scope.service.model.service.version,
				"modelNameVersionId": $scope.service.model.service.uuid,
				"modelName": $scope.service.model.service.name,
				"inputs": ""
			});
			
			$scope.$broadcast("createComponent", {
			    componentId : COMPONENT.VNF,
			    callbackFunction : createVnfCallbackFunction
			});		
		};

		$scope.addVfModuleInstance = function(vnfInstance, vfModuleModel) {
			
			DataService.setSubscriberName($scope.service.instance.subscriberName);
			DataService.setGlobalCustomerId($scope.service.instance.globalCustomerId);
			DataService.setServiceType($scope.service.instance.serviceType);
			DataService.setServiceInstanceName($scope.service.instance.name);
			DataService.setServiceInstanceId($scope.service.instance.id);
			DataService.setServiceName($scope.service.model.service.name);

			var vnfModelInvariantUuid = vnfInstance.object["persona-model-id"];
			var vnfModelVersion = vnfInstance.object["persona-model-version"];
			var vnfModel = $scope.service.modelByInvariantUuid.vnfs[vnfModelInvariantUuid][vnfModelVersion];
			
			var availableVolumeGroupList = [];
			angular.forEach(vnfInstance["availableVolumeGroups"], function(volumeGroupInstance, key) {
				availableVolumeGroupList.push({"instance": volumeGroupInstance});
			});
			
			if (vfModuleModel.volumeGroupAllowed) {
				DataService.setAvailableVolumeGroupList(availableVolumeGroupList);
			}
			
			DataService.setModelInfo(COMPONENT.SERVICE, {
				"modelInvariantId": $scope.service.model.service.invariantUuid,
				"modelVersion": $scope.service.model.service.version,
				"modelNameVersionId": $scope.service.model.service.uuid,
				"modelName": $scope.service.model.service.name,
				"inputs": ""
			});

			DataService.setVnfInstanceId(vnfInstance.object["vnf-id"]);
			
			DataService.setModelInfo(COMPONENT.VNF, {
				"modelInvariantId": vnfModel.invariantUuid,
				"modelVersion": vnfModel.version,
				"modelNameVersionId": vnfModel.uuid,
				"modelName": vnfModel.name,
				"modelCustomizationName": vnfModel.modelCustomizationName,
				"inputs": ""
			});
			
			DataService.setModelInfo(COMPONENT.VF_MODULE, {
				"modelInvariantId": vfModuleModel.invariantUuid,
				"modelVersion": vfModuleModel.version,
				"modelNameVersionId": vfModuleModel.uuid,
				"modelName": vfModuleModel.name,
				"inputs": ""
			});
			
			$scope.$broadcast("createComponent", {
			    componentId : COMPONENT.VF_MODULE,
			    callbackFunction : createVfModuleCallbackFunction
			});
		
		};

		$scope.addVolumeGroupInstance = function(vnfInstance, volumeGroupModel) {
			
			DataService.setSubscriberName($scope.service.instance.subscriberName);
			DataService.setGlobalCustomerId($scope.service.instance.globalCustomerId);
			DataService.setServiceType($scope.service.instance.serviceType);
			DataService.setServiceInstanceName($scope.service.instance.name);
			DataService.setServiceInstanceId($scope.service.instance.id);
			DataService.setServiceName($scope.service.model.service.name);

			DataService.setModelInfo(COMPONENT.SERVICE, {
				"modelInvariantId": $scope.service.model.service.invariantUuid,
				"modelVersion": $scope.service.model.service.version,
				"modelNameVersionId": $scope.service.model.service.uuid,
				"modelName": $scope.service.model.service.name,
				"inputs": ""
			});

			DataService.setVnfInstanceId(vnfInstance.object["vnf-id"]);

			var vnfModelInvariantUuid = vnfInstance.object["persona-model-id"];
			var vnfModelVersion = vnfInstance.object["persona-model-version"];
			var vnfModel = $scope.service.modelByInvariantUuid.vnfs[vnfModelInvariantUuid][vnfModelVersion];
			
			DataService.setModelInfo(COMPONENT.VNF, {
				"modelInvariantId": vnfModel.invariantUuid,
				"modelVersion": vnfModel.version,
				"modelNameVersionId": vnfModel.uuid,
				"modelName": vnfModel.name,
				"modelCustomizationName": vnfModel.modelCustomizationName,
				"inputs": ""
			});
			
			DataService.setModelInfo(COMPONENT.VOLUME_GROUP, {
				"modelInvariantId": volumeGroupModel.invariantUuid,
				"modelVersion": volumeGroupModel.version,
				"modelNameVersionId": volumeGroupModel.uuid,
				"modelName": volumeGroupModel.name,
				"inputs": ""
			});
			
			$scope.$broadcast("createComponent", {
			    componentId : COMPONENT.VOLUME_GROUP,
			    callbackFunction : createVolumeGroupCallbackFunction
			});
		};

		$scope.attachVolumeGroupInstance = function(vfModuleInstance, volumeGroupInstance) {

			var vnfInstance = this.vnf;
			var vnfModelInvariantUuid = vnfInstance.object["persona-model-id"];
			var vnfModelVersion = vnfInstance.object["persona-model-version"];
			var vnfModel = $scope.service.modelByInvariantUuid.vnfs[vnfModelInvariantUuid][vnfModelVersion];
			
			var vfModuleModelInvariantUuid = vfModuleInstance.object["persona-model-id"];
			var vfModuleVersion = vfModuleInstance.object["persona-model-version"];
			var vfModuleModel = vnfModel.vfModules[vfModuleModelInvariantUuid][vfModuleVersion];
			
			var volumeGroupModelInvariantUuid = volumeGroupInstance.object["persona-model-id"];
			var volumeGroupModelVersion = volumeGroupInstance.object["persona-model-version"];
			var volumeGroupModel = vnfModel.volumeGroups[volumeGroupModelInvariantUuid][volumeGroupModelVersion];
			
			if (vfModuleModel.uuid != volumeGroupModel.uuid) alert("Cannot attach this volume group to this VF module (models do not match)");
			
			DataService.setSubscriberName($scope.service.instance.subscriberName);
			DataService.setGlobalCustomerId($scope.service.instance.globalCustomerId);
			DataService.setServiceName($scope.service.model.name);
			DataService.setServiceType($scope.service.instance.serviceType);
			DataService.setServiceInstanceName($scope.service.instance.name);
			DataService.setServiceInstanceId($scope.service.instance.id);

			DataService.setModelInfo(COMPONENT.SERVICE, {
				"modelInvariantId": $scope.service.model.service.invariantUuid,
				"modelVersion": $scope.service.model.service.version,
				"modelNameVersionId": $scope.service.model.service.uuid,
				"modelName": $scope.service.model.service.name,
				"inputs": ""
			});

			DataService.setVnfInstanceId(vnfInstance.object["vnf-id"]);

			DataService.setModelInfo(COMPONENT.VNF, {
				"modelInvariantId": vnfModel.invariantUuid,
				"modelVersion": vnfModel.version,
				"modelNameVersionId": vnfModel.uuid,
				"modelName": vnfModel.name,
				"modelCustomizationName": vnfModel.modelCustomizationName,
				"inputs": ""
			});
			
			DataService.setModelInfo(COMPONENT.VOLUME_GROUP, {
				"modelInvariantId": volumeGroupModel.invariantUuid,
				"modelVersion": volumeGroupModel.version,
				"modelNameVersionId": volumeGroupModel.uuid,
				"modelName": volumeGroupModel.name,
				"inputs": ""
			});
			
			$scope.$broadcast("createComponent", {
			    componentId : COMPONENT.VOLUME_GROUP,
			    callbackFunction : createVolumeGroupCallbackFunction
			});
			/*
			 * Code to manipulate the angular ui-tree
			var volumeGroups = this.vnf.volumeGroups;
			volumeGroups.splice(volumeGroups.indexOf(volumeGroup), 1);
			vfModule.volumeGroups.push(volumeGroup);
			*/
		};

		$scope.resetProgress = function() {
			$scope.percentProgress = 0;
			$scope.progressClass = "progress-bar progress-bar-info";
		};

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
		};
		
		$scope.reloadRoute = function() {
			$route.reload();
		}

		var createVnfCallbackFunction = function(response) {
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
					$scope.reloadRoute();
				} else {
					color = "#F88";
				}
				$scope.callbackStyle = {
						"background-color" : color
				};
			}, 500);
			
		
			
		};
		
		var deleteCallbackFunction = function(response) {
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
					$scope.reloadRoute();
				} else {
					color = "#F88";
				}
				$scope.callbackStyle = {
						"background-color" : color
				};
			}, 500);
			
		};
		
		var createVfModuleCallbackFunction = function(response) {
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
					$scope.reloadRoute();
				} else {
					color = "#F88";
				}
				$scope.callbackStyle = {
						"background-color" : color
				};
			}, 500);

		};
			
		var deleteServiceInstanceCallbackFunction = function(response) {
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
					$location.path("/instances/services")
				} else {
					color = "#F88";
				}
				$scope.callbackStyle = {
						"background-color" : color
				};
			}, 500);

		};
	
		var createVolumeGroupCallbackFunction = function(response) {
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
					$scope.reloadRoute();
				} else {
					color = "#F88";
				}
				$scope.callbackStyle = {
						"background-color" : color
				};
			}, 500);
			
			
		
		};
			
	});
})();
