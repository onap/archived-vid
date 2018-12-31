package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;



@JsonIgnoreProperties(ignoreUnknown = true)
public class Relationship {

	public String relatedTo;
	
	public String relatedLink;

	public String relationshipLabel;
	
	public List<RelationshipData> relationshipData;
	
	public List<RelatedToProperty> relatedToProperty;

	
	public String getRelatedTo() {
		return relatedTo;
	}

    @JsonProperty("related-to")
	public void setJsonRelatedTo(String relatedTo) {
		this.relatedTo = relatedTo;
	}

	public String getRelatedLink() {
		return relatedLink;
	}

    @JsonProperty("related-link")
	public void setJsonRelatedLink(String relatedLink) {
		this.relatedLink = relatedLink;
	}

	public List<RelationshipData> getRelationDataList() {
		return relationshipData;
	}

    @JsonProperty("relationship-data")
	public void setJsonRelationDataList(List<RelationshipData> relationDataList) {
		this.relationshipData = relationDataList;
	}

	public List<RelatedToProperty> getRelatedToPropertyList() {
		return relatedToProperty;
	}

    @JsonProperty("related-to-property")
	public void setJsonRelatedToPropertyList(List<RelatedToProperty> relatedToPropertyList) {
		this.relatedToProperty = relatedToPropertyList;
	}

	public String getRelationshipLabel() {
		return relationshipLabel;
	}

    @JsonProperty("relationship-label")
	public void setJsonRelationshipLabel(String relationshipLabel) {
		this.relationshipLabel = relationshipLabel;
	}
}
