package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RelatedTo {
    private final String id;
    private final String relationshipLabel;
    private final String nodeType;
    private final String url;

    public RelatedTo(
            @JsonProperty("id") String id,
            @JsonProperty("relationship-label") String relationshipLabel,
            @JsonProperty("node-type") String nodeType,
            @JsonProperty("url") String url) {
        this.id = id;
        this.relationshipLabel = relationshipLabel;
        this.nodeType = nodeType;
        this.url = url;
    }
    @JsonProperty("id")
    public String getId() {
        return id;
    }
    @JsonProperty("relationship-label")
    public String getRelationshipLabel() {
        return relationshipLabel;
    }
    @JsonProperty("node-type")
    public String getNodeType() {
        return nodeType;
    }
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }
}
