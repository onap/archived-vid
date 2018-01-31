package org.onap.vid.aai.model.AaiGetPnfs;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.onap.vid.aai.model.AaiRelationResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pnf extends AaiRelationResponse {

    @JsonProperty("pnf-name")
    public String pnfName;
    @JsonProperty("pnf-name2")
    public String pnfName2;
    @JsonProperty("pnf-name2-source")
    public String pnfName2Source;
    @JsonProperty("pnf-id")
    public String pnfId;
    @JsonProperty("equip-type")
    public String equipType;
    @JsonProperty("equip-vendor")
    public String equipVendor;
    @JsonProperty("equip-model")
    public String equipModel;

    public String getPnfName() {
        return pnfName;
    }

    public void setPnfName(String pnfName) {
        this.pnfName = pnfName;
    }

    public String getEquipType() {
        return equipType;
    }

    public void setEquipType(String equipType) {
        this.equipType = equipType;
    }

    public String getEquipVendor() {
        return equipVendor;
    }

    public void setEquipVendor(String equipVendor) {
        this.equipVendor = equipVendor;
    }

    public String getPnfName2() {
        return pnfName2;
    }

    public void setPnfName2(String pnfName2) {
        this.pnfName2 = pnfName2;
    }

    public String getPnfId() {
        return pnfId;
    }

    public void setPnfId(String pnfId) {
        this.pnfId = pnfId;
    }

    public String getEquipModel() {
        return equipModel;
    }

    public void setEquipModel(String equipModel) {
        this.equipModel = equipModel;
    }

    public String getPnfName2Source() { return pnfName2Source; }

    public void setPnfName2Source(String pnfName2Source) { this.pnfName2Source = pnfName2Source; }
}

