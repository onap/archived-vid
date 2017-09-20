package org.openecomp.vid.aai.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class RelationshipList {
	
	
	public List<Relationship> getRelationship() {
		return relationship;
	}

	public void setRelationship(List<Relationship> relationship) {
		this.relationship = relationship;
	}

	@JsonProperty("relationship")
	public List<Relationship> relationship;
	
	
	

}
