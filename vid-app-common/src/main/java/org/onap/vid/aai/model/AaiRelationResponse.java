package org.onap.vid.aai.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty("resource-version")
    public void setJsonResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

    @JsonProperty("relationship-list")
    public void setJsonRelationshipList(RelationshipList relationshipList) {
        this.relationshipList = relationshipList;
    }


}
