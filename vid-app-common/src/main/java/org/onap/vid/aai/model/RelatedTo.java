package org.onap.vid.aai.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
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

    public String getId() {
        return id;
    }

    public String getRelationshipLabel() {
        return relationshipLabel;
    }

    public String getNodeType() {
        return nodeType;
    }

    public String getUrl() {
        return url;
    }
}
