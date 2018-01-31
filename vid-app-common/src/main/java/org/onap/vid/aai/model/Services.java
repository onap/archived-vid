package org.onap.vid.aai.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Services {
    @JsonProperty("global-customer-id")
    public String globalCustomerId;

    @JsonProperty("subscriber-name")
    public String subscriberName;

    @JsonProperty("subscriber-type")
    public String subscriberType;

    @JsonProperty("resource-version")
    public String resourceVersion;
    
    @JsonProperty("service-subscriptions")
    public List<ServiceSubscription> serviceSubscriptions;


}
