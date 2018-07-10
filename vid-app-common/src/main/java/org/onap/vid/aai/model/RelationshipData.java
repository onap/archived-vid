package org.onap.vid.aai.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

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
