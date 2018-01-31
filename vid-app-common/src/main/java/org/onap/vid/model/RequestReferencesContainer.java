package org.onap.vid.model;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.MoreObjects;
import org.onap.vid.domain.mso.RequestReferences;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestReferencesContainer {
    private final RequestReferences requestReferences;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public RequestReferencesContainer(@JsonProperty("requestReferences") RequestReferences requestReferences) {
        this.requestReferences = requestReferences;
    }

    public RequestReferences getRequestReferences() {
        return requestReferences;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("requestReferences", requestReferences)
                .add("additionalProperties", additionalProperties)
                .toString();
    }
}
