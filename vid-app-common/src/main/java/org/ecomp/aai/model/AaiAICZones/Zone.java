package org.ecomp.aai.model.AaiAICZones;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Zone {
    @JsonProperty("zone-id")
    public String zoneId;
    
    @JsonProperty("zone-name")
    public String zoneName;
}
