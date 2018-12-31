package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleResult {
    private String id;
    private String nodeType;
    private String url;
    private Properties properties;
    private List<RelatedTo> relatedTo = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setJsonId(String id) {
        this.id = id;
    }

    public String getNodeType() {
        return nodeType;
    }

    @JsonProperty("node-type")
    public void setJsonNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setJsonUrl(String url) {
        this.url = url;
    }

    public Properties getProperties() {
        return properties;
    }

    @JsonProperty("properties")
    public void setJsonProperties(Properties properties) {
        this.properties = properties;
    }

    public List<RelatedTo> getRelatedTo() {
        return relatedTo;
    }

    @JsonProperty("related-to")
    public void setJsonRelatedTo(List<RelatedTo> relatedTo) {
        this.relatedTo = relatedTo;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setJsonAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
