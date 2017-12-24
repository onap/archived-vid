package org.openecomp.vid.mso;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface MsoResponseWrapperInterface {
    @JsonProperty("entity")
    Object getEntity();

    @JsonProperty("status")
    int getStatus();

    @org.codehaus.jackson.annotate.JsonIgnore
    @com.fasterxml.jackson.annotation.JsonIgnore
    String getResponse();
}
