
package org.onap.vid.mso.model;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;


/**
 * fields communicating the cloud configuration in a standard way
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "nodeLocation",
    "lcpCloudRegionId",
    "tenantId",
    "cloudOwner"
})
public class CloudConfiguration {

    /**
     * Location identifier for the node
     * 
     */
    @JsonProperty("nodeLocation")
    private String nodeLocation;
    /**
     * LCP Node Location identifier
     * 
     */
    @JsonProperty("lcpCloudRegionId")
    private String lcpCloudRegionId;
    /**
     * Openstack tenant id
     * 
     */
    @JsonProperty("tenantId")
    private String tenantId;
    /**
     * the cloud owner
     * 
     */
    @JsonProperty("cloudOwner")
    private String cloudOwner;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     * Location identifier for the node
     * 
     * @return
     *     The nodeLocation
     */
    @JsonProperty("nodeLocation")
    public String getNodeLocation() {
        return nodeLocation;
    }

    /**
     * Location identifier for the node
     * 
     * @param nodeLocation
     *     The nodeLocation
     */
    @JsonProperty("nodeLocation")
    public void setNodeLocation(String nodeLocation) {
        this.nodeLocation = nodeLocation;
    }

    /**
     * LCP Node Location identifier
     * 
     * @return
     *     The lcpCloudRegionId
     */
    @JsonProperty("lcpCloudRegionId")
    public String getLcpCloudRegionId() {
        return lcpCloudRegionId;
    }

    /**
     * LCP Node Location identifier
     * 
     * @param lcpCloudRegionId
     *     The lcpCloudRegionId
     */
    @JsonProperty("lcpCloudRegionId")
    public void setLcpCloudRegionId(String lcpCloudRegionId) {
        this.lcpCloudRegionId = lcpCloudRegionId;
    }

    /**
     * Openstack tenant id
     * 
     * @return
     *     The tenantId
     */
    @JsonProperty("tenantId")
    public String getTenantId() {
        return tenantId;
    }

    /**
     * Openstack tenant id
     * 
     * @param tenantId
     *     The tenantId
     */
    @JsonProperty("tenantId")
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * the cloud owner
     * 
     * @return
     *     The cloudOwner
     */
    @JsonProperty("cloudOwner")
    public String getCloudOwner() {
        return cloudOwner;
    }

    /**
     * the cloud owner
     * 
     * @param cloudOwner
     *     The cloudOwner
     */
    @JsonProperty("cloudOwner")
    public void setCloudOwner(String cloudOwner) {
        this.cloudOwner = cloudOwner;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(nodeLocation).append(lcpCloudRegionId).append(tenantId).append(cloudOwner).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof CloudConfiguration)) {
            return false;
        }
        CloudConfiguration rhs = ((CloudConfiguration) other);
        return new EqualsBuilder().append(nodeLocation, rhs.nodeLocation).append(lcpCloudRegionId, rhs.lcpCloudRegionId).append(tenantId, rhs.tenantId).append(cloudOwner, rhs.cloudOwner).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
