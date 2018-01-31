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

package org.onap.vid.policy.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/*
	[
	  {
	    "policyConfigMessage": "Config Retrieved! ",
	    "policyConfigStatus": "CONFIG_RETRIEVED",
	    "type": "JSON",
	    "config": "{\"service\":\"TimeLimitAndVerticalTopology\",\"policyName\":\"SNIRO_CM_1707.Demo_TimeLimitAndVerticalTopology_zone_localTime\",\"description\":\"dev instance\",\"templateVersion\":\"1702.03\",\"version\":\"1707\",\"priority\":\"4\",\"riskType\":\"test\",\"riskLevel\":\"3\",\"guard\":\"False\",\"content\":{\"serviceType\":\"networkOnDemand\",\"identity\":\"vnf_upgrade_policy\",\"policyScope\":{\"serviceType\":[\"networkOnDemand\"],\"aicZone\":[\" \"],\"entityType\":[\"vnf\"]},\"timeSchedule\":{\"allowedPeriodicTime\":[{\"day\":\"weekday\",\"timeRange\":[{\"start_time\":\"04:00:00\",\"end_time\":\"13:00:00\"}]}]},\"nodeType\":[\"vnf\"],\"type\":\"timeLimitAndVerticalTopology\",\"conflictScope\":\"vnf_zone\"}}",
	    "policyName": "SNIRO_CM_1707.Config_MS_Demo_TimeLimitAndVerticalTopology_zone_localTime.1.xml",
	    "policyVersion": "1",
	    "matchingConditions": {
	      "ECOMPName": "SNIRO-Placement",
	      "ConfigName": "",
	      "service": "TimeLimitAndVerticalTopology",
	      "uuid": "",
	      "Location": ""
	    },
	    "responseAttributes": {},
	    "property": null
	  },
	  {
	    "policyConfigMessage": "Config Retrieved! ",
	    "policyConfigStatus": "CONFIG_RETRIEVED",
	    "type": "JSON",
	    "config": "{\"service\":\"TimeLimitAndVerticalTopology\",\"policyName\":\"SNIRO_CM_1707.Demo_TimeLimitAndVerticalTopology_pserver_localTime\",\"description\":\"dev instance\",\"templateVersion\":\"1702.03\",\"version\":\"1707\",\"priority\":\"4\",\"riskType\":\"test\",\"riskLevel\":\"3\",\"guard\":\"False\",\"content\":{\"serviceType\":\"networkOnDemand\",\"identity\":\"vnf_upgrade_policy\",\"policyScope\":{\"serviceType\":[\"networkOnDemand\"],\"aicZone\":[\" \"],\"entityType\":[\"vnf\"]},\"timeSchedule\":{\"allowedPeriodicTime\":[{\"day\":\"weekday\",\"timeRange\":[{\"start_time\":\"04:00:00\",\"end_time\":\"13:00:00\"}]}]},\"nodeType\":[\"vnf\"],\"type\":\"timeLimitAndVerticalTopology\",\"conflictScope\":\"vnf_pserver\"}}",
	    "policyName": "SNIRO_CM_1707.Config_MS_Demo_TimeLimitAndVerticalTopology_pserver_localTime.1.xml",
	    "policyVersion": "1",
	    "matchingConditions": {
	      "ECOMPName": "SNIRO-Placement",
	      "ConfigName": "",
	      "service": "TimeLimitAndVerticalTopology",
	      "uuid": "",
	      "Location": ""
	    },
	    "responseAttributes": {},
	    "property": null
	  },
	  {
	    "policyConfigMessage": "Config Retrieved! ",
	    "policyConfigStatus": "CONFIG_RETRIEVED",
	    "type": "JSON",
	    "config": "{\"service\":\"TimeLimitAndVerticalTopology\",\"policyName\":\"SNIRO_CM_1707.Demo_TimeLimitAndVerticalTopology_vnf_localTime\",\"description\":\"dev instance\",\"templateVersion\":\"1702.03\",\"version\":\"1707\",\"priority\":\"4\",\"riskType\":\"test\",\"riskLevel\":\"3\",\"guard\":\"False\",\"content\":{\"serviceType\":\"networkOnDemand\",\"identity\":\"vnf_upgrade_policy\",\"policyScope\":{\"serviceType\":[\"networkOnDemand\"],\"aicZone\":[\" \"],\"entityType\":[\"vnf\"]},\"timeSchedule\":{\"allowedPeriodicTime\":[{\"day\":\"weekday\",\"timeRange\":[{\"start_time\":\"04:00:00\",\"end_time\":\"13:00:00\"}]}]},\"nodeType\":[\"vnf\"],\"type\":\"timeLimitAndVerticalTopology\",\"conflictScope\":\"vnf\"}}",
	    "policyName": "SNIRO_CM_1707.Config_MS_Demo_TimeLimitAndVerticalTopology_vnf_localTime.1.xml",
	    "policyVersion": "1",
	    "matchingConditions": {
	      "ECOMPName": "SNIRO-Placement",
	      "ConfigName": "",
	      "service": "TimeLimitAndVerticalTopology",
	      "uuid": "",
	      "Location": ""
	    },
	    "responseAttributes": {},
	    "property": null
	  }
	]
*/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "policyConfigMessage",
    "policyConfigStatus",
    "type",
    "config",
    "policyName",
    "policyVersion",
    "matchingConditions"
})
public class RequestDetails {
	
	@JsonProperty("policyName")
    private String policyName;	 
    
	@JsonProperty("policyName")
    public String getPolicyName() {
        return policyName;
    }

    @JsonProperty("policyName")
    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }
    
}