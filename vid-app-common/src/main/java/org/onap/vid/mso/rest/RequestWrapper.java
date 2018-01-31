package org.onap.vid.mso.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * request wrapper structure.
 */
public class RequestWrapper {


    /** The request. */
    private Request request;


    /**
     * Gets the request.
     *
     * @return     The requestDetails
     */
    @JsonProperty("request")
    public Request getRequest() {
        return request;
    }

    /**
     * Sets the request.
     *
     * @param request     The request
     */
    @JsonProperty
    public void setRequest(Request request) {
        this.request = request;
    }

}
