package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RelationshipData {
    public String relationshipKey;
    public String relationshipValue;

    public String getRelationshipKey() {
        return relationshipKey;
    }

    @JsonAlias("relationship-key")
    public void setRelationshipKey(String relationshipKey) {
        this.relationshipKey = relationshipKey;
    }

    public String getRelationshipValue() {
        return relationshipValue;
    }

    @JsonAlias("relationship-value")
    public void setRelationshipValue(String relationshipValue) {
        this.relationshipValue = relationshipValue;
    }

}
