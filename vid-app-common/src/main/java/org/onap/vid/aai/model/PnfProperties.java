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
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "pnf-name",
        "equip-type",
        "equip-vendor",
        "equip-model",
        "in-maint",
        "resource-version"
})
public class PnfProperties {

    public String pnfName;
    public String equipType;
    public String equipVendor;
    public String equipModel;
    public Boolean inMaint;
    public String resourceVersion;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonProperty("pnf-name")
    public void setJsonPnfName(String pnfName) {
        this.pnfName = pnfName;
    }

    @JsonProperty("equip-type")
    public void setJsonEquipType(String equipType) {
        this.equipType = equipType;
    }

    @JsonProperty("equip-vendor")
    public void setJsonEquipVendor(String equipVendor) {
        this.equipVendor = equipVendor;
    }

    @JsonProperty("equip-model")
    public void setJsonEquipModel(String equipModel) {
        this.equipModel = equipModel;
    }

    @JsonProperty("in-maint")
    public void setJsonInMaint(Boolean inMaint) {
        this.inMaint = inMaint;
    }

    @JsonProperty("resource-version")
    public void setJsonResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

}
