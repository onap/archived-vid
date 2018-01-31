package org.onap.vid.aai.model.AaiGetAicZone;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Zone {
    @JsonProperty("zone-id")
    public String zoneId;
    
    @JsonProperty("zone-name")
    public String zoneName;
}
