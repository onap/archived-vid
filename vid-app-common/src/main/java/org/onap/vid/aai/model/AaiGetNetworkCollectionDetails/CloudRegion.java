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

package org.onap.vid.aai.model.AaiGetNetworkCollectionDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

import static java.util.Collections.emptyList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CloudRegion {

    private final String cloudOwner;
    private final String cloudRegionId;

    public CloudRegion(
            @JsonProperty("cloud-owner") String cloudOwner,
            @JsonProperty("cloud-region-id") String cloudRegionId
    ) {
        this.cloudOwner = cloudOwner;
        this.cloudRegionId = cloudRegionId;
    }

    public String getCloudOwner() {
        return cloudOwner;
    }

    public String getCloudRegionId() {
        return cloudRegionId;
    }

    public static class Collection {
        private final List<CloudRegion> cloudRegions;

        public Collection(@JsonProperty("cloud-region") List<CloudRegion> cloudRegions) {
            this.cloudRegions = ObjectUtils.defaultIfNull(cloudRegions, emptyList());
        }

        public List<CloudRegion> getCloudRegions() {
            return cloudRegions;
        }
    }
}
