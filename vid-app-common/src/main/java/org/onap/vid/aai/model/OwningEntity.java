package org.onap.vid.aai.model;


import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * Created by moriya1 on 08/10/2017.
 */
public class OwningEntity extends AaiRelationResponse {
    private String owningEntityId;
    private String owningEntityName;

    public String getOwningEntityId() {
        return owningEntityId;
    }

    @JsonAlias("owning-entity-id")
    public void setOwningEntityId(String owningEntityId) {
        this.owningEntityId = owningEntityId;
    }

    public String getOwningEntityName() {
        return owningEntityName;
    }

    @JsonAlias("owning-entity-name")
    public void setOwningEntityName(String owningEntityName) {
        this.owningEntityName = owningEntityName;
    }


}
