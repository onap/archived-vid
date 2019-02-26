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
