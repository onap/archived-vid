package org.onap.vid.model.mso;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

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
