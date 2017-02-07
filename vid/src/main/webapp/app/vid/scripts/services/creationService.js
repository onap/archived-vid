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

/*
 * "CreationService" isolates the "component-specific" logic required by the
 * "CreationDialog" controller.
 * 
 * "Components" are defined as the 5 element types managed by the dialogs: A)
 * Service B) VNF C) VF Module D) Volume Group and E) Network.
 * 
 */

var CreationService = function($log, AaiService, AsdcService, DataService,
		ComponentService, COMPONENT, FIELD, PARAMETER, UtilityService) {

	var _this = this;

	var getAsyncOperationList = function() {
		switch (_this.componentId) {
		case COMPONENT.SERVICE:
			return [ getSubscribers ];
		case COMPONENT.NETWORK:
			return [];
		case COMPONENT.VNF:
			return [ getLcpCloudRegionTenantList ];
		case COMPONENT.VF_MODULE:
			return [ getLcpCloudRegionTenantList ];
		case COMPONENT.VOLUME_GROUP:
			return [ getLcpCloudRegionTenantList ];
		}
	};

	/*
	 * "getSummaryList" and "getUserProvidedList" return parameters that should
	 * be displayed in the summary and user provided sections, respectively. The
	 * functions are expected to return lists that are in the format needed by
	 * the parameter-block directive.
	 */

	var getSummaryList = function() {

		/*
		 * These placeholders should be removed and their usage in
		 * "getSummaryList" should be replaced by appropriate code as the
		 * requirements and interfaces firm up.
		 */

		var PLACEHOLDER_RESOURCE_DESCRIPTION = "Resource Description (PLACEHOLDER)";
		var PLACEHOLDER_SERVICE_CATEGORY = "Service Category (PLACEHOLDER)";
		var PLACEHOLDER_VF_MODULE_DESCRIPTION = "VF Module Description (PLACEHOLDER)";
		var PLACEHOLDER_VF_MODULE_LABEL = "VF Module Label (PLACEHOLDER)";
		var PLACEHOLDER_VF_MODULE_TYPE = "VF Module Type (PLACEHOLDER)";

		_this.parameterList = new Array();

		/*
		 * Common fields displayed at the top of all create instance screens.
		 */
		addToList(FIELD.NAME.SERVICE_NAME, DataService.getServiceName());

		switch (_this.componentId) {
		case COMPONENT.SERVICE:
			addToList(FIELD.NAME.SERVICE_INVARIANT_UUID, DataService
					.getModelInfo(_this.componentId)["modelInvariantId"]);
			addToList(FIELD.NAME.SERVICE_VERSION, DataService
					.getModelInfo(_this.componentId)["modelVersion"]);
			addToList(FIELD.NAME.SERVICE_UUID, DataService
					.getModelInfo(_this.componentId)["modelNameVersionId"]);
			addToList(FIELD.NAME.SERVICE_DESCRIPTION, DataService
					.getModelInfo(_this.componentId)["description"]);
			addToList(FIELD.NAME.SERVICE_CATEGORY, DataService
					.getModelInfo(_this.componentId)["category"]);
			break;
		case COMPONENT.VF_MODULE:
			addToList(FIELD.NAME.SUBSCRIBER_NAME, DataService
					.getSubscriberName());
			addToList(FIELD.NAME.SERVICE_INSTANCE_NAME, DataService
					.getServiceInstanceName());
			addToList(FIELD.NAME.MODEL_NAME, DataService
					.getModelInfo(_this.componentId)["modelName"]);
			addToList(FIELD.NAME.MODEL_INVARIANT_UUID, DataService
					.getModelInfo(_this.componentId)["modelInvariantId"]);
			addToList(FIELD.NAME.MODEL_VERSION, DataService
					.getModelInfo(_this.componentId)["modelVersion"]);
			addToList(FIELD.NAME.MODEL_UUID, DataService
					.getModelInfo(_this.componentId)["modelNameVersionId"]);
			break;
		case COMPONENT.NETWORK:
		case COMPONENT.VNF:
		case COMPONENT.VOLUME_GROUP:
			addToList(FIELD.NAME.SUBSCRIBER_NAME, DataService
					.getSubscriberName());
			addToList(FIELD.NAME.SERVICE_INSTANCE_NAME, DataService
					.getServiceInstanceName());
			addToList(FIELD.NAME.MODEL_NAME, DataService
					.getModelInfo(_this.componentId)["modelName"]);
			addToList(FIELD.NAME.MODEL_INVARIANT_UUID, DataService
					.getModelInfo(_this.componentId)["modelInvariantId"]);
			addToList(FIELD.NAME.MODEL_VERSION, DataService
					.getModelInfo(_this.componentId)["modelVersion"]);
			addToList(FIELD.NAME.MODEL_UUID, DataService
					.getModelInfo(_this.componentId)["modelNameVersionId"]);
			break;
		}

		return _this.parameterList;
	};

	var getUserProvidedList = function() {

		var parameterList = [ FIELD.PARAMETER.INSTANCE_NAME ];

		switch (_this.componentId) {
		case COMPONENT.SERVICE:
			parameterList = parameterList.concat([ getSubscribersParameter(),
					FIELD.PARAMETER.SERVICE_TYPE_DISABLED ]);
			break;
		case COMPONENT.NETWORK:
		case COMPONENT.VNF:
			parameterList = parameterList.concat([ getServiceId(),
					getLcpRegion(), FIELD.PARAMETER.LCP_REGION_TEXT_HIDDEN,
					FIELD.PARAMETER.TENANT_DISABLED ]);
			break;
		case COMPONENT.VF_MODULE:
			parameterList = parameterList.concat([
			        getLcpRegion(),
					FIELD.PARAMETER.LCP_REGION_TEXT_HIDDEN,
					FIELD.PARAMETER.TENANT_DISABLED
			]);
			
			var availableVolumeGroupList = DataService.getAvailableVolumeGroupList();
			
			if (availableVolumeGroupList && availableVolumeGroupList.length > 0) {
				var availableVolumeGroupNames = ["None"];

				for (var i = 0; i < availableVolumeGroupList.length; i++) {
					availableVolumeGroupNames.push(availableVolumeGroupList[i].instance.name);
				}
				
				parameterList.push(addOptionList(
						FIELD.PARAMETER.AVAILABLE_VOLUME_GROUP,
						availableVolumeGroupNames));
			}
			break;
		case COMPONENT.VOLUME_GROUP:
			parameterList = parameterList.concat([ getLcpRegion(),
					FIELD.PARAMETER.LCP_REGION_TEXT_HIDDEN,
					FIELD.PARAMETER.TENANT_DISABLED ]);
		}

		parameterList.push(FIELD.PARAMETER.SUPPRESS_ROLLBACK);

		addArbitraryParameters(parameterList);

		return parameterList;
	};

	var addArbitraryParameters = function(parameterList) {
		var inputs = DataService.getModelInfo(_this.componentId).inputs;
		if (inputs) { 
			for ( var key in inputs) {
				parameterList.push({
					id : key,
					/*
					 * "name" is the display name. The simplest option is to just
					 * display this value (e.g. "name: key"). An alternative used
					 * here is to use "getFieldDisplayName" to display a more "user
					 * friendly" value. See "componentService:getDisplayName" for
					 * mapping details.
					 */
					name : ComponentService.getFieldDisplayName(key),
					value : inputs[key]["default"],
					isRequired : true,
					/*
					 * If the field needs to be considered required, the attribute
					 * "isRequired: true" can be added here.
					 */
					description : inputs[key]["description"]
				});
			}
		}
	};

	var addToList = function(name, value) {
		_this.parameterList.push({
			name : name,
			value : value
		});
	};

	/*
	 * The "*Mso*" functions return URL and request details that can be passed
	 * to the MSO controller. The request details defines the info passed as
	 * part of the POST body.
	 */

	var getMsoUrl = function() {
		switch (_this.componentId) {
		case COMPONENT.NETWORK:
			return "mso_create_nw_instance/"
					+ DataService.getServiceInstanceId();
		case COMPONENT.SERVICE:
			return "mso_create_svc_instance";
		case COMPONENT.VNF:
			return "mso_create_vnf_instance/"
					+ DataService.getServiceInstanceId();
		case COMPONENT.VF_MODULE:
			return "mso_create_vfmodule_instance/"
					+ DataService.getServiceInstanceId() + "/vnfs/"
					+ DataService.getVnfInstanceId();
		case COMPONENT.VOLUME_GROUP:
			return "mso_create_volumegroup_instance/"
					+ DataService.getServiceInstanceId() + "/vnfs/"
					+ DataService.getVnfInstanceId();
		}
	};

	var getMsoRequestDetails = function(parameterList) {
		console.log("getMsoRequestDetails invoked");
		var modelInfo = DataService.getModelInfo(_this.componentId);
		var requestDetails = {
			requestInfo : {
				instanceName : getValueFromList(FIELD.ID.INSTANCE_NAME,
						parameterList),
				source : "VID",
				suppressRollback : getValueFromList(FIELD.ID.SUPPRESS_ROLLBACK,
						parameterList),
			},
			modelInfo : {
				modelType : _this.componentId,
				modelInvariantId : modelInfo.modelInvariantId,
				modelNameVersionId : modelInfo.modelNameVersionId,
				modelName : modelInfo.modelName,
				modelVersion : modelInfo.modelVersion
			},
			requestParameters : {
				userParams : getArbitraryParameters(parameterList)
			}
		};

		switch (_this.componentId) {
		case COMPONENT.SERVICE:
			requestDetails.subscriberInfo = {
				globalSubscriberId : DataService.getGlobalCustomerId(),
				subscriberName : DataService.getSubscriberName()
			};
			requestDetails.requestParameters.subscriptionServiceType = getValueFromList(
					FIELD.ID.SERVICE_TYPE, parameterList);
			break;
		case COMPONENT.NETWORK:
		case COMPONENT.VNF:
			console.log("getMsoRequestDetails COMPONENT.VNF");
			var lcpRegion = getValueFromList(FIELD.ID.LCP_REGION, parameterList);
			if (lcpRegion === FIELD.KEY.LCP_REGION_TEXT) {
				lcpRegion = getValueFromList(FIELD.ID.LCP_REGION_TEXT,
						parameterList);
			}
			requestDetails.cloudConfiguration = {
				lcpCloudRegionId : lcpRegion,
				tenantId : getValueFromList(FIELD.ID.TENANT, parameterList)
			};
			requestDetails.requestInfo.productFamilyId = getValueFromList(
					FIELD.ID.PRODUCT_FAMILY, parameterList);
			// override model info for VNF since it needs the customization name
			var vnfModelInfo = {
				modelType : _this.componentId,
				modelInvariantId : modelInfo.modelInvariantId,
				modelNameVersionId : modelInfo.modelNameVersionId,
				modelName : modelInfo.modelName,
				modelVersion : modelInfo.modelVersion,
				modelCustomizationName : modelInfo.modelCustomizationName
			};
			requestDetails.modelInfo = vnfModelInfo;
			break;
		case COMPONENT.VF_MODULE:
				var lcpRegion = getValueFromList(FIELD.ID.LCP_REGION, parameterList);
				if (lcpRegion === FIELD.KEY.LCP_REGION_TEXT) {
					lcpRegion = getValueFromList(FIELD.ID.LCP_REGION_TEXT,
							parameterList);
				}
				requestDetails.cloudConfiguration = {
						lcpCloudRegionId : lcpRegion,
						tenantId : getValueFromList(FIELD.ID.TENANT, parameterList)
				};	
				break;
		case COMPONENT.VOLUME_GROUP:
			var lcpRegion = getValueFromList(FIELD.ID.LCP_REGION, parameterList);
			if (lcpRegion === FIELD.KEY.LCP_REGION_TEXT) {
				lcpRegion = getValueFromList(FIELD.ID.LCP_REGION_TEXT,
						parameterList);
			}
			requestDetails.cloudConfiguration = {
					lcpCloudRegionId : lcpRegion,
					tenantId : getValueFromList(FIELD.ID.TENANT, parameterList)
			};	
			
			break;
		}

		var relatedInstanceList = getRelatedInstanceList(parameterList);

		if (relatedInstanceList !== undefined) {
			requestDetails.relatedInstanceList = relatedInstanceList;
		}

		return requestDetails;
	};

	var getRelatedInstanceList = function(parameterList) {
		var relatedInstanceList = new Array();
		switch (_this.componentId) {
		case COMPONENT.SERVICE:
			return undefined;
		case COMPONENT.NETWORK:
		case COMPONENT.VNF:
			addRelatedInstance(relatedInstanceList, COMPONENT.SERVICE,
					DataService.getServiceInstanceId());
			break;
		case COMPONENT.VF_MODULE:
			addRelatedInstance(relatedInstanceList, COMPONENT.SERVICE,
					DataService.getServiceInstanceId());
			addRelatedInstance(relatedInstanceList, COMPONENT.VNF, DataService
					.getVnfInstanceId());

			var availableVolumeGroup = getValueFromList(
					FIELD.ID.AVAILABLE_VOLUME_GROUP, parameterList);
			
			if (UtilityService.hasContents(availableVolumeGroup) && availableVolumeGroup !== "None") {
				var availableVolumeGroups = DataService.getAvailableVolumeGroupList();
				
				for (var i = 0; i < availableVolumeGroups.length; i++) {
					if (availableVolumeGroups[i].instance.name == availableVolumeGroup) {
						DataService.setModelInfo(COMPONENT.VOLUME_GROUP, DataService.getModelInfo(COMPONENT.VF_MODULE));
						DataService.setVolumeGroupInstanceId(availableVolumeGroups[i].instance.object["volume-group-id"]);
						break;
					}
				}
				
				addRelatedInstance(relatedInstanceList, COMPONENT.VOLUME_GROUP,
						DataService.getVolumeGroupInstanceId());
			}
			break;
		case COMPONENT.VOLUME_GROUP:
			addRelatedInstance(relatedInstanceList, COMPONENT.SERVICE,
					DataService.getServiceInstanceId());
			addRelatedInstance(relatedInstanceList, COMPONENT.VNF, DataService
					.getVnfInstanceId());
			break;
		}

		return relatedInstanceList;
	};

	var addRelatedInstance = function(relatedInstanceList, componentId,
			instanceId) {
		var modelInfo = DataService.getModelInfo(componentId);
		var relatedInstance;
		if (modelInfo !== undefined) {
			if (componentId === COMPONENT.VNF) {
				relatedInstance = {
					"instanceId" : instanceId,
					"modelInfo" : {
						"modelType" : componentId,
						"modelName" : modelInfo.modelName,
						"modelInvariantId" : modelInfo.modelInvariantId,
						"modelVersion" : modelInfo.modelVersion,
						"modelNameVersionId" : modelInfo.modelNameVersionId,
						"modelCustomizationName" : modelInfo.modelCustomizationName
					}
				};
			} else {
				relatedInstance = {
					"instanceId" : instanceId,
					"modelInfo" : {
						"modelType" : componentId,
						"modelName" : modelInfo.modelName,
						"modelInvariantId" : modelInfo.modelInvariantId,
						"modelVersion" : modelInfo.modelVersion,
						"modelNameVersionId" : modelInfo.modelNameVersionId
					}
				};
			}
			relatedInstanceList.push({
				relatedInstance : relatedInstance
			});
		}
	};

	/*
	 * var getArbitraryParameters = function(parameterList) { var
	 * arbitraryParameters = new Object(); for (var i = 0; i <
	 * parameterList.length; i++) { var parameter = parameterList[i]; switch
	 * (parameter.id) { case FIELD.ID.INSTANCE_NAME: case
	 * FIELD.ID.PRODUCT_FAMILY: case FIELD.ID.LCP_REGION: case
	 * FIELD.ID.LCP_REGION_TEXT: case FIELD.ID.SERVICE_TYPE: case
	 * FIELD.ID.TENANT: case FIELD.ID.SUPPRESS_ROLLBACK: break; default:
	 * arbitraryParameters[parameter.id] = parameter.value; } } return
	 * arbitraryParameters; }
	 */
	var getArbitraryParameters = function(parameterList) {
		var arbitraryParameters = new Object();
		var arbitraryArray = new Array();
		for (var i = 0; i < parameterList.length; i++) {
			var parameter = parameterList[i];
			switch (parameter.id) {
			case FIELD.ID.AVAILABLE_VOLUME_GROUP:
				break;
			case FIELD.ID.INSTANCE_NAME:
				break;
			case FIELD.ID.PRODUCT_FAMILY:
				break;
			case FIELD.ID.LCP_REGION:
				break;
			case FIELD.ID.LCP_REGION_TEXT:
				break;
			case FIELD.ID.SERVICE_TYPE:
				break;
			case FIELD.ID.TENANT:
				break;
			case FIELD.ID.SUPPRESS_ROLLBACK:
				break;
			case FIELD.ID.SUBSCRIBER_NAME:
				break;
			default:
				arbitraryParameters = {
					name : parameter.id,
					value : parameter.value
				}
				arbitraryArray.push(arbitraryParameters);
			}
		}
		return (arbitraryArray);
	}

	var getModel = function() {
		AsdcService.getModel(DataService.getModelId(), function(response) {
			DataService.setModelInfo(_this.componentId, {
				modelInvariantId : response.data.invariantUUID,
				modelNameVersionId : response.data.uuid,
				modelName : response.data.name,
				modelVersion : response.data.version,
				inputs : response.data.inputs
			});
			UtilityService.startNextAsyncOperation();
		});
	};

	var getSubscriptionServiceTypeList = function() {
		AaiService.getSubscriptionServiceTypeList(DataService
				.getGlobalCustomerId(), function(response) {
			DataService.setSubscriptionServiceTypeList(response);
			UtilityService.startNextAsyncOperation();
		});
	};

	var getSubscribers = function() {
		AaiService.getSubscribers(function(response) {
			DataService.setSubscribers(response);
			UtilityService.startNextAsyncOperation();
		});
	};

	var getLcpCloudRegionTenantList = function() {
		AaiService.getLcpCloudRegionTenantList(DataService
				.getGlobalCustomerId(), DataService.getServiceType(), function(
				response) {
			DataService.setCloudRegionTenantList(response);
			UtilityService.startNextAsyncOperation();
		});
	};

	var internalGetParametersHandler = function() {
		if (angular.isFunction(_this.getParametersHandler)) {
			_this.getParametersHandler({
				summaryList : getSummaryList(),
				userProvidedList : getUserProvidedList()
			});
		}
	};

	var getSubscribersParameter = function() {
		var subscribers = DataService.getSubscribers();
		var parameter = FIELD.PARAMETER.SUBSCRIBER_NAME;
		parameter.optionList = [];

		for (var i = 0; i < subscribers.length; i++) {
			parameter.optionList.push({
				id : subscribers[i]["global-customer-id"],
				name : subscribers[i]["subscriber-name"]
			})
		}
		return parameter;
	};

	var getServiceId = function() {
		var serviceIdList = DataService.getServiceIdList();
		var serviceTypeList = DataService.getSubscriptionServiceTypeList();
		var parameter = FIELD.PARAMETER.PRODUCT_FAMILY;
		parameter.optionList = new Array();
		
		if (serviceTypeList == null) {
			getSubscriptionServiceTypeList();
			serviceTypeList = DataService.getSubscriptionServiceTypeList();
		}
		var went = 0;
		for (var i = 0; i < serviceIdList.length; i++) {
			var go = 0;
			var name = serviceIdList[i].id;

			if (serviceTypeList != null) { 
				console.log("STL: " + serviceTypeList);
				for (var k = 0; k < serviceTypeList.length; k++) {
					if (angular.equals(name,serviceTypeList[k])) {
						go = 1;
						went = 1;
					}
				}
			} else { 
				go = 1;
				went = 1;
			}
			if (go == 1) {
				parameter.optionList.push({
					id : serviceIdList[i].id,
					name : serviceIdList[i].description
				});
			}
		}  // load them all, ours wasn't in the list
		if (went == 0) { 
			for (var i = 0; i < serviceIdList.length; i++) {
					parameter.optionList.push({
						id : serviceIdList[i].id,
						name : serviceIdList[i].description
					});
			}
		}
		return parameter;
	};

	var getLcpRegion = function() {
		var cloudRegionTenantList = DataService.getCloudRegionTenantList();
		var parameter = FIELD.PARAMETER.LCP_REGION;
		parameter.optionList = new Array();
		for (var i = 0; i < cloudRegionTenantList.length; i++) {
			for (var j = 0; j < parameter.optionList.length; j++) {
				if (parameter.optionList[j].id === cloudRegionTenantList[i].cloudRegionId) {
					break;
				}
			}
			if (j < parameter.optionList.length) {
				continue;
			}
			parameter.optionList.push({
				id : cloudRegionTenantList[i].cloudRegionId
			});
		}
		return parameter;
	};

	var getTenantList = function(cloudRegionId) {
		var cloudRegionTenantList = DataService.getCloudRegionTenantList();
		var parameter = FIELD.PARAMETER.TENANT_ENABLED;
		parameter.optionList = new Array();
		for (var i = 0; i < cloudRegionTenantList.length; i++) {
			if (cloudRegionTenantList[i].cloudRegionId === cloudRegionId) {
				parameter.optionList.push({
					id : cloudRegionTenantList[i].tenantId,
					name : cloudRegionTenantList[i].tenantName
				});
			}
		}
		return parameter;

	};

	var addOptionList = function(parameter, optionSimpleArray) {
		var optionList = new Array();
		if (!angular.isArray(optionSimpleArray)) {
			return optionList;
		}
		for (var i = 0; i < optionSimpleArray.length; i++) {
			optionList.push({
				name : optionSimpleArray[i]
			});
		}
		parameter.optionList = optionList;
		return parameter;
	};

	var getValueFromList = function(id, parameterList) {
		for (var i = 0; i < parameterList.length; i++) {
			if (parameterList[i].id === id) {
				return parameterList[i].value;
			}
		}
	};

	var updateUserParameterList = function(updatedId, parameterListControl) {
		if (updatedId === FIELD.ID.LCP_REGION) {
			var list = parameterListControl.getList(updatedId);
			if (list[0].selectedIndex >= 0) {
				parameterListControl
						.updateList([ getTenantList(list[0].value) ]);
			} else {
				parameterListControl
						.updateList([ FIELD.PARAMETER.TENANT_DISABLED ]);
			}
			if (list[0].value === FIELD.KEY.LCP_REGION_TEXT) {
				parameterListControl
						.updateList([ FIELD.PARAMETER.LCP_REGION_TEXT_VISIBLE ]);
			} else {
				parameterListControl
						.updateList([ FIELD.PARAMETER.LCP_REGION_TEXT_HIDDEN ]);
			}
		} else if (updatedId === FIELD.ID.SUBSCRIBER_NAME) {
			var list = parameterListControl.getList(updatedId);
			if (list[0].selectedIndex >= 0) {
				DataService.setGlobalCustomerId(list[0].value);

				AaiService.getSubscriptionServiceTypeList(DataService
						.getGlobalCustomerId(), function(response) {
					DataService.setSubscriptionServiceTypeList(response);
					var serviceTypeParameters = FIELD.PARAMETER.SERVICE_TYPE;
					serviceTypeParameters.optionList = [];

					for (var i = 0; i < response.length; i++) {
						serviceTypeParameters.optionList.push({
							"id" : response[i],
							"name" : response[i]
						});
					}
					parameterListControl.updateList([ serviceTypeParameters ]);
				});

			} else {
				parameterListControl
						.updateList([ FIELD.PARAMETER.SERVICE_TYPE_DISABLED ]);
			}
		}
	};

	return {
		initializeComponent : function(componentId) {
			_this.componentId = ComponentService.initialize(componentId);
		},
		setHttpErrorHandler : function(httpErrorHandler) {
			_this.httpErrorHandler = httpErrorHandler;
		},
		getComponentDisplayName : ComponentService.getComponentDisplayName,
		getParameters : function(getParametersHandler) {
			_this.getParametersHandler = getParametersHandler;
			UtilityService.setHttpErrorHandler(_this.httpErrorHandler);
			UtilityService.startAsyncOperations(getAsyncOperationList(),
					internalGetParametersHandler);
		},
		updateUserParameterList : updateUserParameterList,
		getMsoRequestDetails : getMsoRequestDetails,
		getMsoUrl : getMsoUrl
	}
}

app.factory("CreationService", [ "$log", "AaiService", "AsdcService",
		"DataService", "ComponentService", "COMPONENT", "FIELD", "PARAMETER",
		"UtilityService", CreationService ]);
