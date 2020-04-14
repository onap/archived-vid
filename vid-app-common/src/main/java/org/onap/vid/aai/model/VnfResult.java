/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) 
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        VnfResult vnfResult = (VnfResult) o;
        return Objects.equals(id, vnfResult.id) &&
                Objects.equals(nodeType, vnfResult.nodeType) &&
                Objects.equals(url, vnfResult.url) &&
                Objects.equals(properties, vnfResult.properties) &&
                Objects.equals(relatedTo, vnfResult.relatedTo) &&
                Objects.equals(additionalProperties, vnfResult.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nodeType, url, properties, relatedTo, additionalProperties);
    }
}
