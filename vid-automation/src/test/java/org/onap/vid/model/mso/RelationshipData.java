package org.onap.vid.model.mso;


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

	public String relationshipKey;
	
	public String relationshipValue;

}
