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

var StatusService = function($log, AaiService, DataService, ComponentService, COMPONENT,
	FIELD, PARAMETER, UtilityService) {

    var _this = this;
    
    var getAsyncOperationList = function() {
		
    		return [ getTargetProvStatus ];
		
	};

    var getSummaryList = function() {
	switch (_this.componentId) {
	case COMPONENT.NETWORK:
	case COMPONENT.SERVICE:
	case COMPONENT.VNF:
	case COMPONENT.VF_MODULE:
	case COMPONENT.VOLUME_GROUP:
	    return [ {
		name : FIELD.NAME.SUBSCRIBER_NAME,
		value : DataService.getSubscriberName()
	    }, {
		name : FIELD.NAME.SERVICE_INSTANCE_ID,
		value : DataService.getServiceInstanceId()
	    }, {
		name : FIELD.NAME.SERVICE_INSTANCE_ID,
		value : DataService.getServiceInstanceName()
		}, {
		name : FIELD.NAME.SERVICE_TYPE,
		value : DataService.getServiceType()
	    } ];
	}
    };
     
    var getVNFStatusList = function() {
    	 var inventoryItem = DataService.getInventoryItem();
    	 var newProvStatus = DataService.getUpdatedVNFProvStatus();
    	 if ( UtilityService.hasContents(newProvStatus) ) {
    		 
    	 }
    	 else
    		{
    		 newProvStatus = inventoryItem['prov-status'];
    		}
    	 return [ {
    		    name : FIELD.NAME.VNF_VNF_ID,
     		    value : inventoryItem['vnf-id']
    		    }, {
    			name : FIELD.NAME.VNF_VNF_Name,
    			value : inventoryItem['vnf-name']
    		    }, {
    		    name : FIELD.NAME.VNF_VNF_Type,
        		value : inventoryItem['vnf-type']
    			}, {
    			name : FIELD.NAME.VNF_Service_ID,
    			value : inventoryItem['service-id']
    		    }, {
    		    name : FIELD.NAME.VNF_ORCHESTRATION_STATUS,
    			value :inventoryItem['orchestration-status']
    			}, {
        		name : FIELD.NAME.VNF_In_Maint,
        	    value :inventoryItem['in-maint']
        		}, {
            	name : FIELD.NAME.VNF_Operational_Status,
            	value :inventoryItem['operational-state']
            	},
            	{
                name : FIELD.NAME.VNF_Current_Prov_Status,
                value : newProvStatus
                }
    	];
    };

    var internalGetParametersHandler = function() {
		if (angular.isFunction(_this.getParametersHandler)) {
			if (_this.componentId == COMPONENT.SERVICE)
			_this.getParametersHandler({
				summaryList : getSummaryList(),
				userProvidedList : getUserProvidedList()
			}, true);
			else
				_this.getParametersHandler({
					summaryList : getSummaryList(),
					userProvidedList : getUserProvidedList()
				}, false);
		}
	};
	
	var getTargetProvStatus = function() {
		AaiService.getProvOptionsFromSystemProp(function(response) {
			DataService.setSystemPropProvStatus(response);
			UtilityService.startNextAsyncOperation();
		});
	};
	
	var getUserProvidedList = function() {

	   var parameterList = [];
	
		parameterList = parameterList.concat([
				getTargetProvParameter()]); 

	   return parameterList;
    };
    
	var getTargetProvParameter = function() {
		var provStatus = DataService.getSystemPropProvStatus();
		var parameter = FIELD.PARAMETER.VNF_TARGET_PROVSTATUS;
		var provArray = provStatus.data.split(",");
		parameter.optionList = new Array();
		for (var i = 0; i < provArray.length; i++) {
			parameter.optionList.push({
				id : i+1,
				name : provArray[i]
			}); 
		}
		return parameter;
	};

	var getTargetProvParameterText = function(index) {
		var provStatus = DataService.getSystemPropProvStatus();
		var parameter = FIELD.PARAMETER.VNF_TARGET_PROVSTATUS;
		var provArray = provStatus.data.split(",");
	     
		return provArray[index-1];
	};
	
	var getTargetProvParameters = function() {
		var provStatus = DataService.getSystemPropProvStatus();
	
	};
	
    var updateUserParameterList = function(updatedId, parameterListControl) {
    	console.log ("updateUserParameterList() updatedId=" + updatedId);
		if (updatedId === FIELD.ID.VNF_TARGETPROVSTATUS) {
			var list = parameterListControl.getList(updatedId);
			
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
	getSummaryList : getSummaryList,
	getVNFStatusList : getVNFStatusList,
	getParameters : function(getParametersHandler) {
		_this.getParametersHandler = getParametersHandler;
		UtilityService.setHttpErrorHandler(_this.httpErrorHandler);
		UtilityService.startAsyncOperations(getAsyncOperationList(),
				internalGetParametersHandler);
	},
	updateUserParameterList : updateUserParameterList,
	getTargetProvParameterText : getTargetProvParameterText
    }
}

appDS2.factory("StatusService", [ "$log", "AaiService", "DataService", "ComponentService",
	"COMPONENT", "FIELD", "PARAMETER", "UtilityService", StatusService ]);
