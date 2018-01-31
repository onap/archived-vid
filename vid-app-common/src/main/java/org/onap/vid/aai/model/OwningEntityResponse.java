package org.onap.vid.aai.model;


import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created by moriya1 on 08/10/2017.
 */
public class OwningEntityResponse {

    @JsonProperty("owning-entity")
    private List<OwningEntity> owningEntity;


    @JsonProperty("owning-entity")
    public List<OwningEntity> getOwningEntity() {
        return owningEntity;
    }

    @JsonProperty("owning-entity")
    public void setOwningEntity(List<OwningEntity> owningEntity) {
        this.owningEntity = owningEntity;
    }


}
