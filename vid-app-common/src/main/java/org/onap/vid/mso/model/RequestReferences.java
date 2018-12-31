
package org.onap.vid.mso.model;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;


/**
 * provides the instanceId and requestId associated with the request
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "instanceId",
    "requestId"
})
public class RequestReferences {

    /**
     * UUID for the service instance
     * (Required)
     * 
     */
    @JsonProperty("instanceId")
    private String instanceId;
    /**
     * UUID for the request
     * (Required)
     * 
     */
    @JsonProperty("requestId")
    private String requestId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     * UUID for the service instance
     * (Required)
     * 
     * @return
     *     The instanceId
     */
    @JsonProperty("instanceId")
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * UUID for the service instance
     * (Required)
     * 
     * @param instanceId
     *     The instanceId
     */
    @JsonProperty("instanceId")
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * UUID for the request
     * (Required)
     * 
     * @return
     *     The requestId
     */
    @JsonProperty("requestId")
    public String getRequestId() {
        return requestId;
    }

    /**
     * UUID for the request
     * (Required)
     * 
     * @param requestId
     *     The requestId
     */
    @JsonProperty("requestId")
    public void setRequestId(String requestId) {
        this.requestId = requestId;
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
        return new HashCodeBuilder().append(instanceId).append(requestId).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RequestReferences)) {
            return false;
        }
        RequestReferences rhs = ((RequestReferences) other);
        return new EqualsBuilder().append(instanceId, rhs.instanceId).append(requestId, rhs.requestId).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
