package org.openecomp.vid.changeManagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public class VnfDetails {

	public VnfDetails() {
	}

	public VnfDetails(String UUID, String invariantUUID) {
		this.UUID = UUID;
		this.invariantUUID = invariantUUID;
	}

	@JsonProperty("UUID")
	private String UUID;
	
	@JsonProperty("invariantUUID")
	private String invariantUUID;
	
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
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		VnfDetails that = (VnfDetails) o;

		if (getUUID() != null ? !getUUID().equals(that.getUUID()) : that.getUUID() != null) return false;
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
