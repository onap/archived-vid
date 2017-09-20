package org.openecomp.vid.changeManagement;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "relatedInstance"
})
public class RelatedInstanceList {

    @JsonProperty("relatedInstance")
    public RelatedInstance relatedInstance;

    @JsonSetter
    public RelatedInstance getRelatedInstance() {
        return relatedInstance;
    }

    @JsonSetter
    public void setRelatedInstance(RelatedInstance relatedInstance) {
        this.relatedInstance = relatedInstance;
    }
}
