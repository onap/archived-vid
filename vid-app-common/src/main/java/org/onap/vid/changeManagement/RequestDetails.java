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

package org.onap.vid.changeManagement;

import com.fasterxml.jackson.annotation.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"modelInfo",
"cloudConfiguration",
"requestInfo",
"requestParameters",
"vnfName",
"vnfInstanceId"
})
public class RequestDetails extends org.onap.vid.mso.rest.RequestDetails{

	@JsonProperty("vnfName")
    private String vnfName;
	@JsonProperty("vnfInstanceId")
    private String vnfInstanceId;

	/** The related model list. */
	@JsonProperty("relatedInstanceList")
	public List<RelatedInstanceList> relatedInstList;

	@JsonProperty("vnfName")
	public String getVnfName() {
		return vnfName;
	}

	@JsonProperty("vnfName")
	public void setVnfName(String vnfName) {
		this.vnfName = vnfName;
	}
	@JsonProperty("vnfInstanceId")
	public String getVnfInstanceId() {
		return vnfInstanceId;
	}

	@JsonProperty("vnfInstanceId")
	public void setVnfInstanceId(String vnfInstanceId) {
		this.vnfInstanceId = vnfInstanceId;
	}

	@JsonGetter
	public List<RelatedInstanceList> getRelatedInstList() {
		return relatedInstList;
	}

	@JsonSetter
	public void setRelatedInstList(List<RelatedInstanceList> relatedInstList) {
		this.relatedInstList = relatedInstList;
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
