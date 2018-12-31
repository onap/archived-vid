package org.onap.vid.aai.model;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by moriya1 on 08/10/2017.
 */
public class OwningEntity extends AaiRelationResponse {
    private String owningEntityId;
    private String owningEntityName;

    public String getOwningEntityId() {
        return owningEntityId;
    }

    @JsonProperty("owning-entity-id")
    public void setJsonOwningEntityId(String owningEntityId) {
        this.owningEntityId = owningEntityId;
    }

    public String getOwningEntityName() {
        return owningEntityName;
    }

    @JsonProperty("owning-entity-name")
    public void setJsonOwningEntityName(String owningEntityName) {
        this.owningEntityName = owningEntityName;
    }


}
