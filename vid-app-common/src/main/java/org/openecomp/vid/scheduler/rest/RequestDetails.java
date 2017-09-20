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

package org.openecomp.vid.scheduler.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
//import javax.annotation.Generated;

import org.openecomp.vid.domain.mso.CloudConfiguration;
import org.openecomp.vid.domain.mso.ModelInfo;
import org.openecomp.vid.domain.mso.RequestInfo;
import org.openecomp.vid.domain.mso.RequestParameters;
import org.openecomp.vid.domain.mso.SubscriberInfo;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/*
 * "domain" : "ChangeManagement",
	"scheduleId" : "3569b875-d40e-4adb-a288-a74f4b59ec1c",
	"scheduleName" : "VnfUpgrade/DWF",
	"userId" : "jf9860@att.com",
	"domainData" : {
		"WorkflowName" : "HEAT Stack Software Update for vNFs"
	},
	"status" : "PendingOptimization",
	"schedulingInfo" : {
		"scheduleId" : "ChangeManagement.3569b875-d40e-4adb-a288-a74f4b59ec1c",
		"normalDurationInSecs" : 60,
		"AdditionalDurationInSecs" : 0,
		"concurrencyLimit" : 10,
		"policyId" : ["SNIRO.TimeLimitAndVerticalTopology"],
		"groups" : [{
				"groupId" : " group1",
				"node" : ["satmo415vbc", "satmo455vbc"],
				"changeWindows" : [{
						"startTime" : "2017-02-15T01:00:00Z",
						"finishTime" : "2017-02-15T02:00:00Z"
					}
				]
			}, {
				"groupId" : " group2",
				"node" : ["satmo555vbc"],
				"changeWindows" : [{
						"startTime" : "2017-02-15T01:00:00Z",
						"finishTime" : "2017-02-15T02:00:00Z"
					}, {
						"startTime" : "2017-02-15T05:00:00Z",
						"finishTime" : "2017-02-15T05:30:00Z"
					}
				]
			}
		]
*/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "domain",
    "scheduleId",
    "scheduleName",
    "userId",
    "domainData",
    "status",
    "schcedulingInfo"
})
public class RequestDetails {
	
	@JsonProperty("domain")
    private String domain;
	 
	 
    
	@JsonProperty("domain")
    public String getDomain() {
        return domain;
    }

    @JsonProperty("domain")
    public void setDomain(String domain) {
        this.domain = domain;
    }
}
