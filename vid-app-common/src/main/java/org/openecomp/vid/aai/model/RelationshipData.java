package org.openecomp.vid.aai.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class RelationshipData {
	
	public String getRelationshipKey() {
		return relationshipKey;
	}

	public void setRelationshipKey(String relationshipKey) {
		this.relationshipKey = relationshipKey;
	}

	public String getRelationshipValue() {
		return relationshipValue;
	}

	public void setRelationshipValue(String relationshipValue) {
		this.relationshipValue = relationshipValue;
	}

	@JsonProperty("relationship-key")
	public String relationshipKey;
	
	@JsonProperty("relationship-value")
	public String relationshipValue;

}
