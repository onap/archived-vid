package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RelationshipData {
    public String relationshipKey;
    public String relationshipValue;

    public String getRelationshipKey() {
        return relationshipKey;
    }

    @JsonProperty("relationship-key")
    public void setJsonRelationshipKey(String relationshipKey) {
        this.relationshipKey = relationshipKey;
    }

    public String getRelationshipValue() {
        return relationshipValue;
    }

    @JsonProperty("relationship-value")
    public void setJsonRelationshipValue(String relationshipValue) {
        this.relationshipValue = relationshipValue;
    }

}
