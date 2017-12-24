package org.openecomp.vid.aai.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class LogicalLinkResponse {

    @JsonProperty("link-name")
    public String linkName;

    @JsonProperty("in-maint")
    public Boolean inMaint;

    @JsonProperty("link-type")
    public String linkType;

    @JsonProperty("resource-version")
    public String resourceVersion;

    @JsonProperty("purpose")
    public String purpose;

    @JsonProperty("relationship-list")
    public RelationshipList relationshipList;

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public Boolean getInMaint() {
        return inMaint;
    }

    public void setInMaint(Boolean inMaint) {
        this.inMaint = inMaint;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

    public void setRelationshipList(RelationshipList relationshipList) {
        this.relationshipList = relationshipList;
    }
}
