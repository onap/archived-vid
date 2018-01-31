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
	var UPLOAD_SUPPLEMENTARY_STATUS_CHECK_ENABLED = false;
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
	var MSO_MAX_POLLS = 10;
	/*
	 * Number of msecs that VID will wait between MSO polls.
	 */
	var MSO_POLLING_INTERVAL_MSECS = 10000;
	
	var SCHEDULER_POLLING_INTERVAL_MSECS = 10000;
	
	var SCHEDULER_MAX_POLLS = 10;
	/*
	 * List of all service model invariant UUIDs that need macro instantiation.
	 * Example:
	 * MACRO_SERVICES : ["3cf30cbb-5fe7-4fb3-b049-559a4997b221", "b135a703-bab5-4295-a37f-580a4f2d0961"]
	 * 
	 */
	var COMPONENT_LIST_NAMED_QUERY_ID = "0367193e-c785-4d5f-9cb8-7bc89dc9ddb7";
	var MACRO_SERVICES = ["c9514b73-3dfe-4d7e-9146-b318d48655d9", "93150ffa-00c6-4ea0-85f2-3536ca46ebd2",
		"2b54297f-72e7-4a94-b451-72df88d0be0b",
		"d27e42cf-087e-4d31-88ac-6c4b7585f800",
		"ec0c4bab-c272-4dab-b087-875031bb0c9f","0311f998-9268-4fd6-bbba-afff15087b72","43596836-ae36-4608-a987-6608ede10dac","306caa85-74c7-48a9-aa22-7e3a564b957a",
		"e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0"];

	var SCHEDULER_CALLBACK_URL = "https://vid-web-ete.ecomp.cci.att.com:8000/vid/change-management/workflow/";

	var SCHEDULER_PORTAL_URL = "https://www.e-access.att.com";

    return {
    	ASDC_MODEL_STATUS : ASDC_MODEL_STATUS,
    	MSO_MAX_POLLS : MSO_MAX_POLLS,
    	MSO_POLLING_INTERVAL_MSECS : MSO_POLLING_INTERVAL_MSECS,
    	SCHEDULER_MAX_POLLS : SCHEDULER_MAX_POLLS,
    	SCHEDULER_POLLING_INTERVAL_MSECS : SCHEDULER_POLLING_INTERVAL_MSECS,
    	VNF_STATUS_CHECK_ENABLED : VNF_STATUS_CHECK_ENABLED,
    	VNF_VALID_STATUS_LIST : vnfValidStatusList,
		UPLOAD_SUPPLEMENTARY_STATUS_CHECK_ENABLED : UPLOAD_SUPPLEMENTARY_STATUS_CHECK_ENABLED,
    	MACRO_SERVICES : MACRO_SERVICES,
    	COMPONENT_LIST_NAMED_QUERY_ID : COMPONENT_LIST_NAMED_QUERY_ID,
        SCHEDULER_CALLBACK_URL: SCHEDULER_CALLBACK_URL,
        SCHEDULER_PORTAL_URL: SCHEDULER_PORTAL_URL
    };
})())
