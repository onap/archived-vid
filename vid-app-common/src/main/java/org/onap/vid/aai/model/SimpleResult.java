package org.onap.vid.aai.model;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleResult {
    @JsonProperty("id")
    private String id;
    @JsonProperty("node-type")
    private String nodeType;
    @JsonProperty("url")
    private String url;
    @JsonProperty("properties")
    private Properties properties;
    @JsonProperty("related-to")
    private List<RelatedTo> relatedTo = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("node-type")
    public String getNodeType() {
        return nodeType;
    }

    @JsonProperty("node-type")
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("properties")
    public Properties getProperties() {
        return properties;
    }

    @JsonProperty("properties")
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @JsonProperty("related-to")
    public List<RelatedTo> getRelatedTo() {
        return relatedTo;
    }

    @JsonProperty("related-to")
    public void setRelatedTo(List<RelatedTo> relatedTo) {
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
