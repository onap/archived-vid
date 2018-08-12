
package org.opencomp.vid.model.mso;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;


/**
 * fields providing information about the subscriber associated with the request
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "globalSubscriberId",
    "subscriberCommonSiteId",
    "subscriberName"
})
public class SubscriberInfo {

    /**
     * global Customer Id understood by A&AI
     * 
     */
    @JsonProperty("globalSubscriberId")
    private String globalSubscriberId;
    /**
     * id representing the location of the subscriber
     * 
     */
    @JsonProperty("subscriberCommonSiteId")
    private String subscriberCommonSiteId;
    /**
     * name of the customer or subscriber
     * 
     */
    @JsonProperty("subscriberName")
    private String subscriberName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * global Customer Id understood by A&AI
     * 
     * @return
     *     The globalSubscriberId
     */
    @JsonProperty("globalSubscriberId")
    public String getGlobalSubscriberId() {
        return globalSubscriberId;
    }

    /**
     * global Customer Id understood by A&AI
     * 
     * @param globalSubscriberId
     *     The globalSubscriberId
     */
    @JsonProperty("globalSubscriberId")
    public void setGlobalSubscriberId(String globalSubscriberId) {
        this.globalSubscriberId = globalSubscriberId;
    }

    /**
     * id representing the location of the subscriber
     * 
     * @return
     *     The subscriberCommonSiteId
     */
    @JsonProperty("subscriberCommonSiteId")
    public String getSubscriberCommonSiteId() {
        return subscriberCommonSiteId;
    }

    /**
     * id representing the location of the subscriber
     * 
     * @param subscriberCommonSiteId
     *     The subscriberCommonSiteId
     */
    @JsonProperty("subscriberCommonSiteId")
    public void setSubscriberCommonSiteId(String subscriberCommonSiteId) {
        this.subscriberCommonSiteId = subscriberCommonSiteId;
    }

    /**
     * name of the customer or subscriber
     * 
     * @return
     *     The subscriberName
     */
    @JsonProperty("subscriberName")
    public String getSubscriberName() {
        return subscriberName;
    }

    /**
     * name of the customer or subscriber
     * 
     * @param subscriberName
     *     The subscriberName
     */
    @JsonProperty("subscriberName")
    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
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
        return new HashCodeBuilder().append(globalSubscriberId).append(subscriberCommonSiteId).append(subscriberName).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SubscriberInfo) == false) {
            return false;
        }
        SubscriberInfo rhs = ((SubscriberInfo) other);
        return new EqualsBuilder().append(globalSubscriberId, rhs.globalSubscriberId).append(subscriberCommonSiteId, rhs.subscriberCommonSiteId).append(subscriberName, rhs.subscriberName).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
