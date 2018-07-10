package org.onap.vid.aai.model.AaiGetNetworkCollectionDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RelationshipData {
	@JsonProperty("relationship-key")
	public String getRelationshipKey() {
		return relationshipKey;
	}
	@JsonProperty("relationship-key")
	public void setRelationshipKey(String relationshipKey) {
		this.relationshipKey = relationshipKey;
	}
	@JsonProperty("relationship-value")
	public String getRelationshipValue() {
		return relationshipValue;
	}
	@JsonProperty("relationship-value")
	public void setRelationshipValue(String relationshipValue) {
		this.relationshipValue = relationshipValue;
	}

	public String relationshipKey;
	
	public String relationshipValue;

}
