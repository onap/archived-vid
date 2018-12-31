package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LogicalLinkResponse {

    public String linkName;

    public Boolean inMaint;

    public String linkType;

    public String resourceVersion;

    public String purpose;

    public RelationshipList relationshipList;

    public String getLinkName() {
        return linkName;
    }

    @JsonProperty("link-name")
    public void setJsonLinkName(String linkName) {
        this.linkName = linkName;
    }

    public Boolean getInMaint() {
        return inMaint;
    }

    @JsonProperty("in-maint")
    public void setJsonInMaint(Boolean inMaint) {
        this.inMaint = inMaint;
    }

    public String getLinkType() {
        return linkType;
    }

    @JsonProperty("link-type")
    public void setJsonLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    @JsonProperty("resource-version")
    public void setJsonResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    public String getPurpose() {
        return purpose;
    }

    @JsonProperty("purpose")
    public void setJsonPurpose(String purpose) {
        this.purpose = purpose;
    }

    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

    @JsonProperty("relationship-list")
    public void setJsonRelationshipList(RelationshipList relationshipList) {
        this.relationshipList = relationshipList;
    }
}
