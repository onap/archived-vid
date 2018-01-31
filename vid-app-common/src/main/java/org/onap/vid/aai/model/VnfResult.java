package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.*;
import org.codehaus.jackson.annotate.JsonProperty;
import org.onap.vid.RelatedTo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "node-type",
        "url",
        "properties",
        "related-to"
})
public class VnfResult {

    @JsonProperty("id")
    public String id;
    @com.fasterxml.jackson.annotation.JsonProperty("node-type")
    @JsonProperty("node-type")
    public String nodeType;
    @JsonProperty("url")
    public String url;
    @JsonProperty("properties")
    public ServiceProperties properties;
    @JsonProperty("related-to")
    @com.fasterxml.jackson.annotation.JsonProperty("related-to")
    public List<RelatedTo> relatedTo = null;
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
}
