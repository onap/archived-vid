package org.openecomp.vid.aai;

import com.fasterxml.jackson.annotation.*;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "service-instance-id",
        "service-instance-name",
        "model-invariant-id",
        "model-version-id",
        "resource-version",
        "orchestration-status",
        "global-customer-id",
        "subscriber-name",
        "subscriber-type",
        "vnf-id",
        "vnf-name",
        "vnf-type",
        "service-id",
        "prov-status",
        "in-maint",
        "is-closed-loop-disabled",
        "model-customization-id",
        "nf-type",
        "nf-function",
        "nf-role",
        "nf-naming-code"
})
public class ServiceProperties {

    @JsonProperty("service-instance-id")
    public String serviceInstanceId;
    @JsonProperty("service-instance-name")
    public String serviceInstanceName;
    @JsonProperty("model-invariant-id")
    public String modelInvariantId;
    @JsonProperty("model-version-id")
    public String modelVersionId;
    @JsonProperty("resource-version")
    public String resourceVersion;
    @JsonProperty("orchestration-status")
    public String orchestrationStatus;
    @JsonProperty("global-customer-id")
    public String globalCustomerId;
    @JsonProperty("subscriber-name")
    public String subscriberName;
    @JsonProperty("subscriber-type")
    public String subscriberType;
    @JsonProperty("vnf-id")
    public String vnfId;
    @JsonProperty("vnf-name")
    public String vnfName;
    @JsonProperty("vnf-type")
    public String vnfType;
    @JsonProperty("service-id")
    public String serviceId;
    @JsonProperty("prov-status")
    public String provStatus;
    @JsonProperty("in-maint")
    public Boolean inMaint;
    @JsonProperty("is-closed-loop-disabled")
    public Boolean isClosedLoopDisabled;
    @JsonProperty("model-customization-id")
    public String modelCustomizationId;
    @JsonProperty("nf-type")
    public String nfType;
    @JsonProperty("nf-function")
    public String nfFunction;
    @JsonProperty("nf-role")
    public String nfRole;
    @JsonProperty("nf-naming-code")
    public String nfNamingCode;
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
