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

    public String getInterfaceId() {
        return interfaceId;
    }

    public Boolean getIsPortMirrored() {
        return isPortMirrored;
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
