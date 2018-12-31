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
public class PnfResult {

    public String id;
    public String nodeType;
    public String url;
    public PnfProperties properties;
    public List<RelatedTo> relatedTo;

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
    public void setJsonProperties(PnfProperties properties) {
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
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
