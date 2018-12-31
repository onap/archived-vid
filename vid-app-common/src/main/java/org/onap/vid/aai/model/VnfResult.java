package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.*;

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
    @JsonProperty("node-type")
    public String nodeType;
    @JsonProperty("url")
    public String url;
    @JsonProperty("properties")
    public ServiceProperties properties;
    @JsonProperty("related-to")
    public List<RelatedTo> relatedTo = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("id")
    public void setJsonId(String id) {
        this.id = id;
    }

    @JsonProperty("node-type")
    public void setJsonNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    @JsonProperty("url")
    public void setJsonUrl(String url) {
        this.url = url;
    }

    @JsonProperty("properties")
    public void setJsonProperties(ServiceProperties properties) {
        this.properties = properties;
    }

    @JsonProperty("related-to")
    public void setJsonRelatedTo(List<RelatedTo> relatedTo) {
        this.relatedTo = relatedTo;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setJsonAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
