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
