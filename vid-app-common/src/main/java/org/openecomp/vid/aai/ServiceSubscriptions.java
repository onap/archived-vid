package org.openecomp.vid.aai;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created by Oren on 7/9/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class ServiceSubscriptions {

    @JsonProperty("service-subscription")
    public List<ServiceSubscription> serviceSubscription;
}
