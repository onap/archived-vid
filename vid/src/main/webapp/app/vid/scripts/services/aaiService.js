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

var AaiService = function($http, $log, PropertyService, UtilityService) {
    return {
		getSubscriptionServiceTypeList : function(globalCustomerId,
			successCallbackFunction) {
		    $log
			    .debug("AaiService:getSubscriptionServiceTypeList: globalCustomerId: "
				    + globalCustomerId);
		    $http.get(
			    PropertyService.getAaiBaseUrl()
				    + "/aai_sub_details/"
				    + globalCustomerId + "?r=" + Math.random(),
			    {
				timeout : PropertyService
					.getServerResponseTimeoutMsec()
			    }).then(function(response) {
			    	if (response.data && response.data["service-subscriptions"]) {
				    	var serviceTypes = [];
				    	var serviceSubscriptions = response.data["service-subscriptions"]["service-subscription"];
				    	
				    	for (var i = 0; i < serviceSubscriptions.length; i++) {
				    		serviceTypes.push(serviceSubscriptions[i]["service-type"]);
				    	}
				    	
				    	successCallbackFunction(serviceTypes);
			    	} else {
			    		successCallbackFunction([]);
			    	}
			    })["catch"]
			    (UtilityService.runHttpErrorHandler);
		},
		getLcpCloudRegionTenantList : function(globalCustomerId, serviceType,
				successCallbackFunction) {
			    $log
				    .debug("AaiService:getLcpCloudRegionTenantList: globalCustomerId: "
					    + globalCustomerId);
			    var url =   PropertyService.getAaiBaseUrl()
			    + "/aai_get_tenants/"
			    + globalCustomerId + "/" + serviceType + "?r=" + Math.random();
		
			    $http.get(url,
				    {
					timeout : PropertyService
						.getServerResponseTimeoutMsec()
				    }).then(function(response) {
				    	var lcpCloudRegionTenants = [];
				    	var aaiLcpCloudRegionTenants = response.data;
				    	
				    	lcpCloudRegionTenants.push({
			    			"cloudRegionId": "",
			    			"tenantName": "Please choose a region",
			    			"tenantId": ""
			    		});
				    	
				    	for (var i = 0; i < aaiLcpCloudRegionTenants.length; i++) {
				    		lcpCloudRegionTenants.push({
				    			"cloudRegionId": aaiLcpCloudRegionTenants[i]["cloudRegionID"],
				    			"tenantName": aaiLcpCloudRegionTenants[i]["tenantName"],
				    			"tenantId": aaiLcpCloudRegionTenants[i]["tenantID"]
				    		});
				    	}
				    	
				    	successCallbackFunction(lcpCloudRegionTenants);
				    })["catch"]
				    (UtilityService.runHttpErrorHandler);
		},
		getSubscribers : function(successCallbackFunction) {
			    $log
				    .debug("AaiService:getSubscribers");
			    var url =   PropertyService.getAaiBaseUrl()
			    + "/aai_get_subscribers?r=" + Math.random();
		
			    $http.get(url,
				    {
					timeout : PropertyService
						.getServerResponseTimeoutMsec()
				    }).then(function(response) {
				    	if (response.data) {
				    		successCallbackFunction(response.data.customer);
				    	} else {
				    		successCallbackFunction([]);
				    	}
				    })["catch"]
				    (UtilityService.runHttpErrorHandler);
		}
    }
}

app.factory("AaiService", [ "$http", "$log", "PropertyService",
	"UtilityService", AaiService ]);
