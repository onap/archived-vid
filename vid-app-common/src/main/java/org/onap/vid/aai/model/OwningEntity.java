package org.onap.vid.aai.model;


import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by moriya1 on 08/10/2017.
 */
public class OwningEntity extends AaiRelationResponse {
    @JsonProperty("owning-entity-id")
    private String owningEntityId;
    @JsonProperty("owning-entity-name")
    private String owningEntityName;

    @JsonProperty("owning-entity-id")
    public String getOwningEntityId() {
        return owningEntityId;
    }

    @JsonProperty("owning-entity-id")
    public void setOwningEntityId(String owningEntityId) {
        this.owningEntityId = owningEntityId;
    }

    @JsonProperty("owning-entity-name")
    public String getOwningEntityName() {
        return owningEntityName;
    }

    @JsonProperty("owning-entity-name")
    public void setOwningEntityName(String owningEntityName) {
        this.owningEntityName = owningEntityName;
    }


}
