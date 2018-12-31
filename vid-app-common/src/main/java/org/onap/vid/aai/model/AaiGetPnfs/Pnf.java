package org.onap.vid.aai.model.AaiGetPnfs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("pnf-name")
    public void setJsonPnfName(String pnfName) {
        this.pnfName = pnfName;
    }

    public String getEquipType() {
        return equipType;
    }

    @JsonProperty("equip-type")
    public void setJsonEquipType(String equipType) {
        this.equipType = equipType;
    }

    public String getEquipVendor() {
        return equipVendor;
    }

    @JsonProperty("equip-vendor")
    public void setJsonEquipVendor(String equipVendor) {
        this.equipVendor = equipVendor;
    }

    public String getPnfName2() {
        return pnfName2;
    }

    @JsonProperty("pnf-name2")
    public void setJsonPnfName2(String pnfName2) {
        this.pnfName2 = pnfName2;
    }

    public String getPnfId() {
        return pnfId;
    }

    @JsonProperty("pnf-id")
    public void setJsonPnfId(String pnfId) {
        this.pnfId = pnfId;
    }

    public String getEquipModel() {
        return equipModel;
    }

    @JsonProperty("equip-model")
    public void setJsonEquipModel(String equipModel) {
        this.equipModel = equipModel;
    }

    public String getPnfName2Source() { return pnfName2Source; }

    @JsonProperty("pnf-name2-source")
    public void setJsonPnfName2Source(String pnfName2Source) { this.pnfName2Source = pnfName2Source; }
}

