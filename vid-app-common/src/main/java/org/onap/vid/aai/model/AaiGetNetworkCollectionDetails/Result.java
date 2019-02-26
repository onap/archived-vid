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

package org.onap.vid.aai.model.AaiGetNetworkCollectionDetails;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {

    @JsonProperty("service-instance")
    private ServiceInstance serviceInstance;
    @JsonProperty("collection")
    private Collection collection;
    @JsonProperty("instance-group")
    private InstanceGroup instanceGroup;
    @JsonProperty("networks")
    private List<Network> networks;

    public Result(){
        this.networks = new ArrayList<>();
    }


    @JsonProperty("service-instance")
    public ServiceInstance getServiceInstance() {
        return serviceInstance;
    }

    @JsonProperty("service-instance")
    public void setServiceInstance(ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    @JsonProperty("collection")
    public Collection getCollection() {
        return collection;
    }

    @JsonProperty("collection")
    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    @JsonProperty("instance-group")
    public InstanceGroup getInstanceGroup() {
        return instanceGroup;
    }

    @JsonProperty("instance-group")
    public void setInstanceGroup(InstanceGroup instanceGroup) {
        this.instanceGroup = instanceGroup;
    }

    @JsonProperty("networks")
    public List<Network> getNetworks() { return networks; }

    @JsonProperty("networks")
    public void setNetworks(List<Network> networks) { this.networks = networks; }


}
