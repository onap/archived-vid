/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 IBM.
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

package org.onap.vid.changeManagement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetVnfWorkflowRelationRequest {
	
	@JsonProperty("vnfsDetails")
	private List<VnfDetails>  vnfsDetails;

	public GetVnfWorkflowRelationRequest() {
	}

	public GetVnfWorkflowRelationRequest(List<VnfDetails> vnfsDetails) {
		this.vnfsDetails = vnfsDetails;
	}
	
	@JsonProperty("vnfsDetails")
	public List<VnfDetails> getVnfDetails() {
		return vnfsDetails;
	}
	
	@JsonProperty("vnfsDetails")
	public void setVnfDetails(List<VnfDetails> vnfDetails) {
		this.vnfsDetails = vnfDetails;
	}
}
