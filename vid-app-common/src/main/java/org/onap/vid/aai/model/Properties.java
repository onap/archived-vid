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

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;

public class Properties {

    //properties for l-interface node-type
    @JsonProperty("interface-name")
    private String interfaceName;

    @JsonProperty("interface-id")
    private String interfaceId;

    @JsonProperty("is-port-mirrored")
    private Boolean isPortMirrored;

    //properties for tenant node-type
    @JsonProperty("tenant-id")
    private String tenantId;

    @JsonProperty("tenant-name")
    private String tenantName;

    //properties for cloud-region node-type
    @JsonProperty("cloud-region-id")
    private String cloudRegionId;

    private Map<String, String> additionalProperties = new HashMap<>();

    public Properties(){}

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public Boolean getIsPortMirrored() {
        return isPortMirrored;
    }

    public void setPortMirrored(Boolean portMirrored) {
        isPortMirrored = portMirrored;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getCloudRegionId() {
        return cloudRegionId;
    }

    public void setCloudRegionId(String cloudRegionId) {
        this.cloudRegionId = cloudRegionId;
    }

    @JsonAnyGetter
    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(String name, String value) {
        additionalProperties.put(name, value);
    }
}
