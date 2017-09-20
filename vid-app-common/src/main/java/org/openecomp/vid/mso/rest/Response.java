package org.openecomp.vid.mso.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {

    /** The status. */
    private int status;

    /** The entity. */
    private RequestList entity;

    /**
     * Gets the status.
     *
     * @return     The status
     */
    @JsonProperty("status")
    public int getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status     The status
     */
    @JsonProperty("status")
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Gets the entity.
     *
     * @return     The entity
     */
    @JsonProperty("entity")
    public RequestList getEntity() {
        return entity;
    }

    /**
     * Sets the entity.
     *
     * @param entity     The entity
     */
    @JsonProperty("entity")
    public void setEntity(RequestList entity) {
        this.entity = entity;
    }


}
