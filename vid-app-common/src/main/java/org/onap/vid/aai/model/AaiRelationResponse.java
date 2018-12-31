package org.onap.vid.aai.model;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by moriya1 on 08/10/2017.
 */
public class AaiRelationResponse {

    private String resourceVersion;
    private RelationshipList relationshipList;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();


    public String getResourceVersion() {
        return resourceVersion;
    }

    @JsonAlias("resource-version")
    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

    @JsonAlias("relationship-list")
    public void setRelationshipList(RelationshipList relationshipList) {
        this.relationshipList = relationshipList;
    }


}
