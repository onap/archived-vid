/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 IBM.
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

package org.onap.vid.model;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.MoreObjects;
import org.onap.vid.mso.model.RequestReferences;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestReferencesContainer {
    private final RequestReferences requestReferences;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();
    
    public RequestReferencesContainer(@JsonProperty("requestReferences") RequestReferences requestReferences) {
        this.requestReferences = requestReferences;
    }
    
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public RequestReferences getRequestReferences() {
        return requestReferences;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("requestReferences", requestReferences)
                .add("additionalProperties", additionalProperties)
                .toString();
    }
}
