package org.onap.vid.model.mso;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface MsoResponseWrapperInterface {
    @JsonProperty("entity")
    Object getEntity();

    @JsonProperty("status")
    int getStatus();

    @JsonIgnore
    String getResponse();
}
