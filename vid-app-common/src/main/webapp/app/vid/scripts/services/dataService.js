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

var DataService = function($log, DataService) {

	var _this = this;

	return {
		getAvailableVolumeGroupList : function() {
			return _this.availableVolumeGroupList;
		},
		setAvailableVolumeGroupList : function(availableVolumeGroupList) {
			_this.availableVolumeGroupList = availableVolumeGroupList;
		},
		getCloudRegionTenantList : function() {
			return _this.cloudRegionTenantList;
		},
		setCloudRegionTenantList : function(cloudRegionTenantList) {
			_this.cloudRegionTenantList = cloudRegionTenantList;
		},
		getCloudOwnerAndLcpCloudRegionFromOptionId : function (cloudRegionOptionId) {
			var cloudRegionTenantList = this.getCloudRegionTenantList();
			var cloudRegionTenant = _.find(cloudRegionTenantList, {"cloudRegionOptionId": cloudRegionOptionId});
			return {
				cloudOwner: cloudRegionTenant.cloudOwner,
				cloudRegionId: cloudRegionTenant.cloudRegionId
			};
		},
		getGlobalCustomerId : function() {
			return _this.globalCustomerId;
		},
		setGlobalCustomerId : function(globalCustomerId) {
			_this.globalCustomerId = globalCustomerId;
		},
		getCustomizationUuid : function() {
			return _this.customizationUUID;
		},
		setCustomizationUuid : function(customizationUUID) {
			_this.customizationUUID = customizationUUID;
		},
		getResCustomizationUuid : function() {
			return _this.rescustomizationUUID;
		},
		setResCustomizationUuid : function(rescustomizationUUID) {
			_this.rescustomizationUUID = rescustomizationUUID;
		},
		getInventoryItem : function() {
			return _this.inventoryItem;
		},
		setInventoryItem : function(inventoryItem) {
			_this.inventoryItem = inventoryItem;
		},
		getModelId : function() {
			return _this.modelId;
		},
		setModelId : function(modelId) {
			_this.modelId = modelId;
		},
		setModelInstanceName : function(modelInstanceName) {
			_this.modelInstanceName = modelInstanceName;
		},
		getModelInfo : function(componentId) {
			return _this.modelInfo[componentId];
		},
		setModelInfo : function(componentId, modelInfo) {
			if (_this.modelInfo === undefined) {
				_this.modelInfo = new Object;
			}
			_this.modelInfo[componentId] = modelInfo;
		},
		getNetworkInstanceId : function() {
			return _this.networkInstanceId;
		},
		setNetworkInstanceId : function(networkInstanceId) {
			_this.networkInstanceId = networkInstanceId;
		},
		getServiceIdList : function() {
			return _this.serviceIdList;
		},
		setServiceIdList : function(serviceIdList) {
			_this.serviceIdList = serviceIdList;
		},
		setAicZones : function(aicZones) {
			_this.aicZones = aicZones;
		},
		getAicZones : function(){
			return _this.aicZones;
		},
		getAicZoneForPNF : function(){
				return _this.aicZone;
		},
		getServiceInstanceId : function() {
			return _this.serviceInstanceId;
		},
		setServiceInstanceId : function(serviceInstanceId) {
			_this.serviceInstanceId = serviceInstanceId;
		},
		getServiceInstanceName : function() {
			return _this.serviceInstanceName;
		},
		setServiceInstanceName : function(serviceInstanceName) {
			_this.serviceInstanceName = serviceInstanceName;
		},
		getServiceName : function() {
			return _this.serviceName;
		},
		setServiceName : function(serviceName) {
			_this.serviceName = serviceName;
		},
		getServiceType : function() {
			return _this.serviceType;
		},
		setServiceType : function(serviceType) {
			_this.serviceType = serviceType;
		},
		getServiceUuid : function() {
			return _this.serviceUuid;
		},
		setServiceUuid : function(serviceUuid) {
			_this.serviceUuid = serviceUuid;
		},
		getLoggedInUserId : function() {
			return _this.loggedInUserId;
		},
		setLoggedInUserId : function(loggedInUserId) {
			_this.loggedInUserId = loggedInUserId;
		},
		getSubscriberName : function() {
			return _this.subscriberName;
		},
		setSubscriberName : function(subscriberName) {
			_this.subscriberName = subscriberName;
		},
		getSubscribers : function() {
			return _this.subscribers;
		},
		setSubscribers : function(subscribers) {
			_this.subscribers = subscribers;
		},
		getSubscriptionServiceTypeList : function() {
			return _this.subscriptionServiceTypeList;
		},
		setSubscriptionServiceTypeList : function(subscriptionServiceTypeList) {
			_this.subscriptionServiceTypeList = subscriptionServiceTypeList;
		},
		setUserServiceInstanceName : function(userServiceInstanceName) {
			_this.userServiceInstanceName = userServiceInstanceName;
		},
		getVfModuleInstanceId : function() {
			return _this.vfModuleInstanceId;
		},
		setVfModuleInstanceId : function(vfModuleInstanceId) {
			_this.vfModuleInstanceId = vfModuleInstanceId;
		},
		getVnfInstanceId : function() {
			return _this.vnfInstanceId;
		},
		setVnfInstanceId : function(vnfInstanceId) {
			_this.vnfInstanceId = vnfInstanceId;
		},
		getVfModuleInstanceName : function() {
				return _this.vfModuleInstanceName;
		},
		setVfModuleInstanceName : function(vfModuleInstanceName) {
				_this.vfModuleInstanceName = vfModuleInstanceName;
		},
		getVolumeGroupInstanceId : function() {
			return _this.volumeGroupInstanceId;
		},
		setVolumeGroupInstanceId : function(volumeGroupInstanceId) {
			_this.volumeGroupInstanceId = volumeGroupInstanceId;
		},
		getLcpRegion : function() {
			return _this.lcpRegion;
		},
		setLcpRegion : function(lcpRegion) {
			_this.lcpRegion = lcpRegion;
		},
		getTenant : function() {
			return _this.tenant;
		},
		setTenant : function(tenant) {
			_this.tenant = tenant;
		},
		setServiceInstanceToCustomer : function(serviceInstanceToCustomer) {
			_this.serviceInstanceToCustomer = [];
			_this.serviceInstanceToCustomer = serviceInstanceToCustomer;
		},
		getMsoRequestParametersTestApi: function(){
			return sessionStorage.getItem("msoRequestParametersTestApiValue");
		},
		setALaCarte : function(aval) {
			_this.aLaCarte = aval;
		},
		getALaCarte : function() {
			// if not set return true
			if (_this.aLaCarte === undefined) {
				return true;
			}
			return _this.aLaCarte;
		},
		setShouldIncludeInAsyncInstantiationFlow: function (val) {
				_this.shouldIncludeInAsyncInstantiationFlow = val;
		},
		getShouldIncludeInAsyncInstantiationFlow: function(){
			if (_this.shouldIncludeInAsyncInstantiationFlow === undefined) {
					return false;
			}
			return _this.shouldIncludeInAsyncInstantiationFlow;
		},
		setMacro : function(aval) {
			_this.macro = aval;
		},
		getMacro : function() {
			if (_this.macro === undefined) {
				return false;
			}
			return _this.macro;
		},
		getResources : function() {
			return _this.resources;
		},
		setResources : function(r) {
			_this.resources = r;
		},
		getSystemPropProvStatus : function() {
			return _this.syspropProvStatusList;
		},
		setSystemPropProvStatus : function(r) {
			_this.syspropProvStatusList = r;
		},
		getUpdatedVNFProvStatus : function() {
			return _this.updatedvnfProvStatus;
		},
		setUpdatedVNFProvStatus : function(r) {
			_this.updatedvnfProvStatus = r;
		},
		setArbitraryParameters : function (pList) {
			_this.arbitraryParameters = pList;
		},
		getArbitraryParameters : function () {
			return _this.arbitraryParameters;
		},
		setE2EService:function(b){
			_this.e2eService=b;
		},
		getE2EService:function(){
			return _this.e2eService;
		},
		setHideServiceFields:function(b){
			_this.hideServiceFields=b;
		},
		getHideServiceFields:function(){
			return _this.hideServiceFields;
		},
		getServiceProxies:function(){
            return _this.serviceProxies;
		},
		setServiceProxies:function(serviceProxies){
				 _this.serviceProxies = serviceProxies;
		},
		getSourceServiceProxies:function(){
				return _this.sourceServiceProxies;
		},
		setSourceServiceProxies:function(sourceServiceProxies){
				_this.sourceServiceProxies = sourceServiceProxies;
		},
		getCollectorServiceProxies:function(){
				return _this.collectorServiceProxies;
		},
		setCollectorServiceProxies:function(collectorServiceProxies){
				_this.collectorServiceProxies = collectorServiceProxies;
		},
		getConfigurationByPolicy:function() {
        	return _this.configurationByPolicy;
		},
		setConfigurationByPolicy:function (configurationByPolicy) {
			_this.configurationByPolicy = configurationByPolicy;
				},
		getPortMirroningConfigFields:function(){
				return _this.portMirroningConfigFields;
		},
		setPortMirroningConfigFields:function(portMirroningConfigFields){
				_this.portMirroningConfigFields = portMirroningConfigFields;
		},
		getConfigurationInstanceId : function() {
				return _this.configurationInstanceId;
		},
		setConfigurationInstanceId : function(configurationInstanceId) {
				_this.configurationInstanceId = configurationInstanceId;
		},
		getPnf: function () {
				return _this.pnf;
		},
		setPnf: function (pnf) {
				_this.pnf = pnf;
		},
		getOwningEntityProperties: function () {
				return _this.owningEntityProperties;
		},
		setOwningEntityProperties: function (properties) {
				_this.owningEntityProperties = properties;
		},
		getHasTemplate: function () {
				return _this.hasTemplate;
		},
		setHasTemplate: function (hasTemplate) {
				_this.hasTemplate = hasTemplate;
		}
	};
};

appDS2.factory("DataService", [ "$log", DataService ]);
