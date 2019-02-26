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

package org.onap.vid.mso.rest;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;


/**
 * instanceIds that may be associated with a particular request
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "networkInstanceId",
    "serviceInstanceId",
    "vfModuleInstanceId",
    "vnfInstanceId",
    "volumeGroupInstanceId"
})
public class InstanceIds {

    /**
     * UUID for the network instance (if any)
     * 
     */
    @JsonProperty("networkInstanceId")
    private String networkInstanceId;
    /**
     * UUID for the service instance
     * 
     */
    @JsonProperty("serviceInstanceId")
    private String serviceInstanceId;
    /**
     * UUID for the vfModule instance (if any)
     * 
     */
    @JsonProperty("vfModuleInstanceId")
    private String vfModuleInstanceId;
    /**
     * UUID for the vnf instance (if any)
     * 
     */
    @JsonProperty("vnfInstanceId")
    private String vnfInstanceId;
    /**
     * UUID for the volume group instance (if any)
     * 
     */
    @JsonProperty("volumeGroupInstanceId")
    private String volumeGroupInstanceId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     * UUID for the network instance (if any)
     * 
     * @return
     *     The networkInstanceId
     */
    @JsonProperty("networkInstanceId")
    public String getNetworkInstanceId() {
        return networkInstanceId;
    }

    /**
     * UUID for the network instance (if any)
     * 
     * @param networkInstanceId
     *     The networkInstanceId
     */
    @JsonProperty("networkInstanceId")
    public void setNetworkInstanceId(String networkInstanceId) {
        this.networkInstanceId = networkInstanceId;
    }

    /**
     * UUID for the service instance
     * 
     * @return
     *     The serviceInstanceId
     */
    @JsonProperty("serviceInstanceId")
    public String getServiceInstanceId() {
        return serviceInstanceId;
    }

    /**
     * UUID for the service instance
     * 
     * @param serviceInstanceId
     *     The serviceInstanceId
     */
    @JsonProperty("serviceInstanceId")
    public void setServiceInstanceId(String serviceInstanceId) {
        this.serviceInstanceId = serviceInstanceId;
    }

    /**
     * UUID for the vfModule instance (if any)
     * 
     * @return
     *     The vfModuleInstanceId
     */
    @JsonProperty("vfModuleInstanceId")
    public String getVfModuleInstanceId() {
        return vfModuleInstanceId;
    }

    /**
     * UUID for the vfModule instance (if any)
     * 
     * @param vfModuleInstanceId
     *     The vfModuleInstanceId
     */
    @JsonProperty("vfModuleInstanceId")
    public void setVfModuleInstanceId(String vfModuleInstanceId) {
        this.vfModuleInstanceId = vfModuleInstanceId;
    }

    /**
     * UUID for the vnf instance (if any)
     * 
     * @return
     *     The vnfInstanceId
     */
    @JsonProperty("vnfInstanceId")
    public String getVnfInstanceId() {
        return vnfInstanceId;
    }

    /**
     * UUID for the vnf instance (if any)
     * 
     * @param vnfInstanceId
     *     The vnfInstanceId
     */
    @JsonProperty("vnfInstanceId")
    public void setVnfInstanceId(String vnfInstanceId) {
        this.vnfInstanceId = vnfInstanceId;
    }

    /**
     * UUID for the volume group instance (if any)
     * 
     * @return
     *     The volumeGroupInstanceId
     */
    @JsonProperty("volumeGroupInstanceId")
    public String getVolumeGroupInstanceId() {
        return volumeGroupInstanceId;
    }

    /**
     * UUID for the volume group instance (if any)
     * 
     * @param volumeGroupInstanceId
     *     The volumeGroupInstanceId
     */
    @JsonProperty("volumeGroupInstanceId")
    public void setVolumeGroupInstanceId(String volumeGroupInstanceId) {
        this.volumeGroupInstanceId = volumeGroupInstanceId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(networkInstanceId).append(serviceInstanceId).append(vfModuleInstanceId).append(vnfInstanceId).append(volumeGroupInstanceId).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof InstanceIds)) {
            return false;
        }
        InstanceIds rhs = ((InstanceIds) other);
        return new EqualsBuilder().append(networkInstanceId, rhs.networkInstanceId).append(serviceInstanceId, rhs.serviceInstanceId).append(vfModuleInstanceId, rhs.vfModuleInstanceId).append(vnfInstanceId, rhs.vnfInstanceId).append(volumeGroupInstanceId, rhs.volumeGroupInstanceId).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
