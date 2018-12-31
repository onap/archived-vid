package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonAlias;

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

    @JsonAlias("link-name")
    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public Boolean getInMaint() {
        return inMaint;
    }

    @JsonAlias("in-maint")
    public void setInMaint(Boolean inMaint) {
        this.inMaint = inMaint;
    }

    public String getLinkType() {
        return linkType;
    }

    @JsonAlias("link-type")
    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    @JsonAlias("resource-version")
    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    public String getPurpose() {
        return purpose;
    }

    @JsonAlias("purpose")
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

    @JsonAlias("relationship-list")
    public void setRelationshipList(RelationshipList relationshipList) {
        this.relationshipList = relationshipList;
    }
}
