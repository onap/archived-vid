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

import com.fasterxml.jackson.annotation.JsonProperty;

public class VnfDetails {

    @JsonProperty("UUID")
	private String UUID;
	
	@JsonProperty("invariantUUID")
	private String invariantUUID;
	
	public VnfDetails() {
	}

	public VnfDetails(String UUID, String invariantUUID) {
		this.UUID = UUID;
		this.invariantUUID = invariantUUID;
	}

	
	@JsonProperty("UUID")
	public String getUUID() {
		return UUID;
	}
	
	@JsonProperty("UUID")
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	
	@JsonProperty("invariantUUID")
	public String getInvariantUUID() {
		return invariantUUID;
	}
	
	@JsonProperty("invariantUUID")
	public void setInvariantUUID(String invariantUUID) {
		this.invariantUUID = invariantUUID;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) 
            return true;
		if (o == null || getClass() != o.getClass())
            return false;

		VnfDetails that = (VnfDetails) o;

		if (getUUID() != null ? !getUUID().equals(that.getUUID()) : that.getUUID() != null)
            return false;
		return getInvariantUUID() != null ? getInvariantUUID().equals(that.getInvariantUUID()) : that.getInvariantUUID() == null;
	}

	@Override
	public int hashCode() {
		int result = getUUID() != null ? getUUID().hashCode() : 0;
		result = 31 * result + (getInvariantUUID() != null ? getInvariantUUID().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "VnfDetails{" +
				"UUID='" + UUID + '\'' +
				", invariantUUID='" + invariantUUID + '\'' +
				'}';
	}
}
