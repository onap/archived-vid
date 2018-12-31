package org.onap.vid.aai.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by moriya1 on 08/10/2017.
 */
public class OwningEntityResponse {

    private List<OwningEntity> owningEntity;


    public List<OwningEntity> getOwningEntity() {
        return owningEntity;
    }

    @JsonProperty("owning-entity")
    public void setJsonOwningEntity(List<OwningEntity> owningEntity) {
        this.owningEntity = owningEntity;
    }


}
