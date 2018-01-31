package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

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

    @JsonProperty("pnf-name")
    public String pnfName;
    @JsonProperty("equip-type")
    public String equipType;
    @JsonProperty("equip-vendor")
    public String equipVendor;
    @JsonProperty("equip-model")
    public String equipModel;
    @JsonProperty("in-maint")
    public Boolean inMaint;
    @JsonProperty("resource-version")
    public String resourceVersion;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
