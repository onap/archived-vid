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

package org.onap.vid.aai.model.AaiGetPnfs;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.onap.vid.aai.model.AaiRelationResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pnf extends AaiRelationResponse {

    private String pnfName;
    private String pnfName2;
    private String pnfName2Source;
    private String pnfId;
    private String equipType;
    private String equipVendor;
    private String equipModel;

    public String getPnfName() {
        return pnfName;
    }

    @JsonAlias("pnf-name")
    public void setPnfName(String pnfName) {
        this.pnfName = pnfName;
    }

    public String getEquipType() {
        return equipType;
    }

    @JsonAlias("equip-type")
    public void setEquipType(String equipType) {
        this.equipType = equipType;
    }

    public String getEquipVendor() {
        return equipVendor;
    }

    @JsonAlias("equip-vendor")
    public void setEquipVendor(String equipVendor) {
        this.equipVendor = equipVendor;
    }

    public String getPnfName2() {
        return pnfName2;
    }

    @JsonAlias("pnf-name2")
    public void setPnfName2(String pnfName2) {
        this.pnfName2 = pnfName2;
    }

    public String getPnfId() {
        return pnfId;
    }

    @JsonAlias("pnf-id")
    public void setPnfId(String pnfId) {
        this.pnfId = pnfId;
    }

    public String getEquipModel() {
        return equipModel;
    }

    @JsonAlias("equip-model")
    public void setEquipModel(String equipModel) {
        this.equipModel = equipModel;
    }

    public String getPnfName2Source() { return pnfName2Source; }

    @JsonAlias("pnf-name2-source")
    public void setPnfName2Source(String pnfName2Source) { this.pnfName2Source = pnfName2Source; }
}

