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

package org.onap.vid.aai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerServiceSubscription {

    @JsonProperty("service-type")
    public String serviceType;

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    public List<ServiceSubscriptionRelatedNodes> getServiceSubscriptionRelatedNodes() {
        return serviceSubscriptionRelatedNodes;
    }

    public void setServiceSubscriptionRelatedNodes(List<ServiceSubscriptionRelatedNodes> serviceSubscriptionRelatedNodes) {
        this.serviceSubscriptionRelatedNodes = serviceSubscriptionRelatedNodes;
    }

    @JsonProperty("resource-version")
    public String resourceVersion;

    @JsonProperty("related-nodes")
    public List<ServiceSubscriptionRelatedNodes> serviceSubscriptionRelatedNodes;
}
