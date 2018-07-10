package org.onap.vid;

import com.fasterxml.jackson.annotation.*;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "node-type",
        "relationship-label",
        "url"
})
public class RelatedTo {

    @JsonProperty("id")
    public String id;
    @JsonProperty("node-type")
    public String nodeType;
    @JsonProperty("relationship-label")
    public String relationshipLabel;
    @JsonProperty("url")
    public String url;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
