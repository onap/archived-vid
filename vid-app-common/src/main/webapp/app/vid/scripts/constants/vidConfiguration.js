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

appDS2.constant("VIDCONFIGURATION", (function() {
	/*
	 * VNF_STATUS_CHECK_ENABLED: Determines whether VID will check the VNF valid status combination list, before allowing VNF updates.
	 * Set to false, to disable the check.
	 */
	var VNF_STATUS_CHECK_ENABLED = false;
	/*
	 * UPLOAD_SUPPLEMENTARY_STATUS_CHECK_ENABLED: Determines the Property to Govern Presence of Upload Supplementary File on Volume Group Screen.
	 * Set to false, to disable the check.
	 */
	var UPLOAD_SUPPLEMENTARY_STATUS_CHECK_ENABLED = true;
	/*
	 * List of valid VNF status combinations
	 */
	var vnfValidStatusList = [
			{
				"provStatus": "preprov",
				"orchestrationStatus": "pending-create",
				"inMaint": false,
				"operationalStatus": null
			},
			{
				"provStatus": "preprov",
				"orchestrationStatus": "created",
				"inMaint": false,
				"operationalStatus": null
			},
			{
				"provStatus": "preprov",
				"orchestrationStatus": "active",
				"inMaint": false,
				"operationalStatus": null
			},
			{
				"provStatus": "nvtprov",
				"orchestrationStatus": "active",
				"inMaint": false,
				"operationalStatus": null
			},
			{
				"provStatus": "prov",
				"orchestrationStatus": "active",
				"inMaint": false,
				"operationalStatus": "out-of-service-path"
			},
			{
				"provStatus": "prov",
				"orchestrationStatus": "activated",
				"inMaint": false,
				"operationalStatus": "out-of-service-path"
			}
	];
	/* 
	 * The model status VID uses to query SDC for a list of models. The possible values are:
	 * DISTRIBUTION_NOT_APPROVED,
	 * DISTRIBUTION_APPROVED,
	 * DISTRIBUTED,
	 * DISTRIBUTION_REJECTED,
	 * ALL,
	 * In the production env, this should always be set to DISTRIBUTED
	 */
	var ASDC_MODEL_STATUS = "DISTRIBUTED";
	/*
	 * Max number of times that VID will poll MSO for a given request status
	 */
	var MSO_MAX_POLLS = 1440;
	/*
	 * Number of msecs that VID will wait between MSO polls.
	 */
	var MSO_POLLING_INTERVAL_MSECS = 200;
	
	var SERVER_RESPONSE_TIMEOUT_MSECS = 300000;

	var SCHEDULER_POLLING_INTERVAL_MSECS = 10000;
	
	var SCHEDULER_MAX_POLLS = 10;
	/*
	 * List of all service model invariant UUIDs that need macro instantiation.
	 * Example:
	 * MACRO_SERVICES : ["3cf30cbb-5fe7-4fb3-b049-559a4997b221", "b135a703-bab5-4295-a37f-580a4f2d0961"]
	 * 
	 */
	var COMPONENT_LIST_NAMED_QUERY_ID = "0367193e-c785-4d5f-9cb8-7bc89dc9ddb7";
	var MACRO_SERVICES = [];

	var SCHEDULER_CALLBACK_URL = "x";

	var SCHEDULER_PORTAL_URL = "x";
	
	var SDNC_SHOW_ASSIGNMENTS_URL = "https://sdnc.api.simpledemo.onap.org:8448/configAdapter/index#/resource_manager/<SERVICE_INSTANCE_ID>";
	
    return {
    	ASDC_MODEL_STATUS : ASDC_MODEL_STATUS,
    	MSO_MAX_POLLS : MSO_MAX_POLLS,
    	MSO_POLLING_INTERVAL_MSECS : MSO_POLLING_INTERVAL_MSECS,
        SERVER_RESPONSE_TIMEOUT_MSECS : SERVER_RESPONSE_TIMEOUT_MSECS,
    	SCHEDULER_MAX_POLLS : SCHEDULER_MAX_POLLS,
    	SCHEDULER_POLLING_INTERVAL_MSECS : SCHEDULER_POLLING_INTERVAL_MSECS,
    	VNF_STATUS_CHECK_ENABLED : VNF_STATUS_CHECK_ENABLED,
    	VNF_VALID_STATUS_LIST : vnfValidStatusList,
		UPLOAD_SUPPLEMENTARY_STATUS_CHECK_ENABLED : UPLOAD_SUPPLEMENTARY_STATUS_CHECK_ENABLED,
    	MACRO_SERVICES : MACRO_SERVICES,
    	COMPONENT_LIST_NAMED_QUERY_ID : COMPONENT_LIST_NAMED_QUERY_ID,
        SCHEDULER_CALLBACK_URL: SCHEDULER_CALLBACK_URL,
        SCHEDULER_PORTAL_URL: SCHEDULER_PORTAL_URL,
	    SDNC_SHOW_ASSIGNMENTS_URL: SDNC_SHOW_ASSIGNMENTS_URL
    };
})())
