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

app.factory("FIELD", [ "PARAMETER", function(PARAMETER) {

    /*
     * ID values are typically used internally.
     */
    var ID = {
	AVAILABLE_VOLUME_GROUP : "availableVolumeGroup",
	INSTANCE_NAME : "instanceName",
	LCP_REGION : "lcpRegion",
	LCP_REGION_TEXT : "lcpRegionText",
	PRODUCT_FAMILY : "productFamily",
	SERVICE_TYPE : "serviceType",
	SUBSCRIBER_NAME : "subscriberName",
	SUPPRESS_ROLLBACK : "suppressRollback",
	TENANT : "tenant"
    };

    var KEY = {
	LCP_REGION_TEXT : "DEFAULTREGION"
    };

    /*
     * NAME values are displayed on GUI pages.
     */
    var NAME = {
	AVAILABLE_VOLUME_GROUP : "Available Volume Group",
	INSTANCE_NAME : "Instance Name",
	CUSTOMER_ID : "Customer ID",
	LCP_REGION : "LCP Region",
	LCP_REGION_TEXT : "AIC 2.5 Region",
	MODEL_INVARIANT_UUID: "Model Invariant UUID",
	MODEL_NAME: "Model Name",
	MODEL_VERSION: "Model Version",
	MODEL_UUID: "Model UUID",
	PRODUCT_FAMILY : "Product Family",
	RESOURCE_DESCRIPTION : "Resource Description",
	RESOURCE_NAME : "Resource Name",
	SERVICE_CATEGORY : "Service Category",
	SERVICE_DESCRIPTION : "Service Description",
	SERVICE_INSTANCE_ID : "Service Instance ID",
	SERVICE_INSTANCE_NAME : "Service Instance Name",
	SERVICE_INVARIANT_UUID : "Service Invariant UUID",
	SERVICE_NAME : "Service Name",
	SERVICE_TYPE : "Service Type",
	SERVICE_UUID : "Service UUID",
	SERVICE_VERSION : "Service Version",
	SUBSCRIBER_NAME : "Subscriber Name",
	SUPPRESS_ROLLBACK : "Suppress Rollback on Failure",
	TENANT : "Tenant",
	USER_SERVICE_INSTANCE_NAME : "User Service Instance Name",
	VF_MODULE_DESCRIPTION : "VF Module Description",
	VF_MODULE_LABEL : "VF Module Label",
	VF_MODULE_TYPE : "VF Module Type"
    };

    /*
     * PROMPT values are initial values displayed in select lists.
     */
    var PROMPT = {
	AVAILABLE_VOLUME_GROUP : "Select Volume Group",
	LCP_REGION : "Select LCP Region",
	PRODUCT_FAMILY : "Select Product Family",
	SERVICE_TYPE : "Select Service Type",
	SUBSCRIBER_NAME : "Select Subscriber Name",
	TENANT : "Select Tenant Name"
    };

    /*
     * PARAMETER values indicate field configurations that are provided to
     * parameter block directives.
     */

    var PARAMETER = {
	AVAILABLE_VOLUME_GROUP : {
	    name : NAME.AVAILABLE_VOLUME_GROUP,
	    id : ID.AVAILABLE_VOLUME_GROUP,
	    type : PARAMETER.SELECT,
	    prompt : PROMPT.AVAILABLE_VOLUME_GROUP,
	    isRequired : true
	},
	INSTANCE_NAME : {
	    name : NAME.INSTANCE_NAME,
	    id : ID.INSTANCE_NAME,
	    isRequired : true
	},
	LCP_REGION : {
	    name : NAME.LCP_REGION,
	    id : ID.LCP_REGION,
	    type : PARAMETER.SELECT,
	    prompt : PROMPT.LCP_REGION,
	    isRequired : true
	},
	LCP_REGION_TEXT_HIDDEN : {
	    id : ID.LCP_REGION_TEXT,
	    isVisible : false
	},
	LCP_REGION_TEXT_VISIBLE : {
	    name : NAME.LCP_REGION_TEXT,
	    id : ID.LCP_REGION_TEXT,
	    isRequired : true,
	    isVisible : true
	},
	PRODUCT_FAMILY : {
	    name : NAME.PRODUCT_FAMILY,
	    id : ID.PRODUCT_FAMILY,
	    type : PARAMETER.SELECT,
	    prompt : PROMPT.PRODUCT_FAMILY,
	    isRequired : true
	},
	SERVICE_TYPE : {
	    name : NAME.SERVICE_TYPE,
	    id : ID.SERVICE_TYPE,
	    type : PARAMETER.SELECT,
	    prompt : PROMPT.SERVICE_TYPE,
	    isRequired : true
	},
	SERVICE_TYPE_DISABLED : {
	    name : NAME.SERVICE_TYPE,
	    id : ID.SERVICE_TYPE,
	    type : PARAMETER.SELECT,
	    isEnabled : false,
	    isRequired : true
	},
	SUPPRESS_ROLLBACK : {
	    name : NAME.SUPPRESS_ROLLBACK,
	    id : ID.SUPPRESS_ROLLBACK,
	    type : PARAMETER.BOOLEAN,
	    value : false
	},
	SUBSCRIBER_NAME : {
		name : NAME.SUBSCRIBER_NAME,
		id : ID.SUBSCRIBER_NAME,
	    type : PARAMETER.SELECT,
	    prompt : PROMPT.SUBSCRIBER_NAME,
		isRequired : true
	},
	TENANT_DISABLED : {
	    name : NAME.TENANT,
	    id : ID.TENANT,
	    type : PARAMETER.SELECT,
	    isEnabled : false,
	    isRequired : true
	},
	TENANT_ENABLED : {
	    name : NAME.TENANT,
	    id : ID.TENANT,
	    type : PARAMETER.SELECT,
	    isEnabled : true,
	    prompt : PROMPT.TENANT,
	    isRequired : true
	}
    };

    return {
	ID : ID,
	KEY : KEY,
	NAME : NAME,
	PARAMETER : PARAMETER
    }
} ]);
