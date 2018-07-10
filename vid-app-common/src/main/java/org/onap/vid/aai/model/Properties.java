package org.onap.vid.aai.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Properties {

    private final String interfaceName;
    private final String interfaceId;
    private final Boolean isPortMirrored;

    public Properties(
            @JsonProperty("interface-name") String interfaceName,
            @JsonProperty("interface-id") String interfaceId,
            @JsonProperty("is-port-mirrored") Boolean isPortMirrored) {
        this.interfaceName = interfaceName;
        this.interfaceId = interfaceId;
        this.isPortMirrored = isPortMirrored;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public Boolean getIsPortMirrored() {
        return isPortMirrored;
    }
}
