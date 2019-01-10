package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    @JsonAlias("related-to")
	public void setRelatedTo(String relatedTo) {
		this.relatedTo = relatedTo;
	}

	public String getRelatedLink() {
		return relatedLink;
	}

    @JsonAlias("related-link")
	public void setRelatedLink(String relatedLink) {
		this.relatedLink = relatedLink;
	}

	public List<RelationshipData> getRelationDataList() {
		return relationshipData;
	}

    @JsonAlias("relationship-data")
	public void setRelationDataList(List<RelationshipData> relationDataList) {
		this.relationshipData = relationDataList;
	}

	public List<RelatedToProperty> getRelatedToPropertyList() {
		return relatedToProperty;
	}

    @JsonAlias("related-to-property")
	public void setRelatedToPropertyList(List<RelatedToProperty> relatedToPropertyList) {
		this.relatedToProperty = relatedToPropertyList;
	}

	public String getRelationshipLabel() {
		return relationshipLabel;
	}

    @JsonAlias("relationship-label")
	public void setRelationshipLabel(String relationshipLabel) {
		this.relationshipLabel = relationshipLabel;
	}
}
