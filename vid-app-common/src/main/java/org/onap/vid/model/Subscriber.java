package org.onap.vid.model;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Oren on 7/4/17.
 */
public class Subscriber {

    @JsonProperty("global-customer-id")
    public String globalCustomerId;

    @JsonProperty("subscriber-name")
    public String subscriberName;

    @JsonProperty("subscriber-type")
    public String subscriberType;

    @JsonProperty("resource-version")
    public String resourceVersion;




}
