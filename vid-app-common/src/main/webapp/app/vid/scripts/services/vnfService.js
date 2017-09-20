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

var VnfService = function($http, $log, VIDCONFIGURATION, FIELD, UtilityService) {
	var isVnfStatusValid = function(vnfInstance) {

		if ( (UtilityService.isObjectEmpty(vnfInstance)) || (UtilityService.isObjectEmpty(vnfInstance.object)) ) {
			return (errorInternalMsg);
		}
		var status = {
				"provStatus": "",
				"orchestrationStatus": "",
				"inMaint": false,
				"operationalStatus": null
		};
		var errorAaiStatusMsg = "The prov-status, orchestration-status or in-maint fields are not captured correctly in A&AI for this VNF: "
			+ vnfInstance.object['vnf-name'] + ". Please update these statuses in A&AI before attempting this change.";
		var errorInvalidCombinationMsg = "The VNF: " +  vnfInstance.object['vnf-name'] + 
		", has reached a status where further changes cannot be made in VID. Additional changes should be made through the Change Management Processes.";
		var errorInternalMsg = "Internal VID Error: The VNF Instance is not populated."
			
		if ( ( UtilityService.hasContents ( vnfInstance.object[FIELD.ID.ORCHESTRATION_STATUS] ) ) &&
				( UtilityService.hasContents ( vnfInstance.object[FIELD.ID.IN_MAINT] ) ) &&
				( UtilityService.hasContents ( vnfInstance.object[FIELD.ID.PROV_STATUS] ) ) ) {
			
			status.provStatus = vnfInstance.object[FIELD.ID.PROV_STATUS].toLowerCase();
			console.log ("PROVSTATUS: " + vnfInstance.object[FIELD.ID.PROV_STATUS].toLowerCase());
			
			status.orchestrationStatus = vnfInstance.object[FIELD.ID.ORCHESTRATION_STATUS].toLowerCase();
			console.log ("ORCHESTRATION STATUS: " + vnfInstance.object[FIELD.ID.ORCHESTRATION_STATUS].toLowerCase());
			
			status.inMaint = vnfInstance.object[FIELD.ID.IN_MAINT];
			console.log ("IN MAINT: " + vnfInstance.object[FIELD.ID.IN_MAINT]);
			
			if ( UtilityService.hasContents(vnfInstance.object[FIELD.ID.OPERATIONAL_STATUS]) ) {
				status.operationalStatus = vnfInstance.object[FIELD.ID.OPERATIONAL_STATUS].toLowerCase();
			}
			var i = VIDCONFIGURATION.VNF_VALID_STATUS_LIST.length;
		    if ( i > 0 ) {
			    while (i--) {
			    	var item = VIDCONFIGURATION.VNF_VALID_STATUS_LIST[i];
			        if ( (item.provStatus === status.provStatus) && (item.inMaint === status.inMaint ) 
			        		&& (item.orchestrationStatus === status.orchestrationStatus) )  {
			        	if (UtilityService.hasContents(vnfInstance.object[FIELD.ID.OPERATIONAL_STATUS])) {
			        		if (status.operationalStatus === "") { status.operationalStatus = null }
			        		if ( item.operationalStatus === status.operationalStatus ) {
			        			return ("");
			        		}
			        	}
			        	else {
			        		// no contents
			        		if ( item.operationalStatus === null ) {
			        			return ("");
			        		}
			        	}
			       }
			    }
		    }
			
			return (errorInvalidCombinationMsg);
		}
		else {
			return (errorAaiStatusMsg);
		}
	};
	var isVnfListStatusValid = function(vnfArray) {
		var msg = "";
		for(var i = 0; i < vnfArray.length; i++) {
			var vnf = vnfArray[i];
			msg = isVnfStatusValid (vnf);
			if ( msg != "" ) {
				return (msg);
			} 
		}
		return (msg);
	};
	return {
		isVnfStatusValid: isVnfStatusValid,
		isVnfListStatusValid : isVnfListStatusValid
	}
};

appDS2.factory("VnfService", [ "$http", "$log", "VIDCONFIGURATION", "FIELD",
                            "UtilityService", VnfService ]);
