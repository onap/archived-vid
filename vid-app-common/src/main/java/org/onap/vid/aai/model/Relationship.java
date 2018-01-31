package org.onap.vid.aai.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;




public class Relationship {
	
	@JsonProperty("related-to")
	public String relatedTo;
	
	@JsonProperty("related-link")
	public String relatedLink;

	@JsonProperty("relationship-label")
	public String relationshipLabel;
	
	@JsonProperty("relationship-data")
	public List<RelationshipData> relationshipData;
	
	@JsonProperty("related-to-property")
	public List<RelatedToProperty> relatedToProperty;

	
	public String getRelatedTo() {
		return relatedTo;
	}

	public void setRelatedTo(String relatedTo) {
		this.relatedTo = relatedTo;
	}

	public String getRelatedLink() {
		return relatedLink;
	}

	public void setRelatedLink(String relatedLink) {
		this.relatedLink = relatedLink;
	}

	public List<RelationshipData> getRelationDataList() {
		return relationshipData;
	}

	public void setRelationDataList(List<RelationshipData> relationDataList) {
		this.relationshipData = relationDataList;
	}

	public List<RelatedToProperty> getRelatedToPropertyList() {
		return relatedToProperty;
	}

	public void setRelatedToPropertyList(List<RelatedToProperty> relatedToPropertyList) {
		this.relatedToProperty = relatedToPropertyList;
	}

	public String getRelationshipLabel() {
		return relationshipLabel;
	}

	public void setRelationshipLabel(String relationshipLabel) {
		this.relationshipLabel = relationshipLabel;
	}
}
