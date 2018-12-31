package org.onap.vid.aai.model.AaiGetAicZone;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Zone {
    @JsonProperty("zone-id")
    public String zoneId;

    @JsonProperty("zone-name")
    public String zoneName;
}
