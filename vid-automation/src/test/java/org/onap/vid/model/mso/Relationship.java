package org.onap.vid.model.mso;

import java.util.List;


public class Relationship {
	
	public String relatedTo;
	
	public String relatedLink;
	
	public List<RelationshipData> relationshipData;
	
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


}
