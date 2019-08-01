/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 Nokia.
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

package org.onap.vid.aai.model.AaiGetPnfs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.onap.vid.aai.model.AaiRelationResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class Pnf extends AaiRelationResponse {

    private final String pnfId;
    private final String pnfName;
    private final String pnfName2;
    private final String pnfName2Source;
    private final String equipType;
    private final String equipVendor;
    private final String equipModel;

    @JsonCreator
    public Pnf(
            @JsonProperty("pnf-id") String pnfId, @JsonProperty("pnf-name") String pnfName,
            @JsonProperty("pnf-name2") String pnfName2, @JsonProperty("pnf-name2-source") String pnfName2Source,
            @JsonProperty("equip-type") String equipType, @JsonProperty("equip-vendor") String equipVendor,
            @JsonProperty("equip-model") String equipModel) {

        this.pnfId = pnfId;
        this.pnfName = pnfName;
        this.pnfName2 = pnfName2;
        this.pnfName2Source = pnfName2Source;
        this.equipType = equipType;
        this.equipVendor = equipVendor;
        this.equipModel = equipModel;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getPnfId() {
        return pnfId;
    }

    public String getPnfName() {
        return pnfName;
    }

    public String getPnfName2() {
        return pnfName2;
    }

    public String getPnfName2Source() {
        return pnfName2Source;
    }

    public String getEquipType() {
        return equipType;
    }

    public String getEquipVendor() {
        return equipVendor;
    }

    public String getEquipModel() {
        return equipModel;
    }

    public static class Builder {

        private String pnfId;
        private String pnfName;
        private String pnfName2;
        private String pnfName2Source;
        private String equipType;
        private String equipVendor;
        private String equipModel;

        public Builder withPnfId(String pnfId) {
            this.pnfId = pnfId;
            return this;
        }

        public Builder withPnfName(String pnfName) {
            this.pnfName = pnfName;
            return this;
        }

        public Builder withPnfName2(String pnfName2) {
            this.pnfName2 = pnfName2;
            return this;
        }

        public Builder withPnfName2Source(String pnfName2Source) {
            this.pnfName2Source = pnfName2Source;
            return this;
        }

        public Builder withEquipType(String equipType) {
            this.equipType = equipType;
            return this;
        }

        public Builder withEquipVendor(String equipVendor) {
            this.equipVendor = equipVendor;
            return this;
        }

        public Builder withEquipModel(String equipModel) {
            this.equipModel = equipModel;
            return this;
        }

        public Pnf build() {
            return new Pnf(pnfId, pnfName, pnfName2, pnfName2Source, equipType, equipVendor, equipModel);
        }
    }
}

