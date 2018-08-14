
package org.onap.vid.model.mso;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;


/**
 * fields providing general context information for the request
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "billingAccountNumber",
    "callbackUrl",
    "correlator",
    "instanceName",
    "orderNumber",
    "orderVersion",
    "productFamilyId",
    "source",
    "suppressRollback",
    "responseValue",
    "requestorId"
})
public class RequestInfo {

    /**
     * billing account associated with the model being operated on
     * 
     */
    @JsonProperty("billingAccountNumber")
    private String billingAccountNumber;
    /**
     * client URL to use for asynchronous responses
     * 
     */
    @JsonProperty("callbackUrl")
    private String callbackUrl;
    /**
     * Optional correlationId for async callback requests
     * 
     */
    @JsonProperty("correlator")
    private String correlator;
    /**
     * Client provided name for the instance being operated on by the operation (note: not guaranteed to be unique)
     * 
     */
    @JsonProperty("instanceName")
    private String instanceName;
    /**
     * reference to an order
     * 
     */
    @JsonProperty("orderNumber")
    private String orderNumber;
    /**
     * order version number
     * 
     */
    @JsonProperty("orderVersion")
    private Double orderVersion;
    /**
     * UUID for the product family associated with the model being operated on
     * 
     */
    @JsonProperty("productFamilyId")
    private String productFamilyId;
    /**
     * source of the request--not authoritative--actual source revealed via authentication
     * 
     */
    @JsonProperty("source")
    private String source;
    /**
     * true or false boolean indicating whether rollbacks should be suppressed on failures
     * 
     */
    @JsonProperty("suppressRollback")
    private Boolean suppressRollback;
    /**
     * Is the user selected value based on the validResponses list provided to complete the manual task
     * 
     */
    @JsonProperty("responseValue")
    private String responseValue;
    /**
     * The id of the person who initiated the completion request
     * 
     */
    @JsonProperty("requestorId")
    private String requestorId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * billing account associated with the model being operated on
     * 
     * @return
     *     The billingAccountNumber
     */
    @JsonProperty("billingAccountNumber")
    public String getBillingAccountNumber() {
        return billingAccountNumber;
    }

    /**
     * billing account associated with the model being operated on
     * 
     * @param billingAccountNumber
     *     The billingAccountNumber
     */
    @JsonProperty("billingAccountNumber")
    public void setBillingAccountNumber(String billingAccountNumber) {
        this.billingAccountNumber = billingAccountNumber;
    }

    /**
     * client URL to use for asynchronous responses
     * 
     * @return
     *     The callbackUrl
     */
    @JsonProperty("callbackUrl")
    public String getCallbackUrl() {
        return callbackUrl;
    }

    /**
     * client URL to use for asynchronous responses
     * 
     * @param callbackUrl
     *     The callbackUrl
     */
    @JsonProperty("callbackUrl")
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    /**
     * Optional correlationId for async callback requests
     * 
     * @return
     *     The correlator
     */
    @JsonProperty("correlator")
    public String getCorrelator() {
        return correlator;
    }

    /**
     * Optional correlationId for async callback requests
     * 
     * @param correlator
     *     The correlator
     */
    @JsonProperty("correlator")
    public void setCorrelator(String correlator) {
        this.correlator = correlator;
    }

    /**
     * Client provided name for the instance being operated on by the operation (note: not guaranteed to be unique)
     * 
     * @return
     *     The instanceName
     */
    @JsonProperty("instanceName")
    public String getInstanceName() {
        return instanceName;
    }

    /**
     * Client provided name for the instance being operated on by the operation (note: not guaranteed to be unique)
     * 
     * @param instanceName
     *     The instanceName
     */
    @JsonProperty("instanceName")
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    /**
     * reference to an order
     * 
     * @return
     *     The orderNumber
     */
    @JsonProperty("orderNumber")
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * reference to an order
     * 
     * @param orderNumber
     *     The orderNumber
     */
    @JsonProperty("orderNumber")
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * order version number
     * 
     * @return
     *     The orderVersion
     */
    @JsonProperty("orderVersion")
    public Double getOrderVersion() {
        return orderVersion;
    }

    /**
     * order version number
     * 
     * @param orderVersion
     *     The orderVersion
     */
    @JsonProperty("orderVersion")
    public void setOrderVersion(Double orderVersion) {
        this.orderVersion = orderVersion;
    }

    /**
     * UUID for the product family associated with the model being operated on
     * 
     * @return
     *     The productFamilyId
     */
    @JsonProperty("productFamilyId")
    public String getProductFamilyId() {
        return productFamilyId;
    }

    /**
     * UUID for the product family associated with the model being operated on
     * 
     * @param productFamilyId
     *     The productFamilyId
     */
    @JsonProperty("productFamilyId")
    public void setProductFamilyId(String productFamilyId) {
        this.productFamilyId = productFamilyId;
    }

    /**
     * source of the request--not authoritative--actual source revealed via authentication
     * 
     * @return
     *     The source
     */
    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    /**
     * source of the request--not authoritative--actual source revealed via authentication
     * 
     * @param source
     *     The source
     */
    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * true or false boolean indicating whether rollbacks should be suppressed on failures
     * 
     * @return
     *     The suppressRollback
     */
    @JsonProperty("suppressRollback")
    public Boolean getSuppressRollback() {
        return suppressRollback;
    }

    /**
     * true or false boolean indicating whether rollbacks should be suppressed on failures
     * 
     * @param suppressRollback
     *     The suppressRollback
     */
    @JsonProperty("suppressRollback")
    public void setSuppressRollback(Boolean suppressRollback) {
        this.suppressRollback = suppressRollback;
    }

    /**
     * Is the user selected value based on the validResponses list provided to complete the manual task
     * 
     * @return
     *     The responseValue
     */
    @JsonProperty("responseValue")
    public String getResponseValue() {
        return responseValue;
    }

    /**
     * Is the user selected value based on the validResponses list provided to complete the manual task
     * 
     * @param responseValue
     *     The responseValue
     */
    @JsonProperty("responseValue")
    public void setResponseValue(String responseValue) {
        this.responseValue = responseValue;
    }

    /**
     * The id of the person who initiated the completion request
     * 
     * @return
     *     The requestorId
     */
    @JsonProperty("requestorId")
    public String getRequestorId() {
        return requestorId;
    }

    /**
     * The id of the person who initiated the completion request
     * 
     * @param requestorId
     *     The requestorId
     */
    @JsonProperty("requestorId")
    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
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
        return new HashCodeBuilder().append(billingAccountNumber).append(callbackUrl).append(correlator).append(instanceName).append(orderNumber).append(orderVersion).append(productFamilyId).append(source).append(suppressRollback).append(responseValue).append(requestorId).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RequestInfo) == false) {
            return false;
        }
        RequestInfo rhs = ((RequestInfo) other);
        return new EqualsBuilder().append(billingAccountNumber, rhs.billingAccountNumber).append(callbackUrl, rhs.callbackUrl).append(correlator, rhs.correlator).append(instanceName, rhs.instanceName).append(orderNumber, rhs.orderNumber).append(orderVersion, rhs.orderVersion).append(productFamilyId, rhs.productFamilyId).append(source, rhs.source).append(suppressRollback, rhs.suppressRollback).append(responseValue, rhs.responseValue).append(requestorId, rhs.requestorId).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
