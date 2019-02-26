/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

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
