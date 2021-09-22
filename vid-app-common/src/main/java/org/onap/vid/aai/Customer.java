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
public class Customer {
	
    @JsonProperty("global-customer-id")
    public String globalCustomerId;

    @JsonProperty("subscriber-name")
    public String subscriberName;

    public String getGlobalCustomerId() {
        return globalCustomerId;
    }

    public void setGlobalCustomerId(String globalCustomerId) {
        this.globalCustomerId = globalCustomerId;
    }

    public String getSubscriberName() {
        return subscriberName;
    }

    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    public String getSubscriberType() {
        return subscriberType;
    }

    public void setSubscriberType(String subscriberType) {
        this.subscriberType = subscriberType;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    public List<CustomerRelatedNodes> getCustomerRelatedNodes() {
        return customerRelatedNodes;
    }

    public void setCustomerRelatedNodes(List<CustomerRelatedNodes> customerRelatedNodes) {
        this.customerRelatedNodes = customerRelatedNodes;
    }

    @JsonProperty("subscriber-type")
    public String subscriberType;

    @JsonProperty("resource-version")
    public String resourceVersion;

    @JsonProperty("related-nodes")
    public List<CustomerRelatedNodes> customerRelatedNodes;
}
