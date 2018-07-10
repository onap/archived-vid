package org.onap.vid.aai.model.AaiGetNetworkCollectionDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RelationshipList {
	
	@JsonProperty("relationship")
	public List<Relationship> getRelationship() {
		return relationship;
	}

	@JsonProperty("relationship")
	public void setRelationship(List<Relationship> relationship) {
		this.relationship = relationship;
	}

	public List<Relationship> relationship;
	
	
	

}
