package org.onap.vid.aai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Oren on 7/9/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class ServiceSubscriptions {

    @JsonProperty("service-subscription")
    public List<ServiceSubscription> serviceSubscription;
}
