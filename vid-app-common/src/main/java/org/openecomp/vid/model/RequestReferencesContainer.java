package org.openecomp.vid.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openecomp.vid.domain.mso.RequestReferences;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestReferencesContainer {
    private final RequestReferences requestReferences;

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
                .toString();
    }
}
