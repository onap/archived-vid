package org.openecomp.vid.aai.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;


public class RelationshipList {
	
	@JsonProperty("relationship")
	public List<Relationship> getRelationship() {
		return relationship;
	}
	
	@JsonProperty("relationship")
	public void setRelationship(List<Relationship> relationship) {
		this.relationship = relationship;
	}

	@JsonProperty("relationship")
	public List<Relationship> relationship;
	
	
	

}
